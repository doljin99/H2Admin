/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotmail.doljin99;

/**
 *
 * @author dolji
 */
public class TablePrivileges {
    
    private final String tableName;
    private final String tableType;

    private boolean insertPrivileges;
    private boolean selectPrivileges;
    private boolean updatePrivileges;
    private boolean deletePrivileges;

    public TablePrivileges(String tableName, String tableType) {
        this.tableName = tableName;
        this.tableType = tableType;
    }

    public String getTableName() {
        return tableName;
    }

    public String getTableType() {
        return tableType;
    }

    public void setRightsAll(boolean grant) {
        insertPrivileges = grant;
        selectPrivileges = grant;
        updatePrivileges = grant;
        deletePrivileges = grant;
    }

    public boolean isInsertPrivileges() {
        return insertPrivileges;
    }

    public void setInsertPrivileges(boolean InsertPrivileges) {
        this.insertPrivileges = InsertPrivileges;
    }

    public boolean isSelectPrivileges() {
        return selectPrivileges;
    }

    public void setSelectPrivileges(boolean SelectPrivileges) {
        this.selectPrivileges = SelectPrivileges;
    }

    public boolean isUpdatePrivileges() {
        return updatePrivileges;
    }

    public void setUpdatePrivileges(boolean updatePrivileges) {
        this.updatePrivileges = updatePrivileges;
    }

    public boolean isDeletePrivileges() {
        return deletePrivileges;
    }

    public void setDeletePrivileges(boolean deletePrivileges) {
        this.deletePrivileges = deletePrivileges;
    }
}
