package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.ProcessClient;
import sg.gov.moh.iais.egp.bsb.constant.ProcessContants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.process.DoScreeningDto;
import sg.gov.moh.iais.egp.bsb.dto.task.TaskListSearchResultDto;
import sg.gov.moh.iais.egp.bsb.entity.Application;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

/**
 * @author : LiRan
 * @date : 2021/8/20
 */
@Delegator(value = "DOScreeningDelegator")
@Slf4j
public class DOScreeningDelegator {
    private final ProcessClient processClient;

    @Autowired
    public DOScreeningDelegator(ProcessClient processClient) {
        this.processClient = processClient;
    }

    public void start(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_SYSTEM_CONFIG,
                AuditTrailConsts.FUNCTION_ERROR_MESSAGES_MANAGEMENT);
        HttpServletRequest request = bpc.request;
        IaisEGPHelper.clearSessionAttr(request, ProcessContants.class);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        Application application = processClient.getApplicationById("05EF1B40-E3E2-EB11-8B7D-000C293F0C88").getEntity();
        ParamUtil.setSessionAttr(request, ProcessContants.APPLICATION_INFO_ATTR, application);

    }

    public void screenedByDO(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;
        Application application = (Application)ParamUtil.getSessionAttr(request, ProcessContants.APPLICATION_INFO_ATTR);
        String facilityId = application.getFacility().getId();
        String remarks = ParamUtil.getString(request, ProcessContants.REMARKS);
        String riskLevel = ParamUtil.getString(request, ProcessContants.RISK_LEVEL);
        String commentsOnRiskLevelAssessment = ParamUtil.getString(request, ProcessContants.COMMENTS_ON_RISK_LEVEL_ASSESSMENT);
        String processingDecision = ParamUtil.getString(request, ProcessContants.PROCESSING_DECISION);
        String erpReport = ParamUtil.getString(request, ProcessContants.ERP_REPORT);
        String redTeamingReport = ParamUtil.getString(request, ProcessContants.RED_TEAMING_REPORT);
        String lentivirusReport = ParamUtil.getString(request, ProcessContants.LENTIVIRUS_REPORT);
        String internalInspectionReport = ParamUtil.getString(request, ProcessContants.INTERNAL_INSPECTION_REPORT);
        String selectedApprovedFacilityCertifier = ParamUtil.getString(request, ProcessContants.SELECTED_APPROVED_FACILITY_CERTIFIER);
        String validityStartDate = ParamUtil.getString(request, ProcessContants.VALIDITY_START_DATE);
        String validityEndDate = ParamUtil.getString(request, ProcessContants.VALIDITY_END_DATE);
        DoScreeningDto doScreeningDto = new DoScreeningDto();
        doScreeningDto.setFacilityId(facilityId);
        doScreeningDto.setRiskLevel(riskLevel);
        doScreeningDto.setRiskLevelComments(commentsOnRiskLevelAssessment);
        doScreeningDto.setErpReportDt(Formatter.parseDate(erpReport));
        doScreeningDto.setRedTeamingReportDt(Formatter.parseDate(redTeamingReport));
        doScreeningDto.setLentivirusReportDt(Formatter.parseDate(lentivirusReport));
        doScreeningDto.setInternalInspectionReportDt(Formatter.parseDate(internalInspectionReport));
        doScreeningDto.setValidityStartDt(Formatter.parseDate(validityStartDate));
        doScreeningDto.setValidityEndDt(Formatter.parseDate(validityEndDate));
        doScreeningDto.setSelectedAfc(selectedApprovedFacilityCertifier);
        ResponseDto<DoScreeningDto> doScreeningDtoResponseDto = processClient.updateFacilityByMohProcess(doScreeningDto);

    }

    public void reject(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
    }

    public void requestForInformation(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
    }


}
