/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jaxesa.redis;

import java.time.Duration;
import static jaxesa.redis.RedisAPI.reconnect;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 *
 * @author Administrator
 */
public class RedisPoolGeneric 
{

    String gHost = "";
    int    gPortNo = -1;
    int    gMaxConNumber = 1;
    int    gTimeout = 1000;//default

    //JedisPool gSSEJedisPool = new JedisPool();
    public RedisPoolGeneric()
    {
        
    }
    
    public RedisPoolGeneric(String pHost, int pPortNo,  int pMaxConNumber, int pTimeout)
    {
        gHost = pHost;
        gPortNo = pPortNo;
        gMaxConNumber = pMaxConNumber;
        gTimeout = pTimeout;
    }

    public void config(String pHost, int pPortNo, int pMaxConNumber, int pTimeout)
    {
        gHost = pHost;
        gPortNo = pPortNo;
        gMaxConNumber = pMaxConNumber;
        gTimeout = pTimeout;
    }

    public JedisPool connect()
    {
        JedisPool jdPool = new JedisPool();
        
        final JedisPoolConfig poolConfig = buildPoolConfig(gMaxConNumber);

        jdPool = new JedisPool(poolConfig, gHost, gPortNo, gTimeout);
        return jdPool;
    }

    @Deprecated
    public Jedis getNewConnection(JedisPool pjdPool)
    {
        try
        {
            return pjdPool.getResource();
        }
        catch(Exception e)
        {
            reconnect();
            
            return null;
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
}
