package sg.gov.moh.iais.egp.bsb.dto.process;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : LiRan
 * @date : 2021/12/29
 */
@Data
public class AOProcessingDto implements Serializable {
    private String taskId;
    private String applicationId;
    private SubmitDetailsDto submitDetailsDto;

    private String doRemarks;
    private String riskLevel;
    private String riskLevelComments;
    private String doRecommendation;
    private String selectedAfc;
    private String hmRemarks;
    private String hmDecision;

    private String aoRemarks;
    private String reviewingDecision;
    private String lentivirusReportDate;
    private String internalInspectionReportDate;
    private String validityStartDate;
    private String validityEndDate;
    private String finalRemarks;
}
