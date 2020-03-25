/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.defs;

/**
 *
 * @author esabil
 */
public final class ShipShuk
{
    public static String ACTIVATION_TOKEN_KEY_PREFIX   = "actv-tk-";

    // to list all the items queued up 
    // on redis client
    // lrange <listname> <startindex> <stopindex>
    // lrange <listname> 0 10
    public static String SIGNUP_QUEUE_NEW_REQUEST = "signup#requests";
}


