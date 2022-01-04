package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
import com.ecquaria.cloud.moh.iais.common.helper.dataSubmission.DsConfigHelper;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.CommonValidator;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationProperty;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.service.datasubmission.VssDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Process: MohVSSDataSubmission
 *
 * @Description VssDataSubmissionDelegator
 * @Auther chenlei on 12/13/2021.
 */
@Slf4j
@Delegator("vssDataSubmissionDelegator")
public class VssDataSubmissionDelegator {

    @Autowired
    private VssDataSubmissionService vssDataSubmissionService;

    /**
     * Step: Start
     *
     * @param bpc
     */
    public void doStart(BaseProcessClass bpc) {
        log.info(" -----VssDataSubmissionDelegator Start ------ ");
        DsConfigHelper.clearVssSession(bpc.request);
        DsConfigHelper.initVssConfig(bpc.request);
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
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CRUD_ACTION_TYPE_VSS, actionType);
        ParamUtil.setRequestAttr(bpc.request, "title", DataSubmissionHelper.getMainTitle(DataSubmissionConsts.DS_APP_TYPE_NEW));
        DsConfig config = DsConfigHelper.getCurrentConfig(DataSubmissionConsts.DS_VSS, bpc.request);
        String smallTitle = "";
        if (config != null) {
            smallTitle = config.getText();
        }
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>" + smallTitle + "</strong>");
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
        }
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
        DsConfig currentConfig = DsConfigHelper.getCurrentConfig(DataSubmissionConsts.DS_VSS, bpc.request);
        String currentCode = currentConfig.getCode();
        log.info(StringUtil.changeForLog(" ----- DoStep Step Code: " + currentCode + " ------ "));
        int status = 0;
        if (DsConfigHelper.VSS_STEP_TREATMENT.equals(currentCode)) {
            status = doTreatment(bpc.request);
        } else if (DsConfigHelper.VSS_STEP_CONSENT_PARTICULARS.equals(currentCode)) {
            status = doConsentParticulars(bpc.request);
        } else if (DsConfigHelper.VSS_STEP_TFSSP_PARTICULARS.equals(currentCode)) {
            status = doTfsspParticulars(bpc.request);
        } /*else if(DsConfigHelper.VSS_STEP_PREVIEW.equals(currentCode)){
            doPreview(bpc.request);
        }*/
        log.info(StringUtil.changeForLog(" ----- DoStep Status: " + status + " ------ "));
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.ACTION_STATUS, status);
    }

    private void prepareTreatment(HttpServletRequest request) {
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = DataSubmissionHelper.getCurrentVssDataSubmission(request);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.VSS_DATA_SUBMISSION, vssSuperDataSubmissionDto);
    }

    private int doTreatment(HttpServletRequest request) {
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = DataSubmissionHelper.getCurrentVssDataSubmission(request);
        vssSuperDataSubmissionDto = vssSuperDataSubmissionDto  == null ? new VssSuperDataSubmissionDto() : vssSuperDataSubmissionDto;
        VssTreatmentDto vssTreatmentDto = vssSuperDataSubmissionDto.getVssTreatmentDto() == null ? new VssTreatmentDto() : vssSuperDataSubmissionDto.getVssTreatmentDto();
        TreatmentDto treatmentDto = vssTreatmentDto.getTreatmentDto() == null ? new TreatmentDto() : vssTreatmentDto.getTreatmentDto();
        ControllerHelper.get(request,treatmentDto);
        try {
           int age= -Formatter.compareDateByDay(treatmentDto.getBirthData());
            treatmentDto.setAge(age/365);
        }catch (Exception e){
            e.printStackTrace();
        }
        vssTreatmentDto.setTreatmentDto(treatmentDto);
        vssSuperDataSubmissionDto.setVssTreatmentDto(vssTreatmentDto);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.VSS_DATA_SUBMISSION, vssSuperDataSubmissionDto);
        Map<String,String> errMap = IaisCommonUtils.genNewHashMap();
        String actionType = ParamUtil.getString(request, DataSubmissionConstant.CRUD_TYPE);
        if("next".equals(actionType)){
            ValidationResult result = WebValidationHelper.validateProperty(treatmentDto,"VSS");
            if(result !=null){
                errMap.putAll(result.retrieveAll());
            }
        }
        if(!errMap.isEmpty()){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errMap));
            return 0;
        }
        return 1;
    }

    private void prepareConsentParticulars(HttpServletRequest request) {
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = DataSubmissionHelper.getCurrentVssDataSubmission(request);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.VSS_DATA_SUBMISSION, vssSuperDataSubmissionDto);
    }

    private int doConsentParticulars(HttpServletRequest request) {
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = DataSubmissionHelper.getCurrentVssDataSubmission(request);
        vssSuperDataSubmissionDto = vssSuperDataSubmissionDto  == null ? new VssSuperDataSubmissionDto() : vssSuperDataSubmissionDto;
        VssTreatmentDto vssTreatmentDto = vssSuperDataSubmissionDto.getVssTreatmentDto() == null ? new VssTreatmentDto() : vssSuperDataSubmissionDto.getVssTreatmentDto();
        GuardianAppliedPartDto guardianAppliedPartDto = vssTreatmentDto.getGuardianAppliedPartDto() == null ? new GuardianAppliedPartDto() : vssTreatmentDto.getGuardianAppliedPartDto();
        ControllerHelper.get(request,guardianAppliedPartDto);
        String appliedPartBirthday = ParamUtil.getString(request,"appliedPartBirthday");
        String guardianBirthday = ParamUtil.getString(request,"guardianBirthday");
        String courtOrderIssueDate = ParamUtil.getString(request,"courtOrderIssueDate");
        try {
            Date aDate = Formatter.parseDate(appliedPartBirthday);
            Date gDate = Formatter.parseDate(guardianBirthday);
            Date cDate = Formatter.parseDate(courtOrderIssueDate);
            guardianAppliedPartDto.setAppliedPartBirthday(aDate);
            guardianAppliedPartDto.setGuardianBirthday(gDate);
            guardianAppliedPartDto.setCourtOrderIssueDate(cDate);
        } catch (Exception e) {
           e.printStackTrace();
        }
        vssTreatmentDto.setGuardianAppliedPartDto(guardianAppliedPartDto);
        vssSuperDataSubmissionDto.setVssTreatmentDto(vssTreatmentDto);

        ParamUtil.setSessionAttr(request, DataSubmissionConstant.VSS_DATA_SUBMISSION, vssSuperDataSubmissionDto);

        Map<String,String> errMap = IaisCommonUtils.genNewHashMap();
        String actionType = ParamUtil.getString(request, DataSubmissionConstant.CRUD_TYPE);
        if("next".equals(actionType)){
            ValidationResult result = WebValidationHelper.validateProperty(guardianAppliedPartDto,"VSS");
            if(result !=null){
                errMap.putAll(result.retrieveAll());
            }
        }
        if(!errMap.isEmpty()){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errMap));
            return 0;
        }
        return 1;
    }

    private void prepareTfsspParticulars(HttpServletRequest request) {
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = DataSubmissionHelper.getCurrentVssDataSubmission(request);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.VSS_DATA_SUBMISSION, vssSuperDataSubmissionDto);
    }

    private int doTfsspParticulars(HttpServletRequest request) {
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = DataSubmissionHelper.getCurrentVssDataSubmission(request);
        vssSuperDataSubmissionDto = vssSuperDataSubmissionDto  == null ? new VssSuperDataSubmissionDto() : vssSuperDataSubmissionDto;
        VssTreatmentDto vssTreatmentDto = vssSuperDataSubmissionDto.getVssTreatmentDto() == null ? new VssTreatmentDto() : vssSuperDataSubmissionDto.getVssTreatmentDto();
        SexualSterilizationDto sexualSterilizationDto = vssTreatmentDto.getSexualSterilizationDto() == null ? new SexualSterilizationDto() : vssTreatmentDto.getSexualSterilizationDto();
        ControllerHelper.get(request,sexualSterilizationDto);
        String operationDate = ParamUtil.getString(request,"operationDate");
        String hecReviewDate = ParamUtil.getString(request,"hecReviewDate");
        try {
            Date oDate = Formatter.parseDate(operationDate);
            Date hDate = Formatter.parseDate(hecReviewDate);
            sexualSterilizationDto.setOperationDate(oDate);
            sexualSterilizationDto.setHecReviewDate(hDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        vssTreatmentDto.setSexualSterilizationDto(sexualSterilizationDto);
        vssSuperDataSubmissionDto.setVssTreatmentDto(vssTreatmentDto);

        ParamUtil.setSessionAttr(request, DataSubmissionConstant.VSS_DATA_SUBMISSION, vssSuperDataSubmissionDto);

        Map<String,String> errMap = IaisCommonUtils.genNewHashMap();
        String actionType = ParamUtil.getString(request, DataSubmissionConstant.CRUD_TYPE);
        if("next".equals(actionType)){
            ValidationResult result = WebValidationHelper.validateProperty(sexualSterilizationDto,"VSS");
            if(result !=null){
                errMap.putAll(result.retrieveAll());
            }
        }
        if(!errMap.isEmpty()){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errMap));
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
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = DataSubmissionHelper.getCurrentVssDataSubmission(bpc.request);
        vssSuperDataSubmissionDto.setDataSubmissionDto(DataSubmissionHelper.initDataSubmission(vssSuperDataSubmissionDto,
                false));
        vssSuperDataSubmissionDto.setCycleDto(DataSubmissionHelper.initCycleDto(vssSuperDataSubmissionDto, false));
        DataSubmissionHelper.setCurrentVssDataSubmission(vssSuperDataSubmissionDto,bpc.request);
        DataSubmissionDto dataSubmissionDto = vssSuperDataSubmissionDto.getDataSubmissionDto();
        if (StringUtil.isEmpty(dataSubmissionDto.getSubmissionNo())) {
            String submissionNo = vssDataSubmissionService.getSubmissionNo(DataSubmissionConsts.DS_VSS);
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
        vssSuperDataSubmissionDto = vssDataSubmissionService.saveVssSuperDataSubmissionDto(vssSuperDataSubmissionDto);
        try {
            /* vssSuperDataSubmissionDto = vssDataSubmissionService.saveVssSuperDataSubmissionDtoToBE(vssSuperDataSubmissionDto);*/
        } catch (Exception e) {
            log.error(StringUtil.changeForLog("The Eic saveVssSuperDataSubmissionDtoToBE failed ===>" + e.getMessage()), e);
        }
        if (!StringUtil.isEmpty(vssSuperDataSubmissionDto.getDraftId())) {
            vssDataSubmissionService.updateDataSubmissionDraftStatus(vssSuperDataSubmissionDto.getDraftId(),
                    DataSubmissionConsts.DS_STATUS_INACTIVE);
        }
        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.VSS_DATA_SUBMISSION, vssSuperDataSubmissionDto);
        ParamUtil.setRequestAttr(bpc.request, "emailAddress", DataSubmissionHelper.getLicenseeEmailAddrs(bpc.request));
        ParamUtil.setRequestAttr(bpc.request, "submittedBy", DataSubmissionHelper.getLoginContext(bpc.request).getUserName());
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, DataSubmissionConstant.PAGE_STAGE_ACK);
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.PRINT_FLAG, DataSubmissionConstant.PRINT_FLAG_ACKVSS);
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
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = DataSubmissionHelper.getCurrentVssDataSubmission(bpc.request);
        if (vssSuperDataSubmissionDto != null) {
            vssSuperDataSubmissionDto.setDraftNo(vssDataSubmissionService.getDraftNo(DataSubmissionConsts.DS_VSS,
                    vssSuperDataSubmissionDto.getDraftNo()));
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
            }
        } else if ("previous".equals(crudType)) {
            actionType = DataSubmissionHelper.setPreviousAction(DataSubmissionConsts.DS_VSS, bpc.request);
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
