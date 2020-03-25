package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditTaskDataFillterDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceBeConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.AuditAssginListValidateDto;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AuditSystemListService;
import com.ecquaria.cloud.moh.iais.service.AuditTcuListService;
import com.ecquaria.cloud.moh.iais.validation.AuditAssginListValidate;
import com.ecquaria.cloud.moh.iais.validation.AuditCancelTaskValidate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author: jiahao
 * @Date: 2020/2/20 15:08
 */
@Slf4j
@Delegator("auditTcuListDelegator")
public class AuditTcuListDelegator {
    @Autowired
    AuditTcuListService auditTcuListService;
    @Autowired
    AuditSystemListService auditSystemListService;
    private String SUBMIT_MESSAGE_SUCCESS = "submit_message_success";
    private String MAIN_URL = "mainUrl";

    public void start(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction("hcsa-application", "hcsa application");
    }

    public void init(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,"ISTUC",true);
        List<AuditTaskDataFillterDto> auditTaskDataDtos = auditTcuListService.getAuditTcuList();
        ParamUtil.setSessionAttr(request, "auditTaskDataDtos", (Serializable) auditTaskDataDtos);
        List<SelectOption> aduitTypeOp = auditSystemListService.getAuditOp();
        auditSystemListService.getInspectors(auditTaskDataDtos);
        ParamUtil.setSessionAttr(request, "aduitTypeOp", (Serializable) aduitTypeOp);
        ParamUtil.setSessionAttr(request, "modulename", "Tcu Audit List");
    }

    public void pre(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
    }

    public void preconfirm(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        getListData(request);
        AuditAssginListValidate auditAssginListValidate = new AuditAssginListValidate();
        Map<String, String> errMap = auditAssginListValidate.validate(request);
        if (errMap.isEmpty()) {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setSessionAttr(request, "actionCancel", "back");
            ParamUtil.setSessionAttr(request, "actionConfirm", "confirm");
        } else {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
        }
    }

    public AuditAssginListValidateDto getValueFromPage(HttpServletRequest request) {
        AuditAssginListValidateDto dto = new AuditAssginListValidateDto();
        getListData(request);
        return dto;
    }

    public void confirm(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the confirm start ...."));
        HttpServletRequest request = bpc.request;
        List<AuditTaskDataFillterDto> auditTaskDataDtos = (List<AuditTaskDataFillterDto>) ParamUtil.getSessionAttr(request, "auditTaskDataDtos");
//        auditSystemListService.doSubmit(auditTaskDataDtos);
        ParamUtil.setRequestAttr(request, SUBMIT_MESSAGE_SUCCESS, HcsaLicenceBeConstant.AUDIT_INSPECTION_CONFIRM_SUCCESS_MESSAGE);
        ParamUtil.setRequestAttr(request, MAIN_URL, "MohAduitTcuList");
    }

    public void cancelTask(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        getListData(request);
        AuditAssginListValidate auditAssginListValidate = new AuditAssginListValidate();
        Map<String, String> errMap = auditAssginListValidate.validate(request);
        ParamUtil.setSessionAttr(request, "actionCancel", "back");
        ParamUtil.setSessionAttr(request, "actionCancelAudit", "cancel");
    }

    public void cancel(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        List<AuditTaskDataFillterDto> auditTaskDataDtos = (List<AuditTaskDataFillterDto>) ParamUtil.getSessionAttr(request, "auditTaskDataDtos");
        AuditCancelTaskValidate auditCancelTaskValidate = new AuditCancelTaskValidate();
        Map<String, String> errorMap = auditCancelTaskValidate.validate(request);
        if (errorMap != null && errorMap.size() > 0) {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
        } else {
            ParamUtil.setRequestAttr(request, SUBMIT_MESSAGE_SUCCESS, HcsaLicenceBeConstant.AUDIT_INSPECTION_CANCEL_TASKS_SUCCESS_MESSAGE);
            ParamUtil.setRequestAttr(request, MAIN_URL, "MohAduitTcuList");
            AuditTrailHelper.auditFunction("MohAduitTcuList", "cancel tasks");
            // save cancel task
            // auditSystemListService.doCancel(auditTaskDataDtos);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
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


}
