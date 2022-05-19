package com.ecquaria.cloud.moh.iais.helper.excel;

import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author yichen
 * @Date:2020/8/27
 */

public final class CellStyleHelper {
    public CellStyleHelper(){}

    /**
     * Cell color, locked status, hidden or not
     */
    private static XSSFCellStyle lockStyle = null;

    /**
     * If without this value, the generated data will be locked
     */
    private static XSSFCellStyle unlockStyle = null;

    private static XSSFCellStyle textStyle = null;

    public static void initUnlockStyle(XSSFWorkbook workbook){
        if (workbook != null) {
            XSSFCellStyle xssfCellStyle = workbook.createCellStyle();
            xssfCellStyle.setLocked(false);
            xssfCellStyle.setHidden(false);
            xssfCellStyle.setAlignment(HorizontalAlignment.CENTER);
            xssfCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            xssfCellStyle.setWrapText(true);
            unlockStyle = xssfCellStyle;
        }
    }

    public static void initTextStyle(XSSFWorkbook workbook){
        if (workbook != null) {
            XSSFCellStyle xssfCellStyle = workbook.createCellStyle();
            XSSFDataFormat format = workbook.createDataFormat();
            xssfCellStyle.setDataFormat(format.getFormat("@"));
            textStyle = xssfCellStyle;
        }
    }

    public static void initLockStyle(XSSFWorkbook workbook) {
        if (workbook!= null) {
            XSSFCellStyle xssfCellStyle = workbook.createCellStyle();
            xssfCellStyle.setAlignment(HorizontalAlignment.CENTER);
            xssfCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            xssfCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            xssfCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
            xssfCellStyle.setLocked(true);
            xssfCellStyle.setHidden(true);
            xssfCellStyle.setWrapText(true);
            lockStyle = xssfCellStyle;
        }
    }

    public static XSSFCellStyle getLockStyle() {
        return lockStyle;
    }

    public static XSSFCellStyle getUnlockStyle() {
        return unlockStyle;
    }

    public static XSSFCellStyle getTextStyle() {
        return textStyle;
    }

    public static XSSFCellStyle getXSSFCellStyle(XSSFRow sheetRow, boolean readonly, boolean hidden) {
        XSSFWorkbook workbook = sheetRow.getSheet().getWorkbook();
        XSSFCellStyle xssfCellStyle = workbook.createCellStyle();
        xssfCellStyle.setLocked(readonly);
        xssfCellStyle.setHidden(hidden);
        xssfCellStyle.setAlignment(HorizontalAlignment.CENTER);
        xssfCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        xssfCellStyle.setWrapText(true);
        XSSFDataFormat format = workbook.createDataFormat();
        xssfCellStyle.setDataFormat(format.getFormat("@"));
        return xssfCellStyle;
    }

}
