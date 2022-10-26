package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
public class FacilityAuthoriserDto implements Serializable {
    @Data
    @NoArgsConstructor
    public static class FacilityAuthorisedPersonnel implements Serializable {
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

    /** Get a list of committee data for display.
     * All fields are not master codes */
    public List<FacilityAuthoriserFileDto> getDataListForDisplay() {
        return FacilityAuthoriserFileDto.toDisplayDtoList(this.facAuthPersonnelList);
    }
}
