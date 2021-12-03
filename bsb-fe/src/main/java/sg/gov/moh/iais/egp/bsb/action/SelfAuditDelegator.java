package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.AuditClient;
import sg.gov.moh.iais.egp.bsb.constant.AuditConstants;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.FacilitySubmitSelfAuditDto;
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

    @Autowired
    public SelfAuditDelegator(AuditClient auditClient){
        this.auditClient = auditClient;
    }

    public void start(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction(MODULE_AUDIT, FUNCTION_AUDIT);
        HttpServletRequest request = bpc.request;
        IaisEGPHelper.clearSessionAttr(request, AuditConstants.class);
        ParamUtil.setSessionAttr(request, PARAM_AUDIT_SEARCH, null);
        ParamUtil.setSessionAttr(request, SELF_AUDIT_DATA, null);
    }

    public void prepareFacilitySelfAuditData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacilitySubmitSelfAuditDto dto = getAuditDto(request);
        FacilitySubmitSelfAuditDto auditDto = getAuditDto(request);
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request,ValidationConstants.KEY_SHOW_ERROR_SWITCH);
        if(Boolean.TRUE.equals(needShowError)){
            ParamUtil.setRequestAttr(request,ValidationConstants.KEY_VALIDATION_ERRORS,auditDto.retrieveValidationResult());
        }
        if (!StringUtils.hasLength(dto.getAuditId())) {
            //get needed data by appId(contain:audit,auditApp,Facility)
            String auditId = ParamUtil.getMaskedString(request, AUDIT_ID);
            ResponseDto<FacilitySubmitSelfAuditDto> responseDto = auditClient.getSelfAuditDataByAuditId(auditId);
            if (responseDto.ok()) {
                dto = responseDto.getEntity();
                ParamUtil.setSessionAttr(request, SELF_AUDIT_DATA, dto);
            } else {
                log.warn("get audit API doesn't return ok, the response is {}", responseDto);
                ParamUtil.setRequestAttr(request, SELF_AUDIT_DATA, new FacilitySubmitSelfAuditDto());
            }
        }
        ParamUtil.setSessionAttr(request, SELF_AUDIT_DATA, dto);
    }

    public void preConfirm(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacilitySubmitSelfAuditDto dto = getAuditDto(request);
        String scenarioCategory = ParamUtil.getRequestString(request,PARAM_SCENARIO_CATEGORY);
        dto.setScenarioCategory(scenarioCategory);
        dto.setModule("facSelfAudit");
        doValidation(dto,request);
        ParamUtil.setSessionAttr(request, SELF_AUDIT_DATA, dto);
    }

    public void submitSelfAuditReport(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacilitySubmitSelfAuditDto dto = getAuditDto(request);
        dto.setAuditStatus(PARAM_AUDIT_STATUS_PENDING_DO);
        dto.setAppStatus(APP_STATUS_PEND_DO);
        dto.setAuditAppStatus(PARAM_AUDIT_STATUS_PENDING_DO);
        auditClient.facilitySubmitSelfAudit(dto);
    }

    private FacilitySubmitSelfAuditDto getAuditDto(HttpServletRequest request) {
        FacilitySubmitSelfAuditDto auditDto = (FacilitySubmitSelfAuditDto) ParamUtil.getSessionAttr(request, AuditConstants.SELF_AUDIT_DATA);
        return auditDto == null ? getDefaultAuditDto() : auditDto;
    }

    private FacilitySubmitSelfAuditDto getDefaultAuditDto() {
        return new FacilitySubmitSelfAuditDto();
    }

    /**
     * just a method to do simple valid,maybe update in the future
     * doValidation
     * */
    private void doValidation(FacilitySubmitSelfAuditDto dto,HttpServletRequest request){
        if(dto.doValidation()){
            ParamUtil.setRequestAttr(request, ValidationConstants.IS_VALID,ValidationConstants.YES);
        }else{
            ParamUtil.setRequestAttr(request, ValidationConstants.IS_VALID,ValidationConstants.NO);
            ParamUtil.setRequestAttr(request,ValidationConstants.KEY_SHOW_ERROR_SWITCH,Boolean.TRUE);
        }
    }
}
