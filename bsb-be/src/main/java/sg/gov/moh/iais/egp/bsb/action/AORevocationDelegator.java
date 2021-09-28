package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.DocClient;
import sg.gov.moh.iais.egp.bsb.client.ProcessClient;
import sg.gov.moh.iais.egp.bsb.constant.ProcessContants;
import sg.gov.moh.iais.egp.bsb.constant.RevocationConstants;
import sg.gov.moh.iais.egp.bsb.dto.BsbEmailParam;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.AuditDocDto;
import sg.gov.moh.iais.egp.bsb.dto.revocation.*;
import sg.gov.moh.iais.egp.bsb.client.RevocationClient;
import sg.gov.moh.iais.egp.bsb.entity.*;
import sg.gov.moh.iais.egp.bsb.helper.BsbNotificationHelper;
import sg.gov.moh.iais.egp.bsb.helper.SendNotificationHelper;
import sg.gov.moh.iais.egp.bsb.util.JoinAddress;
import sg.gov.moh.iais.egp.bsb.util.JoinBiologicalName;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static sg.gov.moh.iais.egp.bsb.constant.EmailConstants.*;

/**
 * @author Zhu Tangtang
 */
@Slf4j
@Delegator(value = "AORevocationDelegator")
public class AORevocationDelegator {
    @Autowired
    private RevocationClient revocationClient;

    @Autowired
    private ProcessClient processClient;

//    @Autowired
//    private SendNotificationHelper sendNotificationHelper;

    @Autowired
    private BsbNotificationHelper bsbNotificationHelper;
    @Autowired
    private DocClient docClient;

    /**
     * StartStep: startStep
     *
     * @param bpc
     * @throws IllegalAccessException
     */
    public void start(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction(RevocationConstants.MODULE_REVOCATION, RevocationConstants.FUNCTION_REVOCATION);
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
            ParamUtil.setRequestAttr(request, RevocationConstants.KEY_APPLICATION_PAGE_INFO, searchResult.getEntity().getPageInfo());
            List<Application> applications = searchResult.getEntity().getTasks();
            //get facilityId
            for (Application application : applications) {
                application.getFacility().setFacilityAddress(JoinAddress.joinAddress(application));
                FacilityActivity activity = revocationClient.getFacilityActivityByApplicationId(application.getId()).getEntity();
                if (activity!=null) {
                    application.getFacility().setActiveType(activity.getActivityType());
                    List<FacilitySchedule> facilityScheduleList = activity.getFacilitySchedules();
                    String bioNames = JoinBiologicalName.joinBiologicalName(facilityScheduleList, processClient);
                    application.setBiologicalName(bioNames);
                }
            }
            ParamUtil.setRequestAttr(request, RevocationConstants.KEY_APPLICATION_DATA_LIST, applications);
        } else {
            log.warn("get revocation application API doesn't return ok, the response is {}", searchResult);
            ParamUtil.setRequestAttr(request, RevocationConstants.KEY_APPLICATION_PAGE_INFO, PageInfo.emptyPageInfo(searchDto));
            ParamUtil.setRequestAttr(request, RevocationConstants.KEY_APPLICATION_DATA_LIST, new ArrayList<>());
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
        String applicationNo = ParamUtil.getString(request, RevocationConstants.PARAM_APPLICATION_NO);
        String applicationType = ParamUtil.getString(request, RevocationConstants.PARAM_APPLICATION_TYPE);
        String applicationStatus = ParamUtil.getString(request, RevocationConstants.PARAM_APPLICATION_STATUS);
        Date searchAppDateFrom = Formatter.parseDate(ParamUtil.getString(request, RevocationConstants.PARAM_SEARCH_APP_DATE_FROM));
        Date searchAppDateTo = Formatter.parseDate(ParamUtil.getString(request, RevocationConstants.PARAM_SEARCH_APP_DATE_TO));

        searchDto.setFacilityName(facilityName);
        searchDto.setFacilityClassification(facilityClassification);
        searchDto.setActivityType(facilityType);
        searchDto.setProcessType(processType);
        searchDto.setApplicationNo(applicationNo);
        searchDto.setApplicationType(applicationType);
        searchDto.setApplicationStatus(applicationStatus);
        searchDto.setFacilityAddress(facilityAddress);
        searchDto.setPage(0);
        searchDto.setSearchAppDateFrom(searchAppDateFrom);
        searchDto.setSearchAppDateTo(searchAppDateTo);

//        String validateStatus = "appDate";
//        ValidationResult vResult = WebValidationHelper.validateProperty(searchDto,validateStatus);
//        if(vResult != null && vResult.isHasErrors()){
//            Map<String,String> errorMap = vResult.retrieveAll();
//            ParamUtil.setRequestAttr(request, ProcessContants.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
//        }
        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_APPLICATION_SEARCH, searchDto);
    }

    /**
     * AutoStep: doPaging
     *
     * @param bpc
     */
    public void page(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ApprovalOfficerQueryDto searchDto = getSearchDto(request);
        String actionValue = ParamUtil.getString(request, RevocationConstants.KEY_ACTION_VALUE);
        switch (actionValue) {
            case "changeSize":
                int pageSize = ParamUtil.getInt(request, RevocationConstants.KEY_PAGE_SIZE);
                searchDto.setPage(0);
                searchDto.setSize(pageSize);
                break;
            case "changePage":
                int pageNo = ParamUtil.getInt(request, RevocationConstants.KEY_PAGE_NO);
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
        String field = ParamUtil.getString(request, RevocationConstants.KEY_ACTION_VALUE);
        String sortType = ParamUtil.getString(request, RevocationConstants.KEY_ACTION_ADDT);
        searchDto.changeSort(field, sortType);
        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_APPLICATION_SEARCH, searchDto);
    }

    /**
     * AutoStep: prepareData
     *
     * @param bpc
     */
    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_APPLICATION, null);
        ParamUtil.setSessionAttr(request,RevocationConstants.AUDIT_DOC_DTO, null);
        ParamUtil.setSessionAttr(request, RevocationConstants.FLAG, null);
        ParamUtil.setSessionAttr(request, RevocationConstants.BACK, null);
        String appId = ParamUtil.getRequestString(request,RevocationConstants.PARAM_APP_ID);
        appId = MaskUtil.unMaskValue("id",appId);

        Application application = revocationClient.getApplicationById(appId).getEntity();
        List<ApplicationMisc> applicationMiscs=application.getAppMiscs();

        String address = JoinAddress.joinAddress(application);
        application.getFacility().setFacilityAddress(address);

        FacilityActivity activity = revocationClient.getFacilityActivityByApplicationId(application.getId()).getEntity();
        if (activity != null) {
            application.getFacility().setActiveType(activity.getActivityType());
        }
        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_APPLICATION, application);
        ParamUtil.setSessionAttr(request, RevocationConstants.FACILITY, application.getFacility());
        ParamUtil.setRequestAttr(request, RevocationConstants.PARAM_APPLICATION_MISC_LIST, applicationMiscs);

        //get history list
        List<RoutingHistory> historyDtoList = revocationClient.getAllHistory().getEntity();
        ParamUtil.setRequestAttr(request, RevocationConstants.PARAM_PROCESSING_HISTORY,historyDtoList);

        List<FacilityDoc> facilityDocList = docClient.getFacilityDocByFacId(application.getFacility().getId()).getEntity();
        List<FacilityDoc> docList = new ArrayList<>();
        for (FacilityDoc facilityDoc : facilityDocList) {
            //这里拿不到，只能拿到当前用户名
            String submitByName = IaisEGPHelper.getCurrentAuditTrailDto().getMohUserId();
            facilityDoc.setSubmitByName(submitByName);
            docList.add(facilityDoc);
        }
        AuditDocDto auditDocDto = new AuditDocDto();
        auditDocDto.setFacilityDocs(docList);

        ParamUtil.setSessionAttr(request,RevocationConstants.AUDIT_DOC_DTO, auditDocDto);
        ParamUtil.setSessionAttr(request, RevocationConstants.FLAG, RevocationConstants.APP);
        ParamUtil.setSessionAttr(request, RevocationConstants.BACK, RevocationConstants.REVOCATION_TASK_LIST);
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
        revocationClient.updateApplicationStatusById(aoDecisionDto.getApplication().getId(),RevocationConstants.PARAM_APPLICATION_STATUS_APPROVED);
        revocationClient.updateFacilityStatusById(aoDecisionDto.getApplication().getFacility().getId(),RevocationConstants.PARAM_FACILITY_STATUS_REVOKED,RevocationConstants.PARAM_APPROVAL_STATUS_REVOKED);
        revocationClient.saveApplicationMisc(aoDecisionDto.getMisc());
        aoDecisionDto.getHistory().setAppStatus(RevocationConstants.PARAM_APPLICATION_STATUS_APPROVED);
        revocationClient.saveHistory(aoDecisionDto.getHistory());

        //send notification
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date=dateFormat.format(new Date());
        String address = JoinAddress.joinAddress(aoDecisionDto.getApplication());

        BsbEmailParam bsbEmailParam = new BsbEmailParam();
        bsbEmailParam.setMsgTemplateId(MSG_TEMPLATE_REVOCATION_USER_APPROVED);
        bsbEmailParam.setRefId(aoDecisionDto.getApplication().getApplicationNo());
        bsbEmailParam.setRefIdType("appNo");
        bsbEmailParam.setQueryCode("1");
        bsbEmailParam.setReqRefNum("1");
        Map map = new HashMap();
        map.put("applicationNo", aoDecisionDto.getApplication().getApplicationNo());
        map.put("FacilityAddress",address);
        map.put("FacilityName",aoDecisionDto.getApplication().getFacility().getFacilityName());
        map.put("Date",date);
        map.put("Reason",aoDecisionDto.getMisc().getReasonContent());
        Map subMap = new HashMap();
        subMap.put("applicationNo", aoDecisionDto.getApplication().getApplicationNo());
        bsbEmailParam.setMsgSubject(subMap);
        bsbEmailParam.setMsgContent(map);
        bsbNotificationHelper.sendNotification(bsbEmailParam);
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
        revocationClient.updateApplicationStatusById(aoDecisionDto.getApplication().getId(),RevocationConstants.PARAM_APPLICATION_STATUS_REJECTED);
        revocationClient.saveApplicationMisc(aoDecisionDto.getMisc());
        aoDecisionDto.getHistory().setAppStatus(RevocationConstants.PARAM_APPLICATION_STATUS_REJECTED);
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
        revocationClient.updateApplicationStatusById(aoDecisionDto.getApplication().getId(),RevocationConstants.PARAM_APPLICATION_STATUS_PENDING_DO);
        revocationClient.saveApplicationMisc(aoDecisionDto.getMisc());
        aoDecisionDto.getHistory().setAppStatus(RevocationConstants.PARAM_APPLICATION_STATUS_PENDING_DO);
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
        revocationClient.updateApplicationStatusById(aoDecisionDto.getApplication().getId(),RevocationConstants.PARAM_APPLICATION_STATUS_PENDING_HM);
        revocationClient.saveApplicationMisc(aoDecisionDto.getMisc());
        aoDecisionDto.getHistory().setAppStatus(RevocationConstants.PARAM_APPLICATION_STATUS_PENDING_HM);
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
        Application application =(Application)ParamUtil.getSessionAttr(request, RevocationConstants.PARAM_APPLICATION);

        String reason = ParamUtil.getString(request, RevocationConstants.PARAM_REASON);
        String remarks = ParamUtil.getString(request, RevocationConstants.PARAM_AOREMARKS);

        ApplicationMisc misc = new ApplicationMisc();
        Application newApp = new Application();
        newApp.setId(application.getId());
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
