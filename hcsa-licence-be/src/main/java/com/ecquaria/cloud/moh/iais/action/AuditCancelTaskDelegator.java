package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditTaskDataFillterDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AuditSystemListService;
import com.ecquaria.cloud.moh.iais.service.AuditSystemPotitalListService;
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
    private static final String SUBMIT_MESSAGE_SUCCESS = "submit_message_success";
    private static final String MAIN_URL = "mainUrl";

    private static final String AUDIT_TASK_DATA_DTOS=  "auditTaskDataDtos";
    private static final String ACTION_TODO=  "actionTodo";

    public void start(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."+bpc));
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_AUDIT_INSPECTION, AuditTrailConsts.FUNCTION_CANCEL_AUDIT_INSP);
    }

    public void init(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart init ...."));
    }

    public void pre(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart pre ...."));
        HttpServletRequest request = bpc.request;
        List<AuditTaskDataFillterDto> auditTaskDataDtos = auditSystemPotitalListService.getSystemPotentailAdultCancelList();
        auditTaskDataDtos = auditSystemListService.getInspectors(auditTaskDataDtos);
        ParamUtil.setSessionAttr(request, AUDIT_TASK_DATA_DTOS, (Serializable) auditTaskDataDtos);
        ParamUtil.setSessionAttr(request, "modulename", AuditTrailConsts.FUNCTION_CANCEL_AUDIT_INSP);
    }



    public void cancelTask(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart cancelTask ...."));
         String action = (String) ParamUtil.getSessionAttr( bpc.request,ACTION_TODO);
        HttpServletRequest request = bpc.request;
        List<AuditTaskDataFillterDto> auditTaskDataDtos  = (List<AuditTaskDataFillterDto>)ParamUtil.getSessionAttr(request,AUDIT_TASK_DATA_DTOS);
         if( !StringUtil.isEmpty(action)){
             // action to do  1 :  Confirm , 0 :   Reject
            if("1".equalsIgnoreCase(action)){
                auditSystemListService.doCanceledTask(auditTaskDataDtos);
                ParamUtil.setRequestAttr(request, SUBMIT_MESSAGE_SUCCESS, MessageUtil.getMessageDesc("AUDIT_ACK003"));
                ParamUtil.setRequestAttr(request, MAIN_URL, "MohAduitCancelTask");
            }else {
                auditSystemListService.doRejectCancelTask(auditTaskDataDtos);
                ParamUtil.setRequestAttr(request, SUBMIT_MESSAGE_SUCCESS, MessageUtil.getMessageDesc("AUDIT_ACK004") );
                ParamUtil.setRequestAttr(request, MAIN_URL, "MohAduitCancelTask");
            }
         }
        ParamUtil.setSessionAttr(bpc.request,ACTION_TODO, null);
    }

    public void vad(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart vad ...."));
        HttpServletRequest request = bpc.request;
        getListData(request);
        AuditCancelOrRejectValidate auditCancelOrRejectValidate = new AuditCancelOrRejectValidate();
        Map<String, String> errMap = auditCancelOrRejectValidate.validate(request);
        if (!errMap.isEmpty()) {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
        } else {
            // action to do  1 :  Confirm , 0 :   Reject
            ParamUtil.setSessionAttr(request,ACTION_TODO, ParamUtil.getString(request,ACTION_TODO));
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
        }
    }


    private void getListData(HttpServletRequest request) {
        List<AuditTaskDataFillterDto> auditTaskDataDtos  = (List<AuditTaskDataFillterDto>)ParamUtil.getSessionAttr(request,AUDIT_TASK_DATA_DTOS);
        if(!IaisCommonUtils.isEmpty(auditTaskDataDtos)){
            for(int i = 0;i<auditTaskDataDtos.size();i++){
                    String forad = ParamUtil.getString(request,i+"selectForAd");
                    String reason =  ParamUtil.getString(request,i+"newReason");
                    if(!StringUtil.isEmpty(forad)){
                        auditTaskDataDtos.get(i).setSelectedForAudit(true);
                    }else{
                        auditTaskDataDtos.get(i).setSelectedForAudit(false);
                    }
                    auditTaskDataDtos.get(i).setReasonForAO(reason);
            }
        }
        ParamUtil.setSessionAttr(request,AUDIT_TASK_DATA_DTOS,(Serializable) auditTaskDataDtos);
    }




}
