package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.AuditClient;
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
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

/**
 * @author Zhu Tangtang
 */
@Slf4j
@Delegator(value = "auditCreationDelegator")
public class AuditCreationDelegator {

    private static final String FACILITY_CLASSIFICATION_1 = "FACCLA001";
    private static final String FACILITY_CLASSIFICATION_2 = "FACCLA002";
    private static final String FACILITY_CLASSIFICATION_3 = "FACCLA003";
    private static final String FACILITY_CLASSIFICATION_4 = "FACCLA004";
    private static final String FACILITY_CLASSIFICATION_5 = "FACCLA005";

    private static final String ACTIVITY_TYPE_1 = "ACTVITY001";
    private static final String ACTIVITY_TYPE_2 = "ACTVITY002";
    private static final String ACTIVITY_TYPE_5 = "ACTVITY005";
    private static final String ACTIVITY_TYPE_8 = "ACTVITY008";

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
     * AuditListCreationList
     * AutoStep: prepareData
     *
     * @param bpc
     */
    public void prepareAuditListData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,AuditConstants.PARAM_YEAR,null);
        selectOption(request);
        // get search DTO
        AuditQueryDto searchDto=getSearchDto(request);
        searchDto.setFrom(AuditConstants.PARAM_CREATE_AUDIT);
        ParamUtil.setSessionAttr(request, AuditConstants.PARAM_AUDIT_SEARCH, searchDto);
        // call API to get searched data
        ResponseDto<FacilityQueryResultDto> searchResult = auditClient.queryFacility(searchDto);

        if (searchResult.ok()) {
            ParamUtil.setRequestAttr(request, AuditConstants.KEY_AUDIT_PAGE_INFO, searchResult.getEntity().getPageInfo());
            List<FacilityActivity> audits = searchResult.getEntity().getTasks();
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
     * CreateAuditList
     * AutoStep: prepareData
     *
     * @param bpc
     */
    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, AuditConstants.FACILITY_LIST, null);
        String[] facIds = ParamUtil.getMaskedStrings(request, AuditConstants.FACILITY_ID);
        List<String> facList=new ArrayList<>();
        for (String facId : facIds) {
            if (StringUtil.isNotEmpty(facId)){
                facList.add(facId);
            }
        }
        List<String> list = repeatListWayTwo(facList);
        List<Facility> facilityList=new ArrayList<>();
        for (String fId : list) {
            Facility facility = auditClient.getFacilityById(fId).getEntity();
            List<FacilityActivity> activityList = auditClient.getFacilityActivityByFacilityId(fId).getEntity();
            if (!activityList.isEmpty()) {
                facility.setFacilityActivities(activityList);
            }
            facilityList.add(facility);
        }
        ParamUtil.setSessionAttr(request, AuditConstants.FACILITY_LIST, (Serializable) facilityList);
    }

    /**
     * CreateAuditList
     * AutoStep: prepareData
     *
     * @param bpc
     */
    public void doCreate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacilityAudit facilityAudit = new FacilityAudit();
        List<Facility> facilityList = (List<Facility>)ParamUtil.getSessionAttr(request, AuditConstants.FACILITY_LIST);
        String auditType = ParamUtil.getRequestString(request,AuditConstants.PARAM_AUDIT_TYPE);
        String remarks = ParamUtil.getRequestString(request,AuditConstants.PARAM_REMARKS);
        facilityAudit.setAuditType(auditType);
        facilityAudit.setStatus("AUDITST001");
        facilityAudit.setRemarks(remarks);
        if (!facilityList.isEmpty()){
            for (Facility facility : facilityList) {
                String mainActivity = getMainActivity(facility.getFacilityClassification());
                List<FacilityActivity> facilityActivities = facility.getFacilityActivities();
                for (FacilityActivity facilityActivity : facilityActivities) {
                    if (facilityActivity.getActivityType().equals(mainActivity)){
                        facilityAudit.setApproval(facilityActivity.getApproval());
                    }
                }
                auditClient.saveFacilityAudit(facilityAudit);
            }
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

    public void selectOption(HttpServletRequest request){
        List<String> facNames = biosafetyEnquiryClient.queryDistinctFN().getEntity();
        List<SelectOption> selectModel = new ArrayList<>();
        for (String facName : facNames) {
            selectModel.add(new SelectOption(facName,facName));
        }
        ParamUtil.setRequestAttr(request, AuditConstants.PARAM_FACILITY_NAME, selectModel);
    }

    public static List<String> repeatListWayTwo(List<String> list){
        HashSet<String> set = new HashSet<>(list);
        list.clear();
        list.addAll(set);
        return list;
    }

    public String getMainActivity(String facClass){
        String mainActivity = "";
        if(StringUtils.hasLength(facClass)){
            switch (facClass){
                case FACILITY_CLASSIFICATION_2:
                case FACILITY_CLASSIFICATION_1:
                    mainActivity = ACTIVITY_TYPE_1;
                    break;
                case FACILITY_CLASSIFICATION_3:
                    mainActivity = ACTIVITY_TYPE_2;
                    break;
                case FACILITY_CLASSIFICATION_4:
                    mainActivity = ACTIVITY_TYPE_5;
                    break;
                case FACILITY_CLASSIFICATION_5:
                    mainActivity = ACTIVITY_TYPE_8;
                    break;
                default:
                    log.info("no such facility classification type");
                    break;
            }
        }
        return mainActivity;
    }

}
