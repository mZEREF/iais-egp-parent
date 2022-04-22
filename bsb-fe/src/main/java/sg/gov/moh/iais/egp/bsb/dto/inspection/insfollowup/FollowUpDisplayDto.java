package sg.gov.moh.iais.egp.bsb.dto.inspection.insfollowup;

import lombok.Data;

import java.io.Serializable;

@Data
public class FollowUpDisplayDto implements Serializable {
    private String id;
    private String itemDescription;
    private String observation;
    private String actionRequired;
    private String dueDate;
    private String mohRemarks;
    private String applicantRemarks;
}
