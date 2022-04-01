package sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FacilityBiologicalAgentInfo implements Serializable {
    private String id;
    private String batName;
    private String schedule;
    private String status;
    private String approveType;
    private String facilityActivityId;
}
