/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotmail.doljin99;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import org.h2.tools.Server;

/**
 *
 * @author dolji
 */
public class ServerMan {

    public static final String TCP_PORT = "-tcpPort";
    public static final String TCP_ALLOW_OTHERS = "-tcpAllowOthers";
    public static final String IF_NOT_EXISTS = "-ifNotExists";
    public static final String IF_EXISTS = "-ifExists";
    public static final String TCP_DAEMON = "-tcpDaemon";
    public static final String TCP_PASSWORD = "-tcpPassword";

    private String serverName;
    private boolean local;
    private String hostAddress;
    private String tcpPort;
    private boolean tcpAllowOthers;
    private boolean ifNotExists;
    private boolean tcpDaemon;
    private String tcpPassword;
    private String baseDir;
    
    private DatabaseMen databaseMen;

    private transient String[] args = {
        TCP_PORT, tcpPort, TCP_ALLOW_OTHERS, IF_NOT_EXISTS, TCP_DAEMON, TCP_PASSWORD, tcpPassword
    };

    private transient Server server;
    private boolean run;
    private transient String message;

    /**
     *
     * @param name
     */
    public ServerMan(String name) {
        this(name, "9092");
    }

    /**
     *
     * @param name
     * @param port
     */
    public ServerMan(String name, String port) {
        this(name, "localhost", port, true);
    }

    /**
     *
     * @param name
     * @param hostAddress
     * @param port
     * @param tcpAllowOthers
     */
    public ServerMan(String name, String hostAddress, String port, boolean tcpAllowOthers) {
        this(name, hostAddress, port, tcpAllowOthers, false);
    }
    
    public ServerMan(String name, String hostAddress, String port, boolean tcpAllowOthers, boolean ifNotExists) {
        this(name, hostAddress, port, tcpAllowOthers, ifNotExists, new DatabaseMen());
    }

    /**
     *
     * @param name
     * @param hostAddress
     * @param port
     * @param tcpAllowOthers
     * @param ifNotExists
     * @param databaseMen
     */
    public ServerMan(String name, String hostAddress, String port, boolean tcpAllowOthers, boolean ifNotExists, DatabaseMen databaseMen) {
        this.serverName = name;
        this.hostAddress = hostAddress;
        this.tcpPort = (port == null || port.isEmpty()) ? "9092" : port;
        this.tcpAllowOthers = tcpAllowOthers;
        this.ifNotExists = ifNotExists;
        this.databaseMen = databaseMen;

        init();
    }

    private void init() {
        run = false;
        if (hostAddress == null || hostAddress.isEmpty()) {
            if (isLocal()) {
                hostAddress = "localhost";
            }
        }
    }

    /**
     * Get the value of local
     *
     * @return the value of local
     */
    public boolean isLocal() {
        return local;
    }

    /**
     * Set the value of local
     *
     * @param local new value of local
     */
    public void setLocal(boolean local) {
        this.local = local;
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

    public boolean isTcpDaemon() {
        return tcpDaemon;
    }

    public void setTcpDaemon(boolean tcpDaemon) {
        this.tcpDaemon = tcpDaemon;
    }

    public String getTcpPassword() {
        return tcpPassword;
    }

    public void setTcpPassword(String tcpPassword) {
        this.tcpPassword = tcpPassword;
    }

    public String getServerName() {
        return serverName;
    }

    public String getBaseDir() {
        return baseDir;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    public String getPort() {
        return tcpPort;
    }

    public boolean isTcpAllowOthers() {
        return tcpAllowOthers;
    }

    public void setTcpAllowOthers(boolean tcpAllowOthers) {
        this.tcpAllowOthers = tcpAllowOthers;
    }

    public boolean isIfNotExists() {
        return ifNotExists;
    }

    public void setIfNotExists(boolean ifNotExists) {
        this.ifNotExists = ifNotExists;
    }

    public Server createServer() {
        if (serverName == null || serverName.isEmpty()) {
            setMessage("이름을 설정해 주십시오.");
            return null;
        }
        ArrayList<String> temp = new ArrayList<>();

        temp.add(ServerMan.TCP_PORT);
        temp.add(getPort());
        if (isTcpAllowOthers()) {
            temp.add(ServerMan.TCP_ALLOW_OTHERS);
        }
        if (isIfNotExists()) {
            temp.add(ServerMan.IF_NOT_EXISTS);
        } else {
            temp.add(ServerMan.IF_EXISTS);
        }
        if (isTcpDaemon()) {
            temp.add(ServerMan.TCP_DAEMON);
        }
        if (getTcpPassword() != null && !getTcpPassword().isEmpty()) {
            temp.add(ServerMan.TCP_PASSWORD);
            temp.add(getTcpPassword());
        }

        args = new String[temp.size()];
        for (int i = 0; i < temp.size(); i++) {
            args[i] = temp.get(i);
        }
        try {
            server = Server.createTcpServer(args);
            setMessage("서버 생성 성공");
        } catch (SQLException ex) {
            server = null;
            setMessage("서버 생성 에러" + ex.getLocalizedMessage());
        }
        return server;
    }

    public boolean start() throws SQLException {
        if (!local) {
            message = "remote server can't start: " + serverName;
            return false;
        }
        if (server == null) {
            server = createServer();
        }
        try {
            server.start();
            tcpPort = String.valueOf(server.getPort());
            run = true;
            message = "server start: " + tcpPort;
        } catch (SQLException ex) {
            if (!isRun()) {
                message = "server start error: " + ex.getLocalizedMessage();
            }
        }
        return run;
    }

    public boolean stop() throws SQLException {
        if (server == null) {
            server = createServer();
        }
        int port = server.getPort();
        server.stop();
        run = false;
        message = "서버가 종료되었습니다: " + port;

        return true;
    }

    public boolean shutdown() throws SQLException {
        if (server == null) {
            server = createServer();
        }
        int port = server.getPort();
        Server.shutdownTcpServer("tcp://localhost:" + getPort(), getTcpPassword(), true, false);
        run = false;
        message = "서버가 종료되었습니다: " + port;

        return true;
    }

    public boolean isRun() {
        if (isLocal()) {
            return run;
        }
        if (run) {
            return run;
        }
        return isRemoteRun();
    }
    
    private boolean isRemoteRun() {
        Connection connection = getRemoteConnection();
        if (connection == null) {
            return false;
        }
        try {
            connection.close();
        } catch (SQLException ex) {
        }
        return true;
    }    
    
    public Connection getRemoteConnection() {
        if (databaseMen == null) {
            databaseMen = new DatabaseMen();
            return null;
        }
        if (databaseMen.isEmpty()) {
            return null;
        }
        Connection connection;
        try {
            connection = DriverManager.
                getConnection(makeRemoteJdbcUrl(), "sa", "");
            setMessage("connection 성공: " + makeRemoteJdbcUrl());
            return connection;
        } catch (SQLException ex) {
            setMessage(makeRemoteJdbcUrl() + " connection 실패: " + ex.getLocalizedMessage());
            return null;
        }        
    }    

    private String makeRemoteJdbcUrl() {
        if (databaseMen == null) {
            databaseMen = new DatabaseMen();
            return null;
        }
        if (databaseMen.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("jdbc:h2:tcp://");
        sb.append(getHostAddress());
        sb.append(":");
        sb.append(getPort());
        sb.append("/");
        sb.append(getBaseDir());
        sb.append("/");
        sb.append(databaseMen.get(0).getDatabaseName());
        
        return sb.toString();
    }

    public String getMessage() {
        return message;
    }

    void setMessage(String message) {
        this.message = message;
    }

    void setRun(boolean run) {
        this.run = run;
    }

    public String getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(String tcpPort) {
        this.tcpPort = tcpPort;
    }

    public void setTcpPort(int port) {
        tcpPort = String.valueOf(port);
    }

    public Server getServer() {
        return server;
    }

    public DatabaseMen getDatabaseMen() {
        return databaseMen;
    }

    public void setDatabaseMen(DatabaseMen databaseMen) {
        this.databaseMen = databaseMen;
    }
    
    public DatabaseMan addDatabase(String databaseName) {
        DatabaseMan databaseMan = new DatabaseMan(serverName, local, hostAddress, tcpPort, baseDir, databaseName);
        return addDatabase(databaseMan);
    }
    
    public DatabaseMan addDatabase(DatabaseMan databaseMan) {
        if (databaseMen == null) {
            databaseMen = new DatabaseMen();
        }
        boolean ok = databaseMen.add(databaseMan);
        if (ok) {
            message = databaseMan.getDatabaseName() + " 데이터베이스가 추가 되었습니다.";
        } else {
            message = databaseMan.getDatabaseName() + " 데이터베이스가 추가가 실패하였습니다.";
            databaseMan = null;
        }
        return databaseMan;
    }   

    public DatabaseMan findDatabaseMan(String serverName, String databaseName) {
        if (databaseMen == null) {
            databaseMen = new DatabaseMen();
        }
        return databaseMen.findByName(serverName, databaseName);
    }
    

    public DatabaseMan findDatabaseByName(String databaseName) {
        if (databaseMen == null) {
            databaseMen = new DatabaseMen();
        }
        return databaseMen.findByName(serverName, databaseName);
    }

    void remoteStop() throws SQLException {
        // no password: ""
        String password = getTcpPassword();
        if (password == null) {
            password = "";
        }
        Server.shutdownTcpServer("tcp://" + hostAddress + ":" + getPort(), password, true, false);
    }
}
