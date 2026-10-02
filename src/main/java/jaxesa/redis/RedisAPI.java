/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.ScriptOutputType;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
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

    static boolean gbInitialized = false;

    static int gResponseTimeout = 2000;//2 seconds wait for the responsd to command
    static int gResourceTimeout = 2000;//2 seconds wait to get a resource from the pool

    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // IMPORTANT
    // If you didn't call connect() explicitly jedis at first getsource forinstance
    // will connect to jedis with default settings localhost / 6379
    // but if the connection settings different than this wont work
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    // example URL "redis://localhost:6379/0" and 6379 = Port
    public static String connect(String psHostURL, int piPort, int pMaxConNumber, int pResourceTimeout) throws Exception
    {
        return RedisLettuce.connect(psHostURL, piPort, pMaxConNumber, pResourceTimeout);
    }

    public static RedisClient getClient()
    {
        return RedisLettuce.getRedisClient();
    }

/*
    public static String connect(String psHost, int piPort, int pMaxConNumber, int pResourceTimeout) throws Exception
    {
        gHost = psHost;
        gPort = piPort;
        gConnNumber = pMaxConNumber;

        if(pResourceTimeout!=-1)
            gResourceTimeout = pResourceTimeout;

        boolean bInit = false;
        gbInitialized = true;

        try
        {
            if (gPort==0)
                bInit = true;

            final JedisPoolConfig poolConfig = buildPoolConfig(pMaxConNumber);

            gJedisPool = new JedisPool(poolConfig, psHost, piPort, gResponseTimeout);// this is for response from the command timeout
            Jedis jedis = getConnection();

            jedis.ping();

            jedis.close();//return to the pool

            return "";
        }
        catch(Exception e)
        {
            //return e.getMessage();
            throw e;
        }
    }
*/
    
    public static void close()
    {
        RedisLettuce.close();
    }
    
    // close the pool connections
    /*
    public static void close()
    {
        gJedisPool.close();
    }
    */

    static private GenericObjectPoolConfig<StatefulRedisConnection<String, String>> buildPoolConfig(int pMaxConNumber)
    {
        return RedisLettuce.buildPoolConfig(pMaxConNumber);
    }
    
/*
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
        poolConfig.setMaxWaitMillis(gResourceTimeout);// this is another timeout to capture a resource

        return poolConfig;
    }
*/

    public static String getConnectionPoolStats()
    {
        return RedisLettuce.getConnectionPoolStats();
    }    
/*
    public static String getConnectionPoolStats()
    {
        int iIdleCon   = gJedisPool.getNumIdle();
        int iActiveCon = gJedisPool.getNumActive();
        int iWaitingCon= gJedisPool.getNumWaiters();

        return "Idle: " + iIdleCon + " - Active: " + iActiveCon + " - Waiting: " + iWaitingCon;
    }
*/
    
    public static ssoRedisLettuce getConnection(String pConnectionName)
    {
        return RedisLettuce.getConnection(pConnectionName);
    }
    
    public static ssoRedisLettuce getConnectionAsync(String pConnectionName)
    {
        return RedisLettuce.getConnectionAsync(pConnectionName);
    }
/*
    public static Jedis getConnection()
    {
        try
        {
            if(gbInitialized==true)
            {
                int iIdleNum = gJedisPool.getNumIdle();
                if(iIdleNum==0)
                    iIdleNum = iIdleNum;
                    
                return gJedisPool.getResource();
            }
            else
                return null;// If it is falling here that means the REDIS is not initialized (connection pool not started)
        }
        catch(Exception e)
        {
            reconnect();
            
            return null;
        }
    }
*/
    
    public static boolean reconnect()
    {
        // NO NEED WITH LETTUCE AS IT MANAGES IT AUTO
        
        /*
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
        */

        return true;
    }

    //---------------------------------------------------------------------------------
    // NUMBER FUNCTIONS (KEY + VALUE Pair)
    //---------------------------------------------------------------------------------
    public static class JNumber
    {
        public static long increase(ssoRedisLettuce lettuce, String pKey,int pBy)
        {
            //return jedis.incrBy(pKey, pBy);
            return lettuce.incrBy(pKey, pBy);
        }
        
        public static long decrease(ssoRedisLettuce lettuce, String pKey, int pBy)
        {
            //return jedis.decrBy(pKey, pBy);
            return lettuce.decrBy(pKey, pBy);
        }
    }
    
    public static class Lua
    {
        /*
            EXAMPLE

            private static final String SCRIPT =
            "local tempGroup1 = ARGV[1] " +
            "local tempGroup2 = ARGV[2] " +
            "local tempGroup3 = ARGV[3] " +

            "redis.call('SUNIONSTORE', tempGroup1, KEYS[1], KEYS[2]) " +
            "redis.call('SUNIONSTORE', tempGroup2, KEYS[3], KEYS[4]) " +
            "redis.call('SUNIONSTORE', tempGroup3, KEYS[5], KEYS[6]) " +

            "local result = redis.call('SINTER', tempGroup1, tempGroup2, tempGroup3) " + //execute line

            "redis.call('DEL', tempGroup1, tempGroup2, tempGroup3) " + // del line

            "return result";

            // EXECUTE prototype
            public static Set<String> findMatches(
                    RedisCommands<String, String> sync,
                    String k1, String k2,   // group 1 source of keys
                    String k3, String k4,   // group 2 source of keys
                    String k5, String k6    // group 3 source of keys
            ) {
                String suffix = UUID.randomUUID().toString();

                String[] keys = { k1, k2, k3, k4, k5, k6 };
                String[] argv = {
                    "tmp:g1:" + suffix,
                    "tmp:g2:" + suffix,
                    "tmp:g3:" + suffix
                };

                List<String> result = sync.eval(SCRIPT, ScriptOutputType.MULTI, keys, argv);

                return new HashSet<>(result);
        */
        
        // Union multiple keys then match them 
        // Same groups will be uninized (combined) different groups will be cross matched
        public static ssoUnionMatchScript prepareUnionMatchScript(ArrayList<ssoRedisUnionKey> paKeys)
        {
            ssoUnionMatchScript pckg = new ssoUnionMatchScript();
            ArrayList<String> aKeysInOrder = new ArrayList<String>();
            ArrayList<String> aArgsInOrder = new ArrayList<String>();

            String sScript      = "";
            String sExecuteLine = "";//"local result = redis.call('SINTER', tempGroup1, tempGroup2, tempGroup3) " + //execute line
            String sDelLine     = "";

            // Calc group number of keys 
            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            List<String> distinctGroups = paKeys.stream()
                                            .map(k -> k.group)
                                            .distinct()
                                            .collect(Collectors.toList());

            // Create script-variables 
            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            String suffix = UUID.randomUUID().toString();
            for(int i=0;i<distinctGroups.size();i++)
            {
                int nGroupNo = i+1;
                String sGroupName = distinctGroups.get(i);
                
                sScript += "local tempGroup" + sGroupName + "= ARGV[" + nGroupNo + "]" + " ";

                aArgsInOrder.add("tmp:grp" + sGroupName + ":" + suffix);
            }

            // Create script-UNION-execute methods 
            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            int nKeyNo = 0;
            for(int i=0;i<distinctGroups.size();i++)
            {
                int nGroupNo = i+1;
                String sGroupName = distinctGroups.get(i);

                //sScript += "redis.call('SUNIONSTORE', tempGroup" + sGroup + ", KEYS[1], KEYS[2]) ";
                sScript += "redis.call('SUNIONSTORE', tempGroup" + sGroupName;
                for(int j=0;j<paKeys.size();j++)
                {
                    //int nKeyNo = j+1;
                    if(paKeys.get(j).group.equals(sGroupName)==true)
                    {
                        nKeyNo++;

                        sScript += "," + "KEYS[" + nKeyNo + "] ";

                        aKeysInOrder.add(paKeys.get(j).key);
                    }
                }

                sScript += ") ";

            }

            // Create script-MATCH-execute line
            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            if(distinctGroups.size()>1)
                sExecuteLine = "local result = redis.call('SINTER', ";// + sGroupName;
            else
                sExecuteLine = "local result = redis.call('SMEMBERS', ";
            
            sDelLine = "redis.call('DEL', ";//tempGroup" + sGroupName;
            for(int i=0;i<distinctGroups.size();i++)
            {
                String sGroupName = distinctGroups.get(i);
                if(i!=0)
                {
                    sExecuteLine += ",";
                    sDelLine += ",";
                }
                
                int nGroupNo = i+1;
                
                sExecuteLine += "tempGroup" + sGroupName;
                sDelLine += "tempGroup" + sGroupName;
            }
            sExecuteLine += ") ";
            sDelLine +=  ") ";

            sScript += " ";
            
            sScript += sExecuteLine + " ";//for sinter if only one group no cross matching all union 
            sScript += sDelLine + " ";
            sScript += "return result";

            pckg.script = sScript;
            pckg.args   = aArgsInOrder.toArray(new String[0]);
            pckg.keys   = aKeysInOrder.toArray(new String[0]);

            return pckg;
        }

        public static Set<String> execute(ssoRedisLettuce lettuce, String pScript, String[] pKeys, String[] pArgs)
        {
            List<String> result = ((RedisCommands<String, String>)lettuce.connection.sync()).eval(pScript, ScriptOutputType.MULTI, pKeys, pArgs);
            
            return new HashSet<>(result);
        }
    }
    
    //---------------------------------------------------------------------------------
    // STRING FUNCTIONS (KEY + VALUE Pair)
    //---------------------------------------------------------------------------------
    public static class Sinter
    {
        //public static String set(Jedis jedis, String pKey, String pVal, int piExpirySeconds)
        public static RedisFuture<Long> saddex(ssoRedisLettuce lettuce, String pKey, int piExpirySeconds, String... pVal)
        {
            //return jedis.setex(pKey, piExpirySeconds, pVal);
            return lettuce.saddex(pKey, piExpirySeconds, pVal);
        }

        public static RedisFuture<Long> sadd(ssoRedisLettuce lettuce, String pKey, String... pVal)
        {
            //return jedis.set(pKey, pVal);
            return lettuce.sadd(pKey, pVal);
        }

        public static RedisFuture<Set<String>> get(ssoRedisLettuce lettuce, String... pKey)
        {
            //return jedis.get(pKey);
            return lettuce.sread(pKey);
        }

        public static long remove(ssoRedisLettuce lettuce, String... pKeys)
        {
            //return jedis.del(pKeys);
            return lettuce.remove(pKeys);
        }
    }
    
    //---------------------------------------------------------------------------------
    // STRING FUNCTIONS (KEY + VALUE Pair)
    //---------------------------------------------------------------------------------
    public static class JString
    {
        //public static String set(Jedis jedis, String pKey, String pVal, int piExpirySeconds)
        public static String set(ssoRedisLettuce lettuce, String pKey, String pVal, int piExpirySeconds)
        {
            //return jedis.setex(pKey, piExpirySeconds, pVal);
            return lettuce.setex(pKey, pVal, piExpirySeconds);
        }

        public static String set(ssoRedisLettuce lettuce, String pKey, String pVal)
        {
            //return jedis.set(pKey, pVal);
            return lettuce.set(pKey, pVal);
        }

        public static String setSync(ssoRedisLettuce lettuce, String pKey, String pVal)
        {
            //return jedis.set(pKey, pVal);
            return lettuce.setSync(pKey, pVal);
        }

        public static String get(ssoRedisLettuce lettuce, String pKey)
        {
            //return jedis.get(pKey);
            return lettuce.get(pKey);
        }

        public static String getSync(ssoRedisLettuce lettuce, String pKey)
        {
            //return jedis.get(pKey);
            return lettuce.getSync(pKey);
        }

        public static long remove(ssoRedisLettuce lettuce, String... pKeys)
        {
            //return jedis.del(pKeys);
            return lettuce.remove(pKeys);
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
        public static boolean set(ssoRedisLettuce lettuce, String psKey, String pFieldName, String pFieldVal)
        {
            //return jedis.hset(psKey, pFieldName, pFieldVal);
            return lettuce.hset(psKey, pFieldName, pFieldVal);
        }

        public static boolean set(ssoRedisLettuce lettuce, String psKey, String pFieldName, String pFieldVal, int piExpirySeconds)
        {
            boolean jRet = lettuce.hset(psKey, pFieldName, pFieldVal);
            
            lettuce.expire(psKey, piExpirySeconds);
            
            return jRet;
        }

        public static String getField(ssoRedisLettuce lettuce, String psKey, String pFieldName)
        {
            //return jedis.hget(psKey, pFieldName);
            return lettuce.hget(psKey, pFieldName);
        }

        public static long remove(ssoRedisLettuce lettuce, String... pKeys)
        {
            //return jedis.del(pKeys);
            return lettuce.del(pKeys);
        }
    }

    //---------------------------------------------------------------------------------
    // LIST FUNCTIONS (queue FIFO)
    //
    // This can be used as QUEUE 
    // pListKey = Queue Name
    //---------------------------------------------------------------------------------
    public static class JLists
    {
        //Top = Left Bottom = Right
        //pListKey = Queue Name
        public static long push(ssoRedisLettuce lettuce, String pListKey, String pListEl, boolean pbFromTop)
        {
            if (pbFromTop==true)
                return lettuce.lpush(pListKey, pListEl);
            else
                return lettuce.rpush(pListKey, pListEl);
        }

        //Top = Left Bottom = Right
        //pListKey = Queue Name
        public static String pop(ssoRedisLettuce lettuce, String pListKey, boolean pbFromTop)
        {
            if (pbFromTop==true)
                return lettuce.lpop(pListKey);
            else
                return lettuce.rpop(pListKey);
        }

        public static long size(ssoRedisLettuce lettuce, String pListKey)
        {
            return lettuce.llen(pListKey);
        }
        
        // This deletes the queue
        public static long remove(ssoRedisLettuce lettuce, String... pKeys)//DELETES QUEUE
        {
            return lettuce.del(pKeys);
        }

        public static long removeEl(ssoRedisLettuce lettuce, String pListKey, int pCount, String pKey)//DELETES QUEUE
        {
            return lettuce.lrem(pListKey, pCount, pKey);
        }
    }


}


