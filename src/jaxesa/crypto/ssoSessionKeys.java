/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.crypto;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 *
 * @author Administrator
 */
public class ssoSessionKeys 
{
    public String sPublicKey;//P
    public String sPrivateKey;//S
    public int    KeyLen;
    public String KeyIndex;
    
    public PublicKey  kPublicKey;
    public PrivateKey kPrivateKey;
}
