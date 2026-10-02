/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jaxesa.db;

import jaxesa.persistence.annotations.ParameterMode;

/**
 *
 * @author Administrator
 */
public class dbParam
{
    public int index = 1;//start 1
    public String name = "";
    public Class type;
    public Object val;
    public ParameterMode mode = ParameterMode.IN;//default IN
}

