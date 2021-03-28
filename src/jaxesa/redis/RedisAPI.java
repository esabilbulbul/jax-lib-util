/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.redis;

import java.time.Duration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 *
 * @author esabil
 * 
 * https://gist.github.com/JonCole/925630df72be1351b21440625ff2671f
 * 
 */
public final class RedisAPI
{
    static String gHost = "";
    static int    gPort = 0;
    static int    gConnNumber = 0;
    
    static JedisPool gJedisPool = new JedisPool();

    public static String connect(String psHost, int piPort, int pMaxConNumber)
    {
        gHost = psHost;
        gPort = piPort;
        gConnNumber = pMaxConNumber;

        boolean bInit = false;
        
        try
        {
            if (gPort==0)
                bInit = true;
            
            final JedisPoolConfig poolConfig = buildPoolConfig(pMaxConNumber);
            gJedisPool = new JedisPool(poolConfig, psHost, piPort);
            Jedis jedis = getConnection();

            jedis.ping();

            return "";
        }
        catch(Exception e)
        {
            return e.getMessage();
        }
    }

    static private JedisPoolConfig buildPoolConfig(int pMaxConNumber)
    {
        int iMaxConNumber = pMaxConNumber;
        int iMinIdleNum   = pMaxConNumber / 8;

        final JedisPoolConfig poolConfig = new JedisPoolConfig();

        poolConfig.setMaxTotal(iMaxConNumber);//128);
        poolConfig.setMaxIdle(iMaxConNumber);//128);
        poolConfig.setMinIdle(iMinIdleNum);//16);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
        poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
        poolConfig.setNumTestsPerEvictionRun(3);
        poolConfig.setBlockWhenExhausted(true);

        return poolConfig;
    }

    public static Jedis getConnection()
    {
        try
        {
            return gJedisPool.getResource();
        }
        catch(Exception e)
        {
            reconnect();
            
            return null;
        }
    }

    public static boolean reconnect()
    {
        try
        {
            final JedisPoolConfig poolConfig = buildPoolConfig(gConnNumber);
            gJedisPool = new JedisPool(poolConfig, gHost, gPort);

            // WARNING : THIS MUST BE CLOSED OFF OTHERWISE CREATES INIFITE LOOP 
            //Jedis jedis = getConnection();
            //jedis.ping();

            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    //---------------------------------------------------------------------------------
    // NUMBER FUNCTIONS 
    //---------------------------------------------------------------------------------
    public static class JNumber
    {
        public static long increase(Jedis jedis, String pKey,int pBy)
        {
            //
            return jedis.incrBy(pKey, pBy);
            
        }
        
        public static long decrease(Jedis jedis, String pKey, int pBy)
        {
            return jedis.decrBy(pKey, pBy);
            
        }
    }
    
    //---------------------------------------------------------------------------------
    // STRING FUNCTIONS 
    //---------------------------------------------------------------------------------
    public static class JString
    {
        public static String set(Jedis jedis, String pKey, String pVal, int piExpirySeconds)
        {
            return jedis.setex(pKey, piExpirySeconds, pVal);
        }

        public static String set(Jedis jedis, String pKey, String pVal)
        {
            return jedis.set(pKey, pVal);
        }

        public static String get(Jedis jedis, String pKey)
        {
            return jedis.get(pKey);
        }

        public static long remove(Jedis jedis, String... pKeys)
        {
            return jedis.del(pKeys);
            //return jedis.del(pKey);
        }
    }

    //---------------------------------------------------------------------------------
    // HASHES FUNCTIONS 
    // 
    // Recommendation: Prefer to use String (redis) with JSON field
    // that is simpler
    //---------------------------------------------------------------------------------
    public static class JHashes
    {
        public static long set(Jedis jedis, String psKey, String pFieldName, String pFieldVal)
        {
            return jedis.hset(psKey, pFieldName, pFieldVal);
        }

        public static long set(Jedis jedis, String psKey, String pFieldName, String pFieldVal, int piExpirySeconds)
        {
            long jRet = jedis.hset(psKey, pFieldName, pFieldVal);
            
            jedis.expire(psKey, piExpirySeconds);
            
            return jRet;
        }

        public static String getField(Jedis jedis, String psKey, String pFieldName)
        {
            return jedis.hget(psKey, pFieldName);
        }

        public static long remove(Jedis jedis, String... pKeys)
        {
            return jedis.del(pKeys);
        }
    }

    //---------------------------------------------------------------------------------
    // LIST FUNCTIONS 
    //
    // This can be used as QUEUE 
    //pListKey = Queue Name
    //---------------------------------------------------------------------------------
    public static class JLists
    {
        //Top = Left Bottom = Right
        //pListKey = Queue Name
        public static long push(Jedis jedis, String pListKey, String pListEl, boolean pbFromTop)
        {
            if (pbFromTop==true)
                return jedis.lpush(pListKey, pListEl);
            else
                return jedis.rpush(pListKey, pListEl);
        }

        //Top = Left Bottom = Right
        //pListKey = Queue Name
        public static String pop(Jedis jedis, String pListKey, boolean pbFromTop)
        {
            if (pbFromTop==true)
                return jedis.lpop(pListKey);
            else
                return jedis.rpop(pListKey);
        }
        
        public static long size(Jedis jedis, String pListKey)
        {
            return jedis.llen(pListKey);
        }
    }
    

}
