package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.WithdrawApplicationDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.memorypage.PaginationHandler;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sg.gov.moh.iais.egp.bsb.client.BsbFileClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.client.WithdrawnClient;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.file.FileRepoSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.withdrawn.AppSubmitWithdrawnDto;
import sg.gov.moh.iais.egp.bsb.dto.file.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.dto.withdrawn.WithdrawnAckDto;
import sg.gov.moh.iais.egp.bsb.util.mastercode.MasterCodeHolder;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_WITHDRAWN_APPLICATION;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_WITHDRAWN_APPLICATION;
import static sg.gov.moh.iais.egp.bsb.constant.DocConstants.PARAM_PRIMARY_DOC_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_DOC_TYPES_JSON;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_OPTIONS_DOC_TYPES;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_ACKNOWLEDGEMENT;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_AO;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_AO_APPROVAL_LETTER_REVIEW;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_AO_RECTIFICATION_REVIEW;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_AO_REVIEW;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_APPOINTMENT_SCHEDULE;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_CLARIFICATION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_DO;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_DO_APPROVAL_LETTER_DRAFT;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_DO_CONFIRM_INSPECTION_DATE;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_DO_RECTIFICATION_REVIEW;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_HM;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_INPUT;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_INSPECTION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_INSPECTION_READINESS;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_INSPECTION_REPORT;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_NC_RECTIFICATION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_REPORT_FINALISATION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_REPORT_REVISION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_SUBMIT_SELF_ASSESSMENT;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_TYPE_CANCEL;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_TYPE_DEREGISTRATION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_TYPE_NEW;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_TYPE_RENEW;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_TYPE_RFC;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_TYPE_SUBMISSION;

@Slf4j
@Delegator("bsbWithDrawnAppDelegator")
public class WithdrawnAppDelegator {
    private static final String ACTION_TYPE_SUBMIT = "doSubmit";
    private static final String ACTION_TYPE_PREPARE = "prepare";
    private static final String ACTION_TYPE = "action_type";
    public static final String WITHDRAWN_APP_DTO = "withdrawnDto";
    private static final String PARAM_REASON = "reason";
    private static final String PARAM_REMARKS = "remarks";
    private static final String PARAM_BACK_URL = "backUrl";
    private static final String PARAM_FROM = "from";
    private static final String PARAM_ADD_WITHDRAWN_APP_NOS = "addWithdrawnAppNos";
    public static final String PARAM_NEW_FILES = "newFiles";

    private final WithdrawnClient withdrawnClient;
    private final FileRepoClient fileRepoClient;
    private final BsbFileClient bsbFileClient;

    @Autowired
    public WithdrawnAppDelegator(WithdrawnClient withdrawnClient, FileRepoClient fileRepoClient, BsbFileClient bsbFileClient) {
        this.withdrawnClient = withdrawnClient;
        this.fileRepoClient = fileRepoClient;
        this.bsbFileClient = bsbFileClient;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(WITHDRAWN_APP_DTO);
        request.getSession().removeAttribute(PARAM_FROM);
        request.getSession().removeAttribute(PARAM_BACK_URL);
        request.getSession().removeAttribute(PARAM_ADD_WITHDRAWN_APP_NOS);
        request.getSession().removeAttribute(PARAM_PRIMARY_DOC_DTO);
        request.getSession().removeAttribute(PARAM_NEW_FILES);
        AuditTrailHelper.auditFunction(MODULE_WITHDRAWN_APPLICATION, FUNCTION_WITHDRAWN_APPLICATION);
    }

    @SneakyThrows(JsonProcessingException.class)
    public void prepareData(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("****The prepareDate Step****"));
        HttpServletRequest request = bpc.request;
        AppSubmitWithdrawnDto dto = getWithdrawnDto(request);
        String from = request.getParameter(PARAM_FROM);
        if (StringUtils.isEmpty(dto.getAppId())) {
            String maskedApplicationId = request.getParameter("withdrawnAppId");
            String applicationId = MaskUtil.unMaskValue("id", maskedApplicationId);
            if (maskedApplicationId == null || applicationId == null || maskedApplicationId.equals(applicationId)) {
                throw new IaisRuntimeException("Invalid Application ID");
            }
            ResponseDto<AppSubmitWithdrawnDto> responseDto = withdrawnClient.getWithdrawnDataByApplicationId(applicationId);
            if (responseDto.ok()) {
                dto = responseDto.getEntity();
            } else {
                log.warn("get withdrawn API doesn't return ok, the response is {}", responseDto);
                ParamUtil.setRequestAttr(request, WITHDRAWN_APP_DTO, new AppSubmitWithdrawnDto());
            }
            //
            ParamUtil.setSessionAttr(request, PARAM_FROM, from);
        }
        if (StringUtils.hasLength(from)) {
            if (from.equals("application")) {
                ParamUtil.setSessionAttr(request, PARAM_BACK_URL, "/bsb-web/eservice/INTERNET/MohBSBInboxApp");
            } else if (from.equals("dataSubmission")) {
                ParamUtil.setSessionAttr(request, PARAM_BACK_URL, "/bsb-web/eservice/INTERNET/DataSubInbox");
            }
        }
        ParamUtil.setSessionAttr(request, WITHDRAWN_APP_DTO, dto);

        List<SelectOption> docTypeOps = MasterCodeHolder.DOCUMENT_TYPE.allOptions();
        ParamUtil.setRequestAttr(request, KEY_OPTIONS_DOC_TYPES, docTypeOps);
        ObjectMapper mapper = new ObjectMapper();
        String docTypeOpsJson = mapper.writeValueAsString(docTypeOps);
        ParamUtil.setRequestAttr(request, KEY_DOC_TYPES_JSON, docTypeOpsJson);

        //prepare user can withdraw application list
        List<WithdrawApplicationDto> canWithdrawAppList = getCanWithdrawAppList();
        // remove current appNo
        if(!CollectionUtils.isEmpty(canWithdrawAppList)){
            String finalApplicationNo = dto.getInitialAppNo();
            if (StringUtils.hasLength(finalApplicationNo)){
                canWithdrawAppList.removeIf(h -> finalApplicationNo.equals(h.getApplicationNo()));
            }
        }
        //
        PaginationHandler<WithdrawApplicationDto> handler = new PaginationHandler<>("withdrawPagDiv", "withdrawBodyDiv");
        handler.setAllData(canWithdrawAppList);
        handler.preLoadingPage();
    }

    public List<WithdrawApplicationDto> getCanWithdrawAppList() {
        List<WithdrawApplicationDto> withdrawApplicationDtoList = withdrawnClient.getApplicationByAppTypesAndStatus(getWithdrawnAppTypeAndStatus()).getEntity();
        for (WithdrawApplicationDto withdrawApplicationDto : withdrawApplicationDtoList) {
            String appNoMaskId = MaskUtil.maskValue("appNo", withdrawApplicationDto.getApplicationNo());
            withdrawApplicationDto.setMaskId(appNoMaskId);
        }
        return withdrawApplicationDtoList;
    }

    public Map<String, List<String>> getWithdrawnAppTypeAndStatus() {
        Map<String, List<String>> appTypeAndStatusMap = new HashMap<>(6);
        //New
        List<String> newStatus = new ArrayList<>(19);
        newStatus.add(APP_STATUS_PEND_DO);
        newStatus.add(APP_STATUS_PEND_AO);
        newStatus.add(APP_STATUS_PEND_HM);
        newStatus.add(APP_STATUS_PEND_INPUT);
        newStatus.add(APP_STATUS_PEND_APPOINTMENT_SCHEDULE);
        newStatus.add(APP_STATUS_PEND_SUBMIT_SELF_ASSESSMENT);
        newStatus.add(APP_STATUS_PEND_INSPECTION_READINESS);
        newStatus.add(APP_STATUS_PEND_CLARIFICATION);
        newStatus.add(APP_STATUS_PEND_INSPECTION);
        newStatus.add(APP_STATUS_PEND_INSPECTION_REPORT);
        newStatus.add(APP_STATUS_PEND_AO_REVIEW);
        newStatus.add(APP_STATUS_PEND_REPORT_REVISION);
        newStatus.add(APP_STATUS_PEND_REPORT_FINALISATION);
        newStatus.add(APP_STATUS_PEND_NC_RECTIFICATION);
        newStatus.add(APP_STATUS_PEND_DO_RECTIFICATION_REVIEW);
        newStatus.add(APP_STATUS_PEND_AO_RECTIFICATION_REVIEW);
        newStatus.add(APP_STATUS_PEND_DO_APPROVAL_LETTER_DRAFT);
        newStatus.add(APP_STATUS_PEND_AO_APPROVAL_LETTER_REVIEW);
        newStatus.add(APP_STATUS_PEND_DO_CONFIRM_INSPECTION_DATE);
        appTypeAndStatusMap.put(APP_TYPE_NEW, newStatus);

        //Renew
        List<String> renewStatus = new ArrayList<>(19);
        renewStatus.add(APP_STATUS_PEND_DO);
        renewStatus.add(APP_STATUS_PEND_AO);
        renewStatus.add(APP_STATUS_PEND_HM);
        renewStatus.add(APP_STATUS_PEND_INPUT);
        renewStatus.add(APP_STATUS_PEND_APPOINTMENT_SCHEDULE);
        renewStatus.add(APP_STATUS_PEND_SUBMIT_SELF_ASSESSMENT);
        renewStatus.add(APP_STATUS_PEND_INSPECTION_READINESS);
        renewStatus.add(APP_STATUS_PEND_CLARIFICATION);
        renewStatus.add(APP_STATUS_PEND_INSPECTION);
        renewStatus.add(APP_STATUS_PEND_INSPECTION_REPORT);
        renewStatus.add(APP_STATUS_PEND_AO_REVIEW);
        renewStatus.add(APP_STATUS_PEND_REPORT_REVISION);
        renewStatus.add(APP_STATUS_PEND_REPORT_FINALISATION);
        renewStatus.add(APP_STATUS_PEND_NC_RECTIFICATION);
        renewStatus.add(APP_STATUS_PEND_DO_RECTIFICATION_REVIEW);
        renewStatus.add(APP_STATUS_PEND_AO_RECTIFICATION_REVIEW);
        renewStatus.add(APP_STATUS_PEND_DO_APPROVAL_LETTER_DRAFT);
        renewStatus.add(APP_STATUS_PEND_AO_APPROVAL_LETTER_REVIEW);
        renewStatus.add(APP_STATUS_PEND_DO_CONFIRM_INSPECTION_DATE);
        appTypeAndStatusMap.put(APP_TYPE_RENEW, renewStatus);

        //RFC
        List<String> rfcStatus = new ArrayList<>(19);
        rfcStatus.add(APP_STATUS_PEND_DO);
        rfcStatus.add(APP_STATUS_PEND_AO);
        rfcStatus.add(APP_STATUS_PEND_HM);
        rfcStatus.add(APP_STATUS_PEND_INPUT);
        rfcStatus.add(APP_STATUS_PEND_APPOINTMENT_SCHEDULE);
        rfcStatus.add(APP_STATUS_PEND_SUBMIT_SELF_ASSESSMENT);
        rfcStatus.add(APP_STATUS_PEND_INSPECTION_READINESS);
        rfcStatus.add(APP_STATUS_PEND_CLARIFICATION);
        rfcStatus.add(APP_STATUS_PEND_INSPECTION);
        rfcStatus.add(APP_STATUS_PEND_INSPECTION_REPORT);
        rfcStatus.add(APP_STATUS_PEND_AO_REVIEW);
        rfcStatus.add(APP_STATUS_PEND_REPORT_REVISION);
        rfcStatus.add(APP_STATUS_PEND_REPORT_FINALISATION);
        rfcStatus.add(APP_STATUS_PEND_NC_RECTIFICATION);
        rfcStatus.add(APP_STATUS_PEND_DO_RECTIFICATION_REVIEW);
        rfcStatus.add(APP_STATUS_PEND_AO_RECTIFICATION_REVIEW);
        rfcStatus.add(APP_STATUS_PEND_DO_APPROVAL_LETTER_DRAFT);
        rfcStatus.add(APP_STATUS_PEND_AO_APPROVAL_LETTER_REVIEW);
        rfcStatus.add(APP_STATUS_PEND_DO_CONFIRM_INSPECTION_DATE);
        appTypeAndStatusMap.put(APP_TYPE_RFC, rfcStatus);

        //Cancellation
        List<String> cancelStatus = new ArrayList<>(3);
        cancelStatus.add(APP_STATUS_PEND_DO);
        cancelStatus.add(APP_STATUS_PEND_AO);
        cancelStatus.add(APP_STATUS_PEND_HM);
        appTypeAndStatusMap.put(APP_TYPE_CANCEL, cancelStatus);

        //DeRegistration
        List<String> deRegistrationStatus = new ArrayList<>(3);
        deRegistrationStatus.add(APP_STATUS_PEND_DO);
        deRegistrationStatus.add(APP_STATUS_PEND_AO);
        deRegistrationStatus.add(APP_STATUS_PEND_HM);
        appTypeAndStatusMap.put(APP_TYPE_DEREGISTRATION, deRegistrationStatus);

        //Data Submission
        List<String> dataSubmissionStatus = new ArrayList<>(1);
        dataSubmissionStatus.add(APP_STATUS_PEND_ACKNOWLEDGEMENT);
        appTypeAndStatusMap.put(APP_TYPE_SUBMISSION, dataSubmissionStatus);

        return appTypeAndStatusMap;
    }

    public AppSubmitWithdrawnDto getUserEnteredData(HttpServletRequest request){
        AppSubmitWithdrawnDto withdrawnDto = getWithdrawnDto(request);
        String reason = ParamUtil.getRequestString(request, PARAM_REASON);
        String remarks = ParamUtil.getRequestString(request, PARAM_REMARKS);
        String paramAppNos = ParamUtil.getString(request, "withdraw_app_list");
        withdrawnDto.setReason(reason);
        withdrawnDto.setRemarks(remarks);
        if (StringUtils.hasLength(paramAppNos)){
            String[] withdrawAppNos = paramAppNos.split("#");
            List<String> appNoList = Arrays.asList(withdrawAppNos);
            withdrawnDto.setWithdrawnAppNos(appNoList);
            List<String> addWithdrawnAppNos = new ArrayList<>(appNoList.size());
            addWithdrawnAppNos.addAll(appNoList);
            if (StringUtils.hasLength(withdrawnDto.getInitialAppNo())){
                addWithdrawnAppNos.removeIf(appNo -> appNo.equals(withdrawnDto.getInitialAppNo()));
            }
            ParamUtil.setSessionAttr(request, PARAM_ADD_WITHDRAWN_APP_NOS,(Serializable) addWithdrawnAppNos);
        }
        return withdrawnDto;
    }

    public void preConfirm(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        AppSubmitWithdrawnDto dto = getUserEnteredData(mulReq);
        //doc
        PrimaryDocDto primaryDocDto = getPrimaryDocDto(request);
        primaryDocDto.reqObjMapping(mulReq);
        //set need Validation value
        dto.setDocMetas(primaryDocDto.convertToDocMetaList());

        //validation
        String actionType = "";
        ValidationResultDto validationResultDto = withdrawnClient.validateWithdrawnDto(dto);
        if (!validationResultDto.isPass()){
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            actionType = ACTION_TYPE_PREPARE;
        }else {
            actionType = ACTION_TYPE_SUBMIT;
        }
        ParamUtil.setRequestAttr(request, ACTION_TYPE, actionType);
        ParamUtil.setSessionAttr(request, WITHDRAWN_APP_DTO, dto);
        ParamUtil.setSessionAttr(request, PARAM_NEW_FILES, (Serializable) primaryDocDto.getNewDocMap());
        ParamUtil.setSessionAttr(request, PARAM_PRIMARY_DOC_DTO, primaryDocDto);
    }

    public void submitWithdrawn(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        AppSubmitWithdrawnDto dto = getWithdrawnDto(request);
        PrimaryDocDto primaryDocDto = getPrimaryDocDto(request);

        //complete simple save file to db and save data to dto for show in jsp
        MultipartFile[] files = primaryDocDto.getNewDocMap().values().stream().map(NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
        List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
        //newFile change to saved File and save info to db
        List<NewFileSyncDto> newFilesToSync = new ArrayList<>(primaryDocDto.newFileSaved(repoIds));
        dto.setSavedInfos(primaryDocDto.getExistDocTypeList());

        withdrawnClient.saveWithdrawnApp(dto);

        try {
            // sync files to BE file-repo (save new added files, delete useless files)
            if (!newFilesToSync.isEmpty()) {
                /* Ignore the failure of sync files currently.
                 * We should add a mechanism to retry synchronization of files in the future */
                FileRepoSyncDto syncDto = new FileRepoSyncDto();
                syncDto.setNewFiles(newFilesToSync);
                bsbFileClient.saveFiles(syncDto);
            }
        } catch (Exception e) {
            log.error("Fail to sync files to BE", e);
        }
        WithdrawnAckDto withdrawnAckDto = new WithdrawnAckDto();
        withdrawnAckDto.setApplicationNos(dto.getWithdrawnAppNos());
        withdrawnAckDto.setWithdrawnDate(new Date());
        ParamUtil.setRequestAttr(request,"withdrawnDtoListAck",withdrawnAckDto);
    }

    private AppSubmitWithdrawnDto getWithdrawnDto(HttpServletRequest request) {
        AppSubmitWithdrawnDto dto = (AppSubmitWithdrawnDto) ParamUtil.getSessionAttr(request, WITHDRAWN_APP_DTO);
        return dto == null ? getDefaultWithdrawnDto() : dto;
    }

    private AppSubmitWithdrawnDto getDefaultWithdrawnDto() {
        return new AppSubmitWithdrawnDto();
    }

    private PrimaryDocDto getPrimaryDocDto(HttpServletRequest request) {
        PrimaryDocDto docDto = (PrimaryDocDto) ParamUtil.getSessionAttr(request, PARAM_PRIMARY_DOC_DTO);
        return docDto == null ? new PrimaryDocDto() : docDto;
    }
}
