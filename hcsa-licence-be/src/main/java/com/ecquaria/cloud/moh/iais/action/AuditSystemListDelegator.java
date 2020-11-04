package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
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
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AuditSystemListService;
import com.ecquaria.cloud.moh.iais.service.AuditSystemPotitalListService;
import com.ecquaria.cloud.moh.iais.util.LicenceUtil;
import com.ecquaria.cloud.moh.iais.validation.AduitSystemGenerateValidate;
import com.ecquaria.cloud.moh.iais.validation.AuditAssginListValidate;
import com.ecquaria.cloud.moh.iais.validation.AuditCancelTaskValidate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Author: jiahao
 * @Date: 2020/2/19 10:45
 */
@Slf4j
@Delegator("auditSystemListDelegator")
public class AuditSystemListDelegator {

    @Autowired
    AuditSystemPotitalListService auditSystemPotitalListService;
    @Autowired
    AuditSystemListService auditSystemListService;


    private String SESSION_AUDIT_SYSTEM_POTENTIAL_DTO_FOR_SEARCH_NAME = "auditSystemPotentialDtoForSearch";
    private String  SUBMIT_MESSAGE_SUCCESS = "submit_message_success";
    private String  MAIN_URL              ="mainUrl";
    public void start(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_AUDIT_INSPECTION, AuditTrailConsts.FUNCTION_SYSTEM_AUDIT_LIST);
    }

    public void init(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,"ISTUC",Boolean.FALSE);
        ParamUtil.setSessionAttr(request, SESSION_AUDIT_SYSTEM_POTENTIAL_DTO_FOR_SEARCH_NAME, null);
        ParamUtil.setSessionAttr(request,"modulename",AuditTrailConsts.FUNCTION_SYSTEM_AUDIT_LIST);
        ParamUtil.setSessionAttr(request, HcsaLicenceBeConstant.SEARCH_PRAM_FOR_AUDIT_LIST,null);
        ParamUtil.setSessionAttr(request, HcsaLicenceBeConstant.SEARCH_PRAM_FOR_AUDIT_LIST_TRUE_RESULT,null);
    }

    public void pre(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        List<HcsaServiceDto> hcsaServiceDtos = auditSystemListService.getActiveHCIServices();
        ParamUtil.setRequestAttr(request, "activeHCIServiceNames", (Serializable) auditSystemListService.getActiveHCIServicesByNameOrCode(hcsaServiceDtos, HcsaLicenceBeConstant.GET_HCI_SERVICE_SELECTION_NAME_TAG));
        ParamUtil.setRequestAttr(request, "activeHCIServiceCodes", (Serializable) auditSystemListService.getActiveHCIServicesByNameOrCode(hcsaServiceDtos, HcsaLicenceBeConstant.GET_HCI_SERVICE_SELECTION_COED_TAG));
        ParamUtil.setRequestAttr(request, "complianceLastResultOptions", (Serializable) LicenceUtil.getResultsLastCompliance());
        ParamUtil.setRequestAttr(request, "premTypeOp", (Serializable) LicenceUtil.getPremisesType());
        ParamUtil.setRequestAttr(request, "hclCodeOp", (Serializable) auditSystemListService.getActiveHCICode());
        ParamUtil.setRequestAttr(request,"riskTypeOp", (Serializable) MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_SYSTEM_RISK_TYPE));
    }

    public void vad(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the vad start ...."));
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
        String genNum = ParamUtil.getString(request, "genNum");
        String svcNameSelect = ParamUtil.getStringsToString(request, "svcName");
        String svcNameCodeSelect = ParamUtil.getStringsToString(request, "hclSCode");
        AuditSystemPotentialDto dto = auditSystemPotitalListService.initDtoForSearch();
        List<String> serviceNmaeList = IaisCommonUtils.genNewArrayList();
        if(serviceNames != null && serviceNames.length >0){
            serviceNmaeList.addAll(Arrays.asList(serviceNames));
        }
        List<String> hcsaServiceCodeList = IaisCommonUtils.genNewArrayList();
        if(hclSCodes != null && hclSCodes.length > 0) {
            serviceNmaeList.addAll(Arrays.asList(hclSCodes));
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
        dto.setGenerateNumString(genNum);
        if (!StringUtil.isEmpty(genNum) && StringUtil.stringIsFewDecimal(genNum,null))
            dto.setGenerateNum(Integer.valueOf(genNum));
        ParamUtil.setSessionAttr(request, SESSION_AUDIT_SYSTEM_POTENTIAL_DTO_FOR_SEARCH_NAME, dto);
        AduitSystemGenerateValidate auditTestValidate = new AduitSystemGenerateValidate();
        Map<String, String> errMap = auditTestValidate.validate(request);
        if (errMap.isEmpty()) {
            List<SelectOption> aduitTypeOp = auditSystemListService.getAuditOp();
            ParamUtil.setSessionAttr(request, "aduitTypeOp", (Serializable) aduitTypeOp);
            List<AuditTaskDataFillterDto> auditTaskDataDtos = auditSystemPotitalListService.getSystemPotentailAdultList(dto);
            auditTaskDataDtos =  auditSystemListService.getInspectors(auditTaskDataDtos);
            if(!IaisCommonUtils.isEmpty(auditTaskDataDtos)){
                for(AuditTaskDataFillterDto auditTaskDataFillterDto : auditTaskDataDtos ){
                    ParamUtil.setSessionAttr(request, "inspectors"+auditTaskDataFillterDto.getWorkGroupId(), (Serializable) auditTaskDataFillterDto.getInspectors());
                }
            }
            ParamUtil.setSessionAttr(request, HcsaLicenceBeConstant.SEARCH_PRAM_FOR_AUDIT_LIST,dto.getSearchParam());
            ParamUtil.setSessionAttr(request, HcsaLicenceBeConstant.SEARCH_PRAM_FOR_AUDIT_LIST_TRUE_RESULT,dto.getSearchResult());
            ParamUtil.setSessionAttr(request, HcsaLicenceBeConstant.SEARCH_PRAM_FOR_AUDIT_LIST_RESULT, (Serializable) auditTaskDataDtos);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
        } else {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
        }
    }

    public void next(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the next start ...."));
        HttpServletRequest request = bpc.request;

    }

    public void listpageNext(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_AUDIT_INSPECTION, AuditTrailConsts.FUNCTION_SYSTEM_AUDIT_LIST);
    }
    public void doPage(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doPage start ...."));
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

    public void remove(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        List<AuditTaskDataFillterDto> auditTaskDataDtos = (List<AuditTaskDataFillterDto>) ParamUtil.getSessionAttr(request, HcsaLicenceBeConstant.SEARCH_PRAM_FOR_AUDIT_LIST_RESULT);
        getListData(request);
        auditTaskDataDtos = auditSystemListService.doRemove(auditTaskDataDtos);
        ParamUtil.setSessionAttr(request, HcsaLicenceBeConstant.SEARCH_PRAM_FOR_AUDIT_LIST_RESULT, (Serializable) auditTaskDataDtos);
    }

    public void confirm(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        getListData(request);
        AuditAssginListValidate auditAssginListValidate = new AuditAssginListValidate();
        Map<String, String> errMap = auditAssginListValidate.validate(request);
        if (errMap.isEmpty()) {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setSessionAttr(request,"actionCancel","back");
            ParamUtil.setSessionAttr(request,"actionConfirm","submit");
        } else {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
        }
    }

    private void getListData(HttpServletRequest request) {
        List<AuditTaskDataFillterDto> auditTaskDataDtos = (List<AuditTaskDataFillterDto>) ParamUtil.getSessionAttr(request, HcsaLicenceBeConstant.SEARCH_PRAM_FOR_AUDIT_LIST_RESULT);
        if (!IaisCommonUtils.isEmpty(auditTaskDataDtos)) {
            for (int i = 0; i < auditTaskDataDtos.size(); i++) {
                String auditType = ParamUtil.getString(request, i + "auditType");
                String inspectorId;
                try {
                    inspectorId = ParamUtil.getMaskedString(request, i + "insOp");
                }catch (Exception e){
                    inspectorId = ParamUtil.getRequestString(request, i + "insOp");
                }
                AuditTaskDataFillterDto auditTaskDataFillterDto = auditTaskDataDtos.get(i);
                if(!auditTaskDataFillterDto.isAudited()){
                    String inspectorName = LicenceUtil.getSelectOptionTextFromSelectOptions(auditTaskDataFillterDto.getInspectors(), inspectorId);
                    auditTaskDataFillterDto.setInspector(inspectorName);
                    auditTaskDataFillterDto.setInspectorId(inspectorId);
                    auditTaskDataFillterDto.setAuditType(auditType);
                }
                String forad = ParamUtil.getString(request, i + "selectForAd");
                String number = ParamUtil.getString(request, i + "number");
                if (!StringUtil.isEmpty(forad)) {
                    auditTaskDataFillterDto.setSelectedForAudit(true);
                } else {
                    auditTaskDataFillterDto.setSelectedForAudit(false);
                }
                if (!StringUtil.isEmpty(number)) {
                    auditTaskDataFillterDto.setSelected(true);
                } else {
                    auditTaskDataFillterDto.setSelected(false);
                }
            }
        }
        ParamUtil.setSessionAttr(request, HcsaLicenceBeConstant.SEARCH_PRAM_FOR_AUDIT_LIST_RESULT, (Serializable) auditTaskDataDtos);
    }



    public AuditAssginListValidateDto getValueFromPage(HttpServletRequest request) {
        AuditAssginListValidateDto dto = new AuditAssginListValidateDto();
        getListData(request);
        return dto;
    }

    public void precreatehcl(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_AUDIT_INSPECTION, AuditTrailConsts.FUNCTION_SYSTEM_AUDIT_LIST);
    }

    public void precanceltask(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_AUDIT_INSPECTION, AuditTrailConsts.FUNCTION_CANCEL_AUDIT_INSP);
        getListData(request);
        AuditAssginListValidate auditAssginListValidate = new AuditAssginListValidate();
        Map<String, String> errMap = auditAssginListValidate.validate(request);
        if (errMap.isEmpty()) {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setSessionAttr(request,"actionCancel","back");
            ParamUtil.setSessionAttr(request,"actionCancelAudit","cancel");
        } else {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
        }
    }

    public void submit(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the submit start ...."));
        HttpServletRequest request = bpc.request;
        List<AuditTaskDataFillterDto> auditTaskDataDtos = (List<AuditTaskDataFillterDto>) ParamUtil.getSessionAttr(request, HcsaLicenceBeConstant.SEARCH_PRAM_FOR_AUDIT_LIST_RESULT);
        auditSystemListService.doSubmit(auditTaskDataDtos);
        ParamUtil.setRequestAttr(request,SUBMIT_MESSAGE_SUCCESS,MessageUtil.getMessageDesc("AUDIT_ACK001"));
        ParamUtil.setRequestAttr(request,MAIN_URL,"MohAduitSystemList");
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_AUDIT_INSPECTION, AuditTrailConsts.FUNCTION_SYSTEM_AUDIT_LIST);

    }

    public void createhcl(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_AUDIT_INSPECTION, AuditTrailConsts.FUNCTION_SYSTEM_AUDIT_LIST);
    }

    public void canceltask(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        List<AuditTaskDataFillterDto> auditTaskDataDtos = (List<AuditTaskDataFillterDto>) ParamUtil.getSessionAttr(request, HcsaLicenceBeConstant.SEARCH_PRAM_FOR_AUDIT_LIST_RESULT);
        AuditCancelTaskValidate auditCancelTaskValidate = new AuditCancelTaskValidate();
        Map<String, String> errorMap = auditCancelTaskValidate.validate(request);
        if(errorMap != null && errorMap.size() >0){
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
        }else {
            ParamUtil.setRequestAttr(request,SUBMIT_MESSAGE_SUCCESS,MessageUtil.getMessageDesc("AUDIT_ACK002"));
            ParamUtil.setRequestAttr(request,MAIN_URL,"MohAduitSystemList");
            AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_AUDIT_INSPECTION, AuditTrailConsts.FUNCTION_CANCEL_AUDIT_INSP);
            // save cancel task
            auditSystemListService.doCancel(auditTaskDataDtos);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
        }
    }

    public void actionButton(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the actionButton start ...."));
        HttpServletRequest request = bpc.request;
        String action = ParamUtil.getString(request,"crud_action_type");
        if(StringUtil.isEmpty(action)){
            log.info(StringUtil.changeForLog("crud_action_type is null"));
        }else {
            log.info(StringUtil.changeForLog("crud_action_type is " + action));
        }
        ParamUtil.setRequestAttr(request,"crud_action_type",action);
        log.info(StringUtil.changeForLog("the actionButton end ...."));
    }

}
