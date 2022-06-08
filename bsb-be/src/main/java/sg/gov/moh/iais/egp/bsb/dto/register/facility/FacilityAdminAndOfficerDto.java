package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.info.common.EmployeeInfo;

import java.io.Serializable;
import java.util.List;

@Data
public class FacilityAdminAndOfficerDto implements Serializable {
    private EmployeeInfo mainAdmin;

    private EmployeeInfo alternativeAdmin;

    private List<EmployeeInfo> officerList;
}
