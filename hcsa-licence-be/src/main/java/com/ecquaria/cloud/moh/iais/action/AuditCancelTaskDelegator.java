package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditTaskDataFillterDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceBeConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AuditSystemListService;
import com.ecquaria.cloud.moh.iais.service.AuditSystemPotitalListService;
import com.ecquaria.cloud.moh.iais.util.LicenceUtil;
import com.ecquaria.cloud.moh.iais.validation.AuditAssginListValidate;
import com.ecquaria.cloud.moh.iais.validation.AuditCancelOrRejectValidate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author: wangyu
 * @Date: 2020/4/14
 */
@Slf4j
@Delegator("auditCancelTaskDelegator")
public class AuditCancelTaskDelegator {
    @Autowired
    AuditSystemListService auditSystemListService;
    @Autowired
    AuditSystemPotitalListService auditSystemPotitalListService;
    private String SUBMIT_MESSAGE_SUCCESS = "submit_message_success";
    private String MAIN_URL = "mainUrl";

    public void start(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction("hcsa-licence", "cancel task");
    }

    public void init(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        List<AuditTaskDataFillterDto> auditTaskDataDtos = auditSystemPotitalListService.getSystemPotentailAdultCancelList();
        ParamUtil.setSessionAttr(request, "auditTaskDataDtos", (Serializable) auditTaskDataDtos);
        List<SelectOption> aduitTypeOp = auditSystemListService.getAuditOp();
        auditSystemListService.getInspectors(auditTaskDataDtos);
        for(AuditTaskDataFillterDto auditTaskDataFillterDto : auditTaskDataDtos ){
            ParamUtil.setSessionAttr(request, "inspectors"+auditTaskDataFillterDto.getWorkGroupId(), (Serializable) auditTaskDataFillterDto.getInspectors());
        }
        ParamUtil.setSessionAttr(request, "aduitTypeOp", (Serializable) aduitTypeOp);
        ParamUtil.setSessionAttr(request, "modulename", "Audit Cancel Task List");
    }

    public void pre(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
    }



    public void cancelTask(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
         String action = (String) ParamUtil.getSessionAttr( bpc.request,"actionTodo");
        HttpServletRequest request = bpc.request;
         if( !StringUtil.isEmpty(action)){
             // action to do  1 :  Confirm , 0 :   Reject
            if("1".equalsIgnoreCase(action)){
                ParamUtil.setRequestAttr(request, SUBMIT_MESSAGE_SUCCESS, HcsaLicenceBeConstant.AUDIT_INSPECTION_CANCEL_TASKS_SUCCESS_MESSAGE_CONFIRM);
                ParamUtil.setRequestAttr(request, MAIN_URL, "MohAduitCancelTask");
            }else {
                ParamUtil.setRequestAttr(request, SUBMIT_MESSAGE_SUCCESS, HcsaLicenceBeConstant.AUDIT_INSPECTION_CANCEL_TASKS_SUCCESS_MESSAGE_REJECT );
                ParamUtil.setRequestAttr(request, MAIN_URL, "MohAduitCancelTask");
            }
         }
        ParamUtil.setSessionAttr(bpc.request,"actionTodo", null);
    }

    public void vad(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        getListData(request);
        AuditCancelOrRejectValidate auditCancelOrRejectValidate = new AuditCancelOrRejectValidate();
        Map<String, String> errMap = auditCancelOrRejectValidate.validate(request);
        if (!errMap.isEmpty()) {
            List<AuditTaskDataFillterDto> auditTaskDataDtos  = (List<AuditTaskDataFillterDto>)ParamUtil.getSessionAttr(request,"auditTaskDataDtos");

            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
        } else {
            // action to do  1 :  Confirm , 0 :   Reject
            ParamUtil.setSessionAttr(request,"actionTodo", ParamUtil.getString(request,"actionTodo"));
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
        }
    }


    private void getListData(HttpServletRequest request) {
        List<AuditTaskDataFillterDto> auditTaskDataDtos  = (List<AuditTaskDataFillterDto>)ParamUtil.getSessionAttr(request,"auditTaskDataDtos");
        if(!IaisCommonUtils.isEmpty(auditTaskDataDtos)){
            for(int i = 0;i<auditTaskDataDtos.size();i++){
                if(auditTaskDataDtos.get(i).isAddAuditList()){
                    String auditType = ParamUtil.getString(request,i+"auditType");
                    String inspectorId = ParamUtil.getString(request,i+"insOp");
                    String inspectorName = LicenceUtil.getSelectOptionTextFromSelectOptions(auditTaskDataDtos.get(i).getInspectors(),inspectorId);
                    String forad = ParamUtil.getString(request,i+"selectForAd");
                    String number = ParamUtil.getString(request,i+"number");
                    auditTaskDataDtos.get(i).setAuditType(auditType);
                    auditTaskDataDtos.get(i).setInspectorId(inspectorId);
                    auditTaskDataDtos.get(i).setInspector(inspectorName);
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
        ParamUtil.setSessionAttr(request,"auditTaskDataDtos",(Serializable) auditTaskDataDtos);
    }




}
