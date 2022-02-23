package sg.gov.moh.iais.egp.bsb.dto.datasubmission;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.entity.FacilityAdminDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.FacilityAuthoriserDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.FacilityBiosafetyCommitteeDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.FacilityOfficerDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;

import java.io.Serializable;
import java.util.List;

@Data
public class MohReviewDataSubmissionDto implements Serializable {
    private String applicationId;
    private String taskId;
    private String notificationType;
    //info
    private SubmissionDetailsDto submissionDetailsDto;
    // bioSafety personnel
    private List<FacilityAdminDto> facilityAdminDtoList;
    private List<FacilityBiosafetyCommitteeDto> facilityBiosafetyCommitteeDtoList;
    private List<FacilityAuthoriserDto> facilityAuthoriserDtoList;
    private FacilityOfficerDto facilityOfficerDto;
    // BA/T
    private String facilityClassification;
    private String activityType;
    private List<BatDto> batDtoList;
    //doc
    private List<DocDisplayDto> docDisplayDtoList;
    //do processing
    private String doRemarks;//text 500 O
    private String aoRemarks;//text 500 O
    private String doDecision;
    private String aoDecision;

    private String module;
}