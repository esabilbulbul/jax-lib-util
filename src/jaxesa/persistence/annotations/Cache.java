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
import jaxesa.definitions.LimitParams;

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
    public boolean value() default true;//
    public String SplitKeyColumn() default "";//if empty, cache all the rows. Otherwise, only cache the rows for the keyColumn that are matched. Only works with LAZY Load type.
    public int maxRow() default LimitParams.MAX_CACHE_ROW_NUMBER;
    public int maxDuration() default LimitParams.MAX_CACHE_DURATION;//1/2 day
    public CacheLoadTypes type() default CacheLoadTypes.LAZY;//on_startup is not supported as of Dec 2020. The next releases will support 
    public CacheLevels level() default CacheLevels.LEVEL_2;//default redis memdb will be used
}


