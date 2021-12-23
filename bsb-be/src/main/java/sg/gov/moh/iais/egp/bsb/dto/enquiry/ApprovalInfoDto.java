package sg.gov.moh.iais.egp.bsb.dto.enquiry;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.common.annotation.ExcelSheetProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 *@author YiMing
 * @version 2021/10/15 14:16
 **/
@Getter
@Setter
@ExcelSheetProperty(sheetName = "Approval Information")
public class ApprovalInfoDto implements Serializable {

    private String id;

    private String apprNo;

    @ExcelProperty(cellIndex = 1, cellName = "Application Type")
    private String type;

    @ExcelProperty(cellIndex = 2, cellName = "Application status")
    private String status;

    @ExcelProperty(cellIndex = 3, cellName = "Facility Classification")
    private String facClassification;

    @ExcelProperty(cellIndex = 4, cellName = "Facility Type")
    private String facType;

    @ExcelProperty(cellIndex = 5, cellName = "Facility Name")
    private String facName;

    @ExcelProperty(cellIndex = 6, cellName = "Facility Address")
    private String facAddress;

    @ExcelProperty(cellIndex = 7, cellName = "Facility Status")
    private String facStatus;

    @ExcelProperty(cellIndex = 8, cellName = "Biological Agent")
    private String bat;

    @ExcelProperty(cellIndex = 9, cellName = "Nature Of The Sample")
    private String sampleName;

    @ExcelProperty(cellIndex = 20, cellName = "Risk Level")
    private String riskLevel;
    private String physicalPossessionOfBA;
}
