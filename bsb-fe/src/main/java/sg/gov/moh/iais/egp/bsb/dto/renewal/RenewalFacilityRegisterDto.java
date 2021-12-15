package sg.gov.moh.iais.egp.bsb.dto.renewal;

import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.*;

import java.util.Collection;
import java.util.Map;

/**
 * @author : LiRan
 * @date : 2021/12/11
 */
@Data
@NoArgsConstructor
public class RenewalFacilityRegisterDto {
    private InstructionDto instructionDto;
    private ReviewDto reviewDto;
    private FacilitySelectionDto facilitySelectionDto;
    private FacilityProfileDto facilityProfileDto;
    private FacilityOperatorDto facilityOperatorDto;
    private FacilityAuthoriserDto facilityAuthoriserDto;
    private FacilityAdministratorDto facilityAdministratorDto;
    private FacilityOfficerDto facilityOfficerDto;
    private FacilityCommitteeDto facilityCommitteeDto;
    private Map<String, BiologicalAgentToxinDto> biologicalAgentToxinMap;
    private Collection<DocRecordInfo> docRecordInfos;
    private AuditTrailDto auditTrailDto;
}
