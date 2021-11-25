package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.common.annotation.ExcelSheetProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description PatientInfoExcelDto
 * @Auther chenlei on 11/24/2021.
 */
@Data
@ExcelSheetProperty(sheetName = "PATIENT_INFO", sheetAt = 0, startRowIndex = 1)
public class PatientInfoExcelDto implements Serializable {

    @ExcelProperty(cellIndex = 0, cellName = "PATIENT_NAME", readOnly = true)
    private String name;

    @ExcelProperty(cellIndex = 1, cellName = "PATIENT_ID_TYPE", readOnly = true)
    private String idType;

    @ExcelProperty(cellIndex = 2, cellName = "PATIENT_ID_NO", readOnly = true)
    private String idNumber;

    @ExcelProperty(cellIndex = 3, cellName = "PATIENT_DOB", readOnly = true)
    private String birthDate;

    @ExcelProperty(cellIndex = 4, cellName = "PATIENT_NATIONALITY", readOnly = true)
    private String nationality;

    @ExcelProperty(cellIndex = 5, cellName = "PATIENT_ETHNIC_GROUP", readOnly = true)
    private String ethnicGroup;

    @ExcelProperty(cellIndex = 6, cellName = "PATIENT_ETHNIC_GROUP_OTHERS", readOnly = true)
    private String ethnicGroupOther;

    @ExcelProperty(cellIndex = 7, cellName = "IS_AWARE_OF_PREVIOUS_IDENTITY", readOnly = true)
    private String isPreviousIdentification;

    @ExcelProperty(cellIndex = 8, cellName = "PREVIOUS_PATIENT_ID_TYPE", readOnly = true)
    private String preIdType;

    @ExcelProperty(cellIndex = 9, cellName = "PREVIOUS_PATIENT_ID_NO", readOnly = true)
    private String preIdNumber;

    @ExcelProperty(cellIndex = 10, cellName = "PREVIOUS_PATIENT_NATIONALITY", readOnly = true)
    private String preNationality;

    @ExcelProperty(cellIndex = 11, cellName = "HUSBAND_NAME", readOnly = true)
    private String nameHbd;

    @ExcelProperty(cellIndex = 12, cellName = "HUSBAND_ID_TYPE", readOnly = true)
    private String idTypeHbd;

    @ExcelProperty(cellIndex = 13, cellName = "HUSBAND_ID_NO", readOnly = true)
    private String idNumberHbd;

    @ExcelProperty(cellIndex = 14, cellName = "HUSBAND_DOB", readOnly = true)
    private String birthDayHbd;

    @ExcelProperty(cellIndex = 15, cellName = "HUSBAND_NATIONALITY", readOnly = true)
    private String nationalityHbd;

    @ExcelProperty(cellIndex = 16, cellName = "HUSBAND_ETHNIC_GROUP", readOnly = true)
    private String ethnicGroupHbd;

    @ExcelProperty(cellIndex = 17, cellName = "HUSBAND_ETHNIC_GROUP_OTHERS", readOnly = true)
    private String ethnicGroupOtherHbd;

}
