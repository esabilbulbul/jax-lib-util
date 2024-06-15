/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxsys.worker;

/**
 *
 * @author Administrator
 */
public class ssoWorkerConfig 
{
    // Config Params (w direct definitions) (MUST PARAMS)
    //--------------------------------------------------------------------------
    public String ServletClass = "";//main base lib core
    public String ServletName  = "";
    public String WorkerEngine = "";
    public String ServerRootFolder = "";
    public String ServerBaseDir = "";

    // Config Params (w init-params)(MUST PARAMS)
    //--------------------------------------------------------------------------
    public String ServerCode   = "";
    public String LogServerURL = "";
    public String LogQueueName = "";
    public String LogFolder    = "";
    public String LogFileName  = "";//prefix
    public String AppFilesRoot = "";//prefix
    public String ContactUsEmail = "";//prefix
    public String ContactUsEmailPwd = "";//prefix
    
}
