/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jaxesa.persistence.annotations.CacheLoadTypes;

/**
 *
 * @author Administrator
 */
@Deprecated
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD) //on method level
public @interface Cacheable 
{
    public CacheLoadTypes type() default CacheLoadTypes.LAZY;
    public String key() default "";//seperated with "-"
    public String name() default "";// must be same name with Stored Procedure 
}

