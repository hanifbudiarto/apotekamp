/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package admin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Muhammad Hanif B
 */
public class Settings {
    
    public static ArrayList<String> getDbConnProperties () {
        ArrayList<String> listDbConn = new ArrayList<>();
        FileInputStream inputStream = null;
        try {
            File configFile = new File("src/config/db.properties");
            inputStream = new FileInputStream(configFile);
            Properties props = new Properties();
            props.load(inputStream);
            String host = props.getProperty("hostname");
            String port = props.getProperty("port");
            
            if (host == null || port == null)
                return null;
            else {
                listDbConn.add(host);
                listDbConn.add(port);
                return listDbConn;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    
    public static boolean setDbConnProperties (String hostname, String port) {
        FileInputStream inputStream = null;
        try {
            File configFile = new File("src/config/db.properties");
            inputStream = new FileInputStream(configFile);
            Properties props = new Properties();
            props.load(inputStream);
            props.setProperty("hostname", hostname);
            props.setProperty("port", port);
            FileOutputStream outputStream = new FileOutputStream(configFile);
            props.store(outputStream, "DB Connection Properties");
            outputStream.close();
            
            return true;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
}
