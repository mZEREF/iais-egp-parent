package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.ProcessClient;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants;
import sg.gov.moh.iais.egp.bsb.dto.process.MohProcessDto;
import sg.gov.moh.iais.egp.bsb.service.MohProcessService;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_AO_SCREENING;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_FACILITY_REGISTRATION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_DO_RECOMMENDATION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_DO_SCREENING;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_HM_DECISION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_INSPECTION_TASK_ASSIGNMENT;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.MOH_PROCESS_DECISION_APPROVE_TO_PROCEED_TO_NEXT_STAGE;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.MOH_PROCESS_DECISION_REJECT;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_BACK_TO_DO;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_TO_HM;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.KEY_MOH_PROCESS_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_TASK_ID;


@Delegator(value = "aoScreeningDelegator")
@Slf4j
public class MohAOScreeningDelegator {
    private final ProcessClient processClient;
    private final MohProcessService mohProcessService;

    @Autowired
    public MohAOScreeningDelegator(ProcessClient processClient, MohProcessService mohProcessService) {
        this.processClient = processClient;
        this.mohProcessService = mohProcessService;
    }


    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_MOH_PROCESS_DTO);
        MaskHelper.taskProcessUnmask(request, PARAM_NAME_APP_ID, PARAM_NAME_TASK_ID);
        AuditTrailHelper.auditFunction(MODULE_FACILITY_REGISTRATION, FUNCTION_AO_SCREENING);
    }

    public void prepareData(BaseProcessClass bpc) {
        mohProcessService.prepareData(bpc, FUNCTION_AO_SCREENING);
    }

    public void prepareSwitch(BaseProcessClass bpc) {
        mohProcessService.prepareSwitch(bpc, FUNCTION_AO_SCREENING);
    }

    public void process(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String taskId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_TASK_ID);
        String appId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_APP_ID);
        MohProcessDto mohProcessDto = (MohProcessDto) ParamUtil.getSessionAttr(request, KEY_MOH_PROCESS_DTO);
        String processingDecision = mohProcessDto.getProcessingDecision();
        ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_CURRENT_TASK, FUNCTION_AO_SCREENING);
        switch (processingDecision) {
            case MOH_PROCESS_DECISION_APPROVE_TO_PROCEED_TO_NEXT_STAGE:
                String nextAppStatus = processClient.saveAOScreeningApprove(appId, taskId, mohProcessDto);
                if (nextAppStatus.equals(APP_STATUS_PEND_INSPECTION_TASK_ASSIGNMENT)) {
                    ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_NEXT_ROLE, ModuleCommonConstants.KEY_APPLICANT);
                } else if (nextAppStatus.equals(APP_STATUS_PEND_DO_RECOMMENDATION)) {
                    ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_NEXT_ROLE, ModuleCommonConstants.KEY_DO);
                }
                ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_NEXT_TASK, MasterCodeUtil.getCodeDesc(nextAppStatus));
                break;
            case MOH_PROCESS_DECISION_REJECT:
                processClient.saveAOScreeningReject(appId, taskId, mohProcessDto);
                break;
            case MOH_PROCESS_DECISION_ROUTE_BACK_TO_DO:
                processClient.saveAOScreeningRouteBackToDo(appId, taskId, mohProcessDto);
                ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_NEXT_TASK, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_DO_SCREENING));
                ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_NEXT_ROLE, ModuleCommonConstants.KEY_DO);
                break;
            case MOH_PROCESS_DECISION_ROUTE_TO_HM:
                processClient.saveAOScreeningRouteToHm(appId, taskId, mohProcessDto);
                ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_NEXT_TASK, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_HM_DECISION));
                ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_NEXT_ROLE, ModuleCommonConstants.KEY_HM);
                break;
            default:
                log.info("don't have such processingDecision {}", StringUtils.normalizeSpace(processingDecision));
                break;
        }
    }
}