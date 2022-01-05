package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.SuspensionClient;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.suspension.SuspensionReinstatementDto;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.KEY_TASK_ID;
import static sg.gov.moh.iais.egp.bsb.constant.RevocationConstants.*;

/**
 * @author : tangtang
 */
@Slf4j
@Delegator("bsbSuspensionDelegator")
public class BsbSuspensionDelegator {
    private static final String MODULE_NAME = "Suspension";
    private static final String ACTION_TYPE_SAVE = "doSave";
    private static final String ACTION_TYPE_PREPARE = "prepare";
    private static final String ACTION_TYPE_NEXT = "doNext";
    private static final String ACTION_TYPE = "action_type";
    private static final String SUSPENSION_REINSTATEMENT_DTO = "suspensionReinstatementDto";
    private static final String ACK_MSG = "ackMsg";
    private static final String DO_SUSPEND_ACK_MSG = "You have successfully submitted a Suspension Task.";
    private static final String AO_HM_SUSPEND_ACK_MSG = "You have successfully approved the application.";

    private final SuspensionClient suspensionClient;

    public BsbSuspensionDelegator(SuspensionClient suspensionClient) {
        this.suspensionClient = suspensionClient;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction(MODULE_NAME, MODULE_NAME);
        ParamUtil.setSessionAttr(request, SUSPENSION_REINSTATEMENT_DTO, null);
        ParamUtil.setSessionAttr(request,BACK,null);
    }

    /**
     * Duty officer submit suspension request
     */
    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String from = ParamUtil.getRequestString(request, FROM);
        ParamUtil.setSessionAttr(request,FROM,null);
        SuspensionReinstatementDto suspensionReinstatementDto = getSuspensionDto(request);
        if (StringUtils.isEmpty(suspensionReinstatementDto.getApprovalId()) && StringUtils.isEmpty(suspensionReinstatementDto.getApplicationId())) {
            if (from.equals(FAC)) {
                String approvalId = ParamUtil.getRequestString(request, PARAM_APPROVAL_ID);
                approvalId = MaskUtil.unMaskValue("id", approvalId);
                suspensionReinstatementDto = suspensionClient.getSuspensionDataByApprovalId(approvalId).getEntity();
            }
            if (from.equals(APP)) {
                String maskedAppId = ParamUtil.getString(request, KEY_APP_ID);
                String maskedTaskId = ParamUtil.getString(request, KEY_TASK_ID);
                String appId = MaskUtil.unMaskValue("id", maskedAppId);
                String taskId = MaskUtil.unMaskValue("id", maskedTaskId);
                if (appId == null || appId.equals(maskedAppId)) {
                    throw new IaisRuntimeException("Invalid masked application ID");
                }
                if (taskId == null || taskId.equals(maskedTaskId)) {
                    throw new IaisRuntimeException("Invalid masked task ID");
                }
                suspensionReinstatementDto = suspensionClient.getSuspensionDataByApplicationId(appId).getEntity();
                suspensionReinstatementDto.setTaskId(taskId);
            }
        }
        ParamUtil.setSessionAttr(request,BACK,from);
        ParamUtil.setSessionAttr(request, SUSPENSION_REINSTATEMENT_DTO, suspensionReinstatementDto);
    }

    public void doSuspensionValidate(BaseProcessClass bpc) {
        //validate duty officer submitted data
        HttpServletRequest request = bpc.request;
        SuspensionReinstatementDto dto = getSuspensionDto(request);
        dto.getDOSuspensionData(request);
        //validation
        String actionType = "";
        ValidationResultDto validationResultDto = suspensionClient.validateSuspensionDto(dto);
        if (!validationResultDto.isPass()){
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            actionType = ACTION_TYPE_PREPARE;
        }else {
            actionType = ACTION_TYPE_NEXT;
        }
        ParamUtil.setRequestAttr(request, ACTION_TYPE, actionType);
        ParamUtil.setSessionAttr(request, SUSPENSION_REINSTATEMENT_DTO, dto);
    }

    public void doSuspension(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SuspensionReinstatementDto dto = getSuspensionDto(request);
        suspensionClient.doSuspension(dto);
        ParamUtil.setRequestAttr(request, ACK_MSG, DO_SUSPEND_ACK_MSG);
    }

    public void aoSuspensionValidate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SuspensionReinstatementDto dto = getSuspensionDto(request);
        dto.getAOSuspensionData(request);
        validateData(dto,request);
        ParamUtil.setSessionAttr(request, SUSPENSION_REINSTATEMENT_DTO, dto);
    }

    public void aoSuspension(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SuspensionReinstatementDto dto = getSuspensionDto(request);
        suspensionClient.aoSuspension(dto);
        ParamUtil.setRequestAttr(request, ACK_MSG, AO_HM_SUSPEND_ACK_MSG);
    }

    public void hmSuspensionValidate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SuspensionReinstatementDto dto = getSuspensionDto(request);
        dto.getHMSuspensionData(request);
        validateData(dto,request);
        ParamUtil.setSessionAttr(request, SUSPENSION_REINSTATEMENT_DTO, dto);
    }

    public void hmSuspension(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SuspensionReinstatementDto dto = getSuspensionDto(request);
        suspensionClient.hmSuspension(dto);
        ParamUtil.setRequestAttr(request, ACK_MSG, AO_HM_SUSPEND_ACK_MSG);
    }

    /************************************** Reinstatement ***********************************************/
    public void doReinstatementValidate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SuspensionReinstatementDto dto = getSuspensionDto(request);
        dto.getDOReinstatementData(request);
        validateData(dto,request);
        ParamUtil.setSessionAttr(request, SUSPENSION_REINSTATEMENT_DTO, dto);
    }

    public void doReinstatement(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SuspensionReinstatementDto dto = getSuspensionDto(request);
        suspensionClient.doReinstatement(dto);
        ParamUtil.setRequestAttr(request, ACK_MSG, DO_SUSPEND_ACK_MSG);
    }

    public void aoReinstatementValidate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SuspensionReinstatementDto dto = getSuspensionDto(request);
        dto.getAOReinstatementData(request);
        validateData(dto,request);
        ParamUtil.setSessionAttr(request, SUSPENSION_REINSTATEMENT_DTO, dto);
    }

    public void aoReinstatement(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SuspensionReinstatementDto dto = getSuspensionDto(request);
        suspensionClient.aoReinstatement(dto);
        ParamUtil.setRequestAttr(request, ACK_MSG, AO_HM_SUSPEND_ACK_MSG);
    }

    public void hmReinstatementValidate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SuspensionReinstatementDto dto = getSuspensionDto(request);
        dto.getHMReinstatementData(request);
        validateData(dto,request);
        ParamUtil.setSessionAttr(request, SUSPENSION_REINSTATEMENT_DTO, dto);
    }

    public void hmReinstatement(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SuspensionReinstatementDto dto = getSuspensionDto(request);
        suspensionClient.hmReinstatement(dto);
        ParamUtil.setRequestAttr(request, ACK_MSG, AO_HM_SUSPEND_ACK_MSG);
    }

    /*********************************** Common ************************************************/
    private SuspensionReinstatementDto getSuspensionDto(HttpServletRequest request) {
        SuspensionReinstatementDto auditDto = (SuspensionReinstatementDto) ParamUtil.getSessionAttr(request, SUSPENSION_REINSTATEMENT_DTO);
        return auditDto == null ? getDefaultSuspensionDto() : auditDto;
    }

    private SuspensionReinstatementDto getDefaultSuspensionDto() {
        return new SuspensionReinstatementDto();
    }

    private void validateData(SuspensionReinstatementDto dto, HttpServletRequest request){
        //validation
        String actionType = "";
        ValidationResultDto validationResultDto = suspensionClient.validateSuspensionDto(dto);
        if (!validationResultDto.isPass()){
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            actionType = ACTION_TYPE_PREPARE;
        }else {
            actionType = ACTION_TYPE_SAVE;
        }
        ParamUtil.setRequestAttr(request, ACTION_TYPE, actionType);
    }
}
