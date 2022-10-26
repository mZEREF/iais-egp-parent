package sg.gov.moh.iais.egp.bsb.dto.chklst.assessment;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.constant.AssessmentState;


/**
 * Data in this DTO is used when applicant try to submit self-assessment,
 * we provide a simple table for them to review
 */
@Data
public class PreAssessmentDto {
    private String facName;
    private String classification;
    private String appId;
    private AssessmentState assessmentState;
}
