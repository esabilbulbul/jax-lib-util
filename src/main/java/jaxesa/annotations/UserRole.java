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
public enum UserRole 
{
    ADMIN("A"),
    MANAGER("M"),
    INVENTORY("I"),
    FINANCE("F"),
    CASHIER("C");

    private final String abbreviation;

    // Constructor for the enum values
    UserRole(String abbreviation) 
    {
        this.abbreviation = abbreviation;
    }

    // Getter method for the abbreviation
    public String value() 
    {
        return abbreviation;
    }

}

