/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package admin;

import database.MysqlConnect;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Muhammad Hanif B
 */
public class Hutang {
    
    private final ArrayList<String> parameter;
    private final ArrayList<ArrayList<String>> paramTransaction;
    DateFormat uiFormat;
    DateFormat dbFormat;
    
    public Hutang () {
        parameter = new ArrayList<>();
        paramTransaction = new ArrayList<>();
        
        uiFormat = new SimpleDateFormat("dd-MM-yyyy");
        dbFormat = new SimpleDateFormat("yyyy-MM-dd");
    }
    
    public ResultSet getAllHutang (String conditions) {
        MysqlConnect conn = MysqlConnect.getDbCon();
        if (conn != null) {
            String query = "SELECT HUTANG_ID AS Nomor, DATE_FORMAT(TANGGAL,'%d-%m-%Y') AS Tanggal, SUPPLIER as Supplier, JUMLAH as 'Total Hutang', BAYAR as 'Sudah Terbayar', KEKURANGAN as Kekurangan, DATE_FORMAT(DEADLINE,'%d-%m-%Y') AS 'Jatuh Tempo' FROM hutang";
            query += conditions;
            try {
                ResultSet rset = conn.query(query, null);
                return rset;
            } catch (SQLException ex) {
                Logger.getLogger(Hutang.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    public boolean BayarHutang (String jml, String id) {
        MysqlConnect conn = MysqlConnect.getDbCon();
        if (conn != null) {
            String query = "UPDATE hutang SET BAYAR=BAYAR+?, KEKURANGAN=KEKURANGAN-? WHERE HUTANG_ID=?";
            ArrayList<String> param = new ArrayList<>();
            param.add(jml);
            param.add(jml);
            param.add(id);
            
            return conn.insert(query, param);
        }
        return false;
    }
    
    
    public boolean insert (Object [][] hutangs) {
        MysqlConnect conn = MysqlConnect.getDbCon();
        
        String query = "TRUNCATE hutang";               
        parameter.add(query);
        paramTransaction.add(null);
        
        int rowLength = hutangs.length;
        for (int i=0; i<rowLength; i++){
            parameter.add("INSERT INTO hutang (TANGGAL,SUPPLIER,JUMLAH,BAYAR,KEKURANGAN,DEADLINE) VALUES(?,?,?,?,?,?)");
            ArrayList<String> paramInsert = new ArrayList<>();
            
            Date tglInput = null, tglTempo = null;
            try {
                tglInput = uiFormat.parse(hutangs[i][1].toString());
                tglTempo = uiFormat.parse(hutangs[i][6].toString());
            } catch (ParseException ex) {
                Logger.getLogger(Hutang.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            paramInsert.add(dbFormat.format(tglInput));
            paramInsert.add(hutangs[i][2].toString());
            paramInsert.add(hutangs[i][3].toString());
            paramInsert.add(hutangs[i][4].toString());
            paramInsert.add(hutangs[i][5].toString());
            paramInsert.add(dbFormat.format(tglTempo));
            paramTransaction.add(paramInsert);
        }
        boolean finalResult = conn.insertTransaction(parameter, paramTransaction);
        return finalResult;
    }
    
}
