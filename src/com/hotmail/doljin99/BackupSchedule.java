package com.hotmail.doljin99;


import com.hotmail.doljin99.loginmanager.LoginManager;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author dolji
 */
public class BackupSchedule {

    private transient String serverName;
    private String serverName_enc;
    private transient String cronName;
    private String cronName_enc;
    private String minPart;
    private String hourPart;
    private String domPart;
    private String monthPart;
    private String dowPart;
    private long startDate;
    private long endDate;
    private transient String databaseName;
    private String databaseName_enc;
    private transient String backupDir;
    private String backupDir_enc;
    private transient String backupFile;
    private String backupFile_enc;
    private int retryLimit;
    private long entryDatetime;
    private transient String remark;
    private String remark_enc;

    public String getServerName() {
        return serverName;
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

    public String getCronName() {
        return cronName;
    }

    public void setCronName(String cronName) {
        this.cronName = cronName;
    }

    public String getCronName_enc() {
        return cronName_enc;
    }

    public void setCronName_enc(String cronName_enc) {
        this.cronName_enc = cronName_enc;
    }

    public String getMinPart() {
        return minPart;
    }

    public void setMinPart(String minPart) {
        this.minPart = minPart;
    }

    public String getHourPart() {
        return hourPart;
    }

    public void setHourPart(String hourPart) {
        this.hourPart = hourPart;
    }

    public String getDayOfMonthPart() {
        return domPart;
    }

    public void setDayOfMonthPart(String domPart) {
        this.domPart = domPart;
    }

    public String getMonthPart() {
        return monthPart;
    }

    public void setMonthPart(String monthPart) {
        this.monthPart = monthPart;
    }

    public String getDayOfWeekPart() {
        return dowPart;
    }

    public void setDayOfWeekPart(String dowPart) {
        this.dowPart = dowPart;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDatabaseName_enc() {
        return databaseName_enc;
    }

    public void setDatabaseName_enc(String databaseName_enc) {
        this.databaseName_enc = databaseName_enc;
    }

    public String getBackupDir() {
        return backupDir;
    }

    public void setBackupDir(String backupDir) {
        this.backupDir = backupDir;
    }

    public String getBackupDir_enc() {
        return backupDir_enc;
    }

    public void setBackupDir_enc(String backupDir_enc) {
        this.backupDir_enc = backupDir_enc;
    }

    public String getBackupFile() {
        return backupFile;
    }

    public void setBackupFile(String backupFile) {
        this.backupFile = backupFile;
    }

    public String getBackupFile_enc() {
        return backupFile_enc;
    }

    public void setBackupFile_enc(String backupFile_enc) {
        this.backupFile_enc = backupFile_enc;
    }

    public int getRetryLimit() {
        return retryLimit;
    }

    public void setRetryLimit(int retryLimit) {
        this.retryLimit = retryLimit;
    }

    public long getEntryDatetime() {
        return entryDatetime;
    }

    public void setEntryDatetime(long entryDatetime) {
        this.entryDatetime = entryDatetime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark_enc() {
        return remark_enc;
    }

    public void setRemark_enc(String remark_enc) {
        this.remark_enc = remark_enc;
    }

    public void decryptFields(LoginManager loginManager) {
        serverName = loginManager.decrypt(serverName_enc);
        cronName = loginManager.decrypt(cronName_enc);
        databaseName = loginManager.decrypt(databaseName_enc);
        backupDir = loginManager.decrypt(backupDir_enc);
        backupFile = loginManager.decrypt(backupFile_enc);
        remark = loginManager.decrypt(remark_enc);
    }

    public void encryptFields(LoginManager loginManager) {
        serverName_enc = loginManager.encrypt(serverName);
        cronName_enc = loginManager.encrypt(cronName);
        databaseName_enc = loginManager.encrypt(databaseName);
        backupDir_enc = loginManager.encrypt(backupDir);
        backupFile_enc = loginManager.encrypt(backupFile);
        remark_enc = loginManager.encrypt(remark);
    }
}
