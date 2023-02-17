package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.common.annotation.ExcelSheetProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * PatientInfoCycleExcelDto
 *
 * @Author Shufei
 * @Date 2023/2/15 14:35
 **/
@Data
@ExcelSheetProperty(sheetName = "Sheet1", sheetAt = 0, startRowIndex = 0)
public class PatientInfoCycleExcelDto implements Serializable {

    @ExcelProperty(cellIndex = 0, cellName = "(1) Patient Name (as per NRIC/FIN/Passport Number)", readOnly = true)
    private String name;

    @ExcelProperty(cellIndex = 1, cellName = "(2) Patient ID Type", readOnly = true)
    private String idType;

    @ExcelProperty(cellIndex = 2, cellName = "(3) Patient ID No.", readOnly = true)
    private String idNumber;

    @ExcelProperty(cellIndex = 3, cellName = "(4) Date of Birth", readOnly = true)
    private String birthDate;

    @ExcelProperty(cellIndex = 4, cellName = "(5) Nationality", readOnly = true)
    private String nationality;

    @ExcelProperty(cellIndex = 5, cellName = "(6) Ethnic Group", readOnly = true)
    private String ethnicGroup;

    @ExcelProperty(cellIndex = 6, cellName = "(7) Ethnic Group (Others)", readOnly = true)
    private String ethnicGroupOther;

    @ExcelProperty(cellIndex = 7, cellName = "(8) Has patient registered for AR/IUI Treatment using another identification number before", readOnly = true)
    private String isPreviousIdentification;

    @ExcelProperty(cellIndex = 8, cellName = "(9) Previously used ID No", readOnly = true)
    private String preIdNumber;

    @ExcelProperty(cellIndex = 9, cellName = "(10) Previously used nationality", readOnly = true)
    private String preNationality;

    @ExcelProperty(cellIndex = 10, cellName = "(11) Previously used name", readOnly = true)
    private String preName;

    @ExcelProperty(cellIndex = 11, cellName = "(12) Husband's ID Type", readOnly = true)
    private String idTypeHbd;

    @ExcelProperty(cellIndex = 12, cellName = "(13) Husband's ID No.", readOnly = true)
    private String idNumberHbd;

    @ExcelProperty(cellIndex = 13, cellName = "(14) Husband's name", readOnly = true)
    private String nameHbd;

    @ExcelProperty(cellIndex = 14, cellName = "(15) Husband's Date of Birth", readOnly = true)
    private String birthDateHbd;

    @ExcelProperty(cellIndex = 15, cellName = "(16) Husband's Nationality", readOnly = true)
    private String nationalityHbd;

    @ExcelProperty(cellIndex = 16, cellName = "(17) Husband's Ethnic Group", readOnly = true)
    private String ethnicGroupHbd;

    @ExcelProperty(cellIndex = 17, cellName = "(18) Husband's Ethnic Group (Others)", readOnly = true)
    private String ethnicGroupOtherHbd;
}
