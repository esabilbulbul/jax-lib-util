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

//import jaxesa.annotations.Enumarations.ProducesTypes;
import jaxesa.annotations.MediaType;
/**
 *
 * @author Administrator
 */


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD) //on class level
public @interface Produces 
{
    public MediaType value() default  MediaType.PLAINTEXT;
}
