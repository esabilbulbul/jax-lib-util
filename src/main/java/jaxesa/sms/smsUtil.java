/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jaxesa.sms;

import java.math.BigInteger;
import java.util.HexFormat;
import jaxesa.crypto.HMAC;

/**
 *
 * @author Administrator
 */
public final class smsUtil
{

    private static String gsSMSDefaultKeyHex = "42756C62756C6C657231393532402E21";
    private static byte[] gbyteSMSDefaultKey = HexFormat.of().parseHex(gsSMSDefaultKeyHex);

    // HMAC Algorithm 
    public static String generateSMSSignature(String pUserId, String pNumbers)
    {
        String sData = pUserId + "-" + pNumbers;
        return HMAC.calculateHex(gbyteSMSDefaultKey, sData);
    }

    public static boolean verifySMSSignature(String pData, String pSignature)
    {
        return HMAC.verifyHex(gbyteSMSDefaultKey, pData, pSignature);
    }

 
}   

