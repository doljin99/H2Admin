/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotmail.doljin99;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import org.h2.tools.Server;

/**
 *
 * @author dolji
 */
public class MyUtilities {

    public static boolean isAvailablePort(int portNr) {
        ServerSocket ignored = null;
        try {
            ignored = new ServerSocket(portNr);
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            if (ignored != null) {
                try {
                    ignored.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    public static Server createServer(ServerMan serverMan) {
        String name = serverMan.getServerName();
        if (name == null || name.isEmpty()) {
            serverMan.setMessage("이름을 설정해 주십시오.");
            return null;
        }
        ArrayList<String> temp = new ArrayList<>();

        temp.add(ServerMan.TCP_PORT);
        temp.add(serverMan.getPort());
        if (serverMan.isTcpAllowOthers()) {
            temp.add(ServerMan.TCP_ALLOW_OTHERS);
        }
        if (serverMan.isIfNotExists()) {
            temp.add(ServerMan.IF_NOT_EXISTS);
        }
        if (serverMan.isTcpDaemon()) {
            temp.add(ServerMan.TCP_DAEMON);
        }
        if (serverMan.getTcpPassword() != null && !serverMan.getTcpPassword().isEmpty()) {
            temp.add(ServerMan.TCP_PASSWORD);
            temp.add(serverMan.getTcpPassword());
        }

        String[] args = new String[temp.size()];
        for (int i = 0; i < temp.size(); i++) {
            args[i] = temp.get(i);
        }
        Server server;
        try {
            server = Server.createTcpServer(args);
            serverMan.setMessage("서버 생성 성공");
        } catch (SQLException ex) {
            server = null;
            serverMan.setMessage("서버 생성 에러" + ex.getLocalizedMessage());
        }
        return server;
    }

    public static void upTableRow(JTable table) {
        int rowNo = table.getSelectedRow();
        if (rowNo < 1) {
            return;
        }
        stopCellEditing(table);
        exchangeTableRow(table, rowNo, rowNo - 1);
        table.changeSelection(rowNo - 1, 1, false, false);
    }

    public static void downTableRow(JTable table) {
        int rowNo = table.getSelectedRow();
        if (rowNo >= table.getRowCount()) {
            return;
        }
        stopCellEditing(table);
        exchangeTableRow(table, rowNo, rowNo + 1);
        table.changeSelection(rowNo + 1, 1, false, false);
    }

    public static void stopCellEditing(JTable table) {
        int rowNo = table.getSelectedRow();
        int columnNo = table.getSelectedColumn();
        if (rowNo != -1 && columnNo != -1) {
            if (table.isEditing()) {
                TableCellEditor tce = table.getCellEditor(rowNo, columnNo);
                if (tce != null) {
                    tce.stopCellEditing();
                }
            }
        }
    }

    public static void exchangeTableRow(JTable table, int rowNo1, int rowNo2) {
        int columnCount = table.getColumnCount();
        int rowCount = table.getRowCount();
        if (rowNo1 < 0 || rowNo2 < 0) {
            return;
        }
        if (rowNo1 > rowCount - 1 || rowNo2 > rowCount - 1) {
            return;
        }

        Object[] saveRow = new Object[columnCount];
        for (int i = 0; i < columnCount; i++) {
            saveRow[i] = table.getValueAt(rowNo1, i);
        }
        for (int i = 0; i < columnCount; i++) {
            table.setValueAt(table.getValueAt(rowNo2, i), rowNo1, i);
        }
        for (int i = 0; i < columnCount; i++) {
            table.setValueAt(saveRow[i], rowNo2, i);
        }
        table.validate();
    }

    public static void alignColumnWidth(JTable grid) {
        alignColumnWidth(grid, grid.getFont());
    }

    public static void alignColumnWidth(JTable grid, Font font) {
        alignColumnWidth(grid, font, false);
    }

    public static void alignColumnWidth(JTable grid, Font font, Boolean withHeight) {
        grid.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        Font headerFont;
        int style = font.getStyle() | Font.BOLD;
        headerFont = font.deriveFont(style);
        FontMetrics fm = grid.getFontMetrics(font);
        FontMetrics fmHeader = grid.getFontMetrics(headerFont);

        int[] columnMaxWidth = new int[grid.getColumnCount()];
        String temp;
        int redundant;
        for (int i = 0; i < columnMaxWidth.length; i++) {
            Object obj = grid.getColumnModel().getColumn(i).getHeaderValue();
            if (obj == null) {
                continue;
            }
            redundant = fmHeader.stringWidth("A");
            int iconWidth = 0;
            if (obj instanceof JLabel) {
                Icon icon = ((JLabel) obj).getIcon();
                if (icon != null) {
                    iconWidth = icon.getIconWidth();
                }
                temp = ((JLabel) obj).getText();
            } else {
                temp = obj.toString();
            }
            if (temp == null || temp.length() == 0) {
                continue;
            }
            columnMaxWidth[i] = fmHeader.stringWidth(temp) + iconWidth + redundant;
        }

        for (int i = 0; i < grid.getRowCount(); i++) {
            for (int j = 0; j < columnMaxWidth.length; j++) {
                redundant = fm.stringWidth("A");
                if (grid.getValueAt(i, j) != null && !grid.getValueAt(i, j).toString().equals("")) {
                    int iconWidth = 0;
                    TableCellRenderer comp = grid.getColumnModel().getColumn(j).getCellRenderer();
                    if (comp != null && comp instanceof JLabel) {
                        Icon icon = ((JLabel) comp).getIcon();
                        if (icon != null) {
                            iconWidth = icon.getIconWidth();
                        }
                    }
                    temp = grid.getValueAt(i, j).toString();
                    if (temp == null || temp.length() == 0) {
                        continue;
                    }
                    columnMaxWidth[j] = Math.max(columnMaxWidth[j], fm.stringWidth(temp) + iconWidth + redundant);
                }
            }
        }
        for (int i = 0; i < columnMaxWidth.length; i++) {
            grid.getColumnModel().getColumn(i).setPreferredWidth(columnMaxWidth[i] + 8);
        }

        if (withHeight && fm != null) {
            grid.setRowHeight(fm.getHeight() + 2);
        }
    }

    /**
     * BorderLayout pane의 center component를 설정(기존 것은 삭제)
     *
     * @param pane
     * @param component
     * @return 성공 여부
     */
    public static boolean setCenterComponent(JPanel pane, JComponent component) {
        BorderLayout layout = (BorderLayout) pane.getLayout();
        if (layout == null) {
            return false;
        }
        Component old = layout.getLayoutComponent(BorderLayout.CENTER);
        if (old != null) {
            pane.remove(layout.getLayoutComponent(BorderLayout.CENTER));
        }
        pane.add(component, BorderLayout.CENTER);
        pane.validate();
        return true;
    }

    public static void setTableHAllHeadersAlignment(JTable grid, int alignment) {
        if (grid == null) {
            return;
        }
        int columnCount = grid.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            grid.getColumnModel().getColumn(i).setHeaderRenderer(new JTableHeaderRenderer(alignment));
        }
    }

    /**
     * JTable의 index 위치의 헤더를 지정한 alignment로 설정.
     *
     * @param grid JTable
     * @param index header 위치
     * @param alignment SwingConstants 의 LEFT, CENTER, RIGHT,LEADING, TRAILING
     */
    public static void setTableHeaderAlignment(JTable grid, int index, int alignment) {
        if (grid == null) {
            return;
        }
        if (grid.getColumnCount() <= index) {
            return;
        }
        grid.getColumnModel().getColumn(index).setHeaderRenderer(new JTableHeaderRenderer(alignment));
    }

    static class JTableHeaderRenderer implements TableCellRenderer {

        private static final int[] AVAILABLE_ALIGNMENT = {
            SwingConstants.LEFT, SwingConstants.CENTER, SwingConstants.RIGHT, SwingConstants.LEADING, SwingConstants.TRAILING
        };
        private int horizontalAlignment = SwingConstants.LEFT;

        public JTableHeaderRenderer(int horizontalAlignment) {
            for (int i = 0; i < AVAILABLE_ALIGNMENT.length; i++) {
                int alingnment = AVAILABLE_ALIGNMENT[i];
                if (horizontalAlignment == alingnment) {
                    this.horizontalAlignment = horizontalAlignment;
                    break;
                }
            }
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

            TableCellRenderer r = table.getTableHeader().getDefaultRenderer();
            JLabel label = (JLabel) r.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            label.setHorizontalAlignment(horizontalAlignment);
            return label;
        }
    }

    /**
     * 주어진 문자열에 대해 특정 단어의 좌,우가 공백 문자인 순수 단어로 포함되었는지 여부를 판단 특정단어로 시작하거나 끝나는 경우도
     * 참으로 표현한다. value가 "WHERE COLUMN_A = ?"일 때 word가 "WHERE" 인 경우 참 value가
     * "FROM TABLE_A WHERE" 일 때 word가 "WHERE"인 경우 참 value가 "FROM TABLE_A WHERE
     * COLUMN_A = ?"일 때 word가 "WHERE"인 경우 참
     *
     * @param value
     * @param word
     * @return
     */
    public static boolean containWord(String value, String word) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        if (word == null || word.isEmpty()) {
            return false;
        }
        int index = 0;
        int offset = 0;
        String text = value;
        while (index >= 0 && offset < text.length()) {
            index = text.indexOf(word, offset);
//            System.out.println("index = " + index);
//            System.out.println("offset = " + offset);
            if (index < 0) {
                offset = index + word.length();
                continue;
            }
            int checkPoint2 = index + word.length();
            if (index == 0) {
                if (checkPoint2 >= text.length()) {
                    return true;
                }
                if ((Character.isWhitespace(text.charAt(word.length())))) {
                    return true;
                }
            } else {
                if (!(Character.isWhitespace(text.charAt(index - 1)))) {
//                    System.out.println("character at index - 1 = <" + text.charAt(index - 1) + ">");
                    offset = index + word.length();
                    continue;
                }
                if (checkPoint2 >= text.length()) {
                    return true;
                }
                if ((Character.isWhitespace(text.charAt(checkPoint2)))) {
//                    System.out.println("character at checkPoint2 = <" + text.charAt(checkPoint2) + ">");
                    return true;
                }
            }
            offset = index + word.length();
        }
        return false;
    }
}
