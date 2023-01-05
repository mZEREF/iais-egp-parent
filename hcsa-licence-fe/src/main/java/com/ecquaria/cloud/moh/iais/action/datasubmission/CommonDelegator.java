package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArChangeInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArCurrentInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleStageSelectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DisposalStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsCenterDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.EmbryoTransferredOutcomeStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.EndCycleStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.OutcomeStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationProperty;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceFeMsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant.ACTION_TYPE;

/**
 * CommonDelegator
 *
 * @author suocheng
 * @date 10/20/2021
 */
@Slf4j
public abstract class CommonDelegator {

    protected static final String ACTION_TYPE_PAGE = "page";
    protected static final String ACTION_TYPE_RETURN = "return";
    protected static final String ACTION_TYPE_CONFIRM = "confirm";
    protected static final String ACTION_TYPE_DRAFT = "draft";
    protected static final String ACTION_TYPE_SUBMISSION = "submission";
    protected final static String DONOR_SOURSE_OTHERS = "Others";
    private final static String HAS_DISPOSAL = "hasDisposal";
    @Autowired
    protected ArDataSubmissionService arDataSubmissionService;
    @Autowired
    private LicenceClient licenceClient;
    @Autowired
    NotificationHelper notificationHelper;
    @Autowired
    private LicenceFeMsgTemplateClient licenceFeMsgTemplateClient;

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
        session.removeAttribute(DataSubmissionConstant.AR_DATA_LIST);
    }

    /**
     * StartStep: PrepareSwitch
     *
     * @param bpc
     * @throws
     */
    public void doPrepareSwitch(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("-----" + this.getClass().getSimpleName() + " Prepare Switch -----"));
        ArSuperDataSubmissionDto currentArDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        ParamUtil.setRequestAttr(bpc.request, "title", DataSubmissionHelper.getMainTitle(currentArDataSubmission.getAppType()));
        String actionType = (String) ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        String jumpActionType = ParamUtil.getRequestString(bpc.request, ACTION_TYPE);
        log.info(StringUtil.changeForLog("----- Action Type: " + actionType + " -----"));
        if (StringUtil.isEmpty(actionType)) {
            if ("jumpStage".equals(jumpActionType)){
                ParamUtil.setRequestAttr(bpc.request, "missSaveDraft", IaisEGPConstant.YES);
                actionType = ACTION_TYPE_RETURN;
            } else {
                actionType = ACTION_TYPE_PAGE;
            }
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, actionType);
        }
        prepareSwitch(bpc);
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", DataSubmissionHelper.getSmallTitle(DataSubmissionConsts.DS_AR,
                currentArDataSubmission.getAppType(), currentArDataSubmission.getSubmissionType()));
        ParamUtil.setRequestAttr(bpc.request, "stageList", arDataSubmissionService.genAvailableStageList(bpc.request, false));
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
        log.info(StringUtil.changeForLog("The doReturn start ..."));
        String actionType = ParamUtil.getRequestString(bpc.request, ACTION_TYPE);
        String haveJump = (String) ParamUtil.getRequestAttr(bpc.request, "haveJump");
        String missSaveDraft = (String) ParamUtil.getRequestAttr(bpc.request, "missSaveDraft");
        if ("jumpStage".equals(actionType)) {
            if (!IaisEGPConstant.YES.equals(missSaveDraft) && !IaisEGPConstant.YES.equals(haveJump)) {
                setDraftArSuperDataSubmissionDto(bpc.request);
                doDraft(bpc);
                ParamUtil.setRequestAttr(bpc.request, "saveDraftSuccess", null);
            }
            arDataSubmissionService.jumpJudgement(bpc.request);
        } else {
            returnStep(bpc);
            ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
            String uri = InboxConst.URL_MAIN_WEB_MODULE + "MohInternetInbox";
            if (arSuperDataSubmission != null) {
                if (!DataSubmissionConsts.DS_APP_TYPE_NEW.equals(arSuperDataSubmission.getAppType())) {
                    uri = InboxConst.URL_MAIN_WEB_MODULE + "MohInternetInbox";
                } else {
                    if (DataSubmissionConsts.AR_TYPE_SBT_CYCLE_STAGE.equals(arSuperDataSubmission.getSubmissionType())
                    && Arrays.asList(DataSubmissionConsts.AR_CYCLE_AR,
                            DataSubmissionConsts.AR_CYCLE_IUI,
                            DataSubmissionConsts.AR_CYCLE_EFO,
                            DataSubmissionConsts.AR_CYCLE_SFO)
                            .contains(arSuperDataSubmission.getDataSubmissionDto().getCycleStage())) {
                        uri = InboxConst.URL_LICENCE_WEB_MODULE + "MohARAndIUIDataSubmission/PreARIUIDataSubmission";
                    } else {
                        uri = InboxConst.URL_LICENCE_WEB_MODULE + "MohARCycleStagesManual";
                    }
                }
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
    }

    private void setDraftArSuperDataSubmissionDto(HttpServletRequest request) {
        String orgId = "";
        String userId = "";
        LoginContext loginContext = DataSubmissionHelper.getLoginContext(request);
        if (loginContext != null) {
            orgId = loginContext.getOrgId();
            userId = loginContext.getUserId();
        }
        ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(request);
        arSuperDataSubmission.setDraftNo(null);
        arSuperDataSubmission.setDraftId(null);
        CycleStageSelectionDto selectionDto = arSuperDataSubmission.getSelectionDto();
        if (selectionDto != null) {
            ArSuperDataSubmissionDto draftDto = arDataSubmissionService.getDraftArSuperDataSubmissionDtoByConds(orgId, arSuperDataSubmission.getHciCode(), selectionDto.getStage(), userId);
            if (draftDto != null) {
                arSuperDataSubmission.setDraftNo(draftDto.getDraftNo());
                arSuperDataSubmission.setDraftId(draftDto.getDraftId());
            }
        }
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmission, request);
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
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE,
                DataSubmissionConstant.PAGE_STAGE_PAGE);
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
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, DataSubmissionConstant.PAGE_STAGE_PREVIEW);
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.PRINT_FLAG, DataSubmissionConstant.PRINT_FLAG_ART);
        prepareConfim(bpc);
        processDisposalInvCommon(bpc.request);
    }

    /**
     * StartStep: PrepareConfim
     *
     * @param bpc
     * @throws
     */
    public void prepareConfim(BaseProcessClass bpc) {}

    /**
     * StartStep: Draft
     *
     * @param bpc
     * @throws
     */
    public void doDraft(BaseProcessClass bpc) {
        String currentStage = (String) ParamUtil.getRequestAttr(bpc.request, "currentStage");
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, currentStage);
        ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        if (arSuperDataSubmission != null) {
            arSuperDataSubmission.setOrgId(DataSubmissionHelper.getOrgId(bpc.request));
            arSuperDataSubmission.setDraftNo(
                    arDataSubmissionService.getDraftNo(DataSubmissionConsts.DS_AR, arSuperDataSubmission.getDraftNo()));
            arSuperDataSubmission = arDataSubmissionService.saveDataSubmissionDraft(arSuperDataSubmission);
            DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmission, bpc.request);
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

    /**
     * StartStep: Submission
     *
     * @param bpc
     * @throws
     */
    public void doSubmission(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("-----" + this.getClass().getSimpleName() + " Do Submission -----"));
        submission(bpc);
        ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        DataSubmissionDto dataSubmissionDto = arSuperDataSubmission.getDataSubmissionDto();
        CycleDto cycle = arSuperDataSubmission.getCycleDto();
        String cycleType = cycle.getCycleType();
        String navCurrentCycle = arSuperDataSubmission.getSelectionDto().getNavCurrentCycle();
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
        String stage = dataSubmissionDto.getCycleStage();
        // Spec: 3.3.3.1.9
        String status = cycle.getStatus();
        if (DataSubmissionConsts.DS_CYCLE_AR.equals(cycleType)) {
            if (DataSubmissionConsts.AR_STAGE_END_CYCLE.equals(stage)) {
                EndCycleStageDto endCycleStageDto = arSuperDataSubmission.getEndCycleStageDto();
                // 80441
                if (endCycleStageDto.getCycleAbandoned() != null && endCycleStageDto.getCycleAbandoned()) {
                    status = DataSubmissionConsts.DS_STATUS_COMPLETED_END_WITH_ABANDONED;
                } else {
                    status = DataSubmissionConsts.DS_STATUS_COMPLETED_END_CYCEL;
                }
            } else if (DataSubmissionConsts.AR_STAGE_OUTCOME_OF_EMBRYO_TRANSFERED.equals(stage)) {
                EmbryoTransferredOutcomeStageDto outcomeStageDto = arSuperDataSubmission.getEmbryoTransferredOutcomeStageDto();
                String transferedOutcome = outcomeStageDto.getTransferedOutcome();
                if (StringUtil.isIn(transferedOutcome, new String[]{
                        DataSubmissionConsts.OUTCOME_OF_EMBRYO_TRANSFERRED_NO_PREGNANCY_DETECTED,
                        DataSubmissionConsts.OUTCOME_OF_EMBRYO_TRANSFERRED_UNKNOWN})) {
                    status = DataSubmissionConsts.DS_STATUS_OET_NO_PREGNACY_UNKNOWN;
                } else if (StringUtil.isIn(transferedOutcome, new String[]{
                        DataSubmissionConsts.OUTCOME_OF_EMBRYO_TRANSFERRED_CLINICAL_PREGNANCY})) {//3.3.4.3
                    status = DataSubmissionConsts.DS_STATUS_PENDING_BIRTH_OUTCOMES;
                } else if (DataSubmissionConsts.AR_STAGE_OUTCOME_OF_PREGNANCY.equals(stage)) {
                    status = DataSubmissionConsts.DS_STATUS_COMPLETED_OUTCOME_OF_PREGNANCY;
                }
            } else if (DataSubmissionConsts.AR_STAGE_OUTCOME.equals(stage)) {//3.3.4.3
                OutcomeStageDto outcomeStageDto = arSuperDataSubmission.getOutcomeStageDto();
                if ("Y".equals(outcomeStageDto.getPregnancyDetected())) {
                    status = DataSubmissionConsts.DS_STATUS_PENDING_BIRTH_OUTCOMES;
                }
            }
        } else if (DataSubmissionConsts.DS_CYCLE_EFO.equals(cycleType)) {
            if (DataSubmissionConsts.AR_STAGE_FREEZING.equals(stage)) {
                status = DataSubmissionConsts.DS_STATUS_COMPLETE_FREEZING;
            }
        } else if (DataSubmissionConsts.DS_CYCLE_IUI.equals(cycleType)) {
            if (DataSubmissionConsts.AR_STAGE_OUTCOME.equals(stage)) {
                OutcomeStageDto outcomeStageDto = arSuperDataSubmission.getOutcomeStageDto();
                if (!"Y".equals(outcomeStageDto.getPregnancyDetected())) {
                    status = DataSubmissionConsts.DS_STATUS_OUTCOME_NO_DETECTED;
                } else {//3.3.4.3
                    status = DataSubmissionConsts.DS_STATUS_PENDING_BIRTH_OUTCOMES;
                }
            } else if (DataSubmissionConsts.AR_STAGE_OUTCOME_OF_PREGNANCY.equals(stage)) {
                status = DataSubmissionConsts.DS_STATUS_COMPLETED_OUTCOME_OF_PREGNANCY;
            }
        } else if (DataSubmissionConsts.DS_CYCLE_NON.equals(cycleType)) {
            status = DataSubmissionConsts.DS_STATUS_COMPLETED;
        }
        if (StringUtil.isEmpty(status)) {
            status = DataSubmissionConsts.DS_STATUS_ACTIVE;
        }
        cycle.setStatus(status);
        arSuperDataSubmission.setCycleDto(cycle);
        log.info(StringUtil.changeForLog("-----Cycle Type: " + cycleType + " - Stage : " + stage
                + " - Status: " + status + " -----"));

        LoginContext loginContext = DataSubmissionHelper.getLoginContext(bpc.request);
        if (loginContext != null) {
            dataSubmissionDto.setSubmitBy(loginContext.getUserId());
            dataSubmissionDto.setSubmitDt(new Date());
        }
        ArChangeInventoryDto arChangeInventoryDto = arSuperDataSubmission.getArChangeInventoryDto();
        if (arChangeInventoryDto != null&&arSuperDataSubmission.getArCurrentInventoryDto()!=null) {
            ArCurrentInventoryDto arCurrentInventoryDto = arSuperDataSubmission.getArCurrentInventoryDto();
            arCurrentInventoryDto = ArCurrentInventoryDto.addChange(arCurrentInventoryDto, arChangeInventoryDto);
            arSuperDataSubmission.setArCurrentInventoryDto(arCurrentInventoryDto);
        }
        arSuperDataSubmission = arDataSubmissionService.saveArSuperDataSubmissionDto(arSuperDataSubmission);
        try {
            arSuperDataSubmission = arDataSubmissionService.saveArSuperDataSubmissionDtoToBE(arSuperDataSubmission);
        } catch (Exception e) {
            log.error(StringUtil.changeForLog("The Eic saveArSuperDataSubmissionDtoToBE failed ===>" + e.getMessage()), e);
        }
        if (!StringUtil.isEmpty(arSuperDataSubmission.getDraftId())) {
            arDataSubmissionService.updateDataSubmissionDraftStatus(arSuperDataSubmission.getDraftId(),
                    DataSubmissionConsts.DS_STATUS_INACTIVE);
        }
        String emailAddress;
        if(!isRfc(bpc.request)){
            emailAddress = DataSubmissionHelper.getEmailAddrsByRoleIdsAndLicenseeId(bpc.request, MsgTemplateConstants.MSG_TEMPLATE_AR_INCOMPLETE_CYCLE_EMAIL);
        } else {
            emailAddress = DataSubmissionHelper.getEmailAddrsByRoleIdsAndLicenseeId(bpc.request, MsgTemplateConstants.MSG_TEMPLATE_AR_INCOMPLETE_CYCLE_EMAIL);
            sendRfcEmail(dataSubmissionDto, loginContext);
        }
        if(!isRfc(bpc.request) && StringUtil.isIn(dataSubmissionDto.getCycleStage(),DataSubmissionHelper.getAllARCycleStages())){
            try {
                sendNotification(dataSubmissionDto, bpc.request);
            } catch (IOException | TemplateException e) {
                log.error(StringUtil.changeForLog("ar submit successfully send inboxMsg is error "+loginContext.getLoginId() + "----"+ loginContext.getUserName() + "----"+ dataSubmissionDto.getSubmissionNo()));
            }
        }
        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmission);
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.EMAIL_ADDRESS,emailAddress);
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.SUBMITTED_BY,
                DataSubmissionHelper.getLoginContext(bpc.request).getUserName());
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, DataSubmissionConstant.PAGE_STAGE_ACK);
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.PRINT_FLAG, DataSubmissionConstant.PRINT_FLAG_ACKART);
        //update HeadStageNav
        CycleStageSelectionDto selectionDto = arDataSubmissionService.getCycleStageSelectionDtoByConds(arSuperDataSubmission.getPatientInfoDto().getPatient().getPatientCode(),
                arSuperDataSubmission.getHciCode(), arSuperDataSubmission.getCycleDto().getId());
        selectionDto.setCycle(cycleType);
        selectionDto.setNavCurrentCycle(navCurrentCycle);
        selectionDto.setLastStatus(cycle.getStatus());
        arSuperDataSubmission.setSelectionDto(selectionDto);
        ParamUtil.setRequestAttr(bpc.request, "stageList", arDataSubmissionService.genAvailableStageList(bpc.request, true));
    }

    /**
     * StartStep: Submission
     *
     * @param bpc
     * @throws
     */
    public void submission(BaseProcessClass bpc) {}

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
        getDisposalCommon(bpc.request);
        pageAction(bpc);
        valDisposalRFCCommon(bpc.request);
        if ("return".equals(actionType)) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, null);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, actionType);
        }
    }

    protected void valDisposalRFCCommon(HttpServletRequest request) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        DisposalStageDto disposalStageDto = arSuperDataSubmissionDto.getDisposalStageDto();
        if (StringUtil.isEmpty(ParamUtil.getRequestString(request, IaisEGPConstant.ERRORMSG))
                && AppConsts.YES.equals(ParamUtil.getRequestString(request, DataSubmissionConstant.RFC_NO_CHANGE_ERROR))
                && isRfc(request)) {
            ArSuperDataSubmissionDto arOldSuperDataSubmissionDto = DataSubmissionHelper.getOldArDataSubmission(request);
            DisposalStageDto oldDisposalStageDto = arOldSuperDataSubmissionDto.getDisposalStageDto();
            if ((disposalStageDto == null && oldDisposalStageDto != null) || (disposalStageDto != null && oldDisposalStageDto == null)) {
                ParamUtil.setRequestAttr(request, DataSubmissionConstant.RFC_NO_CHANGE_ERROR, AppConsts.NO);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_TYPE_CONFIRM);
            }
            if (disposalStageDto != null && !disposalStageDto.equals(oldDisposalStageDto)) {
                ParamUtil.setRequestAttr(request, DataSubmissionConstant.RFC_NO_CHANGE_ERROR, AppConsts.NO);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_TYPE_CONFIRM);
            }
        }
    }

    private void sendRfcEmail(DataSubmissionDto dataSubmissionDto, LoginContext loginContext){
        EmailParam emailParamEmail = new EmailParam();
        Map<String, Object> msgSubjectMap = IaisCommonUtils.genNewHashMap();
        String requestDate = Formatter.formatDate(new Date());
        msgSubjectMap.put("submissionId", dataSubmissionDto.getSubmissionNo());
        msgSubjectMap.put("requestDate", requestDate);
        msgSubjectMap.put("officer_name", "officer_name");
        emailParamEmail.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_AR_RFC_EMAIL);
        emailParamEmail.setTemplateContent(msgSubjectMap);
        emailParamEmail.setQueryCode(IaisEGPHelper.generateRandomString(26));
        emailParamEmail.setReqRefNum(IaisEGPHelper.generateRandomString(26));
        emailParamEmail.setServiceTypes(DataSubmissionConsts.DS_AR_NEW);
        emailParamEmail.setRefIdType(NotificationHelper.RECEIPT_TYPE_LICENSEE_ID);
        emailParamEmail.setRefId(loginContext.getLicenseeId());
        notificationHelper.sendNotification(emailParamEmail);
    }

    private void sendNotification(DataSubmissionDto dataSubmissionDto, HttpServletRequest request) throws TemplateException, IOException {
        MsgTemplateDto msgTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_AR_SUBMIT_INBOX_MSG).getEntity();
        Map<String, Object> msgContentMap = IaisCommonUtils.genNewHashMap();
        msgContentMap.put("officer_name", "officer_name");
        msgContentMap.put("requestDate", Formatter.formatDateTime(new Date(),"dd/MM/yyyy HH:mm:ss"));
        msgContentMap.put("submissionId", dataSubmissionDto.getSubmissionNo());
        Map<String, Object> msgSubjectMap = IaisCommonUtils.genNewHashMap();
        msgSubjectMap.put("serverName", "Assisted Reproduction");
        String subject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(), msgSubjectMap);
        EmailParam msgParam = new EmailParam();
        msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_AR_SUBMIT_INBOX_MSG);
        msgParam.setTemplateContent(msgContentMap);
        msgParam.setQueryCode(dataSubmissionDto.getSubmissionNo());
        msgParam.setReqRefNum(dataSubmissionDto.getSubmissionNo());
        msgParam.setServiceTypes(DataSubmissionConsts.DS_AR_NEW);
        msgParam.setRefId(DataSubmissionHelper.getLicenseeId(request));
        msgParam.setSubject(subject);
        msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
        notificationHelper.sendNotification(msgParam);
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
        String[] declaration = ParamUtil.getStrings(bpc.request, "declaration");
        ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        DataSubmissionDto dataSubmissionDto = arSuperDataSubmission.getDataSubmissionDto();
        if(declaration != null && declaration.length >0){
            dataSubmissionDto.setDeclaration(declaration[0]);
        }else{
            dataSubmissionDto.setDeclaration(null);
        }
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmission, bpc.request);
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
        // validation
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap(1);
        //for declaration
        String crud_action_type = ParamUtil.getString(bpc.request, DataSubmissionConstant.CRUD_TYPE);
        if("submission".equals(crud_action_type)){
            String[] declaration = ParamUtil.getStrings(bpc.request, "declaration");
            if(declaration == null || declaration.length == 0){
                errorMap.put("declaration", "GENERAL_ERR0006");
            }
        }

        String actionType = ParamUtil.getString(bpc.request, DataSubmissionConstant.CRUD_TYPE);
        if (isRfc(bpc.request) && ACTION_TYPE_SUBMISSION.equals(actionType)) {
            ArChangeInventoryDto arChangeInventoryDto = DataSubmissionHelper.getCurrentArChangeInventoryDto(bpc.request);
            ArCurrentInventoryDto arCurrentInventoryDto = ArCurrentInventoryDto.addChange(DataSubmissionHelper.getCurrentArCurrentInventoryDto(bpc.request), arChangeInventoryDto);
            if (arCurrentInventoryDto.getFrozenOocyteNum() < 0
                    || arCurrentInventoryDto.getFreshOocyteNum() < 0
                    || arCurrentInventoryDto.getThawedOocyteNum() < 0
                    || arCurrentInventoryDto.getFrozenEmbryoNum() < 0
                    || arCurrentInventoryDto.getFreshEmbryoNum() < 0
                    || arCurrentInventoryDto.getThawedEmbryoNum() < 0
                    || arCurrentInventoryDto.getFrozenSpermNum() < 0) {
                errorMap.put("inventoryLessZero", "Patient's inventory cannot be less than zero");
            }
        }
        //for ds center validation
        LoginContext login = AccessUtil.getLoginUser(bpc.request);
        List<DsCenterDto> centerDtos = licenceClient.getDsCenterDtosByOrgIdAndCentreType(login.getOrgId(), DataSubmissionConsts.DS_AR).getEntity();
        if (IaisCommonUtils.isEmpty(centerDtos)) {
            errorMap.put("topErrorMsg", "DS_ERR070");
        }
        if (!errorMap.isEmpty()) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_TYPE_CONFIRM);
        }
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
        verifyCommon(request, errorMap);
        if (!errorMap.isEmpty()) {
            log.info(StringUtil.changeForLog("----- Error Massage: " + errorMap + " -----"));
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMAP, errorMap);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, failedCrudActionType);
            return false;
        } else if (StringUtil.isNotEmpty(passCrudActionType)) {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, passCrudActionType);
        }
        return true;
    }

    protected void verifyCommon(HttpServletRequest request, Map<String, String> errorMap) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        DataSubmissionDto dataSubmissionDto = arSuperDataSubmissionDto.getDataSubmissionDto();
        if (isRfc(request)) {
            if (StringUtil.isEmpty(dataSubmissionDto.getAmendReason())) {
                errorMap.put("amendReason", "GENERAL_ERR0006");
            } else if (isOthers(dataSubmissionDto.getAmendReason()) && StringUtil.isEmpty(dataSubmissionDto.getAmendReasonOther())) {
                errorMap.put("amendReasonOther", "GENERAL_ERR0006");
            }
        }
        DisposalStageDto disposalStageDto = arSuperDataSubmissionDto.getDisposalStageDto();
        if (disposalStageDto != null) {
            ValidationResult validationResult = WebValidationHelper.validateProperty(disposalStageDto, "save");
            errorMap.putAll(validationResult.retrieveAll());
        }
    }

    private void getDisposalCommon(HttpServletRequest request) {
        String hasDisposal = ParamUtil.getString(request, HAS_DISPOSAL);
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        if (AppConsts.YES.equals(hasDisposal) || DataSubmissionConsts.AR_STAGE_DISPOSAL.equals(arSuperDataSubmissionDto.getDataSubmissionDto().getCycleStage())) {
            DisposalStageDto disposalStageDto = arSuperDataSubmissionDto.getDisposalStageDto();
            if (disposalStageDto == null) {
                disposalStageDto = new DisposalStageDto();
            }
            setDisposalByPage(disposalStageDto, request);
            arSuperDataSubmissionDto.setDisposalStageDto(disposalStageDto);
            DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto, request);
        } else {
            arSuperDataSubmissionDto.setDisposalStageDto(null);
        }
    }

    protected void getRfcCommon(HttpServletRequest request) {
        if (isRfc(request)) {
            ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
            DataSubmissionDto dataSubmissionDto = arSuperDataSubmissionDto.getDataSubmissionDto();
            dataSubmissionDto.setAmendReason(ParamUtil.getString(request, "amendReason"));
            if (isOthers(dataSubmissionDto.getAmendReason())) {
                dataSubmissionDto.setAmendReasonOther(ParamUtil.getString(request, "amendReasonOther"));
            } else {
                dataSubmissionDto.setAmendReasonOther(null);
            }
        }
    }

    protected boolean isOthers(String others){
        return StringUtil.isIn(others,new String[]{DataSubmissionConsts.CYCLE_STAGE_AMEND_REASON_OTHERS,DataSubmissionConsts.DONOR_SAMPLE_AMEND_REASON_OTHERS,DataSubmissionConsts.PATIENT_AMENDMENT_OTHER});
    }

    protected boolean isRfc(HttpServletRequest request){
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        return arSuperDataSubmissionDto != null && arSuperDataSubmissionDto.getDataSubmissionDto() != null && DataSubmissionConsts.DS_APP_TYPE_RFC.equalsIgnoreCase(arSuperDataSubmissionDto.getDataSubmissionDto().getAppType());
    }

    public final boolean validatePageData(HttpServletRequest request, Object obj, String property, String... actionType) {
        return needValidate(request, actionType) ? validatePageData(request, obj, property, ACTION_TYPE_CONFIRM, ACTION_TYPE_PAGE,
                null, null) : true;
    }

    public final boolean validatePageData(HttpServletRequest request, Object obj, String property, List validationDtos,
            String... actionType) {
        return needValidate(request, actionType) ? validatePageData(request, obj, property, ACTION_TYPE_CONFIRM, ACTION_TYPE_PAGE,
                validationDtos, null) : true;
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

    protected boolean needValidate(HttpServletRequest request, String... actionType) {
        return StringUtil.isIn(ParamUtil.getString(request, DataSubmissionConstant.CRUD_TYPE), actionType);
    }

    //TODO from ar center
    protected final List<SelectOption> getSourseList(HttpServletRequest request) {
        Map<String, String> stringStringMap = IaisCommonUtils.genNewHashMap();
        DataSubmissionHelper.setArPremisesMap(request).values().stream().forEach(v -> stringStringMap.put(v.getHciCode(), v.getPremiseLabel()));
        List<SelectOption> selectOptions = DataSubmissionHelper.genOptions(stringStringMap);
        selectOptions.add(new SelectOption(DataSubmissionConsts.AR_SOURCE_OTHER, DONOR_SOURSE_OTHERS));
        return selectOptions;
    }

    private void setDisposalByPage(DisposalStageDto disposalStageDto, HttpServletRequest request) {
        String disposedType = ParamUtil.getString(request, "disposedType");
        disposalStageDto.setDisposedType(disposedType);
        int totalNum = 0;
        boolean isInt = true;
        if (disposedType != null) {
            switch (disposedType) {
                case DataSubmissionConsts.DISPOSAL_TYPE_FRESH_OOCYTE:
                case DataSubmissionConsts.DISPOSAL_TYPE_FROZEN_OOCYTE:
                case DataSubmissionConsts.DISPOSAL_TYPE_THAWED_OOCYTE:
                    disposalStageDto.setDisposedTypeDisplay(1);
                    Integer immature = null;
                    try {
                        String immatureString = ParamUtil.getString(request, "immature");
                        disposalStageDto.setImmatureString(immatureString);
                        if (StringUtil.isEmpty(immatureString)) {
                            disposalStageDto.setImmature(null);
                        } else {
                            immature = ParamUtil.getInt(request, "immature");
                            totalNum += immature;
                            disposalStageDto.setImmature(immature);
                        }

                    } catch (Exception e) {
                        log.error("no int");
                        isInt = false;
                        disposalStageDto.setImmature(null);
                    }
                    Integer abnormallyFertilised = null;
                    try {
                        String abnormallyFertilisedString = ParamUtil.getString(request, "abnormallyFertilised");
                        disposalStageDto.setAbnormallyFertilisedString(abnormallyFertilisedString);
                        if (StringUtil.isEmpty(abnormallyFertilisedString)) {
                            disposalStageDto.setAbnormallyFertilised(null);
                        } else {
                            abnormallyFertilised = ParamUtil.getInt(request, "abnormallyFertilised");
                            totalNum += abnormallyFertilised;
                            disposalStageDto.setAbnormallyFertilised(abnormallyFertilised);
                        }

                    } catch (Exception e) {
                        log.error("no int");
                        isInt = false;
                        disposalStageDto.setAbnormallyFertilised(null);
                    }
                    Integer unfertilised = null;
                    try {
                        String unfertilisedString = ParamUtil.getString(request, "unfertilised");
                        disposalStageDto.setUnfertilisedString(unfertilisedString);
                        if (StringUtil.isEmpty(unfertilisedString)) {
                            disposalStageDto.setUnfertilised(null);
                        } else {
                            unfertilised = ParamUtil.getInt(request, "unfertilised");
                            totalNum += unfertilised;
                            disposalStageDto.setUnfertilised(unfertilised);
                        }

                    } catch (Exception e) {
                        isInt = false;
                        disposalStageDto.setUnfertilised(null);
                        log.error("no int");
                    }
                    Integer atretic = null;
                    try {
                        String atreticString = ParamUtil.getString(request, "atretic");
                        disposalStageDto.setAtreticString(atreticString);
                        if (StringUtil.isEmpty(atreticString)) {
                            disposalStageDto.setAtretic(null);
                        } else {
                            atretic = ParamUtil.getInt(request, "atretic");
                            totalNum += atretic;
                            disposalStageDto.setAtretic(atretic);
                        }

                    } catch (Exception e) {
                        isInt = false;
                        disposalStageDto.setAtretic(null);
                        log.error("no int");
                    }
                    Integer damaged = null;
                    try {
                        String damagedString = ParamUtil.getString(request, "damaged");
                        disposalStageDto.setDamagedString(damagedString);
                        if (StringUtil.isEmpty(damagedString)) {
                            disposalStageDto.setDamaged(null);
                        } else {
                            damaged = ParamUtil.getInt(request, "damaged");
                            totalNum += damaged;
                            disposalStageDto.setDamaged(damaged);
                        }

                    } catch (Exception e) {
                        isInt = false;
                        disposalStageDto.setDamaged(null);
                        log.error("no int");
                    }
                    Integer lysedOrDegenerated = null;
                    try {
                        String lysedOrDegeneratedString = ParamUtil.getString(request, "lysedOrDegenerated");
                        disposalStageDto.setLysedOrDegeneratedString(lysedOrDegeneratedString);
                        if (StringUtil.isEmpty(lysedOrDegeneratedString)) {
                            disposalStageDto.setLysedOrDegenerated(null);
                        } else {
                            lysedOrDegenerated = ParamUtil.getInt(request, "lysedOrDegenerated");
                            totalNum += lysedOrDegenerated;
                            disposalStageDto.setLysedOrDegenerated(lysedOrDegenerated);
                        }

                    } catch (Exception e) {
                        isInt = false;
                        disposalStageDto.setLysedOrDegenerated(null);
                        log.error("no int");
                    }
                    break;
                case DataSubmissionConsts.DISPOSAL_TYPE_FRESH_EMBRYO:
                case DataSubmissionConsts.DISPOSAL_TYPE_FROZEN_EMBRYO:
                case DataSubmissionConsts.DISPOSAL_TYPE_THAWED_EMBRYO:
                    disposalStageDto.setDisposedTypeDisplay(2);
                    Integer unhealthyNum = null;
                    try {
                        String unhealthyNumString = ParamUtil.getString(request, "unhealthyNum");
                        disposalStageDto.setUnhealthyNumString(unhealthyNumString);
                        if (StringUtil.isEmpty(unhealthyNumString)) {
                            disposalStageDto.setUnhealthyNum(null);
                        } else {
                            unhealthyNum = ParamUtil.getInt(request, "unhealthyNum");
                            totalNum += unhealthyNum;
                            disposalStageDto.setUnhealthyNum(unhealthyNum);
                        }

                    } catch (Exception e) {
                        disposalStageDto.setUnhealthyNum(null);
                        isInt = false;
                        log.error("no int");
                    }
                    break;
                case DataSubmissionConsts.DISPOSAL_TYPE_FROZEN_SPERM:
                    disposalStageDto.setDisposedTypeDisplay(3);
                    break;
                default:
            }
        }

        Integer otherDiscardedNum = null;
        try {
            String otherDiscardedNumString = ParamUtil.getString(request, "otherDiscardedNum");
            disposalStageDto.setOtherDiscardedNumString(otherDiscardedNumString);
            if (StringUtil.isEmpty(otherDiscardedNumString)) {
                disposalStageDto.setOtherDiscardedNum(null);
            } else {
                otherDiscardedNum = ParamUtil.getInt(request, "otherDiscardedNum");
                totalNum += otherDiscardedNum;
                disposalStageDto.setOtherDiscardedNum(otherDiscardedNum);
            }

        } catch (Exception e) {
            disposalStageDto.setOtherDiscardedNum(null);
            isInt = false;
            log.error("no int");
        }
        String otherDiscardedReason = ParamUtil.getString(request, "otherDiscardedReason");
        disposalStageDto.setOtherDiscardedReason(otherDiscardedReason);
        if (isInt) {
            disposalStageDto.setTotalNum(totalNum);
        } else {
            disposalStageDto.setTotalNum(null);
        }
    }

    private void processDisposalInvCommon(HttpServletRequest request) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        if (DataSubmissionConsts.AR_STAGE_DISPOSAL.equals(arSuperDataSubmissionDto.getSelectionDto().getStage()) ) {
            return;
        }
        ArChangeInventoryDto arChangeInventoryDto = arSuperDataSubmissionDto.getArChangeInventoryDto();
        if (arChangeInventoryDto == null) {
            arChangeInventoryDto = new ArChangeInventoryDto();
        }
        DisposalStageDto disposalStageDto = arSuperDataSubmissionDto.getDisposalStageDto();
        if (disposalStageDto != null) {
            switch (disposalStageDto.getDisposedType()) {
                case DataSubmissionConsts.DISPOSAL_TYPE_FRESH_OOCYTE:
                    arChangeInventoryDto.setFreshOocyteNum(arChangeInventoryDto.getFreshOocyteNum() - disposalStageDto.getTotalNum());
                    break;
                case DataSubmissionConsts.DISPOSAL_TYPE_FROZEN_OOCYTE:
                    arChangeInventoryDto.setFrozenOocyteNum(arChangeInventoryDto.getFrozenOocyteNum() - disposalStageDto.getTotalNum());
                    break;
                case DataSubmissionConsts.DISPOSAL_TYPE_THAWED_OOCYTE:
                    arChangeInventoryDto.setThawedOocyteNum(arChangeInventoryDto.getThawedOocyteNum() - disposalStageDto.getTotalNum());
                    break;
                case DataSubmissionConsts.DISPOSAL_TYPE_FRESH_EMBRYO:
                    arChangeInventoryDto.setFreshEmbryoNum(arChangeInventoryDto.getFreshEmbryoNum() - disposalStageDto.getTotalNum());
                    break;
                case DataSubmissionConsts.DISPOSAL_TYPE_FROZEN_EMBRYO:
                    arChangeInventoryDto.setFrozenEmbryoNum(arChangeInventoryDto.getFrozenEmbryoNum() - disposalStageDto.getTotalNum());
                    break;
                case DataSubmissionConsts.DISPOSAL_TYPE_THAWED_EMBRYO:
                    arChangeInventoryDto.setThawedEmbryoNum(arChangeInventoryDto.getThawedEmbryoNum() - disposalStageDto.getTotalNum());
                    break;
                case DataSubmissionConsts.DISPOSAL_TYPE_FROZEN_SPERM:
                    arChangeInventoryDto.setFrozenSpermNum(arChangeInventoryDto.getFrozenSpermNum() - disposalStageDto.getTotalNum());
                    break;
                default:
            }
        }
    }
}
