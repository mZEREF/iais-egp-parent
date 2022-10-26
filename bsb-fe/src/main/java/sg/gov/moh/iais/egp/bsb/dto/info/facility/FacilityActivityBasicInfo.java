package sg.gov.moh.iais.egp.bsb.dto.info.facility;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class FacilityActivityBasicInfo implements Serializable {
    private String id;

    @JsonProperty("activity_type")
    private String activityType;
}
