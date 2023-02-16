package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.common.annotation.ExcelSheetProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * OutcomeTransferExcelDto
 *
 * @Author dongchi
 * @Date 2023/2/13 15:43
 **/
@Data
@ExcelSheetProperty(sheetName = "Outcome of Embryo Transfer", sheetAt = 6, startRowIndex = 1)
public class OutcomeTransferExcelDto  implements Serializable {
    @ExcelProperty(cellIndex = 0, cellName = "(1) Patient ID Type ", readOnly = true)
    private String idType;

    @ExcelProperty(cellIndex = 1, cellName = "(2) Patient ID No. ", readOnly = true)
    private String idNo;

    @ExcelProperty(cellIndex = 2, cellName = "(3) Outcome of Embryo Transferred\n" +
            "\n" +
            "Note: Unknown outcomes refer to scenarios where patient did not respond to follow-up, post-transfer procedure", readOnly = true)
    private String transferedOutcome;
}
