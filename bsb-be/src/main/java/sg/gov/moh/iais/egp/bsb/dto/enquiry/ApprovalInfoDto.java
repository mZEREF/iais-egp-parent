package sg.gov.moh.iais.egp.bsb.dto.enquiry;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.common.annotation.ExcelSheetProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * AUTHOR: YiMing
 * DATE:2021/7/26 15:51
 * DESCRIPTION: TODO
 **/

@Getter
@Setter
@ExcelSheetProperty(sheetName = "ApprovalInformation")
public class ApprovalInfoDto implements Serializable {

    @ExcelProperty(cellIndex = 1, cellName = "Application Type")
    private String approvalType;

    @ExcelProperty(cellIndex = 2, cellName = "Application status")
    private String approvalStatus;

    @ExcelProperty(cellIndex = 3, cellName = "Facility Classification")
    private String facilityClassification;

    @ExcelProperty(cellIndex = 4, cellName = "Facility Type")
    private String facilityType;

    @ExcelProperty(cellIndex = 5, cellName = "Facility Name")
    private String facilityName;

    @ExcelProperty(cellIndex = 6, cellName = "Facility Address")
    private String facilityAddress;

    @ExcelProperty(cellIndex = 7, cellName = "Facility Status")
    private String facilityStatus;

    @ExcelProperty(cellIndex = 8, cellName = "Biological Agent")
    private String agent;

    @ExcelProperty(cellIndex = 9, cellName = "Nature Of The Sample")
    private String natureOfTheSample;

    @ExcelProperty(cellIndex = 20, cellName = "Risk Level")
    private String riskLevelOfTheBiologicalAgent;
    private String physicalPossessionOfBA;
}
