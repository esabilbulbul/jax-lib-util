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
    public static String SESSION_TOKEN_KEY_PREFIX = "ssn-tk-";
            
    public static String ACTIVATION_TOKEN_KEY_PREFIX   = "actv-tk-";//if you change this REMEMBER to change JSP page as well 

    // to list all the items queued up 
    // on redis client
    // lrange <listname> <startindex> <stopindex>
    // lrange <listname> 0 10
    // QUEUES-NAMES
    public static String SIGNUP_QUEUE_NEW_REQUEST = "signup#requests";
    
    public static String SIGNUP_QUEUE_NEW_MESSAGE = "signup#messages";
    
    //public static String gQUEUE_NAME_CALLBACK = "ss.frmwrk.callback.queue";
}


