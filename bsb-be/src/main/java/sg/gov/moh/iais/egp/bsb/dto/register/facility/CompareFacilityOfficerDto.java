package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import lombok.Data;
import lombok.NoArgsConstructor;
import sg.gov.moh.iais.egp.bsb.dto.info.common.EmployeeInfo;

@Data
@NoArgsConstructor
public class CompareFacilityOfficerDto {
    private EmployeeInfo oldEmployeeInfo;
    private EmployeeInfo newEmployeeInfo;

    public CompareFacilityOfficerDto(EmployeeInfo oldEmployeeInfo, EmployeeInfo newEmployeeInfo) {
        this.oldEmployeeInfo = oldEmployeeInfo;
        this.newEmployeeInfo = newEmployeeInfo;
    }
}
