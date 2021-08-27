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
import sg.gov.moh.iais.egp.bsb.client.ProcessClient;
import sg.gov.moh.iais.egp.bsb.constant.RevocationConstants;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.revocation.*;
import sg.gov.moh.iais.egp.bsb.client.RevocationClient;
import sg.gov.moh.iais.egp.bsb.entity.*;
import sg.gov.moh.iais.egp.bsb.util.JoinAddress;
import sg.gov.moh.iais.egp.bsb.util.JoinBiologicalName;
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

    @Autowired
    private ProcessClient processClient;

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

        if (searchResult.ok()) {
            ParamUtil.setRequestAttr(request, KEY_APPLICATION_PAGE_INFO, searchResult.getEntity().getPageInfo());
            List<Application> applications = searchResult.getEntity().getTasks();
            //get facilityId
            String facId="";
            for (Application application : applications) {
                application.getFacility().setFacilityAddress(JoinAddress.joinAddress(application));
                facId=application.getFacility().getId();
                List<FacilitySchedule> facilityScheduleList = application.getFacility().getFacilitySchedules();
                List<Biological> biologicalList = new ArrayList<>();
                //get Biological by facilityIds
                if (facilityScheduleList != null && facilityScheduleList.size() > 0){
                    for (int i = 0; i < facilityScheduleList.size(); i++) {
                        List<FacilityBiologicalAgent> facilityBiologicalAgentList = facilityScheduleList.get(i).getFacilityBiologicalAgents();
                        if (facilityBiologicalAgentList != null && facilityBiologicalAgentList.size() > 0){
                            for (int j = 0; j < facilityBiologicalAgentList.size(); j++) {
                                String biologicalId = facilityBiologicalAgentList.get(j).getBiologicalId();
                                biologicalList.add(processClient.getBiologicalById(biologicalId).getEntity());
                            }
                        }
                    }
                }
                String bioNames = JoinBiologicalName.joinBiologicalName(biologicalList);
                application.setBiologicalName(bioNames);
            }
            ParamUtil.setRequestAttr(request, KEY_APPLICATION_DATA_LIST, applications);
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
        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_APPLICATION_SEARCH, null);
        ApprovalOfficerQueryDto searchDto = getSearchDto(request);
        searchDto.clearAllFields();
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
            String[] strArr = facilityAddress.split(" ");
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
        List<Application> list = new LinkedList<>();
        HttpServletRequest request = bpc.request;

        String appId = ParamUtil.getMaskedString(request, RevocationConstants.PARAM_APP_ID);
        Application application = revocationClient.getApplicationById(appId).getEntity();
        List<ApplicationMisc> applicationMiscs=application.getAppMiscs();

        String address = JoinAddress.joinAddress(application);
        application.getFacility().setFacilityAddress(address);
        list.add(application);

        ParamUtil.setRequestAttr(request, RevocationConstants.PARAM_REVOCATION_DETAIL, list);
        ParamUtil.setRequestAttr(request, RevocationConstants.PARAM_APPLICATION_MISC_LIST, applicationMiscs);

        //get history list
        List<RoutingHistory> historyDtoList = revocationClient.getAllHistory().getEntity();
        ParamUtil.setRequestAttr(request, RevocationConstants.PARAM_PROCESSING_HISTORY,historyDtoList);
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
        revocationClient.updateApplicationStatusById(aoDecisionDto.getApplication().getId(),RevocationConstants.PARAM_APPLICATION_STATUS_APPROVE);
        revocationClient.updateFacilityStatusById(aoDecisionDto.getApplication().getFacility().getId(),RevocationConstants.PARAM_FACILITY_STATUS_REVOKED,RevocationConstants.PARAM_APPROVAL_STATUS_REVOKED);
        revocationClient.saveApplicationMisc(aoDecisionDto.getMisc());
        aoDecisionDto.getHistory().setAppStatus(RevocationConstants.PARAM_APPLICATION_STATUS_APPROVE);
        revocationClient.saveHistory(aoDecisionDto.getHistory());
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
        revocationClient.updateApplicationStatusById(aoDecisionDto.getApplication().getId(),RevocationConstants.PARAM_APPLICATION_STATUS_REJECT);
        revocationClient.saveApplicationMisc(aoDecisionDto.getMisc());
        aoDecisionDto.getHistory().setAppStatus(RevocationConstants.PARAM_APPLICATION_STATUS_REJECT);
        revocationClient.saveHistory(aoDecisionDto.getHistory());
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
        revocationClient.updateApplicationStatusById(aoDecisionDto.getApplication().getId(),RevocationConstants.PARAM_APPLICATION_STATUS_ROUTE_BACK);
        revocationClient.saveApplicationMisc(aoDecisionDto.getMisc());
        aoDecisionDto.getHistory().setAppStatus(RevocationConstants.PARAM_APPLICATION_STATUS_ROUTE_BACK);
        revocationClient.saveHistory(aoDecisionDto.getHistory());
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
        revocationClient.updateApplicationStatusById(aoDecisionDto.getApplication().getId(),RevocationConstants.PARAM_APPLICATION_STATUS_ROUTE_TO_HM);
        revocationClient.saveApplicationMisc(aoDecisionDto.getMisc());
        aoDecisionDto.getHistory().setAppStatus(RevocationConstants.PARAM_APPLICATION_STATUS_ROUTE_TO_HM);
        revocationClient.saveHistory(aoDecisionDto.getHistory());
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

        String appId = ParamUtil.getString(request, RevocationConstants.PARAM_APPLICATION_ID);
        Application application = revocationClient.getApplicationById(appId).getEntity();

        String reason = ParamUtil.getString(request, RevocationConstants.PARAM_REASON);
        String remarks = ParamUtil.getString(request, RevocationConstants.PARAM_AOREMARKS);

        ApplicationMisc misc = new ApplicationMisc();
        Application newApp = new Application();
        newApp.setId(appId);
        misc.setRemarks(remarks);
        misc.setReason(RevocationConstants.PARAM_REASON_TYPE_AO);
        misc.setReasonContent(reason);
        misc.setApplication(newApp);

        //get user name
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        RoutingHistory history=new RoutingHistory();
        history.setActionBy(loginContext.getUserName());
        history.setInternalRemarks(remarks);
        history.setApplicationNo(application.getApplicationNo());

        //Deposit the temporary DTO
        AODecisionDto aoDecisionDto=new AODecisionDto();
        aoDecisionDto.setApplication(application);
        aoDecisionDto.setMisc(misc);
        aoDecisionDto.setHistory(history);

        return aoDecisionDto;
    }

}
