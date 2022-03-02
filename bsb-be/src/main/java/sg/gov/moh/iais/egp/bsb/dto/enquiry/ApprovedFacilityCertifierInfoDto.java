package sg.gov.moh.iais.egp.bsb.dto.enquiry;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.common.annotation.ExcelSheetProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@ExcelSheetProperty(sheetName = "ApplicationInformation")
public class ApprovedFacilityCertifierInfoDto implements Serializable {

    @ExcelProperty(cellIndex = 1, cellName = "Organisation Name")
    private String organisationName;

    @ExcelProperty(cellIndex = 2, cellName = "Organisation Address")
    private String organisationAddress;

    @ExcelProperty(cellIndex = 3, cellName = "Approved facility Certifier Status")
    private String afcStatus;

    @ExcelProperty(cellIndex = 4, cellName = "Administrator")
    private String administrator;

    @ExcelProperty(cellIndex = 5, cellName = "Approved Date")
    private String approvedDate;

    @ExcelProperty(cellIndex = 6, cellName = "Expiry Date")
    private String expiryDate;
    
    private String action;
}
