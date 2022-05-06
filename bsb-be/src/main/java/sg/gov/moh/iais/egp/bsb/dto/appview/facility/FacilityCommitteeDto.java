package sg.gov.moh.iais.egp.bsb.dto.appview.facility;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;

import java.io.Serializable;
import java.util.List;

@Data
public class FacilityCommitteeDto {
    @Data
    @NoArgsConstructor
    public static class BioSafetyCommitteePersonnel implements Serializable {
        private String committeeEntityId;
        private String salutation;
        private String name;
        private String nationality;
        private String idType;
        private String idNumber;
        private String designation;
        private String contactNo;
        private String email;
        private String employmentStartDt;
        private String workArea;
        private String role;
        private String employee;
        private String externalCompName;
    }
    private List<BioSafetyCommitteePersonnel> facCommitteePersonnelList;

    @JsonIgnore
    private DocRecordInfo savedFile;
    @JsonIgnore
    private NewDocInfo newFile;
    @JsonIgnore
    private String toBeDeletedRepoId;
    @JsonIgnore
    private boolean dataErrorExists;
}
