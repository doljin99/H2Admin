/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotmail.doljin99;

import java.util.HashMap;

/**
 *
 * @author dolji
 */
public class BackupSchesuduleMap extends HashMap<String, BackupSchedule> {

    public BackupSchesuduleMap() {
        super();
    }
    
    public BackupSchedule add(BackupSchedule backupSchedule) {
        String key = makeKey(backupSchedule);
        if (containsKey(key)) {
            return null;
        }
        
        return put(key, backupSchedule);
    }
    
    private String makeKey(BackupSchedule backupSchedule) {
        if (backupSchedule == null || backupSchedule.getServerName() == null || backupSchedule.getCronName() == null) {
            return null;
        }
        
        return backupSchedule.getServerName().toUpperCase() + ":" + backupSchedule.getCronName().toUpperCase();
    }
}
