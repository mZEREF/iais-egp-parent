package sg.gov.moh.iais.egp.bsb.dto.info.facility;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.dto.info.bat.FacilityBiologicalAgentInfo;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FacilityDetailsInfo extends FacilityBasicInfo{
    private String classification;
    private List<String> existingFacilityActivityTypeApprovalList;
    private List<FacilityActivityInfo> facilityActivityInfoList;
    private List<FacilityBiologicalAgentInfo> facilityBiologicalAgentInfoList;
}
