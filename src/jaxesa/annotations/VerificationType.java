/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.annotations;

/**
 *
 * @author esabil
 */

public enum VerificationType
{
    NONE,           //Will do nothing related to Token
    MUST,           //Will force to verify token
    GENERATEONLY    //Will generate token at each call without verifying the recieved token
}

