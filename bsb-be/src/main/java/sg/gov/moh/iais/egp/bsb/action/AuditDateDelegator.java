package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
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
import sg.gov.moh.iais.egp.bsb.entity.Application;
import sg.gov.moh.iais.egp.bsb.entity.Facility;
import sg.gov.moh.iais.egp.bsb.entity.FacilityAudit;
import sg.gov.moh.iais.egp.bsb.entity.FacilityAuditApp;
import sg.gov.moh.iais.egp.bsb.util.DateUtil;
import sg.gov.moh.iais.egp.bsb.util.JoinAddress;
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

    private static final String FACILITY = "facility";
    private static final String FACILITY_AUDIT_LIST = "facilityAuditList";
    private static final String FACILITY_AUDIT = "facilityAudit";
    private static final String FACILITY_AUDIT_APP = "facilityAuditAPP";
    private static final String AUDIT_ID = "auditId";
    private static final String LAST_AUDIT_DATE = "lastAuditDt";

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
        selectOption(request);
        // get search DTO
        AuditQueryDto searchDto = getSearchDto(request);
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
        searchDto.setFacilityType(facilityType);
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
        ParamUtil.setSessionAttr(request, FACILITY_AUDIT, null);
        String auditId = ParamUtil.getMaskedString(request, AUDIT_ID);
        String auditDt = ParamUtil.getString(request,LAST_AUDIT_DATE);
        FacilityAudit audit = new FacilityAudit();
        audit.setId(auditId);

        Date auditDate = null;
        if (StringUtil.isNotEmpty(auditDt)) {
            auditDate = Formatter.parseDate(auditDt);
            audit.setAuditDt(auditDate);
        }
        ParamUtil.setSessionAttr(request, FACILITY_AUDIT, audit);
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

        FacilityAudit facilityAudit = (FacilityAudit) ParamUtil.getSessionAttr(request, FACILITY_AUDIT);
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
     * AutoStep: prepareData
     * MohDOCheckAuditDt
     * MohAOCheckAuditDt
     * @param bpc
     */
    public void prepareDOAndAOReviewData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, FACILITY_AUDIT_APP, null);

        String auditAppId = "50229C6F-A50F-EC11-BE6E-000C298D317C";
        FacilityAuditApp facilityAuditApp = auditClient.getFacilityAuditAppById(auditAppId).getEntity();

        Facility facility = facilityAuditApp.getFacilityAudit().getFacility();
        Application application = new Application();
        application.setFacility(facility);
        String facilityAddress = JoinAddress.joinAddress(application);
        facility.setFacilityAddress(facilityAddress);
        ParamUtil.setRequestAttr(request,FACILITY,facility);

        ParamUtil.setSessionAttr(request, FACILITY_AUDIT_APP, facilityAuditApp);
    }

    /**
     * MohDOCheckAuditDt
     * @param bpc
     */
    public void DOVerifiedAuditDate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacilityAuditApp facilityAuditApp = (FacilityAuditApp)ParamUtil.getSessionAttr(request,FACILITY_AUDIT_APP);
        String remark = ParamUtil.getRequestString(request,AuditConstants.PARAM_REMARKS);
        String decision = ParamUtil.getRequestString(request,AuditConstants.PARAM_DECISION);
        //
        facilityAuditApp.setDoRemarks(remark);
        facilityAuditApp.setDoDecision(decision);
        facilityAuditApp.setStatus("AUDITST005");
        auditClient.processAuditDate(facilityAuditApp);
    }

    /**
     * MohDOCheckAuditDt
     * @param bpc
     */
    public void DORejectAuditDate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacilityAuditApp facilityAuditApp = (FacilityAuditApp)ParamUtil.getSessionAttr(request,FACILITY_AUDIT_APP);
        String remark = ParamUtil.getRequestString(request,AuditConstants.PARAM_REMARKS);
        String reason = ParamUtil.getRequestString(request,AuditConstants.PARAM_REASON);
        String decision = ParamUtil.getRequestString(request,AuditConstants.PARAM_DECISION);
        //
        facilityAuditApp.setDoRemarks(remark);
        facilityAuditApp.setDoDecision(decision);
        facilityAuditApp.setDoReason(reason);
        facilityAuditApp.setStatus("AUDITST005");
        auditClient.processAuditDate(facilityAuditApp);
    }

    /**
     * MohAOCheckAuditDt
     * @param bpc
     */
    public void AOApprovalAuditDate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacilityAuditApp facilityAuditApp = (FacilityAuditApp)ParamUtil.getSessionAttr(request,FACILITY_AUDIT_APP);
        String remark = ParamUtil.getRequestString(request,AuditConstants.PARAM_REMARKS);
        //
        facilityAuditApp.setAoRemarks(remark);
        facilityAuditApp.setStatus("AUDITST003");
        //
        facilityAuditApp.getFacilityAudit().setStatus("AUDITST002");
        facilityAuditApp.getFacilityAudit().setAuditDt(facilityAuditApp.getRequestAuditDt());
        auditClient.processAuditDate(facilityAuditApp);
    }

    /**
     * MohAOCheckAuditDt
     * @param bpc
     */
    public void AORejectAuditDate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacilityAuditApp facilityAuditApp = (FacilityAuditApp)ParamUtil.getSessionAttr(request,FACILITY_AUDIT_APP);
        String remark = ParamUtil.getRequestString(request,AuditConstants.PARAM_REMARKS);
        String reason = ParamUtil.getRequestString(request,AuditConstants.PARAM_REASON);
        //
        facilityAuditApp.setAoRemarks(remark);
        facilityAuditApp.setAoReason(reason);
        facilityAuditApp.setStatus("AUDITST007");
        auditClient.processAuditDate(facilityAuditApp);
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

    public void selectOption(HttpServletRequest request) {
        List<String> facNames = biosafetyEnquiryClient.queryDistinctFN().getEntity();
        List<SelectOption> selectModel = IaisCommonUtils.genNewArrayList();
        for (String facName : facNames) {
            selectModel.add(new SelectOption(facName, facName));
        }
        ParamUtil.setRequestAttr(request, AuditConstants.PARAM_FACILITY_NAME, selectModel);
    }

}
