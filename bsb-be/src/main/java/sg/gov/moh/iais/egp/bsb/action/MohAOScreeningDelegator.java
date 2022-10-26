package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.ProcessClient;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.dto.process.MohProcessDto;
import sg.gov.moh.iais.egp.bsb.service.DualDocSortingService;
import sg.gov.moh.iais.egp.bsb.service.MohProcessService;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_AO_SCREENING;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_FACILITY_REGISTRATION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_DO_RECOMMENDATION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_INSPECTION_CERTIFICATION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.MOH_PROCESS_DECISION_APPROVE_TO_PROCEED_TO_NEXT_STAGE;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.MOH_PROCESS_DECISION_REJECT_APPLICATION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_BACK_TO_DO;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_TO_HM;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_INTERNAL_DOC_BSB_ASSESSMENT_AND_RECOMMENDATION;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.KEY_MOH_PROCESS_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_TASK_ID;


@Slf4j
@RequiredArgsConstructor
@Delegator(value = "aoScreeningDelegator")
public class MohAOScreeningDelegator {
    private final ProcessClient processClient;
    private final MohProcessService mohProcessService;

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        DualDocSortingService.clearDualDocSortingInfo(request);
        request.getSession().removeAttribute(KEY_MOH_PROCESS_DTO);
        request.getSession().removeAttribute(KEY_INTERNAL_DOC_BSB_ASSESSMENT_AND_RECOMMENDATION);
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
        switch (processingDecision) {
            case MOH_PROCESS_DECISION_APPROVE_TO_PROCEED_TO_NEXT_STAGE:
                String nextAppStatus = processClient.saveAOScreeningApprove(appId, taskId, mohProcessDto);
                if (nextAppStatus.equals(APP_STATUS_PEND_INSPECTION_CERTIFICATION)) {
                    ParamUtil.setRequestAttr(request, ModuleCommonConstants.RESULT_MSG, "BISACKNEW003");
                } else if (nextAppStatus.equals(APP_STATUS_PEND_DO_RECOMMENDATION)) {
                    ParamUtil.setRequestAttr(request, ModuleCommonConstants.RESULT_MSG, "BISACKNEW006");
                }
                break;
            case MOH_PROCESS_DECISION_REJECT_APPLICATION:
                processClient.saveAOScreeningReject(appId, taskId, mohProcessDto);
                ParamUtil.setRequestAttr(request, ModuleCommonConstants.RESULT_MSG, "BISACKNEW005");
                break;
            case MOH_PROCESS_DECISION_ROUTE_BACK_TO_DO:
                processClient.saveAOScreeningRouteBackToDo(appId, taskId, mohProcessDto);
                ParamUtil.setRequestAttr(request, ModuleCommonConstants.RESULT_MSG, "BISACKNEW007");
                break;
            case MOH_PROCESS_DECISION_ROUTE_TO_HM:
                processClient.saveAOScreeningRouteToHm(appId, taskId, mohProcessDto);
                ParamUtil.setRequestAttr(request, ModuleCommonConstants.RESULT_MSG, "BISACKNEW008");
                break;
            default:
                log.info("don't have such processingDecision {}", StringUtils.normalizeSpace(processingDecision));
                break;
        }
    }
}