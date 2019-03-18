/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.error;

/**
 *
 * @author Administrator
 */
public class ErrorCode extends Exception
{
    String  GroupCode;
    int     Id;
    String  Description;//Default description
    private String  RuntimeStack;//This is not for the enums, only for the exceptions captured to elevate this to the higher level through this data el
    
    @Override
    public String toString()
    {
        //if (RuntimeStack==null)
            return GroupCode + "-" + Id + " Description: " + Description + " RuntimeStack: " + RuntimeStack;
        //else
            //return GroupCode + "-" + Id + " Description:" + Description + " RunTime: ";//+ RuntimeStack;
    }
    
    public String getRawMessage()
    {
        return RuntimeStack;
    }
    
    //GetMessage should drop line into the log file
    public String getMessage()
    {
        return toString();
    }

    public int value()
    {
        return Id;
    }
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //                          WARNING
    //
    // Don't call this method this way as shown as it will cause be overriden 
    // by multiple threads
    // DON'T : 
    //      ErrorCodes.Persistence.EXCEPTION.exception().addDescription("ddd");
    //
    // USE THIS WAY:
    //        ErrorCode Err = new ErrorCode();
    //        Err = ErrorCodes.Persistence.EXCEPTION.exception();
    //        Err.addDescription("Requested Field Name: " + pFieldName + e.getMessage());
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public ErrorCode addDescription(String pDesc)
    {
        this.RuntimeStack = pDesc;
        return this;
    }
            
}

