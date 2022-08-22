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
public class UserInfo {    
    private String serverName;
    private String userId;
    private String userPassword;
    private boolean asministrator;
    private boolean grantAll;
    private boolean grantInsert;
    private boolean grantSelect;
    private boolean grantUpdate;
    private boolean grantDelete;
    private boolean schemaAll;
    private String remark;
    
    private ArrayList<TablePrivileges> schemaGrants;

    public ArrayList<TablePrivileges> getSchemaGrants() {
        return schemaGrants;
    }

    public void setSchemaGrants(ArrayList<TablePrivileges> schemaGrants) {
        this.schemaGrants = schemaGrants;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public boolean isAsministrator() {
        return asministrator;
    }

    public void setAsministrator(boolean asministrator) {
        this.asministrator = asministrator;
    }

    public boolean isGrantAll() {
        return grantAll;
    }

    public void setGrantAll(boolean grantAll) {
        this.grantAll = grantAll;
    }

    public boolean isGrantInsert() {
        return grantInsert;
    }

    public void setGrantInsert(boolean grantInsert) {
        this.grantInsert = grantInsert;
    }

    public boolean isGrantSelect() {
        return grantSelect;
    }

    public void setGrantSelect(boolean grantSelect) {
        this.grantSelect = grantSelect;
    }

    public boolean isGrantUpdate() {
        return grantUpdate;
    }

    public void setGrantUpdate(boolean grantUpdate) {
        this.grantUpdate = grantUpdate;
    }

    public boolean isGrantDelete() {
        return grantDelete;
    }

    public void setGrantDelete(boolean grantDelete) {
        this.grantDelete = grantDelete;
    }

    public boolean isSchemaAll() {
        return schemaAll;
    }

    public void setSchemaAll(boolean schemaAll) {
        this.schemaAll = schemaAll;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    
}
