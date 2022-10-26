package sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdeFacilityInfo implements Serializable {
    private String name;
    private Boolean recommendApprove;
    // identification
    private String facilityCode;
}
