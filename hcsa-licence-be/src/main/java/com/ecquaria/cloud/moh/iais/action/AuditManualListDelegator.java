package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditSystemPotentialDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditTaskDataFillterDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceBeConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.AuditAssginListValidateDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AuditSystemListService;
import com.ecquaria.cloud.moh.iais.service.AuditSystemPotitalListService;
import com.ecquaria.cloud.moh.iais.util.LicenceUtil;
import com.ecquaria.cloud.moh.iais.validation.AduitSystemGenerateValidate;
import com.ecquaria.cloud.moh.iais.validation.AuditAssginListValidate;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.ecquaria.cloud.moh.iais.validation.AuditCancelTaskValidate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @Author: jiahao
 * @Date: 2020/2/21 16:54
 */
@Slf4j
@Delegator("auditManualListDelegator")
public class AuditManualListDelegator {

    @Autowired
    AuditSystemPotitalListService auditSystemPotitalListService;
    @Autowired
    AuditSystemListService auditSystemListService;
    private static final String SESSION_AUDIT_SYSTEM_POTENTIAL_DTO_FOR_SEARCH_NAME = "auditSystemPotentialDtoForSearch";
    private static final String  SUBMIT_MESSAGE_SUCCESS = "submit_message_success";
    private static final String  MAIN_URL              ="mainUrl";
    private static final String  DO_CHANGE              = "dochange";
    public void start(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_AUDIT_INSPECTION, AuditTrailConsts.FUNCTION_MANUAL_AUDIT_LIST);
    }
    public void init(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart init ...."));
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,"ISTUC",Boolean.FALSE);
        ParamUtil.setSessionAttr(request, SESSION_AUDIT_SYSTEM_POTENTIAL_DTO_FOR_SEARCH_NAME, null);
        ParamUtil.setSessionAttr(request,"modulename",AuditTrailConsts.FUNCTION_MANUAL_AUDIT_LIST);
        ParamUtil.setSessionAttr(request, HcsaLicenceBeConstant.SEARCH_PRAM_FOR_AUDIT_LIST,null);
        ParamUtil.setSessionAttr(request, HcsaLicenceBeConstant.SEARCH_PRAM_FOR_AUDIT_LIST_TRUE_RESULT,null);
        ParamUtil.setSessionAttr(request, HcsaLicenceBeConstant.SESSION_ROLEIDS_FOR_AUDIT,null);
        ParamUtil.setSessionAttr(request, HcsaLicenceBeConstant.SESSION_ROLEIDS_FOR_AUDIT_SELECT, null);
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<SelectOption> roleIds = auditSystemListService.getCanViewAuditRoles(loginContext.getRoleIds());
        if(!IaisCommonUtils.isEmpty(roleIds)){
            ParamUtil.setSessionAttr(request, HcsaLicenceBeConstant.SESSION_ROLEIDS_FOR_AUDIT, (Serializable) roleIds);
            ParamUtil.setSessionAttr(request, HcsaLicenceBeConstant.SESSION_ROLEIDS_FOR_AUDIT_SELECT,  roleIds.get(0).getValue());
        }
    }
    public void pre(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        AuditSystemPotentialDto dto = (AuditSystemPotentialDto) ParamUtil.getSessionAttr(request, SESSION_AUDIT_SYSTEM_POTENTIAL_DTO_FOR_SEARCH_NAME);
        if(dto != null){
            ParamUtil.setSessionAttr(request, HcsaLicenceBeConstant.SESSION_ROLEIDS_FOR_AUDIT_SELECT,  dto.getSelectRole());
        }
        List<HcsaServiceDto> hcsaServiceDtos = auditSystemListService.getActiveHCIServices();
        ParamUtil.setRequestAttr(request, "activeHCIServiceNames", (Serializable) auditSystemListService.getActiveHCIServicesByNameOrCode(hcsaServiceDtos, HcsaLicenceBeConstant.GET_HCI_SERVICE_SELECTION_NAME_TAG));
        ParamUtil.setRequestAttr(request, "activeHCIServiceCodes", (Serializable) auditSystemListService.getActiveHCIServicesByNameOrCode(hcsaServiceDtos, HcsaLicenceBeConstant.GET_HCI_SERVICE_SELECTION_COED_TAG));
        ParamUtil.setRequestAttr(request, "complianceLastResultOptions", (Serializable) LicenceUtil.getResultsLastCompliance());
        ParamUtil.setRequestAttr(request, "premTypeOp", (Serializable) LicenceUtil.getPremisesType());
        ParamUtil.setRequestAttr(request, "hclCodeOp", (Serializable) auditSystemListService.getActiveHCICode());
        ParamUtil.setRequestAttr(request,"riskTypeOp", (Serializable) MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_SYSTEM_RISK_TYPE));
    }
    public void remove(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart remove ...."));
    }
    public void preconfirm(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart preconfirm ...."));
        HttpServletRequest request = bpc.request;
        getListData(request);
        AuditAssginListValidate auditAssginListValidate = new AuditAssginListValidate();
        Map<String, String> errMap = auditAssginListValidate.validate(request);
        if(errMap.isEmpty()){
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setSessionAttr(request,"actionCancel","doback");
            ParamUtil.setSessionAttr(request,"actionConfirm","confirm");
        }else{
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
            ParamUtil.setSessionAttr(request,DO_CHANGE,"Y");
        }
    }
    public void confirm(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the confirm start ...."));
        HttpServletRequest request = bpc.request;
        List<AuditTaskDataFillterDto> auditTaskDataDtos  = (List<AuditTaskDataFillterDto>)ParamUtil.getSessionAttr(request,HcsaLicenceBeConstant.SEARCH_PRAM_FOR_AUDIT_LIST_RESULT);
        auditSystemListService.doSubmit(auditTaskDataDtos);
        ParamUtil.setRequestAttr(request,SUBMIT_MESSAGE_SUCCESS,MessageUtil.getMessageDesc("AUDIT_ACK001"));
        ParamUtil.setRequestAttr(request,MAIN_URL,"MohAuditManualList");
    }
    public void precancel(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart precancel ...."));
        HttpServletRequest request = bpc.request;
        getListData(request);
        AuditAssginListValidate auditAssginListValidate = new AuditAssginListValidate();
        Map<String, String> errMap = auditAssginListValidate.validate(request);
        if(errMap.isEmpty()){
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setSessionAttr(request,"actionCancel","doback");
            ParamUtil.setSessionAttr(request,"actionCancelAudit","docancel");
        }else{
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
            ParamUtil.setSessionAttr(request,DO_CHANGE,"Y");
        }
    }

    public void cancel(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart cancel ...."));
        HttpServletRequest request = bpc.request;
        List<AuditTaskDataFillterDto> auditTaskDataDtos = (List<AuditTaskDataFillterDto>) ParamUtil.getSessionAttr(request, HcsaLicenceBeConstant.SEARCH_PRAM_FOR_AUDIT_LIST_RESULT);
        AuditCancelTaskValidate auditCancelTaskValidate = new AuditCancelTaskValidate();
        Map<String, String> errorMap = auditCancelTaskValidate.validate(request);
        if(errorMap != null && errorMap.size()>0){
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
        }else {
            ParamUtil.setRequestAttr(request,SUBMIT_MESSAGE_SUCCESS,MessageUtil.getMessageDesc("AUDIT_ACK002"));
            ParamUtil.setRequestAttr(request,MAIN_URL,"MohAuditManualList");
            AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_AUDIT_INSPECTION, AuditTrailConsts.FUNCTION_MANUAL_AUDIT_LIST);
            // save cancel task
            auditSystemListService.doCancel(auditTaskDataDtos);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
        }

    }

    public void vad(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart vad ...."));
        HttpServletRequest request = bpc.request;
        String[] serviceNames = ParamUtil.getStrings(request, "svcName");
        String postcode = ParamUtil.getString(request, "postcode");
        String inspectionStartDate = ParamUtil.getDate(request, "inspectionStartDate");
        String inspectionEndDate = ParamUtil.getDate(request, "inspectionEndDate");
        String complianceLastResult = ParamUtil.getString(request, "complianceLastResult");
        String[] hclSCodes = ParamUtil.getStrings(request, "hclSCode");
        String hclCode = ParamUtil.getString(request, "hclCode");
        String premType = ParamUtil.getString(request, "premType");
        String riskType = ParamUtil.getString(request, "riskType");
        String svcNameSelect = ParamUtil.getStringsToString(request, "svcName");
        String svcNameCodeSelect = ParamUtil.getStringsToString(request, "hclSCode");
        String selectRole = ParamUtil.getString(request,HcsaLicenceBeConstant.SESSION_ROLEIDS_FOR_AUDIT_SELECT);
        List<SelectOption> roles = ( List<SelectOption>) ParamUtil.getSessionAttr(request, HcsaLicenceBeConstant.SESSION_ROLEIDS_FOR_AUDIT);
        AuditSystemPotentialDto dto = auditSystemPotitalListService.initDtoForSearch();
        List<String> serviceNmaeList = IaisCommonUtils.genNewArrayList();
        if(serviceNames != null && serviceNames.length >0){
            dto.setSvcNameSelectList(Arrays.asList(serviceNames));
            serviceNmaeList.addAll(dto.getSvcNameSelectList());
        }else {
            dto.setSvcNameSelectList(null);
        }
        List<String> hcsaServiceCodeList = IaisCommonUtils.genNewArrayList();
        if(hclSCodes != null && hclSCodes.length > 0) {
            dto.setSvcNameCodeSelectList(Arrays.asList(hclSCodes));
            serviceNmaeList.addAll(dto.getSvcNameCodeSelectList());
        }else {
            dto.setSvcNameCodeSelectList(null);
        }
        dto.setSvcNameSelect(svcNameSelect);
        dto.setSvcNameCodeSelect(svcNameCodeSelect);
        dto.setSvcNameList(serviceNmaeList);
        dto.setHcsaServiceCodeList(hcsaServiceCodeList);
        dto.setPostalCode(postcode);
        dto.setLastInspectionStart(inspectionStartDate);
        dto.setLastInspectionEnd(inspectionEndDate);
        dto.setHclCode(hclCode);
        dto.setResultLastCompliance(complianceLastResult);
        dto.setPremisesType(premType);
        dto.setTypeOfRisk(riskType);
        if(!auditSystemListService.rightControlForRole(roles,selectRole)){
            log.info(StringUtil.changeForLog("--Illegal Role ID :" + selectRole+"---------"));
            dto.setSelectRole(roles.get(0).getValue());
        } else {
            ParamUtil.setSessionAttr(request, HcsaLicenceBeConstant.SESSION_ROLEIDS_FOR_AUDIT_SELECT, selectRole);
            dto.setSelectRole(selectRole);
        }
        ParamUtil.setSessionAttr(request, SESSION_AUDIT_SYSTEM_POTENTIAL_DTO_FOR_SEARCH_NAME, dto);
        AduitSystemGenerateValidate auditTestValidate = new AduitSystemGenerateValidate();
        Map<String, String> errMap = auditTestValidate.validate(request);
        if (errMap.isEmpty()) {
            List<SelectOption> aduitTypeOp = auditSystemListService.getAuditOp();
            ParamUtil.setSessionAttr(request,"aduitTypeOp",(Serializable) aduitTypeOp);
            getData(dto,request);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
        } else {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
        }

    }
    private void getData( AuditSystemPotentialDto dto,HttpServletRequest request){
        List<AuditTaskDataFillterDto> auditTaskDataDtos = auditSystemPotitalListService.getSystemPotentailAdultList(dto);
        auditTaskDataDtos =  auditSystemListService.getInspectors(auditTaskDataDtos);
        if(!IaisCommonUtils.isEmpty(auditTaskDataDtos)){
            for(AuditTaskDataFillterDto auditTaskDataFillterDto : auditTaskDataDtos ){
                ParamUtil.setSessionAttr(request, "inspectors"+auditTaskDataFillterDto.getWorkGroupId(), (Serializable) auditTaskDataFillterDto.getInspectors());
            }
        }
        ParamUtil.setSessionAttr(request, HcsaLicenceBeConstant.SEARCH_PRAM_FOR_AUDIT_LIST_RESULT, (Serializable) auditTaskDataDtos);
        ParamUtil.setSessionAttr(request, HcsaLicenceBeConstant.SEARCH_PRAM_FOR_AUDIT_LIST,dto.getSearchParam());
        ParamUtil.setSessionAttr(request, HcsaLicenceBeConstant.SEARCH_PRAM_FOR_AUDIT_LIST_TRUE_RESULT,dto.getSearchResult());
    }
    public void doPage(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doPage doPage ...."));
        HttpServletRequest request = bpc.request;
        String pageNo = ParamUtil.getString(request, "pageJumpNoTextchangePage");
        String pageSize = ParamUtil.getString(request, "pageJumpNoPageSize");
        AuditSystemPotentialDto dto = ( AuditSystemPotentialDto)   ParamUtil.getSessionAttr(request, SESSION_AUDIT_SYSTEM_POTENTIAL_DTO_FOR_SEARCH_NAME);
        if (!StringUtil.isEmpty(pageNo)) {
            dto.setPageNo(Integer.parseInt(pageNo));
        }
        if (!StringUtil.isEmpty(pageSize)) {
            dto.setPageSize(Integer.parseInt(pageSize));
        }
        ParamUtil.setSessionAttr(request, SESSION_AUDIT_SYSTEM_POTENTIAL_DTO_FOR_SEARCH_NAME,dto);
        getData(dto,request);
    }

    public void next(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        log.debug(StringUtil.changeForLog("the next start ...."));
        ParamUtil.setSessionAttr(request,DO_CHANGE,null);

    }
    public void nextToViewTaskList(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart nextToViewTaskList ...."));
        HttpServletRequest request = bpc.request;
        List<AuditTaskDataFillterDto> auditTaskDataDtos  = (List<AuditTaskDataFillterDto>)ParamUtil.getSessionAttr(request,HcsaLicenceBeConstant.SEARCH_PRAM_FOR_AUDIT_LIST_RESULT);
        String dochange =  (String)  ParamUtil.getSessionAttr(request,DO_CHANGE);
        if(StringUtil.isEmpty(dochange)){
            if(getSelectedList(request,auditTaskDataDtos)){
                Map<String, String> errMap = new HashMap<>(1);
                errMap.put("selectedOne","AUDIT_ERR001");
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
            }else {
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
            }
        }else {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
        }

    }

    private boolean getSelectedList(HttpServletRequest request, List<AuditTaskDataFillterDto> auditTaskDataDtos) {
        if(!IaisCommonUtils.isEmpty(auditTaskDataDtos)){
            boolean noSelectedAudit = true;
            for(int i=0; i<auditTaskDataDtos.size();i++){
                String selected = ParamUtil.getString(request,i+"selectForAuditList");
                if(!StringUtil.isEmpty(selected)){
                    auditTaskDataDtos.get(i).setAddAuditList(true);
                    noSelectedAudit = false;
                }else {
                    auditTaskDataDtos.get(i).setAddAuditList(false);
                }
            }
            ParamUtil.setSessionAttr(request,HcsaLicenceBeConstant.SEARCH_PRAM_FOR_AUDIT_LIST_RESULT,(Serializable) auditTaskDataDtos);
            ParamUtil.setSessionAttr(request,"isSelectedAudit",noSelectedAudit);
            return  noSelectedAudit;
        }
        return false;
    }
    public AuditAssginListValidateDto getValueFromPage(HttpServletRequest request) {
        AuditAssginListValidateDto dto = new AuditAssginListValidateDto();
        getListData(request);
        return dto;
    }
    private void getListData(HttpServletRequest request) {
        List<AuditTaskDataFillterDto> auditTaskDataDtos  = (List<AuditTaskDataFillterDto>)ParamUtil.getSessionAttr(request,HcsaLicenceBeConstant.SEARCH_PRAM_FOR_AUDIT_LIST_RESULT);
        if(!IaisCommonUtils.isEmpty(auditTaskDataDtos)){
            for(int i = 0;i<auditTaskDataDtos.size();i++){
                if(auditTaskDataDtos.get(i).isAddAuditList()){
                    String auditType = ParamUtil.getString(request,i+"auditType");
                    String inspectorId;
                    try {
                        inspectorId = ParamUtil.getMaskedString(request, i + "insOp");
                    }catch (Exception e){
                        inspectorId = ParamUtil.getRequestString(request, i + "insOp");
                    }
                    String inspectorName = LicenceUtil.getSelectOptionTextFromSelectOptions(auditTaskDataDtos.get(i).getInspectors(),inspectorId);
                    String forad = ParamUtil.getString(request,i+"selectForAd");
                    String number = ParamUtil.getString(request,i+"number");
                    if(!auditTaskDataDtos.get(i).isAudited()){
                        auditTaskDataDtos.get(i).setAuditType(auditType);
                        auditTaskDataDtos.get(i).setInspectorId(inspectorId);
                        auditTaskDataDtos.get(i).setInspector(inspectorName);
                    }
                    if(!StringUtil.isEmpty(forad)){
                        auditTaskDataDtos.get(i).setSelectedForAudit(true);
                    }else{
                        auditTaskDataDtos.get(i).setSelectedForAudit(false);
                    }
                    if(!StringUtil.isEmpty(number)){
                        auditTaskDataDtos.get(i).setSelected(true);
                    }else{
                        auditTaskDataDtos.get(i).setSelected(false);
                    }
                }
            }
        }
        ParamUtil.setSessionAttr(request,HcsaLicenceBeConstant.SEARCH_PRAM_FOR_AUDIT_LIST_RESULT,(Serializable) auditTaskDataDtos);
    }

}
