package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.ProcessClient;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants;
import sg.gov.moh.iais.egp.bsb.dto.process.MohProcessDto;
import sg.gov.moh.iais.egp.bsb.service.MohProcessService;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_HM_APPROVAL;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_FACILITY_REGISTRATION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_AO_APPROVAL;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.KEY_MOH_PROCESS_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_TASK_ID;


@Delegator(value = "hmProcessingDelegator")
@Slf4j
public class MohHMProcessingDelegator {
    private final ProcessClient processClient;
    private final MohProcessService mohProcessService;

    @Autowired
    public MohHMProcessingDelegator(ProcessClient processClient, MohProcessService mohProcessService) {
        this.processClient = processClient;
        this.mohProcessService = mohProcessService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_MOH_PROCESS_DTO);
        MaskHelper.taskProcessUnmask(request, PARAM_NAME_APP_ID, PARAM_NAME_TASK_ID);
        AuditTrailHelper.auditFunction(MODULE_FACILITY_REGISTRATION, FUNCTION_HM_APPROVAL);
    }

    public void prepareData(BaseProcessClass bpc) {
        mohProcessService.prepareData(bpc, FUNCTION_HM_APPROVAL);
    }

    public void prepareSwitch(BaseProcessClass bpc) {
        mohProcessService.prepareSwitch(bpc, FUNCTION_HM_APPROVAL);
    }

    public void process(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String taskId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_TASK_ID);
        String appId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_APP_ID);
        MohProcessDto mohProcessDto = (MohProcessDto) ParamUtil.getSessionAttr(request, KEY_MOH_PROCESS_DTO);
        processClient.saveHMProcessingApproveOrReject(appId, taskId, mohProcessDto);
        ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_CURRENT_TASK, FUNCTION_HM_APPROVAL);
        ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_NEXT_TASK, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_AO_APPROVAL));
        ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_NEXT_ROLE, ModuleCommonConstants.KEY_AO);
    }
}