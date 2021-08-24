package sg.gov.moh.iais.egp.bsb.dto.enquiry;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.common.annotation.ExcelSheetProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * AUTHOR: YiMing
 * DATE:2021/7/26 10:48
 * DESCRIPTION: TODO
 **/

@Data
@ExcelSheetProperty(sheetName = "ApplicationInformation")
public class ApplicationInfoDto implements Serializable{


    private String id;

    @ExcelProperty(cellIndex = 1, cellName = "Application No")
    private String applicationNo;

    @ExcelProperty(cellIndex = 2, cellName = "Application Type")
    private String applicationType;

    @ExcelProperty(cellIndex = 3, cellName = "Application Status")
    private String applicationStatus;

    @ExcelProperty(cellIndex = 4, cellName = "Application Submission Date")
    private String applicationSubmissionDate;

    @ExcelProperty(cellIndex = 5, cellName = "Approval  Date")
    private String approvalDate;

    @ExcelProperty(cellIndex = 6, cellName = "Facility Classfiication")
    private String facilityClassification;

    @ExcelProperty(cellIndex = 7,cellName = "Facility Type")
    private String facilityType;

    @ExcelProperty(cellIndex = 8, cellName = "Facility Name")
    private String facilityName;

    @ExcelProperty(cellIndex = 9, cellName = "Biological Agent/Login")
    private String biologicalAgent;

    @ExcelProperty(cellIndex = 10, cellName = "Risk Level Of The Biological Agent/Login")
    private String riskLevelOfTheBiologicalAgent;

    @ExcelProperty(cellIndex = 11, cellName = "Process Type")
    private String processType;

    @ExcelProperty(cellIndex = 12, cellName = "Verified By DO")
    private String verifiedByDO;

    @ExcelProperty(cellIndex = 13, cellName = "Verified By AO")
    private String verifiedByAO;

    @ExcelProperty(cellIndex = 14, cellName = "Verified By HM")
    private String verifiedByHM;
}
