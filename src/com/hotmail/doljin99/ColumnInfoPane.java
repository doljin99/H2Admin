/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.hotmail.doljin99;

/**
 *
 * @author dolji
 */
public class ColumnInfoPane extends javax.swing.JPanel {

    private final TableColumn column;

    /**
     * Creates new form ColumnInfoPane
     *
     * @param column
     */
    public ColumnInfoPane(TableColumn column) {
        initComponents();

        this.column = column;

        init();
    }

    private void init() {
        jTextFieldTableCat.setText(column.getTableCat());
        jTextFieldTableSchem.setText(column.getTableSchem());
        jTextFieldTableName.setText(column.getTableName());
        jTextFieldColumnName.setText(column.getColumnName());
        jTextFieldDataType.setText(String.valueOf(column.getDataType()));
        jTextFieldTypeName.setText(column.getTypeName());
        jTextFieldColumnSize.setText(String.valueOf(column.getColumnSize()));
        jTextFieldDecimalDigits.setText(String.valueOf(column.getDecimalDigits()));
        jTextFieldNumPrecRadix.setText(String.valueOf(column.getNumPrecRadix()));
        jTextFieldNullable.setText(String.valueOf(column.getNullable()));
        jTextFieldColumnDef.setText(column.getColumnDef());
        jTextFieldCharOctetLength.setText(String.valueOf(column.getCharOctetLength()));
        jTextFieldOrdinalPosition.setText(String.valueOf(column.getOrdinalPosition()));
        jTextFieldIsNullable.setText(column.getIsNullable());
        jTextFieldIsAutoincrement.setText(column.getIsAutoincrement());
        jTextFieldIsGeneratedcolumn.setText(column.getIsGeneratedcolumn());
        
        validate();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jTextFieldTableCat = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldTableSchem = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldTableName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldColumnName = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldDataType = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldTypeName = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldColumnSize = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextFieldDecimalDigits = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextFieldNumPrecRadix = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTextFieldNullable = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextFieldColumnDef = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextFieldCharOctetLength = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTextFieldOrdinalPosition = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jTextFieldIsNullable = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jTextFieldIsAutoincrement = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jTextFieldIsGeneratedcolumn = new javax.swing.JTextField();

        jLabel1.setText("TABLE_CAT");

        jTextFieldTableCat.setEditable(false);

        jLabel2.setText("TABLE_SCHEM");

        jTextFieldTableSchem.setEditable(false);

        jLabel3.setText("TABLE_NAME");

        jTextFieldTableName.setEditable(false);

        jLabel4.setText("COLUMN_NAME");

        jTextFieldColumnName.setEditable(false);

        jLabel5.setText("DATA_TYPE");

        jTextFieldDataType.setEditable(false);

        jLabel6.setText("TYPE_NAME");

        jTextFieldTypeName.setEditable(false);

        jLabel7.setText("COLUMN_SIZE");

        jTextFieldColumnSize.setEditable(false);

        jLabel8.setText("DECIMAL_DIGITS");

        jTextFieldDecimalDigits.setEditable(false);

        jLabel9.setText("NUM_PREC_RADIX");

        jTextFieldNumPrecRadix.setEditable(false);

        jLabel10.setText("NULLABLE");

        jTextFieldNullable.setEditable(false);

        jLabel11.setText("COLUMN_DEF");

        jTextFieldColumnDef.setEditable(false);

        jLabel12.setText("CHAR_OCTET_LENGTH");

        jTextFieldCharOctetLength.setEditable(false);

        jLabel13.setText("ORDINAL_POSITION");

        jTextFieldOrdinalPosition.setEditable(false);

        jLabel14.setText("IS_NULLABLE");

        jTextFieldIsNullable.setEditable(false);

        jLabel15.setText("IS_AUTOINCREMENT");

        jTextFieldIsAutoincrement.setEditable(false);

        jLabel16.setText("IS_GENERATEDCOLUMN");

        jTextFieldIsGeneratedcolumn.setEditable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3))
                        .addGap(70, 70, 70)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldTableSchem, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
                            .addComponent(jTextFieldTableName)
                            .addComponent(jTextFieldTableCat)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addGap(57, 57, 57)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldDataType, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextFieldTypeName, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextFieldColumnName)
                            .addComponent(jTextFieldColumnSize, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextFieldDecimalDigits, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11))
                        .addGap(49, 49, 49)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldNullable, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextFieldColumnDef)
                            .addComponent(jTextFieldNumPrecRadix, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15)
                            .addComponent(jLabel16))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldIsNullable, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextFieldOrdinalPosition, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextFieldIsAutoincrement)
                            .addComponent(jTextFieldIsGeneratedcolumn, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextFieldCharOctetLength, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldTableCat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextFieldTableSchem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextFieldTableName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextFieldColumnName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextFieldDataType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextFieldTypeName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jTextFieldColumnSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jTextFieldDecimalDigits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jTextFieldNumPrecRadix, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jTextFieldNullable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jTextFieldColumnDef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jTextFieldCharOctetLength, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldOrdinalPosition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jTextFieldIsNullable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jTextFieldIsAutoincrement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jTextFieldIsGeneratedcolumn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField jTextFieldCharOctetLength;
    private javax.swing.JTextField jTextFieldColumnDef;
    private javax.swing.JTextField jTextFieldColumnName;
    private javax.swing.JTextField jTextFieldColumnSize;
    private javax.swing.JTextField jTextFieldDataType;
    private javax.swing.JTextField jTextFieldDecimalDigits;
    private javax.swing.JTextField jTextFieldIsAutoincrement;
    private javax.swing.JTextField jTextFieldIsGeneratedcolumn;
    private javax.swing.JTextField jTextFieldIsNullable;
    private javax.swing.JTextField jTextFieldNullable;
    private javax.swing.JTextField jTextFieldNumPrecRadix;
    private javax.swing.JTextField jTextFieldOrdinalPosition;
    private javax.swing.JTextField jTextFieldTableCat;
    private javax.swing.JTextField jTextFieldTableName;
    private javax.swing.JTextField jTextFieldTableSchem;
    private javax.swing.JTextField jTextFieldTypeName;
    // End of variables declaration//GEN-END:variables
}
