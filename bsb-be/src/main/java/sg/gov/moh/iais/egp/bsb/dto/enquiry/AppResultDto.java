package sg.gov.moh.iais.egp.bsb.dto.enquiry;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.common.annotation.ExcelSheetProperty;
import lombok.Data;



/**
 *@author YiMing
 * @version 2021/10/15 14:16
 **/

@Data
@ExcelSheetProperty(sheetName = "ApplicationInformation")
public class AppResultDto {
    private String id;
    @ExcelProperty(cellIndex = 1, cellName = "Application No")
    private String applicationNo;
    @ExcelProperty(cellIndex = 2, cellName = "Application Type")
    private String appType;
    @ExcelProperty(cellIndex = 3, cellName = "Application Status")
    private String appStatus;
    @ExcelProperty(cellIndex = 4, cellName = "Application Submission Date")
    private String applicationEntityDt;
    @ExcelProperty(cellIndex = 5, cellName = "Approval Date")
    private String approvalEntityDt;
    @ExcelProperty(cellIndex = 6, cellName = "Facility Classification")
    private String facilityClassification;
    @ExcelProperty(cellIndex = 7,cellName = "Facility Type")
    private String facilityType;
    @ExcelProperty(cellIndex = 8, cellName = "Facility Name")
    private String facilityName;
    @ExcelProperty(cellIndex = 9, cellName = "Biological Agent/Login")
    private String bioName;
    @ExcelProperty(cellIndex = 10, cellName = "Risk Level Of The Biological Agent/Login")
    private String riskLevel;
    @ExcelProperty(cellIndex = 11, cellName = "Process Type")
    private String processType;
    @ExcelProperty(cellIndex = 12, cellName = "Verified By DO")
    private String doVerifiedEntityDt;
    @ExcelProperty(cellIndex = 13, cellName = "Verified By AO")
    private String aoVerifiedEntityDt;
    @ExcelProperty(cellIndex = 14, cellName = "Verified By HM")
    private String hmVerifiedEntityDt;

}
