package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.AuditClient;
import sg.gov.moh.iais.egp.bsb.client.BiosafetyEnquiryClient;
import sg.gov.moh.iais.egp.bsb.client.RevocationClient;
import sg.gov.moh.iais.egp.bsb.constant.AuditConstants;
import sg.gov.moh.iais.egp.bsb.constant.RevocationConstants;
import sg.gov.moh.iais.egp.bsb.dto.BsbEmailParam;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.revocation.FacilityQueryDto;
import sg.gov.moh.iais.egp.bsb.dto.revocation.FacilityQueryResultDto;
import sg.gov.moh.iais.egp.bsb.entity.*;
import sg.gov.moh.iais.egp.bsb.helper.BsbNotificationHelper;
import sg.gov.moh.iais.egp.bsb.util.JoinAddress;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.ParseException;
import java.util.*;

import static sg.gov.moh.iais.egp.bsb.constant.EmailConstants.MSG_TEMPLATE_REVOCATION_AO_APPROVED;
import static sg.gov.moh.iais.egp.bsb.constant.ResponseConstants.ERROR_INFO_ERROR_MSG;

/**
 * @author Zhu Tangtang
 */
@Delegator(value = "DORevocationDelegator")
@Slf4j
public class DORevocationDelegator {
    @Autowired
    private RevocationClient revocationClient;

    @Autowired
    private BsbNotificationHelper bsbNotificationHelper;

    @Autowired
    private BiosafetyEnquiryClient biosafetyEnquiryClient;

    @Autowired
    private AuditClient auditClient;

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
        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_REVOCATION_DETAIL, null);
    }

    /**
     * AutoStep: prepareData
     *
     * @param bpc
     */
    public void prepareFacilityListData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacilityQueryDto searchDto=getSearchDto(request);
        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_FACILITY_SEARCH, searchDto);
        // call API to get searched data
        ResponseDto<FacilityQueryResultDto> searchResult = revocationClient.queryActiveFacility(searchDto);

        if (searchResult.ok()) {
            ParamUtil.setRequestAttr(request, RevocationConstants.KEY_APPLICATION_PAGE_INFO, searchResult.getEntity().getPageInfo());
            List<Facility> facilities = searchResult.getEntity().getTasks();
            //get facilityId
//            for (Facility facility : facilities) {
//                FacilityActivity activity = revocationClient.getFacilityActivityByApplicationId(application.getId()).getEntity();
//                activityList = auditClient.getFacilityActivityByFacilityId(audit.getFacility().getId()).getEntity();
//                audit.getFacility().setFacilityActivities(activityList);
//                application.getFacility().setActiveType(activity.getActivityType());
//            }
            ParamUtil.setRequestAttr(request, RevocationConstants.KEY_APPLICATION_DATA_LIST, facilities);
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
        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_FACILITY_SEARCH, null);
        FacilityQueryDto searchDto = getSearchDto(request);
        searchDto.clearAllFields();
        String facilityName = ParamUtil.getString(request, RevocationConstants.PARAM_FACILITY_NAME);
        String facilityClassification = ParamUtil.getString(request, RevocationConstants.PARAM_FACILITY_CLASSIFICATION);
        String facilityType = ParamUtil.getString(request, RevocationConstants.PARAM_FACILITY_TYPE);

        searchDto.setFacilityName(facilityName);
        searchDto.setFacilityClassification(facilityClassification);
        searchDto.setActiveType(facilityType);
        searchDto.setPage(0);

//        String validateStatus = "appDate";
//        ValidationResult vResult = WebValidationHelper.validateProperty(searchDto,validateStatus);
//        if(vResult != null && vResult.isHasErrors()){
//            Map<String,String> errorMap = vResult.retrieveAll();
//            ParamUtil.setRequestAttr(request, ProcessContants.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
//        }
        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_FACILITY_SEARCH, searchDto);
    }

    /**
     * AutoStep: doPaging
     *
     * @param bpc
     */
    public void page(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacilityQueryDto searchDto = getSearchDto(request);
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
        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_FACILITY_SEARCH, searchDto);
    }

    /**
     * AutoStep: doSorting
     *
     * @param bpc
     */
    public void sort(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacilityQueryDto searchDto = getSearchDto(request);
        String field = ParamUtil.getString(request, RevocationConstants.KEY_ACTION_VALUE);
        String sortType = ParamUtil.getString(request, RevocationConstants.KEY_ACTION_ADDT);
        searchDto.changeSort(field, sortType);
        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_FACILITY_SEARCH, searchDto);
    }

    /**
     * AutoStep: prepareData
     *
     * @param bpc
     */
    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,RevocationConstants.FACILITY,null);
        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_REVOCATION_DETAIL, null);
        ParamUtil.setSessionAttr(request,RevocationConstants.AUDIT_DOC_DTO, null);
        ParamUtil.setSessionAttr(request,RevocationConstants.FLAG,null);

        String from = ParamUtil.getRequestString(request,RevocationConstants.FROM);

        if (StringUtil.isNotEmpty(from)) {
            if (from.equals(RevocationConstants.APP)) {
                List<Application> list = new ArrayList<>();
                String appId = ParamUtil.getMaskedString(request, RevocationConstants.PARAM_APP_ID);
                Application application = revocationClient.getApplicationById(appId).getEntity();
                //Do address processing
                String address = JoinAddress.joinAddress(application);
                application.getFacility().setFacilityAddress(address);

                FacilityActivity activity = revocationClient.getFacilityActivityByApplicationId(application.getId()).getEntity();
                application.getFacility().setActiveType(activity.getActivityType());

                list.add(application);
                ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_REVOCATION_DETAIL, (Serializable) list);
                ParamUtil.setSessionAttr(request, RevocationConstants.FACILITY, application.getFacility());
                ParamUtil.setSessionAttr(request,RevocationConstants.FLAG,RevocationConstants.APP);
            }
            if(from.equals(RevocationConstants.FAC)){
                String facId = ParamUtil.getMaskedString(request, RevocationConstants.PARAM_FACILITY_ID);
                Facility facility = auditClient.getFacilityById(facId).getEntity();

                Application application=new Application();
                application.setFacility(facility);
                String address = JoinAddress.joinAddress(application);
                facility.setFacilityAddress(address);

                List<FacilityActivity> activities = facility.getFacilityActivities();
                facility.setFacilityActivities(activities);
                ParamUtil.setSessionAttr(request, RevocationConstants.FACILITY, facility);
                ParamUtil.setSessionAttr(request,RevocationConstants.FLAG,RevocationConstants.FAC);
            }
        }
    }

    /**
     * AutoStep: doCreate
     *
     * @param bpc
     */
    public void save(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

        String facilityId = ParamUtil.getString(request, RevocationConstants.PARAM_FACILITY_ID);

        Application resultDto = new Application();
        Facility facility = new Facility();
        facility.setId(facilityId);
        resultDto.setFacility(facility);
        resultDto.setAppType(RevocationConstants.PARAM_APPLICATION_TYPE_REVOCATION);
        //
        resultDto.setProcessType(RevocationConstants.PARAM_PROCESS_TYPE_FACILITY_REGISTRATION);
        //
        resultDto.setStatus(RevocationConstants.PARAM_APPLICATION_STATUS_PENDING_AO);
        resultDto.setApplicationDt(new Date());

        FeignResponseEntity<Application> result = revocationClient.saveApplication(resultDto);

        String reason = ParamUtil.getString(request, RevocationConstants.PARAM_REASON);
        String remarks = ParamUtil.getString(request, RevocationConstants.PARAM_DOREMARKS);

        ApplicationMisc miscDto = new ApplicationMisc();
        Application app=new Application();
        app.setId(result.getEntity().getId());
        miscDto.setRemarks(remarks);
        miscDto.setReasonContent(reason);
        miscDto.setApplication(app);
        miscDto.setReason(RevocationConstants.PARAM_REASON_TYPE_DO);

        ResponseDto<ApplicationMisc> miscResponseDto= revocationClient.saveApplicationMisc(miscDto).getEntity();
        if("INVALID_ARGS".equals(miscResponseDto.getErrorCode())) {
            ParamUtil.setRequestAttr(request, ERROR_INFO_ERROR_MSG, miscResponseDto.getErrorInfos().get(ERROR_INFO_ERROR_MSG));
        }

        //get user name
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);

        RoutingHistory historyDto=new RoutingHistory();
        historyDto.setAppStatus(result.getEntity().getStatus());
        historyDto.setActionBy(loginContext.getUserName());
        historyDto.setInternalRemarks(miscDto.getRemarks());
        historyDto.setApplicationNo(result.getEntity().getApplicationNo());
        revocationClient.saveHistory(historyDto);

        List<Application> list = (List<Application>) ParamUtil.getSessionAttr(request, RevocationConstants.PARAM_REVOCATION_DETAIL);
        String flag = (String) ParamUtil.getSessionAttr(request,RevocationConstants.FLAG);
        BsbEmailParam bsbEmailParam = new BsbEmailParam();
        if (flag.equals(RevocationConstants.APP)) {
            for (Application application : list) {
                bsbEmailParam.setMsgTemplateId(MSG_TEMPLATE_REVOCATION_AO_APPROVED);
                bsbEmailParam.setRefId(application.getFacility().getId());
                bsbEmailParam.setRefIdType("facId");
                bsbEmailParam.setQueryCode("1");
                bsbEmailParam.setReqRefNum("1");
                Map map = new HashMap();
                map.put("applicationNo", application.getApplicationNo());
                map.put("ApprovalNo", "Approval001");
                if (application.getProcessType().equals(RevocationConstants.PARAM_PROCESS_TYPE_APPROVAL_TO_POSSESS)
                        || application.getProcessType().equals(RevocationConstants.PARAM_PROCESS_TYPE_APPROVAL_TO_LSP)
                        || application.getProcessType().equals(RevocationConstants.PARAM_PROCESS_TYPE_SPECIAL_APPROVAL_TO_HANDLE)) {
                    map.put("Type", application.getFacility().getApprovalType());
                }
                if (application.getProcessType().equals(RevocationConstants.PARAM_PROCESS_TYPE_FACILITY_REGISTRATION)) {
                    map.put("Type", application.getFacility().getFacilityType());
                }
                if (application.getProcessType().equals(RevocationConstants.PARAM_PROCESS_TYPE_AFC_REGISTRATION)) {
                    map.put("Type", MasterCodeUtil.getCodeDesc(RevocationConstants.PARAM_PROCESS_TYPE_AFC_REGISTRATION));
                }
                map.put("officer", loginContext.getUserName());
                Map subMap = new HashMap();
                subMap.put("applicationNo", application.getApplicationNo());
                bsbEmailParam.setMsgSubject(subMap);
                bsbEmailParam.setMsgContent(map);
            }
            bsbNotificationHelper.sendNotification(bsbEmailParam);
        }
        if (flag.equals(RevocationConstants.FAC)) {

        }
    }

    /**
     * AutoStep: updateNum
     *
     * @param bpc
     */
    public void updateNum(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
//        List<Application> list=new LinkedList<>();
//        Application application = revocationClient.getApplicationById("ED1354B8-57FA-EB11-BE6E-000C298D317C").getEntity();
        //Do address processing
//        String address = JoinAddress.joinAddress(application);
//        application.getFacility().setFacilityAddress(address);
//        list.add(application);
//        ParamUtil.setRequestAttr(request, RevocationConstants.PARAM_REVOCATION_DETAIL, list);
        //TODO update inventory method
    }

    private FacilityQueryDto getSearchDto(HttpServletRequest request) {
        FacilityQueryDto searchDto = (FacilityQueryDto) ParamUtil.getSessionAttr(request, RevocationConstants.PARAM_FACILITY_SEARCH);
        return searchDto == null ? getDefaultSearchDto() : searchDto;
    }

    private FacilityQueryDto getDefaultSearchDto() {
        FacilityQueryDto dto = new FacilityQueryDto();
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
