package sg.gov.moh.iais.egp.bsb.dto.processderegistration;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.SubmissionDetailsInfo;

import java.io.Serializable;


@Data
public class AOProcessDto implements Serializable {
    private SubmissionDetailsInfo submissionDetailsInfo;

    private String applicationId;
    private String taskId;

    //dynamic content
    private String subject;//text 200 O
    private String dynamicContent;//text 4000 O

    //ao processing
    private String currentStatus;
    private String doRemarks;
    private String doFinalRemarks;
    private String aoRemarks;//text 300 O
    private String finalRemarks;
    private String processingDecision;//dropdown M (Approve, Reject, Route back to Duty Officer, Route to Higher Manager)
    private String reasonForRejection;//text 500 O, Mandatory if “Processing Decision” is “Rejected”
}
