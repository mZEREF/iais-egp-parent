package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.AuditClientBE;
import sg.gov.moh.iais.egp.bsb.client.BiosafetyEnquiryClient;
import sg.gov.moh.iais.egp.bsb.constant.AuditConstants;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.AuditQueryDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.FacilityQueryResultDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.SaveAuditDto;
import sg.gov.moh.iais.egp.bsb.entity.*;
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
    private AuditClientBE auditClientBE;
    @Autowired
    private BiosafetyEnquiryClient biosafetyEnquiryClient;

    public void start(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction(MODULE_AUDIT, FUNCTION_AUDIT);
        HttpServletRequest request = bpc.request;
        IaisEGPHelper.clearSessionAttr(request, AuditConstants.class);
        ParamUtil.setSessionAttr(request, PARAM_AUDIT_SEARCH, null);
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

        List<FacilityQueryResultDto.FacInfo> facilityList = (List<FacilityQueryResultDto.FacInfo>) ParamUtil.getSessionAttr(request, KEY_AUDIT_DATA_LIST);
        Map<String, FacilityQueryResultDto.FacInfo> facInfoMap = new HashMap<>(facilityList.size());
        for (FacilityQueryResultDto.FacInfo facInfo : facilityList) {
            facInfoMap.put(facInfo.getFacId(), facInfo);
        }
        //
        facilityList.clear();
        for (String facId : facIds) {
            if (facInfoMap.containsKey(facId)) {
                facilityList.add(facInfoMap.get(facId));
            }
        }
        ParamUtil.setSessionAttr(request, FACILITY_LIST, (Serializable) facilityList);
    }

    public void doCreate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        List<FacilityQueryResultDto.FacInfo> facilityList = (List<FacilityQueryResultDto.FacInfo>) ParamUtil.getSessionAttr(request, KEY_AUDIT_DATA_LIST);

        List<SaveAuditDto> auditDtos = new ArrayList<>(facilityList.size());
        if (!CollectionUtils.isEmpty(facilityList)) {
            for (int i = 0; i < facilityList.size(); i++) {
                String auditType = ParamUtil.getRequestString(request, PARAM_AUDIT_TYPE + SEPARATOR + i);
                String remarks = ParamUtil.getRequestString(request, PARAM_REMARKS + SEPARATOR + i);
                //
                SaveAuditDto dto = new SaveAuditDto();
                dto.setAuditType(auditType);
                dto.setRemarks(remarks);
                dto.setStatus("AUDITST001");
                dto.setApprovalId(facilityList.get(i).getApprovalId());
                dto.setProcessType(facilityList.get(i).getProcessType());
                auditDtos.add(dto);
                auditClientBE.saveFacilityAudit(auditDtos);
            }
        }
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

    public static List<String> deduplication(List<String> list) {
        HashSet<String> set = new HashSet<>(list);
        list.clear();
        list.addAll(set);
        return list;
    }

    public String getMainActivity(String facClass) {
        String mainActivity = "";
        if (StringUtils.hasLength(facClass)) {
            switch (facClass) {
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
