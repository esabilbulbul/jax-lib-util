/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jaxesa.crypto;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.HexFormat;

/**
 *
 * @author Administrator
 */
public final class HMAC 
{
    private static final String ALGORITHM = "HmacSHA256";

    private HMAC() {}
    

    /** Computes the raw HMAC-SHA256 of the message. */
    public static byte[] calculate(byte[] key, byte[] message) 
    {

        try
        {
            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(new SecretKeySpec(key, ALGORITHM));
            return mac.doFinal(message);
        }
        catch (GeneralSecurityException e)
        {
            throw new IllegalStateException("HMAC calculation failed", e);
        }
    }

    /** Convenience: HMAC as a lowercase hex string. */
    public static String calculateHex(byte[] key, String message) 
    {
        return HexFormat.of().formatHex(calculate(key, message.getBytes(StandardCharsets.UTF_8)));
    }

    /** Verifies a received hex signature in constant time. */
    //public static boolean verifyHex(byte[] key, String message, String receivedHex)
    public static boolean verifyHex(byte[] key, String message, String signature)
    {
        byte[] expected = calculate(key, message.getBytes(StandardCharsets.UTF_8));
        byte[] received;
        try {
            received = HexFormat.of().parseHex(signature);
        } catch (IllegalArgumentException e) {
            return false; // malformed signature
        }

        return MessageDigest.isEqual(expected, received);
    }

}
