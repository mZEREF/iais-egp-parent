package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sg.gov.moh.iais.egp.bsb.client.AuditClient;
import sg.gov.moh.iais.egp.bsb.client.BsbFileClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.constant.AuditConstants;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.FacilitySubmitSelfAuditDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.dto.file.FileRepoSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.DataSubmissionConstants.KEY_NON_OBJECT_ERROR;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_DO;


/**
 * @author Zhu Tangtang
 */
@Slf4j
@Delegator(value = "selfAuditDelegator")
public class SelfAuditDelegator {

    private final AuditClient auditClient;
    private final FileRepoClient fileRepoClient;
    private final BsbFileClient bsbFileClient;

    @Autowired
    public SelfAuditDelegator(AuditClient auditClient, FileRepoClient fileRepoClient, BsbFileClient bsbFileClient) {
        this.auditClient = auditClient;
        this.fileRepoClient = fileRepoClient;
        this.bsbFileClient = bsbFileClient;
    }

    public void start(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction(MODULE_AUDIT, FUNCTION_AUDIT);
        HttpServletRequest request = bpc.request;
        IaisEGPHelper.clearSessionAttr(request, AuditConstants.class);
        ParamUtil.setSessionAttr(request, PARAM_AUDIT_SEARCH, null);
        ParamUtil.setSessionAttr(request, SELF_AUDIT_DATA, null);
    }

    public void prepareFacilitySelfAuditData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacilitySubmitSelfAuditDto dto = getAuditDto(request);
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, ValidationConstants.KEY_SHOW_ERROR_SWITCH);
        if (Boolean.TRUE.equals(needShowError)) {
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, dto.retrieveValidationResult());
        }
        if (!StringUtils.hasLength(dto.getAuditId())) {
            //get needed data by appId(contain:audit,auditApp,Facility)
            String auditId = ParamUtil.getMaskedString(request, AUDIT_ID);
            ResponseDto<FacilitySubmitSelfAuditDto> responseDto = auditClient.getSelfAuditDataByAuditId(auditId);
            if (responseDto.ok()) {
                dto = responseDto.getEntity();
                ParamUtil.setSessionAttr(request, SELF_AUDIT_DATA, dto);
            } else {
                log.warn("get audit API doesn't return ok, the response is {}", responseDto);
                ParamUtil.setRequestAttr(request, SELF_AUDIT_DATA, new FacilitySubmitSelfAuditDto());
            }
        }
        setAuditDoc(request,dto);
        ParamUtil.setSessionAttr(request, SELF_AUDIT_DATA, dto);
    }

    public void preConfirm(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        FacilitySubmitSelfAuditDto dto = getAuditDto(request);
        String scenarioCategory = ParamUtil.getRequestString(request, PARAM_SCENARIO_CATEGORY);
        dto.setScenarioCategory(scenarioCategory);
        dto.setModule("facSelfAudit");
        PrimaryDocDto primaryDocDto = new PrimaryDocDto();
        primaryDocDto.reqObjMapping(mulReq, request, "SelfAudit");
        dto.setPrimaryDocDto(primaryDocDto);
        dto.setDocType("SelfAudit");

        //joint repoId exist
        String newRepoId = String.join(",", primaryDocDto.getNewDocMap().keySet());
        dto.setRepoIdNewString(newRepoId);
        //set newDocFiles
        dto.setNewDocInfos(primaryDocDto.getNewDocTypeList());
        //set need Validation value
        dto.setDocMetas(primaryDocDto.doValidation());
        setAuditDoc(request,dto);
        doValidation(dto, request);
        ParamUtil.setSessionAttr(request, SELF_AUDIT_DATA, dto);
    }

    public void submitSelfAuditReport(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacilitySubmitSelfAuditDto dto = getAuditDto(request);
        dto.setAuditStatus(PARAM_AUDIT_STATUS_PENDING_DO);
        dto.setAppStatus(APP_STATUS_PEND_DO);
        dto.setAuditAppStatus(PARAM_AUDIT_STATUS_PENDING_DO);
        //
        PrimaryDocDto primaryDocDto = dto.getPrimaryDocDto();
        List<NewFileSyncDto> newFilesToSync = null;
        if (primaryDocDto != null) {
            //complete simple save file to db and save data to dto for show in jsp
            MultipartFile[] files = primaryDocDto.getNewDocMap().values().stream().map(PrimaryDocDto.NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
            List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
            //newFile change to saved File and save info to db
            newFilesToSync = new ArrayList<>(primaryDocDto.newFileSaved(repoIds));
            dto.setSavedInfos(primaryDocDto.getExistDocTypeList());
        } else {
            log.info(KEY_NON_OBJECT_ERROR);
        }
        auditClient.facilitySubmitSelfAudit(dto);
        try {
            // sync files to BE file-repo (save new added files, delete useless files)
            if (newFilesToSync != null && !newFilesToSync.isEmpty()) {
                /* Ignore the failure of sync files currently.
                 * We should add a mechanism to retry synchronization of files in the future */
                FileRepoSyncDto syncDto = new FileRepoSyncDto();
                syncDto.setNewFiles(newFilesToSync);
                bsbFileClient.saveFiles(syncDto);
            }
        } catch (Exception e) {
            log.error("Fail to sync files to BE", e);
        }
    }

    private FacilitySubmitSelfAuditDto getAuditDto(HttpServletRequest request) {
        FacilitySubmitSelfAuditDto auditDto = (FacilitySubmitSelfAuditDto) ParamUtil.getSessionAttr(request, AuditConstants.SELF_AUDIT_DATA);
        return auditDto == null ? getDefaultAuditDto() : auditDto;
    }

    private FacilitySubmitSelfAuditDto getDefaultAuditDto() {
        return new FacilitySubmitSelfAuditDto();
    }

    /**
     * just a method to do simple valid,maybe update in the future
     * doValidation
     */
    private void doValidation(FacilitySubmitSelfAuditDto dto, HttpServletRequest request) {
        if (dto.doValidation()) {
            ParamUtil.setRequestAttr(request, ValidationConstants.IS_VALID, ValidationConstants.YES);
        } else {
            ParamUtil.setRequestAttr(request, ValidationConstants.IS_VALID, ValidationConstants.NO);
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
        }
    }

    public static void setAuditDoc(HttpServletRequest request, FacilitySubmitSelfAuditDto revokeDto) {
        PrimaryDocDto primaryDocDto = new PrimaryDocDto();
        primaryDocDto.setSavedDocMap(sg.gov.moh.iais.egp.bsb.util.CollectionUtils.uniqueIndexMap(revokeDto.getDocRecordInfos(), PrimaryDocDto.DocRecordInfo::getRepoId));
        Map<String, List<PrimaryDocDto.DocRecordInfo>> saveFiles = primaryDocDto.getExistDocTypeMap();
        Set<String> docTypes = saveFiles.keySet();
        ParamUtil.setRequestAttr(request, "docTypes", docTypes);
        ParamUtil.setRequestAttr(request, "savedFiles", saveFiles);
        ParamUtil.setSessionAttr(request, "primaryDocDto", primaryDocDto);
    }
}
