package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
import com.ecquaria.cloud.moh.iais.common.helper.dataSubmission.DsConfigHelper;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
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
import java.util.Map;

/**
 * @Description TopDataSubmissionDelegator
 * @Auther chenlei on 12/22/2021.
 */
@Slf4j
@Delegator("topDataSubmissionDelegator")
public class TopDataSubmissionDelegator {

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
    }

    /**
     * Step: PrepareSwitch
     *
     * @param bpc
     */
    public void prepareSwitch(BaseProcessClass bpc) {
        log.info(" ----- PrepareSwitch ------ ");
        String actionType = getActionType(bpc.request);
        log.info(StringUtil.changeForLog("Action Type: " + actionType));
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CRUD_ACTION_TYPE_TOP, actionType);
        ParamUtil.setRequestAttr(bpc.request, "title", DataSubmissionHelper.getMainTitle(DataSubmissionConsts.DS_APP_TYPE_NEW));
        DsConfig config = DsConfigHelper.getCurrentConfig(DataSubmissionConsts.DS_TOP, bpc.request);
        String smallTitle = "";
        if (config != null) {
            smallTitle = config.getText();
        }
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>" + smallTitle + "</strong>");
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
        log.info(" -----PrepareStepData ------ ");
        String pageStage = DataSubmissionConstant.PAGE_STAGE_PAGE;
        DsConfig currentConfig = DsConfigHelper.getCurrentConfig(DataSubmissionConsts.DS_TOP, bpc.request);
        if (DsConfigHelper.TOP_STEP_PREVIEW.equals(currentConfig.getCode())) {
            pageStage = DataSubmissionConstant.PAGE_STAGE_PREVIEW;
        }
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, pageStage);
        String currentCode = currentConfig.getCode();
        log.info(StringUtil.changeForLog(" ----- PrepareStepData Step Code: " + currentCode + " ------ "));
        if (DsConfigHelper.TOP_STEP_PATIENT.equals(currentCode)) {
            preparePatient(bpc.request);
        } else if (DsConfigHelper.TOP_STEP_PLANNING.equals(currentCode)) {
            prepareFamilyPlanning(bpc.request);
        }else if (DsConfigHelper.TOP_STEP_PRE_TERMINATION.equals(currentCode)) {
            preparePreTermination(bpc.request);
        }else if (DsConfigHelper.TOP_STEP_PRESENT_TERMINATION.equals(currentCode)) {
            preparePresentTermination(bpc.request);
        }else if (DsConfigHelper.TOP_STEP_POST_TERMINATION.equals(currentCode)) {
            preparePostTermination(bpc.request);
        }/*else if (DsConfigHelper.TOP_STEP_PREVIEW.equals(currentCode)) {

        }*/
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
        if (DsConfigHelper.TOP_STEP_PATIENT.equals(currentCode)) {
            status = doPatient(bpc.request);
        } else if (DsConfigHelper.TOP_STEP_PLANNING.equals(currentCode)) {
            status = doFamilyPlanning(bpc.request);
        }else if (DsConfigHelper.TOP_STEP_PRE_TERMINATION.equals(currentCode)) {
            status = doPreTermination(bpc.request);
        }else if (DsConfigHelper.TOP_STEP_PRESENT_TERMINATION.equals(currentCode)) {
            status = doPresentTermination(bpc.request);
        }else if (DsConfigHelper.TOP_STEP_POST_TERMINATION.equals(currentCode)) {
            status = doPostTermination(bpc.request);
        }/*else if (DsConfigHelper.TOP_STEP_PREVIEW.equals(currentCode)) {

        }*/
        log.info(StringUtil.changeForLog(" ----- DoStep Status: " + status + " ------ "));
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.ACTION_STATUS, status);
    }

    private void preparePatient(HttpServletRequest request) {
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(request);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.TOP_DATA_SUBMISSION, topSuperDataSubmissionDto);
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
    }

    private int doPatient(HttpServletRequest request) {
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(request);
        topSuperDataSubmissionDto = topSuperDataSubmissionDto  == null ? new TopSuperDataSubmissionDto() : topSuperDataSubmissionDto;
        TerminationOfPregnancyDto terminationOfPregnancyDto = topSuperDataSubmissionDto.getTerminationOfPregnancyDto() == null ? new TerminationOfPregnancyDto() : topSuperDataSubmissionDto.getTerminationOfPregnancyDto();
        PatientInformationDto patientInformationDto = terminationOfPregnancyDto.getPatientInformationDto() == null ? new PatientInformationDto() : terminationOfPregnancyDto.getPatientInformationDto();
        ControllerHelper.get(request, patientInformationDto);
        terminationOfPregnancyDto.setPatientInformationDto(patientInformationDto);
        topSuperDataSubmissionDto.setTerminationOfPregnancyDto(terminationOfPregnancyDto);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.TOP_DATA_SUBMISSION, topSuperDataSubmissionDto);
        Map<String,String> errMap = IaisCommonUtils.genNewHashMap();
       if(DataSubmissionHelper.isToNextAction(request)){
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
        ControllerHelper.get(request, familyPlanDto);
        terminationOfPregnancyDto.setFamilyPlanDto(familyPlanDto);
        topSuperDataSubmissionDto.setTerminationOfPregnancyDto(terminationOfPregnancyDto);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.TOP_DATA_SUBMISSION, topSuperDataSubmissionDto);
        Map<String,String> errMap = IaisCommonUtils.genNewHashMap();
        if(DataSubmissionHelper.isToNextAction(request)){
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
        if(DataSubmissionHelper.isToNextAction(request)){
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
        ControllerHelper.get(request, terminationDto);
        String topDate=ParamUtil.getString(request,"topDate");
        terminationDto.setTopDate(topDate);
        terminationOfPregnancyDto.setTerminationDto(terminationDto);
        topSuperDataSubmissionDto.setTerminationOfPregnancyDto(terminationOfPregnancyDto);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.TOP_DATA_SUBMISSION, topSuperDataSubmissionDto);
        Map<String,String> errMap = IaisCommonUtils.genNewHashMap();
        if(DataSubmissionHelper.isToNextAction(request)){
            ValidationResult result = WebValidationHelper.validateProperty(terminationDto,"TOP");
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



    private int doPostTermination(HttpServletRequest request) {
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(request);
        topSuperDataSubmissionDto = topSuperDataSubmissionDto  == null ? new TopSuperDataSubmissionDto() : topSuperDataSubmissionDto;
        TerminationOfPregnancyDto terminationOfPregnancyDto = topSuperDataSubmissionDto.getTerminationOfPregnancyDto() == null ? new TerminationOfPregnancyDto() : topSuperDataSubmissionDto.getTerminationOfPregnancyDto();
        PostTerminationDto postTerminationDto = terminationOfPregnancyDto.getPostTerminationDto() == null ? new PostTerminationDto() : terminationOfPregnancyDto.getPostTerminationDto();
        ControllerHelper.get(request, postTerminationDto);
        terminationOfPregnancyDto.setPostTerminationDto(postTerminationDto);
        topSuperDataSubmissionDto.setTerminationOfPregnancyDto(terminationOfPregnancyDto);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.TOP_DATA_SUBMISSION, topSuperDataSubmissionDto);
        Map<String,String> errMap = IaisCommonUtils.genNewHashMap();
        if(DataSubmissionHelper.isToNextAction(request)){
            ValidationResult result = WebValidationHelper.validateProperty(postTerminationDto,"TOP");
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

    /**
     * Step: DoSubmission
     *
     * @param bpc
     */
    public void doSubmission(BaseProcessClass bpc) {
        log.info(" ----- DoSubmission ------ ");
        /**
         * DpSuperDataSubmissionDto dpSuperDataSubmissionDto = DataSubmissionHelper.getCurrentDpDataSubmission(bpc.request);
         *         DataSTubmissionDto dataSubmissionDto = dpSuperDataSubmissionDto.getDataSubmissionDto();
         *          *         CycleDto cycle = dpSuperDataSubmissionDto.getCycleDto();
         *          *         String cycleType = cycle.getCycleType();
         *          *         if (StringUtil.isEmpty(dataSubmissionDto.getSubmissionNo())) {
         *          *             String submissionNo = dpDataSubmissionService.getSubmissionNo(DataSubmissionConsts.DS_DRP);
         *          *             dataSubmissionDto.setSubmissionNo(submissionNo);
         *          *         }
         *          *         if (StringUtil.isEmpty(dataSubmissionDto.getStatus())) {
         *          *             dataSubmissionDto.setStatus(DataSubmissionConsts.DS_STATUS_COMPLETED);
         *          *         }
         *          *         String stage = dataSubmissionDto.getCycleStage();
         *          *         String status = DataSubmissionConsts.DS_STATUS_ACTIVE;
         *          *
         *          *         cycle.setStatus(status);
         *          *         log.info(StringUtil.changeForLog("-----Cycle Type: " + cycleype + " - Stage : " + stage
         *                 + " - Status: " + status + " -----"));
         *
         *         LoginContext loginContext = DataSubmissionHelper.getLoginContext(bpc.request);
         *         if (loginContext != null) {
         *             dataSubmissionDto.setSubmitBy(loginContext.getUserId());
         *             dataSubmissionDto.setSubmitDt(new Date());
         *         }
         *         dpSuperDataSubmissionDto = dpDataSubmissionService.saveDpSuperDataSubmissionDto(dpSuperDataSubmissionDto);
         *         try {
         *             dpSuperDataSubmissionDto = dpDataSubmissionService.saveDpSuperDataSubmissionDtoToBE(dpSuperDataSubmissionDto);
         *         } catch (Exception e) {
         *             log.error(StringUtil.changeForLog("The Eic saveDpSuperDataSubmissionDtoToBE failed ===>" + e.getMessage()), e);
         *         }
         *         if (!StringUtil.isEmpty(dpSuperDataSubmissionDto.getDraftId())) {
         *             dpDataSubmissionService.updateDataSubmissionDraftStatus(dpSuperDataSubmissionDto.getDraftId(),
         *                     DataSubmissionConsts.DS_STATUS_INACTIVE);
         *         }
         *         ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.DP_DATA_SUBMISSION, dpSuperDataSubmissionDto);
         *         ParamUtil.setRequestAttr(bpc.request, "emailAddress", DataSubmissionHelper.getLicenseeEmailAddrs(bpc.request));
         *         ParamUtil.setRequestAttr(bpc.request, "submittedBy", DataSubmissionHelper.getLoginContext(bpc.request).getUserName());
         *         ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, DataSubmissionConstant.PAGE_STAGE_ACK);
         *         ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.PRINT_FLAG, DataSubmissionConstant.PRINT_FLAG_ACKDRP);
         */
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, DataSubmissionConstant.PAGE_STAGE_ACK);

    }

    /**
     * Step: DoDraft
     *
     * @param bpc
     */
    public void doDraft(BaseProcessClass bpc) {
        log.info(" ----- DoDraft ------ ");
        String currentStage = (String) ParamUtil.getRequestAttr(bpc.request, "currentStage");
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, currentStage);
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(bpc.request);
        if (topSuperDataSubmissionDto != null) {
            topSuperDataSubmissionDto.setDraftNo(topDataSubmissionService.getDraftNo(DataSubmissionConsts.DS_TOP,
                    topSuperDataSubmissionDto.getDraftNo()));
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
            }
        } else if (DataSubmissionHelper.isToNextAction(bpc.request)) {
            Integer status = (Integer) ParamUtil.getRequestAttr(bpc.request, DataSubmissionConstant.ACTION_STATUS);
            if (status == null || 0 == status) {// current
                actionType = DataSubmissionHelper.setCurrentAction(DataSubmissionConsts.DS_TOP, bpc.request);
            } else if (1 == status) { // nexts
                actionType = crudType;
                DsConfigHelper.setActiveConfig(actionType, bpc.request);
            }
        } else if ("previous".equals(crudType)) {
            actionType = DataSubmissionHelper.setPreviousAction(DataSubmissionConsts.DS_TOP, bpc.request);
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
}
