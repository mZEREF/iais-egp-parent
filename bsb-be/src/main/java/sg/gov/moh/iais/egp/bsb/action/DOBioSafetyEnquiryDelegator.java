package sg.gov.moh.iais.egp.bsb.action;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.OnlineEnquiryClient;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.enquiry.*;
import sg.gov.moh.iais.egp.bsb.util.mastercode.MasterCodeHolder;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.BioSafetyEnquiryConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_VALIDATION_ERRORS;

@Delegator(value = "bioSafetyEnquiryDelegator")
@Slf4j
public class DOBioSafetyEnquiryDelegator {
    private final OnlineEnquiryClient onlineEnquiryClient;

    public DOBioSafetyEnquiryDelegator(OnlineEnquiryClient onlineEnquiryClient) {
        this.onlineEnquiryClient = onlineEnquiryClient;
    }

    public void start(BaseProcessClass bpc) {
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_ONLINE_ENQUIRY, "Biosafety Enquiry");
        HttpServletRequest request = bpc.request;
        ParamUtil.clearSession(request,KEY_SEARCH_DTO_APPLICATION);
        ParamUtil.clearSession(request,KEY_SEARCH_DTO_FACILITY);
        ParamUtil.clearSession(request,KEY_SEARCH_DTO_APPROVAL);
        ParamUtil.clearSession(request,KEY_SEARCH_DTO_AFC);
        ParamUtil.clearSession(request,PARAM_SEARCH_KEY);
    }

    public void init(BaseProcessClass bpc){

    }

    public void preBioSafetySearch(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String searchKey = ParamUtil.getString(request,PARAM_SEARCH_KEY);
        if(StringUtils.hasLength(searchKey)){
            switch (searchKey){
                case PARAM_CHOICE_APPLICATION:
                    ParamUtil.setRequestAttr(request,OPTIONS_APPLICATION_TYPE,MasterCodeHolder.APPLICATION_TYPE.allOptions());
                    ParamUtil.setRequestAttr(request,OPTIONS_APPLICATION_STATUS,MasterCodeHolder.APPLICATION_STATUS.allOptions());
                    ParamUtil.setRequestAttr(request,OPTIONS_FACILITY_CLASSIFICATION,MasterCodeHolder.FACILITY_CLASSIFICATION.allOptions());
                    ParamUtil.setRequestAttr(request,OPTIONS_PROCESS_TYPE,MasterCodeHolder.PROCESS_TYPE.allOptions());
                    break;
                case PARAM_CHOICE_FACILITY:
                    ParamUtil.setRequestAttr(request,OPTIONS_FACILITY_CLASSIFICATION,MasterCodeHolder.FACILITY_CLASSIFICATION.allOptions());
                    ParamUtil.setRequestAttr(request,OPTIONS_FACILITY_STATUS,MasterCodeHolder.APPROVAL_STATUS.allOptions());
                    ParamUtil.setRequestAttr(request,OPTIONS_APPROVE_FACILITY_CERTIFIER,MasterCodeHolder.APPROVE_FACILITY_CERTIFIER.allOptions());
                    break;
                case PARAM_CHOICE_APPROVAL:
                    ParamUtil.setRequestAttr(request,OPTIONS_FACILITY_CLASSIFICATION,MasterCodeHolder.FACILITY_CLASSIFICATION.allOptions());
                    ParamUtil.setRequestAttr(request,OPTIONS_APPROVAL_TYPE,MasterCodeHolder.APPROVAL_TYPE.allOptions());
                    ParamUtil.setRequestAttr(request,OPTIONS_APPROVAL_STATUS,MasterCodeHolder.APPROVAL_STATUS.allOptions());
                    break;
                case PARAM_CHOICE_APPROVED_FACILITY_CERTIFIER:
                    ParamUtil.setRequestAttr(request,OPTIONS_AFC_STATUS,MasterCodeHolder.APPROVAL_STATUS.allOptions());
                    break;
                default:
                    break;
            }
        }
    }

    public void doBioSafetySearch(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String choice = ParamUtil.getString(request,PARAM_SEARCH_KEY);
        doBasicSearch(request,choice);
        ParamUtil.setSessionAttr(request,PARAM_SEARCH_KEY,choice);
    }


    public void doBasicSearch(HttpServletRequest request,String choice){
        Assert.hasLength(choice,"choice is null");
        switch (choice){
            case PARAM_CHOICE_APPLICATION:
                doApplicationSearch(request);
                break;
            case PARAM_CHOICE_FACILITY:
                doFacilitySearch(request);
                break;
            case PARAM_CHOICE_APPROVAL:
                doApprovalSearch(request);
                break;
            case PARAM_CHOICE_APPROVED_FACILITY_CERTIFIER:
                doAFCSearch(request);
                break;
            default:
                break;
        }

    }


    public void doApplicationSearch(HttpServletRequest request){
        AppSearchDto dto = getAppSearchDto(request);
        dto.clearAllFields();
        dto.reqObjMapping(request);
        //do enquiry and get result
        if(Boolean.TRUE.equals(validationParam(request, PARAM_CHOICE_APPLICATION, dto))){
            ResponseDto<AppResultPageInfoDto> resultPageInfoDto =  onlineEnquiryClient.getApplication(dto);
            if(resultPageInfoDto.ok()){
                ParamUtil.setRequestAttr(request,KEY_PAGE_INFO,resultPageInfoDto.getEntity().getPageInfo());
                ParamUtil.setRequestAttr(request,KEY_APPLICATION_RESULT,resultPageInfoDto.getEntity().getBsbApp());
            }else{
                ParamUtil.setRequestAttr(request,KEY_PAGE_INFO, PageInfo.emptyPageInfo(dto));
                ParamUtil.setRequestAttr(request,KEY_APPLICATION_RESULT,new ArrayList<>());
            }
        }
        ParamUtil.setSessionAttr(request,KEY_SEARCH_DTO_APPLICATION,dto);
    }

    public void doFacilitySearch(HttpServletRequest request){
        FacSearchDto dto = getFacSearchDto(request);
        dto.clearAllFields();
        dto.reqObjMapping(request);
        if(Boolean.TRUE.equals(validationParam(request, PARAM_CHOICE_FACILITY, dto))){
            ResponseDto<FacResultPageInfoDto> resultPageInfoDto =  onlineEnquiryClient.getFacility(dto);
            if(resultPageInfoDto.ok()){
                ParamUtil.setRequestAttr(request,KEY_PAGE_INFO,resultPageInfoDto.getEntity().getPageInfo());
                ParamUtil.setRequestAttr(request,KEY_FACILITY_RESULT,resultPageInfoDto.getEntity().getBsbFac());
            }else{
                ParamUtil.setRequestAttr(request,KEY_PAGE_INFO, PageInfo.emptyPageInfo(dto));
                ParamUtil.setRequestAttr(request,KEY_FACILITY_RESULT,new ArrayList<>());
            }
        }
        ParamUtil.setSessionAttr(request,KEY_SEARCH_DTO_FACILITY,dto);
    }

    public void doApprovalSearch(HttpServletRequest request){
        ApprovalSearchDto dto = getApprovalSearchDto(request);
        dto.clearAllFields();
        dto.reqObjMapping(request);
        if(Boolean.TRUE.equals(validationParam(request, PARAM_CHOICE_APPROVAL, dto))){
            ResponseDto<ApprovalResultDto> resultPageInfoDto =  onlineEnquiryClient.getApproval(dto);
            if(resultPageInfoDto.ok()){
                ParamUtil.setRequestAttr(request,KEY_PAGE_INFO,resultPageInfoDto.getEntity().getPageInfo());
                ParamUtil.setRequestAttr(request,KEY_APPROVAL_RESULT,resultPageInfoDto.getEntity().getBsbApproval());
            }else{
                ParamUtil.setRequestAttr(request,KEY_PAGE_INFO, PageInfo.emptyPageInfo(dto));
                ParamUtil.setRequestAttr(request,KEY_APPROVAL_RESULT,new ArrayList<>());
            }
        }
        ParamUtil.setSessionAttr(request,KEY_SEARCH_DTO_APPROVAL,dto);
    }

    public void doAFCSearch(HttpServletRequest request){
        AFCSearchDto dto = getAFCSearchDto(request);
        dto.clearAllFields();
        dto.reqObjMapping(request);
        if(Boolean.TRUE.equals(validationParam(request, PARAM_CHOICE_APPROVED_FACILITY_CERTIFIER, dto))){
            ResponseDto<AFCResultPageInfoDto> resultPageInfoDto =  onlineEnquiryClient.getApprovedFacilityCertifier(dto);
            if(resultPageInfoDto.ok()){
                ParamUtil.setRequestAttr(request,KEY_PAGE_INFO,resultPageInfoDto.getEntity().getPageInfo());
                ParamUtil.setRequestAttr(request,KEY_AFC_RESULT,resultPageInfoDto.getEntity().getBsbAFC());
            }else{
                ParamUtil.setRequestAttr(request,KEY_PAGE_INFO, PageInfo.emptyPageInfo(dto));
                ParamUtil.setRequestAttr(request,KEY_AFC_RESULT,new ArrayList<>());
            }
        }
        ParamUtil.setSessionAttr(request,KEY_SEARCH_DTO_AFC,dto);
    }

    private Boolean validationParam(HttpServletRequest request, String propertyName, Object object) {
        ValidationResult vResult = WebValidationHelper.validateProperty(object, propertyName);
        if (vResult != null && vResult.isHasErrors()) {
            Map<String, String> errorMap = vResult.retrieveAll();
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, WebValidationHelper.generateJsonStr(errorMap));
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }

    private AppSearchDto getAppSearchDto(HttpServletRequest request) {
        AppSearchDto dto = (AppSearchDto) ParamUtil.getSessionAttr(request, KEY_SEARCH_DTO_APPLICATION);
        return dto == null ? getDefaultAppSearchDto() : dto;
    }

    private AppSearchDto getDefaultAppSearchDto() {
        return new AppSearchDto();
    }

    private FacSearchDto getFacSearchDto(HttpServletRequest request) {
        FacSearchDto dto = (FacSearchDto) ParamUtil.getSessionAttr(request, KEY_SEARCH_DTO_FACILITY);
        return dto == null ? getDefaultFacSearchDto() : dto;
    }

    private FacSearchDto getDefaultFacSearchDto() {
        return new FacSearchDto();
    }

    private ApprovalSearchDto getApprovalSearchDto(HttpServletRequest request) {
        ApprovalSearchDto dto = (ApprovalSearchDto) ParamUtil.getSessionAttr(request, KEY_SEARCH_DTO_APPROVAL);
        return dto == null ? getDefaultApprovalSearchDto() : dto;
    }

    private ApprovalSearchDto getDefaultApprovalSearchDto() {
        return new ApprovalSearchDto();
    }

    private AFCSearchDto getAFCSearchDto(HttpServletRequest request) {
        AFCSearchDto dto = (AFCSearchDto) ParamUtil.getSessionAttr(request, KEY_SEARCH_DTO_AFC);
        return dto == null ? getDefaultAFCSearchDto() : dto;
    }

    private AFCSearchDto getDefaultAFCSearchDto() {
        return new AFCSearchDto();
    }
}
