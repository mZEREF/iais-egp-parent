package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DsElisLicenceDto {
    @ExcelProperty(cellIndex = 0, cellName = "UEN")
    private String uen;

    @ExcelProperty(cellIndex = 1, cellName = "HCI Code")
    private String hciCode;

    @ExcelProperty(cellIndex = 2, cellName = "Licence Type")
    private String licenceType;

    @ExcelProperty(cellIndex = 3, cellName = "Licence Start date")
    private String licStartDate;

    @ExcelProperty(cellIndex = 4, cellName = "Licence End Date")
    private String licEndDate;

    @ExcelProperty(cellIndex = 5, cellName = "Licence Cease Date")
    private String licCeseDate;

    @ExcelProperty(cellIndex = 6, cellName = "Licensee Name")
    private String licenseeName;

    @ExcelProperty(cellIndex = 7, cellName = "Postal Code")
    private String postalCode;

    @ExcelProperty(cellIndex = 8, cellName = "Address Type")
    private String addressType;

    @ExcelProperty(cellIndex = 9, cellName = "Block")
    private String block;

    @ExcelProperty(cellIndex = 10, cellName = "Floor")
    private String floor;

    @ExcelProperty(cellIndex = 11, cellName = "Unit")
    private String unit;

    @ExcelProperty(cellIndex = 12, cellName = "Street Name")
    private String streetName;

    @ExcelProperty(cellIndex = 13, cellName = "Building Name")
    private String buildingName;

    @ExcelProperty(cellIndex = 14, cellName = "HCI Name")
    private String hciName;

    @ExcelProperty(cellIndex = 15, cellName = "Contact No")
    private String contactNo;

    @ExcelProperty(cellIndex = 16, cellName = "Postal Code")
    private String prePostalCode;

    @ExcelProperty(cellIndex = 17, cellName = "Address Type")
    private String preAddressType;

    @ExcelProperty(cellIndex = 18, cellName = "Block")
    private String preBlock;

    @ExcelProperty(cellIndex = 19, cellName = "Floor")
    private String preFloor;

    @ExcelProperty(cellIndex = 20, cellName = "Unit")
    private String preUnit;

    @ExcelProperty(cellIndex = 21, cellName = "Street Name")
    private String preStreetName;

    @ExcelProperty(cellIndex = 22, cellName = "Building Name")
    private String preBuildingName;
}
