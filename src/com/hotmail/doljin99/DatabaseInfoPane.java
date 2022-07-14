/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.hotmail.doljin99;

/**
 *
 * @author dolji
 */
public class DatabaseInfoPane extends javax.swing.JPanel {

    private final ServerMan serverMan;
    private final DatabaseMan databaseMan;

    public DatabaseInfoPane(ServerMan serverMan, String databaseName) {
        this.serverMan = serverMan;
        initComponents();

        databaseMan = serverMan.findDatabaseByName(databaseName);
        init();
    }

    private void init() {
        if (serverMan == null) {
            jTextFieldName.setText("해당 서버 정보가 없습니다.");
            return;
        }
        if (databaseMan == null) {
            jTextFieldDatabaseName.setText("해당 DB 정보가 없습니다.");
            return;
        }
        jTextFieldName.setText(serverMan.getServerName());
        jTextFieldPort.setText(serverMan.getPort());
        jTextFieldBaseDir.setText(serverMan.getBaseDir());
        jCheckBoxRun.setSelected(serverMan.isRun());
        jCheckBoxIfNotExists.setSelected(serverMan.isIfNotExists());
        jCheckBoxTcpAllowOthers.setSelected(serverMan.isTcpAllowOthers());
        jCheckBoxTcpDaemon.setSelected(serverMan.isTcpDaemon());
        jPasswordFieldTcpPassword.setText(serverMan.getTcpPassword());
        jTextFieldDatabaseName.setText(databaseMan.getDatabaseName());
        jTextFieldJdbcUrl.setText(makeJdbcUrl());
    }

    private String makeJdbcUrl() {
        return serverMan.getJdbcUrl(databaseMan.getDatabaseName());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jTextFieldName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldPort = new javax.swing.JTextField();
        jCheckBoxTcpAllowOthers = new javax.swing.JCheckBox();
        jCheckBoxIfNotExists = new javax.swing.JCheckBox();
        jCheckBoxTcpDaemon = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        jPasswordFieldTcpPassword = new javax.swing.JPasswordField();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldBaseDir = new javax.swing.JTextField();
        jCheckBoxRun = new javax.swing.JCheckBox();
        jCheckBoxVisible = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldDatabaseName = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldJdbcUrl = new javax.swing.JTextField();
        jButtonCopy = new javax.swing.JButton();

        jLabel1.setText("서버 이름");

        jTextFieldName.setEditable(false);

        jLabel2.setText("port");

        jTextFieldPort.setEditable(false);

        jCheckBoxTcpAllowOthers.setText("tcpAllowOthers");
        jCheckBoxTcpAllowOthers.setEnabled(false);

        jCheckBoxIfNotExists.setText("ifNotExists");
        jCheckBoxIfNotExists.setEnabled(false);

        jCheckBoxTcpDaemon.setText("tcpDaemon");
        jCheckBoxTcpDaemon.setEnabled(false);

        jLabel3.setText("패스워드");

        jPasswordFieldTcpPassword.setEditable(false);

        jLabel4.setText("base dir");

        jTextFieldBaseDir.setEditable(false);

        jCheckBoxRun.setText("run");
        jCheckBoxRun.setEnabled(false);

        jCheckBoxVisible.setText("보이기");
        jCheckBoxVisible.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxVisibleItemStateChanged(evt);
            }
        });

        jLabel5.setText("DB 명");

        jTextFieldDatabaseName.setEditable(false);

        jLabel6.setText("JDBC Url");

        jTextFieldJdbcUrl.setEditable(false);

        jButtonCopy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Copy16.gif"))); // NOI18N
        jButtonCopy.setText("복사");
        jButtonCopy.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCopyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jCheckBoxRun)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBoxTcpAllowOthers)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBoxIfNotExists)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBoxTcpDaemon)
                        .addGap(17, 17, 17))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jTextFieldJdbcUrl)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButtonCopy))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jPasswordFieldTcpPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jCheckBoxVisible))
                                    .addComponent(jTextFieldDatabaseName, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jTextFieldBaseDir, javax.swing.GroupLayout.Alignment.TRAILING)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldName)
                                    .addComponent(jTextFieldPort))))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jTextFieldPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jCheckBoxRun)
                            .addComponent(jCheckBoxTcpAllowOthers)
                            .addComponent(jCheckBoxIfNotExists)
                            .addComponent(jCheckBoxTcpDaemon))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jPasswordFieldTcpPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCheckBoxVisible))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jTextFieldBaseDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5))
                    .addComponent(jTextFieldDatabaseName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextFieldJdbcUrl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonCopy))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBoxVisibleItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxVisibleItemStateChanged
        if (jCheckBoxVisible.isSelected()) {
            jPasswordFieldTcpPassword.setEchoChar((char) 0);
        } else {
            jPasswordFieldTcpPassword.setEchoChar('*');
        }
    }//GEN-LAST:event_jCheckBoxVisibleItemStateChanged

    private void jButtonCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCopyActionPerformed
        jTextFieldJdbcUrl.selectAll();
        jTextFieldJdbcUrl.copy();
    }//GEN-LAST:event_jButtonCopyActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCopy;
    private javax.swing.JCheckBox jCheckBoxIfNotExists;
    private javax.swing.JCheckBox jCheckBoxRun;
    private javax.swing.JCheckBox jCheckBoxTcpAllowOthers;
    private javax.swing.JCheckBox jCheckBoxTcpDaemon;
    private javax.swing.JCheckBox jCheckBoxVisible;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPasswordField jPasswordFieldTcpPassword;
    private javax.swing.JTextField jTextFieldBaseDir;
    private javax.swing.JTextField jTextFieldDatabaseName;
    private javax.swing.JTextField jTextFieldJdbcUrl;
    private javax.swing.JTextField jTextFieldName;
    private javax.swing.JTextField jTextFieldPort;
    // End of variables declaration//GEN-END:variables
}
