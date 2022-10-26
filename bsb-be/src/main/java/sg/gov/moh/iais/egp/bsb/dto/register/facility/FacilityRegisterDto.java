package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.dto.register.bat.BiologicalAgentToxinDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;


@Data
@Slf4j
@NoArgsConstructor
public class FacilityRegisterDto implements Serializable {
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
    private FacilityAfcDto afcDto;
}
