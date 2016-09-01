package cashier;

import database.MysqlConnect;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Muhammad Hanif B
 */
public class Sale {
    private final ArrayList<String> parameter, parameter2;       
    private final ArrayList<ArrayList<String>> paramTransaction, paramTransaction2;
    
    public Sale () {
        this.parameter = new ArrayList<>();
        this.paramTransaction = new ArrayList<>();
        this.parameter2 = new ArrayList<>();
        this.paramTransaction2 = new ArrayList<>();
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
            String query = "select insert_penjualan('"+faktur[0].toString()+"','"+faktur[1].toString()+"','"+faktur[2].toString()+"') as LASTID";    
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
            for (int i=0; i<rowLength; i++){
                parameter.add("INSERT INTO detail_penjualan (PENJUALAN_ID,KODE_OBAT,JUMLAH_JUAL,HARGA_JUAL,SUBTOTAL_JUAL) values (?,?,?,?,?)");
                ArrayList<String> temp = new ArrayList<>();
                temp.add(lastId);
                temp.add(obats[i][0].toString());
                temp.add(obats[i][2].toString());
                temp.add(obats[i][3].toString());   
                temp.add(obats[i][4].toString()); 
                paramTransaction.add(temp);
                
                parameter2.add("UPDATE obat SET STOK=STOK-? WHERE KODE_OBAT=?");
                ArrayList<String> temp2 = new ArrayList<>();
                temp2.add(obats[i][2].toString());
                temp2.add(obats[i][0].toString());
                paramTransaction2.add(temp2);
            }
            boolean finalResult = conn.insertTransaction(parameter, paramTransaction);
            boolean finalResult2 = conn.insertTransaction(parameter2, paramTransaction2);
            if (finalResult && finalResult2) return lastId;
        }
        return null;
    }
    
    public boolean checkNomorFaktur (String nomorFaktur) {
        MysqlConnect conn = MysqlConnect.getDbCon();
        if (conn != null) {
            String query = "SELECT COUNT(1) AS JML FROM penjualan WHERE NOMOR_FAKTUR=?";
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
            String query = "CALL DELETE_PENJUALAN(?)";
            ArrayList<String> param = new ArrayList<>();
            param.add(nomorFaktur);            
            conn.insert(query, param);
            return true;
        }
        return false;
    }
}
