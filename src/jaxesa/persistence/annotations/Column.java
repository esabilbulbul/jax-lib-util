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
import java.math.BigInteger;

/**
 *
 * @author Administrator
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD) 
public @interface Column
{
    public String  name() default "";
    public boolean nullable() default false;
    public int     length() default 0;
    public boolean unique() default false;//also used as Key / Index
    public int     precision() default 0;
    public int     scale()     default 0;
}
