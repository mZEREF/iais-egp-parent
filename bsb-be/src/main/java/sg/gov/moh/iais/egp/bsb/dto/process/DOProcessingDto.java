package sg.gov.moh.iais.egp.bsb.dto.process;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * @author : LiRan
 * @date : 2021/12/29
 */
@Data
public class DOProcessingDto implements Serializable {
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

    private static final String KEY_REMARKS = "remarks";
    private static final String KEY_RISK_LEVEL = "riskLevel";
    private static final String KEY_RISK_LEVEL_COMMENTS = "riskLevelComments";
    private static final String KEY_PROCESSING_DECISION = "processingDecision";
    private static final String KEY_ERP_REPORT_DATE = "erpReportDate";
    private static final String KEY_RED_TEAMING_REPORT_DATE = "redTeamingReportDate";
    private static final String KEY_LENTIVIRUS_REPORT_DATE = "lentivirusReportDate";
    private static final String KEY_INTERAL_INSPECTION_REPORT = "internalInspectionReportDate";
    private static final String KEY_VALIDITY_START_DATE = "validityStartDate";
    private static final String KEY_VALIDITY_END_DATE = "validityEndDate";
    private static final String KEY_FINAL_REMARKS = "finalRemarks";

    public void reqObjMapping(HttpServletRequest request) {
        this.setRemarks(ParamUtil.getString(request, KEY_REMARKS));
        this.setRiskLevel(ParamUtil.getString(request, KEY_RISK_LEVEL));
        this.setRiskLevelComments(ParamUtil.getString(request, KEY_RISK_LEVEL_COMMENTS));
        this.setProcessingDecision(ParamUtil.getString(request, KEY_PROCESSING_DECISION));
        this.setLentivirusReportDate(ParamUtil.getString(request, KEY_LENTIVIRUS_REPORT_DATE));
        this.setInternalInspectionReportDate(ParamUtil.getString(request, KEY_INTERAL_INSPECTION_REPORT));
        this.setValidityStartDate(ParamUtil.getString(request, KEY_VALIDITY_START_DATE));
        this.setValidityEndDate(ParamUtil.getString(request, KEY_VALIDITY_END_DATE));
        this.setFinalRemarks(ParamUtil.getString(request, KEY_FINAL_REMARKS));
    }
}
