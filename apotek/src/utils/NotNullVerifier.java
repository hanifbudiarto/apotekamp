/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package utils;

import java.awt.Color;
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
public class NotNullVerifier extends InputVerifier {

    Border originalBorder ;
    Border errorBorder = BorderFactory.createLineBorder(Color.RED);
    JLabel errorLabel;    
    boolean isGetOriginalBorder = true;
    
    public void setErrorLabel(JLabel label) {
        this.errorLabel = label;
    }
    
    @Override
    public boolean verify(JComponent input) { 
        JTextField textField = (JTextField) input;        
        if(isGetOriginalBorder) {
            originalBorder = textField.getBorder();
            isGetOriginalBorder = false;
        }
        
        if (textField.getText().length() != 0 || !textField.getText().isEmpty()) {
            textField.setBorder(originalBorder);
            errorLabel.setText("");
            return true;
        }
        else {
            textField.setBorder(errorBorder);
            errorLabel.setText("Tidak boleh kosong");
            return false;
        }  
    }
    
}
