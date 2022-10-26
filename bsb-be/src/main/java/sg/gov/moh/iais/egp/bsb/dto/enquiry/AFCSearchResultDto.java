package sg.gov.moh.iais.egp.bsb.dto.enquiry;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.common.annotation.ExcelSheetProperty;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.entity.ApprovedFacilityCertifierAdminDto;

import java.util.List;


@Data
@ExcelSheetProperty(sheetName = "ApplicationInformation")
public class AFCSearchResultDto {
    @ExcelProperty(cellIndex = 1, cellName = "Organisation Name")
    private String orgName;
    @ExcelProperty(cellIndex = 2, cellName = "Organisation Address")
    private String orgAddress;
    @ExcelProperty(cellIndex = 3, cellName = "Approved facility Certifier Status")
    private String afcStatus;
    private List<ApprovedFacilityCertifierAdminDto> adminDtoList;
    @ExcelProperty(cellIndex = 4, cellName = "Approved Date")
    private String approvedDt;
    @ExcelProperty(cellIndex = 5, cellName = "Expiry Date")
    private String expiryDt;
}
