package sg.gov.moh.iais.egp.bsb.dto.process;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : LiRan
 * @date : 2021/12/29
 */
@Data
public class HMScreeningDto implements Serializable {
    private String taskId;
    private String applicationId;
    private SubmitDetailsDto submitDetailsDto;

    private String riskLevel;
    private String riskLevelComments;
    private String doRecommendation;
    private String aoReviewDecision;
    private String aoRemarks;
    private String validityStartDate;
    private String validityEndDate;

    private String hmRemarks;
    private String processingDecision;
}
