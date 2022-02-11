package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.constant.RevocationConstants;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.revocation.*;
import sg.gov.moh.iais.egp.bsb.client.RevocationClient;
import sg.gov.moh.iais.egp.bsb.dto.suspension.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.service.ProcessHistoryService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.KEY_TASK_ID;
import static sg.gov.moh.iais.egp.bsb.constant.RevocationConstants.*;

/**
 * @author Zhu Tangtang
 */
@Slf4j
@Delegator(value = "AORevocationDelegator")
public class AORevocationDelegator {
    private static final String ACTION_TYPE_ROUTE_BACK = "routeBack";
    private static final String ACTION_TYPE_ROUTE_TO_HM = "routeToHM";
    private static final String ACTION_TYPE_REJECT = "reject";
    private static final String ACTION_TYPE_APPROVE = "approve";
    private static final String ACTION_TYPE_PREPARE = "prepare";
    private static final String ACTION_TYPE = "action_type";

    private final RevocationClient revocationClient;
    private final FileRepoClient fileRepoClient;
    private final ProcessHistoryService processHistoryService;

    public AORevocationDelegator(RevocationClient revocationClient, FileRepoClient fileRepoClient, ProcessHistoryService processHistoryService) {
        this.revocationClient = revocationClient;
        this.fileRepoClient = fileRepoClient;
        this.processHistoryService = processHistoryService;
    }

    public void start(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction(MODULE_REVOCATION, FUNCTION_REVOCATION);
        HttpServletRequest request = bpc.request;
        IaisEGPHelper.clearSessionAttr(request, RevocationConstants.class);
        ParamUtil.setSessionAttr(request, PARAM_APPLICATION_SEARCH, null);
        ParamUtil.setSessionAttr(request, PARAM_REVOCATION_DETAIL, null);
        ParamUtil.setSessionAttr(request, PARAM_REVOKE_DTO, null);
        ParamUtil.setSessionAttr(request, BACK_URL, null);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, KEY_CAN_UPLOAD, "N");

        SubmitRevokeDto revokeDto = getRevokeDto(request);
        if (!StringUtils.hasLength(revokeDto.getAppId())) {
            String maskedAppId = ParamUtil.getString(request, KEY_APP_ID);
            String maskedTaskId = ParamUtil.getString(request, KEY_TASK_ID);
            if (log.isInfoEnabled()) {
                log.info("masked application id: [{}]", LogUtil.escapeCrlf(maskedAppId));
                log.info("masked task id: [{}]", LogUtil.escapeCrlf(maskedTaskId));
            }
            String appId = MaskUtil.unMaskValue("id", maskedAppId);
            String taskId = MaskUtil.unMaskValue("id", maskedTaskId);
            if (appId == null || appId.equals(maskedAppId)) {
                throw new IaisRuntimeException("Invalid masked application ID");
            }
            if (taskId == null || taskId.equals(maskedTaskId)) {
                throw new IaisRuntimeException("Invalid masked task ID");
            }
            revokeDto = revocationClient.getSubmitRevokeDtoByAppId(appId).getEntity();
            revokeDto.setTaskId(taskId);
            PrimaryDocDto primaryDocDto = new PrimaryDocDto();
            primaryDocDto.setSavedDocMap(revokeDto.getQueryDocMap());
            revokeDto.setPrimaryDocDto(primaryDocDto);
            //show routingHistory list
            processHistoryService.getAndSetHistoryInSession(revokeDto.getApplicationNo(), request);
        }
        ParamUtil.setSessionAttr(request, BACK_URL, TASK_LIST_URL);
        ParamUtil.setSessionAttr(request, PARAM_REVOKE_DTO, revokeDto);
    }

    public void doValidate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SubmitRevokeDto revokeDto = getRevokeDto(request);
        revokeDto.aoReqObjMapping(request);
        //
        PrimaryDocDto primaryDocDto;
        if (revokeDto.getPrimaryDocDto() != null) {
            primaryDocDto = revokeDto.getPrimaryDocDto();
        } else {
            primaryDocDto = new PrimaryDocDto();
        }
        primaryDocDto.reqObjMapping(request, "Revocation");
        revokeDto.setPrimaryDocDto(primaryDocDto);
        //
        validateData(revokeDto, request);
        ParamUtil.setSessionAttr(request, PARAM_REVOKE_DTO, revokeDto);
    }

    public void approve(BaseProcessClass bpc) {
        SubmitRevokeDto submitRevokeDto = getRevokeDto(bpc.request);
        submitRevokeDto.setStatus(PARAM_APPLICATION_STATUS_APPROVED);
        PrimaryDocDto primaryDocDto = submitRevokeDto.getPrimaryDocDto();
        if (primaryDocDto != null && !CollectionUtils.isEmpty(primaryDocDto.getToBeDeletedRepoIds())) {
            deleteUnwantedDoc(primaryDocDto);
            submitRevokeDto.setToBeDeletedDocIds(primaryDocDto.getToBeDeletedRepoIds());
        }
        revocationClient.updateRevokeApplication(submitRevokeDto);
    }

    public void reject(BaseProcessClass bpc) {
        SubmitRevokeDto submitRevokeDto = getRevokeDto(bpc.request);
        submitRevokeDto.setStatus(PARAM_APPLICATION_STATUS_REJECTED);
        PrimaryDocDto primaryDocDto = submitRevokeDto.getPrimaryDocDto();
        if (primaryDocDto != null && !CollectionUtils.isEmpty(primaryDocDto.getToBeDeletedRepoIds())) {
            deleteUnwantedDoc(primaryDocDto);
            submitRevokeDto.setToBeDeletedDocIds(primaryDocDto.getToBeDeletedRepoIds());
        }
        revocationClient.updateRevokeApplication(submitRevokeDto);
    }

    public void routebackToDO(BaseProcessClass bpc) {
        SubmitRevokeDto submitRevokeDto = getRevokeDto(bpc.request);
        submitRevokeDto.setStatus(PARAM_APPLICATION_STATUS_PENDING_DO);
        PrimaryDocDto primaryDocDto = submitRevokeDto.getPrimaryDocDto();
        if (primaryDocDto != null && !CollectionUtils.isEmpty(primaryDocDto.getToBeDeletedRepoIds())) {
            deleteUnwantedDoc(primaryDocDto);
            submitRevokeDto.setToBeDeletedDocIds(primaryDocDto.getToBeDeletedRepoIds());
        }
        revocationClient.updateRevokeApplication(submitRevokeDto);
    }

    public void routeToHM(BaseProcessClass bpc) {
        SubmitRevokeDto submitRevokeDto = getRevokeDto(bpc.request);
        submitRevokeDto.setStatus(PARAM_APPLICATION_STATUS_PENDING_HM);
        PrimaryDocDto primaryDocDto = submitRevokeDto.getPrimaryDocDto();
        if (primaryDocDto != null && !CollectionUtils.isEmpty(primaryDocDto.getToBeDeletedRepoIds())) {
            deleteUnwantedDoc(primaryDocDto);
            submitRevokeDto.setToBeDeletedDocIds(primaryDocDto.getToBeDeletedRepoIds());
        }
        revocationClient.updateRevokeApplication(submitRevokeDto);
    }

    private SubmitRevokeDto getRevokeDto(HttpServletRequest request) {
        SubmitRevokeDto searchDto = (SubmitRevokeDto) ParamUtil.getSessionAttr(request, PARAM_REVOKE_DTO);
        return searchDto == null ? getDefaultRevokeDto() : searchDto;
    }

    private SubmitRevokeDto getDefaultRevokeDto() {
        return new SubmitRevokeDto();
    }

    private void validateData(SubmitRevokeDto dto, HttpServletRequest request){
        //validation
        String actionType = null;
        ValidationResultDto validationResultDto = revocationClient.validateRevoke(dto);
        if (!validationResultDto.isPass()){
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            actionType = ACTION_TYPE_PREPARE;
        }else {
            if (dto.getAoDecision().equals("MOHPRO008")){
                actionType = ACTION_TYPE_ROUTE_BACK;
            } else if (dto.getAoDecision().equals("MOHPRO009")){
                actionType = ACTION_TYPE_ROUTE_TO_HM;
            } else if (dto.getAoDecision().equals("MOHPRO003")){
                actionType = ACTION_TYPE_REJECT;
            } else if (dto.getAoDecision().equals("MOHPRO007")){
                actionType = ACTION_TYPE_APPROVE;
            }
        }
        ParamUtil.setRequestAttr(request, ACTION_TYPE, actionType);
    }
    /** Delete unwanted documents in FE file repo.
     * This method will remove the repoId from the toBeDeletedRepoIds set after a call of removal.
     * @param primaryDocDto document DTO have the specific structure
     * @return a list of repo IDs deleted in FE file repo
     */
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
}
