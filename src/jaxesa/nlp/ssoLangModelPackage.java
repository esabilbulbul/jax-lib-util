/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.nlp;

import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class ssoLangModelPackage 
{
    public ArrayList<ssoModelFile> Sentence;
    public ArrayList<ssoModelFile> Token;
    public ArrayList<ssoModelFile> POSTagger;
    
    public ssoLangModelPackage()
    {
        Sentence  = new ArrayList<ssoModelFile>();
        Token     = new ArrayList<ssoModelFile>();
        POSTagger = new ArrayList<ssoModelFile>();
    }
}
