package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DoctorInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsCenterDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsConfig;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.GuardianAppliedPartDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.SexualSterilizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TreatmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.VssDocumentDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.VssSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.VssTreatmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.helper.dataSubmission.DsConfigHelper;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.ControllerHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppCommService;
import com.ecquaria.cloud.moh.iais.service.LicenceViewService;
import com.ecquaria.cloud.moh.iais.service.client.ComFileRepoClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceFeMsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.DocInfoService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.DsLicenceService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.VssDataSubmissionService;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.ecquaria.cloud.moh.iais.common.helper.dataSubmission.DsConfigHelper.VSS_CURRENT_STEP;
import static com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant.VS_DOCTOR_INFO_FROM_ELIS;
import static com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant.VS_DOCTOR_INFO_FROM_PRS;
import static com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant.VS_DOCTOR_INFO_USER_NEW_REGISTER;

/**
 * Process: MohVSSDataSubmission
 *
 * @Description VssDataSubmissionDelegator
 * @Auther chenlei on 12/13/2021.
 */
@Slf4j
@Delegator("vssDataSubmissionDelegator")
public class VssDataSubmissionDelegator {
    private static final String SUBMIT_FLAG = "VSS_SubmitFlaaaaaa7$frg";

    private static final String VSS_FILES = "vssFiles";

    @Autowired
    private VssDataSubmissionService vssDataSubmissionService;

    @Autowired
    private ComFileRepoClient comFileRepoClient;


    public static final String ACTION_TYPE_CONFIRM = "confirm";

    @Autowired
    LicenceFeMsgTemplateClient licenceFeMsgTemplateClient;

    @Autowired
    NotificationHelper notificationHelper;

    @Autowired
    LicenceViewService licenceViewService;

    @Autowired
    DsLicenceService dsLicenceService;

    @Autowired
    private AppCommService appSubmissionService;

    @Autowired
    private LicenceClient licenceClient;

    @Autowired
    private DocInfoService docInfoService;

    /**
     * Step: Start
     *
     * @param bpc
     */
    public void doStart(BaseProcessClass bpc) {
        log.info(" -----VssDataSubmissionDelegator Start ------ ");
        DsConfigHelper.clearVssSession(bpc.request);
        DsConfigHelper.initVssConfig(bpc.request);
        DataSubmissionHelper.clearSession(bpc.request);
        ParamUtil.setSessionAttr(bpc.request, "doctorInformationPE", null);
        ParamUtil.setSessionAttr(bpc.request, VSS_FILES, null);
        ParamUtil.setSessionAttr(bpc.request, "seesion_files_map_ajax_feselectedVssFile", null);
        ParamUtil.setSessionAttr(bpc.request, "seesion_files_map_ajax_feselectedVssFile_MaxIndex", null);
        ParamUtil.clearSession(bpc.request, IaisEGPConstant.SEESION_FILES_MAP_AJAX + "selectedVssFile",
                IaisEGPConstant.SEESION_FILES_MAP_AJAX + "selectedVssFile" + IaisEGPConstant.SEESION_FILES_MAP_AJAX_MAX_INDEX,
                IaisEGPConstant.GLOBAL_MAX_INDEX_SESSION_ATTR);
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_DATA_SUBMISSION, AuditTrailConsts.FUNCTION_ONLINE_ENQUIRY_VSS);

        String orgId = "";
        String userId = "";
        LoginContext loginContext = DataSubmissionHelper.getLoginContext(bpc.request);
        if (loginContext != null) {
            orgId = loginContext.getOrgId();
            userId = loginContext.getUserId();
        }
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = vssDataSubmissionService.getVssSuperDataSubmissionDtoDraftByConds(orgId, DataSubmissionConsts.VSS_TYPE_SBT_VSS, userId);
        if (vssSuperDataSubmissionDto != null) {
            ParamUtil.setRequestAttr(bpc.request, "hasDraft", Boolean.TRUE);
        }
        ParamUtil.setSessionAttr(bpc.request, SUBMIT_FLAG, null);
    }


    /**
     * Step: PrepareSwitch
     *
     * @param bpc
     */
    public void prepareSwitch(BaseProcessClass bpc) {
        log.info(" ----- PrepareSwitch ------ ");
        DsConfigHelper.initVssConfig(bpc.request);
        String actionType = getActionType(bpc.request);
        String crudType = ParamUtil.getString(bpc.request, DataSubmissionConstant.CRUD_TYPE);
        if (DataSubmissionConstant.CRUD_TYPE_FROM_DRAFT.equals(crudType)) {
            actionType = DsConfigHelper.VSS_STEP_TREATMENT;
            DsConfigHelper.setActiveConfig(actionType, bpc.request);
        }
        log.info(StringUtil.changeForLog("Action Type: " + actionType));
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CRUD_ACTION_TYPE_VSS, actionType);
        ParamUtil.setRequestAttr(bpc.request, "title", DataSubmissionHelper.getMainTitle(DataSubmissionConsts.DS_APP_TYPE_NEW));
        DsConfig config = DsConfigHelper.getCurrentConfig(DataSubmissionConsts.DS_VSS, bpc.request);
        String smallTitle = "";
        if (config != null) {
            smallTitle = config.getText();
        }
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>" + "Voluntary Sterilization" + "</strong>");

    }

    private String getActionType(HttpServletRequest request) {
        String actionType = (String) ParamUtil.getRequestAttr(request, DataSubmissionConstant.CRUD_ACTION_TYPE_VSS);
        if (StringUtil.isEmpty(actionType)) {
            actionType = DataSubmissionHelper.initAction(DataSubmissionConsts.DS_VSS, DsConfigHelper.VSS_STEP_TREATMENT, request);
        }
        return actionType;
    }

    /**
     * Step: PrepareStepData
     *
     * @param bpc
     */
    public void prepareStepData(BaseProcessClass bpc) {
        log.info(" -----PrepareStepData ------ ");
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = DataSubmissionHelper.getCurrentVssDataSubmission(bpc.request);
        if (vssSuperDataSubmissionDto == null) {
            vssSuperDataSubmissionDto = new VssSuperDataSubmissionDto();
        }
        String crud_action_type = ParamUtil.getRequestString(bpc.request, DataSubmissionConstant.CRUD_ACTION_TYPE_VSS);
        if (StringUtil.isEmpty(crud_action_type)) {
            crud_action_type = "";
        }
        //draft
        if (crud_action_type.equals("resume")) {
            String userId = "";
            LoginContext loginContext = DataSubmissionHelper.getLoginContext(bpc.request);
            if (loginContext != null) {
                userId = loginContext.getUserId();
            }
            vssSuperDataSubmissionDto = vssDataSubmissionService.getVssSuperDataSubmissionDtoDraftByConds(vssSuperDataSubmissionDto.getOrgId(), vssSuperDataSubmissionDto.getSubmissionType(), userId);
            if (vssSuperDataSubmissionDto == null) {
                log.warn("Can't resume data!");
                vssSuperDataSubmissionDto = new VssSuperDataSubmissionDto();
            }
            DataSubmissionHelper.setCurrentVssDataSubmission(vssSuperDataSubmissionDto, bpc.request);
            ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CRUD_ACTION_TYPE_VSS, DataSubmissionConstant.PAGE_STAGE_PAGE);
            //set docs from draft
            if (!CollectionUtils.isEmpty(vssSuperDataSubmissionDto.getFileMap())) {
                ParamUtil.setSessionAttr(bpc.request, IaisEGPConstant.SEESION_FILES_MAP_AJAX + "selectedVssFile", (Serializable) vssSuperDataSubmissionDto.getFileMap());
                ParamUtil.setSessionAttr(bpc.request, IaisEGPConstant.SEESION_FILES_MAP_AJAX + "selectedVssFile" + IaisEGPConstant.SEESION_FILES_MAP_AJAX_MAX_INDEX, vssSuperDataSubmissionDto.getFileMap().size());
            }
        } else if (crud_action_type.equals("delete")) {
            vssDataSubmissionService.deleteVssSuperDataSubmissionDtoDraftByConds(vssSuperDataSubmissionDto.getOrgId(), DataSubmissionConsts.VSS_TYPE_SBT_VSS);
            vssSuperDataSubmissionDto = new VssSuperDataSubmissionDto();
        }
        retrieveHciCode(bpc.request, vssSuperDataSubmissionDto);
        initVssSuperDataSubmissionDto(bpc.request, vssSuperDataSubmissionDto);
        ParamUtil.setSessionAttr(bpc.request, "hcSelectList", (Serializable) getSourseList(bpc.request));

        List<DsConfig> configList =DsConfigHelper.initVssConfig(bpc.request);

        for (DsConfig cfg:configList
        ) {
            VssTreatmentDto vssTreatmentDto =  vssSuperDataSubmissionDto.getVssTreatmentDto();
            if(vssTreatmentDto!=null&&!cfg.getCode().equals(DsConfigHelper.TOP_STEP_PREVIEW)&&!cfg.getCode().equals(DsConfigHelper.VSS_STEP_TREATMENT)){
                int status=0;
                if (DsConfigHelper.VSS_STEP_CONSENT_PARTICULARS.equals(cfg.getCode())&&vssTreatmentDto.getGuardianAppliedPartDto()!=null) {
                    ValidationResult result = WebValidationHelper.validateProperty(vssTreatmentDto.getGuardianAppliedPartDto(),DataSubmissionConsts.DS_VSS);
                    status = result.isHasErrors()?0:1;
                }else if (DsConfigHelper.VSS_STEP_TFSSP_PARTICULARS.equals(cfg.getCode())&&vssTreatmentDto.getSexualSterilizationDto()!=null) {
                    ValidationResult result = WebValidationHelper.validateProperty(vssTreatmentDto.getSexualSterilizationDto(),DataSubmissionConsts.DS_VSS);
                    status = result.isHasErrors()?0:1;
                }
                cfg.setStatus(status);
                DsConfigHelper.setConfig(DataSubmissionConsts.DS_VSS, cfg, bpc.request);
            }
        }
        DataSubmissionHelper.setCurrentVssDataSubmission(vssSuperDataSubmissionDto, bpc.request);
        String pageStage = DataSubmissionConstant.PAGE_STAGE_PAGE;
        DsConfig currentConfig = DsConfigHelper.getCurrentConfig(DataSubmissionConsts.DS_VSS, bpc.request);
        if (DsConfigHelper.VSS_STEP_PREVIEW.equals(currentConfig.getCode())) {
            pageStage = DataSubmissionConstant.PAGE_STAGE_PREVIEW;
        }
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, pageStage);
        String currentCode = currentConfig.getCode();
        log.info(StringUtil.changeForLog(" ----- PrepareStepData Step Code: " + currentCode + " ------ "));
        if (DsConfigHelper.VSS_STEP_TREATMENT.equals(currentCode)) {
            prepareTreatment(bpc.request);
        } else if (DsConfigHelper.VSS_STEP_CONSENT_PARTICULARS.equals(currentCode)) {
            prepareConsentParticulars(bpc.request);
        } else if (DsConfigHelper.VSS_STEP_TFSSP_PARTICULARS.equals(currentCode)) {
            prepareTfsspParticulars(bpc.request);

        } else if (DsConfigHelper.VSS_STEP_PREVIEW.equals(currentCode)) {
            preparePreview(bpc.request);
        }

    }

    private void initVssSuperDataSubmissionDto(HttpServletRequest request, VssSuperDataSubmissionDto vssSuperDataSubmissionDto) {

        String orgId = Optional.ofNullable(DataSubmissionHelper.getLoginContext(request))
                .map(LoginContext::getOrgId).orElse("");
        vssSuperDataSubmissionDto.setOrgId(orgId);
        vssSuperDataSubmissionDto.setSubmissionType(DataSubmissionConsts.VSS_TYPE_SBT_VSS);
        vssSuperDataSubmissionDto.setAppType(DataSubmissionConsts.DS_APP_TYPE_NEW);
        vssSuperDataSubmissionDto.setFe(true);
        vssSuperDataSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());

        vssSuperDataSubmissionDto.setCycleDto(DataSubmissionHelper.initCycleDto(vssSuperDataSubmissionDto,
                DataSubmissionHelper.getLicenseeId(request), false));
        vssSuperDataSubmissionDto.setDataSubmissionDto(DataSubmissionHelper.initDataSubmission(vssSuperDataSubmissionDto, false));
    }

    private void retrieveHciCode(HttpServletRequest request, VssSuperDataSubmissionDto vssSuperDataSubmissionDto) {
        Map<String, PremisesDto> premisesMap =
                (Map<String, PremisesDto>) request.getSession().getAttribute(DataSubmissionConstant.VSS_PREMISES_MAP);
        if (premisesMap == null || premisesMap.isEmpty()) {
            LoginContext loginContext = DataSubmissionHelper.getLoginContext(request);
            String licenseeId = null;
            if (loginContext != null) {
                licenseeId = loginContext.getLicenseeId();
            }
            premisesMap = vssDataSubmissionService.getVssCenterPremises(licenseeId);
        }
        premisesMap.values().stream().forEach(v -> vssSuperDataSubmissionDto.setPremisesDto(v));

        /*   *//*DataSubmissionHelper.setVssPremisesMap(request).values().stream().forEach(v-> vssSuperDataSubmissionDto.setPremisesDto(v));*//*
        Map<String, PremisesDto>  premisesDtoMap = DataSubmissionHelper.setVssPremisesMap(request);
        premisesDtoMap.values().stream().forEach(v->
                vssSuperDataSubmissionDto.setPremisesDto(v)
        );*/
    }

    protected final List<SelectOption> getSourseList(HttpServletRequest request) {
        Map<String, String> stringStringMap = IaisCommonUtils.genNewHashMap();
        DataSubmissionHelper.setVsPremisesMap(request).values().stream().forEach(v -> stringStringMap.put(v.getHciCode(), v.getPremiseLabel()));
        return DataSubmissionHelper.genOptions(stringStringMap);
    }

    /**
     * Step: DoStep
     *
     * @param bpc
     */
    public void doStep(BaseProcessClass bpc) {
        log.info(" ----- DoStep ------ ");
        HttpServletRequest request = bpc.request;
        String crudType = ParamUtil.getString(request, DataSubmissionConstant.CRUD_TYPE);
        if ("return".equals(crudType)) {
            return;
        }
        //for ds center validation
        LoginContext login = AccessUtil.getLoginUser(request);
        List<DsCenterDto> centerDtos = licenceClient.getDsCenterDtosByOrgIdAndCentreType(login.getOrgId(), DataSubmissionConsts.DS_VSS).getEntity();
        if (IaisCommonUtils.isEmpty(centerDtos)) {
            Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
            errMap.put("topErrorMsg", "DS_ERR070");
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
            return;
        }
        DsConfig currentConfig = DsConfigHelper.getCurrentConfig(DataSubmissionConsts.DS_VSS, request);
        String currentCode = currentConfig.getCode();
        log.info(StringUtil.changeForLog(" ----- DoStep Step Code: " + currentCode + " ------ "));
        int status = 0;// 0: current page; -1: back / previous; 1: next; 2: submission
        if ("previous".equals(crudType)) {
            currentConfig.setStatus(status);
            DsConfigHelper.setConfig(DataSubmissionConsts.DS_VSS, currentConfig, request);
        }
        if (DsConfigHelper.VSS_STEP_TREATMENT.equals(currentCode)) {
            status = doTreatment(request);
        } else if (DsConfigHelper.VSS_STEP_CONSENT_PARTICULARS.equals(currentCode)) {
            status = doConsentParticulars(request);
        } else if (DsConfigHelper.VSS_STEP_TFSSP_PARTICULARS.equals(currentCode)) {
            status = doTfsspParticulars(request);
        } else if (DsConfigHelper.VSS_STEP_PREVIEW.equals(currentCode)) {
            status = doPreview(request);
        }
        if ("next".equals(crudType) || DataSubmissionHelper.isToNextAction(request)) {
            currentConfig.setStatus(status);
            DsConfigHelper.setConfig(DataSubmissionConsts.DS_VSS, currentConfig, request);
        } else {
            status = 0;
            currentConfig.setStatus(status);
            DsConfigHelper.setConfig(DataSubmissionConsts.DS_VSS, currentConfig, request);
        }
        vssDataSubmissionService.displayToolTipJudgement(request);
        log.info(StringUtil.changeForLog(" ----- DoStep Status: " + status + " ------ "));
        ParamUtil.setRequestAttr(request, DataSubmissionConstant.ACTION_STATUS, status);
        ParamUtil.setRequestAttr(request, "currentStage", DataSubmissionConstant.PAGE_STAGE_PAGE);

    }

    private void prepareTreatment(HttpServletRequest request) {
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = DataSubmissionHelper.getCurrentVssDataSubmission(request);
        VssTreatmentDto vssTreatmentDto = vssSuperDataSubmissionDto.getVssTreatmentDto() == null ? new VssTreatmentDto() : vssSuperDataSubmissionDto.getVssTreatmentDto();
        TreatmentDto treatmentDto = vssTreatmentDto.getTreatmentDto() == null ? new TreatmentDto() : vssTreatmentDto.getTreatmentDto();
        if (StringUtil.isEmpty(treatmentDto.getSterilizationReason())) {
            treatmentDto.setSterilizationReason(DataSubmissionConsts.MAIN_REASON_FOR_STERILIZATION_MENTAL_ILLNESS);
        }
        vssTreatmentDto.setTreatmentDto(treatmentDto);
        vssSuperDataSubmissionDto.setVssTreatmentDto(vssTreatmentDto);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.VSS_DATA_SUBMISSION, vssSuperDataSubmissionDto);

    }

    private int doTreatment(HttpServletRequest request) {
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = DataSubmissionHelper.getCurrentVssDataSubmission(request);
        vssSuperDataSubmissionDto = vssSuperDataSubmissionDto == null ? new VssSuperDataSubmissionDto() : vssSuperDataSubmissionDto;
        VssTreatmentDto vssTreatmentDto = vssSuperDataSubmissionDto.getVssTreatmentDto() == null ? new VssTreatmentDto() : vssSuperDataSubmissionDto.getVssTreatmentDto();
        TreatmentDto treatmentDto = vssTreatmentDto.getTreatmentDto() == null ? new TreatmentDto() : vssTreatmentDto.getTreatmentDto();
        ControllerHelper.get(request, treatmentDto);
        if(StringUtil.isNotEmpty(treatmentDto.getIdNumber())){
            treatmentDto.setIdNumber(treatmentDto.getIdNumber().toUpperCase());
        }
        try {
            int ageNew=Formatter.getAge(treatmentDto.getBirthDate());

            treatmentDto.setAge(ageNew);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        if (StringUtil.isNotEmpty(treatmentDto.getEthnicGroup()) && !treatmentDto.getEthnicGroup().equals("ECGP004")) {
            treatmentDto.setOtherEthnicGroup(null);
        }
        if (StringUtil.isNotEmpty(treatmentDto.getOccupation()) && !treatmentDto.getOccupation().equals("VSSOP013")) {
            treatmentDto.setOtherOccupation(null);
        }
        if (StringUtil.isNotEmpty(treatmentDto.getSterilizationReason()) && !treatmentDto.getSterilizationReason().equals("VSSRFS009")) {
            treatmentDto.setOtherSterilizationReason(null);
        }
        if (StringUtil.isEmpty(treatmentDto.getOrgId())) {
            treatmentDto.setOrgId(vssSuperDataSubmissionDto.getOrgId());
        }
        vssTreatmentDto.setTreatmentDto(treatmentDto);
        vssSuperDataSubmissionDto.setVssTreatmentDto(vssTreatmentDto);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.VSS_DATA_SUBMISSION, vssSuperDataSubmissionDto);
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        String actionType = ParamUtil.getString(request, DataSubmissionConstant.CRUD_TYPE);
        if ("next".equals(actionType) || DataSubmissionHelper.isToNextAction(request)) {
            ValidationResult result = WebValidationHelper.validateProperty(treatmentDto, DataSubmissionConsts.DS_VSS);
            if (result != null) {
                errMap.putAll(result.retrieveAll());
            }
        }

        if (!errMap.isEmpty()) {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
            return 0;
        }
        return 1;
    }

    private void prepareConsentParticulars(HttpServletRequest request) {
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = DataSubmissionHelper.getCurrentVssDataSubmission(request);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.VSS_DATA_SUBMISSION, vssSuperDataSubmissionDto);
        ParamUtil.setSessionAttr(request, VSS_FILES, null);
    }

    private int doConsentParticulars(HttpServletRequest request) {
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = DataSubmissionHelper.getCurrentVssDataSubmission(request);
        vssSuperDataSubmissionDto = vssSuperDataSubmissionDto == null ? new VssSuperDataSubmissionDto() : vssSuperDataSubmissionDto;
        VssTreatmentDto vssTreatmentDto = vssSuperDataSubmissionDto.getVssTreatmentDto() == null ? new VssTreatmentDto() : vssSuperDataSubmissionDto.getVssTreatmentDto();
        GuardianAppliedPartDto guardianAppliedPartDto = vssTreatmentDto.getGuardianAppliedPartDto() == null ? new GuardianAppliedPartDto() : vssTreatmentDto.getGuardianAppliedPartDto();
        ControllerHelper.get(request, guardianAppliedPartDto);
        if(StringUtil.isNotEmpty(guardianAppliedPartDto.getAppliedPartIdNo())){
            guardianAppliedPartDto.setAppliedPartIdNo(guardianAppliedPartDto.getAppliedPartIdNo().toUpperCase());
        }
        if(StringUtil.isNotEmpty(guardianAppliedPartDto.getGuardianIdNo())){
            guardianAppliedPartDto.setGuardianIdNo(guardianAppliedPartDto.getGuardianIdNo().toUpperCase());
        }
        String appliedPartBirthday = ParamUtil.getString(request, "appliedPartBirthday");
        String guardianBirthday = ParamUtil.getString(request, "guardianBirthday");
        String courtOrderIssueDate = ParamUtil.getString(request, "courtOrderIssueDate");
        try {
            Date aDate = Formatter.parseDate(appliedPartBirthday);
            Date gDate = Formatter.parseDate(guardianBirthday);
            Date cDate = Formatter.parseDate(courtOrderIssueDate);
            guardianAppliedPartDto.setAppliedPartBirthday(aDate);
            guardianAppliedPartDto.setGuardianBirthday(gDate);
            guardianAppliedPartDto.setCourtOrderIssueDate(cDate);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        setFiles(guardianAppliedPartDto, request);
        vssTreatmentDto.setGuardianAppliedPartDto(guardianAppliedPartDto);
        vssSuperDataSubmissionDto.setVssTreatmentDto(vssTreatmentDto);

        ParamUtil.setSessionAttr(request, DataSubmissionConstant.VSS_DATA_SUBMISSION, vssSuperDataSubmissionDto);

        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        String actionType = ParamUtil.getString(request, DataSubmissionConstant.CRUD_TYPE);
        if ("next".equals(actionType) || DataSubmissionHelper.isToNextAction(request)) {
            ValidationResult result = WebValidationHelper.validateProperty(guardianAppliedPartDto, DataSubmissionConsts.DS_VSS);
            if (result != null) {
                errMap.putAll(result.retrieveAll());
            }
        }
        if (!errMap.isEmpty()) {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
            return 0;
        }
        return 1;
    }

    private void prepareTfsspParticulars(HttpServletRequest request) {
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = DataSubmissionHelper.getCurrentVssDataSubmission(request);
        VssTreatmentDto vssTreatmentDto = vssSuperDataSubmissionDto.getVssTreatmentDto() == null ? new VssTreatmentDto() : vssSuperDataSubmissionDto.getVssTreatmentDto();
        TreatmentDto treatmentDto = vssTreatmentDto.getTreatmentDto() == null ? new TreatmentDto() : vssTreatmentDto.getTreatmentDto();
        String[] sterilizationMd;
        if (treatmentDto.getGender().equals(DataSubmissionConsts.GENDER_MALE)) {
            sterilizationMd = new String[]{DataSubmissionConsts.METHOD_OF_STERILIZATION_OCCLUSION_REMOVAL};
        } else {
            sterilizationMd = new String[]{"VSMOS001", "VSMOS002", "VSMOS003", "VSMOS004"};
        }
        List<SelectOption> sterilizationLists = MasterCodeUtil.retrieveOptionsByCodes(sterilizationMd);
        ParamUtil.setSessionAttr(request, "sterilizationLists", (Serializable) sterilizationLists);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.VSS_DATA_SUBMISSION, vssSuperDataSubmissionDto);
        ParamUtil.setRequestAttr(request, DataSubmissionConstant.CURRENT_PAGE_STAGE, ACTION_TYPE_CONFIRM);

        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        jumpControl(request, errMap, vssSuperDataSubmissionDto);
        if (!errMap.isEmpty()) {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
        }
    }

    private int doTfsspParticulars(HttpServletRequest request) {
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = DataSubmissionHelper.getCurrentVssDataSubmission(request);
        vssSuperDataSubmissionDto = vssSuperDataSubmissionDto == null ? new VssSuperDataSubmissionDto() : vssSuperDataSubmissionDto;
        VssTreatmentDto vssTreatmentDto = vssSuperDataSubmissionDto.getVssTreatmentDto() == null ? new VssTreatmentDto() : vssSuperDataSubmissionDto.getVssTreatmentDto();
        SexualSterilizationDto sexualSterilizationDto = vssTreatmentDto.getSexualSterilizationDto() == null ? new SexualSterilizationDto() : vssTreatmentDto.getSexualSterilizationDto();
        DoctorInformationDto doctorInformationDto = vssSuperDataSubmissionDto.getDoctorInformationDto();
        if (doctorInformationDto == null) {
            doctorInformationDto = new DoctorInformationDto();
        }
        String doctorName = ParamUtil.getString(request, "names");
        String doctorInformationPE = ParamUtil.getString(request, "doctorInformationPE");
        String specialty = ParamUtil.getString(request, "specialty");
        String subSpecialty = ParamUtil.getString(request, "subSpecialty");
        String qualification = ParamUtil.getString(request, "qualification");
        String doctorReignNo = ParamUtil.getString(request, "doctorReignNo");
        String sterilizationMethod = ParamUtil.getString(request, "sterilizationMethod");
        String hecReviewedHospital = ParamUtil.getString(request, "hecReviewedHospital");
        String operationDate = ParamUtil.getString(request, "operationDate");
        String reviewedByHec = ParamUtil.getString(request, "reviewedByHec");
        String hecReviewDate = ParamUtil.getString(request, "hecReviewDate");
        String sterilizationHospital = ParamUtil.getString(request, "sterilizationHospital");
        String otherQualification = ParamUtil.getString(request, "otherQualification");
        String doctorInformations = ParamUtil.getString(request, "doctorInformations");
        sexualSterilizationDto.setSterilizationHospital(sterilizationHospital);
        sexualSterilizationDto.setOtherQualification(otherQualification);
        sexualSterilizationDto.setDoctorReignNo(doctorReignNo);
        sexualSterilizationDto.setDoctorName(doctorName);
        sexualSterilizationDto.setSterilizationMethod(sterilizationMethod);
        sexualSterilizationDto.setHecReviewedHospital(hecReviewedHospital);
        sexualSterilizationDto.setSpecialty(specialty);
        sexualSterilizationDto.setSubSpecialty(subSpecialty);
        sexualSterilizationDto.setQualification(qualification);
        sexualSterilizationDto.setDoctorInformationPE(doctorInformationPE);
        sexualSterilizationDto.setDoctorInformations(doctorInformations);

        // doctor info get from eLis
        if ("true".equals(sexualSterilizationDto.getDoctorInformationPE())) {
            String names = ParamUtil.getString(request, "names");
            String dSpeciality = ParamUtil.getString(request, "dSpecialitys");
            String dSubSpeciality = ParamUtil.getString(request, "dSubSpecialitys");
            String dQualification = ParamUtil.getString(request, "dQualifications");
            sexualSterilizationDto.setDoctorName(names);
            sexualSterilizationDto.setSpecialty(dSpeciality);
            sexualSterilizationDto.setSubSpecialty(dSubSpeciality);
            sexualSterilizationDto.setQualification(dQualification);
            doctorInformationDto.setName(sexualSterilizationDto.getDoctorName());
            doctorInformationDto.setDoctorReignNo(sexualSterilizationDto.getDoctorReignNo());
            doctorInformationDto.setSpeciality(sexualSterilizationDto.getSpecialty());
            doctorInformationDto.setSubSpeciality(sexualSterilizationDto.getSubSpecialty());
            doctorInformationDto.setQualification(sexualSterilizationDto.getQualification());
            doctorInformationDto.setDoctorSource(VS_DOCTOR_INFO_FROM_ELIS);
            vssSuperDataSubmissionDto.setDoctorInformationDto(doctorInformationDto);
        } else if ("false".equals(sexualSterilizationDto.getDoctorInformationPE())) {
            // doctor info get from PRS
            doctorInformationDto.setName(sexualSterilizationDto.getDoctorName());
            doctorInformationDto.setDoctorReignNo(sexualSterilizationDto.getDoctorReignNo());
            doctorInformationDto.setSpeciality(sexualSterilizationDto.getSpecialty());
            doctorInformationDto.setSubSpeciality(sexualSterilizationDto.getSubSpecialty());
            doctorInformationDto.setQualification(sexualSterilizationDto.getQualification());
            doctorInformationDto.setDoctorSource(VS_DOCTOR_INFO_FROM_PRS);
            DoctorInformationDto elisDoctorInformationDto = docInfoService.getDoctorInformationDtoByConds(sexualSterilizationDto.getDoctorReignNo(), DataSubmissionConsts.DOCTOR_SOURCE_ELIS_VSS, vssSuperDataSubmissionDto.getHciCode());
            if (elisDoctorInformationDto != null){
                doctorInformationDto.setElis(true);
            }
            vssSuperDataSubmissionDto.setDoctorInformationDto(doctorInformationDto);
        }
        // doctor not register,get data from input
        if ("true".equals(sexualSterilizationDto.getDoctorInformations())) {
            String dName = ParamUtil.getString(request, "dName");
            String dSpeciality = ParamUtil.getString(request, "dSpeciality");
            String dSubSpeciality = ParamUtil.getString(request, "dSubSpeciality");
            String dQualification = ParamUtil.getString(request, "dQualification");
            doctorInformationDto.setName(dName);
            doctorInformationDto.setDoctorReignNo(sexualSterilizationDto.getDoctorReignNo());
            doctorInformationDto.setSubSpeciality(dSubSpeciality);
            doctorInformationDto.setSpeciality(dSpeciality);
            doctorInformationDto.setQualification(dQualification);
            doctorInformationDto.setDoctorSource(VS_DOCTOR_INFO_USER_NEW_REGISTER);
            sexualSterilizationDto.setDoctorName(dName);
            vssSuperDataSubmissionDto.setDoctorInformationDto(doctorInformationDto);
        } else {
            sexualSterilizationDto.setDoctorName(doctorName);
        }
        if (StringUtil.isNotEmpty(reviewedByHec)) {
            sexualSterilizationDto.setReviewedByHec(reviewedByHec.equals("true"));
        }
        try {
            Date oDate = Formatter.parseDate(operationDate);
            Date hDate = Formatter.parseDate(hecReviewDate);
            sexualSterilizationDto.setOperationDate(oDate);
            sexualSterilizationDto.setHecReviewDate(hDate);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        vssTreatmentDto.setSexualSterilizationDto(sexualSterilizationDto);
        vssSuperDataSubmissionDto.setVssTreatmentDto(vssTreatmentDto);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.VSS_DATA_SUBMISSION, vssSuperDataSubmissionDto);
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        String actionType = ParamUtil.getString(request, DataSubmissionConstant.CRUD_TYPE);
        if ("next".equals(actionType) || DataSubmissionHelper.isToNextAction(request)) {
            ValidationResult result = WebValidationHelper.validateProperty(sexualSterilizationDto, DataSubmissionConsts.DS_VSS);
            if (result != null) {
                errMap.putAll(result.retrieveAll());
            }
        }
        if (!errMap.isEmpty()) {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
            return 0;
        }
        return 1;
    }

    private void setFiles(GuardianAppliedPartDto guardianAppliedPartDto, HttpServletRequest request) {
        List<VssDocumentDto> vssDoc = new ArrayList<>();
        log.info("-----------ajax-upload-file start------------");
        Map<String, File> map = (Map<String, File>) ParamUtil.getSessionAttr(request, IaisEGPConstant.SEESION_FILES_MAP_AJAX + "selectedVssFile");
        IaisEGPHelper.getCurrentAuditTrailDto().getMohUserId();
        if (IaisCommonUtils.isNotEmpty(map)) {
            Date date = new Date();
            List<File> files = IaisCommonUtils.genNewArrayList(1);
            String mapKey = "selectedVssFile";
            map.forEach((k, v) -> {
                if (v != null) {
                    VssDocumentDto vssDocumentDto = new VssDocumentDto();
                    vssDocumentDto.setDocName(v.getName());
                    long size = v.length() / 1024;
                    vssDocumentDto.setDocSize(Integer.parseInt(String.valueOf(size)));
                    String md5Code = FileUtils.getFileMd5(v);
                    vssDocumentDto.setMd5Code(md5Code);
                    vssDocumentDto.setSubmitBy(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
                    vssDocumentDto.setSubmitDt(date);
                    vssDocumentDto.setStatus(DataSubmissionConsts.VSS_NEED_SYSN_BE_STATUS);
                    FileRepoDto fileRepoDto = new FileRepoDto();
                    fileRepoDto.setFileName(v.getName());
                    fileRepoDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    fileRepoDto.setRelativePath(AppConsts.FALSE);
                    files.add(v);
                    //save file to file DB
                    String repoId = comFileRepoClient.saveFileRepo(files).get(0);
                    files.clear();
                    vssDocumentDto.setFileRepoId(repoId);
                    vssDocumentDto.setSeqNum(Integer.parseInt(k.replace(mapKey, "")));
                    vssDoc.add(vssDocumentDto);
                }

            });
            vssDoc.sort(Comparator.comparing(VssDocumentDto::getSeqNum));
        }
        if (IaisCommonUtils.isNotEmpty(guardianAppliedPartDto.getVssDocumentDto())) {
            guardianAppliedPartDto.getVssDocumentDto().clear();
        }
        guardianAppliedPartDto.setVssDocumentDto(vssDoc);
        request.getSession().setAttribute(VSS_FILES, vssDoc);
        request.getSession().setAttribute("seesion_files_map_ajax_feselectedVssFile", map);
        request.getSession().setAttribute("seesion_files_map_ajax_feselectedVssFile_MaxIndex", vssDoc.size());

    }

    private void preparePreview(HttpServletRequest request) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = DataSubmissionHelper.getCurrentVssDataSubmission(request);
        jumpControl(request, errMap, vssSuperDataSubmissionDto);
        if (!errMap.isEmpty()) {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
        }
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.VSS_DATA_SUBMISSION, vssSuperDataSubmissionDto);
        ParamUtil.setRequestAttr(request, DataSubmissionConstant.PRINT_FLAG, DataSubmissionConstant.PRINT_FLAG_VSS);
        ParamUtil.setSessionAttr(request, "isPrintDoc", null);
    }

    private int doPreview(HttpServletRequest request) {
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = DataSubmissionHelper.getCurrentVssDataSubmission(request);
        vssSuperDataSubmissionDto = vssSuperDataSubmissionDto == null ? new VssSuperDataSubmissionDto() : vssSuperDataSubmissionDto;
        VssTreatmentDto vssTreatmentDto = vssSuperDataSubmissionDto.getVssTreatmentDto() == null ? new VssTreatmentDto() : vssSuperDataSubmissionDto.getVssTreatmentDto();
        TreatmentDto treatmentDto = vssTreatmentDto.getTreatmentDto() == null ? new TreatmentDto() : vssTreatmentDto.getTreatmentDto();
        GuardianAppliedPartDto guardianAppliedPartDto = vssTreatmentDto.getGuardianAppliedPartDto() == null ? new GuardianAppliedPartDto() : vssTreatmentDto.getGuardianAppliedPartDto();
        SexualSterilizationDto sexualSterilizationDto = vssTreatmentDto.getSexualSterilizationDto() == null ? new SexualSterilizationDto() : vssTreatmentDto.getSexualSterilizationDto();
        DataSubmissionDto dataSubmissionDto = vssSuperDataSubmissionDto.getDataSubmissionDto();
        String[] declaration = ParamUtil.getStrings(request, "declaration");
        if (declaration != null && declaration.length > 0) {
            dataSubmissionDto.setDeclaration(declaration[0]);
        } else {
            dataSubmissionDto.setDeclaration(null);
        }
        DataSubmissionHelper.setCurrentVssDataSubmission(vssSuperDataSubmissionDto, request);
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        String actionType = ParamUtil.getString(request, DataSubmissionConstant.CRUD_TYPE);
        if ("next".equals(actionType) || DataSubmissionHelper.isToNextAction(request)) {
            ValidationResult treatmentDtoResult = WebValidationHelper.validateProperty(treatmentDto, DataSubmissionConsts.DS_VSS);
            ValidationResult gapResult = WebValidationHelper.validateProperty(guardianAppliedPartDto, DataSubmissionConsts.DS_VSS);
            ValidationResult ssResult = WebValidationHelper.validateProperty(sexualSterilizationDto, DataSubmissionConsts.DS_VSS);

            if (declaration == null || declaration.length == 0) {
                errMap.put("declaration", "GENERAL_ERR0006");
            }
            if (treatmentDtoResult != null) {
                errMap.putAll(treatmentDtoResult.retrieveAll());
            }
            if (gapResult != null) {
                errMap.putAll(gapResult.retrieveAll());
            }
            if (ssResult != null) {
                errMap.putAll(ssResult.retrieveAll());
            }
        }
        if (!errMap.isEmpty()) {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
            return 0;
        }
        return 2;
    }

    public void jumpControl(HttpServletRequest request, Map<String, String> errMap, VssSuperDataSubmissionDto vssSuperDataSubmissionDto) {
        DsConfig currentConfig = DsConfigHelper.getCurrentConfig(DataSubmissionConsts.DS_VSS, request);
        vssSuperDataSubmissionDto = vssSuperDataSubmissionDto == null ? new VssSuperDataSubmissionDto() : vssSuperDataSubmissionDto;
        VssTreatmentDto vssTreatmentDto = vssSuperDataSubmissionDto.getVssTreatmentDto() == null ? new VssTreatmentDto() : vssSuperDataSubmissionDto.getVssTreatmentDto();
        TreatmentDto treatmentDto = vssTreatmentDto.getTreatmentDto() == null ? new TreatmentDto() : vssTreatmentDto.getTreatmentDto();
        GuardianAppliedPartDto guardianAppliedPartDto = vssTreatmentDto.getGuardianAppliedPartDto() == null ? new GuardianAppliedPartDto() : vssTreatmentDto.getGuardianAppliedPartDto();
        SexualSterilizationDto sexualSterilizationDto = vssTreatmentDto.getSexualSterilizationDto() == null ? new SexualSterilizationDto() : vssTreatmentDto.getSexualSterilizationDto();
        //
        ValidationResult treatmentDtoResult = new ValidationResult();
        ValidationResult gapResult = new ValidationResult();
        ValidationResult ssResult = new ValidationResult();
        //if clicked page is Particulars of Treatment for Sexual Sterilization Performed page,validate the data from the previous two pages
        if (currentConfig.getSeqNo() == 3) {
            treatmentDtoResult = WebValidationHelper.validateProperty(treatmentDto, DataSubmissionConsts.DS_VSS);
            gapResult = WebValidationHelper.validateProperty(guardianAppliedPartDto, DataSubmissionConsts.DS_VSS);
        } else if (currentConfig.getSeqNo() == 4) {
            //if clicked page is Preview & Submit page,validate the data from the previous three pages
            treatmentDtoResult = WebValidationHelper.validateProperty(treatmentDto, DataSubmissionConsts.DS_VSS);
            gapResult = WebValidationHelper.validateProperty(guardianAppliedPartDto, DataSubmissionConsts.DS_VSS);
            ssResult = WebValidationHelper.validateProperty(sexualSterilizationDto, DataSubmissionConsts.DS_VSS);
        }
        //
        List<DsConfig> configs = DsConfigHelper.getDsConfigList(DataSubmissionConsts.DS_VSS, request);
        errMap.putAll(treatmentDtoResult.retrieveAll());
        errMap.putAll(gapResult.retrieveAll());
        errMap.putAll(ssResult.retrieveAll());
        // check errorMsg, navigate to the first validate error page
        if (!CollectionUtils.isEmpty(treatmentDtoResult.retrieveAll())) {
            configs.get(0).setActive(true);
            configs.get(1).setActive(false);
            configs.get(2).setActive(false);
            configs.get(3).setActive(false);
            ParamUtil.setRequestAttr(request, VSS_CURRENT_STEP, configs.get(0));
            ParamUtil.setRequestAttr(request, DataSubmissionConstant.CURRENT_PAGE_STAGE, DataSubmissionConstant.PAGE_STAGE_PAGE);
        } else if (!CollectionUtils.isEmpty(gapResult.retrieveAll())) {
            configs.get(0).setStatus(1);
            configs.get(1).setActive(true);
            configs.get(2).setActive(false);
            configs.get(3).setActive(false);
            ParamUtil.setRequestAttr(request, VSS_CURRENT_STEP, configs.get(1));
            ParamUtil.setRequestAttr(request, DataSubmissionConstant.CURRENT_PAGE_STAGE, DataSubmissionConstant.PAGE_STAGE_PAGE);
        } else if (!CollectionUtils.isEmpty(ssResult.retrieveAll())) {
            configs.get(0).setStatus(1);
            configs.get(1).setStatus(1);
            configs.get(2).setActive(true);
            configs.get(3).setActive(false);
            ParamUtil.setRequestAttr(request, VSS_CURRENT_STEP, configs.get(2));
            ParamUtil.setRequestAttr(request, DataSubmissionConstant.CURRENT_PAGE_STAGE, ACTION_TYPE_CONFIRM);
        } else if (CollectionUtils.isEmpty(errMap)){
            configs.get(0).setStatus(1);
            configs.get(1).setStatus(1);
            configs.get(2).setStatus(1);
        }
        DsConfigHelper.setDsConfigList(DataSubmissionConsts.DS_VSS, configs, request);
    }

    /**
     * Step: DoSubmission
     *
     * @param bpc
     */
    public void doSubmission(BaseProcessClass bpc) {
        log.info(" ----- DoSubmission ------ ");
        String submitFlag = (String) ParamUtil.getSessionAttr(bpc.request, SUBMIT_FLAG);
        if (!StringUtil.isEmpty(submitFlag)) {
            throw new IaisRuntimeException("Double submit");
        }
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = DataSubmissionHelper.getCurrentVssDataSubmission(bpc.request);

        DataSubmissionDto dataSubmissionDto = vssSuperDataSubmissionDto.getDataSubmissionDto();
        CycleDto cycle = vssSuperDataSubmissionDto.getCycleDto();
        String cycleType = cycle.getCycleType();
        String status = cycle.getStatus();
        if (StringUtil.isEmpty(dataSubmissionDto.getSubmissionNo())) {
            String submissionNo = vssDataSubmissionService.getSubmissionNo(DataSubmissionConsts.DS_VSS);
            dataSubmissionDto.setSubmissionNo(submissionNo);
        }
        if (DataSubmissionConsts.DS_APP_TYPE_RFC.equals(dataSubmissionDto.getAppType())) {
            dataSubmissionDto.setStatus(DataSubmissionConsts.DS_STATUS_AMENDED);
        } else if (StringUtil.isEmpty(dataSubmissionDto.getStatus())) {
            dataSubmissionDto.setStatus(DataSubmissionConsts.DS_STATUS_COMPLETED);
        }

        if (StringUtil.isEmpty(status)) {
            status = DataSubmissionConsts.DS_STATUS_ACTIVE;
        }

        cycle.setStatus(status);
        vssSuperDataSubmissionDto.setCycleDto(cycle);
        String licenseeId = null;
        LoginContext loginContext = DataSubmissionHelper.getLoginContext(bpc.request);
        if (loginContext != null) {
            dataSubmissionDto.setSubmitBy(loginContext.getUserId());
            dataSubmissionDto.setSubmitDt(new Date());
            licenseeId = loginContext.getLicenseeId();
        }
        VssTreatmentDto vssTreatmentDto = vssSuperDataSubmissionDto.getVssTreatmentDto();
        SexualSterilizationDto sexualSterilizationDto = vssTreatmentDto.getSexualSterilizationDto();
        TreatmentDto treatmentDto = vssTreatmentDto.getTreatmentDto();
        if (StringUtil.isNotEmpty(treatmentDto.getPatientName())) {
            treatmentDto.setPatientName(treatmentDto.getPatientName().toUpperCase(AppConsts.DFT_LOCALE));
        }
        if (sexualSterilizationDto.getOperationDate() != null) {
            try {
                if (Formatter.compareDateByDay(dataSubmissionDto.getSubmitDt(), sexualSterilizationDto.getOperationDate()) > 30) {
                    sexualSterilizationDto.setLateSubmit(true);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }

        }
        vssTreatmentDto.setSexualSterilizationDto(sexualSterilizationDto);
        vssSuperDataSubmissionDto.setVssTreatmentDto(vssTreatmentDto);
        vssSuperDataSubmissionDto.setFe(true);
        vssSuperDataSubmissionDto = vssDataSubmissionService.saveVssSuperDataSubmissionDto(vssSuperDataSubmissionDto);
        try {
            ProfessionalResponseDto professionalResponseDto = appSubmissionService.retrievePrsInfo(vssSuperDataSubmissionDto.getVssTreatmentDto().getSexualSterilizationDto().getDoctorReignNo());
            if ("-1".equals(professionalResponseDto.getStatusCode()) || "-2".equals(professionalResponseDto.getStatusCode())) {
                sexualSterilizationDto.setDoctorInformations("true");
            }
            vssSuperDataSubmissionDto = vssDataSubmissionService.saveVssSuperDataSubmissionDtoToBE(vssSuperDataSubmissionDto);
        } catch (Exception e) {
            log.error(StringUtil.changeForLog("The Eic saveVssSuperDataSubmissionDtoToBE failed ===>" + e.getMessage()), e);
        }
        if (!StringUtil.isEmpty(vssSuperDataSubmissionDto.getDraftId())) {
            vssDataSubmissionService.updateDataSubmissionDraftStatus(vssSuperDataSubmissionDto.getDraftId(),
                    DataSubmissionConsts.DS_STATUS_INACTIVE);
        }
        String licenseeDtoName = AccessUtil.getLoginUser(bpc.request).getUserName();
        String submissionNo = vssSuperDataSubmissionDto.getDataSubmissionDto().getSubmissionNo();
        try {
            sendMsgAndEmail("Voluntary Sterilization", licenseeId, licenseeDtoName, submissionNo);
        } catch (IOException | TemplateException e) {
            log.error(e.getMessage(), e);
        }
        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.VSS_DATA_SUBMISSION, vssSuperDataSubmissionDto);
        ParamUtil.setRequestAttr(bpc.request, "emailAddress", DataSubmissionHelper.getEmailAddrsByRoleIdsAndLicenseeId(bpc.request, MsgTemplateConstants.MSG_TEMPLATE_VSS_SUBMITTED_ACK_EMAIL));
        ParamUtil.setRequestAttr(bpc.request, "submittedBy", DataSubmissionHelper.getLoginContext(bpc.request).getUserName());
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, DataSubmissionConstant.PAGE_STAGE_ACK);
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.PRINT_FLAG, DataSubmissionConstant.PRINT_FLAG_ACKVSS);
        ParamUtil.setSessionAttr(bpc.request, SUBMIT_FLAG, AppConsts.YES);
    }

    /**
     * Step: DoDraft
     *
     * @param bpc
     */
    public void doDraft(BaseProcessClass bpc) {
        log.info(" ----- DoDraft ------ ");
        String currentStage = (String) ParamUtil.getRequestAttr(bpc.request, "currentStage");
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CRUD_ACTION_TYPE_VSS, currentStage);
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = DataSubmissionHelper.getCurrentVssDataSubmission(bpc.request);
        if (vssSuperDataSubmissionDto != null) {
            vssSuperDataSubmissionDto.setDraftNo(vssDataSubmissionService.getDraftNo(DataSubmissionConsts.DS_VSS,
                    vssSuperDataSubmissionDto.getDraftNo()));
            Map<String, File> map = (Map<String, File>) ParamUtil.getSessionAttr(bpc.request, IaisEGPConstant.SEESION_FILES_MAP_AJAX + "selectedVssFile");
            vssSuperDataSubmissionDto.setFileMap(map);
            vssSuperDataSubmissionDto = vssDataSubmissionService.saveDataSubmissionDraft(vssSuperDataSubmissionDto);
            DataSubmissionHelper.setCurrentVssDataSubmission(vssSuperDataSubmissionDto, bpc.request);
            ParamUtil.setRequestAttr(bpc.request, "saveDraftSuccess", "success");
        } else {
            log.info(StringUtil.changeForLog("The vssSuperDataSubmission is null"));
        }
    }

    /**
     * Step: DoRfc
     *
     * @param bpc
     */
    public void doRfc(BaseProcessClass bpc) {
        log.info(" ----- DoRfc ------ ");
    }

    /**
     * Step: DoWithdraw
     *
     * @param bpc
     */
    public void doWithdraw(BaseProcessClass bpc) {
        log.info(" ----- DoWithdraw ------ ");
    }

    /**
     * Step: DoControl
     *
     * @param bpc
     */
    public void doControl(BaseProcessClass bpc) {
        log.info(" ----- DoControl ------ ");
        String crudType = ParamUtil.getString(bpc.request, DataSubmissionConstant.CRUD_TYPE);
        String actionType = null;
        if ("return".equals(crudType)) {
            actionType = "return";
        } else if ("next".equals(crudType)) {
            Integer status = (Integer) ParamUtil.getRequestAttr(bpc.request, DataSubmissionConstant.ACTION_STATUS);
            if (status == null || 0 == status) {// current
                actionType = DataSubmissionHelper.setCurrentAction(DataSubmissionConsts.DS_VSS, bpc.request);
            } else if (-1 == status) { // previous
                actionType = DataSubmissionHelper.setPreviousAction(DataSubmissionConsts.DS_VSS, bpc.request);
            } else if (1 == status) { // next
                actionType = DataSubmissionHelper.setNextAction(DataSubmissionConsts.DS_VSS, bpc.request);
            } else if (2 == status) {// to submission
                actionType = "submission";
            }
        } else if (DataSubmissionHelper.isToNextAction(bpc.request)) {
            Integer status = (Integer) ParamUtil.getRequestAttr(bpc.request, DataSubmissionConstant.ACTION_STATUS);
            if (status == null || 0 == status) {// current
                actionType = DataSubmissionHelper.setCurrentAction(DataSubmissionConsts.DS_VSS, bpc.request);
            } else if (1 == status) { // nexts
                actionType = crudType;
                DsConfigHelper.setActiveConfig(actionType, bpc.request);
            }
        } else if ("previous".equals(crudType)) {
            actionType = DataSubmissionHelper.setPreviousAction(DataSubmissionConsts.DS_VSS, bpc.request);
        } else if ("preview".equals(crudType)) {
            actionType = crudType;
        } else {
            actionType = crudType;
            DsConfigHelper.setActiveConfig(actionType, bpc.request);
        }
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CRUD_ACTION_TYPE_VSS, actionType);
    }

    /**
     * Step: DoReturn
     *
     * @param bpc
     */
    public void doReturn(BaseProcessClass bpc) throws IOException {
        log.info(" ----- DoReturn ------ ");
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = DataSubmissionHelper.getCurrentVssDataSubmission(bpc.request);
        String target = InboxConst.URL_MAIN_WEB_MODULE + "MohInternetInbox";
        if (vssSuperDataSubmissionDto != null && DataSubmissionConsts.DS_APP_TYPE_NEW.equals(vssSuperDataSubmissionDto.getAppType())) {
            target = InboxConst.URL_LICENCE_WEB_MODULE + "MohDataSubmission/PrepareCompliance";
        }
        StringBuilder url = new StringBuilder();
        url.append(InboxConst.URL_HTTPS)
                .append(bpc.request.getServerName())
                .append(target);
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
        IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
    }

    private void sendMsgAndEmail(String serverName, String licenseeId, String submitterName, String submissionNo) throws IOException, TemplateException {
        MsgTemplateDto msgTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_DS_SUBMITTED_ACK_MSG).getEntity();
        Map<String, Object> msgContentMap = IaisCommonUtils.genNewHashMap();
        msgContentMap.put("serverName", serverName);
        msgContentMap.put("ApplicantName", submitterName);
        msgContentMap.put("submissionId", submissionNo);
        msgContentMap.put("date", Formatter.formatDateTime(new Date(), "dd/MM/yyyy HH:mm:ss"));
        msgContentMap.put("MOH_AGENCY_NAME", AppConsts.MOH_AGENCY_NAME);

        Map<String, Object> msgSubjectMap = IaisCommonUtils.genNewHashMap();
        msgSubjectMap.put("serverName", serverName);
        String subject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(), msgSubjectMap);

        EmailParam msgParam = new EmailParam();
        msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_DS_SUBMITTED_ACK_MSG);
        msgParam.setTemplateContent(msgContentMap);
        msgParam.setQueryCode(submissionNo);
        msgParam.setReqRefNum(submissionNo);
        msgParam.setServiceTypes(DataSubmissionConsts.DS_VSS);
        msgParam.setRefId(licenseeId);
        msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
        msgParam.setSubject(subject);
        notificationHelper.sendNotification(msgParam);
        log.info(StringUtil.changeForLog("***************** send VSS Notification  end *****************"));
        //send email
        EmailParam emailParamEmail = MiscUtil.transferEntityDto(msgParam, EmailParam.class);
        emailParamEmail.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_VSS_SUBMITTED_ACK_EMAIL);
        emailParamEmail.setRefIdType(NotificationHelper.RECEIPT_TYPE_LICENSEE_ID);
        notificationHelper.sendNotification(emailParamEmail);
        log.info(StringUtil.changeForLog("***************** send VSS Email  end *****************"));
    }
}

