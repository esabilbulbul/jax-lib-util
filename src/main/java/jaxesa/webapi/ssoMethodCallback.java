/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.webapi;

import java.util.ArrayList;
import jaxesa.api.callback.ssoCallbackParam;

/**
 *
 * @author Administrator
 */
//This is the object passed to the callback function
public class ssoMethodCallback {
    
    public String method = "";// class.package.method name
    public ArrayList<ssoCallbackParam>  paParams = new ArrayList<ssoCallbackParam>();
}
