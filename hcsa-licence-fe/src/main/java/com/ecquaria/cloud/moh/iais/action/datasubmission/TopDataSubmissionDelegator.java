package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DoctorInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsCenterDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsConfig;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.FamilyPlanDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PostTerminationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PreTerminationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TerminationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TerminationOfPregnancyDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TopSuperDataSubmissionDto;
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
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppCommService;
import com.ecquaria.cloud.moh.iais.service.LicenceViewService;
import com.ecquaria.cloud.moh.iais.service.client.AssistedReproductionClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceFeMsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.DocInfoService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.TopDataSubmissionService;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import static com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant.ACTION_TYPE_CONFIRM;
import static com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant.COUNSE_LLING_PLACE;
import static com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant.COUNSE_LLING_PLACE_AGES;
import static com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant.TOP_DOCTOR_INFO_FROM_ELIS;
import static com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant.TOP_DOCTOR_INFO_FROM_PRS;
import static com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant.TOP_DOCTOR_INFO_USER_NEW_REGISTER;
import static com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant.TOP_DRUG_PLACE;
import static com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant.TOP_PLACE;

/**
 * Process: MohNEWTOPDataSumission
 *
 * @Description TopDataSubmissionDelegator
 * @Auther zhixing on 2/14/2022.
 */
@Slf4j
@Delegator("topDataSubmissionDelegator")
public class TopDataSubmissionDelegator {
    public static final String SUBMIT_FLAG = "TopSubbbmitF__lag";

    @Autowired
    private AppCommService appSubmissionService;
    //private static String COUNSELLING = null;

    @Autowired
    private TopDataSubmissionService topDataSubmissionService;

    @Autowired
    private LicenceViewService licenceViewService;

    @Autowired
    private LicenceFeMsgTemplateClient licenceFeMsgTemplateClient;

    @Autowired
    private NotificationHelper notificationHelper;

    @Autowired
    private AssistedReproductionClient assistedReproductionClient;

    @Autowired
    private DocInfoService docInfoService;
    @Autowired
    private LicenceClient licenceClient;
    /**
     * Step: Start
     *
     * @param bpc
     */
    public void doStart(BaseProcessClass bpc) {
        DsConfigHelper.clearTopSession(bpc.request);
        DsConfigHelper.initTopConfig(bpc.request);
        ParamUtil.setSessionAttr(bpc.request, "doctorInformationPE", null);
        log.info(StringUtil.changeForLog("-----" + this.getClass().getSimpleName() + " Start -----"));
        DataSubmissionHelper.clearSession(bpc.request);
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_DATA_SUBMISSION, AuditTrailConsts.FUNCTION_ONLINE_ENQUIRY_TOP);

        ParamUtil.setSessionAttr(bpc.request, SUBMIT_FLAG, null);
    }

    /**
     * Step: PrepareSwitch
     *
     * @param bpc
     */
    public void prepareSwitch(BaseProcessClass bpc) {
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(bpc.request);
        if(topSuperDataSubmissionDto==null){

            topSuperDataSubmissionDto=initTopSuperDataSubmissionDto(bpc.request);
            DataSubmissionHelper.setCurrentTopDataSubmission(topSuperDataSubmissionDto, bpc.request);
        }
        if(DataSubmissionConsts.DS_APP_TYPE_RFC.equals(topSuperDataSubmissionDto.getDataSubmissionDto().getAppType())){
            retrieveHciCode(bpc.request, topSuperDataSubmissionDto);
            if(!StringUtil.isEmpty(topSuperDataSubmissionDto.getDataSubmissionDto().getDeclaration())){
                topSuperDataSubmissionDto.getDataSubmissionDto().getDeclaration();
            }
            String crud_action_type = ParamUtil.getRequestString(bpc.request, DataSubmissionConstant.CRUD_TYPE);
            if (StringUtil.isEmpty(crud_action_type)){
                crud_action_type="";
            }
            if(crud_action_type.equals("rfc")){
                ParamUtil.setRequestAttr(bpc.request, "patientMotionless","motionles");
                if(!crud_action_type.equals("resume") && !crud_action_type.equals("delete")){
                    DataSubmissionDto dataSubmissionDto=topSuperDataSubmissionDto.getDataSubmissionDto();
                    String orgId = "";
                    String userId = "";
                    LoginContext loginContext = DataSubmissionHelper.getLoginContext(bpc.request);
                    if (loginContext != null) {
                        orgId = loginContext.getOrgId();
                        userId = loginContext.getUserId();
                    }
//                    if (topDataSubmissionService.getTopSuperDataSubmissionDtoRfcDraftByConds(orgId,DataSubmissionConsts.TOP_TYPE_SBT_TERMINATION_OF_PRE,dataSubmissionDto.getId(),userId) != null) {
//                        ParamUtil.setRequestAttr(bpc.request, "hasDrafts", Boolean.TRUE);
//                    }
                }
            }
            //draft
            if (crud_action_type.equals("resume")) {
                String userId = "";
                LoginContext loginContext = DataSubmissionHelper.getLoginContext(bpc.request);
                if (loginContext != null) {
                    userId = loginContext.getUserId();
                }
                topSuperDataSubmissionDto = topDataSubmissionService.getTopSuperDataSubmissionDtoRfcDraftByConds(topSuperDataSubmissionDto.getOrgId(),topSuperDataSubmissionDto.getSubmissionType(), topSuperDataSubmissionDto.getDataSubmissionDto().getId(),userId);
                if (topSuperDataSubmissionDto == null) {
                    log.warn("Can't resume data!");
                    topSuperDataSubmissionDto = new TopSuperDataSubmissionDto();
                }
                DataSubmissionHelper.setCurrentTopDataSubmission(topSuperDataSubmissionDto,bpc.request);
                ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CRUD_ACTION_TYPE_TOP,DataSubmissionConstant.PAGE_STAGE_PAGE);
            } else if (crud_action_type.equals("delete")) {
                topDataSubmissionService.deleteTopSuperDataSubmissionDtoRfcDraftByConds(topSuperDataSubmissionDto.getOrgId(), DataSubmissionConsts.TOP_TYPE_SBT_TERMINATION_OF_PRE,topSuperDataSubmissionDto.getDataSubmissionDto().getId());
            }
        }

        DsConfigHelper.initTopConfig(bpc.request);
        String actionType = getActionType(bpc.request);

        log.info(StringUtil.changeForLog("Action Type: " + actionType));
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CRUD_ACTION_TYPE_TOP, actionType);

        DsConfig config = DsConfigHelper.getCurrentConfig(DataSubmissionConsts.DS_TOP, bpc.request);
        String smallTitle = "";
        if (config != null) {
            smallTitle = config.getText();
        }
        Map<String, PremisesDto> premisesMap =
                (Map<String, PremisesDto>) bpc.request.getSession().getAttribute(DataSubmissionConstant.TOP_PREMISES_MAP);
        if (premisesMap == null || premisesMap.isEmpty()) {
            LoginContext loginContext = DataSubmissionHelper.getLoginContext(bpc.request);
            String licenseeId = null;
            if (loginContext != null) {
                licenseeId = loginContext.getLicenseeId();
            }
            premisesMap = topDataSubmissionService.getTopCenterPremises(licenseeId);
        }

        ParamUtil.setSessionAttr(bpc.request,COUNSE_LLING_PLACE,(Serializable) topDataSubmissionService.getSourseList(bpc.request));
        ParamUtil.setSessionAttr(bpc.request,COUNSE_LLING_PLACE_AGES,(Serializable) topDataSubmissionService.getSourseListAge(bpc.request));
        ParamUtil.setSessionAttr(bpc.request,TOP_PLACE,(Serializable) topDataSubmissionService.getSourseLists(bpc.request));
        ParamUtil.setSessionAttr(bpc.request,TOP_DRUG_PLACE,(Serializable) topDataSubmissionService.getSourseListsDrug(bpc.request));
        List<SelectOption> topCenterSelOpts = DataSubmissionHelper.genPremisesOptions(premisesMap);
        ParamUtil.setSessionAttr(bpc.request,"topCenterSelOpts", (Serializable) topCenterSelOpts);


        TerminationOfPregnancyDto terminationOfPregnancyDto=topSuperDataSubmissionDto.getTerminationOfPregnancyDto();
        if(terminationOfPregnancyDto==null){
            terminationOfPregnancyDto=new TerminationOfPregnancyDto();
        }
        PatientInformationDto patientInformationDto=terminationOfPregnancyDto.getPatientInformationDto();
        if(patientInformationDto==null){
            patientInformationDto=new PatientInformationDto();
        }
        if(!StringUtil.isEmpty(patientInformationDto.getPatientAge())){
            try {
                patientInformationDto.setPatientAge(Formatter.getAge(patientInformationDto.getBirthData()));
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
            /*if (patientInformationDto.getPatientAge()<=16 || patientInformationDto.getPatientAge()>=65) {
                ParamUtil.setRequestAttr(bpc.request, "showPatientAgePT", AppConsts.YES);
            }*/
        }
        TopSuperDataSubmissionDto superDto = DataSubmissionHelper.getCurrentTopDataSubmission(bpc.request);
        ParamUtil.setRequestAttr(bpc.request, "title", DataSubmissionHelper.getMainTitle(superDto.getAppType()));
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", DataSubmissionHelper.getSmallTitle(DataSubmissionConsts.DS_TOP,
                superDto.getAppType(), superDto.getSubmissionType()));


    }
    private String getActionType(HttpServletRequest request) {
        String actionType = (String) ParamUtil.getRequestAttr(request, DataSubmissionConstant.CRUD_ACTION_TYPE_TOP);
        if (StringUtil.isEmpty(actionType)) {
            actionType = DataSubmissionHelper.initAction(DataSubmissionConsts.DS_TOP, DsConfigHelper.TOP_STEP_PATIENT, request);
        }
        return actionType;
    }

    /**
     * Step: PrepareStepData
     *
     * @param bpc
     */
    public void prepareStepData(BaseProcessClass bpc) {
        String submitFlag = (String) ParamUtil.getSessionAttr(bpc.request, SUBMIT_FLAG);
        if (!StringUtil.isEmpty(submitFlag)) {
            throw new IaisRuntimeException("Double Submit");
        }
        TopSuperDataSubmissionDto topSuperDataSubmissionDto =DataSubmissionHelper.getCurrentTopDataSubmission(bpc.request);
        if (topSuperDataSubmissionDto == null) {
            topSuperDataSubmissionDto =new TopSuperDataSubmissionDto();
        }
        String crud_action_type = ParamUtil.getRequestString(bpc.request, DataSubmissionConstant.CRUD_ACTION_TYPE_TOP);
        if (StringUtil.isEmpty(crud_action_type)){
            crud_action_type="";
        }
        if(DataSubmissionConsts.DS_APP_TYPE_NEW.equals(topSuperDataSubmissionDto.getDataSubmissionDto().getAppType())){
            //draft
            if (crud_action_type.equals("resume")) {
                String userId = "";
                LoginContext loginContext = DataSubmissionHelper.getLoginContext(bpc.request);
                if (loginContext != null) {
                    userId = loginContext.getUserId();
                }
                topSuperDataSubmissionDto = topDataSubmissionService.getTopSuperDataSubmissionDtoDraftByConds(topSuperDataSubmissionDto.getOrgId(),topSuperDataSubmissionDto.getSubmissionType(),userId);
                if (topSuperDataSubmissionDto == null) {
                    log.warn("Can't resume data!");
                    topSuperDataSubmissionDto = new TopSuperDataSubmissionDto();
                }
                DataSubmissionHelper.setCurrentTopDataSubmission(topSuperDataSubmissionDto,bpc.request);
                ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CRUD_ACTION_TYPE_TOP,DataSubmissionConstant.PAGE_STAGE_PAGE);
            } else if (crud_action_type.equals("delete")) {
                topDataSubmissionService.deleteTopSuperDataSubmissionDtoDraftByConds(topSuperDataSubmissionDto.getOrgId(), DataSubmissionConsts.TOP_TYPE_SBT_TERMINATION_OF_PRE,topSuperDataSubmissionDto.getAppType());
                topSuperDataSubmissionDto=initTopSuperDataSubmissionDto(bpc.request);
                DataSubmissionHelper.setCurrentTopDataSubmission(topSuperDataSubmissionDto, bpc.request);
            }
        }

        List<DsConfig> configList =DsConfigHelper.initTopConfig(bpc.request);

        for (DsConfig cfg:configList
        ) {
            TerminationOfPregnancyDto terminationOfPregnancyDto =  topSuperDataSubmissionDto.getTerminationOfPregnancyDto();

            if(terminationOfPregnancyDto!=null&&!cfg.getCode().equals(DsConfigHelper.TOP_STEP_PREVIEW)&&!cfg.getCode().equals(DsConfigHelper.TOP_STEP_PATIENT)){
                int status=0;

                 if (DsConfigHelper.TOP_STEP_PLANNING.equals(cfg.getCode())&&terminationOfPregnancyDto.getFamilyPlanDto()!=null) {
                    ValidationResult result = WebValidationHelper.validateProperty(terminationOfPregnancyDto.getFamilyPlanDto(),"TOP");
                    status = result.isHasErrors()?0:1;
                }else if (DsConfigHelper.TOP_STEP_PRE_TERMINATION.equals(cfg.getCode())&&terminationOfPregnancyDto.getPreTerminationDto()!=null) {
                    ValidationResult result = WebValidationHelper.validateProperty(terminationOfPregnancyDto.getPreTerminationDto(),"TOP");
                    status = result.isHasErrors()?0:1;
                }else if (DsConfigHelper.TOP_STEP_PRESENT_TERMINATION.equals(cfg.getCode())) {
                     PreTerminationDto preTerminationDto= terminationOfPregnancyDto.getPreTerminationDto();
                     if(needDoTop(preTerminationDto)){
                         if(terminationOfPregnancyDto.getTerminationDto()==null){
                             ValidationResult result = WebValidationHelper.validateProperty(new TerminationDto(),"TOP");
                             status = result.isHasErrors()?0:1;
                         }else {
                             ValidationResult result = WebValidationHelper.validateProperty(terminationOfPregnancyDto.getTerminationDto(),"TOP");
                             status = result.isHasErrors()?0:1;
                         }
                     }else {
                         status =1;
                     }
                }else if (DsConfigHelper.TOP_STEP_POST_TERMINATION.equals(cfg.getCode())) {
                    PreTerminationDto preTerminationDto= terminationOfPregnancyDto.getPreTerminationDto();
                    if(needDoTop(preTerminationDto)){
                        if(terminationOfPregnancyDto.getPostTerminationDto()==null){
                            ValidationResult result = WebValidationHelper.validateProperty(new PostTerminationDto(),"TOP");
                            status = result.isHasErrors()?0:1;
                        }else {
                            ValidationResult result = WebValidationHelper.validateProperty(terminationOfPregnancyDto.getPostTerminationDto(),"TOP");
                            status = result.isHasErrors()?0:1;
                        }

                    }else {
                        status =1;
                    }
                }
                cfg.setStatus(status);

                DsConfigHelper.setConfig(DataSubmissionConsts.DS_TOP, cfg, bpc.request);
            }
        }
        DataSubmissionHelper.setCurrentTopDataSubmission(topSuperDataSubmissionDto, bpc.request);
        String pageStage = DataSubmissionConstant.PAGE_STAGE_PAGE;
        DsConfig currentConfig = DsConfigHelper.getCurrentConfig(DataSubmissionConsts.DS_TOP, bpc.request);
        if (DsConfigHelper.TOP_STEP_PREVIEW.equals(currentConfig.getCode())) {
            pageStage = DataSubmissionConstant.PAGE_STAGE_PREVIEW;
        }
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, pageStage);
        String currentCode = currentConfig.getCode();
        log.info(StringUtil.changeForLog(" ----- PrepareStepData Step Code: " + currentCode + " ------ "));
        if(DsConfigHelper.TOP_STEP_PATIENT.equals(currentCode)){
            preparePatientInformation(bpc.request);
        }else if (DsConfigHelper.TOP_STEP_PLANNING.equals(currentCode)) {
            prepareFamilyPlanning(bpc.request);
        }else if (DsConfigHelper.TOP_STEP_PRE_TERMINATION.equals(currentCode)) {
            preparePreTermination(bpc.request);
        }else if (DsConfigHelper.TOP_STEP_PRESENT_TERMINATION.equals(currentCode)) {
            preparePresentTermination(bpc.request);
        }else if (DsConfigHelper.TOP_STEP_POST_TERMINATION.equals(currentCode)) {
            preparePostTermination(bpc.request);
        }
        TerminationOfPregnancyDto terminationOfPregnancyDto= topSuperDataSubmissionDto.getTerminationOfPregnancyDto();
        if(StringUtil.isEmpty(terminationOfPregnancyDto)){
            terminationOfPregnancyDto=new TerminationOfPregnancyDto();
        }
        PatientInformationDto patientInformationDto= terminationOfPregnancyDto.getPatientInformationDto();
        if(patientInformationDto!=null&&!StringUtil.isEmpty(patientInformationDto.getBirthData())){
            ParamUtil.setSessionAttr(bpc.request, "birthDate",topSuperDataSubmissionDto.getTerminationOfPregnancyDto().getPatientInformationDto().getBirthData());
        }
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.PRINT_FLAG, DataSubmissionConstant.PRINT_FLAG_TOP);
//        if(StringUtil.isEmpty(COUNSELLING)){
//          COUNSELLING = dsLicenceService.getCounselling();
//        }
//        ParamUtil.setRequestAttr(bpc.request,"counselling",COUNSELLING);
    }

    private void retrieveHciCode(HttpServletRequest request ,TopSuperDataSubmissionDto topSuperDataSubmissionDto) {
        Map<String, PremisesDto> premisesMap =
                (Map<String, PremisesDto>) request.getSession().getAttribute(DataSubmissionConstant.TOP_PREMISES_MAP);
        if (premisesMap == null || premisesMap.isEmpty()) {
            LoginContext loginContext = DataSubmissionHelper.getLoginContext(request);
            String licenseeId = null;
            if (loginContext != null) {
                licenseeId = loginContext.getLicenseeId();
            }
            premisesMap = topDataSubmissionService.getTopCenterPremises(licenseeId);
        }
        if (premisesMap.size() !=0) {
            List<PremisesDto> premisesDtos= IaisCommonUtils.genNewArrayList();
            premisesDtos.addAll(premisesMap.values());
            premisesDtos.sort(this::comparePremisesDto);
            topSuperDataSubmissionDto.setPremisesDto(premisesDtos.get(0));
        }
    }

    private int comparePremisesDto(PremisesDto a, PremisesDto b){
        if(a != null && b!=null){
            try{
                if(StringUtil.isEmpty(a.getBusinessName())){
                    return -1;
                }
                if(StringUtil.isEmpty(b.getBusinessName())){
                    return 1;
                }
                String aBn=a.getBusinessName().toUpperCase();
                String bBn=b.getBusinessName().toUpperCase();

                return aBn.compareTo(bBn);
            }catch (Exception e){
                log.error(e.getMessage(),e);

            }
        }
        return 0;
    }
    /**
     * Step: DoStep
     *
     * @param bpc
     */
    public void doStep(BaseProcessClass bpc) {
        String crudType = ParamUtil.getString(bpc.request, DataSubmissionConstant.CRUD_TYPE);
        if ("return".equals(crudType)) {
            return;
        }
        DsConfig currentConfig = DsConfigHelper.getCurrentConfig(DataSubmissionConsts.DS_TOP, bpc.request);
        String currentCode = currentConfig.getCode();
        log.info(StringUtil.changeForLog(" ----- DoStep Step Code: " + currentCode + " ------ "));
        int status = 0; // 0: current page; -1: back / previous; 1: next
        if("previous".equals(crudType)){
            currentConfig.setStatus(status);
            DsConfigHelper.setConfig(DataSubmissionConsts.DS_TOP, currentConfig, bpc.request);
        }
        if(DsConfigHelper.TOP_STEP_PATIENT.equals(currentCode)){
            status = doPatientInformation(bpc.request);
        }else if (DsConfigHelper.TOP_STEP_PLANNING.equals(currentCode)) {
            status = doFamilyPlanning(bpc.request);
        }else if (DsConfigHelper.TOP_STEP_PRE_TERMINATION.equals(currentCode)) {
            status = doPreTermination(bpc.request);
        }else if (DsConfigHelper.TOP_STEP_PRESENT_TERMINATION.equals(currentCode)) {
            TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(bpc.request);
            TerminationOfPregnancyDto terminationOfPregnancyDto =  topSuperDataSubmissionDto.getTerminationOfPregnancyDto();
            PreTerminationDto preTerminationDto= terminationOfPregnancyDto.getPreTerminationDto();
            if(needDoTop(preTerminationDto)){
                status = doPresentTermination(bpc.request);
            }else {
                status =1;
            }
        }else if (DsConfigHelper.TOP_STEP_POST_TERMINATION.equals(currentCode)) {
            TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(bpc.request);
            TerminationOfPregnancyDto terminationOfPregnancyDto =  topSuperDataSubmissionDto.getTerminationOfPregnancyDto();
            PreTerminationDto preTerminationDto= terminationOfPregnancyDto.getPreTerminationDto();
            if(needDoTop(preTerminationDto)){
                status = doPostTermination(bpc.request);
            }else {
                status =1;
            }
        }else if(DsConfigHelper.TOP_STEP_PREVIEW.equals(currentCode)){
            status = doPreview(bpc.request);
        }
        if("next".equals(crudType)|| DataSubmissionHelper.isToNextAction(bpc.request)){
            currentConfig.setStatus(status);
            DsConfigHelper.setConfig(DataSubmissionConsts.DS_TOP, currentConfig, bpc.request);
        }
        topDataSubmissionService.displayToolTipJudgement(bpc.request);
        log.info(StringUtil.changeForLog(" ----- DoStep Status: " + status + " ------ "));
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.ACTION_STATUS, status);

        ParamUtil.setRequestAttr(bpc.request, "currentStage", DataSubmissionConstant.PAGE_STAGE_PAGE);
    }

    private void preparePatientInformation(HttpServletRequest request) {
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(request);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.TOP_DATA_SUBMISSION, topSuperDataSubmissionDto);
        if (topSuperDataSubmissionDto == null) {
            topSuperDataSubmissionDto =new TopSuperDataSubmissionDto();
        }
        TerminationOfPregnancyDto terminationOfPregnancyDto=topSuperDataSubmissionDto.getTerminationOfPregnancyDto();
        if(terminationOfPregnancyDto==null){
            terminationOfPregnancyDto=new TerminationOfPregnancyDto();
        }
        PatientInformationDto patientInformationDto= terminationOfPregnancyDto.getPatientInformationDto();
        if(StringUtil.isEmpty(patientInformationDto)){
            patientInformationDto=new PatientInformationDto();
        }
        terminationOfPregnancyDto.setPatientInformationDto(patientInformationDto);
        topSuperDataSubmissionDto.setTerminationOfPregnancyDto(terminationOfPregnancyDto);
        DataSubmissionHelper.setCurrentTopDataSubmission(topSuperDataSubmissionDto, request);
    }

    private void prepareFamilyPlanning(HttpServletRequest request) {
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(request);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.TOP_DATA_SUBMISSION, topSuperDataSubmissionDto);
    }
    private void preparePreTermination(HttpServletRequest request) {
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(request);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.TOP_DATA_SUBMISSION, topSuperDataSubmissionDto);
    }
    private void preparePresentTermination(HttpServletRequest request) {
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(request);
        TerminationDto terminationDto=topSuperDataSubmissionDto.getTerminationOfPregnancyDto().getTerminationDto();
        if(terminationDto==null){
            terminationDto=new TerminationDto();
        }
        boolean b = true;
        if(StringUtil.isEmpty(terminationDto.getPregnancyOwn())){
            terminationDto.setPregnancyOwn(b);
        }
        if(StringUtil.isEmpty(terminationDto.getTakenOwn())){
            terminationDto.setTakenOwn(b);
        }
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.TOP_DATA_SUBMISSION, topSuperDataSubmissionDto);
    }
    private void preparePostTermination(HttpServletRequest request) {
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(request);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.TOP_DATA_SUBMISSION, topSuperDataSubmissionDto);
        ParamUtil.setRequestAttr(request, DataSubmissionConstant.CURRENT_PAGE_STAGE, ACTION_TYPE_CONFIRM);
    }

    private int doPreview(HttpServletRequest request) {
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(request);
        TerminationOfPregnancyDto terminationOfPregnancyDto = topSuperDataSubmissionDto.getTerminationOfPregnancyDto();
        DataSubmissionDto dataSubmissionDto = topSuperDataSubmissionDto.getDataSubmissionDto();
        String[] declaration = ParamUtil.getStrings(request, "declaration");
        if(declaration != null && declaration.length >0){
            dataSubmissionDto.setDeclaration(declaration[0]);
        }else{
            dataSubmissionDto.setDeclaration(null);
        }
        if(isRfc(request)){
            dataSubmissionDto.setAmendReason(ParamUtil.getString(request,"amendReason"));
            if(isOthers(dataSubmissionDto.getAmendReason())){
                dataSubmissionDto.setAmendReasonOther(ParamUtil.getString(request,"amendReasonOther"));
            }else {
                dataSubmissionDto.setAmendReasonOther(null);
            }
        }

        DataSubmissionHelper.setCurrentTopDataSubmission(topSuperDataSubmissionDto, request);
        if(terminationOfPregnancyDto==null){
            terminationOfPregnancyDto = new TerminationOfPregnancyDto();
        }
        PatientInformationDto patientInformationDto=terminationOfPregnancyDto.getPatientInformationDto();
        if(patientInformationDto==null){
            patientInformationDto=new PatientInformationDto();
        }
        FamilyPlanDto familyPlanDto = terminationOfPregnancyDto.getFamilyPlanDto();
        if(familyPlanDto==null){
            familyPlanDto = new FamilyPlanDto();
        }
        PreTerminationDto preTerminationDto=terminationOfPregnancyDto.getPreTerminationDto();
        if(preTerminationDto==null){
            preTerminationDto = new PreTerminationDto();
        }
        TerminationDto terminationDto = terminationOfPregnancyDto.getTerminationDto();
        PostTerminationDto postTerminationDto = terminationOfPregnancyDto.getPostTerminationDto();

        if(needDoTop(preTerminationDto)){
            if(terminationDto==null){
                terminationDto = new TerminationDto();
            }
            if(postTerminationDto==null){
                postTerminationDto = new PostTerminationDto();
            }
        }
        Map<String,String> errMap = IaisCommonUtils.genNewHashMap();
        String actionType = ParamUtil.getString(request, DataSubmissionConstant.CRUD_TYPE);
        if(isRfc(request)){
            if(StringUtil.isEmpty(dataSubmissionDto.getAmendReason())){
                errMap.put("amendReason","GENERAL_ERR0006");
            }else if(isOthers(dataSubmissionDto.getAmendReason()) && StringUtil.isEmpty(dataSubmissionDto.getAmendReasonOther())){
                errMap.put("amendReasonOther","GENERAL_ERR0006");
            }
        }
        ValidationResult result1 = WebValidationHelper.validateProperty(patientInformationDto,"TOP");
        if(result1.isHasErrors()){
            errMap.putAll(result1.retrieveAll());
            ParamUtil.setRequestAttr(request, "patientInfor", "false");
        }
        ValidationResult result2 = WebValidationHelper.validateProperty(familyPlanDto,"TOP");
        if(result2.isHasErrors()){
            errMap.putAll(result2.retrieveAll());
            ParamUtil.setRequestAttr(request, "familyPlan", "false");
        }
        ValidationResult result3 = WebValidationHelper.validateProperty(preTerminationDto,"TOP");
        if(result3.isHasErrors()){
            errMap.putAll(result3.retrieveAll());
            ParamUtil.setRequestAttr(request, "preTermination", "false");
        }
        if(needDoTop(preTerminationDto)){
            ValidationResult result4;
            if(terminationDto==null){
                result4 = WebValidationHelper.validateProperty(new TerminationDto(), "TOP");
            }else {
                result4 = WebValidationHelper.validateProperty(terminationDto, "TOP");
            }
            if (result4.isHasErrors()) {
                errMap.putAll(result4.retrieveAll());
                ParamUtil.setRequestAttr(request, "termination", "false");
            }
        }
        if(needDoTop(preTerminationDto)){
            ValidationResult result5 ;
            if(postTerminationDto==null){
                result5 = WebValidationHelper.validateProperty(new PostTerminationDto(), "TOP");
            }else {
                result5 = WebValidationHelper.validateProperty(postTerminationDto, "TOP");
            }
            if (result5.isHasErrors()) {
                errMap.putAll(result5.retrieveAll());
                ParamUtil.setRequestAttr(request, "postTermination", "false");
            }
        }
        if("next".equals(actionType) || DataSubmissionHelper.isToNextAction(request)){
            if(declaration == null || declaration.length == 0){
                errMap.put("declaration", "GENERAL_ERR0006");
            }
        }
        if(isRfc(request)){
            if("next".equals(actionType)){
                TopSuperDataSubmissionDto topOldSuperDataSubmissionDto = DataSubmissionHelper.getOldTopSuperDataSubmissionDto(request);
                /*TerminationOfPregnancyDto terminationOfPregnancyDto=topSuperDataSubmissionDto.getTerminationOfPregnancyDto();*/
                if(topOldSuperDataSubmissionDto != null){
                    if(topOldSuperDataSubmissionDto.getTerminationOfPregnancyDto()!= null){
                        if(terminationOfPregnancyDto.getPatientInformationDto().equals(topOldSuperDataSubmissionDto.getTerminationOfPregnancyDto().getPatientInformationDto())){
                            if(terminationOfPregnancyDto.getFamilyPlanDto().equals(topOldSuperDataSubmissionDto.getTerminationOfPregnancyDto().getFamilyPlanDto())){
                                if(terminationOfPregnancyDto.getPreTerminationDto().equals(topOldSuperDataSubmissionDto.getTerminationOfPregnancyDto().getPreTerminationDto())){
                                    if(terminationOfPregnancyDto.getTerminationDto()==null){
                                        if(topOldSuperDataSubmissionDto.getTerminationOfPregnancyDto().getTerminationDto()==null){
                                            ParamUtil.setRequestAttr(request, DataSubmissionConstant.RFC_NO_CHANGE_ERROR, AppConsts.YES);
                                            errMap.put("rfcNOchange","rfcNOchange");
                                        }
                                    }else
                                    if(terminationOfPregnancyDto.getTerminationDto().equals(topOldSuperDataSubmissionDto.getTerminationOfPregnancyDto().getTerminationDto())){
                                        if(terminationOfPregnancyDto.getPostTerminationDto().equals(topOldSuperDataSubmissionDto.getTerminationOfPregnancyDto().getPostTerminationDto())){
                                            ParamUtil.setRequestAttr(request, DataSubmissionConstant.RFC_NO_CHANGE_ERROR, AppConsts.YES);
                                            errMap.put("rfcNOchange","rfcNOchange");

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if(!errMap.isEmpty()){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMAP,errMap);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errMap));
            return 0;
        }
        return 2 ;
    }

    private int doPatientInformation(HttpServletRequest request) {
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(request);
        topSuperDataSubmissionDto = topSuperDataSubmissionDto  == null ? new TopSuperDataSubmissionDto() : topSuperDataSubmissionDto;
        TerminationOfPregnancyDto terminationOfPregnancyDto = topSuperDataSubmissionDto.getTerminationOfPregnancyDto() == null ? new TerminationOfPregnancyDto() : topSuperDataSubmissionDto.getTerminationOfPregnancyDto();
        PatientInformationDto patientInformationDto=terminationOfPregnancyDto.getPatientInformationDto() == null ? new PatientInformationDto() : terminationOfPregnancyDto.getPatientInformationDto();
        FamilyPlanDto familyPlanDto = terminationOfPregnancyDto.getFamilyPlanDto() == null ? new FamilyPlanDto() : terminationOfPregnancyDto.getFamilyPlanDto();
        PreTerminationDto preTerminationDto=terminationOfPregnancyDto.getPreTerminationDto() == null ? new PreTerminationDto() : terminationOfPregnancyDto.getPreTerminationDto();
        TerminationDto terminationDto = terminationOfPregnancyDto.getTerminationDto();
        PostTerminationDto postTerminationDto = terminationOfPregnancyDto.getPostTerminationDto();

        if(needDoTop(preTerminationDto)){
            if(terminationDto==null){
                terminationDto = new TerminationDto();
            }
            if(postTerminationDto==null){
                postTerminationDto = new PostTerminationDto();
            }
        }
        ControllerHelper.get(request, patientInformationDto);
        if(StringUtil.isNotEmpty(patientInformationDto.getIdNumber())){
            patientInformationDto.setIdNumber(patientInformationDto.getIdNumber().toUpperCase());
        }
        String[] livingChildrenGenders= ParamUtil.getStrings(request, "livingChildrenGenders");
        if(StringUtil.isNumber(patientInformationDto.getLivingChildrenNo()) && !StringUtil.isEmpty(patientInformationDto.getLivingChildrenNo())){
            if (Integer.parseInt(patientInformationDto.getLivingChildrenNo()) > 0) {
                if( !IaisCommonUtils.isEmpty(livingChildrenGenders)){

                    patientInformationDto.setLivingChildrenGenders(Arrays.asList(livingChildrenGenders));
                }else{
                    patientInformationDto.setLivingChildrenGenders(null);
                }
            } else if(Integer.parseInt(patientInformationDto.getLivingChildrenNo()) <= 0){
                patientInformationDto.setLivingChildrenGenders(null);
            }
        }
        if(StringUtil.isEmpty(patientInformationDto.getOrgId())){
            patientInformationDto.setOrgId(topSuperDataSubmissionDto.getOrgId());
        }
        terminationOfPregnancyDto.setPatientInformationDto(patientInformationDto);
        topSuperDataSubmissionDto.setTerminationOfPregnancyDto(terminationOfPregnancyDto);
        try {
            String birthDate = terminationOfPregnancyDto.getPatientInformationDto().getBirthData();
            if(StringUtil.isNotEmpty(terminationOfPregnancyDto.getPreTerminationDto().getCounsellingDate())){
                String counsellingGiven = terminationOfPregnancyDto.getPreTerminationDto().getCounsellingDate();
                int ageNew=Formatter.getAge(birthDate,counsellingGiven);
                terminationOfPregnancyDto.getPreTerminationDto().setCounsellingAge(ageNew);
                if(ageNew>=16&&"AR_SC_001".equals(terminationOfPregnancyDto.getPreTerminationDto().getCounsellingPlace())){
                    terminationOfPregnancyDto.getPreTerminationDto().setCounsellingPlace(null);
                }

            }
        }catch (Exception e){

            log.error(StringUtil.changeForLog("setCounsellingAge is error"));
        }
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.TOP_DATA_SUBMISSION, topSuperDataSubmissionDto);
        Map<String,String> errMap = IaisCommonUtils.genNewHashMap();
        //for ds center validation
        LoginContext login = AccessUtil.getLoginUser(request);
        List<DsCenterDto> centerDtos = licenceClient.getDsCenterDtosByOrgIdAndCentreType(login.getOrgId(), DataSubmissionConsts.DS_TOP).getEntity();
        if (IaisCommonUtils.isEmpty(centerDtos)) {
            errMap.put("topErrorMsg", "DS_ERR070");
        }
        String actionType = ParamUtil.getString(request, DataSubmissionConstant.CRUD_TYPE);
        if("next".equals(actionType)|| DataSubmissionHelper.isToNextAction(request)){
            ValidationResult result = WebValidationHelper.validateProperty(patientInformationDto,"TOP");
            if(result !=null){
                errMap.putAll(result.retrieveAll());
            }
        }
        if(!errMap.isEmpty()){
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errMap));
            return 0;
        }
        if(DsConfigHelper.TOP_STEP_PRE_TERMINATION.equals(actionType)){
            ValidationResult result = WebValidationHelper.validateProperty(familyPlanDto,"TOP");
            if(result !=null){
                errMap.putAll(result.retrieveAll());
            }
            if(!errMap.isEmpty()){
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errMap));
                return 2;
            }
        }
        if(DsConfigHelper.TOP_STEP_PRESENT_TERMINATION.equals(actionType)){
            ValidationResult result2 = WebValidationHelper.validateProperty(familyPlanDto,"TOP");
            if(result2 !=null){
                errMap.putAll(result2.retrieveAll());
            }
            if(!errMap.isEmpty()){
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errMap));
                return 2;
            }
            if(!StringUtil.isEmpty(preTerminationDto.getCounsellingGiven())){
                if(preTerminationDto.getCounsellingGiven()){
                    ValidationResult result = WebValidationHelper.validateProperty(preTerminationDto,"TOPY");
                    if(result !=null){
                        errMap.putAll(result.retrieveAll());
                    }
                }else if(!preTerminationDto.getCounsellingGiven()){
                    ValidationResult result = WebValidationHelper.validateProperty(preTerminationDto,"TOPN");
                    if(result !=null){
                        errMap.putAll(result.retrieveAll());
                    }
                }
            } else {
                ValidationResult result = WebValidationHelper.validateProperty(preTerminationDto,"TOP");
                if(result !=null){
                    errMap.putAll(result.retrieveAll());
                }
            }
            if(!errMap.isEmpty()){
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errMap));
                return 3;
            }
        }
        if(DsConfigHelper.TOP_STEP_POST_TERMINATION.equals(actionType)){
            ValidationResult result2 = WebValidationHelper.validateProperty(familyPlanDto,"TOP");
            if(result2 !=null){
                errMap.putAll(result2.retrieveAll());
            }
            if(!errMap.isEmpty()){
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errMap));
                return 2;
            }
            if(!StringUtil.isEmpty(preTerminationDto.getCounsellingGiven())){
                if(preTerminationDto.getCounsellingGiven()){
                    ValidationResult result3 = WebValidationHelper.validateProperty(preTerminationDto,"TOPY");
                    if(result3 !=null){
                        errMap.putAll(result3.retrieveAll());
                    }
                }else if(!preTerminationDto.getCounsellingGiven()){
                    ValidationResult result3 = WebValidationHelper.validateProperty(preTerminationDto,"TOPN");
                    if(result3 !=null){
                        errMap.putAll(result3.retrieveAll());
                    }
                }
            } else {
                ValidationResult result3 = WebValidationHelper.validateProperty(preTerminationDto,"TOP");
                if(result3 !=null){
                    errMap.putAll(result3.retrieveAll());
                }
            }
            if(!errMap.isEmpty()){
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errMap));
                return 3;
            }
            if(needDoTop(preTerminationDto)){
                ValidationResult result ;
                if(terminationDto==null){
                    result = WebValidationHelper.validateProperty(new TerminationDto(), "TOP");
                }else {
                    result = WebValidationHelper.validateProperty(terminationDto, "TOP");
                }
                if (result != null) {
                    errMap.putAll(result.retrieveAll());
                }
            }
            if(!errMap.isEmpty()){
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errMap));
                return 4;
            }
        }
        if(DsConfigHelper.TOP_STEP_PREVIEW.equals(actionType)){
            ValidationResult result2 = WebValidationHelper.validateProperty(familyPlanDto,"TOP");
            if(result2 !=null){
                errMap.putAll(result2.retrieveAll());
            }
            if(!errMap.isEmpty()){
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errMap));
                return 2;
            }
            if(!StringUtil.isEmpty(preTerminationDto.getCounsellingGiven())){
                if(preTerminationDto.getCounsellingGiven()){
                    ValidationResult result3 = WebValidationHelper.validateProperty(preTerminationDto,"TOPY");
                    if(result3 !=null){
                        errMap.putAll(result3.retrieveAll());
                    }
                }else if(!preTerminationDto.getCounsellingGiven()){
                    ValidationResult result3 = WebValidationHelper.validateProperty(preTerminationDto,"TOPN");
                    if(result3 !=null){
                        errMap.putAll(result3.retrieveAll());
                    }
                }
            } else {
                ValidationResult result3 = WebValidationHelper.validateProperty(preTerminationDto,"TOP");
                if(result3 !=null){
                    errMap.putAll(result3.retrieveAll());
                }
            }
            if(!errMap.isEmpty()){
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errMap));
                return 3;
            }
            if(needDoTop(preTerminationDto)){
                ValidationResult result4 ;
                if(terminationDto==null){
                    result4 = WebValidationHelper.validateProperty(new TerminationDto(), "TOP");
                }else {
                    result4 = WebValidationHelper.validateProperty(terminationDto, "TOP");
                }
                if (result4 != null) {
                    errMap.putAll(result4.retrieveAll());
                }
            }
            if(!errMap.isEmpty()){
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errMap));
                return 4;
            }
            if(needDoTop(preTerminationDto)){
                ValidationResult result ;
                if(postTerminationDto==null){
                    result = WebValidationHelper.validateProperty(new PostTerminationDto(), "TOP");
                }else {
                    result = WebValidationHelper.validateProperty(postTerminationDto, "TOP");
                }
                if(result !=null){
                    errMap.putAll(result.retrieveAll());
                }
            }

        }
        if(!errMap.isEmpty()){
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errMap));
            return 5;
        }
        return 1;
    }

    private int doFamilyPlanning(HttpServletRequest request) {
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(request);
        topSuperDataSubmissionDto = topSuperDataSubmissionDto  == null ? new TopSuperDataSubmissionDto() : topSuperDataSubmissionDto;
        TerminationOfPregnancyDto terminationOfPregnancyDto = topSuperDataSubmissionDto.getTerminationOfPregnancyDto() == null ? new TerminationOfPregnancyDto() : topSuperDataSubmissionDto.getTerminationOfPregnancyDto();
        FamilyPlanDto familyPlanDto = terminationOfPregnancyDto.getFamilyPlanDto() == null ? new FamilyPlanDto() : terminationOfPregnancyDto.getFamilyPlanDto();
        PreTerminationDto preTerminationDto=terminationOfPregnancyDto.getPreTerminationDto() == null ? new PreTerminationDto() : terminationOfPregnancyDto.getPreTerminationDto();
        TerminationDto terminationDto = terminationOfPregnancyDto.getTerminationDto();
        PostTerminationDto postTerminationDto = terminationOfPregnancyDto.getPostTerminationDto();

        if(needDoTop(preTerminationDto)){
            if(terminationDto==null){
                terminationDto = new TerminationDto();
            }
            if(postTerminationDto==null){
                postTerminationDto = new PostTerminationDto();
            }
        }
        PatientInformationDto patientInformationDto=terminationOfPregnancyDto.getPatientInformationDto();
        if(patientInformationDto==null){
            patientInformationDto=new PatientInformationDto();
        }
        ControllerHelper.get(request, familyPlanDto);
        try {
            int ageNew=Formatter.getAge(patientInformationDto.getBirthData());

            patientInformationDto.setPatientAge(ageNew);
        }catch (Exception e){
            log.error(StringUtil.changeForLog("setPatientAge is error"));

        }
        if(!StringUtil.isEmpty(patientInformationDto.getPatientAge()) && patientInformationDto.getPatientAge()<16){
            familyPlanDto.setNeedHpbConsult(true);
        }
        if(StringUtil.isDigit(familyPlanDto.getGestAgeBaseOnUltrWeek())){
            if(Integer.parseInt(familyPlanDto.getGestAgeBaseOnUltrWeek())<15){
                familyPlanDto.setAbortChdMoreWksGender(null);
            }
        }else {
            familyPlanDto.setAbortChdMoreWksGender(null);
        }
        terminationOfPregnancyDto.setFamilyPlanDto(familyPlanDto);
        topSuperDataSubmissionDto.setTerminationOfPregnancyDto(terminationOfPregnancyDto);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.TOP_DATA_SUBMISSION, topSuperDataSubmissionDto);
        request.getSession().setAttribute(DataSubmissionConstant.TOP_DATA_SUBMISSION, topSuperDataSubmissionDto);
        Map<String,String> errMap = IaisCommonUtils.genNewHashMap();
        String actionType = ParamUtil.getString(request, DataSubmissionConstant.CRUD_TYPE);
        if("next".equals(actionType)|| DataSubmissionHelper.isToNextAction(request)){
            ValidationResult result = WebValidationHelper.validateProperty(familyPlanDto,"TOP");
            if(result !=null){
                errMap.putAll(result.retrieveAll());
            }
        }
        if(!errMap.isEmpty()){
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errMap));
            return 0;
        }
        if(DsConfigHelper.TOP_STEP_PRESENT_TERMINATION.equals(actionType)){
            if(!StringUtil.isEmpty(preTerminationDto.getCounsellingGiven())){
                if(preTerminationDto.getCounsellingGiven()){
                    ValidationResult result = WebValidationHelper.validateProperty(preTerminationDto,"TOPY");
                    if(result !=null){
                        errMap.putAll(result.retrieveAll());
                    }
                }else if(!preTerminationDto.getCounsellingGiven()){
                    ValidationResult result = WebValidationHelper.validateProperty(preTerminationDto,"TOPN");
                    if(result !=null){
                        errMap.putAll(result.retrieveAll());
                    }
                }
            } else {
                ValidationResult result = WebValidationHelper.validateProperty(preTerminationDto,"TOP");
                if(result !=null){
                    errMap.putAll(result.retrieveAll());
                }
            }
            if(!errMap.isEmpty()){
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errMap));
                return 3;
            }
        }
        if(DsConfigHelper.TOP_STEP_POST_TERMINATION.equals(actionType)){
            if(!StringUtil.isEmpty(preTerminationDto.getCounsellingGiven())){
                if(preTerminationDto.getCounsellingGiven()){
                    ValidationResult result3 = WebValidationHelper.validateProperty(preTerminationDto,"TOPY");
                    if(result3 !=null){
                        errMap.putAll(result3.retrieveAll());
                    }
                }else if(!preTerminationDto.getCounsellingGiven()){
                    ValidationResult result3 = WebValidationHelper.validateProperty(preTerminationDto,"TOPN");
                    if(result3 !=null){
                        errMap.putAll(result3.retrieveAll());
                    }
                }
            } else {
                ValidationResult result3 = WebValidationHelper.validateProperty(preTerminationDto,"TOP");
                if(result3 !=null){
                    errMap.putAll(result3.retrieveAll());
                }
            }
            if(!errMap.isEmpty()){
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errMap));
                return 3;
            }
            if(needDoTop(preTerminationDto)){
                ValidationResult result ;
                if(terminationDto==null){
                    result = WebValidationHelper.validateProperty(new TerminationDto(), "TOP");
                }else {
                    result = WebValidationHelper.validateProperty(terminationDto, "TOP");
                }
                if (result != null) {
                    errMap.putAll(result.retrieveAll());
                }
            }
            if(!errMap.isEmpty()){
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errMap));
                return 4;
            }
        }
        if(DsConfigHelper.TOP_STEP_PREVIEW.equals(actionType)){
            ValidationResult result2 = WebValidationHelper.validateProperty(familyPlanDto,"TOP");
            if(result2 !=null){
                errMap.putAll(result2.retrieveAll());
            }
            if(!errMap.isEmpty()){
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errMap));
                return 2;
            }
            if(!StringUtil.isEmpty(preTerminationDto.getCounsellingGiven())){
                if(preTerminationDto.getCounsellingGiven()){
                    ValidationResult result3 = WebValidationHelper.validateProperty(preTerminationDto,"TOPY");
                    if(result3 !=null){
                        errMap.putAll(result3.retrieveAll());
                    }
                }else if(!preTerminationDto.getCounsellingGiven()){
                    ValidationResult result3 = WebValidationHelper.validateProperty(preTerminationDto,"TOPN");
                    if(result3 !=null){
                        errMap.putAll(result3.retrieveAll());
                    }
                }
            } else {
                ValidationResult result3 = WebValidationHelper.validateProperty(preTerminationDto,"TOP");
                if(result3 !=null){
                    errMap.putAll(result3.retrieveAll());
                }
            }
            if(!errMap.isEmpty()){
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errMap));
                return 3;
            }
            if(needDoTop(preTerminationDto)){
                ValidationResult result4 ;
                if(terminationDto==null){
                    result4 = WebValidationHelper.validateProperty(new TerminationDto(), "TOP");
                }else {
                    result4 = WebValidationHelper.validateProperty(terminationDto, "TOP");
                }
                if (result4 != null) {
                    errMap.putAll(result4.retrieveAll());
                }
            }
            if(!errMap.isEmpty()){
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errMap));
                return 4;
            }
            if(needDoTop(preTerminationDto)){
                ValidationResult result ;
                if(postTerminationDto==null){
                    result = WebValidationHelper.validateProperty(new PostTerminationDto(), "TOP");
                }else {
                    result = WebValidationHelper.validateProperty(postTerminationDto, "TOP");
                }
                if(result !=null){
                    errMap.putAll(result.retrieveAll());
                }
            }

        }
        if(!errMap.isEmpty()){
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errMap));
            return 5;
        }
        return 1;
    }

    private int doPreTermination(HttpServletRequest request) {
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(request);
        topSuperDataSubmissionDto = topSuperDataSubmissionDto  == null ? new TopSuperDataSubmissionDto() : topSuperDataSubmissionDto;
        TerminationOfPregnancyDto terminationOfPregnancyDto = topSuperDataSubmissionDto.getTerminationOfPregnancyDto() == null ? new TerminationOfPregnancyDto() : topSuperDataSubmissionDto.getTerminationOfPregnancyDto();
        PreTerminationDto preTerminationDto = terminationOfPregnancyDto.getPreTerminationDto() == null ? new PreTerminationDto() : terminationOfPregnancyDto.getPreTerminationDto();
        TerminationDto terminationDto = terminationOfPregnancyDto.getTerminationDto();
        PostTerminationDto postTerminationDto = terminationOfPregnancyDto.getPostTerminationDto();

        if(needDoTop(preTerminationDto)){
            if(terminationDto==null){
                terminationDto = new TerminationDto();
            }
            if(postTerminationDto==null){
                postTerminationDto = new PostTerminationDto();
            }
        }
        ControllerHelper.get(request, preTerminationDto);

        if(StringUtil.isNotEmpty(preTerminationDto.getCounsellorIdNo())){
            preTerminationDto.setCounsellorIdNo(preTerminationDto.getCounsellorIdNo().toUpperCase());
        }
        topSuperDataSubmissionDto.getDataSubmissionDto().setSubmitDt(new Date());
        ParamUtil.setSessionAttr(request, "topDates", null);
        if(StringUtil.isNotEmpty(preTerminationDto.getCounsellingDate())){
            try {
                if(terminationDto!=null&&StringUtil.isNotEmpty(terminationDto.getTopDate())){
                    if(StringUtil.isNotEmpty(preTerminationDto.getCounsellingResult())&&!preTerminationDto.getCounsellingResult().equals("TOPPCR003")){
                        if(preTerminationDto.getCounsellingResult().equals("TOPPCR001")){
                            if(StringUtil.isNotEmpty(preTerminationDto.getSecCounsellingResult())&&!preTerminationDto.getSecCounsellingResult().equals("TOPSP003")&&!preTerminationDto.getSecCounsellingResult().equals("TOPSP001")){
                                if(Formatter.compareDateByDay(terminationDto.getTopDate(),preTerminationDto.getCounsellingDate())<1){
                                    ParamUtil.setSessionAttr(request, "topDates", Boolean.TRUE);
                                }else {
                                    ParamUtil.setSessionAttr(request, "topDates", Boolean.FALSE);
                                }
                            }
                            if(preTerminationDto.getPatientAppointment().equals("0")){
                                if(Formatter.compareDateByDay(terminationDto.getTopDate(),preTerminationDto.getCounsellingDate())<1){
                                    ParamUtil.setSessionAttr(request, "topDates", Boolean.TRUE);
                                }else {
                                    ParamUtil.setSessionAttr(request, "topDates", Boolean.FALSE);
                                }
                            }
                        }else {
                            if(Formatter.compareDateByDay(terminationDto.getTopDate(),preTerminationDto.getCounsellingDate())<1){
                                ParamUtil.setSessionAttr(request, "topDates", Boolean.TRUE);
                            }else {
                                ParamUtil.setSessionAttr(request, "topDates", Boolean.FALSE);
                            }
                        }
                    }
                }
            }catch (Exception e){
                log.error(StringUtil.changeForLog("CounsellingDate is error"));
            }

        }

        if(preTerminationDto.getCounsellingAge()!=null){
            if(preTerminationDto.getCounsellingAge()>=16){
                String counsellingPlace = ParamUtil.getRequestString(request, "counsellingPlace");
                preTerminationDto.setCounsellingPlace(counsellingPlace);
            }else {
                String counsellingPlaceAge = ParamUtil.getRequestString(request, "counsellingPlaceAge");
                preTerminationDto.setCounsellingPlace(counsellingPlaceAge);
            }
        }
        /*ParamUtil.setSessionAttr(request, "counsellingPlace",counsellingPlace);*/
        if(StringUtil.isNotEmpty(preTerminationDto.getSecCounsellingDate())){
            try {
                Integer counsellingAge = ParamUtil.getInt(request, "counsellingAge");
                preTerminationDto.setCounsellingAge(counsellingAge);

            }catch (Exception e){
                log.error(StringUtil.changeForLog("counsellingAge is error"));

            }
        }
        String patientAppointment=ParamUtil.getString(request, "patientAppointment");
        preTerminationDto.setPatientAppointment(patientAppointment);
        terminationOfPregnancyDto.setPreTerminationDto(preTerminationDto);
        setPreTerminationDtoFieldsNull(terminationOfPregnancyDto);
        topSuperDataSubmissionDto.setTerminationOfPregnancyDto(terminationOfPregnancyDto);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.TOP_DATA_SUBMISSION, topSuperDataSubmissionDto);

        Map<String,String> errMap = IaisCommonUtils.genNewHashMap();
        String actionType = ParamUtil.getString(request, DataSubmissionConstant.CRUD_TYPE);
        if("next".equals(actionType) || DataSubmissionHelper.isToNextAction(request)){
            if(preTerminationDto.getCounsellingGiven()!=null){
                if(preTerminationDto.getCounsellingGiven()){
                    ValidationResult result = WebValidationHelper.validateProperty(preTerminationDto,"TOPY");
                    if(result !=null){
                        errMap.putAll(result.retrieveAll());
                    }
                   /* if(StringUtil.isEmpty(preTerminationDto.getCounsellingPlace())){
                        ParamUtil.setSessionAttr(request, "counsellingPlaceError", "This is a mandatory field.");
                    }*/
                }else {
                    ValidationResult result = WebValidationHelper.validateProperty(preTerminationDto,"TOPN");
                    if(result !=null){
                        errMap.putAll(result.retrieveAll());
                    }
                }
            } else {
                ValidationResult result = WebValidationHelper.validateProperty(preTerminationDto,"TOP");
                if(result !=null){
                    errMap.putAll(result.retrieveAll());
                }
            }
        }
        if(!errMap.isEmpty()){
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errMap));
            return 0;
        }
        if(DsConfigHelper.TOP_STEP_POST_TERMINATION.equals(actionType)){
            if(needDoTop(preTerminationDto)){
                ValidationResult result ;
                if(terminationDto==null){
                    result = WebValidationHelper.validateProperty(new TerminationDto(), "TOP");
                }else {
                    result = WebValidationHelper.validateProperty(terminationDto, "TOP");
                }
                if (result != null) {
                    errMap.putAll(result.retrieveAll());
                }
            }
            if(!errMap.isEmpty()){
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errMap));
                return 4;
            }
        }
        if(DsConfigHelper.TOP_STEP_PREVIEW.equals(actionType)){
            if(needDoTop(preTerminationDto)){
                ValidationResult result4 ;
                if(terminationDto==null){
                    result4 = WebValidationHelper.validateProperty(new TerminationDto(), "TOP");
                }else {
                    result4 = WebValidationHelper.validateProperty(terminationDto, "TOP");
                }
                if (result4 != null) {
                    errMap.putAll(result4.retrieveAll());
                }
            }
            if(!errMap.isEmpty()){
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errMap));
                return 4;
            }
            if(needDoTop(preTerminationDto)){
                ValidationResult result ;
                if(postTerminationDto==null){
                    result = WebValidationHelper.validateProperty(new PostTerminationDto(), "TOP");
                }else {
                    result = WebValidationHelper.validateProperty(postTerminationDto, "TOP");
                }
                if(result !=null){
                    errMap.putAll(result.retrieveAll());
                }
            }

        }
        if(!errMap.isEmpty()){
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errMap));
            return 5;
        }
        return 1;
    }

    private void setPreTerminationDtoFieldsNull(TerminationOfPregnancyDto terminationOfPregnancyDto){

        PreTerminationDto preTerminationDto =terminationOfPregnancyDto.getPreTerminationDto();
        FamilyPlanDto familyPlanDto=terminationOfPregnancyDto.getFamilyPlanDto();
        if (familyPlanDto != null) {
            int weeksD = 0;
            int weeksU = 0;
            int weeks = Integer.parseInt(familyPlanDto.getGestAgeBaseOnUltrWeek());
            BigDecimal b1 = new BigDecimal(familyPlanDto.getGestAgeBaseOnUltrDay());
            BigDecimal b2 = new BigDecimal(Integer.toString(7));
            weeksU = weeks + b1.divide(b2, 0, BigDecimal.ROUND_UP).intValue();
            weeksD = weeks + b1.divide(b2, 0, BigDecimal.ROUND_DOWN).intValue();
            if (weeksD < 13 || weeksU > 24) {
                preTerminationDto.setCounsellingGivenOnMin(false);
                preTerminationDto.setPatientSign(false);
            }
        }
        if(preTerminationDto.getCounsellingGiven()!=null){
            if(preTerminationDto.getCounsellingGiven()){
                preTerminationDto.setNoCounsReason(null);
                if(preTerminationDto.getCounsellingResult()!=null&&!preTerminationDto.getCounsellingResult().equals("TOPPCR001")){
                    preTerminationDto.setPatientAppointment(null);

                }else {
                    if(preTerminationDto.getPatientAppointment()!=null&&!preTerminationDto.getPatientAppointment().equals("1")){
                        preTerminationDto.setSecCounsellingDate(null);
                        preTerminationDto.setSecCounsellingResult(null);

                    }
                }
                if(preTerminationDto.getCounsellingPlace()!=null&&preTerminationDto.getCounsellingPlace().equals("AR_SC_001") ){
                    preTerminationDto.setPreCounsNoCondReason(null);
                }
            }else {
                preTerminationDto.setCounsellorIdType(null);
                preTerminationDto.setCounsellorIdNo(null);
                preTerminationDto.setCounsellorName(null);
                preTerminationDto.setCounsellingReignNo(null);
                preTerminationDto.setCounsellingDate(null);
                preTerminationDto.setCounsellingResult(null);
                preTerminationDto.setCounsellingPlace(null);
                preTerminationDto.setCounsellingAge(null);

                preTerminationDto.setPatientAppointment(null);
                preTerminationDto.setSecCounsellingDate(null);
                preTerminationDto.setSecCounsellingResult(null);
                preTerminationDto.setPreCounsNoCondReason(null);
               }

        }


    }

    private int doPresentTermination(HttpServletRequest request) {
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(request);
        topSuperDataSubmissionDto = topSuperDataSubmissionDto  == null ? new TopSuperDataSubmissionDto() : topSuperDataSubmissionDto;
        DoctorInformationDto doctorInformationDto=topSuperDataSubmissionDto.getDoctorInformationDto();
        if(doctorInformationDto==null){
            doctorInformationDto=new DoctorInformationDto();
        }
        TerminationOfPregnancyDto terminationOfPregnancyDto = topSuperDataSubmissionDto.getTerminationOfPregnancyDto() == null ? new TerminationOfPregnancyDto() : topSuperDataSubmissionDto.getTerminationOfPregnancyDto();
        PreTerminationDto preTerminationDto=terminationOfPregnancyDto.getPreTerminationDto() == null ? new PreTerminationDto() : terminationOfPregnancyDto.getPreTerminationDto();
        TerminationDto terminationDto = terminationOfPregnancyDto.getTerminationDto();
        PostTerminationDto postTerminationDto = terminationOfPregnancyDto.getPostTerminationDto();

        if(terminationDto==null){
            terminationDto = new TerminationDto();
        }
        if(postTerminationDto==null){
            postTerminationDto = new PostTerminationDto();
        }
        ControllerHelper.get(request, terminationDto);
        if(terminationDto.getPregnancyOwn()!=null&&terminationDto.getPregnancyOwn()){
            terminationDto.setPrescribeTopPlace(null);
        }
        if(terminationDto.getTakenOwn()!=null&&terminationDto.getTakenOwn()){
            terminationDto.setTopDrugPlace(null);
        }
        if(terminationDto.getPerformedOwn()!=null&&terminationDto.getPerformedOwn()){
            terminationDto.setTopPlace(null);
        }
        topSuperDataSubmissionDto.getDataSubmissionDto().setSubmitDt(new Date());

        ParamUtil.setSessionAttr(request, "topDates", null);
        if(StringUtil.isNotEmpty(preTerminationDto.getCounsellingDate())){
            try {
                if(StringUtil.isNotEmpty(terminationDto.getTopDate())){
                    if(StringUtil.isNotEmpty(preTerminationDto.getCounsellingResult())&&!preTerminationDto.getCounsellingResult().equals("TOPPCR003")){
                        if(preTerminationDto.getCounsellingResult().equals("TOPPCR001")){
                            if(StringUtil.isNotEmpty(preTerminationDto.getSecCounsellingResult())&&!preTerminationDto.getSecCounsellingResult().equals("TOPSP003")&&!preTerminationDto.getSecCounsellingResult().equals("TOPSP001")){
                                if(Formatter.compareDateByDay(terminationDto.getTopDate(),preTerminationDto.getCounsellingDate())<1){
                                    ParamUtil.setSessionAttr(request, "topDates", Boolean.TRUE);
                                }else {
                                    ParamUtil.setSessionAttr(request, "topDates", Boolean.FALSE);
                                }
                            }
                            if(preTerminationDto.getPatientAppointment().equals("0")){
                                if(Formatter.compareDateByDay(terminationDto.getTopDate(),preTerminationDto.getCounsellingDate())<1){
                                    ParamUtil.setSessionAttr(request, "topDates", Boolean.TRUE);
                                }else {
                                    ParamUtil.setSessionAttr(request, "topDates", Boolean.FALSE);
                                }
                            }
                        }else {
                            if(Formatter.compareDateByDay(terminationDto.getTopDate(),preTerminationDto.getCounsellingDate())<1){
                                ParamUtil.setSessionAttr(request, "topDates", Boolean.TRUE);
                            }else {
                                ParamUtil.setSessionAttr(request, "topDates", Boolean.FALSE);
                            }
                        }
                    }
                }
            }catch (Exception e){
                log.error(StringUtil.changeForLog("CounsellingDate is error"));
            }
        }
        ProfessionalResponseDto professionalResponseDto=appSubmissionService.retrievePrsInfo(terminationDto.getDoctorRegnNo());
        DoctorInformationDto doctorInformationDtoELIS=docInfoService.getDoctorInformationDtoByConds(terminationDto.getDoctorRegnNo(),DataSubmissionConsts.DOCTOR_SOURCE_ELIS_TOP, topSuperDataSubmissionDto.getHciCode());
        ParamUtil.setSessionAttr(request, "DoctorELISAndPrs",null);
        if(professionalResponseDto!=null){
            if("-1".equals(professionalResponseDto.getStatusCode()) || "-2".equals(professionalResponseDto.getStatusCode()) || professionalResponseDto.isHasException()){
                if("false".equals(terminationDto.getTopDoctorInformations())){
                    if("true".equals(terminationDto.getDoctorInformationPE())){
                        String doctorName = ParamUtil.getString(request, "names");
                        String dSpeciality = ParamUtil.getString(request, "dSpecialitys");
                        String dSubSpeciality = ParamUtil.getString(request, "dSubSpecialitys");
                        String dQualification = ParamUtil.getString(request, "dQualifications");
                        terminationDto.setDoctorName(doctorName);
                        terminationDto.setSpecialty(dSpeciality);
                        terminationDto.setSubSpecialty(dSubSpeciality);
                        terminationDto.setQualification(dQualification);
                        doctorInformationDto.setName(terminationDto.getDoctorName());
                        doctorInformationDto.setDoctorReignNo(terminationDto.getDoctorRegnNo());
                        doctorInformationDto.setSpeciality(terminationDto.getSpecialty());
                        doctorInformationDto.setSubSpeciality(terminationDto.getSubSpecialty());
                        doctorInformationDto.setQualification(terminationDto.getQualification());
                        doctorInformationDto.setDoctorSource(TOP_DOCTOR_INFO_FROM_ELIS);
                        topSuperDataSubmissionDto.setDoctorInformationDto(doctorInformationDto);
                    }
                }
            }else if("false".equals(terminationDto.getDoctorInformationPE())){
                if(doctorInformationDtoELIS!=null){
                    ParamUtil.setSessionAttr(request, "DoctorELISAndPrs",true);
                    doctorInformationDto.setElis(true);
                }else {
                    ParamUtil.setSessionAttr(request, "DoctorELISAndPrs",false);
                }
                String doctorName = ParamUtil.getString(request, "names");
                doctorInformationDto.setName(doctorName);
                doctorInformationDto.setDoctorReignNo(terminationDto.getDoctorRegnNo());
                doctorInformationDto.setSpeciality(terminationDto.getSpecialty());
                doctorInformationDto.setSubSpeciality(terminationDto.getSubSpecialty());
                doctorInformationDto.setQualification(terminationDto.getQualification());
                doctorInformationDto.setDoctorSource(TOP_DOCTOR_INFO_FROM_PRS);
                topSuperDataSubmissionDto.setDoctorInformationDto(doctorInformationDto);
            }
        }
        if("true".equals(terminationDto.getTopDoctorInformations())){
            String dName = ParamUtil.getString(request, "dName");
            String dSpeciality = ParamUtil.getString(request, "dSpeciality");
            String dSubSpeciality = ParamUtil.getString(request, "dSubSpeciality");
            String dQualification = ParamUtil.getString(request, "dQualification");
            doctorInformationDto.setName(dName);
            doctorInformationDto.setDoctorReignNo(terminationDto.getDoctorRegnNo());
            doctorInformationDto.setSubSpeciality(dSubSpeciality);
            doctorInformationDto.setSpeciality(dSpeciality);
            doctorInformationDto.setQualification(dQualification);
            doctorInformationDto.setDoctorSource(TOP_DOCTOR_INFO_USER_NEW_REGISTER);
            terminationDto.setDoctorName(dName);
            topSuperDataSubmissionDto.setDoctorInformationDto(doctorInformationDto);
        }else {
            String doctorName = ParamUtil.getString(request, "names");
            terminationDto.setDoctorName(doctorName);
        }
        String topDate=ParamUtil.getString(request,"topDate");
        terminationDto.setTopDate(topDate);
        terminationOfPregnancyDto.setTerminationDto(terminationDto);
        topSuperDataSubmissionDto.setTerminationOfPregnancyDto(terminationOfPregnancyDto);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.TOP_DATA_SUBMISSION, topSuperDataSubmissionDto);
        Map<String,String> errMap = IaisCommonUtils.genNewHashMap();
        String actionType = ParamUtil.getString(request, DataSubmissionConstant.CRUD_TYPE);
        if("next".equals(actionType) || DataSubmissionHelper.isToNextAction(request)){
            if(needDoTop(preTerminationDto)){
                ValidationResult result = WebValidationHelper.validateProperty(terminationDto, "TOP");
                if (result != null) {
                    errMap.putAll(result.retrieveAll());
                }
            }
        }
        if(!errMap.isEmpty()){
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errMap));
            return 0;
        }
        if(DsConfigHelper.TOP_STEP_PREVIEW.equals(actionType)){
            if(needDoTop(preTerminationDto)){
                ValidationResult result = WebValidationHelper.validateProperty(postTerminationDto,"TOP");
                if(result !=null){
                    errMap.putAll(result.retrieveAll());
                }
            }
        }
        if(!errMap.isEmpty()){
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errMap));
            return 5;
        }
        return 1;
    }



    private int doPostTermination(HttpServletRequest request) {
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(request);
        topSuperDataSubmissionDto = topSuperDataSubmissionDto  == null ? new TopSuperDataSubmissionDto() : topSuperDataSubmissionDto;
        TerminationOfPregnancyDto terminationOfPregnancyDto = topSuperDataSubmissionDto.getTerminationOfPregnancyDto() == null ? new TerminationOfPregnancyDto() : topSuperDataSubmissionDto.getTerminationOfPregnancyDto();
        PostTerminationDto postTerminationDto = terminationOfPregnancyDto.getPostTerminationDto() == null ? new PostTerminationDto() : terminationOfPregnancyDto.getPostTerminationDto();
        PreTerminationDto preTerminationDto=terminationOfPregnancyDto.getPreTerminationDto() == null ? new PreTerminationDto() : terminationOfPregnancyDto.getPreTerminationDto();
        ControllerHelper.get(request, postTerminationDto);
        if(StringUtil.isNotEmpty(postTerminationDto.getCounsellorIdNo())){
            postTerminationDto.setCounsellorIdNo(postTerminationDto.getCounsellorIdNo().toUpperCase());
        }
        String TopPlace=ParamUtil.getString(request,"TopPlace");
        postTerminationDto.setCounsellingPlace(TopPlace);
        terminationOfPregnancyDto.setPostTerminationDto(postTerminationDto);
        topSuperDataSubmissionDto.setTerminationOfPregnancyDto(terminationOfPregnancyDto);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.TOP_DATA_SUBMISSION, topSuperDataSubmissionDto);
        Map<String,String> errMap = IaisCommonUtils.genNewHashMap();
        String actionType = ParamUtil.getString(request, DataSubmissionConstant.CRUD_TYPE);
        if("next".equals(actionType) || DataSubmissionHelper.isToNextAction(request)){
            if(needDoTop(preTerminationDto)){
                ValidationResult result = WebValidationHelper.validateProperty(postTerminationDto,"TOP");
                if(result !=null){
                    errMap.putAll(result.retrieveAll());
                }
            }

        }
        if(!errMap.isEmpty()){
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errMap));
            return 0;
        }
        return 1;
    }

    /**
     * Step: DoSubmission
     *
     * @param bpc
     */
    public void doSubmission(BaseProcessClass bpc) throws ParseException {
        String submitFlag = (String) ParamUtil.getSessionAttr(bpc.request, SUBMIT_FLAG);
        if (!StringUtil.isEmpty(submitFlag)) {
            throw new IaisRuntimeException("Double Submit");
        }
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(bpc.request);
        if (!DataSubmissionHelper.canDoRfc(topSuperDataSubmissionDto.getDataSubmissionDto().getId())) {
            ParamUtil.setRequestAttr(bpc.request, "valFlag", "fail");
            ParamUtil.setRequestAttr(bpc.request, "rfcOutdateFlag", "yes");
            return;
        }
        topSuperDataSubmissionDto.setDataSubmissionDto(DataSubmissionHelper.initDataSubmission(topSuperDataSubmissionDto, false));
        topSuperDataSubmissionDto.setCycleDto(DataSubmissionHelper.initCycleDto(topSuperDataSubmissionDto,
                DataSubmissionHelper.getLicenseeId(bpc.request), false));
        DataSubmissionHelper.setCurrentTopDataSubmission(topSuperDataSubmissionDto,bpc.request);
        DataSubmissionDto dataSubmissionDto = topSuperDataSubmissionDto.getDataSubmissionDto();
        if (StringUtil.isEmpty(dataSubmissionDto.getSubmissionNo())) {
            String submissionNo = topDataSubmissionService.getSubmissionNo(DataSubmissionConsts.DS_TOP);
            dataSubmissionDto.setSubmissionNo(submissionNo);
        }
        if (DataSubmissionConsts.DS_APP_TYPE_RFC.equals(dataSubmissionDto.getAppType())) {
            dataSubmissionDto.setStatus(DataSubmissionConsts.DS_STATUS_AMENDED);
        } else if (StringUtil.isEmpty(dataSubmissionDto.getStatus())) {
            dataSubmissionDto.setStatus(DataSubmissionConsts.DS_STATUS_COMPLETED);
        }
        if (StringUtil.isEmpty(dataSubmissionDto.getStatus())) {
            dataSubmissionDto.setStatus(DataSubmissionConsts.DS_STATUS_COMPLETED);
        }

        LoginContext loginContext = DataSubmissionHelper.getLoginContext(bpc.request);
        String licenseeId = null;
        if (loginContext != null) {
            dataSubmissionDto.setSubmitBy(loginContext.getUserId());
            dataSubmissionDto.setSubmitDt(new Date());
            licenseeId = loginContext.getLicenseeId();
        }
        TerminationOfPregnancyDto terminationOfPregnancyDto=topSuperDataSubmissionDto.getTerminationOfPregnancyDto();
        TerminationDto terminationDto = terminationOfPregnancyDto.getTerminationDto();
        PreTerminationDto preTerminationDto = terminationOfPregnancyDto.getPreTerminationDto();
        PatientInformationDto patientInformationDto=terminationOfPregnancyDto.getPatientInformationDto();
        if (StringUtil.isNotEmpty(patientInformationDto.getPatientName())) {
            patientInformationDto.setPatientName(patientInformationDto.getPatientName().toUpperCase(AppConsts.DFT_LOCALE));
        }
        String dayA = MasterCodeUtil.getCodeDesc("TOPDAY001");
        String dayB = MasterCodeUtil.getCodeDesc("TOPDAY003");
        String dayC = MasterCodeUtil.getCodeDesc("TOPDAY002");
        String dayD = MasterCodeUtil.getCodeDesc("TOPDAY004");
        String dayE = MasterCodeUtil.getCodeDesc("TOPDAY006");
        String dayF = MasterCodeUtil.getCodeDesc("TOPDAY005");
        String submitDt=Formatter.formatDateTime(new Date(), "dd/MM/yyyy HH:mm:ss");
        try {
            int dayIntA=Integer.parseInt(dayA);
            int dayIntB=Integer.parseInt(dayB);
            int dayIntC=Integer.parseInt(dayC);
            int dayIntD=Integer.parseInt(dayD);
            int dayIntE=Integer.parseInt(dayE);
            int dayIntF=Integer.parseInt(dayF);
            //a.TOP procedure is completed; Data was submitted more than 30 days (Configurable) from TOP Date;
            if(Formatter.compareDateByDay(submitDt,terminationDto.getTopDate())>=dayIntA){
                terminationDto.setLateSubmit(true);
            }
            if(preTerminationDto.getCounsellingGiven()){
                //b.Only 1 pre-TOP counselling session done and decision is not to abort; Data was submitted more than 30 days from the Pre-Counselling Date;
                if("TOPPCR003".equals(preTerminationDto.getCounsellingResult())){
                    if(Formatter.compareDateByDay(submitDt,preTerminationDto.getCounsellingDate())>=dayIntB){
                        terminationDto.setLateSubmit(true);
                    }
                }
                //c.Only 1 pre-TOP counselling session done and patient was lost to follow-up; Data was submitted more than 37 days from Pre-counselling date;
                if("TOPPCR001".equals(preTerminationDto.getCounsellingResult())){
                    if("0".equals(preTerminationDto.getPatientAppointment())){
                        if(Formatter.compareDateByDay(submitDt,preTerminationDto.getCounsellingDate())>=dayIntC){
                            terminationDto.setLateSubmit(true);
                        }
                    }
                }
                if("TOPPCR002".equals(preTerminationDto.getCounsellingResult())){
                    if(Formatter.compareDateByDay(submitDt,preTerminationDto.getCounsellingDate())>=dayIntC){
                        terminationDto.setLateSubmit(true);
                    }
                }
                if("TOPPCR001".equals(preTerminationDto.getCounsellingResult())){
                    if("1".equals(preTerminationDto.getPatientAppointment())){
                        //d.More than 1 pre-TOP counselling session done and decision is not to abort; Data was submitted more than 30 days from Pre-Counselling Date;
                        if("TOPSP003".equals(preTerminationDto.getSecCounsellingResult())){
                            if(Formatter.compareDateByDay(submitDt,preTerminationDto.getCounsellingDate())>=dayIntD){
                                terminationDto.setLateSubmit(true);
                            }
                        }
                        //f.More than 1 pre-TOP counselling session, decision is to abort, Data was submitted more than 30 days from the last Pre-counselling date.
                        if("TOPSP004".equals(preTerminationDto.getSecCounsellingResult())){
                            if(Formatter.compareDateByDay(submitDt,preTerminationDto.getSecCounsellingDate())>=dayIntF){
                                terminationDto.setLateSubmit(true);
                            }else if(StringUtil.isEmpty(preTerminationDto.getSecCounsellingDate())&&Formatter.compareDateByDay(submitDt,preTerminationDto.getCounsellingDate())>=dayIntF){
                                terminationDto.setLateSubmit(true);
                            }
                        }
                    }
                }
                //e.More than 1 pre-TOP counselling session, patient did not return for subsequent appointment; Data was submitted more than 37 days from the Second/Final Pre-Counselling Date

                if("TOPPCR001".equals(preTerminationDto.getCounsellingResult())){
                    if("1".equals(preTerminationDto.getPatientAppointment())){
                        if("TOPSP001".equals(preTerminationDto.getSecCounsellingResult())){
                            if(Formatter.compareDateByDay(submitDt,preTerminationDto.getSecCounsellingDate())>=dayIntE){
                                terminationDto.setLateSubmit(true);
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            log.error(StringUtil.changeForLog("setLateSubmit is error"));
        }
        topSuperDataSubmissionDto = topDataSubmissionService.saveTopSuperDataSubmissionDto(topSuperDataSubmissionDto);
        try {
            ProfessionalResponseDto professionalResponseDto=appSubmissionService.retrievePrsInfo(topSuperDataSubmissionDto.getDoctorInformationDto().getDoctorReignNo());
            if("-1".equals(professionalResponseDto.getStatusCode()) || "-2".equals(professionalResponseDto.getStatusCode())){
                terminationDto.setTopDoctorInformations("true");
            }
        } catch (Exception e) {
            log.error(StringUtil.changeForLog("The Eic saveTOPSuperDataSubmissionDtoToBE failed ===>" + e.getMessage()), e);
        }
        topSuperDataSubmissionDto = topDataSubmissionService.saveTopSuperDataSubmissionDtoToBE(topSuperDataSubmissionDto);
        if (!StringUtil.isEmpty(topSuperDataSubmissionDto.getDraftId())) {
            topDataSubmissionService.updateDataSubmissionDraftStatus(topSuperDataSubmissionDto.getDraftId(),
                    DataSubmissionConsts.DS_STATUS_INACTIVE);
        }

        String licenseeDtoName = AccessUtil.getLoginUser(bpc.request).getUserName();
        String submissionNo = topSuperDataSubmissionDto.getDataSubmissionDto().getSubmissionNo();
        /*String dateStr = Formatter.formatDateTime(new Date(),"dd/MM/yyyy HH:mm:ss");*/
        try {
            /*sendNotification(licenseeDtoName, submissionNo, licenseeId, dateStr);*/
            if(topSuperDataSubmissionDto.getDataSubmissionDto().getAppType().equals(DataSubmissionConsts.DS_APP_TYPE_RFC)){
                sendRfcMsgAndEmail(licenseeId, licenseeDtoName, submissionNo);
            }else {
                sendMsgAndEmail(licenseeId, "Termination Of Pregnancy", licenseeDtoName, submissionNo);
            }

        } catch (IOException | TemplateException e) {
            log.error(StringUtil.changeForLog("sendMsgAndEmail is error"+licenseeId + "----"+ licenseeDtoName + "----"+ submissionNo));

        }
        String emailAddress;
        if(!isRfc(bpc.request)){
            emailAddress = DataSubmissionHelper.getEmailAddrsByRoleIdsAndLicenseeId(bpc.request, MsgTemplateConstants.MSG_TEMPLATE_TOP_SUBMITTED_ACK_EMAIL);
        } else {
            emailAddress = DataSubmissionHelper.getEmailAddrsByRoleIdsAndLicenseeId(bpc.request, MsgTemplateConstants.MSG_TEMPLATE_TOP_SUBMITTED_RFC_ACK_EMAIL);
        }
        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.TOP_DATA_SUBMISSION, topSuperDataSubmissionDto);
        ParamUtil.setRequestAttr(bpc.request, "emailAddress", emailAddress);
        ParamUtil.setRequestAttr(bpc.request, "submittedBy", DataSubmissionHelper.getLoginContext(bpc.request).getUserName());
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.PRINT_FLAG, DataSubmissionConstant.PRINT_FLAG_ACKTOP);
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, DataSubmissionConstant.PAGE_STAGE_ACK);
        ParamUtil.setSessionAttr(bpc.request, SUBMIT_FLAG, AppConsts.YES);
        ParamUtil.setRequestAttr(bpc.request, "valFlag", "pass");
    }

    /**
     * Step: DoDraft
     *
     * @param bpc
     */
    public void doDraft(BaseProcessClass bpc) {
        String currentStage = (String) ParamUtil.getRequestAttr(bpc.request,"currentStage");
        Map<String,String> errMap= (Map<String, String>) ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.ERRORMAP);
        if(IaisCommonUtils.isNotEmpty(errMap)){
            errMap.remove("amendReason");
            errMap.remove("amendReasonOther");
            errMap.remove("declaration");
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errMap));

        }
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CRUD_ACTION_TYPE_TOP, currentStage);
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(bpc.request);
        if (topSuperDataSubmissionDto != null) {
            topSuperDataSubmissionDto.setDraftNo(topDataSubmissionService.getDraftNo(DataSubmissionConsts.DS_TOP, topSuperDataSubmissionDto.getDraftNo()));
            topSuperDataSubmissionDto = topDataSubmissionService.saveDataSubmissionDraft(topSuperDataSubmissionDto);
            DataSubmissionHelper.setCurrentTopDataSubmission(topSuperDataSubmissionDto, bpc.request);
            ParamUtil.setRequestAttr(bpc.request, "saveDraftSuccess", "success");
        } else {
            log.info(StringUtil.changeForLog("The topSuperDataSubmission is null"));
        }
    }

    /**
     * Step: DoRfc
     *
     * @param bpc
     */
    public void doRfc(BaseProcessClass bpc) {
        if(isRfc(bpc.request)){
            TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getOldTopSuperDataSubmissionDto(bpc.request);
            if(topSuperDataSubmissionDto != null){
                ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.RFC_NO_CHANGE_ERROR, AppConsts.YES);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, DataSubmissionConstant.PAGE_STAGE_PAGE);
            }
        }
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
        String crudType = ParamUtil.getString(bpc.request, DataSubmissionConstant.CRUD_TYPE);

        String actionType = null;
        if ("return".equals(crudType)) {
            actionType = "return";
        } else if ("next".equals(crudType)) {
            Integer status = (Integer) ParamUtil.getRequestAttr(bpc.request, DataSubmissionConstant.ACTION_STATUS);
            if (status == null || 0 == status) {// current
                actionType = DataSubmissionHelper.setCurrentAction(DataSubmissionConsts.DS_TOP, bpc.request);
            } else if (-1 == status) { // previous
                actionType = DataSubmissionHelper.setPreviousAction(DataSubmissionConsts.DS_TOP, bpc.request);
            } else if (1 == status) { // next
                actionType = DataSubmissionHelper.setNextAction(DataSubmissionConsts.DS_TOP, bpc.request);
            } else if(2 == status){
                actionType="submission";
            }
        } else if (DataSubmissionHelper.isToNextAction(bpc.request)) {
            Integer status = (Integer) ParamUtil.getRequestAttr(bpc.request, DataSubmissionConstant.ACTION_STATUS);
            if (status == null || 0 == status) {// current
                actionType = DataSubmissionHelper.setCurrentAction(DataSubmissionConsts.DS_TOP, bpc.request);
            } else if (1 == status) { // nexts
                actionType = crudType;
                DsConfigHelper.setActiveConfig(actionType, bpc.request);
            }else if(2 == status){
                actionType = "TOPT002";
                DsConfigHelper.setActiveConfig(actionType, bpc.request);
            }else if(3 == status){
                actionType = "TOPT003";
                DsConfigHelper.setActiveConfig(actionType, bpc.request);
            }else if(4 == status){
                actionType = "TOPT004";
                DsConfigHelper.setActiveConfig(actionType, bpc.request);
            }else if(5 == status){
                actionType = "TOPT005";
                DsConfigHelper.setActiveConfig(actionType, bpc.request);
            }
        } else if ("previous".equals(crudType)) {//back
            actionType = DataSubmissionHelper.setPreviousAction(DataSubmissionConsts.DS_TOP, bpc.request);
        } else if ("page".equals(crudType) || "preview".equals(crudType)) {
            actionType = crudType;
        } else if("confim".equals(crudType)){
            actionType="page";
        } else {
            actionType = crudType;
            DsConfigHelper.setActiveConfig(actionType, bpc.request);
        }

        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CRUD_ACTION_TYPE_TOP, actionType);
    }

    /**
     * Step: DoReturn
     *
     * @param bpc
     */
    public void doReturn(BaseProcessClass bpc) throws IOException {
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(bpc.request);
        String target = InboxConst.URL_MAIN_WEB_MODULE + "MohInternetInbox";
        if (topSuperDataSubmissionDto != null && DataSubmissionConsts.DS_APP_TYPE_NEW.equals(topSuperDataSubmissionDto.getAppType())) {
            target = InboxConst.URL_LICENCE_WEB_MODULE + "MohDataSubmission/PrepareCompliance";
        }
        StringBuilder url = new StringBuilder();
        url.append(InboxConst.URL_HTTPS)
                .append(bpc.request.getServerName())
                .append(target);
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
        IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
    }
    private TopSuperDataSubmissionDto initTopSuperDataSubmissionDto(HttpServletRequest request) {
            TopSuperDataSubmissionDto topSuperDataSubmissionDto=new TopSuperDataSubmissionDto();
        retrieveHciCode(request, topSuperDataSubmissionDto);
        String orgId = Optional.ofNullable(DataSubmissionHelper.getLoginContext(request))
                .map(LoginContext::getOrgId).orElse("");

        topSuperDataSubmissionDto.setOrgId(orgId);
        topSuperDataSubmissionDto.setSubmissionType(DataSubmissionConsts.TOP_TYPE_SBT_TERMINATION_OF_PRE);
        topSuperDataSubmissionDto.setAppType(DataSubmissionConsts.DS_APP_TYPE_NEW);
        topSuperDataSubmissionDto.setFe(true);
        topSuperDataSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());

        topSuperDataSubmissionDto.setCycleDto(DataSubmissionHelper.initCycleDto(topSuperDataSubmissionDto,
                DataSubmissionHelper.getLicenseeId(request), false));
        topSuperDataSubmissionDto.setDataSubmissionDto(DataSubmissionHelper.initDataSubmission(topSuperDataSubmissionDto, false));

        return topSuperDataSubmissionDto;
    }

    private boolean isRfc(HttpServletRequest request) {
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(request);
        return topSuperDataSubmissionDto != null && topSuperDataSubmissionDto.getDataSubmissionDto() != null && DataSubmissionConsts.DS_APP_TYPE_RFC.equalsIgnoreCase(topSuperDataSubmissionDto.getDataSubmissionDto().getAppType());
    }
    protected boolean isOthers(String others){
        return StringUtil.isIn(others,DataSubmissionConsts.REASON_FOR_TOP_AMEND_OTHERS);
    }

    private void sendMsgAndEmail(String licenseeId, String serverName, String submitterName, String submissionNo) throws IOException, TemplateException {
        MsgTemplateDto msgTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_DS_SUBMITTED_ACK_MSG).getEntity();
        Map<String, Object> msgContentMap = IaisCommonUtils.genNewHashMap();
        msgContentMap.put("serverName", serverName);
        msgContentMap.put("ApplicantName", submitterName);
        msgContentMap.put("submissionId", submissionNo);
        msgContentMap.put("date",Formatter.formatDateTime(new Date(),"dd/MM/yyyy HH:mm:ss"));
        msgContentMap.put("MOH_AGENCY_NAME", AppConsts.MOH_AGENCY_NAME);


        Map<String, Object> msgSubjectMap = IaisCommonUtils.genNewHashMap();
        msgSubjectMap.put("serverName", serverName);
        String subject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(), msgSubjectMap);

        EmailParam msgParam = new EmailParam();
        msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_DS_SUBMITTED_ACK_MSG);
        msgParam.setTemplateContent(msgContentMap);
        msgParam.setQueryCode(submissionNo);
        msgParam.setReqRefNum(submissionNo);
        msgParam.setServiceTypes(DataSubmissionConsts.DS_TOP_NEW);
        msgParam.setRefId(licenseeId);
        msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
        msgParam.setSubject(subject);
        notificationHelper.sendNotification(msgParam);
        log.info(StringUtil.changeForLog("***************** send TOP Notification  end *****************"));
        //send email
        EmailParam emailParamEmail = MiscUtil.transferEntityDto(msgParam, EmailParam.class);
        emailParamEmail.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_TOP_SUBMITTED_ACK_EMAIL);
        emailParamEmail.setRefIdType(NotificationHelper.RECEIPT_TYPE_LICENSEE_ID);
        notificationHelper.sendNotification(emailParamEmail);
        log.info(StringUtil.changeForLog("***************** send TOP Email  end *****************"));
    }
    private void sendRfcMsgAndEmail(String licenseeId, String submitterName, String submissionNo) throws IOException, TemplateException {
        MsgTemplateDto msgTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_DS_SUBMITTED_RFC_ACK_MSG).getEntity();
        Map<String, Object> msgContentMap = IaisCommonUtils.genNewHashMap();
        msgContentMap.put("ApplicantName", submitterName);
        msgContentMap.put("submissionId", submissionNo);
        msgContentMap.put("date", Formatter.formatDateTime(new Date(),"dd/MM/yyyy HH:mm:ss"));
        msgContentMap.put("MOH_AGENCY_NAME", AppConsts.MOH_AGENCY_NAME);

        Map<String, Object> msgSubjectMap = IaisCommonUtils.genNewHashMap();
        msgSubjectMap.put("submissionId", submissionNo);
        String subject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(), msgSubjectMap);

        EmailParam msgParam = new EmailParam();
        msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_DS_SUBMITTED_RFC_ACK_MSG);
        msgParam.setTemplateContent(msgContentMap);
        msgParam.setQueryCode(submissionNo);
        msgParam.setReqRefNum(submissionNo);
        msgParam.setServiceTypes(DataSubmissionConsts.DS_TOP_SUP);
        msgParam.setRefId(licenseeId);
        msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
        msgParam.setSubject(subject);
        notificationHelper.sendNotification(msgParam);
        log.info(StringUtil.changeForLog("***************** send TOP Notification  end *****************"));
        //send email
        EmailParam emailParamEmail = MiscUtil.transferEntityDto(msgParam, EmailParam.class);
        emailParamEmail.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_TOP_SUBMITTED_RFC_ACK_EMAIL);
        emailParamEmail.setRefIdType(NotificationHelper.RECEIPT_TYPE_LICENSEE_ID);
        notificationHelper.sendNotification(emailParamEmail);
        log.info(StringUtil.changeForLog("***************** send TOP Email  end *****************"));
    }

    private boolean needDoTop(PreTerminationDto preTerminationDto){
        if(preTerminationDto!=null&&preTerminationDto.getCounsellingGiven()!=null){
            if(preTerminationDto.getCounsellingGiven()){
                if(preTerminationDto.getCounsellingResult()!=null){
                    if("TOPPCR003".equals(preTerminationDto.getCounsellingResult())){
                        return false;
                    }else {
                        if("TOPPCR001".equals(preTerminationDto.getCounsellingResult())){
                            if ("1".equals(preTerminationDto.getPatientAppointment())) {
                                return !"TOPSP001".equals(preTerminationDto.getSecCounsellingResult()) && !"TOPSP003".equals(preTerminationDto.getSecCounsellingResult());
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
