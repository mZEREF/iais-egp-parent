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
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import sg.gov.moh.iais.egp.bsb.client.*;
import sg.gov.moh.iais.egp.bsb.constant.AuditConstants;
import sg.gov.moh.iais.egp.bsb.constant.RevocationConstants;
import sg.gov.moh.iais.egp.bsb.dto.BsbEmailParam;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.AuditDocDto;
import sg.gov.moh.iais.egp.bsb.dto.revocation.ApprovalQueryDto;
import sg.gov.moh.iais.egp.bsb.dto.revocation.ApprovalQueryResultDto;
import sg.gov.moh.iais.egp.bsb.dto.revocation.SubmitRevokeDto;
import sg.gov.moh.iais.egp.bsb.dto.revocation.ViewSelectedRevokeApplicationDto;
import sg.gov.moh.iais.egp.bsb.entity.*;
import sg.gov.moh.iais.egp.bsb.helper.BsbNotificationHelper;
import sg.gov.moh.iais.egp.bsb.util.JoinParamUtil;
import sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private DocClient docClient;
    @Autowired
    FeApplicationClient feApplicationClient;

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
        selectOption(request);
        ApprovalQueryDto searchDto = getSearchDto(request);
        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_FACILITY_SEARCH, searchDto);
        // call API to get searched data
        ResponseDto<ApprovalQueryResultDto> searchResult = revocationClient.queryActiveApproval(searchDto);

        if (searchResult.ok()) {
            ParamUtil.setRequestAttr(request, RevocationConstants.KEY_APPLICATION_PAGE_INFO, searchResult.getEntity().getPageInfo());
            List<Approval> approvals = searchResult.getEntity().getTasks();
            for (Approval approval : approvals) {
                List<FacilityActivity> activities = approval.getFacilityActivities();
                List<FacilityBiologicalAgent> facilityBiologicalAgents = approval.getFacilityBiologicalAgents();
                Facility facility = new Facility();
                if (!activities.isEmpty()) {
                    facility = activities.get(0).getFacility();
                } else if (!facilityBiologicalAgents.isEmpty()) {
                    facility = facilityBiologicalAgents.get(0).getFacility();
                }
                if (!StringUtils.isEmpty(facility)) {
                    String address = TableDisplayUtil.getOneLineAddress(facility.getBlkNo(), facility.getStreetName(), facility.getFloorNo(),
                            facility.getUnitNo(), facility.getPostalCode());
                    facility.setFacilityAddress(address);
                }
                approval.setFacility(facility);
            }
            //get facilityId
            ParamUtil.setRequestAttr(request, RevocationConstants.KEY_APPLICATION_DATA_LIST, approvals);
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
    public void doSearch(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_FACILITY_SEARCH, null);
        ApprovalQueryDto searchDto = getSearchDto(request);
        searchDto.clearAllFields();
        String facilityName = ParamUtil.getString(request, RevocationConstants.PARAM_FACILITY_NAME);
        String facilityClassification = ParamUtil.getString(request, RevocationConstants.PARAM_FACILITY_CLASSIFICATION);
        String facilityType = ParamUtil.getString(request, RevocationConstants.PARAM_FACILITY_TYPE);

        searchDto.setFacilityName(facilityName);
        searchDto.setFacilityClassification(facilityClassification);
        searchDto.setActiveType(facilityType);
        searchDto.setPage(0);

        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_FACILITY_SEARCH, searchDto);
    }

    /**
     * AutoStep: doPaging
     *
     * @param bpc
     */
    public void page(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ApprovalQueryDto searchDto = getSearchDto(request);
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
        ApprovalQueryDto searchDto = getSearchDto(request);
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
        ParamUtil.setSessionAttr(request, RevocationConstants.FACILITY, null);
        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_REVOCATION_DETAIL, null);
        ParamUtil.setSessionAttr(request, RevocationConstants.AUDIT_DOC_DTO, null);
        ParamUtil.setSessionAttr(request, RevocationConstants.FLAG, null);
        ParamUtil.setSessionAttr(request, RevocationConstants.BACK, null);
        ParamUtil.setSessionAttr(request, RevocationConstants.AUDIT_DOC_DTO, null);
        ParamUtil.setSessionAttr(request, RevocationConstants.APPROVAL, null);

        String from = ParamUtil.getRequestString(request, RevocationConstants.FROM);

        if (StringUtil.isNotEmpty(from)) {
            if (from.equals(RevocationConstants.APP)) {
                String appId = ParamUtil.getMaskedString(request, RevocationConstants.PARAM_APP_ID);
                ViewSelectedRevokeApplicationDto dto = revocationClient.getRevokeDetailByApplicationId(appId).getEntity();
                Application application = dto.getApplication();
                String processType = application.getProcessType();
                Facility facility = new Facility();
                Approval approval = new Approval();
                String address = "";
                if (processType.equals(RevocationConstants.PARAM_PROCESS_TYPE_FACILITY_REGISTRATION)) {
                    //join with activity
                    approval = dto.getActivity().getApproval();
                    approval.setActiveType(dto.getActivity().getActivityType());
                    facility = dto.getActivity().getFacility();
                    facility.setActiveType(dto.getActivity().getActivityType());
                    if (!StringUtils.isEmpty(facility)) {
                        address = TableDisplayUtil.getOneLineAddress(facility.getBlkNo(), facility.getStreetName(), facility.getFloorNo(),
                                facility.getUnitNo(), facility.getPostalCode());
                        facility.setFacilityAddress(address);
                    }
                } else if (processType.equals(RevocationConstants.PARAM_PROCESS_TYPE_AFC_REGISTRATION)) {
                    //join with bsb_facility_certifier_reg
                    approval = dto.getFacilityCertifierReg().getApproval();
                } else {
                    //join with BA/T
                    approval = dto.getFacilityBiologicalAgent().getApproval();
                    approval.setActiveType(dto.getFacilityBiologicalAgent().getFacilityActivity().getActivityType());
                    facility = dto.getFacilityBiologicalAgent().getFacility();
                    facility.setActiveType(dto.getFacilityBiologicalAgent().getFacilityActivity().getActivityType());
                    if (!StringUtils.isEmpty(facility)) {
                        address = TableDisplayUtil.getOneLineAddress(facility.getBlkNo(), facility.getStreetName(), facility.getFloorNo(),
                                facility.getUnitNo(), facility.getPostalCode());
                        facility.setFacilityAddress(address);
                    }
                }
                approval.setFacility(facility);
                AuditDocDto auditDocDto = new AuditDocDto();
                if (!StringUtils.isEmpty(facility.getId())) {
                    List<FacilityDoc> facilityDocList = docClient.getFacilityDocByFacId(facility.getId()).getEntity();
                    List<FacilityDoc> docList = new ArrayList<>();
                    if (!facilityDocList.isEmpty()) {
                        for (FacilityDoc facilityDoc : facilityDocList) {
                            //todo You can only get the current user name
                            String submitByName = IaisEGPHelper.getCurrentAuditTrailDto().getMohUserId();
                            facilityDoc.setSubmitByName(submitByName);
                            docList.add(facilityDoc);
                        }
                    }
                    auditDocDto.setFacilityDocs(docList);
                }

                ParamUtil.setSessionAttr(request, RevocationConstants.AUDIT_DOC_DTO, auditDocDto);
                ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_APPLICATION, application);
                ParamUtil.setSessionAttr(request, RevocationConstants.FACILITY, facility);
                ParamUtil.setSessionAttr(request, RevocationConstants.APPROVAL, approval);
                ParamUtil.setSessionAttr(request, RevocationConstants.FLAG, RevocationConstants.APP);
                ParamUtil.setSessionAttr(request, RevocationConstants.BACK, RevocationConstants.REVOCATION_APPLICATION);
            }
            if (from.equals(RevocationConstants.FAC)) {
                String approvalId = ParamUtil.getMaskedString(request, RevocationConstants.PARAM_APPROVAL_ID);
                Approval approval = revocationClient.getApprovalById(approvalId).getEntity();
                Facility facility = new Facility();
                if (approval.getProcessType().equals(RevocationConstants.PARAM_PROCESS_TYPE_FACILITY_REGISTRATION)) {
                    //join with activity
                    List<FacilityActivity> activities = approval.getFacilityActivities();
                    if (!activities.isEmpty()) {
                        facility = activities.get(0).getFacility();
                    }
                } else if (approval.getProcessType().equals(RevocationConstants.PARAM_PROCESS_TYPE_AFC_REGISTRATION)) {
                    //join with bsb_facility_certifier_reg
                } else {
                    //join with BA/T
                    List<FacilityBiologicalAgent> agents = approval.getFacilityBiologicalAgents();
                    if (!agents.isEmpty()) {
                        facility = agents.get(0).getFacility();
                    }
                }
                if (!StringUtils.isEmpty(facility)) {
                    String address = TableDisplayUtil.getOneLineAddress(facility.getBlkNo(), facility.getStreetName(), facility.getFloorNo(),
                            facility.getUnitNo(), facility.getPostalCode());
                    facility.setFacilityAddress(address);
                }
                approval.setFacility(facility);

                ParamUtil.setSessionAttr(request, RevocationConstants.APPROVAL, approval);
                ParamUtil.setSessionAttr(request, RevocationConstants.FLAG, RevocationConstants.FAC);
                ParamUtil.setSessionAttr(request, RevocationConstants.FACILITY, facility);
                ParamUtil.setSessionAttr(request, RevocationConstants.BACK, RevocationConstants.REVOCATION_FACILITY);
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

        String flag = (String) ParamUtil.getSessionAttr(request, RevocationConstants.FLAG);
        Approval approval = (Approval) ParamUtil.getSessionAttr(request, RevocationConstants.APPROVAL);
        FeignResponseEntity<Application> result = null;
        if (flag.equals(RevocationConstants.FAC)) {
            Application application = new Application();

//            RestTemplate restTemplate = new RestTemplate();
//            String appNo=(String) restTemplate.getForEntity("http://bsb-fe-api/",null,String.class).getBody();

//            application.setApplicationNo(feApplicationClient.genApplicationNumber(RevocationConstants.APP_TYPE_REVOCATION).getEntity());
            application.setAppType(RevocationConstants.PARAM_APPLICATION_TYPE_REVOCATION);

            String processType = approval.getProcessType();
            application.setProcessType(processType);
            application.setStatus(RevocationConstants.PARAM_APPLICATION_STATUS_PENDING_AO);
            application.setApplicationDt(new Date());
            SubmitRevokeDto dto = new SubmitRevokeDto();
            dto.setApplication(application);
            dto.setApproval(approval);
            result = revocationClient.saveRevokeApplication(dto);
        }
        if (flag.equals(RevocationConstants.APP)) {
            Application application = (Application)ParamUtil.getSessionAttr(request, RevocationConstants.PARAM_APPLICATION);
            application.setStatus(RevocationConstants.PARAM_APPLICATION_STATUS_PENDING_AO);
            application.setApplicationDt(new Date());
            result = revocationClient.updateApplication(application);
        }

        String reason = ParamUtil.getString(request, RevocationConstants.PARAM_REASON);
        String remarks = ParamUtil.getString(request, RevocationConstants.PARAM_DOREMARKS);

        ApplicationMisc miscDto = new ApplicationMisc();
        miscDto.setRemarks(remarks);
        miscDto.setReasonContent(reason);
        miscDto.setApplication(result.getEntity());
        miscDto.setReason(RevocationConstants.PARAM_REASON_TYPE_DO);
        ResponseDto<ApplicationMisc> miscResponseDto = revocationClient.saveApplicationMisc(miscDto);
        if ("INVALID_ARGS".equals(miscResponseDto.getErrorCode())) {
            ParamUtil.setRequestAttr(request, ERROR_INFO_ERROR_MSG, miscResponseDto.getErrorInfos().get(ERROR_INFO_ERROR_MSG));
        }
        //get user name
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        Application application = result.getEntity();
        RoutingHistory historyDto = new RoutingHistory();
        historyDto.setAppStatus(RevocationConstants.PARAM_APPLICATION_STATUS_PENDING_AO);
        historyDto.setActionBy(loginContext.getUserName());
        historyDto.setInternalRemarks(miscDto.getRemarks());
        historyDto.setApplicationNo(application.getApplicationNo());
        revocationClient.saveHistory(historyDto);
//
//        BsbEmailParam bsbEmailParam = new BsbEmailParam();
//        bsbEmailParam.setMsgTemplateId(MSG_TEMPLATE_REVOCATION_AO_APPROVED);
//        bsbEmailParam.setRefId(application.getFacility().getId());
//        bsbEmailParam.setRefIdType("facId");
//        bsbEmailParam.setQueryCode("1");
//        bsbEmailParam.setReqRefNum("1");
//        Map<String,Object> map = new HashMap<>();
//        map.put("applicationNo", application.getApplicationNo());
//        map.put("ApprovalNo", "Approval001");
//        if (application.getProcessType().equals(RevocationConstants.PARAM_PROCESS_TYPE_APPROVAL_TO_POSSESS)
//                || application.getProcessType().equals(RevocationConstants.PARAM_PROCESS_TYPE_APPROVAL_TO_LSP)
//                || application.getProcessType().equals(RevocationConstants.PARAM_PROCESS_TYPE_SPECIAL_APPROVAL_TO_HANDLE)) {
//            map.put("Type", facility1.getApprovalType());
//        }
//        if (application.getProcessType().equals(RevocationConstants.PARAM_PROCESS_TYPE_FACILITY_REGISTRATION)) {
//            List<FacilityActivity> activityList = auditClient.getFacilityActivityByFacilityId(facility1.getId()).getEntity();
//            String activityType = "";
//            if (!activityList.isEmpty()) {
//                activityType = JoinParamUtil.joinActivityType(activityList);
//            }
//            map.put("Type", activityType);
//        }
//        if (application.getProcessType().equals(RevocationConstants.PARAM_PROCESS_TYPE_AFC_REGISTRATION)) {
//            map.put("Type", MasterCodeUtil.getCodeDesc(RevocationConstants.PARAM_PROCESS_TYPE_AFC_REGISTRATION));
//        }
//        map.put("officer", loginContext.getUserName());
//        Map<String,Object> subMap = new HashMap<>();
//        subMap.put("applicationNo", application.getApplicationNo());
//        bsbEmailParam.setMsgSubject(subMap);
//        bsbEmailParam.setMsgContent(map);
//        bsbNotificationHelper.sendNotification(bsbEmailParam);
    }

    /**
     * AutoStep: updateNum
     *
     * @param bpc
     */
    public void updateNum(BaseProcessClass bpc) {
        //TODO update inventory method
    }

    private ApprovalQueryDto getSearchDto(HttpServletRequest request) {
        ApprovalQueryDto searchDto = (ApprovalQueryDto) ParamUtil.getSessionAttr(request, RevocationConstants.PARAM_FACILITY_SEARCH);
        return searchDto == null ? getDefaultSearchDto() : searchDto;
    }

    private ApprovalQueryDto getDefaultSearchDto() {
        ApprovalQueryDto dto = new ApprovalQueryDto();
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
