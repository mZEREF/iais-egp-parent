package com.ecquaria.cloud.moh.iais.helper.excel;

/*
 *author: huachong
 *date time:9/18/2019 1:05 PM
 *description:
 */

import com.ecquaria.cloud.moh.iais.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.poi.ss.usermodel.CellType.STRING;

@Slf4j
public final class ExcelReader {
    private static String pattern = "yyyy-MM-dd HH:mm:ss";
    public static String DATE_TYPE_NAME             = "java.util.Date";

    public static String EXCEL_TYPE_HSSF			= "xls";
    public static String EXCEL_TYPE_XSSF			= "xlsx";

    public static <T> List<T> excelReader(File file, Class<T> clazz) {
        List<List<String>> result = parse(file);
        return result.stream().map(x -> setField(clazz, x)).collect(Collectors.toList());
    }

    public static List<List<String>> parse(File file) {
        Sheet sheet = parseFile(file);
        int rowCount = sheet.getPhysicalNumberOfRows();
        int cellCount = sheet.getRow(0).getPhysicalNumberOfCells();
        List<List<String>> result = new ArrayList<>();
        for (int i = 1; i < rowCount; i++) {
            List<String> cellResult = new ArrayList<>();
            for (int j = 0; j < cellCount; j++) {
                cellResult.add(getCellValue(sheet, i, j));
            }
            result.add(cellResult);
        }
        return result;
    }

    /**
     *
     * @param file
     * @return
     */
    @SuppressWarnings("resource")
    public static Sheet parseFile(File file) {
        FileInputStream in = null;
        Workbook workBook = null;
        try {
            String suffix = file.getName().substring(file.getName().indexOf(".") + 1);
            in = new FileInputStream(file);
            workBook = suffix.equals(EXCEL_TYPE_XSSF) ? new XSSFWorkbook(in) : new HSSFWorkbook(in);
            return workBook.getSheetAt(0);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.info(e.getMessage());
                }
            }
        }
    }

    public static <T> T setField(Class<T> clazz, List<String> rowDatas) {
        try {
            T obj = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(ExcelProperty.class)){
                    ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
                    int index = excelProperty.index();
                    String format = excelProperty.format();
                    Object value = getFieldValue(field, rowDatas.get(index), format);
                    field.setAccessible(true);
                    field.set(obj, value);
                }
            }
            return obj;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

   public static String getCellValue(Sheet sheet, int rowIndex, int cellIndex) {
        return getCellValue(sheet.getRow(rowIndex).getCell(cellIndex));
    }

    @SuppressWarnings("deprecation")
    private static String getCellValue(Cell cell) {
        String cellValue = "";
        if (cell != null) {
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
                case BLANK:
                    cellValue = "";
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

    private static Object getFieldValue(Field field, String value, String pattern) {
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
        } else if (typeClass == Boolean.class) {
            val = Boolean.valueOf(value);
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
