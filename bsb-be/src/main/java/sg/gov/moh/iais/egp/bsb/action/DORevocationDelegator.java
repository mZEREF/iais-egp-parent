package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
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
import sg.gov.moh.iais.egp.bsb.constant.BioSafetyEnquiryConstants;
import sg.gov.moh.iais.egp.bsb.constant.RevocationConstants;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.AuditDocDto;
import sg.gov.moh.iais.egp.bsb.dto.enquiry.ApprovalResultDto;
import sg.gov.moh.iais.egp.bsb.dto.enquiry.EnquiryDto;
import sg.gov.moh.iais.egp.bsb.dto.revocation.SubmitRevokeDto;
import sg.gov.moh.iais.egp.bsb.dto.revocation.ViewSelectedRevokeApplicationDto;
import sg.gov.moh.iais.egp.bsb.entity.*;
import sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static sg.gov.moh.iais.egp.bsb.constant.BioSafetyEnquiryConstants.KEY_PAGE_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.RevocationConstants.*;

/**
 * @author Zhu Tangtang
 */
@Delegator(value = "DORevocationDelegator")
@Slf4j
public class DORevocationDelegator {
    @Autowired
    private RevocationClient revocationClient;

    @Autowired
    private BiosafetyEnquiryClient biosafetyEnquiryClient;

    @Autowired
    private DocClient docClient;
    @Autowired
    FeApplicationClient feApplicationClient;

    /**
     * StartStep: startStep
     */
    public void start(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction(MODULE_REVOCATION, FUNCTION_REVOCATION);
        HttpServletRequest request = bpc.request;
        IaisEGPHelper.clearSessionAttr(request, RevocationConstants.class);
        ParamUtil.setSessionAttr(request, PARAM_REVOCATION_DETAIL, null);
    }

    /**
     * AutoStep: prepareData
     * Temporarily disabled
     * enter from Biosafety enquiry
     * approval list
     */
    public void prepareFacilityListData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        EnquiryDto searchDto = getSearchDto(request);
        ParamUtil.setSessionAttr(request, PARAM_FACILITY_SEARCH, searchDto);
        // call API to get searched data

        ApprovalResultDto approvalResultDto = biosafetyEnquiryClient.getApproval(searchDto).getEntity();
        ParamUtil.setRequestAttr(request, KEY_APPLICATION_DATA_LIST, approvalResultDto.getBsbApproval());
        ParamUtil.setRequestAttr(request, BioSafetyEnquiryConstants.PARAM_APPROVAL_INFO_SEARCH, searchDto);
        ParamUtil.setRequestAttr(request, KEY_PAGE_INFO, approvalResultDto.getPageInfo());
        log.info(StringUtil.changeForLog(approvalResultDto.getBsbApproval().toString() + "==========facility"));


        ResponseDto<ApprovalResultDto> searchResult = biosafetyEnquiryClient.getApproval(searchDto);

        if (searchResult.ok()) {
            ParamUtil.setRequestAttr(request, KEY_APPLICATION_PAGE_INFO, searchResult.getEntity().getPageInfo());
            ParamUtil.setRequestAttr(request, KEY_APPLICATION_DATA_LIST, searchResult.getEntity().getBsbApproval());
        } else {
            log.warn("get revocation application API doesn't return ok, the response is {}", searchResult);
            ParamUtil.setRequestAttr(request, KEY_APPLICATION_PAGE_INFO, PageInfo.emptyPageInfo(searchDto));
            ParamUtil.setRequestAttr(request, KEY_APPLICATION_DATA_LIST, new ArrayList<>());
        }
    }

    /**
     * AutoStep: doSearch
     * Temporarily disabled
     */
    public void doSearch(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, PARAM_FACILITY_SEARCH, null);
        EnquiryDto searchDto = getSearchDto(request);
        searchDto.clearAllFields();
        String approvalStatus = ParamUtil.getString(request, PARAM_APPROVAL_STATUS);
        String approvalNo = ParamUtil.getString(request, PARAM_APPROVAL_NO);
        searchDto.setApprovalStatus(approvalStatus);
        searchDto.setApprovalNo(approvalNo);
        searchDto.setPage(0);

        ParamUtil.setSessionAttr(request, PARAM_FACILITY_SEARCH, searchDto);
    }

    /**
     * AutoStep: doPaging
     * Temporarily disabled
     */
    public void page(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        EnquiryDto searchDto = getSearchDto(request);
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
        ParamUtil.setSessionAttr(request, PARAM_FACILITY_SEARCH, searchDto);
    }

    /**
     * AutoStep: doSorting
     * Temporarily disabled
     */
    public void sort(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        EnquiryDto searchDto = getSearchDto(request);
        String field = ParamUtil.getString(request, KEY_ACTION_VALUE);
        String sortType = ParamUtil.getString(request, KEY_ACTION_ADDT);
        searchDto.changeSort(field, sortType);
        ParamUtil.setSessionAttr(request, PARAM_FACILITY_SEARCH, searchDto);
    }

    /**
     * AutoStep: prepareData
     */
    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, FACILITY, null);
        ParamUtil.setSessionAttr(request, PARAM_REVOCATION_DETAIL, null);
        ParamUtil.setSessionAttr(request, AUDIT_DOC_DTO, null);
        ParamUtil.setSessionAttr(request, FLAG, null);
        ParamUtil.setSessionAttr(request, BACK, null);
        ParamUtil.setSessionAttr(request, AUDIT_DOC_DTO, null);
        ParamUtil.setSessionAttr(request, APPROVAL, null);

        String from = ParamUtil.getRequestString(request, FROM);

        if (StringUtil.isNotEmpty(from)) {
            if (from.equals(APP)) {
                String appId = ParamUtil.getRequestString(request, PARAM_APP_ID);
                appId = MaskUtil.unMaskValue("id", appId);
                ViewSelectedRevokeApplicationDto dto = revocationClient.getRevokeDetailByApplicationId(appId).getEntity();
                Application application = dto.getApplication();
                String processType = application.getProcessType();
                Facility facility = new Facility();
                Approval approval;
                String address = "";
                if (processType.equals(PARAM_PROCESS_TYPE_FACILITY_REGISTRATION)) {
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
                } else if (processType.equals(PARAM_PROCESS_TYPE_AFC_REGISTRATION)) {
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
                    if (!CollectionUtils.isEmpty(facilityDocList)) {
                        for (FacilityDoc facilityDoc : facilityDocList) {
                            String submitByName = IaisEGPHelper.getCurrentAuditTrailDto().getMohUserId();
                            facilityDoc.setSubmitByName(submitByName);
                            docList.add(facilityDoc);
                        }
                    }
                    auditDocDto.setFacilityDocs(docList);
                }

                ParamUtil.setSessionAttr(request, AUDIT_DOC_DTO, auditDocDto);
                ParamUtil.setSessionAttr(request, PARAM_APPLICATION, application);
                ParamUtil.setSessionAttr(request, FACILITY, facility);
                ParamUtil.setSessionAttr(request, APPROVAL, approval);
                ParamUtil.setSessionAttr(request, FLAG, APP);
                ParamUtil.setSessionAttr(request, BACK, REVOCATION_TASK_LIST);
            }
            if (from.equals(FAC)) {
                String approvalId = ParamUtil.getRequestString(request, PARAM_APPROVAL_ID);
                approvalId = MaskUtil.unMaskValue("id", approvalId);
                Approval approval = revocationClient.getApprovalById(approvalId).getEntity();
                Facility facility = new Facility();
                if (approval.getProcessType().equals(PARAM_PROCESS_TYPE_FACILITY_REGISTRATION)) {
                    //join with activity
                    List<FacilityActivity> activities = approval.getFacilityActivities();
                    if (!CollectionUtils.isEmpty(activities)) {
                        facility = activities.get(0).getFacility();
                    }
                } else if (approval.getProcessType().equals(PARAM_PROCESS_TYPE_AFC_REGISTRATION)) {
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

                ParamUtil.setSessionAttr(request, APPROVAL, approval);
                ParamUtil.setSessionAttr(request, FLAG, FAC);
                ParamUtil.setSessionAttr(request, FACILITY, facility);
                ParamUtil.setSessionAttr(request, BACK, REVOCATION_FACILITY);
            }
        }
    }

    /**
     * AutoStep: doCreate
     */
    public void save(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

        String reason = ParamUtil.getString(request, PARAM_REASON);
        String remarks = ParamUtil.getString(request, PARAM_DOREMARKS);
        String flag = (String) ParamUtil.getSessionAttr(request, FLAG);
        SubmitRevokeDto submitRevokeDto = new SubmitRevokeDto();
        //get user name
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        submitRevokeDto.setLoginUser(loginContext.getUserName());
        if (flag.equals(FAC)) {
            Approval approval = (Approval) ParamUtil.getSessionAttr(request, APPROVAL);
            String processType = approval.getProcessType();
            submitRevokeDto.setProcessType(processType);
            submitRevokeDto.setReasonContent(reason);
            submitRevokeDto.setReason(PARAM_REASON_TYPE_DO);
            submitRevokeDto.setAppType(PARAM_APPLICATION_TYPE_REVOCATION);
            submitRevokeDto.setStatus(PARAM_APPLICATION_STATUS_PENDING_AO);
            submitRevokeDto.setApplicationDt(new Date());
            submitRevokeDto.setRemarks(remarks);
            submitRevokeDto.setApprovalId(approval.getId());
            revocationClient.saveRevokeApplication(submitRevokeDto);
        }
        if (flag.equals(APP)) {
            Application application = (Application) ParamUtil.getSessionAttr(request, PARAM_APPLICATION);
            submitRevokeDto.setStatus(PARAM_APPLICATION_STATUS_PENDING_AO);
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

    private EnquiryDto getSearchDto(HttpServletRequest request) {
        EnquiryDto searchDto = (EnquiryDto) ParamUtil.getSessionAttr(request, PARAM_FACILITY_SEARCH);
        return searchDto == null ? getDefaultSearchDto() : searchDto;
    }

    private EnquiryDto getDefaultSearchDto() {
        EnquiryDto dto = new EnquiryDto();
        dto.clearAllFields();
        dto.defaultPaging();
        return dto;
    }
}
