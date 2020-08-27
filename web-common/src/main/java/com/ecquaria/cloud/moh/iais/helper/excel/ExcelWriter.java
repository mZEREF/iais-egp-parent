package com.ecquaria.cloud.moh.iais.helper.excel;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.common.annotation.ExcelSheetProperty;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;



/**
 * To use this class, you need to set the initial value, such as the class of the source data, and pass a list set
 * @author yi chen
 * @Date:2020/6/5
 **/
@Slf4j
public final class ExcelWriter {
    /**
     * Cell color, locked status, hidden or not
     */
    private static XSSFCellStyle lockStyle = null;

    /**
     * If without this value, the generated data will be locked
     */
    private static XSSFCellStyle unlockStyle = null;

    private static XSSFCellStyle textStyle = null;

    private static XSSFWorkbook workbook;

    private static String safetyPwd = "password$1";

    private ExcelWriter(){}

    /**
     * default model
     * @Author yichen
     **/
    public static File writerToExcel(final List<?> source, Class<?> sourceClz, String fileName) throws Exception {
        return writerToExcel(source, sourceClz, null, fileName, false, true, null, null);
    }

    /**
     * @param{file} This parameter represents an existing file
     * @Author yichen
     **/
    public static File writerToExcel(final List<?> source, Class<?> sourceClz, final File file, String fileName, boolean block, boolean headName) throws Exception {
        return writerToExcel(source, sourceClz, file, fileName, block, headName, null, null);
    }

    /**
     * @param{unlockCellMap} This parameter represents an columns that do not need to be locked
     * @Author yichen
     **/
    public static File writerToExcel(final List<?> source, Class<?> sourceClz, final File file, String fileName, boolean block, boolean headName, Map<Integer, List<Integer>> unlockCellMap) throws Exception {
        return writerToExcel(source, sourceClz, file, fileName, block, headName, unlockCellMap, null);
    }

    private static boolean isNew(File file){
        return file == null ? true : false;
    }


    private static File createNewExcel(String fileName, String sheetName, List<?> source, Class<?> sourceClz, boolean block, boolean headName, Map<Integer, List<Integer>> unlockCellMap, String pwd, int startCellIndex) throws Exception {
        File out = new File(fileName);
        try (OutputStream fileOutputStream = Files.newOutputStream(Paths.get(out.getPath()))) {
            workbook = XSSFWorkbookFactory.createWorkbook();

            startInternal();

            Sheet sheet = workbook.createSheet(sheetName);

            parseSheet(source, sourceClz, block, headName, unlockCellMap, startCellIndex, sheet, pwd);

            workbook.write(fileOutputStream);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw e;
        }finally {
            if (workbook != null){
                workbook.close();
            }
        }

        return out;
    }

    private static File appendToExcel(final File file, final String fileName, final Integer sheetAt, final List<?> source, Class<?> sourceClz,
                                      final boolean block, final boolean headName, final Map<Integer, List<Integer>> unlockCellMap, final String pwd, final int startCellIndex) throws Exception {
        File out = new File(fileName);

        try (InputStream fileInputStream = Files.newInputStream(Paths.get(file.getPath())); OutputStream outputStream =  Files.newOutputStream(Paths.get(out.getPath()))) {
            workbook = XSSFWorkbookFactory.createWorkbook(fileInputStream);

            startInternal();

            Sheet sheet = workbook.getSheetAt(sheetAt);

            parseSheet(source, sourceClz, block, headName, unlockCellMap, startCellIndex, sheet, pwd);

            workbook.write(outputStream);

        } catch (Exception e) {
            throw e;
        }finally {
            if (workbook != null){
                workbook.close();
            }
        }

        return out;
    }

    public static File writerToExcel(final List<?> source, Class<?> sourceClz, final File file, String fileName, boolean block, boolean headName,
                                     final Map<Integer, List<Integer>> unlockCellMap, final String pwd) throws Exception {
        if (source == null || sourceClz == null) {
            log.info("don't have source when writer to excel!!!!");
            throw new IaisRuntimeException("Please check the export excel parameters.");
        }

        ExcelSheetProperty property = getSheetPropertyByClz(sourceClz);
        int startCellIndex = property.startRowIndex();
        int sheetAt = property.sheetAt();
        String sheetName = property.sheetName();
        final String postFileName = FileUtils.generationFileName(fileName, FileUtils.EXCEL_TYPE_XSSF);
        boolean isNew = isNew(file);

        if (isNew){
            return createNewExcel(postFileName, sheetName, source, sourceClz, block, headName, unlockCellMap, pwd, startCellIndex);
        }else {
            return appendToExcel(file, postFileName, sheetAt, source, sourceClz, block, headName, unlockCellMap, pwd, startCellIndex);
        }
    }

    private static void startInternal() {
        initLockStyle();

        initUnlockStyle();

        initTextStyle();
    }

    private static ExcelSheetProperty getSheetPropertyByClz(Class<?> sourceClz){
        ExcelSheetProperty property = sourceClz.getAnnotation(ExcelSheetProperty.class);
        if (property == null){
            throw new IaisRuntimeException("can not find sheet property!");
        }
        return property;
    }

    private static void parseSheet(List<?> source, Class<?> sourceClz, boolean block, boolean headName, Map<Integer, List<Integer>> unlockCellMap, int startCellIndex, Sheet sheet, String password) {
        if (unlockCellMap != null && !unlockCellMap.isEmpty()){
            unlockCellByMap(sheet, unlockCellMap);
        }

        if (headName){
            setFieldName(sourceClz, sheet, startCellIndex);
        }

        parseCell(source, sourceClz, sheet, startCellIndex);

        if (block){
            lockSheetWorkspace(sheet, password);
        }
    }

    private static void parseCell(List<?> source, Class<?> sourceClz, Sheet sheet, int startCellIndex){
        try {
            createCellValue(sheet, source, sourceClz, startCellIndex);
        } catch (NoSuchMethodException e) {
            log.error("========NoSuchMethodException=>>>>>>>>>>>>>>", e);
        } catch (IllegalAccessException e) {
            log.error("========IllegalAccessException=>>>>>>>>>>>>>>", e);
        } catch (InvocationTargetException e) {
            log.error("========InvocationTargetException=>>>>>>>>>>>>>>", e);
        }


    }

    private static void lockSheetWorkspace(Sheet sheet, String pwd){
        if (StringUtils.isEmpty(pwd)){
            sheet.protectSheet(safetyPwd);
        }else {
            sheet.protectSheet(pwd);
        }
    }

    private static void createCellValue(final Sheet sheet, final List<?> source, final Class<?> sourceClz, int startCellIndex) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
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

            Field[] fields = sourceClz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(ExcelProperty.class)) {
                    ExcelProperty annotation = field.getAnnotation(ExcelProperty.class);
                    int index = annotation.cellIndex();
                    Cell cell = sheetRow.createCell(index);
                    Class objectType = annotation.objectType();
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

                    if (objectType == Date.class){
                        cell.setCellStyle(textStyle);
                    }

                    Object val = sourceClz.getDeclaredMethod("get" +
                            StringUtils.capitalize(field.getName())).invoke(t);

                    cell.setCellValue(setValue(val));
                }
            }

        }

    }

    private static String setValue(final Object obj) {
        return obj == null ? "" : obj.toString();
    }



    private static void unlockCellByMap(Sheet sheet, Map<Integer, List<Integer>> unlockCellMap){
        for (Map.Entry<Integer, List<Integer>> ent : unlockCellMap.entrySet()){
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

    private static void initUnlockStyle(){
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

    private static void initTextStyle(){
        if (workbook != null) {
            XSSFCellStyle xssfCellStyle = workbook.createCellStyle();
            XSSFDataFormat format = workbook.createDataFormat();
            xssfCellStyle.setDataFormat(format.getFormat("@"));
            textStyle = xssfCellStyle;
        }
    }

    private static void initLockStyle() {
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

    private static void setFieldName(final Class<?> clz, final Sheet sheet, int startCellIndex) {
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

    private static void autoSizeCell(Sheet sheet, List<Integer> cell){
        if (sheet != null && !IaisCommonUtils.isEmpty(cell)){
            for (Integer i : cell){
                int columnWidth = sheet.getColumnWidth(i) / 256;
                sheet.setColumnWidth(i, columnWidth * 512);
                sheet.autoSizeColumn(i);
            }
        }
    }
}
