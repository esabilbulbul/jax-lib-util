/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.persistence.annotations;

/**
 *
 * @author Administrator
 */
public enum CacheLoadTypes 
{
    LAZY,   // Loads at the query time (first time)
    ON_START // LOADS all the data when the servlets starting up
}
