package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.common.annotation.ExcelSheetProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * SfoExcelDto
 *
 * @Author Shufei
 * @Date 2023/2/8 16:03
 **/
@Data
@ExcelSheetProperty(sheetName = "SFO", sheetAt = 0, startRowIndex = 0)
public class SfoExcelDto implements Serializable {
    @ExcelProperty(cellIndex = 0, cellName = "(1) Patient ID Type", readOnly = true)
    private String idType;

    @ExcelProperty(cellIndex = 1, cellName = "(2) Patient ID No.", readOnly = true)
    private String idNumber;

    @ExcelProperty(cellIndex = 2, cellName = "(3) Date of Freezing", readOnly = true)
    private String dateFreezing;

    @ExcelProperty(cellIndex = 3, cellName = "(4) Is it medically indicated", readOnly = true)
    private String isMedicallyIndicated;

    @ExcelProperty(cellIndex = 4, cellName = "(5) Reason", readOnly = true)
    private String reason;

    @ExcelProperty(cellIndex = 5, cellName = "(6) Reason(Others)", readOnly = true)
    private String othersReason;

    @ExcelProperty(cellIndex = 6, cellName = "(7) No. of Vials Cryopreserved", readOnly = true)
    private String cryopreserved;

}
