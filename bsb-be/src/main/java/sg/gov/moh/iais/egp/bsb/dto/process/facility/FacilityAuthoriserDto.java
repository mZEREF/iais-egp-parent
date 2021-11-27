package sg.gov.moh.iais.egp.bsb.dto.process.facility;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


@Data
public class FacilityAuthoriserDto implements Serializable{
    @Data
    @NoArgsConstructor
    public static class FacilityAuthorisedPersonnel implements Serializable {
        private String name;
        private String nationality;
        private String idType;
        private String idNumber;
        private String designation;
        private String contactNo;
        private String email;
        private String employmentStartDate;
        private String employmentPeriod;
        private String workArea;
        private String securityClearanceDate;
        private String isProtectedPlace;
    }

    private String inputMethod;
    private List<FacilityAuthorisedPersonnel> facAuthPersonnelList;
    private String isProtectedPlace;
}
