package sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RFFacilityInfo implements Serializable {
    private String id;
    private String facilityName;
    private String status;
}
