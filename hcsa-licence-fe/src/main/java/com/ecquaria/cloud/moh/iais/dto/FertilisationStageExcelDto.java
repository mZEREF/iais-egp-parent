package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.common.annotation.ExcelSheetProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author dongchi
 * @date 2023-02-13
 */
@Data
@ExcelSheetProperty(sheetName = "Fertilisation", sheetAt = 3, startRowIndex = 0)
public class FertilisationStageExcelDto  implements Serializable {
    @ExcelProperty(cellIndex = 0, cellName = "(1) Patient ID Type", readOnly = true)
    private String idType;

    @ExcelProperty(cellIndex = 1, cellName = "(2) Patient ID No.", readOnly = true)
    private String idNo;

    @ExcelProperty(cellIndex = 2, cellName = "(3) Source of Oocyte: Donor", readOnly = true)
    private String sourceOfOocyte;

    @ExcelProperty(cellIndex = 3, cellName = "(4) Source of Oocyte: Patient", readOnly = true)
    private String sourceOfOocytePatient;

    @ExcelProperty(cellIndex = 4, cellName = "(5) Source of Oocyte: Patient's Ovarian Tissue", readOnly = true)
    private String sourceOfOocytePot;

    @ExcelProperty(cellIndex = 5, cellName = "(6) Was Fresh or frozen oocyte(s) used?", readOnly = true)
    private String oocyteUsed;

    @ExcelProperty(cellIndex = 6, cellName = "(7) How many oocyte(s) were used in this cycle?", readOnly = true)
    private String usedOocytesNum;



    @ExcelProperty(cellIndex = 7, cellName = "(8) Source of Semen: Husband", readOnly = true)
    private String sourceOfSemenHus;

    @ExcelProperty(cellIndex = 8, cellName = "(9) Source of Semen: Husband's Testicular Tissue", readOnly = true)
    private String sourceOfSemenHusTes;

    @ExcelProperty(cellIndex = 9, cellName = "(10) Source of Semen: Donor", readOnly = true)
    private String sourceOfSemenDon;

    @ExcelProperty(cellIndex = 10, cellName = "(11) Source of Semen: Donor's Testicular Tissue", readOnly = true)
    private String sourceOfSemenDonTes;

    @ExcelProperty(cellIndex = 11, cellName = "(12) Was fresh or frozen sperm used", readOnly = true)
    private String freshOrFrozen;

    @ExcelProperty(cellIndex = 12, cellName = "=CONCAT(\"(\",COLUMN(),\")\",\" How many vials of sperm were extracted?\")", readOnly = true)
    private String extractedSpermVialsNum;

    @ExcelProperty(cellIndex = 13, cellName = "(14) How many vials of sperm were used this cycle?", readOnly = true)
    private String usedSpermVialsNum;



    @ExcelProperty(cellIndex = 14, cellName = "(15) IFV used", readOnly = true)
    private String ifvUsed;

    @ExcelProperty(cellIndex = 15, cellName = "(16) ICSI used", readOnly = true)
    private String icsiUsed;

    @ExcelProperty(cellIndex = 16, cellName = "(17) GIFT used", readOnly = true)
    private String giftUsed;

    @ExcelProperty(cellIndex = 17, cellName = "(18) ZIFT used", readOnly = true)
    private String ziftUsed;

    @ExcelProperty(cellIndex = 18, cellName = "(19) No. of Fresh Oocytes Inseminated", readOnly = true)
    private String freshOocytesInseminatedNum;

    @ExcelProperty(cellIndex = 19, cellName = "(20) No. of Thawed Oocytes Inseminated", readOnly = true)
    private String thawedOocytesInseminatedNum;

    @ExcelProperty(cellIndex = 20, cellName = "(21) No. of Fresh Oocytes Microinjected", readOnly = true)
    private String freshOocytesMicroInjectedNum;

    @ExcelProperty(cellIndex = 21, cellName = "(22) No. of Thawed Oocytes Microinjected", readOnly = true)
    private String thawedOocytesMicroInjectedNum;

    @ExcelProperty(cellIndex = 22, cellName = "(23) No. of Fresh Oocytes Used for GIFT", readOnly = true)
    private String freshOocytesGiftNum;

    @ExcelProperty(cellIndex = 23, cellName = "(24) No. of Thawed Oocytes Used for GIFT", readOnly = true)
    private String thawedOocytesGiftNum;

    @ExcelProperty(cellIndex = 24, cellName = "(25) No. of Fresh Oocytes Used for ZIFT", readOnly = true)
    private String freshOocytesZiftNum;

    @ExcelProperty(cellIndex = 25, cellName = "(26) No. of Thawed Oocytes Used for ZIFT", readOnly = true)
    private String thawedOocytesZiftNum;

}
