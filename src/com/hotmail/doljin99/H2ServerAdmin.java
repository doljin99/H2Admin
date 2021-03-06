/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.hotmail.doljin99;

import codecompletionlibrary.MakeWordList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import com.hotmail.doljin99.loginmanager.LoginManager;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.tree.TreePath;
import org.apache.commons.io.FileUtils;
import org.h2.engine.Constants;

/**
 *
 * @author dolji
 */
public class H2ServerAdmin extends javax.swing.JFrame {

    public static final String VERSION = "v0.8.1";

    private LoginManager loginManager;
    private JPanel glassPane;

    public static final String SERVER_MEN_PATH = "server.json";
    public static final String SERVER_MEN_ENC_DIR = ".serverList";
    public static final String SERVER_MEN_ENC_PATH = "server_enc.json";

    private ServerMen serverList;
    private ServerTreePane serverTreePane;

    private List<String> codeCompletionList;
    private InactivityListener activityTimer;
    private UnblockPane unblockPane;

    private String blockMessage;
    private int interval = 5;
    private ActionTimeOutListener actionTimeOutListener;

    /**
     * Creates new form HeServerAdmin
     */
    public H2ServerAdmin() {
        initComponents();

        init();
    }

    private void init() {
        loginManager = new LoginManager();
        codeCompletionList = MakeWordList.make();
        makeGlassPane();

        setIconImage(new ImageIcon(H2ServerAdmin.class.getResource("h2adminpro.png")).getImage());
        setSize(920, 810);
        jSplitPane1.setDividerLocation(645);
        setLocationRelativeTo(this);
        setVisible(true);

        String password = getPassword();
        loginManager.login(password);

        if (loginManager.isLogin()) {
            makeServerList();
        } else {
            blockWindow("???????????? ?????????????????????.");
        }

        resetActivityTimer();
    }

    private void makeServerList() {
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        if (H2AUtilities.isOldServerListFile()) {
            logMessage("old version server ?????? ????????? ????????????.");
            serverList = readServerMenOld();
        } else if (H2AUtilities.isServerListFile()) {
            logMessage("server ?????? ????????? ????????????.");
            serverList = readServerMen();
        } else {
            serverList = new ServerMen();
        }
        serverTreePane = new ServerTreePane(serverList, jTextAreaStatus, codeCompletionList, loginManager);
        jPanelServers.add(serverTreePane, BorderLayout.CENTER);
        jPanelServers.validate();
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    private void makeGlassPane() {
        glassPane = (JPanel) this.getGlassPane();
        glassPane.setOpaque(true);
        glassPane.setLayout(new BorderLayout());
        JButton loginButton = new JButton("login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
//        glassPane.add(loginButton);
        unblockPane = new UnblockPane(loginManager);
        glassPane.add(unblockPane, BorderLayout.CENTER);
        glassPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                unblockPane.setStatus(blockMessage);
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                showOwnedWindows();
                if (serverList == null) {
                    makeServerList();
                }
                resetActivityTimer(interval);
            }
        });
    }

    class OnairWindow {

        Window window;
        int locationX;
        int locationY;
        int sizeX;
        int sizeY;

        public OnairWindow(Window window) {
            this.window = window;
        }

        public Window getWindow() {
            return window;
        }

        public void setWindow(Window window) {
            this.window = window;
        }

        public int getLocationX() {
            return locationX;
        }

        public void setLocationX(int locationX) {
            this.locationX = locationX;
        }

        public int getLocationY() {
            return locationY;
        }

        public void setLocationY(int locationY) {
            this.locationY = locationY;
        }

        public int getSizeX() {
            return sizeX;
        }

        public void setSizeX(int sizeX) {
            this.sizeX = sizeX;
        }

        public int getSizeY() {
            return sizeY;
        }

        public void setSizeY(int sizeY) {
            this.sizeY = sizeY;
        }

    }

    private ArrayList<OnairWindow> ownWindows;

    private void blockWindow(String msg) {
        blockMessage = msg;

        ownWindows = new ArrayList<>();
        Window[] windows = getOwnedWindows();
        for (Window window : windows) {
            if (!window.isVisible()) {
                window.dispose();
                continue;
            }
            OnairWindow onairWindow = new OnairWindow(window);
            onairWindow.setLocationX(window.getX());
            onairWindow.setLocationY(window.getY());
            onairWindow.setLocationX(window.getX());
            onairWindow.setSizeX(window.getWidth());
            onairWindow.setSizeY(window.getHeight());
            ownWindows.add(onairWindow);
            window.setSize(20, 20);
            window.setLocation(getX() + 20, getY() + 20);
            window.toBack();
            window.setVisible(false);
        }

        glassPane.setVisible(true);
        this.toFront();
        stopActivityTimer();
    }

    private void unblockWindow() {
        showOwnedWindows();
        glassPane.setVisible(false);
        if (serverList == null) {
            makeServerList();
        }
        resetActivityTimer(interval);
    }

    private void showOwnedWindows() {
        if (ownWindows == null || ownWindows.isEmpty()) {
            return;
        }
        for (int i = 0; i < ownWindows.size(); i++) {
            OnairWindow onairWindow = ownWindows.get(i);
            Window window = onairWindow.getWindow();
            window.setSize(onairWindow.getSizeX(), onairWindow.getSizeY());
            window.setLocation(onairWindow.getLocationX(), onairWindow.getLocationY());
            window.toFront();
            window.setVisible(true);
            window.validate();
        }
        ownWindows.clear();
    }

    private void login() {
        String password = getPassword();
        if (loginManager.login(password)) {
            unblockWindow();
        } else {
            logMessage("login??? ?????????????????????.");
            logMessage("\t" + loginManager.getMessage());
        }
    }

    private String getPassword() {
        CheckPasswordDialog dialog;
        dialog = new CheckPasswordDialog(this, true, loginManager);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        String password = dialog.getPassword();
        dialog.dispose();
        return password;
    }

    private void decryptServerMen(ServerMen serverMen) {
        for (int i = 0; i < serverMen.size(); i++) {
            ServerMan serverMan = serverMen.get(i);
            serverMan.decryptFields(loginManager);
            DatabaseMen databaseMen = serverMan.getDatabaseMen();
            for (int j = 0; j < databaseMen.size(); j++) {
                DatabaseMan databaseMan = databaseMen.get(j);
                databaseMan.decryptFields(loginManager);
            }
        }
    }

    class ActionTimeOutListener extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            blockWindow(interval + " ??? ?????? ????????? ?????? ?????? ?????????????????????.");
        }
    }

    private void logout(String message) {
        loginManager.logout();
        glassPane.setVisible(true);
        logMessage(message);
    }

    private ServerMen readServerMen() {
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        FileInputStream fileInputStream = null;
        BufferedReader br = null;
        try {
            java.lang.reflect.Type serverType = new TypeToken<ServerMen>() {
            }.getType();
            fileInputStream = new FileInputStream(SERVER_MEN_ENC_DIR + File.separator + SERVER_MEN_PATH);
            br = new BufferedReader(new InputStreamReader(fileInputStream, "UTF-8"));

            ServerMen serverMen = gson.fromJson(br, serverType);

            if (serverMen == null) {
                serverMen = new ServerMen();
                logMessage("server ?????? ????????? ?????? ????????????:" + SERVER_MEN_ENC_DIR + File.separator + SERVER_MEN_PATH);
                return serverMen;
            }

            decryptServerMen(serverMen);

            return serverMen;
        } catch (NoSuchElementException ex) {
            logMessage("?????? ??????: ????????? ???????????? ServerMen ??? ????????? ??????." + ex.getLocalizedMessage());
            return new ServerMen();
        } catch (FileNotFoundException ex) {
            logMessage("?????? ????????? ???????????????: ????????? ??????????????????:" + ex.getLocalizedMessage());
            return new ServerMen();
        } catch (UnsupportedEncodingException ex) {
            logMessage("?????? ??????:  " + ex.getLocalizedMessage());
            return new ServerMen();
        } catch (JsonIOException ex) {
            logMessage("?????? ??????:  " + ex.getLocalizedMessage());
            return new ServerMen();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException ex) {
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    private ServerMen readServerMenOld() {
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        BufferedReader br = null;
        try {
            java.lang.reflect.Type serverType = new TypeToken<ServerMenOld>() {
            }.getType();
            br = new BufferedReader(new InputStreamReader(new FileInputStream(SERVER_MEN_PATH), "UTF-8"));

            ServerMenOld serverMen = gson.fromJson(br, serverType);
            if (serverMen == null) {
                serverMen = new ServerMenOld();
                logMessage("server ?????? ????????? ?????? ????????????:" + SERVER_MEN_PATH);
            }

            return H2AUtilities.upgradeServerMen(loginManager, serverMen);
        } catch (NoSuchElementException ex) {
            logMessage("?????? ??????: ????????? ???????????? ServerMen ??? ????????? ??????." + ex.getLocalizedMessage());
            return new ServerMen();
        } catch (FileNotFoundException ex) {
            logMessage("?????? ????????? ???????????????: ????????? ??????????????????: " + ex.getLocalizedMessage());
            return new ServerMen();
        } catch (UnsupportedEncodingException ex) {
            logMessage("?????? ??????:  " + ex.getLocalizedMessage());
            return new ServerMen();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                }
            }
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

        jPanel1 = new javax.swing.JPanel();
        jToolBarTop = new javax.swing.JToolBar();
        jButtonAddServer = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jButtonChangeUserInfo = new javax.swing.JButton();
        jButtonStopServer = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jButtonDeleteServer = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jButtonRefreshTree = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanelServers = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaStatus = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuItemBackup = new javax.swing.JMenuItem();
        jMenuEdit = new javax.swing.JMenu();
        jMenuItemLChangePassword = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        jMenuItemBlockWindow = new javax.swing.JMenuItem();
        jMenuItemSetBlockInterval = new javax.swing.JMenuItem();
        jMenuHelp = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        jMenuItem6 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("H2 ?????? ?????? ??????");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setLayout(new java.awt.BorderLayout());

        jToolBarTop.setRollover(true);

        jButtonAddServer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/New16.gif"))); // NOI18N
        jButtonAddServer.setText("server");
        jButtonAddServer.setToolTipText("????????? local ?????? remote  H2 ????????????????????? ???????????????.");
        jButtonAddServer.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonAddServer.setFocusable(false);
        jButtonAddServer.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonAddServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddServerActionPerformed(evt);
            }
        });
        jToolBarTop.add(jButtonAddServer);
        jToolBarTop.add(jSeparator1);

        jButtonChangeUserInfo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Edit16.gif"))); // NOI18N
        jButtonChangeUserInfo.setText("ID,PW ??????");
        jButtonChangeUserInfo.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonChangeUserInfo.setFocusable(false);
        jButtonChangeUserInfo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonChangeUserInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonChangeUserInfoActionPerformed(evt);
            }
        });
        jToolBarTop.add(jButtonChangeUserInfo);

        jButtonStopServer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Stop16.gif"))); // NOI18N
        jButtonStopServer.setText("stop server");
        jButtonStopServer.setToolTipText("????????? ?????????????????? ????????? ??????????????????.");
        jButtonStopServer.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonStopServer.setFocusable(false);
        jButtonStopServer.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonStopServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStopServerActionPerformed(evt);
            }
        });
        jToolBarTop.add(jButtonStopServer);
        jToolBarTop.add(jSeparator2);

        jButtonDeleteServer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Delete16.gif"))); // NOI18N
        jButtonDeleteServer.setText("server");
        jButtonDeleteServer.setToolTipText("????????? ?????????????????? ????????? ???????????????.");
        jButtonDeleteServer.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonDeleteServer.setFocusable(false);
        jButtonDeleteServer.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonDeleteServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteServerActionPerformed(evt);
            }
        });
        jToolBarTop.add(jButtonDeleteServer);
        jToolBarTop.add(jSeparator3);

        jButtonRefreshTree.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Refresh16.gif"))); // NOI18N
        jButtonRefreshTree.setText("Refresh tree");
        jButtonRefreshTree.setToolTipText("?????? ?????? ????????? ?????? ????????? ???????????????.");
        jButtonRefreshTree.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonRefreshTree.setFocusable(false);
        jButtonRefreshTree.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonRefreshTree.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRefreshTreeActionPerformed(evt);
            }
        });
        jToolBarTop.add(jButtonRefreshTree);

        jPanel1.add(jToolBarTop, java.awt.BorderLayout.NORTH);

        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jTabbedPane1.setMinimumSize(new java.awt.Dimension(54, 201));
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(54, 200));

        jPanelServers.setLayout(new java.awt.BorderLayout());
        jTabbedPane1.addTab("Servers", jPanelServers);

        jSplitPane1.setLeftComponent(jTabbedPane1);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jTextAreaStatus.setEditable(false);
        jTextAreaStatus.setColumns(20);
        jTextAreaStatus.setRows(5);
        jScrollPane1.setViewportView(jTextAreaStatus);

        jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jSplitPane1.setRightComponent(jPanel2);

        jPanel1.add(jSplitPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jMenuFile.setText("File");

        jMenuItemBackup.setText("??????");
        jMenuItemBackup.setToolTipText("?????? ?????? ?????? ?????? ????????????????????? ???????????????.");
        jMenuItemBackup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemBackupActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemBackup);

        jMenuBar1.add(jMenuFile);

        jMenuEdit.setText("Edit");

        jMenuItemLChangePassword.setText("login ???????????? ??????");
        jMenuItemLChangePassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemLChangePasswordActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuItemLChangePassword);
        jMenuEdit.add(jSeparator4);

        jMenuItemBlockWindow.setText("?????? ??????");
        jMenuItemBlockWindow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemBlockWindowActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuItemBlockWindow);

        jMenuItemSetBlockInterval.setText("?????? ?????? ???????????? ??????");
        jMenuItemSetBlockInterval.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSetBlockIntervalActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuItemSetBlockInterval);

        jMenuBar1.add(jMenuEdit);

        jMenuHelp.setText("Help");

        jMenuItem2.setText("????????????");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenuHelp.add(jMenuItem2);

        jMenuItem1.setText("???????????? ??? ??????");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenuHelp.add(jMenuItem1);

        jMenuItem3.setText("?????? ??????");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenuHelp.add(jMenuItem3);

        jMenuItem4.setText("Discussions");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenuHelp.add(jMenuItem4);

        jMenuItem5.setText("Issues");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenuHelp.add(jMenuItem5);
        jMenuHelp.add(jSeparator5);

        jMenuItem6.setText("?????? ??????");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenuHelp.add(jMenuItem6);

        jMenuBar1.add(jMenuHelp);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (glassPane.isVisible()) {
            logMessage("?????? ?????? ????????? ????????? ??? ????????????. ?????? ????????? ??????????????????.");
            return;
        }
        if (loginManager == null || loginManager.isLogoff()) {
            stopImmadiately();
        }
        String password = getPassword();
        if (!loginManager.checkPassword(password)) {
            logMessage("??????????????? ????????? ????????? ??? ????????????.");
            return;
        }
        stopAfterConfirm();
    }//GEN-LAST:event_formWindowClosing

    private void stopAfterConfirm() {
        int stop = JOptionPane.showConfirmDialog(this, "?????????????????????????", "?????? ??????", JOptionPane.OK_CANCEL_OPTION);
        if (stop != JOptionPane.OK_OPTION) {
            logMessage("????????? ?????????????????????.");
            return;
        }
        stopImmadiately();
    }

    private void stopImmadiately() {
        if (serverList == null) {
            System.exit(0);
        }
        for (int i = 0; i < serverList.size(); i++) {
            ServerMan serverMan = serverList.get(i);
            if (serverMan.isLocal() && serverMan.isRun()) {
                try {
                    serverMan.stop();
                } catch (SQLException ex) {
                    System.out.println("H2?????? ??? ????????? " + serverMan.getServerName() + " ?????? ?????? ??????: " + ex.getLocalizedMessage());
                }
            }
        }

        saveServerInformation();
        setVisible(false);
        System.exit(0);
    }

    private void saveServerInformation() {
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        Writer serverWriter = null;

        try {
            File dir = new File(SERVER_MEN_ENC_DIR);
            if (!dir.exists()) {
                FileUtils.forceMkdir(dir);
            }
            java.lang.reflect.Type serverType = new TypeToken<ServerMen>() {
            }.getType();
            serverWriter = new OutputStreamWriter(new FileOutputStream(SERVER_MEN_ENC_DIR + File.separator + SERVER_MEN_PATH), "UTF-8");
            BufferedWriter bufferedWriter = new BufferedWriter(serverWriter);
            gson.toJson(serverList, serverType, bufferedWriter);
            bufferedWriter.close();
            logMessage("?????? ?????? ?????? ???????????????.");
        } catch (UnsupportedEncodingException | FileNotFoundException ex) {
            logMessage("?????? ?????? ?????? ??? ??????: " + ex.getLocalizedMessage());
            sleep(2000);
        } catch (IOException ex) {
            logMessage("?????? ?????? ?????? ??? ??????: " + ex.getLocalizedMessage());
            sleep(2000);
        } catch (JsonIOException ex) {
            logMessage("?????? ?????? ?????? ??? ??????: " + ex.getLocalizedMessage());
            sleep(2000);
        } finally {
            if (serverWriter != null) {
                try {
                    serverWriter.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    private void sleep(int interval) {
        try {
            Thread.sleep(interval);
        } catch (InterruptedException ex) {
        }

    }

    private void jButtonStopServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStopServerActionPerformed
        TreePath selectionPath = serverTreePane.getSelectedPath();
        if (selectionPath == null || selectionPath.getPathCount() != 2) {
            logMessage("?????? ????????? ?????? ????????? ??????????????????.");
            return;
        }
        String serverName = selectionPath.getLastPathComponent().toString();
        ServerMan serverMan = findServerMan(serverName);
        if (serverMan == null) {
            logMessage("?????? ????????? ????????????: " + serverName);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append((serverMan.isLocal()) ? "localhost" : "remote").append(" ");
        sb.append(serverMan.getServerName()).append("??????");
        String title = sb.toString() + " ??????";
        String message = title + "???????????????????";
        int response = JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.NO_OPTION) {
            logMessage(" ?????? ????????? ?????????????????????");
            return;
        }
        try {
            if (serverMan.stop()) {
                logMessage("?????? ?????? ??????: " + serverMan.getMessage());
            } else {
                logMessage("?????? ?????? ??????: " + serverMan.getMessage());
            }
        } catch (SQLException ex) {
            logMessage("?????? ?????? ??? ??????: " + ex.getLocalizedMessage());
        }
    }//GEN-LAST:event_jButtonStopServerActionPerformed

    ServerMan findServerMan(String name) {
        for (int i = 0; i < serverList.size(); i++) {
            ServerMan serverMan = serverList.get(i);
            if (name.equals(serverMan.getServerName())) {
                return serverMan;
            }
        }
        return null;
    }

    private void jButtonRefreshTreeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRefreshTreeActionPerformed
        refreshTree();
    }//GEN-LAST:event_jButtonRefreshTreeActionPerformed

    private void refreshTree() {
        serverTreePane = new ServerTreePane(serverList, jTextAreaStatus, codeCompletionList, loginManager);
        TreePath selected = serverTreePane.getSelectedPath();

        H2AUtilities.setCenterComponent(jPanelServers, serverTreePane);
        serverTreePane.setSelectionPath(selected);
        serverTreePane.validate();
    }

    private void jButtonDeleteServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteServerActionPerformed
        TreePath path = serverTreePane.getSelectedPath();
        if (path == null || path.getPathCount() < 2) {
            logMessage("");
            return;
        }

        Object[] paths = path.getPath();
        String serverName = paths[1].toString();
        String msg = serverName + " ?????????????????? ?????? ????????? ?????????????????????????";
        int response = JOptionPane.showConfirmDialog(getRootPane(), msg, "?????? ?????? ?????? ??????", JOptionPane.OK_CANCEL_OPTION);
        if (response != JOptionPane.OK_OPTION) {
            logMessage(serverName + " ?????? ?????? ????????? ??????????????????.");
            return;
        }

        ServerMan deleted = serverList.delete(serverName);
        if (deleted == null) {
            logMessage(serverName + " ?????? ?????? ????????? ??????????????????.");
            return;
        }
        logMessage(serverName + " ?????? ?????? ????????? ??????????????????.");
        refreshTree();
    }//GEN-LAST:event_jButtonDeleteServerActionPerformed

    private void jButtonAddServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddServerActionPerformed
        AddServerDialog dialog = new AddServerDialog(this, true);
        dialog.setVisible(true);

        ServerMan serverMan = dialog.getServerMan();
        if (serverMan == null) {
            System.out.println(" ?????? ????????? ?????????????????????");
            return;
        }
        serverMan.encryptFields(loginManager);
        DatabaseMen databaseMen = serverMan.getDatabaseMen();
        for (int i = 0; i < databaseMen.size(); i++) {
            DatabaseMan databaseMan = databaseMen.get(i);
            databaseMan.encryptFields(loginManager);
        }
        if (serverList.addNew(serverMan)) {
            logMessage("server ??????: " + serverMan.getPort());
            refreshTree();
            saveServerInformation();
        } else {
            logMessage("server ?????? ??????: " + serverList.getMessage());
            dialog.dispose();
        }
    }//GEN-LAST:event_jButtonAddServerActionPerformed

    private void jMenuItemBlockWindowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemBlockWindowActionPerformed
        blockWindow("???????????? ????????? ?????????????????????.");
    }//GEN-LAST:event_jMenuItemBlockWindowActionPerformed

    private void jMenuItemSetBlockIntervalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSetBlockIntervalActionPerformed
        String num = JOptionPane.showInputDialog(this, "??? ????????? ???????????????.", "" + interval);
        try {
            interval = Integer.valueOf(num);
        } catch (NumberFormatException ex) {
            logMessage("????????? ???????????? ?????? ??? 5????????? ???????????????.");
            interval = 5;
        } catch (NullPointerException ex) {
            logMessage("null?????? ???????????? ?????? ??? 5????????? ???????????????.");
            interval = 5;
        }
        resetActivityTimer(interval);
        logMessage("?????? ????????? " + interval + "????????? ?????????????????????.");
    }//GEN-LAST:event_jMenuItemSetBlockIntervalActionPerformed

    private void jMenuItemBackupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemBackupActionPerformed
        serverTreePane.backup();
    }//GEN-LAST:event_jMenuItemBackupActionPerformed

    private void jMenuItemLChangePasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemLChangePasswordActionPerformed
        ChangePasswordDialog dialog = new ChangePasswordDialog(this, true, loginManager);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }//GEN-LAST:event_jMenuItemLChangePasswordActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        String uri = "https://github.com/doljin99/H2Admin/wiki/Tutorial--%EB%B0%8F-%EC%98%88%EC%A0%9C-%EC%9E%90%EB%A3%8C";
        openBrowser(uri);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        String uri = "https://github.com/doljin99/H2Admin";
        openBrowser(uri);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        String uri = "https://github.com/doljin99/H2Admin/releases";
        openBrowser(uri);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        String uri = "https://github.com/doljin99/H2Admin/discussions";
        openBrowser(uri);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        String uri = "https://github.com/doljin99/H2Admin/discussions";
        openBrowser(uri);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        H2AAbout about = new H2AAbout(Constants.FULL_VERSION, VERSION);
        JDialog dialog = new JDialog(this, "????????????", true);
        dialog.setLayout(new BorderLayout());
        dialog.add(about, BorderLayout.CENTER);
        dialog.setSize(500, 260);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jButtonChangeUserInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonChangeUserInfoActionPerformed
        TreePath path = serverTreePane.getSelectedPath();
        if (path == null || path.getPathCount() != 2) {
            logMessage("????????? ????????? ????????? ?????? ????????? ??????????????????.");
            return;
        }
        String serverName = path.getLastPathComponent().toString();
        ServerMan serverMan = findServerMan(serverName);
        if (serverMan == null) {
            logMessage("?????? ????????? ????????????: " + serverName);
            return;
        }
        ChangeUserInfoDialog dialog = new ChangeUserInfoDialog(this, true, serverMan, loginManager);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }//GEN-LAST:event_jButtonChangeUserInfoActionPerformed

    private void openBrowser(String uri) {
        try {
            Desktop.getDesktop().browse(URI.create(uri));
        } catch (IOException ex) {
            logMessage("??????????????? ?????? ?????? ?????? ????????? ???????????????: " + uri);
        }
    }

    private void resetActivityTimer() {
        resetActivityTimer(5);
    }

    private void stopActivityTimer() {
        if (activityTimer != null) {
            activityTimer.stop();
            logMessage("activity listener stop.");
        } else {
            logMessage("activity listener can't stop.");
        }
    }

    private void resetActivityTimer(int time) {
        interval = time;
        actionTimeOutListener = new ActionTimeOutListener();
        activityTimer = new InactivityListener(this, actionTimeOutListener, interval);
        activityTimer.start();
    }

    private void logMessage(String msg) {
        jTextAreaStatus.append(msg + "\n");
        jTextAreaStatus.setCaretPosition(jTextAreaStatus.getText().length() - 1);
        jTextAreaStatus.validate();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(H2ServerAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new H2ServerAdmin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddServer;
    private javax.swing.JButton jButtonChangeUserInfo;
    private javax.swing.JButton jButtonDeleteServer;
    private javax.swing.JButton jButtonRefreshTree;
    private javax.swing.JButton jButtonStopServer;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu jMenuEdit;
    private javax.swing.JMenu jMenuFile;
    private javax.swing.JMenu jMenuHelp;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItemBackup;
    private javax.swing.JMenuItem jMenuItemBlockWindow;
    private javax.swing.JMenuItem jMenuItemLChangePassword;
    private javax.swing.JMenuItem jMenuItemSetBlockInterval;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelServers;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextAreaStatus;
    private javax.swing.JToolBar jToolBarTop;
    // End of variables declaration//GEN-END:variables
}
