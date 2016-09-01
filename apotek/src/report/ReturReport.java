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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Muhammad Hanif B
 */
public class ReturReport extends BaseReport {
    
    public void createReportById (String returId, String tanggalRetur, String nomorFaktur, String supplier) {         
        try {
            ResultSet result = null;
            try {
                result = conn.query(
"SELECT dr.kode_obat, o.nama_obat, dr.jumlah_retur, dr.harga_beli, dr.subtotal_retur \n" +
"FROM detail_retur dr LEFT JOIN obat o ON dr.kode_obat = o.kode_obat \n" +
"WHERE dr.retur_id = "+returId, null);
            } catch (SQLException ex) {
                Logger.getLogger(ReturReport.class.getName()).log(Level.SEVERE, null, ex);
            }

            totalColumn = Columns.column("Total", "subtotal_retur", currencyType)
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
                            Columns.column("Jml", "jumlah_retur", DataTypes.integerType()).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),
                            Columns.column("Harga", "harga_beli", currencyType).setPattern(currencyType.getPattern()).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),                            
                            totalColumn
                    )
                    .title (
                            Components.verticalList()
                                    .add(
                                            Components.horizontalList()
                                                .add(
                                                    Components.image(getClass().getResource("/resources/logo-kop.png")).setHorizontalAlignment(HorizontalAlignment.LEFT),
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
                                            Components.text("\nNOTA RETUR").setStyle(boldCenteredStyle),
                                            Components.horizontalList()
                                                .add(
                                                        Components.text("TGL RETUR").setFixedWidth(80),
                                                        Components.text(":").setFixedWidth(10),
                                                        Components.text(tanggalRetur)
                                                ),
                                            Components.horizontalList()
                                                .add(
                                                        Components.text("NO. FAKTUR").setFixedWidth(80),
                                                        Components.text(":").setFixedWidth(10),
                                                        Components.text(nomorFaktur)
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
            viewer.setTitle("Cetak Laporan Retur");
            viewer.setVisible(true);
        } catch (DRException ex) {
            Logger.getLogger(ReturReport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void createReportByDate (String dateFrom, String dateTo) {
        Date from = null, to = null;
        try {
            from = dbFormat.parse (dateFrom);
            to = dbFormat.parse (dateTo);
        } catch (ParseException ex) {
            Logger.getLogger(ReturReport.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            ResultSet result = null;
            String query = "select dr.retur_id, concat(r.nomor_faktur,' - ',date_format(r.tanggal_retur, '%d/%m/%Y')) as faktur, \n" +
"dr.kode_obat, o.nama_obat, o.satuan_obat, dr.jumlah_retur, dr.harga_beli, dr.subtotal_retur \n" +
"from detail_retur dr\n" +
"left join retur r on r.retur_id = dr.retur_id\n" +
"left join obat o on o.kode_obat = dr.kode_obat\n" +
"where r.tanggal_retur between ? and ? order by 1";
            
            ArrayList<String> param = new ArrayList<>();
            param.add(dateFrom);
            param.add(dateTo);
            
            try {
                result = conn.query(query, param);
            } catch (SQLException ex) {
                Logger.getLogger(ReturReport.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            TextColumnBuilder<String> itemColumn = DynamicReports.col.column("NO. FAKTUR :", "faktur", DataTypes.stringType());
            ColumnGroupBuilder group = DynamicReports.grp.group(itemColumn)
                    .setHeaderLayout(GroupHeaderLayout.TITLE_AND_VALUE);
            
            totalColumn = Columns.column("Total", "subtotal_retur", currencyType)
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
                        Columns.column("Jml", "jumlah_retur", DataTypes.integerType()).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),                            
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
                                        Components.text("\nDETAIL RETUR").setStyle(boldCenteredStyle),
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
            String querySummary = "select dr.kode_obat, o.nama_obat, o.satuan_obat, sum(dr.jumlah_retur) as jumlah, sum(dr.subtotal_retur) as subtotal_retur \n" +
"from detail_retur dr \n" +
"left join retur r on dr.retur_id = r.retur_id\n" +
"left join obat o on dr.kode_obat = o.kode_obat\n" +
"where r.tanggal_retur between ? and ?\n" +
"group by dr.kode_obat order by 1";
            
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
                                    Components.text("\nREKAP RETUR").setStyle(boldCenteredStyle),
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
            viewer.setTitle("Cetak Laporan Retur");
            viewer.setVisible(true);
        } catch (DRException ex) {
            Logger.getLogger(ReturReport.class.getName()).log(Level.SEVERE, null, ex);
        }
  
    }
}
