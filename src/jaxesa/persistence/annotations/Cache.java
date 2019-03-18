/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.persistence.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Administrator
 * 
 * Cache         : This annotations enables Entity the value stored in memory
 * ParameterTable: This allows the value of entities serialized open to all users
 * SysTable      : This allows the values of entities serialized as well the difference from ParameterTable is this is for system user only
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) 
public @interface Cache 
{
    public boolean value() default true;
}
