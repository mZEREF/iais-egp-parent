package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.AuditClientBE;
import sg.gov.moh.iais.egp.bsb.client.BiosafetyEnquiryClient;
import sg.gov.moh.iais.egp.bsb.constant.AuditConstants;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.AuditQueryDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.FacilityQueryResultDto;
import sg.gov.moh.iais.egp.bsb.entity.*;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhu Tangtang
 */
@Slf4j
@Delegator(value = "cancelAuditDelegator")
public class CancelAuditDelegator {

    @Autowired
    private AuditClientBE auditClientBE;

    @Autowired
    private BiosafetyEnquiryClient biosafetyEnquiryClient;

    /**
     * StartStep: startStep
     *
     * @param bpc
     * @throws IllegalAccessException
     */
    public void start(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction(AuditConstants.MODULE_AUDIT, AuditConstants.FUNCTION_AUDIT);
        HttpServletRequest request = bpc.request;
        IaisEGPHelper.clearSessionAttr(request, AuditConstants.class);
        ParamUtil.setSessionAttr(request, AuditConstants.PARAM_AUDIT_SEARCH, null);
    }

    /**
     * OngoingAuditList
     * AutoStep: prepareData
     *
     * @param bpc
     */
    public void prepareCancelAuditListData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        selectOption(request);
        // get search DTO
        AuditQueryDto searchDto = getSearchDto(request);
        searchDto.setFrom(AuditConstants.PARAM_CANCEL_AUDIT);
        ParamUtil.setSessionAttr(request, AuditConstants.PARAM_AUDIT_SEARCH, searchDto);
        // call API to get searched data
        ResponseDto<FacilityQueryResultDto> searchResult = auditClientBE.queryFacility(searchDto);

        if (searchResult.ok()) {
            ParamUtil.setRequestAttr(request, AuditConstants.KEY_AUDIT_PAGE_INFO, searchResult.getEntity().getPageInfo());
            List<FacilityActivity> audits = searchResult.getEntity().getTasks();
            ParamUtil.setRequestAttr(request, AuditConstants.KEY_AUDIT_DATA_LIST, audits);
        } else {
            log.warn("get audit API doesn't return ok, the response is {}", searchResult);
            ParamUtil.setRequestAttr(request, AuditConstants.KEY_AUDIT_PAGE_INFO, PageInfo.emptyPageInfo(searchDto));
            ParamUtil.setRequestAttr(request, AuditConstants.KEY_AUDIT_DATA_LIST, new ArrayList<>());
        }
    }

    /**
     * AutoStep: doSearch
     *
     * @param bpc
     */
    public void doSearch(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, AuditConstants.PARAM_AUDIT_SEARCH, null);
        AuditQueryDto searchDto = getSearchDto(request);
        searchDto.clearAllFields();
        String facilityName = ParamUtil.getString(request, AuditConstants.PARAM_FACILITY_NAME);
        String facilityClassification = ParamUtil.getString(request, AuditConstants.PARAM_FACILITY_CLASSIFICATION);
        String facilityType = ParamUtil.getString(request, AuditConstants.PARAM_FACILITY_TYPE);
        String auditType = ParamUtil.getString(request, AuditConstants.PARAM_AUDIT_TYPE);

        searchDto.setFacilityName(facilityName);
        searchDto.setFacilityClassification(facilityClassification);
        searchDto.setActiveType(facilityType);
        searchDto.setAuditType(auditType);
        ParamUtil.setSessionAttr(request, AuditConstants.PARAM_AUDIT_SEARCH, searchDto);
    }

    /**
     * AutoStep: prepareData
     *
     * @param bpc
     */
    public void prepareDOCancelAuditData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, AuditConstants.AUDIT_LIST, null);
        String[] auditIds = ParamUtil.getMaskedStrings(request, AuditConstants.AUDIT_ID);
        List<FacilityAudit> auditList = new ArrayList<>();
        for (String auditId : auditIds) {
            FacilityAudit facilityAudit = auditClientBE.getFacilityAuditById(auditId).getEntity();
            List<FacilityActivity> activityList = auditClientBE.getFacilityActivityByFacilityId(facilityAudit.getFacility().getId()).getEntity();
            if (!activityList.isEmpty()) {
                facilityAudit.getFacility().setFacilityActivities(activityList);
            }
            auditList.add(facilityAudit);
        }
        ParamUtil.setSessionAttr(request, AuditConstants.AUDIT_LIST, (Serializable) auditList);
    }

    /**
     * @param bpc
     */
    public void doSubmitCancelAudit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        List<FacilityAudit> auditList = (List<FacilityAudit>) ParamUtil.getSessionAttr(request, AuditConstants.AUDIT_LIST);
        String cancelReason = ParamUtil.getRequestString(request, AuditConstants.PARAM_REASON);
        for (FacilityAudit audit : auditList) {
            audit.setCancelReason(cancelReason);
            audit.setStatus(AuditConstants.PARAM_AUDIT_STATUS_PENDING_AO);
            auditClientBE.updateAudit(audit);
        }
    }

    /**
     * AutoStep: prepareData
     *
     * @param bpc
     */
    public void prepareAOCancelAuditData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, AuditConstants.FACILITY_AUDIT_APP, null);
        String auditAppId = "D57B8FFB-151D-EC11-BE6E-000C298D317C";

        FacilityAuditApp facilityAuditApp = auditClientBE.getFacilityAuditAppById(auditAppId).getEntity();
        FacilityAudit facilityAudit = auditClientBE.getFacilityAuditById(facilityAuditApp.getFacilityAudit().getId()).getEntity();

        List<FacilityActivity> activityList = auditClientBE.getFacilityActivityByFacilityId(facilityAudit.getFacility().getId()).getEntity();
        if (!activityList.isEmpty()) {
            facilityAudit.getFacility().setFacilityActivities(activityList);
        }

        facilityAuditApp.setFacilityAudit(facilityAudit);

        ParamUtil.setSessionAttr(request, AuditConstants.FACILITY_AUDIT_APP, facilityAuditApp);
    }

    /**
     * @param bpc
     */
    public void aoApprovalAuditDate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacilityAuditApp auditApp = (FacilityAuditApp)ParamUtil.getSessionAttr(request, AuditConstants.FACILITY_AUDIT_APP);
        FacilityAudit audit = new FacilityAudit();
        auditApp.setStatus(AuditConstants.PARAM_AUDIT_STATUS_CANCELLED);
        audit.setStatus(AuditConstants.PARAM_AUDIT_STATUS_CANCELLED);
        auditApp.setFacilityAudit(audit);
        auditClientBE.processAuditDate(auditApp);
    }

    /**
     * @param bpc
     */
    public void aoRejectAuditDate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacilityAuditApp auditApp = (FacilityAuditApp)ParamUtil.getSessionAttr(request, AuditConstants.FACILITY_AUDIT_APP);
        auditApp.setStatus(AuditConstants.PARAM_AUDIT_STATUS_PENDING_DO);
        auditClientBE.processAuditDate(auditApp);
    }

    /**
     * AutoStep: page
     *
     * @param bpc
     */
    public void page(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AuditQueryDto searchDto = getSearchDto(request);
        String actionValue = ParamUtil.getString(request, AuditConstants.KEY_ACTION_VALUE);
        switch (actionValue) {
            case "changeSize":
                int pageSize = ParamUtil.getInt(request, AuditConstants.KEY_PAGE_SIZE);
                searchDto.setPage(0);
                searchDto.setSize(pageSize);
                break;
            case "changePage":
                int pageNo = ParamUtil.getInt(request, AuditConstants.KEY_PAGE_NO);
                searchDto.setPage(pageNo - 1);
                break;
            default:
                log.warn("page, action_value is invalid: {}", actionValue);
                break;
        }
        ParamUtil.setSessionAttr(request, AuditConstants.PARAM_AUDIT_SEARCH, searchDto);
    }

    /**
     * AutoStep: sort
     *
     * @param bpc
     */
    public void sort(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AuditQueryDto searchDto = getSearchDto(request);
        String field = ParamUtil.getString(request, AuditConstants.KEY_ACTION_VALUE);
        String sortType = ParamUtil.getString(request, AuditConstants.KEY_ACTION_ADDT);
        searchDto.changeSort(field, sortType);
        ParamUtil.setSessionAttr(request, AuditConstants.PARAM_AUDIT_SEARCH, searchDto);
    }

    private AuditQueryDto getSearchDto(HttpServletRequest request) {
        AuditQueryDto searchDto = (AuditQueryDto) ParamUtil.getSessionAttr(request, AuditConstants.PARAM_AUDIT_SEARCH);
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
        ParamUtil.setRequestAttr(request, AuditConstants.PARAM_FACILITY_NAME, selectModel);
    }

}
