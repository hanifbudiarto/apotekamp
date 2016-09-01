/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cashier;

import admin.ObatUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigInteger;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import report.ReturReport;
import utils.FrameUtil;
import utils.JTextFieldValidation;
import utils.OnlyNumberVerifier;
import utils.SortedComboBoxModel;
import utils.TableUtil;

/**
 *
 * @author Muhammad Hanif B
 */
public class ReturUI extends javax.swing.JPanel {

    /**
     * Creates new form ReturUI
     */

    DefaultTableModel dtm = new DefaultTableModel();
    private boolean isHasChanged = false;
    ArrayList<JTextField> fields = new ArrayList<>();
    BigInteger totalPrice;
    
    NumberFormat moneyFormatter = NumberFormat.getCurrencyInstance();
    
    public ReturUI() {
        initComponents();
        this.initDatePicker();
        this.populateCbNomorFaktur();      
        
        jTable1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                if (jTable1.getSelectedRow() != -1) {
                     if (jTable1.getValueAt(jTable1.getSelectedRow(), 0) == null) tfKodeObat.setText(""); else tfKodeObat.setText(jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString());
                     if (jTable1.getValueAt(jTable1.getSelectedRow(), 1) == null) tfNamaObat.setText(""); else tfNamaObat.setText(jTable1.getValueAt(jTable1.getSelectedRow(), 1).toString());
                     if (jTable1.getValueAt(jTable1.getSelectedRow(), 4) == null) tfJumlahBeli.setText(""); else tfJumlahBeli.setText(jTable1.getValueAt(jTable1.getSelectedRow(), 4).toString());
                     if (jTable1.getValueAt(jTable1.getSelectedRow(), 5) == null) tfHargaBeli.setText(""); else tfHargaBeli.setText(jTable1.getValueAt(jTable1.getSelectedRow(), 5).toString());
                }
            }
        });
        cbNomorFaktur.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearJtableTextField();
                isHasChanged = true;
                calculateTotalRetur();
            }
        });
        
        this.initComponentArray();  
        
        OnlyNumberVerifier onlyNum = new OnlyNumberVerifier();
        
        onlyNum.setErrorLabel(validationLabel);
        tfJumlahRetur.setInputVerifier(onlyNum);
    }

    private void initComponentArray () {
        fields.add(tfInvoiceDate);
        fields.add(tfSupplier);
        fields.add(tfKodeObat);
        fields.add(tfNamaObat);
        fields.add(tfHargaBeli);
        fields.add(tfJumlahBeli);
        fields.add(tfJumlahRetur);
    }
    
    private void clearJtableTextField (){
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
    null,
    new String [] {
        "Kode", "Nama", "Harga", "Jumlah Retur", "Subtotal"
    }
){@Override
public boolean isCellEditable(int row, int column){return false;}});
        
        jTable1.setModel(new DefaultTableModel());
        
        for (JTextField field : fields) {
            field.setText("");
        }
    }
    
    private void initDatePicker () {
        dpFaktur.setDate(new Date());
        dpFaktur.setFormats(new String[] { "dd-MM-yyyy" });
        dpFaktur.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                String date = formatter.format(dpFaktur.getDate());
                try {
                    dpFaktur.setDate(formatter.parse(date));
                } catch (ParseException ex) {
                    Logger.getLogger(ProcurementUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    private void populateCbNomorFaktur () {
        AutoCompleteDecorator.decorate(cbNomorFaktur);
        List<String> invoices = new Procurement().getAllPembelian();
        SortedComboBoxModel<String> model  = new SortedComboBoxModel<>();
        for (String invoice : invoices) {
            model.addElement(invoice.toUpperCase());
        }
        cbNomorFaktur.setModel(model);
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
        jPanel5 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        dpFaktur = new org.jdesktop.swingx.JXDatePicker();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cbNomorFaktur = new javax.swing.JComboBox();
        searchButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        tfKodeObat = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        tfJumlahRetur = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        tfHargaBeli = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        tfNamaObat = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        tfInvoiceDate = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        tfSupplier = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        tfJumlahBeli = new javax.swing.JTextField();
        validationLabel = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        completeButton = new javax.swing.JButton();
        btnClearRetur = new javax.swing.JButton();
        lblRetur = new javax.swing.JLabel();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Calibri Light", 0, 24)); // NOI18N
        jLabel2.setText("Retur Obat");

        jLabel3.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        jLabel3.setText("Menu ini digunakan untuk melakukan transaksi retur obat");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/retur.png"))); // NOI18N

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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addGap(37, 37, 37)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(385, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Retur"));

        dpFaktur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dpFakturActionPerformed(evt);
            }
        });

        jLabel4.setText("Tgl Retur");

        jLabel5.setText("Nomor Faktur");

        searchButton.setText("Search");
        searchButton.setToolTipText("");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(jTable1);

        addButton.setText("Add");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });
        jPanel6.add(addButton);

        deleteButton.setText("Delete");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        jPanel6.add(deleteButton);

        tfKodeObat.setEnabled(false);
        tfKodeObat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfKodeObatActionPerformed(evt);
            }
        });

        jLabel7.setText("Kode Obat");

        jLabel8.setText("Jml Retur");

        jLabel6.setText("Harga Beli");

        tfHargaBeli.setEnabled(false);

        jLabel9.setText("Nama");

        tfNamaObat.setEnabled(false);

        jLabel10.setText("Tgl Invoice");

        tfInvoiceDate.setEnabled(false);

        jLabel11.setText("Supplier");

        tfSupplier.setEnabled(false);

        jLabel12.setText("Jml Beli");

        tfJumlahBeli.setEnabled(false);

        validationLabel.setForeground(new java.awt.Color(255, 0, 0));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dpFaktur, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(cbNomorFaktur, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(searchButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(31, 31, 31)
                        .addComponent(tfInvoiceDate, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tfSupplier))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel9))
                        .addGap(28, 28, 28)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tfKodeObat)
                            .addComponent(tfNamaObat, javax.swing.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel12))
                        .addGap(31, 31, 31)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfHargaBeli)
                            .addComponent(tfJumlahBeli)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(34, 34, 34)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(validationLabel)
                            .addComponent(tfJumlahRetur, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dpFaktur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cbNomorFaktur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(tfInvoiceDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(tfSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(tfKodeObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(tfNamaObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(tfHargaBeli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(tfJumlahBeli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(tfJumlahRetur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(validationLabel)
                .addGap(1, 1, 1)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("List Obat Retur"));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            null,
            new String [] {
                "Kode", "Nama", "Harga", "Jumlah Retur", "Subtotal"
            }
        ){public boolean isCellEditable(int row, int column){return false;}});
        jScrollPane2.setViewportView(jTable2);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel16.setText("Total");
        jPanel8.add(jLabel16);

        lblTotal.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblTotal.setText("0");
        jPanel8.add(lblTotal);

        completeButton.setText("Complete");
        completeButton.setEnabled(false);
        completeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                completeButtonActionPerformed(evt);
            }
        });
        jPanel7.add(completeButton);

        btnClearRetur.setText("Clear");
        btnClearRetur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearReturActionPerformed(evt);
            }
        });
        jPanel7.add(btnClearRetur);

        lblRetur.setFont(new java.awt.Font("Calibri Light", 1, 18)); // NOI18N
        lblRetur.setForeground(new java.awt.Color(0, 153, 0));
        jPanel7.add(lblRetur);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void completeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_completeButtonActionPerformed
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                completeButton.setEnabled(false);
                addButton.setEnabled(false);
                deleteButton.setEnabled(false);
                lblRetur.setText("Mohon Tunggu...");
                completingRetur();
                lblRetur.setText("");                        
            }
        });
        t.start();
    }//GEN-LAST:event_completeButtonActionPerformed

    private void completingRetur () {
        TableUtil tblUtil = new TableUtil(jTable2);   
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");        
        String dateFaktur = formatter.format(dpFaktur.getDate()); 
        Object[] obj = new Object[4];
        obj[0] = dateFaktur;
        obj[1] = cbNomorFaktur.getSelectedItem().toString();
        obj[2] = totalPrice.toString();
        String lastId = new Retur().insert(obj,tblUtil.getTableData());
        if (lastId != null) {             
            SimpleDateFormat newFormat = new SimpleDateFormat("dd MMMM yyyy"); 
            new ReturReport().createReportById(lastId, newFormat.format(dpFaktur.getDate()), cbNomorFaktur.getSelectedItem().toString(), tfSupplier.getText());
        }
    }
    
    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        try {
            isHasChanged = false;
            this.calculateTotalRetur();
            
            jTable1.clearSelection();

            dtm = TableUtil.buildTableModel(new Procurement().getPembelianByNoFaktur(cbNomorFaktur.getSelectedItem().toString()), false);
            String tanggalFaktur = dtm.getValueAt(0, 6).toString();
            String supplier = dtm.getValueAt(0,7).toString();
            
            tfInvoiceDate.setText(tanggalFaktur);
            tfSupplier.setText(supplier);
            
            dtm.setColumnCount(6);
            dtm.fireTableDataChanged();
            jTable1.setModel(dtm);            
        } catch (SQLException ex) {
            Logger.getLogger(ObatUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_searchButtonActionPerformed

    private void tfKodeObatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfKodeObatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfKodeObatActionPerformed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        if (new JTextFieldValidation(this, validationLabel, fields).validate()) {
            this.addItemRetur(); 
        }
    }//GEN-LAST:event_addButtonActionPerformed

    private void dpFakturActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dpFakturActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dpFakturActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        int id = jTable2.getSelectedRow();
        if (id != -1) {
            dtm = (DefaultTableModel)jTable2.getModel();
            dtm.removeRow(id);
        }
        this.calculateTotalRetur();
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void btnClearReturActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearReturActionPerformed
        FrameUtil.changeUI(new ReturUI(), (JFrame) SwingUtilities.getWindowAncestor(this));
    }//GEN-LAST:event_btnClearReturActionPerformed

    private void addItemRetur () {
        String hargaBeli = tfHargaBeli.getText();
        String jumlahRetur = tfJumlahRetur.getText();
        Integer subtotal = Integer.parseInt(tfHargaBeli.getText())*Integer.parseInt(tfJumlahRetur.getText());
        
        String[] newRow = {
            tfKodeObat.getText(),
            tfNamaObat.getText(),
            hargaBeli,
            jumlahRetur,
            subtotal.toString()
        };
        
        dtm = (DefaultTableModel)jTable2.getModel();
        dtm.addRow(newRow); 
        this.calculateTotalRetur();
    }

    private void calculateTotalRetur () {
        TableUtil tblUtil = new TableUtil(jTable2);
        Object[][] obj = tblUtil.getTableData();
        int rowLength = obj.length;
        totalPrice = new BigInteger("0");
        BigInteger currentTotal;
        for (int i=0; i<rowLength; i++){
            currentTotal = new BigInteger(obj[i][4].toString());          
            totalPrice = totalPrice.add(currentTotal);
        }
        lblTotal.setText(moneyFormatter.format(Double.parseDouble(totalPrice.toString())));
        completeButton.setEnabled(jTable2.getRowCount()>0 && !isHasChanged);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton btnClearRetur;
    private javax.swing.JComboBox cbNomorFaktur;
    private javax.swing.JButton completeButton;
    private javax.swing.JButton deleteButton;
    private org.jdesktop.swingx.JXDatePicker dpFaktur;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JLabel lblRetur;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JButton searchButton;
    private javax.swing.JTextField tfHargaBeli;
    private javax.swing.JTextField tfInvoiceDate;
    private javax.swing.JTextField tfJumlahBeli;
    private javax.swing.JTextField tfJumlahRetur;
    private javax.swing.JTextField tfKodeObat;
    private javax.swing.JTextField tfNamaObat;
    private javax.swing.JTextField tfSupplier;
    private javax.swing.JLabel validationLabel;
    // End of variables declaration//GEN-END:variables
}
