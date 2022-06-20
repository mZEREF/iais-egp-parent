package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationProperty;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.LicenceViewService;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceFeMsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.DpDataSubmissionService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.DsLicenceService;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description DpCommonDelegator
 * @Auther chenlei on 11/18/2021.
 */
@Slf4j
public abstract class DpCommonDelegator {

    protected static final String ACTION_TYPE_PAGE = "page";
    protected static final String ACTION_TYPE_RETURN = "return";
    protected static final String ACTION_TYPE_CONFIRM = "confirm";
    protected static final String ACTION_TYPE_DRAFT = "draft";
    protected static final String ACTION_TYPE_SUBMISSION = "submission";

    @Autowired
    private DpDataSubmissionService dpDataSubmissionService;

    @Autowired
    LicenceFeMsgTemplateClient licenceFeMsgTemplateClient;

    @Autowired
    NotificationHelper notificationHelper;

    @Autowired
    LicenceViewService licenceViewService;

    @Autowired
    DsLicenceService dsLicenceService;

    @Autowired
    private LicenceClient licenceClient;
    /**
     * StartStep: Start
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("-----" + this.getClass().getSimpleName() + " Start -----"));
        start(bpc);
    }

    /**
     * StartStep: Start
     *
     * @param bpc
     * @throws
     */
    public void start(BaseProcessClass bpc) {
        HttpSession session = bpc.request.getSession();
        session.removeAttribute(DataSubmissionConstant.DP_DATA_LIST);
    }

    /**
     * StartStep: PrepareSwitch
     *
     * @param bpc
     * @throws
     */
    public void doPrepareSwitch(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("-----" + this.getClass().getSimpleName() + " Prepare Switch -----"));
        DpSuperDataSubmissionDto superDto = DataSubmissionHelper.getCurrentDpDataSubmission(bpc.request);
        ParamUtil.setRequestAttr(bpc.request, "title", DataSubmissionHelper.getMainTitle(superDto.getAppType()));
        String actionType = (String) ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        log.info(StringUtil.changeForLog("----- Action Type: " + actionType + " -----"));
        if (StringUtil.isEmpty(actionType)) {
            actionType = ACTION_TYPE_PAGE;
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, actionType);
        }
        prepareSwitch(bpc);
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", DataSubmissionHelper.getSmallTitle(DataSubmissionConsts.DS_DRP,
                superDto.getAppType(), superDto.getSubmissionType()));
    }

    /**
     * StartStep: PrepareSwitch
     *
     * @param bpc
     * @throws
     */
    public abstract void prepareSwitch(BaseProcessClass bpc);

    /**
     * StartStep: Return
     *
     * @param bpc
     * @throws
     */
    public void doReturn(BaseProcessClass bpc) throws IOException {
        returnStep(bpc);
        DpSuperDataSubmissionDto dpSuperDataSubmissionDto = DataSubmissionHelper.getCurrentDpDataSubmission(bpc.request);
        String URL = InboxConst.URL_LICENCE_WEB_MODULE + "MohDPDataSumission/PrepareSubmission";
        if (dpSuperDataSubmissionDto != null && !DataSubmissionConsts.DS_APP_TYPE_NEW.equals(dpSuperDataSubmissionDto.getAppType())) {
            URL = InboxConst.URL_MAIN_WEB_MODULE + "MohInternetInbox";
        }
        StringBuilder url = new StringBuilder();
        url.append(InboxConst.URL_HTTPS)
                .append(bpc.request.getServerName())
                .append(URL);
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
        IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
    }

    /**
     * StartStep: Return
     *
     * @param bpc
     * @throws
     */
    public void returnStep(BaseProcessClass bpc) {}

    /**
     * StartStep: PreparePage
     *
     * @param bpc
     * @throws
     */
    public void doPreparePage(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("-----" + this.getClass().getSimpleName() + " Prepare Page -----"));
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, ACTION_TYPE_PAGE);
        preparePage(bpc);
    }

    /**
     * StartStep: PreparePage
     *
     * @param bpc
     * @throws
     */
    public void preparePage(BaseProcessClass bpc) {}

    /**
     * StartStep: PrepareConfim
     *
     * @param bpc
     * @throws
     */
    public void doPrepareConfim(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("-----" + this.getClass().getSimpleName() + " Prepare Confirm Page -----"));
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, "preview");
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.PRINT_FLAG, DataSubmissionConstant.PRINT_FLAG_DRP);
        prepareConfim(bpc);
    }

    /**
     * StartStep: PrepareConfim
     *
     * @param bpc
     * @throws
     */
    public void prepareConfim(BaseProcessClass bpc) {}

    ;

    /**
     * StartStep: Draft
     *
     * @param bpc
     * @throws
     */
    public void doDraft(BaseProcessClass bpc) {
        String currentStage = (String) ParamUtil.getRequestAttr(bpc.request, "currentStage");
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, currentStage);
        DpSuperDataSubmissionDto dpSuperDataSubmissionDto = DataSubmissionHelper.getCurrentDpDataSubmission(bpc.request);
        CycleDto cycle = dpSuperDataSubmissionDto.getCycleDto();
        if((DataSubmissionConsts.DP_TYPE_SBT_DRUG_PRESCRIBED.equals(dpSuperDataSubmissionDto.getSubmissionType()))){
            if(DataSubmissionConsts.DRUG_DISPENSED.equals(dpSuperDataSubmissionDto.getDrugPrescribedDispensedDto().getDrugSubmission().getDrugType())){
                cycle.setCycleType(DataSubmissionConsts.DS_CYCLE_DRP_DISPENSED);
            }
        }
        if (dpSuperDataSubmissionDto != null) {
            dpSuperDataSubmissionDto.setDraftNo(dpDataSubmissionService.getDraftNo(DataSubmissionConsts.DS_DRP,
                    dpSuperDataSubmissionDto.getDraftNo()));
            dpSuperDataSubmissionDto = dpDataSubmissionService.saveDataSubmissionDraft(dpSuperDataSubmissionDto);
            DataSubmissionHelper.setCurrentDpDataSubmission(dpSuperDataSubmissionDto, bpc.request);
            ParamUtil.setRequestAttr(bpc.request, "saveDraftSuccess", "success");
        } else {
            log.info(StringUtil.changeForLog("The arSuperDataSubmission is null"));
        }
        draft(bpc);
    }

    /**
     * StartStep: Draft
     *
     * @param bpc
     * @throws
     */
    public void draft(BaseProcessClass bpc) {}

    ;

    /**
     * StartStep: Submission
     *
     * @param bpc
     * @throws
     */
    public void doSubmission(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("-----" + this.getClass().getSimpleName() + " Do Submission -----"));
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, "ack");
        submission(bpc);
        DpSuperDataSubmissionDto dpSuperDataSubmissionDto = DataSubmissionHelper.getCurrentDpDataSubmission(bpc.request);
        DataSubmissionDto dataSubmissionDto = dpSuperDataSubmissionDto.getDataSubmissionDto();
        CycleDto cycle = dpSuperDataSubmissionDto.getCycleDto();
        String cycleType = cycle.getCycleType();
        if (StringUtil.isEmpty(dataSubmissionDto.getSubmissionNo())) {
            String submissionNo = dpDataSubmissionService.getSubmissionNo(DataSubmissionConsts.DS_DRP);
            dataSubmissionDto.setSubmissionNo(submissionNo);
        }
        if (DataSubmissionConsts.DS_APP_TYPE_RFC.equals(dataSubmissionDto.getAppType())) {
            dataSubmissionDto.setStatus(DataSubmissionConsts.DS_STATUS_AMENDED);
        } else if (StringUtil.isEmpty(dataSubmissionDto.getStatus())) {
            dataSubmissionDto.setStatus(DataSubmissionConsts.DS_STATUS_COMPLETED);
        }
        String stage = dataSubmissionDto.getCycleStage();
        String status = DataSubmissionConsts.DS_STATUS_ACTIVE;
        PatientDto patientDto =dpSuperDataSubmissionDto.getPatientDto() ==null ? new PatientDto() : dpSuperDataSubmissionDto.getPatientDto();
        cycle.setPatientCode(patientDto.getPatientCode());
        cycle.setStatus(status);
        DrugPrescribedDispensedDto drugPrescribedDispensedDto = dpSuperDataSubmissionDto.getDrugPrescribedDispensedDto();
        if(drugPrescribedDispensedDto != null){
            DrugSubmissionDto drugSubmissionDto = drugPrescribedDispensedDto.getDrugSubmission();
            if(drugSubmissionDto != null){
               String drupType = drugSubmissionDto.getDrugType();
               log.info(StringUtil.changeForLog("The drupType is -->:"+drupType));
               if(DataSubmissionConsts.DRUG_PRESCRIBED.equals(drupType)){
                   cycleType =DataSubmissionConsts.DS_CYCLE_DRP_PRESCRIBED;
               }else if(DataSubmissionConsts.DRUG_DISPENSED.equals(drupType)){
                   cycleType =DataSubmissionConsts.DS_CYCLE_DRP_DISPENSED;
               }
            }else{
                log.info(StringUtil.changeForLog("The drugSubmissionDto is null ..."));
            }
        }else{
            log.info(StringUtil.changeForLog("The drugPrescribedDispensedDto is null ..."));
        }
        cycle.setCycleType(cycleType);
        log.info(StringUtil.changeForLog("-----Cycle Type: " + cycleType + " - Stage : " + stage
                + " - Status: " + status + " -----"));

        String licenseeId = null;
        LoginContext loginContext = DataSubmissionHelper.getLoginContext(bpc.request);
        if (loginContext != null) {
            dataSubmissionDto.setSubmitBy(loginContext.getUserId());
            dataSubmissionDto.setSubmitDt(new Date());
            licenseeId = loginContext.getLicenseeId();
        }
        dpSuperDataSubmissionDto.setFe(true);
        dpSuperDataSubmissionDto = dpDataSubmissionService.saveDpSuperDataSubmissionDto(dpSuperDataSubmissionDto);
        try {
            dpSuperDataSubmissionDto = dpDataSubmissionService.saveDpSuperDataSubmissionDtoToBE(dpSuperDataSubmissionDto);
        } catch (Exception e) {
            log.error(StringUtil.changeForLog("The Eic saveDpSuperDataSubmissionDtoToBE failed ===>" + e.getMessage()), e);
        }
        if (!StringUtil.isEmpty(dpSuperDataSubmissionDto.getDraftId())) {
            dpDataSubmissionService.updateDataSubmissionDraftStatus(dpSuperDataSubmissionDto.getDraftId(),
                    DataSubmissionConsts.DS_STATUS_INACTIVE);
        }

        String serverName = null;
        if(dpSuperDataSubmissionDto.getCycleDto().getCycleType().equals(DataSubmissionConsts.DS_CYCLE_PATIENT_DRP)){
            serverName ="Patient Information";
        }else if(dpSuperDataSubmissionDto.getCycleDto().getCycleType().equals(DataSubmissionConsts.DS_CYCLE_DRP_PRESCRIBED)){
            serverName ="Drug Prescribed";
        }else if(dpSuperDataSubmissionDto.getCycleDto().getCycleType().equals(DataSubmissionConsts.DS_CYCLE_DRP_DISPENSED)){
            serverName = "Drug Dispensed";
        }

        LicenseeDto licenseeDto = licenceViewService.getLicenseeDtoBylicenseeId(licenseeId);
        String licenseeDtoName = licenseeDto.getName();
        String submissionNo = dpSuperDataSubmissionDto.getDataSubmissionDto().getSubmissionNo();
        String licenceId = "";
        List<LicenceDto> licenceDtoList = licenceClient.getLicenceDtoByHciCode(dpSuperDataSubmissionDto.getHciCode(), licenseeId).getEntity();
        if (!IaisCommonUtils.isEmpty(licenceDtoList)) {
            LicenceDto licenceDto = licenceDtoList.get(0);
            licenceId = licenceDto.getId();
        }
        try {
            sendMsgAndEmail(serverName,licenceId, licenseeDtoName, submissionNo);
        } catch (IOException | TemplateException e) {
            log.error(e.getMessage(), e);
        }
        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.DP_DATA_SUBMISSION, dpSuperDataSubmissionDto);
        ParamUtil.setRequestAttr(bpc.request, "emailAddress", DataSubmissionHelper.getLicenseeEmailAddrs(bpc.request));
        ParamUtil.setRequestAttr(bpc.request, "submittedBy", DataSubmissionHelper.getLoginContext(bpc.request).getUserName());
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, DataSubmissionConstant.PAGE_STAGE_ACK);
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.PRINT_FLAG, DataSubmissionConstant.PRINT_FLAG_ACKDRP);
    }

    /**
     * StartStep: Submission
     *
     * @param bpc
     * @throws
     */
    public void submission(BaseProcessClass bpc) {}

    ;

    /**
     * StartStep: PageAction
     *
     * @param bpc
     * @throws
     */
    public void doPageAction(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("-----" + this.getClass().getSimpleName() + " Page Action -----"));
        // for draft back
        ParamUtil.setRequestAttr(bpc.request, "currentStage", ACTION_TYPE_PAGE);
        String actionType = ParamUtil.getString(bpc.request, DataSubmissionConstant.CRUD_TYPE);
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, actionType);
        getRfcCommon(bpc.request);
        pageAction(bpc);
    }

    /**
     * StartStep: PageAction
     *
     * @param bpc
     * @throws
     */
    public abstract void pageAction(BaseProcessClass bpc);

    /**
     * StartStep: PageConfirmAction
     *
     * @param bpc
     * @throws
     */
    public void doPageConfirmAction(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("-----" + this.getClass().getSimpleName() + " Confirm Action -----"));
        // for draft back
        ParamUtil.setRequestAttr(bpc.request, "currentStage", ACTION_TYPE_CONFIRM);
        String crud_action_type = ParamUtil.getString(bpc.request, DataSubmissionConstant.CRUD_TYPE);
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, crud_action_type);
        // declaration
        DpSuperDataSubmissionDto dpSuperDataSubmission = DataSubmissionHelper.getCurrentDpDataSubmission(bpc.request);
        DataSubmissionDto dataSubmissionDto = dpSuperDataSubmission.getDataSubmissionDto();
        if(dataSubmissionDto.getSubmissionType().equals(DataSubmissionConsts.DP_TYPE_SBT_PATIENT_INFO)){
            String[] declaration = ParamUtil.getStrings(bpc.request, "declaration");
            if(declaration != null && declaration.length >0){
                dataSubmissionDto.setDeclaration(declaration[0]);
            }else{
                dataSubmissionDto.setDeclaration(null);
            }
        }
        DataSubmissionHelper.setCurrentDpDataSubmission(dpSuperDataSubmission, bpc.request);
        // others
        pageConfirmAction(bpc);
    }

    /**
     * StartStep: PageConfirmAction
     *
     * @param bpc
     * @throws
     */
    public void pageConfirmAction(BaseProcessClass bpc) {
        DpSuperDataSubmissionDto dpSuperDataSubmission = DataSubmissionHelper.getCurrentDpDataSubmission(bpc.request);
        DataSubmissionDto dataSubmissionDto = dpSuperDataSubmission.getDataSubmissionDto();
        if(dataSubmissionDto.getSubmissionType().equals(DataSubmissionConsts.DP_TYPE_SBT_PATIENT_INFO)){
            Map<String, String> errorMap = IaisCommonUtils.genNewHashMap(1);
            //for declaration
            String crud_action_type = ParamUtil.getString(bpc.request, DataSubmissionConstant.CRUD_TYPE);
            if("submission".equals(crud_action_type)){
                String[] declaration = ParamUtil.getStrings(bpc.request, "declaration");
                if(declaration == null || declaration.length == 0){
                    errorMap.put("declaration", "GENERAL_ERR0006");
                }
            }
            if (!errorMap.isEmpty()) {
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_TYPE_CONFIRM);
            }
        }
    }




    protected void verifyRfcCommon(HttpServletRequest request,Map<String,String> errorMap){
        if(isRfc(request)){
            DpSuperDataSubmissionDto dpSuperDataSubmissionDto = DataSubmissionHelper.getCurrentDpDataSubmission(request);
            DataSubmissionDto dataSubmissionDto = dpSuperDataSubmissionDto.getDataSubmissionDto();
            if(StringUtil.isEmpty(dataSubmissionDto.getAmendReason())){
                errorMap.put("amendReason","GENERAL_ERR0006");
            }else if("DP_TP001".equals(dataSubmissionDto.getSubmissionType())){
                if(isOthers(dataSubmissionDto.getAmendReason()) && StringUtil.isEmpty(dataSubmissionDto.getAmendReasonOther())){
                    errorMap.put("amendReasonOther","GENERAL_ERR0006");
                }
            }else if("DP_TP002".equals(dataSubmissionDto.getSubmissionType())){
                DrugPrescribedDispensedDto drugPrescribedDispensedDto=dpSuperDataSubmissionDto.getDrugPrescribedDispensedDto();
                DrugSubmissionDto drugSubmissionDto=drugPrescribedDispensedDto.getDrugSubmission();
                String drugType=drugSubmissionDto.getDrugType();
                if("DPD001".equals(drugType)){
                    if(isSdp(dataSubmissionDto.getAmendReason()) && StringUtil.isEmpty(dataSubmissionDto.getAmendReasonOther())){
                        errorMap.put("amendReasonOther","GENERAL_ERR0006");
                    }
                }else if("DPD002".equals(drugType)){
                    if(isSdd(dataSubmissionDto.getAmendReason()) && StringUtil.isEmpty(dataSubmissionDto.getAmendReasonOther())){
                        errorMap.put("amendReasonOther","GENERAL_ERR0006");
                    }
                }
            }
        }
    }

    protected void getRfcCommon(HttpServletRequest request){
        if(isRfc(request)){
            DpSuperDataSubmissionDto dpSuperDataSubmissionDto = DataSubmissionHelper.getCurrentDpDataSubmission(request);
            DataSubmissionDto dataSubmissionDto = dpSuperDataSubmissionDto.getDataSubmissionDto();
            dataSubmissionDto.setAmendReason(ParamUtil.getString(request,"amendReason"));
            if("DP_TP001".equals(dataSubmissionDto.getSubmissionType())){
                if(isOthers(dataSubmissionDto.getAmendReason())){
                    dataSubmissionDto.setAmendReasonOther(ParamUtil.getString(request,"amendReasonOther"));
                }else {
                    dataSubmissionDto.setAmendReasonOther(null);
                }
            }else if("DP_TP002".equals(dataSubmissionDto.getSubmissionType())){
                DrugPrescribedDispensedDto drugPrescribedDispensedDto=dpSuperDataSubmissionDto.getDrugPrescribedDispensedDto();
                DrugSubmissionDto drugSubmissionDto=drugPrescribedDispensedDto.getDrugSubmission();
                String drugType=drugSubmissionDto.getDrugType();
                if("DPD001".equals(drugType)){
                    if(isSdp(dataSubmissionDto.getAmendReason())){
                        dataSubmissionDto.setAmendReasonOther(ParamUtil.getString(request,"amendReasonOther"));
                    }else {
                        dataSubmissionDto.setAmendReasonOther(null);
                    }
                }else if("DPD002".equals(drugType)){
                    if(isSdd(dataSubmissionDto.getAmendReason())){
                        dataSubmissionDto.setAmendReasonOther(ParamUtil.getString(request,"amendReasonOther"));
                    }else {
                        dataSubmissionDto.setAmendReasonOther(null);
                    }
                }
            }
        }
    }

    private void sendMsgAndEmail(String serverName,String licenceId, String submitterName, String submissionNo) throws IOException, TemplateException {
        MsgTemplateDto msgTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_DS_SUBMITTED_ACK_MSG).getEntity();
        Map<String, Object> msgContentMap = IaisCommonUtils.genNewHashMap();
        msgContentMap.put("serverName", serverName);
        msgContentMap.put("submitterName", submitterName);
        msgContentMap.put("submissionId", submissionNo);
        msgContentMap.put("date", Formatter.formatDateTime(new Date(),"dd/MM/yyyy HH:mm:ss"));
        msgContentMap.put("MOH_AGENCY_NAME", AppConsts.MOH_AGENCY_NAME);

        Map<String, Object> msgSubjectMap = IaisCommonUtils.genNewHashMap();
        msgSubjectMap.put("serverName", serverName);
        String subject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(), msgSubjectMap);

        EmailParam msgParam = new EmailParam();
        msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_DS_SUBMITTED_ACK_MSG);
        msgParam.setTemplateContent(msgContentMap);
        msgParam.setQueryCode(submissionNo);
        msgParam.setReqRefNum(submissionNo);
        msgParam.setServiceTypes(DataSubmissionConsts.DS_DRP);
        msgParam.setRefId(licenceId);
        msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
        msgParam.setSubject(subject);
        notificationHelper.sendNotification(msgParam);
        log.info(StringUtil.changeForLog("***************** send VSS Notification  end *****************"));
        //send email
        EmailParam emailParamEmail = MiscUtil.transferEntityDto(msgParam, EmailParam.class);
        emailParamEmail.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_DS_SUBMITTED_ACK_EMAIL);
        emailParamEmail.setRefIdType(NotificationHelper.RECEIPT_TYPE_LICENSEE_ID);
        notificationHelper.sendNotification(emailParamEmail);
        log.info(StringUtil.changeForLog("***************** send VSS Email  end *****************"));
    }


    public final boolean validatePageData(HttpServletRequest request, Object obj, String property, String passCrudActionType,
            String failedCrudActionType, List validationDtos, Map<Object, ValidationProperty> validationPropertyList) {
        ValidationResult validationResult = WebValidationHelper.validateProperty(obj, property);
        String prefix = "";
        String suffix = "";
        if (IaisCommonUtils.isNotEmpty(validationPropertyList)) {
            ValidationProperty validationProperty = validationPropertyList.get(obj);
            if (validationProperty != null) {
                prefix = validationProperty.getPrefix();
                suffix = validationProperty.getSuffix();
            }
        }
        Map<String, String> errorMap = validationResult.retrieveAll(prefix, suffix);
        if (!IaisCommonUtils.isEmpty(validationDtos)) {
            for (int i = 0; i < validationDtos.size(); i++) {
                if (IaisCommonUtils.isNotEmpty(validationPropertyList)) {
                    ValidationProperty validationProperty = validationPropertyList.get(validationDtos.get(i));
                    if (validationProperty != null) {
                        prefix = validationProperty.getPrefix();
                        suffix = validationProperty.getSuffix();
                    }
                }
                Map<String, String> errorMap1 = WebValidationHelper.validateProperty(validationDtos.get(i), property).retrieveAll(
                        prefix, suffix);
                if (!errorMap1.isEmpty()) {
                    errorMap.putAll(errorMap1);
                }
            }
        }
        verifyRfcCommon(request,errorMap);
        if (!errorMap.isEmpty()) {
            log.info(StringUtil.changeForLog("----- Error Massage: " + errorMap + " -----"));
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, failedCrudActionType);
            return false;
        } else if (StringUtil.isNotEmpty(passCrudActionType)) {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, passCrudActionType);
        }
        return true;
    }
    protected boolean isOthers(String others){
        return StringUtil.isIn(others,DataSubmissionConsts.DP_PATIENT_INFO_AMEND_REASON_OTHERS);
    }

    protected boolean isSdp(String sdp){
        return StringUtil.isIn(sdp,DataSubmissionConsts.DP_DRUG_PRESCRIBED_OTHERS);
    }

    protected boolean isSdd(String sdd){
        return StringUtil.isIn(sdd,DataSubmissionConsts.DP_DRUG_DISPENSED_OTHERS);
    }

    protected boolean isRfc(HttpServletRequest request){
        DpSuperDataSubmissionDto dpSuperDataSubmissionDto = DataSubmissionHelper.getCurrentDpDataSubmission(request);
        return dpSuperDataSubmissionDto != null && dpSuperDataSubmissionDto.getDataSubmissionDto() != null && DataSubmissionConsts.DS_APP_TYPE_RFC.equalsIgnoreCase(dpSuperDataSubmissionDto.getDataSubmissionDto().getAppType());
    }
    public final boolean validatePageData(HttpServletRequest request, Object obj, String property, String... actionType) {
        return needValidate(request, actionType) ? validatePageData(request, obj, property, ACTION_TYPE_CONFIRM, ACTION_TYPE_PAGE,
                null, null) : true;
    }

    public final boolean validatePageDataHaveValidationProperty(HttpServletRequest request, Object obj, String property,
            ValidationProperty validationProperty, String... actionType) {
        Map<Object, ValidationProperty> validationPropertyList = IaisCommonUtils.genNewHashMap();
        validationPropertyList.put(obj, validationProperty);
        return needValidate(request, actionType) ? validatePageData(request, obj, property, ACTION_TYPE_CONFIRM, ACTION_TYPE_PAGE,
                null, validationPropertyList) : true;
    }

    public final boolean validatePageDataHaveValidationProperty(HttpServletRequest request, Object obj, String property,
            List validationDtos, Map<Object, ValidationProperty> validationPropertyList, String... actionType) {
        return needValidate(request, actionType) ? validatePageData(request, obj, property, ACTION_TYPE_CONFIRM, ACTION_TYPE_PAGE,
                validationDtos, validationPropertyList) : true;
    }

    private boolean needValidate(HttpServletRequest request, String... actionType) {
        return StringUtil.isIn(ParamUtil.getString(request, DataSubmissionConstant.CRUD_TYPE), actionType);
    }
}
