package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistSectionDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.constant.ChecklistConstants;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.chklst.ChklstItemAnswerDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.InspectionChecklistDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.InspectionOutcomeDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.SelfAssessmtChklDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsFindingFormDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsInfoDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsProcessDto;
import sg.gov.moh.iais.egp.bsb.util.ChecklistHelper;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_VALID;


@Slf4j
@Delegator("bsbInspectionDO")
public class InspectionDODelegator {
    private final InspectionClient inspectionClient;

    @Autowired
    public InspectionDODelegator(InspectionClient inspectionClient) {
        this.inspectionClient = inspectionClient;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MaskHelper.taskProcessUnmask(request, KEY_APP_ID, KEY_TASK_ID);

        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_INSPECTION, AuditTrailConsts.FUNCTION_INSPECTION_CHECKLIST);
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        session.removeAttribute(KEY_INS_INFO);
        session.removeAttribute(KEY_INS_DECISION);
        session.removeAttribute(KEY_INS_FINDING);
        session.removeAttribute(KEY_INS_OUTCOME);
        session.removeAttribute(KEY_INS_CHK_LST);
        session.removeAttribute(KEY_SELF_ASSESSMENT_ANSWER_MAP);
        session.removeAttribute(KEY_INS_CHKL_ANSWER_MAP);
        session.removeAttribute(KEY_INSPECTION_CONFIG);
        session.removeAttribute(KEY_CHKL_ITEM_SELECTION);

        /* Retrieve config for inspection findings.
         * The config ID in inspection findings and inspection checklist are the same.
         * We need to try to find the config ID in above two places, once find one of them, we use this config ID
         * consistently in any other places. If not found, we need to retrieve from the config by type, module etc.
         * from chkl config of HCSA.  */
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        InsFindingFormDto findingFormDto = inspectionClient.getInsFindings(appId);
        InspectionChecklistDto checklistDto = inspectionClient.getSavedInspectionChecklist(appId);
        if (checklistDto == null) {
            checklistDto = new InspectionChecklistDto();
            checklistDto.setApplicationId(appId);
            checklistDto.setVersion(1);
            checklistDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        }
        String configId = null;
        ChecklistConfigDto configDto;
        if (StringUtils.hasLength(checklistDto.getChkLstConfigId())) {
            configId = checklistDto.getChkLstConfigId();
            findingFormDto.setConfigId(checklistDto.getChkLstConfigId());
        } else if (StringUtils.hasLength(findingFormDto.getConfigId())) {
            configId = findingFormDto.getConfigId();
            checklistDto.setChkLstConfigId(findingFormDto.getConfigId());
        }
        if (configId != null) {
            configDto = inspectionClient.getChecklistConfigById(configId);
        } else {
            /* If we don't find the existing checklist or findings,
             * we get the config by HCSA chkl config module.
             * The configDto is asserted not to be null, if not found, an exception will be thrown. */
            configDto = inspectionClient.getMaxVersionChecklistConfig(appId, HcsaChecklistConstants.INSPECTION);
            findingFormDto.setConfigId(configDto.getId());
            checklistDto.setChkLstConfigId(configDto.getId());
        }
        ParamUtil.setSessionAttr(request, KEY_INSPECTION_CONFIG, configDto);
        ParamUtil.setSessionAttr(request, KEY_INS_FINDING, findingFormDto);
        ParamUtil.setSessionAttr(request, KEY_INS_CHK_LST, checklistDto);


        // generate and set checklist item selection into session
        setFindingItemSelectionString(request);
    }

    private void setFindingItemSelectionString(HttpServletRequest request) {
        ChecklistConfigDto configDto = (ChecklistConfigDto) ParamUtil.getSessionAttr(request, KEY_INSPECTION_CONFIG);
        StringBuilder itemSelectionBuilder = new StringBuilder();
        StringBuilder itemNiceSelectBuilder = new StringBuilder();
        /* The {REPLACE} will be replaced into separator and index for usage */

        boolean waitingFirstItem = true;
        itemSelectionBuilder.append("<select name=\"").append(KEY_FINDING_ITEM).append("{REPLACE}\" style=\"display:none\" aria-label=\"finding item\">");
        itemNiceSelectBuilder.append("<div class=\"nice-select\" tabindex=\"0\">").append("<span class=\"current\">");
        for (ChecklistSectionDto sectionDto : configDto.getSectionDtos()) {
            for (ChecklistItemDto itemDto : sectionDto.getChecklistItemDtos()) {
                String itemKey = sectionDto.getId() + "--v--" + itemDto.getItemId();
                itemSelectionBuilder.append("<option value=\"").append(itemKey).append("\">")
                        .append(itemDto.getChecklistItem()).append("</option>");
                if (waitingFirstItem) {
                    itemNiceSelectBuilder.append(itemDto.getChecklistItem()).append("</span><ul class=\"list\">");
                    waitingFirstItem = false;
                }
                itemNiceSelectBuilder.append("<li class=\"option\" data-value=\"").append(itemKey).append("\">")
                        .append(itemDto.getChecklistItem()).append("</li>");
            }
        }
        itemSelectionBuilder.append("</select>");
        itemNiceSelectBuilder.append("</ul></div>");
        // escape is needed, we don't use c:out in JSP
        String itemSelection = StringEscapeUtils.escapeHtml(itemSelectionBuilder.toString() + itemNiceSelectBuilder.toString());
        ParamUtil.setSessionAttr(request, KEY_CHKL_ITEM_SELECTION, itemSelection);
    }

    public void pre(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        // facility info and inspection info
        InsInfoDto infoDto = (InsInfoDto) ParamUtil.getSessionAttr(request, KEY_INS_INFO);
        if (infoDto == null) {
            infoDto = inspectionClient.getInsInfo(appId);
            ParamUtil.setSessionAttr(request, KEY_INS_INFO, infoDto);
        }
        // inspection outcome
        InspectionOutcomeDto outcomeDto = (InspectionOutcomeDto) ParamUtil.getSessionAttr(request, KEY_INS_OUTCOME);
        if (outcomeDto == null) {
            outcomeDto = inspectionClient.getInsOutcome(appId);
            outcomeDto.setApplicationId(appId);
            ParamUtil.setSessionAttr(request, KEY_INS_OUTCOME, outcomeDto);
        }
        // processing info
        InsProcessDto processDto = (InsProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        if (processDto == null) {
            processDto = new InsProcessDto();
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

        ParamUtil.setRequestAttr(request, KEY_CHKL_CONFIG, configDto);
        ParamUtil.setRequestAttr(request, KEY_ANSWER_MAP, answerMap);
        ParamUtil.setRequestAttr(request, KEY_EDITABLE, Boolean.FALSE);
    }

    public void preChecklist(BaseProcessClass bpc) throws JsonProcessingException {
        HttpServletRequest request = bpc.request;
        ChecklistConfigDto configDto = (ChecklistConfigDto) ParamUtil.getSessionAttr(request, KEY_INSPECTION_CONFIG);

        Object answerMapObj = ParamUtil.getSessionAttr(request, KEY_INS_CHKL_ANSWER_MAP);
        Map<String, String> answerMap;
        if (answerMapObj == null) {
            InspectionChecklistDto checklistDto = (InspectionChecklistDto) ParamUtil.getSessionAttr(request, KEY_INS_CHK_LST);
            if (StringUtils.hasLength(checklistDto.getAnswer())) {
                ObjectMapper mapper = new ObjectMapper();
                List<ChklstItemAnswerDto> answerDtoList = mapper.readValue(checklistDto.getAnswer(), new TypeReference<List<ChklstItemAnswerDto>>() {});
                answerMap = Maps.newHashMapWithExpectedSize(answerDtoList.size());
                for (ChklstItemAnswerDto answerDto : answerDtoList) {
                    answerMap.put(answerDto.getSectionId() + KEY_SEPARATOR + answerDto.getItemId(), answerDto.getAnswer());
                }
            } else {
                // compute item amount, use it to determine the map capacity

                int amt = 0;
                for(ChecklistSectionDto sectionDto: configDto.getSectionDtos()) {
                    amt = amt + sectionDto.getChecklistItemDtos().size();
                }
                answerMap = Maps.newHashMapWithExpectedSize(amt);
            }
            ParamUtil.setSessionAttr(request, KEY_INS_CHKL_ANSWER_MAP, (HashMap<String, String>) answerMap);
        } else {
            answerMap = (Map<String, String>) answerMapObj;
        }

        // set DTOs needed for checklist
        ParamUtil.setRequestAttr(request, KEY_CHKL_CONFIG, configDto);
        ParamUtil.setRequestAttr(request, KEY_ANSWER_MAP, answerMap);
        ParamUtil.setRequestAttr(request, KEY_EDITABLE, Boolean.TRUE);
    }

    public void clearData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        Map<String, String> answerMap = (Map<String, String>) ParamUtil.getSessionAttr(request, KEY_INS_CHKL_ANSWER_MAP);
        answerMap.clear();
        ParamUtil.setSessionAttr(request, KEY_INS_CHKL_ANSWER_MAP, (HashMap<String, String>) answerMap);
    }

    public void validateAndSaveChecklist(BaseProcessClass bpc) throws JsonProcessingException {
        HttpServletRequest request = bpc.request;
        ChecklistConfigDto configDto = (ChecklistConfigDto) ParamUtil.getSessionAttr(request, KEY_INSPECTION_CONFIG);
        Map<String, String> answerMap = (Map<String, String>) ParamUtil.getSessionAttr(request, KEY_INS_CHKL_ANSWER_MAP);
        boolean allFilled = true;
        for (ChecklistSectionDto sectionDto : configDto.getSectionDtos()) {
            for (ChecklistItemDto itemDto : sectionDto.getChecklistItemDtos()) {
                String key = sectionDto.getId() + KEY_SEPARATOR + itemDto.getItemId();
                String value = ParamUtil.getString(request, key);
                if (ChecklistConstants.VALID_ANSWERS.contains(value)) {
                    answerMap.put(key, value);
                } else {
                    allFilled = false;
                }
            }
        }
        ParamUtil.setSessionAttr(request, KEY_INS_CHKL_ANSWER_MAP, (HashMap<String, String>) answerMap);
        ParamUtil.setRequestAttr(request, KEY_VALID, allFilled);

        if (!allFilled) {
            ParamUtil.setRequestAttr(request, KEY_ERROR_MESSAGE, "Please complete the checklist questions.");
        } else {
            // save checklist answer into DB, change app status to pending readiness
            List<ChklstItemAnswerDto> answerDtoList = new ArrayList<>(answerMap.size());
            for (Map.Entry<String, String> entry : answerMap.entrySet()) {
                String[] keyParts = entry.getKey().split(KEY_SEPARATOR);
                ChklstItemAnswerDto a = new ChklstItemAnswerDto(keyParts[0], keyParts[1], entry.getValue());
                answerDtoList.add(a);
            }
            ObjectMapper mapper = new ObjectMapper();
            String answer = mapper.writeValueAsString(answerDtoList);
            InspectionChecklistDto checklistDto = (InspectionChecklistDto) ParamUtil.getSessionAttr(request, KEY_INS_CHK_LST);
            checklistDto.setAnswer(answer);
            ParamUtil.setSessionAttr(request, KEY_INS_CHK_LST, checklistDto);
            inspectionClient.submitInspectionChecklist(checklistDto);
            ParamUtil.setRequestAttr(bpc.request, "ackMsg", MessageUtil.getMessageDesc("GENERAL_ERR0038"));
        }
    }

    public void saveInsFinding(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ChecklistConfigDto configDto = (ChecklistConfigDto) ParamUtil.getSessionAttr(request, KEY_INSPECTION_CONFIG);
        InsFindingFormDto findingFormDto = (InsFindingFormDto) ParamUtil.getSessionAttr(request, KEY_INS_FINDING);
        int existingItemMaxIdx = findingFormDto.getItemDtoList().size() - 1;

        String idxes = ParamUtil.getString(request, "sectionIdx");
        String[] idxArr = idxes.trim().split(" +");
        ArrayList<InsFindingFormDto.InsFindingItemDto> newFindingItemList = new ArrayList<>(idxArr.length);
        for (String idx : idxArr) {
            /* We first try to retrieve the item from the existing list.
             * This is because, if the item is an existing one, the client won't submit the itemKey, we need to retrieve
             * it from the existing data list. */
            int index = Integer.parseInt(idx);
            InsFindingFormDto.InsFindingItemDto findingItemDto;
            if (index > existingItemMaxIdx) {
                findingItemDto = new InsFindingFormDto.InsFindingItemDto();
            } else {
                findingItemDto = findingFormDto.getItemDtoList().get(index);
            }

            /* We try to get the itemKey, if we get it, we treat is as a new added finding, even the item is retrieved \
             * from existing data.
             * This is because, user can delete all existing items. This action lead the index to be computed starts
             * from 0 again. So even the index exists in the old list, it's not always mean the old data, it can be a
             * new data also.
             * So we always check if the itemKey exists or not and treat the item new added if the value is found.
             */
            String itemKey = ParamUtil.getString(request, KEY_FINDING_ITEM + KEY_SECTION_SEPARATOR + idx);
            if (StringUtils.hasLength(itemKey)) {
                // new finding have the item key, we need to get the key and value from the config
                findingItemDto.setItemValue(itemKey);
                ChecklistItemDto itemDto = ChecklistHelper.findItemByIdPath(configDto, itemKey);
                findingItemDto.setItemText(itemDto.getChecklistItem());
            }


            findingItemDto.setFindingType(ParamUtil.getString(request, KEY_FINDING_TYPE + KEY_SECTION_SEPARATOR + idx));
            findingItemDto.setRemarks(ParamUtil.getString(request, KEY_FINDING_REMARK + KEY_SECTION_SEPARATOR + idx));
            findingItemDto.setDeadline(ParamUtil.getString(request, KEY_FINDING_DEADLINE + KEY_SECTION_SEPARATOR + idx));

            newFindingItemList.add(findingItemDto);
        }

        findingFormDto.setItemDtoList(newFindingItemList);

        // save inspection findings
        ValidationResultDto validationResultDto = inspectionClient.saveInspectionFindings(findingFormDto);
        if (!validationResultDto.isPass()) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
        }
        ParamUtil.setSessionAttr(request, KEY_INS_FINDING, findingFormDto);
        ParamUtil.setRequestAttr(request, TAB_ACTIVE, TAB_INS_FINDING);
    }

    public void saveInsOutcome(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InspectionOutcomeDto outcomeDto = (InspectionOutcomeDto) ParamUtil.getSessionAttr(request, KEY_INS_OUTCOME);
        outcomeDto.setDeficiency(ParamUtil.getString(request, KEY_OUTCOME_DEFICIENCY));
        outcomeDto.setFollowUpRequired(ParamUtil.getString(request, KEY_OUTCOME_FOLLOWUP));
        outcomeDto.setOutcome(ParamUtil.getString(request, KEY_OUTCOME_OUTCOME));
        outcomeDto.setRemarks(ParamUtil.getString(request, KEY_OUTCOME_REMARKS));

        ValidationResultDto validationResultDto = inspectionClient.saveInspectionOutcome(outcomeDto);
        if (!validationResultDto.isPass()) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
        }
        ParamUtil.setSessionAttr(request, KEY_INS_OUTCOME, outcomeDto);
        ParamUtil.setRequestAttr(request, TAB_ACTIVE, TAB_INS_FINDING);
    }

    public void handleSubmit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        InsProcessDto processDto = (InsProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        processDto.reqObjMapping(request);
        ParamUtil.setSessionAttr(request, KEY_INS_DECISION, processDto);
        ValidationResultDto validationResultDto = inspectionClient.validateActualInspectionDODecision(processDto, appId);
        String validateResult;
        if (validationResultDto.isPass()) {
            if (DECISION_SUBMIT_AO.equals(processDto.getDecision())) {
                validateResult = "ao";
            } else if (DECISION_ROUTE_APPLICANT.equals(processDto.getDecision())) {
                validateResult = "applicant";
            } else if (DECISION_MARK_FINAL.equals(processDto.getDecision())) {
                validateResult = "final";
            } else {
                validateResult = "invalid";
            }
        } else {
            log.error("Validation failure info: {}", validationResultDto.toErrorMsg());
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            ParamUtil.setRequestAttr(request, TAB_ACTIVE, TAB_PROCESSING);
            validateResult = "back";
        }
        ParamUtil.setRequestAttr(request, KEY_VALID, validateResult);
    }

    public void submitToAo(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsProcessDto processDto = (InsProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        inspectionClient.submitInspectionReportToAO(appId, taskId, processDto);
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, "You have successfully completed your task");
    }

    public void routeToApplicant(BaseProcessClass bpc) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void markFinal(BaseProcessClass bpc) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
