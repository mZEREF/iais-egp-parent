package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.common.annotation.ExcelSheetProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ExcelSheetProperty(sheetName = "Outcome of IUI Cycle", sheetAt = 1, startRowIndex = 1)
public class OutcomeOfIUICycleStageExcelDto implements Serializable {
    @ExcelProperty(cellIndex = 0, cellName = "(1) Patient ID Type ", readOnly = true)
    private String patientIdType;

    @ExcelProperty(cellIndex = 1, cellName = "(2) Patient ID No. ", readOnly = true)
    private String patientIdNo;

    @ExcelProperty(cellIndex = 2, cellName = "(1) Is Clinical Pregnancy Detected", readOnly = true)
    private String isClinicalPregnancyDetected;
}
