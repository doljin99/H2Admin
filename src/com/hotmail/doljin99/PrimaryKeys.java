/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotmail.doljin99;

import java.util.ArrayList;

/**
 *
 * @author dolji
 */
public class PrimaryKeys extends ArrayList<PrimaryKey> {
    public static final String PK_NAME = "PK_NAME";
    public static final String KEY_SEQ  = "KEY_SEQ";
    public static final String COLUMN_NAME  = "COLUMN_NAME";
    

    private String pkName;

    public PrimaryKeys() {
    }

    public void setPkName(String pkName) {
        this.pkName = pkName;
    }

    public String getPkName() {
        return pkName;
    }
    
    public int isKey(String columnName) {
        for (PrimaryKey key : this) {
            if (key.getColumnName().equalsIgnoreCase(columnName)) {
                return key.getKeySeq();
            }
        }
        return 0;
    }
}
