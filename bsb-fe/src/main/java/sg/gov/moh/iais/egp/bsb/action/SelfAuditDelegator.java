package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.AuditClient;
import sg.gov.moh.iais.egp.bsb.client.DocClient;
import sg.gov.moh.iais.egp.bsb.constant.AuditConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.FacilitySubmitSelfAuditDto;
import sg.gov.moh.iais.egp.bsb.entity.*;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_DO;


/**
 * @author Zhu Tangtang
 */
@Slf4j
@Delegator(value = "selfAuditDelegator")
public class SelfAuditDelegator {

    private final AuditClient auditClient;
    private final DocClient docClient;

    @Autowired
    public SelfAuditDelegator(AuditClient auditClient,DocClient docClient){
        this.auditClient = auditClient;
        this.docClient = docClient;
    }

    public void start(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction(MODULE_AUDIT, FUNCTION_AUDIT);
        HttpServletRequest request = bpc.request;
        IaisEGPHelper.clearSessionAttr(request, AuditConstants.class);
        ParamUtil.setSessionAttr(request, PARAM_AUDIT_SEARCH, null);
    }

    public void prepareFacilitySelfAuditData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        //get needed data by appId(contain:audit,auditApp,Facility)
        String auditId = ParamUtil.getMaskedString(request, AUDIT_ID);
        ResponseDto<FacilitySubmitSelfAuditDto> responseDto = auditClient.getSelfAuditDataByAuditId(auditId);
        if (responseDto.ok()) {
            FacilitySubmitSelfAuditDto dto = responseDto.getEntity();
            ParamUtil.setSessionAttr(request, SELF_AUDIT_DATA, dto);
        } else {
            log.warn("get audit API doesn't return ok, the response is {}", responseDto);
            ParamUtil.setRequestAttr(request, SELF_AUDIT_DATA, new FacilitySubmitSelfAuditDto());
        }
    }

    public void submitSelfAuditReport(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacilitySubmitSelfAuditDto dto = (FacilitySubmitSelfAuditDto)ParamUtil.getSessionAttr(request, SELF_AUDIT_DATA);
        String scenarioCategory = ParamUtil.getRequestString(request,PARAM_SCENARIO_CATEGORY);
        dto.setScenarioCategory(scenarioCategory);
        dto.setAuditStatus(PARAM_AUDIT_STATUS_PENDING_DO);
        dto.setAppStatus(APP_STATUS_PEND_DO);
        dto.setAuditAppStatus(PARAM_AUDIT_STATUS_PENDING_DO);
        auditClient.facilitySubmitSelfAudit(dto);
    }
}
