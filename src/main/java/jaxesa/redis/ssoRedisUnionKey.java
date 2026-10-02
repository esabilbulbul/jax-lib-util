/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jaxesa.redis;

/**
 *
 * @author Administrator
 */
public class ssoRedisUnionKey {
    
    public String group = "";
    public String key = "";//redis key
    
    public ssoRedisUnionKey(String pGroup, String pKey)
    {
        group = pGroup;
        key   = pKey;
    }
}
