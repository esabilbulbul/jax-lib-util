/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.framework.misc;

import java.util.ArrayList;

/**
 *
 * @author esabil
 */
public final class frameworkMisc 
{
    public static String getParameterValue(String pName, ArrayList<HTTPReqParameter> paParams)
    {
        for (HTTPReqParameter paramN:paParams)
        {
            if (paramN.Name.equals(pName)==true)
            {
                return paramN.Value;
            }
        }
        
        return "";
    }

    public static String getCookieValue(String pName, String[] paCookies)
    {
        for (String paramN:paCookies)
        {
            if (paramN!=null)
            {
                String[] parts = paramN.split("=");

                if (parts[0].trim().equals(pName)==true)
                {
                    if (parts.length==1)
                        return "";

                    return parts[1];
                }
            }
        }

        return null;
    }
}
