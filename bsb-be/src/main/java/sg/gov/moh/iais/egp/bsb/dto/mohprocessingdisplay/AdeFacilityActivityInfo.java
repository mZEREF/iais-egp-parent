package sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay;

import lombok.Data;

import java.io.Serializable;

@Data
public class AdeFacilityActivityInfo implements Serializable {
    private String activityType;
    private Boolean recommendApprove;
}
