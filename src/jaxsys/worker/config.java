/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxsys.worker;

import java.util.ArrayList;
import jaxsys.worker.ssoWorkers;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import jaxsys.worker.ssoWorkers;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Administrator
 */
public final class config 
{
    public static ssoWorkerConfig gConfig = new ssoWorkerConfig();

    // Optional Params
    public static ArrayList<ssoWorkers> gWorkers = new ArrayList<ssoWorkers>();

    static String PRM_CONFIG_NAME_SERVER_CODE          = "servercode";
    static String PRM_CONFIG_NAME_LOG_SERVER_ROOT      = "log_root";//log root folder
    static String PRM_CONFIG_NAME_LOG_SERVER_BASE_DIR  = "log_base";//log root folder
    static String PRM_CONFIG_NAME_LOG_SERVER_URL       = "logserverurl";
    static String PRM_CONFIG_NAME_LOG_QUEUE_NAME       = "logqueuename";
    static String PRM_CONFIG_NAME_LOG_FOLDER           = "logfolder";
    static String PRM_CONFIG_NAME_LOG_FILE_NAME        = "filename";
    static String PRM_CONFIG_NAME_APP_FILES_ROOT       = "app_files_path";
    static String PRM_CONFIG_NAME_CONTACT_US_EMAIL     = "contact_us_email";
    static String PRM_CONFIG_NAME_CONTACT_US_EMAIL_PWD = "contact_us_email_pwd";

    // Direct Definitive Params
    //--------------------------------------------------------------------------
    static String PRM_CONFIG_NAME_SERVLET_NAME   = "servlet-name";
    static String PRM_CONFIG_NAME_SERVLET_CLASS  = "servlet-class";
    static String PRM_CONFIG_NAME_WORKER_ENGINE  = "listener-class";

    static String PRM_NAME_PREFIX_WORKER = "worker_";

    // App.xml file = psConfigFile
    public static boolean readConfig(String psConfigFile)
    {
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(psConfigFile);
            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            boolean rc = false;

            rc = readServletParams(doc);
            if (rc==true)
            {
                // Read Engine (listener) params
                readWorkerEngineParams(doc);
            }

            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    public static boolean readWorkerEngineParams(Document doc)
    {
        NodeList workerEngines = doc.getElementsByTagName("listener");

        //Node    SNode    = workerEngines.item(0);
        for (int i=0; i<workerEngines.getLength(); i++)
        {
            Node    SNode    = workerEngines.item(i);
            Element SElement = (Element)SNode;

            NodeList EngineList = SElement.getElementsByTagName(PRM_CONFIG_NAME_WORKER_ENGINE);
            for (int j=0; j<EngineList.getLength(); j++)
            {
                Node     INode    = EngineList.item(j);
                Element  IElement = (Element)INode;

                //gPrm_WorkerEngine = IElement.getTextContent();
                gConfig.WorkerEngine = IElement.getTextContent();
            }

        }

        return true;
    }
    
    public static void addWorker(String pWorkerClass, String pCycleMiliseconds)
    {
        ssoWorkers newWorker = new ssoWorkers();
        
        newWorker.className = pWorkerClass;
        newWorker.occurence = pCycleMiliseconds;
        
        gWorkers.add(newWorker);
    }

    public static boolean readServletParams(Document doc)
    {
        try
        {
            //------------------------------------------------------------------
            //                     Init Params & Worker Threads
            //------------------------------------------------------------------
            //NodeList initParams = doc.getElementsByTagName("init-param");

            NodeList servletParams = doc.getElementsByTagName("servlet");
            //Object a = doc.getFirstChild();
            for (int i=0; i<servletParams.getLength(); i++)
            {
                Node    SNode    = servletParams.item(i);
                Element SElement = (Element)SNode;

                //--------------------------------------------------------------
                // Read Identity Params
                //--------------------------------------------------------------
                NodeList servletName  = SElement.getElementsByTagName(PRM_CONFIG_NAME_SERVLET_NAME);
                if (servletName.getLength()==1)
                {
                    String sServletName    = servletName.item(0).getTextContent();

                    parseServletParams(PRM_CONFIG_NAME_SERVLET_NAME, sServletName);
                }

                NodeList servletClass = SElement.getElementsByTagName(PRM_CONFIG_NAME_SERVLET_CLASS);
                if (servletClass.getLength()==1)
                {
                    String sServletClass    = servletClass.item(0).getTextContent();
                    
                    parseServletParams(PRM_CONFIG_NAME_SERVLET_CLASS, sServletClass);
                }
                
                //--------------------------------------------------------------
                //
                // There are two types of init-param
                // 1. config params
                // 2. worker params
                //
                //--------------------------------------------------------------
                NodeList initParams = SElement.getElementsByTagName("init-param");
                for (int j=0; j<initParams.getLength(); j++)
                {
                    ssoWorkers newWorker = new ssoWorkers();
                    boolean bWorker      = false;
                    boolean rc           = false;

                    Node     INode    = initParams.item(j);
                    Element  IElement = (Element)INode;

                    // Worker Name
                    //----------------------------------------------------------
                    NodeList prmName = IElement.getElementsByTagName("param-name");
                    if (prmName.getLength()==1)//if param-name not found skip we don't know what this entry is
                    {
                        String sPrmName = prmName.item(0).getTextContent();

                        rc = isParamWorker(sPrmName);
                        if (rc == true)
                        {
                            //Param Type : Worker
                            //--------------------------------------------------
                            bWorker = true;
                            newWorker.name = sPrmName.trim();
                        }
                        else
                        {
                            //Param Type : Config 
                            //--------------------------------------------------
                            bWorker = false;
                        }

                        // Worker Class
                        //----------------------------------------------------------
                        NodeList prmClass = IElement.getElementsByTagName("param-value");
                        if (prmClass.getLength()==1)
                        {
                            String sPrmValBlock = prmClass.item(0).getTextContent();

                            if (bWorker==true)
                            {
                                String[] aPrmValParts = sPrmValBlock.split(",");

                                String sWorkerClass     = aPrmValParts[0].trim();
                                String sWorkerOccurence = aPrmValParts[1].trim();

                                newWorker.className = sWorkerClass;
                                newWorker.occurence = sWorkerOccurence;
                            }
                            else
                            {
                                // Param Type : Config 
                                //--------------------------------------------------
                                parseServletParams(sPrmName, sPrmValBlock);
                            }
                        }
                    }

                    if (bWorker==true)
                    {
                        // WE NO LONGER SUPPORT MULTIPLE WORKER INSTANCE. ONE WORKER AT A TIME 
                        // AND IT IS PASSED AS ARGUMENT AT MAIN CALL
                        gWorkers.add(newWorker);//open for now
                    }

                }//end of init-param for

            }

            return true;
            
        }
        catch(Exception e)
        {
            return false;
        }
    }

    public static void parseServletParams(String psPrmName, String psPrmVal)
    {
        if (psPrmName.trim().toLowerCase().equals(PRM_CONFIG_NAME_SERVER_CODE)==true)
        {
            //gPrm_ServerCode  = psPrmVal;
            gConfig.ServerCode = psPrmVal;
        }
        else if (psPrmName.trim().toLowerCase().equals(PRM_CONFIG_NAME_LOG_SERVER_URL)==true)
        {
            //gPrm_LogServerURL = psPrmVal;
            gConfig.LogServerURL = psPrmVal;
        }
        else if (psPrmName.trim().toLowerCase().equals(PRM_CONFIG_NAME_LOG_QUEUE_NAME)==true)
        {
            //gPrm_LogQueueName = psPrmVal;
            gConfig.LogQueueName = psPrmVal;
        }
        else if (psPrmName.trim().toLowerCase().equals(PRM_CONFIG_NAME_LOG_FOLDER)==true)
        {
            //gPrm_LogFolder = psPrmVal;
            gConfig.LogFolder = psPrmVal;
        }
        else if (psPrmName.trim().toLowerCase().equals(PRM_CONFIG_NAME_LOG_FILE_NAME)==true)
        {
            //gPrm_LogFileName = psPrmVal;
            gConfig.LogFileName = psPrmVal;
        }
        else if (psPrmName.trim().toLowerCase().equals(PRM_CONFIG_NAME_SERVLET_NAME)==true)
        {
            //gPrm_ServletName = psPrmVal;
            gConfig.ServletName = psPrmVal;
        }
        else if (psPrmName.trim().toLowerCase().equals(PRM_CONFIG_NAME_SERVLET_CLASS)==true)
        {
            //gPrm_ServletClass = psPrmVal;
            gConfig.ServletClass = psPrmVal;
        }
        else if (psPrmName.trim().toLowerCase().equals(PRM_CONFIG_NAME_LOG_SERVER_ROOT)==true)
        {
            //gPrm_ServerRootFolder = psPrmVal;
            gConfig.ServerRootFolder = psPrmVal;
        }
        else if (psPrmName.trim().toLowerCase().equals(PRM_CONFIG_NAME_LOG_SERVER_BASE_DIR)==true)
        {
            //gPrm_ServerBaseDir = psPrmVal;
            gConfig.ServerBaseDir = psPrmVal;
        }
        else if (psPrmName.trim().toLowerCase().equals(PRM_CONFIG_NAME_APP_FILES_ROOT)==true)
        {
            //gPrm_AppFilesRoot = psPrmVal;
            gConfig.AppFilesRoot = psPrmVal;
        }
        else if (psPrmName.trim().toLowerCase().equals(PRM_CONFIG_NAME_CONTACT_US_EMAIL)==true)
        {
            //core.gPrm_ContactUsEmail = psPrmVal;
            gConfig.ContactUsEmail = psPrmVal;
        }
        else if (psPrmName.trim().toLowerCase().equals(PRM_CONFIG_NAME_CONTACT_US_EMAIL_PWD)==true)
        {
            //core.gPrm_ContactUsEmailPwd = psPrmVal;
            gConfig.ContactUsEmailPwd = psPrmVal;
        }
    }

    public static boolean isParamWorker(String psPrmName)
    {
        int index = psPrmName.trim().toLowerCase().indexOf(PRM_NAME_PREFIX_WORKER);
    
        if (index==0)
        {
            return true;
        }

        return false;

    }

    public static boolean checkMustParams()
    {
        
        //if (gPrm_ServletClass.trim().length()==0)
        if (gConfig.ServletClass.trim().length()==0)
            return false;

        //if (gPrm_ServletName.trim().length()==0)
        if (gConfig.ServletName.trim().length()==0)
            return false;

        //if (gPrm_WorkerEngine.trim().length()==0)
        if (gConfig.WorkerEngine.trim().length()==0)
            return false;

        //if (gPrm_ServerCode.trim().length()==0)
        if (gConfig.ServerCode.trim().length()==0)
            return false;
        
        //if (gPrm_LogServerURL.trim().length()==0)
        if (gConfig.LogServerURL.trim().length()==0)
            return false;
        
        //if (gPrm_LogQueueName.trim().length()==0)
        if (gConfig.LogQueueName.trim().length()==0)
            return false;
        
        //if (gPrm_LogFolder.trim().length()==0)
        if (gConfig.LogFolder.trim().length()==0)
            return false;

        //if (gPrm_LogFileName.trim().length()==0)
        if (gConfig.LogFileName.trim().length()==0)
            return false;

        return true;
   }
}
