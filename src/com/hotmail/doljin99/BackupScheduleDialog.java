/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.hotmail.doljin99;

import com.hotmail.doljin99.loginmanager.CronTime;
import com.hotmail.doljin99.loginmanager.LoginManager;
import java.awt.Cursor;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.h2.tools.Backup;

/**
 *
 * @author dolji
 */
public class BackupScheduleDialog extends javax.swing.JDialog {

    private final ServerMan serverMan;
    private final DatabaseMan databaseMan;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    private static final String EXTENSION = ".zip";

    private boolean done;
    private String message;
    private final BackupSchedules backupSchedules;
    private final LoginManager loginManager;

    /**
     * Creates new form BackupDialog
     *
     * @param parent
     * @param modal
     * @param serverMan
     * @param databaseMan
     * @param backupSchedules
     * @param loginManager
     */
    public BackupScheduleDialog(java.awt.Frame parent, boolean modal,
        ServerMan serverMan, DatabaseMan databaseMan, BackupSchedules backupSchedules,
        LoginManager loginManager) {
        super(parent, modal);
        initComponents();
        this.serverMan = serverMan;
        this.databaseMan = databaseMan;
        this.backupSchedules = backupSchedules;
        this.loginManager = loginManager;

        init();
    }

    private void init() {
        jTextFieldServerName.setText(serverMan.getServerName());
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
        jTextFieldDatabaseName.setText(dbName);

        jTextFieldCronName.getDocument().addDocumentListener(new CronNameDocumentListener());
        jTableSchedulesTable.getSelectionModel().addListSelectionListener(new ScheduleListSelectionListener());

        setBackupSchedules();
        setInitialDate();

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

    class ScheduleListSelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            int row = jTableSchedulesTable.getSelectedRow();
            if (row < 0 || row >= jTableSchedulesTable.getRowCount()) {
                return;
            }

            BackupSchedule backupSchedule = backupSchedules.getBackupSchedule(
                (String) jTableSchedulesTable.getValueAt(row, 1),
                (String) jTableSchedulesTable.getValueAt(row, 2));
            if (backupSchedule == null) {
                return;
            }
            displayScheduleValues(backupSchedule);
        }
    }

    private BackupSchedule makeBackupSchedule() {
        BackupSchedule backupSchedule = new BackupSchedule();

        backupSchedule.setServerName(jTextFieldServerName.getText());
        backupSchedule.setCronName(jTextFieldCronName.getText());
        backupSchedule.setMinValue(jTextFieldMinPart.getText());
        backupSchedule.setHourValue(jTextFieldHourPart.getText());
        backupSchedule.setDayOfMonthValue(jTextFieldDayOfMonthPart.getText());
        backupSchedule.setMonthValue(jTextFieldMonthPart.getText());
        backupSchedule.setDayOfWeekValue(jTextFieldDayOfWeekPart.getText());
        long startDate = makeStartDate();
        if (startDate < 0) {
            return null;
        }
        long endDate = makeEndDate();
        if (endDate < 0) {
            return null;
        }
        backupSchedule.setStartDate(makeStartDate());
        backupSchedule.setEndDate(makeEndDate());
        backupSchedule.setDatabaseName(jTextFieldDatabaseName.getText());
        backupSchedule.setBackupDir(jTextFieldBackupDir.getText());
        backupSchedule.setBackupFile(jTextFieldBackupFileName.getText());
        backupSchedule.setEntryDatetime(System.currentTimeMillis());

        backupSchedule.encryptFields(loginManager);

        return backupSchedule;
    }

    private long makeStartDate() {
        try {
            long timeMillis = -1;
            Calendar calendar = Calendar.getInstance();

            int year = Integer.valueOf(jTextFieldStartYear.getText());
            int month = Integer.valueOf(jTextFieldStartMonth.getText());
            int day = Integer.valueOf(jTextFieldStartDay.getText());

            calendar.set(Calendar.YEAR, year);
            if (month < 1 || month > 12) {
                setStatus("시작 월 값 범위 에러: " + month);
                return timeMillis;
            }
            calendar.set(Calendar.MONTH, month - 1);
            int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            if (day < 1 || day > maxDay) {
                setStatus("시작 일 값 범위 에러: " + month);
                return timeMillis;
            }
            calendar.set(Calendar.DAY_OF_MONTH, day);

            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            timeMillis = calendar.getTimeInMillis();

            return timeMillis;
        } catch (NullPointerException | NumberFormatException ex) {
            setStatus("일자 정보 에러: " + ex.getLocalizedMessage());
            return -1;
        }
    }

    private long makeEndDate() {
        try {
            long timeMillis = -1;
            Calendar calendar = Calendar.getInstance();

            int year = Integer.valueOf(jTextFieldEndYear.getText());
            int month = Integer.valueOf(jTextFieldEndMonth.getText());
            int day = Integer.valueOf(jTextFieldEndDay.getText());

            calendar.set(Calendar.YEAR, year);
            if (month < 1 || month > 12) {
                setStatus("종료 월 값 범위 에러: " + month);
                return timeMillis;
            }
            calendar.set(Calendar.MONTH, month - 1);
            int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            if (day < 1 || day > maxDay) {
                setStatus("종료 일 값 범위 에러: " + month);
                return timeMillis;
            }
            calendar.set(Calendar.DAY_OF_MONTH, day);

            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);

            timeMillis = calendar.getTimeInMillis();

            return timeMillis;
        } catch (NullPointerException | NumberFormatException ex) {
            setStatus("일자 정보 에러: " + ex.getLocalizedMessage());
            return -1;
        }
    }

    private void displayScheduleValues(BackupSchedule backupSchedule) {
        if (serverMan.isRun()) {
            jRadioButtonOnline.setSelected(true);
        } else {
            jRadioButtonOffline.setSelected(true);
        }
        jTextFieldServerName.setText(backupSchedule.getServerName());
        jTextFieldDatabaseName.setText(backupSchedule.getDatabaseName());
        jTextFieldCronName.setText(backupSchedule.getCronName());
        jTextFieldBaseDir.setText(backupSchedule.getBackupDir());
        jTextFieldBackupDir.setText(backupSchedule.getBackupDir());
        jTextFieldBackupFileName.setText(makeBackupFilename(backupSchedule.getServerName(), backupSchedule.getDatabaseName()));

        jTextFieldMinPart.setText(backupSchedule.getMinValue());
        jTextFieldHourPart.setText(backupSchedule.getHourValue());
        jTextFieldDayOfMonthPart.setText(backupSchedule.getDayOfMonthValue());
        jTextFieldMonthPart.setText(backupSchedule.getMonthValue());
        jTextFieldDayOfWeekPart.setText(backupSchedule.getDayOfWeekValue());

        Date startDate = new Date(backupSchedule.getStartDateMillis());
        String startDateString = dateFormat.format(startDate);
        String[] items = startDateString.split("-");
        jTextFieldStartYear.setText(items[0]);
        jTextFieldStartMonth.setText(items[1]);
        jTextFieldStartDay.setText(items[2]);

        Date endDate = new Date(backupSchedule.getEndDateMillis());
        String endDateString = dateFormat.format(endDate);
        items = endDateString.split("-");
        jTextFieldEndYear.setText(items[0]);
        jTextFieldEndMonth.setText(items[1]);
        jTextFieldEndDay.setText(items[2]);
    }

    class CronNameDocumentListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            doAction();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            doAction();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            doAction();
        }

        private void doAction() {
            if (backupSchedules.exist(jTextFieldServerName.getText(), jTextFieldCronName.getText())) {
                jButtonAdd.setEnabled(false);
                jButtonUpdate.setEnabled(true);
                jButtonDelete.setEnabled(true);
            } else {
                jButtonAdd.setEnabled(true);
                jButtonUpdate.setEnabled(false);
                jButtonDelete.setEnabled(false);
            }
        }
    }

    private void setBackupSchedules() {
        DefaultTableModel model = (DefaultTableModel) jTableSchedulesTable.getModel();
        model.setRowCount(0);
        if (backupSchedules == null) {
            return;
        }
        Object[] row = new Object[3];
        for (int i = 0; i < backupSchedules.size(); i++) {
            BackupSchedule backupSchedule = backupSchedules.get(i);
            row[0] = i + 1;
            row[1] = backupSchedule.getServerName();
            row[2] = backupSchedule.getCronName();

            model.addRow(row);
        }
        H2AUtilities.alignColumnWidth(jTableSchedulesTable);
        jTableSchedulesTable.validate();
    }

    private void setInitialDate() {
        Calendar calendar = Calendar.getInstance();
        String currentDate = dateFormat.format(calendar.getTime());

        String[] items = currentDate.split("-");
        jTextFieldStartYear.setText(items[0]);
        jTextFieldStartMonth.setText(items[1]);
        jTextFieldStartDay.setText(items[2]);

        jTextFieldEndYear.setText("9999");
        jTextFieldEndMonth.setText("12");
        jTextFieldEndDay.setText("31");

        jTextFieldTestYear.setText(items[0]);
        jTextFieldTestMonth.setText(items[1]);
        jTextFieldTestDay.setText(items[2]);
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
        jButtonAdd = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jButtonUpdate = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jButtonDelete = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        jButtonCancel = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel3 = new javax.swing.JPanel();
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
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableSchedulesTable = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        jTextFieldCronName = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jTextFieldServerName = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jTextFieldDatabaseName = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jTextFieldMinPart = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTextFieldHourPart = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextFieldDayOfMonthPart = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextFieldMonthPart = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTextFieldDayOfWeekPart = new javax.swing.JTextField();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTextAreaTimes = new javax.swing.JTextArea();
        jLabel17 = new javax.swing.JLabel();
        jTextFieldStartYear = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldStartMonth = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldStartDay = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jTextFieldEndYear = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldEndMonth = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldEndDay = new javax.swing.JTextField();
        jTextFieldTestYear = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldTestMonth = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextFieldTestDay = new javax.swing.JTextField();
        jToolBar2 = new javax.swing.JToolBar();
        jButtonSimulateDay = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jButtonSimulateYear = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jButtonStopSimulation = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        jButtonCopyResult = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaStatus = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("정기 백업 관리");

        jToolBar1.setRollover(true);

        jButtonAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/SaveAll16.gif"))); // NOI18N
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
        jToolBar1.add(jSeparator4);

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
        jToolBar1.add(jSeparator5);

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

        getContentPane().add(jToolBar1, java.awt.BorderLayout.NORTH);

        jSplitPane1.setDividerLocation(500);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jLabel1.setText("base dir");
        jLabel1.setToolTipText("");

        jTextFieldBaseDir.setEditable(false);

        buttonGroupOnOffline.add(jRadioButtonOnline);
        jRadioButtonOnline.setSelected(true);
        jRadioButtonOnline.setText("Online");
        jRadioButtonOnline.setEnabled(false);

        buttonGroupOnOffline.add(jRadioButtonOffline);
        jRadioButtonOffline.setText("Offline");
        jRadioButtonOffline.setEnabled(false);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 204)), "백업 대상 DB", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("맑은 고딕", 0, 12), new java.awt.Color(0, 51, 204))); // NOI18N

        buttonGroupDBSelect.add(jRadioButtonAllDB);
        jRadioButtonAllDB.setSelected(true);
        jRadioButtonAllDB.setText("모든 DB");
        jRadioButtonAllDB.setToolTipText("서버의 모든 DB를 백업");
        jRadioButtonAllDB.setEnabled(false);

        buttonGroupDBSelect.add(jRadioButtonSelectedDB);
        jRadioButtonSelectedDB.setText("선택된 DB");
        jRadioButtonSelectedDB.setToolTipText("서버에서 선택한 DB만 백업");
        jRadioButtonSelectedDB.setEnabled(false);

        buttonGroupDBSelect.add(jRadioButtonOnlyOneDB);
        jRadioButtonOnlyOneDB.setText("유일한DB");
        jRadioButtonOnlyOneDB.setToolTipText("서버에 DB 한개만 있음");
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
                .addContainerGap(132, Short.MAX_VALUE))
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
        jButtonBackupDir.setText("백업 dir");
        jButtonBackupDir.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonBackupDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBackupDirActionPerformed(evt);
            }
        });

        jTextFieldBackupDir.setEditable(false);
        jTextFieldBackupDir.setText("~");

        jLabel2.setText("백업 파일");

        jTextFieldBackupFileName.setEditable(false);
        jTextFieldBackupFileName.setText("serverName-dbName-yyyy-MM-dd-HH-mm-ss.zip");
        jTextFieldBackupFileName.setToolTipText("시간은 현재 시간을 보여주며 백업작업 당시 시간으로 설정됩니다.");

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 204)), "정기백업 목록", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("맑은 고딕", 0, 12), new java.awt.Color(0, 51, 204))); // NOI18N
        jPanel5.setLayout(new java.awt.BorderLayout());

        jTableSchedulesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "순번", "서버명", "제목"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTableSchedulesTable);

        jPanel5.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jLabel14.setText("제목");

        jLabel15.setText("서버명");

        jTextFieldServerName.setEditable(false);

        jLabel19.setText("DB명");

        jTextFieldDatabaseName.setEditable(false);

        javax.swing.GroupLayout jPanelOfflineLayout = new javax.swing.GroupLayout(jPanelOffline);
        jPanelOffline.setLayout(jPanelOfflineLayout);
        jPanelOfflineLayout.setHorizontalGroup(
            jPanelOfflineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelOfflineLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelOfflineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE)
                    .addGroup(jPanelOfflineLayout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldServerName))
                    .addGroup(jPanelOfflineLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(34, 34, 34)
                        .addComponent(jTextFieldBaseDir))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanelOfflineLayout.createSequentialGroup()
                        .addGroup(jPanelOfflineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonBackupDir)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanelOfflineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldBackupDir)
                            .addComponent(jTextFieldBackupFileName, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)))
                    .addGroup(jPanelOfflineLayout.createSequentialGroup()
                        .addComponent(jRadioButtonOnline)
                        .addGap(18, 18, 18)
                        .addComponent(jRadioButtonOffline)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanelOfflineLayout.createSequentialGroup()
                        .addGroup(jPanelOfflineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19)
                            .addComponent(jLabel14))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanelOfflineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldCronName)
                            .addComponent(jTextFieldDatabaseName))))
                .addContainerGap())
        );
        jPanelOfflineLayout.setVerticalGroup(
            jPanelOfflineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelOfflineLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelOfflineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jTextFieldServerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelOfflineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jTextFieldDatabaseName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelOfflineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jTextFieldCronName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelOfflineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButtonOnline)
                    .addComponent(jRadioButtonOffline))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelOfflineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldBaseDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelOfflineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonBackupDir)
                    .addComponent(jTextFieldBackupDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelOfflineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextFieldBackupFileName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(jPanelOffline, java.awt.BorderLayout.CENTER);

        jSplitPane1.setLeftComponent(jPanel3);

        jLabel16.setText("특정일자");

        jLabel9.setText("분");

        jTextFieldMinPart.setText("*");
        jTextFieldMinPart.setToolTipText(" [0,59]");
        jTextFieldMinPart.setMaximumSize(new java.awt.Dimension(50, 2147483647));
        jTextFieldMinPart.setMinimumSize(new java.awt.Dimension(50, 23));
        jTextFieldMinPart.setPreferredSize(new java.awt.Dimension(70, 23));

        jLabel10.setText("시");

        jTextFieldHourPart.setText("*");
        jTextFieldHourPart.setToolTipText(" [0,23]");
        jTextFieldHourPart.setMaximumSize(new java.awt.Dimension(50, 2147483647));
        jTextFieldHourPart.setMinimumSize(new java.awt.Dimension(50, 23));
        jTextFieldHourPart.setPreferredSize(new java.awt.Dimension(70, 23));

        jLabel11.setText("일");

        jTextFieldDayOfMonthPart.setText("*");
        jTextFieldDayOfMonthPart.setToolTipText(" [1,31], L -> 해당월의 마지막일, L-n -> 해당월의 마지막일의 n번째 전일");
        jTextFieldDayOfMonthPart.setMaximumSize(new java.awt.Dimension(50, 2147483647));
        jTextFieldDayOfMonthPart.setMinimumSize(new java.awt.Dimension(50, 23));
        jTextFieldDayOfMonthPart.setPreferredSize(new java.awt.Dimension(70, 23));

        jLabel12.setText("월");

        jTextFieldMonthPart.setText("*");
        jTextFieldMonthPart.setToolTipText("[1,12]");
        jTextFieldMonthPart.setMaximumSize(new java.awt.Dimension(50, 2147483647));
        jTextFieldMonthPart.setMinimumSize(new java.awt.Dimension(50, 23));
        jTextFieldMonthPart.setPreferredSize(new java.awt.Dimension(70, 23));

        jLabel13.setText("요일");

        jTextFieldDayOfWeekPart.setText("*");
        jTextFieldDayOfWeekPart.setToolTipText("[1~7] -> [일~토], m#n(L) -> n(마지막)주차 m요일");
        jTextFieldDayOfWeekPart.setMaximumSize(new java.awt.Dimension(50, 2147483647));
        jTextFieldDayOfWeekPart.setMinimumSize(new java.awt.Dimension(50, 23));
        jTextFieldDayOfWeekPart.setPreferredSize(new java.awt.Dimension(70, 23));

        jTextAreaTimes.setColumns(20);
        jTextAreaTimes.setRows(5);
        jScrollPane6.setViewportView(jTextAreaTimes);

        jLabel17.setText("시작일자");

        jTextFieldStartYear.setText("2022");

        jLabel3.setText("-");

        jTextFieldStartMonth.setText("09");

        jLabel4.setText("-");

        jTextFieldStartDay.setText("02");

        jLabel18.setText("종료일자");

        jTextFieldEndYear.setText("9999");

        jLabel5.setText("-");

        jTextFieldEndMonth.setText("12");

        jLabel6.setText("-");

        jTextFieldEndDay.setText("31");

        jTextFieldTestYear.setText("2022");

        jLabel7.setText("-");

        jTextFieldTestMonth.setText("09");

        jLabel8.setText("-");

        jTextFieldTestDay.setText("02");

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        jButtonSimulateDay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Search16.gif"))); // NOI18N
        jButtonSimulateDay.setText("특정 일자 검사");
        jButtonSimulateDay.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonSimulateDay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSimulateDayActionPerformed(evt);
            }
        });
        jToolBar2.add(jButtonSimulateDay);
        jToolBar2.add(jSeparator2);

        jButtonSimulateYear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Search16.gif"))); // NOI18N
        jButtonSimulateYear.setText("1년 simulation");
        jButtonSimulateYear.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonSimulateYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSimulateYearActionPerformed(evt);
            }
        });
        jToolBar2.add(jButtonSimulateYear);
        jToolBar2.add(jSeparator3);

        jButtonStopSimulation.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Stop16.gif"))); // NOI18N
        jButtonStopSimulation.setText("simulation정지");
        jButtonStopSimulation.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonStopSimulation.setEnabled(false);
        jButtonStopSimulation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStopSimulationActionPerformed(evt);
            }
        });
        jToolBar2.add(jButtonStopSimulation);
        jToolBar2.add(jSeparator6);

        jButtonCopyResult.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Copy16.gif"))); // NOI18N
        jButtonCopyResult.setText("결과복사");
        jButtonCopyResult.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonCopyResult.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCopyResultActionPerformed(evt);
            }
        });
        jToolBar2.add(jButtonCopyResult);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(23, 23, 23)
                        .addComponent(jTextFieldMinPart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldHourPart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldMonthPart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldDayOfMonthPart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldDayOfWeekPart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldTestYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldTestMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(7, 7, 7)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldTestDay, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldStartYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldStartMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)
                                .addGap(7, 7, 7)
                                .addComponent(jTextFieldStartDay, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldEndYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldEndMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldEndDay, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 73, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jTextFieldStartYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jTextFieldStartMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jTextFieldStartDay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(jTextFieldEndYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jTextFieldEndMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jTextFieldEndDay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jTextFieldTestYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jTextFieldTestMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jTextFieldTestDay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jTextFieldMinPart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldHourPart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jTextFieldDayOfMonthPart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jTextFieldMonthPart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jTextFieldDayOfWeekPart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                .addContainerGap())
        );

        jSplitPane1.setRightComponent(jPanel4);

        getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jTextAreaStatus.setColumns(20);
        jTextAreaStatus.setRows(5);
        jScrollPane1.setViewportView(jTextAreaStatus);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void chooseBackupDir() {
        File directory = H2AUtilities.chooseDirectory("H2 DB Backup 디렉터리 선택");
        if (directory == null) {
            setStatus("백업 디렉터리 선택을 취소하였습니다.");
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
    }

    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActionPerformed
        BackupSchedule backupSchedule = makeBackupSchedule();
        if (backupSchedule == null) {
            setStatus("추가할 정기 백업 정보 에러.");
            return;
        }
        if (backupSchedules.addBackupSchedule(backupSchedule) == null) {
            setStatus("정기 백업 추가 실패: " + backupSchedule.getServerName() + ", " + backupSchedule.getCronName());
        } else {
            setStatus("정기 백업 추가 성공: " + backupSchedule.getServerName() + ", " + backupSchedule.getCronName());
            refreshBackupScheduleGrid();
        }
    }//GEN-LAST:event_jButtonAddActionPerformed

    private void refreshBackupScheduleGrid() {
        int selectedRow = jTableSchedulesTable.getSelectedRow();
        setBackupSchedules();
        if (selectedRow >= 0 && selectedRow < jTableSchedulesTable.getRowCount()) {
            jTableSchedulesTable.getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
        }
    }

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
        Connection connection = serverMan.getConnection(databaseMan.getDatabaseName());
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
        } finally {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        done = false;
        message = "백업을 취소하였습니다.";
        setVisible(false);
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonBackupDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBackupDirActionPerformed
        chooseBackupDir();
    }//GEN-LAST:event_jButtonBackupDirActionPerformed

    private void jButtonSimulateDayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSimulateDayActionPerformed
        jTextAreaTimes.setText("");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd E HH:mm:ss");
        Calendar start = getStartDate();
        Calendar end = getEndDate();
        Calendar testDate = getTestDate();
        if (start == null || end == null || testDate == null) {
            return;
        }
        System.out.println("start = " + format.format(start.getTime()));
        System.out.println("end = " + format.format(end.getTime()));
        System.out.println("testDate = " + format.format(testDate.getTime()));
        CronTime cronTime = new CronTime();
//            start.getTimeInMillis(),
//            end.getTimeInMillis(),
//            jTextFieldMinPart.getText(), jTextFieldHourPart.getText(),
//            jTextFieldDayOfMonthPart.getText(),
//            jTextFieldMonthPart.getText(),
//            jTextFieldDayOfWeekPart.getText());
        cronTime.setStartDate(start);
        cronTime.setEndDate(end);
        cronTime.setMinValue(jTextFieldMinPart.getText());
        cronTime.setHourValue(jTextFieldHourPart.getText());
        cronTime.setDayOfMonthValue(jTextFieldDayOfMonthPart.getText().toUpperCase());
        cronTime.setMonthValue(jTextFieldMonthPart.getText());
        cronTime.setDayOfWeekValue(jTextFieldDayOfWeekPart.getText().toUpperCase());

        Date date = new Date();
        ArrayList<Long> calculated = cronTime.getWaitTimesInOneDay(testDate.getTimeInMillis());
        if (calculated == null) {
            setStatus("에러: " + cronTime.getMessage());
            return;
        }
        if (calculated.isEmpty()) {
            setStatus("범위 밖 결과: " + cronTime.getMessage());
            return;
        }
        for (int i = 0; i < calculated.size(); i++) {
            Long timeInMillis = calculated.get(i);
            date.setTime(timeInMillis);
            appendDate((i + 1) + " - " + format.format(date));
        }
    }//GEN-LAST:event_jButtonSimulateDayActionPerformed

    private Calendar getStartDate() {
        return getDate(jTextFieldStartYear.getText(), jTextFieldStartMonth.getText(), jTextFieldStartDay.getText(), true);
    }

    private Calendar getEndDate() {
        return getDate(jTextFieldEndYear.getText(), jTextFieldEndMonth.getText(), jTextFieldEndDay.getText(), false);
    }

    private Calendar getTestDate() {
        return getDate(jTextFieldTestYear.getText(), jTextFieldTestMonth.getText(), jTextFieldTestDay.getText(), true);
    }

    private Calendar getDate(String yearString, String monthString, String dayString, boolean start) {
        int year;
        try {
            year = Integer.valueOf(yearString);
        } catch (NumberFormatException ex) {
            return null;
        }
        int month;
        try {
            month = Integer.valueOf(monthString);
            if (month < 1 || month > 12) {
                return null;
            }
        } catch (NumberFormatException ex) {
            return null;
        }
        int day;
        try {
            day = Integer.valueOf(dayString);
            if (day < 1 || month > 31) {
                return null;
            }
        } catch (NumberFormatException ex) {
            return null;
        }
        if (!isValidDate(year, month, day)) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        if (start) {
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        } else {
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
        }

        return calendar;
    }

    private final int[] largeMonth = {1, 3, 5, 7, 8, 10, 12};

    private boolean isValidDate(int year, int month, int day) {
        if (month < 1 || month > 12) {
            return false;
        }
        if (day < 1 || month > 31) {
            return false;
        }

        int lastDayOfMonth = getLastDayOfMonth(year, month);
        return day <= lastDayOfMonth;
    }

    private int getLastDayOfMonth(int year, int month) {
        int lastDayOfMonth;
        if (month == 2) {
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                lastDayOfMonth = 29;
            } else {
                lastDayOfMonth = 28;
            }
            return lastDayOfMonth;
        }
        for (int i = 0; i < largeMonth.length; i++) {
            if (largeMonth[i] == month) {
                return 31;
            }
        }
        return 30;
    }

    class SimulatorRunnable implements Runnable {

        private boolean run = true;
        private int year;

        public SimulatorRunnable(int year) {
            this.year = year;
        }

        @Override
        public void run() {
            try {
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                jButtonSimulateYear.setEnabled(false);
                jButtonSimulateDay.setEnabled(false);
                jButtonStopSimulation.setEnabled(true);
                
                StringBuilder sb = new StringBuilder();
                sb.append("서버명: ").append(jTextFieldServerName.getText()).append("\n");
                sb.append("DB명: ").append(jTextFieldDatabaseName.getText()).append("\n");
                sb.append("cron명: ").append(jTextFieldCronName.getText()).append("\n");
                
                sb.append("cron 표현식: ");
                sb.append(" [ ").append(jTextFieldMinPart.getText()).append(" ] ").append("~");
                sb.append(" [ ").append(jTextFieldHourPart.getText()).append(" ] ").append("~");
                sb.append(" [ ").append(jTextFieldDayOfMonthPart.getText().toUpperCase()).append(" ] ").append("~");
                sb.append(" [ ").append(jTextFieldMonthPart.getText()).append(" ] ").append("~");
                sb.append(" [ ").append(jTextFieldDayOfWeekPart.getText().toUpperCase()).append(" ]");
                appendDate(sb.toString());
                
                Calendar startDate = Calendar.getInstance();
                Calendar endDate = Calendar.getInstance();
                startDate.set(Calendar.YEAR, year);
                startDate.set(Calendar.MONTH, 0);
                startDate.set(Calendar.DATE, 1);
                startDate.set(Calendar.HOUR_OF_DAY, 0);
                startDate.set(Calendar.MINUTE, 0);
                startDate.set(Calendar.SECOND, 0);
                startDate.set(Calendar.MILLISECOND, 0);
                endDate.set(Calendar.YEAR, year);
                endDate.set(Calendar.MONTH, 11);
                endDate.set(Calendar.DATE, 31);
                endDate.set(Calendar.HOUR_OF_DAY, 23);
                endDate.set(Calendar.MINUTE, 59);
                endDate.set(Calendar.SECOND, 59);
                endDate.set(Calendar.MILLISECOND, 999);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd E HH:mm:ss");
                Date applyDate = startDate.getTime();
                Date firedDate = endDate.getTime();
                appendDate("적용 일자: " + format.format(applyDate));
                appendDate("종료 일자: " + format.format(firedDate));
                ArrayList<Long> calculated;
                Date date = new Date();

                CronTime cronTime = new CronTime();
                cronTime.setStartDate(startDate);
                cronTime.setEndDate(endDate);
                cronTime.setMinValue(jTextFieldMinPart.getText());
                cronTime.setHourValue(jTextFieldHourPart.getText());
                cronTime.setDayOfMonthValue(jTextFieldDayOfMonthPart.getText());
                cronTime.setMonthValue(jTextFieldMonthPart.getText());
                cronTime.setDayOfWeekValue(jTextFieldDayOfWeekPart.getText());

                int count = 0;
                for (int i = 0; run && i < endDate.getActualMaximum(Calendar.DAY_OF_YEAR); i++) {
                    try {
                        calculated = cronTime.getWaitTimesInOneDay(startDate);
                        if (calculated == null) {
                            setStatus("에러: " + cronTime.getMessage());
                            return;
                        }
                        if (calculated.isEmpty()) {
//                            setStatus("범위 밖 결과: " + cronTimeValue.getMessage());
                            continue;
                        }
                        for (int j = 0; j < calculated.size(); j++) {
                            Long timeInMillis = calculated.get(j);
                            date.setTime(timeInMillis);
                            appendDate((++count) + " - " + format.format(date));
                        }
                    } finally {
                        startDate.add(Calendar.DATE, 1);
                    }
                }
            } finally {
                jButtonSimulateYear.setEnabled(true);
                jButtonSimulateDay.setEnabled(true);
                jButtonStopSimulation.setEnabled(false);
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        }

        public void stop() {
            run = false;
        }
    }

    private void appendDate(String date) {
        jTextAreaTimes.append(date + "\n");
        jTextAreaTimes.setCaretPosition(jTextAreaTimes.getText().length() - 1);
        jTextAreaTimes.validate();
    }

    private SimulatorRunnable simulatorRunnable;
    private void jButtonSimulateYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSimulateYearActionPerformed
        jTextAreaTimes.setText("");
        int year;
        try {
            year = Integer.valueOf(jTextFieldStartYear.getText());
        } catch (NumberFormatException ex) {
            year = Calendar.getInstance().get(Calendar.YEAR);
        }
        simulatorRunnable = new SimulatorRunnable(year);
        Thread simulator = new Thread(simulatorRunnable);
        simulator.setDaemon(true);
        simulator.start();
    }//GEN-LAST:event_jButtonSimulateYearActionPerformed

    private void jButtonStopSimulationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStopSimulationActionPerformed
        if (simulatorRunnable != null) {
            simulatorRunnable.stop();
        }
    }//GEN-LAST:event_jButtonStopSimulationActionPerformed

    private void jButtonUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUpdateActionPerformed
        BackupSchedule backupSchedule = makeBackupSchedule();
        if (backupSchedule == null) {
            setStatus("수절할 정기 백업 정보 에러.");
            return;
        }
        if (backupSchedules.updateBackupSchedule(backupSchedule) == null) {
            setStatus("정기 백업 수정 실패: " + backupSchedule.getServerName() + ", " + backupSchedule.getCronName());
        } else {
            setStatus("정기 백업 수정 성공: " + backupSchedule.getServerName() + ", " + backupSchedule.getCronName());
        }
    }//GEN-LAST:event_jButtonUpdateActionPerformed

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed
        String key = BackupSchedules.makeKey(jTextFieldServerName.getText(), jTextFieldCronName.getText());
        int response = JOptionPane.showConfirmDialog(this, key + "을(를) 삭제하겠습니까?", "정기 백업 삭제 확인", JOptionPane.OK_CANCEL_OPTION);
        if (response != JOptionPane.OK_OPTION) {
            setStatus(key + " : 삭제 취소 되었습니다.");
            return;
        }
        BackupSchedule backupSchedule = backupSchedules.getBackupSchedule(jTextFieldServerName.getText(), jTextFieldCronName.getText());
        if (backupSchedules.deleteSchedule(backupSchedule)) {
            setStatus(key + " : 삭제되었습니다.");
            refreshBackupScheduleGrid();
        } else {
            setStatus(key + " : 삭제 실패하였습니다.");
        }
    }//GEN-LAST:event_jButtonDeleteActionPerformed

    private void jButtonCopyResultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCopyResultActionPerformed
        jTextAreaTimes.copy();
        setStatus("결과가 클립보드에 복사되었습니다.");
    }//GEN-LAST:event_jButtonCopyResultActionPerformed

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
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonBackupDir;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonCopyResult;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonSimulateDay;
    private javax.swing.JButton jButtonSimulateYear;
    private javax.swing.JButton jButtonStopSimulation;
    private javax.swing.JButton jButtonUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanelOffline;
    private javax.swing.JRadioButton jRadioButtonAllDB;
    private javax.swing.JRadioButton jRadioButtonOffline;
    private javax.swing.JRadioButton jRadioButtonOnline;
    private javax.swing.JRadioButton jRadioButtonOnlyOneDB;
    private javax.swing.JRadioButton jRadioButtonSelectedDB;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTable jTableSchedulesTable;
    private javax.swing.JTextArea jTextAreaStatus;
    private javax.swing.JTextArea jTextAreaTimes;
    private javax.swing.JTextField jTextFieldBackupDir;
    private javax.swing.JTextField jTextFieldBackupFileName;
    private javax.swing.JTextField jTextFieldBaseDir;
    private javax.swing.JTextField jTextFieldCronName;
    private javax.swing.JTextField jTextFieldDatabaseName;
    private javax.swing.JTextField jTextFieldDayOfMonthPart;
    private javax.swing.JTextField jTextFieldDayOfWeekPart;
    private javax.swing.JTextField jTextFieldEndDay;
    private javax.swing.JTextField jTextFieldEndMonth;
    private javax.swing.JTextField jTextFieldEndYear;
    private javax.swing.JTextField jTextFieldHourPart;
    private javax.swing.JTextField jTextFieldMinPart;
    private javax.swing.JTextField jTextFieldMonthPart;
    private javax.swing.JTextField jTextFieldServerName;
    private javax.swing.JTextField jTextFieldStartDay;
    private javax.swing.JTextField jTextFieldStartMonth;
    private javax.swing.JTextField jTextFieldStartYear;
    private javax.swing.JTextField jTextFieldTestDay;
    private javax.swing.JTextField jTextFieldTestMonth;
    private javax.swing.JTextField jTextFieldTestYear;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    // End of variables declaration//GEN-END:variables
}
