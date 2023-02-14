package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.common.annotation.ExcelSheetProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * embryoCreatedExcelDto
 *
 * @Author dongchi
 * @Date 2023/2/13 15:33
 **/
@Data
@ExcelSheetProperty(sheetName = "Embryo Created", sheetAt = 4, startRowIndex = 1)
public class EmbryoCreatedExcelDto implements Serializable {
    @ExcelProperty(cellIndex = 0, cellName = "(1) Patient ID Type ", readOnly = true)
    private String idType;

    @ExcelProperty(cellIndex = 1, cellName = "(2) Patient ID No. ", readOnly = true)
    private String idNo;

    @ExcelProperty(cellIndex = 2, cellName = "(3) No. of Transferrable embryos created from fresh oocyte(s)", readOnly = true)
    private String transEmbrFreshOccNum;

    @ExcelProperty(cellIndex = 3, cellName = "(4) No. of Poor Quality / Unhealthy / Abnormally Developed embryos created from fresh oocyte(s)", readOnly = true)
    private String poorDevFreshOccNum;

    @ExcelProperty(cellIndex = 4, cellName = "(5) No. of Transferrable embryos created from thawed oocyte(s)", readOnly = true)
    private String transEmbrThawOccNum;

    @ExcelProperty(cellIndex = 5, cellName = "(6) No. of Poor Quality / Unhealthy / Abnormally Developed embryos  created from thawed oocyte(s)", readOnly = true)
    private String poorDevThawOccNum;
}
