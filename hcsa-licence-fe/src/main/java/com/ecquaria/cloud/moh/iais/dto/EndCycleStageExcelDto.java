package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.common.annotation.ExcelSheetProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * EndCycleStageExcelDto
 *
 * @Author dongchi
 * @Date 2023/2/13 16:59
 **/
@Data
@ExcelSheetProperty(sheetName = "Complete or Abandoned Cycle", sheetAt = 8, startRowIndex = 2)
public class EndCycleStageExcelDto implements Serializable {
    @ExcelProperty(cellIndex = 0, cellName = "(1) Patient ID Type ", readOnly = true)
    private String idType;

    @ExcelProperty(cellIndex = 1, cellName = "(2) Patient ID No. ", readOnly = true)
    private String idNo;

    @ExcelProperty(cellIndex = 2, cellName = "(3) Is Current Cycle Completed?", readOnly = true)
    private String cycleAbandoned;

    @ExcelProperty(cellIndex = 3, cellName = "(4) Reason for abandonment", readOnly = true)
    private String abandonReason;

    @ExcelProperty(cellIndex = 4, cellName = "(5) Reason for Abandonment (Others)\n" +
            "\n" +
            "Please indicate the reason for abandoning cycle via free text.", readOnly = true)
    private String otherAbandonReason;
}
