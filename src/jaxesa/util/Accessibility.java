/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.util;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Administrator
 */
public class Accessibility 
{    
    private AtomicInteger       aisem_flag;//0:false, 1:true
    private Semaphore           semaphore; //Entity/Table level lock
    //private boolean             sem_flag;  //false: free true:busy

    private int SEM_FLAG_UP   = 1;
    private int SEM_FLAG_DOWN = 0;
    
    public Accessibility()
    {
        aisem_flag = new AtomicInteger();
        aisem_flag.set(SEM_FLAG_DOWN);
        semaphore    = new Semaphore(1);
        //sem_flag     = false;
    }
    
    public void Unlock()
    {
        semaphore.release();
        
        aisem_flag.set(SEM_FLAG_DOWN);
        
        //sem_flag = false;
    }
    
    public boolean Lock() throws InterruptedException
    {
        try
        {
            semaphore.acquire();
            
            aisem_flag.set(SEM_FLAG_UP);
            //sem_flag = true;
            
            return true;
        }
        catch(InterruptedException e)
        {
            throw e;
        }
    }
    
    private boolean isSemFlag()
    {
        int ival = aisem_flag.get();
        if (ival==SEM_FLAG_DOWN)
            return false;//flag up
        else
            return true;//flag down
    }
    
    public boolean isAllowed(int pTimeoutinSeconds)
    {
        try
        {
            long    lTotWaitTime = 0;
            long    lTimeout_ms = pTimeoutinSeconds *1000; //wait max 30 seconds
            int     WaitCycleTime = 40;//miliseconds
            boolean sem_flag;
            
            //Alternative: You may one want to consider using AtomicInteger instead; get/set thread safe
            sem_flag = isSemFlag();
            if (sem_flag==false)
                return true;

            while(lTotWaitTime < lTimeout_ms)
            {
                sem_flag = isSemFlag();
                if (sem_flag==false)
                    return true;

                Thread.sleep(WaitCycleTime);

                lTotWaitTime += WaitCycleTime;
            }
            
            return false;
        }
        
        catch(InterruptedException e)
        {
            return false;
        }
    }
}
