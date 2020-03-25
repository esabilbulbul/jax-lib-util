/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.signup;

/**
 *
 * @author esabil
 */
public class ssoSignup 
{
    public String IP       = "";
    public String UserType = "";
    public String Name     = "";
    public String LastName = "";
    public String PCountry = "";
    public String Phone    = "";
    public String Gender   = "";
    public String Birthday = "";
    public String Email    = "";
    public String Pwd      = "";
    public String GHash    = "";
    public String CountryCode = "";
    public String State        = "";
    public String RegionCode  = "";
    public String Lang = "";
    public String SysDateTime = "";
    public ssoSignupToken Token = new ssoSignupToken();//this will be calculated the process token generated (step 1. receive req, step 2. gen token and send email 3. activate)

}
