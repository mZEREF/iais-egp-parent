package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.action.HcsaFileAjaxController;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.constant.intranet.user.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.organization.OrganizationConstants;
import com.ecquaria.cloud.moh.iais.common.constant.sample.DemoConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArChangeInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArCurrentInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArCycleStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleStageSelectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorSampleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.HusbandDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.ControllerHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.DsRfcHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.client.ArFeClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArCycleBatchUploadService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.PatientService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.SfoCycleUploadService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.TransferInOutCycleUploadService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.impl.IUICycleBatchUploadImpl;
import com.ecquaria.cloud.moh.iais.service.datasubmission.impl.NonPatientDonorSampleUploadServiceImpl;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
    private static final String ACTION_TYPE_PRE_UPLOAD = "preUpload";
    private static final String ACTION_TYPE_SUBMIT_UPLOAD = "submitUpload";

    private static final String CENTRE_SEL = "centreSel";
    private static final String UPLOAD_SUBMIT_TYPE= "sumbitType";
    private static final String CURRENT_PAGE_STAGE = "currentPageStage";
    private static final String CURRENT_STAGE = "currentStage";
    public static final String PATIENT_INFO_DTO = "patientInfoDto";
    public static final String SAVE_DRAFT_SUCCESS = "saveDraftSuccess";
    public static final String EXISTED_PATIENT = "existedPatient";
    public static final String HAS_CYCLE = "hasCycle";
    public static final String CYCLE_SELECT = "cycleRadio";
    public static final String SUBMIT_FLAG  = "arIuiDmSUbmmitFlag__Attr";

    private static final String AR_CYCLE_STAGE_LIST = "AR_CYCLE_STAGE_LIST";
    private static final String PAGE_SHOW_FILE = "showPatientFile";
    private static final String FILE_APPEND = "uploadFile";
    private static final String SEESION_FILES_MAP_AJAX = HcsaFileAjaxController.SEESION_FILES_MAP_AJAX + FILE_APPEND;

    @Autowired
    private GenerateIdClient generateIdClient;

    @Autowired
    private PatientService patientService;

    @Autowired
    private ArDataSubmissionService arDataSubmissionService;

    @Autowired
    private ArFeClient arFeClient;

    @Autowired
    private LicenceClient licenceClient;

    @Autowired
    private NotificationHelper notificationHelper;

    @Autowired
    private SfoCycleUploadService sfoCycleUploadService;

    @Autowired
    private NonPatientDonorSampleUploadServiceImpl nonPatientDonorSampleUploadService;

    @Autowired
    private IUICycleBatchUploadImpl iuiCycleBatchUpload;

    @Autowired
    private ArCycleBatchUploadService arCycleBatchUploadService;

    @Autowired
    private TransferInOutCycleUploadService transferInOutCycleUploadService;


    public void start(BaseProcessClass bpc) {
        log.info("----- Assisted Reproduction Submission Start -----");

        HttpServletRequest request = bpc.request;
        DataSubmissionHelper.clearSession(request);
        ParamUtil.setSessionAttr(request, PATIENT_INFO_DTO, null);
        ParamUtil.setSessionAttr(request, EXISTED_PATIENT, null);

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
        ParamUtil.setSessionAttr(bpc.request, SUBMIT_FLAG, null);
    }

    public void doARIUIDataSubmission(BaseProcessClass bpc) {
        String submitFlag = (String) ParamUtil.getSessionAttr(bpc.request, SUBMIT_FLAG);
        if (!StringUtil.isEmpty(submitFlag)) {
            throw new IaisRuntimeException("Double Submit");
        }
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
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
        boolean startNunCycle;
        if (DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO.equals(submissionType)) {
            PatientInfoDto patientInfo = genPatientByPage(request, currentSuper.getOrgId(),false);
            String previousIdentification = ParamUtil.getString(request, "previousIdentification");
            if (StringUtil.isEmpty(previousIdentification)) {
                ParamUtil.setRequestAttr(request, "jumpValidateHusband", AppConsts.YES);
                errorMap.put("previousIdentification", "GENERAL_ERR0006");
            }
            if (Boolean.TRUE.equals(patientInfo.getPatient().getPreviousIdentification()) && !patientInfo.isRetrievePrevious()) {
                errorMap.put("preIdNumber", "DS_MSG006");
            }
            ValidationResult validationResult = WebValidationHelper.validateProperty(patientInfo, "save");
            errorMap.putAll(validationResult.retrieveAll());
            if (patientInfo != null && patientInfo.getPatient() != null){
                errorMap.putAll(doValidationBirthDate(patientInfo.getPatient(),patientInfo.getHusband()));
            }
//            String hasIdNumber = ParamUtil.getString(request, "ptHasIdNumber");
//            if ("1".equals(hasIdNumber)){
//            }
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
                String nextNunCycleStage = ParamUtil.getString(request,"nextNunCycleStage");
                String cycleRadio = ParamUtil.getString(request, CYCLE_SELECT);
                String lastStatus = ParamUtil.getString(request, "lastStatus");
                boolean start = DataSubmissionHelper.startNewCycle(lastStatus);
                if (cycleRadio == null) {
                    start = true;
                }
                ParamUtil.setRequestAttr(request, CYCLE_SELECT, cycleRadio);
                String hasCycle = ParamUtil.getString(request, HAS_CYCLE);
                currentSuper.getDataSubmissionDto().setCycleStage(nextStage);
                startNewCycle = start==true && ("newCycle".equals(cycleRadio) || cycleRadio == null);
                startNunCycle = start==false && "newCycle".equals(cycleRadio);
                if (startNewCycle && StringUtil.isEmpty(nextStage)) {
                    errorMap.put("nextStage", "GENERAL_ERR0006");
                }
                if (startNunCycle && StringUtil.isEmpty(nextNunCycleStage)) {
                    errorMap.put("nextNunCycleStage", "GENERAL_ERR0006");
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
                String licenseeId = DataSubmissionHelper.getLicenseeId(request);
                if (startNewCycle){
                    selectionDto.setStage(nextStage);
                    currentSuper.setCycleDto(DataSubmissionHelper.initCycleDto(selectionDto, currentSuper.getSvcName(), hciCode, licenseeId));
                    selectionDto.setNavCurrentCycle(selectionDto.getCycle());
                } else if (startNunCycle){
                    selectionDto.setStage(nextNunCycleStage);
                    currentSuper.setCycleDto(DataSubmissionHelper.initCycleDto(selectionDto, currentSuper.getSvcName(), hciCode, licenseeId));
                } else {
                    selectionDto.setStage(nextStage);
                    currentSuper.setCycleDto(DataSubmissionHelper.initCycleDto(selectionDto, currentSuper.getSvcName(), hciCode, licenseeId));
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
            // submit donorSample
        } else if (DataSubmissionConsts.AR_TYPE_SBT_DONOR_SAMPLE.equals(submissionType)) {
            donorSamplePageAction(request, submissionType, actionType, errorMap);
            // donor Inventory
            arDataSubmissionService.getDonorInventory(currentSuper);
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
        if (ACTION_TYPE_AMEND.equals(actionType)){
            DataSubmissionDto dataSubmissionDto = licenceClient.getDataSubmissionDto(currentSuper.getPatientInfoDto().getPatient().getSubmissionId()).getEntity();
            arDataSubmissionService.prepareArRfcData(currentSuper,dataSubmissionDto.getSubmissionNo(),bpc.request);
        }
        DataSubmissionHelper.setCurrentArDataSubmission(currentSuper, request);
        ParamUtil.setSessionAttr(request, SUBMIT_FLAG, AppConsts.YES);
    }

    private static Map<String, String> doValidationBirthDate(PatientDto patientDto,HusbandDto husbandDto){
        Map<String, String> errMsg = IaisCommonUtils.genNewHashMap();
        if (StringUtil.isNotEmpty(patientDto.getBirthDate())){
            String age1 = MasterCodeUtil.getCodeDesc("PT_AGE_001");
            String age2 = MasterCodeUtil.getCodeDesc("PT_AGE_002");
            int age = Formatter.getAge(patientDto.getBirthDate());
            int husAge = Formatter.getAge(husbandDto.getBirthDate());
            if (Integer.parseInt(age1) > age || Integer.parseInt(age2) < age) {
                errMsg.put("birthDate",DataSubmissionHelper.getAgeMessage(DataSubmissionConstant.DS_SHOW_PATIENT));
            }
            if (Integer.parseInt(age1) > husAge || Integer.parseInt(age2) < husAge) {
                errMsg.put("birthDateHbd",DataSubmissionHelper.getAgeMessage(DataSubmissionConstant.DS_SHOW_HUSBAND));
            }
        }
        return errMsg;
    }

    private int getSamplesNum(DonorSampleDto donorSampleDto){
        int res = 0;
        if(StringUtil.isNumber(donorSampleDto.getTrainingNum())) {
            res += Integer.parseInt(donorSampleDto.getTrainingNum());
        }
        if(StringUtil.isNumber(donorSampleDto.getTreatNum())) {
            res += Integer.parseInt(donorSampleDto.getTreatNum());
        }
        if(StringUtil.isNumber(donorSampleDto.getDonResForTreatNum())) {
            res += Integer.parseInt(donorSampleDto.getDonResForTreatNum());
        }
        if(StringUtil.isNumber(donorSampleDto.getDonResForCurCenNotTreatNum())) {
            res += Integer.parseInt(donorSampleDto.getDonResForCurCenNotTreatNum());
        }
        return res;
    }

    public void preAmendPatient(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        arSuperDataSubmissionDto.setAppType(DataSubmissionConsts.DS_APP_TYPE_RFC);
        ParamUtil.setRequestAttr(bpc.request, "title", DataSubmissionHelper.getMainTitle(arSuperDataSubmissionDto.getAppType()));
        ParamUtil.setRequestAttr(bpc.request,"smallTitle", DataSubmissionHelper.getSmallTitle(DataSubmissionConsts.DS_AR,
                arSuperDataSubmissionDto.getAppType(), arSuperDataSubmissionDto.getSubmissionType()));
        ParamUtil.setRequestAttr(bpc.request, CURRENT_PAGE_STAGE, ACTION_TYPE_AMEND);
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
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        DataSubmissionDto dataSubmissionDto = licenceClient.getDataSubmissionDto(arSuperDataSubmissionDto.getPatientInfoDto().getPatient().getSubmissionId()).getEntity();
        dataSubmissionDto.setAmendReason(ParamUtil.getString(request, "amendReason"));
        if (DataSubmissionConsts.PATIENT_AMENDMENT_OTHER.equals(dataSubmissionDto.getAmendReason())) {
            dataSubmissionDto.setAmendReasonOther(ParamUtil.getString(request, "amendReasonOther"));
        } else {
            dataSubmissionDto.setAmendReasonOther(null);
        }

        arSuperDataSubmissionDto.setDataSubmissionDto(dataSubmissionDto);
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto, request);
        if (StringUtil.isEmpty(dataSubmissionDto.getAmendReason())) {
            errorMap.put("amendReason", "GENERAL_ERR0006");
        } else if (DataSubmissionConsts.PATIENT_AMENDMENT_OTHER.equals(dataSubmissionDto.getAmendReason()) && StringUtil.isEmpty(dataSubmissionDto.getAmendReasonOther())) {
            errorMap.put("amendReasonOther", "GENERAL_ERR0006");
        }

        if (ACTION_TYPE_RETURN.equals(actionType)){
            arSuperDataSubmissionDto.setAppType(DataSubmissionConsts.DS_APP_TYPE_NEW);
            updateSubmissionType(arSuperDataSubmissionDto, DataSubmissionConsts.AR_TYPE_SBT_CYCLE_STAGE);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_TYPE_PAGE);
            return;
        }

        ParamUtil.setSessionAttr(request, PATIENT_INFO_DTO, arSuperDataSubmissionDto.getPatientInfoDto());
        PatientInfoDto patientInfo = genPatientByPage(request, arSuperDataSubmissionDto.getOrgId(), true);
        arSuperDataSubmissionDto.setPatientInfoDto(patientInfo);

        errorMap.putAll(doValidationBirthDate(patientInfo.getPatient(),patientInfo.getHusband()));

        if (ACTION_TYPE_CONFIRM.equals(actionType)) {
            ValidationResult validationResult = WebValidationHelper.validateProperty(patientInfo, "rfc");
            errorMap.putAll(validationResult.retrieveAll());
            if (errorMap.isEmpty()) {
                ParamUtil.setRequestAttr(bpc.request, CURRENT_PAGE_STAGE, ACTION_TYPE_SUBMISSION);
                if (DataSubmissionConsts.DS_APP_TYPE_RFC.equals(arSuperDataSubmissionDto.getAppType()) && DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO.equals(arSuperDataSubmissionDto.getSubmissionType())) {
                    valRfcPatient(bpc.request, arSuperDataSubmissionDto.getPatientInfoDto());
                }
            } else {
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_TYPE_AMEND);
                return;
            }
        }

    }

    public void prepareConfirm(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, DataSubmissionConstant.PAGE_STAGE_PREVIEW);
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.PRINT_FLAG, DataSubmissionConstant.PRINT_FLAG_ART);
        // set flag, only donor sample or patient
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        ParamUtil.setRequestAttr(bpc.request, "title", DataSubmissionHelper.getMainTitle(arSuperDataSubmissionDto.getAppType()));
        ParamUtil.setRequestAttr(bpc.request,"smallTitle", DataSubmissionHelper.getSmallTitle(DataSubmissionConsts.DS_AR,
                arSuperDataSubmissionDto.getAppType(), arSuperDataSubmissionDto.getSubmissionType()));
        String submissionType = arSuperDataSubmissionDto.getSubmissionType();
        String printFlag = DataSubmissionConstant.PRINT_FLAG_ART;
        if (DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO.equals(submissionType)) {
            DsRfcHelper.handle(arSuperDataSubmissionDto.getPatientInfoDto());
            printFlag = DataSubmissionConsts.DS_PATIENT_ART;
        }
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.PRINT_FLAG, printFlag);
    }

    protected void valRfcPatient(HttpServletRequest request, PatientInfoDto patientInfoDto){
        ArSuperDataSubmissionDto arOldSuperDataSubmissionDto = DataSubmissionHelper.getOldArDataSubmission(request);
        if(arOldSuperDataSubmissionDto != null && arOldSuperDataSubmissionDto.getPatientInfoDto()!= null && arOldSuperDataSubmissionDto.getPatientInfoDto().equals(patientInfoDto)){
            ParamUtil.setRequestAttr(request, DataSubmissionConstant.RFC_NO_CHANGE_ERROR, AppConsts.YES);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE,ACTION_TYPE_AMEND);
        }
    }

    public void submission(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        PatientInfoDto patientInfoDto = arSuperDataSubmission.getPatientInfoDto();
        CycleDto cycle = arSuperDataSubmission.getCycleDto();
        if (patientInfoDto != null) {
            cycle.setPatientCode(patientInfoDto.getPatient().getPatientCode());
//            String isPatHasId = ParamUtil.getString(bpc.request, "ptHasIdNumber");
//            if ("0".equals(isPatHasId)){
//
//            }
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
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_TYPE_ACK);
        } else if (DataSubmissionConsts.AR_TYPE_SBT_DONOR_SAMPLE.equals(submissionType)) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_TYPE_ACK);
            DataSubmissionDto dataSubmissionDto = arSuperDataSubmission.getDataSubmissionDto();
            LoginContext loginContext = DataSubmissionHelper.getLoginContext(bpc.request);
            donorSampleSendEmail(dataSubmissionDto, loginContext);
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
        String emailAddress = DataSubmissionHelper.getEmailAddrsByRoleIdsAndLicenseeId(bpc.request, MsgTemplateConstants.MSG_TEMPLATE_AR_INCOMPLETE_CYCLE_EMAIL);
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.EMAIL_ADDRESS, emailAddress);
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.SUBMITTED_BY, DataSubmissionHelper.getLoginContext(bpc.request).getUserName());
    }

    public void pageConfirmAction(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setRequestAttr(request, CURRENT_STAGE, ACTION_TYPE_CONFIRM);
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap(1);
        String actionType = ParamUtil.getString(request, DataSubmissionConstant.CRUD_TYPE);
        ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, actionType);
        ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(request);
        if (ACTION_TYPE_SUBMISSION.equals(actionType)) {
            String[] declaration = ParamUtil.getStrings(request, "declaration");
            if (declaration == null || declaration.length == 0) {
                errorMap.put("declaration", "GENERAL_ERR0006");
            }
            DataSubmissionDto dataSubmissionDto = arSuperDataSubmission.getDataSubmissionDto();
            if (declaration != null && declaration.length > 0) {
                dataSubmissionDto.setDeclaration(declaration[0]);
            } else {
                dataSubmissionDto.setDeclaration(null);
            }
        }else if (ACTION_TYPE_RETURN.equals(actionType) && DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO.equals(arSuperDataSubmission.getSubmissionType()) && DataSubmissionConsts.DS_APP_TYPE_RFC.equals(arSuperDataSubmission.getAppType())){
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_TYPE_AMEND);
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

    private void setDonorInv(ArSuperDataSubmissionDto arSuperDataSubmissionDto) {
        DonorSampleDto donorSampleDto = arSuperDataSubmissionDto.getDonorSampleDto();
        int freshOocyteNum = 0;
        int frozenOocyteNum = 0;
        int frozenEmbryoNum = 0;
        int frozenSpermNum = 0;
        int freshSpermNum = 0;

        if (DataSubmissionConsts.DONATED_TYPE_FRESH_OOCYTE.equals(donorSampleDto.getSampleType())) {
            freshOocyteNum += getSamplesNum(donorSampleDto);
        } else if (DataSubmissionConsts.DONATED_TYPE_FROZEN_OOCYTE.equals(donorSampleDto.getSampleType())) {
            frozenOocyteNum += getSamplesNum(donorSampleDto);
        } else if (DataSubmissionConsts.DONATED_TYPE_FROZEN_EMBRYO.equals(donorSampleDto.getSampleType())) {
            frozenEmbryoNum += getSamplesNum(donorSampleDto);
        } else if (DataSubmissionConsts.DONATED_TYPE_FROZEN_SPERM.equals(donorSampleDto.getSampleType())){
            frozenSpermNum += getSamplesNum(donorSampleDto);
        } else  if (DataSubmissionConsts.DONATED_TYPE_FRESH_SPERM.equals(donorSampleDto.getSampleType())){
            freshSpermNum += getSamplesNum(donorSampleDto);
        }
        ArChangeInventoryDto arChangeInventoryDto = arSuperDataSubmissionDto.getArChangeInventoryDto();
        if (DataSubmissionConsts.DONATED_TYPE_FROZEN_EMBRYO.equals(donorSampleDto.getSampleType())) {
            ArChangeInventoryDto secondArChangeInventoryDto = arSuperDataSubmissionDto.getSecondArChangeInventoryDto();
            if (secondArChangeInventoryDto == null) {
                secondArChangeInventoryDto = new ArChangeInventoryDto();
            }
            int secondFrozenEmbryoNum = 0;
            int secondFrozenSpermNum = 0;
            if (DataSubmissionConsts.DONATED_TYPE_FROZEN_EMBRYO.equals(donorSampleDto.getSampleType())) {
                secondFrozenEmbryoNum += getSamplesNum(donorSampleDto);
            } else if (DataSubmissionConsts.DONATED_TYPE_FROZEN_SPERM.equals(donorSampleDto.getSampleType())){
                secondFrozenSpermNum += getSamplesNum(donorSampleDto);
            }
            secondArChangeInventoryDto.setFrozenEmbryoNum(secondFrozenEmbryoNum);
            secondArChangeInventoryDto.setFrozenSpermNum(secondFrozenSpermNum);
            arSuperDataSubmissionDto.setSecondArChangeInventoryDto(secondArChangeInventoryDto);
        }
        if (arChangeInventoryDto == null){
            arChangeInventoryDto = new ArChangeInventoryDto();
        }
        arChangeInventoryDto.setFreshOocyteNum(freshOocyteNum);
        arChangeInventoryDto.setFrozenOocyteNum(frozenOocyteNum);
        arChangeInventoryDto.setFrozenEmbryoNum(frozenEmbryoNum);
        arChangeInventoryDto.setFrozenSpermNum(frozenSpermNum);
        arChangeInventoryDto.setFreshSpermNum(freshSpermNum);
        arSuperDataSubmissionDto.setArChangeInventoryDto(arChangeInventoryDto);
    }

    private void donorSamplePageAction(HttpServletRequest request, String submissionType, String actionType, Map<String, String> errorMap) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        DonorSampleDto donorSampleDto = genDonorSampleDtoByPage(request);
        arSuperDataSubmissionDto.setDonorSampleDto(donorSampleDto);
        setDonorInv(arSuperDataSubmissionDto);
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
            if (premisesMap.size() == 1) {
                premisesMap.forEach((k, v) -> {
                    ParamUtil.setRequestAttr(bpc.request,"localPremisesLabel", v.getPremiseLabel());
                });
            }
            List<SelectOption> arCenterSelOpts = DataSubmissionHelper.genPremisesOptions(premisesMap);
            ParamUtil.setRequestAttr(bpc.request, "premisesOpts", arCenterSelOpts);
        }
        List<SelectOption> nricFinTypeSelOpts = MasterCodeUtil.retrieveByCategory(MasterCodeUtil.CATE_ID_DS_ID_TYPE).stream()
                .filter(it -> !it.getCode().equals(DataSubmissionConsts.AR_ID_TYPE_PASSPORT_NO))
                .map(it -> new SelectOption(it.getCode(), it.getDescription()))
                .collect(Collectors.toList());
        ParamUtil.setRequestAttr(bpc.request, "nricFinTypeSelOpts", nricFinTypeSelOpts);

        List<SelectOption> offCycleOpts = MasterCodeUtil.retrieveOptionsByCodes(
                DataSubmissionHelper.getAllOffCycleStage().toArray(new String[]{}));
        ParamUtil.setRequestAttr(bpc.request, "offCycleOps", offCycleOpts);

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
        String orgId = currentSuper.getOrgId();
        String userId = "";
        LoginContext loginContext = DataSubmissionHelper.getLoginContext(request);
        if (loginContext != null) {
            orgId = loginContext.getOrgId();
            userId = loginContext.getUserId();
        }
        if ("resume".equals(actionValue)) {
            ArSuperDataSubmissionDto arSuperDataSubmissionDtoDraft = null;
            if (selectionDto != null) {
                arSuperDataSubmissionDtoDraft = arDataSubmissionService.getArSuperDataSubmissionDtoDraftById(
                        currentSuper.getDraftId());
                ParamUtil.setRequestAttr(request, DataSubmissionConstant.CRUD_ACTION_TYPE_CT, transferNextStage(selectionDto.getStage()));
            } else if (DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO.equals(currentSuper.getSubmissionType())){

                String isPatHasId = ParamUtil.getString(request, "ptHasIdNumber");
                ParamUtil.setRequestAttr(request,"ptHasIdNumber",isPatHasId);
                String identityNo = ParamUtil.getString(request, "identityNo");
                String idType = patientService.judgeIdType(isPatHasId,identityNo);
                ArSuperDataSubmissionDto dataSubmissionDraft = arDataSubmissionService.getArPatientSubmissionDraftByConds(orgId, DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO, idType, identityNo, userId);
                arSuperDataSubmissionDtoDraft = arDataSubmissionService.getArSuperDataSubmissionDtoDraftById(dataSubmissionDraft.getDraftId());
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
            } else if (DataSubmissionConsts.AR_TYPE_SBT_DONOR_SAMPLE.equals(currentSuper.getSubmissionType())) {
                ArSuperDataSubmissionDto dataSubmissionDraft = arDataSubmissionService.getArSuperDataSubmissionDtoDraftByConds(orgId,DataSubmissionConsts.AR_TYPE_SBT_DONOR_SAMPLE,null,userId);
                arSuperDataSubmissionDtoDraft = arDataSubmissionService.getArSuperDataSubmissionDtoDraftById(dataSubmissionDraft.getDraftId());
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
            }
            if (arSuperDataSubmissionDtoDraft != null) {
                DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDtoDraft, request);
            }
            ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, actionType);
            return true;
        } else if ("delete".equals(actionValue)) {
            if (selectionDto != null) {
                arDataSubmissionService.deleteArSuperDataSubmissionDtoDraftByConds(selectionDto.getPatientIdType(),
                        selectionDto.getPatientIdNumber(), selectionDto.getPatientNationality(),
                        orgId, hciCode);
                ParamUtil.setRequestAttr(request, DataSubmissionConstant.CRUD_ACTION_TYPE_CT, transferNextStage(selectionDto.getStage()));
            } else if (DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO.equals(currentSuper.getSubmissionType())){
                String isPatHasId = ParamUtil.getRequestString(request, "ptHasIdNumber");
                ParamUtil.setRequestAttr(request,"ptHasIdNumber",isPatHasId);
                String idNo = ParamUtil.getRequestString(request, "identityNo");
                String idType = patientService.judgeIdType(isPatHasId,idNo);
                PatientInfoDto patientInfoDto = new PatientInfoDto();
                PatientDto patientDto = new PatientDto();
                patientDto.setIdType(idType);
                patientDto.setIdNumber(idNo);
                patientInfoDto.setPatient(patientDto);
                currentSuper.setPatientInfoDto(patientInfoDto);
                ArSuperDataSubmissionDto dataSubmissionDraft = arDataSubmissionService.getArPatientSubmissionDraftByConds(orgId, DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO, idType, idNo, userId);
                arFeClient.deleteArSuperDataSubmissionDtoDraftByDraftNo(dataSubmissionDraft.getDraftNo());
            } else if (DataSubmissionConsts.AR_TYPE_SBT_DONOR_SAMPLE.equals(currentSuper.getSubmissionType())){
                ArSuperDataSubmissionDto dataSubmissionDraft = arDataSubmissionService.getArSuperDataSubmissionDtoDraftByConds(orgId,DataSubmissionConsts.AR_TYPE_SBT_DONOR_SAMPLE,null,userId);
                arFeClient.deleteArSuperDataSubmissionDtoDraftByDraftNo(dataSubmissionDraft.getDraftNo());
            }

            currentSuper.setDraftNo(null);
            currentSuper.setDraftId(null);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, actionType);
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
                    .filter(arDraft -> arDraft.getSelectionDto().getStage()!= null&&arDraft.getSelectionDto().getStage().equals(selectionDto.getStage()))
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

        String ptHasIdNumber = ParamUtil.getString(request, "ptHasIdNumber");
        if ("1".equals(ptHasIdNumber) && patient != null){
            String birthDate = ParamUtil.getString(request, "birthDate");
            patient.setBirthDate(birthDate);
        }else if ("0".equals(ptHasIdNumber) && patient != null){
            String birthDate = ParamUtil.getString(request, "dateBirth");
            patient.setBirthDate(birthDate);
        }
        if (isAmend) {
            //amend just replace field need filled
            PatientDto oldPatient = patientInfo.getPatient();
            oldPatient.setIdType(patient.getIdType());
            oldPatient.setIdNumber(patient.getIdNumber());
            oldPatient.setName(patient.getName());
            oldPatient.setBirthDate(patient.getBirthDate());
            oldPatient.setNationality(patient.getNationality());
            oldPatient.setEthnicGroup(patient.getEthnicGroup());
            oldPatient.setEthnicGroupOther(patient.getEthnicGroupOther());
            oldPatient.setPreviousIdentification(patient.getPreviousIdentification());
            patient = oldPatient;


            HusbandDto oldHusband = patientInfo.getHusband();
            oldHusband.setIdType(husband.getIdType());
            oldHusband.setIdNumber(husband.getIdNumber());
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
            ParamUtil.setRequestAttr(request,"ptHasIdNumber",hasIdNumber);
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
        if (!isAmend){
            patient.setPatientCode(patientService.getPatientCode(patientCode));
        }


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
        String femaleIdType = ParamUtil.getString(request, "hasIdNumberF");
        String maleIdType = ParamUtil.getString(request, "hasIdNumberM");
        donorSampleDto.setIdType(femaleIdType);
        donorSampleDto.setIdTypeMale(maleIdType);
        Map<String, PremisesDto> premisesMap = DataSubmissionHelper.setArPremisesMap(request);
        if (premisesMap.size() == 1) {
            premisesMap.forEach((k, v) -> {
               donorSampleDto.setSampleFromHciCode(v.getId());
            });
        } else {
            String localDsCenter = ParamUtil.getRequestString(request, "centreSel");
            donorSampleDto.setSampleFromHciCode(localDsCenter);
        }
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
                DataSubmissionConsts.DONATED_TYPE_FROZEN_EMBRYO,
                DataSubmissionConsts.DONATED_TYPE_FRESH_SPERM
        ).contains(sampleType)) {
            donorSampleDto.setSampleKeyMale(generateIdClient.getSeqId().getEntity());
        }

        String hasIdNumberF = ParamUtil.getString(request, "hasIdNumberF");
        ParamUtil.setSessionAttr(request,"hasIdNumberF",hasIdNumberF);
        String idNo = donorSampleDto.getIdNumber();
        if (conformToDonorNic(idNo)){
            donorSampleDto.setIdType(patientService.judgeIdType(hasIdNumberF, idNo));
        }

        String hasIdNumberM = ParamUtil.getString(request, "hasIdNumberM");
        ParamUtil.setSessionAttr(request,"hasIdNumberM",hasIdNumberM);
        String idNoM = donorSampleDto.getIdNumberMale();
        if (conformToDonorNic(idNoM)){
            donorSampleDto.setIdTypeMale(patientService.judgeIdType(hasIdNumberM, idNoM));
        }

        return donorSampleDto;
    }

    private static  Boolean conformToDonorNic(String idNo){
        Boolean result = Boolean.FALSE;
        if (StringUtil.isNotEmpty(idNo)){
            boolean b = SgNoValidator.validateFin(idNo);
            boolean b1 = SgNoValidator.validateNric(idNo);
            if (b || b1) {
                result = Boolean.TRUE;
            }
        }
        return result;
    }

    private String transferNextStage(String nextStage) {
        return DataSubmissionConsts.AR_CYCLE_SFO.equals(nextStage)?DataSubmissionConsts.AR_CYCLE_EFO:nextStage;
    }

    private void donorSampleSendEmail(DataSubmissionDto dataSubmissionDto, LoginContext loginContext) {
        EmailParam emailParamEmail = new EmailParam();
        Map<String, Object> msgSubjectMap = IaisCommonUtils.genNewHashMap();
        String requestDate = Formatter.formatDate(new Date());
        msgSubjectMap.put("submissionId", dataSubmissionDto.getSubmissionNo());
        msgSubjectMap.put("requestDate", requestDate);
        msgSubjectMap.put("officer_name", "officer_name");
        emailParamEmail.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_AR_SUBMIT_EMAIL);
        emailParamEmail.setTemplateContent(msgSubjectMap);
        emailParamEmail.setQueryCode(IaisEGPHelper.generateRandomString(26));
        emailParamEmail.setReqRefNum(IaisEGPHelper.generateRandomString(26));
        emailParamEmail.setServiceTypes(DataSubmissionConsts.DS_AR_NEW);
        emailParamEmail.setRefIdType(NotificationHelper.RECEIPT_TYPE_LICENSEE_ID);
        emailParamEmail.setRefId(loginContext.getLicenseeId());
        notificationHelper.sendNotification(emailParamEmail);
    }

    public void preBatchUpload(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("----- DoPreview -----"));
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        if (arSuperDataSubmissionDto == null) {
            arSuperDataSubmissionDto = new ArSuperDataSubmissionDto();
        }
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto, bpc.request);
        String crudype = ParamUtil.getString(bpc.request, DataSubmissionConstant.CRUD_TYPE);
        String batchUploadType = ParamUtil.getString(bpc.request, UPLOAD_SUBMIT_TYPE);
        String isUploadFile = ParamUtil.getString(bpc.request, DemoConstants.CRUD_ACTION_VALUE);
        arSuperDataSubmissionDto.setBatchUploadType(batchUploadType);

        if (StringUtil.isIn(crudype, new String[]{"return", "back"})) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "return");
            clearSession(bpc.request);
            return;
        }

        // todo validation by batchUploadType
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        List<ArCycleStageDto> cycleStageDtoDtos = null;
        int fileItemSize = 0;
        switch (batchUploadType){
            case DataSubmissionConsts.AR_CYCLE_UPLOAD:
                errorMap = arCycleBatchUploadService.preBatchUpload(bpc, errorMap);
                break;

            case DataSubmissionConsts.SFO_CYCLE_UPLOAD:
                errorMap = sfoCycleUploadService.getSfoCycleUploadFile(bpc.request, errorMap, fileItemSize);
                break;
            case DataSubmissionConsts.TRANSFER_IN_OUT_CYCLE_UPLOAD:
                errorMap = transferInOutCycleUploadService.getTransferInOutUploadFile(bpc.request, errorMap,fileItemSize);
                break;
            case DataSubmissionConsts.DONOR_CYCLE_UPLOAD:
                errorMap = nonPatientDonorSampleUploadService.getErrorMap(bpc.request);
                break;
            case DataSubmissionConsts.IUI_CYCLE_UPLOAD:
                errorMap = iuiCycleBatchUpload.getErrorMap(bpc.request);
                break;

        }

        if (!errorMap.isEmpty()) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_TYPE_PAGE);
            clearSession(bpc.request);
            return;
        } else {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_TYPE_SUBMIT_UPLOAD);
        }
    }

    public void submitBatchUpload(BaseProcessClass bpc) {
        // todo submission by batchUploadType
        String declaration = ParamUtil.getString(bpc.request, "declaration");
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        Boolean submitSfoCycleFile = (Boolean) ParamUtil.getRequestAttr(bpc.request, "isSfoCycleFile");
        Boolean submitTransferInOutCycleFile = (Boolean) ParamUtil.getRequestAttr(bpc.request, "isTransferInOutCycleFile");
        Boolean submitDonorSampleFile = (Boolean) ParamUtil.getRequestAttr(bpc.request, "isDonorSampleFile");
        if (Boolean.TRUE.equals(submitSfoCycleFile)){
            sfoCycleUploadService.saveSfoCycleUploadFile(bpc.request, arSuperDataSubmissionDto);
        }
        if (Boolean.TRUE.equals(submitTransferInOutCycleFile)){
            transferInOutCycleUploadService.saveTransferInOutCycleUploadFile(bpc.request, arSuperDataSubmissionDto);
        }
        if (Boolean.TRUE.equals(submitDonorSampleFile)){
            nonPatientDonorSampleUploadService.saveNonPatientDonorSampleFile(bpc.request, arSuperDataSubmissionDto);
        }
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_TYPE_ACK);
    }


    private void clearSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute(SEESION_FILES_MAP_AJAX);
//        session.removeAttribute(SOVENOR_INVENTORY_LIST);
        session.removeAttribute(PAGE_SHOW_FILE);
        session.removeAttribute(DataSubmissionConstant.AR_DATA_LIST);
    }
}
