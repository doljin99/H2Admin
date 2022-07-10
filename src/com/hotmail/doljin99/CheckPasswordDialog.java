/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.hotmail.doljin99;

import com.hotmail.doljin99.loginmanager.LoginManager;

/**
 *
 * @author dolji
 */
public class CheckPasswordDialog extends javax.swing.JDialog {

    private final LoginManager loginManager;
    private String password;

    /**
     * Creates new form CheckPasswordJDialog
     *
     * @param parent
     * @param modal
     * @param loginManager
     */
    public CheckPasswordDialog(java.awt.Frame parent, boolean modal, LoginManager loginManager) {
        super(parent, modal);
        initComponents();

        this.loginManager = loginManager;

        init();
    }

    private void init() {
        if (loginManager.installed()) {
            jPasswordFieldConfirm.setEnabled(false);
            jCheckBoxConfirm.setEnabled(false);
            setTitle("패스워드 입력");
        } else {
            setTitle("패스워드 신규 등록");
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

        jToolBar1 = new javax.swing.JToolBar();
        jButtonSave = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jButton2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPasswordFieldPassword = new javax.swing.JPasswordField();
        jCheckBoxPassword = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        jPasswordFieldConfirm = new javax.swing.JPasswordField();
        jCheckBoxConfirm = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaStatus = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(CheckPasswordDialog.class, "CheckPasswordDialog.title")); // NOI18N

        jToolBar1.setRollover(true);

        jButtonSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Save16.gif"))); // NOI18N
        jButtonSave.setText(org.openide.util.NbBundle.getMessage(CheckPasswordDialog.class, "CheckPasswordDialog.jButtonSave.text")); // NOI18N
        jButtonSave.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonSave.setFocusable(false);
        jButtonSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonSave);
        jToolBar1.add(jSeparator1);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Stop16.gif"))); // NOI18N
        jButton2.setText(org.openide.util.NbBundle.getMessage(CheckPasswordDialog.class, "CheckPasswordDialog.jButton2.text")); // NOI18N
        jButton2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton2.setFocusable(false);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        getContentPane().add(jToolBar1, java.awt.BorderLayout.NORTH);

        jLabel1.setText(org.openide.util.NbBundle.getMessage(CheckPasswordDialog.class, "CheckPasswordDialog.jLabel1.text")); // NOI18N

        jPasswordFieldPassword.setText(org.openide.util.NbBundle.getMessage(CheckPasswordDialog.class, "CheckPasswordDialog.jPasswordFieldPassword.text")); // NOI18N
        jPasswordFieldPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordFieldPasswordActionPerformed(evt);
            }
        });

        jCheckBoxPassword.setText(org.openide.util.NbBundle.getMessage(CheckPasswordDialog.class, "CheckPasswordDialog.jCheckBoxPassword.text")); // NOI18N
        jCheckBoxPassword.setEnabled(false);
        jCheckBoxPassword.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxPasswordItemStateChanged(evt);
            }
        });

        jLabel2.setText(org.openide.util.NbBundle.getMessage(CheckPasswordDialog.class, "CheckPasswordDialog.jLabel2.text")); // NOI18N

        jPasswordFieldConfirm.setText(org.openide.util.NbBundle.getMessage(CheckPasswordDialog.class, "CheckPasswordDialog.jPasswordFieldConfirm.text")); // NOI18N
        jPasswordFieldConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordFieldConfirmActionPerformed(evt);
            }
        });

        jCheckBoxConfirm.setText(org.openide.util.NbBundle.getMessage(CheckPasswordDialog.class, "CheckPasswordDialog.jCheckBoxConfirm.text")); // NOI18N
        jCheckBoxConfirm.setEnabled(false);
        jCheckBoxConfirm.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxConfirmItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPasswordFieldPassword)
                    .addComponent(jPasswordFieldConfirm, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBoxPassword)
                    .addComponent(jCheckBoxConfirm))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jPasswordFieldPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBoxPassword))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jPasswordFieldConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBoxConfirm))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jTextAreaStatus.setEditable(false);
        jTextAreaStatus.setColumns(20);
        jTextAreaStatus.setLineWrap(true);
        jTextAreaStatus.setRows(5);
        jTextAreaStatus.setWrapStyleWord(true);
        jScrollPane1.setViewportView(jTextAreaStatus);

        jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        submit();
    }//GEN-LAST:event_jButtonSaveActionPerformed

    private void submit() {
        password = new String(jPasswordFieldPassword.getPassword());
        if (password == null || password.isEmpty()) {
            setStatus("패스워드를 입력하세요.");
            return;
        }
        if (loginManager.installed()) {
            setVisible(false);
        }
        String confirm = new String(jPasswordFieldConfirm.getPassword());
        if (confirm == null || confirm.isEmpty()) {
            setStatus("확인용 패스워드를 입력하세요.");
            return;
        }
        if (!loginManager.validPasswordConfig(password, confirm)) {
            setStatus(loginManager.getMessage());
            return;
        }
        setVisible(false);
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        password = null;
        setVisible(false);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jCheckBoxPasswordItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxPasswordItemStateChanged
        if (jCheckBoxPassword.isSelected()) {
            jPasswordFieldPassword.setEchoChar((char) 0);
        } else {
            jPasswordFieldPassword.setEchoChar('*');
        }
    }//GEN-LAST:event_jCheckBoxPasswordItemStateChanged

    private void jCheckBoxConfirmItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxConfirmItemStateChanged
        if (jCheckBoxConfirm.isSelected()) {
            jPasswordFieldConfirm.setEchoChar((char) 0);
        } else {
            jPasswordFieldConfirm.setEchoChar('*');
        }
    }//GEN-LAST:event_jCheckBoxConfirmItemStateChanged

    private void jPasswordFieldPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordFieldPasswordActionPerformed
        if (loginManager != null && loginManager.installed()) {
            submit();
        }
        jPasswordFieldConfirm.requestFocus();
    }//GEN-LAST:event_jPasswordFieldPasswordActionPerformed

    private void jPasswordFieldConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordFieldConfirmActionPerformed
        if (loginManager == null || !loginManager.installed()) {
            submit();
        }
    }//GEN-LAST:event_jPasswordFieldConfirmActionPerformed

    private void setStatus(String msg) {
        jTextAreaStatus.append(msg + "\n");
        jTextAreaStatus.setCaretPosition(jTextAreaStatus.getText().length() - 1);
        jTextAreaStatus.validate();
    }

    public String getPassword() {
        return password;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JCheckBox jCheckBoxConfirm;
    private javax.swing.JCheckBox jCheckBoxPassword;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPasswordField jPasswordFieldConfirm;
    private javax.swing.JPasswordField jPasswordFieldPassword;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JTextArea jTextAreaStatus;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}