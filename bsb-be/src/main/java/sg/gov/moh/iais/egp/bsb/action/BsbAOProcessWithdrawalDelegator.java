package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.client.WithdrawnClient;
import sg.gov.moh.iais.egp.bsb.dto.withdrawn.AppSubmitWithdrawnDto;
import sg.gov.moh.iais.egp.bsb.service.ProcessWithdrawalService;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_WITHDRAWN_APPLICATION;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_AO_PROCESSING;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ROUTING_HISTORY_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_TASK_ID;
import static sg.gov.moh.iais.egp.bsb.service.ProcessWithdrawalService.WITHDRAWN_APP_DTO;

/**
 * @author : tangtang
 */
@Slf4j
@Delegator("aoProcessWithdrawalDelegator")
public class BsbAOProcessWithdrawalDelegator {
    private final WithdrawnClient withdrawnClient;
    private final ProcessWithdrawalService processWithdrawalService;

    public BsbAOProcessWithdrawalDelegator(WithdrawnClient withdrawnClient, ProcessWithdrawalService processWithdrawalService) {
        this.withdrawnClient = withdrawnClient;
        this.processWithdrawalService = processWithdrawalService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(WITHDRAWN_APP_DTO);
        request.getSession().removeAttribute(KEY_ROUTING_HISTORY_LIST);
        MaskHelper.taskProcessUnmask(request, PARAM_NAME_APP_ID, PARAM_NAME_TASK_ID);
        AuditTrailHelper.auditFunction(MODULE_WITHDRAWN_APPLICATION, FUNCTION_AO_PROCESSING);
    }

    public void prepareData(BaseProcessClass bpc) {
        processWithdrawalService.prepareData(bpc);
    }

    public void aoValidate(BaseProcessClass bpc) {
        //validate approval officer submitted data
        HttpServletRequest request = bpc.request;
        AppSubmitWithdrawnDto dto = (AppSubmitWithdrawnDto) ParamUtil.getSessionAttr(request, WITHDRAWN_APP_DTO);
        dto.reqObjMapping(request);
        dto.setModule("aoProcessWithdrawn");
        processWithdrawalService.validateData(dto, request);
        ParamUtil.setSessionAttr(request, WITHDRAWN_APP_DTO, dto);
    }

    public void aoSave(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        processWithdrawalService.setProcessDto(request);
        AppSubmitWithdrawnDto dto = (AppSubmitWithdrawnDto) ParamUtil.getSessionAttr(request, WITHDRAWN_APP_DTO);
        withdrawnClient.aoProcessWithdrawnApp(dto);
    }


}
