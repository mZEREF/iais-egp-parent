package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.constant.ChecklistConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants;
import sg.gov.moh.iais.egp.bsb.dto.chklst.ChklstItemAnswerDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.SelfAssessmtChklDto;
import sg.gov.moh.iais.egp.bsb.service.InspectionSelfAssessmentService;
import sg.gov.moh.iais.egp.bsb.service.RfiService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ERROR_MESSAGE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_VALID;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_RFI_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants.ACTION_EDIT;
import static sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants.ACTION_FILL;
import static sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants.KEY_ACTIONS;
import static sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants.KEY_CURRENT_ACTION;
import static sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants.KEY_EDITABLE;
import static sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants.KEY_ENTRY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants.KEY_REMARKS;
import static sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants.KEY_SELF_ASSESSMENT_ANSWER_MAP;
import static sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants.KEY_SELF_ASSESSMENT_CHK_LST;
import static sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants.KEY_SELF_ASSESSMENT_CONFIG;
import static sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants.KEY_SEPARATOR;
import static sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants.MASK_PARAM;
import static sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants.SEESION_FILES_MAP_AJAX;


@Slf4j
@Delegator("bsbRfiInspectionSelfAssessment")
public class BsbRfiInspectionSelfAssessmentDelegator {
    private final InspectionClient inspectionClient;
    private final InspectionSelfAssessmentService inspectionSelfAssessmentService;
    private final RfiService rfiService;


    public BsbRfiInspectionSelfAssessmentDelegator(InspectionClient inspectionClient, InspectionSelfAssessmentService inspectionSelfAssessmentService, RfiService rfiService) {
        this.inspectionClient = inspectionClient;
        this.inspectionSelfAssessmentService = inspectionSelfAssessmentService;
        this.rfiService = rfiService;
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
        session.removeAttribute(SEESION_FILES_MAP_AJAX);

        // get app ID from request parameter
        String maskedAppId = ParamUtil.getString(request, KEY_APP_ID);
        String appId = MaskUtil.unMaskValue(KEY_RFI_APP_ID, maskedAppId);
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
        ParamUtil.setSessionAttr(request, KEY_APP_ID, appId);


        // set audit trail info (We can set appNo here, may be added in future)
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_MAIN_FUNCTION, AuditTrailConsts.FUNCTION_SELF_ASSESSMENT);
    }

    public void init(BaseProcessClass bpc) {
        inspectionSelfAssessmentService.init(bpc);
    }

    public void preLoad(BaseProcessClass bpc) {
        inspectionSelfAssessmentService.preLoad(bpc);
    }

    public void bindAction(BaseProcessClass bpc) {
        inspectionSelfAssessmentService.bindAction(bpc);
    }

    public void loadExistingData(BaseProcessClass bpc) {
        inspectionSelfAssessmentService.loadExistingData(bpc);
    }

    public void prepareUpload(BaseProcessClass bpc) {
        inspectionSelfAssessmentService.prepareUpload(bpc);
    }

    public void doUpload(BaseProcessClass bpc) {
        inspectionSelfAssessmentService.doUpload(bpc);
    }

    public void prepareForSelfAssessmt(BaseProcessClass bpc) throws JsonProcessingException {
        inspectionSelfAssessmentService.prepareForSelfAssessment(bpc);
    }

    public void clearData(BaseProcessClass bpc) {
        inspectionSelfAssessmentService.clearData(bpc);
    }

    public void save(BaseProcessClass bpc) throws JsonProcessingException {
        HttpServletRequest request = bpc.request;
        String currentAction = (String) ParamUtil.getSessionAttr(request, KEY_CURRENT_ACTION);
        if (!ACTION_EDIT.equals(currentAction) && !ACTION_FILL.equals(currentAction)) {
            throw new IaisRuntimeException("Invalid action [" + currentAction + "] to reach here!");
        }
        /* For each sectionId--itemId, retrieve value, put into the answerMap.
         * Check if all questions are answered, if not, route back. */
        ChecklistConfigDto configDto = (ChecklistConfigDto) ParamUtil.getSessionAttr(request, KEY_SELF_ASSESSMENT_CONFIG);
        Map<String, ChklstItemAnswerDto> answerMap = (Map<String, ChklstItemAnswerDto>) ParamUtil.getSessionAttr(request, KEY_SELF_ASSESSMENT_ANSWER_MAP);
        boolean allFilled = true;
        for (ChecklistSectionDto sectionDto : configDto.getSectionDtos()) {
            for (ChecklistItemDto itemDto : sectionDto.getChecklistItemDtos()) {
                String key = configDto.getId() + KEY_SEPARATOR + sectionDto.getId() + KEY_SEPARATOR + itemDto.getItemId();
                String remarksKey = configDto.getId() + KEY_SEPARATOR + sectionDto.getId() + KEY_SEPARATOR + itemDto.getItemId() + KEY_REMARKS;
                String value = ParamUtil.getString(request, key);
                String remarks = ParamUtil.getString(request, remarksKey);
                answerMap.put(key, new ChklstItemAnswerDto(configDto.getId(), sectionDto.getId(), itemDto.getItemId(), value, remarks));
                if (!ChecklistConstants.VALID_ANSWERS.contains(value)) {
                    allFilled = false;
                }
            }
        }

        ParamUtil.setSessionAttr(request, KEY_SELF_ASSESSMENT_ANSWER_MAP, (HashMap<String, ChklstItemAnswerDto>) answerMap);
        ParamUtil.setRequestAttr(request, KEY_VALID, allFilled);
        if (!allFilled) {
            log.info("Applicant try to submit self-assessment, but not all items checked");
            ParamUtil.setRequestAttr(request, KEY_ERROR_MESSAGE, "Please complete the checklist questions.");
        } else {
            log.info("All items checked, save self-assessment data");
            // save checklist answer into DB, change app status to pending readiness
            List<ChklstItemAnswerDto> answerDtoList = new ArrayList<>(answerMap.size());
            for (Map.Entry<String, ChklstItemAnswerDto> entry : answerMap.entrySet()) {
                answerDtoList.add(entry.getValue());
            }
            ObjectMapper mapper = new ObjectMapper();
            String answer = mapper.writeValueAsString(answerDtoList);
            SelfAssessmtChklDto answerRecordDto = (SelfAssessmtChklDto) ParamUtil.getSessionAttr(request, KEY_SELF_ASSESSMENT_CHK_LST);
            answerRecordDto.setAnswer(answer);
            ParamUtil.setSessionAttr(request, KEY_SELF_ASSESSMENT_CHK_LST, answerRecordDto);
            ParamUtil.setRequestAttr(bpc.request, "ackMsg", MessageUtil.getMessageDesc("GENERAL_ERR0038"));

            // save data
            rfiService.saveInspectionSelfAssessment(request, answerRecordDto);
            // acknowledge page need appId
            String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
            ParamUtil.setRequestAttr(request, KEY_APP_ID, appId);
        }
    }

    public void print(BaseProcessClass bpc) throws JsonProcessingException {
        inspectionSelfAssessmentService.print(bpc);
    }
}
