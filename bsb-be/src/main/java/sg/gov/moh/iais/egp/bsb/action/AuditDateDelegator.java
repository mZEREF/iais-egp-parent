package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.AuditClient;
import sg.gov.moh.iais.egp.bsb.client.BiosafetyEnquiryClient;
import sg.gov.moh.iais.egp.bsb.constant.AuditConstants;
import sg.gov.moh.iais.egp.bsb.constant.RevocationConstants;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.AuditQueryDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.AuditQueryResultDto;
import sg.gov.moh.iais.egp.bsb.entity.Facility;
import sg.gov.moh.iais.egp.bsb.entity.FacilityAudit;
import sg.gov.moh.iais.egp.bsb.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Zhu Tangtang
 */
@Slf4j
@Delegator(value = "auditDateDelegator")
public class AuditDateDelegator {

    private static final String KEY_AUDIT_PAGE_INFO = "pageInfo";
    private static final String KEY_AUDIT_DATA_LIST = "dataList";
    private static final String KEY_ACTION_VALUE = "action_value";
    private static final String KEY_ACTION_ADDT = "action_additional";

    private static final String KEY_PAGE_SIZE = "pageJumpNoPageSize";
    private static final String KEY_PAGE_NO = "pageJumpNoTextchangePage";

    private static final String FACILITY_LIST = "facilityList";
    private static final String FACILITY_AUDIT_LIST = "facilityAuditList";
    private static final String AUDIT_ID = "auditId";

    @Autowired
    private AuditClient auditClient;

    @Autowired
    private BiosafetyEnquiryClient biosafetyEnquiryClient;

    /**
     * StartStep: startStep
     *
     * @param bpc
     * @throws IllegalAccessException
     */
    public void start(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_SYSTEM_CONFIG, AuditTrailConsts.FUNCTION_ERROR_MESSAGES_MANAGEMENT);
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
        selectOption(request);
        // get search DTO
        AuditQueryDto searchDto=getSearchDto(request);
        ParamUtil.setSessionAttr(request, AuditConstants.PARAM_AUDIT_SEARCH, searchDto);
        // call API to get searched data
        ResponseDto<AuditQueryResultDto> searchResult = auditClient.getAllAudit(searchDto);

        if (searchResult.ok()) {
            ParamUtil.setRequestAttr(request, KEY_AUDIT_PAGE_INFO, searchResult.getEntity().getPageInfo());
            List<FacilityAudit> audits = searchResult.getEntity().getTasks();
            ParamUtil.setRequestAttr(request, KEY_AUDIT_DATA_LIST, audits);
        } else {
            log.warn("get revocation application API doesn't return ok, the response is {}", searchResult);
            ParamUtil.setRequestAttr(request, KEY_AUDIT_PAGE_INFO, PageInfo.emptyPageInfo(searchDto));
            ParamUtil.setRequestAttr(request, KEY_AUDIT_DATA_LIST, new ArrayList<>());
        }

    }

    /**
     * AutoStep: doSearch
     *
     * @param bpc
     */
    public void doSearch(BaseProcessClass bpc) throws ParseException {
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
        searchDto.setFacilityType(facilityType);
        searchDto.setAuditType(auditType);
        ParamUtil.setSessionAttr(request, AuditConstants.PARAM_AUDIT_SEARCH, searchDto);
    }

    /**
     * AutoStep: prepareData
     *
     * @param bpc
     */
    public void prepareSpecifyDtData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, FACILITY_AUDIT_LIST, null);
        String auditId =  ParamUtil.getMaskedString(request, AUDIT_ID);
        List<FacilityAudit> facilityAuditList=new ArrayList<>();
        FacilityAudit audit=new FacilityAudit();
        audit.setId(auditId);
        facilityAuditList.add(audit);
        ParamUtil.setSessionAttr(request, FACILITY_AUDIT_LIST, (Serializable) facilityAuditList);
    }

    /**
     * AutoStep: prepareData
     *
     * @param bpc
     */
    public void specifyDt(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacilityAudit facilityAudit = new FacilityAudit();
        List<FacilityAudit> facilityAuditList = (List<FacilityAudit>)ParamUtil.getSessionAttr(request, FACILITY_AUDIT_LIST);
        String remarks = ParamUtil.getRequestString(request,AuditConstants.PARAM_REMARKS);
        String auditDate = ParamUtil.getRequestString(request,AuditConstants.PARAM_AUDIT_DATE);
        Date startDt = null;
        Date endDt = null;
        facilityAudit.setRemarks(remarks);
        for (FacilityAudit audit : facilityAuditList) {
            facilityAudit.setId(audit.getId());
            startDt = audit.getCreatedAt();
            Facility facility = auditClient.getFacilityById(audit.getFacility().getId()).getEntity();
            endDt = facility.getApprovalDate();
        }
        String startDtStr = DateUtil.dateToStr(startDt);
        String endDtStr = DateUtil.dateToStr(endDt);
        if (DateUtil.checkDateVal(auditDate,startDtStr,endDtStr)){
            //Modifying current Data
        }else{
            //Copy current data to audit_app table and change current data status to suspend
        }

    }

    /**
     * AutoStep: page
     *
     * @param bpc
     */
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
        String field = ParamUtil.getString(request, KEY_ACTION_VALUE);
        String sortType = ParamUtil.getString(request, KEY_ACTION_ADDT);
        searchDto.changeSort(field, sortType);
        ParamUtil.setSessionAttr(request, AuditConstants.PARAM_AUDIT_SEARCH, searchDto);
    }

    private AuditQueryDto getSearchDto(HttpServletRequest request) {
        AuditQueryDto searchDto = (AuditQueryDto) ParamUtil.getSessionAttr(request, RevocationConstants.PARAM_AUDIT_SEARCH);
        return searchDto == null ? getDefaultSearchDto() : searchDto;
    }

    private AuditQueryDto getDefaultSearchDto() {
        AuditQueryDto dto = new AuditQueryDto();
        dto.clearAllFields();
        dto.defaultPaging();
        return dto;
    }

    public void selectOption(HttpServletRequest request){
        List<String> facNames = biosafetyEnquiryClient.queryDistinctFN().getEntity();
        List<SelectOption> selectModel = IaisCommonUtils.genNewArrayList();
        for (String facName : facNames) {
            selectModel.add(new SelectOption(facName,facName));
        }
        ParamUtil.setRequestAttr(request, AuditConstants.PARAM_FACILITY_NAME, selectModel);
    }

}
