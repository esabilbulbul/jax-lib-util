/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.signup;

import java.math.BigInteger;

/**
 *
 * @author esabil
 */
public class ssoSignupToken 
{
    public BigInteger   Id;
    public String email;
    public String dtime;
    public String Token;
    
    public ssoSignupToken()
    {
        Id = BigInteger.ZERO;
        email   = "";
        Token   = "";
    }
}
