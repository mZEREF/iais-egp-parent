package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistSectionDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.constant.AssessmentState;
import sg.gov.moh.iais.egp.bsb.constant.ChecklistConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.chklst.ChklstItemAnswerDto;
import sg.gov.moh.iais.egp.bsb.dto.chklst.assessment.PreAssessmentDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.SelfAssessmtChklDto;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants.*;


@Slf4j
@Delegator("bsbSubmitSelfAssessment")
public class BsbSubmitSelfAssessmentDelegator {
    private final InspectionClient inspectionClient;

    @Autowired
    public BsbSubmitSelfAssessmentDelegator(InspectionClient inspectionClient) {
        this.inspectionClient = inspectionClient;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        // clear sessions
        HttpSession session = request.getSession();
        session.removeAttribute(KEY_ENTRY_APP_ID);
        session.removeAttribute(KEY_ACTIONS);
        session.removeAttribute(KEY_CURRENT_ACTION);
        session.removeAttribute(KEY_EDITABLE);
        session.removeAttribute(KEY_SELF_ASSESSMENT_CONFIG);
        session.removeAttribute(KEY_SELF_ASSESSMENT_CHK_LST);
        session.removeAttribute(KEY_SELF_ASSESSMENT_ANSWER_MAP);

        // get app ID from request parameter
        String maskedAppId = ParamUtil.getString(request, KEY_APP_ID);
        String appId = MaskUtil.unMaskValue(MASK_PARAM, maskedAppId);
        if (appId == null || appId.equals(maskedAppId)) {
            throw new IllegalArgumentException("Invalid masked app ID:" + LogUtil.escapeCrlf(maskedAppId));
        }
        /* Here we don't use the same key to edit the checklist latter.
         * We don't use this to display the checklist form directly.
         * Because we keep the chance in the future: we may use a appId to show a list of app's checklist to be filled,
         * and then select one of them to fill in according to each appId.
         * Currently, we only have one app's checklist to be filled, so this app will be the same as the app in next
         * page. */
        ParamUtil.setSessionAttr(request, KEY_ENTRY_APP_ID, appId);



        // set audit trail info (We can set appNo here, may be added in future)
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_MAIN_FUNCTION, AuditTrailConsts.FUNCTION_SELF_ASSESSMENT);
    }


    public void init(BaseProcessClass bpc) {
        // do nothing now
    }


    public void preLoad(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_ENTRY_APP_ID);
        // use appId to get facility and activity data, and assessment action state
        ResponseDto<PreAssessmentDto> responseDto = inspectionClient.getAssessmentState(appId);
        if (responseDto.ok()) {
            PreAssessmentDto dto = responseDto.getEntity();
            ArrayList<String> actions = new ArrayList<>(assessmentActions(dto.getAssessmentState()));
            ParamUtil.setRequestAttr(request, KEY_DATA_DTO, dto);
            ParamUtil.setSessionAttr(request, KEY_ACTIONS, actions);
        } else {
            log.error("Fail to get pre assessment info");
            throw new IaisRuntimeException(LogUtil.escapeCrlf(responseDto.getErrorDesc()));
        }
    }


    @SuppressWarnings("unchecked")
    public void bindAction(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String action = request.getParameter(KEY_ACTION_VALUE);
        /* We don't check allow actions for Print, because 'Print' will open a new window.
         * The old session attributes are lost (unavailable) in the new session.
         * It's unnecessary to check the print actually, if no assessment available, the page
         * answer will be empty. */
        if (!ACTION_PRINT.equals(action)) {
            List<String> allowActions = (List<String>) ParamUtil.getSessionAttr(request, KEY_ACTIONS);
            if (!allowActions.contains(action)) {
                throw new IaisRuntimeException("Invalid action:" + LogUtil.escapeCrlf(action));
            }
        }

        String maskedAppId = request.getParameter(KEY_ACTION_ADDITIONAL);
        String appId = MaskUtil.unMaskValue(MASK_PARAM, maskedAppId);
        if (appId == null || appId.equals(maskedAppId)) {
            throw new IllegalArgumentException("Invalid masked app ID:" + LogUtil.escapeCrlf(maskedAppId));
        }

        /* put the action into session, we use the saved 'View' action to determine not to save the data from request */
        ParamUtil.setSessionAttr(request, KEY_CURRENT_ACTION, action);
        ParamUtil.setRequestAttr(request, KEY_APP_ID, appId);

        if (ACTION_FILL.equals(action) || ACTION_EDIT.equals(action)) {
            ParamUtil.setSessionAttr(request, KEY_EDITABLE, Boolean.TRUE);
        } else {
            ParamUtil.setSessionAttr(request, KEY_EDITABLE, Boolean.FALSE);
        }
    }


    /**For edit(RFI), view and print situation, we need to load the existing data first at here.
     * We put the self-assessment data in session, so this method will be called once only.
     * Later steps route back to the self-assessment page, it will not retrieve existing data again, instead, it
     * retrieves data from session. */
    public void loadExistingData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String action = (String) ParamUtil.getSessionAttr(request, KEY_CURRENT_ACTION);
        if (ACTION_PRINT.equals(action) || ACTION_VIEW.equals(action) || ACTION_EDIT.equals(action)) {
            String appId = (String) ParamUtil.getRequestAttr(request, KEY_APP_ID);
            SelfAssessmtChklDto answerRecordDto = inspectionClient.getSavedSelfAssessment(appId);
            if (answerRecordDto != null) {
                ParamUtil.setSessionAttr(request, KEY_SELF_ASSESSMENT_CHK_LST, answerRecordDto);
            }
        }
    }

    public void prepareForSelfAssessment(BaseProcessClass bpc) throws JsonProcessingException {
        HttpServletRequest request = bpc.request;
        SelfAssessmtChklDto answerRecordDto = (SelfAssessmtChklDto) ParamUtil.getSessionAttr(request, KEY_SELF_ASSESSMENT_CHK_LST);
        if (answerRecordDto == null) {
            // first time to submit a self-assessment
            String appId = (String) ParamUtil.getRequestAttr(request, KEY_APP_ID);
            answerRecordDto = new SelfAssessmtChklDto();
            answerRecordDto.setApplicationId(appId);
            answerRecordDto.setVersion(1);
            answerRecordDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);

            ChecklistConfigDto configDto = inspectionClient.getMaxVersionChecklistConfig(appId, HcsaChecklistConstants.SELF_ASSESSMENT);
            answerRecordDto.setChkLstConfigId(configDto.getId());

            ParamUtil.setSessionAttr(request, KEY_SELF_ASSESSMENT_CONFIG, configDto);
            ParamUtil.setSessionAttr(request, KEY_SELF_ASSESSMENT_CHK_LST, answerRecordDto);
        }
        ChecklistConfigDto configDto = (ChecklistConfigDto) ParamUtil.getSessionAttr(request, KEY_SELF_ASSESSMENT_CONFIG);
        if (configDto == null) {
            String configId = answerRecordDto.getChkLstConfigId();
            configDto = inspectionClient.getChecklistConfigById(configId);
            ParamUtil.setSessionAttr(request, KEY_SELF_ASSESSMENT_CONFIG, configDto);
        }
        Map<String, String> answerMap = (Map<String, String>) ParamUtil.getSessionAttr(request, KEY_SELF_ASSESSMENT_ANSWER_MAP);
        if (answerMap == null) {
            if (StringUtils.hasLength(answerRecordDto.getAnswer())) {
                // convert saved JSON to answer map
                String answerJson = answerRecordDto.getAnswer();
                ObjectMapper mapper = new ObjectMapper();
                List<ChklstItemAnswerDto> answerDtoList = mapper.readValue(answerJson, new TypeReference<List<ChklstItemAnswerDto>>() {});
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
            ParamUtil.setSessionAttr(request, KEY_SELF_ASSESSMENT_ANSWER_MAP, (HashMap<String, String>) answerMap);
        }

        // set DTOs needed by checklist page
        ParamUtil.setRequestAttr(request, KEY_CHKL_CONFIG, configDto);
        ParamUtil.setRequestAttr(request, KEY_ANSWER_MAP, answerMap);
    }

    public void save(BaseProcessClass bpc) throws JsonProcessingException {
        HttpServletRequest request = bpc.request;
        String currentAction = (String) ParamUtil.getSessionAttr(request, KEY_CURRENT_ACTION);
        if (!ACTION_EDIT.equals(currentAction) && !ACTION_FILL.equals(currentAction)) {
            throw new IaisRuntimeException("Invalid action!");
        }
        /* For each sectionId--itemId, retrieve value, put into the answerMap.
         * Check if all questions are answered, if not, route back. */
        ChecklistConfigDto configDto = (ChecklistConfigDto) ParamUtil.getSessionAttr(request, KEY_SELF_ASSESSMENT_CONFIG);
        Map<String, String> answerMap = (Map<String, String>) ParamUtil.getSessionAttr(request, KEY_SELF_ASSESSMENT_ANSWER_MAP);
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
        ParamUtil.setSessionAttr(request, KEY_SELF_ASSESSMENT_ANSWER_MAP, (HashMap<String, String>) answerMap);
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
            SelfAssessmtChklDto answerRecordDto = (SelfAssessmtChklDto) ParamUtil.getSessionAttr(request, KEY_SELF_ASSESSMENT_CHK_LST);
            answerRecordDto.setAnswer(answer);
            ParamUtil.setSessionAttr(request, KEY_SELF_ASSESSMENT_CHK_LST, answerRecordDto);
            inspectionClient.submitSelfAssessment(answerRecordDto);
            ParamUtil.setRequestAttr(bpc.request, "ackMsg", MessageUtil.getMessageDesc("GENERAL_ERR0038"));
        }
    }


    @SuppressWarnings("unchecked")
    public void clearData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        Map<String, String> answerMap = (Map<String, String>) ParamUtil.getSessionAttr(request, KEY_SELF_ASSESSMENT_ANSWER_MAP);
        answerMap.clear();
        ParamUtil.setSessionAttr(request, KEY_SELF_ASSESSMENT_ANSWER_MAP, (HashMap<String, String>) answerMap);
    }

    public void print(BaseProcessClass bpc) throws JsonProcessingException {
        HttpServletRequest request = bpc.request;
        // load data
        loadExistingData(bpc);
        prepareForSelfAssessment(bpc);

        // this value will override session value temporary at the print page
        ParamUtil.setRequestAttr(request, KEY_EDITABLE, Boolean.TRUE);
    }


    /** Provide actions according to assessment state
     * @return a list of actions can be applied to current state */
    public List<String> assessmentActions(AssessmentState state) {
        List<String> actions;
        if (state == AssessmentState.PEND) {
            actions = Collections.singletonList(ACTION_FILL);
        } else if (state == AssessmentState.SUBMITTED) {
            actions = Arrays.asList(ACTION_VIEW, ACTION_PRINT);
        } else if (state == AssessmentState.RFI) {
            actions = Arrays.asList(ACTION_EDIT, ACTION_PRINT);
        } else {
            actions = Collections.emptyList();
        }
        return actions;
    }
}
