/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.crypto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Administrator
 */
public final class SHA256 
{
    public static String calculate(String psData)
    {
        try
        {
            //String text = "esabil";
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(psData.getBytes(StandardCharsets.UTF_8));

            return DatatypeConverter.printHexBinary(hash);
        }
        catch(Exception e)
        {
            return "";
        }
    }
}
