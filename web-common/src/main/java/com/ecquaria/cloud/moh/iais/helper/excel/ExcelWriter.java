package com.ecquaria.cloud.moh.iais.helper.excel;

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
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * To use this class, you need to set the initial value, such as the class of the source data, and pass a list set
 * @author yi chen
 * @Date:2020/6/5
 **/
@Slf4j
public class ExcelWriter {
    private static String EXCEL_TYPE_XSSF = "xlsx";

    /**
     * Applies to the current makefile,
     * if not set, it will be generated according to the {@code Class<?> clz} by simple name
     * */
    private String fileName;

    /**
     * When creating new file instead of adding data to old file, you do not need set value.
     * by default, {@code newModule is true}, always create new file.
     */
    private File file;

    /** {code unlockCell} is a matrix, such as [3][3].
     * It will unlock the data of the first row and cell, first row and the second cell and so on,  until the end of the third row.
     */
    private int[][] unlockCell = null;

    /**
     * Same as ${@code unlockCell}, This key is row, values store the coordinates of columns
     */
    private Map<Integer, List<Integer>> unlockCellMap;

    /**
     * Cell color, locked status, hidden or not
     */
    private  XSSFCellStyle lockStyle = null;

    /**
     * If without this value, the generated data will be locked
     */
    private  XSSFCellStyle unlockStyle = null;

    /** setting source data format through a {@code Class<?>} */
    private Class<?> clz;

    private boolean hasNeedCellName = true;

    private boolean newModule = true;

    protected int startCellIndex = 0;

    protected String sheetName;

    protected int sheetAt = 0;

    private static XSSFWorkbook workbook;

    protected String zheshigaisidemima = "password$1";

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
            throw new IaisRuntimeException("can not find sheet property!");
        }

        startCellIndex = property.startRowIndex();
        sheetName = property.sheetName();
        sheetAt = property.sheetAt();
    }

    private void setFieldName(final Class<?> clz, final Sheet sheet) {
        Row sheetRow = sheet.createRow(startCellIndex);
        Cell firstCell = sheetRow.createCell(0);
        firstCell.setCellValue("SN");
        firstCell.setCellStyle(lockStyle);
        Field[] fields = clz.getDeclaredFields();


        List<Integer> autoSizeCell = IaisCommonUtils.genNewArrayList();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ExcelProperty.class)) {
                ExcelProperty annotation = field.getAnnotation(ExcelProperty.class);
                int rowIndex = annotation.cellIndex();
                String rowName = annotation.cellName();

                Cell cell = sheetRow.createCell(rowIndex);
                cell.setCellStyle(unlockStyle);
                cell.setCellValue(rowName);

                autoSizeCell.add(rowIndex);
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

        File out;
        final String localFileName = fileName;
        Sheet sheet;
        log.info(StringUtil.changeForLog("current workspace " + sheetName));
        log.info("current filename " + localFileName);
        if (newModule){
            out = new File(localFileName);
            try (FileOutputStream fileOutputStream = new FileOutputStream(out)) {
                workbook = XSSFWorkbookFactory.createWorkbook();
                sheet  = workbook.createSheet(sheetName);
                doParse(source, sheet);
                workbook.write(fileOutputStream);

            } catch (Exception e) {
                throw new Exception("has IO error when when export excel");
            }finally {
                workbook.close();
            }
        }else {
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                workbook = XSSFWorkbookFactory.createWorkbook(fileInputStream);
                sheet = workbook.getSheetAt(sheetAt);

                doParse(source, sheet);

                OutputStream outputStream = new FileOutputStream(localFileName);
                workbook.write(outputStream);
                out = new File(localFileName);
                workbook.close();
            } catch (Exception e) {
                throw new Exception("has error when when export excel, may be is resource corrupted");
            }finally {
                workbook.close();
            }
        }

        return out;
    }

    /**
     * For special requirements, the method can be ignored
     * @Author yichen
     * @Date: 11:49 2020/6/15
     **/
    public File writerToExcelByIndex(final File file, int sheetAt, List<String> val, Map<Integer, List<Integer>> excelConfigIndex) throws Exception {
        if (file == null){
            throw new IaisRuntimeException("can not find file when writerToExcelByIndex");
        }

        File out;
        final String localFileName = generationFileName();
        Sheet sheet;
        log.info("current filename " + localFileName);
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            workbook = XSSFWorkbookFactory.createWorkbook(fileInputStream);

            sheet  = workbook.getSheetAt(sheetAt);

            int count = 0;
            for (Map.Entry<Integer, List<Integer>> entry : excelConfigIndex.entrySet()){
                int row = entry.getKey();
                Row sheetRow = sheet.getRow(row);

                if (sheetRow == null){
                    continue;
                }

                List<Integer> cellIndex = entry.getValue();
                for (Integer i : cellIndex){
                    Cell cell = sheetRow.createCell(i);
                    String value = val.get(count);
                    cell.setCellValue(value);
                    count++;
                }
            }

            OutputStream outputStream = new FileOutputStream(localFileName);
            workbook.write(outputStream);
            out = new File(localFileName);
            workbook.close();
        } catch (Exception e) {
            throw new Exception("has error when when export excel, may be is resource corrupted");
        }finally {
            workbook.close();
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
            sheet.protectSheet(zheshigaisidemima);
        }
    }

    private void createCellValue(final Sheet sheet, final List<?> source) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        //sequence number
        int cellIndex = startCellIndex + 1;
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
                    int index = annotation.cellIndex();
                    Cell cell = sheetRow.createCell(index);
                    boolean readOnly = annotation.readOnly();
                    boolean hidden = annotation.hidden();

                    if (readOnly){
                        cell.setCellStyle(lockStyle);
                    }else {
                        cell.setCellStyle(unlockStyle);
                    }

                    if (hidden){
                        sheet.setColumnHidden(index, true);
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
        if (StringUtils.isEmpty(fileName) && clz != null){
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
