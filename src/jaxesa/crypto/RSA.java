/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.crypto;

import java.io.StringWriter;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import jaxesa.util.Util;
/**
 * Encrypt / Decrypt 
 * https://gist.github.com/dmydlarz/32c58f537bb7e0ab9ebf
 * 
 * Generate Key
 * https://eureka.ykyuen.info/2010/04/24/java-creating-an-rsa-key-pair-in-java/
 * https://gist.github.com/liudong/3993726
 * 
 * Save to/from
 * https://www.devglan.com/java8/rsa-encryption-decryption-java
 * 
 * @author Administrator
 */
public final class RSA 
{
    public static int RSA_KEY_SIZE = 1024;

    //must be multiple of 1024
    public static void setKeyPairLength(int pLen)
    {
        RSA_KEY_SIZE = pLen;
    }

    public static ssoRSAKeyPair generateKeyPair()
    {
        KeyPairGenerator kpg;
        ssoRSAKeyPair newKeyPair = new ssoRSAKeyPair();

        try
        {
            // Create a 1024 bit RSA private key
            kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(RSA_KEY_SIZE);
            KeyPair kp = kpg.genKeyPair();
            //Key publicKey = kp.getPublic();
            //Key privateKey = kp.getPrivate();
            //PublicKey  publicKey  = kp.getPublic();
            //PrivateKey privateKey = kp.getPrivate();

            byte [] baPublicKey  =  kp.getPublic().getEncoded();
            byte [] baPrivateKey =  kp.getPrivate().getEncoded();

            newKeyPair.sPublicKey  = Base64.getEncoder().encodeToString(baPublicKey);
            newKeyPair.sPrivateKey = Base64.getEncoder().encodeToString(baPrivateKey);

            return newKeyPair;
            
            /*
            KeyFactory fact = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec pub = (RSAPublicKeySpec) fact.getKeySpec(publicKey, RSAPublicKeySpec.class);
            RSAPrivateKeySpec priv = (RSAPrivateKeySpec) fact.getKeySpec(privateKey, RSAPrivateKeySpec.class);
            */

            //KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            /*
            X509EncodedKeySpec keySpec_publickey = new X509EncodedKeySpec(Base64.getDecoder().decode(sPublicKey.getBytes()));
            PublicKey publicKey_regen            = keyFactory.generatePublic(keySpec_publickey);

            PKCS8EncodedKeySpec keySpec_privkey  = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(sPrivateKey.getBytes()));
            PrivateKey privateKey_regen          = keyFactory.generatePrivate(keySpec_privkey);
            */
            /*
            // encrypt the message
            byte [] encrypted = encrypt(privateKey, "This is a secret message");
            //byte [] encrypted = encrypt(privateKey_regen, "This is a secret message");     
            System.out.println(new String(encrypted));  // <<encrypted message>>

            // decrypt the message
            byte[] secret = decrypt(publicKey, encrypted);
            //byte[] secret = decrypt(publicKey_regen, encrypted);
            System.out.println(new String(secret));     // This is a secret message

            return newKeyPair;
            */
        }
        catch(Exception e)
        {
            return null;
        }
    }

    public static PublicKey decodePublicKey(String psPublicKey)
    {
        try
        {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            
            X509EncodedKeySpec keySpec_publickey = new X509EncodedKeySpec(Base64.getDecoder().decode(psPublicKey.getBytes()));
            
            PublicKey KeyP            = keyFactory.generatePublic(keySpec_publickey);

            return KeyP;
        }
        catch(Exception e)
        {
            return null;
        }
    }

    public static PrivateKey decodePrivateKey(String psPrivateKey)
    {
        try
        {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            
            PKCS8EncodedKeySpec keySpec_privkey  = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(psPrivateKey.getBytes()));
            
            PrivateKey KeyS          = keyFactory.generatePrivate(keySpec_privkey);
            
            return KeyS;
        }
        catch(Exception e)
        {
            return null;
        }
    }

    //IMPORTANT:
    // Data length max 117 for 1024 bit
    public static byte[] encrypt(PrivateKey privateKey, String message) throws Exception 
    {
        Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);  

        return cipher.doFinal(message.getBytes());  
    }

    //IMPORTANT:
    // Data length max 117 for 1024 bit
    public static byte[] decrypt(PublicKey publicKey, byte [] encrypted) throws Exception
    {
        Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        return cipher.doFinal(encrypted);
    }
}


