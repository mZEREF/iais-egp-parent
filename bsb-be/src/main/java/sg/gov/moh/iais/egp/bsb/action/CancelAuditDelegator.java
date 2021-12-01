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
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.client.AuditClientBE;
import sg.gov.moh.iais.egp.bsb.client.BiosafetyEnquiryClient;
import sg.gov.moh.iais.egp.bsb.constant.AuditConstants;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.AuditQueryDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.FacilityQueryResultDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.OfficerProcessAuditDto;
import sg.gov.moh.iais.egp.bsb.entity.*;
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
        List<FacilityQueryResultDto.FacInfo> facInfos = (List<FacilityQueryResultDto.FacInfo>)ParamUtil.getSessionAttr(request, KEY_AUDIT_DATA_LIST);
        Map<String, FacilityQueryResultDto.FacInfo> facInfoMap = new HashMap<>(facInfos.size());
        for (FacilityQueryResultDto.FacInfo facInfo : facInfos) {
            facInfoMap.put(facInfo.getAuditId(), facInfo);
        }
        //
        facInfos.clear();
        for (String auditId : auditIds) {
            if (facInfoMap.containsKey(auditId)) {
                facInfos.add(facInfoMap.get(auditId));
            }
        }
        ParamUtil.setSessionAttr(request, AUDIT_LIST, (Serializable) facInfos);
    }

    public void doSubmitCancelAudit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<FacilityQueryResultDto.FacInfo> facInfos = (List<FacilityQueryResultDto.FacInfo>) ParamUtil.getSessionAttr(request, AUDIT_LIST);
        OfficerProcessAuditDto dto = new OfficerProcessAuditDto();
        String cancelReason = ParamUtil.getRequestString(request, PARAM_REASON);
        for (FacilityQueryResultDto.FacInfo facInfo : facInfos) {
            dto.setAuditId(facInfo.getAuditId());
            dto.setCancelReason(cancelReason);
            dto.setAuditAppStatus(PARAM_AUDIT_STATUS_PENDING_AO);
            dto.setAppStatus(APP_STATUS_PEND_AO);
            dto.setActionBy(loginContext.getUserName());
            auditClientBE.officerCancelAudit(dto);
        }
        ParamUtil.setRequestAttr(request,BACK_URL,BACK_URL_CANCEL_LIST);
    }

    public void prepareAOCancelAuditData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
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
            OfficerProcessAuditDto dto = responseDto.getEntity();
            dto.setTaskId(taskId);
            ParamUtil.setSessionAttr(request, KEY_OFFICER_PROCESS_DATA, dto);
        } else {
            log.warn("get audit API doesn't return ok, the response is {}", responseDto);
            ParamUtil.setRequestAttr(request, KEY_OFFICER_PROCESS_DATA, new OfficerProcessAuditDto());
        }
    }

    public void aoApproveCancel(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        OfficerProcessAuditDto dto = (OfficerProcessAuditDto)ParamUtil.getSessionAttr(request, KEY_OFFICER_PROCESS_DATA);
        dto.setAuditStatus(PARAM_AUDIT_STATUS_CANCELLED);
        dto.setAuditAppStatus(PARAM_AUDIT_STATUS_CANCELLED);
        dto.setAppStatus(APP_STATUS_APPROVED);
        dto.setActionBy(loginContext.getUserName());
        auditClientBE.officerCancelAudit(dto);
        //
        ParamUtil.setRequestAttr(request,BACK_URL,BACK_URL_TASK_LIST);
    }

    public void aoRejectCancel(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        OfficerProcessAuditDto dto = (OfficerProcessAuditDto)ParamUtil.getSessionAttr(request, KEY_OFFICER_PROCESS_DATA);
        dto.setAuditAppStatus(PARAM_AUDIT_STATUS_CANCELLED);
        dto.setAppStatus(APP_STATUS_REJECTED);
        dto.setActionBy(loginContext.getUserName());
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

    public void selectOption(HttpServletRequest request) {
        List<String> facNames = biosafetyEnquiryClient.queryDistinctFN().getEntity();
        List<SelectOption> selectModel = new ArrayList<>();
        for (String facName : facNames) {
            selectModel.add(new SelectOption(facName, facName));
        }
        ParamUtil.setRequestAttr(request, PARAM_FACILITY_NAME, selectModel);
    }

}
