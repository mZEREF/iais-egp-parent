package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import sg.gov.moh.iais.egp.bsb.client.BsbFileClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.FileRepoSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.RectifyFindingFormDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.RectifyInsReportDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.RectifyInsReportSaveDto;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.*;

@Service
@Slf4j
public class InspectionService {
    private final FileRepoClient fileRepoClient;
    private final BsbFileClient bsbFileClient;

    public InspectionService(FileRepoClient fileRepoClient, BsbFileClient bsbFileClient) {
        this.fileRepoClient = fileRepoClient;
        this.bsbFileClient = bsbFileClient;
    }

    /**
     *loadOriginIconStatus
     * a method used to load origin icon status from database
     * @param itemDtoList document search from database contain docSubType
     * @param request HttpServletRequest
     * */
    public void loadOriginIconStatus(List<RectifyFindingFormDto.RectifyFindingItemDto> itemDtoList, RectifyInsReportDto reportDto, HttpServletRequest request){
        //docSubType that is item value -- section id + --v-- +config id
        Map<String,String> itemRectifyMap = Maps.newHashMapWithExpectedSize(itemDtoList.size());
        if(!itemDtoList.isEmpty()){
            for (RectifyFindingFormDto.RectifyFindingItemDto itemDto : itemDtoList) {
                turnCurrentIconStatus(request,reportDto,itemDto.getItemValue(),itemRectifyMap);
            }
        }
        ParamUtil.setSessionAttr(request,KEY_ITEM_RECTIFY_MAP,new HashMap<>(itemRectifyMap));
    }

    public void turnCurrentIconStatus(HttpServletRequest request,RectifyInsReportDto docDto,String itemValue,Map<String,String> itemRectifyMap){
        //turn rectify icon to Y
        //saved doc dto is null and new doc dto is null turn rectify icon to N
        Assert.hasLength(itemValue,"itemValue is null");
        Map<String,List<NewDocInfo>> newDocSubTypeMap = docDto.getNewDocSubTypeMap();
        Map<String,List<DocRecordInfo>> savedDocSubTypeMap = docDto.getSavedDocSubTypeMap();
        if(CollectionUtils.isEmpty(newDocSubTypeMap.get(itemValue)) && CollectionUtils.isEmpty(savedDocSubTypeMap.get(itemValue))){
            itemRectifyMap.put(itemValue, MasterCodeConstants.NO);
        }else{
            itemRectifyMap.put(itemValue,MasterCodeConstants.YES);
        }
        ParamUtil.setSessionAttr(request,KEY_ITEM_RECTIFY_MAP,new HashMap<>(itemRectifyMap));
    }


    public Map<String,String> getItemRectifyMap(HttpServletRequest request){
        Map<String,String> itemRectifyMap  = (Map<String, String>) ParamUtil.getSessionAttr(request,KEY_ITEM_RECTIFY_MAP);
        return itemRectifyMap == null?new HashMap<>():itemRectifyMap;
    }

    public RectifyInsReportDto getRectifyNcsSavedDocDto(HttpServletRequest request){
        RectifyInsReportDto  reportDto = (RectifyInsReportDto) ParamUtil.getSessionAttr(request,KEY_RECTIFY_SAVED_DOC_DTO);
        return reportDto == null?new RectifyInsReportDto():reportDto;
    }

    public RectifyInsReportSaveDto getRectifyNCsSavedDto(HttpServletRequest request){
        RectifyInsReportSaveDto dto = (RectifyInsReportSaveDto) ParamUtil.getSessionAttr(request,KEY_RECTIFY_SAVED_DTO);
        return dto == null?new RectifyInsReportSaveDto():dto;
    }

    /** Save new uploaded documents into FE file repo.
     * @param dto document DTO have the specific structure
     * @return a list of DTOs can be used to sync to BE
     */
    public List<NewFileSyncDto> saveNewUploadedDoc(RectifyInsReportDto dto) {
        List<NewFileSyncDto> newFilesToSync = null;
        if (!dto.getNewDocMap().isEmpty()) {
            MultipartFile[] files = dto.getNewDocMap().values().stream().map(NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
            List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
            newFilesToSync = dto.newFileSaved(repoIds);
        }
        return newFilesToSync;
    }

    /** Delete unwanted documents in FE file repo.
     * This method will remove the repoId from the toBeDeletedRepoIds set after a call of removal.
     * @param dto document DTO have the specific structure
     * @return a list of repo IDs deleted in FE file repo
     */
    public List<String> deleteUnwantedDoc(RectifyInsReportDto dto) {
        /* Ignore the failure when try to delete FE files because this is not a big issue.
         * The not deleted file won't be retrieved, so it's just a waste of disk space */
        List<String> toBeDeletedRepoIds = new ArrayList<>(dto.getToBeDeletedRepoIds());
        for (String id: toBeDeletedRepoIds) {
            FileRepoDto fileRepoDto = new FileRepoDto();
            fileRepoDto.setId(id);
            fileRepoClient.removeFileById(fileRepoDto);
            dto.getToBeDeletedRepoIds().remove(id);
        }
        return toBeDeletedRepoIds;
    }

    /** Sync new uploaded documents to BE; delete unwanted documents in BE too.
     * @param newFilesToSync a list of DTOs contains ID and data
     * @param toBeDeletedRepoIds a list of repo IDs to be deleted in BE
     */
    public void syncNewDocsAndDeleteFiles(List<NewFileSyncDto> newFilesToSync, List<String> toBeDeletedRepoIds) {
        // sync files to BE file-repo (save new added files, delete useless files)
        if (!CollectionUtils.isEmpty(newFilesToSync) || !CollectionUtils.isEmpty(toBeDeletedRepoIds)) {
            /* Ignore the failure of sync files currently.
             * We should add a mechanism to retry synchronization of files in the future */
            FileRepoSyncDto syncDto = new FileRepoSyncDto();
            syncDto.setNewFiles(newFilesToSync);
            syncDto.setToDeleteIds(toBeDeletedRepoIds);
            bsbFileClient.saveFiles(syncDto);
        }
    }
}
