package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.chklst.ChklstItemAnswerDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.SelfAssessmtChklDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsFacInfoDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.PreInsProcessDto;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.MASK_PARAM_ID;


@Slf4j
@Delegator("bsbPreInspection")
public class PreInspectionDelegator {
    private final InspectionClient inspectionClient;

    @Autowired
    public PreInspectionDelegator(InspectionClient inspectionClient) {
        this.inspectionClient = inspectionClient;
    }


    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String maskedAppId = request.getParameter(KEY_APP_ID);
        String appId = MaskUtil.unMaskValue(MASK_PARAM_ID, maskedAppId);
        if (!StringUtils.hasLength(appId) || appId.equals(maskedAppId)) {
            throw new IaisRuntimeException("Invalid maskedAppId:" + LogUtil.escapeCrlf(maskedAppId));
        }
        String maskedTaskId = request.getParameter(KEY_APP_ID);
        String taskId = MaskUtil.unMaskValue(MASK_PARAM_ID, maskedTaskId);
        if (!StringUtils.hasLength(taskId) || taskId.equals(maskedTaskId)) {
            throw new IaisRuntimeException("Invalid maskedTaskId:" + LogUtil.escapeCrlf(maskedTaskId));
        }

        ParamUtil.setSessionAttr(request, KEY_APP_ID, appId);
        ParamUtil.setSessionAttr(request, KEY_TASK_ID, appId);

        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_INSPECTION, AuditTrailConsts.FUNCTION_PRE_INSPECTION);
    }

    public void init(BaseProcessClass bpc) {
        HttpSession session = bpc.request.getSession();
        session.removeAttribute(KEY_INS_FAC_INFO);
        session.removeAttribute(KEY_INS_DECISION);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InsFacInfoDto facInfoDto = (InsFacInfoDto) ParamUtil.getSessionAttr(request, KEY_INS_FAC_INFO);
        if (facInfoDto == null) {
            String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
            facInfoDto = inspectionClient.getInsFacInfo(appId);
            ParamUtil.setSessionAttr(request, KEY_INS_FAC_INFO, facInfoDto);
        }
        PreInsProcessDto processDto = (PreInsProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        if (processDto == null) {
            processDto = new PreInsProcessDto();
            ParamUtil.setSessionAttr(request, KEY_INS_DECISION, processDto);
        }
    }

    public void bindAction(BaseProcessClass bpc) {
        // do nothing now
    }


    public void preSelfAssessment(BaseProcessClass bpc) throws JsonProcessingException {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        SelfAssessmtChklDto answerRecordDto = inspectionClient.getSavedSelfAssessment(appId);
        ChecklistConfigDto configDto = inspectionClient.getChecklistConfigById(answerRecordDto.getChkLstConfigId());

        String answerJson = answerRecordDto.getAnswer();
        ObjectMapper mapper = new ObjectMapper();
        List<ChklstItemAnswerDto> answerDtoList = mapper.readValue(answerJson, new TypeReference<List<ChklstItemAnswerDto>>() {});
        Map<String, String> answerMap = Maps.newHashMapWithExpectedSize(answerDtoList.size());
        for (ChklstItemAnswerDto answerDto : answerDtoList) {
            answerMap.put(answerDto.getSectionId() + KEY_SEPARATOR + answerDto.getItemId(), answerDto.getAnswer());
        }

        ParamUtil.setRequestAttr(request, KEY_SELF_ASSESSMENT_CONFIG, configDto);
        ParamUtil.setRequestAttr(request, KEY_SELF_ASSESSMENT_ANSWER_MAP, answerMap);
        ParamUtil.setRequestAttr(request, KEY_EDITABLE, Boolean.FALSE);
    }


    public void validateSubmission(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        PreInsProcessDto processDto = (PreInsProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        processDto.reqObjMapping(request);
        ParamUtil.setSessionAttr(request, KEY_INS_DECISION, processDto);
        ValidationResultDto validationResultDto = inspectionClient.validatePreInsSubmission(processDto);
        String validateResult;
        if (validationResultDto.isPass()) {
            if (DECISION_MARK_READY.equals(processDto.getDecision())) {
                validateResult = "ready";
            } else if (DECISION_RFI.equals(processDto.getDecision())) {
                validateResult = "rfi";
            } else {
                validateResult = "unknown";
            }
        } else {
            validateResult = "invalid";
        }
        ParamUtil.setRequestAttr(request, KEY_VALID, validateResult);
    }


    public void ready(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        PreInsProcessDto processDto = (PreInsProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        inspectionClient.changeInspectionStatusToReady(appId, taskId, processDto);
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, "You have successfully completed your task");
    }


    public void rfi(BaseProcessClass bpc) {
        // to be implemented in the future
    }
}
