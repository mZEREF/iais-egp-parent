package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.constant.RevocationConstants;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.revocation.*;
import sg.gov.moh.iais.egp.bsb.client.RevocationClient;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Zhu Tangtang
 */
@Slf4j
@Delegator(value = "AORevocationDelegator")
public class AORevocationDelegator {

    private static final String KEY_APPLICATION_PAGE_INFO = "pageInfo";
    private static final String KEY_APPLICATION_DATA_LIST = "dataList";
    private static final String KEY_ACTION_VALUE = "action_value";
    private static final String KEY_ACTION_ADDT = "action_additional";
    private static final String KEY_ACTION_TYPE = "crud_action_type";

    private static final String KEY_PAGE_SIZE = "pageJumpNoPageSize";
    private static final String KEY_PAGE_NO = "pageJumpNoTextchangePage";

    @Autowired
    private RevocationClient revocationClient;

    /**
     * StartStep: startStep
     *
     * @param bpc
     * @throws IllegalAccessException
     */
    public void start(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_SYSTEM_CONFIG, AuditTrailConsts.FUNCTION_ERROR_MESSAGES_MANAGEMENT);
        HttpServletRequest request = bpc.request;
        IaisEGPHelper.clearSessionAttr(request, RevocationConstants.class);
        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_APPLICATION_SEARCH, null);
        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_APPLICATION_SEARCH_RESULT, null);
        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_REVOCATION_DETAIL, null);
    }

    /**
     * AutoStep: prepareData
     *
     * @param bpc
     */
    public void prepareTaskListData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        // get search DTO
        ApprovalOfficerQueryDto searchDto=getSearchDto(request);
        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_APPLICATION_SEARCH, searchDto);
        // call API to get searched data
        ResponseDto<AOQueryResultDto> searchResult = revocationClient.doQuery(searchDto);
        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_APPLICATION_SEARCH_RESULT, (Serializable) searchResult.getEntity().getBsbInboxes());

        if (searchResult.ok()) {
            ParamUtil.setRequestAttr(request, KEY_APPLICATION_PAGE_INFO, searchResult.getEntity().getPageInfo());
            ParamUtil.setRequestAttr(request, KEY_APPLICATION_DATA_LIST, searchResult.getEntity().getBsbInboxes());
        } else {
            log.warn("get revocation application API doesn't return ok, the response is {}", searchResult);
            ParamUtil.setRequestAttr(request, KEY_APPLICATION_PAGE_INFO, PageInfo.emptyPageInfo(searchDto));
            ParamUtil.setRequestAttr(request, KEY_APPLICATION_DATA_LIST, new ArrayList<>());
        }

    }

    /**
     * AutoStep: doSearch
     *
     * @param bpc
     */
    public void doSearch(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;
        ApprovalOfficerQueryDto searchDto = new ApprovalOfficerQueryDto();

        String facilityName = ParamUtil.getString(request, RevocationConstants.PARAM_FACILITY_NAME);
        String facilityAddress = ParamUtil.getString(request, RevocationConstants.PARAM_FACILITY_ADDRESS);
        String facilityClassification = ParamUtil.getString(request, RevocationConstants.PARAM_FACILITY_CLASSIFICATION);
        String facilityType = ParamUtil.getString(request, RevocationConstants.PARAM_FACILITY_TYPE);
        String processType = ParamUtil.getString(request, RevocationConstants.PARAM_PROCESS_TYPE);
        String applicationDt=ParamUtil.getString(request, RevocationConstants.PARAM_APPLICATION_DATE);
        String applicationNo = ParamUtil.getString(request, RevocationConstants.PARAM_APPLICATION_NO);
        String applicationType = ParamUtil.getString(request, RevocationConstants.PARAM_APPLICATION_TYPE);
        String applicationStatus = ParamUtil.getString(request, RevocationConstants.PARAM_APPLICATION_STATUS);

        Date applicationDate=null;

        searchDto.setFacilityName(facilityName);
        searchDto.setFacilityClassification(facilityClassification);
        searchDto.setFacilityType(facilityType);
        searchDto.setProcessType(processType);
        searchDto.setApplicationNo(applicationNo);
        searchDto.setApplicationType(applicationType);
        searchDto.setApplicationStatus(applicationStatus);
        searchDto.setPage(0);
        if (StringUtil.isNotEmpty(applicationDt)) {
            applicationDate = Formatter.parseDate(ParamUtil.getString(request, RevocationConstants.PARAM_APPLICATION_DATE));
            searchDto.setApplicationDate(applicationDate);
        }
        if (StringUtil.isNotEmpty(facilityAddress)) {
            String[] strArr = facilityAddress.split("");
            String blockNo = strArr[0];
            String streetName = strArr[1];
            String postalCode = strArr[3];
            searchDto.setBlockNo(blockNo);
            searchDto.setStreetName(streetName);
            searchDto.setPostalCode(postalCode);

            String[] arr = strArr[2].split("-");
            String floorNo = arr[0];
            String unitNo = arr[1];
            searchDto.setFloorNo(floorNo);
            searchDto.setUnitNo(unitNo);
        }
        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_APPLICATION_SEARCH, searchDto);
        //Select back the query criteria
        ParamUtil.setRequestAttr(request, RevocationConstants.PARAM_FACILITY_NAME, facilityName);
        ParamUtil.setRequestAttr(request, RevocationConstants.PARAM_FACILITY_CLASSIFICATION, facilityClassification);
        ParamUtil.setRequestAttr(request, RevocationConstants.PARAM_FACILITY_TYPE, facilityType);
        ParamUtil.setRequestAttr(request, RevocationConstants.PARAM_PROCESS_TYPE, processType);
        ParamUtil.setRequestAttr(request, RevocationConstants.PARAM_APPLICATION_DATE, applicationDate);
        ParamUtil.setRequestAttr(request, RevocationConstants.PARAM_APPLICATION_NO, applicationNo);
        ParamUtil.setRequestAttr(request, RevocationConstants.PARAM_APPLICATION_TYPE, applicationType);
        ParamUtil.setRequestAttr(request, RevocationConstants.PARAM_APPLICATION_STATUS, applicationStatus);
        ParamUtil.setRequestAttr(request, RevocationConstants.PARAM_FACILITY_ADDRESS, facilityAddress);
    }

    /**
     * AutoStep: doPaging
     *
     * @param bpc
     */
    public void page(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ApprovalOfficerQueryDto searchDto = getSearchDto(request);
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
        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_APPLICATION_SEARCH, searchDto);
    }

    /**
     * AutoStep: doSorting
     *
     * @param bpc
     */
    public void sort(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ApprovalOfficerQueryDto searchDto = getSearchDto(request);
        String field = ParamUtil.getString(request, KEY_ACTION_VALUE);
        String sortType = ParamUtil.getString(request, KEY_ACTION_ADDT);
        searchDto.changeSort(field, sortType);
        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_APPLICATION_SEARCH, searchDto);
    }

    /**
     * AutoStep: prepareData
     *
     * @param bpc
     */
    public void prepareData(BaseProcessClass bpc) {
        List<RevocationDetailsDto> list = new LinkedList<>();
        HttpServletRequest request = bpc.request;
        String appId = ParamUtil.getMaskedString(request, "appId");
        RevocationDetailsDto revocationDetailsDto = revocationClient.getApplicationById(appId).getEntity();
        list.add(revocationDetailsDto);
        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_REVOCATION_DETAIL, (Serializable) list);
        //get history list
        List<BsbRoutingHistoryDto> historyDtoList = revocationClient.getAllHistory().getEntity();
        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_PROCESSING_HISTORY, (Serializable) historyDtoList);
    }

    /**
     * AutoStep: approve
     * Change the status of bsb_facility to Revoked
     * Change the status of bsb_application to Approved
     *
     * @param bpc
     */
    public void approve(BaseProcessClass bpc) {
        AODecisionDto aoDecisionDto = before(bpc);
        revocationClient.updateApplicationStatusById(aoDecisionDto.getRevocationDetailsDto().getApplicationId(),"BSBAPST009");
        revocationClient.updateFacilityStatusById(aoDecisionDto.getFacId(),"FACSTA007","APPRSTA003");
        revocationClient.saveApplicationMisc(aoDecisionDto.getRevocationDetailsDto());
        aoDecisionDto.getHistoryDto().setAppStatus("BSBAPST009");
        revocationClient.saveHistory(aoDecisionDto.getHistoryDto());
    }

    /**
     * AutoStep: reject
     *
     * Change the status of bsb_application to Rejected
     *
     * @param bpc
     */
    public void reject(BaseProcessClass bpc) {
        AODecisionDto aoDecisionDto = before(bpc);
        revocationClient.updateApplicationStatusById(aoDecisionDto.getRevocationDetailsDto().getApplicationId(),"BSBAPST008");
        revocationClient.saveApplicationMisc(aoDecisionDto.getRevocationDetailsDto());
        aoDecisionDto.getHistoryDto().setAppStatus("BSBAPST008");
        revocationClient.saveHistory(aoDecisionDto.getHistoryDto());
    }

    /**
     * AutoStep: routeback
     *
     * Change the status of bsb_application to Pending DO
     *
     * @param bpc
     */
    public void routebackToDO(BaseProcessClass bpc) {
        AODecisionDto aoDecisionDto = before(bpc);
        revocationClient.updateApplicationStatusById(aoDecisionDto.getRevocationDetailsDto().getApplicationId(),"BSBAPST003");
        revocationClient.saveApplicationMisc(aoDecisionDto.getRevocationDetailsDto());
        aoDecisionDto.getHistoryDto().setAppStatus("BSBAPST001");
        revocationClient.saveHistory(aoDecisionDto.getHistoryDto());
    }

    /**
     * AutoStep: routeToHM
     *
     * Change the status of bsb_application to Pending HM
     *
     * @param bpc
     */
    public void routeToHM(BaseProcessClass bpc) {
        AODecisionDto aoDecisionDto = before(bpc);
        revocationClient.updateApplicationStatusById(aoDecisionDto.getRevocationDetailsDto().getApplicationId(),"BSBAPST003");
        revocationClient.saveApplicationMisc(aoDecisionDto.getRevocationDetailsDto());
        aoDecisionDto.getHistoryDto().setAppStatus("BSBAPST003");
        revocationClient.saveHistory(aoDecisionDto.getHistoryDto());
    }

    private ApprovalOfficerQueryDto getSearchDto(HttpServletRequest request) {
        ApprovalOfficerQueryDto searchDto = (ApprovalOfficerQueryDto) ParamUtil.getSessionAttr(request, RevocationConstants.PARAM_APPLICATION_SEARCH);
        return searchDto == null ? getDefaultSearchDto() : searchDto;
    }

    private ApprovalOfficerQueryDto getDefaultSearchDto() {
        ApprovalOfficerQueryDto dto = new ApprovalOfficerQueryDto();
        dto.clearAllFields();
        dto.defaultPaging();
        return dto;
    }

    private AODecisionDto before(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        List<RevocationDetailsDto> list=(List<RevocationDetailsDto>) ParamUtil.getSessionAttr(request,RevocationConstants.PARAM_REVOCATION_DETAIL);
        String reason = ParamUtil.getString(request, RevocationConstants.PARAM_REASON);
        String remarks = ParamUtil.getString(request, RevocationConstants.PARAM_DOREMARKS);
        String appId="";
        String facId="";
        String applicationNo="";
        for (RevocationDetailsDto revocationDetailsDto : list) {
            appId = revocationDetailsDto.getApplicationId();
            facId = revocationDetailsDto.getFacilityId();
            applicationNo=revocationDetailsDto.getApplicationNo();
        }

        RevocationDetailsDto revocationDetailsDto = new RevocationDetailsDto();
        revocationDetailsDto.setRemarks(remarks);
        revocationDetailsDto.setReasonContent(reason);
        revocationDetailsDto.setApplicationId(appId);

        //get user name
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        BsbRoutingHistoryDto historyDto=new BsbRoutingHistoryDto();
        historyDto.setActionBy(loginContext.getUserName());
        historyDto.setInternalRemarks(revocationDetailsDto.getRemarks());
        historyDto.setApplicationNo(applicationNo);

        //Deposit the temporary DTO
        AODecisionDto aoDecisionDto=new AODecisionDto();
        aoDecisionDto.setFacId(facId);
        aoDecisionDto.setRevocationDetailsDto(revocationDetailsDto);
        aoDecisionDto.setHistoryDto(historyDto);

        return aoDecisionDto;
    }

}
