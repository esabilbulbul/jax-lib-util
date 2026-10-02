/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.api.callback;

import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class ssoCallbackQueueItem 
{
    public String Id = "";//Callback Id
    public String UserId = "";
    public ArrayList<ssoCallbackParam> params = new ArrayList<ssoCallbackParam>();
    public String source = "";
    public String method = "";
}
