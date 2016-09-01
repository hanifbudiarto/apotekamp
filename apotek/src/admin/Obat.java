/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package admin;

import database.MysqlConnect;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Muhammad Hanif B
 */
public class Obat {
    private final ArrayList<String> parameter;       
    private final ArrayList<ArrayList<String>> paramTransaction;
    
    public Obat () {
        this.parameter = new ArrayList<>();
        this.paramTransaction = new ArrayList<>();
    }
    
    public ResultSet getAllObat (String conditions) {
        MysqlConnect conn = MysqlConnect.getDbCon();
        if (conn != null) {
            String query = "SELECT KODE_OBAT AS Kode, NAMA_OBAT AS 'Nama Obat', SATUAN_OBAT AS Satuan, KATEGORI_OBAT AS Kategori, STOK AS Stok, HARGA AS Harga FROM obat ";
            query += conditions;
            try {
                ResultSet rset = conn.query(query, null);
                return rset;
            } catch (SQLException ex) {
                Logger.getLogger(Obat.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    public ResultSet getObatByMinStock (String min) {
        MysqlConnect conn = MysqlConnect.getDbCon();
        if (conn != null) {
            String query = "SELECT KODE_OBAT AS Kode, NAMA_OBAT AS 'Nama Obat', STOK AS Stok FROM obat WHERE stok <= " +min;
            try {
                ResultSet rset = conn.query(query, null);
                return rset;
            } catch (SQLException ex) {
                Logger.getLogger(Obat.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    public String getHighestPriceObat (String kodeObat) {
        MysqlConnect conn = MysqlConnect.getDbCon();
        if (conn != null) {
            String query = "select max(harga_jual) as harga from detail_pembelian where kode_obat = ? and expired > now()";
            ArrayList<String> param = new ArrayList<>();
            param.add(kodeObat);
            try {                
                ResultSet rs = conn.query(query, param);
                while (rs.next()) {
                    return rs.getString("harga");
                }  
            } catch (SQLException ex) {
                Logger.getLogger(Obat.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    public ArrayList<ArrayList<String>> getSpecificFieldObat (String field, String condition) {
        MysqlConnect conn = MysqlConnect.getDbCon();
        if (conn != null) {
            try {
                String[] columns = field.split(",");                
                String query = "SELECT "+field+" FROM obat ";
                query += condition;
                
                ResultSet rs = conn.query(query, null);
                ArrayList<ArrayList<String>> arrlist = new ArrayList<>();
                while (rs.next()) {
                    ArrayList<String> list = new ArrayList<>();
                    for (String column : columns) {                        
                        list.add(rs.getString(column));
                    }      
                    arrlist.add(list);
                }       
                return arrlist;
            } catch (SQLException ex) {
                Logger.getLogger(Satuan.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    public boolean delete_test (String kode) {
        MysqlConnect conn = MysqlConnect.getDbCon();
        if (conn != null) {
            ArrayList<String> param = new ArrayList<>();
            param.add(kode);
            return conn.insert("DELETE FROM obat WHERE kode_obat=?", param);
        }
        return false;
    }
    
    public boolean update_test (String kode, String nama, String satuan, String kategori) {
        MysqlConnect conn = MysqlConnect.getDbCon();
        if (conn != null) {
            ArrayList<String> paramUpd = new ArrayList<>();
            paramUpd.add(nama);
            paramUpd.add(satuan);
            paramUpd.add(kategori);
            paramUpd.add(kode);
            return conn.insert("UPDATE obat set nama_obat=?, satuan_obat=?, kategori_obat=? where kode_obat=?", paramUpd);
        }
        return false;
    }
    
    public boolean insert_test (String kode, String nama, String satuan, String kategori, String stok, String harga) {
        MysqlConnect conn = MysqlConnect.getDbCon();
        if (conn != null) {
            ResultSet rs = getAllObat("where kode_obat='"+kode+"'");
            try {
                if (!rs.next()) {
                    ArrayList<String> param = new ArrayList<>();
                    param.add(kode);
                    param.add(nama);
                    param.add(satuan);
                    param.add(kategori);
                    param.add(stok);
                    param.add(harga);
                    return conn.insert("INSERT INTO obat VALUES(?,?,?,?,?,?)", param);
                }
            } catch (SQLException ex) {
                Logger.getLogger(Obat.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    public boolean insert (Object [][] obats) {
        MysqlConnect conn = MysqlConnect.getDbCon();
        if (conn != null){
//            String query = "TRUNCATE OBAT";               
            parameter.clear();
            paramTransaction.clear();

//            parameter.add(query);
//            paramTransaction.add(null);

            int rowLength = obats.length;
            for (int i=0; i<rowLength; i++){    
                if (obats[i][0] != null) {
                    parameter.add("DELETE FROM OBAT WHERE OBAT_ID=?");
                    ArrayList<String> paramDelete = new ArrayList<>();
                    paramDelete.add(obats[i][0].toString());
                    paramTransaction.add(paramDelete);
                }
                
                parameter.add("INSERT INTO OBAT (KODE_OBAT,NAMA_OBAT,SATUAN_OBAT,KATEGORI_OBAT,STOK,HARGA) values (?,?,?,?,?,?)");
                ArrayList<String> paramInsert = new ArrayList<>();
                paramInsert.add(obats[i][1].toString());
                paramInsert.add(obats[i][2].toString());
                paramInsert.add(obats[i][3].toString());
                paramInsert.add(obats[i][4].toString());
                paramInsert.add(obats[i][5].toString());
                paramInsert.add(obats[i][6].toString());
                paramTransaction.add(paramInsert);
            }
            boolean finalResult = conn.insertTransaction(parameter, paramTransaction);
            return finalResult;
        }
        return false;
    }
    
}
