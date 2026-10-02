/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jaxesa.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.SetArgs;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.support.ConnectionPoolSupport;
import java.time.Duration;
import java.util.ArrayList;
import jaxesa.util.Util;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 *
 * @author Administrator
 */
public final class RedisLettuce 
{
    static String gHostURL = "";
    static int    gPort = 0;
    static int    gConnNumber = 0;
    
    static boolean gbInitialized = false;

    static int LETTUCE_MODE_SYNC     = 0;//DEFAULT
    static int LETTUCE_MODE_ASYNC    = 1;
    static int LETTUCE_MODE_REACTIVE = 2;

    private static RedisClient gRedisClient;
    public static GenericObjectPool<StatefulRedisConnection<String, String>> gLettucePool;//public for test

    static int gResponseTimeout = 2000;//2 seconds wait for the responsd to command
    static int gResourceTimeout = 2000;//2 seconds wait to get a resource from the pool
 
    public static boolean gbLettuceLogOn = false;//default not active
    public static ArrayList<String> gaLettuceLogs = new ArrayList<String>();

    public static void add2Log(String pName, String pMsg)
    {
        if(gbLettuceLogOn==true)
        {
            String sDateTime = Util.DateTime.GetDateTime_s();
            gaLettuceLogs.add(sDateTime + " - " + pName + " - " + pMsg);
        }
    }

    public static ArrayList<String> getLogs()
    {
        add2Log("", "------------");
                
        return gaLettuceLogs;
    }
    
    public static ssoRedisLettuceCore connectPrivate(String psHostURL, int piPort, int pMaxConNumber, int pResourceTimeout) throws Exception
    {
        add2Log("", "Connecting to Redis (Private)..");
                
        String sURL = psHostURL + ":" + piPort + "/0";

        ssoRedisLettuceCore lettuceCore = new ssoRedisLettuceCore();

        RedisClient lRedisClient;
        GenericObjectPool<StatefulRedisConnection<String, String>> lLettucePool;

        // 1️⃣ Create Redis client
        lRedisClient = RedisClient.create(sURL);

        GenericObjectPoolConfig<StatefulRedisConnection<String, String>> poolConfig = new GenericObjectPoolConfig<>();

        poolConfig = buildPoolConfig(pMaxConNumber);

        // 3️⃣ Create the pool
        lLettucePool = ConnectionPoolSupport.createGenericObjectPool(() -> gRedisClient.connect(StringCodec.UTF8), poolConfig);

        lettuceCore.client = lRedisClient;
        
        add2Log("", "ConnectedPrivate..");

        return lettuceCore;
    }

    // example URL "redis://localhost:6379/0" and 6379 = Port
    // pbGlobalSource = that will set the gRedisClient and gLettucePool
    // Client -> Pool -> Connection -> Commands
    public static String connect(String psHostURL, int piPort, int pMaxConNumber, int pResourceTimeout) throws Exception
    {
        add2Log("", "Connecting to Redis..");
        
        boolean bError = false;

        StatefulRedisConnection<String, String> connection = null;

        try
        {
            String sURL = psHostURL + ":" + piPort + "/0";

            gHostURL = psHostURL;
            gPort = piPort;
            gConnNumber = pMaxConNumber;

            if(pResourceTimeout!=-1)
                gResourceTimeout = pResourceTimeout;

            // 1️⃣ Create Redis client
            //gRedisClient = RedisClient.create(psHostURL);
            gRedisClient = RedisClient.create(sURL);

            GenericObjectPoolConfig<StatefulRedisConnection<String, String>> poolConfig = new GenericObjectPoolConfig<>();

            poolConfig = buildPoolConfig(pMaxConNumber);

            // 3️⃣ Create the pool
            if(gbInitialized==false)
                gLettucePool = ConnectionPoolSupport.createGenericObjectPool(() -> gRedisClient.connect(StringCodec.UTF8), poolConfig);

            //RedisCommands<String, String> commands = getConnection();

            gbInitialized = true;
            
            add2Log("", "Connected..");
        }
        catch(Exception e)
        {
            bError = true;
            
            e.printStackTrace();
            
            add2Log("", e.getMessage());
        }
        finally {
            if (connection != null) {
                try {

                    //gLettucePool.returnObject(connection); // return it to the pool 
                    release(connection);
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if(bError==true)
            return null;
        else
            return "";
    }

    public static RedisClient getRedisClient()
    {
        return gRedisClient;
    }

    static public GenericObjectPoolConfig<StatefulRedisConnection<String, String>> buildPoolConfig(int pMaxConNumber)
    {
        int iMaxConNumber = pMaxConNumber;
        int iMinIdleNum   = pMaxConNumber / 8;

        GenericObjectPoolConfig<StatefulRedisConnection<String, String>> poolConfig = new GenericObjectPoolConfig<>();

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
        poolConfig.setMaxWaitMillis(gResourceTimeout);// this is another timeout to capture a resource

        return poolConfig;
    }

    public static ssoRedisLettuce getConnection(String pConnectionName)
    {
        return getConnection(LETTUCE_MODE_SYNC, pConnectionName);//default sync
    }

    public static ssoRedisLettuce getConnectionAsync(String pConnectionName)
    {
        return getConnection(LETTUCE_MODE_ASYNC, pConnectionName);//default sync
    }

    public static ssoRedisLettuce getConnection(int pMode, String pConnectionName)
    {
        return getConnectionPrivate(gLettucePool, pMode, gbInitialized, pConnectionName);
    }

    //public static RedisCommands<String, String> getConnection(int pMode)
    public static ssoRedisLettuce getConnectionPrivate( GenericObjectPool<StatefulRedisConnection<String, String>>  pPool, 
                                                        int                                                         pMode, 
                                                        boolean                                                     pbInitialized, 
                                                        String                                                      pConnectionName)
    {
        add2Log(pConnectionName, "getConnectionPrivate");
        
        ssoRedisLettuce lettuce = new ssoRedisLettuce();
        
        lettuce.name = pConnectionName;
        lettuce.pool = pPool;// gLettucePool;

        StatefulRedisConnection<String, String> connection = null;

        try
        {
            if(pbInitialized==true)
            {
                int iIdleNum = pPool.getNumIdle();
                if(iIdleNum==0)
                    iIdleNum = iIdleNum;

                connection = pPool.borrowObject(); // borrow a fresh connection

                add2Log(pConnectionName, "Stats> Idle: " + iIdleNum + " Active: " + pPool.getNumActive() + " Waiters: " + pPool.getNumWaiters());

                lettuce.mode = pMode;
                lettuce.connection = connection;

                if (pMode==LETTUCE_MODE_REACTIVE)
                    lettuce.commands = connection.reactive();
                else if (pMode==LETTUCE_MODE_ASYNC)
                    lettuce.commands = connection.async();
                else
                    lettuce.commands = connection.sync();//default
                    

                return lettuce;
            }
            else
                return null;// If it is falling here that means the REDIS is not initialized (connection pool not started)
        }
        catch(Exception e)
        {
            add2Log("", "getConnectionPrivate -> Exception");

            //reconnect();//no need because lettuce auto reconnect by design
            if(lettuce!=null)
                lettuce.close();
                        
            return null;
        }
    }

    // close all the pool connections
    public static void close()
    {
        closePrivate(gRedisClient, gLettucePool);
        //gLettucePool.close();
        //gRedisClient.shutdown();
    }

    public static void closePrivate(RedisClient pRedisClient, GenericObjectPool<StatefulRedisConnection<String, String>> pPool)
    {
        if(pPool!=null)
            pPool.close();

        if(pRedisClient!=null)
            pRedisClient.shutdown();
    }

    // return connection to the pool
    public static void release(StatefulRedisConnection<String, String> pConnection)
    {
        releasePrivate(gLettucePool, pConnection);
        //gLettucePool.returnObject(pConnection); // return it to the pool 
    }

    public static void releasePrivate(GenericObjectPool<StatefulRedisConnection<String, String>> pPool, StatefulRedisConnection<String, String> pConnection)
    {
        pPool.returnObject(pConnection); // return it to the pool 
    }

    public static String getConnectionPoolStats()
    {
        return getConnectionPoolStatsPrivate(gLettucePool);
    }

    public static String getConnectionPoolStatsPrivate(GenericObjectPool<StatefulRedisConnection<String, String>> pPool)
    {
        int iIdleCon   = pPool.getNumIdle();
        int iActiveCon = pPool.getNumActive();
        int iWaitingCon= pPool.getNumWaiters();

        return "Idle: " + iIdleCon + " - Active: " + iActiveCon + " - Waiting: " + iWaitingCon;
    }
    //---------------------------------------------------------------------------------
    // STRING FUNCTIONS (KEY + VALUE Pair)
    //---------------------------------------------------------------------------------
    public static class JString
    {
        // sync mode
        public static String set(ssoRedisLettuce pLettuce, String pKey, String pVal, int piExpirySeconds)
        {
            // THIS IS ONLY FOR REDIS SYNC
            return ((RedisCommands<String, String>)pLettuce.commands).set(pKey, pVal, SetArgs.Builder.ex(piExpirySeconds));
        }

        // sync mode
        public static String set(ssoRedisLettuce pLettuce, String pKey, String pVal)
        {
            return ((RedisCommands<String, String>)pLettuce.commands).set(pKey, pVal);
            //return jedis.set(pKey, pVal);
        }

        // sync mode
        public static String get(ssoRedisLettuce pLettuce, String pKey)
        {
            return ((RedisCommands<String, String>)pLettuce.commands).get(pKey);
        }

        // sync mode
        public static long remove(ssoRedisLettuce pLettuce, String... pKeys)
        {
            return ((RedisCommands<String, String>)pLettuce.commands).del(pKeys);
            //return jedis.del(pKeys);
        }
    }

}

