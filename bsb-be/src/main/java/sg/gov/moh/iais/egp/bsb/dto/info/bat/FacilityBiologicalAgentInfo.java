package sg.gov.moh.iais.egp.bsb.dto.info.bat;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FacilityBiologicalAgentInfo extends BatBasicInfo{
    private String schedule;
    private String status;
    private String approveType;
    private String facilityBiologicalAgentId;
    private String facilityActivityId;
}
