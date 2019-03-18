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

import jaxesa.persistence.annotations.GenerationType;
import java.math.BigInteger;

/**
 *
 * @author Administrator
 */
@Target(value = {ElementType.METHOD, ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface GeneratedValue 
{
    public GenerationType strategy() default GenerationType.AUTO;

    public String generator() default "";
}

