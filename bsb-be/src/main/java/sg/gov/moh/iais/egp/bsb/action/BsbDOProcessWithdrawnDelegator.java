package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.client.WithdrawnClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.dto.withdrawn.AppSubmitWithdrawnDto;
import sg.gov.moh.iais.egp.bsb.service.DualDocSortingService;
import sg.gov.moh.iais.egp.bsb.service.ProcessWithdrawalService;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_DO_VERIFICATION;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_WITHDRAWN_APPLICATION;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ROUTING_HISTORY_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_TASK_ID;
import static sg.gov.moh.iais.egp.bsb.service.ProcessWithdrawalService.WITHDRAWN_APP_DTO;

/**
 * @author : tangtang
 */
@Slf4j
@Delegator("doProcessWithdrawalDelegator")
@RequiredArgsConstructor
public class BsbDOProcessWithdrawnDelegator {
    private final WithdrawnClient withdrawnClient;
    private final ProcessWithdrawalService processWithdrawalService;


    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction(MODULE_WITHDRAWN_APPLICATION, FUNCTION_DO_VERIFICATION);
        DualDocSortingService.clearDualDocSortingInfo(request);
        request.getSession().removeAttribute(WITHDRAWN_APP_DTO);
        request.getSession().removeAttribute(KEY_ROUTING_HISTORY_LIST);
        MaskHelper.taskProcessUnmask(request, PARAM_NAME_APP_ID, PARAM_NAME_TASK_ID);
    }

    public void prepareData(BaseProcessClass bpc) {
        processWithdrawalService.prepareData(bpc);
    }

    public void bindAction(BaseProcessClass bpc) {
        DualDocSortingService.readDualDocSortingInfo(bpc.request);
    }

    public void doValidate(BaseProcessClass bpc) {
        //validate duty officer submitted data
        HttpServletRequest request = bpc.request;
        AppSubmitWithdrawnDto dto = (AppSubmitWithdrawnDto) ParamUtil.getSessionAttr(request, WITHDRAWN_APP_DTO);
        dto.reqObjMapping(request);
        dto.setModule("doProcessWithdrawn");
        processWithdrawalService.validateData(dto, request);
        ParamUtil.setSessionAttr(request, WITHDRAWN_APP_DTO, dto);
    }

    public void doSave(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        processWithdrawalService.setProcessDto(request);
        AppSubmitWithdrawnDto dto = (AppSubmitWithdrawnDto) ParamUtil.getSessionAttr(request, WITHDRAWN_APP_DTO);
        String decision = dto.getDoDecision();
        if (MasterCodeConstants.MOH_PROCESS_DECISION_ACCEPT.equals(decision)) {
            withdrawnClient.doAcceptWithdrawal(dto);
            ParamUtil.setRequestAttr(request, ModuleCommonConstants.RESULT_MSG, "BISACKNEW006");
        } else if (MasterCodeConstants.MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION.equals(decision)) {
            withdrawnClient.saveWithdrawalRfi(dto);
            ParamUtil.setRequestAttr(request, ModuleCommonConstants.RESULT_MSG, "BISACKNEW004");
        } else if (MasterCodeConstants.MOH_PROCESS_DECISION_REJECT.equals(decision)) {
            withdrawnClient.doRejectWithdrawal(dto);
            ParamUtil.setRequestAttr(request, ModuleCommonConstants.RESULT_MSG, "BISACKNEW005");
        }
        ParamUtil.setRequestAttr(request,"resultMsg","ACKWDL002");
    }
}
