package com.ecquaria.cloud.moh.iais.helper.excel;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.common.annotation.ExcelSheetProperty;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ReflectionUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.ExcelSheetDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author yi chen
 * @Date:2020/6/8
 **/

@Slf4j
public final class ExcelReader {
    private static final String pattern = "yyyy-MM-dd HH:mm:ss";

    private ExcelReader(){}

    public static <T> List<T> readerToBean(final File file, final Class<?> clz) throws Exception {
        return readerToBean(file, clz, false);
    }

    public static <T> List<T> readerToBean(final File file, final Class<?> clz, boolean defaultValueNull) throws Exception {
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

        return (List<T>) result.stream().map(x -> setField(clz, x, defaultValueNull)).collect(Collectors.toList());
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
        int realCellCount = sheet.getRow(startCellIndex + 1).getLastCellNum();
        if (startCellIndex >= 0) {
            realCellCount = Math.max(realCellCount, sheet.getRow(startCellIndex).getLastCellNum());
        }

        List<List<String>> result = IaisCommonUtils.genNewArrayList();
        for (int i =  startCellIndex + 1; i <= rowCount; i++) {
            Row row = sheet.getRow(i);
            if (isEmpty(row, realCellCount)){
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

    private static boolean isEmpty(Row row, int realCellCount) {
        if (row == null || row.toString().isEmpty()) {
            return true;
        }
        return IntStream.range(0, realCellCount).parallel().noneMatch(i -> StringUtil.isNotEmpty(getCellValue(row.getCell(i))));
    }

    @SuppressWarnings("resource")
    private static Sheet parseFile(final File file, int sheetAt) throws Exception {
        Workbook workBook = null;
        try (InputStream in = Files.newInputStream(file.toPath())){
            char indexChar = ".".charAt(0);
            workBook = new XSSFWorkbook(in);
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

    private static <T> T setField(final Class<T> clazz, final List<String> rowData, boolean defaultValueNull) {
        try {
            T obj = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                // Use iais project folder ExcelProperty Annotation Class
                if (field.isAnnotationPresent(ExcelProperty.class)){
                    ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
                    int index = excelProperty.cellIndex();
                    String format = excelProperty.format();
                    Object value = getFieldValue(field, rowData.get(index), format, defaultValueNull);
                    try {
                        Method setMed = clazz.getMethod("set" + StringUtil.capitalize(field.getName()), field.getType());
                        setMed.invoke(obj, value);
                    } catch (NoSuchMethodException | InvocationTargetException e) {
                        throw new IaisRuntimeException(e);
                    }
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

    private static String getCellValue(final Cell cell) {
        String cellValue = "";
        if (cell != null && cell.getCellType() != CellType.BLANK) {
            switch (cell.getCellType()) {
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        cellValue = Formatter.formatDateTime(cell.getDateCellValue());
                    } else {
                        //cell.setCellType(STRING);
                        cellValue = String.valueOf(cell.getNumericCellValue());
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

    private static Object getFieldValue(final Field field, final String value, final String pattern, boolean defaultValueNull) {
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
            val = defaultValueNull ? StringUtil.getStringEmptyToNull(value) : value;
        }
        return val;
    }

    public static <T> Map<String, List<T>> readerToBeans(final File file, final List<ExcelSheetDto> excelSheetDtos) throws Exception {
        Map<String, List<?>> map = readerToDiffBeans(file, excelSheetDtos);
        Map<String, List<T>> data = IaisCommonUtils.genNewHashMap();
        for (Map.Entry<String, List<?>> entry : map.entrySet()) {
            data.put(entry.getKey(), entry.getValue().stream().map(obj -> (T)obj).collect(Collectors.toList()));
        }
        return data;
    }

    public static Map<String, List<?>> readerToDiffBeans(final File file, final List<ExcelSheetDto> excelSheetDtos) throws Exception {
        if (file == null || !file.exists()) {
            throw new IaisRuntimeException("Please check excel source is exists");
        }

        if (excelSheetDtos == null || excelSheetDtos.isEmpty()) {
            throw new IaisRuntimeException("excel sheet dot error");
        }
        Map<String, List<?>> data = IaisCommonUtils.genNewHashMap();
        Workbook workBook = null;
        try (InputStream in = Files.newInputStream(file.toPath())) {
            workBook = new XSSFWorkbook(in);
            for (ExcelSheetDto excelSheetDto : excelSheetDtos) {
                int sheetAt = excelSheetDto.getSheetAt();
                Sheet sheet = workBook.getSheetAt(sheetAt);
                if (sheet == null) {
                    log.info(StringUtil.changeForLog("excel sheet name error"));
                    continue;
                }
                String sheetName = sheet.getSheetName();
                String name = excelSheetDto.getSheetName();
                if (!StringUtil.isEmpty(name) && !name.equals(sheetName)) {
                    log.info(StringUtil.changeForLog("excel sheet name error" + sheetName + " : " + name));
                    continue;
                }
                List<?> ans = parseSheetToList(sheet, excelSheetDto);
                data.put(excelSheetDto.getSheetName(), ans);
            }
            return data;
        } finally {
            try {
                if (workBook != null) {
                    workBook.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private static <T> List<T> parseSheetToList(Sheet sheet, ExcelSheetDto excelSheetDto) throws Exception {
        int startRowIndex = excelSheetDto.getStartRowIndex();
        boolean defaultValueNull = excelSheetDto.isDefaultValueNull();
        Class<T> clazz = (Class<T>) excelSheetDto.getSourceClass();
        Field[] fields = clazz.getDeclaredFields();
        List<T> ans = IaisCommonUtils.genNewArrayList();
        int rowCount = sheet.getLastRowNum();
        for (int i = startRowIndex; i <= rowCount; i++) {
            Row row = sheet.getRow(i);
            T o = clazz.newInstance();
            for (Field field : fields) {
                if (field.isAnnotationPresent(ExcelProperty.class)) {
                    ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
                    int index = excelProperty.cellIndex();
                    String format = excelProperty.format();
                    String cellValue = getCellValue(row.getCell(index));
                    Object value = getFieldValue(field, cellValue, format, defaultValueNull);
                    ReflectionUtil.setPropertyObj(field, value, o);
                }
            }
            ans.add(o);
        }
        return ans;
    }

}
