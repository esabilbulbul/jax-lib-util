/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package btree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import jaxesa.util.Util;

/**
 *
 * Reference: https://www.tutorialspoint.com/data_structures_algorithms/tree_data_structure.htm
 * 
 * @author Administrator
 */
public class BinaryTree 
{
    public LinkedList<BinaryTreeNode> gBTreeDB = new LinkedList<BinaryTreeNode>();
    
    public short KEY_TYPE_NUMBER = 0;
    public short KEY_TYPE_STRING = 1;
    
    public boolean test(long pKey, Object pData, Long pFilePos)
    {
        BinaryTreeNode newNode    = new BinaryTreeNode();
        newNode.lKey  = pKey;
        newNode.index = gBTreeDB.size();
        gBTreeDB.add(newNode);
        return true;
    }
    
    public void sort()
    {
        LinkedList<String> t = new LinkedList<String>();
        
        for (BinaryTreeNode NodeX:gBTreeDB)
        {
            t.add(Long.toString(NodeX.lKey) + "-" + Integer.toString(NodeX.index));
        }
        
        Collections.sort(t);
    }
    
    public boolean add(long pKey, Object pData, Long pFilePos)
    {
        BinaryTreeNode newNode    = new BinaryTreeNode();
        BinaryTreeNode curNode    = new BinaryTreeNode();
        BinaryTreeNode parentNode = new BinaryTreeNode();
        
        int bTreeSize = gBTreeDB.size();

        if (pFilePos!=null)
            newNode.Obj.FilePos = pFilePos;

        newNode.KeyType         = KEY_TYPE_NUMBER;
        newNode.lKey            = pKey;
        newNode.leftChildIndex  = null;
        newNode.rightChildIndex = null;
        newNode.Obj = new BinaryTreeObj();
        newNode.Obj.oData       = pData;
        
        if (bTreeSize==0)
        {
            newNode.index = bTreeSize;
            gBTreeDB.add(newNode);
            return true;
        }
        else
        {
            curNode = gBTreeDB.get(0);//current = root
            
            //for (BinaryTreeNode curNode:gBTreeDB)//parent = current
            while(curNode!=null)
            {
                parentNode = curNode;
                
                if (pKey < parentNode.lKey)
                {
                    //insert to the left
                    if (curNode.leftChildIndex==null)
                    {
                        curNode.leftChildIndex = bTreeSize;
                        newNode.index = bTreeSize;
                        gBTreeDB.add(newNode);
                        return true;
                    }
                    else
                    {
                        curNode = gBTreeDB.get(curNode.leftChildIndex);
                    }
                }
                else
                {
                    if (pKey == parentNode.lKey)
                        pKey = pKey;

                    //insert to the right
                    if (curNode.rightChildIndex==null)
                    {
                        curNode.rightChildIndex = bTreeSize;
                        newNode.index = bTreeSize;
                        gBTreeDB.add(newNode);
                        return true;
                    }
                    else
                    {
                        curNode = gBTreeDB.get(curNode.rightChildIndex);
                    }
                    
                }
                
            }
        }
        
        return false;
    }
    
    public Object getValue(long pKey)
    {
        int index = -1;
        
        index = getIndex(pKey);
        
        if (index!=-1)
            return gBTreeDB.get(index).Obj.oData;
        else
            return null;
    }
    
    public BinaryTreeObj getValueObject(long pKey)
    {
        int index = -1;
        
        index = getIndex(pKey);
        
        if (index!=-1)
            return gBTreeDB.get(index).Obj;
        else
            return null;
    }
    
    public int getIndex(long pKey)
    {
        BinaryTreeNode curNode    = new BinaryTreeNode();
        BinaryTreeNode parentNode = new BinaryTreeNode();

        boolean bDataFound = false;
        
        curNode = gBTreeDB.get(0);//current = root
        
        while(bDataFound==false)
        {
            parentNode = curNode;
            
            if (pKey==parentNode.lKey)
                return parentNode.index;
            
            if (pKey < parentNode.lKey)
            {
                if (curNode.leftChildIndex!=null)
                {
                    curNode = gBTreeDB.get(curNode.leftChildIndex);
                }
                else
                {
                    break;
                }
            }
            else
            {
                if (curNode.rightChildIndex!=null)
                {
                    curNode = gBTreeDB.get(curNode.rightChildIndex);
                }
                else
                {
                    break;
                }
            }
        }
        
        return -1;
    }
    
}


