/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package report;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.builder.component.MultiPageListBuilder;
import net.sf.dynamicreports.report.builder.datatype.DataTypes;
import net.sf.dynamicreports.report.builder.group.ColumnGroupBuilder;
import net.sf.dynamicreports.report.builder.subtotal.SubtotalBuilder;
import net.sf.dynamicreports.report.constant.GroupHeaderLayout;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Muhammad Hanif B
 */
public class ProcurementReport extends BaseReport{
    
    public ProcurementReport () {}
    
    public void createReportById (String orderId) {   
        Date tglFaktur = null;
        
        try {
            ResultSet result = null;
            String tanggal_faktur = null, nomor_faktur = null, supplier = null;
            try {
                result = conn.query("select dp.kode_obat, o.nama_obat, dp.batch_obat, DATE_FORMAT(dp.expired,'%d/%m/%Y') as expired, dp.jumlah_beli, dp.harga_beli, (dp.jumlah_beli*dp.harga_beli) as total_beli, p.tanggal_faktur, p.nomor_faktur, p.supplier, p.total from detail_pembelian dp left join pembelian p on dp.pembelian_id = p.pembelian_id \n" +
"left join obat o on dp.kode_obat = o.kode_obat where dp.pembelian_id = "+orderId, null);
                result.next();
                tanggal_faktur = result.getString("tanggal_faktur");
                tglFaktur = dbFormat.parse(tanggal_faktur);
                nomor_faktur = result.getString("nomor_faktur");
                supplier = result.getString("supplier");
                result.previous();
            } catch (    SQLException | ParseException ex) {
                Logger.getLogger(ProcurementReport.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            totalColumn = Columns.column("Total", "total_beli", currencyType)
                    .setPattern(currencyType.getPattern())
                    .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
            report
                    .setPageMargin(DynamicReports.margin(40))
                    .setColumnTitleStyle(columnTitleStyle)
                    .highlightDetailEvenRows()
                    .setSubtotalStyle(boldCenteredStyle)
                    .columns(
                            rowNumberColumn,
                            Columns.column("Kode", "kode_obat", DataTypes.stringType()).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),
                            Columns.column("Nama", "nama_obat", DataTypes.stringType()).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),
                            Columns.column("Batch", "batch_obat", DataTypes.stringType()).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),
                            Columns.column("Exp", "expired", DataTypes.stringType()).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),
                            Columns.column("Jml", "jumlah_beli", DataTypes.integerType()).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),
                            Columns.column("Harga", "harga_beli", currencyType).setPattern(currencyType.getPattern()).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),                                                        
                            totalColumn
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
                                                            Components.text("Tlp. 08111686677").setStyle(boldCenteredStyle),
                                                            Components.text("APA : Dewi Rahmayani, S. Farm, Apt").setStyle(boldCenteredStyle),
                                                            Components.text("No. SIPA : 19860516/SIPA_32.01/2016/2-00068").setStyle(boldCenteredStyle),
                                                            Components.text("No. SIA : 449/ IA/00044/BPMTSP/2016").setStyle(boldCenteredStyle)
                                                        )
                                                ),       
                                            Components.text("\nNOTA BELANJA").setStyle(boldCenteredStyle),
                                            Components.horizontalList()
                                                .add(
                                                        Components.text("TANGGAL").setFixedWidth(80),
                                                        Components.text(":").setFixedWidth(10),
                                                        Components.text(reportFormat.format(tglFaktur))
                                                ),
                                            Components.horizontalList()
                                                .add(
                                                        Components.text("NO. FAKTUR").setFixedWidth(80),
                                                        Components.text(":").setFixedWidth(10),
                                                        Components.text(nomor_faktur)
                                                ),
                                            Components.horizontalList()
                                                .add(
                                                        Components.text("SUPPLIER").setFixedWidth(80),
                                                        Components.text(":").setFixedWidth(10),
                                                        Components.text(supplier)
                                                )
                                    ),                            
                            Components.text("")
                    )
                    .pageFooter(Components.pageXofY().setStyle(boldCenteredStyle))                    
                    .subtotalsAtSummary(DynamicReports.sbt.sum(totalColumn))
                    .setDataSource(result);
            
            JasperViewer viewer = new JasperViewer(report.toJasperPrint(), false);
            viewer.setExtendedState(JFrame.MAXIMIZED_BOTH);
            viewer.setTitle("Cetak Laporan Belanja");
            viewer.setVisible(true); 
        } catch (DRException ex) {
            Logger.getLogger(ProcurementReport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void createReportByDate (String dateFrom, String dateTo) {
        Date from = null, to = null;
        try {
            from = dbFormat.parse (dateFrom);
            to = dbFormat.parse (dateTo);
        } catch (ParseException ex) {
            Logger.getLogger(ProcurementReport.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            ResultSet result = null;
            String query = "select dp.pembelian_id, concat(p.nomor_faktur,' - ',date_format(p.tanggal_faktur, '%d/%m/%Y')) as faktur, \n" +
"dp.kode_obat, o.nama_obat, o.satuan_obat, dp.jumlah_beli, dp.harga_beli, dp.subtotal_beli \n" +
"from detail_pembelian dp\n" +
"left join pembelian p on p.pembelian_id = dp.pembelian_id\n" +
"left join obat o on o.kode_obat = dp.kode_obat\n" +
"where p.tanggal_faktur between ? and ? order by 1";
            
            ArrayList<String> param = new ArrayList<>();
            param.add(dateFrom);
            param.add(dateTo);
            
            try {
                result = conn.query(query, param);
            } catch (SQLException ex) {
                Logger.getLogger(ProcurementReport.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            TextColumnBuilder<String> itemColumn = DynamicReports.col.column("NO. FAKTUR :", "faktur", DataTypes.stringType());
            ColumnGroupBuilder group = DynamicReports.grp.group(itemColumn)
                    .setHeaderLayout(GroupHeaderLayout.TITLE_AND_VALUE);
//                    .showColumnHeaderAndFooter();
            
            totalColumn = Columns.column("Total", "subtotal_beli", currencyType)
                    .setPattern(currencyType.getPattern())
                    .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
            SubtotalBuilder subt = DynamicReports.sbt.sum(totalColumn);
            
            report
                    .setPageMargin(DynamicReports.margin(40))
                    .setColumnTitleStyle(columnTitleStyle)
                    .highlightDetailEvenRows()
                    .setSubtotalStyle(boldCenteredStyle)
                    .columns(                                                        
                            Columns.column("Kode", "kode_obat", DataTypes.stringType()).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),
                            Columns.column("Nama", "nama_obat", DataTypes.stringType()).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),
                            Columns.column("Satuan", "satuan_obat", DataTypes.stringType()).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),
                            Columns.column("Jml", "jumlah_beli", DataTypes.integerType()).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),                            
                            Columns.column("Harga", "harga_beli", currencyType).setPattern(currencyType.getPattern()).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),                            
                            totalColumn
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
                                Components.text("\nDETAIL BELANJA").setStyle(boldCenteredStyle),
                                Components.text("\n"+reportFormat.format(from)+" - "+reportFormat.format(to)).setStyle(boldCenteredStyle)
                            ),                            
                        Components.text("")
                    )
                    .groupBy(group)
                    .subtotalsAtGroupFooter(group, subt)
                    .pageFooter(Components.pageXofY().setStyle(boldCenteredStyle))
                    .subtotalsAtSummary(DynamicReports.sbt.sum(totalColumn))
                    .setDataSource(result);
            
            /* Summary */
            MultiPageListBuilder builder = Components.multiPageList();
            builder.newPage();
            
            JasperReportBuilder summaryReport = DynamicReports.report();
            String querySummary = "select dp.kode_obat, o.nama_obat, o.satuan_obat, sum(dp.jumlah_beli) as jumlah, sum(dp.subtotal_beli) as subtotal_beli from detail_pembelian dp \n" +
"left join pembelian p on dp.pembelian_id = p.pembelian_id\n" +
"left join obat o on o.kode_obat = dp.kode_obat\n" +
"where p.tanggal_faktur between ? and ?\n" +
"group by dp.kode_obat order by 1";
            
            try {
                summaryReport
                        .setColumnTitleStyle(columnTitleStyle)
                        .highlightDetailEvenRows()
                        .setSubtotalStyle(boldCenteredStyle)
                        .columns(
                                rowNumberColumn,
                                Columns.column("Kode", "kode_obat", DataTypes.stringType()).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),
                                Columns.column("Nama", "nama_obat", DataTypes.stringType()).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),
                                Columns.column("Satuan", "satuan_obat", DataTypes.stringType()).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),
                                Columns.column("Jml", "jumlah", DataTypes.integerType()).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),
                                totalColumn
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
                                    Components.text("\nREKAP BELANJA").setStyle(boldCenteredStyle),
                                    Components.text("\n"+reportFormat.format(from)+" - "+reportFormat.format(to)).setStyle(boldCenteredStyle)
                                ),                            
                            Components.text("")
                        )
                        .pageFooter(Components.pageXofY().setStyle(boldCenteredStyle))
                        .subtotalsAtSummary(DynamicReports.sbt.sum(totalColumn))
                        .setDataSource(conn.query(querySummary, param));
                builder.add(Components.subreport(summaryReport));
                builder.newPage();
            } catch (SQLException ex) {
                Logger.getLogger(ProcurementReport.class.getName()).log(Level.SEVERE, null, ex);
            }
            report.summary(builder);
            
            /* End Summary*/
            
            JasperViewer viewer = new JasperViewer(report.toJasperPrint(), false);
            viewer.setExtendedState(JFrame.MAXIMIZED_BOTH);
            viewer.setTitle("Cetak Laporan Belanja");
            viewer.setVisible(true);
        } catch (DRException ex) {
            Logger.getLogger(ProcurementReport.class.getName()).log(Level.SEVERE, null, ex);
        }
  
    }
}
