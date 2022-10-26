package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.bat.BiologicalAgentToxinDto;
import sg.gov.moh.iais.egp.bsb.dto.renewal.FacilityInstructionDto;
import sg.gov.moh.iais.egp.bsb.dto.renewal.FacilityRegistrationReviewDto;
import sg.gov.moh.iais.egp.bsb.dto.renewal.InstructionDto;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;


@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FacilityRegisterDto implements Serializable{
    // if is new application, appId is null.
    private String appId;
    //this is used to saveDraft module
    private String appType;

    private String uen;
    private FacilitySelectionDto facilitySelectionDto;
    private FacilityProfileDto facilityProfileDto;
    private FacilityOperatorDto facilityOperatorDto;
    private FacilityAdminAndOfficerDto facilityAdminAndOfficerDto;
    private FacilityCommitteeDto facilityCommitteeDto;
    private FacilityAuthoriserDto facilityAuthoriserDto;
    private Map<String, BiologicalAgentToxinDto> biologicalAgentToxinMap;
    private OtherApplicationInfoDto otherAppInfoDto;
    private Collection<DocRecordInfo> docRecordInfos;
    private Collection<NewDocInfo> newDocInfos;
    private Collection<String> createdFileRepoIds;
    private FacilityAfcDto afcDto;
    private PreviewSubmitDto previewSubmitDto;

    //renewal special dto
    private InstructionDto instructionDto;// afc
    private FacilityInstructionDto facilityInstructionDto; // facility
    private FacilityRegistrationReviewDto facilityRegistrationReviewDto;

    //use in rfc and renew
    private String amendFacilityId;
}
