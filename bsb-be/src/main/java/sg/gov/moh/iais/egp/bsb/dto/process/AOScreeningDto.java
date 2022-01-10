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
public class AOScreeningDto implements Serializable {
    private String doRemarks;
    private String riskLevel;
    private String riskLevelComments;
    private String doProcessingDecision;

    private String aoRemarks;
    private String reviewingDecision;
    private String erpReportDate;
    private String redTeamingReportDate;
    private String lentivirusReportDate;
    private String internalInspectionReportDate;
    private String selectedAfc;
    private String validityStartDate;
    private String validityEndDate;
    private String finalRemarks;

    private static final String KEY_AO_REMARKS = "aoRemarks";
    private static final String KEY_REVIEWING_DECISION = "reviewingDecision";
    private static final String KEY_ERP_REPORT_DATE = "erpReportDate";
    private static final String KEY_RED_TEAMING_REPORT_DATE = "redTeamingReportDate";
    private static final String KEY_LENTIVIRUS_REPORT_DATE = "lentivirusReportDate";
    private static final String KEY_INTERAL_INSPECTION_REPORT = "internalInspectionReportDate";
    private static final String KEY_SELECTED_AFC = "selectedAfc";
    private static final String KEY_VALIDITY_START_DATE = "validityStartDate";
    private static final String KEY_VALIDITY_END_DATE = "validityEndDate";
    private static final String KEY_FINAL_REMARKS = "finalRemarks";

    public void reqObjMapping(HttpServletRequest request) {
        this.setAoRemarks(ParamUtil.getString(request, KEY_AO_REMARKS));
        this.setReviewingDecision(ParamUtil.getString(request, KEY_REVIEWING_DECISION));
        this.setErpReportDate(ParamUtil.getString(request, KEY_ERP_REPORT_DATE));
        this.setRedTeamingReportDate(ParamUtil.getString(request, KEY_RED_TEAMING_REPORT_DATE));
        this.setLentivirusReportDate(ParamUtil.getString(request, KEY_LENTIVIRUS_REPORT_DATE));
        this.setInternalInspectionReportDate(ParamUtil.getString(request, KEY_INTERAL_INSPECTION_REPORT));
        this.setSelectedAfc(ParamUtil.getString(request, KEY_SELECTED_AFC));
        this.setValidityStartDate(ParamUtil.getString(request, KEY_VALIDITY_START_DATE));
        this.setValidityEndDate(ParamUtil.getString(request, KEY_VALIDITY_END_DATE));
        this.setFinalRemarks(ParamUtil.getString(request, KEY_FINAL_REMARKS));
    }
}
