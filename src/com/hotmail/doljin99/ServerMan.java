/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotmail.doljin99;

import com.hotmail.doljin99.loginmanager.LoginManager;
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

    private transient String serverName;
    private String serverName_enc;

    private boolean local;

    private transient String hostAddress;
    private String hostAddress_enc;

    private transient String tcpPort;
    private String tcpPort_enc;

    private boolean tcpAllowOthers;
    private boolean ifNotExists;
    private boolean tcpDaemon;

    private transient String tcpPassword;
    private String tcpPassword_enc;

    private transient String baseDir;
    private String baseDir_enc;

    private transient String rootUser;
    private String rootUser_enc;
    private transient String rootPassword;
    private String rootPassword_enc;

    private DatabaseMen databaseMen;

    private transient String[] args = {
        TCP_PORT, tcpPort, TCP_ALLOW_OTHERS, IF_NOT_EXISTS, TCP_DAEMON, TCP_PASSWORD, tcpPassword
    };

    private transient Server server;
    private transient boolean run;
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
        if (isLocal()) {
            return localStop();
        } else {
            return remoteStop();
        }
    }

    public boolean localStop() throws SQLException {
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
        Server.shutdownTcpServer("tcp://" + hostAddress + ":" + getPort(), getTcpPassword(), true, false);
        run = false;
        message = serverName + " 서버가 종료되었습니다: " + port;

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

    public String getRootUser() {
        return rootUser;
    }

    public void setRootUser(String rootUser) {
        this.rootUser = rootUser;
    }

    public String getRootPassword() {
        return rootPassword;
    }

    public void setRootPassword(String rootPassword) {
        this.rootPassword = rootPassword;
    }

    public DatabaseMen getDatabaseMen() {
        return databaseMen;
    }

    public void setDatabaseMen(DatabaseMen databaseMen) {
        this.databaseMen = databaseMen;
    }

    public DatabaseMan addDatabase(String databaseName) {
        DatabaseMan databaseMan = new DatabaseMan(databaseName);
        return addDatabase(databaseMan);
    }

    public DatabaseMan addDatabase(DatabaseMan databaseMan) {
        if (databaseMen == null) {
            databaseMen = new DatabaseMen();
        }
        boolean ok = databaseMen.addNew(databaseMan);
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
        return databaseMen.findByName(databaseName);
    }

    public DatabaseMan findDatabaseByName(String databaseName) {
        if (databaseMen == null) {
            databaseMen = new DatabaseMen();
        }
        return databaseMen.findByName(databaseName);
    }

    public Connection getConnection(String databaseName) {
        if (rootUser == null || rootUser.isEmpty()) {
            rootUser = "SA";
        }
        if (rootPassword == null) {
            rootPassword = "";
        }
        Connection connection;
        try {
            connection = DriverManager.
                getConnection(getJdbcUrl(databaseName), rootUser, rootPassword);
            setMessage("connection 성공: " + getJdbcUrl(databaseName));
            return connection;
        } catch (SQLException ex) {
            setMessage(getJdbcUrl(databaseName) + " connection 실패: " + ex.getLocalizedMessage());
            return null;
        }
    }

    public String getJdbcUrl(String databaseName) {
        StringBuilder sb = new StringBuilder();
        sb.append("jdbc:h2:tcp://");
        sb.append(getHostAddress());
        sb.append(":");
        sb.append(getPort());
        sb.append("/");
        sb.append(getBaseDir());
        sb.append("/");
        sb.append(databaseName);

        return sb.toString();
    }

    public boolean connectionTest(String databaseName) {
        Connection conn = null;
        try {
            conn = DriverManager.
                getConnection(getJdbcUrl(databaseName), rootUser, rootPassword);
            setMessage("connection 성공: " + getJdbcUrl(databaseName));
            return true;
        } catch (SQLException ex) {
            setMessage(getJdbcUrl(databaseName) + "connection 실패: " + ex.getLocalizedMessage());
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

    boolean remoteStop() {
        // no password: ""
        String password = getTcpPassword();
        if (password == null) {
            password = "";
        }
        try {
            Server.shutdownTcpServer("tcp://" + hostAddress + ":" + getPort(), password, true, true);
            message = ("정지된 remote " + serverName + " 서버: " + hostAddress + ":" + getPort());
            run = false;
            return true;
        } catch (SQLException ex) {
            message = "" + ex.getLocalizedMessage();
            return false;
        }
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerName_enc() {
        return serverName_enc;
    }

    public void setServerName_enc(String serverName_enc) {
        this.serverName_enc = serverName_enc;
    }

    public String getHostAddress_enc() {
        return hostAddress_enc;
    }

    public void setHostAddress_enc(String hostAddress_enc) {
        this.hostAddress_enc = hostAddress_enc;
    }

    public String getTcpPort_enc() {
        return tcpPort_enc;
    }

    public void setTcpPort_enc(String tcpPort_enc) {
        this.tcpPort_enc = tcpPort_enc;
    }

    public String getTcpPassword_enc() {
        return tcpPassword_enc;
    }

    public void setTcpPassword_enc(String tcpPassword_enc) {
        this.tcpPassword_enc = tcpPassword_enc;
    }

    public String getBaseDir_enc() {
        return baseDir_enc;
    }

    public void setBaseDir_enc(String baseDir_enc) {
        this.baseDir_enc = baseDir_enc;
    }

    protected void decryptFields(LoginManager loginManager) {
        serverName = loginManager.decrypt(serverName_enc);
        hostAddress = loginManager.decrypt(hostAddress_enc);
        tcpPort = loginManager.decrypt(tcpPort_enc);
        baseDir = loginManager.decrypt(baseDir_enc);
        tcpPassword = loginManager.decrypt(tcpPassword_enc);
        rootUser = loginManager.decrypt(rootUser_enc);
        rootPassword = loginManager.decrypt(rootPassword_enc);
    }

    protected void encryptFields(LoginManager loginManager) {
        serverName_enc = loginManager.encrypt(serverName);
        hostAddress_enc = loginManager.encrypt(hostAddress);
        tcpPort_enc = loginManager.encrypt(tcpPort);
        baseDir_enc = loginManager.encrypt(baseDir);
        tcpPassword_enc = loginManager.encrypt(tcpPassword);
        rootUser_enc = loginManager.encrypt(rootUser);
        rootPassword_enc = loginManager.encrypt(rootPassword);
    }

    public String getRootUser_enc() {
        return rootUser_enc;
    }

    public void setRootUser_enc(String rootUser_enc) {
        this.rootUser_enc = rootUser_enc;
    }

    public String getRootPassword_enc() {
        return rootPassword_enc;
    }

    public void setRootPassword_enc(String rootPassword_enc) {
        this.rootPassword_enc = rootPassword_enc;
    }

    Connection getConnection() {
        if (databaseMen == null || databaseMen.isEmpty()) {
            return null;
        }
        return getConnection(databaseMen.get(0).getDatabaseName());
    }
}
