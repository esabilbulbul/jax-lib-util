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

/**
 *
 * @author Administrator
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD) //on class level
public @interface Callback
{
    // WARNING: This Annotation only works if the API returns ssoAPIResponse type of variable 
    public ThreadActionType type() default ThreadActionType.NONE;
    public String source() default "";//this MUST be same as the API name the annotations tagged to. WARNING: The source method/class MUST be NOT static class
    public String targetClass() default "";//this MUST be package.class.method style declaration
    public String targetMethod() default "";
}

