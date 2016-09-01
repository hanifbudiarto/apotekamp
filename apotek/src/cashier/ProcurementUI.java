/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cashier;

import admin.Obat;
import admin.Supplier;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import report.ProcurementReport;
import utils.FrameUtil;
import utils.JTextFieldValidation;
import utils.NotNullVerifier;
import utils.OnlyNumberVerifier;
import utils.SortedComboBoxModel;
import utils.TableUtil;

/**
 *
 * @author Muhammad Hanif B
 */
public class ProcurementUI extends javax.swing.JPanel implements ISearch {

    /**
     * Creates new form ProcurementUI
     */
    ArrayList<JTextField> fields = new ArrayList<>();    
    DefaultTableModel dtm;
    ArrayList<ArrayList<String>> allObat;
    
    BigInteger totalPrice;
    
    NumberFormat moneyFormatter = NumberFormat.getCurrencyInstance();
    
    public ProcurementUI() {
        initComponents();
        btnCancel.setVisible(false);
        
        this.populateComboSupplier();
        this.initDatePicker();
        this.initComboKodeNamaObat();
        this.groupingRadioButton();
        
        if(cbKodeObat.getItemCount()>0) syncCbNamaObat(cbKodeObat.getSelectedItem().toString());
        
        cbKodeObat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(cbKodeObat.getItemCount()>0) syncCbNamaObat(cbKodeObat.getSelectedItem().toString());
            }
        });
        cbNamaObat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               if(cbNamaObat.getItemCount()>0) syncCbKodeObat(cbNamaObat.getSelectedItem().toString());
            }
        });
        rbHpp.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                tfMargin.setEnabled(true);
                tfHargaManual.setEnabled(false);                  
                tfHargaManual.setText("");
            }
        });
        rbManual.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                tfHargaManual.setEnabled(true);
                tfMargin.setEnabled(false);
                tfMargin.setText("");             
            }
        });
        rbHighestPrice.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                 tfMargin.setEnabled(false);
                 tfHargaManual.setEnabled(false);
                 tfMargin.setText("");
                 tfHargaManual.setText("");
            }
        });

        NotNullVerifier notNull = new NotNullVerifier();
        notNull.setErrorLabel(validationLabel);
        
        OnlyNumberVerifier onlyNum = new OnlyNumberVerifier();
        onlyNum.setErrorLabel(validationLabel);
        
        tfNoFaktur.setInputVerifier(notNull);   
        tfNoBatch.setInputVerifier(notNull); 
        tfJumlahBeli.setInputVerifier(onlyNum); 
        tfHargaBeli.setInputVerifier(onlyNum);
  
        fields.add(tfNoFaktur);
        fields.add(tfNoBatch);
        fields.add(tfJumlahBeli);
        fields.add(tfHargaBeli);
        
        this.generateRandomNoFaktur();
    }
    
    private void generateRandomNoFaktur(){        
        String faktur = "PR";
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyy");
        String date = formatter.format(new Date());
        faktur += date;
        Random random = new Random();
        int num = random.nextInt(10000);
        String formatted = String.format("%05d",num);
        faktur += formatted;
        
        while (new Procurement().checkNomorFaktur(faktur)){}
        tfNoFaktur.setText(faktur);
    }
    
    private void groupingRadioButton(){
        ButtonGroup bgroup = new ButtonGroup();
        bgroup.add(rbHpp);
        bgroup.add(rbManual);
        bgroup.add(rbHighestPrice);
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
    
//    public void setCbNamaObat(String namaObat){
//        cbNamaObat.setSelectedItem(namaObat);
//        syncCbKodeObat(namaObat);
//    }
    
    @Override
    public void setCbKodeObat(String kodeObat){
        cbKodeObat.setSelectedItem(kodeObat);
        syncCbNamaObat(kodeObat);
    }
    
    @Override
    public void setCbSupplier(String supp){
        cbSupplier.setSelectedItem(supp);
    }
    
    private void populateComboSupplier () {
        AutoCompleteDecorator.decorate(cbSupplier);
        List<String> suppliers = new Supplier().getAllSupplier();   
        int suppSize = suppliers.size();
        DefaultComboBoxModel<String> model  = new DefaultComboBoxModel<>();        
        for (int i=0; i<suppSize; i++){
            model.addElement(suppliers.get(i).toUpperCase());            
        }     
        cbSupplier.setModel(model);
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
        
        dpExpired.setDate(new Date());
        dpExpired.setFormats(new String[] { "dd-MM-yyyy" });
        dpExpired.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                String date = formatter.format(dpExpired.getDate());
                try {
                    dpExpired.setDate(formatter.parse(date));
                } catch (ParseException ex) {
                    Logger.getLogger(ProcurementUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        dialogSupplier = new javax.swing.JDialog();
        jTextField1 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        dpFaktur = new org.jdesktop.swingx.JXDatePicker();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        tfNoFaktur = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        cbSupplier = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        tfHargaBeli = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        tfJumlahBeli = new javax.swing.JTextField();
        tfNoBatch = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        rbHpp = new javax.swing.JRadioButton();
        rbManual = new javax.swing.JRadioButton();
        rbHighestPrice = new javax.swing.JRadioButton();
        tfMargin = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        tfHargaManual = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        cbKodeObat = new javax.swing.JComboBox();
        dpExpired = new org.jdesktop.swingx.JXDatePicker();
        cbNamaObat = new javax.swing.JComboBox();
        jLabel17 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        validationLabel = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblObat = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        btnCancel = new javax.swing.JButton();
        completeButton = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        lblCompleteProc = new javax.swing.JLabel();

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

        jButton2.setText("Cari");

        jLabel18.setText("Supplier");

        jButton3.setText("Pilih");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 289, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout dialogSupplierLayout = new javax.swing.GroupLayout(dialogSupplier.getContentPane());
        dialogSupplier.getContentPane().setLayout(dialogSupplierLayout);
        dialogSupplierLayout.setHorizontalGroup(
            dialogSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialogSupplierLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dialogSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dialogSupplierLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(dialogSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dialogSupplierLayout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton2))
                            .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        dialogSupplierLayout.setVerticalGroup(
            dialogSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialogSupplierLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dialogSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Calibri Light", 0, 24)); // NOI18N
        jLabel2.setText("Pembelian Obat");

        jLabel3.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        jLabel3.setText("Menu ini digunakan untuk melakukan transaksi pembelian obat");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/buy.png"))); // NOI18N

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

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Pembelian"));

        jLabel4.setText("Tgl Faktur");

        jLabel5.setText("No Faktur");

        tfNoFaktur.setEnabled(false);

        jLabel12.setText("Supplier");

        cbSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbSupplierActionPerformed(evt);
            }
        });

        jButton1.setText("Cari");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addGap(24, 24, 24)
                .addComponent(dpFaktur, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(tfNoFaktur, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jLabel12)
                .addGap(24, 24, 24)
                .addComponent(cbSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap(59, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dpFaktur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(tfNoFaktur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(cbSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Obat"));

        jLabel6.setText("Kode Obat");

        jLabel7.setText("Nama Obat");

        jLabel8.setText("Expired");

        jLabel9.setText("Jumlah Beli");

        jLabel10.setText("Harga Beli/Satuan");

        jLabel11.setText("Nomor Batch");

        jLabel13.setText("Harga Jual");

        rbHpp.setText("HPP");

        rbManual.setText("Manual");

        rbHighestPrice.setSelected(true);
        rbHighestPrice.setText("Harga Tertinggi");

        tfMargin.setEnabled(false);

        jLabel14.setText("Margin(%)");

        tfHargaManual.setEnabled(false);

        jLabel15.setText("Harga Rp.");

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

        cbNamaObat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbNamaObat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbNamaObatActionPerformed(evt);
            }
        });

        jLabel17.setText("+ PPN (10%)");

        validationLabel.setForeground(new java.awt.Color(255, 0, 0));
        jPanel11.add(validationLabel);

        jButton4.setText("Cari");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel8))
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addComponent(cbKodeObat, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton4))
                                            .addComponent(cbNamaObat, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel10)
                                            .addComponent(jLabel9)))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(17, 17, 17)
                                        .addComponent(dpExpired, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tfNoBatch, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tfHargaBeli, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                            .addComponent(tfJumlahBeli))
                        .addGap(30, 30, 30)
                        .addComponent(jLabel13)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(rbHpp)
                                    .addComponent(rbManual))
                                .addGap(12, 12, 12)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel14)
                                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tfHargaManual, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(tfMargin, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel17))))
                            .addComponent(rbHighestPrice))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(rbHpp)
                            .addComponent(tfMargin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14)
                            .addComponent(jLabel17))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rbManual)
                            .addComponent(tfHargaManual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rbHighestPrice))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(tfJumlahBeli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)
                            .addComponent(cbKodeObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(tfHargaBeli, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbNamaObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfNoBatch, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(dpExpired, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(3, 3, 3)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("List Obat"));

        tblObat.setModel(new javax.swing.table.DefaultTableModel(
            null,
            new String [] {
                "Kode", "Batch", "Expired", "Jml Beli", "Harga Beli", "Subtotal Beli", "Margin", "Manual", "Tertinggi", "Harga Jual"
            }
        ){public boolean isCellEditable(int row, int column){return false;}});
        jScrollPane2.setViewportView(tblObat);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel16.setText("Total");
        jPanel8.add(jLabel16);

        lblTotal.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblTotal.setText("0");
        jPanel8.add(lblTotal);

        btnCancel.setText("Cancel Last Transaction");
        btnCancel.setEnabled(false);
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        jPanel7.add(btnCancel);

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

        lblCompleteProc.setFont(new java.awt.Font("Calibri Light", 1, 18)); // NOI18N
        lblCompleteProc.setForeground(new java.awt.Color(0, 153, 0));
        jPanel7.add(lblCompleteProc);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(41, 41, 41))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(186, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        if (new JTextFieldValidation(this, validationLabel, fields).validate()) {
            boolean isItemAdded = this.addItemBelanja();
            if (isItemAdded)  this.calculateTotal();
            else {
                if (rbHighestPrice.isSelected()) JOptionPane.showMessageDialog(this, "Obat ini belum pernah masuk sebelumnya\nSilahkan Pilih harga manual atau HPP");
                else JOptionPane.showMessageDialog(this, "Gagal!");                        
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
                lblCompleteProc.setText("Mohon Tunggu...");
                completingProcurement();
                lblCompleteProc.setText("");
                btnCancel.setEnabled(true);
            }
        });
        t.start(); 
    }//GEN-LAST:event_completeButtonActionPerformed

    private void completingProcurement () {        
        TableUtil tblUtil = new TableUtil(tblObat);   
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");        
        String dateFaktur = formatter.format(dpFaktur.getDate()); 
        Object[] obj = new Object[4];
        obj[0] = dateFaktur;
        obj[1] = tfNoFaktur.getText();
        obj[2] = cbSupplier.getSelectedItem().toString();
        obj[3] = totalPrice.toString();
        String lastId = new Procurement().insert(obj,tblUtil.getTableData());
        if (lastId != null) { 
            //FrameUtil.changeUI(new ProcurementUI(), (JFrame) SwingUtilities.getWindowAncestor(this));
            new ProcurementReport().createReportById(lastId);
        }
    }
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        new SupplierSearchUI(this).setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cbSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbSupplierActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbSupplierActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        new KodeObatSearchUI(this).setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void cbNamaObatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbNamaObatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbNamaObatActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        FrameUtil.changeUI(new ProcurementUI(), (JFrame) SwingUtilities.getWindowAncestor(this));
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        int reply = JOptionPane.showConfirmDialog(this, "Batalkan transaksi terakhir?");            
        if (reply == 0) {
            if (new Procurement().cancelTransaction(tfNoFaktur.getText())) {                        
                JOptionPane.showMessageDialog(this, "Sukses!");
                FrameUtil.changeUI(new ProcurementUI(), (JFrame) SwingUtilities.getWindowAncestor(this));
            } else {JOptionPane.showMessageDialog(this, "Gagal!");}
        }
    }//GEN-LAST:event_btnCancelActionPerformed

    private String calculateHarga (String kodeObat, String hargaBeli, String margin, String hargaManual, String hargaTertinggi) {
        Double harga = 0.0;
        if (!margin.isEmpty()) {
            harga = Double.parseDouble(hargaBeli) + (Double.parseDouble(hargaBeli)*(Double.parseDouble(margin)+10.0)/100); 
        }
        else if (!hargaManual.isEmpty()) {
            harga = Double.parseDouble(hargaManual);
        }
        else if (hargaTertinggi.equals("YES")) {
            String price = new Obat().getHighestPriceObat(kodeObat);
            if (price == null) price = "0";
            harga = Double.parseDouble(price);
        }

        harga = harga/100.0;
        harga = Math.ceil(harga);
        harga = harga*100.0;
        String[] arr = harga.toString().split("\\.");
        return arr[0];
    }
    
    private boolean addItemBelanja() {
        String hargaTertinggi = "NO";
        if (rbHighestPrice.isSelected()) {
            hargaTertinggi = "YES";            
        }
        String kodeObat = cbKodeObat.getSelectedItem().toString();
        String margin = tfMargin.getText();
        String hargaManual = tfHargaManual.getText();
        String hargaBeli = tfHargaBeli.getText();
        String jumlahBeli = tfJumlahBeli.getText();
        Integer subtotal = Integer.parseInt(jumlahBeli)*Integer.parseInt(hargaBeli);
        String subtotalBeli = subtotal.toString();
        String harga = this.calculateHarga(kodeObat, hargaBeli, margin, hargaManual, hargaTertinggi);
        
        if ("0".equals(harga)) return false;
        
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");        
        String dateExp = formatter.format(dpExpired.getDate()); 

        String[] newRow = {
            kodeObat,
            tfNoBatch.getText(),
            dateExp,
            jumlahBeli,
            hargaBeli,
            subtotalBeli,            
            margin,
            hargaManual,
            hargaTertinggi,
            harga
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
        BigInteger jml, hargaBeli, currentTotal;
        for (int i=0; i<rowLength; i++){
            jml = new BigInteger(obj[i][3].toString());
            hargaBeli = new BigInteger(obj[i][4].toString());
            currentTotal = jml.multiply(hargaBeli);
            
            totalPrice = totalPrice.add(currentTotal);
        }
        lblTotal.setText(moneyFormatter.format(Double.parseDouble(totalPrice.toString())));
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnClear;
    private javax.swing.JComboBox cbKodeObat;
    private javax.swing.JComboBox cbNamaObat;
    private javax.swing.JComboBox cbSupplier;
    private javax.swing.JButton completeButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JDialog dialogSupplier;
    private org.jdesktop.swingx.JXDatePicker dpExpired;
    private org.jdesktop.swingx.JXDatePicker dpFaktur;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lblCompleteProc;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JRadioButton rbHighestPrice;
    private javax.swing.JRadioButton rbHpp;
    private javax.swing.JRadioButton rbManual;
    private javax.swing.JTable tblObat;
    private javax.swing.JTextField tfHargaBeli;
    private javax.swing.JTextField tfHargaManual;
    private javax.swing.JTextField tfJumlahBeli;
    private javax.swing.JTextField tfMargin;
    private javax.swing.JTextField tfNoBatch;
    private javax.swing.JTextField tfNoFaktur;
    private javax.swing.JLabel validationLabel;
    // End of variables declaration//GEN-END:variables
}
