package com.ecquaria.cloud.moh.iais.helper.excel;

/*
 *author: yichen
 *date time:9/18/2019 1:07 PM
 *description:
 */

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.common.annotation.ExcelSheetProperty;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@Slf4j
public final class ExcelWriter {
    private static String EXCEL_TYPE_XSSF = "xlsx";

    private String fileName;

    private File file;

    private int[][] unlockCell = null;

    private Map<Integer, List<Integer>> unlockCellMap;

    private  XSSFCellStyle lockStyle = null;

    private  XSSFCellStyle unlockStyle = null;

    private Class<?> clz;

    private int startCellIndex = 1;

    private boolean hasNeedCellName = true;

    private boolean newModule = true;

    private String sheetName;

    private static XSSFWorkbook workbook;

    protected String password = "password$1";

    private boolean needBlock = false;

    public void setNeedBlock(boolean needBlock) {
        this.needBlock = needBlock;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setNewModule(boolean newModule) {
        this.newModule = newModule;
    }

    public void setUnlockCellMap(Map<Integer, List<Integer>> unlockCellMap) {
        this.unlockCellMap = unlockCellMap;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
    }

    public void setUnlockCell(int[][] unlockCell) {
        this.unlockCell = unlockCell;
    }

    public void setHasNeedCellName(boolean hasNeedCellName) {
        this.hasNeedCellName = hasNeedCellName;
    }

    private void initCheck() throws ClassNotFoundException {
        if (clz == null){
            throw new ClassNotFoundException("can not find excel source class");
        }

        fileName = generationFileName();
    }

    private void initSheetProperty(){
        ExcelSheetProperty property = clz.getAnnotation(ExcelSheetProperty.class);
        if (property == null){
            System.out.println("can not find sheet property!");
        }

        startCellIndex = property.startIndex();
        sheetName = property.sheetName();
    }

    private  void setFieldName(final Class<?> clz, final Sheet sheet) {
        Row sheetRow = sheet.createRow(startCellIndex - 1);
        Cell firstCell = sheetRow.createCell(0);
        firstCell.setCellValue("SN");
        firstCell.setCellStyle(lockStyle);
        Field[] fields = clz.getDeclaredFields();


        List<Integer> autoSizeCell = IaisCommonUtils.genNewArrayList();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ExcelProperty.class)) {
                ExcelProperty annotation = field.getAnnotation(ExcelProperty.class);
                int rowIndx = annotation.index();
                String rowName = annotation.cellName();

                Cell cell = sheetRow.createCell(rowIndx);
                cell.setCellStyle(unlockStyle);
                cell.setCellValue(rowName);

                autoSizeCell.add(rowIndx);
            }
        }

        autoSizeCell(sheet, autoSizeCell);
    }

    private void autoSizeCell(Sheet sheet, List<Integer> cell){
        if (sheet != null && !IaisCommonUtils.isEmpty(cell)){
            for (Integer i : cell){
                int columnWidth = sheet.getColumnWidth(i) / 256;
                sheet.setColumnWidth(i, columnWidth * 512);
                sheet.autoSizeColumn(i);
            }
        }
    }

    private void startInternal() throws ClassNotFoundException {
        initCheck();

        initSheetProperty();
    }

    public File writerToExcel(final List<?> source) throws Exception {
        startInternal();

        if (source == null) {
            log.info("don't have source when writer to excel!!!!");
            throw new IaisRuntimeException("Please check the export excel parameters.");
        }

        File out = file;
        final String localFileName = fileName;
        Sheet sheet = null;
        log.info(StringUtil.changeForLog("current workspace ") + sheetName);
        log.info("current filename " + localFileName);
        if (newModule){
            out = new File(localFileName);
            try (FileOutputStream fileOutputStream = new FileOutputStream(out)) {
                workbook = XSSFWorkbookFactory.createWorkbook();
                sheet  = workbook.createSheet(sheetName);
                doParse(source, sheet);
                try {
                    workbook.write(fileOutputStream);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            } catch (IOException e) {
                throw new ClassNotFoundException("has IO error when when export excel");
            }
        }else {
            try (FileInputStream fileInputStream = new FileInputStream(out)) {
                workbook = XSSFWorkbookFactory.createWorkbook(fileInputStream);
                sheet = workbook.getSheet(sheetName);

                doParse(source, sheet);

                try (OutputStream outputStream = new FileOutputStream(localFileName)){
                    workbook.write(outputStream);
                    out = new File(localFileName);
                } catch (Exception e) {
                    throw new ClassNotFoundException("has  error when when export excel");
                }

            } catch (IOException e) {
                throw new ClassNotFoundException("has  error when when export excel");
            }
        }

        return out;
    }

    private void doParse(List<?> source, Sheet sheet){
        initLockStyle();

        initUnlockStyle();

        if (unlockCell != null && unlockCell.length > 0){
            unlockCell(sheet);
        }

        if (unlockCellMap != null && !unlockCellMap.isEmpty()){
            unlockCellByMap(sheet);
        }

        if (hasNeedCellName){
            setFieldName(clz, sheet);

        }

        try {
            createCellValue(sheet, source);
        } catch (NoSuchMethodException e) {
            log.error("========NoSuchMethodException=>>>>>>>>>>>>>>", e);
        } catch (IllegalAccessException e) {
            log.error("========IllegalAccessException=>>>>>>>>>>>>>>", e);
        } catch (InvocationTargetException e) {
            log.error("========InvocationTargetException=>>>>>>>>>>>>>>", e);
        }

        if (needBlock){
            sheet.protectSheet(password);
        }
    }

    private void createCellValue(final Sheet sheet, final List<?> source) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        //sequence number
        int cellIndex = startCellIndex;
        int sequence = 1;
        for (Object t : source) {
            Row sheetRow = sheet.createRow(cellIndex);
            Cell firstCell = sheetRow.createCell(0);
            firstCell.setCellStyle(lockStyle);
            firstCell.setCellValue(sequence);

            sequence++;
            cellIndex++;

            Field[] fields = clz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(ExcelProperty.class)) {
                    ExcelProperty annotation = field.getAnnotation(ExcelProperty.class);
                    int rowIndx = annotation.index();
                    Cell cell = sheetRow.createCell(rowIndx);
                    boolean readOnly = annotation.readOnly();
                    boolean hidden = annotation.hidden();

                    if (readOnly){
                        cell.setCellStyle(lockStyle);
                    }else {
                        cell.setCellStyle(unlockStyle);
                    }

                    if (hidden){
                        sheet.setColumnHidden(rowIndx, true);
                    }

                    cell.setCellValue(setValue(clz.getDeclaredMethod("get" +
                            StringUtils.capitalize(field.getName())).invoke(t)));
                }
            }

        }

    }

    private String setValue(final Object obj) {
        return obj == null ? "" : obj.toString();
    }

    private  String generationFileName() {
        long currentTimeMillis = System.currentTimeMillis();
        String jointText = currentTimeMillis + "." + EXCEL_TYPE_XSSF;
        if (StringUtils.isEmpty(fileName)){
            fileName = clz.getSimpleName();
        }

        return fileName + "-" + jointText;
    }

    private void unlockCell(Sheet sheet){
        int[][] val = unlockCell;
        for (int i = 0; i < val.length; i++){
            for (int j = 0; j < val[i].length; j++){
                Row row = sheet.getRow(i);
                if (row != null){
                    Cell cell = row.getCell(j);
                    if (cell != null){
                        cell.setCellStyle(unlockStyle);
                    }
                }
            }
        }
    }

    private void unlockCellByMap(Sheet sheet){
        Map<Integer, List<Integer>> val = unlockCellMap;
        for (Map.Entry<Integer, List<Integer>> ent : val.entrySet()){
            Integer rowIndex = ent.getKey();
            List<Integer> cellIndex = ent.getValue();
            for (Integer i : cellIndex){
                Row row = sheet.getRow(rowIndex);
                if (row != null){
                    Cell cell = row.getCell(i);
                    if (cell != null){
                        cell.setCellStyle(unlockStyle);
                    }
                }
            }
        }
    }

    private  void initUnlockStyle(){
        if (workbook != null) {
            XSSFCellStyle xssfCellStyle = workbook.createCellStyle();
            xssfCellStyle.setLocked(false);
            xssfCellStyle.setHidden(false);
            xssfCellStyle.setAlignment(HorizontalAlignment.CENTER);
            unlockStyle = xssfCellStyle;
        }
    }

    private void initLockStyle() {
        if (workbook!= null) {
            XSSFCellStyle xssfCellStyle = workbook.createCellStyle();
            xssfCellStyle.setAlignment(HorizontalAlignment.CENTER);
            xssfCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            xssfCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
            xssfCellStyle.setLocked(true);
            xssfCellStyle.setHidden(true);
            lockStyle = xssfCellStyle;
        }
    }
}
