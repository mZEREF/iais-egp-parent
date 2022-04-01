package sg.gov.moh.iais.egp.bsb.dto.appview.facility;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;

import java.util.Collection;
import java.util.Map;


@Data
@Slf4j
@NoArgsConstructor
public class FacilityRegisterDto {
    private FacilitySelectionDto facilitySelectionDto;
    private FacilityProfileDto facilityProfileDto;
    private FacilityOperatorDto facilityOperatorDto;
    private FacilityAdminAndOfficerDto facilityAdminAndOfficerDto;
    private FacilityCommitteeDto facilityCommitteeDto;
    private FacilityAuthoriserDto facilityAuthoriserDto;
    private Map<String, BiologicalAgentToxinDto> biologicalAgentToxinMap;
    private OtherApplicationInfoDto otherAppInfoDto;
    private Collection<DocRecordInfo> docRecordInfos;
    private FacilityAfcDto afcDto;
}
