package sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RFFacilityDetailsInfo implements Serializable {
    private String classification;
    private String facilityActivityType;
    private List<RFBatInfo> rfBatInfoList;
    private List<RFFacilityInfo> rfFacilityInfoList;
}
