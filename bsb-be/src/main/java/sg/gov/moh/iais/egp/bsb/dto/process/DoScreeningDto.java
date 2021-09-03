package sg.gov.moh.iais.egp.bsb.dto.process;

import lombok.Data;

import java.util.Date;

/**
 * @author : LiRan
 * @date : 2021/8/23
 */
@Data
public class DoScreeningDto {

    private String facilityId;

    private String riskLevel;

    private String riskLevelComments;

    private Date erpReportDt;

    private Date redTeamingReportDt;

    private Date lentivirusReportDt;

    private Date internalInspectionReportDt;

    private Date validityStartDt;

    private Date validityEndDt;

    private String selectedAfc;

    private String applicationId;

    private String applicationNo;

    private String status;

    private String appStatus;

    private String remarks;

    private String processDecision;

    private String actionBy;

    private String reason;

    private String finalRemarks;
}
