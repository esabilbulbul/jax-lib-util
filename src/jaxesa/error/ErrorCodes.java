/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.error;

/**
 *
 * @author Administrator
 */
public final class ErrorCodes
{
    //static ErrorCode Attrib = new ErrorCode();
    public static void report(Object exc)
    {
        String RuntimeStack = "";
        
        //Write to Q
        if (exc!=null)
        {
            if (exc.getClass()!=ErrorCode.class)//Exception class or some other classs
            {
                //Attrib.RuntimeStack = e.toString();//Read its exception message
                RuntimeStack = exc.toString();
            }
            else if (exc.getClass()==String.class)
            {
                RuntimeStack = exc.toString();
            }
        }

        //Write to Q //TBD
    }
    
    public static void report()
    {
        report("");
    }
    
    public enum Framework
    {
        OK                              (0, "OK"),
        CLASSPATH_NOT_FOUND             (1, "Classpath not found"),
        EXCEPTION_GETMETHODS            (2, "Exception get method"),
        EXCEPTION_GETCLASSPATH          (3, "Exception GetClassPath"),
        EXCEPTION_SERVICE_EXECUTE       (4, "Exception Service Execution"),
        EXCEPTION                       (5, "Exception"),
        EXCEPTION_INIT                  (6, "Exception - Framework Init"),
        EXCEPTION_INIT_DB_CONS          (7, "Exception - Initialize DB Cons"),
        EXCEPTION_PREQ_OUT_OF_REP       (8, "Exception - Out of Repository - too short URL call"),
        EXCEPTION_PREQ_URI_SHORT        (9, "Exception - too short URI"),
        EXCEPTION_PREQ_SERVICE_NOT_EXEC (10,"Exception - Service not executable: Check parameters"),
        EXCEPTION_PREQ_SERVICE_NOT_FOUND(11,"Exception - method not found"),
        EXCEPTION_PREQ_DB_CON_ERROR     (12,"Exception - db connection error"),
        EXCEPTION_PREQ_DB_CON_GONE      (13,"Exception - db connection no longer exist"),
        EXCEPTION_SES_TIMEOUT           (14,"Exception - session timeout"),
        EXCEPTION_DESTROY_CONNECTION    (15,"Connections are forced to closing"),
        EXCEPTION_TASK_FETCH_ERROR      (16,"Exception - Task fetch error"),
        EXCEPTION_TASK_EXC_ERROR        (17,"Exception - Task execution error"),
        EXCEPTION_TASK_HISTORY_EXC_ERROR(18,"Exception - Task History execution error"),
       EXCEPTION_TASK_MANAGER_CYCLE_FAIL(19,"Exception - Task Manager Processes cycle failed")
        ;

        ErrorCode Attrib = new ErrorCode();
        
        Framework(int pId, String pMsg)
        {
            Attrib.GroupCode       = "FRM-";
            Attrib.Id              = pId;
            Attrib.Description     = pMsg;
            //Attrib.RuntimeStack    = "";//Only report phase
        }
        
        public int value()
        {
            return Attrib.Id;
        }
        
        public ErrorCode exception()
        {
            return Attrib;
        }
    }
    
    public enum Persistence 
    {        
        OK                                     (0, "OK"),
        EXCEPTION                              (1, "Exception - Entity find()"),
        ENTITY_NOT_FOUND                       (2, "Entity Not Found"),
        TYPE_NOT_SUPPORTED                     (3, "Type Not Supported"),
        ENTITY_LOAD_ERROR                      (4, "Entity Load Error"),
        MUST_COLUMNS_NOT_SATISFIED             (5, "Must columns requirement is not satisfied"),
        CREDENTIALS_NOT_SET                    (6, "Credentials are not set"),
        EXCEPTION_GETMETHODS                   (7, "Exception Get Mehtods"),
        SQL_OBJ_ALREADY_EXIST                  (8, "Object Already Exist"),
        EXCEPTION_ENTITY_PERSIST               (9, "Exception - Entity Persist Error"),
        EXCEPTION_GENERATE_UID                 (10,"Exception - Generate UID"),
        EXCEPTION_CACHE_CHANGE_LOG             (11,"Exception - Cache Change Log"),
        EXCEPTION_DB_STAT_CHECK                (12,"Exception - db stat check: isValid"),
        EXCEPTION_ENTITY_REMOVE                (13,"Exception - Entity remove() Error"),
        EXCEPTION_ENTITY_REMOVE_IAE            (14,"Exception - Entity remove() Error: Illegal Access"),
        EXCEPTION_ENTITY_FLD_VAL_GET           (15,"Exception - Entity Field Value Get"),
        EXCEPTION_ENTITY_REMOVE_2              (16,"Exception - Entity remove() Error 2"),
        EXCEPTION_ENTITY_FIND_CLA_NOT_FOUND    (17,"Exception - Entity find() class not found"),
        EXCEPTION_ENTITY_FIND_INSTANTATION     (18,"Exception - Entity find() instantation"),
        EXCEPTION_ENTITY_FIND_ILGL_ACCESS      (19,"Exception - Entity find() illegal access"),
        EXCEPTION_ENTITY_MERGE                 (20,"Exception - Entity merge() "),
        EXCEPTION_ENTITY_CRT_CHANGELOG         (21,"Exception - Create Change Log"),
        EXCEPTION_ENTITY_ROWLASTUPDATE_N       (22,"Exception - GetRowLastUpdate_N_ColstoChange"),
        EXCEPTION_ENTITY_CREATE_SP_QUERY       (23,"Exception - Create SP Query"),
        EXCEPTION_ENTITY_CREATE_TYPE_QUERY     (24,"Exception - Create Type Query"),
        EXCEPTION_ENTITY_CREATE_RAW_QUERY      (25,"Exception - Create Raw Query"),
        EXCEPTION_ENTITY_CREATE_RAW_UPDATE     (26,"Exception - Create Raw Update"),
        EXCEPTION_ENTITY_CREATE_CLOSE          (27,"Exception - Create Entity Man close()"),
        EXCEPTION_EMF_BREAK_ENTITY             (28,"Exception - Break Entity List"),
        EXCEPTION_EMF_GENERATE_DML             (29,"Exception - Generate DML"),
        EXCEPTION_EMF_GET_USER_STMT            (30,"Exception - Get User Statements"),
        EXCEPTION_EMF_BREAK_ENTITY_2           (31,"Exception - Break Entity Class"),
        EXCEPTION_PRS_INITIALIZE               (32,"Exception - Persistence Initialize"),
        EXCEPTION_PRS_LOAD_ENTITY              (33,"Exception - Load Entity"),
        EXCEPTION_PRS_INITIALIZE_2             (34,"Exception - Persistence Initialize 2"),
        EXCEPTION_SYS_DBSESSION_BUSY           (35,"Exception - Persistence Initialize 2")
        ;
        
        ErrorCode Attrib = new ErrorCode();
        
        Persistence(int pId, String pMsg)
        {
            Attrib.GroupCode       = "PRS-";
            Attrib.Id              = pId;
            Attrib.Description     = pMsg;
            //Attrib.RuntimeStack    = "";//Only report phase
        }
        
        public int value()
        {
            return Attrib.Id;
        }
        
        public ErrorCode exception()
        {
            return Attrib;
        }
        
    }
    
    public enum SetupManager
    {        
        OK                                     (0, "OK"),
        EXCEPTION                              (1, "Exception"),
        ERROR_INSERT_ENTITY_DATA               (2, "Insert New Data Error"),
        ERROR_CLEAR_ENTITY_TABLE               (3, "Entity Table Clear Error"),
        ERROR_GENERATE_DB_OBJECT               (4, "Entity Table Clear Error"),
        ERROR_SEQUENCE_TABLE_MISSING           (5, "Sequence object not found in back up file"),
        ERROR_SEQUENCE_SP_MISSING              (6, "Sequence Store Procedure not found in back up file"),
        ERROR_DELETE_SQL_OBJECT                (7, "Delete SQL Object Error"),
        ERROR_UNKNOWN_SQL_OBJECT_TYPE          (8, "Unknown SQL Object Type"),
        ERROR_DB_CREATE                        (9, "DB create error"),
        EXCEPTION_INSTALL_BACKUP               (10,"Exception - Install Backup"),
        EXCEPTION_GEN_ENTITY_DATA              (11,"Exception - Generate Entity Data"),
        EXCEPTION_GEN_USER_DEF_OBJECT          (12,"Exception - Generate User Defined Object"),
        EXCEPTION_CREATE_ENTITIES              (13,"Exception - Create Entities"),
        EXCEPTION_CREATE_SEQUENCE              (14,"Exception - Create Sequence")
        ;
        
        ErrorCode Attrib = new ErrorCode();
        
        SetupManager(int pId, String pMsg)
        {
            Attrib.GroupCode       = "STP-";
            Attrib.Id              = pId;
            Attrib.Description     = pMsg;
            //Attrib.RuntimeStack    = "";//Only report phase
        }
        
        public int value()
        {
            return Attrib.Id;
        }
        
        public ErrorCode exception()
        {
            return Attrib;
        }
        
    }
    
    public enum Util
    {
        EXCEPTION                              (1, "Exception");
        
        ErrorCode Attrib = new ErrorCode();
        
        Util(int pId, String pMsg)
        {
            Attrib.GroupCode       = "UTL-";
            Attrib.Id              = pId;
            Attrib.Description     = pMsg;
            //Attrib.RuntimeStack    = "";//Only report phase
        }
        
        public int value()
        {
            return Attrib.Id;
        }
        
        public ErrorCode exception()
        {
            return Attrib;
        }
    
    }
    
    public enum Cache
    {
        EXCEPTION                              (1, "Exception"),
        EXCEPTION_LOAD_ENTITY_DATA             (2, "Exception - Load Entity Data"),
        EXCEPTION_FETCH_ENTITY_DATA            (3, "Exception - Fetch Entity Data"),
        EXCEPTION_DISMISS_CHANGE               (4, "Exception - Dismiss the change"),
        EXCEPTION_UPDATE_CHANGE                (5, "Exception - Update the change"),
        EXCEPTION_THR_MAINCACHE                (6, "Exception - Thread Main Cache Data"),
        EXCEPTION_CACHE_UPDATER_PROC           (7, "Exception - Cache Update Process"),
        EXCEPTION_CACHE_UPDATER_START          (8, "Exception - Cache Update Start");
        
        ErrorCode Attrib = new ErrorCode();
        
        Cache(int pId, String pMsg)
        {
            Attrib.GroupCode       = "CCH-";
            Attrib.Id              = pId;
            Attrib.Description     = pMsg;
            //Attrib.RuntimeStack    = "";//Only report phase
        }
        
        public int value()
        {
            return Attrib.Id;
        }
        
        public ErrorCode exception()
        {
            return Attrib;
        }
    
    }
    
    public enum Locks
    {
        EXCEPTION                              (1, "Exception"),
        EXCEPTION_LOCK_MAIN_THR                (2, "Exception - LockMaintenance Process"),
        EXCEPTION_SHOW_LOCK_STATS              (3, "Exception - ShowLockStats Process"),
        EXCEPTION_ADD_NEW_ROW_LOCK             (4, "Exception - AddNewRowLock "),
        EXCEPTION_ADD_REMOVE_ROW_LOCK          (5, "Exception - RemoveRowLock "),
        EXCEPTION_LOCK_TIMEOUT                 (6, "Exception - Lock Timeout"),
        EXCEPTION_SWITCH_CLUSTER               (7, "Exception - Switch Cluster Error")
        ;
        
        ErrorCode Attrib = new ErrorCode();
        
        Locks(int pId, String pMsg)
        {
            Attrib.GroupCode       = "CCH-";
            Attrib.Id              = pId;
            Attrib.Description     = pMsg;
            //Attrib.RuntimeStack    = "";//Only report phase
        }
        
        public int value()
        {
            return Attrib.Id;
        }
        
        public ErrorCode exception()
        {
            return Attrib;
        }
    
    }
    
    public enum SQL
    {
        EXCEPTION                              (1, "Exception");
        
        ErrorCode Attrib = new ErrorCode();
        
        SQL(int pId, String pMsg)
        {
            Attrib.GroupCode       = "UTL-";
            Attrib.Id              = pId;
            Attrib.Description     = pMsg;
            //Attrib.RuntimeStack    = "";//Only report phase
        }
        
        public int value()
        {
            return Attrib.Id;
        }
        
        public ErrorCode exception()
        {
            return Attrib;
        }
    
    }

}

