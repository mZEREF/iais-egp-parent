package sg.gov.moh.iais.egp.bsb.dto.processderegistration;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.SubmissionDetailsInfo;

import java.io.Serializable;


@Data
public class DOProcessDto implements Serializable {
    private SubmissionDetailsInfo submissionDetailsInfo;

    private String applicationId;
    private String taskId;

    //dynamic content
    private String subject;//text 200 O
    private String dynamicContent;//text 4000 O

    //do processing
    private String currentStatus;
    private String doRemarks;//text 500 O
    private String finalRemarks;//checkbox O
    private String processingDecision;//dropdown M (Recommend Approval, Rejected, Request for Information)
    private String reasonForRejection;//text 500 O, Mandatory if “Processing Decision” is “Rejected”
}
