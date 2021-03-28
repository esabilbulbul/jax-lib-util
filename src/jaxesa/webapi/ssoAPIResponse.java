/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.webapi;

import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class ssoAPIResponse
{
    public String Id;
    public String Response;
    public String ResponseMsg;//optional
    public String Content;
    public String ClientSessionId;
    public boolean AuthenticationFailed=false;
    public String  callbackId;//tHIS IS SERVER SIDE USE ONLY. This will trigger the thread that will be called after the API call. Otherwise, don't use. 
    //public String ut; //token user state (token)
    
    public ArrayList<ssoCookie> cookies = new ArrayList<ssoCookie>();
}


