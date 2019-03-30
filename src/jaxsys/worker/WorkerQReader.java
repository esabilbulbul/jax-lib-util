/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxsys.worker;

import jaxesa.activeMQ.MQ;

/**
 *
 * @author Administrator
 */
public final class WorkerQReader 
{
    public static String readFromQueue(MQ gQueue)
    {
        String sMsg = "";
        boolean bTest = true;
        int iCounter = 0;
        try
        {
            while(bTest==true)
            {
                sMsg = gQueue.pop(100);//timeout = 100s

                if (sMsg!=null)
                {
                    if (sMsg.equals(MQ.TESTMSG)==true)
                    {
                        bTest = true;//burn test messages as possible (max cycle 100)
                        //return 0;
                    }
                    else
                    {
                        return sMsg;//Long.parseLong(sMsg);
                    }
                }
                
                iCounter++;
                
                if (iCounter>=100)
                    break;
            }
            
            return "0";
        }
        catch(Exception e)
        {
            //LogManager.SysLog(-1, "Worker: '(ss-worker-content-checker)' read from queue failed" + e.getMessage());
            gQueue.reset();
            return "-1";
        }
    }
}
