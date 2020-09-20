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
    MUST,           //Will force to verify token (this will verify the token source and state of user must be "v" (verified/loggedin)
    SOURCEONLY,     //Will force to verify token regardless of the user states (anonymous user will be allowed as well)
    GENERATEONLY    //Will generate token at each call without need to pass the verification of the recieved token
}

