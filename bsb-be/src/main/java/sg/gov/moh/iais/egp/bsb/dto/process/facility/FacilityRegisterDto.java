package sg.gov.moh.iais.egp.bsb.dto.process.facility;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.dto.process.DocRecordInfo;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

@Data
@Slf4j
public class FacilityRegisterDto implements Serializable {
    private FacilityProfileDto facilityProfileDto;
    private FacilityOperatorDto facilityOperatorDto;
    private FacilityAuthoriserDto facilityAuthoriserDto;
    private FacilityAdminDto facilityAdministratorDto;
    private FacilityOfficerDto facilityOfficerDto;
    private FacilityCommitteeDto facilityCommitteeDto;
    private Map<String, BiologicalAgentToxinDto> biologicalAgentToxinMap;
    private Collection<DocRecordInfo> docRecordInfos;
}
