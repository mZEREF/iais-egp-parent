package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.ProcessClient;
import sg.gov.moh.iais.egp.bsb.client.RfiClient;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.dto.process.MohProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.PageAppEditSelectDto;
import sg.gov.moh.iais.egp.bsb.service.DualDocSortingService;
import sg.gov.moh.iais.egp.bsb.service.MohProcessService;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_DO_SCREENING;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_FACILITY_REGISTRATION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.MOH_PROCESS_DECISION_INSPECTION_AND_CERTIFICATION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.MOH_PROCESS_DECISION_REJECT;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.MOH_PROCESS_DECISION_SCREENED_BY_DO_PROCEED_TO_NEXT_STAGE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.KEY_MOH_PROCESS_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_PAGE_APP_EDIT_SELECT_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_TASK_ID;


@Slf4j
@RequiredArgsConstructor
@Delegator(value = "doScreeningDelegator")
public class MohDOScreeningDelegator {
    private final ProcessClient processClient;
    private final MohProcessService mohProcessService;
    private final RfiClient rfiClient;

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        DualDocSortingService.clearDualDocSortingInfo(session);
        session.removeAttribute(KEY_MOH_PROCESS_DTO);
        // if can select RFI section, need clear this, set in 'BeViewApplicationDelegator'
        session.removeAttribute(KEY_PAGE_APP_EDIT_SELECT_DTO);
        MaskHelper.taskProcessUnmask(request, PARAM_NAME_APP_ID, PARAM_NAME_TASK_ID);
        AuditTrailHelper.auditFunction(MODULE_FACILITY_REGISTRATION, FUNCTION_DO_SCREENING);
    }

    public void prepareData(BaseProcessClass bpc) {
        mohProcessService.prepareData(bpc, FUNCTION_DO_SCREENING);
    }

    public void prepareSwitch(BaseProcessClass bpc) {
        mohProcessService.prepareSwitch(bpc, FUNCTION_DO_SCREENING);
    }

    public void process(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String taskId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_TASK_ID);
        String appId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_APP_ID);
        MohProcessDto mohProcessDto = (MohProcessDto) ParamUtil.getSessionAttr(request, KEY_MOH_PROCESS_DTO);
        String processingDecision = mohProcessDto.getProcessingDecision();
        switch (processingDecision) {
            case MOH_PROCESS_DECISION_INSPECTION_AND_CERTIFICATION:
            case MOH_PROCESS_DECISION_SCREENED_BY_DO_PROCEED_TO_NEXT_STAGE:
                processClient.saveDOScreeningScreenedByDO(appId, taskId, mohProcessDto);
                ParamUtil.setRequestAttr(request, ModuleCommonConstants.RESULT_MSG, "BISACKNEW009");
                break;
            case MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION:
                mohProcessDto.setPageAppEditSelectDto((PageAppEditSelectDto) ParamUtil.getSessionAttr(request, KEY_PAGE_APP_EDIT_SELECT_DTO));
                rfiClient.saveDOScreeningRfi(mohProcessDto, appId, taskId);
                ParamUtil.setRequestAttr(request, ModuleCommonConstants.RESULT_MSG, "BISACKNEW004");
                break;
            case MOH_PROCESS_DECISION_REJECT:
                processClient.saveDOScreeningReject(appId, taskId, mohProcessDto);
                ParamUtil.setRequestAttr(request, ModuleCommonConstants.RESULT_MSG, "BISACKNEW005");
                break;
            default:
                log.info("don't have such processingDecision {}", StringUtils.normalizeSpace(processingDecision));
                break;
        }
    }
}