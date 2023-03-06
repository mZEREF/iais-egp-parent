package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.common.annotation.ExcelSheetProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * OFOCycleStageExcelDto
 *
 * @author jiawei_gu
 * @date 2/9/2023
 */

@Data
@ExcelSheetProperty(sheetName = "OFO Cycle Stage", sheetAt = 0, startRowIndex = 1)
public class OFOCycleStageExcelDto implements Serializable {

    @ExcelProperty(cellIndex = 0, cellName = "(1) Patient ID Type ", readOnly = true)
    private String patientIdType;

    @ExcelProperty(cellIndex = 1, cellName = "(2) Patient ID No. ", readOnly = true)
    private String patientIdNo;

    @ExcelProperty(cellIndex = 2, cellName = "(3) Date of Freezing", readOnly = true)
    private String dateOfFreezing;

    @ExcelProperty(cellIndex = 3, cellName = "(4) Is it medically indicated? \n" +
            "\n" +
            "Note: To indicate if the patient is undergoing egg freezing due to medical reason(s)", readOnly = true)
    private String isMedicallyIndicated;

    @ExcelProperty(cellIndex = 4, cellName = "(5) Reason", readOnly = true)
    private String reason;

    @ExcelProperty(cellIndex = 5, cellName = "(6) Reason(Others)\n" +
            "\n" +
            "Note: To indicate via freetext if 'Others' is chosen for (6).", readOnly = true)
    private String freetextOtherReason;

    @ExcelProperty(cellIndex = 6, cellName = "(7) No. Cryopreserved", readOnly = true)
    private String noCryopreserved;

    @ExcelProperty(cellIndex = 7, cellName = "(8) Others\n" +
            "\n" +
            "Note: Mandatory if 'No. Cryopreserved' is 0.", readOnly = true)
    private String others;
}
