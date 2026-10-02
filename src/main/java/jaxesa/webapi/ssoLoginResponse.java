/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.webapi;

/**
 *
 * @author esabil
 */
public class ssoLoginResponse 
{
    public boolean bSucceded = false;
    public boolean bTOUForce = false;//Terms Of Use 
    public boolean bPwdChange = false;
    public String  errCode = "";
    public String  UserId = "";
    public String  AccId = "";// user can have multiple accounts
    public String  profileName = "";
}
