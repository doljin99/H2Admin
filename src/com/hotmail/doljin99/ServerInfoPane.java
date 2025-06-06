/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.hotmail.doljin99;

/**
 *
 * @author dolji
 */
public class ServerInfoPane extends javax.swing.JPanel {

    private ServerMan serverMan;

    public ServerInfoPane(ServerMan serverMan) {
        this.serverMan = serverMan;
        initComponents();

        init();
    }

    private void init() {
        if (serverMan == null) {
            jTextFieldName.setText("해당 서버 정보가 없습니다.");
            return;
        }
        jTextFieldName.setText(serverMan.getServerName());
        if (serverMan.isLocal()) {
            jRadioButtonLocal.setSelected(true);
        } else {
            jRadioButtonremote.setSelected(true);
        }        
        jTextFieldHostAddress.setText(serverMan.getHostAddress());
        jTextFieldPort.setText(serverMan.getPort());
        jTextFieldBaseDir.setText(serverMan.getBaseDir());
        jTextFieldRootUser.setText(serverMan.getRootUser());
        jPasswordFieldRootPassword.setText(serverMan.getRootPassword());
        jCheckBoxRun.setSelected(serverMan.isRun());
        jCheckBoxIfNotExists.setSelected(serverMan.isIfNotExists());
        jCheckBoxTcpAllowOthers.setSelected(serverMan.isTcpAllowOthers());
        jCheckBoxTcpDaemon.setSelected(serverMan.isTcpDaemon());
        jPasswordFieldTcpPassword.setText(serverMan.getTcpPassword());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupLocation = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldPort = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldHostAddress = new javax.swing.JTextField();
        jCheckBoxTcpAllowOthers = new javax.swing.JCheckBox();
        jCheckBoxIfNotExists = new javax.swing.JCheckBox();
        jCheckBoxTcpDaemon = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        jPasswordFieldTcpPassword = new javax.swing.JPasswordField();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldBaseDir = new javax.swing.JTextField();
        jCheckBoxRun = new javax.swing.JCheckBox();
        jCheckBoxVisibleTcp = new javax.swing.JCheckBox();
        jRadioButtonLocal = new javax.swing.JRadioButton();
        jRadioButtonremote = new javax.swing.JRadioButton();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldRootUser = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jPasswordFieldRootPassword = new javax.swing.JPasswordField();
        jCheckBoxVisibleRoot = new javax.swing.JCheckBox();

        jLabel1.setText("서버 이름");

        jTextFieldName.setEditable(false);

        jLabel2.setText("port");

        jTextFieldPort.setEditable(false);

        jLabel5.setText("서버 주소");

        jTextFieldHostAddress.setEditable(false);

        jCheckBoxTcpAllowOthers.setText("tcpAllowOthers");
        jCheckBoxTcpAllowOthers.setEnabled(false);

        jCheckBoxIfNotExists.setText("ifNotExists");
        jCheckBoxIfNotExists.setEnabled(false);

        jCheckBoxTcpDaemon.setText("tcpDaemon");
        jCheckBoxTcpDaemon.setEnabled(false);

        jLabel3.setText("tcp 패스워드");

        jPasswordFieldTcpPassword.setEditable(false);

        jLabel4.setText("base dir");

        jTextFieldBaseDir.setEditable(false);

        jCheckBoxRun.setText("run");
        jCheckBoxRun.setEnabled(false);

        jCheckBoxVisibleTcp.setText("보이기");
        jCheckBoxVisibleTcp.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxVisibleTcpItemStateChanged(evt);
            }
        });

        buttonGroupLocation.add(jRadioButtonLocal);
        jRadioButtonLocal.setSelected(true);
        jRadioButtonLocal.setText("local");
        jRadioButtonLocal.setEnabled(false);

        buttonGroupLocation.add(jRadioButtonremote);
        jRadioButtonremote.setText("remote");
        jRadioButtonremote.setEnabled(false);

        jLabel6.setText("root 사용자");

        jTextFieldRootUser.setEditable(false);

        jLabel7.setText("root 패스워드");

        jPasswordFieldRootPassword.setEditable(false);

        jCheckBoxVisibleRoot.setText("보이기");
        jCheckBoxVisibleRoot.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxVisibleRootItemStateChanged(evt);
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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(23, 23, 23)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPasswordFieldTcpPassword)
                                .addGap(18, 18, 18)
                                .addComponent(jCheckBoxVisibleTcp))
                            .addComponent(jTextFieldBaseDir)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGap(40, 40, 40)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldHostAddress, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextFieldPort)
                            .addComponent(jTextFieldName)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jRadioButtonLocal)
                                .addGap(18, 18, 18)
                                .addComponent(jRadioButtonremote))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jCheckBoxRun)
                                .addGap(18, 18, 18)
                                .addComponent(jCheckBoxTcpAllowOthers)
                                .addGap(18, 18, 18)
                                .addComponent(jCheckBoxIfNotExists)
                                .addGap(18, 18, 18)
                                .addComponent(jCheckBoxTcpDaemon)))
                        .addGap(0, 82, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(30, 30, 30)
                        .addComponent(jTextFieldRootUser))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(jPasswordFieldRootPassword)
                        .addGap(18, 18, 18)
                        .addComponent(jCheckBoxVisibleRoot)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButtonLocal)
                    .addComponent(jRadioButtonremote))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextFieldHostAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jPasswordFieldTcpPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jCheckBoxVisibleTcp))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextFieldBaseDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jTextFieldRootUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jPasswordFieldRootPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBoxVisibleRoot))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBoxRun)
                    .addComponent(jCheckBoxTcpAllowOthers)
                    .addComponent(jCheckBoxIfNotExists)
                    .addComponent(jCheckBoxTcpDaemon))
                .addContainerGap(16, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBoxVisibleTcpItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxVisibleTcpItemStateChanged
        if (jCheckBoxVisibleTcp.isSelected()) {
            jPasswordFieldTcpPassword.setEchoChar((char) 0);
        } else {
            jPasswordFieldTcpPassword.setEchoChar('*');
        }
    }//GEN-LAST:event_jCheckBoxVisibleTcpItemStateChanged

    private void jCheckBoxVisibleRootItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxVisibleRootItemStateChanged
        if (jCheckBoxVisibleRoot.isSelected()) {
            jPasswordFieldRootPassword.setEchoChar((char) 0);
        } else {
            jPasswordFieldRootPassword.setEchoChar('*');
        }
    }//GEN-LAST:event_jCheckBoxVisibleRootItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupLocation;
    private javax.swing.JCheckBox jCheckBoxIfNotExists;
    private javax.swing.JCheckBox jCheckBoxRun;
    private javax.swing.JCheckBox jCheckBoxTcpAllowOthers;
    private javax.swing.JCheckBox jCheckBoxTcpDaemon;
    private javax.swing.JCheckBox jCheckBoxVisibleRoot;
    private javax.swing.JCheckBox jCheckBoxVisibleTcp;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPasswordField jPasswordFieldRootPassword;
    private javax.swing.JPasswordField jPasswordFieldTcpPassword;
    private javax.swing.JRadioButton jRadioButtonLocal;
    private javax.swing.JRadioButton jRadioButtonremote;
    private javax.swing.JTextField jTextFieldBaseDir;
    private javax.swing.JTextField jTextFieldHostAddress;
    private javax.swing.JTextField jTextFieldName;
    private javax.swing.JTextField jTextFieldPort;
    private javax.swing.JTextField jTextFieldRootUser;
    // End of variables declaration//GEN-END:variables
}
