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
public enum CacheLevels 
{
    LEVEL_1, // apps own memory
    LEVEL_2  // memory db engine i.e. redis
}
