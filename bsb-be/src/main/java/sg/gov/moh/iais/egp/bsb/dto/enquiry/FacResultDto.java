package sg.gov.moh.iais.egp.bsb.dto.enquiry;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.common.annotation.ExcelSheetProperty;
import com.googlecode.jmapper.annotations.JMap;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@ExcelSheetProperty(sheetName = "FacilityInformation")
public class FacResultDto {
    @ExcelProperty(cellIndex = 1, cellName = "Facility Name")
    private String facilityName;
    @ExcelProperty(cellIndex = 3, cellName = "Facility Classification")
    private String facilityClassification;
    private String facilityType;
    @ExcelProperty(cellIndex = 2, cellName = "Facility Address")
    private String address;
    @ExcelProperty(cellIndex = 4, cellName = "Biological Agent/Login")
    private String batName;
    @ExcelProperty(cellIndex = 5, cellName = "Risk Of The Biological Agent/Login")
    private String riskLevel;
    @ExcelProperty(cellIndex = 6, cellName = "Facility Expiry Date")
    private String expiryDate;
    @ExcelProperty(cellIndex = 7, cellName = "Gazetted Area")
    private String gazettedArea;
    @ExcelProperty(cellIndex = 8, cellName = "Facility Operator")
    private String operatorName;
    private List<FacilityAdmin> facilityAdmins;
    @ExcelProperty(cellIndex = 9, cellName = "Current Facility Status")
    private String facilityStatus;
    private String afcName;


    @NoArgsConstructor
    @Data
    public static class FacilityAdmin{
        private String type;
        private String name;
    }
}
