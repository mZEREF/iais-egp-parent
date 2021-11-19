package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.*;
import sg.gov.moh.iais.egp.bsb.constant.AuditConstants;
import sg.gov.moh.iais.egp.bsb.constant.RevocationConstants;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.AuditDocDto;
import sg.gov.moh.iais.egp.bsb.dto.revocation.ApprovalQueryDto;
import sg.gov.moh.iais.egp.bsb.dto.revocation.ApprovalQueryResultDto;
import sg.gov.moh.iais.egp.bsb.dto.revocation.SubmitRevokeDto;
import sg.gov.moh.iais.egp.bsb.dto.revocation.ViewSelectedRevokeApplicationDto;
import sg.gov.moh.iais.egp.bsb.entity.*;
import sg.gov.moh.iais.egp.bsb.helper.BsbNotificationHelper;
import sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static sg.gov.moh.iais.egp.bsb.constant.RevocationConstants.STAGE_PROCESSING;

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
    private AuditClientBE auditClientBE;

    @Autowired
    private DocClient docClient;
    @Autowired
    FeApplicationClient feApplicationClient;

    /**
     * StartStep: startStep
     */
    public void start(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction(RevocationConstants.MODULE_REVOCATION, RevocationConstants.FUNCTION_REVOCATION);
        HttpServletRequest request = bpc.request;
        IaisEGPHelper.clearSessionAttr(request, RevocationConstants.class);
        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_REVOCATION_DETAIL, null);
    }

    /**
     * AutoStep: prepareData
     * Temporarily disabled
     * enter from Biosafety enquiry
     * approval list
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
     * Temporarily disabled
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
     * Temporarily disabled
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
     * Temporarily disabled
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
                String appId = ParamUtil.getRequestString(request, RevocationConstants.PARAM_APP_ID);
                appId = MaskUtil.unMaskValue("id", appId);
                ViewSelectedRevokeApplicationDto dto = revocationClient.getRevokeDetailByApplicationId(appId).getEntity();
                Application application = dto.getApplication();
                String processType = application.getProcessType();
                Facility facility = new Facility();
                Approval approval;
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
                ParamUtil.setSessionAttr(request, RevocationConstants.BACK, RevocationConstants.REVOCATION_TASK_LIST);
            }
            if (from.equals(RevocationConstants.FAC)) {
                String approvalId = ParamUtil.getRequestString(request, RevocationConstants.PARAM_APPROVAL_ID);
                approvalId = MaskUtil.unMaskValue("id", approvalId);
                Approval approval = revocationClient.getApprovalById(approvalId).getEntity();
                Facility facility = new Facility();
                if (approval.getProcessType().equals(RevocationConstants.PARAM_PROCESS_TYPE_FACILITY_REGISTRATION)) {
                    //join with activity
                    List<FacilityActivity> activities = approval.getFacilityActivities();
                    if (!CollectionUtils.isEmpty(activities)) {
                        facility = activities.get(0).getFacility();
                    }
                } else if (approval.getProcessType().equals(RevocationConstants.PARAM_PROCESS_TYPE_AFC_REGISTRATION)) {
                    //join with bsb_facility_certifier_reg
                } else {
                    //join with BA/T
                    List<FacilityBiologicalAgent> agents = approval.getFacilityBiologicalAgents();
                    if (!CollectionUtils.isEmpty(agents)) {
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
     */
    public void save(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

        String reason = ParamUtil.getString(request, RevocationConstants.PARAM_REASON);
        String remarks = ParamUtil.getString(request, RevocationConstants.PARAM_DOREMARKS);
        String flag = (String) ParamUtil.getSessionAttr(request, RevocationConstants.FLAG);
        SubmitRevokeDto submitRevokeDto = new SubmitRevokeDto();
        //get user name
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        submitRevokeDto.setLoginUser(loginContext.getUserName());
        if (flag.equals(RevocationConstants.FAC)) {
            Approval approval = (Approval) ParamUtil.getSessionAttr(request, RevocationConstants.APPROVAL);
            String processType = approval.getProcessType();
            submitRevokeDto.setProcessType(processType);
            submitRevokeDto.setReasonContent(reason);
            submitRevokeDto.setReason(RevocationConstants.PARAM_REASON_TYPE_DO);
            submitRevokeDto.setAppType(RevocationConstants.PARAM_APPLICATION_TYPE_REVOCATION);
            submitRevokeDto.setStatus(RevocationConstants.PARAM_APPLICATION_STATUS_PENDING_AO);
            submitRevokeDto.setApplicationDt(new Date());
            submitRevokeDto.setRemarks(remarks);
            submitRevokeDto.setApprovalId(approval.getId());
            revocationClient.saveRevokeApplication(submitRevokeDto);
        }
        if (flag.equals(RevocationConstants.APP)) {
            Application application = (Application) ParamUtil.getSessionAttr(request, RevocationConstants.PARAM_APPLICATION);
            submitRevokeDto.setStatus(RevocationConstants.PARAM_APPLICATION_STATUS_PENDING_AO);
            submitRevokeDto.setApplicationDt(new Date());
            submitRevokeDto.setAppId(application.getId());
            revocationClient.saveRevokeApplication(submitRevokeDto);
        }
    }

    /**
     * AutoStep: updateNum
     */
    public void updateInventory(BaseProcessClass bpc) {
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
        List<SelectOption> selectModel = new ArrayList<>();
        for (String facName : facNames) {
            selectModel.add(new SelectOption(facName, facName));
        }
        ParamUtil.setRequestAttr(request, AuditConstants.PARAM_FACILITY_NAME, selectModel);
    }

}
