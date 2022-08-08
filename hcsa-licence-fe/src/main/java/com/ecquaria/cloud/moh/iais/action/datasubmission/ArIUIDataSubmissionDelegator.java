package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.constant.organization.OrganizationConstants;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.HusbandDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.ControllerHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.DsRfcHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.PatientService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Delegator("arIUIDataSubmissionDelegator")
@Slf4j
public class ArIUIDataSubmissionDelegator extends CommonDelegator{
    private static final String CENTRE_SEL = "centreSel";
    private static final String CURRENT_STAGE = "currentStage";
    private static final String ACTION_TYPE_AMEND = "amend";
    private final PatientService patientService;

    public ArIUIDataSubmissionDelegator(PatientService patientService) {
        this.patientService = patientService;
    }

    public void start(BaseProcessClass bpc){
        log.info("----- Assisted Reproduction Submission Start -----");
        //clear session parameters
        HttpSession session = bpc.request.getSession();
        session.removeAttribute(DataSubmissionConstant.AR_PREMISES_MAP);
        session.removeAttribute(DataSubmissionConstant.AR_DATA_SUBMISSION);
        session.removeAttribute("patientInfoDto");
    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        String actionType = ParamUtil.getString(bpc.request,DataSubmissionConstant.CRUD_TYPE);
        String crudActionType = ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE);
        //exist crud action type is filled by stage self
        String currentStage = ParamUtil.getRequestString(bpc.request,CURRENT_STAGE);
        if(StringUtils.hasLength(actionType) && !StringUtils.hasLength(crudActionType)){
            if(DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO.equals(arSuperDataSubmissionDto.getSubmissionType()) && CommonDelegator.ACTION_TYPE_SUBMISSION.equals(actionType)){
                PatientInfoDto patientInfo = arSuperDataSubmissionDto.getPatientInfoDto();
                boolean isRFC = DataSubmissionConsts.DS_APP_TYPE_RFC.equals(patientInfo.getAppType());
                String profile = isRFC ? ACTION_TYPE_AMEND : "save";
                validatePageData(bpc.request, patientInfo, profile, ACTION_TYPE_SUBMISSION,currentStage,null,null);
            } else{
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE,actionType);
            }
        }
    }

    @Override
    public void pageAction(BaseProcessClass bpc) {

    }

    public void init(BaseProcessClass bpc){
    }


    public void preARIUIDataSubmission(BaseProcessClass bpc){
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, "ar-submission");
        String actionValue = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_VALUE);
        //AR Centre that is performing this submission
        Map<String, PremisesDto> premisesMap = DataSubmissionHelper.setArPremisesMap(bpc.request);
        if (premisesMap.isEmpty()) {
            Map<String, String> map = IaisCommonUtils.genNewHashMap(2);
            map.put(CENTRE_SEL, "There are no active Assisted Reproduction licences");
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(map));
        } else if (premisesMap.size() == 1) {
            premisesMap.forEach((k, v) -> {
                bpc.request.getSession().setAttribute(DataSubmissionConstant.AR_PREMISES, v);
                bpc.request.setAttribute("premisesLabel", v.getPremiseLabel());
            });
        } else {
            bpc.request.setAttribute("premisesOpts", DataSubmissionHelper.genPremisesOptions(premisesMap));
        }

        if("needCycle".equals(actionValue)){
            ParamUtil.setRequestAttr(bpc.request,"needShowCycle",true);
        }
    }

    public void doARIUIDataSubmission(BaseProcessClass bpc){
        //mark current stage
        ParamUtil.setRequestAttr(bpc.request, CURRENT_STAGE, CommonDelegator.ACTION_TYPE_PAGE);
        String submissionType = ParamUtil.getString(bpc.request, "submissionType");
        String submissionMethod = ParamUtil.getString(bpc.request, "submissionMethod");

        //submissionMethod: Form Entry/Batch Upload
        //submissionType: Yes(donor sample)/No(patient)
        Map<String, String> map = Maps.newHashMapWithExpectedSize(5);
        if(!StringUtils.hasLength(submissionMethod)){
            map.put("submissionMethod", "GENERAL_ERR0006");
        }

        if(!StringUtils.hasLength(submissionType)){
            map.put("submissionType", "GENERAL_ERR0006");
        }

        //prepare ArSubmissionDto for donor sample or patient or cycle
        ArSuperDataSubmissionDto currentSuper = prepareArSuperDataSubmissionDto(bpc,submissionType,submissionMethod,map);

        if(DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO.equals(submissionType)){
            PatientInfoDto patientInfo = (PatientInfoDto) ParamUtil.getSessionAttr(bpc.request,"patientInfoDto");
            if(Objects.isNull(patientInfo)){
                patientInfo = prepareSavePatient(bpc,currentSuper.getOrgId(),currentSuper.getAppType(),false);
            }
            currentSuper.setPatientInfoDto(patientInfo);
        } else if (DataSubmissionConsts.AR_TYPE_SBT_DONOR_SAMPLE.equals(submissionType)){

        }

        DataSubmissionHelper.setCurrentArDataSubmission(currentSuper,bpc.request);
    }


    public void preAmendPatient(BaseProcessClass bpc){
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, "amend-patient");
    }

    public void doAmendPatient(BaseProcessClass bpc){
        ParamUtil.setRequestAttr(bpc.request,CURRENT_STAGE,ACTION_TYPE_AMEND);
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        arSuperDataSubmissionDto.setAppType(DataSubmissionConsts.DS_APP_TYPE_RFC);
        PatientInfoDto patientInfoDto = prepareSavePatient(bpc,DataSubmissionConsts.DS_APP_TYPE_RFC,true);
        arSuperDataSubmissionDto.setPatientInfoDto(patientInfoDto);
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto,bpc.request);
    }

    public void prepareConfirm(BaseProcessClass bpc){
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.PRINT_FLAG, DataSubmissionConsts.DS_PATIENT_ART);
        ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmission, bpc.request);
    }

    @Override
    public void submission(BaseProcessClass bpc){
        ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        PatientInfoDto patientInfoDto = arSuperDataSubmission.getPatientInfoDto();
        CycleDto cycle = arSuperDataSubmission.getCycleDto();
        cycle.setPatientCode(patientInfoDto.getPatient().getPatientCode());
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmission, bpc.request);

        //handle next page to jump
        // save or amend patient info -> next page is current page
        // donor sample next page confirm page - you can go to seek more details from deployed
        String submissionType = arSuperDataSubmission.getSubmissionType();
        if(DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO.equals(submissionType)){
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE,CommonDelegator.ACTION_TYPE_PAGE);
            ParamUtil.setRequestAttr(bpc.request,"needCycle",true);
        }
    }

    public void doBack(BaseProcessClass bpc) throws IOException {
        log.info(StringUtil.changeForLog("The doReturn start ..."));
        returnStep(bpc);
        ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        String currentStage = ParamUtil.getRequestString(bpc.request,CURRENT_STAGE);
        String uri = InboxConst.URL_MAIN_WEB_MODULE + "MohInternetInbox";

        String submissionType = arSuperDataSubmission.getSubmissionType();
        if(DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO.equals(submissionType)){
            if(ACTION_TYPE_AMEND.equals(currentStage)){
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
        log.info(StringUtil.changeForLog("The url is -->:"+url));
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
        IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
        log.info(StringUtil.changeForLog("The doReturn end ..."));
    }

    public void prepareAck(BaseProcessClass bpc){
        //may do something in future
    }

    public PatientInfoDto prepareSavePatient(BaseProcessClass bpc,String appType,boolean isAmend){
        return prepareSavePatient(bpc,null,appType,isAmend);
    }


    /**
     * @param isAmend : decide amend patient or not - just using for amending patient's details
     * **/
    public PatientInfoDto prepareSavePatient(BaseProcessClass bpc,String orgId,String appType,boolean isAmend){
        PatientInfoDto patientInfo = new PatientInfoDto();
        if(isAmend){
            patientInfo = (PatientInfoDto) ParamUtil.getSessionAttr(bpc.request,"patientInfoDto");
        }

        PatientDto patient = ControllerHelper.get(bpc.request, PatientDto.class);
        HusbandDto husband = ControllerHelper.get(bpc.request, HusbandDto.class, "Hbd");


        //next is difference of handling about new and amend
        if(isAmend){
            //amend just replace field need filled
           PatientDto oldPatient  = patientInfo.getPatient();
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


    private PatientDto retrievePrePatient(PatientDto patient, PatientDto previous) {
        PatientDto db = new PatientDto();
        //get id number type
        String idDiff = checkIdentityNoType(previous.getIdNumber());

        //idType is marked as a list due to this special type NRIC owns two type - PINK IC or BLUE IC
        List<String> idTypes = new ArrayList<>(2);
        if(OrganizationConstants.ID_TYPE_NRIC.equals(idDiff)){
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


    public ArSuperDataSubmissionDto prepareArSuperDataSubmissionDto(BaseProcessClass bpc,String submissionType,String submissionMethod,Map<String,String> map){
        // check premises
        HttpSession session = bpc.request.getSession();
        String centreSel = ParamUtil.getString(bpc.request, CENTRE_SEL);
        Map<String, PremisesDto> premisesMap = (Map<String, PremisesDto>) session.getAttribute(
                DataSubmissionConstant.AR_PREMISES_MAP);
        PremisesDto premisesDto = (PremisesDto) session.getAttribute(DataSubmissionConstant.AR_PREMISES);
        if (!StringUtils.isEmpty(centreSel)) {
            if (premisesMap != null) {
                premisesDto = premisesMap.get(centreSel);
            }
            if (premisesDto == null) {
                map.put(CENTRE_SEL, "GENERAL_ERR0049");
            }
        } else if (premisesMap != null && premisesMap.size() > 1) {
            map.put(CENTRE_SEL, "GENERAL_ERR0006");
        } else if (premisesDto == null) {
            map.put(CENTRE_SEL, "There are no active Assisted Reproduction licences");
        }

        ArSuperDataSubmissionDto currentSuper = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        boolean reNew = isNeedReNew(currentSuper, submissionType, submissionMethod, premisesDto);
        if (reNew) {
            currentSuper = new ArSuperDataSubmissionDto();
        }

        String orgId = "";
        String licenseeId = "";
        String userId = "";
        LoginContext loginContext = DataSubmissionHelper.getLoginContext(bpc.request);
        if (loginContext != null) {
            orgId = loginContext.getOrgId();
            licenseeId = loginContext.getLicenseeId();
            userId = loginContext.getUserId();
        }

        if (!map.isEmpty()) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(map));
        } else {
            String hciCode =  premisesDto !=null ? premisesDto.getHciCode() : "";
            String actionValue = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
            log.info(StringUtil.changeForLog("Action Type: " + actionValue));
            if (StringUtil.isEmpty(actionValue)) {
                ArSuperDataSubmissionDto dataSubmissionDraft = null;
                if (!DataSubmissionConsts.DS_METHOD_FILE_UPLOAD.equals(submissionMethod)) {
                    dataSubmissionDraft = arDataSubmissionService.getArSuperDataSubmissionDtoDraftByConds(
                            orgId, submissionType, hciCode, userId);
                }
                if (dataSubmissionDraft != null/* && !Objects.equals(dataSubmissionDraft.getDraftNo(), currentSuper.getDraftNo())*/) {
                    ParamUtil.setRequestAttr(bpc.request, "hasDraft", Boolean.TRUE);
                }
            } else if ("resume".equals(actionValue)) {
                currentSuper = arDataSubmissionService.getArSuperDataSubmissionDtoDraftByConds(orgId, submissionType,
                        hciCode, userId);
                if (currentSuper == null) {
                    log.warn("Can't resume data!");
                    currentSuper = new ArSuperDataSubmissionDto();
                } else {
                    reNew = false;
                }
            } else if ("delete".equals(actionValue)) {
                arDataSubmissionService.deleteArSuperDataSubmissionDtoDraftByConds(orgId, submissionType, hciCode);
                currentSuper = new ArSuperDataSubmissionDto();
                reNew = true;
            }
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

    public static String checkIdentityNoType(String identityNo){
        String upper = identityNo.toUpperCase();
        boolean b = SgNoValidator.validateNric(upper);
        boolean b1 = SgNoValidator.validateFin(upper);
        if (b){
            return OrganizationConstants.ID_TYPE_NRIC;
        }

        if (b1){
            return OrganizationConstants.ID_TYPE_FIN;
        }

        return OrganizationConstants.ID_TYPE_PASSPORT;
    }


}
