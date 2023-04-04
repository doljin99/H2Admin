/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotmail.doljin99;

import com.hotmail.doljin99.loginmanager.LoginManager;
import java.util.ArrayList;

/**
 *
 * @author dolji
 */
public class ServerMen {

    private ArrayList<ServerMan> serverMen;
    private BackupSchedules backupSchedules;
    private transient String message;

    public ServerMen() {
        this(new ArrayList<ServerMan>(), new BackupSchedules());
    }
    public ServerMen(ArrayList<ServerMan> serverMen, BackupSchedules backupSchedules) {
        this.serverMen = serverMen;
        this.backupSchedules = backupSchedules;
    }

    public boolean addServerMan(ServerMan serverMan) {
        if (serverMan == null) {
            message = "추가할 대상이 없습니다.";
            return false;
        }
        String name = serverMan.getServerName();
        if (name == null || name.isEmpty()) {
            message = "이름이 없습니다.";
            return false;
        }
        String port = serverMan.getPort();
        if (port == null || port.isEmpty()) {
            message = "port 번호가 없습니다.";
            return false;
        }
        if (duplicatedName(name)) {
            message = "중복된 이름입니다.";
            return false;
        }
        if (serverMan.isLocal()) {
            if (usedPort(port)) {
                message = "다른 서버에 설정된 port 번호입니다.";
                return false;
            }
            int portNo;
            try {
                portNo = Integer.parseInt(port);
                if (!H2AUtilities.isAvailablePort(portNo)) {
                    message = "사용 중인 port 번호입니다.";
                    return false;
                }
            } catch (NumberFormatException ex) {
                message = "사용 중인 port 번호입니다.";
                return false;
            }
        }
        if (serverMen == null) {
            serverMen = new ArrayList<>();
        }

        return serverMen.add(serverMan);
    }

    private boolean duplicatedName(String name) {
        for (ServerMan server : serverMen) {
            if (server.getServerName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    private boolean usedPort(String port) {
        for (ServerMan server : serverMen) {
            if (!server.isLocal()) {
                continue;
            }
            if (server.getPort().equalsIgnoreCase(port)) {
                return true;
            }
        }
        return false;
    }

    public ServerMan findByName(String name) {
        for (ServerMan server : serverMen) {
            if (server.getServerName().equalsIgnoreCase(name)) {
                return server;
            }
        }
        return null;
    }

    public int getIndex(String name) {
        for (int i = 0; i < serverMen.size(); i++) {
            ServerMan server = serverMen.get(i);
            if (server.getServerName().equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }

    public String getMessage() {
        return message;
    }

    ServerMan delete(String serverName) {
        int index = getIndex(serverName);
        if (index < 0) {
            message = serverName + " 서버가 없습니다.";
            return null;
        }
        return serverMen.remove(index);
    }    

    public void decryptServerMen(LoginManager loginManager) {
        for (ServerMan serverMan : serverMen) {
            serverMan.decryptFields(loginManager);
        }
        if (backupSchedules == null) {
            backupSchedules = new BackupSchedules();
        }
        backupSchedules.decryptFields(loginManager);
    }

    void encryptFields(LoginManager loginManager) {
        for (ServerMan serverMan : serverMen) {
            serverMan.encryptFields(loginManager);
        }
        if (backupSchedules == null) {
            backupSchedules = new BackupSchedules();
        }
        backupSchedules.encryptFields(loginManager);
    }

    public BackupSchedules getBackupSchedules() {
        if (backupSchedules == null) {
            backupSchedules = new BackupSchedules();
        }
        return backupSchedules;
    }

    public void setBackupSchedules(BackupSchedules backupSchedules) {
        this.backupSchedules = backupSchedules;
    }

    int getServerListSize() {
        if (serverMen == null) {
            serverMen = new ArrayList<>();
            return 0;
        }
        return serverMen.size();
    }

    ServerMan getServerMan(int i) {
        if (serverMen == null) {
            serverMen = new ArrayList<>();
        }
        return serverMen.get(i);
    }    
}
