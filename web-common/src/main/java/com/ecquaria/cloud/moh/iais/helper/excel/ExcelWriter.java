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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;



/**
 * To use this class, you need to set the initial value, such as the class of the source data, and pass a list set
 * @author yi chen
 * @Date:2020/6/5
 **/
@Slf4j
public final class ExcelWriter {
    private static String EXCEL_TYPE_XSSF = "xlsx";

    /**
     * Cell color, locked status, hidden or not
     */
    private static XSSFCellStyle lockStyle = null;

    /**
     * If without this value, the generated data will be locked
     */
    private static XSSFCellStyle unlockStyle = null;

    private static XSSFWorkbook workbook;

    private static String zheshigaisidemima = "password$1";

    private ExcelWriter(){}

    /**
     * default model
     * @Author yichen
     **/
    public static File writerToExcel(final List<?> source, Class<?> sourceClz, String fileName) throws Exception {
        return writerToExcel(source, sourceClz, null, fileName, false, true, null, null);
    }
    public static File writerToExcel(final List<?> source, Class<?> sourceClz, final File file, String fileName, boolean block, boolean headName) throws Exception {
        return writerToExcel(source, sourceClz, file, fileName, block, headName, null, null);
    }

    public static File writerToExcel(final List<?> source, Class<?> sourceClz, final File file, String fileName, boolean block, boolean headName, Map<Integer, List<Integer>> unlockCellMap) throws Exception {
        return writerToExcel(source, sourceClz, file, fileName, block, headName, unlockCellMap, null);
    }

    public static File writerToExcel(final List<?> source, Class<?> sourceClz, final File file, String fileName, boolean block, boolean headName, Map<Integer, List<Integer>> unlockCellMap, String pwd) throws Exception {
        if (source == null || sourceClz == null) {
            log.info("don't have source when writer to excel!!!!");
            throw new IaisRuntimeException("Please check the export excel parameters.");
        }

        ExcelSheetProperty property = getSheetPropertyByClz(sourceClz);
        int startCellIndex = property.startRowIndex();
        int sheetAt = property.sheetAt();
        String sheetName = property.sheetName();

        File out;
        final String localFileName = FileUtils.generationFileName(fileName, EXCEL_TYPE_XSSF);
        if (file == null){
            out = new File(localFileName);
            try (FileOutputStream fileOutputStream = new FileOutputStream(out)) {
                workbook = XSSFWorkbookFactory.createWorkbook();

                startInternal();

                Sheet sheet = workbook.createSheet(sheetName);

                parseSheet(source, sourceClz, block, headName, unlockCellMap, startCellIndex, sheet, pwd);

                workbook.write(fileOutputStream);
            } catch (Exception e) {
                throw new Exception("has IO error when when export excel");
            }finally {
                workbook.close();
            }
        }else {
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                workbook = XSSFWorkbookFactory.createWorkbook(fileInputStream);

                startInternal();

                Sheet sheet = workbook.getSheetAt(sheetAt);
                parseSheet(source, sourceClz, block, headName, unlockCellMap, startCellIndex, sheet, pwd);
                OutputStream outputStream = new FileOutputStream(localFileName);
                workbook.write(outputStream);
                out = new File(localFileName);

                return out;
            } catch (Exception e) {
                throw new Exception("has error when when export excel, may be is resource corrupted");
            }finally {
                workbook.close();
            }
        }

        return out;
    }

    private static void startInternal() {
        initLockStyle();

        initUnlockStyle();
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
            sheet.protectSheet(zheshigaisidemima);
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

                    cell.setCellValue(setValue(sourceClz.getDeclaredMethod("get" +
                            StringUtils.capitalize(field.getName())).invoke(t)));
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
