/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotmail.doljin99;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.DatabaseMetaData;

/**
 *
 * @author dolji
 */
public class Tables extends ArrayList<Table> {

    public void addTables(String serverName, String catalog, DatabaseMetaData metaData) {
        ResultSet generalInfo = null;
        try {
            if (metaData == null) {
                System.out.println("database metadata가 null입니다..");
                return;
            }
            generalInfo = metaData.getTables(catalog, "PUBLIC", null, null);
            while (generalInfo.next()) {
                Table table = new Table();
                table.setServerName(serverName);

                table.setJdbcUrl(metaData.getURL());

                table.setTableCat(generalInfo.getString(Table.TABLE_CAT));
                table.setTableSchem(generalInfo.getString(Table.TABLE_SCHEM));
                table.setTableName(generalInfo.getString(Table.TABLE_NAME));
                table.setTypeName(generalInfo.getString(Table.TYPE_NAME));
                table.setRemarks(generalInfo.getString(Table.REMARKS));
                table.setTableType(generalInfo.getString(Table.TABLE_TYPE));
                table.setRemarks(generalInfo.getString(Table.REMARKS));
                table.setTypeCat(generalInfo.getString(Table.TYPE_CAT));
                table.setTypeSchem(generalInfo.getString(Table.TYPE_SCHEM));
                table.setSelfReferencingColName(generalInfo.getString(Table.SELF_REFERENCING_COL_NAME));
                table.setRefGeneration(generalInfo.getString(Table.REF_GENERATION));

                ResultSet pkeyInfo = null;
                PrimaryKeys primaryKeys = new PrimaryKeys();
                try {
                    pkeyInfo = metaData.getPrimaryKeys(catalog, "PUBLIC", generalInfo.getString(Table.TABLE_NAME));
                    while (pkeyInfo.next()) {
                        primaryKeys.setPkName(pkeyInfo.getString(PrimaryKeys.PK_NAME));

                        PrimaryKey primaryKey = new PrimaryKey();
                        primaryKey.setKeySeq(pkeyInfo.getInt(PrimaryKeys.KEY_SEQ));
                        primaryKey.setColumnName(pkeyInfo.getString(PrimaryKeys.COLUMN_NAME));

                        primaryKeys.add(primaryKey);
                    }
                } catch (SQLException ex) {
                    System.out.println("primary key 정보 작성 중 에러: " + ex.getLocalizedMessage());
                } finally {
                    if (pkeyInfo != null) {
                        pkeyInfo.close();
                    }
                }
                table.setPrimaryKeys(primaryKeys);

                add(table);
            }
        } catch (SQLException ex) {
            System.out.println("table 정보 생성 중 에러: " + ex.getLocalizedMessage());
        } finally {
            if (generalInfo != null) {
                try {
                    generalInfo.close();
                } catch (SQLException ex) {
                }
            }
        }
    }
    
    Tables getDatabaseTables(String serverName, String databaseName) {
        Tables tables = new Tables();
        for (Table table : this) {
            if (table.getServerName().equalsIgnoreCase(serverName)
                && table.getTableCat().equalsIgnoreCase(databaseName)) {
                tables.add(table);
            }
        }
        return tables;
    }

    Table findByName(String serverName, String databaseName, String tableName) {
        for (Table table : this) {
            if (table.getServerName().equalsIgnoreCase(serverName)
                && table.getTableCat().equalsIgnoreCase(databaseName)
                && table.getTableName().equalsIgnoreCase(tableName)) {
                return table;
            }
        }
        return null;
    }
}
