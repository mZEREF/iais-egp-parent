package sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay;

import lombok.Data;

import java.io.Serializable;

@Data
public class SubmissionDetailsInfo implements Serializable {
    private String applicationNo;
    private String applicationType;
    private String applicationSubType;
    private String applicationStatus;
}
