package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DsElisUserDto {
    @ExcelProperty(cellIndex = 0, cellName = "UEN")
    private String uen;

    @ExcelProperty(cellIndex = 1, cellName = "HCI Code")
    private String hciCode;

    @ExcelProperty(cellIndex = 2, cellName = "NRIC")
    private String nric;

    @ExcelProperty(cellIndex = 3, cellName = "Name")
    private String name;

    @ExcelProperty(cellIndex = 4, cellName = "Salutation")
    private String salutation;

    @ExcelProperty(cellIndex = 5, cellName = "Designation")
    private String designation;

    @ExcelProperty(cellIndex = 6, cellName = "Mobile No")
    private String mobile;

    @ExcelProperty(cellIndex = 7, cellName = "Office No")
    private String office;

    @ExcelProperty(cellIndex = 8, cellName = "Email")
    private String email;

    @ExcelProperty(cellIndex = 9, cellName = "Role")
    private String role;

    @ExcelProperty(cellIndex = 10, cellName = "IsActive")
    private String isActive;
}
