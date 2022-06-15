/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotmail.doljin99;

/**
 *
 * @author dolji
 */
public class ForeignKey {

    public static final String FK_NAME = "FK_NAME";
    public static final String KEY_SEQ = "KEY_SEQ";
    public static final String FKCOLUMN_NAME = "FKCOLUMN_NAME";
    public static final String PKTABLE_NAME = "PKTABLE_NAME";
    public static final String PKCOLUMN_NAME = "PKCOLUMN_NAME";
    public static final String UPDATE_RULE = "UPDATE_RULE";
    public static final String DELETE_RULE = "DELETE_RULE";
    public static final String DEFERRABILITY = "DEFERRABILITY";

    public static final short IMPORTED_NO_ACTION = 3;	// - do not allow update of primary key if it has been imported
    public static final short IMPORTED_KEY_CASCADE = 0;	// - change imported key to agree with primary key update
    public static final short IMPORTED_KEY_SET_NULL = 2;	// - change imported key to NULL if its primary key has been updated
    public static final short IMPORTED_KEY_SET_DEFAULT = 4;	// - change imported key to default values if its primary key has been updated
    public static final short IMPORTED_KEY_RESTRICT = 1;	// - same as importedKeyNoAction (for ODBC 2.x compatibility)

    public static final short IMPORTED_KEY_INITIALLY_DEFERRED = 5;	// - see SQL92 for definition
    public static final short IMPORTED_KEY_INITIALLY_IMMEDIATE = 6;	// - see SQL92 for definition
    public static final short IMPORTED_KEY_NOT_DEFERRABLE = 7;	// - see SQL92 for definition

    public static final String[] RULE_NAMES = {
        "IMPORTED_KEY_CASCADE",
        "IMPORTED_KEY_RESTRICT",
        "IMPORTED_KEY_SET_NULL",
        "IMPORTED_NO_ACTION",
        "IMPORTED_KEY_SET_DEFAULT",
        "IMPORTED_KEY_INITIALLY_DEFERRED",
        "IMPORTED_KEY_INITIALLY_IMMEDIATE",
        "IMPORTED_KEY_NOT_DEFERRABLE"
    };
    public static final String[] RULE_SHORT_NAMES = {
        "KEY_CASCADE",
        "KEY_RESTRICT",
        "KEY_SET_NULL",
        "NO_ACTION",
        "KEY_SET_DEFAULT",
        "INITIALLY_DEFERRED",
        "INITIALLY_IMMEDIATE",
        "KEY_NOT_DEFERRABLE"
    };

    private String foreignKeyName;
    private Short keySeq;
    private String foreignKeyColumn;
    private String importedTable;
    private String importedKeyColumn;
    private Short updateRule;
    private Short deleteRule;
    private Short deferrability;

    public String getForeignKeyName() {
        return foreignKeyName;
    }

    public void setForeignKeyName(String foreignKeyName) {
        this.foreignKeyName = foreignKeyName;
    }

    public Short getKeySeq() {
        return keySeq;
    }

    public void setKeySeq(Short keySeq) {
        this.keySeq = keySeq;
    }

    public String getForeignKeyColumn() {
        return foreignKeyColumn;
    }

    public void setForeignKeyColumn(String foreignKeyColumn) {
        this.foreignKeyColumn = foreignKeyColumn;
    }

    public String getImportedKeyColumn() {
        return importedKeyColumn;
    }

    public void setImportedKeyColumn(String importedKeyColumn) {
        this.importedKeyColumn = importedKeyColumn;
    }

    public Short getUpdateRule() {
        return updateRule;
    }

    public String getUpdateRuleString() {
        if (updateRule == null) {
            return "unknown";
        }
        return RULE_NAMES[updateRule];
    }

    public void setUpdateRule(Short updateRule) {
        this.updateRule = updateRule;
    }

    public Short getDeleteRule() {
        return deleteRule;
    }

    public String getDeleteRuleString() {
        if (deleteRule == null) {
            return "unknown";
        }
        return RULE_NAMES[deleteRule];
    }

    public void setDeleteRule(Short deleteRule) {
        this.deleteRule = deleteRule;
    }

    public Short getDeferrability() {
        return deferrability;
    }

    public String getDeferrabilityString() {
        if (deferrability == null) {
            return "unknown";
        }
        return RULE_NAMES[deferrability];
    }

    public void setDeferrability(Short deferrability) {
        this.deferrability = deferrability;
    }

    /**
     * Get the value of importedTable
     *
     * @return the value of importedTable
     */
    public String getImportedTable() {
        return importedTable;
    }

    /**
     * Set the value of importedTable
     *
     * @param importedTable new value of importedTable
     */
    public void setImportedTable(String importedTable) {
        this.importedTable = importedTable;
    }

}
