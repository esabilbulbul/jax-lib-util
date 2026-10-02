/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jaxesa.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import static jaxesa.redis.RedisLettuce.LETTUCE_MODE_SYNC;
import org.apache.commons.pool2.impl.GenericObjectPool;

/**
 *
 * @author Administrator
 */
public class ssoRedisLettuceCore {
    
    public int mode = LETTUCE_MODE_SYNC;//default
    public RedisClient client;
    public GenericObjectPool<StatefulRedisConnection<String, String>> pool;
    

}
