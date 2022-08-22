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
public class DatabaseMan {

//    private transient String serverName;
//    private String serverName_enc;
//    private boolean local;
//    private boolean memory;
//    private transient String hostAddress;
//    private String hostAddress_enc;
//    private transient String port;
//    private String port_enc;
//    private String userPassword;
//    private transient String baseDir;
//    private String baseDir_enc;

    private transient String databaseName;
    private String databaseName_enc;
    
    private Users users;

    private transient String message;

    public DatabaseMan() {
        this(null);
    }

    /**
     *
     * @param databaseName
     */
    public DatabaseMan(String databaseName) {
        this(databaseName, new Users());
    }
    /**
     *
     * @param databaseName
     * @param users
     */
    public DatabaseMan(String databaseName, Users users) {
        this.databaseName = databaseName;
        this.users = users;
    }
    
//    public DatabaseMan(String serverName, boolean local, boolean memory, String port, String user, String userPassword, String baseDir, String databaseName) {
//        this(serverName, local, memory, "localhost", port, user, userPassword, baseDir, databaseName);
//    }

//    public DatabaseMan(String serverName, boolean local, boolean memory, String hostAddress, String port, String user, String userPassword, String baseDir, String databaseName) {
//        this.serverName = serverName;
//        this.local = local;
//        this.memory = memory;
//        this.hostAddress = hostAddress;
//        this.port = port;
//        this.userPassword = userPassword;
//        this.baseDir = baseDir;
//        this.databaseName = databaseName;
//        this.user = user;
//    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDatabaseName_enc() {
        return databaseName_enc;
    }

    public void setDatabaseName_enc(String databaseName_enc) {
        this.databaseName_enc = databaseName_enc;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    protected void decryptFields(LoginManager loginManager) {
        databaseName = loginManager.decrypt(databaseName_enc);
        if (users == null) {
            return;
        }
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            user.decryptFields(loginManager);
        }
    }

    protected void encryptFields(LoginManager loginManager) {
        databaseName_enc = loginManager.encrypt(databaseName);
        if (users == null) {
            return;
        }
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            user.encryptFields(loginManager);
        }
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }
    
    public void serUser(User user) {
        if (users == null || users.isEmpty()) {
            return;
        }
        users.add(user);
    }

    public User getUser(String userName) {
        if (users == null || users.isEmpty()) {
            return null;
        }
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (user.getUserName().equalsIgnoreCase(userName)) {
                return user;
            }
        }
        return null;
    }
}
