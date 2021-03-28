/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.persistence.cache;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import jaxesa.persistence.annotations.CacheLevels;
import jaxesa.persistence.misc.NamedQueryTypes;
import jaxesa.util.Util;
import redis.clients.jedis.Jedis;

/**
 *
 * @author Administrator
 */
public final class Caching 
{
    public static AtomicInteger gIsLocalMemoryAvailable = new AtomicInteger(1);//0 = Not Free, 1= Free

    public static ArrayList<ssoCacheStats> gCacheStats = new ArrayList<ssoCacheStats>();

    public static ArrayList<CacheLocalMemoryItem> gLocalMemory = new ArrayList<CacheLocalMemoryItem>();

    public static final NamedQueryTypes CACHE_TYPE_ENTITY             = NamedQueryTypes.NONE;//Default for entities
    public static final NamedQueryTypes CACHE_TYPE_NAMED_QUERY        = NamedQueryTypes.NAMED_QUERY;
    /*
    public static final NamedQueryTypes CACHE_TYPE_NAMED_NATIVE_QUERY = NamedQueryTypes.NAMED_NATIVE_QUERY;//
    public static final NamedQueryTypes CACHE_TYPE_STORED_PROCEDURE   = NamedQueryTypes.STORED_PROCEDURE;
    */

    public static void updateCacheStat(Class<?> pClass, long pTimeStamp)
    {
        ssoCacheStats stat = new ssoCacheStats();

        stat = getCacheStat(pClass);
        if (stat==null)
        {
            ssoCacheStats newStat = new ssoCacheStats();
            newStat.cls = pClass;
            newStat.timeStamp = pTimeStamp;
            gCacheStats.add(newStat);
        }
        else
        {
            stat.timeStamp = pTimeStamp;
        }

    }

    public static ssoCacheStats getCacheStat(Class<?> pClass)
    {
        for(ssoCacheStats statN:gCacheStats)
        {
            if(statN.cls==pClass)
                return statN;
        }
        
        return null;
    }
    
    //memory db key (redis)
    // pQueryName = only for namedQueries
    // pGroupKey = only if wanted grouped caching i.e. caching per account Id results 
    public static String generateCacheKey(NamedQueryTypes       pCacheType, 
                                          String                pEntityName, 
                                          String                pQueryName, 
                                          ArrayList<Object>     pGroupKeys )
    {
        String sMemKey = "";

        sMemKey = pEntityName;

        // SUFFIX 1
        if (pCacheType==CACHE_TYPE_NAMED_QUERY)
        {                
            sMemKey += "." + "NMQ";
        }
        /*
        else if (pCacheType==CACHE_TYPE_NAMED_NATIVE_QUERY)
        {
            sMemKey += "." + "NM_NTVQ";
        }
        else if (pCacheType==CACHE_TYPE_STORED_PROCEDURE)
        {
            sMemKey += "." + "SP";
        }*/
        
        // SUFFIX 2
        if (pQueryName.trim().length()>0)
        {
            sMemKey += "." + pQueryName;
        }

        // SUFFIX 3 (Split Group Keys)
        //
        //---------------------------------------------------------------------
        if(pGroupKeys!=null)
        {
            for(Object keyN: pGroupKeys)
            {
                if (keyN.toString().trim().length()>0)
                    sMemKey += "." + keyN.toString();
            }
        }

        /*
        if (pGroupKey.trim().length()>0)
        {
            sMemKey += "." + pGroupKey;
        }
        */

        return sMemKey;
    }

    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //                              FLUSH MEMORY
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public static void flush(CacheLevels pMemoryLevel, Jedis   pJedis, String[] psKeys)
    {
        long iDelNum = 0;

        if (pMemoryLevel==CacheLevels.LEVEL_2)
        {
            iDelNum = Util.Redis.JString.remove(pJedis, psKeys);
        }
        else
        {
            removeMemoryL1Items(psKeys);
        }
    }

    synchronized static void removeMemoryL1Items(String[] psKeys)
    {
        
        // splice 
        for(String keyN: psKeys)
        {
            for(CacheLocalMemoryItem memN:gLocalMemory)
            {
                if (memN.key.equals(keyN)==true)
                {
                    gLocalMemory.remove(memN);
                    break;//1st loop
                }
            }
        }
    }

    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //                              READ MEMORY
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public static String readMemory(CacheLevels pMemoryLevel, Jedis   pJedis, String psTableKey)
    {
        String sRows = "";

        if (pMemoryLevel==CacheLevels.LEVEL_2)
        {
            sRows = Util.Redis.JString.get(pJedis, psTableKey);
        }
        else
        {
            sRows = getLocalMemoryItem(psTableKey);
        }

        return sRows;
    }

    static String getLocalMemoryItem(String psKey)
    {
        ArrayList<CacheLocalMemoryItem> MemItems = new ArrayList<CacheLocalMemoryItem>();
        //String sMemoryItem = "";
        
        MemItems = gLocalMemory;
        
        for (CacheLocalMemoryItem itemN:MemItems)
        {
            if(itemN.key.trim().equals(psKey.trim())==true)
            {
                return itemN.data;
            }
        }

        return null;
    }

    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //                              SAVE 2 MEMORY
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public static void save2Memory( CacheLevels pCacheLevel,
                                    Jedis       pJedis,
                                    String      psTableKey,
                                    String      psTableData,
                                    int         pMaxDuration) throws Exception
    {
        if (pCacheLevel==CacheLevels.LEVEL_1)
            save2LocalMemory(psTableKey, psTableData, pMaxDuration);
        else
            save2MemoryDB(pJedis, psTableKey, psTableData, pMaxDuration);//default memory db / redis
    }

    synchronized static void save2LocalMemory(  String psTableKey, 
                                                String psTableData, 
                                                int pMaxDuration) throws Exception
    {
        CacheLocalMemoryItem newItem = new CacheLocalMemoryItem();
        newItem.key  = psTableKey;
        newItem.data = psTableData;

        gLocalMemory.add(newItem);

    }

    static void save2MemoryDB(  Jedis   pJedis,
                                String psTableKey, 
                                String psTableData, 
                                int pMaxDuration) throws Exception
    {
        try
        {
            Util.Redis.JString.set(pJedis, psTableKey, psTableData, pMaxDuration);
        }
        catch(Exception e)
        {
            throw e;
        }
    }

}
