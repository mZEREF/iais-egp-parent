package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.common.annotation.ExcelSheetProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * DisposalExcelDto
 *
 * @author jiawei_gu
 * @date 2/9/2023
 */

@Data
@ExcelSheetProperty(sheetName = "Disposal", sheetAt = 3, startRowIndex = 0)
public class DisposalExcelDto implements Serializable {

    @ExcelProperty(cellIndex = 0, cellName = "(1) Patient ID Type ", readOnly = true)
    private String patientIdType;

    @ExcelProperty(cellIndex = 1, cellName = "(2) Patient ID No. ", readOnly = true)
    private String patientIdNo;

    @ExcelProperty(cellIndex = 2, cellName = "(3) What was disposed", readOnly = true)
    private String disposedItem;

    @ExcelProperty(cellIndex = 3, cellName = "(4) No. of Immature Disposed", readOnly = true)
    private String noImmatureDisposed;

    @ExcelProperty(cellIndex = 4, cellName = "(5) No. of Abnormally Fertilised Disposed", readOnly = true)
    private String noAbnormallyFertilisedDisposed;

    @ExcelProperty(cellIndex = 5, cellName = "(6) No. of Unfertilised Disposed", readOnly = true)
    private String noUnfertilisedDisposed;

    @ExcelProperty(cellIndex = 6, cellName = "(7) No. of Atretic Disposed", readOnly = true)
    private String noAtreticDisposed;

    @ExcelProperty(cellIndex = 7, cellName = "(8) No. of Damaged Disposed", readOnly = true)
    private String noDamagedDisposed;

    @ExcelProperty(cellIndex = 8, cellName = "(9) No. of Lysed/ Degenerated Disposed", readOnly = true)
    private String noLysedDegeneratedDisposed;

    @ExcelProperty(cellIndex = 9, cellName = "(10) No. of Poor Quality/Unhealthy/Abnormal Discarded", readOnly = true)
    private String noPoorQualityUnhealthyAbnormalDisposed;

    @ExcelProperty(cellIndex = 10, cellName = "(12) Discarded for Other Reasons", readOnly = true)
    private String discardedForOtherReasons;

    @ExcelProperty(cellIndex = 11, cellName = "(13) Other reasons for Discarding", readOnly = true)
    private String otherReasonsForDiscarding;
}
