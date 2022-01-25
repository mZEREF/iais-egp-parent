package sg.gov.moh.iais.egp.bsb.dto.processderegistration;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : LiRan
 * @date : 2022/1/20
 */
@Data
public class HMProcessDto implements Serializable {
    private SubmissionDetailsDto submissionDetailsDto;

    private String applicationId;
    private String taskId;

    //dynamic content
    private String subject;//text 200 O
    private String dynamicContent;//text 4000 O

    //hm processing
    private String currentStatus;
    private String hmRemarks;//text 500 O
    private String processingDecision;//dropdown M (Approve, Reject)
}
