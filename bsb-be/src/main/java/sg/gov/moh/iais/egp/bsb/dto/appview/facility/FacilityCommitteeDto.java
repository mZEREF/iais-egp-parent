package sg.gov.moh.iais.egp.bsb.dto.appview.facility;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


@Data
@NoArgsConstructor
public class FacilityCommitteeDto implements Serializable{
    @Data
    @NoArgsConstructor
    public static class BioSafetyCommitteePersonnel implements Serializable {
        private String committeeEntityId;
        private String name;
        private String nationality;
        private String idType;
        private String idNumber;
        private String designation;
        private String contactNo;
        private String email;
        private String employmentStartDt;
        private String expertiseArea;
        private String role;
        private String employee;
        private String externalCompName;
    }

    private String inputMethod;
    private List<BioSafetyCommitteePersonnel> facCommitteePersonnelList;
}
