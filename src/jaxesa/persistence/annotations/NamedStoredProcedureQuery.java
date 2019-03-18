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
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) 
public @interface NamedStoredProcedureQuery 
{
    public String name();

    public String procedureName();

    public StoredProcedureParameter[] parameters() default {};

    public Class[] resultClasses() default {};

    public String[] resultSetMappings() default {};

    //public QueryHint[] hints() default {};
}
