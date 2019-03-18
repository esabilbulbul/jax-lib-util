/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package btree;

/**
 * 
 * Reference: https://www.tutorialspoint.com/data_structures_algorithms/tree_data_structure.htm
 * 
 * @author Administrator
 */
public class BinaryTreeNode
{
    short         KeyType;//numeric (0) vs string (1)
    
    int           index;
    long          lKey;
    BinaryTreeObj Obj;

    Integer       leftChildIndex;
    Integer       rightChildIndex;
}


