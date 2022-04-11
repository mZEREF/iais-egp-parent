package sg.gov.moh.iais.egp.bsb.dto.appview.facility;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.info.common.EmployeeInfo;

import java.util.List;

@Data
public class FacilityAdminAndOfficerDto {
    private EmployeeInfo mainAdmin;

    private EmployeeInfo alternativeAdmin;

    private List<EmployeeInfo> officerList;
}
