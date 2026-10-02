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
    public static String SIGNUP_QUEUE_NEW_REQUEST = "signup#requests";//activation?
    
    public static String SIGNUP_QUEUE_NEW_MESSAGE = "signup#messages";//email
    
    //public static String gQUEUE_NAME_CALLBACK = "ss.frmwrk.callback.queue";
    
    // FTP FILE STATS 
    //-------------------------------------------------------------------------
    public static String FILE_STAT_NEW          = "N";//NEW / RECEIVED
    public static String FILE_STAT_PROCESSING   = "P";
    public static String FILE_STAT_FAILED       = "F";
    public static String FILE_STAT_FAILED_VIRUS = "V";
    public static String FILE_STAT_SUCCEEDED    = "S";

    // FILE STAT UPDATE SOURCES 
    //-------------------------------------------------------------------------
    public static String FILE_STAT_UPD_SOURCE_EOD_IMPORTER = "EOD";
    public static String FILE_STAT_UPD_SOURCE_INV_IMPORTER = "INV";
    public static String FILE_STAT_UPD_SOURCE_IMG_IMPORTER = "IMG";

}


