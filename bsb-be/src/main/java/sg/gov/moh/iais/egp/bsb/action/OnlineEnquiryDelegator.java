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
import sg.gov.moh.iais.egp.bsb.client.BiosafetyEnquiryClient;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.enquiry.*;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.BioSafetyEnquiryConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_VALIDATION_ERRORS;


@Delegator(value = "onlineEnquiryDelegator")
@Slf4j
public class OnlineEnquiryDelegator {

    private final BiosafetyEnquiryClient biosafetyEnquiryClient;

    public OnlineEnquiryDelegator(BiosafetyEnquiryClient biosafetyEnquiryClient) {
        this.biosafetyEnquiryClient = biosafetyEnquiryClient;
    }

    public void start(BaseProcessClass bpc) {
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_ONLINE_ENQUIRY, "Biosafety Enquiry");
        HttpServletRequest request = bpc.request;
        ParamUtil.clearSession(request,KEY_SEARCH_DTO_APPLICATION);
        ParamUtil.clearSession(request,KEY_SEARCH_DTO_FACILITY);
        ParamUtil.clearSession(request,KEY_SEARCH_DTO_APPROVAL);
        ParamUtil.clearSession(request,KEY_SEARCH_DTO_AFC);
    }




    public void doBasicSearch(HttpServletRequest request,String choice,String text){
        Assert.hasLength(choice,"choice is null");
        switch (choice){
            case PARAM_CHOICE_APPLICATION:
                doApplicationSearch(request,text);
                break;
            case PARAM_CHOICE_FACILITY:
                doFacilitySearch(request,text);
                break;
            case PARAM_CHOICE_APPROVAL:
                doApprovalSearch(request,text);
                break;
            case PARAM_CHOICE_APPROVED_FACILITY_CERTIFIER:
                doAFCSearch(request,text);
                break;
            default:
                break;
        }

    }


    public void doApplicationSearch(HttpServletRequest request,String text){
        AppSearchDto dto = getAppSearchDto(request);
        if(StringUtils.hasLength(text)){
            dto.setAppNo(text);
        }else{
            dto.clearAllFields();
            dto.reqObjMapping(request);
        }
        if(Boolean.TRUE.equals(validationParam(request, PARAM_CHOICE_APPLICATION, dto))){
            ResponseDto<AppResultPageInfoDto> resultPageInfoDto =  biosafetyEnquiryClient.getApplication(dto);
            if(resultPageInfoDto.ok()){
                ParamUtil.setRequestAttr(request,KEY_PAGE_INFO,resultPageInfoDto.getEntity().getPageInfo());
                ParamUtil.setRequestAttr(request,KEY_APPLICATION_RESULT,resultPageInfoDto.getEntity().getBsbApp());
            }else{
                ParamUtil.setRequestAttr(request,KEY_PAGE_INFO, PageInfo.emptyPageInfo(dto));
                ParamUtil.setRequestAttr(request,KEY_APPLICATION_RESULT,new ArrayList<>());
            }
        }
    }

    public void doFacilitySearch(HttpServletRequest request,String text){
        FacSearchDto dto = getFacSearchDto(request);
        if(StringUtils.hasLength(text)){
            dto.setFacName(text);
        }else{
            dto.clearAllFields();
            dto.reqObjMapping(request);
        }
        if(Boolean.TRUE.equals(validationParam(request, PARAM_CHOICE_FACILITY, dto))){
            ResponseDto<FacResultPageInfoDto> resultPageInfoDto =  biosafetyEnquiryClient.getFacility(dto);
            if(resultPageInfoDto.ok()){
                ParamUtil.setRequestAttr(request,KEY_PAGE_INFO,resultPageInfoDto.getEntity().getPageInfo());
                ParamUtil.setRequestAttr(request,KEY_APPLICATION_RESULT,resultPageInfoDto.getEntity().getBsbFac());
            }else{
                ParamUtil.setRequestAttr(request,KEY_PAGE_INFO, PageInfo.emptyPageInfo(dto));
                ParamUtil.setRequestAttr(request,KEY_APPLICATION_RESULT,new ArrayList<>());
            }
        }
    }

    public void doApprovalSearch(HttpServletRequest request,String text){
        ApprovalSearchDto dto = getApprovalSearchDto(request);
        if(StringUtils.hasLength(text)){
            dto.setApprovalType(text);
        }else{
            dto.clearAllFields();
            dto.reqObjMapping(request);
        }
        if(Boolean.TRUE.equals(validationParam(request, PARAM_CHOICE_APPROVAL, dto))){
            ResponseDto<ApprovalResultDto> resultPageInfoDto =  biosafetyEnquiryClient.getApproval(dto);
            if(resultPageInfoDto.ok()){
                ParamUtil.setRequestAttr(request,KEY_PAGE_INFO,resultPageInfoDto.getEntity().getPageInfo());
                ParamUtil.setRequestAttr(request,KEY_APPLICATION_RESULT,resultPageInfoDto.getEntity().getBsbApproval());
            }else{
                ParamUtil.setRequestAttr(request,KEY_PAGE_INFO, PageInfo.emptyPageInfo(dto));
                ParamUtil.setRequestAttr(request,KEY_APPLICATION_RESULT,new ArrayList<>());
            }
        }
    }

    public void doAFCSearch(HttpServletRequest request,String text){
        AFCSearchDto dto = getAFCSearchDto(request);
        if(StringUtils.hasLength(text)){
            dto.setOrgName(text);
        }else{
            dto.clearAllFields();
            dto.reqObjMapping(request);
        }
        if(Boolean.TRUE.equals(validationParam(request, PARAM_CHOICE_APPROVED_FACILITY_CERTIFIER, dto))){
            ResponseDto<AFCResultPageInfoDto> resultPageInfoDto =  biosafetyEnquiryClient.getApprovedFacilityCertifier(dto);
            if(resultPageInfoDto.ok()){
                ParamUtil.setRequestAttr(request,KEY_PAGE_INFO,resultPageInfoDto.getEntity().getPageInfo());
                ParamUtil.setRequestAttr(request,KEY_APPLICATION_RESULT,resultPageInfoDto.getEntity().getBsbAFC());
            }else{
                ParamUtil.setRequestAttr(request,KEY_PAGE_INFO, PageInfo.emptyPageInfo(dto));
                ParamUtil.setRequestAttr(request,KEY_APPLICATION_RESULT,new ArrayList<>());
            }
        }
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
