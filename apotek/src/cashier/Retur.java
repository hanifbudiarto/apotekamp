/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cashier;

import database.MysqlConnect;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Muhammad Hanif B
 */
public class Retur {
    
    private final ArrayList<String> parameter, parameter2, parameter3;       
    private final ArrayList<ArrayList<String>> paramTransaction, paramTransaction2, paramTransaction3;

    public Retur (){
        this.parameter = new ArrayList<>();
        this.paramTransaction = new ArrayList<>();
        this.parameter2 = new ArrayList<>();
        this.paramTransaction2 = new ArrayList<>();
        this.parameter3 = new ArrayList<>();
        this.paramTransaction3 = new ArrayList<>();
    }

    private String getLastInsertRetur (ResultSet rs) {
        try {
            List<String> list = new ArrayList<>();
            while (rs.next()) {
                list.add(rs.getString("LASTID"));
            }
            return list.get(0);
        } catch (SQLException ex) {
            Logger.getLogger(Retur.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public String insert (Object[] faktur, Object [][] obats) {
        MysqlConnect conn = MysqlConnect.getDbCon();
        if (conn != null){
            String query = "SELECT INSERT_RETUR('"+faktur[0].toString()+"','"+faktur[1].toString()+"','"+faktur[2].toString()+"') as LASTID";    
            String lastId = null;
            
            try {
                lastId = this.getLastInsertRetur(conn.query(query, null));
            } catch (SQLException ex) {
                Logger.getLogger(Retur.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            parameter.clear(); parameter2.clear(); parameter3.clear();
            paramTransaction.clear(); paramTransaction2.clear(); paramTransaction3.clear();

            int rowLength = obats.length;
            for (int i=0; i<rowLength; i++){                
                parameter.add("INSERT INTO detail_retur (RETUR_ID,KODE_OBAT,HARGA_BELI,JUMLAH_RETUR,SUBTOTAL_RETUR) values (?,?,?,?,?)");
                ArrayList<String> temp = new ArrayList<>();
                temp.add(lastId);
                temp.add(obats[i][0].toString());
                temp.add(obats[i][2].toString());
                temp.add(obats[i][3].toString());   
                temp.add(obats[i][4].toString()); 
                paramTransaction.add(temp);
                
                parameter2.add("UPDATE obat SET STOK=STOK-? WHERE KODE_OBAT=?");
                ArrayList<String> temp2 = new ArrayList<>();
                temp2.add(obats[i][3].toString());
                temp2.add(obats[i][0].toString());
                paramTransaction2.add(temp2);
                
                parameter3.add("CALL update_pembelian_from_retur (?,?,?)");
                ArrayList<String> temp3 = new ArrayList<>();
                temp3.add(faktur[1].toString());
                temp3.add(obats[i][0].toString());
                temp3.add(obats[i][3].toString());                
                paramTransaction3.add(temp3);
            }
            boolean finalResult = conn.insertTransaction(parameter, paramTransaction);
            boolean finalResult2 = conn.insertTransaction(parameter2, paramTransaction2);
            boolean finalResult3 = conn.insertTransaction(parameter3, paramTransaction3);
            if (finalResult && finalResult2 && finalResult3) return lastId;
        }
        return null;
    }
}
