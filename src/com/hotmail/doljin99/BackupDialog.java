/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.hotmail.doljin99;

import java.awt.Cursor;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.h2.tools.Backup;
import org.openide.util.Exceptions;

/**
 *
 * @author dolji
 */
public class BackupDialog extends javax.swing.JDialog {

    private final ServerMan serverMan;
    private final DatabaseMan databaseMan;

    private SimpleDateFormat dateFormat;
    private static final String EXTENSION = ".zip";

    private boolean done;
    private String message;

    /**
     * Creates new form BackupDialog
     *
     * @param parent
     * @param modal
     * @param serverMan
     * @param databaseMan
     */
    public BackupDialog(java.awt.Frame parent, boolean modal, ServerMan serverMan, DatabaseMan databaseMan) {
        super(parent, modal);
        initComponents();
        this.serverMan = serverMan;
        this.databaseMan = databaseMan;

        init();
    }

    private void init() {
        String onOff;
        if (serverMan.isRun()) {
            jRadioButtonOnline.setSelected(true);
            onOff = "ON";
        } else {
            jRadioButtonOffline.setSelected(true);
            onOff = "OFF";
        }
        setTitle(onOff + "Line Baclup");
        jTextFieldBaseDir.setText(serverMan.getBaseDir());
        if (databaseMan != null) {
            jRadioButtonSelectedDB.setSelected(true);
        } else {
            jRadioButtonAllDB.setSelected(true);
        }
        String dbName;
        if (databaseMan == null) {
            dbName = "ALL";
        } else {
            dbName = databaseMan.getDatabaseName();
        }

        dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

        jTextFieldBackupFileName.setText(makeBackupFilename(serverMan.getServerName(), dbName));
        jTextFieldBackupFileName.validate();

        done = false;
        message = "";

        jRadioButtonAllDB.validate();
        jRadioButtonOnline.validate();
        jRadioButtonOffline.validate();
        jRadioButtonOnlyOneDB.validate();
        jRadioButtonSelectedDB.validate();

        validate();
    }

    private String makeFileNamePrefix(String serverName, String dbName, String onOff) {
        StringBuilder sb = new StringBuilder();
        sb.append(serverName).append("-");
        if (dbName == null) {
            sb.append("ALL");
        } else {
            sb.append(dbName);
        }
        sb.append("-").append(onOff).append("-");
        return sb.toString();
    }

    public String getMessage() {
        return message;
    }

    private String makeCurrentDateString() {
        return dateFormat.format(new Date(System.currentTimeMillis()));
    }

    private String makeBackupFilename(String serverName, String databaseName) {
        String directory = jTextFieldBackupDir.getText();
        if (directory == null || directory.isEmpty()) {
            directory = ".";
        }
        String onOff;
        if (serverMan.isRun()) {
            jRadioButtonOnline.setSelected(true);
            onOff = "ON";
        } else {
            jRadioButtonOffline.setSelected(true);
            onOff = "OFF";
        }
        return directory + File.separator 
            + makeFileNamePrefix(serverName, databaseName, onOff) 
            + makeCurrentDateString() 
            + EXTENSION;
    }
    
    private String makeBackupFilename(String directory, String serverName, String databaseName, String onOff) {
        return directory + File.separator + makeFileNamePrefix(serverName, databaseName, onOff) + makeCurrentDateString() + EXTENSION;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupDBSelect = new javax.swing.ButtonGroup();
        buttonGroupOnOffline = new javax.swing.ButtonGroup();
        jToolBar1 = new javax.swing.JToolBar();
        jButtonBackup = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jButtonCancel = new javax.swing.JButton();
        jPanelOffline = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldBaseDir = new javax.swing.JTextField();
        jRadioButtonOnline = new javax.swing.JRadioButton();
        jRadioButtonOffline = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        jRadioButtonAllDB = new javax.swing.JRadioButton();
        jRadioButtonSelectedDB = new javax.swing.JRadioButton();
        jRadioButtonOnlyOneDB = new javax.swing.JRadioButton();
        jButtonBackupDir = new javax.swing.JButton();
        jTextFieldBackupDir = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldBackupFileName = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaStatus = new javax.swing.JTextArea();

        jToolBar1.setRollover(true);

        jButtonBackup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/SaveAll16.gif"))); // NOI18N
        jButtonBackup.setText(org.openide.util.NbBundle.getMessage(BackupDialog.class, "BackupDialog.jButtonBackup.text")); // NOI18N
        jButtonBackup.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonBackup.setEnabled(false);
        jButtonBackup.setFocusable(false);
        jButtonBackup.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonBackup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBackupActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonBackup);
        jToolBar1.add(jSeparator1);

        jButtonCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Stop16.gif"))); // NOI18N
        jButtonCancel.setText(org.openide.util.NbBundle.getMessage(BackupDialog.class, "BackupDialog.jButtonCancel.text")); // NOI18N
        jButtonCancel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonCancel.setFocusable(false);
        jButtonCancel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonCancel);

        getContentPane().add(jToolBar1, java.awt.BorderLayout.NORTH);

        jLabel1.setText(org.openide.util.NbBundle.getMessage(BackupDialog.class, "BackupDialog.jLabel1.text")); // NOI18N

        jTextFieldBaseDir.setEditable(false);
        jTextFieldBaseDir.setText(org.openide.util.NbBundle.getMessage(BackupDialog.class, "BackupDialog.jTextFieldBaseDir.text")); // NOI18N

        buttonGroupOnOffline.add(jRadioButtonOnline);
        jRadioButtonOnline.setSelected(true);
        jRadioButtonOnline.setText(org.openide.util.NbBundle.getMessage(BackupDialog.class, "BackupDialog.jRadioButtonOnline.text")); // NOI18N
        jRadioButtonOnline.setEnabled(false);

        buttonGroupOnOffline.add(jRadioButtonOffline);
        jRadioButtonOffline.setText(org.openide.util.NbBundle.getMessage(BackupDialog.class, "BackupDialog.jRadioButtonOffline.text")); // NOI18N
        jRadioButtonOffline.setEnabled(false);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 204)), org.openide.util.NbBundle.getMessage(BackupDialog.class, "BackupDialog.jPanel2.border.title"))); // NOI18N

        buttonGroupDBSelect.add(jRadioButtonAllDB);
        jRadioButtonAllDB.setSelected(true);
        jRadioButtonAllDB.setText(org.openide.util.NbBundle.getMessage(BackupDialog.class, "BackupDialog.jRadioButtonAllDB.text")); // NOI18N
        jRadioButtonAllDB.setToolTipText(org.openide.util.NbBundle.getMessage(BackupDialog.class, "BackupDialog.jRadioButtonAllDB.toolTipText")); // NOI18N
        jRadioButtonAllDB.setEnabled(false);

        buttonGroupDBSelect.add(jRadioButtonSelectedDB);
        jRadioButtonSelectedDB.setText(org.openide.util.NbBundle.getMessage(BackupDialog.class, "BackupDialog.jRadioButtonSelectedDB.text")); // NOI18N
        jRadioButtonSelectedDB.setToolTipText(org.openide.util.NbBundle.getMessage(BackupDialog.class, "BackupDialog.jRadioButtonSelectedDB.toolTipText")); // NOI18N
        jRadioButtonSelectedDB.setEnabled(false);

        buttonGroupDBSelect.add(jRadioButtonOnlyOneDB);
        jRadioButtonOnlyOneDB.setText(org.openide.util.NbBundle.getMessage(BackupDialog.class, "BackupDialog.jRadioButtonOnlyOneDB.text")); // NOI18N
        jRadioButtonOnlyOneDB.setToolTipText(org.openide.util.NbBundle.getMessage(BackupDialog.class, "BackupDialog.jRadioButtonOnlyOneDB.toolTipText")); // NOI18N
        jRadioButtonOnlyOneDB.setActionCommand(org.openide.util.NbBundle.getMessage(BackupDialog.class, "BackupDialog.jRadioButtonOnlyOneDB.actionCommand")); // NOI18N
        jRadioButtonOnlyOneDB.setEnabled(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jRadioButtonAllDB)
                .addGap(18, 18, 18)
                .addComponent(jRadioButtonSelectedDB)
                .addGap(18, 18, 18)
                .addComponent(jRadioButtonOnlyOneDB)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButtonAllDB)
                    .addComponent(jRadioButtonSelectedDB)
                    .addComponent(jRadioButtonOnlyOneDB))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButtonBackupDir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Import16.gif"))); // NOI18N
        jButtonBackupDir.setText(org.openide.util.NbBundle.getMessage(BackupDialog.class, "BackupDialog.jButtonBackupDir.text")); // NOI18N
        jButtonBackupDir.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonBackupDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBackupDirActionPerformed(evt);
            }
        });

        jTextFieldBackupDir.setEditable(false);
        jTextFieldBackupDir.setText(org.openide.util.NbBundle.getMessage(BackupDialog.class, "BackupDialog.jTextFieldBackupDir.text")); // NOI18N

        jLabel2.setText(org.openide.util.NbBundle.getMessage(BackupDialog.class, "BackupDialog.jLabel2.text")); // NOI18N

        jTextFieldBackupFileName.setEditable(false);
        jTextFieldBackupFileName.setText(org.openide.util.NbBundle.getMessage(BackupDialog.class, "BackupDialog.jTextFieldBackupFileName.text")); // NOI18N
        jTextFieldBackupFileName.setToolTipText(org.openide.util.NbBundle.getMessage(BackupDialog.class, "BackupDialog.jTextFieldBackupFileName.toolTipText")); // NOI18N

        javax.swing.GroupLayout jPanelOfflineLayout = new javax.swing.GroupLayout(jPanelOffline);
        jPanelOffline.setLayout(jPanelOfflineLayout);
        jPanelOfflineLayout.setHorizontalGroup(
            jPanelOfflineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelOfflineLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelOfflineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelOfflineLayout.createSequentialGroup()
                        .addComponent(jRadioButtonOnline)
                        .addGap(18, 18, 18)
                        .addComponent(jRadioButtonOffline)
                        .addGap(247, 395, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelOfflineLayout.createSequentialGroup()
                        .addGroup(jPanelOfflineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelOfflineLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(34, 34, 34)
                                .addComponent(jTextFieldBaseDir))
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelOfflineLayout.createSequentialGroup()
                                .addGroup(jPanelOfflineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButtonBackupDir)
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanelOfflineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldBackupDir)
                                    .addComponent(jTextFieldBackupFileName))))
                        .addGap(20, 20, 20))))
        );
        jPanelOfflineLayout.setVerticalGroup(
            jPanelOfflineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelOfflineLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelOfflineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButtonOnline)
                    .addComponent(jRadioButtonOffline))
                .addGap(18, 18, 18)
                .addGroup(jPanelOfflineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldBaseDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelOfflineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonBackupDir)
                    .addComponent(jTextFieldBackupDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelOfflineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextFieldBackupFileName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        getContentPane().add(jPanelOffline, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jTextAreaStatus.setColumns(20);
        jTextAreaStatus.setRows(5);
        jScrollPane1.setViewportView(jTextAreaStatus);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonBackupDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBackupDirActionPerformed
        File directory = H2AUtilities.chooseDirectory("H2 DB Backup 디렉터리 선택");
        if (directory == null) {
            setStatus("백업 디렉터리 선택을 취소하였습니다.");
            jButtonBackup.setEnabled(false);
            return;
        }
        jTextFieldBackupDir.setText(directory.getAbsolutePath());
        jTextFieldBackupDir.validate();
        
        String dbName;
        if (databaseMan == null) {
            dbName = "ALL";
        } else {
            dbName = databaseMan.getDatabaseName();
        }

        jTextFieldBackupFileName.setText(makeBackupFilename(serverMan.getServerName(), dbName));

        jButtonBackup.setEnabled(true);
    }//GEN-LAST:event_jButtonBackupDirActionPerformed

    private void jButtonBackupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBackupActionPerformed
        if (serverMan.isRun()) {
            onlineBackup();
        } else {
            offlineBackup();
        }
        setVisible(false);
    }//GEN-LAST:event_jButtonBackupActionPerformed

    private void onlineBackup() {
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        if (databaseMan != null) {
            onlineDatabaseBackup(databaseMan);
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            return;
        }
        DatabaseMen databaseMen = serverMan.getDatabaseMen();
        for (int i = 0; i < databaseMen.size(); i++) {
            DatabaseMan source = databaseMen.get(i);
            onlineDatabaseBackup(source);
//            System.out.println("source db = " + source.getDatabaseName());
        }
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    private void onlineDatabaseBackup(DatabaseMan databaseMan) {
        Connection connection = databaseMan.getConnection();
        if (connection == null) {
            setStatus("Connection 획득에 실패하여 online Backup을 실행할 수 없습니다: " + databaseMan.getDatabaseName());
            return;
        }
        Statement statement = null;
        try {
            String backupFileName = makeBackupFilename(serverMan.getServerName(), databaseMan.getDatabaseName());
            statement = connection.createStatement();
            statement.execute("BACKUP TO '" + backupFileName + "'");
            done = true;
            message += "백업이 실행되었습니다: " + backupFileName + "\n";
        } catch (SQLException ex) {
            setStatus("online 백업 중 에러: " + ex.getLocalizedMessage());
            message += "online 백업 중 에러: " + ex.getLocalizedMessage() + "\n";
            done = false;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                }
            }
        }
    }

    private void offlineBackup() {
        String dbName = null;
        String dbArg;
        if (jRadioButtonAllDB.isSelected()) {
            dbArg = "";
        } else if (jRadioButtonSelectedDB.isSelected()) {
            if (databaseMan != null) {
                dbArg = databaseMan.getDatabaseName();
                dbName = databaseMan.getDatabaseName();
            } else {
                dbArg = "";
            }
        } else {
            DatabaseMen databaseMen = serverMan.getDatabaseMen();
            if (databaseMen != null && serverMan.getDatabaseMen().size() == 1) {
                dbArg = null;
            } else {
                dbArg = "";
            }
        }
        try {
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            String backupFileName = makeBackupFilename(serverMan.getServerName(), dbName);
            String baseDir = jTextFieldBaseDir.getText();
            if (baseDir == null || baseDir.isEmpty()) {
                baseDir = ".";
            }
            Backup.execute(backupFileName, baseDir, dbArg, false);
            done = true;
            message = "offline 백업이 실행되었습니다: " + backupFileName;
        } catch (SQLException ex) {
            done = false;
            setStatus("offline 백업 중 에러: " + ex.getLocalizedMessage());
            message = "offline 백업 중 에러: " + ex.getLocalizedMessage();
            Exceptions.printStackTrace(ex);
        } finally {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        done = false;
        message = "백업을 취소하였습니다.";
        setVisible(false);
    }//GEN-LAST:event_jButtonCancelActionPerformed

    public boolean isDone() {
        return done;
    }

    private void setStatus(String msg) {
        jTextAreaStatus.append(msg + "\n");
        jTextAreaStatus.setCaretPosition(jTextAreaStatus.getText().length() - 1);
        jTextAreaStatus.validate();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupDBSelect;
    private javax.swing.ButtonGroup buttonGroupOnOffline;
    private javax.swing.JButton jButtonBackup;
    private javax.swing.JButton jButtonBackupDir;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelOffline;
    private javax.swing.JRadioButton jRadioButtonAllDB;
    private javax.swing.JRadioButton jRadioButtonOffline;
    private javax.swing.JRadioButton jRadioButtonOnline;
    private javax.swing.JRadioButton jRadioButtonOnlyOneDB;
    private javax.swing.JRadioButton jRadioButtonSelectedDB;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JTextArea jTextAreaStatus;
    private javax.swing.JTextField jTextFieldBackupDir;
    private javax.swing.JTextField jTextFieldBackupFileName;
    private javax.swing.JTextField jTextFieldBaseDir;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
