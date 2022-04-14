/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotmail.doljin99;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author dolji
 */
public class TableColumns extends ArrayList<TableColumn> {

    public TableColumns() {
        super();
    }

    public void addColumns(String serverName, ResultSet resultSet) {
        try {
            if (resultSet == null || resultSet.isClosed()) {
                System.out.println("ResultSet이 닫혔습니다.");
                return;
            }
            while (resultSet.next()) {
                TableColumn column = new TableColumn();
                column.setServerName(serverName);
                column.setTableCat(resultSet.getString(TableColumn.TABLE_CAT));
                column.setTableSchem(resultSet.getString(TableColumn.TABLE_SCHEM));
                column.setTableName(resultSet.getString(TableColumn.TABLE_NAME));
                column.setColumnName(resultSet.getString(TableColumn.COLUMN_NAME));
                column.setDataType(resultSet.getInt(TableColumn.DATA_TYPE));
                column.setTypeName(resultSet.getString(TableColumn.TYPE_NAME));
                column.setColumnSize(resultSet.getInt(TableColumn.COLUMN_SIZE));
                column.setBufferLength(resultSet.getInt(TableColumn.BUFFER_LENGTH));
                column.setDecimalDigits(resultSet.getInt(TableColumn.DECIMAL_DIGITS));
                column.setNumPrecRadix(resultSet.getInt(TableColumn.NUM_PREC_RADIX));
                column.setNullable(resultSet.getInt(TableColumn.NULLABLE));
                column.setRemarks(resultSet.getString(TableColumn.REMARKS));
                column.setColumnDef(resultSet.getString(TableColumn.COLUMN_DEF));
                column.setSqlDataType(resultSet.getInt(TableColumn.SQL_DATA_TYPE));
                column.setSqlDatetimeSub(resultSet.getInt(TableColumn.SQL_DATETIME_SUB));
                column.setCharOctetLength(resultSet.getInt(TableColumn.CHAR_OCTET_LENGTH));
                column.setOrdinalPosition(resultSet.getInt(TableColumn.ORDINAL_POSITION));
                column.setIsNullable(resultSet.getString(TableColumn.IS_NULLABLE));
                column.setScopeCatalog(resultSet.getString(TableColumn.SCOPE_CATALOG));
                column.setScopeSchema(resultSet.getString(TableColumn.SCOPE_SCHEMA));
                column.setScopeTable(resultSet.getString(TableColumn.SCOPE_TABLE));
                column.setSourceDataType(resultSet.getShort(TableColumn.SOURCE_DATA_TYPE));
                column.setIsAutoincrement(resultSet.getString(TableColumn.IS_AUTOINCREMENT));
                column.setIsGeneratedcolumn(resultSet.getString(TableColumn.IS_GENERATEDCOLUMN));

                add(column);
            }
        } catch (SQLException ex) {
            System.out.println("column 정보 생성 중 에러: " + ex.getLocalizedMessage());
        }
    }

    public TableColumn findColumn(String serverName, String databaseName, String tableName, String columnName) {
        for (TableColumn column : this) {
//            System.out.println("serverName: " + column.getServerName()
//                + ", TableCat() " + column.getTableCat()
//                + ", TableName() " + column.getTableName()
//                + ", ColumnName() " + column.getColumnName()
//            );
            if (column.getServerName().equalsIgnoreCase(serverName)
                && column.getTableCat().equalsIgnoreCase(databaseName)
                && column.getTableName().equalsIgnoreCase(tableName)
                && column.getColumnName().equalsIgnoreCase(columnName)) {
                return column;
            }
        }
        return null;
    }

    public TableColumns findTableColumns(String serverName, String databaseName) {
        TableColumns tableColumns = new TableColumns();
        for (TableColumn column : this) {
            if (column.getServerName().equalsIgnoreCase(serverName)
                && column.getTableCat().equalsIgnoreCase(databaseName)) {
                tableColumns.add(column);
            }
        }
        return tableColumns;
    }

    public TableColumns findTableColumns(String serverName, String databaseName, String tableName) {
        TableColumns tableColumns = new TableColumns();
        for (TableColumn column : this) {
            if (column.getServerName().equalsIgnoreCase(serverName)
                && column.getTableCat().equalsIgnoreCase(databaseName)
                && column.getTableName().equalsIgnoreCase(tableName)) {
                tableColumns.add(column);
            }
        }
        return tableColumns;
    }
}
