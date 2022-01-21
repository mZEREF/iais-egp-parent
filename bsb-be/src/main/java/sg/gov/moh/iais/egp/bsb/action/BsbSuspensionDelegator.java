package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.client.SuspensionClient;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.suspension.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.dto.suspension.SuspensionReinstatementDto;
import sg.gov.moh.iais.egp.bsb.service.ProcessHistoryService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.KEY_TASK_ID;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APPROVAL_STATUS_SUSPENDED;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.PROCESS_TYPE_FAC_CERTIFIER_REG;
import static sg.gov.moh.iais.egp.bsb.constant.RevocationConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ROUTING_HISTORY_LIST;

/**
 * @author : tangtang
 */
@Slf4j
@Delegator("bsbSuspensionDelegator")
public class BsbSuspensionDelegator {
    private static final String SUSPENSION_MODULE_NAME = "Suspension";
    private static final String REINSTATEMENT_MODULE_NAME = "Reinstatement";
    private static final String ACTION_TYPE_SAVE = "doSave";
    private static final String ACTION_TYPE_PREPARE = "prepare";
    private static final String ACTION_TYPE_NEXT = "doNext";
    private static final String ACTION_TYPE = "action_type";
    private static final String SUSPENSION_REINSTATEMENT_DTO = "suspensionReinstatementDto";
    private static final String ACK_MSG = "ackMsg";
    private static final String DO_SUSPEND_ACK_MSG = "You have successfully submitted a Suspension Task.";
    private static final String AO_HM_SUSPEND_ACK_MSG = "You have successfully approved the application.";
    private static final String SUSPENSION_DOC_TYPE = "Suspension";
    private static final String REINSTATEMENT_DOC_TYPE = "Reinstatement";
    private static final String KEY_CAN_UPLOAD = "canUpload";

    private final SuspensionClient suspensionClient;
    private final FileRepoClient fileRepoClient;
    private final ProcessHistoryService processHistoryService;

    public BsbSuspensionDelegator(SuspensionClient suspensionClient, FileRepoClient fileRepoClient, ProcessHistoryService processHistoryService) {
        this.suspensionClient = suspensionClient;
        this.fileRepoClient = fileRepoClient;
        this.processHistoryService = processHistoryService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction(SUSPENSION_MODULE_NAME, null);
        AuditTrailHelper.auditFunction(REINSTATEMENT_MODULE_NAME, null);
        ParamUtil.setSessionAttr(request, SUSPENSION_REINSTATEMENT_DTO, null);
        ParamUtil.setSessionAttr(request, BACK, null);
        request.getSession().removeAttribute(KEY_ROUTING_HISTORY_LIST);
        request.getSession().removeAttribute(KEY_CAN_UPLOAD);
    }

    /**
     * Duty officer submit suspension request
     */
    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, KEY_CAN_UPLOAD, "Y");
        String from = ParamUtil.getRequestString(request, FROM);
        ParamUtil.setSessionAttr(request, FROM, null);
        SuspensionReinstatementDto dto = getSuspensionDto(request);
        if (StringUtils.isEmpty(dto.getApprovalId()) && StringUtils.isEmpty(dto.getApplicationId())) {
            if (from.equals(FAC)) {
                String approvalId = ParamUtil.getRequestString(request, PARAM_APPROVAL_ID);
                approvalId = MaskUtil.unMaskValue("id", approvalId);
                dto = suspensionClient.getSuspensionDataByApprovalId(approvalId).getEntity();
            }
            if (from.equals(APP)) {
                String maskedAppId = ParamUtil.getString(request, KEY_APP_ID);
                String maskedTaskId = ParamUtil.getString(request, KEY_TASK_ID);
                String appId = MaskUtil.unMaskValue("id", maskedAppId);
                String taskId = MaskUtil.unMaskValue("id", maskedTaskId);
                if (appId == null || appId.equals(maskedAppId)) {
                    throw new IaisRuntimeException("Invalid masked application ID");
                }
                if (taskId == null || taskId.equals(maskedTaskId)) {
                    throw new IaisRuntimeException("Invalid masked task ID");
                }
                dto = suspensionClient.getSuspensionDataByApplicationId(appId).getEntity();
                dto.setTaskId(taskId);
                PrimaryDocDto primaryDocDto = new PrimaryDocDto();
                primaryDocDto.setSavedDocMap(dto.getQueryDocMap());
                dto.setPrimaryDocDto(primaryDocDto);
                //show routingHistory list
                processHistoryService.getAndSetHistoryInSession(dto.getApplicationNo(), request);
            }
        }
        setModuleType(dto);
        ParamUtil.setSessionAttr(request, BACK, from);
        ParamUtil.setSessionAttr(request, SUSPENSION_REINSTATEMENT_DTO, dto);
    }

    public void aoPrepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, KEY_CAN_UPLOAD, "N");
        String from = ParamUtil.getRequestString(request, FROM);
        ParamUtil.setSessionAttr(request, FROM, null);
        SuspensionReinstatementDto dto = getSuspensionDto(request);
        if (StringUtils.isEmpty(dto.getApprovalId()) && StringUtils.isEmpty(dto.getApplicationId())) {
            String maskedAppId = ParamUtil.getString(request, KEY_APP_ID);
            String maskedTaskId = ParamUtil.getString(request, KEY_TASK_ID);
            String appId = MaskUtil.unMaskValue("id", maskedAppId);
            String taskId = MaskUtil.unMaskValue("id", maskedTaskId);
            if (appId == null || appId.equals(maskedAppId)) {
                throw new IaisRuntimeException("Invalid masked application ID");
            }
            if (taskId == null || taskId.equals(maskedTaskId)) {
                throw new IaisRuntimeException("Invalid masked task ID");
            }
            dto = suspensionClient.getSuspensionDataByApplicationId(appId).getEntity();
            dto.setTaskId(taskId);
            PrimaryDocDto primaryDocDto = new PrimaryDocDto();
            primaryDocDto.setSavedDocMap(dto.getQueryDocMap());
            dto.setPrimaryDocDto(primaryDocDto);
            //show routingHistory list
            processHistoryService.getAndSetHistoryInSession(dto.getApplicationNo(), request);
        }
        setModuleType(dto);
        ParamUtil.setSessionAttr(request, BACK, from);
        ParamUtil.setSessionAttr(request, SUSPENSION_REINSTATEMENT_DTO, dto);
    }

    private void setModuleType(SuspensionReinstatementDto dto) {
        if (dto.getApprovalStatus().equals(APPROVAL_STATUS_SUSPENDED)) {
            AuditTrailHelper.auditFunction(REINSTATEMENT_MODULE_NAME, REINSTATEMENT_MODULE_NAME);
        } else {
            AuditTrailHelper.auditFunction(SUSPENSION_MODULE_NAME, SUSPENSION_MODULE_NAME);
        }
    }

    public void doSuspensionValidate(BaseProcessClass bpc) {
        //validate duty officer submitted data
        HttpServletRequest request = bpc.request;
        SuspensionReinstatementDto dto = getSuspensionDto(request);
        dto.getDOSuspensionData(request);
        //
        PrimaryDocDto primaryDocDto;
        if (dto.getPrimaryDocDto() != null) {
            primaryDocDto = dto.getPrimaryDocDto();
        } else {
            primaryDocDto = new PrimaryDocDto();
        }
        primaryDocDto.reqObjMapping(request, SUSPENSION_DOC_TYPE);
        dto.setPrimaryDocDto(primaryDocDto);
        //
        dto.setDocMetas(primaryDocDto.doValidation("doSuspension"));
        //validation
        if (dto.getApprovalProcessType().equals(PROCESS_TYPE_FAC_CERTIFIER_REG)) {
            validateData(dto, request);
        } else {
            String actionType = "";
            ValidationResultDto validationResultDto = suspensionClient.validateSuspensionDto(dto);
            if (!validationResultDto.isPass()) {
                ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
                actionType = ACTION_TYPE_PREPARE;
            } else {
                actionType = ACTION_TYPE_NEXT;
            }
            ParamUtil.setRequestAttr(request, ACTION_TYPE, actionType);
        }
        ParamUtil.setSessionAttr(request, SUSPENSION_REINSTATEMENT_DTO, dto);
    }

    public void doSuspension(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SuspensionReinstatementDto dto = getSuspensionDto(request);
        PrimaryDocDto primaryDocDto = dto.getPrimaryDocDto();
        if (primaryDocDto != null) {
            if (!CollectionUtils.isEmpty(primaryDocDto.getToBeDeletedDocIds())) {
                deleteUnwantedDoc(primaryDocDto);
                dto.setToBeDeletedDocIds(primaryDocDto.getToBeDeletedDocIds());
            }
            MultipartFile[] files = primaryDocDto.getNewDocMap().values().stream().map(NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
            List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
            //newFile change to saved File and save info to db
            primaryDocDto.newFileSaved(repoIds);
            dto.setSavedInfos(new ArrayList<>(primaryDocDto.getSavedDocMap().values()));
        } else {
            log.info(KEY_NON_OBJECT_ERROR);
        }
        suspensionClient.doSuspension(dto);
        ParamUtil.setRequestAttr(request, ACK_MSG, DO_SUSPEND_ACK_MSG);
    }

    public void aoSuspensionValidate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SuspensionReinstatementDto dto = getSuspensionDto(request);
        dto.getAOSuspensionData(request);
        //
        PrimaryDocDto primaryDocDto;
        if (dto.getPrimaryDocDto() != null) {
            primaryDocDto = dto.getPrimaryDocDto();
        } else {
            primaryDocDto = new PrimaryDocDto();
        }
        primaryDocDto.reqObjMapping(request, SUSPENSION_DOC_TYPE);
        dto.setPrimaryDocDto(primaryDocDto);
        //
        validateData(dto, request);
        ParamUtil.setSessionAttr(request, SUSPENSION_REINSTATEMENT_DTO, dto);
    }

    public void aoSuspension(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SuspensionReinstatementDto dto = getSuspensionDto(request);
        PrimaryDocDto primaryDocDto = dto.getPrimaryDocDto();
        if (primaryDocDto != null && !CollectionUtils.isEmpty(primaryDocDto.getToBeDeletedRepoIds())) {
            deleteUnwantedDoc(primaryDocDto);
            dto.setToBeDeletedDocIds(primaryDocDto.getToBeDeletedRepoIds());
        }
        suspensionClient.aoSuspension(dto);
        ParamUtil.setRequestAttr(request, ACK_MSG, AO_HM_SUSPEND_ACK_MSG);
    }

    public void hmSuspensionValidate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SuspensionReinstatementDto dto = getSuspensionDto(request);
        dto.getHMSuspensionData(request);
        //
        PrimaryDocDto primaryDocDto;
        if (dto.getPrimaryDocDto() != null) {
            primaryDocDto = dto.getPrimaryDocDto();
        } else {
            primaryDocDto = new PrimaryDocDto();
        }
        primaryDocDto.reqObjMapping(request, SUSPENSION_DOC_TYPE);
        dto.setPrimaryDocDto(primaryDocDto);
        //
        validateData(dto, request);
        ParamUtil.setSessionAttr(request, SUSPENSION_REINSTATEMENT_DTO, dto);
    }

    public void hmSuspension(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SuspensionReinstatementDto dto = getSuspensionDto(request);
        PrimaryDocDto primaryDocDto = dto.getPrimaryDocDto();
        if (primaryDocDto != null && !CollectionUtils.isEmpty(primaryDocDto.getToBeDeletedRepoIds())) {
            deleteUnwantedDoc(primaryDocDto);
            dto.setToBeDeletedDocIds(primaryDocDto.getToBeDeletedRepoIds());
        }
        suspensionClient.hmSuspension(dto);
        ParamUtil.setRequestAttr(request, ACK_MSG, AO_HM_SUSPEND_ACK_MSG);
    }

    /************************************** Reinstatement ***********************************************/
    public void doReinstatementValidate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SuspensionReinstatementDto dto = getSuspensionDto(request);
        dto.getDOReinstatementData(request);
        //
        PrimaryDocDto primaryDocDto;
        if (dto.getPrimaryDocDto() != null) {
            primaryDocDto = dto.getPrimaryDocDto();
        } else {
            primaryDocDto = new PrimaryDocDto();
        }
        primaryDocDto.reqObjMapping(request, REINSTATEMENT_DOC_TYPE);
        dto.setPrimaryDocDto(primaryDocDto);
        //
        dto.setDocMetas(primaryDocDto.doValidation("doReinstatement"));
        validateData(dto, request);
        ParamUtil.setSessionAttr(request, SUSPENSION_REINSTATEMENT_DTO, dto);
    }

    public void doReinstatement(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SuspensionReinstatementDto dto = getSuspensionDto(request);
        PrimaryDocDto primaryDocDto = dto.getPrimaryDocDto();
        if (primaryDocDto != null) {
            if (!CollectionUtils.isEmpty(primaryDocDto.getToBeDeletedRepoIds())) {
                deleteUnwantedDoc(primaryDocDto);
                dto.setToBeDeletedDocIds(primaryDocDto.getToBeDeletedRepoIds());
            }
            MultipartFile[] files = primaryDocDto.getNewDocMap().values().stream().map(NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
            List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
            //newFile change to saved File and save info to db
            primaryDocDto.newFileSaved(repoIds);
            dto.setSavedInfos(new ArrayList<>(primaryDocDto.getSavedDocMap().values()));
        } else {
            log.info(KEY_NON_OBJECT_ERROR);
        }
        suspensionClient.doReinstatement(dto);
        ParamUtil.setRequestAttr(request, ACK_MSG, DO_SUSPEND_ACK_MSG);
    }

    public void aoReinstatementValidate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SuspensionReinstatementDto dto = getSuspensionDto(request);
        dto.getAOReinstatementData(request);
        //
        PrimaryDocDto primaryDocDto;
        if (dto.getPrimaryDocDto() != null) {
            primaryDocDto = dto.getPrimaryDocDto();
        } else {
            primaryDocDto = new PrimaryDocDto();
        }
        primaryDocDto.reqObjMapping(request, SUSPENSION_DOC_TYPE);
        dto.setPrimaryDocDto(primaryDocDto);
        //
        validateData(dto, request);
        ParamUtil.setSessionAttr(request, SUSPENSION_REINSTATEMENT_DTO, dto);
    }

    public void aoReinstatement(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SuspensionReinstatementDto dto = getSuspensionDto(request);
        PrimaryDocDto primaryDocDto = dto.getPrimaryDocDto();
        if (primaryDocDto != null && !CollectionUtils.isEmpty(primaryDocDto.getToBeDeletedRepoIds())) {
            deleteUnwantedDoc(primaryDocDto);
            dto.setToBeDeletedDocIds(primaryDocDto.getToBeDeletedRepoIds());
        }
        suspensionClient.aoReinstatement(dto);
        ParamUtil.setRequestAttr(request, ACK_MSG, AO_HM_SUSPEND_ACK_MSG);
    }

    public void hmReinstatementValidate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SuspensionReinstatementDto dto = getSuspensionDto(request);
        dto.getHMReinstatementData(request);
        //
        PrimaryDocDto primaryDocDto;
        if (dto.getPrimaryDocDto() != null) {
            primaryDocDto = dto.getPrimaryDocDto();
        } else {
            primaryDocDto = new PrimaryDocDto();
        }
        primaryDocDto.reqObjMapping(request, SUSPENSION_DOC_TYPE);
        dto.setPrimaryDocDto(primaryDocDto);
        //
        validateData(dto, request);
        ParamUtil.setSessionAttr(request, SUSPENSION_REINSTATEMENT_DTO, dto);
    }

    public void hmReinstatement(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SuspensionReinstatementDto dto = getSuspensionDto(request);
        PrimaryDocDto primaryDocDto = dto.getPrimaryDocDto();
        if (primaryDocDto != null && !CollectionUtils.isEmpty(primaryDocDto.getToBeDeletedRepoIds())) {
            deleteUnwantedDoc(primaryDocDto);
            dto.setToBeDeletedDocIds(primaryDocDto.getToBeDeletedRepoIds());
        }
        suspensionClient.hmReinstatement(dto);
        ParamUtil.setRequestAttr(request, ACK_MSG, AO_HM_SUSPEND_ACK_MSG);
    }

    /*********************************** Common ************************************************/
    private SuspensionReinstatementDto getSuspensionDto(HttpServletRequest request) {
        SuspensionReinstatementDto auditDto = (SuspensionReinstatementDto) ParamUtil.getSessionAttr(request, SUSPENSION_REINSTATEMENT_DTO);
        return auditDto == null ? getDefaultSuspensionDto() : auditDto;
    }

    private SuspensionReinstatementDto getDefaultSuspensionDto() {
        return new SuspensionReinstatementDto();
    }

    private void validateData(SuspensionReinstatementDto dto, HttpServletRequest request) {
        //validation
        String actionType = "";
        ValidationResultDto validationResultDto = suspensionClient.validateSuspensionDto(dto);
        if (!validationResultDto.isPass()) {
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            actionType = ACTION_TYPE_PREPARE;
        } else {
            actionType = ACTION_TYPE_SAVE;
        }
        ParamUtil.setRequestAttr(request, ACTION_TYPE, actionType);
    }

    /**
     * Delete unwanted documents in FE file repo.
     * This method will remove the repoId from the toBeDeletedRepoIds set after a call of removal.
     *
     * @param primaryDocDto document DTO have the specific structure
     * @return a list of repo IDs deleted in FE file repo
     */
    public List<String> deleteUnwantedDoc(PrimaryDocDto primaryDocDto) {
        /* Ignore the failure when try to delete FE files because this is not a big issue.
         * The not deleted file won't be retrieved, so it's just a waste of disk space */
        List<String> toBeDeletedRepoIds = new ArrayList<>(primaryDocDto.getToBeDeletedRepoIds());
        for (String id : toBeDeletedRepoIds) {
            FileRepoDto fileRepoDto = new FileRepoDto();
            fileRepoDto.setId(id);
            fileRepoClient.removeFileById(fileRepoDto);
            primaryDocDto.getToBeDeletedRepoIds().remove(id);
        }
        return toBeDeletedRepoIds;
    }
}
