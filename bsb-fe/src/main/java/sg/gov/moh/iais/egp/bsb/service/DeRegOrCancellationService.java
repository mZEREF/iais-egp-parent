package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import sg.gov.moh.iais.egp.bsb.client.BsbFileClient;
import sg.gov.moh.iais.egp.bsb.client.DeRegOrCancellationClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.dto.deregorcancellation.CancellationApprovalDto;
import sg.gov.moh.iais.egp.bsb.dto.deregorcancellation.DeRegistrationAFCDto;
import sg.gov.moh.iais.egp.bsb.dto.deregorcancellation.DeRegistrationFacilityDto;
import sg.gov.moh.iais.egp.bsb.dto.file.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.module.CessationAndDeRegConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.*;

/**
 * @author : LiRan
 * @date : 2022/1/18
 */
@Service
@Slf4j
public class DeRegOrCancellationService {
    private final FileRepoClient fileRepoClient;
    private final BsbFileClient bsbFileClient;
    private final DeRegOrCancellationClient deRegOrCancellationClient;
    private final DocSettingService docSettingService;

    public DeRegOrCancellationService(FileRepoClient fileRepoClient, BsbFileClient bsbFileClient,
                                      DeRegOrCancellationClient deRegOrCancellationClient, DocSettingService docSettingService) {
        this.fileRepoClient = fileRepoClient;
        this.bsbFileClient = bsbFileClient;
        this.deRegOrCancellationClient = deRegOrCancellationClient;
        this.docSettingService = docSettingService;
    }

    public void setDocSettingAndData(HttpServletRequest request, CommonDocDto commonDocDto) {
        ParamUtil.setRequestAttr(request, "docSettings", docSettingService.getAttachmentsDocSettings());
        Map<String, List<DocRecordInfo>> savedFiles = commonDocDto.getExistDocTypeMap();
        Map<String, List<NewDocInfo>> newFiles = commonDocDto.getNewDocTypeMap();
        ParamUtil.setRequestAttr(request, "savedFiles", savedFiles);
        ParamUtil.setRequestAttr(request, "newFiles", newFiles);
    }

    public void deleteAndSyncDocs(CommonDocDto commonDocDto, List<NewFileSyncDto> newFilesToSync){
        try {
            // delete docs
            List<String> toBeDeletedRepoIds = deleteUnwantedDoc(commonDocDto);
            // sync docs
            syncNewDocsAndDeleteFiles(newFilesToSync, toBeDeletedRepoIds);
        } catch (Exception e) {
            log.error("Fail to sync files to BE", e);
        }
    }

    public void saveDeRegistrationFacilityDraft(HttpServletRequest request){
        DeRegistrationFacilityDto deRegistrationFacilityDto = (DeRegistrationFacilityDto) ParamUtil.getSessionAttr(request, KEY_DE_REGISTRATION_FACILITY_DTO);

        //save docs
        CommonDocDto commonDocDto = getCommonDocDoc(request);
        List<NewFileSyncDto> newFileSyncDtoList = saveNewUploadedDoc(commonDocDto);

        deRegistrationFacilityDto.setDocRecordInfos(new ArrayList<>(commonDocDto.getSavedDocMap().values()));
        //save data
        String draftAppNo = deRegOrCancellationClient.saveNewDeRegistrationFacilityDraft(deRegistrationFacilityDto);
        deRegistrationFacilityDto.setDraftAppNo(draftAppNo);
        ParamUtil.setSessionAttr(request, KEY_DE_REGISTRATION_FACILITY_DTO, deRegistrationFacilityDto);

        deleteAndSyncDocs(commonDocDto, newFileSyncDtoList);

        ParamUtil.setRequestAttr(request, KEY_IND_AFTER_SAVE_AS_DRAFT, Boolean.TRUE);
    }

    public void saveCancellationApprovalDraft(HttpServletRequest request){
        CancellationApprovalDto cancellationApprovalDto = (CancellationApprovalDto) ParamUtil.getSessionAttr(request, KEY_CANCELLATION_APPROVAL_DTO);

        //save docs
        CommonDocDto commonDocDto = getCommonDocDoc(request);
        List<NewFileSyncDto> newFileSyncDtoList = saveNewUploadedDoc(commonDocDto);

        cancellationApprovalDto.setDocRecordInfos(new ArrayList<>(commonDocDto.getSavedDocMap().values()));
        //save data
        String draftAppNo = deRegOrCancellationClient.saveNewCancellationApprovalDraft(cancellationApprovalDto);
        cancellationApprovalDto.setDraftAppNo(draftAppNo);
        ParamUtil.setSessionAttr(request, KEY_CANCELLATION_APPROVAL_DTO, cancellationApprovalDto);

        deleteAndSyncDocs(commonDocDto, newFileSyncDtoList);

        ParamUtil.setRequestAttr(request, KEY_IND_AFTER_SAVE_AS_DRAFT, Boolean.TRUE);
    }

    public void saveDeRegistrationAFCDraft(HttpServletRequest request){
        DeRegistrationAFCDto deRegistrationAFCDto = (DeRegistrationAFCDto) ParamUtil.getSessionAttr(request, KEY_DE_REGISTRATION_AFC_DTO);

        //save docs
        CommonDocDto commonDocDto = getCommonDocDoc(request);
        List<NewFileSyncDto> newFileSyncDtoList = saveNewUploadedDoc(commonDocDto);

        deRegistrationAFCDto.setDocRecordInfos(new ArrayList<>(commonDocDto.getSavedDocMap().values()));
        //save data
        String draftAppNo = deRegOrCancellationClient.saveNewDeRegistrationAFCDraft(deRegistrationAFCDto);
        deRegistrationAFCDto.setDraftAppNo(draftAppNo);
        ParamUtil.setSessionAttr(request, KEY_DE_REGISTRATION_AFC_DTO, deRegistrationAFCDto);

        deleteAndSyncDocs(commonDocDto, newFileSyncDtoList);

        ParamUtil.setRequestAttr(request, KEY_IND_AFTER_SAVE_AS_DRAFT, Boolean.TRUE);
    }

    public CommonDocDto getCommonDocDoc(HttpServletRequest request){
        CommonDocDto commonDocDto = (CommonDocDto) ParamUtil.getSessionAttr(request, DocConstants.KEY_COMMON_DOC_DTO);
        return commonDocDto == null ? new CommonDocDto() : commonDocDto;
    }

    /** Save new uploaded documents into FE file repo.
     * @param commonDocDto document DTO have the specific structure
     * @return a list of DTOs can be used to sync to BE
     */
    public List<NewFileSyncDto> saveNewUploadedDoc(CommonDocDto commonDocDto) {
        List<NewFileSyncDto> newFilesToSync = null;
        if (!commonDocDto.getNewDocMap().isEmpty()) {
            MultipartFile[] files = commonDocDto.getNewDocMap().values().stream().map(NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
            List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
            newFilesToSync = commonDocDto.newFileSaved(repoIds);
        }
        return newFilesToSync;
    }

    /** Delete unwanted documents in FE file repo.
     * This method will remove the repoId from the toBeDeletedRepoIds set after a call of removal.
     * @param commonDocDto document DTO have the specific structure
     * @return a list of repo IDs deleted in FE file repo
     */
    public List<String> deleteUnwantedDoc(CommonDocDto commonDocDto) {
        /* Ignore the failure when try to delete FE files because this is not a big issue.
         * The not deleted file won't be retrieved, so it's just a waste of disk space */
        List<String> toBeDeletedRepoIds = new ArrayList<>(commonDocDto.getToBeDeletedRepoIds());
        for (String id: toBeDeletedRepoIds) {
            FileRepoDto fileRepoDto = new FileRepoDto();
            fileRepoDto.setId(id);
            fileRepoClient.removeFileById(fileRepoDto);
            commonDocDto.getToBeDeletedRepoIds().remove(id);
        }
        return toBeDeletedRepoIds;
    }

    /** Sync new uploaded documents to BE; delete unwanted documents in BE too.
     * @param newFileSyncDtoList a list of DTOs contains ID and data
     * @param toBeDeletedRepoIds a list of repo IDs to be deleted in BE
     */
    public void syncNewDocsAndDeleteFiles(List<NewFileSyncDto> newFileSyncDtoList, List<String> toBeDeletedRepoIds) {
        // sync files to BE file-repo (save new added files, delete useless files)
        if (!CollectionUtils.isEmpty(newFileSyncDtoList) || !CollectionUtils.isEmpty(toBeDeletedRepoIds)) {
            /* Ignore the failure of sync files currently.
             * We should add a mechanism to retry synchronization of files in the future */
            FileRepoSyncDto syncDto = new FileRepoSyncDto();
            syncDto.setNewFiles(newFileSyncDtoList);
            syncDto.setToDeleteIds(toBeDeletedRepoIds);
            bsbFileClient.saveFiles(syncDto);
        }
    }
}
