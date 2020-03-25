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
public class ssoSignupToken 
{
    public long   Id;
    public String email;
    public String dtime;
    public String Token;
    
    public ssoSignupToken()
    {
        Id = 0;
        email   = "";
        Token   = "";
    }
}
