/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotmail.doljin99;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import org.h2.tools.Backup;

/**
 *
 * @author dolji
 */
public class ScheduledBackupRunnable implements Runnable {

    private int termMillis;
    private final ServerMen serverMen;
//    private BackupSchedules backupSchedules;

    private ExecuteBackupPool executeBackupPool;

    private String dateFormat;
    private static final String EXTENSION = ".zip";

    private boolean running = true;

    public ScheduledBackupRunnable(ServerMen serverMen) {
        this(serverMen, 60000);
    }

    public ScheduledBackupRunnable(ServerMen serverMen, int termMillis) {
        this.serverMen = serverMen;
//        this.backupSchedules = serverMen.getBackupSchedules();
        this.termMillis = termMillis;

        init();
    }

    private void init() {
        executeBackupPool = new ExecuteBackupPool();
        dateFormat = "yyyy-MM-dd-HH-mm-ss";
    }

    @Override
    public void run() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                LocalDateTime today;
                LocalDateTime tommorow;
                while (running) {
                    BackupSchedules backupSchedules = serverMen.getBackupSchedules();
                    if (backupSchedules == null || backupSchedules.isEmpty()) {
                        break;
                    }
                    today = LocalDateTime.now().withNano(0);
                    tommorow = today.plusDays(1);

                    ArrayList<LocalDateTime> toBes = new ArrayList<>();
                    for (int i = 0; i < backupSchedules.size(); i++) {
                        BackupSchedule backupSchedule = backupSchedules.get(i);
                        toBes.addAll(backupSchedule.getWaitTimesInOneDay(today));
                        toBes.addAll(backupSchedule.getWaitTimesInOneDay(tommorow));
                        for (int j = 0; j < toBes.size(); j++) {
                            LocalDateTime at = toBes.get(j);
                            if (j > 100) {
                                break;
                            }
                            executeBackupPool.addExecuteBackupRunnable(at, backupSchedule);
                        }
                        toBes.clear();
                    }
                    for (int i = executeBackupPool.size() - 1; i >= 0; i--) {
                        ExecuteBackupScheduleRunnable executeBackupScheduleRunnable = executeBackupPool.get(i);
                        if (executeBackupScheduleRunnable.isDone()) {
                            executeBackupPool.remove(i);
                        }
                    }
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        };
        timer.schedule(task, termMillis);
    }

    public ExecuteBackupPool getExecuteBackupPool() {
        return executeBackupPool;
    }

    class ExecuteBackupPool extends ArrayList<ExecuteBackupScheduleRunnable> {

        public ExecuteBackupPool() {
        }

        public boolean addExecuteBackupRunnable(LocalDateTime at, BackupSchedule backupSchedule) {
            if (at.isBefore(LocalDateTime.now().withNano(0))) {
                return false;
            }
            int dupCcount = 0;
            for (ExecuteBackupScheduleRunnable runnable : this) {
                if (runnable.isDuplicate(at, backupSchedule)) {
                    dupCcount++;
                }
            }
            if (dupCcount > 0) {
                return false;
            }
            ExecuteBackupScheduleRunnable runnable = new ExecuteBackupScheduleRunnable(at, backupSchedule);
            Timer timer = new Timer();
            timer.schedule(runnable, Date.from(at.atZone(ZoneId.systemDefault()).toInstant()));
            
            return add(runnable);
        }

//        public boolean addExecuteBackupRunnable(ExecuteBackupScheduleRunnable runnableAnother) {
//            int dupCcount = 0;
//            for (ExecuteBackupScheduleRunnable runnable : this) {
//                if (runnable.isDuplicate(runnableAnother)) {
//                    dupCcount++;
//                }
//            }
//            if (dupCcount > 0) {
//                return false;
//            }
//            return add(runnableAnother);
//        }
    }

    class ExecuteBackupScheduleRunnable extends TimerTask {

        private final LocalDateTime scheduleTime;
        private BackupSchedule backupSchedule;

        private boolean done;
        private String message;
        private String filePath;

        public ExecuteBackupScheduleRunnable(LocalDateTime scheduleTime, BackupSchedule backupSchedule) {
            this.scheduleTime = scheduleTime;
            this.backupSchedule = backupSchedule;

            done = false;
        }

        @Override
        public void run() {
            try {
                message = "";
                addMessage("작업 시작 시간: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateFormat)));
                H2AUtilities.forceMkdir(H2ServerAdmin.SCHEDULED_BACKUP_LOG_DIRECTORY);
                filePath = H2ServerAdmin.SCHEDULED_BACKUP_LOG_DIRECTORY + File.separator + H2ServerAdmin.SCHEDULED_BACKUP_LOG_NAME;
                String serverName = backupSchedule.getServerName();
                ServerMan serverMan = serverMen.findByName(serverName);
                if (serverMan == null) {
                    addMessage("서버를 찾을 수 없습니다.: " + serverName);
                    return;
                }
                if (!serverMan.isLocal()) {
                    addMessage("현재 버전에서는 remote 서버 데이터베이스에 대한 백업 기능이 없습니다.");
                    return;
                }
                if (serverMan.isRun()) {
                    onlineBackup(serverMan, backupSchedule.getDatabaseName(), backupSchedule.getBackupDir());
                } else {
                    offlineBackup(serverMan, backupSchedule.getDatabaseName(), backupSchedule.getBackupDir());
                }
            } finally {
                done = true;

                try (FileWriter fw = new FileWriter(filePath, true); BufferedWriter writer = new BufferedWriter(fw)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("작업 종료 시간: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateFormat))).append("\n");
                    sb.append(backupSchedule.makeKey()).append(" ").append(scheduleTime.format(DateTimeFormatter.ISO_DATE_TIME)).append("\n");
                    sb.append(message);
                    sb.append("----------------------------------------------------------------------------------------------------\n");

                    writer.write(sb.toString());
                    writer.flush();
                } catch (IOException ex) {
                    System.out.println("백업 로그 작성 중 에러: " + ex.getLocalizedMessage());
                }
            }
        }

        public boolean isDone() {
            return done;
        }

        private void onlineBackup(ServerMan serverMan, String databaseName, String backupDir) {
            if (databaseName != null && !databaseName.equalsIgnoreCase("ALL")) {
                System.out.println("databaseName = " + databaseName);
                onlineDatabaseBackup(serverMan, serverMan.getDatabaseMen().findByName(databaseName), backupDir);
                return;
            }
            DatabaseMen databaseMen = serverMan.getDatabaseMen();
            for (int i = 0; i < databaseMen.size(); i++) {
                DatabaseMan source = databaseMen.get(i);
                onlineDatabaseBackup(serverMan, source, backupDir);
//            System.out.println("source db = " + source.getDatabaseName());
            }
        }

        private void onlineDatabaseBackup(ServerMan serverMan, DatabaseMan databaseMan, String backupDir) {
            Connection connection = serverMan.getConnection(databaseMan.getDatabaseName());
            if (connection == null) {
                addMessage("Connection 획득에 실패하여 online Backup을 실행할 수 없습니다: " + databaseMan.getDatabaseName());
                return;
            }
            Statement statement = null;
            try {
                String backupFileName = makeBackupFilename(serverMan, databaseMan.getDatabaseName(), backupDir);
                statement = connection.createStatement();
                statement.execute("BACKUP TO '" + backupFileName + "'");
                done = true;
                addMessage("online 백업이 실행되었습니다.\n" + backupFileName);
            } catch (SQLException ex) {
                addMessage("online 백업 중 에러: " + ex.getLocalizedMessage());
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

        private String makeBackupFilename(ServerMan serverMan, String databaseName, String backupDir) {
            String directory = backupDir;
            if (backupDir == null || backupDir.isEmpty()) {
                directory = ".";
            }
            String onOff;
            if (serverMan.isRun()) {
                onOff = "ON";
            } else {
                onOff = "OFF";
            }
            return directory + File.separator
                    + makeFileNamePrefix(serverMan.getServerName(), databaseName, onOff)
                    + makeCurrentDateString()
                    + EXTENSION;
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

        private String makeCurrentDateString() {
            return LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateFormat));
        }

        private String makeBackupFilename(String directory, String serverName, String databaseName, String onOff) {
            return directory + File.separator + makeFileNamePrefix(serverName, databaseName, onOff) + makeCurrentDateString() + EXTENSION;
        }

        private void offlineBackup(ServerMan serverMan, String databaseName, String backupDir) {
            String dbName = null;
            String dbArg;
            if (databaseName == null) {
                dbArg = "";
            } else {
                if (databaseName.equalsIgnoreCase("ALL")) {
                    dbArg = "";
                } else {
                    dbArg = databaseName;
                }
                dbName = databaseName;
            }
            try {
                String backupFileName = makeBackupFilename(serverMan, dbName, backupDir);
                String baseDir = serverMan.getBaseDir();
                if (baseDir == null || baseDir.isEmpty()) {
                    baseDir = ".";
                }
                Backup.execute(backupFileName, baseDir, dbArg, false);
                done = true;
                addMessage("offline 백업이 실행되었습니다.\n" + backupFileName);
            } catch (SQLException ex) {
                done = false;
                addMessage("offline 백업 중 에러: " + ex.getLocalizedMessage());
            } finally {
            }
        }

        private void addMessage(String msg) {
            message += msg + "\n";
            System.out.println(msg + "\n");
        }

        public boolean isDuplicate(LocalDateTime at, BackupSchedule anotherBackupSchedule) {
            return scheduleTime.equals(at) && backupSchedule.makeKey().equals(anotherBackupSchedule.makeKey());
        }

        public boolean isDuplicate(ExecuteBackupScheduleRunnable another) {
            return Date.from(scheduleTime.atZone(ZoneId.systemDefault()).toInstant()).getTime() == another.scheduledExecutionTime() && makeKey().equals(another.makeKey());
        }

        private String makeKey() {
            return backupSchedule.makeKey();
        }

        public String getIdentifier() {
            StringBuilder sb = new StringBuilder();
            sb.append(scheduleTime.format(DateTimeFormatter.ofPattern(dateFormat))).append("\n");
            sb.append(backupSchedule.makeKey()).append("\n");
            sb.append(backupSchedule.getBackupDir()).append("\n");

            return sb.toString();
        }

        public LocalDateTime getScheduleTime() {
            return scheduleTime;
        }

        public BackupSchedule getBackupSchedule() {
            return backupSchedule;
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * Get the value of termMillis
     *
     * @return the value of termMillis
     */
    public int getTermMillis() {
        return termMillis;
    }

    /**
     * Set the value of termMillis
     *
     * @param termMillis new value of termMillis
     */
    public void setTermMillis(int termMillis) {
        this.termMillis = termMillis;
    }
}
