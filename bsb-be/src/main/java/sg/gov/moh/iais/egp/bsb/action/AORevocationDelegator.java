package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.DocClient;
import sg.gov.moh.iais.egp.bsb.constant.RevocationConstants;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.AuditDocDto;
import sg.gov.moh.iais.egp.bsb.dto.revocation.*;
import sg.gov.moh.iais.egp.bsb.client.RevocationClient;
import sg.gov.moh.iais.egp.bsb.entity.*;
import sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Zhu Tangtang
 */
@Slf4j
@Delegator(value = "AORevocationDelegator")
public class AORevocationDelegator {
    private final RevocationClient revocationClient;
    private final DocClient docClient;

    public AORevocationDelegator(RevocationClient revocationClient, DocClient docClient) {
        this.revocationClient = revocationClient;
        this.docClient = docClient;
    }

    public void start(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction(RevocationConstants.MODULE_REVOCATION, RevocationConstants.FUNCTION_REVOCATION);
        HttpServletRequest request = bpc.request;
        IaisEGPHelper.clearSessionAttr(request, RevocationConstants.class);
        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_APPLICATION_SEARCH, null);
        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_REVOCATION_DETAIL, null);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_APPLICATION, null);
        ParamUtil.setSessionAttr(request,RevocationConstants.AUDIT_DOC_DTO, null);
        ParamUtil.setSessionAttr(request, RevocationConstants.FACILITY, null);
        ParamUtil.setSessionAttr(request, RevocationConstants.FLAG, null);
        ParamUtil.setSessionAttr(request, RevocationConstants.BACK, null);
        ParamUtil.setRequestAttr(request, RevocationConstants.PARAM_APPLICATION_MISC_LIST, null);
        String appId = ParamUtil.getRequestString(request,RevocationConstants.PARAM_APP_ID);
        appId = MaskUtil.unMaskValue("id",appId);

        ViewSelectedRevokeApplicationDto dto = revocationClient.getRevokeDetailByApplicationId(appId).getEntity();
        Application application = dto.getApplication();
        List<ApplicationMisc> applicationMiscs=application.getAppMiscs();
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

        //get history list
        List<RoutingHistory> historyDtoList = revocationClient.getAllHistory().getEntity();
        ParamUtil.setRequestAttr(request, RevocationConstants.PARAM_PROCESSING_HISTORY,historyDtoList);

        AuditDocDto auditDocDto = new AuditDocDto();
        if (StringUtils.hasLength(facility.getId())) {
            List<FacilityDoc> facilityDocList = docClient.getFacilityDocByFacId(facility.getId()).getEntity();
            List<FacilityDoc> docList = new ArrayList<>();
            for (FacilityDoc facilityDoc : facilityDocList) {
                String submitByName = IaisEGPHelper.getCurrentAuditTrailDto().getMohUserId();
                facilityDoc.setSubmitByName(submitByName);
                docList.add(facilityDoc);
            }
            auditDocDto.setFacilityDocs(docList);
            ParamUtil.setSessionAttr(request, RevocationConstants.FACILITY, facility);
        }
        approval.setFacility(facility);
        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_APPLICATION, application);
        ParamUtil.setSessionAttr(request, RevocationConstants.APPROVAL, approval);
        ParamUtil.setRequestAttr(request, RevocationConstants.PARAM_APPLICATION_MISC_LIST, applicationMiscs);
        ParamUtil.setSessionAttr(request,RevocationConstants.AUDIT_DOC_DTO, auditDocDto);
        ParamUtil.setSessionAttr(request, RevocationConstants.FLAG, RevocationConstants.APP);
        ParamUtil.setSessionAttr(request, RevocationConstants.BACK, RevocationConstants.REVOCATION_TASK_LIST);
    }

    public void approve(BaseProcessClass bpc) {
        SubmitRevokeDto submitRevokeDto = before(bpc);
        submitRevokeDto.setStatus(RevocationConstants.PARAM_APPLICATION_STATUS_APPROVED);
        revocationClient.updateRevokeApplication(submitRevokeDto);
    }

    public void reject(BaseProcessClass bpc) {
        SubmitRevokeDto submitRevokeDto = before(bpc);
        submitRevokeDto.setStatus(RevocationConstants.PARAM_APPLICATION_STATUS_REJECTED);
        revocationClient.updateRevokeApplication(submitRevokeDto);
    }

    public void routebackToDO(BaseProcessClass bpc) {
        SubmitRevokeDto submitRevokeDto = before(bpc);
        submitRevokeDto.setStatus(RevocationConstants.PARAM_APPLICATION_STATUS_PENDING_DO);
        revocationClient.updateRevokeApplication(submitRevokeDto);
    }

    public void routeToHM(BaseProcessClass bpc) {
        SubmitRevokeDto submitRevokeDto = before(bpc);
        submitRevokeDto.setStatus(RevocationConstants.PARAM_APPLICATION_STATUS_PENDING_HM);
        revocationClient.updateRevokeApplication(submitRevokeDto);
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

    private SubmitRevokeDto before(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SubmitRevokeDto submitRevokeDto = new SubmitRevokeDto();
        Application application = (Application)ParamUtil.getSessionAttr(request, RevocationConstants.PARAM_APPLICATION);
        Approval approval = (Approval) ParamUtil.getSessionAttr(request, RevocationConstants.APPROVAL);

        String reason = ParamUtil.getString(request, RevocationConstants.PARAM_REASON);
        String remarks = ParamUtil.getString(request, RevocationConstants.PARAM_AOREMARKS);

        String[] strArr = reason.split(";");
        String a = strArr[strArr.length-1];
        char[] charStr = a.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char c : charStr) {
            sb.append(c);
        }
        boolean contains = reason.contains(sb.toString());
        //get user name
        if (!contains){
            submitRevokeDto.setReason(RevocationConstants.PARAM_REASON_TYPE_AO);
            submitRevokeDto.setReasonContent(reason);
        }
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        submitRevokeDto.setLoginUser(loginContext.getUserName());
        submitRevokeDto.setRemarks(remarks);
        submitRevokeDto.setAppId(application.getId());
        submitRevokeDto.setApprovalId(approval.getId());
        submitRevokeDto.setProcessType(application.getProcessType());
        return submitRevokeDto;
    }

}
