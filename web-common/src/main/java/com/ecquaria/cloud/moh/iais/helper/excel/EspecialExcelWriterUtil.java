package com.ecquaria.cloud.moh.iais.helper.excel;

import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * @author yi chen
 * @Date:2020/6/15
 **/
@Slf4j
public class EspecialExcelWriterUtil {
    private EspecialExcelWriterUtil(){}

    public static File writerToExcelByIndex(final File file, int sheetAt, String[] val,  Map<Integer, List<Integer>> excelConfigIndex) throws Exception {
        return writerToExcelByIndex(file, sheetAt, val, excelConfigIndex, false);
    }

    /**
     * For special requirements
     * @Author yichen
     * @Date: 11:49 2020/6/15
     **/
    public static File writerToExcelByIndex(final File file, int sheetAt, String[] val, Map<Integer, List<Integer>> excelConfigIndex, boolean hidden) throws Exception {
        if (file == null){
            throw new IaisRuntimeException("can not find file when writerToExcelByIndex");
        }


        File out = file;
        XSSFWorkbook workbook = null;
        try (InputStream fileInputStream = java.nio.file.Files.newInputStream(file.toPath())) {
            workbook = XSSFWorkbookFactory.createWorkbook(fileInputStream);

            Sheet sheet = workbook.getSheetAt(sheetAt);

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

                    if (hidden){
                        sheet.setColumnHidden(i, true);
                    }

                    String value = val[count];
                    cell.setCellValue(value);
                    count++;
                }
            }

            OutputStream outputStream = new FileOutputStream(out);
            workbook.write(outputStream);

        } catch (Exception e) {
            throw new Exception("has error when when export excel, may be is resource corrupted");
        }finally {
            if (workbook != null){
                workbook.close();
            }
        }

        return out;
    }
}
