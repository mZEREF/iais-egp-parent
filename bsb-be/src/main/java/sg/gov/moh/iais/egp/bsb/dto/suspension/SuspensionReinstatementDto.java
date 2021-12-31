package sg.gov.moh.iais.egp.bsb.dto.suspension;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * @author tangtang
 */
@Data
public class SuspensionReinstatementDto implements Serializable {
    //search info
    private String applicationNo;
    private String applicationId;
    private String taskId;
    private String approvalId;
    private String approvalNo;
    private String approvalStatus;
    private String facName;
    private String facAddress;
    private String facClassification;
    private String activityType;
    private String applicationStatus;
    //
    private String module;
    //DO
    private String suspensionType;
    private String startDate;
    private String endDate;
    private String additionalComments;
    private String suspensionReason;
    private String doRemarks;
    private String effectiveDate;
    private String reinstatementReason;
    //AO
    private String aoRemarks;
    private String aoDecision;
    //HM
    private String hmRemarks;
    private String hmDecision;

    private static final String KEY_DO_REMARKS = "doRemarks";
    private static final String KEY_DO_DECISION = "doDecision";
    private static final String KEY_AO_REMARKS = "aoRemarks";
    private static final String KEY_AO_DECISION = "aoDecision";
    private static final String KEY_SUSPENSION_TYPE = "suspensionType";
    private static final String KEY_START_DATE = "startDate";
    private static final String KEY_END_DATE = "endDate";
    private static final String KEY_ADDITIONAL_COMMENTS = "additionalComments";
    private static final String KEY_SUSPENSION_REASON = "suspensionReason";
    private static final String KEY_EFFECTIVE_DATE = "effectiveDate";
    private static final String KEY_REINSTATEMENT_REASON = "reinstatementReason";
    private static final String KEY_HM_DECISION = "hmDecision";

    public void reqObjMapping(HttpServletRequest request) {
        this.setDoRemarks(ParamUtil.getString(request, KEY_DO_REMARKS));
        this.setSuspensionType(ParamUtil.getString(request,KEY_SUSPENSION_TYPE));
        this.setSuspensionReason(ParamUtil.getString(request,KEY_SUSPENSION_REASON));
        this.setStartDate(ParamUtil.getString(request,KEY_START_DATE));
        this.setEndDate(ParamUtil.getString(request,KEY_END_DATE));
        this.setAdditionalComments(ParamUtil.getString(request,KEY_ADDITIONAL_COMMENTS));
        this.setEffectiveDate(ParamUtil.getString(request,KEY_EFFECTIVE_DATE));
        this.setReinstatementReason(ParamUtil.getString(request,KEY_REINSTATEMENT_REASON));
        this.setHmDecision(ParamUtil.getString(request,KEY_HM_DECISION));
    }
}
