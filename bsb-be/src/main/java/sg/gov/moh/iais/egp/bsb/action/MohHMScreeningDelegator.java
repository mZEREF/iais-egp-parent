package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.ProcessClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants;
import sg.gov.moh.iais.egp.bsb.dto.process.MohProcessDto;
import sg.gov.moh.iais.egp.bsb.service.MohProcessService;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.*;

/**
 * @author : LiRan
 * @date : 2021/11/22
 */
@Delegator(value = "hmScreeningDelegator")
@Slf4j
public class MohHMScreeningDelegator {
    private final ProcessClient processClient;
    private final MohProcessService mohProcessService;

    @Autowired
    public MohHMScreeningDelegator(ProcessClient processClient, MohProcessService mohProcessService) {
        this.processClient = processClient;
        this.mohProcessService = mohProcessService;
    }


    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_MOH_PROCESS_DTO);
        MaskHelper.taskProcessUnmask(request, PARAM_NAME_APP_ID, PARAM_NAME_TASK_ID);
        AuditTrailHelper.auditFunction(MODULE_NAME, FUNCTION_NAME_HM_SCREENING);
    }

    public void prepareData(BaseProcessClass bpc) {
        mohProcessService.prepareData(bpc, MODULE_NAME_HM_SCREENING);
    }

    public void prepareSwitch(BaseProcessClass bpc){
        mohProcessService.prepareSwitch(bpc, MODULE_NAME_HM_SCREENING);
    }

    public void process(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String taskId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_TASK_ID);
        String appId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_APP_ID);
        MohProcessDto mohProcessDto = (MohProcessDto) ParamUtil.getSessionAttr(request, KEY_MOH_PROCESS_DTO);
        processClient.saveHmScreeningApproveOrReject(appId, taskId, mohProcessDto);
        ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_CURRENT_TASK, "Higher Management Screening");
        ParamUtil.setRequestAttr(request,TaskModuleConstants.KEY_NEXT_TASK, MasterCodeConstants.APP_STATUS_PEND_AO+" Screening");
        ParamUtil.setRequestAttr(request,TaskModuleConstants.KEY_NEXT_ROLE, ModuleCommonConstants.KEY_AO);
    }
}