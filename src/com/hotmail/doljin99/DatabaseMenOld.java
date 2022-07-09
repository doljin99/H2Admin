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
public class DatabaseMenOld extends ArrayList<DatabaseManOld> {
    
    private String message;

    @Override
    public boolean add(DatabaseManOld databaseMan) {
        if (databaseMan == null) {
            message = "추가할 대상이 없습니다.";
            return false;
        }
        String serverName = databaseMan.getServerName();
        if (serverName == null || serverName.isEmpty()) {
            message = "서버 이름이 없습니다.";
            return false;
        }
        String databaseName = databaseMan.getDatabaseName();
        if (databaseName == null || databaseName.isEmpty()) {
            message = "데이터베이스 이름이 없습니다.";
            return false;
        }
        if (duplicatedName(serverName, databaseName)) {
            message = "중복된 데이터베이스입니다.";
            return false;
        }
        String port = databaseMan.getPort();
        if (port == null || port.isEmpty()) {
            message = "port 번호가 없습니다.";
            return false;
        }

        return super.add(databaseMan);
    }

    private boolean duplicatedName(String serverName, String dbName) {
        for (DatabaseManOld databaseMan : this) {
            if (serverName.equalsIgnoreCase(databaseMan.getServerName()) && dbName.equalsIgnoreCase(databaseMan.getDatabaseName())) {
                return true;
            }
        }
        return false;
    }

    public DatabaseManOld findByName(String serverName, String databaseName) {
        for (DatabaseManOld databaseMan : this) {
            if (databaseMan.getServerName().equalsIgnoreCase(serverName) && databaseMan.getDatabaseName().equalsIgnoreCase(databaseName)) {
                return databaseMan;
            }
        }
        return null;
    }

    public int getIndex(String serverName, String databaseName) {
        for (int i = 0; i < this.size(); i++) {
            DatabaseManOld databaseMan = this.get(i);
            if (databaseMan.getServerName().equalsIgnoreCase(serverName) &&  databaseMan.getDatabaseName().equalsIgnoreCase(databaseName)) {
                return i;
            }
        }
        return -1;
    }

    public String getMessage() {
        return message;
    }
}
