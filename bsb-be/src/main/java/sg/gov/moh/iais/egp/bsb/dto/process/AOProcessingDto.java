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
public class AOProcessingDto implements Serializable {
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

    private static final String KEY_AO_REMARKS = "aoRemarks";
    private static final String KEY_REVIEWING_DECISION = "reviewingDecision";
    private static final String KEY_LENTIVIRUS_REPORT_DATE = "lentivirusReportDate";
    private static final String KEY_INTERAL_INSPECTION_REPORT = "internalInspectionReportDate";
    private static final String KEY_VALIDITY_START_DATE = "validityStartDate";
    private static final String KEY_VALIDITY_END_DATE = "validityEndDate";
    private static final String KEY_FINAL_REMARKS = "finalRemarks";

    public void reqObjMapping(HttpServletRequest request) {
        this.setAoRemarks(ParamUtil.getString(request, KEY_AO_REMARKS));
        this.setReviewingDecision(ParamUtil.getString(request, KEY_REVIEWING_DECISION));
        this.setLentivirusReportDate(ParamUtil.getString(request, KEY_LENTIVIRUS_REPORT_DATE));
        this.setInternalInspectionReportDate(ParamUtil.getString(request, KEY_INTERAL_INSPECTION_REPORT));
        this.setValidityStartDate(ParamUtil.getString(request, KEY_VALIDITY_START_DATE));
        this.setValidityEndDate(ParamUtil.getString(request, KEY_VALIDITY_END_DATE));
        this.setFinalRemarks(ParamUtil.getString(request, KEY_FINAL_REMARKS));
    }
}
