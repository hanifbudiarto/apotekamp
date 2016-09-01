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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import utils.FrameUtil;
import utils.JTextFieldValidation;
import utils.OnlyNumberVerifier;
import utils.TableUtil;

/**
 *
 * @author Muhammad Hanif B
 */
public class HutangUI extends javax.swing.JPanel {

    /**
     * Creates new form HutangUI
     */
    DefaultTableModel dtm;
    ArrayList<JXDatePicker> datepickers = new ArrayList<>();
    ArrayList<JTextField> fields = new ArrayList<>();
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

    public HutangUI() {
        initComponents();
        this.populateComboSupplier();
        this.addAllListener();
        this.populateTableHutang(new Hutang().getAllHutang(""));
    }
    
    private void populateComboSupplier () {
        AutoCompleteDecorator.decorate(cbSupplier);
        List<String> suppliers = new Supplier().getAllSupplier();
        DefaultComboBoxModel<String> model  = new DefaultComboBoxModel<>();
        for (String supplier : suppliers) {
            model.addElement(supplier.toUpperCase());
        }
        cbSupplier.setModel(model);
    }
    
    private void initDatePicker () {        
        dpInput.setName("dpInput");
        dpDeadline.setName("dpDeadline");
        datepickers.add(dpInput);
        datepickers.add(dpDeadline);
        
        for (final JXDatePicker datepicker : datepickers) {
            datepicker.setDate(new Date());
            datepicker.setFormats(new String[] { "dd-MM-yyyy" });
            datepicker.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {                    
                    String date = formatter.format(datepicker.getDate());                    
                    try {
                        datepicker.setDate(formatter.parse(date));
                    } catch (ParseException ex) {
                        Logger.getLogger(HutangUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    switch (datepicker.getName()) {
                        case "dpInput":
                            setColumnValueTable(1, date);
                            break;
                        case "dpDeadline":
                            setColumnValueTable(6, date);
                            break;
                    }
                }
            });
        }

    }

    private void initTfVerifier () {
        OnlyNumberVerifier onlyNum = new OnlyNumberVerifier();
        onlyNum.setErrorLabel(validationLabel);
        
        tfJumlahHutang.setInputVerifier(onlyNum);
        tfJumlahBayar.setInputVerifier(onlyNum);
        tfMinus.setInputVerifier(onlyNum);
    }
        
    private void textFieldTyping (JTextField tf) {
         int id = tblHutang.getSelectedRow();
         int index = 0;
         switch (tf.getName()) {
             case "tfJumlahHutang":
                 index = 3;
                 break;
             case "tfJumlahBayar":
                 index = 4;
                 break;
             case "tfMinus":
                 index = 5;
                 break;
         }
         if (id != -1) tblHutang.setValueAt(tf.getText(), id, index);
    }
    
    private void addAllListener () {        
        tfJumlahHutang.setName("tfJumlahHutang");
        tfJumlahBayar.setName("tfJumlahBayar");
        tfMinus.setName("tfMinus");
        
        fields.add(tfJumlahHutang);
        fields.add(tfJumlahBayar);
        fields.add(tfMinus);
        
        for (final JTextField field : fields) {
            field.getDocument().addDocumentListener(new DocumentListener() {

                @Override
                public void insertUpdate(DocumentEvent e) {
                    textFieldTyping(field); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    textFieldTyping(field); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    textFieldTyping(field); //To change body of generated methods, choose Tools | Templates.
                }
            });
        }   
        
        cbSupplier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               int id = tblHutang.getSelectedRow();
               tblHutang.setValueAt(cbSupplier.getSelectedItem().toString(), id, 2);
            }
        });
        this.initDatePicker();
        this.initTfVerifier();
        tblHutang.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                if (tblHutang.getSelectedRow() != -1) {
                    deleteButton.setEnabled(true);              
                    int rowid = tblHutang.getSelectedRow();
                    Date now = new Date();
                    Date input = null, deadline = null;
                    try {
                        input = formatter.parse(tblHutang.getValueAt(rowid, 1).toString());
                        deadline = formatter.parse(tblHutang.getValueAt(rowid, 6).toString());
                    } catch (ParseException ex) {
                        Logger.getLogger(HutangUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    if (input == null) dpInput.setDate(now); else dpInput.setDate(input);
                    if (tblHutang.getValueAt(rowid, 2) == null) tfSupplierBayar.setText(""); else tfSupplierBayar.setText(tblHutang.getValueAt(rowid, 2).toString());
                    if (tblHutang.getValueAt(rowid, 5) == null) tfSisaBayar.setText(""); else {
                        String sisaBayar = tblHutang.getValueAt(rowid, 5).toString();
                        tfSisaBayar.setText(sisaBayar);
                        if (sisaBayar.equals("0")) tfBayar.setEnabled(false);
                    }                    
                    if (deadline == null) dpDeadline.setDate(now); else dpDeadline.setDate(deadline);
                }
            } 
        }); 

    }
    
    private void setColumnValueTable(int columnid, String value){
        int id = tblHutang.getSelectedRow();
        tblHutang.setValueAt(value, id, columnid); 
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
        jPanel7 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        dpInput = new org.jdesktop.swingx.JXDatePicker();
        labelInput = new javax.swing.JLabel();
        cbSupplier = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        tfJumlahHutang = new javax.swing.JTextField();
        dpDeadline = new org.jdesktop.swingx.JXDatePicker();
        jLabel7 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        newButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        refreshButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        tfJumlahBayar = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        validationLabel = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        tfMinus = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHutang = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        tfSisaBayar = new javax.swing.JTextField();
        tfBayar = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        tfSupplierBayar = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        bayarButton = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        validationLabel1 = new javax.swing.JLabel();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Calibri Light", 0, 24)); // NOI18N
        jLabel2.setText("Pembayaran Hutang");

        jLabel3.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        jLabel3.setText("Menu ini digunakan untuk melakukan pembayaran hutang ke supplier");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/payment.png"))); // NOI18N

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
                .addGap(28, 28, 28)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(203, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Informasi"));

        labelInput.setText("Tgl Input");

        cbSupplier.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel4.setText("Supplier");

        jLabel5.setText("Jml Hutang");

        jLabel6.setText("Telah dibayar");

        tfJumlahHutang.setEnabled(false);

        jLabel7.setText("Jatuh Tempo");

        newButton.setText("New");
        newButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newButtonActionPerformed(evt);
            }
        });
        jPanel3.add(newButton);

        deleteButton.setText("Delete");
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        jPanel3.add(deleteButton);

        refreshButton.setText("Refresh");
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });
        jPanel3.add(refreshButton);

        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        jPanel3.add(saveButton);

        tfJumlahBayar.setEnabled(false);

        validationLabel.setForeground(new java.awt.Color(255, 0, 0));
        jPanel6.add(validationLabel);

        jLabel9.setText("Kekurangan");

        tfMinus.setEnabled(false);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addComponent(labelInput)
                                .addGap(23, 23, 23)))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dpInput, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(dpDeadline, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                                .addComponent(cbSupplier, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tfJumlahHutang, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
                            .addComponent(tfJumlahBayar)
                            .addComponent(tfMinus))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelInput)
                            .addComponent(dpInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(dpDeadline, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(tfJumlahHutang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(tfJumlahBayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(tfMinus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Data hutang"));

        tblHutang.setModel(new javax.swing.table.DefaultTableModel(
            null,
            new String [] {
                "Hutang ID", "Tgl Input", "Supplier", "Jumlah", "Bayar", "Kekurangan", "Deadline"
            }
        ){public boolean isCellEditable(int row, int column){return false;}});
        jScrollPane1.setViewportView(tblHutang);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Pembayaran"));

        jLabel8.setText("Sisa Hutang");

        tfSisaBayar.setEnabled(false);

        jLabel10.setText("Bayar");

        jLabel11.setText("Supplier");

        tfSupplierBayar.setEnabled(false);

        bayarButton.setText("Bayar");
        bayarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bayarButtonActionPerformed(evt);
            }
        });
        jPanel9.add(bayarButton);

        validationLabel1.setForeground(new java.awt.Color(255, 0, 0));
        jPanel10.add(validationLabel1);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfBayar)
                            .addComponent(tfSisaBayar)
                            .addComponent(tfSupplierBayar)))
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfSupplierBayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(tfSisaBayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfBayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void newButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newButtonActionPerformed
        TableCellEditor editor = tblHutang.getCellEditor();
        if (editor != null) {
            editor.stopCellEditing();
        }
        dtm = (DefaultTableModel)tblHutang.getModel();
        
        // Tanggal, Supplier, Jumlah, Bayar, Kurang, Deadline
        String[] arr = new String[]{null, null, null, null, null, null};
        dtm.addRow(arr);
        tblHutang.setRowSelectionInterval(dtm.getRowCount()-1, dtm.getRowCount()-1);
        
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        this.setColumnValueTable(1, formatter.format(dpInput.getDate()));
        this.setColumnValueTable(2, cbSupplier.getSelectedItem().toString());
        this.setColumnValueTable(3, tfJumlahHutang.getText());
        this.setColumnValueTable(4, tfJumlahBayar.getText());
        this.setColumnValueTable(5, tfMinus.getText());  
        this.setColumnValueTable(6, formatter.format(dpDeadline.getDate()));
        
        tfJumlahHutang.setText("");
        tfJumlahBayar.setText("");
        tfMinus.setText("");
        
        tfJumlahHutang.setEnabled(true);
        tfJumlahBayar.setEnabled(true);
        tfMinus.setEnabled(true);
        deleteButton.setEnabled(true);
    }//GEN-LAST:event_newButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        int id = tblHutang.getSelectedRow();
        if (id != -1) {
            dtm = (DefaultTableModel)tblHutang.getModel();
            dtm.removeRow(id);
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        if (new JTextFieldValidation(this, validationLabel, fields).validate()) {
            TableUtil tblUtil = new TableUtil(tblHutang);        
            new Hutang().insert(tblUtil.getTableData());
            this.populateTableHutang(new Hutang().getAllHutang(""));
            JOptionPane.showMessageDialog(this, "Menyimpan data sukses!");
        }
    }//GEN-LAST:event_saveButtonActionPerformed
    
    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        FrameUtil.changeUI(new HutangUI(), (JFrame) SwingUtilities.getWindowAncestor(this));
    }//GEN-LAST:event_refreshButtonActionPerformed

    private void bayarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bayarButtonActionPerformed
        String jmlDibayarkan = tfBayar.getText();
        int id = tblHutang.getSelectedRow();
        String hutangId = tblHutang.getValueAt(id, 0).toString();
        boolean bayarHutang = new Hutang().BayarHutang(jmlDibayarkan, hutangId);
        if (bayarHutang) {
            JOptionPane.showMessageDialog(this, "Menyimpan data sukses!");
            FrameUtil.changeUI(new HutangUI(), (JFrame) SwingUtilities.getWindowAncestor(this));
        }
    }//GEN-LAST:event_bayarButtonActionPerformed

    private void populateTableHutang (ResultSet rset) {
        try {
            tblHutang.clearSelection();
            
            dtm = TableUtil.buildTableModel(rset, false);
            dtm.fireTableDataChanged();
            tblHutang.setModel(dtm);

        } catch (SQLException ex) {
            Logger.getLogger(HutangUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bayarButton;
    private javax.swing.JComboBox cbSupplier;
    private javax.swing.JButton deleteButton;
    private org.jdesktop.swingx.JXDatePicker dpDeadline;
    private org.jdesktop.swingx.JXDatePicker dpInput;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
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
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelInput;
    private javax.swing.JButton newButton;
    private javax.swing.JButton refreshButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JTable tblHutang;
    private javax.swing.JTextField tfBayar;
    private javax.swing.JTextField tfJumlahBayar;
    private javax.swing.JTextField tfJumlahHutang;
    private javax.swing.JTextField tfMinus;
    private javax.swing.JTextField tfSisaBayar;
    private javax.swing.JTextField tfSupplierBayar;
    private javax.swing.JLabel validationLabel;
    private javax.swing.JLabel validationLabel1;
    // End of variables declaration//GEN-END:variables
}
