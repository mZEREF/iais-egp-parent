package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.common.annotation.ExcelSheetProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * ArTransferInOutExcelDto
 *
 * @Author dongchi
 * @Date 2023/2/14 10:18
 **/
@Data
@ExcelSheetProperty(sheetName = "Transfer In or Out", sheetAt = 12, startRowIndex = 1)
public class ArTransferInOutExcelDto implements Serializable {
    @ExcelProperty(cellIndex = 0, cellName = "(1) Patient ID Type", readOnly = true)
    private String idType;

    @ExcelProperty(cellIndex = 1, cellName = "(2) Patient ID No.", readOnly = true)
    private String idNumber;

    @ExcelProperty(cellIndex = 2, cellName = "(3) Is this a transfer in or out", readOnly = true)
    private String transferType;

    @ExcelProperty(cellIndex = 3, cellName = "(4) Oocyte(s) was Transferred?", readOnly = true)
    private String isOocyteTransfer;

    @ExcelProperty(cellIndex = 4, cellName = "(5) No. of Oocyte(s) Transferred", readOnly = true)
    private String oocyteNum;

    @ExcelProperty(cellIndex = 5, cellName = "(6) Embryo(s) was Transferred?", readOnly = true)
    private String isEmbryoTransfer;

    @ExcelProperty(cellIndex = 6, cellName = "(7) No. of Embryo(s) Transferred", readOnly = true)
    private String embryoNum;

    @ExcelProperty(cellIndex = 7, cellName = "(8) Sperm was Transferred?", readOnly = true)
    private String isSpermTransfer;

    @ExcelProperty(cellIndex = 8, cellName = "(9) Vials of Sperm Transferred", readOnly = true)
    private String spermVialsNum;

    @ExcelProperty(cellIndex = 9, cellName = "(10) Were the Gamete(s)or Embryos from a Donor?", readOnly = true)
    private String isDonor;

    @ExcelProperty(cellIndex = 10, cellName = "(11) Transferred In from / Transferred Out to\n" +
            "\n" +
            "Note: Indicate the exact AR Centre or Bank Name.", readOnly = true)
    private String transferInOut;

    @ExcelProperty(cellIndex = 11, cellName = "(12) Date of Transfer", readOnly = true)
    private String dateTransfer;
}
