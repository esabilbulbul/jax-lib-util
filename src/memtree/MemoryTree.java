/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memtree;

import btree.BinaryTreeNode;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import jaxesa.util.Util;
import static jaxesa.util.Util.Retention.getObjectDeclaredFields;
import static jaxesa.util.Util.Retention.getObjectFields;

/**
 *
 * @author Administrator
 */
public class MemoryTree 
{
    public LinkedList<String> gTempStringTreeDB = new LinkedList<String>();
    public LinkedList<String> gMemStringTreeDB  = new LinkedList<String>();

    //public LinkedList<MemoryTreeNode> gTempTreeDB = new LinkedList<MemoryTreeNode>();
    //public LinkedList<MemoryTreeNode> gMemTreeDB = new LinkedList<MemoryTreeNode>();
    public ArrayList<MemoryTreeNode> gTempTreeDB = new ArrayList<MemoryTreeNode>();
    public ArrayList<MemoryTreeNode> gMemTreeDB  = new ArrayList<MemoryTreeNode>();
    
    public short MTREE_KEY_TYPE_NUMBER = 0;
    public short MTREE_KEY_TYPE_STRING = 1;
    
    //int FULL_TXT_SEARCH_RANGE = 2;//for test purposes
    int FULL_TXT_SEARCH_RANGE = 1000;//default 1k however subject to change to the size of memory tree
    
    public MemoryTree()
    {
        
    }
    
    public void add(long pKey, Object pData)
    {
        add(pKey, getValueswComma(pData), null);
    }
    
    public void addString(String pKey, String pData)
    {   
        //gTempStringTreeDB.add(new StringBuilder(pKey + ":" + pData));
        gTempStringTreeDB.add(pKey + ":" + pData);
    }
    
    public void sortString()
    {
        /*
        LinkedList<String> SortedMemTree = new LinkedList<String>();
        
        long index = 0;
        for (String NodeN:gTempStringTreeDB)
        {   
            String[] sKey = NodeN.split(":");
            //SortedMemTree.add(Long.toString(NodeN.lKey) + "-" + Integer.toString(NodeN.index));
            //String s = Util.Str.leftPad(Long.toString(NodeN.lKey), "0", 18);
            SortedMemTree.add(Util.Str.leftPad(sKey[0], "0", 18) + "-" + Long.toString(index));
            
            index++;
        }
        
        //Sort by Keys
        //-----------------------------------------
        Collections.sort(SortedMemTree);
        */
    }
    
    String getValueswComma(Object pData)
    {
        try
        {
            String sValues = "";
            Object obj = new Object();
            Field[] Flds = getObjectDeclaredFields(pData);
            for(Field FldN: Flds)
            {
                obj = FldN.get(pData);
                if (obj!=null)
                    sValues += obj.toString();
                
                sValues += ",";
            }
            
            //gTempStringTreeDB.add(sValues);
            return sValues;
            
        }
        catch(Exception e)
        {
            return "";
        }    
    }
    
    public void add(long pKey, String pData, Long pFilePos)
    {
        MemoryTreeNode newNode = new MemoryTreeNode();
        
        newNode.index   = gTempTreeDB.size();
        newNode.lKey    = pKey;
        newNode.KeyType = MTREE_KEY_TYPE_NUMBER;
        newNode.Obj     = pData;
        //newNode.Obj     = new MemoryTreeObj();
        //newNode.Obj.oData   = pData;
        //newNode.Obj.FilePos = pFilePos;
        
        gTempTreeDB.add(newNode);
    }
    
    public long size()
    {
        return gMemTreeDB.size();
    }
    
    public void sort()
    {
        LinkedList<String> SortedMemTree = new LinkedList<String>();
        
        for (MemoryTreeNode NodeN:gTempTreeDB)
        {
            //SortedMemTree.add(Long.toString(NodeN.lKey) + "-" + Integer.toString(NodeN.index));
            //String s = Util.Str.leftPad(Long.toString(NodeN.lKey), "0", 18);
            SortedMemTree.add(Util.Str.leftPad(Long.toString(NodeN.lKey), "0", 18) + "-" + Integer.toString(NodeN.index));
        }
        
        //Sort by Keys
        //-----------------------------------------
        Collections.sort(SortedMemTree);
        
        //Replace in real db array in sorted order
        //-----------------------------------------
        long MEGA_BYTE = 1024 * 1024;
        long heapSize     = Runtime.getRuntime().totalMemory() / MEGA_BYTE;
        long maxHeapSize  = Runtime.getRuntime().maxMemory()   / MEGA_BYTE;
        long freeHeapSize = Runtime.getRuntime().freeMemory()  / MEGA_BYTE;
        
        long lKey      = 0;
        int  lOldIndex = 0;
        MemoryTreeNode NodeX = new MemoryTreeNode();
        int  newIndex  = 0;
        for (String sElement: SortedMemTree)
        {
            NodeX     = null;
            //NodeX.index = -1;
            //NodeX.lKey  = -1;
            //NodeX.Obj   = "";
            
            lKey      = -1;
            lOldIndex = -1;
            
            String[] sEls = sElement.split("-");
            lKey      = Long.parseLong(sEls[0]);
            lOldIndex = Integer.parseInt(sEls[1]);
            
            NodeX = gTempTreeDB.get(lOldIndex);
            
            NodeX.index = newIndex;
            
            gMemTreeDB.add(NodeX);
            
            freeHeapSize = Runtime.getRuntime().freeMemory()  / MEGA_BYTE;
            
            newIndex++;
        }
        
        gTempTreeDB.clear();
    }
    
    Object Val2Obj(String pVals, Class<?> pClass)
    {
        int i=0;
        
        try
        {
            Class<?>    EntityClass  = Class.forName(pClass.getName());
            //Object      InstanceObj  = EntityClass.newInstance();//this one depreciated
            Constructor<?> constructor = EntityClass.getConstructor();
            Object InstanceObj = constructor.newInstance();

            String[]    sVals        = pVals.split(",");
            int         index        = 0;
            Field[]     Flds = null;
            
            if (pClass==String.class)
            {
                return sVals[0];
            }
            
            if (pClass==Integer.class)
            {
                return Integer.parseInt(sVals[0]);
            }
            
            if (pClass==Long.class)
            {
                return Long.parseLong(sVals[0]);
            }
            
            Flds = Util.Retention.getObjectFields(pClass.getName());
            
            Class<?> FldType = null;
            for (Field FldN: Flds)
            {
                FldType = FldN.getType();
                if (FldType==String.class)
                {
                    if (sVals[index].equals("")==false)
                        FldN.set(InstanceObj, sVals[index]);
                }
                else if ( (FldType==Long.class) || (FldType==long.class))
                {
                    if (sVals[index].equals("")==false)
                        FldN.setLong(InstanceObj, Long.parseLong(sVals[index]));
                }
                else if ((FldType==Integer.class) || (FldType==int.class))
                {
                    if (sVals[index].equals("")==false)
                        FldN.setLong(InstanceObj, Integer.parseInt(sVals[index]));
                }
                else
                {
                    if (sVals[index].equals("")==false)
                        FldN.set(InstanceObj, sVals[index]);
                }
                
                i=index;
                index++;
            }
            
            return InstanceObj;
        }
        catch(Exception e)
        {
            return null;
        }
    }
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //
    // Summary: The algorithm tries to find the closest point 
    // within the Full_Text_Search_Range by cutting half of the distance at each
    //
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public Object find(long pKey, Class<?> pClass)
    {
        //Find the bottom to the key first in other words find the first node lower than the key value
        //      Cut the distance by half to find
        // Summary: The algorithm tries to find the closest point within the Full_Text_Search_Range by cutting half of the distance at each
        
        int     iSizeMemTree = gMemTreeDB.size();
        boolean bBottomFound = false;        
        int     iNewRange    = 0;
        if (iSizeMemTree<300000)
        {
            FULL_TXT_SEARCH_RANGE = 1000;
        }
        else
        {
            
            iNewRange = (iSizeMemTree / 300000) * 1000;
            if (iNewRange>=25000)
                FULL_TXT_SEARCH_RANGE = 25000;
            else
                FULL_TXT_SEARCH_RANGE = iNewRange;
            
        }
        //Check the first and last elements make sure they are not the elements looking for
        MemoryTreeNode NodeX = new MemoryTreeNode();
        
        //First
        //NodeX = gMemTreeDB.getFirst();
        NodeX = gMemTreeDB.get(0);
        if (NodeX.lKey==pKey)
            return Val2Obj(NodeX.Obj, pClass);
            //return NodeX.Obj;
        
        //Last
        //NodeX = gMemTreeDB.getLast();
        NodeX = gMemTreeDB.get(iSizeMemTree-1);
        if (NodeX.lKey==pKey)
            return Val2Obj(NodeX.Obj, pClass);
            //return NodeX.Obj;
        
        MTFlrCeilingInfo MTFloorCeilingRange  = new MTFlrCeilingInfo();
        MTFlrCeilingInfo MTFullSearchRAnge    = new MTFlrCeilingInfo();
        
        int iStartIndex = 0;
        int iEndIndex   = 0;

        int iTotReadNo  = 0;
        
        MTFloorCeilingRange = findFloorCeilingRange(pKey);
        iTotReadNo  += MTFloorCeilingRange.ReadCounter;
        if (MTFloorCeilingRange.bExactMatch==false)
        {
            iStartIndex = MTFloorCeilingRange.FloorIndex;
            iEndIndex   = MTFloorCeilingRange.CeilingIndex;
            
            if (!(Math.abs(iEndIndex - iStartIndex)<=FULL_TXT_SEARCH_RANGE))
            {
                //If not within range
                //Find closestPointRange
                MTFloorCeilingRange = findClosestPointRange(pKey, MTFloorCeilingRange.FloorIndex, MTFloorCeilingRange.CeilingIndex);

                if (MTFloorCeilingRange!=null)
                {
                    iStartIndex = MTFloorCeilingRange.FloorIndex;
                    iEndIndex   = MTFloorCeilingRange.CeilingIndex;
                }
                
                iTotReadNo  += MTFloorCeilingRange.ReadCounter;
            }
        }

        if (MTFloorCeilingRange.bExactMatch==true)
        {
            NodeX = gMemTreeDB.get(MTFloorCeilingRange.ExactMatchIndex);
            return Val2Obj(NodeX.Obj, pClass);
            //return NodeX.Obj;
        }
        
        //Search between NextIndex and PrevIndex (remember: PrevIndex > NextIndex)
        for (int i=iStartIndex; i<iEndIndex; i++)
        {
            iTotReadNo++;
            NodeX = null;
            NodeX = gMemTreeDB.get(i);
            
            if (iTotReadNo>FULL_TXT_SEARCH_RANGE)
                iTotReadNo=iTotReadNo;
            
            if (NodeX.lKey==pKey)
                return Val2Obj(NodeX.Obj, pClass);
                //return NodeX.Obj;
        }

        return null;
    }
    
    //Look for the closest point for full_text_search
    private MTFlrCeilingInfo findClosestPointRange(long pKey, int piFloorIndex, int piCeilingIndex)
    {
        int iFloorIndex     = piFloorIndex;
        int iCeilingIndex   = piCeilingIndex;
        MTFlrCeilingInfo MTInfo  = new MTFlrCeilingInfo();
        
        int iReadCounter = 0;
        while( !((MTInfo.bExactMatch==true) || (MTInfo.bWithinFullTextSearch==true)))
        {
            MTInfo = null;
                    
            MTInfo = findTurningPoint(pKey, iFloorIndex, iCeilingIndex);
            iReadCounter += MTInfo.ReadCounter;
            
            if (MTInfo.bExactMatch==true)
            {
                break;
            }
            
            if (MTInfo.bWithinFullTextSearch==true)
            {
                break;
            }
            
            iFloorIndex     = MTInfo.FloorIndex;
            iCeilingIndex   = MTInfo.CeilingIndex;
        }
        
        MTInfo.ReadCounter = iReadCounter;
        
        return MTInfo;
    }
    
    // This searchs for the boundaries (floor, ceiling) and exits the point it 
    // founds the change point. Change point is ,say, search for boundaries going 
    // upward and one point the cursor realized the key left in previous indexes.
    private MTFlrCeilingInfo findTurningPoint(long pKey, int piFloorIndex, int piCeilingIndex)
    {
        MTFlrCeilingInfo MTInfo  = new MTFlrCeilingInfo();
        boolean bFullTextSeachRange = false;
        int iNextIndex = piCeilingIndex;
        int iPrevIndex = piFloorIndex;
        int iLastFloorIndex   = piFloorIndex;
        int iLastCeilingIndex = piCeilingIndex;
        int iDiff      = 0;
        
        boolean      bUpward = true;
        MemoryTreeNode NodeX = new MemoryTreeNode();
        iDiff = Math.abs(iNextIndex - iPrevIndex);
        
        int iReadCounter = 0;
        while (bFullTextSeachRange==false)
        {
            iReadCounter++;
            //if (bUpward==true)
                iNextIndex = iPrevIndex + iDiff/2;
                if (iNextIndex==iPrevIndex)
                {
                    iNextIndex++;//April 15/2017
                }
            //else
            //    iNextIndex = iNextIndex - iDiff/2;
            
            NodeX = gMemTreeDB.get(iNextIndex);
            
            if (NodeX.lKey>pKey)
            {
                iLastCeilingIndex = iNextIndex;
                        
                iDiff = Math.abs(iNextIndex - iPrevIndex);
                if (iDiff<=FULL_TXT_SEARCH_RANGE)
                {
                    //within range
                    //return next and previous
                    MTInfo.CeilingIndex          = iNextIndex;
                    MTInfo.FloorIndex            = iPrevIndex;
                    MTInfo.bWithinFullTextSearch = true;
                    MTInfo.ReadCounter           = iReadCounter;
                    return MTInfo;
                }
                
                if (bUpward==true)
                {
                    //                Change Point
                    //-----------------------------------------
                    
                    //direction changed so the ceiling (direction to down)
                    MTInfo.CeilingIndex          = iNextIndex;
                    MTInfo.FloorIndex            = iPrevIndex;
                    MTInfo.ReadCounter           = iReadCounter;
                    return MTInfo;
                }
                
                bUpward    = false;//search in lower indexes                
                iPrevIndex      = iNextIndex;
            }
            else if (NodeX.lKey==pKey)
            {
                //exact match occured
                MTInfo.ExactMatchIndex = iNextIndex;
                MTInfo.bExactMatch     = true;
                MTInfo.ReadCounter     = iReadCounter;
                return MTInfo;
            }
            else
            {
                iDiff = Math.abs(iLastCeilingIndex - iNextIndex);
                /*
                if (iDiff<=FULL_TXT_SEARCH_RANGE)
                {
                    //within range
                    //return next and previous

                    MTInfo.CeilingIndex          = iNextIndex;
                    MTInfo.FloorIndex            = iPrevIndex;
                    MTInfo.bWithinFullTextSearch = true;
                    MTInfo.ReadCounter           = iReadCounter;
                    return MTInfo;

                }
                */
                
                if (bUpward == false)
                {
                    //                Change Point
                    //-----------------------------------------

                    //direction changed so the ceiling (direction to down)
                    MTInfo.CeilingIndex          = iPrevIndex;
                    MTInfo.FloorIndex            = iNextIndex;                    
                    MTInfo.ReadCounter           = iReadCounter;
                    return MTInfo;
                }
                
                bUpward = true;
                iLastFloorIndex = iPrevIndex;
                iPrevIndex = iNextIndex;
            }
        }
        
        return null;//unknown        
    }
    
    private MTFlrCeilingInfo findFloorCeilingRange(long pKey)
    {
        boolean bUpward      = false;
        MTFlrCeilingInfo MTInfo  = new MTFlrCeilingInfo();
        int     iSizeMemTree = gMemTreeDB.size();
        boolean bBottomFound = false;
        int     ReadCounter  = 0;
        
        int iNextIndex = iSizeMemTree;
        int iPrevIndex = iSizeMemTree;
        int iDiff      = 0;
        
        MemoryTreeNode NodeX = new MemoryTreeNode();
        
        while (bBottomFound == false)
        {
            ReadCounter++;
            
            iNextIndex = iPrevIndex/2;//downward
            
            NodeX = gMemTreeDB.get(iNextIndex);
            
            if (NodeX.lKey>pKey)
            {
                if (iNextIndex==0)
                {
                    //meaning is the key is still smaller then the lowest key, in other word the key not found 
                    iPrevIndex=0;
                    break;
                }

                //larger
                iPrevIndex = iNextIndex;
                bUpward    = false;
                continue;                
            }
            else if (NodeX.lKey==pKey)
            {
                MTInfo.bExactMatch     = true;
                MTInfo.ExactMatchIndex = iNextIndex;
                MTInfo.ReadCounter     = ReadCounter;
                return MTInfo;
            }
            else
            {
                //Smaller
                bBottomFound = true;
                
                iDiff  = Math.abs(iPrevIndex - iNextIndex);
                
                if (iDiff <= FULL_TXT_SEARCH_RANGE)//within full text search range
                {
                    MTInfo.bWithinFullTextSearch = true;
                    break;
                }
                
            }
            
        }
        
        MTInfo.FloorIndex   = iNextIndex;
        MTInfo.CeilingIndex = iPrevIndex;
        MTInfo.ReadCounter     = ReadCounter;
        
        return MTInfo;
    }
    
}


