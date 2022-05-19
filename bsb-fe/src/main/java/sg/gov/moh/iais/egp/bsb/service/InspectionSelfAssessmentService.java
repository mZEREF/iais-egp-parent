package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistSectionDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.ExcelSheetDto;
import com.ecquaria.cloud.moh.iais.dto.FileErrorMsg;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelReader;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelValidatorHelper;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelWriter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.constant.AssessmentState;
import sg.gov.moh.iais.egp.bsb.constant.ChecklistConstants;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.chklst.ChklstItemAnswerDto;
import sg.gov.moh.iais.egp.bsb.dto.chklst.assessment.PreAssessmentDto;
import sg.gov.moh.iais.egp.bsb.dto.chklst.assessment.SelfAssChklItemExcelDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.SelfAssessmtChklDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_ADDITIONAL;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_VALUE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ERROR_MESSAGE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_VALID;
import static sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants.ACTION_DOWNLOAD;
import static sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants.ACTION_EDIT;
import static sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants.ACTION_FILL;
import static sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants.ACTION_PRINT;
import static sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants.ACTION_UPLOAD;
import static sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants.ACTION_UPLOAD_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants.ACTION_VIEW;
import static sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants.FILE_ITEM_ERROR_MSGS;
import static sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants.KEY_ACTIONS;
import static sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants.KEY_ANSWER_MAP;
import static sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants.KEY_CHKL_CONFIG;
import static sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants.KEY_CURRENT_ACTION;
import static sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants.KEY_DATA_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants.KEY_EDITABLE;
import static sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants.KEY_ENTRY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants.KEY_REMARKS;
import static sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants.KEY_SELF_ASSESSMENT_ANSWER_MAP;
import static sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants.KEY_SELF_ASSESSMENT_CHK_LST;
import static sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants.KEY_SELF_ASSESSMENT_CONFIG;
import static sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants.KEY_SEPARATOR;
import static sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants.MASK_PARAM;
import static sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants.SEESION_FILES_MAP_AJAX;
import static sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants.SHEET_NAME_BSB;
import static sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants.SHEET_NAME_COMMON;
import static sg.gov.moh.iais.egp.bsb.constant.module.SelfAssessmentConstants.START_ROW;

@Service
@Slf4j
public class InspectionSelfAssessmentService {

    private final InspectionClient inspectionClient;

    public InspectionSelfAssessmentService(InspectionClient inspectionClient) {
        this.inspectionClient = inspectionClient;
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
        Map<String, ChklstItemAnswerDto> answerMap = (Map<String, ChklstItemAnswerDto>) ParamUtil.getSessionAttr(request, KEY_SELF_ASSESSMENT_ANSWER_MAP);
        if (answerMap == null) {
            if (StringUtils.hasLength(answerRecordDto.getAnswer())) {
                // convert saved JSON to answer map
                String answerJson = answerRecordDto.getAnswer();
                ObjectMapper mapper = new ObjectMapper();
                List<ChklstItemAnswerDto> answerDtoList = mapper.readValue(answerJson, new TypeReference<List<ChklstItemAnswerDto>>() {
                });
                answerMap = Maps.newHashMapWithExpectedSize(answerDtoList.size());
                for (ChklstItemAnswerDto answerDto : answerDtoList) {
                    answerMap.put(answerDto.getConfigId() + KEY_SEPARATOR + answerDto.getSectionId() + KEY_SEPARATOR + answerDto.getItemId(), answerDto);
                }
            } else {
                // compute item amount, use it to determine the map capacity
                int amt = 0;
                for (ChecklistSectionDto sectionDto : configDto.getSectionDtos()) {
                    amt = amt + sectionDto.getChecklistItemDtos().size();
                }
                answerMap = Maps.newHashMapWithExpectedSize(amt);
            }
            ParamUtil.setSessionAttr(request, KEY_SELF_ASSESSMENT_ANSWER_MAP, (HashMap<String, ChklstItemAnswerDto>) answerMap);
        }

        // set DTOs needed by checklist page
        ParamUtil.setRequestAttr(request, KEY_CHKL_CONFIG, configDto);
        ParamUtil.setRequestAttr(request, KEY_ANSWER_MAP, answerMap);
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
            inspectionClient.submitSelfAssessment(answerRecordDto);
            ParamUtil.setRequestAttr(bpc.request, "ackMsg", MessageUtil.getMessageDesc("GENERAL_ERR0038"));
        }
    }


    @SuppressWarnings("unchecked")
    public void clearData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        /*Map<String, ChklstItemAnswerDto> answerMap = (Map<String, ChklstItemAnswerDto>) ParamUtil.getSessionAttr(request, KEY_SELF_ASSESSMENT_ANSWER_MAP);
        answerMap.clear();*/
        ParamUtil.setSessionAttr(request, KEY_SELF_ASSESSMENT_ANSWER_MAP, null);
    }

    public void print(BaseProcessClass bpc) throws JsonProcessingException {
        HttpServletRequest request = bpc.request;
        // load data
        loadExistingData(bpc);
        prepareForSelfAssessment(bpc);

        // this value will override session value temporary at the print page
        ParamUtil.setRequestAttr(request, KEY_EDITABLE, Boolean.FALSE);
    }


    /** Provide actions according to assessment state
     * @return a list of actions can be applied to current state */
    public List<String> assessmentActions(AssessmentState state) {
        List<String> actions;
        if (state == AssessmentState.PEND) {
            actions = Arrays.asList(ACTION_FILL, ACTION_PRINT, ACTION_DOWNLOAD, ACTION_UPLOAD);
        } else if (state == AssessmentState.SUBMITTED) {
            actions = Arrays.asList(ACTION_VIEW, ACTION_PRINT, ACTION_DOWNLOAD, ACTION_UPLOAD);
        } else {
            actions = Collections.emptyList();
        }
        return actions;
    }

    /**
     * Step: PrepareUpload
     */
    public void prepareUpload(BaseProcessClass bpc) {
        log.info("----- PrepareUpload -----");
        clearData(bpc);
        HttpServletRequest request = bpc.request;
        SelfAssessmtChklDto answerRecordDto = (SelfAssessmtChklDto) ParamUtil.getSessionAttr(request, KEY_SELF_ASSESSMENT_CHK_LST);
        if (answerRecordDto != null && StringUtils.hasLength(answerRecordDto.getApplicationId())
                && StringUtils.hasLength(answerRecordDto.getChkLstConfigId())) {
            ParamUtil.setRequestAttr(request, KEY_APP_ID, answerRecordDto.getApplicationId());
            return;
        }
        String maskedAppId = request.getParameter(KEY_ACTION_ADDITIONAL);
        String appId = MaskUtil.unMaskValue(MASK_PARAM, maskedAppId);
        answerRecordDto = inspectionClient.getSavedSelfAssessment(appId);
        if (answerRecordDto == null) {
            answerRecordDto = new SelfAssessmtChklDto();
            answerRecordDto.setApplicationId(appId);
            answerRecordDto.setVersion(1);
            answerRecordDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);

            ChecklistConfigDto configDto = inspectionClient.getMaxVersionChecklistConfig(appId, HcsaChecklistConstants.SELF_ASSESSMENT);
            //ChecklistConfigDto commonConfigDto = inspectionClient.getMaxVersionCommonConfig();
            answerRecordDto.setChkLstConfigId(configDto.getId());
            //answerRecordDto.setCommonChkLstConfigId(commonConfigDto.getId());
        }
        ParamUtil.setRequestAttr(request, KEY_APP_ID, answerRecordDto.getApplicationId());
        ParamUtil.setSessionAttr(request, KEY_SELF_ASSESSMENT_CHK_LST, answerRecordDto);
    }

    /**
     * Step: DoUpload
     */
    public void doUpload(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        log.info(StringUtil.changeForLog("The Action Type: " + actionType));
        if (ModuleCommonConstants.KEY_NAV_BACK.equals(actionType)) {
            ParamUtil.setSessionAttr(request, ACTION_UPLOAD_TYPE, ModuleCommonConstants.KEY_NAV_BACK);
            return;
        }
        SelfAssessmtChklDto answerRecordDto = (SelfAssessmtChklDto) ParamUtil.getSessionAttr(request, KEY_SELF_ASSESSMENT_CHK_LST);
        List<ChklstItemAnswerDto> answerDtos = IaisCommonUtils.genNewArrayList();
        List<FileErrorMsg> errorMsgs = IaisCommonUtils.genNewArrayList();
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        NewDocInfo fileInfo = getFileInfo(request);
        if (fileInfo == null) {
            errorMap.put("selfAssessmentData", MessageUtil.getMessageDesc("GENERAL_ERR0006"));
        } else if (fileInfo.getSize() == 0) {
            errorMap.put("selfAssessmentData", "Could not parse file content.");
        } else if (!FileUtils.isExcel(fileInfo.getFilename())) {
            errorMap.put("selfAssessmentData", MessageUtil.replaceMessage("GENERAL_ERR0018", "XLSX", "fileType"));
        } else {
            Map<String, List<ChklstItemAnswerDto>> result = transformToChklstItemAnswerDtos(fileInfo);
            List<ChklstItemAnswerDto> bsbData = result.get(SHEET_NAME_BSB);
            Boolean isValid = validateChklItemExcelDto(bsbData, SHEET_NAME_BSB, answerRecordDto.getChkLstConfigId(),
                    errorMsgs);
            if (isValid != null && isValid) {
                answerDtos.addAll(bsbData);
            }
            if (isValid == null) {
                errorMap.put("selfAssessmentData", "Could not parse file content. Please download new template to do this.");
                errorMsgs.clear();
            }
            if (!errorMsgs.isEmpty()) {
                Collections.sort(errorMsgs, Comparator.comparing(FileErrorMsg::getSheetName).thenComparing(FileErrorMsg::getRow)
                        .thenComparing(FileErrorMsg::getCol));
                ParamUtil.setRequestAttr(bpc.request, FILE_ITEM_ERROR_MSGS, errorMsgs);
            }
        }
        String nextType = ModuleCommonConstants.KEY_NAV_NEXT;
        if (!errorMap.isEmpty() || IaisCommonUtils.isNotEmpty(errorMsgs)) {
            if (!errorMap.isEmpty()) {
                ParamUtil.setRequestAttr(bpc.request, ValidationConstants.KEY_VALIDATION_ERRORS, JsonUtil.parseToJson(errorMap));
            }
            nextType = ModuleCommonConstants.KEY_NAV_PAGE;
        } else {
            // save
            answerRecordDto.setAnswer(JsonUtil.parseToJson(answerDtos));
            inspectionClient.submitSelfAssessment(answerRecordDto);
        }
        ParamUtil.setRequestAttr(bpc.request, ACTION_UPLOAD_TYPE, nextType);
        ParamUtil.setRequestAttr(bpc.request, "ackMsg", MessageUtil.replaceMessage("GENERAL_ERR0058",
                "Self Assessment Checklist", "data"));
    }

    private Boolean validateChklItemExcelDto(List<ChklstItemAnswerDto> data, String sheetName, String chkLstConfigId,
                                             List<FileErrorMsg> errorMsgs) {
        if (data == null || data.isEmpty()) {
            log.info("No data found!");
            return null;
        }
        Optional<ChklstItemAnswerDto> optional = data.stream()
                .filter(dto -> !Objects.equals(chkLstConfigId, dto.getConfigId())
                        || !ExcelValidatorHelper.isValidUuid(dto.getSectionId())
                        || !ExcelValidatorHelper.isValidUuid(dto.getItemId()))
                .findAny();
        if (optional.isPresent()) {
            log.info(StringUtil.changeForLog("Wrong Data: " + JsonUtil.parseToJson(optional.get()) + " | " + chkLstConfigId));
            return null;
        }
        errorMsgs.addAll(ExcelValidatorHelper.validateExcelList(data, sheetName, ChklstItemAnswerDto::getSnNo, null, START_ROW,
                SelfAssChklItemExcelDto.class));
        return errorMsgs.isEmpty();
    }

    private Map<String, List<ChklstItemAnswerDto>> transformToChklstItemAnswerDtos(NewDocInfo fileInfo) {
        if (fileInfo == null) {
            return IaisCommonUtils.genNewHashMap();
        }
        Map<String, List<ChklstItemAnswerDto>> resultMap = IaisCommonUtils.genNewHashMap();
        try {
            File file = fileInfo.getFile();
            List<ExcelSheetDto> excelSheetDtos = getExcelSheetDtos(null, new ChecklistConfigDto(), null, false);
            Map<String, List<SelfAssChklItemExcelDto>> data = ExcelReader.readerToBeans(file, excelSheetDtos);
            if (data != null && !data.isEmpty()) {
                for (Map.Entry<String, List<SelfAssChklItemExcelDto>> entry : data.entrySet()) {
                    List<ChklstItemAnswerDto> collect = entry.getValue().stream()
                            .filter(dto -> StringUtils.hasLength(dto.getSnNo()))
                            .map(dto -> {
                                String itemKey = dto.getItemKey();
                                if (!StringUtils.hasLength(itemKey)) {
                                    return new ChklstItemAnswerDto();
                                }
                                String[] keys = itemKey.split(KEY_SEPARATOR);
                                if (keys.length != 3) {
                                    return new ChklstItemAnswerDto();
                                }
                                return new ChklstItemAnswerDto(dto.getSnNo(), keys[0], keys[1], keys[2],
                                        ChecklistConstants.getAnswer(dto.getAnswer()), dto.getRemarks());
                            }).collect(Collectors.toList());
                    resultMap.put(entry.getKey(), collect);
                }
            }

        } catch (Exception e) {
            log.error(StringUtil.changeForLog(e.getMessage()), e);
        }
        return resultMap;
    }

    private NewDocInfo getFileInfo(HttpServletRequest request) {
        Map<String, File> fileMap = (Map<String, File>) ParamUtil.getSessionAttr(request, SEESION_FILES_MAP_AJAX);
        if (fileMap == null || fileMap.isEmpty()) {
            log.info("No file found!");
            return null;
        }
        // only one
        Iterator<Map.Entry<String, File>> iterator = fileMap.entrySet().iterator();
        if (!iterator.hasNext()) {
            return null;
        }
        Map.Entry<String, File> next = iterator.next();
        File file = next.getValue();
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        return NewDocInfo.toNewDocInfo(DocConstants.DOC_TYPE_SELF_ASSESSMENT, loginContext.getUserId(), file);
    }

    /**
     * Download / export Template
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @GetMapping(value = "/self-assessment/exporting-template")
    public void exportTemplate(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String maskedAppId = request.getParameter(KEY_APP_ID);
        String appId = MaskUtil.unMaskValue(MASK_PARAM, maskedAppId);
        ChecklistConfigDto configDto = inspectionClient.getMaxVersionChecklistConfig(appId, HcsaChecklistConstants.SELF_ASSESSMENT);
        //ChecklistConfigDto commonConfigDto = inspectionClient.getMaxVersionCommonConfig();
        exportExcel(null, configDto, "Self_Assessment_Template", null, response);
    }

    /**
     * Download / export Data
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @GetMapping(value = "/self-assessment/exporting-data")
    public void exportData(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String maskedAppId = request.getParameter(KEY_APP_ID);
        String appId = MaskUtil.unMaskValue(MASK_PARAM, maskedAppId);
        SelfAssessmtChklDto answerRecordDto = inspectionClient.getSavedSelfAssessment(appId);
        ChecklistConfigDto configDto;
        ChecklistConfigDto commonConfigDto = null;
        Map<String, ChklstItemAnswerDto> answerMap = null;
        if (answerRecordDto == null) {
            configDto = inspectionClient.getMaxVersionChecklistConfig(appId, HcsaChecklistConstants.SELF_ASSESSMENT);
            //commonConfigDto = inspectionClient.getMaxVersionCommonConfig();
        } else {
            configDto = inspectionClient.getChecklistConfigById(answerRecordDto.getChkLstConfigId());
            //commonConfigDto = inspectionClient.getChecklistConfigById(answerRecordDto.getCommonChkLstConfigId());
            if (!StringUtils.isEmpty(answerRecordDto.getAnswer())) {
                List<ChklstItemAnswerDto> answerDtoList = JsonUtil.parseToList(answerRecordDto.getAnswer(), ChklstItemAnswerDto.class);
                if (answerDtoList != null) {
                    answerMap = answerDtoList.stream()
                            .collect(Collectors.toMap((t) -> new StringBuilder()
                                    .append(t.getConfigId())
                                    .append(KEY_SEPARATOR)
                                    .append(t.getSectionId())
                                    .append(KEY_SEPARATOR)
                                    .append(t.getItemId())
                                    .toString(), Function.identity()));
                }
            }
        }
        exportExcel(commonConfigDto, configDto, "Self_Assessment", answerMap, response);
    }

    private void exportExcel(ChecklistConfigDto commonConfigDto, ChecklistConfigDto configDto, String filename,
                             Map<String, ChklstItemAnswerDto> answerMap, HttpServletResponse response) throws Exception {
        try {
            File configInfoTemplate = ResourceUtils.getFile("classpath:template/Self_Assessment_Template.xlsx");
            List<ExcelSheetDto> excelSheetDtos = getExcelSheetDtos(commonConfigDto, configDto, answerMap, true);
            File inputFile = ExcelWriter.writerToExcel(excelSheetDtos, configInfoTemplate, filename);
            FileUtils.writeFileResponseContent(response, inputFile);
            FileUtils.deleteTempFile(inputFile);
        } catch (Exception e) {
            log.error(StringUtil.changeForLog(e.getMessage()), e);
            throw e;
        }
    }

    private List<ExcelSheetDto> getExcelSheetDtos(ChecklistConfigDto commonConfigDto, ChecklistConfigDto configDto,
                                                  Map<String, ChklstItemAnswerDto> answerMap, boolean withData) {
        List<ExcelSheetDto> excelSheetDtos = IaisCommonUtils.genNewArrayList();
        int sheetAt = 1;
        if (commonConfigDto != null) {
            List<SelfAssChklItemExcelDto> data = null;
            if (withData) {
                data = getChklItemExcelDtos(commonConfigDto, answerMap);
            }
            excelSheetDtos.add(getExcelSheetDto(sheetAt++, SHEET_NAME_COMMON, data));
        }

        if (configDto != null) {
            List<SelfAssChklItemExcelDto> data = null;
            if (withData) {
                data = getChklItemExcelDtos(configDto, answerMap);
            }
            excelSheetDtos.add(getExcelSheetDto(sheetAt++, SHEET_NAME_BSB, data));
        }
        return excelSheetDtos;
    }

    private ExcelSheetDto getExcelSheetDto(int sheetAt, String sheetName, List<SelfAssChklItemExcelDto> data) {
        ExcelSheetDto excelSheetDto = new ExcelSheetDto();
        excelSheetDto.setSheetAt(sheetAt);
        excelSheetDto.setSheetName(sheetName);
        excelSheetDto.setBlock(true);
        excelSheetDto.setPwd(Formatter.formatDateTime(new Date(), "yyyyMMdd"));
        excelSheetDto.setStartRowIndex(START_ROW);
        excelSheetDto.setSource(data);
        excelSheetDto.setSourceClass(SelfAssChklItemExcelDto.class);
        excelSheetDto.setDefaultRowHeight((short) 500);
        excelSheetDto.setChangeHeight(true);
        excelSheetDto.setWidthMap(getWidthMap());
        return excelSheetDto;
    }

    private List<SelfAssChklItemExcelDto> getChklItemExcelDtos(ChecklistConfigDto configDto, Map<String, ChklstItemAnswerDto> answerMap) {
        if (configDto == null || configDto.getSectionDtos() == null) {
            return IaisCommonUtils.genNewArrayList();
        }
        List<SelfAssChklItemExcelDto> result = IaisCommonUtils.genNewArrayList();
        List<ChecklistSectionDto> sectionDtos = configDto.getSectionDtos();
        for (int i = 0, m = sectionDtos.size(); i < m; i++) {
            ChecklistSectionDto sectionDto = sectionDtos.get(i);
            List<ChecklistItemDto> checklistItemDtos = sectionDto.getChecklistItemDtos();
            for (int j = 0, n = checklistItemDtos.size(); j < n; j++) {
                ChecklistItemDto itemDto = checklistItemDtos.get(j);
                SelfAssChklItemExcelDto excelDto = new SelfAssChklItemExcelDto();
                excelDto.setSnNo((i+1) + "." + (j+1));
                excelDto.setSection(sectionDto.getSection());
                excelDto.setChecklistItem(itemDto.getChecklistItem());
                String itemKey = new StringBuilder()
                        .append(configDto.getId())
                        .append(KEY_SEPARATOR)
                        .append(sectionDto.getId())
                        .append(KEY_SEPARATOR)
                        .append(itemDto.getItemId())
                        .toString();
                ChklstItemAnswerDto dto = answerMap != null ? answerMap.get(itemKey) : null;
                if (dto != null) {
                    excelDto.setAnswer(ChecklistConstants.displayAnswer(dto.getAnswer()));
                    excelDto.setRemarks(dto.getRemarks());
                } else {
                    excelDto.setAnswer("");
                    excelDto.setRemarks("");
                }
                excelDto.setItemKey(itemKey);
                result.add(excelDto);
            }
        }
        return result;
    }

    private static Map<Integer, Integer> widthMap;

    public static Map<Integer, Integer> getWidthMap() {
        if (widthMap != null) {
            return widthMap;
        }
        widthMap = IaisCommonUtils.genNewHashMap(5);
        widthMap.put(0, 9);
        widthMap.put(1, 18);
        widthMap.put(2, 25);
        widthMap.put(3, 9);
        widthMap.put(4, 25);
        return widthMap;
    }
}
