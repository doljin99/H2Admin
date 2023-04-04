/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.hotmail.doljin99;

import com.hotmail.doljin99.ScheduledBackupRunnable.ExecuteBackupPool;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author dolji
 */
public class BackupScheduleListDialog extends javax.swing.JDialog {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    private final ExecuteBackupPool pool;

    /**
     * Creates new form BackupScheduleListDialog
     *
     * @param parent
     * @param modal
     * @param pool
     */
    public BackupScheduleListDialog(java.awt.Frame parent, boolean modal, ExecuteBackupPool pool) {
        super(parent, modal);
        initComponents();
        this.pool = pool;

        init();
    }

    private void init() {
        ArrayList<BackupScheduleRow> list = new ArrayList<>();
        for (int i = 0; i < pool.size(); i++) {
            ScheduledBackupRunnable.ExecuteBackupScheduleRunnable runnable = pool.get(i);
            BackupScheduleRow row = new BackupScheduleRow();
            row.setServerName(runnable.getBackupSchedule().getServerName());
            row.setCronName(runnable.getBackupSchedule().getCronName());
            row.setDatabaseName(runnable.getBackupSchedule().getDatabaseName());
            row.setScheduleTime(runnable.getScheduleTime().format(DateTimeFormatter.ISO_DATE_TIME));
            row.setBackupDir(runnable.getBackupSchedule().getBackupDir());
            
            list.add(row);
        }
        Collections.sort(list, (BackupScheduleRow o1, BackupScheduleRow o2) -> {
            String time1 = o1.getScheduleTime();
            String time2 = o2.getScheduleTime();
            return time1.compareTo(time2);
        });

        DefaultTableModel model = (DefaultTableModel) jTableList.getModel();
        model.setRowCount(0);
        Object[] row = new Object[6];
        for (int i = 0; i < list.size(); i++) {
            BackupScheduleRow backupScheduleRow = list.get(i);
            row[0] = i + 1;
            row[1] = backupScheduleRow.getServerName();
            row[2] = backupScheduleRow.getCronName();
            row[3] = backupScheduleRow.getDatabaseName();
            row[4] = backupScheduleRow.getScheduleTime();
            row[5] = backupScheduleRow.getBackupDir();

            model.addRow(row);
        }
        H2AUtilities.alignColumnWidth(jTableList);
        jTableList.validate();
    }

    class BackupScheduleRow {

        private String serverName;
        private String cronName;
        private String databaseName;
        private String scheduleTime;
        private String backupDir;

        /**
         * Get the value of backupDir
         *
         * @return the value of backupDir
         */
        public String getBackupDir() {
            return backupDir;
        }

        /**
         * Set the value of backupDir
         *
         * @param backupDir new value of backupDir
         */
        public void setBackupDir(String backupDir) {
            this.backupDir = backupDir;
        }

        /**
         * Get the value of scheduleTime
         *
         * @return the value of scheduleTime
         */
        public String getScheduleTime() {
            return scheduleTime;
        }

        /**
         * Set the value of scheduleTime
         *
         * @param scheduleTime new value of scheduleTime
         */
        public void setScheduleTime(String scheduleTime) {
            this.scheduleTime = scheduleTime;
        }

        /**
         * Get the value of databaseName
         *
         * @return the value of databaseName
         */
        public String getDatabaseName() {
            return databaseName;
        }

        /**
         * Set the value of databaseName
         *
         * @param databaseName new value of databaseName
         */
        public void setDatabaseName(String databaseName) {
            this.databaseName = databaseName;
        }

        /**
         * Get the value of cronName
         *
         * @return the value of cronName
         */
        public String getCronName() {
            return cronName;
        }

        /**
         * Set the value of cronName
         *
         * @param cronName new value of cronName
         */
        public void setCronName(String cronName) {
            this.cronName = cronName;
        }

        public String getServerName() {
            return serverName;
        }

        public void setServerName(String serverName) {
            this.serverName = serverName;
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTableList = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("정기 백업 대기 목록");

        jTableList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "순번", "서버명", "정기 백업명", "데이터베이스명", "일시", "백업디렉터리"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableList);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableList;
    // End of variables declaration//GEN-END:variables
}
