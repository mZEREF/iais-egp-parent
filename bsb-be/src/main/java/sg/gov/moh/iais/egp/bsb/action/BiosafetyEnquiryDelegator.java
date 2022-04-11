package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelWriter;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sg.gov.moh.iais.egp.bsb.client.BiosafetyEnquiryClient;
import sg.gov.moh.iais.egp.bsb.constant.BioSafetyEnquiryConstants;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.PagingAndSortingDto;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.enquiry.*;
import sg.gov.moh.iais.egp.bsb.dto.entity.BiologicalDto;
import sg.gov.moh.iais.egp.bsb.entity.*;
import sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static sg.gov.moh.iais.egp.bsb.constant.BioSafetyEnquiryConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.*;


/**
 * @author YiMing
 * @version 2021/10/15 14:16
 **/
@Delegator(value = "biosafetyEnquiryDelegator")
@Slf4j
public class BiosafetyEnquiryDelegator {

    private final BiosafetyEnquiryClient biosafetyEnquiryClient;

    public BiosafetyEnquiryDelegator(BiosafetyEnquiryClient biosafetyEnquiryClient) {
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


    public void prepareBasicSearch(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String count = ParamUtil.getString(request, PARAM_SEARCH_CHK);
        String text = ParamUtil.getString(request, PARAM_SEARCH_TEXT);
        doBasicSearch(request,count,text);
        preSelectOption(request,null);
        ParamUtil.setRequestAttr(request, PARAM_COUNT, count);
    }



    public void page(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String count = ParamUtil.getString(request,PARAM_COUNT);
        PagingAndSortingDto dto = getSearchDto(request,count+KEY_SEARCH_DTO_SUFFIX);
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        switch (actionValue) {
            case "changeSize":
                int pageSize = ParamUtil.getInt(request, KEY_PAGE_SIZE);
                dto.setPage(0);
                dto.setSize(pageSize);
                break;
            case "changePage":
                int pageNo = ParamUtil.getInt(request, KEY_PAGE_NO);
                dto.setPage(pageNo - 1);
                break;
            default:
                log.warn("page, action_value is invalid: {}", actionValue);
                break;
        }
        ParamUtil.setSessionAttr(request, count+KEY_SEARCH_DTO_SUFFIX, dto);
    }

    public void sort(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String count = ParamUtil.getString(request,PARAM_COUNT);
        PagingAndSortingDto dto = getSearchDto(request,count+KEY_SEARCH_DTO_SUFFIX);
        String field = ParamUtil.getString(request, KEY_ACTION_VALUE);
        String sortType = ParamUtil.getString(request, KEY_ACTION_ADDITIONAL);
        dto.changeSort(field, sortType);
        ParamUtil.setSessionAttr(request, count+KEY_SEARCH_DTO_SUFFIX, dto);
    }


    public void prepareAdvSearch(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String count = ParamUtil.getString(request, PARAM_SEARCH_CHK);
        if (StringUtils.isEmpty(count)) {
            count = "0";
        }
        ParamUtil.setRequestAttr(request, PARAM_COUNT, count);
        preSelectOption(request, count);
    }


    public void preAfterAdvSearch(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String count = ParamUtil.getString(request, PARAM_SEARCH_CHK);
        ParamUtil.setRequestAttr(request, PARAM_COUNT, count);
        preSelectOption(request, count);
        ParamUtil.setRequestAttr(request,KEY_DOWNLOAD,URL_BIO_SAFETY_INFO_FILE);
        // get search DTO
        doBasicSearch(request,count,null);
    }


    public void prepareDetail(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String count = ParamUtil.getString(request, PARAM_SEARCH_CHK);
        ParamUtil.setRequestAttr(request, PARAM_COUNT, count);
    }


    public void selectOption(HttpServletRequest request, String name, List<String> strings) {
        List<SelectOption> selectModel = new ArrayList<>(strings.size());
        for (String string : strings) {
            selectModel.add(new SelectOption(string, string));
        }
        ParamUtil.setRequestAttr(request, name, selectModel);
    }

    public void preSelectOption(HttpServletRequest request, String num) {
        //add action
        List<String> action = new ArrayList<>(3);
        action.add("Revoke");
        action.add("Suspend");
        action.add("Reinstate");
        selectOption(request, "action", action);
        if ("app".equals(num) || "fn".equals(num) || "an".equals(num)) {
            List<String> facNames = biosafetyEnquiryClient.queryDistinctFN().getEntity();
            selectOption(request, "facilityName", facNames);
            List<String> bioNames = biosafetyEnquiryClient.queryDistinctFA().getEntity();
            selectOption(request, "biologicalAgent", bioNames);
        }
        if("on".equals(num)){
           List<String> orgNames =  biosafetyEnquiryClient.queryDistinctOrgName().getEntity();
           selectOption(request,"orgName",orgNames);
        }
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

//
//
//
//    @GetMapping(value = "/bioSafety-information-file")
//    public @ResponseBody
//    void appFileHandler(HttpServletRequest request, HttpServletResponse response) {
//        log.debug(StringUtil.changeForLog("application fileHandler start ...."));
//        File file = null;
//        log.debug("indicates that a app record has been selected ");
//        String count = (String) ParamUtil.getSessionAttr(request,PARAM_COUNT);
//        if(StringUtils.hasLength(count)){
//            switch (count) {
//                case CHOOSE_MARK_APPLICATION: {
//                    List<ApplicationInfoDto> bioSafetyDto = (List<ApplicationInfoDto>) ParamUtil.getSessionAttr(request, PARAM_BIO_SAFETY_ENQUIRY);
//                    try {
//                        file = ExcelWriter.writerToExcel(filedApplicationInfo(bioSafetyDto), ApplicationInfoDto.class, "Application Information_Search_Template");
//                    } catch (Exception e) {
//                        log.error("=======>app fileHandler  error >>>>>", e);
//                    }
//                    break;
//                }
//                case CHOOSE_MARK_APPROVAL: {
//                    List<ApprovalInfoDto> bioSafetyDto = (List<ApprovalInfoDto>) ParamUtil.getSessionAttr(request, PARAM_BIO_SAFETY_ENQUIRY);
//                    try {
//                        file = ExcelWriter.writerToExcel(filedApprovalInfo(bioSafetyDto), ApprovalInfoDto.class, "Approval Information_Search_Template");
//                    } catch (Exception e) {
//                        log.error("=======>approval fileHandler  error >>>>>", e);
//                    }
//                    break;
//                }
//                case CHOOSE_MARK_APPROVED_CERTIFIER_FACILITY: {
//                    List<FacilityCertifierReg> bioSafetyDto = (List<FacilityCertifierReg>) ParamUtil.getSessionAttr(request, PARAM_BIO_SAFETY_ENQUIRY);
//                    try {
//                        file = ExcelWriter.writerToExcel(filedFacilityCertifierInfo(bioSafetyDto), ApprovedFacilityCertifierInfoDto.class, "Approved Facility Certifier Information_Search_Template");
//                    } catch (Exception e) {
//                        log.error("=======>facility certifier fileHandler  error >>>>>", e);
//                    }
//                    break;
//                }
//                case CHOOSE_MARK_FACILITY: {
//                    List<FacilityActivity> bioSafetyDto = (List<FacilityActivity>) ParamUtil.getSessionAttr(request, PARAM_BIO_SAFETY_ENQUIRY);
//                    try {
//                        file = ExcelWriter.writerToExcel(filedFacilityInfo(bioSafetyDto), FacilityInfoDto.class, "Facility Information_Search_Template");
//                    } catch (Exception e) {
//                        log.error("=======>facility fileHandler  error >>>>>", e);
//                    }
//                    break;
//                }
//                default:
//                    if(log.isInfoEnabled()){
//                        log.info("error count{}", LogUtil.escapeCrlf(count));
//                    }
//                    break;
//            }
//        }
//        try {
//            FileUtils.writeFileResponseContent(response, file);
//            FileUtils.deleteTempFile(file);
//        } catch (IOException e) {
//            log.debug(e.getMessage());
//        }
//        log.debug(StringUtil.changeForLog("app fileHandler end ...."));
//    }
//
//    public List<ApplicationInfoDto> filedApplicationInfo(List<ApplicationInfoDto> bsbApp){
//        if(!CollectionUtils.isEmpty(bsbApp)){
//            log.info("empty list applicationInfo");
//        }
//        for (ApplicationInfoDto info : bsbApp) {
//            info.setAppStatus(MasterCodeUtil.getCodeDesc(info.getAppStatus()));
//            info.setAppType(MasterCodeUtil.getCodeDesc(info.getAppType()));
//            info.setFacilityClassification(MasterCodeUtil.getCodeDesc(info.getFacilityClassification()));
//            info.setFacilityType(MasterCodeUtil.getCodeDesc(info.getFacilityType()));
//            info.setProcessType(MasterCodeUtil.getCodeDesc(info.getProcessType()));
//        }
//        return bsbApp;
//    }
//
//    public List<ApprovalInfoDto> filedApprovalInfo(List<ApprovalInfoDto> bsbAppr){
//        if(CollectionUtils.isEmpty(bsbAppr)){
//            log.info("empty list approvalInfo");
//            return Collections.emptyList();
//        }
//        for (ApprovalInfoDto dto : bsbAppr) {
//            dto.setType(MasterCodeUtil.getCodeDesc(dto.getType()));
//            dto.setFacStatus(MasterCodeUtil.getCodeDesc(dto.getFacStatus()));
//            dto.setStatus(MasterCodeUtil.getCodeDesc(dto.getStatus()));
//        }
//
//        return bsbAppr;
//    }
//
//    public List<ApprovedFacilityCertifierInfoDto> filedFacilityCertifierInfo(List<FacilityCertifierReg> regs){
//        if(CollectionUtils.isEmpty(regs)){
//            log.info("empty list facilityCertifier");
//            return Collections.emptyList();
//        }
//        List<ApprovedFacilityCertifierInfoDto> infos = new ArrayList<>(regs.size());
//        SimpleDateFormat sdf = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss ");
//        for (FacilityCertifierReg reg : regs) {
//            ApprovedFacilityCertifierInfoDto info = new ApprovedFacilityCertifierInfoDto();
//            Approval approval = reg.getApproval();
//            info.setAdministrator(reg.getAdminName());
//            info.setApprovedDate(sdf.format(approval.getApprovalDate()));
//            info.setExpiryDate(sdf.format(approval.getApprovalExpiryDate()));
//            info.setOrganisationAddress(reg.getAddress());
//            info.setOrganisationName(reg.getOrgName());
//            info.setAfcStatus(approval.getStatus());
//            infos.add(info);
//        }
//        return infos;
//    }
//
//    public List<FacilityInfoDto> filedFacilityInfo(List<FacilityActivity> fac){
//        if(CollectionUtils.isEmpty(fac)){
//            log.info("empty list facilityInfo");
//            return Collections.emptyList();
//        }
//        List<FacilityInfoDto> infos = new ArrayList<>(fac.size());
//        SimpleDateFormat sdf = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss ");
//        for (FacilityActivity activity : fac) {
//            FacilityInfoDto info = new FacilityInfoDto();
//            Facility facility = activity.getFacility();
//            Approval approval = activity.getApproval();
//            info.setFacilityAddress(TableDisplayUtil.getOneLineAddress(facility.getBlkNo(),facility.getStreetName(),
//                    facility.getFloorNo(),facility.getUnitNo(),facility.getPostalCode()));
////            info.setFacilityAdmin(activity.getAdmin());
//            info.setFacilityType(activity.getActivityType());
//            info.setFacilityName(facility.getFacilityName());
//            info.setFacilityExpiryDate(sdf.format(approval.getApprovalExpiryDate()));
//            info.setFacilityClassification(facility.getFacilityClassification());
//            info.setFacilityOperator(facility.getOperator().getFacOperator());
////            info.setBiologicalAgent(activity.getBioName());
//            info.setGazettedArea(facility.getIsProtected());
////            info.setRiskLevelOfTheBiologicalAgent(activity.getRiskLevel());
//            infos.add(info);
//        }
//        return infos;
//    }



    private PagingAndSortingDto getSearchDto(HttpServletRequest request,String searchKey) {
        PagingAndSortingDto dto = (PagingAndSortingDto) ParamUtil.getSessionAttr(request, searchKey);
        return dto == null ? getDefaultSearchDto() : dto;
    }

    private PagingAndSortingDto getDefaultSearchDto() {
        return new PagingAndSortingDto();
    }

}
