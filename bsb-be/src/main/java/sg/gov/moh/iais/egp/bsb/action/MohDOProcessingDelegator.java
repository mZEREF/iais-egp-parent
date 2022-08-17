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
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants;
import sg.gov.moh.iais.egp.bsb.dto.process.MohProcessDto;
import sg.gov.moh.iais.egp.bsb.service.MohProcessService;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_DO_RECOMMENDATION;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_FACILITY_REGISTRATION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.MOH_PROCESS_DECISION_APPROVE;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.MOH_PROCESS_DECISION_REJECT;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.KEY_MOH_PROCESS_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_TASK_ID;


@Delegator(value = "doProcessingDelegator")
@Slf4j
public class MohDOProcessingDelegator {
    private final ProcessClient processClient;
    private final MohProcessService mohProcessService;

    @Autowired
    public MohDOProcessingDelegator(ProcessClient processClient, MohProcessService mohProcessService) {
        this.processClient = processClient;
        this.mohProcessService = mohProcessService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_MOH_PROCESS_DTO);
        MaskHelper.taskProcessUnmask(request, PARAM_NAME_APP_ID, PARAM_NAME_TASK_ID);
        AuditTrailHelper.auditFunction(MODULE_FACILITY_REGISTRATION, FUNCTION_DO_RECOMMENDATION);
    }

    public void prepareData(BaseProcessClass bpc) {
        mohProcessService.prepareData(bpc, FUNCTION_DO_RECOMMENDATION);
    }

    public void prepareSwitch(BaseProcessClass bpc) {
        mohProcessService.prepareSwitch(bpc, FUNCTION_DO_RECOMMENDATION);
    }

    public void process(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String taskId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_TASK_ID);
        String appId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_APP_ID);
        MohProcessDto mohProcessDto = (MohProcessDto) ParamUtil.getSessionAttr(request, KEY_MOH_PROCESS_DTO);
        String processingDecision = mohProcessDto.getProcessingDecision();
        ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_CURRENT_TASK, FUNCTION_DO_RECOMMENDATION);
        switch (processingDecision) {
            case MOH_PROCESS_DECISION_APPROVE:
            case MOH_PROCESS_DECISION_REJECT:
                processClient.saveDOProcessingRecommendApprovalOrReject(appId, taskId, mohProcessDto);
                ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_NEXT_TASK, MasterCodeUtil.getCodeDesc(MasterCodeConstants.APP_STATUS_PEND_AO_APPROVAL));
                ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_NEXT_ROLE, ModuleCommonConstants.KEY_AO);
                break;
            case MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION:
                processClient.saveDOProcessingRequestForInformation(appId, taskId, mohProcessDto);
                ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_NEXT_TASK, MasterCodeUtil.getCodeDesc(MasterCodeConstants.APP_STATUS_PEND_APPLICANT_CLARIFICATION));
                ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_NEXT_ROLE, ModuleCommonConstants.KEY_APPLICANT);
                break;
            default:
                log.info("don't have such processingDecision {}", StringUtils.normalizeSpace(processingDecision));
                break;
        }
    }
}