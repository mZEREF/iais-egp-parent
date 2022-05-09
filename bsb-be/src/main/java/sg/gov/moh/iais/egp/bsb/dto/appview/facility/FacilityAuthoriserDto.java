package sg.gov.moh.iais.egp.bsb.dto.appview.facility;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class FacilityAuthoriserDto {
    @Data
    @NoArgsConstructor
    public static class FacilityAuthorisedPersonnel {
        private String authEntityId;

        private String salutation;

        private String name;

        private String nationality;

        private String idType;

        private String idNumber;

        private String designation;

        private String contactNo;

        private String email;

        private String employmentStartDt;

        private String employmentPeriod;

        private String workArea;

        private String securityClearanceDt;

        private String employee;

        private String externalCompName;
    }

    private List<FacilityAuthorisedPersonnel> facAuthPersonnelList;

    private String protectedPlace;
}
