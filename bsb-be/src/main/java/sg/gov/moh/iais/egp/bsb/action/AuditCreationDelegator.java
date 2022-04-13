package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import sg.gov.moh.iais.egp.bsb.client.AuditClientBE;
import sg.gov.moh.iais.egp.bsb.client.OnlineEnquiryClient;
import sg.gov.moh.iais.egp.bsb.constant.AuditConstants;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.AuditQueryDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.FacilityQueryResultDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.SaveAuditDto;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;

import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.*;

/**
 * @author Zhu Tangtang
 */
@Slf4j
@Delegator(value = "auditCreationDelegator")
public class AuditCreationDelegator {
    private static final String ACTION_TYPE_SUBMIT = "doSubmit";
    private static final String ACTION_TYPE_PREPARE = "prepare";
    private static final String ACTION_TYPE = "action_type";

    private final AuditClientBE auditClientBE;
    private final OnlineEnquiryClient onlineEnquiryClient;

    public AuditCreationDelegator(AuditClientBE auditClientBE, OnlineEnquiryClient onlineEnquiryClient) {
        this.auditClientBE = auditClientBE;
        this.onlineEnquiryClient = onlineEnquiryClient;
    }

    public void start(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction(MODULE_AUDIT, FUNCTION_AUDIT);
        HttpServletRequest request = bpc.request;
        IaisEGPHelper.clearSessionAttr(request, AuditConstants.class);
        ParamUtil.setSessionAttr(request, PARAM_AUDIT_SEARCH, null);
        ParamUtil.setSessionAttr(request,KEY_MANUAL_AUDIT,null);
    }

    /**
     * AuditListCreationList
     */
    public void prepareAuditListData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, PARAM_YEAR, null);
        ParamUtil.setSessionAttr(request, KEY_AUDIT_DATA_LIST, null);
        selectOption(request);
        // get search DTO
        AuditQueryDto searchDto = getSearchDto(request);
        searchDto.setFrom(PARAM_CREATE_AUDIT);
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

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, FACILITY_LIST, null);
        String[] facIds = ParamUtil.getMaskedStrings(request, FACILITY_ID);
        if (facIds==null){
            facIds = (String[])ParamUtil.getSessionAttr(request,"facIds");
        }
        SaveAuditDto dto = getSaveAuditDto(request);
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request,ValidationConstants.KEY_SHOW_ERROR_SWITCH);
        if(Boolean.TRUE.equals(needShowError)){
            ParamUtil.setRequestAttr(request,ValidationConstants.KEY_VALIDATION_ERRORS,dto.retrieveValidationResult());
        }
        List<FacilityQueryResultDto.FacInfo> facilityList = (List<FacilityQueryResultDto.FacInfo>) ParamUtil.getSessionAttr(request, KEY_AUDIT_DATA_LIST);
        Map<String, FacilityQueryResultDto.FacInfo> facInfoMap = Maps.newHashMapWithExpectedSize(facilityList.size());
        for (FacilityQueryResultDto.FacInfo facInfo : facilityList) {
            facInfoMap.put(facInfo.getFacId(), facInfo);
        }
        facilityList.clear();
        for (String facId : facIds) {
            FacilityQueryResultDto.FacInfo facInfo = facInfoMap.get(facId);
            if (facInfo != null) {
                facilityList.add(facInfo);
            }
        }
        //fill the known data
        if (CollectionUtils.isEmpty(dto.getSaveAudits())){
            List<SaveAuditDto.SaveAudit> saveAudits = new ArrayList<>(0);
            for (FacilityQueryResultDto.FacInfo facInfo : facilityList) {
                SaveAuditDto.SaveAudit saveAudit = new SaveAuditDto.SaveAudit();
                saveAudit.setActivityType(facInfo.getActivityType());
                saveAudit.setFacClassification(facInfo.getFacClassification());
                saveAudit.setFacName(facInfo.getFacName());
                saveAudit.setApprovalId(facInfo.getApprovalId());
                saveAudit.setProcessType(facInfo.getProcessType());
                saveAudits.add(saveAudit);
            }
            dto.setSaveAudits(saveAudits);
        }

        ParamUtil.setSessionAttr(request,"facIds",facIds);
        ParamUtil.setSessionAttr(request,KEY_MANUAL_AUDIT,dto);
    }

    public void doValidate(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SaveAuditDto dto = getSaveAuditDto(request);
        List<SaveAuditDto.SaveAudit> saveAudits = dto.getSaveAudits();
        if (!CollectionUtils.isEmpty(saveAudits)) {
            for (int i = 0; i < saveAudits.size(); i++) {
                SaveAuditDto.SaveAudit saveAudit = saveAudits.get(i);
                saveAudit.setAuditType(ParamUtil.getRequestString(request, PARAM_AUDIT_TYPE + SEPARATOR + i));
                saveAudit.setRemarks(ParamUtil.getRequestString(request, PARAM_REMARKS + SEPARATOR + i));
                saveAudit.setStatus(PARAM_AUDIT_STATUS_PENDING_TASK_ASSIGNMENT);
            }
        }
        validateData(dto,request);
        ParamUtil.setSessionAttr(request,KEY_MANUAL_AUDIT,dto);
    }

    public void doCreate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SaveAuditDto dto = (SaveAuditDto)ParamUtil.getSessionAttr(request,KEY_MANUAL_AUDIT);
        auditClientBE.saveFacilityAudit(dto);
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

    private SaveAuditDto getSaveAuditDto(HttpServletRequest request) {
        SaveAuditDto auditDto = (SaveAuditDto) ParamUtil.getSessionAttr(request, KEY_MANUAL_AUDIT);
        return auditDto == null ? getDefaultSaveAuditDto() : auditDto;
    }

    private SaveAuditDto getDefaultSaveAuditDto() {
        return new SaveAuditDto();
    }

    public void selectOption(HttpServletRequest request) {
        List<String> facNames = onlineEnquiryClient.queryDistinctFN().getEntity();
        List<SelectOption> selectModel = new ArrayList<>(facNames.size());
        for (String facName : facNames) {
            selectModel.add(new SelectOption(facName, facName));
        }
        ParamUtil.setRequestAttr(request, PARAM_FACILITY_NAME, selectModel);
    }

    private void validateData(SaveAuditDto dto, HttpServletRequest request){
        //validation
        String actionType;
        ValidationResultDto validationResultDto = auditClientBE.validateManualAudit(dto);
        if (!validationResultDto.isPass()){
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            actionType = ACTION_TYPE_PREPARE;
        }else {
            actionType = ACTION_TYPE_SUBMIT;
        }
        ParamUtil.setRequestAttr(request, ACTION_TYPE, actionType);
    }

}
