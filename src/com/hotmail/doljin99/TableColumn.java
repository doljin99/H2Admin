/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotmail.doljin99;

/**
 *
 * @author dolji
 */
public class TableColumn {
    public static final String TABLE_CAT = "TABLE_CAT";                     // 01 TABLE_CAT String => table catalog (may be null)
    public static final String TABLE_SCHEM = "TABLE_SCHEM";                 // 02 TABLE_SCHEM String => table schema (may be null)
    public static final String TABLE_NAME = "TABLE_NAME";                   // 03 TABLE_NAME String => table name
    public static final String COLUMN_NAME = "COLUMN_NAME";                 // 04 COLUMN_NAME String => column name
    public static final String DATA_TYPE = "DATA_TYPE";                     // 05 DATA_TYPE int => SQL type from java.sql.Types
    public static final String TYPE_NAME = "TYPE_NAME";                     // 06 TYPE_NAME String => Data source dependent type name,  for a UDT the type name is fully qualified
    public static final String COLUMN_SIZE = "COLUMN_SIZE";                 // 07 COLUMN_SIZE int => column size.
    public static final String BUFFER_LENGTH = "BUFFER_LENGTH";             // 08 BUFFER_LENGTH is not used.
    public static final String DECIMAL_DIGITS = "DECIMAL_DIGITS";           // 09 DECIMAL_DIGITS int => the number of fractional digits. Null is returned for data types where DECIMAL_DIGITS is not applicable.
    public static final String NUM_PREC_RADIX = "NUM_PREC_RADIX";           // 10 NUM_PREC_RADIX int => Radix (typically either 10 or 2)
    public static final String NULLABLE = "NULLABLE";                       // 11 NULLABLE int => is NULL allowed.
    public static final String REMARKS = "REMARKS";                         // 12 REMARKS String => comment describing column (may be null)
    public static final String COLUMN_DEF = "COLUMN_DEF";                   // 13 COLUMN_DEF String => default value for the column, which should be interpreted as a string when the value is enclosed in single quotes (may be null)
    public static final String SQL_DATA_TYPE = "SQL_DATA_TYPE";             // 14 SQL_DATA_TYPE int => unused
    public static final String SQL_DATETIME_SUB = "SQL_DATETIME_SUB";       // 15 SQL_DATETIME_SUB int => unused
    public static final String CHAR_OCTET_LENGTH = "CHAR_OCTET_LENGTH";     // 16 CHAR_OCTET_LENGTH int => for char types the maximum number of bytes in the column
    public static final String ORDINAL_POSITION = "ORDINAL_POSITION";       // 17 ORDINAL_POSITION int => index of column in table (starting at 1)
    public static final String IS_NULLABLE = "IS_NULLABLE";                 // 18 IS_NULLABLE String => ISO rules are used to determine the nullability for a column.
    public static final String SCOPE_CATALOG = "SCOPE_CATALOG";             // 19 SCOPE_CATALOG String => catalog of table that is the scope of a reference attribute (null if DATA_TYPE isn't REF)
    public static final String SCOPE_SCHEMA = "SCOPE_SCHEMA";               // 20 SCOPE_SCHEMA String => schema of table that is the scope of a reference attribute (null if the DATA_TYPE isn't REF)
    public static final String SCOPE_TABLE = "SCOPE_TABLE";                 // 21 SCOPE_TABLE String => table name that this the scope of a reference attribute (null if the DATA_TYPE isn't REF)
    public static final String SOURCE_DATA_TYPE = "SOURCE_DATA_TYPE";       // 22 SOURCE_DATA_TYPE short => source type of a distinct type or user-generated Ref type, SQL type from java.sql.Types (null if DATA_TYPE isn't DISTINCT or user-generated REF)
    public static final String IS_AUTOINCREMENT = "IS_AUTOINCREMENT";       // 23 IS_AUTOINCREMENT String => Indicates whether this column is auto incremented
    public static final String IS_GENERATEDCOLUMN = "IS_GENERATEDCOLUMN";   // 24 IS_GENERATEDCOLUMN String => Indicates whether this is a generated column

    private String serverName;
    
    public static final String[] FIELD_NAMES = {
        TABLE_CAT,
        TABLE_SCHEM,
        TABLE_NAME,
        COLUMN_NAME,
        DATA_TYPE,
        TYPE_NAME,
        COLUMN_SIZE,
        BUFFER_LENGTH,
        DECIMAL_DIGITS,
        NUM_PREC_RADIX,
        NULLABLE,
        REMARKS,
        COLUMN_DEF,
        SQL_DATA_TYPE,
        SQL_DATETIME_SUB,
        CHAR_OCTET_LENGTH,
        ORDINAL_POSITION,
        IS_NULLABLE,
        SCOPE_CATALOG,
        SCOPE_SCHEMA,
        SCOPE_TABLE,
        SOURCE_DATA_TYPE,
        IS_AUTOINCREMENT,
        IS_GENERATEDCOLUMN
    };
    private String tableCat;
    private String tableSchem;
    private String tableName;
    private String columnName;
    private int dataType;
    private String typeName;
    private int columnSize;
    private int bufferLength;
    private int decimalDigits;
    private int numPrecRadix;
    private int nullable;
    private String remarks;
    private String columnDef;
    private int sqlDataType;
    private int sqlDatetimeSub;
    private int charOctetLength;
    private int ordinalPosition;
    private String isNullable;
    private String scopeCatalog;
    private String scopeSchema;
    private String scopeTable;
    private short sourceDataType;
    private String isAutoincrement;
    private String isGeneratedcolumn;

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

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(int columnSize) {
        this.columnSize = columnSize;
    }

    public int getBufferLength() {
        return bufferLength;
    }

    public void setBufferLength(int bufferLength) {
        this.bufferLength = bufferLength;
    }

    public int getDecimalDigits() {
        return decimalDigits;
    }

    public void setDecimalDigits(int decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    public int getNumPrecRadix() {
        return numPrecRadix;
    }

    public void setNumPrecRadix(int numPrecRadix) {
        this.numPrecRadix = numPrecRadix;
    }

    public int getNullable() {
        return nullable;
    }

    public void setNullable(int nullable) {
        this.nullable = nullable;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getColumnDef() {
        return columnDef;
    }

    public void setColumnDef(String columnDef) {
        this.columnDef = columnDef;
    }

    public int getSqlDataType() {
        return sqlDataType;
    }

    public void setSqlDataType(int sqlDataType) {
        this.sqlDataType = sqlDataType;
    }

    public int getSqlDatetimeSub() {
        return sqlDatetimeSub;
    }

    public void setSqlDatetimeSub(int sqlDatetimeSub) {
        this.sqlDatetimeSub = sqlDatetimeSub;
    }

    public int getCharOctetLength() {
        return charOctetLength;
    }

    public void setCharOctetLength(int charOctetLength) {
        this.charOctetLength = charOctetLength;
    }

    public int getOrdinalPosition() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(int ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    public String getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(String isNullable) {
        this.isNullable = isNullable;
    }

    public String getScopeCatalog() {
        return scopeCatalog;
    }

    public void setScopeCatalog(String scopeCatalog) {
        this.scopeCatalog = scopeCatalog;
    }

    public String getScopeSchema() {
        return scopeSchema;
    }

    public void setScopeSchema(String scopeSchema) {
        this.scopeSchema = scopeSchema;
    }

    public String getScopeTable() {
        return scopeTable;
    }

    public void setScopeTable(String scopeTable) {
        this.scopeTable = scopeTable;
    }

    public short getSourceDataType() {
        return sourceDataType;
    }

    public void setSourceDataType(short sourceDataType) {
        this.sourceDataType = sourceDataType;
    }

    public String getIsAutoincrement() {
        return isAutoincrement;
    }

    public void setIsAutoincrement(String isAutoincrement) {
        this.isAutoincrement = isAutoincrement;
    }

    public String getIsGeneratedcolumn() {
        return isGeneratedcolumn;
    }

    public void setIsGeneratedcolumn(String isGeneratedcolumn) {
        this.isGeneratedcolumn = isGeneratedcolumn;
    }
    
}
