package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
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
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Delegator("topPatientInformationDelegator")
public class TopPatientInformationDelegator {

    @Autowired
    private TopDataSubmissionService topDataSubmissionService;

    public static final String CRUD_ACTION_TYPE_PI = "crud_action_type_pi";
    public static final String CURRENT_PAGE = "top_current_page";

    public static final String ACTION_TYPE_RETURN = "return";
    public static final String ACTION_TYPE_PAGE = "page";
    public static final String ACTION_TYPE_CONFIRM = "confim";
    public static final String ACTION_TYPE_DRAFT = "draft";
    public static final String ACTION_TYPE_SUBMIT = "submit";

    /**
     * StartStep: Start
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("-----" + this.getClass().getSimpleName() + " Start -----"));
        /*DataSubmissionHelper.clearSession(bpc.request);*/
        String orgId = Optional.ofNullable(DataSubmissionHelper.getLoginContext(bpc.request))
                .map(LoginContext::getOrgId).orElse("");
        String submissionType = ParamUtil.getString(bpc.request, "submissionType");
        TopSuperDataSubmissionDto dataSubmissionDraft = topDataSubmissionService.getTopSuperDataSubmissionDtoDraftByConds(orgId,submissionType);
        if (dataSubmissionDraft != null) {
            ParamUtil.setRequestAttr(bpc.request, "hasDraft", Boolean.TRUE);
        }
    }

    /**
     * StartStep: Draft
     *
     * @param bpc
     * @throws
     */
    public void doDraft(BaseProcessClass bpc) {
        String currentStage = (String) ParamUtil.getRequestAttr(bpc.request, CURRENT_PAGE);
        ParamUtil.setRequestAttr(bpc.request, CRUD_ACTION_TYPE_PI, currentStage);
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(bpc.request);
        if (topSuperDataSubmissionDto != null) {
            if (StringUtil.isEmpty(topSuperDataSubmissionDto.getDraftNo())) {
                topSuperDataSubmissionDto.setDraftNo(topDataSubmissionService.getDraftNo());
            }
            topSuperDataSubmissionDto = topDataSubmissionService.saveDataSubmissionDraft(topSuperDataSubmissionDto);
            DataSubmissionHelper.setCurrentTopDataSubmission(topSuperDataSubmissionDto, bpc.request);
            ParamUtil.setRequestAttr(bpc.request, "saveDraftSuccess", "success");
        } else {
            log.info(StringUtil.changeForLog("The topSuperDataSubmissionDto is null"));
        }
    }

    /**
     * StartStep: Submission
     *
     * @param bpc
     * @throws
     */

    public void doSubmission(BaseProcessClass bpc) {
        log.info(" ----- DoSubmission ------ ");
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(bpc.request);
        topSuperDataSubmissionDto.setDataSubmissionDto(DataSubmissionHelper.initDataSubmission(topSuperDataSubmissionDto, false));
        topSuperDataSubmissionDto.setCycleDto(DataSubmissionHelper.initCycleDto(topSuperDataSubmissionDto,
                DataSubmissionHelper.getLicenseeId(bpc.request), false));
        DataSubmissionHelper.setCurrentTopDataSubmission(topSuperDataSubmissionDto,bpc.request);
        DataSubmissionDto dataSubmissionDto = topSuperDataSubmissionDto.getDataSubmissionDto();
        CycleDto cycle = topSuperDataSubmissionDto.getCycleDto();
        if (StringUtil.isEmpty(dataSubmissionDto.getSubmissionNo())) {
            String submissionNo = topDataSubmissionService.getSubmissionNo(DataSubmissionConsts.DS_TOP);
            dataSubmissionDto.setSubmissionNo(submissionNo);
        }
        if (StringUtil.isEmpty(dataSubmissionDto.getStatus())) {
            dataSubmissionDto.setStatus(DataSubmissionConsts.DS_STATUS_COMPLETED);
        }

        if (DataSubmissionConsts.DS_APP_TYPE_RFC.equals(dataSubmissionDto.getAppType())) {
            dataSubmissionDto.setStatus(DataSubmissionConsts.DS_STATUS_AMENDED);
        }else if (StringUtil.isEmpty(dataSubmissionDto.getStatus())) {
            dataSubmissionDto.setStatus(DataSubmissionConsts.DS_STATUS_COMPLETED);
        }

        cycle.setStatus(DataSubmissionConsts.DS_STATUS_COMPLETED);

        LoginContext loginContext = DataSubmissionHelper.getLoginContext(bpc.request);
        if (loginContext != null) {
            dataSubmissionDto.setSubmitBy(loginContext.getUserId());
            dataSubmissionDto.setSubmitDt(new Date());
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
        ParamUtil.setRequestAttr(bpc.request, CURRENT_PAGE, ACTION_TYPE_SUBMIT);
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.PRINT_FLAG, DataSubmissionConstant.PRINT_FLAG_ACKTOP);
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, DataSubmissionConstant.PAGE_STAGE_ACK);
    }

    /**
     * StartStep: PrepareConfim
     *
     * @param bpc
     * @throws
     */
    public void doPrepareConfim(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, CURRENT_PAGE, ACTION_TYPE_CONFIRM);
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.PRINT_FLAG, DataSubmissionConstant.PRINT_FLAG_TOP);
    }

    /**
     * StartStep: PageAction
     *
     * @param bpc
     * @throws
     */
    public void doPageAction(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(request);
        if (isRfc(request)) {
            DataSubmissionDto dataSubmissionDto = topSuperDataSubmissionDto.getDataSubmissionDto();
            dataSubmissionDto.setAmendReason(ParamUtil.getString(request, "amendReason"));
            if ("LDTRE_002".equals(dataSubmissionDto.getAmendReason())) {
                dataSubmissionDto.setAmendReasonOther(ParamUtil.getString(request, "amendReasonOther"));
            } else {
                dataSubmissionDto.setAmendReasonOther(null);
            }
        }
        topSuperDataSubmissionDto = topSuperDataSubmissionDto  == null ? new TopSuperDataSubmissionDto() : topSuperDataSubmissionDto;
        PatientInformationDto patientInformationDto=topSuperDataSubmissionDto.getPatientInformationDto();
        if(patientInformationDto == null){
            patientInformationDto=new PatientInformationDto();
        }
        String[] livingChildrenGenders= ParamUtil.getStrings(request, "livingChildrenGenders");
        ControllerHelper.get(request, patientInformationDto);
        if( !IaisCommonUtils.isEmpty(livingChildrenGenders)){

            patientInformationDto.setLivingChildrenGenders(Arrays.asList(livingChildrenGenders));
        }else{
            patientInformationDto.setLivingChildrenGenders(null);
        }

        topSuperDataSubmissionDto.setPatientInformationDto(patientInformationDto);
        DataSubmissionHelper.setCurrentTopDataSubmission(topSuperDataSubmissionDto, request);

        if(StringUtil.isEmpty(patientInformationDto.getOrgId())){
            patientInformationDto.setOrgId(topSuperDataSubmissionDto.getOrgId());
        }
        String crud_action_type = ParamUtil.getRequestString(request, IaisEGPConstant.CRUD_TYPE);
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();

        if (ACTION_TYPE_CONFIRM.equals(crud_action_type)) {
            ValidationResult validationResult = WebValidationHelper.validateProperty(patientInformationDto, "TOP");
            errorMap = validationResult.retrieveAll();
            if (isRfc(request)) {
                TopSuperDataSubmissionDto oldTopSuperDataSubmissionDto = DataSubmissionHelper.getOldTopSuperDataSubmissionDto(request);
                if (errorMap.isEmpty() && oldTopSuperDataSubmissionDto != null && oldTopSuperDataSubmissionDto.getPatientInformationDto() != null && patientInformationDto.equals(oldTopSuperDataSubmissionDto.getPatientInformationDto())) {
                    ParamUtil.setRequestAttr(request, DataSubmissionConstant.RFC_NO_CHANGE_ERROR, AppConsts.YES);
                    crud_action_type = ACTION_TYPE_PAGE;
                }
            }
        }
        if (!errorMap.isEmpty()) {
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            crud_action_type = ACTION_TYPE_PAGE;
        }



        ParamUtil.setRequestAttr(request, CRUD_ACTION_TYPE_PI, crud_action_type);
        ParamUtil.setRequestAttr(bpc.request, CURRENT_PAGE, ACTION_TYPE_PAGE);
    }

    /**
     * StartStep: Return
     *
     * @param bpc
     * @throws
     */
    public void doReturn(BaseProcessClass bpc) throws IOException {
        log.info(" ----- DoReturn ------ ");
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(bpc.request);
        String target = InboxConst.URL_MAIN_WEB_MODULE + "MohInternetInbox";
        if (topSuperDataSubmissionDto != null && DataSubmissionConsts.DS_APP_TYPE_NEW.equals(topSuperDataSubmissionDto.getAppType())) {
            target = InboxConst.URL_LICENCE_WEB_MODULE + "MohNewTOPDataSubmission/PrepareSubmission";
        }
        StringBuilder url = new StringBuilder();
        url.append(InboxConst.URL_HTTPS)
                .append(bpc.request.getServerName())
                .append(target);
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
        IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
    }

    /**
     * StartStep: PageConfirmAction
     *
     * @param bpc
     * @throws
     */
    public void doPageConfirmAction(BaseProcessClass bpc) {
        TopSuperDataSubmissionDto topSuperDataSubmissionDto=DataSubmissionHelper.getCurrentTopDataSubmission(bpc.request);
        if(topSuperDataSubmissionDto==null){
            topSuperDataSubmissionDto=new TopSuperDataSubmissionDto();
        }
        DataSubmissionDto dataSubmissionDto = topSuperDataSubmissionDto.getDataSubmissionDto();
        //declaration
        String crud_action_type = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        String[] declaration = ParamUtil.getStrings(bpc.request, "declaration");
        if(declaration != null && declaration.length >0){
            dataSubmissionDto.setDeclaration(declaration[0]);
        }else{
            dataSubmissionDto.setDeclaration(null);
        }
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap(1);
        if("submission".equals(crud_action_type)){
            if(declaration == null || declaration.length == 0){
                errorMap.put("declaration", "GENERAL_ERR0006");
            }
        }
        if (!errorMap.isEmpty()) {
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            crud_action_type = ACTION_TYPE_CONFIRM;
        }
        ParamUtil.setRequestAttr(bpc.request, CRUD_ACTION_TYPE_PI, crud_action_type);
        ParamUtil.setRequestAttr(bpc.request, CURRENT_PAGE, ACTION_TYPE_CONFIRM);
    }

    /**
     * StartStep: PreparePage
     *
     * @param bpc
     * @throws
     */
    public void doPreparePage(BaseProcessClass bpc) {
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(bpc.request);
        if (topSuperDataSubmissionDto == null) {
            topSuperDataSubmissionDto = initTopSuperDataSubmissionDto(bpc.request);
        }

        PatientInformationDto patientInformationDto = topSuperDataSubmissionDto.getPatientInformationDto();
        if (patientInformationDto == null) {
            patientInformationDto = new PatientInformationDto();
            topSuperDataSubmissionDto.setPatientInformationDto(patientInformationDto);
        }

        DataSubmissionHelper.setCurrentTopDataSubmission(topSuperDataSubmissionDto, bpc.request);
        ParamUtil.setRequestAttr(bpc.request, CURRENT_PAGE, ACTION_TYPE_PAGE);
    }

    private TopSuperDataSubmissionDto initTopSuperDataSubmissionDto(HttpServletRequest request) {
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = new TopSuperDataSubmissionDto();

        String orgId = Optional.ofNullable(DataSubmissionHelper.getLoginContext(request))
                .map(LoginContext::getOrgId).orElse("");

        topSuperDataSubmissionDto.setOrgId(orgId);
        topSuperDataSubmissionDto.setSubmissionMethod(DataSubmissionConsts.DS_METHOD_MANUAL_ENTRY);
        topSuperDataSubmissionDto.setSubmissionType(DataSubmissionConsts.TOP_TYPE_SBT_PATIENT_INFO);
        topSuperDataSubmissionDto.setAppType(DataSubmissionConsts.DS_APP_TYPE_NEW);
        topSuperDataSubmissionDto.setFe(true);
        topSuperDataSubmissionDto.setSvcName(AppServicesConsts.SERVICE_NAME_CLINICAL_LABORATORY);
        topSuperDataSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());

        topSuperDataSubmissionDto.setCycleDto(DataSubmissionHelper.initCycleDto(topSuperDataSubmissionDto,
                DataSubmissionHelper.getLicenseeId(request), false));
        topSuperDataSubmissionDto.setDataSubmissionDto(DataSubmissionHelper.initDataSubmission(topSuperDataSubmissionDto, false));

        return topSuperDataSubmissionDto;
    }

    /**
     * StartStep: PrepareSwitch
     *
     * @param bpc
     * @throws
     */
    public void doPrepareSwitch(BaseProcessClass bpc) {
        String currentAction = ParamUtil.getRequestString(bpc.request, CRUD_ACTION_TYPE_PI);
        if (StringUtil.isEmpty(currentAction)) {
            currentAction = ACTION_TYPE_PAGE;
            ParamUtil.setRequestAttr(bpc.request, CRUD_ACTION_TYPE_PI, currentAction);
        }
    }

    protected void valRFC(HttpServletRequest request, DrugPrescribedDispensedDto drugPrescribedDispensed){
        if(isRfc(request)){
            DpSuperDataSubmissionDto dpOldSuperDataSubmissionDto = DataSubmissionHelper.getOldDpSuperDataSubmissionDto(request);
            if(dpOldSuperDataSubmissionDto != null && dpOldSuperDataSubmissionDto.getDrugPrescribedDispensedDto()!= null && drugPrescribedDispensed.equals(dpOldSuperDataSubmissionDto.getDrugPrescribedDispensedDto())){
                ParamUtil.setRequestAttr(request, DataSubmissionConstant.RFC_NO_CHANGE_ERROR, AppConsts.YES);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE,ACTION_TYPE_PAGE);
            }
        }
    }
    private boolean isRfc(HttpServletRequest request) {
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = DataSubmissionHelper.getCurrentVssDataSubmission(request);
        return vssSuperDataSubmissionDto != null && vssSuperDataSubmissionDto.getDataSubmissionDto() != null && DataSubmissionConsts.DS_APP_TYPE_RFC.equalsIgnoreCase(vssSuperDataSubmissionDto.getDataSubmissionDto().getAppType());
    }

}
