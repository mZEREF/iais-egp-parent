package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sg.gov.moh.iais.egp.bsb.client.BsbFileClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.client.WithdrawnClient;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.FileRepoSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.file.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.withdrawn.AppSubmitWithdrawnDto;
import sg.gov.moh.iais.egp.bsb.dto.withdrawn.WithdrawnAckDto;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sg.gov.moh.iais.egp.bsb.util.mastercode.MasterCodeHolder;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_WITHDRAWN_APPLICATION;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_WITHDRAWN_APPLICATION;
import static sg.gov.moh.iais.egp.bsb.constant.DocConstants.PARAM_PRIMARY_DOC_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_DOC_TYPES_JSON;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_OPTIONS_DOC_TYPES;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_MASKED_RFT_DATA_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_RFI_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_RFT_DATA_ID;

@Slf4j
@Delegator("rfiWithDrawnDelegator")
@RequiredArgsConstructor
public class RfiWithdrawnDelegator {
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
    public static final String PARAM_SAVED_FILES = "savedFiles";

    private final WithdrawnClient withdrawnClient;
    private final FileRepoClient fileRepoClient;
    private final BsbFileClient bsbFileClient;

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(WITHDRAWN_APP_DTO);
        request.getSession().removeAttribute(PARAM_FROM);
        request.getSession().removeAttribute(PARAM_BACK_URL);
        request.getSession().removeAttribute(PARAM_ADD_WITHDRAWN_APP_NOS);
        request.getSession().removeAttribute(PARAM_PRIMARY_DOC_DTO);
        request.getSession().removeAttribute(PARAM_NEW_FILES);
        request.getSession().removeAttribute(PARAM_SAVED_FILES);
        AuditTrailHelper.auditFunction(MODULE_WITHDRAWN_APPLICATION, FUNCTION_WITHDRAWN_APPLICATION);
    }

    @SneakyThrows(JsonProcessingException.class)
    public void prepareData(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("****The prepareDate Step****"));
        HttpServletRequest request = bpc.request;
        AppSubmitWithdrawnDto dto = getWithdrawnDto(request);
        String from = request.getParameter(PARAM_FROM);
        PrimaryDocDto primaryDocDto = getPrimaryDocDto(request);
        if (StringUtils.isEmpty(dto.getAppId())) {
            String maskedRfiAppId = ParamUtil.getString(request, KEY_RFI_APP_ID);
            String maskedRfiDataId = ParamUtil.getString(request, KEY_MASKED_RFT_DATA_ID);
            if (StringUtils.hasLength(maskedRfiAppId) && StringUtils.hasLength(maskedRfiDataId)) {
                String appId = MaskHelper.unmask(KEY_RFI_APP_ID, maskedRfiAppId);
                String rfiDataId = MaskHelper.unmask(KEY_MASKED_RFT_DATA_ID, maskedRfiDataId);
                ParamUtil.setSessionAttr(request, KEY_APP_ID, appId);
                ParamUtil.setSessionAttr(request, KEY_RFT_DATA_ID, rfiDataId);
                ResponseDto<AppSubmitWithdrawnDto> responseDto = withdrawnClient.getRfiWithdrawnDataByWithdrawnAppId(appId);
                if (responseDto.ok()) {
                    dto = responseDto.getEntity();
                    ParamUtil.setSessionAttr(request, WITHDRAWN_APP_DTO, dto);
                    ParamUtil.setSessionAttr(request, PARAM_ADD_WITHDRAWN_APP_NOS, (Serializable) dto.getWithdrawnAppNos());
                    // set saved doc record info
                    Map<String, DocRecordInfo> savedDocMap = CollectionUtils.uniqueIndexMap(DocRecordInfo::getRepoId, dto.getSavedInfos());
                    primaryDocDto.setSavedDocMap(savedDocMap);
                    ParamUtil.setSessionAttr(request, PARAM_PRIMARY_DOC_DTO, primaryDocDto);
                    dto.setSavedInfos(null);
                } else {
                    log.warn("get withdrawn API doesn't return ok, the response is {}", responseDto);
                    ParamUtil.setRequestAttr(request, WITHDRAWN_APP_DTO, new AppSubmitWithdrawnDto());
                    ParamUtil.setSessionAttr(request, PARAM_ADD_WITHDRAWN_APP_NOS, new ArrayList<>());
                }
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
        } else {
            ParamUtil.setSessionAttr(request, PARAM_BACK_URL, "/bsb-web/eservice/INTERNET/MohBSBInboxMsg");
        }
        ParamUtil.setSessionAttr(request, PARAM_SAVED_FILES, (Serializable) primaryDocDto.getSavedDocMap());

        List<SelectOption> docTypeOps = MasterCodeHolder.DOCUMENT_TYPE.allOptions();
        ParamUtil.setRequestAttr(request, KEY_OPTIONS_DOC_TYPES, docTypeOps);
        ObjectMapper mapper = new ObjectMapper();
        String docTypeOpsJson = mapper.writeValueAsString(docTypeOps);
        ParamUtil.setRequestAttr(request, KEY_DOC_TYPES_JSON, docTypeOpsJson);
    }

    public AppSubmitWithdrawnDto getUserEnteredData(HttpServletRequest request) {
        AppSubmitWithdrawnDto withdrawnDto = getWithdrawnDto(request);
        String reason = ParamUtil.getRequestString(request, PARAM_REASON);
        String remarks = ParamUtil.getRequestString(request, PARAM_REMARKS);
        withdrawnDto.setReason(reason);
        withdrawnDto.setRemarks(remarks);
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
        if (!validationResultDto.isPass()) {
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            actionType = ACTION_TYPE_PREPARE;
        } else {
            actionType = ACTION_TYPE_SUBMIT;
        }
        ParamUtil.setRequestAttr(request, ACTION_TYPE, actionType);
        ParamUtil.setSessionAttr(request, WITHDRAWN_APP_DTO, dto);
        ParamUtil.setSessionAttr(request, PARAM_NEW_FILES, (Serializable) primaryDocDto.getNewDocMap());
        ParamUtil.setSessionAttr(request, PARAM_PRIMARY_DOC_DTO, primaryDocDto);
    }

    public void submitWithdrawn(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AppSubmitWithdrawnDto dto = getWithdrawnDto(request);
        PrimaryDocDto primaryDocDto = getPrimaryDocDto(request);
        FileRepoSyncDto syncDto = new FileRepoSyncDto();
        syncDto.setToDeleteIds(new ArrayList<>(primaryDocDto.getToBeDeletedRepoIds()));
        //
        deleteUnwantedDoc(primaryDocDto);
        //complete simple save file to db and save data to dto for show in jsp
        MultipartFile[] files = primaryDocDto.getNewDocMap().values().stream().map(NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
        List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
        //newFile change to saved File and save info to db
        List<NewFileSyncDto> newFilesToSync = new ArrayList<>(primaryDocDto.newFileSaved(repoIds));
        dto.setSavedInfos(primaryDocDto.getExistDocTypeList());

        String rfiDataId = (String) ParamUtil.getSessionAttr(request, KEY_RFT_DATA_ID);
        withdrawnClient.saveWithdrawalRfi(dto,rfiDataId);

        try {
            // sync files to BE file-repo (save new added files, delete useless files)
            if (!newFilesToSync.isEmpty()) {
                /* Ignore the failure of sync files currently.
                 * We should add a mechanism to retry synchronization of files in the future */
                syncDto.setNewFiles(newFilesToSync);
            }
            bsbFileClient.saveFiles(syncDto);
        } catch (Exception e) {
            log.error("Fail to sync files to BE", e);
        }
        WithdrawnAckDto withdrawnAckDto = new WithdrawnAckDto();
        withdrawnAckDto.setApplicationNos(dto.getWithdrawnAppNos());
        withdrawnAckDto.setWithdrawnDate(new Date());
        ParamUtil.setRequestAttr(request, "withdrawnDtoListAck", withdrawnAckDto);
        ParamUtil.setRequestAttr(request,"resultMsg","ACKWDL001");
    }

    public List<String> deleteUnwantedDoc(PrimaryDocDto primaryDocDto) {
        /* Ignore the failure when try to delete FE files because this is not a big issue.
         * The not deleted file won't be retrieved, so it's just a waste of disk space */
        List<String> toBeDeletedRepoIds = new ArrayList<>(primaryDocDto.getToBeDeletedRepoIds());
        for (String id: toBeDeletedRepoIds) {
            FileRepoDto fileRepoDto = new FileRepoDto();
            fileRepoDto.setId(id);
            fileRepoClient.removeFileById(fileRepoDto);
            primaryDocDto.getToBeDeletedRepoIds().remove(id);
        }
        return toBeDeletedRepoIds;
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
