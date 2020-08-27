package com.ecquaria.cloud.moh.iais.helper.excel;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.common.annotation.ExcelSheetProperty;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.poi.ss.usermodel.CellType.STRING;

/**
 * @author yi chen
 * @Date:2020/6/8
 **/

@Slf4j
public final class ExcelReader {
    private static final String pattern = "yyyy-MM-dd HH:mm:ss";

    private ExcelReader(){}

    public static <T> List<T> readerToBean(final File file, final Class<?> clz) throws Exception {
        if (file == null || !file.exists()){
            throw new IaisRuntimeException("Please check excel source is exists");
        }

        if (clz == null){
            throw new IaisRuntimeException("excel bean class error");
        }

        ExcelSheetProperty property = clz.getAnnotation(ExcelSheetProperty.class);
        if (property == null){
            throw new IaisRuntimeException("excel bean class error");
        }

        int startCellIndex = property.startRowIndex();
        int sheetAt = property.sheetAt();
        Sheet sheet = parseFile(file, sheetAt);

        if (sheet == null){
            throw new IaisRuntimeException("excel sheet name error");
        }

        String sheetName = sheet.getSheetName();
        if (!property.sheetName().equals(sheetName)){
            throw new IaisRuntimeException("excel sheet name error");
        }


        List<List<String>> result = sequentialParse(sheet, startCellIndex);

        return (List<T>) result.stream().map(x -> setField(clz, x)).collect(Collectors.toList());
    }

    public static List<String> readerToList(final File file, int sheetAt, Map<Integer, List<Integer>> matrix) throws Exception {
        if (file == null || !file.exists()){
            throw new IaisRuntimeException("Please check excel source is exists");
        }

        Sheet sheet = parseFile(file, sheetAt);

        return parseByMapValue(sheet, matrix);
    }

    private static List<String> parseByMapValue(Sheet sheet, Map<Integer, List<Integer>> matrix){
        List<String> values = new ArrayList<>();
        for (Map.Entry<Integer, List<Integer>> entry : matrix.entrySet()){
            Integer rowIndex = entry.getKey();
            List<Integer> cellList = entry.getValue();
            for (Integer cellIndex : cellList){
                String val = getCellValue(sheet, rowIndex, cellIndex);
                values.add(val);
            }
        }
        return values;
    }

    private static List<List<String>> sequentialParse(final Sheet sheet, final int startCellIndex) {
        int rowCount = sheet.getLastRowNum();
        //int realRowCount = sheet.getPhysicalNumberOfRows();
        int realCellCount = sheet.getRow(startCellIndex).getLastCellNum();

        List<List<String>> result = IaisCommonUtils.genNewArrayList();
        for (int i =  startCellIndex + 1; i <= rowCount; i++) {
            Row row = sheet.getRow(i);
            if (row == null || row.getCell(0) == null ){
                continue;
            }

            List<String> cellResult = IaisCommonUtils.genNewArrayList();
            for (int j = 0; j < realCellCount; j++) {
                cellResult.add(getCellValue(sheet, i, j));
            }
            result.add(cellResult);
        }
        return result;
    }

    @SuppressWarnings("resource")
    private static Sheet parseFile(final File file, int sheetAt) throws Exception {
        Workbook workBook = null;
        try (InputStream in = Files.newInputStream(file.toPath())){
            char indexChar = ".".charAt(0);
            String suffix = file.getName().substring(file.getName().indexOf(indexChar) + 1);
            workBook = suffix.equals(FileUtils.EXCEL_TYPE_XSSF) ? new XSSFWorkbook(in) : new HSSFWorkbook(in);
            return workBook.getSheetAt(sheetAt);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw e;
        }finally {
            try {
                if (workBook != null){
                    workBook.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private static Object setField(final Class<?> clazz, final List<String> rowData) {
        try {
            Object obj = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                // Use iais project folder ExcelProperty Annotation Class
                if (field.isAnnotationPresent(ExcelProperty.class)){
                    ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
                    int index = excelProperty.cellIndex();
                    String format = excelProperty.format();
                    Object value = getFieldValue(field, rowData.get(index), format);
                    field.setAccessible(true);
                    field.set(obj, value);
                }
            }
            return obj;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static String getCellValue(final Sheet sheet, final int rowIndex, final int cellIndex) {
        return getCellValue(sheet.getRow(rowIndex).getCell(cellIndex));
    }

    @SuppressWarnings("deprecation")
    private static String getCellValue(final Cell cell) {
        String cellValue = "";
        if (cell != null && cell.getCellType() != CellType.BLANK) {
            switch (cell.getCellType()) {
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        cellValue = IaisEGPHelper.parseToString(cell.getDateCellValue(), pattern);
                    } else {
                        cell.setCellType(STRING);
                        cellValue = cell.getStringCellValue();
                    }
                    break;
                case STRING:
                    cellValue = cell.getStringCellValue();
                    break;
                case BOOLEAN:
                    cellValue = String.valueOf(cell.getBooleanCellValue());
                    break;
                case FORMULA:
                    cellValue = String.valueOf(cell.getCellFormula());
                    break;
                case ERROR:
                    cellValue = "";
                    break;
                default:
                    cellValue = cell.toString().trim();
                    break;
            }
        }
        return cellValue.trim();
    }

    private static Object getFieldValue(final Field field, final String value, final String pattern) {
        Class<?> typeClass = field.getType();
        Object val;
        if (typeClass == Integer.class || typeClass == int.class) {
            val = Integer.valueOf(value);
        } else if (typeClass == Long.class || typeClass == long.class) {
            val = Long.valueOf(value);
        } else if (typeClass == Float.class || typeClass == float.class) {
            val = Float.valueOf(value);
        } else if (typeClass == Double.class || typeClass == double.class) {
            val = Double.valueOf(value);
        } else if (typeClass == Date.class) {
            val = IaisEGPHelper.parseToDate(value, pattern);
        } else if (typeClass == Short.class || typeClass == short.class) {
            val = Short.valueOf(value);
        } else if (typeClass == Character.class || typeClass == char.class) {
            val = Character.valueOf(value.charAt(0));
        }else if(typeClass == Boolean.class) {
            val = Arrays.asList(BooleanEnum.values()).stream()
                    .filter(x -> x.getName().equals(value.toUpperCase()))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new)
                    .getValue();
        } else {
            val = value;
        }
        return val;
    }



}
