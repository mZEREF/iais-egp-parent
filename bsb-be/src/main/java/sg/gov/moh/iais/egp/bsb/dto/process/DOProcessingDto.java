package sg.gov.moh.iais.egp.bsb.dto.process;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : LiRan
 * @date : 2021/12/29
 */
@Data
public class DOProcessingDto implements Serializable {
    private String taskId;
    private String applicationId;
    private SubmitDetailsDto submitDetailsDto;

    private String selectedAfc;

    private String remarks;
    private String riskLevel;
    private String riskLevelComments;
    private String processingDecision;
    //don't do now
    private String selectApprovingOfficer;
    private String lentivirusReportDate;
    private String internalInspectionReportDate;
    private String validityStartDate;
    private String validityEndDate;
    private String finalRemarks;
}
