package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.common.annotation.ExcelSheetProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * ArOocyteRetrievalExcelDto
 *
 * @Author dongchi
 * @Date 2023/2/14 10:27
 **/
@Data
@ExcelSheetProperty(sheetName = "Oocyte Retrieval", sheetAt = 2, startRowIndex = 1)
public class ArOocyteRetrievalExcelDto implements Serializable {

    @ExcelProperty(cellIndex = 0, cellName = "(1) Patient ID Type ", readOnly = true)
    private String patientIdType;

    @ExcelProperty(cellIndex = 1, cellName = "(2) Patient ID No. ", readOnly = true)
    private String patientIdNo;

    @ExcelProperty(cellIndex = 2, cellName = "(3) Severe Ovarian Hyperstimulation Syndrome", readOnly = true)
    private String isOvarianSyndrome;

    @ExcelProperty(cellIndex = 3, cellName = "(4) Oocyte(s) was retrieved from patient", readOnly = true)
    private String isFromPatient;

    @ExcelProperty(cellIndex = 4, cellName = "(5) Oocyte(s) was retrieved from patient's ovarian tissue?", readOnly = true)
    private String isFromPatientTissue;

    @ExcelProperty(cellIndex = 5, cellName = "(6) No. Retrieved (Mature)", readOnly = true)
    private String matureRetrievedNum;

    @ExcelProperty(cellIndex = 6, cellName = "(7) No. Retrieved (Immature)", readOnly = true)
    private String immatureRetrievedNum;

    @ExcelProperty(cellIndex = 7, cellName = "(8) No. Retrieved (Others)", readOnly = true)
    private String otherRetrievedNum;
}

