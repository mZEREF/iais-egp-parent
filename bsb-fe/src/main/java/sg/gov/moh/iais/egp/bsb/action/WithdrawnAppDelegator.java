package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.WithdrawApplicationDto;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.memorypage.PaginationHandler;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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
import sg.gov.moh.iais.egp.bsb.service.WithdrawnService;
import sg.gov.moh.iais.egp.bsb.util.mastercode.MasterCodeHolder;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_WITHDRAWN_APPLICATION;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_WITHDRAWN_APPLICATION;
import static sg.gov.moh.iais.egp.bsb.constant.DocConstants.PARAM_PRIMARY_DOC_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_DOC_TYPES_JSON;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_OPTIONS_DOC_TYPES;

@Slf4j
@Delegator("bsbWithDrawnAppDelegator")
@RequiredArgsConstructor
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
    private final WithdrawnService withdrawnService;

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
            if (StringUtils.hasLength(applicationId)) {
                ResponseDto<AppSubmitWithdrawnDto> responseDto = withdrawnClient.getWithdrawnDataByApplicationId(applicationId);
                if (responseDto.ok()) {
                    dto = responseDto.getEntity();
                    ParamUtil.setSessionAttr(request, WITHDRAWN_APP_DTO, dto);
                } else {
                    log.warn("get withdrawn API doesn't return ok, the response is {}", responseDto);
                    ParamUtil.setRequestAttr(request, WITHDRAWN_APP_DTO, new AppSubmitWithdrawnDto());
                }
            }
            //
            ParamUtil.setSessionAttr(request, PARAM_FROM, from);
        }
        withdrawnService.setBackUrl(request, from);

        List<SelectOption> docTypeOps = MasterCodeHolder.DOCUMENT_TYPE.allOptions();
        ParamUtil.setRequestAttr(request, KEY_OPTIONS_DOC_TYPES, docTypeOps);
        ObjectMapper mapper = new ObjectMapper();
        String docTypeOpsJson = mapper.writeValueAsString(docTypeOps);
        ParamUtil.setRequestAttr(request, KEY_DOC_TYPES_JSON, docTypeOpsJson);

        //prepare user can withdraw application list
        List<WithdrawApplicationDto> canWithdrawAppList = withdrawnService.getCanWithdrawAppList();
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

    public AppSubmitWithdrawnDto getFormData(HttpServletRequest request){
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
        AppSubmitWithdrawnDto dto = getFormData(mulReq);
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
        ParamUtil.setRequestAttr(request,"resultMsg","ACKWDL001");
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
