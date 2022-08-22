/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.hotmail.doljin99;

import com.hotmail.doljin99.loginmanager.LoginManager;
import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author dolji
 */
public class ManageUserDialog extends javax.swing.JDialog {

    public static final String GET_USERS_SCRIPT
        = "SELECT "
        + "	USER_NAME, IS_ADMIN "
        + "FROM "
        + "	INFORMATION_SCHEMA.USERS "
        + "WHERE "
        + "	USER_NAME != 'SA' ";

    public static final String GET_TABLES_SCRIPT
        = " SELECT  "
        + " 	TABLE_NAME, TABLE_TYPE  "
        + " FROM  "
        + " 	INFORMATION_SCHEMA.TABLES  "
        + " WHERE  "
        + " 	TABLE_SCHEMA = 'PUBLIC' ";

    private static final String GET_USER_TABLE_PRIVILEGES
        = " SELECT  "
        + " 	TABLE_NAME,  PRIVILEGE_TYPE  "
        + " FROM  "
        + " 	INFORMATION_SCHEMA.TABLE_PRIVILEGES  "
        + " WHERE  "
        + " 	GRANTEE = ? AND TABLE_NAME = ?"
        + " ORDER BY  "
        + " 	TABLE_NAME,  PRIVILEGE_TYPE ";

    public static final String CREATE_USER_TEMPLATE
        = "CREATE USER ?1 PASSWORD '?2'";
    public static final String GRANT_TABLE_SCRIPT
        = "GRANT ? ON TABLE ? TO ?";
    public static final String REVOKE_TABLE_SCRIPT
        = "REVOKE ? ON TABLE ? FROM ?";

    private final ServerMan serverMan;
    private final DatabaseMan databaseMan;

    private ArrayList<SimpleTableInfo> databaseTables;
    private Users users;

    private UserPrivileges userPrivileges;
    private UserPriviligesJPanel userPrivilegesPane;
    private final LoginManager loginManager;

    /**
     * Creates new form AddUserDialog
     *
     * @param parent
     * @param modal
     * @param serverMan
     * @param databaseMan
     * @param loginManager
     */
    public ManageUserDialog(java.awt.Frame parent, boolean modal, ServerMan serverMan, DatabaseMan databaseMan, LoginManager loginManager) {
        super(parent, modal);
        initComponents();
        this.serverMan = serverMan;
        this.databaseMan = databaseMan;
        this.loginManager = loginManager;

        init();
    }

    private void init() {
        users = databaseMan.getUsers();
        if (serverMan == null) {
            jButtonAdd.setEnabled(false);
            return;
        }

        setUsersFromFIle();
        setUsersFromMeta();

        setDatabaseTables();

        jTextFieldUserId.getDocument().addDocumentListener(new UserIdChangeListener());
        jTableUsers.getSelectionModel().addListSelectionListener(new UsersSelectionListener());
    }

    private void refreshUserTable() {
        setUsersFromFIle();
        setUsersFromMeta();
        if (jTableUsers.getRowCount() > 0) {
            jTableUsers.setRowSelectionInterval(0, 0);
        }
        jTableUsers.validate();
    }

    private void setUsersFromFIle() {
        if (users == null) {
            users = new Users();
        }
        Object[] row = new Object[2];
        DefaultTableModel model = (DefaultTableModel) jTableUsers.getModel();
        model.setRowCount(0);
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            row[0] = user.getUserName();
            row[1] = user.isAdministrator();

            model.addRow(row);
        }
        jTableUsers.validate();
    }

    private void setUsersFromMeta() {
        Object[] row = new Object[2];
        DefaultTableModel model = (DefaultTableModel) jTableUsers.getModel();
        Connection connection = serverMan.getConnection(databaseMan.getDatabaseName());
        Statement statement = null;
        ResultSet usersResulset = null;
        try {
            statement = connection.createStatement();
            usersResulset = statement.executeQuery(GET_USERS_SCRIPT);
            while (usersResulset.next()) {
                if (existId(usersResulset.getString(1))) {
                    continue;
                }
                User user = new User();
                user.setUserName(usersResulset.getString(1));                
                user.setAdministrator(usersResulset.getBoolean(2));
                user.encryptFields(loginManager);

                row[0] = user.getUserName();
                row[1] = user.isAdministrator();
                users.addUser(user);

                model.addRow(row);
            }
            jTableUsers.validate();
        } catch (SQLException ex) {
            writeMessage("메타데이터에서 사용자 목록 가져오기 에러: " + ex.getLocalizedMessage());
        } finally {
            if (usersResulset != null) {
                try {
                    usersResulset.close();
                } catch (SQLException ex) {
                }
            }
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

    private UserPrivileges makeAllRightsUserPrivileges(String userName, boolean rights) {
        UserPrivileges tempUserPrivileges = new UserPrivileges(userName);
        for (int i = 0; i < databaseTables.size(); i++) {
            SimpleTableInfo simpleTableInfo = databaseTables.get(i);
            TablePrivileges tablePrivileges = makeAllRightsTablePrivileges(simpleTableInfo, rights);
            tablePrivileges.setRightsAll(rights);
        }

        return tempUserPrivileges;
    }

    private TablePrivileges makeAllRightsTablePrivileges(SimpleTableInfo simpleTableInfo, boolean rights) {
        TablePrivileges tablePrivileges = new TablePrivileges(simpleTableInfo.getTableName(), simpleTableInfo.getTableType());
        tablePrivileges.setRightsAll(rights);

        return tablePrivileges;
    }

    class SimpleTableInfo {

        private String tableName;

        private String tableType;

        /**
         * Get the value of tableType
         *
         * @return the value of tableType
         */
        public String getTableType() {
            return tableType;
        }

        /**
         * Set the value of tableType
         *
         * @param tableType new value of tableType
         */
        public void setTableType(String tableType) {
            this.tableType = tableType;
        }

        /**
         * Get the value of tableName
         *
         * @return the value of tableName
         */
        public String getTableName() {
            return tableName;
        }

        /**
         * Set the value of tableName
         *
         * @param tableName new value of tableName
         */
        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

    }

    private void setDatabaseTables() {
        Connection connection = serverMan.getConnection(databaseMan.getDatabaseName());
        Statement statement = null;
        ResultSet tablesResulset = null;
        databaseTables = new ArrayList<>();
        try {
            statement = connection.createStatement();
            tablesResulset = statement.executeQuery(GET_TABLES_SCRIPT);
            while (tablesResulset.next()) {
                SimpleTableInfo simpleTableInfo = new SimpleTableInfo();
                simpleTableInfo.setTableName(tablesResulset.getString(1));
                simpleTableInfo.setTableType(tablesResulset.getString(2));

                databaseTables.add(simpleTableInfo);
            }
            userPrivilegesPane = new UserPriviligesJPanel(databaseTables);
            jPanelPriviliges.add(userPrivilegesPane, BorderLayout.CENTER);
            jPanelPriviliges.validate();

        } catch (SQLException ex) {
            writeMessage("데이터베이스 테이블 목록 가져오기 에러: " + ex.getLocalizedMessage());
        } finally {
            if (tablesResulset != null) {
                try {
                    tablesResulset.close();
                } catch (SQLException ex) {
                }
            }
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

    class UserIdChangeListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            setButtonsEnable();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            setButtonsEnable();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            setButtonsEnable();
        }

        void setButtonsEnable() {
            String id = jTextFieldUserId.getText();
            if (id == null || id.isEmpty()) {
                jButtonAdd.setEnabled(false);
                jButtonUpdate.setEnabled(false);
                jButtonDelete.setEnabled(false);
            }
            if (existId(id)) {
                jButtonAdd.setEnabled(false);
                jButtonUpdate.setEnabled(true);
                jButtonDelete.setEnabled(true);
                setUserPrivileges(id);
                int row = jTableUsers.getSelectedRow();
                jCheckBoxAdministrator.setSelected((boolean) jTableUsers.getValueAt(row, 1));
                jCheckBoxAdministrator.validate();
            } else {
                jButtonAdd.setEnabled(true);
                jButtonUpdate.setEnabled(false);
                jButtonDelete.setEnabled(false);
            }
        }
    }

    private boolean existId(String id) {
        for (User user : users) {
            if (user.getUserName().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    private void setUserPrivileges(String userName) {
        userPrivileges = getTablePrivileges(userName);
        userPrivilegesPane.setPrivileges(userPrivileges);
    }

    private UserPrivileges getTablePrivileges(String userName) {
        UserPrivileges tempUserPrivileges = new UserPrivileges(userName);
        Connection connection = serverMan.getConnection(databaseMan.getDatabaseName());
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(GET_USER_TABLE_PRIVILEGES);
            for (int i = 0; i < databaseTables.size(); i++) {
                SimpleTableInfo simpleTableInfo = databaseTables.get(i);
                ps.setString(1, userName);
                ps.setString(2, simpleTableInfo.getTableName());

                rs = ps.executeQuery();
                int count = 0;
                TablePrivileges tablePrivileges = new TablePrivileges(simpleTableInfo.getTableName(), simpleTableInfo.getTableType());
                tablePrivileges.setRightsAll(false);
                String privilegeType;
                while (rs.next()) {
                    privilegeType = rs.getString(2).trim();
                    if ("INSERT".equalsIgnoreCase(privilegeType)) {
                        tablePrivileges.setInsertPrivileges(true);
                    } else if ("SELECT".equalsIgnoreCase(privilegeType)) {
                        tablePrivileges.setSelectPrivileges(true);
                    } else if ("UPDATE".equalsIgnoreCase(privilegeType)) {
                        tablePrivileges.setUpdatePrivileges(true);
                    } else if ("DELETE".equalsIgnoreCase(privilegeType)) {
                        tablePrivileges.setDeletePrivileges(true);
                    }
                    count++;
                }
                if (count == 0) {
                    tempUserPrivileges.add(makeAllRightsTablePrivileges(simpleTableInfo, false));
                } else {
                    tempUserPrivileges.add(tablePrivileges);
                }
            }
            return tempUserPrivileges;
        } catch (SQLException ex) {
            writeMessage("테이블 권한 가져오기 중 에러: " + ex.getLocalizedMessage());
            return tempUserPrivileges;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
            if (ps != null) {
                try {
                    ps.close();
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

    class UsersSelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            int index = jTableUsers.getSelectedRow();
            if (index < 0 || index >= jTableUsers.getRowCount()) {
                return;
            }
            jTextFieldUserId.setText((String) jTableUsers.getValueAt(index, 0));
            User user = users.getUser((String) jTableUsers.getValueAt(index, 0));
            if (user == null) {
                jPasswordField1.setText("");
            } else {
                jPasswordField1.setText(user.getUserPassword());
            }
            jCheckBoxAdministrator.setSelected((boolean) jTableUsers.getValueAt(index, 1));
            setUserPrivileges((String) jTableUsers.getValueAt(index, 0));
            jButtonAdd.setEnabled(false);
            jButtonUpdate.setEnabled(true);
            jButtonDelete.setEnabled(true);
        }
    }

    private void writeMessage(String msg) {
        jTextAreaMessage.append(msg + "\n");
        jTextAreaMessage.setCaretPosition(jTextAreaMessage.getText().length() - 1);
        jTextAreaMessage.validate();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableUsers = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jButtonAdd = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jButtonUpdate = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jButtonDelete = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jButtonCancel = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldUserId = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jCheckBoxAdministrator = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaMessage = new javax.swing.JTextArea();
        jPanelPriviliges = new javax.swing.JPanel();
        jCheckBoxEcho = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("사용자 관리");

        jSplitPane1.setDividerLocation(200);

        jPanel4.setLayout(new java.awt.BorderLayout());

        jTableUsers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "사용자ID", "administrator"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTableUsers);

        jPanel4.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jSplitPane1.setLeftComponent(jPanel4);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jToolBar1.setRollover(true);

        jButtonAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Add16.gif"))); // NOI18N
        jButtonAdd.setText("추가");
        jButtonAdd.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonAdd.setEnabled(false);
        jButtonAdd.setFocusable(false);
        jButtonAdd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonAdd);
        jToolBar1.add(jSeparator1);

        jButtonUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Edit16.gif"))); // NOI18N
        jButtonUpdate.setText("수정");
        jButtonUpdate.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonUpdate.setEnabled(false);
        jButtonUpdate.setFocusable(false);
        jButtonUpdate.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUpdateActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonUpdate);
        jToolBar1.add(jSeparator2);

        jButtonDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Delete16.gif"))); // NOI18N
        jButtonDelete.setText("삭제");
        jButtonDelete.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonDelete.setEnabled(false);
        jButtonDelete.setFocusable(false);
        jButtonDelete.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonDelete);
        jToolBar1.add(jSeparator3);

        jButtonCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Stop16.gif"))); // NOI18N
        jButtonCancel.setText("취소");
        jButtonCancel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonCancel.setFocusable(false);
        jButtonCancel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonCancel);

        jPanel2.add(jToolBar1, java.awt.BorderLayout.NORTH);

        jLabel1.setText("사용자 ID");

        jLabel2.setText("패스워드");

        jPasswordField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordField1ActionPerformed(evt);
            }
        });

        jCheckBoxAdministrator.setText("as Administrator");
        jCheckBoxAdministrator.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxAdministratorItemStateChanged(evt);
            }
        });

        jTextAreaMessage.setEditable(false);
        jTextAreaMessage.setColumns(20);
        jTextAreaMessage.setLineWrap(true);
        jTextAreaMessage.setRows(5);
        jTextAreaMessage.setTabSize(4);
        jTextAreaMessage.setWrapStyleWord(true);
        jScrollPane1.setViewportView(jTextAreaMessage);

        jPanelPriviliges.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 204)), "Privileges"));
        jPanelPriviliges.setLayout(new java.awt.BorderLayout());

        jCheckBoxEcho.setText("보이기");
        jCheckBoxEcho.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxEchoItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldUserId)
                            .addComponent(jPasswordField1, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBoxAdministrator)
                            .addComponent(jCheckBoxEcho))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanelPriviliges, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldUserId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBoxAdministrator))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBoxEcho))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelPriviliges, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel1, java.awt.BorderLayout.CENTER);

        jSplitPane1.setRightComponent(jPanel2);

        getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBoxEchoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxEchoItemStateChanged
        if (jCheckBoxEcho.isSelected()) {
            jPasswordField1.setEchoChar((char) 0);
        } else {
            jPasswordField1.setEchoChar('*');
        }
    }//GEN-LAST:event_jCheckBoxEchoItemStateChanged

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        setVisible(false);
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jCheckBoxAdministratorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxAdministratorItemStateChanged
        if (jCheckBoxAdministrator.isSelected()) {
            jPanelPriviliges.setEnabled(false);
            userPrivilegesPane.setEnabled(false);
            userPrivilegesPane.setAllRights(true);
            userPrivilegesPane.setEnableTablePrivileges(false);
        } else {
            jPanelPriviliges.setEnabled(true);
            userPrivilegesPane.setEnabled(true);
            userPrivilegesPane.setEnableTablePrivileges(true);
        }
        jPanelPriviliges.validate();
    }//GEN-LAST:event_jCheckBoxAdministratorItemStateChanged

    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActionPerformed
        addUser();
    }//GEN-LAST:event_jButtonAddActionPerformed

    private void addUser() {
        String password = new String(jPasswordField1.getPassword()).trim();
        if (!loginManager.validPasswordConfig(password)) {
            writeMessage(loginManager.getMessage());
            return;
        }
        String userName = jTextFieldUserId.getText();
        if (userName == null || userName.isEmpty() || userName.length() < 2) {
            writeMessage("사용자 이름을 2자 이상 입력하십시오.");
            return;
        }
        if (createUser(userName, password)) {
            updatePrivliges();
            refreshUserTable();
        }
    }

    private boolean createUser(String userName, String password) {
        Connection connection = serverMan.getConnection(databaseMan.getDatabaseName());
        Statement createUserStatement = null;
//        String createTemplate = "CREATE USER '?1' PASSWORD '?2'";
        try {
            String createScript = "CREATE USER " + userName + " PASSWORD '" + password + "'";
            createUserStatement = connection.createStatement();

            createUserStatement.executeUpdate(createScript);
            User user = new User();
            user.setUserName(userName);
            user.setUserPassword(password);
            user.encryptFields(loginManager);
            user.setAdministrator(jCheckBoxAdministrator.isSelected());
            users.addUser(user);
            writeMessage("사용자 생성: " + userName);
            return true;
        } catch (SQLException ex) {
            writeMessage("사용자 생성 중 에러: " + ex.getLocalizedMessage());
            return false;
        } finally {
            if (createUserStatement != null) {
                try {
                    createUserStatement.close();
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

    private void updatePrivliges() {
        String userName = jTextFieldUserId.getText();
        Connection connection = serverMan.getConnection(databaseMan.getDatabaseName());
        Statement grantStatement = null;
        Statement revokeStatement = null;
//        String createTemplate = "CREATE USER '?1' PASSWORD '?2'";
//        String grantTemplete = "GRANT ? ON TABLE ? TO ?";
//        String revokeTemplate = "REVOKE ? ON TABLE ? FROM ?";
        try {
            grantStatement = connection.createStatement();
            revokeStatement = connection.createStatement();
            UserPrivileges updatedPrivileges = userPrivilegesPane.getUpdatesCatalogPrivileges(userName);
            for (int i = 0; i < updatedPrivileges.size(); i++) {
                TablePrivileges tablePrivileges = updatedPrivileges.get(i);
                if (tablePrivileges.isInsertPrivileges()) {
                    grantStatement.executeUpdate("GRANT INSERT  ON TABLE " + tablePrivileges.getTableName() + " TO " + userName);
                } else {
                    revokeStatement.executeUpdate("REVOKE INSERT  ON TABLE " + tablePrivileges.getTableName() + " FROM " + userName);
                }
                if (tablePrivileges.isSelectPrivileges()) {
                    grantStatement.executeUpdate("GRANT SELECT  ON TABLE " + tablePrivileges.getTableName() + " TO " + userName);
                } else {
                    revokeStatement.executeUpdate("REVOKE SELECT  ON TABLE " + tablePrivileges.getTableName() + " FROM " + userName);
                }
                if (tablePrivileges.isUpdatePrivileges()) {
                    grantStatement.executeUpdate("GRANT UPDATE  ON TABLE " + tablePrivileges.getTableName() + " TO " + userName);
                } else {
                    revokeStatement.executeUpdate("REVOKE UPDATE  ON TABLE " + tablePrivileges.getTableName() + " FROM " + userName);
                }
                if (tablePrivileges.isDeletePrivileges()) {
                    grantStatement.executeUpdate("GRANT DELETE  ON TABLE " + tablePrivileges.getTableName() + " TO " + userName);
                } else {
                    revokeStatement.executeUpdate("REVOKE DELETE  ON TABLE " + tablePrivileges.getTableName() + " FROM " + userName);
                }
            }
        } catch (SQLException ex) {
            writeMessage("사용자 권한 변경 중 에러: " + ex.getLocalizedMessage());
        } finally {
            if (grantStatement != null) {
                try {
                    grantStatement.close();
                } catch (SQLException ex) {
                }
            }
            if (revokeStatement != null) {
                try {
                    revokeStatement.close();
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

    private boolean updatePassword() {
        String password = new String(jPasswordField1.getPassword()).trim();
        if (!loginManager.validPasswordConfig(password)) {
            writeMessage(loginManager.getMessage());
            return false;
        }
        String userName = jTextFieldUserId.getText();
        Connection connection = serverMan.getConnection(databaseMan.getDatabaseName());
        Statement createUserStatement = null;
        Statement grantStatement = null;
        Statement revokeStatement = null;
//        String updateUserTemplate = "ALTER  USER '?1' SET PASSWORD '?2'";
        try {
            String createScript = "ALTER USER " + userName + " SET PASSWORD '" + password + "'";
            createUserStatement = connection.createStatement();

            createUserStatement.executeUpdate(createScript);
            User user = users.getUser(userName);
            if (user == null) {
                user = new User();
                user.setUserName(userName);
                user.setUserPassword(password);
                user.encryptFields(loginManager);
                user.setAdministrator(jCheckBoxAdministrator.isSelected());
                users.addUser(user);
            } else {
                user.setUserName(userName);
                user.setUserPassword(password);
                user.encryptFields(loginManager);
                user.setAdministrator(jCheckBoxAdministrator.isSelected());
            }

            writeMessage("사용자 암호 변경: " + userName);
            return true;
        } catch (SQLException ex) {
            writeMessage("사용자 암호 변경 중 에러: " + ex.getLocalizedMessage());
            return false;
        } finally {
            if (createUserStatement != null) {
                try {
                    createUserStatement.close();
                } catch (SQLException ex) {
                }
            }
            if (grantStatement != null) {
                try {
                    grantStatement.close();
                } catch (SQLException ex) {
                }
            }
            if (revokeStatement != null) {
                try {
                    revokeStatement.close();
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

    private void jButtonUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUpdateActionPerformed
        updateUser();
    }//GEN-LAST:event_jButtonUpdateActionPerformed

    private void updateUser() {
        if (updatePassword()) {
            updatePrivliges();
        }
    }
    
    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed
        String userName = jTextFieldUserId.getText();
        int response = JOptionPane.showConfirmDialog(this, userName + ": 삭제 하시겠습니까?", "사용자 삭제", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.NO_OPTION) {
            writeMessage("사용자 삭제를 취소했습니다.");
            return;
        }
        String script = "DROP USER IF EXISTS " + userName.toUpperCase();
        Connection connection = null;
        Statement statement = null;
        try {
            connection = serverMan.getConnection(databaseMan.getDatabaseName());
            statement = connection.createStatement();

            statement.executeUpdate(script);
            users.remove(userName);
            refreshUserTable();
            writeMessage("사용자 " + userName + " 삭제 되었습니다.");
        } catch (SQLException ex) {
            writeMessage("사용자 " + userName + " 삭제 중 에러: " + ex.getLocalizedMessage());
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
    }//GEN-LAST:event_jButtonDeleteActionPerformed

    private void jPasswordField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordField1ActionPerformed
        if (jButtonAdd.isEnabled()) {
            addUser();
        } else if (jButtonUpdate.isEnabled()) {
            updateUser();
        }
        jTableUsers.requestFocus();
    }//GEN-LAST:event_jPasswordField1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonUpdate;
    private javax.swing.JCheckBox jCheckBoxAdministrator;
    private javax.swing.JCheckBox jCheckBoxEcho;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelPriviliges;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTable jTableUsers;
    private javax.swing.JTextArea jTextAreaMessage;
    private javax.swing.JTextField jTextFieldUserId;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
