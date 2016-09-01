/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package report;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Muhammad Hanif B
 */
public class SaleReport extends BaseReport {
    
    public void createReportById (String orderId) {   
        report.setPageFormat(200, 1700, PageOrientation.PORTRAIT);
        
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date tgl = new Date();
        Date tglFaktur = null;
        try {
            ResultSet result = null;
            String nomor_faktur = null;
            try {
                result = conn.query("select concat(dp.kode_obat,'-',o.nama_obat) as obat, concat(dp.jumlah_jual,' @Rp ',dp.harga_jual) as keterangan, subtotal_jual, p.tanggal_faktur, p.nomor_faktur\n" +
"from detail_penjualan dp left join penjualan p on dp.penjualan_id = p.penjualan_id\n" +
"left join obat o on dp.kode_obat = o.kode_obat where dp.penjualan_id = "+orderId, null);
                result.next();
                tglFaktur = dbFormat.parse(result.getString("tanggal_faktur"));
                nomor_faktur = result.getString("nomor_faktur");
                result.previous();
            } catch (    SQLException | ParseException ex) {
                Logger.getLogger(SaleReport.class.getName()).log(Level.SEVERE, null, ex);
            }

            totalColumn = Columns.column("Total", "subtotal_jual", currencyType)
                   .setPattern(currencyType.getPattern())
                   .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);

            report
                    .setColumnTitleStyle(titleStyle)
                    .highlightDetailEvenRows()
                    .setSubtotalStyle(standarStyle)
                    .columns(
                            Columns.column("Kode", "obat", DataTypes.stringType()).setStyle(standarStyle),
                            Columns.column("Ket.", "keterangan", DataTypes.stringType()).setStyle(standarStyle),                                          
                            totalColumn.setStyle(standarStyle)
                    )
                    .title (
                            Components.horizontalList()
                                    .add(
                                        Components.text(""),                                        
                                        Components.image(getClass().getResource("/resources/rsz_logo-kop.png")).setStyle(standarStyle).setFixedDimension(82, 50),
                                        Components.text("")
                                    ),                             
                            Components.verticalList()
                                    .add(            
                                            Components.text("\nJl. Raya Sabilillah Kp. Bojong Baru Citereup-Bogor\nTlp. 08111686677\nAPA : Dewi Rahmayani, S. Farm, Apt\nNo. SIPA : 19860516/SIPA_32.01/2016/2-00068\nNo. SIA : 449/ IA/00044/BPMTSP/2016").setStyle(standarStyle),
                                            Components.text("\nSTRUK").setStyle(standarStyle),
                                            Components.horizontalList()
                                                .add(
                                                        Components.text("TANGGAL").setStyle(standarStyle),
                                                        Components.text(":").setStyle(standarStyle),
                                                        Components.text(reportFormat.format(tglFaktur)).setStyle(standarStyle)
                                                ),
                                            Components.horizontalList()
                                                .add(
                                                        Components.text("NO. FAKTUR").setStyle(standarStyle),
                                                        Components.text(":").setStyle(standarStyle),
                                                        Components.text(nomor_faktur).setStyle(standarStyle)
                                                )
                                    ),                            
                            Components.text("")
                    )
                    .pageFooter(Components.text("TERIMA KASIH ATAS KUNJUNGAN ANDA").setStyle(standarStyle))
                    .pageFooter(Components.text(dateFormat.format(tgl)).setStyle(standarStyle))
                    .subtotalsAtSummary(DynamicReports.sbt.sum(totalColumn).setStyle(standarStyle))
                    .setDataSource(result);
            
            JasperViewer viewer = new JasperViewer(report.toJasperPrint(), false);
            viewer.setExtendedState(JFrame.MAXIMIZED_BOTH);
            viewer.setTitle("Cetak Laporan Penjualan");
            viewer.setVisible(true);
        } catch (DRException ex) {
            Logger.getLogger(SaleReport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void createReportByDate (String dateFrom, String dateTo) {
        Date from = null, to = null;
        try {
            from = dbFormat.parse (dateFrom);
            to = dbFormat.parse (dateTo);
        } catch (ParseException ex) {
            Logger.getLogger(SaleReport.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            ResultSet result = null;
            String query = "select dp.penjualan_id, concat(p.nomor_faktur,' - ',date_format(p.tanggal_faktur, '%d/%m/%Y')) as faktur, \n" +
"dp.kode_obat, o.nama_obat, o.satuan_obat, dp.jumlah_jual, dp.harga_jual, dp.subtotal_jual\n" +
"from detail_penjualan dp\n" +
"left join penjualan p on p.penjualan_id = dp.penjualan_id\n" +
"left join obat o on o.kode_obat = dp.kode_obat\n" +
"where p.tanggal_faktur between ? and ? order by 1";
            
            ArrayList<String> param = new ArrayList<>();
            param.add(dateFrom);
            param.add(dateTo);
            
            try {
                result = conn.query(query, param);
            } catch (SQLException ex) {
                Logger.getLogger(SaleReport.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            TextColumnBuilder<String> itemColumn = DynamicReports.col.column("NO. FAKTUR :", "faktur", DataTypes.stringType());
            ColumnGroupBuilder group = DynamicReports.grp.group(itemColumn)
                    .setHeaderLayout(GroupHeaderLayout.TITLE_AND_VALUE);
                        
            
            totalColumn = Columns.column("Total", "subtotal_jual", currencyType)
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
                            Columns.column("Jml", "jumlah_jual", DataTypes.integerType()).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),                            
                            Columns.column("Jml", "harga_jual", currencyType).setPattern(currencyType.getPattern()).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),                            
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
                                                                Components.text("Tlp. 08111686677").setStyle(boldCenteredStyle)
                                                            )
                                                    ),       
                                            Components.text("\nDETAIL PENJUALAN").setStyle(boldCenteredStyle),
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
            String querySummary = "select dp.kode_obat, o.nama_obat, o.satuan_obat, sum(dp.jumlah_jual) as jumlah_jual, sum(dp.subtotal_jual) as subtotal_jual \n" +
"from detail_penjualan dp\n" +
"left join penjualan p on dp.penjualan_id = p.penjualan_id\n" +
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
                                Columns.column("Jml", "jumlah_jual", DataTypes.integerType()).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),
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
                                    Components.text("\nREKAP PENJUALAN").setStyle(boldCenteredStyle),
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
            viewer.setTitle("Cetak Laporan Penjualan");
            viewer.setVisible(true);
        } catch (DRException ex) {
            Logger.getLogger(SaleReport.class.getName()).log(Level.SEVERE, null, ex);
        }
  
    }
}
