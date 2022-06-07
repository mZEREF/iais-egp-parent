package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.ProcessClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.StageConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants;
import sg.gov.moh.iais.egp.bsb.dto.process.MohProcessDto;
import sg.gov.moh.iais.egp.bsb.service.MohProcessService;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.MOH_PROCESSING_DECISION_APPROVE;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.MOH_PROCESSING_DECISION_REJECT;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.MOH_PROCESSING_DECISION_ROUTE_BACK_TO_DO;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.MOH_PROCESSING_DECISION_ROUTE_BACK_TO_HM;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.FUNCTION_NAME_AO_PROCESSING;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.KEY_MOH_PROCESS_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.MODULE_NAME;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.MODULE_NAME_AO_PROCESSING;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_TASK_ID;


/**
 * @author : LiRan
 * @date : 2021/11/22
 */
@Delegator(value = "aoProcessingDelegator")
@Slf4j
public class MohAOProcessingDelegator {
    private final ProcessClient processClient;
    private final MohProcessService mohProcessService;

    @Autowired
    public MohAOProcessingDelegator(ProcessClient processClient, MohProcessService mohProcessService) {
        this.processClient = processClient;
        this.mohProcessService = mohProcessService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_MOH_PROCESS_DTO);
        MaskHelper.taskProcessUnmask(request, PARAM_NAME_APP_ID, PARAM_NAME_TASK_ID);
        AuditTrailHelper.auditFunction(MODULE_NAME, FUNCTION_NAME_AO_PROCESSING);
    }

    public void prepareData(BaseProcessClass bpc) {
        mohProcessService.prepareData(bpc, MODULE_NAME_AO_PROCESSING);
    }

    public void prepareSwitch(BaseProcessClass bpc){
        mohProcessService.prepareSwitch(bpc, MODULE_NAME_AO_PROCESSING);
    }

    public void process(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String taskId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_TASK_ID);
        String appId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_APP_ID);
        MohProcessDto mohProcessDto = (MohProcessDto) ParamUtil.getSessionAttr(request, KEY_MOH_PROCESS_DTO);
        String processingDecision = mohProcessDto.getProcessingDecision();
        ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_CURRENT_TASK, "Approval Officer Processing");
        switch (processingDecision) {
            case MOH_PROCESSING_DECISION_APPROVE:
                processClient.saveAoProcessingApprove(appId, taskId, mohProcessDto);
                ParamUtil.setRequestAttr(request,TaskModuleConstants.KEY_NEXT_TASK,MasterCodeConstants.APP_STATUS_PENDING_DO_APPROVAL_LETTER_DRAFT);
                ParamUtil.setRequestAttr(request,TaskModuleConstants.KEY_NEXT_ROLE, ModuleCommonConstants.KEY_DO);
                break;
            case MOH_PROCESSING_DECISION_REJECT:
                processClient.saveAoProcessingReject(appId, taskId, mohProcessDto);
                break;
            case MOH_PROCESSING_DECISION_ROUTE_BACK_TO_DO:
                processClient.saveAoProcessingRouteBackToDo(appId, taskId, mohProcessDto);
                ParamUtil.setRequestAttr(request,TaskModuleConstants.KEY_NEXT_TASK, MasterCodeUtil.getCodeDesc(MasterCodeConstants.APP_STATUS_PEND_DO)+" Processing");
                ParamUtil.setRequestAttr(request,TaskModuleConstants.KEY_NEXT_ROLE, ModuleCommonConstants.KEY_DO);
                break;
            case MOH_PROCESSING_DECISION_ROUTE_BACK_TO_HM:
                processClient.saveAoProcessingRouteToHm(appId, taskId, mohProcessDto);
                ParamUtil.setRequestAttr(request,TaskModuleConstants.KEY_NEXT_TASK,MasterCodeUtil.getCodeDesc(MasterCodeConstants.APP_STATUS_PEND_HM));
                ParamUtil.setRequestAttr(request,TaskModuleConstants.KEY_NEXT_ROLE, ModuleCommonConstants.KEY_HM);
                break;
            default:
                log.info("don't have such processingDecision {}", StringUtils.normalizeSpace(processingDecision));
                break;
        }
    }
}