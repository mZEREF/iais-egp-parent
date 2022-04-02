package sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FacilityDetailsInfo implements Serializable {
    private String id;
    private String name;
    private String classification;
    private List<String> existingFacilityActivityTypeApprovalList;
    private List<FacilityActivityInfo> facilityActivityInfoList;
    private List<FacilityBiologicalAgentInfo> facilityBiologicalAgentInfoList;
}
