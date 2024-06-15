/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.activeMQ;

/**
 *
 * @author Administrator
 */
public class ssoAMQObject 
{
    public String id;//Server Code
    public String filename;
    public String reference;
    //public String time_seperated;
    public String time_request_seperated;//raw time
    public String time_save_raw;//raw time
    public String code;
    public String data;
    
    public ssoAMQObject()
    {
        filename  = "";
        reference = "";
        time_request_seperated = "";
        time_save_raw = "";
        code = "";
        data = "";
    }
}
