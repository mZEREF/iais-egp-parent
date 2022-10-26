package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.ProcessClient;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.dto.process.MohProcessDto;
import sg.gov.moh.iais.egp.bsb.service.DualDocSortingService;
import sg.gov.moh.iais.egp.bsb.service.MohProcessService;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_AO_APPROVAL;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_FACILITY_REGISTRATION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.MOH_PROCESS_DECISION_APPROVE_APPLICATION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.MOH_PROCESS_DECISION_REJECT_APPLICATION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_BACK_TO_DO;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_TO_HM;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_INTERNAL_DOC_BSB_ASSESSMENT_AND_RECOMMENDATION;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.KEY_MOH_PROCESS_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_TASK_ID;


@Slf4j
@RequiredArgsConstructor
@Delegator(value = "aoProcessRenewDeferDelegator")
public class MohAOProcessRenewDeferDelegator {
    private final ProcessClient processClient;
    private final MohProcessService mohProcessService;

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        DualDocSortingService.clearDualDocSortingInfo(request);
        request.getSession().removeAttribute(KEY_MOH_PROCESS_DTO);
        request.getSession().removeAttribute(KEY_INTERNAL_DOC_BSB_ASSESSMENT_AND_RECOMMENDATION);
        MaskHelper.taskProcessUnmask(request, PARAM_NAME_APP_ID, PARAM_NAME_TASK_ID);
        AuditTrailHelper.auditFunction(MODULE_FACILITY_REGISTRATION, FUNCTION_AO_APPROVAL);
    }

    public void prepareData(BaseProcessClass bpc) {
        mohProcessService.prepareData(bpc, FUNCTION_AO_APPROVAL);
    }

    public void prepareSwitch(BaseProcessClass bpc) { mohProcessService.prepareSwitch(bpc, FUNCTION_AO_APPROVAL); }

    public void process(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String taskId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_TASK_ID);
        String appId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_APP_ID);
        MohProcessDto mohProcessDto = (MohProcessDto) ParamUtil.getSessionAttr(request, KEY_MOH_PROCESS_DTO);
        String processingDecision = mohProcessDto.getProcessingDecision();
        switch (processingDecision) {
            case MOH_PROCESS_DECISION_APPROVE_APPLICATION:
                processClient.saveAOProcessRenewDeferApprove(appId, taskId, mohProcessDto);
                ParamUtil.setRequestAttr(request, ModuleCommonConstants.RESULT_MSG, "BISACKNEW006");
                break;
            case MOH_PROCESS_DECISION_REJECT_APPLICATION:
                processClient.saveAOProcessRenewDeferReject(appId, taskId, mohProcessDto);
                ParamUtil.setRequestAttr(request, ModuleCommonConstants.RESULT_MSG, "BISACKNEW005");
                break;
            case MOH_PROCESS_DECISION_ROUTE_BACK_TO_DO:
                processClient.saveAOProcessRenewDeferRouteBackToDo(appId, taskId, mohProcessDto);
                ParamUtil.setRequestAttr(request, ModuleCommonConstants.RESULT_MSG, "BISACKNEW007");
                break;
            case MOH_PROCESS_DECISION_ROUTE_TO_HM:
                processClient.saveAOProcessRenewDeferRouteToHm(appId, taskId, mohProcessDto);
                ParamUtil.setRequestAttr(request, ModuleCommonConstants.RESULT_MSG, "BISACKNEW008");
                break;
            default:
                log.info("don't have such processingDecision {}", StringUtils.normalizeSpace(processingDecision));
                break;
        }
    }
}