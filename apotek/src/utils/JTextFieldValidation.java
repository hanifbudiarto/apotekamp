/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package utils;

import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Muhammad Hanif B
 */
public class JTextFieldValidation {
    JPanel panel;
    JLabel errorLabel;
    ArrayList<JTextField> fields = new ArrayList<>();
    
    public JTextFieldValidation (JPanel panel, JLabel label, ArrayList<JTextField> fields) {
        this.panel = panel;
        this.errorLabel = label;
        this.fields = fields;
    }
    
    public boolean validate () {
        panel.requestFocusInWindow();
        if (errorLabel.getText().equals("")) {               
            boolean isAllFieldsFilled = true;
            for (JTextField field : fields) {
                if (field.getText().isEmpty() && field.isEnabled()){
                    field.requestFocus();
                    panel.requestFocusInWindow();
                    isAllFieldsFilled = false;
                    break;
                }                
            }
            return isAllFieldsFilled;
        }
        return false;
    }    
}
