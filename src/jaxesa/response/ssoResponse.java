/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.response;

/**
 *
 * @author esabil
 */
public class ssoResponse 
{
    public int    code;
    public String text;
    
    public ssoResponse()
    {
        code = 0;
        text = "";
    }
    
    public ssoResponse(int pCode, String pText)
    {
        code = pCode;
        text = pText;
    }
}
