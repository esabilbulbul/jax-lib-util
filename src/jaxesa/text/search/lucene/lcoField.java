/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.text.search.lucene;

/**
 *
 * @author Administrator
 */
public class lcoField 
{
    public String name;
    public long   freq;//frequency
    public String source;//T: Title K:Keywords/Tag C: Content

    public lcoField()
    {
        name = "";
        freq = 0;
        
    }
    
}
