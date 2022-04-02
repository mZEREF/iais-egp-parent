package sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay;

import lombok.Data;

import java.io.Serializable;

@Data
public class FacilityActivityInfo implements Serializable {
    private String id;
    private String activityType;
    private String status;
}
