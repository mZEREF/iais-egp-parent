package com.ecquaria.cloud.moh.iais.helper.excel;

import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;

import static com.ecquaria.cloud.moh.iais.helper.FileUtils.EXCEL_TYPE_XSSF;
import static java.nio.file.Files.newInputStream;
import static java.nio.file.Files.newOutputStream;

/**
 * @author yi chen
 * @Date:2020/6/15
 **/
@Slf4j
public class IrregularExcelWriterUtil {
    private IrregularExcelWriterUtil(){}

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

        XSSFWorkbook workbook = null;
        String path = FileUtils.generationFileName("temp"+ System.currentTimeMillis(), EXCEL_TYPE_XSSF);
        File out = MiscUtil.generateFile(path);
        try (InputStream fileInputStream = newInputStream(file.toPath()); OutputStream outputStream = newOutputStream(out.toPath())) {
            workbook = XSSFWorkbookFactory.createWorkbook(fileInputStream);

            Sheet sheet = workbook.getSheetAt(sheetAt);

            int count = 0;
            for (Map.Entry<Integer, List<Integer>> entry : excelConfigIndex.entrySet()){
                int row = entry.getKey();
                Row sheetRow = sheet.getRow(row);

                if (sheetRow == null) {
                    sheetRow = sheet.createRow(row);
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

            workbook.write(outputStream);
        } catch (Exception e) {
            throw new Exception("has error when when export excel, may be is resource corrupted", e);
        }finally {
            if (workbook != null){
                workbook.close();
            }
        }

        return out;
    }
}
