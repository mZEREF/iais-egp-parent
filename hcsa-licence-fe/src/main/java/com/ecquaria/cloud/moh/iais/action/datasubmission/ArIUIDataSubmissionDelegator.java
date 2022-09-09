package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.organization.OrganizationConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArCurrentInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleStageSelectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorSampleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.HusbandDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.ControllerHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.DsRfcHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.PatientService;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.HEAD;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import sop.webflow.rt.api.BaseProcessClass;

import static com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant.JUMP_ACTION_TYPE;

@Delegator("arIUIDataSubmissionDelegator")
@Slf4j
public class ArIUIDataSubmissionDelegator {

    protected static final String ACTION_TYPE_PAGE = "page";
    protected static final String ACTION_TYPE_RETURN = "return";
    protected static final String ACTION_TYPE_CONFIRM = "confirm";
    protected static final String ACTION_TYPE_DRAFT = "draft";
    protected static final String ACTION_TYPE_SUBMISSION = "submission";
    private static final String ACTION_TYPE_STAGE = "stage";
    private static final String ACTION_TYPE_ACK = "ack";
    private static final String ACTION_TYPE_AMEND = "amend";

    private static final String CENTRE_SEL = "centreSel";
    private static final String CURRENT_PAGE_STAGE = "currentPageStage";
    private static final String CURRENT_STAGE = "currentStage";
    public static final String PATIENT_INFO_DTO = "patientInfoDto";
    public static final String SAVE_DRAFT_SUCCESS = "saveDraftSuccess";
    public static final String EXISTED_PATIENT = "existedPatient";
    public static final String HAS_CYCLE = "hasCycle";
    public static final String CYCLE_SELECT = "cycleRadio";

    @Autowired
    private GenerateIdClient generateIdClient;

    @Autowired
    private PatientService patientService;

    @Autowired
    private ArDataSubmissionService arDataSubmissionService;


    public void start(BaseProcessClass bpc) {
        log.info("----- Assisted Reproduction Submission Start -----");

        HttpSession session = bpc.request.getSession();
        session.removeAttribute(DataSubmissionConstant.AR_PREMISES_MAP);
        session.removeAttribute(DataSubmissionConstant.AR_DATA_SUBMISSION);
        session.removeAttribute(PATIENT_INFO_DTO);
        session.removeAttribute(EXISTED_PATIENT);
    }

    public void prepareSwitch(BaseProcessClass bpc) {
        // default crud_action_type
        String actionType = (String) ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        log.info(StringUtil.changeForLog("----- Action Type: " + actionType + " -----"));
        if (StringUtil.isEmpty(actionType)) {
            actionType = ACTION_TYPE_PAGE;
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, actionType);
        }
    }

    public void preARIUIDataSubmission(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, CURRENT_PAGE_STAGE, ACTION_TYPE_PAGE);
        //prepare ar center or it's err msg
        prepareSelOpts(bpc);
        // patient age error msg
        ParamUtil.setRequestAttr(bpc.request, "ageMsg", DataSubmissionHelper.getAgeMessage(DataSubmissionConstant.DS_SHOW_PATIENT));
        ParamUtil.setRequestAttr(bpc.request, "hbdAgeMsg", DataSubmissionHelper.getAgeMessage(DataSubmissionConstant.DS_SHOW_HUSBAND));
    }

    public void doARIUIDataSubmission(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setRequestAttr(request, CURRENT_STAGE, ACTION_TYPE_PAGE);
        String actionType = ParamUtil.getString(request, DataSubmissionConstant.CRUD_TYPE);
        ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, actionType);
        if (ACTION_TYPE_RETURN.equals(actionType)) {
            return;
        }
        String submissionType = ParamUtil.getString(request, "submissionType");
        String submissionMethod = ParamUtil.getString(request, "submissionMethod");
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        validateCommonPage(submissionType, submissionMethod, errorMap);
        String existedPatient = ParamUtil.getString(request, EXISTED_PATIENT);
        ParamUtil.setSessionAttr(request, EXISTED_PATIENT, existedPatient);
        submissionType = IaisEGPConstant.YES.equals(existedPatient) ? DataSubmissionConsts.AR_TYPE_SBT_CYCLE_STAGE : submissionType;

        //prepare ArSubmissionDto for donor sample or patient or cycle
        ArSuperDataSubmissionDto currentSuper = prepareArSuperDataSubmissionDto(request, submissionType, submissionMethod, errorMap);

        // from draft confirm action
        String hciCode = Optional.ofNullable(currentSuper.getPremisesDto())
                .map(PremisesDto::getHciCode)
                .orElse("");
        String actionValue = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_VALUE);
        CycleStageSelectionDto selectionDto = currentSuper.getSelectionDto();
        if (processDraftConfirmAction(request, actionType, hciCode, actionValue, selectionDto)) {
            return;
        }

        boolean startNewCycle = false;
        if (DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO.equals(submissionType)) {
            PatientInfoDto patientInfo = genPatientByPage(request, currentSuper.getOrgId(),false);
            String previousIdentification = ParamUtil.getString(request, "previousIdentification");
            if (StringUtil.isEmpty(previousIdentification) || StringUtil.isEmpty(patientInfo.getHusband().getIdType())) {
                ParamUtil.setRequestAttr(request, "jumpValidateHusband", AppConsts.YES);
                errorMap.put("previousIdentification", "GENERAL_ERR0006");
            }
            if (Boolean.TRUE.equals(patientInfo.getPatient().getPreviousIdentification()) && !patientInfo.isRetrievePrevious()) {
                errorMap.put("preIdNumber", "DS_MSG006");
            }
            ValidationResult validationResult = WebValidationHelper.validateProperty(patientInfo, "save");
            errorMap.putAll(validationResult.retrieveAll());
            currentSuper.setPatientInfoDto(patientInfo);
        } else if (DataSubmissionConsts.AR_TYPE_SBT_CYCLE_STAGE.equals(submissionType)) {
            PatientInfoDto patientInfoDto = (PatientInfoDto) ParamUtil.getSessionAttr(request, PATIENT_INFO_DTO);
            currentSuper.setPatientInfoDto(patientInfoDto);
            if (ACTION_TYPE_AMEND.equals(actionType)) {
                updateSubmissionType(currentSuper, DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_TYPE_AMEND);
            } else {
                // jump to sub stage
                selectionDto = (CycleStageSelectionDto) ParamUtil.getSessionAttr(request, "selectionDto");
                String nextStage = ParamUtil.getString(request, "nextStage");
                String cycleRadio = ParamUtil.getString(request, CYCLE_SELECT);
                ParamUtil.setRequestAttr(request, CYCLE_SELECT, cycleRadio);
                String hasCycle = ParamUtil.getString(request, HAS_CYCLE);
                currentSuper.getDataSubmissionDto().setCycleStage(nextStage);
                startNewCycle = "N".equals(hasCycle) || "newCycle".equals(cycleRadio);
                if (startNewCycle && StringUtil.isEmpty(nextStage)) {
                    errorMap.put("nextStage", "GENERAL_ERR0006");
                }
                if ("Y".equals(hasCycle) && StringUtil.isEmpty(cycleRadio)) {
                    errorMap.put(CYCLE_SELECT, "GENERAL_ERR0006");
                }
                if (errorMap.isEmpty()) {
                    selectionDto = arDataSubmissionService.getCycleStageSelectionDtoByConds(patientInfoDto.getPatient().getPatientCode(),
                            hciCode, cycleRadio);
                    selectionDto.setHciCode(hciCode);
                    if(startNewCycle) {
                        selectionDto.setCycle(null);
                        selectionDto.setUndergoingCycle(false);
                    }
                    if (StringUtil.isNotEmpty(selectionDto.getLastStage())) {
                        selectionDto.setLastStageDesc(MasterCodeUtil.getCodeDesc(selectionDto.getLastStage()));
                    } else {
                        selectionDto.setLastStageDesc("-");
                    }
                }
                selectionDto.setStage(nextStage);
                String licenseeId = DataSubmissionHelper.getLicenseeId(request);
                currentSuper.setCycleDto(DataSubmissionHelper.initCycleDto(selectionDto, currentSuper.getSvcName(), hciCode, licenseeId));
                if (startNewCycle){
                    selectionDto.setNavCurrentCycle(selectionDto.getCycle());
                } else {
                    selectionDto.setNavCurrentCycle(
                            selectionDto
                                    .getCycleDtos().stream().filter(it-> cycleRadio.equals(it.getId()))
                                    .map(CycleDto::getCycleType).findFirst().orElse(selectionDto.getCycle())
                    );
                }
                ArCurrentInventoryDto arCurrentInventoryDto = arDataSubmissionService.getArCurrentInventoryDtoByConds(hciCode, licenseeId, selectionDto.getPatientCode(), currentSuper.getSvcName());
                if (arCurrentInventoryDto == null) {
                    arCurrentInventoryDto = new ArCurrentInventoryDto();
                    arCurrentInventoryDto.setHciCode(hciCode);
                    arCurrentInventoryDto.setSvcName(currentSuper.getSvcName());
                    arCurrentInventoryDto.setLicenseeId(licenseeId);
                    arCurrentInventoryDto.setPatientCode(selectionDto.getPatientCode());
                }
                currentSuper.setArCurrentInventoryDto(arCurrentInventoryDto);
                currentSuper.setSelectionDto(selectionDto);
                ParamUtil.setRequestAttr(request, DataSubmissionConstant.CRUD_ACTION_TYPE_CT, transferNextStage(nextStage));
            }
        } else if (DataSubmissionConsts.AR_TYPE_SBT_DONOR_SAMPLE.equals(submissionType)) {
            donorSamplePageAction(request, submissionType, actionType, errorMap);
        }

        // action is next and error is empty
        if (ACTION_TYPE_SUBMISSION.equals(actionType) && processErrorMsg(errorMap, request)) {
            if (DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO.equals(submissionType)) {
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_TYPE_CONFIRM);
            } else if (DataSubmissionConsts.AR_TYPE_SBT_CYCLE_STAGE.equals(submissionType)) {
                // start new stage
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_TYPE_STAGE);
                if (startNewCycle && !haveStageDraft(request)) {
                    ParamUtil.setRequestAttr(bpc.request, JUMP_ACTION_TYPE, "jump");
                }
            }
        }
        DataSubmissionHelper.setCurrentArDataSubmission(currentSuper, request);
    }


    public void preAmendPatient(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, CURRENT_PAGE_STAGE, ACTION_TYPE_AMEND);
        // patient age error msg
        ParamUtil.setRequestAttr(bpc.request, "ageMsg", DataSubmissionHelper.getAgeMessage(DataSubmissionConstant.DS_SHOW_PATIENT));
        ParamUtil.setRequestAttr(bpc.request, "hbdAgeMsg", DataSubmissionHelper.getAgeMessage(DataSubmissionConstant.DS_SHOW_HUSBAND));
    }

    public void doAmendPatient(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setRequestAttr(request, CURRENT_STAGE, ACTION_TYPE_AMEND);
        String actionType = ParamUtil.getString(request, DataSubmissionConstant.CRUD_TYPE);
        ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, actionType);
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        arSuperDataSubmissionDto.setAppType(DataSubmissionConsts.DS_APP_TYPE_RFC);

        if (ACTION_TYPE_RETURN.equals(actionType)){
            arSuperDataSubmissionDto.setAppType(DataSubmissionConsts.DS_APP_TYPE_NEW);
            updateSubmissionType(arSuperDataSubmissionDto, DataSubmissionConsts.AR_TYPE_SBT_CYCLE_STAGE);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_TYPE_PAGE);
            return;
        }

        PatientInfoDto patientInfo = genPatientByPage(request, arSuperDataSubmissionDto.getOrgId(), true);
        arSuperDataSubmissionDto.setPatientInfoDto(patientInfo);

        if (ACTION_TYPE_SUBMISSION.equals(actionType)) {
            Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
            ValidationResult validationResult = WebValidationHelper.validateProperty(patientInfo, "rfc");
            errorMap.putAll(validationResult.retrieveAll());
            if (processErrorMsg(errorMap, request)) {
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_TYPE_PAGE);
            }
        }

        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto, request);
    }

    public void prepareConfirm(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, CURRENT_PAGE_STAGE, ACTION_TYPE_CONFIRM);
        // set flag, only donor sample or patient
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        String submissionType = arSuperDataSubmissionDto.getSubmissionType();
        String printFlag = DataSubmissionConstant.PRINT_FLAG_ART;
        if (DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO.equals(submissionType)) {
            DsRfcHelper.handle(arSuperDataSubmissionDto.getPatientInfoDto());
            printFlag = DataSubmissionConsts.DS_PATIENT_ART;
        }
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.PRINT_FLAG, printFlag);
    }

    public void submission(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        PatientInfoDto patientInfoDto = arSuperDataSubmission.getPatientInfoDto();
        CycleDto cycle = arSuperDataSubmission.getCycleDto();
        if (patientInfoDto != null) {
            cycle.setPatientCode(patientInfoDto.getPatient().getPatientCode());
        }
        preSubmissionPrepareDataSubmission(bpc, arSuperDataSubmission, arSuperDataSubmission.getDataSubmissionDto());
        // do submission
        arSuperDataSubmission = arDataSubmissionService.saveArSuperDataSubmissionDto(arSuperDataSubmission);
        try {
            arSuperDataSubmission = arDataSubmissionService.saveArSuperDataSubmissionDtoToBE(arSuperDataSubmission);
        } catch (Exception e) {
            log.error(StringUtil.changeForLog("The Eic saveArSuperDataSubmissionDtoToBE failed ===>" + e.getMessage()), e);
        }
        // clear draft
        if (!StringUtil.isEmpty(arSuperDataSubmission.getDraftId())) {
            arDataSubmissionService.updateDataSubmissionDraftStatus(arSuperDataSubmission.getDraftId(),
                    DataSubmissionConsts.DS_STATUS_INACTIVE);
        }
        // set next stage
        String submissionType = arSuperDataSubmission.getSubmissionType();
        if (DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO.equals(submissionType)) {
            // if is new patient, now is existed
            ParamUtil.setSessionAttr(bpc.request, EXISTED_PATIENT, IaisEGPConstant.YES);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, CommonDelegator.ACTION_TYPE_PAGE);
        } else if (DataSubmissionConsts.AR_TYPE_SBT_DONOR_SAMPLE.equals(submissionType)) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_TYPE_ACK);
        }
    }

    public void saveDraft(BaseProcessClass bpc) {
        String currentStage = ParamUtil.getRequestString(bpc.request, CURRENT_STAGE);
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, currentStage);
        // TODO
    }

    public void doBack(BaseProcessClass bpc) throws IOException {
        log.info(StringUtil.changeForLog("The doReturn start ..."));
        ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        String currentStage = ParamUtil.getRequestString(bpc.request, CURRENT_STAGE);
        String uri = InboxConst.URL_LICENCE_WEB_MODULE + "MohDataSubmission/PrepareCompliance";
        if (ACTION_TYPE_AMEND.equals(currentStage) || ACTION_TYPE_CONFIRM.equals(currentStage)) {
            uri = InboxConst.URL_LICENCE_WEB_MODULE + "MohARAndIUIDataSubmission/PreARIUIDataSubmission";
        }
        StringBuilder url = new StringBuilder();
        url.append(InboxConst.URL_HTTPS)
                .append(bpc.request.getServerName())
                .append(uri);
        log.info(StringUtil.changeForLog("The url is -->:" + url));
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
        IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
        log.info(StringUtil.changeForLog("The doReturn end ..."));
    }

    public void prepareAck(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, CURRENT_PAGE_STAGE, ACTION_TYPE_ACK);
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.PRINT_FLAG, DataSubmissionConstant.PRINT_FLAG_ACKART);
        String emailAddress = DataSubmissionHelper.getEmailAddrsByRoleIdsAndLicenseeId(bpc.request, Collections.singletonList(RoleConsts.USER_ROLE_DS_AR));
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.EMAIL_ADDRESS, emailAddress);
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.SUBMITTED_BY, DataSubmissionHelper.getLoginContext(bpc.request).getUserName());
    }

    public void pageConfirmAction(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setRequestAttr(request, CURRENT_STAGE, ACTION_TYPE_CONFIRM);
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap(1);
        String actionType = ParamUtil.getString(request, DataSubmissionConstant.CRUD_TYPE);
        ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, actionType);
        if (ACTION_TYPE_SUBMISSION.equals(actionType)) {
            String[] declaration = ParamUtil.getStrings(request, "declaration");
            if (declaration == null || declaration.length == 0) {
                errorMap.put("declaration", "GENERAL_ERR0006");
            }
            ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(request);
            DataSubmissionDto dataSubmissionDto = arSuperDataSubmission.getDataSubmissionDto();
            if (declaration != null && declaration.length > 0) {
                dataSubmissionDto.setDeclaration(declaration[0]);
            } else {
                dataSubmissionDto.setDeclaration(null);
            }
        }else if (ACTION_TYPE_RETURN.equals(actionType)){
            ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_TYPE_PAGE);
            return;
        }

        if (!errorMap.isEmpty()) {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_TYPE_CONFIRM);
        }
    }

    public void prepareStage(BaseProcessClass bpc) {
        // clear crud_action_type, because sub stage need switch
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, null);
    }

    public void init(BaseProcessClass bpc) {
    }

    private void donorSamplePageAction(HttpServletRequest request, String submissionType, String actionType, Map<String, String> errorMap) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        DonorSampleDto donorSampleDto = genDonorSampleDtoByPage(request);
        arSuperDataSubmissionDto.setDonorSampleDto(donorSampleDto);
        if (ACTION_TYPE_SUBMISSION.equals(actionType)) {
            ValidationResult validationResult = WebValidationHelper.validateProperty(donorSampleDto, "save");
            errorMap.putAll(validationResult.retrieveAll());
            if (processErrorMsg(errorMap, request)) {
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_TYPE_CONFIRM);
            }
        }
    }

    private boolean processErrorMsg(Map<String, String> errorMap, HttpServletRequest request) {
        if (IaisCommonUtils.isNotEmpty(errorMap)) {
            String currentStage = ParamUtil.getRequestString(request, CURRENT_STAGE);
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, currentStage);
            return false;
        }
        return true;
    }

    private void prepareSelOpts(BaseProcessClass bpc) {
        Map<String, PremisesDto> premisesMap = DataSubmissionHelper.setArPremisesMap(bpc.request);
        if (premisesMap.isEmpty()) {
            Map<String, String> errorMap = IaisCommonUtils.genNewHashMap(2);
            errorMap.put("noArLicences", "There are no active Assisted Reproduction licences");
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
        } else {
            List<SelectOption> arCenterSelOpts = DataSubmissionHelper.genPremisesOptions(premisesMap);
            ParamUtil.setRequestAttr(bpc.request, "premisesOpts", arCenterSelOpts);
            ParamUtil.setRequestAttr(bpc.request, "donorSampleFromSelOpts", getSampleFromSelOpts(bpc.request));
        }
        List<SelectOption> nricFinTypeSelOpts = MasterCodeUtil.retrieveByCategory(MasterCodeUtil.CATE_ID_DS_ID_TYPE).stream()
                .filter(it -> !it.getCode().equals(DataSubmissionConsts.AR_ID_TYPE_PASSPORT_NO))
                .map(it -> new SelectOption(it.getCode(), it.getDescription()))
                .collect(Collectors.toList());
        ParamUtil.setRequestAttr(bpc.request, "nricFinTypeSelOpts", nricFinTypeSelOpts);
        List<SelectOption> newCycleOpts = MasterCodeUtil.retrieveOptionsByCodes(
                DataSubmissionHelper.getNextStagesForAr(null, null, null, false, false, false, false, false)
                        .toArray(new String[]{}));
        ParamUtil.setRequestAttr(bpc.request, "newCycleOpts", newCycleOpts);
    }

    private void validateCommonPage(String submissionType, String submissionMethod, Map<String, String> errorMap) {
        if (!StringUtils.hasLength(submissionMethod)) {
            errorMap.put("submissionMethod", "GENERAL_ERR0006");
        }

        if (!StringUtils.hasLength(submissionType)) {
            errorMap.put("submissionType", "GENERAL_ERR0006");
        }
    }

    private boolean processDraftConfirmAction(HttpServletRequest request, String actionType, String hciCode, String actionValue, CycleStageSelectionDto selectionDto) {
        ArSuperDataSubmissionDto currentSuper = DataSubmissionHelper.getCurrentArDataSubmission(request);
        if ("resume".equals(actionValue)) {
            ArSuperDataSubmissionDto arSuperDataSubmissionDtoDraft = arDataSubmissionService.getArSuperDataSubmissionDtoDraftById(
                    currentSuper.getDraftId());
            if (arSuperDataSubmissionDtoDraft != null) {
                DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDtoDraft, request);
            }
            ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, actionType);
            ParamUtil.setRequestAttr(request, DataSubmissionConstant.CRUD_ACTION_TYPE_CT, transferNextStage(selectionDto.getStage()));
            return true;
        } else if ("delete".equals(actionValue)) {
            String orgId = currentSuper.getOrgId();
            arDataSubmissionService.deleteArSuperDataSubmissionDtoDraftByConds(selectionDto.getPatientIdType(),
                    selectionDto.getPatientIdNumber(), selectionDto.getPatientNationality(),
                    orgId, hciCode);
            currentSuper.setDraftNo(null);
            currentSuper.setDraftId(null);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, actionType);
            ParamUtil.setRequestAttr(request, DataSubmissionConstant.CRUD_ACTION_TYPE_CT, transferNextStage(selectionDto.getStage()));
            return true;
        }
        return false;
    }

    private boolean haveStageDraft(HttpServletRequest request) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        CycleStageSelectionDto selectionDto = arSuperDataSubmissionDto.getSelectionDto();
        String orgId = arSuperDataSubmissionDto.getOrgId();
        String userId = "";
        LoginContext loginContext = DataSubmissionHelper.getLoginContext(request);
        if (loginContext != null) {
            userId = loginContext.getUserId();
        }
        String hciCode = Optional.ofNullable(arSuperDataSubmissionDto.getPremisesDto())
                .map(PremisesDto::getHciCode)
                .orElse("");

        String actionValue = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_VALUE);
        List<ArSuperDataSubmissionDto> dataSubmissionDraftList = arDataSubmissionService.getArSuperDataSubmissionDtoDraftByConds(
                selectionDto.getPatientIdType(), selectionDto.getPatientIdNumber(), selectionDto.getPatientNationality(),
                orgId, hciCode, true, userId);
        if (IaisCommonUtils.isNotEmpty(dataSubmissionDraftList)) {
            dataSubmissionDraftList = dataSubmissionDraftList.stream()
                    .filter(arDraft -> arDraft.getSelectionDto().getStage().equals(selectionDto.getStage()))
                    .collect(Collectors.toList());
            if (DataSubmissionConsts.AR_STAGE_TRANSFER_IN_AND_OUT.equals(selectionDto.getStage()) && IaisCommonUtils.isNotEmpty(dataSubmissionDraftList)) {
                dataSubmissionDraftList = dataSubmissionDraftList.stream()
                        .filter(arSuperDataSubmissionDto1 -> StringUtil.isEmpty(arSuperDataSubmissionDto1.getTransferInOutStageDto().getBindSubmissionId()))
                        .collect(Collectors.toList());
            }
        }
        ArSuperDataSubmissionDto dataSubmissionDraft = null;
        if (IaisCommonUtils.isNotEmpty(dataSubmissionDraftList)) {
            dataSubmissionDraft = dataSubmissionDraftList.get(0);
        }
        if (dataSubmissionDraft != null) {
            arSuperDataSubmissionDto.setDraftId(dataSubmissionDraft.getDraftId());
            arSuperDataSubmissionDto.setDraftNo(dataSubmissionDraft.getDraftNo());
            DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto, request);
            ParamUtil.setRequestAttr(request, "hasDraft", Boolean.TRUE);
            return true;
        }
        return false;
    }

    public PatientInfoDto genPatientByPage(HttpServletRequest request, String orgId, boolean isAmend) {
        PatientInfoDto patientInfo = new PatientInfoDto();
        if (isAmend) {
            patientInfo = (PatientInfoDto) ParamUtil.getSessionAttr(request, PATIENT_INFO_DTO);
        }

        PatientDto patient = ControllerHelper.get(request, PatientDto.class);
        HusbandDto husband = ControllerHelper.get(request, HusbandDto.class, "Hbd");

        if (isAmend) {
            //amend just replace field need filled
            PatientDto oldPatient = patientInfo.getPatient();
            oldPatient.setName(patient.getName());
            oldPatient.setBirthDate(patient.getBirthDate());
            oldPatient.setNationality(patient.getNationality());
            oldPatient.setEthnicGroup(patient.getEthnicGroup());
            oldPatient.setEthnicGroupOther(patient.getEthnicGroupOther());
            oldPatient.setPreviousIdentification(Boolean.TRUE);
            patient = oldPatient;


            HusbandDto oldHusband = patientInfo.getHusband();
            oldHusband.setName(husband.getName());
            oldHusband.setBirthDate(husband.getBirthDate());
            oldHusband.setNationality(husband.getNationality());
            oldHusband.setEthnicGroup(husband.getEthnicGroup());
            oldHusband.setEthnicGroupOther(husband.getEthnicGroupOther());
            husband = oldHusband;
        } else {
            String identityNo = ParamUtil.getString(request, "identityNo");
            String hasIdNumber = ParamUtil.getString(request, "ptHasIdNumber");
            String idType = patientService.judgeIdType(hasIdNumber, identityNo);

            patient.setIdType(idType);
            patient.setIdNumber(identityNo);
            patient.setOrgId(orgId);

            String hubHasIdNumber = ParamUtil.getString(request, "hubHasIdNumber");
            String hubIdType = patientService.judgeIdType(hubHasIdNumber, husband.getIdNumber());
            husband.setIdType(hubIdType);

            patientInfo.setIsPreviousIdentification(Boolean.TRUE.equals(Boolean.TRUE.equals(patient.getPreviousIdentification())) ? IaisEGPConstant.YES : IaisEGPConstant.NO);
        }

        patientInfo.setAppType(isAmend?DataSubmissionConsts.DS_APP_TYPE_NEW:DataSubmissionConsts.DS_APP_TYPE_NEW);
        DsRfcHelper.prepare(patient);
        patientInfo.setPatient(patient);

        DsRfcHelper.prepare(husband);
        patientInfo.setHusband(husband);

        String patientCode = null;
        if (Boolean.TRUE.equals(patient.getPreviousIdentification())) {
            PatientDto previous = ControllerHelper.get(request, PatientDto.class, "pre", "");

            PatientDto db = retrievePrePatient(patient, previous);

            if (db != null && !StringUtils.isEmpty(db.getId())) {
                patientInfo.setRetrievePrevious(true);
                previous = db;
            } else {
                patientInfo.setRetrievePrevious(false);
            }

            patientInfo.setPrevious(previous);

            // retrieve patient code if exist previous patient information
            patientCode = previous.getPatientCode();
        } else {
            patientInfo.setRetrievePrevious(false);
            patientInfo.setPrevious(null);
        }

        patient.setPatientCode(patientService.getPatientCode(patientCode));

        return patientInfo;
    }


    private void preSubmissionPrepareDataSubmission(BaseProcessClass bpc, ArSuperDataSubmissionDto arSuperDataSubmission, DataSubmissionDto dataSubmissionDto) {
        if (StringUtil.isEmpty(dataSubmissionDto.getSubmissionNo())) {
            String submissionNo = arDataSubmissionService.getSubmissionNo(arSuperDataSubmission.getSelectionDto(),
                    DataSubmissionConsts.DS_AR);
            dataSubmissionDto.setSubmissionNo(submissionNo);
        }
        if (DataSubmissionConsts.DS_APP_TYPE_RFC.equals(dataSubmissionDto.getAppType())) {
            dataSubmissionDto.setStatus(DataSubmissionConsts.DS_STATUS_AMENDED);
        } else if (StringUtil.isEmpty(dataSubmissionDto.getStatus())) {
            dataSubmissionDto.setStatus(DataSubmissionConsts.DS_STATUS_ACTIVE);
        }
        LoginContext loginContext = DataSubmissionHelper.getLoginContext(bpc.request);
        if (loginContext != null) {
            dataSubmissionDto.setSubmitBy(loginContext.getUserId());
            dataSubmissionDto.setSubmitDt(new Date());
        }
    }

    private PatientDto retrievePrePatient(PatientDto patient, PatientDto previous) {
        String idType = patientService.judgeIdType(previous.getIdNumber());
        return patientService.getActiveArPatientByConds(idType, previous.getIdNumber(),
                previous.getNationality(), patient.getOrgId());
    }


    private ArSuperDataSubmissionDto prepareArSuperDataSubmissionDto(HttpServletRequest request, String submissionType, String submissionMethod, Map<String, String> errorMap) {
        // check premises
        HttpSession session = request.getSession();
        String centreSel = ParamUtil.getString(request, CENTRE_SEL);
        Map<String, PremisesDto> premisesMap = (Map<String, PremisesDto>) ParamUtil.getSessionAttr(request, DataSubmissionConstant.AR_PREMISES_MAP);
        PremisesDto premisesDto = null;
        if (!StringUtils.isEmpty(centreSel)) {
            if (premisesMap != null) {
                premisesDto = premisesMap.get(centreSel);
            }
            if (premisesDto == null) {
                errorMap.put(CENTRE_SEL, "GENERAL_ERR0049");
            }
        } else if (premisesMap != null && premisesMap.size() > 1) {
            errorMap.put(CENTRE_SEL, "GENERAL_ERR0006");
        } else {
            errorMap.put(CENTRE_SEL, "There are no active Assisted Reproduction licences");
        }

        ArSuperDataSubmissionDto currentSuper = DataSubmissionHelper.getCurrentArDataSubmission(request);
        ArSuperDataSubmissionDto oldSuperDto = currentSuper;
        boolean reNew = isNeedReNewArSuperDto(currentSuper, submissionType, submissionMethod, premisesDto);
        if (reNew) {
            currentSuper = new ArSuperDataSubmissionDto();
        }

        String orgId = "";
        String licenseeId = "";
        LoginContext loginContext = DataSubmissionHelper.getLoginContext(request);
        if (loginContext != null) {
            orgId = loginContext.getOrgId();
            licenseeId = loginContext.getLicenseeId();
        }
        currentSuper.setAppType(DataSubmissionConsts.DS_APP_TYPE_NEW);
        currentSuper.setOrgId(orgId);
        currentSuper.setLicenseeId(licenseeId);
        currentSuper.setCentreSel(centreSel);
        currentSuper.setPremisesDto(premisesDto);
        currentSuper.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        currentSuper.setSubmissionMethod(submissionMethod);
        currentSuper.setSubmissionType(submissionType);
        if (reNew) {
            currentSuper.setDataSubmissionDto(DataSubmissionHelper.initDataSubmission(currentSuper, true));
            currentSuper.setCycleDto(DataSubmissionHelper.initCycleDto(currentSuper, true));
        }
        DataSubmissionHelper.setCurrentArDataSubmission(currentSuper, request);
        return currentSuper;
    }

    private void updateSubmissionType(ArSuperDataSubmissionDto arSuperDataSubmissionDto, String submissionType){
        arSuperDataSubmissionDto.setSubmissionType(submissionType);
        arSuperDataSubmissionDto.setDataSubmissionDto(DataSubmissionHelper.initDataSubmission(arSuperDataSubmissionDto, false));
        arSuperDataSubmissionDto.setCycleDto(DataSubmissionHelper.initCycleDto(arSuperDataSubmissionDto, false));
    }

    private boolean isNeedReNewArSuperDto(ArSuperDataSubmissionDto arSuperDto, String submissionType, String submissionMethod,
                                          PremisesDto premisesDto) {
        if (arSuperDto == null
                || !Objects.equals(submissionType, arSuperDto.getSubmissionType())
                || !Objects.equals(submissionMethod, arSuperDto.getSubmissionMethod())) {
            return true;
        }
        String hciCode = Optional.ofNullable(premisesDto)
                .map(PremisesDto::getHciCode)
                .orElse("");
        return !Objects.equals(hciCode, arSuperDto.getHciCode());
    }

    private String checkIdentityNoType(String identityNo) {
        String upper = identityNo.toUpperCase();
        boolean b = SgNoValidator.validateNric(upper);
        boolean b1 = SgNoValidator.validateFin(upper);
        if (b) {
            return OrganizationConstants.ID_TYPE_NRIC;
        }

        if (b1) {
            return OrganizationConstants.ID_TYPE_FIN;
        }

        return OrganizationConstants.ID_TYPE_PASSPORT;
    }

    private DonorSampleDto genDonorSampleDtoByPage(HttpServletRequest request) {
        DonorSampleDto donorSampleDto = ControllerHelper.get(request, DonorSampleDto.class);
        String sampleType = donorSampleDto.getSampleType();

        if (Arrays.asList(
                DataSubmissionConsts.DONATED_TYPE_FRESH_OOCYTE,
                DataSubmissionConsts.DONATED_TYPE_FROZEN_OOCYTE,
                DataSubmissionConsts.DONATED_TYPE_FROZEN_EMBRYO
        ).contains(sampleType)) {
            donorSampleDto.setSampleKey(generateIdClient.getSeqId().getEntity());
        }
        if (Arrays.asList(
                DataSubmissionConsts.DONATED_TYPE_FROZEN_SPERM,
                DataSubmissionConsts.DONATED_TYPE_FROZEN_EMBRYO
        ).contains(sampleType)) {
            donorSampleDto.setSampleKeyMale(generateIdClient.getSeqId().getEntity());
        }

        String hasIdNumberF = ParamUtil.getString(request, "hasIdNumberF");
        String idNo = donorSampleDto.getIdNumber();
        donorSampleDto.setIdType(patientService.judgeIdType(hasIdNumberF, idNo));

        String hasIdNumberM = ParamUtil.getString(request, "hasIdNumberM");
        String idNoM = donorSampleDto.getIdNumberMale();
        donorSampleDto.setIdTypeMale(patientService.judgeIdType(hasIdNumberM, idNoM));

        return donorSampleDto;
    }

    private List<SelectOption> getSampleFromSelOpts(HttpServletRequest request) {
        Map<String, String> stringStringMap = IaisCommonUtils.genNewHashMap();
        DataSubmissionHelper.setArPremisesMap(request).values().forEach(v -> stringStringMap.put(v.getHciCode(), v.getPremiseLabel()));
        List<SelectOption> selectOptions = DataSubmissionHelper.genOptions(stringStringMap);
        selectOptions.add(new SelectOption(DataSubmissionConsts.AR_SOURCE_OTHER, "Others"));
        return selectOptions;
    }

    private String transferNextStage(String nextStage) {
        return DataSubmissionConsts.AR_CYCLE_SFO.equals(nextStage)?DataSubmissionConsts.AR_CYCLE_EFO:nextStage;
    }
}
