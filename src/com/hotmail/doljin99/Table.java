/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotmail.doljin99;

/**
 *
 * @author dolji
 */
public class Table {

    public static final String TABLE_CAT = "TABLE_CAT";     //   table catalog (may be null)
    public static final String TABLE_SCHEM = "TABLE_SCHEM";	//   table schema (may be null)
    public static final String TABLE_NAME = "TABLE_NAME";	//   table name
    public static final String TABLE_TYPE = "TABLE_TYPE";	//   table type. Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
    public static final String REMARKS = "REMARKS";         //   explanatory comment on the table
    public static final String TYPE_CAT = "TYPE_CAT";       //   the types catalog (may be null)
    public static final String TYPE_SCHEM = "TYPE_SCHEM";	//   the types schema (may be null)
    public static final String TYPE_NAME = "TYPE_NAME";     //   type name (may be null)
    public static final String SELF_REFERENCING_COL_NAME = "SELF_REFERENCING_COL_NAME";	//   name of the designated "identifier" column of a typed table (may be null)
    public static final String REF_GENERATION = "REF_GENERATION";	//   specifies how values in SELF_REFERENCING_COL_NAME are created. Values are "SYSTEM", "USER", "DERIVED". (may be null)
    
    /**
     * DatabaseMetaData 의 getTables의 ResultSet 컬럼
     */
    public static final String[] TABLES_ATTRIBUTE_COLUMNS = {
        TABLE_CAT,
        TABLE_SCHEM,
        TABLE_NAME,
        TABLE_TYPE,
        REMARKS,
        TYPE_CAT,
        TYPE_SCHEM,
        TYPE_NAME,
        SELF_REFERENCING_COL_NAME,
        REF_GENERATION
    };
    
    private String serverName;
    private String jdbcUrl;
    
    private String tableCat;
    private String tableSchem;
    private String tableName;
    private String tableType;
    private String remarks;
    private String typeCat;
    private String typeSchem;
    private String typeName;
    private String selfReferencingColName;
    private String refGeneration;
    
    /**
     * Prinary Key
     */
    private PrimaryKeys primaryKeys;

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getTableCat() {
        return tableCat;
    }

    public void setTableCat(String tableCat) {
        this.tableCat = tableCat;
    }

    public String getTableSchem() {
        return tableSchem;
    }

    public void setTableSchem(String tableSchem) {
        this.tableSchem = tableSchem;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTypeCat() {
        return typeCat;
    }

    public void setTypeCat(String typeCat) {
        this.typeCat = typeCat;
    }

    public String getTypeSchem() {
        return typeSchem;
    }

    public void setTypeSchem(String typeSchem) {
        this.typeSchem = typeSchem;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getSelfReferencingColName() {
        return selfReferencingColName;
    }

    public void setSelfReferencingColName(String selfReferencingColName) {
        this.selfReferencingColName = selfReferencingColName;
    }

    public String getRefGeneration() {
        return refGeneration;
    }

    public void setRefGeneration(String refGeneration) {
        this.refGeneration = refGeneration;
    }

    public PrimaryKeys getPrimaryKeys() {
        return primaryKeys;
    }

    public void setPrimaryKeys(PrimaryKeys pkeys) {
        this.primaryKeys = pkeys;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }
}
