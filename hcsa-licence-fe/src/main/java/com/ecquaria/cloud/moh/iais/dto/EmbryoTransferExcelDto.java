package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.common.annotation.ExcelSheetProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * EmbryoTransferExcelDto
 *
 * @Author dongchi
 * @Date 2023/2/13 15:42
 **/
@Data
@ExcelSheetProperty(sheetName = "Embryo Transfer", sheetAt = 5, startRowIndex = 1)
public class EmbryoTransferExcelDto implements Serializable {
    @ExcelProperty(cellIndex = 0, cellName = "(1) Patient ID Type ", readOnly = true)
    private String idType;

    @ExcelProperty(cellIndex = 1, cellName = "(2) Patient ID No. ", readOnly = true)
    private String idNo;

    @ExcelProperty(cellIndex = 2, cellName = "(3) No. Transferred", readOnly = true)
    private String transferNum;

    @ExcelProperty(cellIndex = 3, cellName = "(4) 1st Embryo", readOnly = true)
    private String embryo1;

    @ExcelProperty(cellIndex = 4, cellName = "(5) Was the 1st Embryo Transferred a fresh or thawed embryo?", readOnly = true)
    private String embryoType1;

    @ExcelProperty(cellIndex = 5, cellName = "(6) 2nd Embryo", readOnly = true)
    private String embryo2;

    @ExcelProperty(cellIndex = 6, cellName = "(7) Was the 2nd Embryo Transferred a fresh or thawed embryo?", readOnly = true)
    private String embryoType2;

    @ExcelProperty(cellIndex = 7, cellName = "(8) 3rd Embryo", readOnly = true)
    private String embryo3;

    @ExcelProperty(cellIndex = 8, cellName = "(9) Was the 3rd Embryo Transferred a fresh or thawed embryo?", readOnly = true)
    private String embryoType3;

    @ExcelProperty(cellIndex = 9, cellName = "(10) 4th Embryo", readOnly = true)
    private String embryo4;

    @ExcelProperty(cellIndex = 10, cellName = "(11) Was the 4th Embryo Transferred a fresh or thawed embryo?", readOnly = true)
    private String embryoType4;

    @ExcelProperty(cellIndex = 11, cellName = "(12) 5th Embryo", readOnly = true)
    private String embryo5;

    @ExcelProperty(cellIndex = 12, cellName = "(13) Was the 5th Embryo Transferred a fresh or thawed embryo?", readOnly = true)
    private String embryoType5;

    @ExcelProperty(cellIndex = 13, cellName = "(14) 6th Embryo", readOnly = true)
    private String embryo6;

    @ExcelProperty(cellIndex = 14, cellName = "(15) Was the 6th Embryo Transferred a fresh or thawed embryo?", readOnly = true)
    private String embryoType6;

    @ExcelProperty(cellIndex = 15, cellName = "(16) 7th Embryo", readOnly = true)
    private String embryo7;

    @ExcelProperty(cellIndex = 16, cellName = "(17) Was the 7th Embryo Transferred a fresh or thawed embryo?", readOnly = true)
    private String embryoType7;

    @ExcelProperty(cellIndex = 17, cellName = "(18) 8th Embryo", readOnly = true)
    private String embryo8;

    @ExcelProperty(cellIndex = 18, cellName = "(19) Was the 8th Embryo Transferred a fresh or thawed embryo?", readOnly = true)
    private String embryoType8;

    @ExcelProperty(cellIndex = 19, cellName = "(20) 9th Embryo", readOnly = true)
    private String embryo9;

    @ExcelProperty(cellIndex = 20, cellName = "(21) Was the 9th Embryo Transferred a fresh or thawed embryo?", readOnly = true)
    private String embryoType9;

    @ExcelProperty(cellIndex = 21, cellName = "(22) 10th Embryo", readOnly = true)
    private String embryo10;

    @ExcelProperty(cellIndex = 22, cellName = "(23) Was the 10th Embryo Transferred a fresh or thawed embryo?", readOnly = true)
    private String embryoType10;

    @ExcelProperty(cellIndex = 23, cellName = "(24) 1st Date of Transfer", readOnly = true)
    private String firstTransferDate;

    @ExcelProperty(cellIndex = 24, cellName = "(25) 2nd Date of Transfer (if applicable)", readOnly = true)
    private String secondTransferDate;
}
