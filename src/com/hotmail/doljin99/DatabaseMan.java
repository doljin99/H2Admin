/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotmail.doljin99;

import com.hotmail.doljin99.loginmanager.LoginManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author dolji
 */
public class DatabaseMan {

    private transient String serverName;
    private String serverName_enc;
    private boolean local;
    private transient String hostAddress;
    private String hostAddress_enc;
    private transient String port;
    private String port_enc;
    private transient String baseDir;
    private String baseDir_enc;

    private transient String databaseName;
    private String databaseName_enc;

    private transient String message;

    public DatabaseMan() {
    }

    public DatabaseMan(String serverName, boolean local, String port, String baseDir, String databaseName) {
        this(serverName, local, "localhost", port, baseDir, databaseName);
    }

    public DatabaseMan(String serverName, boolean local, String hostAddress, String port, String baseDir, String databaseName) {
        this.serverName = serverName;
        this.local = local;
        this.hostAddress = hostAddress;
        this.port = port;
        this.baseDir = baseDir;
        this.databaseName = databaseName;
    }

    public String getServerName() {
        return serverName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getPort() {
        return port;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getJdbcUrl() {
        StringBuilder sb = new StringBuilder();
        sb.append("jdbc:h2:tcp://");
        sb.append(getHostAddress());
        sb.append(":");
        sb.append(port);
        sb.append("/");
        sb.append(baseDir);
        sb.append("/");
        sb.append(databaseName);

        return sb.toString();
    }

    /**
     * Get the value of hostAddress
     *
     * @return the value of hostAddress
     */
    public String getHostAddress() {
        return hostAddress;
    }

    /**
     * Set the value of hostAddress
     *
     * @param hostAddress new value of hostAddress
     */
    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    public Connection getConnection() {
        Connection connection;
        try {
            connection = DriverManager.
                getConnection(getJdbcUrl(), "sa", "");
            setMessage("connection 성공: " + getJdbcUrl());
            return connection;
        } catch (SQLException ex) {
            setMessage(getJdbcUrl() + " connection 실패: " + ex.getLocalizedMessage());
            return null;
        }
    }

    public boolean connectionTest() {
        Connection conn = null;
        try {
            conn = DriverManager.
                getConnection(getJdbcUrl(), "sa", "");
            setMessage("connection 성공: " + getJdbcUrl());
            return true;
        } catch (SQLException ex) {
            setMessage(getJdbcUrl() + "connection 실패: " + ex.getLocalizedMessage());
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                }
            }
        }
    }

    public String getServerName_enc() {
        return serverName_enc;
    }

    public void setServerName_enc(String serverName_enc) {
        this.serverName_enc = serverName_enc;
    }

    public boolean isLocal() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }

    public String getHostAddress_enc() {
        return hostAddress_enc;
    }

    public void setHostAddress_enc(String hostAddress_enc) {
        this.hostAddress_enc = hostAddress_enc;
    }

    public String getPort_enc() {
        return port_enc;
    }

    public void setPort_enc(String port_enc) {
        this.port_enc = port_enc;
    }

    public String getBaseDir() {
        return baseDir;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    public String getBaseDir_enc() {
        return baseDir_enc;
    }

    public void setBaseDir_enc(String baseDir_enc) {
        this.baseDir_enc = baseDir_enc;
    }

    public String getDatabaseName_enc() {
        return databaseName_enc;
    }

    public void setDatabaseName_enc(String databaseName_enc) {
        this.databaseName_enc = databaseName_enc;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
    
    protected void decryptFields(LoginManager loginManager) {
        serverName = loginManager.decrypt(serverName_enc);
        hostAddress = loginManager.decrypt(hostAddress_enc);
        port = loginManager.decrypt(port_enc);
        baseDir = loginManager.decrypt(baseDir_enc);
        databaseName = loginManager.decrypt(databaseName_enc);
    }
    
    protected void encryptFields(LoginManager loginManager) {
        serverName_enc = loginManager.encrypt(serverName);
        hostAddress_enc = loginManager.encrypt(hostAddress);
        port_enc = loginManager.encrypt(port);
        baseDir_enc = loginManager.encrypt(baseDir);
        databaseName_enc = loginManager.encrypt(databaseName);
    }
}
