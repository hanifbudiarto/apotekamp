package report;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.builder.datatype.DataTypes;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.view.JasperViewer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Muhammad Hanif B
 */
public class StokMinimumReport extends BaseReport {
    
    public void createReportByThreshold (String threshold) {
         try {
            ResultSet result = null;
            String query = "select kode_obat, nama_obat, satuan_obat, kategori_obat, stok from obat where stok <= ? order by 1";
            ArrayList<String> param = new ArrayList<>();
            param.add(threshold);
            
            try {
                result = conn.query(query, param);
            } catch (SQLException ex) {
                Logger.getLogger(StokMinimumReport.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            report
                .setPageMargin(DynamicReports.margin(40))
                .setColumnTitleStyle(columnTitleStyle)
                .highlightDetailEvenRows()
                .setSubtotalStyle(boldCenteredStyle)
                .columns(
                        rowNumberColumn,
                        Columns.column("Kode", "kode_obat", DataTypes.stringType()).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),
                        Columns.column("Nama", "nama_obat", DataTypes.stringType()).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),
                        Columns.column("Satuan", "satuan_obat", DataTypes.stringType()).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),
                        Columns.column("Kategori", "kategori_obat", DataTypes.stringType()).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),
                        Columns.column("Jml", "stok", DataTypes.integerType()).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                )
                .title (
                        Components.verticalList()
                                .add(
                                        Components.horizontalList()
                                                .add(
                                                        Components.image(getClass().getResource("/resources/logo-kop.png")).setHorizontalAlignment(HorizontalAlignment.LEFT),
                                                        //Components.image(getClass().getResource("/resources/alamat-kop.png")).setHorizontalAlignment(HorizontalAlignment.RIGHT)
                                                        Components.verticalList()
                                                            .add(
                                                                Components.text("APOTEK AMP").setStyle(h1Style),
                                                                Components.text("Jl. Raya Sabilillah Kp. Bojong Baru Citereup-Bogor").setStyle(boldCenteredStyle),
                                                                Components.text("Tlp. 08111686677").setStyle(boldCenteredStyle)
                                                            )
                                                ),       
                                        Components.text("\nSTOK OBAT MINIMUM").setStyle(boldCenteredStyle)                                            
                                ),                            
                        Components.text("")
                )
                .pageFooter(Components.pageXofY().setStyle(boldCenteredStyle))                    
                .setDataSource(result);
            
            JasperViewer viewer = new JasperViewer(report.toJasperPrint(), false);
            viewer.setExtendedState(JFrame.MAXIMIZED_BOTH);
            viewer.setTitle("Cetak Laporan Stok Minimum");
            viewer.setVisible(true);
        } catch (DRException ex) {
            Logger.getLogger(StokMinimumReport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
