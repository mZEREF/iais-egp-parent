package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.AuditClient;
import sg.gov.moh.iais.egp.bsb.constant.AuditConstants;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.AuditQueryDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.AuditQueryResultDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.FacilitySubmitSelfAuditDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.FacilityAuditDto;
import sg.gov.moh.iais.egp.bsb.entity.Facility;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_AUDIT_DATE;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_AUDIT;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_PAGE_NO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_PAGE_SIZE;

/**
 * @author Zhu Tangtang
 */
@Slf4j
@Delegator(value = "auditDateDelegatorFE")
public class AuditDateDelegatorFE {
    private final AuditClient auditClient;

    @Autowired
    public AuditDateDelegatorFE(AuditClient auditClient){
        this.auditClient = auditClient;
    }

    public void start(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction(MODULE_AUDIT, FUNCTION_AUDIT_DATE);
        HttpServletRequest request = bpc.request;
        IaisEGPHelper.clearSessionAttr(request, AuditConstants.class);
        ParamUtil.setSessionAttr(request, AuditConstants.PARAM_AUDIT_SEARCH, null);
        ParamUtil.setSessionAttr(request, AuditConstants.PARAM_AUDIT_DTO, null);
    }

    /**
     * OngoingAuditList
     */
    public void prepareAuditListData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,AuditConstants.PARAM_YEAR,null);
        selectOption(request);
        // get search DTO
        AuditQueryDto searchDto = getSearchDto(request);
        ParamUtil.setSessionAttr(request, AuditConstants.PARAM_AUDIT_SEARCH, searchDto);
        // call API to get searched data
        ResponseDto<AuditQueryResultDto> searchResult = auditClient.getAllAudit(searchDto);

        if (searchResult.ok()) {
            ParamUtil.setRequestAttr(request, AuditConstants.KEY_AUDIT_PAGE_INFO, searchResult.getEntity().getPageInfo());
            List<FacilityAuditDto> audits = searchResult.getEntity().getTasks();
            for (FacilityAuditDto audit : audits) {
               Facility facility = auditClient.getFacilityByApproval(audit.getApproval().getId(),audit.getApproval().getProcessType()).getEntity();
               audit.setFacility(facility);
            }
            ParamUtil.setRequestAttr(request, AuditConstants.KEY_AUDIT_DATA_LIST, audits);
        } else {
            log.warn("get audit API doesn't return ok, the response is {}", searchResult);
            ParamUtil.setRequestAttr(request, AuditConstants.KEY_AUDIT_PAGE_INFO, PageInfo.emptyPageInfo(searchDto));
            ParamUtil.setRequestAttr(request, AuditConstants.KEY_AUDIT_DATA_LIST, new ArrayList<>());
        }
    }

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

    public void prepareSpecifyDtData(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, AuditConstants.FACILITY_AUDIT, null);
        String auditId = ParamUtil.getMaskedString(request, AuditConstants.AUDIT_ID);
        String auditDt = ParamUtil.getString(request,AuditConstants.LAST_AUDIT_DATE);
        String module = ParamUtil.getString(request,AuditConstants.PARAM_MODULE_TYPE);

        FacilitySubmitSelfAuditDto auditDto = getAuditDto(request);
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request,ValidationConstants.KEY_SHOW_ERROR_SWITCH);
        if(Boolean.TRUE.equals(needShowError)){
            ParamUtil.setRequestAttr(request,ValidationConstants.KEY_VALIDATION_ERRORS,auditDto.retrieveValidationResult());
        }
        if (!StringUtils.hasLength(auditDto.getAuditId())) {
            auditDto.setAuditId(auditId);
            auditDto.setModule(module);
            if (StringUtils.hasLength(auditDt)) {
                Date auditDate = Formatter.parseDate(auditDt);
                auditDto.setLastAuditDate(auditDate);
            }
        }
        ParamUtil.setSessionAttr(request, AuditConstants.PARAM_AUDIT_DTO, auditDto);
    }

    public void preConfirm(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;
        FacilitySubmitSelfAuditDto dto = getAuditDto(request);

        String remarks = ParamUtil.getRequestString(request, AuditConstants.PARAM_REMARKS);
        String reason = ParamUtil.getString(request,AuditConstants.PARAM_REASON_FOR_CHANGE);
        String auditDate = ParamUtil.getRequestString(request, AuditConstants.PARAM_AUDIT_DATE);

        dto.setRemarks(remarks);
        dto.setChangeReason(reason);
        if (StringUtils.hasLength(auditDate)) {
            Date requestAuditDt = Formatter.parseDate(auditDate);
            dto.setAuditDate(requestAuditDt);
        }
        doValidation(dto,request);
        ParamUtil.setSessionAttr(request, AuditConstants.PARAM_AUDIT_DTO, dto);
    }

    /**
     * specifyDt
     * changeDt
     */
    public void specifyAndChangeDt(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacilitySubmitSelfAuditDto dto = (FacilitySubmitSelfAuditDto)ParamUtil.getSessionAttr(request, AuditConstants.PARAM_AUDIT_DTO);
        auditClient.specifyAndChangeAuditDt(dto);
    }

    public void page(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AuditQueryDto searchDto = getSearchDto(request);
        String actionValue = ParamUtil.getString(request, AuditConstants.KEY_ACTION_VALUE);
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
        ParamUtil.setSessionAttr(request, AuditConstants.PARAM_AUDIT_SEARCH, searchDto);
    }

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

    private FacilitySubmitSelfAuditDto getAuditDto(HttpServletRequest request) {
        FacilitySubmitSelfAuditDto auditDto = (FacilitySubmitSelfAuditDto) ParamUtil.getSessionAttr(request, AuditConstants.PARAM_AUDIT_DTO);
        return auditDto == null ? getDefaultAuditDto() : auditDto;
    }

    private FacilitySubmitSelfAuditDto getDefaultAuditDto() {
        return new FacilitySubmitSelfAuditDto();
    }

    public void selectOption(HttpServletRequest request) {
        List<String> facNames = auditClient.queryDistinctFN().getEntity();
        List<SelectOption> selectModel = new ArrayList<>(facNames.size());
        for (String facName : facNames) {
            selectModel.add(new SelectOption(facName, facName));
        }
        ParamUtil.setRequestAttr(request, AuditConstants.PARAM_FACILITY_NAME, selectModel);
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
