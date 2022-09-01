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
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDonorSampleService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.PatientService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
import org.springframework.util.ObjectUtils;
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
    private static final String CURRENT_STAGE = "currentStage";
    public static final String PATIENT_INFO_DTO = "patientInfoDto";


    @Autowired
    private PatientService patientService;

    @Autowired
    private ArDonorSampleService arDonorSampleService;

    @Autowired
    private ArDataSubmissionService arDataSubmissionService;


    public void start(BaseProcessClass bpc) {
        log.info("----- Assisted Reproduction Submission Start -----");

        HttpSession session = bpc.request.getSession();
        session.removeAttribute(DataSubmissionConstant.AR_PREMISES_MAP);
        session.removeAttribute(DataSubmissionConstant.AR_DATA_SUBMISSION);
        session.removeAttribute(PATIENT_INFO_DTO);
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
        ParamUtil.setRequestAttr(bpc.request, CURRENT_STAGE, ACTION_TYPE_PAGE);
        //prepare ar center or it's err msg
        Map<String, PremisesDto> premisesMap = DataSubmissionHelper.setArPremisesMap(bpc.request);
        if (premisesMap.isEmpty()) {
            Map<String, String> errorMap = IaisCommonUtils.genNewHashMap(2);
            errorMap.put("noArLicences", "There are no active Assisted Reproduction licences");
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
        } else {
            List<SelectOption> arCenterSelOpts = DataSubmissionHelper.genPremisesOptions(premisesMap);
            ParamUtil.setRequestAttr(bpc.request, "premisesOpts", arCenterSelOpts);
            ParamUtil.setRequestAttr(bpc.request, "donorSampleFromSelOpts", arDonorSampleService.getSampleFromSelOpts(bpc.request));
        }
    }

    public void doARIUIDataSubmission(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String actionType = ParamUtil.getString(request, DataSubmissionConstant.CRUD_TYPE);
        ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, actionType);
        if (ACTION_TYPE_RETURN.equals(actionType)) {
            return;
        }
        String submissionType = ParamUtil.getString(request, "submissionType");
        String submissionMethod = ParamUtil.getString(request, "submissionMethod");

        //submissionMethod: Form Entry/Batch Upload
        //submissionType: Yes(donor sample)/No(patient)
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        if (!StringUtils.hasLength(submissionMethod)) {
            errorMap.put("submissionMethod", "GENERAL_ERR0006");
        }

        if (!StringUtils.hasLength(submissionType)) {
            errorMap.put("submissionType", "GENERAL_ERR0006");
        }
        String isRegister = ParamUtil.getString(request, "registeredPatient");
        submissionType = AppConsts.YES.equals(isRegister) ? submissionType : DataSubmissionConsts.AR_TYPE_SBT_CYCLE_STAGE;
        //prepare ArSubmissionDto for donor sample or patient or cycle
        ArSuperDataSubmissionDto currentSuper = DataSubmissionHelper.getCurrentArDataSubmission(request);
        if (currentSuper == null) {
            currentSuper = prepareArSuperDataSubmissionDto(request, submissionType, submissionMethod, errorMap);
        }
        DataSubmissionHelper.setCurrentArDataSubmission(currentSuper, request);

        // from draft confirm
        String hciCode = Optional.ofNullable(currentSuper.getPremisesDto())
                .map(PremisesDto::getHciCode)
                .orElse("");
        String actionValue = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_VALUE);
        CycleStageSelectionDto selectionDto = currentSuper.getSelectionDto();
        if ("resume".equals(actionValue)) {
            ArSuperDataSubmissionDto arSuperDataSubmissionDtoDraft = arDataSubmissionService.getArSuperDataSubmissionDtoDraftById(
                    currentSuper.getDraftId());
            if (arSuperDataSubmissionDtoDraft != null) {
                DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDtoDraft, request);
            }
            ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, actionType);
            ParamUtil.setRequestAttr(request, DataSubmissionConstant.CRUD_ACTION_TYPE_CT, selectionDto.getStage());
            return;
        } else if ("delete".equals(actionValue)) {
            String orgId = currentSuper.getOrgId();
            arDataSubmissionService.deleteArSuperDataSubmissionDtoDraftByConds(selectionDto.getPatientIdType(),
                    selectionDto.getPatientIdNumber(), selectionDto.getPatientNationality(),
                    orgId, hciCode);
            currentSuper.setDraftNo(null);
            currentSuper.setDraftId(null);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, actionType);
            ParamUtil.setRequestAttr(request, DataSubmissionConstant.CRUD_ACTION_TYPE_CT, selectionDto.getStage());
            return;
        }

        boolean hasNewCycle = false;
        if (DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO.equals(submissionType)) {
            PatientInfoDto patientInfo = (PatientInfoDto) ParamUtil.getSessionAttr(request, PATIENT_INFO_DTO);
            if (Objects.isNull(patientInfo)) {
                patientInfo = prepareSavePatient(bpc, currentSuper.getOrgId(), currentSuper.getAppType(), false);
            }
            currentSuper.setPatientInfoDto(patientInfo);
        } else if (DataSubmissionConsts.AR_TYPE_SBT_CYCLE_STAGE.equals(submissionType)) {
            selectionDto = (CycleStageSelectionDto) ParamUtil.getSessionAttr(request, "selectionDto");
            PatientInfoDto patientInfoDto = (PatientInfoDto) ParamUtil.getSessionAttr(request, PATIENT_INFO_DTO);
            String nextStage = ParamUtil.getString(request, "nextStage");
            String cycleRadio = ParamUtil.getString(request, "cycleRadio");
            String hasCycle = ParamUtil.getString(request, "hasCycle");
            currentSuper.getDataSubmissionDto().setCycleStage(nextStage);
            selectionDto.setStage(nextStage);
            currentSuper.setPatientInfoDto(patientInfoDto);
            ParamUtil.setRequestAttr(request, "cycleRadio", cycleRadio);
            hasNewCycle = "N".equals(hasCycle) || "newCycle".equals(cycleRadio);
            if ( hasNewCycle && StringUtil.isEmpty(nextStage)) {
                errorMap.put("nextStage", "GENERAL_ERR0006");
            }
            if (StringUtil.isEmpty(cycleRadio)) {
                errorMap.put("cycleRadio", "GENERAL_ERR0006");
            }
            if (errorMap.isEmpty()) {
                if (!"newCycle".equals(cycleRadio)) {
                    selectionDto = arDataSubmissionService.getCycleStageSelectionDtoByConds(patientInfoDto.getPatient().getPatientCode(),
                            hciCode, cycleRadio);
                    selectionDto.setHciCode(hciCode);
                    if (StringUtil.isNotEmpty(selectionDto.getLastStage())) {
                        selectionDto.setLastStageDesc(MasterCodeUtil.getCodeDesc(selectionDto.getLastStage()));
                    } else {
                        selectionDto.setLastStageDesc("-");
                    }
                }
            }
            currentSuper.setSelectionDto(selectionDto);
            currentSuper.setCycleDto(DataSubmissionHelper.initCycleDto(selectionDto, currentSuper.getSvcName(), hciCode, DataSubmissionHelper.getLicenseeId(request)));
            ParamUtil.setRequestAttr(request, DataSubmissionConstant.CRUD_ACTION_TYPE_CT, nextStage);
        } else if (DataSubmissionConsts.AR_TYPE_SBT_DONOR_SAMPLE.equals(submissionType)) {
            DonorSampleDto donorSampleDto = arDonorSampleService.genDonorSampleDtoByPage(request);
            currentSuper.setDonorSampleDto(donorSampleDto);
            ValidationResult validationResult = WebValidationHelper.validateProperty(donorSampleDto, "save");
            errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_TYPE_CONFIRM);
        }

        if (ACTION_TYPE_SUBMISSION.equals(actionType)) {
            if (!errorMap.isEmpty()) {
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                ParamUtil.setRequestAttr(request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(request, IntranetUserConstant.CRUD_ACTION_TYPE, ACTION_TYPE_PAGE);
            } else if (DataSubmissionConsts.AR_TYPE_SBT_CYCLE_STAGE.equals(submissionType)) {
                // start new stage
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_TYPE_STAGE);
                if (hasNewCycle && !haveStageDraft(request)) {
                    ParamUtil.setRequestAttr(bpc.request, JUMP_ACTION_TYPE, "jump");
                }
            } else if (DataSubmissionConsts.AR_TYPE_SBT_DONOR_SAMPLE.equals(submissionType)) {
                // submit donor sample
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_TYPE_CONFIRM);
            }
        }
        DataSubmissionHelper.setCurrentArDataSubmission(currentSuper, request);
    }


    public void preAmendPatient(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, CURRENT_STAGE, ACTION_TYPE_AMEND);
    }

    public void doAmendPatient(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, CURRENT_STAGE, ACTION_TYPE_AMEND);
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        arSuperDataSubmissionDto.setAppType(DataSubmissionConsts.DS_APP_TYPE_RFC);
        PatientInfoDto patientInfoDto = prepareSavePatient(bpc, DataSubmissionConsts.DS_APP_TYPE_RFC, true);
        arSuperDataSubmissionDto.setPatientInfoDto(patientInfoDto);
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto, bpc.request);
    }

    public void prepareConfirm(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, CURRENT_STAGE, ACTION_TYPE_CONFIRM);
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.PRINT_FLAG, DataSubmissionConsts.DS_PATIENT_ART);
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
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, CommonDelegator.ACTION_TYPE_PAGE);
        } else if (DataSubmissionConsts.AR_TYPE_SBT_DONOR_SAMPLE.equals(submissionType)) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_TYPE_ACK);
        }
    }

    public void saveDraft(BaseProcessClass bpc) {
        String currentStage = (String) ParamUtil.getRequestAttr(bpc.request, CURRENT_STAGE);
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, currentStage);
        // TODO
    }

    public void doBack(BaseProcessClass bpc) throws IOException {
        log.info(StringUtil.changeForLog("The doReturn start ..."));
        ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        String currentStage = ParamUtil.getRequestString(bpc.request, CURRENT_STAGE);
        String uri = InboxConst.URL_MAIN_WEB_MODULE + "MohInternetInbox";

        String submissionType = arSuperDataSubmission.getSubmissionType();
        if (DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO.equals(submissionType)) {
            if (ACTION_TYPE_AMEND.equals(currentStage)) {
                uri = InboxConst.URL_LICENCE_WEB_MODULE + "MohARAndIUIDataSubmission/1/PreARIUIDataSubmission";
            } else if(CommonDelegator.ACTION_TYPE_PAGE.equals(currentStage)){
                 uri = InboxConst.URL_MAIN_WEB_MODULE + "MohInternetInbox";
            }
        } else if(DataSubmissionConsts.AR_TYPE_SBT_DONOR_SAMPLE.equals(submissionType)){
            uri = InboxConst.URL_MAIN_WEB_MODULE + "MohInternetInbox";
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
        String emailAddress = DataSubmissionHelper.getEmailAddrsByRoleIdsAndLicenseeId(bpc.request, Collections.singletonList(RoleConsts.USER_ROLE_DS_AR));
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.EMAIL_ADDRESS, emailAddress);
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.SUBMITTED_BY, DataSubmissionHelper.getLoginContext(bpc.request).getUserName());
    }

    public PatientInfoDto prepareSavePatient(BaseProcessClass bpc, String appType, boolean isAmend) {
        return prepareSavePatient(bpc, null, appType, isAmend);
    }

    public void pageConfirmAction(BaseProcessClass bpc) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap(1);
        String crud_action_type = ParamUtil.getString(bpc.request, DataSubmissionConstant.CRUD_TYPE);
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, crud_action_type);
        if (ACTION_TYPE_SUBMISSION.equals(crud_action_type)) {
            String[] declaration = ParamUtil.getStrings(bpc.request, "declaration");
            if (declaration == null || declaration.length == 0) {
                errorMap.put("declaration", "GENERAL_ERR0006");
            }
            ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
            DataSubmissionDto dataSubmissionDto = arSuperDataSubmission.getDataSubmissionDto();
            if (declaration != null && declaration.length > 0) {
                dataSubmissionDto.setDeclaration(declaration[0]);
            } else {
                dataSubmissionDto.setDeclaration(null);
            }
        }

        if (!errorMap.isEmpty()) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_TYPE_CONFIRM);
        }
    }

    public void init(BaseProcessClass bpc) {
    }

    public void prepareStage(BaseProcessClass bpc) {
        // clear crud_action_type, because sub stage need switch
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, null);
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


    /**
     * @param isAmend : decide amend patient or not - just using for amending patient's details
     **/
    public PatientInfoDto prepareSavePatient(BaseProcessClass bpc, String orgId, String appType, boolean isAmend) {
        PatientInfoDto patientInfo = new PatientInfoDto();
        if (isAmend) {
            patientInfo = (PatientInfoDto) ParamUtil.getSessionAttr(bpc.request, PATIENT_INFO_DTO);
        }

        PatientDto patient = ControllerHelper.get(bpc.request, PatientDto.class);
        HusbandDto husband = ControllerHelper.get(bpc.request, HusbandDto.class, "Hbd");


        //next is difference of handling about new and amend
        if (isAmend) {
            //amend just replace field need filled
            PatientDto oldPatient = patientInfo.getPatient();
           oldPatient.setName(patient.getName());
           oldPatient.setBirthDate(patient.getBirthDate());
           oldPatient.setNationality(patient.getNationality());
           String newEthnicGroup = patient.getEthnicGroup();
           oldPatient.setEthnicGroup(newEthnicGroup);
           if(DataSubmissionConsts.EFO_REASON_OTHERS.equals(newEthnicGroup)){
               oldPatient.setEthnicGroupOther(patient.getEthnicGroupOther());
           }
           patient = oldPatient;


           HusbandDto oldHusband = patientInfo.getHusband();
            oldHusband.setName(husband.getName());
            oldHusband.setBirthDate(husband.getBirthDate());
            oldHusband.setNationality(husband.getNationality());
            String newEthnicGroup1 = husband.getEthnicGroup();
            oldHusband.setEthnicGroup(newEthnicGroup1);
            if(DataSubmissionConsts.EFO_REASON_OTHERS.equals(newEthnicGroup)){
                oldHusband.setEthnicGroupOther(husband.getEthnicGroupOther());
            }
            husband = oldHusband;

        } else {
            String identityNo = ParamUtil.getString(bpc.request,"identityNo");
            String hasIdNumber = ParamUtil.getString(bpc.request,"hasIdNumber");
            if("N".equals(hasIdNumber)){
                patient.setIdType(DataSubmissionConsts.AR_ID_TYPE_PASSPORT_NO);
            }
            patient.setIdNumber(identityNo);
            //this code is used temporarily,may update in future
            patient.setOrgId(orgId);

            patientInfo.setIsPreviousIdentification(patient.isPreviousIdentification() ? IaisEGPConstant.YES : IaisEGPConstant.NO);
        }

        patientInfo.setAppType(appType);
        DsRfcHelper.prepare(patient);
        patientInfo.setPatient(patient);


        DsRfcHelper.prepare(husband);
        patientInfo.setHusband(husband);


        //previous patient info
        String patientCode = null;
        if (patient.isPreviousIdentification()) {
            patientInfo.setRetrievePrevious(true);
            PatientDto previous = ControllerHelper.get(bpc.request, PatientDto.class, "pre", "");

            PatientDto db = retrievePrePatient(patient, previous);

            if (db != null && !StringUtils.isEmpty(db.getId())) {
                previous = db;
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
        PatientDto db = new PatientDto();
        //get id number type
        String idDiff = checkIdentityNoType(previous.getIdNumber());

        //idType is marked as a list due to this special type NRIC owns two type - PINK IC or BLUE IC
        List<String> idTypes = new ArrayList<>(2);
        if (OrganizationConstants.ID_TYPE_NRIC.equals(idDiff)) {
            idTypes.add(DataSubmissionConsts.AR_ID_TYPE_PINK_IC);
            idTypes.add(DataSubmissionConsts.AR_ID_TYPE_BLUE_IC);
        }else if(OrganizationConstants.ID_TYPE_FIN.equals(idDiff)){
            idTypes.add(DataSubmissionConsts.AR_ID_TYPE_FIN_NO);
        }else{
            idTypes.add(DataSubmissionConsts.AR_ID_TYPE_PASSPORT_NO);
        }

        if(idTypes.size() == 1){
            db = patientService.getActiveArPatientByConds(idTypes.get(0), previous.getIdNumber(),
                    previous.getNationality(), patient.getOrgId());
        }
        //no way to tell different types of identity number,so search two times
        if(idTypes.size() > 1){
            String firstIdType = idTypes.get(0);
            String secondIdType = idTypes.get(1);
            PatientDto nric= patientService.getActiveArPatientByConds(firstIdType, previous.getIdNumber(), previous.getNationality(), patient.getOrgId());
            db = ObjectUtils.isEmpty(nric)?
                    (patientService.getActiveArPatientByConds(secondIdType, previous.getIdNumber(), previous.getNationality(), patient.getOrgId()))
                    :nric;
        }
        return db;
    }


    private ArSuperDataSubmissionDto prepareArSuperDataSubmissionDto(HttpServletRequest request, String submissionType, String submissionMethod, Map<String, String> errorMap) {
        // check premises
        HttpSession session = request.getSession();
        String centreSel = ParamUtil.getString(request, CENTRE_SEL);
        Map<String, PremisesDto> premisesMap = (Map<String, PremisesDto>) ParamUtil.getSessionAttr(request, DataSubmissionConstant.AR_PREMISES_MAP);
        PremisesDto premisesDto = (PremisesDto) ParamUtil.getSessionAttr(request, DataSubmissionConstant.AR_PREMISES);
        if (!StringUtils.isEmpty(centreSel)) {
            if (premisesMap != null) {
                premisesDto = premisesMap.get(centreSel);
            }
            if (premisesDto == null) {
                errorMap.put(CENTRE_SEL, "GENERAL_ERR0049");
            }
        } else if (premisesMap != null && premisesMap.size() > 1) {
            errorMap.put(CENTRE_SEL, "GENERAL_ERR0006");
        } else if (premisesDto == null) {
            errorMap.put(CENTRE_SEL, "There are no active Assisted Reproduction licences");
        }

        ArSuperDataSubmissionDto currentSuper = DataSubmissionHelper.getCurrentArDataSubmission(request);
        boolean reNew = isNeedReNew(currentSuper, submissionType, submissionMethod, premisesDto);
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
        currentSuper.setOrgId(orgId);
        currentSuper.setLicenseeId(licenseeId);
        currentSuper.setAppType(DataSubmissionConsts.DS_APP_TYPE_NEW);
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

    private boolean isNeedReNew(ArSuperDataSubmissionDto arSuperDto, String submissionType, String submissionMethod,
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


}
