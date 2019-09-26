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
    public static final String EXCEL_XLS = "xls";

    @Getter @Setter
    public  String exportPath;
    @Getter @Setter
    private String fileName;
    @Getter @Setter
    private String sheetName;

    public <T> void exportXls(List<T> files)  {
        if(files == null || files.size() == 0){
            throw new IaisRuntimeException("the excel mode for export can not be empty");
        }

        String sheetName = files.get(0).getClass().getSimpleName();
        OutputStream fileOut = null;
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet(sheetName);
        int row = 0;
        boolean canWriteCell = false;
        Row sheetRow = sheet.createRow(row);
        try {
            fileOut = new FileOutputStream(this.fileName != null ? this.fileName : "export record.xls");
            for(T t : files){
                Class clz = t.getClass();
                Field[] fields = clz.getDeclaredFields();

                if(row == 0 && !canWriteCell){
                    int cellIndex = 0;
                    for(Field field : fields){
                        String fieldName = field.getName();
                        if(!isNeedWrite(fieldName)){
                            continue;
                        }
                        sheetRow.createCell(cellIndex).setCellValue(IaisEGPHelper.capitalized(field.getName()));
                        cellIndex++;
                    }
                    canWriteCell = true;
                }

                row++;
                sheetRow = sheet.createRow(row);
                Object obj = t;
                int cellIndex2 = 0;
                for(Field field : fields) {
                    String fieldName = field.getName();
                    if(!isNeedWrite(fieldName)){
                        continue;
                    }
                    sheetRow.createCell(cellIndex2).setCellValue(
                            setValue(clz.getDeclaredMethod("get" +
                                    IaisEGPHelper.capitalized(field.getName())).invoke(obj)));
                    cellIndex2++;
                }

            }

            wb.write(fileOut);

        } catch (IOException | IllegalAccessException | InvocationTargetException | NoSuchMethodException  e) {
            log.info("exportXls has exception " + e.getMessage());
        }finally {
            try {
                if (fileOut != null){
                    fileOut.close();
                }
                if(wb != null){
                    wb.close();
                }
            } catch (IOException e) {
                log.info("exportXls close resource exception " + e.getMessage());
            }
        }
    }

    private boolean isNeedWrite(String fieldName){
        if("threadContext".equals(fieldName) || "serialVersionUID".equals(fieldName)){
            return false;
        }
        return true;
    }

    private String setValue(Object obj) {
        return obj == null ? "" : obj.toString();
    }

}
