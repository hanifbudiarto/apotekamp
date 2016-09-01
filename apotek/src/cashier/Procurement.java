/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cashier;

import admin.Obat;
import admin.Supplier;
import database.MysqlConnect;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Muhammad Hanif B
 */
public class Procurement {
    private final ArrayList<String> parameter, parameter2;       
    private final ArrayList<ArrayList<String>> paramTransaction, paramTransaction2;
    
    public Procurement () {
        this.parameter = new ArrayList<>();
        this.paramTransaction = new ArrayList<>();
        this.parameter2 = new ArrayList<>();
        this.paramTransaction2 = new ArrayList<>();
    }
    
    public ArrayList<String> getAllPembelian () {
        MysqlConnect conn = MysqlConnect.getDbCon();
        if (conn != null) {
            try {
                String query = "SELECT NOMOR_FAKTUR FROM pembelian ORDER BY PEMBELIAN_ID DESC";
                                
                ResultSet rs = conn.query(query, null);
                ArrayList<String> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(rs.getString("NOMOR_FAKTUR"));
                }
                return list;
            } catch (SQLException ex) {
                Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    private String getLastInsertPembelian (ResultSet rs) {
        try {
            List<String> list = new ArrayList<>();
            while (rs.next()) {
                list.add(rs.getString("LASTID"));
            }
            return list.get(0);
        } catch (SQLException ex) {
            Logger.getLogger(Procurement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public String insert (Object[] faktur, Object [][] obats) {
        MysqlConnect conn = MysqlConnect.getDbCon();
        if (conn != null){
            String query = "select insert_pembelian('"+faktur[0].toString()+"','"+faktur[1].toString()+"','"+faktur[2].toString()+"','"+faktur[3].toString()+"') as LASTID";    
            String lastId = null;
            try {
                lastId = this.getLastInsertPembelian(conn.query(query, null));
            } catch (SQLException ex) {
                Logger.getLogger(Procurement.class.getName()).log(Level.SEVERE, null, ex);
            }

            parameter.clear();
            paramTransaction.clear();
            parameter2.clear();
            paramTransaction2.clear();

            int rowLength = obats.length;
            
            SimpleDateFormat uiFormatter = new SimpleDateFormat("dd-MM-yyyy");        
            SimpleDateFormat dbFormatter = new SimpleDateFormat("yyyy-MM-dd");

            for (int i=0; i<rowLength; i++){
                parameter.add("INSERT INTO detail_pembelian (PEMBELIAN_ID,KODE_OBAT,BATCH_OBAT,EXPIRED,JUMLAH_BELI,HARGA_BELI,SUBTOTAL_BELI,MARGIN,HARGA_MANUAL,HARGA_TERTINGGI,HARGA_JUAL) values (?,?,?,?,?,?,?,?,?,?,?)");
                ArrayList<String> temp = new ArrayList<>();
                temp.add(lastId);
                temp.add(obats[i][0].toString());
                temp.add(obats[i][1].toString());
                
                String dateExp = null;
                try {
                    Date exp = uiFormatter.parse(obats[i][2].toString());
                    dateExp = dbFormatter.format(exp);
                } catch (ParseException ex) {
                    Logger.getLogger(Procurement.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                temp.add(dateExp);                
                temp.add(obats[i][3].toString());
                temp.add(obats[i][4].toString());
                temp.add(obats[i][5].toString());
                temp.add(obats[i][6].toString());
                temp.add(obats[i][7].toString());
                temp.add(obats[i][8].toString());
                temp.add(obats[i][9].toString());
                paramTransaction.add(temp);
                
                parameter2.add("UPDATE obat SET HARGA=?, STOK=STOK+? WHERE KODE_OBAT=?");
                ArrayList<String> temp2 = new ArrayList<>();
                temp2.add(obats[i][9].toString());
                temp2.add(obats[i][3].toString());
                temp2.add(obats[i][0].toString());
                paramTransaction2.add(temp2);
            }
            boolean finalResult = conn.insertTransaction(parameter, paramTransaction);
            boolean finalResult2 = conn.insertTransaction(parameter2, paramTransaction2);
            if (finalResult && finalResult2) return lastId;
        }
        return null;
    }
    
    public ResultSet getPembelianByNoFaktur (String nomorFaktur) {
        MysqlConnect conn = MysqlConnect.getDbCon();
        if (conn != null) {
            String query = "SELECT x.KODE_OBAT AS 'KODE', o.NAMA_OBAT AS 'NAMA', x.BATCH_OBAT AS 'BATCH', DATE_FORMAT(x.EXPIRED, '%d-%m-%Y') AS 'EXP', x.JUMLAH_BELI AS 'JML', x.HARGA_BELI AS 'HARGA', DATE_FORMAT(x.TANGGAL_FAKTUR, '%d-%m-%Y') as TANGGAL_FAKTUR , x.SUPPLIER\n"+ 
                    "FROM (SELECT dp.KODE_OBAT, dp.BATCH_OBAT, dp.EXPIRED, dp.JUMLAH_BELI, dp.HARGA_BELI, p.TANGGAL_FAKTUR, p.SUPPLIER FROM detail_pembelian dp LEFT JOIN pembelian p ON dp.PEMBELIAN_ID = p.PEMBELIAN_ID\n" +
"WHERE p.NOMOR_FAKTUR = ?) AS x LEFT JOIN obat o ON x.KODE_OBAT = o.KODE_OBAT";
            ArrayList<String> param = new ArrayList<>();
            param.add(nomorFaktur);
            
            try {
                ResultSet rset = conn.query(query, param);
                return rset;
            } catch (SQLException ex) {
                Logger.getLogger(Obat.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    public boolean checkNomorFaktur (String nomorFaktur) {
        MysqlConnect conn = MysqlConnect.getDbCon();
        if (conn != null) {
            String query = "SELECT COUNT(1) AS JML FROM pembelian WHERE NOMOR_FAKTUR=?";
            ArrayList<String> param = new ArrayList<>();
            param.add(nomorFaktur);
            
            try {
                ResultSet rset = conn.query(query, param);
                if (rset==null) return false;
                else {
                    while (rset.next()) {
                        String value = rset.getString("JML");
                        return !value.equals("0");
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(Procurement.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return true;
    }
    
    public boolean cancelTransaction (String nomorFaktur) {
        MysqlConnect conn = MysqlConnect.getDbCon();
        if (conn != null) {
            String query = "CALL DELETE_PEMBELIAN(?)";
            ArrayList<String> param = new ArrayList<>();
            param.add(nomorFaktur);            
            conn.insert(query, param);
            return true;
        }
        return false;
    }
}
