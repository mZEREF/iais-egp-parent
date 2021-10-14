package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.AuditClient;
import sg.gov.moh.iais.egp.bsb.constant.AuditConstants;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.AuditQueryDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.AuditQueryResultDto;
import sg.gov.moh.iais.egp.bsb.entity.*;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Zhu Tangtang
 */
@Slf4j
@Delegator(value = "auditDateDelegator")
public class AuditDateDelegator {

    @Autowired
    private AuditClient auditClient;

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
            List<FacilityAudit> audits = searchResult.getEntity().getTasks();
            for (FacilityAudit audit : audits) {
               Facility facility = auditClient.getFacilityByApproval(audit.getApproval()).getEntity();
               audit.setFacility(facility);
            }
            ParamUtil.setRequestAttr(request, AuditConstants.KEY_AUDIT_DATA_LIST, audits);
        } else {
            log.warn("get audit API doesn't return ok, the response is {}", searchResult);
            ParamUtil.setRequestAttr(request, AuditConstants.KEY_AUDIT_PAGE_INFO, PageInfo.emptyPageInfo(searchDto));
            ParamUtil.setRequestAttr(request, AuditConstants.KEY_AUDIT_DATA_LIST, new ArrayList<>());
        }

        Calendar cd = Calendar.getInstance();
        int year = cd.get(Calendar.YEAR);
        ParamUtil.setSessionAttr(request,AuditConstants.PARAM_YEAR,year);
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
    public void prepareSpecifyDtData(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, AuditConstants.FACILITY_AUDIT, null);
        String auditId = ParamUtil.getMaskedString(request, AuditConstants.AUDIT_ID);
        String auditDt = ParamUtil.getString(request,AuditConstants.LAST_AUDIT_DATE);
        FacilityAudit audit = new FacilityAudit();
        audit.setId(auditId);

        Date auditDate = null;
        if (StringUtil.isNotEmpty(auditDt)) {
            auditDate = Formatter.parseDate(auditDt);
            audit.setAuditDt(auditDate);
        }
        ParamUtil.setSessionAttr(request, AuditConstants.FACILITY_AUDIT, audit);
    }

    /**
     * AutoStep: submit
     * specifyDt
     * changeDt
     *
     * @param bpc
     */
    public void specifyAndChangeDt(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;

        FacilityAudit facilityAudit = (FacilityAudit) ParamUtil.getSessionAttr(request, AuditConstants.FACILITY_AUDIT);
        String remarks = ParamUtil.getRequestString(request, AuditConstants.PARAM_REMARKS);
        String reason = ParamUtil.getString(request,AuditConstants.PARAM_REASON_FOR_CHANGE);
        String auditDate = ParamUtil.getRequestString(request, AuditConstants.PARAM_AUDIT_DATE);

        FacilityAudit audit = new FacilityAudit();
        audit.setId(facilityAudit.getId());
        audit.setRemarks(remarks);
        audit.setChangeReason(reason);
        Date requestAuditDt = null;
        if (StringUtil.isNotEmpty(auditDate)) {
            requestAuditDt = Formatter.parseDate(auditDate);
            audit.setAuditDt(requestAuditDt);
        }
        auditClient.specifyAndChangeAuditDt(audit);
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
        List<String> facNames = auditClient.queryDistinctFN().getEntity();
        List<SelectOption> selectModel = IaisCommonUtils.genNewArrayList();
        for (String facName : facNames) {
            selectModel.add(new SelectOption(facName, facName));
        }
        ParamUtil.setRequestAttr(request, AuditConstants.PARAM_FACILITY_NAME, selectModel);
    }

}
