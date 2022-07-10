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
public class ChangePasswordDialog extends javax.swing.JDialog {

    private final LoginManager loginManager;

    /**
     * Creates new form CheckPasswordJDialog
     *
     * @param parent
     * @param modal
     * @param loginManager
     */
    public ChangePasswordDialog(java.awt.Frame parent, boolean modal, LoginManager loginManager) {
        super(parent, modal);
        initComponents();

        this.loginManager = loginManager;

        init();
    }

    private void init() {
        if (loginManager.installed()) {
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
        jButtonChange = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jButtonCancel = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPasswordFieldLegacy = new javax.swing.JPasswordField();
        jLabel2 = new javax.swing.JLabel();
        jPasswordFieldCandidate = new javax.swing.JPasswordField();
        jCheckBoxConfirm = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaStatus = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(ChangePasswordDialog.class, "ChangePasswordDialog.title")); // NOI18N

        jToolBar1.setRollover(true);

        jButtonChange.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Save16.gif"))); // NOI18N
        jButtonChange.setText(org.openide.util.NbBundle.getMessage(ChangePasswordDialog.class, "ChangePasswordDialog.jButtonChange.text")); // NOI18N
        jButtonChange.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonChange.setFocusable(false);
        jButtonChange.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonChangeActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonChange);
        jToolBar1.add(jSeparator1);

        jButtonCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Stop16.gif"))); // NOI18N
        jButtonCancel.setText(org.openide.util.NbBundle.getMessage(ChangePasswordDialog.class, "ChangePasswordDialog.jButtonCancel.text")); // NOI18N
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

        jLabel1.setText(org.openide.util.NbBundle.getMessage(ChangePasswordDialog.class, "ChangePasswordDialog.jLabel1.text")); // NOI18N

        jPasswordFieldLegacy.setText(org.openide.util.NbBundle.getMessage(ChangePasswordDialog.class, "ChangePasswordDialog.jPasswordFieldLegacy.text")); // NOI18N
        jPasswordFieldLegacy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordFieldLegacyActionPerformed(evt);
            }
        });

        jLabel2.setText(org.openide.util.NbBundle.getMessage(ChangePasswordDialog.class, "ChangePasswordDialog.jLabel2.text")); // NOI18N

        jPasswordFieldCandidate.setText(org.openide.util.NbBundle.getMessage(ChangePasswordDialog.class, "ChangePasswordDialog.jPasswordFieldCandidate.text")); // NOI18N
        jPasswordFieldCandidate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordFieldCandidateActionPerformed(evt);
            }
        });

        jCheckBoxConfirm.setText(org.openide.util.NbBundle.getMessage(ChangePasswordDialog.class, "ChangePasswordDialog.jCheckBoxConfirm.text")); // NOI18N
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
                    .addComponent(jPasswordFieldLegacy)
                    .addComponent(jPasswordFieldCandidate, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jCheckBoxConfirm)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jPasswordFieldLegacy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jPasswordFieldCandidate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void jButtonChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonChangeActionPerformed
        submit();
    }//GEN-LAST:event_jButtonChangeActionPerformed

    private void submit() {
        setStatus("");
        String legacy = new String(jPasswordFieldLegacy.getPassword());
        if (legacy == null || legacy.isEmpty()) {
            setStatus("기존 패스워드를 입력하세요.");
            return;
        }
        String candidate = new String(jPasswordFieldCandidate.getPassword());
        if (candidate == null || candidate.isEmpty()) {
            setStatus("신규 패스워드를 입력하세요.");
            return;
        }
        if (!loginManager.validPasswordConfig(candidate)) {
            setStatus(loginManager.getMessage());
            return;
        }
        if (!loginManager.changePassword(legacy, candidate)) {
            setStatus("패스워드 변경 실패: " + loginManager.getMessage());
            return;
        }
        setStatus("패스워드가 변경 되었습니다.");
        jButtonCancel.setEnabled(false);
        jButtonCancel.setEnabled(false);
    }

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        setVisible(false);
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jCheckBoxConfirmItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxConfirmItemStateChanged
        if (jCheckBoxConfirm.isSelected()) {
            jPasswordFieldCandidate.setEchoChar((char) 0);
        } else {
            jPasswordFieldCandidate.setEchoChar('*');
        }
    }//GEN-LAST:event_jCheckBoxConfirmItemStateChanged

    private void jPasswordFieldLegacyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordFieldLegacyActionPerformed
        submit();
    }//GEN-LAST:event_jPasswordFieldLegacyActionPerformed

    private void jPasswordFieldCandidateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordFieldCandidateActionPerformed
        submit();
    }//GEN-LAST:event_jPasswordFieldCandidateActionPerformed

    private void setStatus(String msg) {
        jTextAreaStatus.append(msg + "\n");
        jTextAreaStatus.setCaretPosition(jTextAreaStatus.getText().length() - 1);
        jTextAreaStatus.validate();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonChange;
    private javax.swing.JCheckBox jCheckBoxConfirm;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPasswordField jPasswordFieldCandidate;
    private javax.swing.JPasswordField jPasswordFieldLegacy;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JTextArea jTextAreaStatus;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}