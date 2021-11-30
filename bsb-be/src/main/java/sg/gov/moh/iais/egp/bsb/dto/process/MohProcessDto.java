package sg.gov.moh.iais.egp.bsb.dto.process;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * @author : LiRan
 * @date : 2021/8/23
 */
@Data
public class MohProcessDto implements Serializable {
    //DOScreening, AOScreening, HMScreening, DOProcessing, AOProcessing, HMProcessing
    private String processFlow;
    //decision final how to process
    private String processType;

    private String taskId;
    private String appId;

    //moh process data
    private String remarks;
    private String riskLevel;
    private String riskLevelComments;
    private String processingDecision;
    private String erpReportDt;
    private String redTeamingReportDt;
    private String lentivirusReportDt;
    private String internalInspectionReportDt;
    private String selectedAfc;
    private String validityStartDt;
    private String validityEndDt;
    private String finalRemarks;

    private ValidationResultDto validationResultDto;

    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("processClient", "validateMohProcessDto", new Object[]{this});
        return validationResultDto.isPass();
    }

    public String retrieveValidationResult() {
        if (this.validationResultDto == null) {
            throw new IllegalStateException("This DTO is not validated");
        }
        return this.validationResultDto.toErrorMsg();
    }

    //    ---------------------------- request -> object ----------------------------------------------

    private static final String KEY_REMARKS = "remarks";
    private static final String KEY_RISK_LEVEL = "riskLevel";
    private static final String KEY_RISK_LEVEL_COMMENTS = "riskLevelComments";
    private static final String KEY_PROCESSING_DECISION = "processingDecision";
    private static final String KEY_ERP_REPORT_DATE = "erpReportDt";
    private static final String KEY_RED_TEAMING_REPORT_DATE = "redTeamingReportDt";
    private static final String KEY_LENTIVIRUS_REPORT_DATE = "lentivirusReportDt";
    private static final String KEY_INTERAL_INSPECTION_REPORT = "internalInspectionReportDt";
    private static final String KEY_SELECTED_AFC = "selectedAfc";
    private static final String KEY_VALIDITY_START_DATE = "validityStartDt";
    private static final String KEY_VALIDITY_END_DATE = "validityEndDt";
    private static final String KEY_FINAL_REMARKS = "finalRemarks";

    public void reqObjMapping(HttpServletRequest request) {
        this.setRemarks(ParamUtil.getString(request, KEY_REMARKS));
        this.setRiskLevel(ParamUtil.getString(request, KEY_RISK_LEVEL));
        this.setRiskLevelComments(ParamUtil.getString(request, KEY_RISK_LEVEL_COMMENTS));
        this.setProcessingDecision(ParamUtil.getString(request, KEY_PROCESSING_DECISION));
        this.setErpReportDt(ParamUtil.getString(request, KEY_ERP_REPORT_DATE));
        this.setRedTeamingReportDt(ParamUtil.getString(request, KEY_RED_TEAMING_REPORT_DATE));
        this.setLentivirusReportDt(ParamUtil.getString(request, KEY_LENTIVIRUS_REPORT_DATE));
        this.setInternalInspectionReportDt(ParamUtil.getString(request, KEY_INTERAL_INSPECTION_REPORT));
        this.setSelectedAfc(ParamUtil.getString(request, KEY_SELECTED_AFC));
        this.setValidityStartDt(ParamUtil.getString(request, KEY_VALIDITY_START_DATE));
        this.setValidityEndDt(ParamUtil.getString(request, KEY_VALIDITY_END_DATE));
        this.setFinalRemarks(ParamUtil.getString(request,KEY_FINAL_REMARKS));
    }
}
