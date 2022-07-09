/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotmail.doljin99;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author dolji
 */
public class DatabaseManOld {

    private String serverName;
    private boolean local;
    private String hostAddress;
    private String port;
    private String baseDir;

    private String databaseName;

    private transient String message;

    public DatabaseManOld(String serverName, boolean local, String port, String baseDir, String databaseName) {
        this(serverName, local, "localhost", port, baseDir, databaseName);
    }

    public DatabaseManOld(String serverName, boolean local, String hostAddress, String port, String baseDir, String databaseName) {
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

    public boolean isLocal() {
        return local;
    }

    public String getBaseDir() {
        return baseDir;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }
    
}
