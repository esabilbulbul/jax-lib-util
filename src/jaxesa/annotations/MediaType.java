/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.annotations;

/**
 *
 * @author Administrator
 */
public enum MediaType
{
    PLAINTEXT,     //("text/plain"), 
    PLAINTEXT_PLUS,// returns ssoAPIResponse("text/plain" will be put in content element) + update Authentication flag
    JSON,          //("application/jason")    
    JSON_PLUS      // returns ssoAPIResponse(from RestAPI) + updates bAuthentication flag
}

/*
    DIFFERENCE BETWEEN PLAINTEXT_PLUS & JSON_PLUS
    
    In plaintext_plus, an empty ssoAPIResponse created and the response message (text) assigned to .Content element
    In Json_plus, the api returns ssoAPIResponse and the framework updates the .AuthenticationFlag before sending it
*/