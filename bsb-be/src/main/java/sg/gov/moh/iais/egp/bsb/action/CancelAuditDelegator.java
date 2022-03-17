package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.AuditClientBE;
import sg.gov.moh.iais.egp.bsb.client.BiosafetyEnquiryClient;
import sg.gov.moh.iais.egp.bsb.constant.AuditConstants;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.*;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;

import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.*;

/**
 * @author Zhu Tangtang
 */
@Slf4j
@Delegator(value = "cancelAuditDelegator")
public class CancelAuditDelegator {
    private static final String ACTION_TYPE_SUBMIT = "doSubmit";
    private static final String ACTION_TYPE_PREPARE = "prepare";
    private static final String ACTION_TYPE_APPROVE = "doApprove";
    private static final String ACTION_TYPE_REJECT = "doReject";
    private static final String ACTION_TYPE = "action_type";

    private final AuditClientBE auditClientBE;
    private final BiosafetyEnquiryClient biosafetyEnquiryClient;

    public CancelAuditDelegator(AuditClientBE auditClientBE, BiosafetyEnquiryClient biosafetyEnquiryClient) {
        this.auditClientBE = auditClientBE;
        this.biosafetyEnquiryClient = biosafetyEnquiryClient;
    }

    public void start(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction(MODULE_AUDIT, FUNCTION_AUDIT);
        HttpServletRequest request = bpc.request;
        IaisEGPHelper.clearSessionAttr(request, AuditConstants.class);
        ParamUtil.setSessionAttr(request, PARAM_AUDIT_SEARCH, null);
        ParamUtil.setSessionAttr(request,KEY_CANCEL_AUDIT,null);
        ParamUtil.setSessionAttr(request, KEY_OFFICER_PROCESS_DATA, null);
        ParamUtil.setSessionAttr(request,PARAM_CANCEL_REASON,null);
    }

    public void prepareCancelAuditListData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, PARAM_YEAR, null);
        ParamUtil.setSessionAttr(request, KEY_AUDIT_DATA_LIST, null);
        selectOption(request);
        // get search DTO
        AuditQueryDto searchDto = getSearchDto(request);
        searchDto.setFrom(PARAM_CANCEL_AUDIT);
        ParamUtil.setSessionAttr(request, PARAM_AUDIT_SEARCH, searchDto);
        // call API to get searched data
        ResponseDto<FacilityQueryResultDto> searchResult = auditClientBE.queryFacility(searchDto);

        if (searchResult.ok()) {
            ParamUtil.setRequestAttr(request, KEY_AUDIT_PAGE_INFO, searchResult.getEntity().getPageInfo());
            List<FacilityQueryResultDto.FacInfo> facInfos = searchResult.getEntity().getTasks();
            ParamUtil.setSessionAttr(request, KEY_AUDIT_DATA_LIST, (Serializable) facInfos);
        } else {
            log.warn("get audit API doesn't return ok, the response is {}", searchResult);
            ParamUtil.setRequestAttr(request, KEY_AUDIT_PAGE_INFO, PageInfo.emptyPageInfo(searchDto));
            ParamUtil.setRequestAttr(request, KEY_AUDIT_DATA_LIST, new ArrayList<>());
        }

        Calendar cd = Calendar.getInstance();
        int year = cd.get(Calendar.YEAR);
        ParamUtil.setSessionAttr(request, PARAM_YEAR, year);
    }

    public void doSearch(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, PARAM_AUDIT_SEARCH, null);
        AuditQueryDto searchDto = getSearchDto(request);
        searchDto.clearAllFields();
        String facilityName = ParamUtil.getString(request, PARAM_FACILITY_NAME);
        String facilityClassification = ParamUtil.getString(request, PARAM_FACILITY_CLASSIFICATION);
        String facilityType = ParamUtil.getString(request, PARAM_FACILITY_TYPE);
        String auditType = ParamUtil.getString(request, PARAM_AUDIT_TYPE);

        searchDto.setFacilityName(facilityName);
        searchDto.setFacilityClassification(facilityClassification);
        searchDto.setActiveType(facilityType);
        searchDto.setAuditType(auditType);
        ParamUtil.setSessionAttr(request, PARAM_AUDIT_SEARCH, searchDto);
    }

    public void prepareDOCancelAuditData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, AUDIT_LIST, null);
        String[] auditIds = ParamUtil.getMaskedStrings(request, AUDIT_ID);
        if (auditIds==null){
            auditIds = (String[])ParamUtil.getSessionAttr(request,"auditIds");
        }
        CancelAuditDto dto = getCancelAuditDto(request);
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request,ValidationConstants.KEY_SHOW_ERROR_SWITCH);
        if(Boolean.TRUE.equals(needShowError)){
            ParamUtil.setRequestAttr(request,ValidationConstants.KEY_VALIDATION_ERRORS,dto.retrieveValidationResult());
        }
        List<FacilityQueryResultDto.FacInfo> facInfos = (List<FacilityQueryResultDto.FacInfo>)ParamUtil.getSessionAttr(request, KEY_AUDIT_DATA_LIST);
        Map<String, FacilityQueryResultDto.FacInfo> facInfoMap = Maps.newHashMapWithExpectedSize(facInfos.size());
        for (FacilityQueryResultDto.FacInfo facInfo : facInfos) {
            facInfoMap.put(facInfo.getAuditId(), facInfo);
        }
        facInfos.clear();
        for (String facId : auditIds) {
            FacilityQueryResultDto.FacInfo facInfo = facInfoMap.get(facId);
            if (facInfo != null) {
                facInfos.add(facInfo);
            }
        }
        //fill the known data
        if (CollectionUtils.isEmpty(dto.getAuditDtos())){
            List<OfficerProcessAuditDto> auditDtos = new ArrayList<>(0);
            for (FacilityQueryResultDto.FacInfo facInfo : facInfos) {
                OfficerProcessAuditDto processAuditDto = new OfficerProcessAuditDto();
                processAuditDto.setAuditType(facInfo.getAuditType());
                processAuditDto.setFacName(facInfo.getFacName());
                processAuditDto.setActivityType(facInfo.getActivityType());
                processAuditDto.setAuditDate(facInfo.getAuditDate());
                processAuditDto.setAuditId(facInfo.getAuditId());
                auditDtos.add(processAuditDto);
            }
            dto.setAuditDtos(auditDtos);
        }
        ParamUtil.setSessionAttr(request,"auditIds",auditIds);
        ParamUtil.setSessionAttr(request,KEY_CANCEL_AUDIT,dto);
    }

    public void doValidate(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String cancelReason = ParamUtil.getRequestString(request, PARAM_CANCEL_REASON);
        CancelAuditDto cancelAuditDto = getCancelAuditDto(request);
        List<OfficerProcessAuditDto> auditDtos = cancelAuditDto.getAuditDtos();
        if (!CollectionUtils.isEmpty(auditDtos)) {
            for (OfficerProcessAuditDto processAuditDto : auditDtos) {
                processAuditDto.setCancelReason(cancelReason);
                processAuditDto.setAuditAppStatus(PARAM_AUDIT_STATUS_PENDING_AO);
                processAuditDto.setAppStatus(APP_STATUS_PEND_AO);
                processAuditDto.setActionBy(loginContext.getUserName());
                processAuditDto.setModule("doCancel");
            }
        }
        doValidateData(cancelAuditDto,request);
        ParamUtil.setSessionAttr(request,KEY_CANCEL_AUDIT,cancelAuditDto);
        ParamUtil.setSessionAttr(request,PARAM_CANCEL_REASON,cancelReason);
    }

    public void doSubmitCancelAudit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        CancelAuditDto dto = getCancelAuditDto(request);
        auditClientBE.doCancelAudit(dto);
        ParamUtil.setRequestAttr(request,BACK_URL,BACK_URL_CANCEL_LIST);
    }

    public void prepareAOCancelAuditData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        OfficerProcessAuditDto dto = getProcessDto(request);
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, ValidationConstants.KEY_SHOW_ERROR_SWITCH);
        if(Boolean.TRUE.equals(needShowError)){
            ParamUtil.setRequestAttr(request,ValidationConstants.KEY_VALIDATION_ERRORS,dto.retrieveValidationResult());
        }
        //Check if it is the first time you have entered this method
        if (!StringUtils.hasLength(dto.getTaskId())) {
            //get needed data by appId(contain:audit,auditApp,Facility)
            String maskedAppId = ParamUtil.getString(request, KEY_APP_ID);
            String maskedTaskId = ParamUtil.getString(request, KEY_TASK_ID);
            if (log.isInfoEnabled()) {
                log.info("masked application id: [{}]", LogUtil.escapeCrlf(maskedAppId));
                log.info("masked task id: [{}]", LogUtil.escapeCrlf(maskedTaskId));
            }
            String appId = MaskUtil.unMaskValue("id", maskedAppId);
            String taskId = MaskUtil.unMaskValue("id", maskedTaskId);
            if (appId == null || appId.equals(maskedAppId)) {
                throw new IaisRuntimeException("Invalid masked application ID");
            }
            if (taskId == null || taskId.equals(maskedTaskId)) {
                throw new IaisRuntimeException("Invalid masked task ID");
            }

            ResponseDto<OfficerProcessAuditDto> responseDto = auditClientBE.getOfficerProcessDataByAppId(appId);
            if (responseDto.ok()) {
                dto = responseDto.getEntity();
                dto.setTaskId(taskId);
                ParamUtil.setSessionAttr(request, KEY_OFFICER_PROCESS_DATA, dto);
            } else {
                log.warn("get audit API doesn't return ok, the response is {}", responseDto);
                ParamUtil.setRequestAttr(request, KEY_OFFICER_PROCESS_DATA, new OfficerProcessAuditDto());
            }
        }
        ParamUtil.setSessionAttr(request, KEY_OFFICER_PROCESS_DATA, dto);
    }

    public void aoValidate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String aoDecision = ParamUtil.getRequestString(request,PARAM_AO_DECISION);
        OfficerProcessAuditDto dto = getProcessDto(request);
        //
        dto.setActionBy(loginContext.getUserName());
        dto.setAoDecision(aoDecision);
        dto.setModule("aoCancel");
        aoValidateData(dto,request);
        ParamUtil.setSessionAttr(request, KEY_OFFICER_PROCESS_DATA, dto);
    }

    public void aoApproveCancel(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        OfficerProcessAuditDto dto = getProcessDto(request);
        dto.setAuditStatus(PARAM_AUDIT_STATUS_CANCELLED);
        dto.setAuditAppStatus(PARAM_AUDIT_STATUS_CANCELLED);
        dto.setAppStatus(APP_STATUS_APPROVED);
        auditClientBE.officerCancelAudit(dto);
        //
        ParamUtil.setRequestAttr(request,BACK_URL,BACK_URL_TASK_LIST);
    }

    public void aoRejectCancel(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        OfficerProcessAuditDto dto = getProcessDto(request);
        dto.setAuditAppStatus(PARAM_AUDIT_STATUS_CANCELLED);
        dto.setAppStatus(APP_STATUS_REJECTED);
        auditClientBE.officerCancelAudit(dto);
        //
        ParamUtil.setRequestAttr(request,BACK_URL,BACK_URL_TASK_LIST);
    }

    public void page(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AuditQueryDto searchDto = getSearchDto(request);
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        switch (actionValue) {
            case "changeSize":
                int pageSize = ParamUtil.getInt(request, KEY_PAGE_SIZE);
                searchDto.setPage(0);
                searchDto.setSize(pageSize);
                break;
            case "changePage":
                int pageNo = ParamUtil.getInt(request, KEY_PAGE_NO);
                searchDto.setPage(pageNo - 1);
                break;
            default:
                log.warn("page, action_value is invalid: {}", actionValue);
                break;
        }
        ParamUtil.setSessionAttr(request, PARAM_AUDIT_SEARCH, searchDto);
    }

    public void sort(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AuditQueryDto searchDto = getSearchDto(request);
        String field = ParamUtil.getString(request, KEY_ACTION_VALUE);
        String sortType = ParamUtil.getString(request, KEY_ACTION_ADDT);
        searchDto.changeSort(field, sortType);
        ParamUtil.setSessionAttr(request, PARAM_AUDIT_SEARCH, searchDto);
    }

    private AuditQueryDto getSearchDto(HttpServletRequest request) {
        AuditQueryDto searchDto = (AuditQueryDto) ParamUtil.getSessionAttr(request, PARAM_AUDIT_SEARCH);
        return searchDto == null ? getDefaultSearchDto() : searchDto;
    }

    private AuditQueryDto getDefaultSearchDto() {
        AuditQueryDto dto = new AuditQueryDto();
        dto.clearAllFields();
        dto.defaultPaging();
        return dto;
    }

    private CancelAuditDto getCancelAuditDto(HttpServletRequest request) {
        CancelAuditDto auditDto = (CancelAuditDto) ParamUtil.getSessionAttr(request, KEY_CANCEL_AUDIT);
        return auditDto == null ? getDefaultSaveAuditDto() : auditDto;
    }

    private CancelAuditDto getDefaultSaveAuditDto() {
        return new CancelAuditDto();
    }

    private OfficerProcessAuditDto getProcessDto(HttpServletRequest request) {
        OfficerProcessAuditDto searchDto = (OfficerProcessAuditDto) ParamUtil.getSessionAttr(request, KEY_OFFICER_PROCESS_DATA);
        return searchDto == null ? getDefaultProcessDto() : searchDto;
    }

    private OfficerProcessAuditDto getDefaultProcessDto() {
        return new OfficerProcessAuditDto();
    }

    public void selectOption(HttpServletRequest request) {
        List<String> facNames = biosafetyEnquiryClient.queryDistinctFN().getEntity();
        List<SelectOption> selectModel = new ArrayList<>(facNames.size());
        for (String facName : facNames) {
            selectModel.add(new SelectOption(facName, facName));
        }
        ParamUtil.setRequestAttr(request, PARAM_FACILITY_NAME, selectModel);
    }

    private void doValidateData(CancelAuditDto dto, HttpServletRequest request){
        //validation
        String actionType;
        ValidationResultDto validationResultDto = auditClientBE.validateDOCancelAudit(dto);
        if (!validationResultDto.isPass()){
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            actionType = ACTION_TYPE_PREPARE;
        }else {
            actionType = ACTION_TYPE_SUBMIT;
        }
        ParamUtil.setRequestAttr(request, ACTION_TYPE, actionType);
    }

    private void aoValidateData(OfficerProcessAuditDto dto, HttpServletRequest request){
        //validation
        String actionType = null;
        ValidationResultDto validationResultDto = auditClientBE.validateOfficerAuditDt(dto);
        if (!validationResultDto.isPass()){
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            actionType = ACTION_TYPE_PREPARE;
        }else {
            if (dto.getAoDecision().equals("MOHPRO003")){
                actionType = ACTION_TYPE_REJECT;
            } else if (dto.getAoDecision().equals("MOHPRO007")){
                actionType = ACTION_TYPE_APPROVE;
            }
        }
        ParamUtil.setRequestAttr(request, ACTION_TYPE, actionType);
    }
}
