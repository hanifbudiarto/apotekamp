package report;

import database.MysqlConnect;
import java.awt.Color;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.datatype.BigDecimalType;
import net.sf.dynamicreports.report.builder.style.BorderBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.VerticalAlignment;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hanif
 */
public class BaseReport {
   
    StyleBuilder boldStyle, boldCenteredStyle, columnTitleStyle, standarStyle, titleStyle;
    StyleBuilder h1Style;
    BorderBuilder borderBuilder;
    TextColumnBuilder<Integer> rowNumberColumn;
    TextColumnBuilder<BigDecimal> totalColumn;
    
    MysqlConnect conn;
    JasperReportBuilder report;
    
    DateFormat reportFormat;
    DateFormat dbFormat;
    
    public CurrencyType currencyType;
    
    public BaseReport () {
        this.currencyType = new CurrencyType();
        
        conn = MysqlConnect.getDbCon();
        report = DynamicReports.report();
        this.initStyleReport();
        
        reportFormat = new SimpleDateFormat("dd MMMM yyyy");
        dbFormat = new SimpleDateFormat("yyyy-MM-dd");
    }
    
    private void initStyleReport () {
        boldStyle = DynamicReports.stl.style().bold();
        boldCenteredStyle = DynamicReports.stl.style(boldStyle)
                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE);
        columnTitleStyle = DynamicReports.stl.style(boldCenteredStyle)
                        .setBorder(DynamicReports.stl.pen1Point())
                        .setBackgroundColor(Color.decode("#a6cb67"));
        standarStyle = DynamicReports.stl.style()
                        .setFontSize(7)
                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE);
        rowNumberColumn = DynamicReports.col.reportRowNumberColumn("No. ")
                        .setFixedColumns(2)
                        .setHorizontalAlignment(HorizontalAlignment.CENTER);
        titleStyle = DynamicReports.stl.style(standarStyle)
                        .setBackgroundColor(Color.LIGHT_GRAY);   
        h1Style = DynamicReports.stl.style(boldCenteredStyle)
                .setFontSize(20);
    }
    
    public class CurrencyType extends BigDecimalType {
        @Override
        public String getPattern() {
            // "Rp #,###.00"
            return "Rp #,###.00";
        }
    }
}
