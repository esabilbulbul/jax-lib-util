/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.queue;

import java.util.ArrayList;
import jaxesa.util.Accessibility;
import jaxesa.util.Util;

/**
 *
 * @author Administrator
 * 
 * 
 * This is not ActiveMQ This is jaxesa internal message queue structure
 * 
 * Calculation over Switch between CLUSTERs
 * 
 * Perception: 500 txn / second
 * In each 300K message, in other words 10 min per percepted calculation, 
 * will trigger the Cluster Switch Process
 * 
 * Write Operation
 *  Lock access to Q
 *  Add the new data
 *  Update the stat
 *  Unlock the access
 * 
 * Read Operation
 *  Read the last stat if change is up
 *  if so, read the data from Q
 *  update last stat last read
 * 
 */
public class MQueue
{
    Accessibility               accessW2Q = new Accessibility();;
    Accessibility               accessR2Q = new Accessibility();;
    
    String                      gFileName;
    int                         gActiveClusterIndex=1;//default
    int                         MAX_CLUSTER_SIZE = 3;//300000;
    int                         Q_WAIT_TIME=30;//Seconds
    
//    int                         gLastIndexReadonCluster1=0;
//    int                         gLastIndexReadonCluster2=0;
    
    ArrayList<MQObj>            CLUSTER1 = new ArrayList<MQObj>();
    ArrayList<MQObj>            CLUSTER2 = new ArrayList<MQObj>();

    // Stats
    int                         RESET_FOR_AC_RECNO_LAST_READ=-1;
    
    int                         gStat_AC_RecordNo=0;
    int                         gStat_AC_RecordNo_LastRead=RESET_FOR_AC_RECNO_LAST_READ;
    
    public void init(String pFileName)
    {
        gFileName = pFileName;
    }
    
    //First IN, First OUT
    public MQObj read() throws Exception
    {
        try
        {
            MQObj MQData = new MQObj();
            int   iStartIndex = 0;
            int   iEndIndex   = 0;

            if (accessR2Q.isAllowed(Q_WAIT_TIME)==true)//30s
            {
                if (gStat_AC_RecordNo==0)
                    return null;
                
                if (gStat_AC_RecordNo==(gStat_AC_RecordNo_LastRead+1))
                    return null;//No new data in Q
                
                int Index  = gStat_AC_RecordNo_LastRead+1;
                
                //Check not out of range
                if (gActiveClusterIndex==1)
                {
                    if (Index>=CLUSTER1.size())
                        return null;
                }
                else
                {
                    if (Index>=CLUSTER2.size())
                        return null;
                }
                
                accessR2Q.Lock();

                    //for (int i=iStartIndex;i<iEndIndex;i++)
                    if (gActiveClusterIndex==1)
                    {                        
                        MQData = CLUSTER1.get(Index);
                        CLUSTER1.get(Index).Status = false;//Inactive or Read
                    }
                    else
                    {
                        MQData = CLUSTER2.get(Index);
                        CLUSTER2.get(Index).Status = false;//Inactive or Read
                    }

                    //gStat_AC_RecordNo_LastRead=Index;
                    UpdateStat_AC_RecordNo_LastRead(Index);
                    MQData.Index = Index;

                accessR2Q.Unlock();

            }
            
            return MQData;
        }
        catch(Exception e)
        {
            accessR2Q.Unlock();
            throw e;
        }
    }
    
    //public void add(String pFileName, Object pData, String RefId) throws Exception
    public void add(MQObj pData) throws Exception
    {
        MQObj   NewData = new MQObj();
        
        NewData.Data        = pData.Data;
        NewData.FileName    = pData.FileName;
        NewData.Status      = true;//Active
        NewData.Reference   = pData.Reference;
        NewData.RequestDate = pData.RequestDate;
        NewData.Code        = pData.Code;
                
        //Sent to Q2F Manager otherwise just stays in the quee
        add2DataCluster(NewData);

        //Update Stat            
        gStat_AC_RecordNo++;
    
    }
    
    public void add(Object pData) throws Exception
    {
        MQObj   NewData = new MQObj();
        
        NewData.Data        = pData;
        NewData.FileName    = "";
        NewData.Status      = true;//Active
        NewData.Reference   = "";
        NewData.RequestDate = Util.DateTime.GetDateTime_l();
        
        add(NewData);
    }
    
    public void add(Object pData, String RefId) throws Exception
    {
        MQObj   NewData = new MQObj();
        
        NewData.Data        = pData;
        NewData.FileName    = "";
        NewData.Status      = true;//Active
        NewData.Reference   = RefId;
        NewData.RequestDate = Util.DateTime.GetDateTime_l();
        
        add(NewData);
    }
    
    void add2DataCluster(MQObj pObj) throws Exception
    {
        try
        {
            if (accessW2Q.isAllowed(Q_WAIT_TIME)==true)//30s
            {
                accessW2Q.Lock();

                //Check the size of Active Cluster if over 50K switch to the other Cluster
                if (sizeofCluster(gActiveClusterIndex)>=MAX_CLUSTER_SIZE)
                {
                    //There is readlock inside
                    SwitchClusters();
                }
                
                pObj.InsertDate = Util.DateTime.GetDateTime_l();
                
                if (gActiveClusterIndex==1)
                    CLUSTER1.add(pObj);
                else
                    CLUSTER2.add(pObj);

                accessW2Q.Unlock();
            }
        }
        catch(Exception e)
        {
            throw e;
        }
    }
    
    private int sizeofCluster(int pClusterIndex)
    {
        if (pClusterIndex==1)
            return CLUSTER1.size();
        else
            return CLUSTER2.size();
    }
    
    private void SwitchClusters() throws Exception
    {
        //Lock For Read
        //Switch Clusters (clear the next cluster, copy the data from previous to the new)
        //Reset last read to -1 (xff)
        //UnLock For Read
        try
        {
            if (accessR2Q.isAllowed(Q_WAIT_TIME)==true)//30s
            {
                accessR2Q.Lock();

                //Clear the next cluster
                if (gActiveClusterIndex==1)
                    CLUSTER2.clear();
                else
                    CLUSTER1.clear();

                int iSize = sizeofCluster(gActiveClusterIndex);
                //Copy remaining data from past to new
                for (int i=(gStat_AC_RecordNo_LastRead+1);i<iSize;i++)
                {
                    MQObj   ExistingData = new MQObj();

                    if (gActiveClusterIndex==1)
                    {
                        ExistingData = CLUSTER1.get(i);
                        CLUSTER2.add(ExistingData);
                    }
                    else
                    {
                        ExistingData = CLUSTER2.get(i);
                        CLUSTER1.add(ExistingData);
                    }
                }

                //Rest last read
                UpdateStat_AC_RecordNo_LastRead(RESET_FOR_AC_RECNO_LAST_READ);

                //Switch Active Cluster Index
                if (gActiveClusterIndex==1)
                    gActiveClusterIndex=2;
                else
                    gActiveClusterIndex=1;

                accessR2Q.Unlock();
            }
        }
        catch(Exception e)
        {
            //Report Error
            throw e;
        }
    }
    
    void UpdateStat_AC_RecordNo_LastRead(int pACReadLastRecNo)
    {
        gStat_AC_RecordNo_LastRead = pACReadLastRecNo;
    }
}


