/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package admin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import utils.FrameUtil;
import utils.JTextFieldValidation;
import utils.NotNullVerifier;
import utils.TableUtil;

/**
 *
 * @author Muhammad Hanif B
 */
public class ObatUI extends javax.swing.JPanel {

    /**
     * Creates new form ObatUI
     */
    DefaultTableModel dtm = new DefaultTableModel();    
    ArrayList<JTextField> fields = new ArrayList<>();
    
    public ObatUI() {
        initComponents();        
        this.populateComboObatKategori();
        this.populateComboObatSatuan();
        this.populateTableObat(new Obat().getAllObat(""));
        this.groupingRadioButton();
        this.addAllListener();     

        NotNullVerifier notNull = new NotNullVerifier();
        notNull.setErrorLabel(validationLabel);
        tfKodeObat.setInputVerifier(notNull);
        tfNamaObat.setInputVerifier(notNull);

        fields.add(tfKodeObat);
        fields.add(tfNamaObat);
    }
    
    private void groupingRadioButton(){
        ButtonGroup bgroup = new ButtonGroup();
        bgroup.add(rbKodeObat);
        bgroup.add(rbNamaObat);
    }
    
    private void textFieldTyping (JTextField tf, int index) {
         int id = jTable1.getSelectedRow();
         if (id != -1) jTable1.setValueAt(tf.getText(), id, index);
    }
    
    private void addAllListener () {
        tfKodeObat.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                textFieldTyping(tfKodeObat, 0); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                textFieldTyping(tfKodeObat, 0); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                textFieldTyping(tfKodeObat, 0); //To change body of generated methods, choose Tools | Templates.
            }
        });     
        tfNamaObat.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                textFieldTyping(tfNamaObat, 1); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                textFieldTyping(tfNamaObat, 1); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                textFieldTyping(tfNamaObat, 1); //To change body of generated methods, choose Tools | Templates.
            }
        });
        cbObatSatuan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               int id = jTable1.getSelectedRow();
               jTable1.setValueAt(cbObatSatuan.getSelectedItem().toString(), id, 2);
            }
        });
        cbObatKategori.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               int id = jTable1.getSelectedRow();
               jTable1.setValueAt(cbObatKategori.getSelectedItem().toString(), id, 3);
            }
        });
        jTable1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                if (jTable1.getSelectedRow() != -1) {
                
                    deleteButton.setEnabled(true);
//                    tfKodeObat.setEnabled(true);
                    tfNamaObat.setEnabled(true);
                    cbObatSatuan.setEnabled(true); 
                    cbObatKategori.setEnabled(true);                
                    
                    if (jTable1.getValueAt(jTable1.getSelectedRow(), 0) == null) tfKodeObat.setText(""); else tfKodeObat.setText(jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString());
                    if (jTable1.getValueAt(jTable1.getSelectedRow(), 1) == null) tfNamaObat.setText(""); else tfNamaObat.setText(jTable1.getValueAt(jTable1.getSelectedRow(), 1).toString());
                    if (jTable1.getValueAt(jTable1.getSelectedRow(), 4) == null) tfStok.setText("0"); else tfStok.setText(jTable1.getValueAt(jTable1.getSelectedRow(), 4).toString());
                    if (jTable1.getValueAt(jTable1.getSelectedRow(), 5) == null) tfHarga.setText("0"); else tfHarga.setText(jTable1.getValueAt(jTable1.getSelectedRow(), 5).toString());

                    cbObatSatuan.setSelectedItem(jTable1.getValueAt(jTable1.getSelectedRow(), 2).toString());
                    cbObatKategori.setSelectedItem(jTable1.getValueAt(jTable1.getSelectedRow(), 3).toString());
                
                }
            } 
        });   
    }

    private void populateComboObatKategori () {
        List<String> categories = new Kategori().getAllKategori();
        DefaultComboBoxModel<String> model  = new DefaultComboBoxModel<>();
        for (String categorie : categories) {
            model.addElement(categorie.toUpperCase());
        }
        cbObatKategori.setModel(model);
    }
    
    private void populateComboObatSatuan () {
        List<String> satuans = new Satuan().getAllSatuan();
        DefaultComboBoxModel<String> model  = new DefaultComboBoxModel<>();
        for (String satuan : satuans) {
            model.addElement(satuan.toUpperCase());
        }
        cbObatSatuan.setModel(model);
    }
    
    private void populateTableObat (ResultSet rset) {
        try {
            jTable1.clearSelection();
            
            dtm = TableUtil.buildTableModel(rset, false);
            dtm.fireTableDataChanged();
            jTable1.setModel(dtm);
            
            tfKodeObat.setText("");
            tfNamaObat.setText("");
            tfStok.setText("0");
            tfHarga.setText("0");
            tfKodeObat.setEnabled(false);
            tfNamaObat.setEnabled(false);
            cbObatSatuan.setEnabled(false);
            cbObatKategori.setEnabled(false);

        } catch (SQLException ex) {
            Logger.getLogger(ObatUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel11 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        tfSearch = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        rbKodeObat = new javax.swing.JRadioButton();
        rbNamaObat = new javax.swing.JRadioButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        tfKodeObat = new javax.swing.JTextField();
        tfNamaObat = new javax.swing.JTextField();
        cbObatSatuan = new javax.swing.JComboBox();
        cbObatKategori = new javax.swing.JComboBox();
        tfStok = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        tfHarga = new javax.swing.JTextField();
        validationLabel = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        newButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        refreshButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Calibri Light", 0, 24)); // NOI18N
        jLabel2.setText("Data Obat");

        jLabel3.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        jLabel3.setText("Menu ini digunakan untuk mengatur data obat");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/capsule-icon.png"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3))
                    .addComponent(jLabel1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Obat"));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel5.setText("Pencarian ");

        searchButton.setText("Cari");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        rbKodeObat.setText("Kode Obat");

        rbNamaObat.setText("Nama Obat");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbKodeObat)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbNamaObat)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(tfSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchButton)
                    .addComponent(rbKodeObat)
                    .addComponent(rbNamaObat))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Informasi"));

        jLabel4.setText("Kode Obat");

        jLabel6.setText("Nama Obat");

        jLabel7.setText("Satuan");

        jLabel8.setText("Kategori");

        jLabel9.setText("Stok");

        tfKodeObat.setEnabled(false);

        tfNamaObat.setEnabled(false);

        cbObatSatuan.setEnabled(false);

        cbObatKategori.setEnabled(false);

        tfStok.setEnabled(false);

        jLabel10.setText("Harga");

        tfHarga.setEnabled(false);

        validationLabel.setForeground(new java.awt.Color(255, 0, 0));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(21, 21, 21)
                        .addComponent(tfKodeObat))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(validationLabel)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(tfNamaObat)
                            .addComponent(cbObatSatuan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbObatKategori, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tfStok)
                            .addComponent(tfHarga))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(tfKodeObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(tfNamaObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cbObatSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(cbObatKategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(tfStok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(tfHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(validationLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        newButton.setText("New");
        newButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newButtonActionPerformed(evt);
            }
        });
        jPanel5.add(newButton);

        deleteButton.setText("Delete");
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        jPanel5.add(deleteButton);

        refreshButton.setText("Refresh");
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });
        jPanel5.add(refreshButton);

        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        jPanel5.add(saveButton);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(43, 43, 43))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void newButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newButtonActionPerformed
        TableCellEditor editor = jTable1.getCellEditor();
        if (editor != null) {
            editor.stopCellEditing();
        }
        String[] arr = new String[]{null, null, null, null, null, null, null};
        dtm.addRow(arr);
        jTable1.setRowSelectionInterval(dtm.getRowCount()-1, dtm.getRowCount()-1);
        int id = jTable1.getSelectedRow();
        jTable1.setValueAt(cbObatSatuan.getSelectedItem().toString(), id, 2);
        jTable1.setValueAt(cbObatKategori.getSelectedItem().toString(), id, 3); 
        
        tfKodeObat.setEnabled(true);
        tfNamaObat.setEnabled(true);
        cbObatSatuan.setEnabled(true);
        cbObatKategori.setEnabled(true);
        
        tfKodeObat.setText("");
        tfNamaObat.setText("");
        tfStok.setText("0");
        tfHarga.setText("0");
        jTable1.setValueAt(tfStok.getText(), id, 4);
        jTable1.setValueAt(tfHarga.getText(), id, 5);
        
        deleteButton.setEnabled(true);
    }//GEN-LAST:event_newButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog (this, "Perintah ini menghapus secara permanen, Lanjutkan?","Warning",dialogButton);
        if(dialogResult == 0){
            new Obat().delete_test(tfKodeObat.getText());
            FrameUtil.changeUI(new ObatUI(), (JFrame) SwingUtilities.getWindowAncestor(this));
        }      
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        if (new JTextFieldValidation(this, validationLabel, fields).validate()) {
            //TableUtil tblUtil = new TableUtil(jTable1);        
            //new Obat().insert(tblUtil.getTableData());
            if (new Obat().insert_test(tfKodeObat.getText(), tfNamaObat.getText(), cbObatSatuan.getSelectedItem().toString(), 
                    cbObatKategori.getSelectedItem().toString(), tfStok.getText(), tfHarga.getText())) {            
                JOptionPane.showMessageDialog(this, "Menyimpan data sukses!");
            }
            else {
                int reply = JOptionPane.showConfirmDialog(this, "Gagal! Kemungkinan terjadi kesamaan kode obat. Lakukan update?");            
                if (reply == 0) {
                    if (new Obat().update_test(tfKodeObat.getText(), tfNamaObat.getText(), cbObatSatuan.getSelectedItem().toString(), 
                    cbObatKategori.getSelectedItem().toString())) {                        
                        JOptionPane.showMessageDialog(this, "Mengubah data sukses!");
                    } else {JOptionPane.showMessageDialog(this, "Gagal mengubah data");}
                }
            }
            FrameUtil.changeUI(new ObatUI(), (JFrame) SwingUtilities.getWindowAncestor(this));
        }
    }//GEN-LAST:event_saveButtonActionPerformed
    
    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        FrameUtil.changeUI(new ObatUI(), (JFrame) SwingUtilities.getWindowAncestor(this));
    }//GEN-LAST:event_refreshButtonActionPerformed

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        this.searchObat();
    }//GEN-LAST:event_searchButtonActionPerformed

    private void searchObat(){
        boolean isKodeObatSelected = rbKodeObat.isSelected();
        boolean isNamaObatSelected = rbNamaObat.isSelected();
     
        String searchString = "";
        searchString = tfSearch.getText();
        
        String conditions = "";
        if (isKodeObatSelected) {
            conditions = "WHERE KODE_OBAT LIKE '%"+searchString+"%'";
        }
        else if (isNamaObatSelected) {
            conditions = "WHERE NAMA_OBAT LIKE '%"+searchString+"%'";
        }
        this.populateTableObat(new Obat().getAllObat(conditions));        
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cbObatKategori;
    private javax.swing.JComboBox cbObatSatuan;
    private javax.swing.JButton deleteButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton newButton;
    private javax.swing.JRadioButton rbKodeObat;
    private javax.swing.JRadioButton rbNamaObat;
    private javax.swing.JButton refreshButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton searchButton;
    private javax.swing.JTextField tfHarga;
    private javax.swing.JTextField tfKodeObat;
    private javax.swing.JTextField tfNamaObat;
    private javax.swing.JTextField tfSearch;
    private javax.swing.JTextField tfStok;
    private javax.swing.JLabel validationLabel;
    // End of variables declaration//GEN-END:variables
}