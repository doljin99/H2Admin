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
public class BackupSchedules extends ArrayList<BackupSchedule> {

    public BackupSchedules() {
        super();
    }
    
    public BackupSchedule addBackupSchedule(BackupSchedule backupSchedule) {
        if (!exist(backupSchedule)) {
            add(backupSchedule);
            return backupSchedule;
        }
        return null;
    }
    
    public BackupSchedule updateBackupSchedule(BackupSchedule backupSchedule) {
        if (!exist(backupSchedule)) {
            return null;
        }
        deleteSchedule(backupSchedule);
        add(backupSchedule);
        
        return backupSchedule;
    }
    
    public boolean exist(BackupSchedule backupSchedule) {
        String key = makeKey(backupSchedule);
        for (int i = 0; i < this.size(); i++) {
            BackupSchedule legacy = this.get(i);
            if (makeKey(legacy).equals(key)) {
                return true;
            }
        }
        return false;
    }
    
    public BackupSchedule getBackupSchedule(String serverName, String cronName) {
        String key = makeKey(serverName, cronName);
        for (int i = 0; i < this.size(); i++) {
            BackupSchedule legacy = this.get(i);
            if (makeKey(legacy).equals(key)) {
                return legacy;
            }
        }
        return null;
    }
    
    public boolean exist(String serverName, String cronName) {
        String key = makeKey(serverName,cronName);
        for (int i = 0; i < this.size(); i++) {
            BackupSchedule legacy = this.get(i);
            if (makeKey(legacy).equals(key)) {
                return true;
            }
        }
        return false;
    }
    
    public BackupSchedule deleteSchedule(BackupSchedule backupSchedule) {
        String key = makeKey(backupSchedule);
        for (int i = 0; i < this.size(); i++) {
            BackupSchedule legacy = this.get(i);
            if (makeKey(legacy).equals(key)) {
               remove(legacy);
               return legacy;
            }
        }
        return null;
    }

    private String makeKey(BackupSchedule backupSchedule) {
        if (backupSchedule == null || backupSchedule.getServerName() == null || backupSchedule.getCronName() == null) {
            return null;
        }

        return makeKey(backupSchedule.getServerName(), backupSchedule.getCronName());
    }
    
    public static String makeKey(String serverName, String cronName) {
        if (serverName == null || cronName == null) {
            return null;
        }

        return serverName.toUpperCase() + ":" + cronName.toUpperCase();
    }
    
    public void encryptFields(LoginManager loginManager) {
        for (BackupSchedule backupSchedule : this) {
            backupSchedule.encryptFields(loginManager);
        }
    }
    
    public void decryptFields(LoginManager loginManager) {
        for (BackupSchedule backupSchedule : this) {
            backupSchedule.decryptFields(loginManager);
        }
    }

}
