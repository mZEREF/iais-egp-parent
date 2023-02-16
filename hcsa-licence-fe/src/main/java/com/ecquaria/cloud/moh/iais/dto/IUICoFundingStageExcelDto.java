package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.common.annotation.ExcelSheetProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * IUICoFundingStageExcelDto
 *
 * @author jiawei_gu
 * @date 2/9/2023
 */

@Data
@ExcelSheetProperty(sheetName = "IUI Co-funding", sheetAt = 2, startRowIndex = 1)
public class IUICoFundingStageExcelDto implements Serializable {

    @ExcelProperty(cellIndex = 0, cellName = "(1) Patient ID Type ", readOnly = true)
    private String patientIdType;

    @ExcelProperty(cellIndex = 1, cellName = "(2) Patient ID No. ", readOnly = true)
    private String patientIdNo;

    @ExcelProperty(cellIndex = 2, cellName = "(3) Is the IUI treatment co-funded", readOnly = true)
    private String isCoFunded;

    @ExcelProperty(cellIndex = 3, cellName = "(4) Is there an Approved Appeal?", readOnly = true)
    private String isApprovedAppeal;

    @ExcelProperty(cellIndex = 4, cellName = "(5) Please indicate appeal reference number (if applicable)", readOnly = true)
    private String appealReferenceNum;

}
