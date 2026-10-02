/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.crypto;

import java.util.ArrayList;
import jaxesa.util.Util;

/**
 *
 * @author esabil
 */
public final class RSAMisc 
{
    static ArrayList<ssoSessionKeys> gSessionKeys = new ArrayList<ssoSessionKeys>();

    public static void addSessionKey(ssoSessionKeys poKeys)
    {
        gSessionKeys.add(poKeys);
    }
    
    public static int getNoOfKey(int pKeyLen)
    {
        int iCounter = 0;

        for(ssoSessionKeys keyN:gSessionKeys)
        {
            if (keyN.KeyLen==pKeyLen)
                iCounter++;
        }

        return iCounter;
    }

    public static ssoSessionKeys pickKey(int pKeyLen)
    {
        ssoSessionKeys sessionKey = new ssoSessionKeys();

        int iKeyNumber = getNoOfKey(pKeyLen);
        int iIndex = Util.Randomize.generateRandomNumber(0, iKeyNumber-1);
        int iCounter = 0;
        for (ssoSessionKeys keyN:gSessionKeys)
        {
            if (keyN.KeyLen==pKeyLen)
                iCounter++;

            if((iCounter==iIndex) || (iIndex==0))
                return keyN;//it should stop here
        }

        return sessionKey;//shouldnot be coming here
    }

    public static ssoSessionKeys getKey(String psKeyIndex)
    {
        for (ssoSessionKeys keyN:gSessionKeys)
        {
            if (keyN.KeyIndex.equals(psKeyIndex)==true)
                return keyN;
        }
        
        return null;
    }
}
