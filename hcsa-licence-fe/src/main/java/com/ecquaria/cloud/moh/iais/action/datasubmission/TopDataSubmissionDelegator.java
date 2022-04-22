package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
import com.ecquaria.cloud.moh.iais.common.helper.dataSubmission.DsConfigHelper;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.ControllerHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.TopDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * Process: MohNEWTOPDataSumission
 *
 * @Description TopDataSubmissionDelegator
 * @Auther zhixing on 2/14/2022.
 */
@Slf4j
@Delegator("topDataSubmissionDelegator")
public class TopDataSubmissionDelegator {

    public static final String ACTION_TYPE_CONFIRM = "confim";
    public static final String ACTION_TYPE_BACK = "back";
    private static final String PREMISES = "premises";
    protected final static String  CONSULTING_CENTER = "Health Promotion Board Counselling Centre";
    protected final static String  TOP_OTHERS    = "Others";
    private final static String  COUNSE_LLING_PLACE          =  "CounsellingPlace";
    private final static String  TOP_PLACE          =  "TopPlace";
    private final static String  TOP_DRUG_PLACE     ="TopDrugPlace";

    @Autowired
    private TopDataSubmissionService topDataSubmissionService;
    /**
     * Step: Start
     *
     * @param bpc
     */
    public void doStart(BaseProcessClass bpc) {
        log.info(" -----TopDataSubmissionDelegator Start ------ ");
        DsConfigHelper.clearTopSession(bpc.request);
        DsConfigHelper.initTopConfig(bpc.request);
        log.info(StringUtil.changeForLog("-----" + this.getClass().getSimpleName() + " Start -----"));
        DataSubmissionHelper.clearSession(bpc.request);

        String orgId = Optional.ofNullable(DataSubmissionHelper.getLoginContext(bpc.request))
                .map(LoginContext::getOrgId).orElse("");
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = topDataSubmissionService.getTopSuperDataSubmissionDtoDraftByConds(orgId,DataSubmissionConsts.TOP_TYPE_SBT_TERMINATION_OF_PRE);
        if (topSuperDataSubmissionDto != null) {
            ParamUtil.setRequestAttr(bpc.request, "hasDraft", Boolean.TRUE);
        }
    }

    /**
     * Step: PrepareSwitch
     *
     * @param bpc
     */
    public void prepareSwitch(BaseProcessClass bpc) {
        log.info(" ----- PrepareSwitch ------ ");
        DsConfigHelper.initTopConfig(bpc.request);
        String actionType = getActionType(bpc.request);
        log.info(StringUtil.changeForLog("Action Type: " + actionType));
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CRUD_ACTION_TYPE_TOP, actionType);
        ParamUtil.setRequestAttr(bpc.request, "title", DataSubmissionHelper.getMainTitle(DataSubmissionConsts.DS_APP_TYPE_NEW));
        DsConfig config = DsConfigHelper.getCurrentConfig(DataSubmissionConsts.DS_TOP, bpc.request);
        String smallTitle = "";
        if (config != null) {
            smallTitle = config.getText();
        }
        ParamUtil.setSessionAttr(bpc.request,COUNSE_LLING_PLACE,(Serializable) getSourseList(bpc.request));
        ParamUtil.setSessionAttr(bpc.request,TOP_PLACE,(Serializable) getSourseLists(bpc.request));
        ParamUtil.setSessionAttr(bpc.request,TOP_DRUG_PLACE,(Serializable) getSourseListsDrug(bpc.request));
        ParamUtil.setRequestAttr(bpc.request, "title", DataSubmissionHelper.getMainTitle(DataSubmissionConsts.DS_APP_TYPE_NEW));
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(bpc.request);
        topSuperDataSubmissionDto = topSuperDataSubmissionDto  == null ? new TopSuperDataSubmissionDto() : topSuperDataSubmissionDto;
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
                int age= -Formatter.compareDateByDay(patientInformationDto.getBirthData());
                patientInformationDto.setPatientAge(age/365);
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
            if (patientInformationDto.getPatientAge()<=16 || patientInformationDto.getPatientAge()>=65) {
                ParamUtil.setRequestAttr(bpc.request, "showPatientAgePT", AppConsts.YES);
            }
        }
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>" + "Termination Of Pregnancy" + "</strong>");
    }
    private String getActionType(HttpServletRequest request) {
        String actionType = (String) ParamUtil.getRequestAttr(request, DataSubmissionConstant.CRUD_ACTION_TYPE_TOP);
        if (StringUtil.isEmpty(actionType)) {
            actionType = DataSubmissionHelper.initAction(DataSubmissionConsts.DS_TOP, DsConfigHelper.TOP_STEP_PATIENT, request);
        }
        return actionType;
    }

    //TODO from ar center
    protected final List<SelectOption> getSourseList(HttpServletRequest request){
        Map<String,String> stringStringMap = IaisCommonUtils.genNewHashMap();
        DataSubmissionHelper.setArPremisesMap(request).values().stream().forEach(v->stringStringMap.put(v.getHciCode(),v.getPremiseLabel()));
        List<SelectOption> selectOptions = DataSubmissionHelper.genOptions(stringStringMap);
        TopSuperDataSubmissionDto currentSuper = DataSubmissionHelper.getCurrentTopDataSubmission(request);
        if(currentSuper==null){
           currentSuper=new TopSuperDataSubmissionDto();
        }
        TerminationOfPregnancyDto terminationOfPregnancyDto= currentSuper.getTerminationOfPregnancyDto();
        if(terminationOfPregnancyDto==null){
            terminationOfPregnancyDto=new TerminationOfPregnancyDto();
        }
        PatientInformationDto patientInformationDto = terminationOfPregnancyDto.getPatientInformationDto() == null ? new PatientInformationDto():terminationOfPregnancyDto.getPatientInformationDto();
        if(!StringUtil.isEmpty(patientInformationDto.getPatientAge())){
            if(patientInformationDto.getPatientAge()<16){
                selectOptions.add(new SelectOption(DataSubmissionConsts.AR_SOURCE_OTHER,CONSULTING_CENTER));
            }
        }
        return selectOptions;
    }
    //TODO from ar center
    protected final List<SelectOption> getSourseLists(HttpServletRequest request){
        Map<String,String> stringStringMap = IaisCommonUtils.genNewHashMap();
        DataSubmissionHelper.setArPremisesMap(request).values().stream().forEach(v->stringStringMap.put(v.getHciCode(),v.getPremiseLabel()));
        List<SelectOption> selectOptions = DataSubmissionHelper.genOptions(stringStringMap);
        return selectOptions;
    }

    //TODO from ar center
    protected final List<SelectOption> getSourseListsDrug(HttpServletRequest request){
        Map<String,String> stringStringMap = IaisCommonUtils.genNewHashMap();
        DataSubmissionHelper.setArPremisesMap(request).values().stream().forEach(v->stringStringMap.put(v.getHciCode(),v.getPremiseLabel()));
        List<SelectOption> selectOptions = DataSubmissionHelper.genOptions(stringStringMap);
        selectOptions.add(new SelectOption(DataSubmissionConsts.AR_SOURCE_OTHER,TOP_OTHERS));
        return selectOptions;
    }

    /**
     * Step: PrepareStepData
     *
     * @param bpc
     */
    public void prepareStepData(BaseProcessClass bpc) {
        log.info(" -----PrepareStepData ------ ");
        TopSuperDataSubmissionDto topSuperDataSubmissionDto =DataSubmissionHelper.getCurrentTopDataSubmission(bpc.request);
        if (topSuperDataSubmissionDto == null) {
            topSuperDataSubmissionDto =new TopSuperDataSubmissionDto();
        }
        String crud_action_type = ParamUtil.getRequestString(bpc.request, DataSubmissionConstant.CRUD_ACTION_TYPE_TOP);
        if (StringUtil.isEmpty(crud_action_type)){
            crud_action_type="";
        }
        //draft
        if (crud_action_type.equals("resume")) {
            topSuperDataSubmissionDto = topDataSubmissionService.getTopSuperDataSubmissionDtoDraftByConds(topSuperDataSubmissionDto.getOrgId(),topSuperDataSubmissionDto.getSubmissionType());
            if (topSuperDataSubmissionDto == null) {
                log.warn("Can't resume data!");
                topSuperDataSubmissionDto = new TopSuperDataSubmissionDto();
            }
            DataSubmissionHelper.setCurrentTopDataSubmission(topSuperDataSubmissionDto,bpc.request);
            ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CRUD_ACTION_TYPE_VSS,DataSubmissionConstant.PAGE_STAGE_PAGE);
        } else if (crud_action_type.equals("delete")) {
            topDataSubmissionService.deleteTopSuperDataSubmissionDtoDraftByConds(topSuperDataSubmissionDto.getOrgId(), DataSubmissionConsts.TOP_TYPE_SBT_TERMINATION_OF_PRE);
            topSuperDataSubmissionDto = new TopSuperDataSubmissionDto();
        }
        initTopSuperDataSubmissionDto(bpc.request,topSuperDataSubmissionDto);

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
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.PRINT_FLAG, DataSubmissionConstant.PRINT_FLAG_TOP);
    }


    /**
     * Step: DoStep
     *
     * @param bpc
     */
    public void doStep(BaseProcessClass bpc) {
        log.info(" ----- DoStep ------ ");
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
            status = doPresentTermination(bpc.request);
        }else if (DsConfigHelper.TOP_STEP_POST_TERMINATION.equals(currentCode)) {
            status = doPostTermination(bpc.request);
        }else if(DsConfigHelper.TOP_STEP_PREVIEW.equals(currentCode)){
            status = doPreview(bpc.request);;
        }
        if("next".equals(crudType)|| DataSubmissionHelper.isToNextAction(bpc.request)){
            currentConfig.setStatus(status);
            DsConfigHelper.setConfig(DataSubmissionConsts.DS_TOP, currentConfig, bpc.request);
        }
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
        TerminationDto terminationDto=terminationOfPregnancyDto.getTerminationDto();
        if(terminationDto==null){
            terminationDto=new TerminationDto();
        }
        if(StringUtil.isEmpty(terminationDto.getPregnancyOwn())){
            terminationDto.setPregnancyOwn(true);
        }
        if(StringUtil.isEmpty(terminationDto.getTakenOwn())){
            terminationDto.setTakenOwn(true);
        }
        terminationOfPregnancyDto.setTerminationDto(terminationDto);
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
        DataSubmissionHelper.setCurrentTopDataSubmission(topSuperDataSubmissionDto, request);
        if(terminationOfPregnancyDto==null){
            terminationOfPregnancyDto = new TerminationOfPregnancyDto();
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
        if(terminationDto==null){
            terminationDto = new TerminationDto();
        }
        PostTerminationDto postTerminationDto = terminationOfPregnancyDto.getPostTerminationDto();
        if(postTerminationDto==null){
            postTerminationDto = new PostTerminationDto();
        }
        Map<String,String> errMap = IaisCommonUtils.genNewHashMap();
        if(declaration == null || declaration.length == 0){
            errMap.put("declaration", "GENERAL_ERR0006");
        }

        ValidationResult result2 = WebValidationHelper.validateProperty(familyPlanDto,"TOP");
        if(result2 !=null){
            errMap.putAll(result2.retrieveAll());
        }
        ValidationResult result3 = WebValidationHelper.validateProperty(preTerminationDto,"TOP");
        if(result3 !=null){
            errMap.putAll(result3.retrieveAll());
        }
        if(!"TOPPCR002".equals(preTerminationDto.getCounsellingResult())) {
            if (!"TOPPCR001".equals(preTerminationDto.getCounsellingResult())) {
                ValidationResult result4 = WebValidationHelper.validateProperty(terminationDto, "TOP");
                if (result4 != null) {
                    errMap.putAll(result4.retrieveAll());
                }
            } else {
                if ("GAZAREA001".equals(preTerminationDto.getPatientAppointment())) {
                    if (!"TOPSP001".equals(preTerminationDto.getSecCounsellingResult()) && !"TOPSP002".equals(preTerminationDto.getSecCounsellingResult())) {
                        ValidationResult result4 = WebValidationHelper.validateProperty(terminationDto, "TOP");
                        if (result4 != null) {
                            errMap.putAll(result4.retrieveAll());
                        }
                    }
                }else {
                    ValidationResult result4 = WebValidationHelper.validateProperty(terminationDto, "TOP");
                    if (result4 != null) {
                        errMap.putAll(result4.retrieveAll());
                    }
                }
            }
        }
        if(!"TOPPCR002".equals(preTerminationDto.getCounsellingResult())) {
            if (!"TOPPCR001".equals(preTerminationDto.getCounsellingResult())) {
                ValidationResult result5 = WebValidationHelper.validateProperty(postTerminationDto, "TOP");
                if (result5 != null) {
                    errMap.putAll(result5.retrieveAll());
                }
            } else {
                if ("GAZAREA001".equals(preTerminationDto.getPatientAppointment())) {
                    if (!"TOPSP001".equals(preTerminationDto.getSecCounsellingResult()) && !"TOPSP002".equals(preTerminationDto.getSecCounsellingResult())) {
                        ValidationResult result5 = WebValidationHelper.validateProperty(postTerminationDto, "TOP");
                        if (result5 != null) {
                            errMap.putAll(result5.retrieveAll());
                        }
                    }
                }else {
                    ValidationResult result5 = WebValidationHelper.validateProperty(postTerminationDto, "TOP");
                    if (result5 != null) {
                        errMap.putAll(result5.retrieveAll());
                    }
                }
            }
        }
        if(!errMap.isEmpty()){
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
        String[] livingChildrenGenders= ParamUtil.getStrings(request, "livingChildrenGenders");
        ControllerHelper.get(request, patientInformationDto);
        if( !IaisCommonUtils.isEmpty(livingChildrenGenders)){

            patientInformationDto.setLivingChildrenGenders(Arrays.asList(livingChildrenGenders));
        }else{
            patientInformationDto.setLivingChildrenGenders(null);
        }
        if(StringUtil.isEmpty(patientInformationDto.getOrgId())){
            patientInformationDto.setOrgId(topSuperDataSubmissionDto.getOrgId());
        }
        terminationOfPregnancyDto.setPatientInformationDto(patientInformationDto);
        topSuperDataSubmissionDto.setTerminationOfPregnancyDto(terminationOfPregnancyDto);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.TOP_DATA_SUBMISSION, topSuperDataSubmissionDto);
        request.getSession().setAttribute(DataSubmissionConstant.TOP_DATA_SUBMISSION, topSuperDataSubmissionDto);
        Map<String,String> errMap = IaisCommonUtils.genNewHashMap();
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
        return 1;
    }

    private int doFamilyPlanning(HttpServletRequest request) {
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(request);
        topSuperDataSubmissionDto = topSuperDataSubmissionDto  == null ? new TopSuperDataSubmissionDto() : topSuperDataSubmissionDto;
        TerminationOfPregnancyDto terminationOfPregnancyDto = topSuperDataSubmissionDto.getTerminationOfPregnancyDto() == null ? new TerminationOfPregnancyDto() : topSuperDataSubmissionDto.getTerminationOfPregnancyDto();
        FamilyPlanDto familyPlanDto = terminationOfPregnancyDto.getFamilyPlanDto() == null ? new FamilyPlanDto() : terminationOfPregnancyDto.getFamilyPlanDto();
        PatientInformationDto patientInformationDto=terminationOfPregnancyDto.getPatientInformationDto();
        if(patientInformationDto==null){
            patientInformationDto=new PatientInformationDto();
        }
        ControllerHelper.get(request, familyPlanDto);
        try {
            int age= -Formatter.compareDateByDay(patientInformationDto.getBirthData());
            patientInformationDto.setPatientAge(age/365);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        if(patientInformationDto.getPatientAge()<16 && !StringUtil.isEmpty(patientInformationDto.getPatientAge())){
            familyPlanDto.setNeedHpbConsult(true);
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
        return 1;
    }

    private int doPreTermination(HttpServletRequest request) {
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(request);
        topSuperDataSubmissionDto = topSuperDataSubmissionDto  == null ? new TopSuperDataSubmissionDto() : topSuperDataSubmissionDto;
        TerminationOfPregnancyDto terminationOfPregnancyDto = topSuperDataSubmissionDto.getTerminationOfPregnancyDto() == null ? new TerminationOfPregnancyDto() : topSuperDataSubmissionDto.getTerminationOfPregnancyDto();
        PreTerminationDto preTerminationDto = terminationOfPregnancyDto.getPreTerminationDto() == null ? new PreTerminationDto() : terminationOfPregnancyDto.getPreTerminationDto();
        ControllerHelper.get(request, preTerminationDto);
        terminationOfPregnancyDto.setPreTerminationDto(preTerminationDto);
        topSuperDataSubmissionDto.setTerminationOfPregnancyDto(terminationOfPregnancyDto);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.TOP_DATA_SUBMISSION, topSuperDataSubmissionDto);

        Map<String,String> errMap = IaisCommonUtils.genNewHashMap();
        String actionType = ParamUtil.getString(request, DataSubmissionConstant.CRUD_TYPE);
        if("next".equals(actionType) || DataSubmissionHelper.isToNextAction(request)){
            ValidationResult result = WebValidationHelper.validateProperty(preTerminationDto,"TOP");
            if(result !=null){
                errMap.putAll(result.retrieveAll());
            }
        }
        if(!errMap.isEmpty()){
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errMap));
            return 0;
        }
        return 1;
    }

    private int doPresentTermination(HttpServletRequest request) {
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(request);
        topSuperDataSubmissionDto = topSuperDataSubmissionDto  == null ? new TopSuperDataSubmissionDto() : topSuperDataSubmissionDto;
        TerminationOfPregnancyDto terminationOfPregnancyDto = topSuperDataSubmissionDto.getTerminationOfPregnancyDto() == null ? new TerminationOfPregnancyDto() : topSuperDataSubmissionDto.getTerminationOfPregnancyDto();
        TerminationDto terminationDto = terminationOfPregnancyDto.getTerminationDto() == null ? new TerminationDto() : terminationOfPregnancyDto.getTerminationDto();
        PreTerminationDto preTerminationDto=terminationOfPregnancyDto.getPreTerminationDto() == null ? new PreTerminationDto() : terminationOfPregnancyDto.getPreTerminationDto();
        ControllerHelper.get(request, terminationDto);
        String topDate=ParamUtil.getString(request,"topDate");
        terminationDto.setTopDate(topDate);
        terminationOfPregnancyDto.setTerminationDto(terminationDto);
        topSuperDataSubmissionDto.setTerminationOfPregnancyDto(terminationOfPregnancyDto);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.TOP_DATA_SUBMISSION, topSuperDataSubmissionDto);
        Map<String,String> errMap = IaisCommonUtils.genNewHashMap();
        String actionType = ParamUtil.getString(request, DataSubmissionConstant.CRUD_TYPE);
        if("next".equals(actionType) || DataSubmissionHelper.isToNextAction(request)){
            if(!"TOPPCR002".equals(preTerminationDto.getCounsellingResult())) {
                if (!"TOPPCR001".equals(preTerminationDto.getCounsellingResult())) {
                    ValidationResult result = WebValidationHelper.validateProperty(terminationDto, "TOP");
                    if (result != null) {
                        errMap.putAll(result.retrieveAll());
                    }
                } else {
                    if ("GAZAREA001".equals(preTerminationDto.getPatientAppointment())) {
                        if (!"TOPSP001".equals(preTerminationDto.getSecCounsellingResult()) && !"TOPSP002".equals(preTerminationDto.getSecCounsellingResult())) {
                            ValidationResult result = WebValidationHelper.validateProperty(terminationDto, "TOP");
                            if (result != null) {
                                errMap.putAll(result.retrieveAll());
                            }
                        }
                    }else {
                        ValidationResult result = WebValidationHelper.validateProperty(terminationDto, "TOP");
                        if (result != null) {
                            errMap.putAll(result.retrieveAll());
                        }
                    }
                }
            }
        }
        if(!errMap.isEmpty()){
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errMap));
            return 0;
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
        String TopPlace=ParamUtil.getString(request,"TopPlace");
        postTerminationDto.setCounsellingPlace(TopPlace);
        terminationOfPregnancyDto.setPostTerminationDto(postTerminationDto);
        topSuperDataSubmissionDto.setTerminationOfPregnancyDto(terminationOfPregnancyDto);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.TOP_DATA_SUBMISSION, topSuperDataSubmissionDto);
        Map<String,String> errMap = IaisCommonUtils.genNewHashMap();
        String actionType = ParamUtil.getString(request, DataSubmissionConstant.CRUD_TYPE);
        if("next".equals(actionType) || DataSubmissionHelper.isToNextAction(request)){
            if(!"TOPPCR002".equals(preTerminationDto.getCounsellingResult())) {
                if (!"TOPPCR001".equals(preTerminationDto.getCounsellingResult())) {
                    ValidationResult result = WebValidationHelper.validateProperty(postTerminationDto,"TOP");
                    if(result !=null){
                        errMap.putAll(result.retrieveAll());
                    }
                } else {
                    if ("GAZAREA001".equals(preTerminationDto.getPatientAppointment())) {
                        if (!"TOPSP001".equals(preTerminationDto.getSecCounsellingResult()) && !"TOPSP002".equals(preTerminationDto.getSecCounsellingResult())) {
                            ValidationResult result = WebValidationHelper.validateProperty(postTerminationDto,"TOP");
                            if(result !=null){
                                errMap.putAll(result.retrieveAll());
                            }
                        }
                    }else {
                        ValidationResult result = WebValidationHelper.validateProperty(postTerminationDto,"TOP");
                        if(result !=null){
                            errMap.putAll(result.retrieveAll());
                        }
                    }
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
    public void doSubmission(BaseProcessClass bpc) {
        log.info(" ----- DoSubmission ------ ");
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(bpc.request);
        topSuperDataSubmissionDto.setDataSubmissionDto(DataSubmissionHelper.initDataSubmission(topSuperDataSubmissionDto, false));
        topSuperDataSubmissionDto.setCycleDto(DataSubmissionHelper.initCycleDto(topSuperDataSubmissionDto,
                DataSubmissionHelper.getLicenseeId(bpc.request), false));
        DataSubmissionHelper.setCurrentTopDataSubmission(topSuperDataSubmissionDto,bpc.request);
        DataSubmissionDto dataSubmissionDto = topSuperDataSubmissionDto.getDataSubmissionDto();
        if (StringUtil.isEmpty(dataSubmissionDto.getSubmissionNo())) {
            String submissionNo = topDataSubmissionService.getSubmissionNo(DataSubmissionConsts.DS_TOP);
            dataSubmissionDto.setSubmissionNo(submissionNo);
        }
        if (StringUtil.isEmpty(dataSubmissionDto.getStatus())) {
            dataSubmissionDto.setStatus(DataSubmissionConsts.DS_STATUS_COMPLETED);
        }

        LoginContext loginContext = DataSubmissionHelper.getLoginContext(bpc.request);
        if (loginContext != null) {
            dataSubmissionDto.setSubmitBy(loginContext.getUserId());
            dataSubmissionDto.setSubmitDt(new Date());
        }
        TerminationOfPregnancyDto terminationOfPregnancyDto=topSuperDataSubmissionDto.getTerminationOfPregnancyDto();
        TerminationDto terminationDto = terminationOfPregnancyDto.getTerminationDto();
        try {
            if(Formatter.compareDateByDay(String.valueOf(dataSubmissionDto.getSubmitDt()),terminationDto.getTopDate())>30){
                terminationDto.setLateSubmit(true);
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }

        topSuperDataSubmissionDto = topDataSubmissionService.saveTopSuperDataSubmissionDto(topSuperDataSubmissionDto);
        try {
            topSuperDataSubmissionDto = topDataSubmissionService.saveTopSuperDataSubmissionDtoToBE(topSuperDataSubmissionDto);
        } catch (Exception e) {
            log.error(StringUtil.changeForLog("The Eic saveTOPSuperDataSubmissionDtoToBE failed ===>" + e.getMessage()), e);
        }
        if (!StringUtil.isEmpty(topSuperDataSubmissionDto.getDraftId())) {
            topDataSubmissionService.updateDataSubmissionDraftStatus(topSuperDataSubmissionDto.getDraftId(),
                    DataSubmissionConsts.DS_STATUS_INACTIVE);
        }
        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.TOP_DATA_SUBMISSION, topSuperDataSubmissionDto);
        ParamUtil.setRequestAttr(bpc.request, "emailAddress", DataSubmissionHelper.getLicenseeEmailAddrs(bpc.request));
        ParamUtil.setRequestAttr(bpc.request, "submittedBy", DataSubmissionHelper.getLoginContext(bpc.request).getUserName());
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.PRINT_FLAG, DataSubmissionConstant.PRINT_FLAG_ACKTOP);
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, DataSubmissionConstant.PAGE_STAGE_ACK);

    }

    /**
     * Step: DoDraft
     *
     * @param bpc
     */
    public void doDraft(BaseProcessClass bpc) {
        log.info(" ----- DoDraft ------ ");
        String currentStage = (String) ParamUtil.getRequestAttr(bpc.request,"currentStage");
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE, currentStage);
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
        log.info(" ----- DoRfc ------ ");
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
        log.info(" ----- DoControl ------ ");
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
            }
        } else if ("previous".equals(crudType)) {//back
            actionType = DataSubmissionHelper.setPreviousAction(DataSubmissionConsts.DS_TOP, bpc.request);
        } else if ("page".equals(crudType) || "preview".equals(crudType)) {
            actionType = crudType;
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
        log.info(" ----- DoReturn ------ ");
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(bpc.request);
        String target = InboxConst.URL_MAIN_WEB_MODULE + "MohInternetInbox";
        if (topSuperDataSubmissionDto != null && DataSubmissionConsts.DS_APP_TYPE_NEW.equals(topSuperDataSubmissionDto.getAppType())) {
            target = InboxConst.URL_LICENCE_WEB_MODULE + "MohDataSubmission";
        }
        StringBuilder url = new StringBuilder();
        url.append(InboxConst.URL_HTTPS)
                .append(bpc.request.getServerName())
                .append(target);
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
        IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
    }
    private TopSuperDataSubmissionDto initTopSuperDataSubmissionDto(HttpServletRequest request, TopSuperDataSubmissionDto topSuperDataSubmissionDto) {

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
    }
