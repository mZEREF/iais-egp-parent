package sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay;

import lombok.Data;

import java.io.Serializable;

@Data
public class AdeBatInfo implements Serializable {
    private String batCode;
    private String schedule;
    private Boolean recommendApprove;
    private String activityType;
}
