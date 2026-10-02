/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.password;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import jaxesa.util.Util;

/**
 *
 * @author Administrator
 */
public class ssPasswords 
{
    public static String generatePwdHashSha256(String psPwd)
    {
        return Util.crypto.sha256.calculateSHA256(psPwd);
    }
    
    // This will be no longer used to store password hash algorithm
    // Current Use of Password Hash Algorithm = 
    public static String generatePwdHashArgon2( String pEmail, //RFU
                                                String pRegDate,//RFU - YYYYMMDDHHmmSS000, 
                                                String pPwdClean)
    {
        return Util.crypto.argon2.generate(pPwdClean);

        /*
        try
        {
            //String sPwdNSaltData = pEmail.trim().substring(0, 20) + pRegDate.trim() + pPwdClean.trim(); // Currently Argon2 manages salting data itself. Therefore these won't be needed
            String sPwdNSaltData = pPwdClean;

            // Create an Argon2 instance
            Argon2 argon2 = Argon2Factory.create();

            // Password to be hashed
            String password = sPwdNSaltData;//"yourPassword123";

            // Argon2 parameters
            int iterations = 3;
            int memory = 65536;
            int parallelism = 1;

            try 
            {
                // Hash the password
                String hash = argon2.hash(iterations, memory, parallelism, password);
                System.out.println("Hashed password: " + hash);

                // Verifying the password
                // boolean matches = argon2.verify(hash, password);
                // System.out.println("Password verification result: " + matches);

                return hash;
            }
            finally 
            {
                argon2.wipeArray(password.toCharArray());
            }
        }
        catch(Exception e)
        {
            String s = e.getMessage();

            return "";
        }
        */
    }

    public static boolean verifyPwdHash(String pPwdHash, String pPwdClean)
    {
        // Create an Argon2 instance
        Argon2 argon2 = Argon2Factory.create();

        // Verifying the password
        boolean matches = argon2.verify(pPwdHash, pPwdClean);
        //System.out.println("Password verification result: " + matches);

        return matches;
    }
}
