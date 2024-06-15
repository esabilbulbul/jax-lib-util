/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.annotations;



import java.lang.annotation.ElementType;
import java.util.ArrayList;
import jaxesa.annotations.MediaType;
import jaxesa.annotations.MethodTypes;

import jaxesa.annotations.Consumes;
import jaxesa.annotations.GET;
import jaxesa.annotations.POST;
import jaxesa.annotations.Path;
import jaxesa.annotations.Produces;



/**
 *
 * @author Administrator
 */
/*
enum MethodTypes
{
    NONE,
    GET,
    POST
};
*/

public class AnnoAttributes 
{
    public class Parameter
    {
        public Integer  Number;
        public String   Name;        
        public Class<?> Format;//String.TYPE, Integer.TYPE Biginteger.TYPE
        
        public Parameter()
        {
            Name        =   "";
            //Value     =   "";
            //OutFormat   =   "";
        }
    }

    
    public String                   ClassName;
    public String                   MethodName;
    public ElementType              Type;//Class, Method and etc.
    public MediaType                Consumes;
    public MethodTypes              MethodType;
    public VerificationType         TokenVerification;
    public UserRole[]               UserRoleRequirements;
    public boolean                  isLoginMethod;
    public boolean                  isLoginVia;
    public String                   ClassPath;
    public String                   MethodPrototype;  //ClassPath @Path + Method Path @Path + Parameters 
    public String                   MethodPath;       //Method Path @Path
    public ArrayList<Parameter>     MethodParameters; //Parameters @Path 
    public ArrayList<Parameter>     ServiceParameters;//Service parameters
    public MediaType                Produces; //Return Type
    public String                   Redirect_URL;
    
    public ThreadActionType         ThreadActType;
    public String                   ThreadSource;
    public String                   ThreadTargetClass;
    public String                   ThreadTargetMethod;
    
    public AnnoAttributes()
    {
        ClassName           =   "";
        MethodName          =   "";
        Type                =   ElementType.TYPE;
        Consumes            =   MediaType.PLAINTEXT;
        MethodType          =   MethodTypes.NONE;//MethodTypes.NONE;
        MethodPrototype     =   "";
        MethodPath          =   "";
        MethodParameters    =   new ArrayList<Parameter>();
        ServiceParameters   =   new ArrayList<Parameter>();
        ClassPath           =   "";
        Produces            =   MediaType.PLAINTEXT;
        TokenVerification   =   VerificationType.NONE;
        
        ThreadActType       =   ThreadActionType.NONE;
        ThreadSource        =   "";
        ThreadTargetClass   =   "";
        ThreadTargetMethod  =   "";
    }
}
