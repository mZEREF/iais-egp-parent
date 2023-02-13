package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.common.annotation.ExcelSheetProperty;
import lombok.Data;

/**
 * ThawingStageExcelDto
 *
 * @Author dongchi
 * @Date 2023/2/13 14:15
 **/

@Data
@ExcelSheetProperty(sheetName = "Thawing", sheetAt = 0, startRowIndex = 0)
public class ThawingStageExcelDto {

    @ExcelProperty(cellIndex = 0, cellName = "(1) Patient ID Type ", readOnly = true)
    private String patientIdType;

    @ExcelProperty(cellIndex = 1, cellName = "(2) Patient ID No. ", readOnly = true)
    private String patientIdNo;

    @ExcelProperty(cellIndex = 2, cellName = "(3) Thawing Oocyte(s)", readOnly = true)
    private String thawedOocytes;

    @ExcelProperty(cellIndex = 3, cellName = "(4) Thawing Embryo(s)", readOnly = true)
    private String thawingEmbryos;

    @ExcelProperty(cellIndex = 4, cellName = "(5) No. of Oocytes Thawed", readOnly = true)
    private String thawedOocytesNum;

    @ExcelProperty(cellIndex = 5, cellName = "(6) No. of Oocytes Survived after Thawing (Mature)", readOnly = true)
    private String thawedOocytesSurvivedMatureNum;

    @ExcelProperty(cellIndex = 6, cellName = "(7) No. of Oocytes Survived after Thawing (Immature)", readOnly = true)
    private String thawedOocytesSurvivedImmatureNum;

    @ExcelProperty(cellIndex = 7, cellName = "(8) No. of Oocytes Survived after Thawing (Others)", readOnly = true)
    private String thawedOocytesSurvivedOtherNum;

    @ExcelProperty(cellIndex = 8, cellName = "(9) No. of Embryos Thawed", readOnly = true)
    private String thawedEmbryosNum;

    @ExcelProperty(cellIndex = 9, cellName = "(10)  No. of Embryos Survived after Thawing ", readOnly = true)
    private String thawedEmbryosSurvivedNum;
}
