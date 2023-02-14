package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.common.annotation.ExcelSheetProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * ArCoFundingExcelDto
 *
 * @Author dongchi
 * @Date 2023/2/14 10:09
 **/
@Data
@ExcelSheetProperty(sheetName = "AR Treatment Co-funding Stage", sheetAt = 10, startRowIndex = 1)
public class ArCoFundingExcelDto implements Serializable {
    @ExcelProperty(cellIndex = 0, cellName = "(1) Patient ID Type ", readOnly = true)
    private String idType;

    @ExcelProperty(cellIndex = 1, cellName = "(2) Patient ID No. ", readOnly = true)
    private String idNo;

    @ExcelProperty(cellIndex = 2, cellName = "(3) Is the ART cycle being co-funded?", readOnly = true)
    private String coFunding;

    @ExcelProperty(cellIndex = 3, cellName = "(4) Appeal reference number (if applicable)", readOnly = true)
    private String isThereAppeal;
}
