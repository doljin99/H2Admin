/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotmail.doljin99;

import com.hotmail.doljin99.loginmanager.LoginManager;

/**
 *
 * @author dolji
 */
public class User {
    
    private transient String userName;
    private String userName_enc;
    private transient String userPassword;
    private String userPassword_enc;

    private boolean administrator;

    public User() {
    }

    /**
     * Get the value of administrator
     *
     * @return the value of administrator
     */
    public boolean isAdministrator() {
        return administrator;
    }

    /**
     * Set the value of administrator
     *
     * @param administrator new value of administrator
     */
    public void setAdministrator(boolean administrator) {
        this.administrator = administrator;
    }

    /**
     * Get the value of userPassword
     *
     * @return the value of userPassword
     */
    public String getUserPassword() {
        return userPassword;
    }

    /**
     * Set the value of userPassword
     *
     * @param userPassword new value of userPassword
     */
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    /**
     * Get the value of userName
     *
     * @return the value of userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Set the value of userName
     *
     * @param userName new value of userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword_enc() {
        return userPassword_enc;
    }

    public void setUserPassword_enc(String userPassword_enc) {
        this.userPassword_enc = userPassword_enc;
    }

    public String getUserName_enc() {
        return userName_enc;
    }

    public void setUserName_enc(String userName_enc) {
        this.userName_enc = userName_enc;
    }
        
    public void encryptFields(LoginManager loginManager) {
        userName_enc = loginManager.encrypt(userName);
        userPassword_enc = loginManager.encrypt(userPassword);
    }
    
    public void decryptFields(LoginManager loginManager) {
        userName = loginManager.decrypt(userName_enc);
        userPassword = loginManager.decrypt(userPassword_enc);
    }
}
