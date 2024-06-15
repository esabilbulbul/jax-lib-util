/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.queue;

/**
 *
 * @author Administrator
 */
public class MQObj 
{
    public int     Index;//This will only be filled when read by the Qmanager
    public boolean Status;//true:Active, false:Inactive
    public String  FileName;
    //public long    RequestDate;//DateTime before sent to add
    public String  RequestDate;
    public long    InsertDate;
    public String  Reference;
    public Object  Data;
    public String  Code;//If Error is reported
    
    public MQObj()
    {
        FileName  = "";
        Data = new Object();
        Code = "";
    }
}
