package sg.gov.moh.iais.egp.bsb.dto.appview.facility;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.info.common.EmployeeInfo;

import java.io.Serializable;
import java.util.List;


@Data
public class FacilityAdminAndOfficerDto implements Serializable {
    private final EmployeeInfo mainAdmin;
    private final EmployeeInfo alternativeAdmin;
    private final List<EmployeeInfo> officerList;
}
