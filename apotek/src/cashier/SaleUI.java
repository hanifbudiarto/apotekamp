/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cashier;

import admin.Obat;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import report.SaleReport;
import utils.FrameUtil;
import utils.NotNullVerifier;
import utils.OnlyNumberVerifier;
import utils.SortedComboBoxModel;
import utils.TableUtil;

/**
 *
 * @author Muhammad Hanif B
 */
public class SaleUI extends javax.swing.JPanel implements ISearch {

    /**
     * Creates new form Sale
     */
    DefaultTableModel dtm;    
    ArrayList<ArrayList<String>> allObat;
    
    NumberFormat moneyFormatter = NumberFormat.getCurrencyInstance();
    BigInteger totalPrice;
    
    public SaleUI() {
        initComponents();
        this.initDatePicker();
        this.initComboKodeNamaObat();
        if (cbKodeObat.getItemCount()>0) populateObatProperties(cbKodeObat.getSelectedItem().toString());
        
        cbKodeObat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               syncCbNamaObat(cbKodeObat.getSelectedItem().toString());
               populateObatProperties(cbKodeObat.getSelectedItem().toString());
            }
        });
        
        cbNamaObat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               syncCbKodeObat(cbNamaObat.getSelectedItem().toString());
               populateObatProperties(cbKodeObat.getSelectedItem().toString());
            }
        });
        
        NotNullVerifier notNull = new NotNullVerifier();
        notNull.setErrorLabel(validationLabel);
        OnlyNumberVerifier onlyNum = new OnlyNumberVerifier();
        onlyNum.setErrorLabel(validationLabel);
        
        tfNoFaktur.setInputVerifier(notNull);
        tfJumlahBeli.setInputVerifier(onlyNum);
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
        this.generateRandomNoFaktur();
    }
    
//    private void initComboAutoComplete (){
//        AutoCompleteDecorator.decorate(cbKodeObat);
//        AutoCompleteDecorator.decorate(cbNamaObat);        
//        this.populateComboKodeObat();
//    }
    
    private void generateRandomNoFaktur(){        
        String faktur = "SL";
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyy");
        String date = formatter.format(new Date());
        faktur += date;
        Random random = new Random();
        int num = random.nextInt(10000);
        String formatted = String.format("%05d",num);
        faktur += formatted;
        
        while (new Sale().checkNomorFaktur(faktur)){}
        tfNoFaktur.setText(faktur);
    }
    
    private void syncCbNamaObat(String kodeObat) {        
        for (ArrayList<String> obat : allObat) {
            if(obat.get(0).equals(kodeObat)) {
                cbNamaObat.setSelectedItem(obat.get(1));
                break;
            }
        }
    }
    
    private void syncCbKodeObat(String namaObat) {
        for (ArrayList<String> obat : allObat) {
            if(obat.get(1).equals(namaObat)) {
                cbKodeObat.setSelectedItem(obat.get(0));
                break;
            }
        }
    }
    
    private void initComboKodeNamaObat(){
        AutoCompleteDecorator.decorate(cbKodeObat);
        AutoCompleteDecorator.decorate(cbNamaObat);
        
        allObat = new ArrayList<>();
        allObat = new Obat().getSpecificFieldObat("KODE_OBAT,NAMA_OBAT", "");

        SortedComboBoxModel<String> modelKodeObat  = new SortedComboBoxModel<>();
        SortedComboBoxModel<String> modelNamaObat  = new SortedComboBoxModel<>();
        for (ArrayList obat : allObat) {
            modelKodeObat.addElement(obat.get(0).toString().toUpperCase());
            modelNamaObat.addElement(obat.get(1).toString().toUpperCase());
        }
        cbKodeObat.setModel(modelKodeObat);
        cbNamaObat.setModel(modelNamaObat);
    }
    
    private void populateObatProperties(String kodeObat) {
        ArrayList<ArrayList<String>> namaObats = new Obat().getSpecificFieldObat("nama_obat,stok,harga", "where kode_obat='"+kodeObat+"'");
        cbNamaObat.setSelectedItem(namaObats.get(0).get(0));
        tfStok.setText(namaObats.get(0).get(1));
        tfHarga.setText(namaObats.get(0).get(2));        
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
        tfNoFaktur = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        cbKodeObat = new javax.swing.JComboBox();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        tfHarga = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        tfJumlahBeli = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        tfStok = new javax.swing.JTextField();
        validationLabel = new javax.swing.JLabel();
        cbNamaObat = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblObat = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        btnCancelSale = new javax.swing.JButton();
        completeButton = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        lblSale = new javax.swing.JLabel();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Calibri Light", 0, 24)); // NOI18N
        jLabel2.setText("Penjualan Obat");

        jLabel3.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        jLabel3.setText("Menu ini digunakan untuk melakukan transaksi penjualan obat");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/buyer-icon.png"))); // NOI18N

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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Penjualan"));

        jLabel4.setText("Tgl Faktur");

        tfNoFaktur.setEnabled(false);

        jLabel6.setText("No Faktur");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addGap(34, 34, 34)
                .addComponent(dpFaktur, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(tfNoFaktur, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dpFaktur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6)
                    .addComponent(tfNoFaktur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Obat"));

        addButton.setText("Add");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });
        jPanel13.add(addButton);

        deleteButton.setText("Delete");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        jPanel13.add(deleteButton);

        jLabel43.setText("Kode Obat");

        jLabel44.setText("Nama Obat");

        jLabel5.setText("Harga");

        tfHarga.setEnabled(false);

        jLabel46.setText("Jumlah");

        jLabel7.setText("Stok");

        tfStok.setToolTipText("");
        tfStok.setEnabled(false);

        validationLabel.setForeground(new java.awt.Color(255, 0, 0));

        cbNamaObat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton1.setText("Cari");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel5)
                            .addComponent(jLabel46))
                        .addGap(48, 48, 48)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addComponent(validationLabel)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(tfHarga)
                            .addComponent(tfJumlahBeli)
                            .addComponent(tfStok)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton1)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel43)
                                    .addComponent(jLabel44))
                                .addGap(27, 27, 27)
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbKodeObat, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbNamaObat, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(jPanel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43)
                    .addComponent(cbKodeObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel44))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(tfStok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(tfHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel46)
                    .addComponent(tfJumlahBeli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(validationLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(140, 140, 140))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("List Obat"));

        tblObat.setModel(new javax.swing.table.DefaultTableModel(
            null,
            new String [] {
                "Kode Obat", "Nama Obat", "Jumlah", "Harga", "Total"
            }
        ){public boolean isCellEditable(int row, int column){return false;}});
        jScrollPane1.setViewportView(tblObat);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel16.setText("Total");
        jPanel8.add(jLabel16);

        lblTotal.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblTotal.setText("0");
        jPanel8.add(lblTotal);

        btnCancelSale.setText("Cancel Last Transaction");
        btnCancelSale.setEnabled(false);
        btnCancelSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelSaleActionPerformed(evt);
            }
        });
        jPanel7.add(btnCancelSale);

        completeButton.setText("Complete");
        completeButton.setEnabled(false);
        completeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                completeButtonActionPerformed(evt);
            }
        });
        jPanel7.add(completeButton);

        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        jPanel7.add(btnClear);

        lblSale.setFont(new java.awt.Font("Calibri Light", 1, 18)); // NOI18N
        lblSale.setForeground(new java.awt.Color(0, 153, 0));
        jPanel7.add(lblSale);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(43, 43, 43))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    private boolean addItemBelanja() {
        String kodeObat = cbKodeObat.getSelectedItem().toString();
        String namaObat = cbNamaObat.getSelectedItem().toString();
        String hargaObat = tfHarga.getText();
        String jumlahObat = tfJumlahBeli.getText();
        Integer totalObat = Integer.parseInt(hargaObat)*Integer.parseInt(jumlahObat);
        
        String[] newRow = {
            kodeObat,
            namaObat,
            jumlahObat,
            hargaObat,
            totalObat.toString()
        };
        
        dtm = (DefaultTableModel)tblObat.getModel();
        dtm.addRow(newRow); 
        completeButton.setEnabled(tblObat.getRowCount()>0);
        return true;
    }
    
    private void calculateTotal () {
        TableUtil tblUtil = new TableUtil(tblObat);
        Object[][] obj = tblUtil.getTableData();
        int rowLength = obj.length;
        totalPrice = new BigInteger("0");
        BigInteger currentTotal;
        for (int i=0; i<rowLength; i++){
            currentTotal = new BigInteger(obj[i][4].toString());          
            totalPrice = totalPrice.add(currentTotal);
        }
        lblTotal.setText(moneyFormatter.format(Double.parseDouble(totalPrice.toString())));
    }
    
    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        this.requestFocusInWindow();
        if (validationLabel.getText().equals("")) {      
            boolean isAllFieldsFilled = true;
            if (tfNoFaktur.getText().isEmpty() && tfNoFaktur.isEnabled()) {    
                tfNoFaktur.requestFocus();
                this.requestFocusInWindow();
                isAllFieldsFilled = false;
            }
            if (tfJumlahBeli.getText().isEmpty() && tfJumlahBeli.isEnabled()) {    
                tfJumlahBeli.requestFocus();
                this.requestFocusInWindow();
                isAllFieldsFilled = false;
            }
            if (isAllFieldsFilled) {        
                this.addItemBelanja();
                this.calculateTotal();               
            }   
        }
    }//GEN-LAST:event_addButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        int id = tblObat.getSelectedRow();
        if (id != -1) {
            dtm = (DefaultTableModel)tblObat.getModel();
            dtm.removeRow(id);
        }
        this.calculateTotal();
        completeButton.setEnabled(tblObat.getRowCount()>0);
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void completeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_completeButtonActionPerformed
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                completeButton.setEnabled(false);
                addButton.setEnabled(false);
                deleteButton.setEnabled(false);
                lblSale.setText("Mohon Tunggu...");
                completingSale();
                btnCancelSale.setEnabled(true);
                lblSale.setText("");                   
            }
        });
        t.start(); 
    }//GEN-LAST:event_completeButtonActionPerformed

    private void completingSale(){
        TableUtil tblUtil = new TableUtil(tblObat);   
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");        
        String dateFaktur = formatter.format(dpFaktur.getDate()); 
        Object[] obj = new Object[4];
        obj[0] = dateFaktur;
        obj[1] = tfNoFaktur.getText();
        obj[2] = totalPrice.toString();
        String lastId = new Sale().insert(obj,tblUtil.getTableData());
        if (lastId != null) { 
            new SaleReport().createReportById(lastId);
        }
    }
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        new KodeObatSearchUI(this).setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        FrameUtil.changeUI(new SaleUI(), (JFrame) SwingUtilities.getWindowAncestor(this));
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnCancelSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelSaleActionPerformed
        int reply = JOptionPane.showConfirmDialog(this, "Batalkan transaksi terakhir?");            
        if (reply == 0) {
            if (new Sale().cancelTransaction(tfNoFaktur.getText())) {                        
                JOptionPane.showMessageDialog(this, "Sukses!");
                FrameUtil.changeUI(new SaleUI(), (JFrame) SwingUtilities.getWindowAncestor(this));
            } else {JOptionPane.showMessageDialog(this, "Gagal!");}
        }
    }//GEN-LAST:event_btnCancelSaleActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton btnCancelSale;
    private javax.swing.JButton btnClear;
    private javax.swing.JComboBox cbKodeObat;
    private javax.swing.JComboBox cbNamaObat;
    private javax.swing.JButton completeButton;
    private javax.swing.JButton deleteButton;
    private org.jdesktop.swingx.JXDatePicker dpFaktur;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblSale;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JTable tblObat;
    private javax.swing.JTextField tfHarga;
    private javax.swing.JTextField tfJumlahBeli;
    private javax.swing.JTextField tfNoFaktur;
    private javax.swing.JTextField tfStok;
    private javax.swing.JLabel validationLabel;
    // End of variables declaration//GEN-END:variables

    @Override
    public void setCbKodeObat(String kodeObat) {
        cbKodeObat.setSelectedItem(kodeObat);
        syncCbNamaObat(kodeObat);
    }

    @Override
    public void setCbSupplier(String supplier) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
