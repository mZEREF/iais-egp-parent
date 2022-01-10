package sg.gov.moh.iais.egp.bsb.dto.appview.facility;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
public class FacilityAdminDto {
    @Data
    @NoArgsConstructor
    public static class FacilityAdministratorInfo implements Serializable {
        private String adminEntityId;
        private String adminName;
        private String nationality;
        private String idType;
        private String idNumber;
        private String designation;
        private String contactNo;
        private String email;
        private String employmentStartDt;
    }

    private FacilityAdministratorInfo mainAdmin;
    private FacilityAdministratorInfo alternativeAdmin;
}
