/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author hanif
 */
public class SelectedPopupItem {
    private static String selectedItem;

    public static String getSelectedItem() {
        return selectedItem;
    }

    public static void setSelectedItem(String selectedItem) {
        SelectedPopupItem.selectedItem = selectedItem;
    }
}
