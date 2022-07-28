package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DsElisDoctorDto {
    @ExcelProperty(cellIndex = 0, cellName = "UEN")
    private String uen;

    @ExcelProperty(cellIndex = 1, cellName = "HCI Code")
    private String hciCode;

    @ExcelProperty(cellIndex = 2, cellName = "PRN")
    private String prn;

    @ExcelProperty(cellIndex = 3, cellName = "Doctor Name")
    private String name;

    @ExcelProperty(cellIndex = 4, cellName = "Register Indicator")
    private String registerIndicator;
}
