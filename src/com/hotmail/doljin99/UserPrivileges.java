/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotmail.doljin99;

import java.util.ArrayList;

/**
 *
 * @author dolji
 */
public class UserPrivileges extends ArrayList<TablePrivileges>{
    
    private final String userName;

    /**
     *
     * @param userName
     */
    public UserPrivileges(String userName) {
        super();
        this.userName = userName;
    }

    /**
     * Get the value of userName
     *
     * @return the value of userName
     */
    public String getUserName() {
        return userName;
    }
    
    public void setRightsAll(boolean grant) {
        for (TablePrivileges tablePrivileges : this) {
            tablePrivileges.setRightsAll(grant);
        }
    }

}
