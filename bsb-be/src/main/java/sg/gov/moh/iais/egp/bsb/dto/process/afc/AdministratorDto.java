package sg.gov.moh.iais.egp.bsb.dto.process.afc;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
public class AdministratorDto implements Serializable{
    @Data
    @NoArgsConstructor
    public static class FacilityAdministratorInfo implements Serializable {
        private String adminName;
        private String nationality;
        private String idType;
        private String idNo;
        private String designation;
        private String contactNo;
        private String email;
        private String employmentStartDate;
    }

    private FacilityAdministratorInfo mainAdmin;
    private FacilityAdministratorInfo alternativeAdmin;
}
