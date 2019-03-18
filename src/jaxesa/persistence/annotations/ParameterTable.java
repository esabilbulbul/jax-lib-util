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
 * The sole purpose of this Annotation is the data in this table will be saved on serialization
 * This parameter table should be used with cache if the data contained wanted to be saved. If this alone used
 * the table will only be created on serialization without data
 * 
 * The data if it is in cache will be saved on serialization
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) 
public @interface ParameterTable 
{
    public boolean system() default false;
}

