/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package utils;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 *
 * @author Muhammad Hanif B
 */
public class SpecificValueVerifier extends InputVerifier {

    Border originalBorder ;
    Border errorBorder = BorderFactory.createLineBorder(Color.RED);
    JLabel errorLabel;    
    boolean isGetOriginalBorder = true;
    ArrayList<String> values = new ArrayList<>();
    
    public void setErrorLabel(JLabel label) {
        this.errorLabel = label;
    }
    
    public void setArrValues(ArrayList<String> val) {
        this.values = val;
    }
    
    @Override
    public boolean verify(JComponent input) { 
        JTextField textField = (JTextField) input;        
        if(isGetOriginalBorder) {
            originalBorder = textField.getBorder();
            isGetOriginalBorder = false;
        }
        
        String tfVal = textField.getText().toUpperCase();
        for (String value : values) {
            if (tfVal.equals(value.toUpperCase())) {
                textField.setBorder(originalBorder);
                errorLabel.setText("");
                return true;
            }
        }

        textField.setBorder(errorBorder);
        String errorMsg = "Hanya bisa diisi nilai berikut : ";
        boolean isFirst = true;
        for (String value : values) {
            if (isFirst) {
                isFirst = false;
            }
            else errorMsg += ", ";
            errorMsg += value.toUpperCase();
        }
        errorLabel.setText(errorMsg);
        return false;
    }
    
}
