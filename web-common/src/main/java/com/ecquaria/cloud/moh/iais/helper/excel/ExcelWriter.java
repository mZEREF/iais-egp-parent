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
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Slf4j
public final class ExcelWriter {

    private static String EXCEL_TYPE_HSSF			= "xls";
    private static String EXCEL_TYPE_XSSF			= "xlsx";

    private static String generationFileName(final String clzName){
        long currentTimeMillis = System.currentTimeMillis();
        String jointText = currentTimeMillis + "." + EXCEL_TYPE_XSSF;
        return clzName + jointText;
    }

    private static String getSheetName(final Class<?> clz){
        ExcelSheetProperty annotation = clz.getAnnotation(ExcelSheetProperty.class);
        if (annotation == null){
            throw new IaisRuntimeException("Please check the sheet annotation for the excel source class.");
        }

        return annotation.sheetName();
    }

    private static void setFieldName(final Class<?> clz, final Sheet sheet){
        Row sheetRow = sheet.createRow(0);
        sheetRow.createCell(0).setCellValue("SN");
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields){
            if (field.isAnnotationPresent(ExcelProperty.class)){
                ExcelProperty annotation = field.getAnnotation(ExcelProperty.class);
                int rowIndx = annotation.index();
                String rowName = annotation.cellName();
                sheetRow.createCell(rowIndx).setCellValue(rowName);
            }
        }
    }

    public static File exportExcel(final List<?> source, final Class<?> clz, final String fileName){
        if (IaisCommonUtils.isEmpty(source) || clz == null){
            throw new IaisRuntimeException("Please check the export excel parameters.");
        }
        XSSFWorkbook wb = new XSSFWorkbook();
        String retFileName = generationFileName(fileName);
        try (FileOutputStream fileOut =  new FileOutputStream(retFileName) ){
            Sheet sheet = wb.createSheet(getSheetName(clz));
            setFieldName(clz, sheet);
            createCellValue(sheet, source, clz);
            wb.write(fileOut);
        } catch (FileNotFoundException e) {
            log.debug(e.getMessage());
        } catch (NoSuchMethodException e) {
            log.debug(e.getMessage());
        } catch (IllegalAccessException e) {
            log.debug(e.getMessage());
        } catch (InvocationTargetException e) {
            log.debug(e.getMessage());
        } catch (IOException e) {
            log.debug(e.getMessage());
        }finally {
            try {
                wb.close();
            } catch (IOException e) {
                log.debug(e.getMessage());
            }
        }

        return new File(retFileName);
    }

    private static void createCellValue(final Sheet sheet, final List<?> source, final Class<?> clz) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //sequence number
        int sequence = 1;
        for (Object t : source){
            Row sheetRow = sheet.createRow(sequence);
            sheetRow.createCell(0).setCellValue(sequence);
            sequence++;

            Field[] fields = clz.getDeclaredFields();
            for(Field field : fields) {
                if (field.isAnnotationPresent(ExcelProperty.class)){
                    ExcelProperty annotation = field.getAnnotation(ExcelProperty.class);
                    int rowIndx = annotation.index();
                    sheetRow.createCell(rowIndx).setCellValue(setValue(clz.getDeclaredMethod("get" +
                            IaisEGPHelper.capitalized(field.getName())).invoke(t)));
                }
            }
        }

    }

    private static String setValue(final Object obj) {
        return obj == null ? "" : obj.toString();
    }

}
