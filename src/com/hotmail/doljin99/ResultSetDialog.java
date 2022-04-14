/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.hotmail.doljin99;

import java.awt.BorderLayout;

/**
 *
 * @author dolji
 */
public class ResultSetDialog extends javax.swing.JDialog {

    private ResultSetPane resultSetPane;
    
    /**
     *
     * @param parent
     * @param modal
     * @param resultSetPane
     */
    public ResultSetDialog(java.awt.Dialog parent, boolean modal, ResultSetPane resultSetPane) {
        super(parent, modal);
        this.resultSetPane = resultSetPane;

        init();
    }

    /**
     * Creates new form ResultSetDialog
     *
     * @param parent
     * @param modal
     * @param resultSetPane
     */
    public ResultSetDialog(java.awt.Frame parent, boolean modal, ResultSetPane resultSetPane) {
        super(parent, modal);
        this.resultSetPane = resultSetPane;

        init();
    }

    private void init() {
        initComponents();
        setTitle(resultSetPane.getScriptName());
        add(resultSetPane, BorderLayout.CENTER);
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
        jSeparator1 = new javax.swing.JToolBar.Separator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jToolBar1.setRollover(true);
        jToolBar1.add(jSeparator1);

        getContentPane().add(jToolBar1, java.awt.BorderLayout.NORTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
