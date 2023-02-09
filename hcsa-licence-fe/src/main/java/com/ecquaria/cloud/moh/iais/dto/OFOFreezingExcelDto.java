package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.common.annotation.ExcelSheetProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * OFOFreezingExcelDto
 *
 * @author jiawei_gu
 * @date 2/9/2023
 */

@Data
@ExcelSheetProperty(sheetName = "Freezing", sheetAt = 2, startRowIndex = 0)
public class OFOFreezingExcelDto implements Serializable {

    @ExcelProperty(cellIndex = 0, cellName = "(1) Patient ID Type ", readOnly = true)
    private String patientIdType;

    @ExcelProperty(cellIndex = 1, cellName = "(2) Patient ID No. ", readOnly = true)
    private String patientIdNo;

    @ExcelProperty(cellIndex = 2, cellName = "(3) No.  Cryopreserved (Fresh Oocyte(s))", readOnly = true)
    private String noCryopreservedFreshOocyte;

    @ExcelProperty(cellIndex = 3, cellName = "(4) No.  Cryopreserved (Thawed Oocytes(s))", readOnly = true)
    private String noCryopreservedThawedOocyte;

    @ExcelProperty(cellIndex = 4, cellName = "(5) No.  Cryopreserved (Fresh Embryo(s))", readOnly = true)
    private String noCryopreservedFreshEmbryo;

    @ExcelProperty(cellIndex = 5, cellName = "(6) No.  Cryopreserved (Thawed Embryo(s))", readOnly = true)
    private String noCryopreservedThawedEmbryo;

    @ExcelProperty(cellIndex = 6, cellName = "(7) Cryopreservation Date", readOnly = true)
    private String cryopreservationDate;
}
