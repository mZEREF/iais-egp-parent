package sg.gov.moh.iais.egp.bsb.util.excel;

import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.*;
import sg.gov.moh.iais.egp.bsb.util.excel.column.ColSkipChecker;
import sg.gov.moh.iais.egp.bsb.util.excel.column.ColumnsSkipChecker;
import sg.gov.moh.iais.egp.bsb.util.excel.column.FirstColSkipChecker;
import sg.gov.moh.iais.egp.bsb.util.excel.column.NoColSkipChecker;
import sg.gov.moh.iais.egp.bsb.util.excel.line.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExcelConverter {
    public static final ExcelConverter DEFAULT = new Builder().trimTrailingSeparator().skipEmptyLine().build();
    public static final ExcelConverter TRIM_SEQUENCE = new Builder().skipFirstColumn().trimTrailingSeparator().skipEmptyLine().build();

    @Getter
    public static class Builder {
        private ColSkipChecker colSkipChecker;
        private final List<LinePostProcessor> linePostProcessors;
        private LineSkipChecker lineSkipChecker;

        public Builder() {
            colSkipChecker = NoColSkipChecker.INSTANCE;
            linePostProcessors = new ArrayList<>();
            lineSkipChecker = NoLineSkipChecker.INSTANCE;
        }

        public Builder colSkipChecker(ColSkipChecker checker) {
            if (checker == null) {
                throw new IllegalArgumentException("Column Skip Checker should not be null");
            }
            this.colSkipChecker = checker;
            return this;
        }

        public Builder skipFirstColumn() {
            this.colSkipChecker = FirstColSkipChecker.INSTANCE;
            return this;
        }

        public Builder skipSpecificColumns(int[] indexes) {
            if (indexes == null) {
                throw new IllegalArgumentException("Indexes should not be null");
            }
            this.colSkipChecker = new ColumnsSkipChecker(indexes);
            return this;
        }

        /** The order to add the processor is the order to execute them */
        public Builder linePostProcessor(LinePostProcessor processor) {
            if (processor == null) {
                throw new IllegalArgumentException("Line Post Processor should not be null");
            }
            this.linePostProcessors.add(processor);
            return this;
        }

        public Builder linePostProcessors(List<LinePostProcessor> linePostProcessors) {
            if (linePostProcessors == null) {
                throw new IllegalArgumentException("Line Post Processors should not be null");
            }
            this.linePostProcessors.addAll(linePostProcessors);
            return this;
        }

        public Builder trimTrailingSeparator() {
            this.linePostProcessors.add(EmptyTrailingSeparatorTrimmer.INSTANCE);
            return this;
        }

        public Builder lineSkipChecker(LineSkipChecker checker) {
            if (checker == null) {
                throw new IllegalArgumentException("Line Skip Checker should not be null");
            }
            this.lineSkipChecker = checker;
            return this;
        }

        public Builder skipEmptyLine() {
            this.lineSkipChecker = EmptyLineSkipChecker.INSTANCE;
            return this;
        }

        public ExcelConverter build() {
            return new ExcelConverter(this);
        }
    }


    private final ColSkipChecker colSkipChecker;
    private final List<LinePostProcessor> linePostProcessors;
    private final LineSkipChecker lineSkipChecker;

    public ExcelConverter() {
        colSkipChecker = NoColSkipChecker.INSTANCE;
        linePostProcessors = Collections.emptyList();
        lineSkipChecker = NoLineSkipChecker.INSTANCE;
    }

    public ExcelConverter(Builder builder) {
        this.colSkipChecker = builder.getColSkipChecker();
        this.linePostProcessors = builder.getLinePostProcessors();
        this.lineSkipChecker = builder.getLineSkipChecker();
    }


    /**
     * This method convert an EXCEL to a list of objects.
     * This method actually convert EXCEL to a middle state CSV first..
     */
    public <T> List<T> excel2List(byte[] contents, Class<T> clz) throws IOException {
        String csv = excelToCsv(contents);
        return CsvConvertUtil.csv2List(csv, clz);
    }


    public <T> List<T> excel2List(InputStream inputStream, Class<T> clz) throws IOException {
        String csv = excelToCsv(inputStream);
        return CsvConvertUtil.csv2List(csv, clz);
    }


    /**
     *  Convert EXCEL content to CSV string.
     * @param content content of EXCEL file
     * @return CSV string
     */
    @SneakyThrows(IOException.class)
    public String excelToCsv(byte[] content) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content);
        return excelToCsv(inputStream);
    }


    /**
     * Convert an EXCEL to CSV string.
     * This method only retrieve data in the first sheet, any other sheets will be ignored.
     * @param inputStream Input stream of an EXCEL file (.xls or .xlsx)
     * @return CSV string
     * @throws IOException if an error occurs while reading the data
     */
    public String excelToCsv(InputStream inputStream) throws IOException {
        Sheet sheet;
        try (Workbook wb = WorkbookFactory.create(inputStream)) {
            sheet = wb.getSheetAt(0);
        }
        return sheetToCsv(sheet);
    }



    /**
     * Convert all data in a sheet to a CSV string.
     * All special characters are well escaped {@link CsvConvertUtil#escapeEmbeddedCharacters(String)}.
     * Each line will contain a trailing comma, so the CSV parser have to allow it.
     * @return CSV string
     */
    private String sheetToCsv(Sheet sheet) {
        DataFormatter formatter = new DataFormatter(true);
        StringBuilder sb = new StringBuilder();
        for (Row row : sheet) {
            StringBuilder line = new StringBuilder();
            int lastCellNum = row.getLastCellNum();
            for (int i = 0; i <= lastCellNum; i++) {
                if (this.colSkipChecker.check(i)) {
                    continue;
                }
                Cell cell = row.getCell(i);
                /* Even the cell doesn't contain value, we need to print a comma, so the raw structure of the table
                 * is kept. If we don't print the comma, the relation between header and the value is broken! */
                if (cell != null) {
                    line.append(CsvConvertUtil.escapeEmbeddedCharacters(formatter.formatCellValue(cell)));
                }
                /* We don't check, just put a comma, so every line will contain an additional trailing comma.
                 * We can let CSV parser to allow the trailing comma, so this won't be a problem. */
                line.append(',');
            }
            for (LinePostProcessor processor : this.linePostProcessors) {
                processor.process(line);
            }
            if (!this.lineSkipChecker.check(line)) {
                sb.append(line).append('\n');
            }
        }
        return sb.toString();
    }
}
