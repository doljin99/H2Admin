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
public class DatabaseMen extends ArrayList<DatabaseMan> {
    
    private transient String message;

    public boolean addNew(DatabaseMan databaseMan) {
        if (databaseMan == null) {
            message = "추가할 대상이 없습니다.";
            return false;
        }
        String databaseName = databaseMan.getDatabaseName();
        if (databaseName == null || databaseName.isEmpty()) {
            message = "데이터베이스 이름이 없습니다.";
            return false;
        }
        if (duplicatedName(databaseName)) {
            message = "중복된 데이터베이스입니다.";
            return false;
        }

        return super.add(databaseMan);
    }

    private boolean duplicatedName(String dbName) {
        for (DatabaseMan databaseMan : this) {
            if (dbName.equalsIgnoreCase(databaseMan.getDatabaseName())) {
                return true;
            }
        }
        return false;
    }

    public DatabaseMan findByName(String databaseName) {
        for (DatabaseMan databaseMan : this) {
            if (databaseMan.getDatabaseName().equalsIgnoreCase(databaseName)) {
                return databaseMan;
            }
        }
        return null;
    }

    public int getIndex(String databaseName) {
        for (int i = 0; i < this.size(); i++) {
            DatabaseMan databaseMan = this.get(i);
            if (databaseMan.getDatabaseName().equalsIgnoreCase(databaseName)) {
                return i;
            }
        }
        return -1;
    }

    public String getMessage() {
        return message;
    }
}
