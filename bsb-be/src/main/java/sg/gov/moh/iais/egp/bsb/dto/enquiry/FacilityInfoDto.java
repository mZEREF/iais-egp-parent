package sg.gov.moh.iais.egp.bsb.dto.enquiry;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.common.annotation.ExcelSheetProperty;
import lombok.Data;
import java.io.Serializable;

/**
 *@author YiMing
 * @version 2021/10/15 14:16
 **/

@Data
@ExcelSheetProperty(sheetName = "FacilityInformation")
public class FacilityInfoDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String facilityId;

    @ExcelProperty(cellIndex = 1, cellName = "Facility Name")
    private String facilityName;

    @ExcelProperty(cellIndex = 2, cellName = "Facility Address")
    private String facilityAddress;

    @ExcelProperty(cellIndex = 3, cellName = "Facility Classification")
    private String facilityClassification;

    @ExcelProperty(cellIndex = 4, cellName = "Facility Type")
    private String facilityType;

    @ExcelProperty(cellIndex = 5, cellName = "Biological Agent/Login")
    private String biologicalAgent;

    @ExcelProperty(cellIndex = 6, cellName = "Risk Of The Bioloical Agent/Login")
    private String riskLevelOfTheBiologicalAgent;

    @ExcelProperty(cellIndex = 7, cellName = "Facility Expiry Date")
    private String facilityExpiryDate;

    @ExcelProperty(cellIndex = 8, cellName = "Gazetted Area")
    private String gazettedArea;

    @ExcelProperty(cellIndex = 9, cellName = "Facility Operator")
    private String facilityOperator;

    @ExcelProperty(cellIndex = 10, cellName = "Facility Admin")
    private String facilityAdmin;

    @ExcelProperty(cellIndex = 11, cellName = "Current Facility Status")
    private String currentFacilityStatus;

    @ExcelProperty(cellIndex = 12, cellName = "Approval Facility Certifier")
    private String approvedFacilityCertifier;
}
