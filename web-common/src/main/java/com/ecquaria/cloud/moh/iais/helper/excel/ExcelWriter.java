package com.ecquaria.cloud.moh.iais.helper.excel;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.common.annotation.ExcelSheetProperty;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.dto.ExcelSheetDto;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.sz.commons.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.nio.file.Files.newInputStream;
import static java.nio.file.Files.newOutputStream;


/**
 * To use this class, you need to set the initial value, such as the class of the source data, and pass a list set
 * @author yi chen
 * @Date:2020/6/5
 **/
@Slf4j
public final class ExcelWriter {
    private static XSSFWorkbook workbook;

    private static String mima = "password$1";

    private ExcelWriter(){}

    /**
     * default model
     * @Author yichen
     **/
    public static File writerToExcel(final List<?> source, Class<?> sourceClz, String fileName) throws Exception {
        return writerToExcel(source, sourceClz, null, fileName, false, true, null, null);
    }

    /**
     * default model
     * @Author yichen
     **/
    public static File writerToExcelSubHead(final List<?> source, Class<?> sourceClz,Class<?> subSourceClz, String fileName) throws Exception {
        return writerToExcelSubHead(source, sourceClz, subSourceClz,null, fileName, false, true, null, null);
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
        File out = MiscUtil.generateFile(fileName);
        try (OutputStream fileOutputStream = newOutputStream(out.toPath())) {
            workbook = XSSFWorkbookFactory.createWorkbook();

            startInternal(workbook);

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

    private static File createNewExcelSubHead(String fileName, String sheetName, List<?> source, Class<?> sourceClz,Class<?> subSourceClz, boolean block, boolean headName, Map<Integer, List<Integer>> unlockCellMap, String pwd, int startCellIndex) throws Exception {
        File out = MiscUtil.generateFile(fileName);
        try (OutputStream fileOutputStream = newOutputStream(out.toPath())) {
            workbook = XSSFWorkbookFactory.createWorkbook();

            startInternal(workbook);

            Sheet sheet = workbook.createSheet(sheetName);

            parseSheetSubHead(source, sourceClz, subSourceClz,block, headName, unlockCellMap, startCellIndex, sheet, pwd);

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
        String path = fileName;
        File out = MiscUtil.generateFile(path);

        try (InputStream fileInputStream = newInputStream(file.toPath()); OutputStream outputStream = newOutputStream(out.toPath())) {
            workbook = XSSFWorkbookFactory.createWorkbook(fileInputStream);

            startInternal(workbook);

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

    public static File writerToExcelSubHead(final List<?> source, Class<?> sourceClz,Class<?> subSourceClz, final File file, String fileName, boolean block, boolean headName,
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
            return createNewExcelSubHead(postFileName, sheetName, source, sourceClz,subSourceClz, block, headName, unlockCellMap, pwd, startCellIndex);
        }else {
            return appendToExcel(file, postFileName, sheetAt, source, sourceClz, block, headName, unlockCellMap, pwd, startCellIndex);
        }
    }

    private static void startInternal(XSSFWorkbook workbook) {
        CellStyleHelper.initLockStyle(workbook);
        CellStyleHelper.initTextStyle(workbook);
        CellStyleHelper.initUnlockStyle(workbook);
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
            setFieldName(sourceClz, sheet, startCellIndex, true);
        }

        parseCell(source, sourceClz, sheet, startCellIndex);

        if (block){
            lockSheetWorkspace(sheet, password);
        }
    }

    private static void parseSheetSubHead(List<?> source, Class<?> sourceClz, Class<?> subSourceClz, boolean block, boolean headName, Map<Integer, List<Integer>> unlockCellMap, int startCellIndex, Sheet sheet, String password) {
        if (unlockCellMap != null && !unlockCellMap.isEmpty()){
            unlockCellByMap(sheet, unlockCellMap);
        }


        try {
            createCellValueSub(sheet, source, sourceClz,subSourceClz, startCellIndex);
        } catch (NoSuchMethodException e) {
            log.error("========NoSuchMethodException=>>>>>>>>>>>>>>", e);
        } catch (IllegalAccessException e) {
            log.error("========IllegalAccessException=>>>>>>>>>>>>>>", e);
        } catch (InvocationTargetException e) {
            log.error("========InvocationTargetException=>>>>>>>>>>>>>>", e);
        }

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
            sheet.protectSheet(mima);
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
            firstCell.setCellStyle(CellStyleHelper.getLockStyle());
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
                        cell.setCellStyle(CellStyleHelper.getLockStyle());
                    }else {
                        cell.setCellStyle(CellStyleHelper.getUnlockStyle());
                    }

                    if (hidden){
                        sheet.setColumnHidden(index, true);
                    }

                    Object val = sourceClz.getDeclaredMethod("get" +
                            StringUtils.capitalize(field.getName())).invoke(t);

                    String str;
                    if (objectType == Date.class) {
                        //Set to text format to avoid errors caused by date modification in different systems
                        String format = annotation.format();
                        if (Date.class.isAssignableFrom(field.getType())) {
                            str = DateUtil.formatDateTime((Date) val, format);
                        } else {
                            str = getValue(val);
                        }
                        cell.setCellStyle(CellStyleHelper.getTextStyle());
                    } else {
                        str = getValue(val);
                    }
                    cell.setCellValue(str);
                }
            }

        }

    }

    private static void createCellValueSub(final Sheet sheet, final List<?> source, final Class<?> sourceClz,final Class<?> subSourceClz, int startCellIndex) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        int cellIndex = startCellIndex ;

        for (Object t : source) {
            setFieldName(sourceClz, sheet, cellIndex++, false);
            Row sheetRow = sheet.createRow(cellIndex);

            cellIndex++;

            Field[] fields = sourceClz.getDeclaredFields();
            Field fieldList=null;
            for (Field field : fields) {
                if (field.isAnnotationPresent(ExcelProperty.class)) {
                    ExcelProperty annotation = field.getAnnotation(ExcelProperty.class);
                    if(field.getType().equals(List.class)){
                        fieldList=field;
                        continue;
                    }
                    int index = annotation.cellIndex();
                    Cell cell = sheetRow.createCell(index);
                    Class objectType = annotation.objectType();
                    boolean readOnly = annotation.readOnly();
                    boolean hidden = annotation.hidden();

                    if (readOnly){
                        cell.setCellStyle(CellStyleHelper.getLockStyle());
                    }else {
                        cell.setCellStyle(CellStyleHelper.getUnlockStyle());
                    }

                    if (hidden){
                        sheet.setColumnHidden(index, true);
                    }

                    Object val = sourceClz.getDeclaredMethod("get" +
                            StringUtils.capitalize(field.getName())).invoke(t);

                    String str;
                    if (objectType == Date.class) {
                        //Set to text format to avoid errors caused by date modification in different systems
                        String format = annotation.format();
                        if (Date.class.isAssignableFrom(field.getType())) {
                            str = DateUtil.formatDateTime((Date) val, format);
                        } else {
                            str = getValue(val);
                        }
                        cell.setCellStyle(CellStyleHelper.getTextStyle());
                    } else {
                        str = getValue(val);
                    }
                    cell.setCellValue(str);
                }
            }

            if(fieldList!=null){

                List list= (List) sourceClz.getDeclaredMethod("get" +
                        StringUtils.capitalize(fieldList.getName())).invoke(t);
                if(IaisCommonUtils.isNotEmpty(list)){
                    setFieldName(subSourceClz, sheet, cellIndex++, false);
                    for (Object ts : list) {

                        Row sheetRowSub = sheet.createRow(cellIndex);

                        cellIndex++;

                        Field[] fieldSubs = subSourceClz.getDeclaredFields();
                        for (Field field : fieldSubs) {
                            if (field.isAnnotationPresent(ExcelProperty.class)) {
                                ExcelProperty annotation = field.getAnnotation(ExcelProperty.class);
                                int index = annotation.cellIndex();
                                Cell cell = sheetRowSub.createCell(index);
                                Class objectType = annotation.objectType();
                                boolean readOnly = annotation.readOnly();
                                boolean hidden = annotation.hidden();

                                if (readOnly){
                                    cell.setCellStyle(CellStyleHelper.getLockStyle());
                                }else {
                                    cell.setCellStyle(CellStyleHelper.getUnlockStyle());
                                }

                                if (hidden){
                                    sheet.setColumnHidden(index, true);
                                }

                                Object val = subSourceClz.getDeclaredMethod("get" +
                                        StringUtils.capitalize(field.getName())).invoke(ts);

                                String str;
                                if (objectType == Date.class) {
                                    //Set to text format to avoid errors caused by date modification in different systems
                                    String format = annotation.format();
                                    if (Date.class.isAssignableFrom(field.getType())) {
                                        str = DateUtil.formatDateTime((Date) val, format);
                                    } else {
                                        str = getValue(val);
                                    }
                                    cell.setCellStyle(CellStyleHelper.getTextStyle());
                                } else {
                                    str = getValue(val);
                                }
                                cell.setCellValue(str);
                            }
                        }
                    }
                }

            }


        }

    }



    private static String getValue(final Object obj) {
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
                        cell.setCellStyle(CellStyleHelper.getUnlockStyle());
                        XSSFDataFormat format = workbook.createDataFormat();
                        cell.getCellStyle().setDataFormat(format.getFormat("@"));
                    }
                }
            }
        }
    }



    private static void setFieldName(final Class<?> clz, final Sheet sheet, int startCellIndex, boolean needSn) {
        Row sheetRow = sheet.createRow(startCellIndex);
        if (needSn) {
            Cell firstCell = sheetRow.createCell(0);
            firstCell.setCellValue("SN");
            firstCell.setCellStyle(CellStyleHelper.getLockStyle());
        }
        Field[] fields = clz.getDeclaredFields();
        List<Integer> autoSizeCell = IaisCommonUtils.genNewArrayList();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ExcelProperty.class)) {
                if(field.getType().equals(List.class)){
                    continue;
                }
                ExcelProperty annotation = field.getAnnotation(ExcelProperty.class);
                int rowIndex = annotation.cellIndex();
                String rowName = annotation.cellName();

                Cell cell = sheetRow.createCell(rowIndex);
                cell.setCellStyle(CellStyleHelper.getUnlockStyle());
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

    public static File writerToExcel(ExcelSheetDto excelSheetDto, final File file, String fileName) throws IOException {
        if (excelSheetDto == null) {
            log.info("don't have source when writer to excel!!!!");
            throw new IaisRuntimeException("Please check the export excel parameters.");
        }
        return writerToExcel(Collections.singletonList(excelSheetDto), file, fileName);
    }

    public static File writerToExcel(List<ExcelSheetDto> excelSheetDtos, final File file, String fileName) throws IOException {
        if (excelSheetDtos == null || excelSheetDtos.isEmpty()) {
            log.info("don't have source when writer to excel!!!!");
            throw new IaisRuntimeException("Please check the export excel parameters.");
        }
        boolean isNew = isNew(file);
        final String postFileName = FileUtils.generationFileName(fileName, FileUtils.EXCEL_TYPE_XSSF);
        String path = postFileName;
        File out = MiscUtil.generateFile(path);
        try (InputStream fileInputStream = newInputStream(file.toPath()); OutputStream outputStream = newOutputStream(out.toPath())) {
            if (isNew) {
                workbook = XSSFWorkbookFactory.createWorkbook();
            } else {
                workbook = XSSFWorkbookFactory.createWorkbook(fileInputStream);
            }
            startInternal(workbook);
            for (ExcelSheetDto excelSheetDto : excelSheetDtos) {
                XSSFSheet sheet;
                if (isNew) {
                    sheet = workbook.getSheet(excelSheetDto.getSheetName());
                } else {
                    sheet = workbook.getSheetAt(excelSheetDto.getSheetAt());
                    if (!StringUtils.isEmpty(excelSheetDto.getSheetName())) {
                        workbook.setSheetName(excelSheetDto.getSheetAt(), excelSheetDto.getSheetName());
                    }
                }
                writeSheet(excelSheetDto, sheet);
            }
            workbook.write(outputStream);
        } catch (Exception e) {
            throw e;
        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }
        return out;
    }

    private static void writeSheet(ExcelSheetDto excelSheetDto, XSSFSheet sheet) {
        Map<Integer, List<Integer>> unlockCellMap = excelSheetDto.getUnlockCellMap();
        if (unlockCellMap != null && !unlockCellMap.isEmpty()) {
            unlockCellByMap(sheet, unlockCellMap);
        }
        if (excelSheetDto.getDefaultRowHeight() != null) {
            sheet.setDefaultRowHeight(excelSheetDto.getDefaultRowHeight());
        }
        if (excelSheetDto.getWidthMap() != null) {
            for (Map.Entry<Integer, Integer> entry : excelSheetDto.getWidthMap().entrySet()) {
                sheet.setColumnWidth(entry.getKey(), entry.getValue() * 256);
            }
        }
        List<?> source = excelSheetDto.getSource();
        Class<?> sourceClass = excelSheetDto.getSourceClass();
        if (excelSheetDto.isNeedFiled() && excelSheetDto.getFiledRowIndexes() != null) {
            for (int row : excelSheetDto.getFiledRowIndexes()) {
                setFieldName(sourceClass, sheet, row, false);
            }
        }
        createCell(source, sourceClass, sheet, excelSheetDto);

        if (excelSheetDto.isBlock()) {
            lockSheetWorkspace(sheet, excelSheetDto.getPwd());
        }
    }

    private static void createCell(List<?> source, Class<?> sourceClass, XSSFSheet sheet, ExcelSheetDto excelSheetDto) {
        try {
            //sequence number
            int cellIndex = excelSheetDto.getStartRowIndex();
            short fontHeight = -1;
            for (Object t : source) {
                XSSFRow sheetRow = sheet.createRow(cellIndex);

                cellIndex++;

                int maxHeight = -1;
                Field[] fields = sourceClass.getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(ExcelProperty.class)) {
                        ExcelProperty annotation = field.getAnnotation(ExcelProperty.class);
                        int index = annotation.cellIndex();
                        XSSFCell cell = sheetRow.createCell(index);
                        Class objectType = annotation.objectType();
                        boolean readOnly = annotation.readOnly();
                        boolean hidden = annotation.hidden();
                        if (fontHeight == -1) {
                            fontHeight = cell.getCellStyle().getFont().getFontHeight();
                        }

                        if (readOnly) {
                            cell.setCellStyle(CellStyleHelper.getLockStyle());
                        } else {
                            cell.setCellStyle(CellStyleHelper.getUnlockStyle());
                        }

                        if (hidden) {
                            sheet.setColumnHidden(index, true);
                        }

                        Object val = sourceClass.getDeclaredMethod("get" +
                                StringUtils.capitalize(field.getName())).invoke(t);

                        String str;
                        if (objectType == Date.class) {
                            //Set to text format to avoid errors caused by date modification in different systems
                            String format = annotation.format();
                            if (Date.class.isAssignableFrom(field.getType())) {
                                str = DateUtil.formatDateTime((Date) val, format);
                            } else {
                                str = getValue(val);
                            }
                            cell.setCellStyle(CellStyleHelper.getXSSFCellStyle(sheetRow, readOnly, hidden));
                        } else {
                            str = getValue(val);
                        }
                        cell.setCellValue(str);
                        if (!hidden && excelSheetDto.isChangeHeight()) {
                            maxHeight = Math.max(maxHeight, getRowHeigt(fontHeight, sheet.getColumnWidth(index), str));
                        }
                    }
                }
                if (excelSheetDto.isChangeHeight()) {
                    sheetRow.setHeight((short) maxHeight);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int getRowHeigt(short fontHeight, int cellWidth, String cellContent) {
        if (null == cellContent || "".equals(cellContent)) {
            return 0;
        }
        int cellContentWidth = cellContent.getBytes().length * 2 * 256;
        int stringNeedsRows = cellContentWidth / cellWidth;
        if (stringNeedsRows < 1) {
            stringNeedsRows = 1;
        }
        return (fontHeight + 50) * (stringNeedsRows + 1);
    }
}
