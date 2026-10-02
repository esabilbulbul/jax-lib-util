/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jaxesa.redis;

import io.lettuce.core.RedisFuture;
import io.lettuce.core.SetArgs;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import java.time.Duration;
import java.util.Set;
import static jaxesa.redis.RedisLettuce.LETTUCE_MODE_SYNC;
import org.apache.commons.pool2.impl.GenericObjectPool;

/**
 *`
 * @author Administrator
 */
public class ssoRedisLettuce extends ssoRedisLettuceCore {

    public String name = "";
    public StatefulRedisConnection<String, String> connection = null;//DONT MAKE IT STATIC FIELD
    public Object commands;//RedisCommands<String, String> or RedisAsyncCommands<String, String>   //DONT MAKE IT STATIC FIELD

    public void ping()
    {
        // NO NEED FOR PING WITH LETTUCE IT IS AUTO
    }

    // returns the con to the pool
    public void close()
    {
        try
        {
            pool.returnObject(connection);// return it to the pool 
            
            RedisLettuce.add2Log(name, "Connection Returned. Stats> " + "Idle: " + pool.getNumIdle() + " Active: " + pool.getNumActive() + " Waiters: " + pool.getNumWaiters());
        }
        catch(Exception e)
        {
            String s = e.getMessage();
            
            RedisLettuce.add2Log(name, "Returning Connection Failure" + s);
        }
    }

    public String set(String pKey, String pVal)
    {
        return ((RedisCommands<String, String>)commands).set(pKey, pVal);
    }
    
    public String setSync(String pKey, String pVal)
    {
        return ((RedisCommands<String, String>)connection.sync()).set(pKey, pVal);
    }

    public String setex(String pKey, String pVal, int piExpirySeconds)
    {
        return ((RedisCommands<String, String>)commands).set(pKey, pVal, SetArgs.Builder.ex(piExpirySeconds));
    }

    public String get(String pKey)
    {
        return ((RedisCommands<String, String>)commands).get(pKey);
    }

    public String getSync(String pKey)
    {
        return ((RedisCommands<String, String>)connection.sync()).get(pKey);
    }

    public RedisFuture<Long> sadd(String pKey, String... pVal)
    {
        return ((RedisAsyncCommands<String, String>)commands).sadd(pKey, pVal);
    }

    public RedisFuture<Long> saddex(String pKey, int piExpirySeconds, String... pVal)
    {
        RedisFuture<Long> lRet =  ((RedisAsyncCommands<String, String>)commands).sadd(pKey, pVal);

        ((RedisAsyncCommands<String, String>)commands).expire(pKey, piExpirySeconds);

        return lRet;
    }

    public RedisFuture<Set<String>> sread(String... pKeys)
    {
        RedisFuture<Set<String>> items = ((RedisAsyncCommands<String, String>)commands).smembers("acc:42:GENDER:MEN");
        
        return items;
    }
    
    
    public long remove(String... pKeys)
    {
        return del(pKeys);
    }
    
    public long del(String... pKeys)
    {
        return ((RedisCommands<String, String>)commands).del(pKeys);
    }
    
    public long incrBy(String pKey,int pBy)
    {
        return ((RedisCommands<String, String>)commands).incrby(pKey, pBy);
    }

    public long decrBy(String pKey,int pBy)
    {
        return ((RedisCommands<String, String>)commands).decrby(pKey, pBy);
    }
    
    public boolean hset(String psKey, String pFieldName, String pFieldVal)
    {
        return ((RedisCommands<String, String>)commands).hset(psKey, pFieldName, pFieldVal);
    }
    
    public boolean hset(String psKey, String pFieldName, String pFieldVal, int piExpirySeconds)
    {
        boolean jRet = ((RedisCommands<String, String>)commands).hset(psKey, pFieldName, pFieldVal);
            
        ((RedisCommands<String, String>)commands).expire(psKey, piExpirySeconds);

        return jRet;
    }
    
    public String hget(String psKey, String pFieldName)
    {
        return ((RedisCommands<String, String>)commands).hget(psKey, pFieldName);
    }
    
    public boolean expire(String pKey, int piExpirySeconds)
    {
        return ((RedisCommands<String, String>)commands).expire(pKey, piExpirySeconds);
    }

    public long lpush(String pListKey, String pListEl)
    {
        return ((RedisCommands<String, String>)commands).lpush(pListKey, pListEl);
    }

    public long rpush(String pListKey, String pListEl)
    {
        return ((RedisCommands<String, String>)commands).rpush(pListKey, pListEl);
    }

    public String lpop(String pListKey)
    {
        return ((RedisCommands<String, String>)commands).lpop(pListKey);
    }

    public String rpop(String pListKey)
    {
        return ((RedisCommands<String, String>)commands).rpop(pListKey);
    }

    public long llen(String pListKey)
    {
        return ((RedisCommands<String, String>)commands).llen(pListKey);
    }

    public long lrem(String pListKey, int pCount, String pKey)
    {
        return ((RedisCommands<String, String>)commands).lrem(pListKey, pCount, pKey);
    }

    public void publish(String pChannel, String pResult)
    {
        ((RedisCommands<String, String>)commands).publish(pChannel, pResult);
    }
}
