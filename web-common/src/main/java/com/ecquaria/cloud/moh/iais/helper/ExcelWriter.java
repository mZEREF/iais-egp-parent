package com.ecquaria.cloud.moh.iais.helper;

/*
 *author: yichen
 *date time:9/18/2019 1:07 PM
 *description:
 */

import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Slf4j
public class ExcelWriter {

    @Getter @Setter
    public  String exportPath;
    @Getter @Setter
    private String fileName;
    @Getter @Setter
    private String sheetName;

    public ExcelWriter(String sheetName, String exportPath){
        this.sheetName = sheetName;
        this.exportPath = exportPath;
    }

    public String generationFileName(String clzName){
        long currentTimeMillis = System.currentTimeMillis();
        String jointText = " " + currentTimeMillis + ".xls";
        return clzName + jointText;
    }

    public <T> void exportXls(List<T> files)  {
        if(files == null || files.isEmpty()){
            throw new IaisRuntimeException("the excel mode for export can not be empty");
        }

        String clzName = files.get(0).getClass().getSimpleName();
        this.fileName = generationFileName(clzName);
        OutputStream fileOut = null;
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet(sheetName);
        int row = 0;
        boolean writeCell = true;
        Row sheetRow = sheet.createRow(row);
        try {
            fileOut = new FileOutputStream(this.fileName);
            for(T t : files){
                Class clz = t.getClass();
                Field[] fields = clz.getDeclaredFields();

                if(row == 0 && writeCell){
                    createCell(fields, sheetRow);
                    writeCell = false;
                }

                row++;
                sheetRow = sheet.createRow(row);
                Object obj = t;
                createCellValue(clz, fields, sheetRow, obj);
            }

            wb.write(fileOut);

        } catch (IOException | IllegalAccessException | InvocationTargetException | NoSuchMethodException  e) {
            log.info("exportXls has exception " + e.getMessage());
        }finally {
            try {
                wb.close();

                if (fileOut != null){
                    fileOut.close();
                }
            } catch (IOException e) {
                log.info("exportXls close IO exception " + e.getMessage());
            }
        }
    }

    private void createCell(Field[] fields, Row sheetRow){
        int cellIndex = 0;
        for(Field field : fields){
            String fieldName = field.getName();
            if(breakWrite(fieldName)){
                continue;
            }
            sheetRow.createCell(cellIndex).setCellValue(IaisEGPHelper.capitalized(field.getName()));
            cellIndex++;
        }
    }

    private void createCellValue(Class clz, Field[] fields, Row sheetRow, Object obj) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        int cellIndex = 0;
        for(Field field : fields) {
            String fieldName = field.getName();
            if(breakWrite(fieldName)){
                continue;
            }
            sheetRow.createCell(cellIndex).setCellValue(
                    setValue(clz.getDeclaredMethod("get" +
                            IaisEGPHelper.capitalized(field.getName())).invoke(obj)));
            cellIndex++;
        }
    }


    private boolean breakWrite(String fieldName){
        return ("threadContext".equals(fieldName) || "serialVersionUID".equals(fieldName));
    }

    private String setValue(Object obj) {
        return obj == null ? "" : obj.toString();
    }

}
