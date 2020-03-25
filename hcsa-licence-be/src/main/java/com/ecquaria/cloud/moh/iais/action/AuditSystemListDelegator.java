package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
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
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AuditSystemListService;
import com.ecquaria.cloud.moh.iais.service.AuditSystemPotitalListService;
import com.ecquaria.cloud.moh.iais.util.LicenceUtil;
import com.ecquaria.cloud.moh.iais.validation.AduitSystemGenerateValidate;
import com.ecquaria.cloud.moh.iais.validation.AuditAssginListValidate;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.ecquaria.cloud.moh.iais.validation.AuditCancelTaskValidate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

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
        AuditTrailHelper.auditFunction("hcsa-application", "hcsa application");
    }

    public void init(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, SESSION_AUDIT_SYSTEM_POTENTIAL_DTO_FOR_SEARCH_NAME, null);
        ParamUtil.setSessionAttr(request,"modulename","System Audit List");
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
        String serviceName = ParamUtil.getString(request, "svcName");
        String postcode = ParamUtil.getString(request, "postcode");
        String inspectionStartDate = ParamUtil.getDate(request, "inspectionStartDate");
        String inspectionEndDate = ParamUtil.getDate(request, "inspectionEndDate");
        String complianceLastResult = ParamUtil.getString(request, "complianceLastResult");
        String hclSCode = ParamUtil.getString(request, "hclSCode");
        String hclCode = ParamUtil.getString(request, "hclCode");
        String premType = ParamUtil.getString(request, "premType");
        String riskType = ParamUtil.getString(request, "riskType");
        String genNum = ParamUtil.getString(request, "genNum");
        List<SelectOption> aduitTypeOp = auditSystemListService.getAuditOp();
        ParamUtil.setSessionAttr(request, "aduitTypeOp", (Serializable) aduitTypeOp);
        AuditSystemPotentialDto dto = new AuditSystemPotentialDto();
        List<String> serviceNmaeList = IaisCommonUtils.genNewArrayList();
        if(!StringUtil.isEmpty(serviceName)) {
            serviceNmaeList.add(serviceName);
        }
        List<String> hcsaServiceCodeList = IaisCommonUtils.genNewArrayList();
        if(!StringUtil.isEmpty(hclSCode)) {
            hcsaServiceCodeList.add(hclSCode);
        }
        dto.setSvcNameList(serviceNmaeList);
        dto.setHcsaServiceCodeList(hcsaServiceCodeList);
        dto.setPostalCode(postcode);
        dto.setLastInspectionStart(inspectionStartDate);
        dto.setLastInspectionEnd(inspectionEndDate);
        dto.setHclCode(hclCode);
        dto.setResultLastCompliance(complianceLastResult);
        dto.setPremisesType(premType);
        dto.setTypeOfRisk(riskType);
        if (!StringUtil.isEmpty(genNum))
            dto.setGenerateNum(Integer.parseInt(genNum));
        ParamUtil.setSessionAttr(request, SESSION_AUDIT_SYSTEM_POTENTIAL_DTO_FOR_SEARCH_NAME, dto);
        List<AuditTaskDataFillterDto> auditTaskDataDtos = auditSystemPotitalListService.getSystemPotentailAdultList(dto);
        auditSystemListService.getInspectors(auditTaskDataDtos);
        ParamUtil.setSessionAttr(request, "auditTaskDataDtos", (Serializable) auditTaskDataDtos);
        AduitSystemGenerateValidate auditTestValidate = new AduitSystemGenerateValidate();
        Map<String, String> errMap = auditTestValidate.validate(request);
        if (errMap.isEmpty()) {
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
        AuditTrailHelper.auditFunction("hcsa-application", "hcsa application");
    }

    public void remove(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        List<AuditTaskDataFillterDto> auditTaskDataDtos = (List<AuditTaskDataFillterDto>) ParamUtil.getSessionAttr(request, "auditTaskDataDtos");
        getListData(request);
        auditTaskDataDtos = auditSystemListService.doRemove(auditTaskDataDtos);
        ParamUtil.setSessionAttr(request, "auditTaskDataDtos", (Serializable) auditTaskDataDtos);
    }

    public void confirm(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        getListData(request);
        AuditAssginListValidate auditAssginListValidate = new AuditAssginListValidate();
        Map<String, String> errMap = auditAssginListValidate.validate(request);
        if (errMap.isEmpty()) {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setRequestAttr(request,"actionCancel","back");
            ParamUtil.setRequestAttr(request,"actionConfirm","submit");
        } else {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
        }
    }

    private void getListData(HttpServletRequest request) {
        List<AuditTaskDataFillterDto> auditTaskDataDtos = (List<AuditTaskDataFillterDto>) ParamUtil.getSessionAttr(request, "auditTaskDataDtos");
        if (!IaisCommonUtils.isEmpty(auditTaskDataDtos)) {
            for (int i = 0; i < auditTaskDataDtos.size(); i++) {
                String auditType = ParamUtil.getString(request, i + "auditType");
                String inspectorId = ParamUtil.getString(request, i + "insOp");
                String inspectorName = getNameById(auditTaskDataDtos.get(i).getInspectors(), inspectorId);
                String forad = ParamUtil.getString(request, i + "selectForAd");
                String number = ParamUtil.getString(request, i + "number");
                auditTaskDataDtos.get(i).setAuditType(auditType);
                auditTaskDataDtos.get(i).setInspectorId(inspectorId);
                auditTaskDataDtos.get(i).setInspector(inspectorName);
                if (!StringUtil.isEmpty(forad)) {
                    auditTaskDataDtos.get(i).setSelectedForAudit(true);
                } else {
                    auditTaskDataDtos.get(i).setSelectedForAudit(false);
                }
                if (!StringUtil.isEmpty(number)) {
                    auditTaskDataDtos.get(i).setSelected(true);
                } else {
                    auditTaskDataDtos.get(i).setSelected(false);
                }
            }
        }
        ParamUtil.setSessionAttr(request, "auditTaskDataDtos", (Serializable) auditTaskDataDtos);
    }

    private String getNameById(List<SelectOption> inspectors, String inspectorId) {
        if (!IaisCommonUtils.isEmpty(inspectors)) {
            for (SelectOption temp : inspectors) {
                if (inspectorId.equals(temp.getValue())) {
                    return temp.getText();
                }
            }
        }
        return null;
    }

    public AuditAssginListValidateDto getValueFromPage(HttpServletRequest request) {
        AuditAssginListValidateDto dto = new AuditAssginListValidateDto();
        getListData(request);
        return dto;
    }

    public void precreatehcl(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction("hcsa-application", "hcsa application");
    }

    public void precanceltask(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction("hcsa-application", "hcsa application");
        getListData(request);
        AuditAssginListValidate auditAssginListValidate = new AuditAssginListValidate();
        Map<String, String> errMap = auditAssginListValidate.validate(request);
        if (errMap.isEmpty()) {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setRequestAttr(request,"actionCancel","back");
            ParamUtil.setRequestAttr(request,"actionCancelAudit","cancel");
        } else {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
        }
    }

    public void submit(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the submit start ...."));
        HttpServletRequest request = bpc.request;
        List<AuditTaskDataFillterDto> auditTaskDataDtos = (List<AuditTaskDataFillterDto>) ParamUtil.getSessionAttr(request, "auditTaskDataDtos");
     //   auditSystemListService.doSubmit(auditTaskDataDtos);
        ParamUtil.setRequestAttr(request,SUBMIT_MESSAGE_SUCCESS,HcsaLicenceBeConstant .AUDIT_INSPECTION_CONFIRM_SUCCESS_MESSAGE);
        ParamUtil.setRequestAttr(request,MAIN_URL,"MohAduitSystemList");
        AuditTrailHelper.auditFunction("hcsa-application", "hcsa application");

    }

    public void createhcl(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction("hcsa-application", "hcsa application");
    }

    public void canceltask(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        List<AuditTaskDataFillterDto> auditTaskDataDtos = (List<AuditTaskDataFillterDto>) ParamUtil.getSessionAttr(request, "auditTaskDataDtos");
        AuditCancelTaskValidate auditCancelTaskValidate = new AuditCancelTaskValidate();
        Map<String, String> errorMap = auditCancelTaskValidate.validate(request);
        if(errorMap != null && errorMap.size() >0){
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
        }else {
            ParamUtil.setRequestAttr(request,SUBMIT_MESSAGE_SUCCESS,HcsaLicenceBeConstant .AUDIT_INSPECTION_CANCEL_TASKS_SUCCESS_MESSAGE);
            ParamUtil.setRequestAttr(request,MAIN_URL,"MohAduitSystemList");
            AuditTrailHelper.auditFunction("MohAduitSystemList", "cancel tasks");
            // save cancel task
            //auditSystemListService.doCancel(auditTaskDataDtos);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
        }
    }

}
