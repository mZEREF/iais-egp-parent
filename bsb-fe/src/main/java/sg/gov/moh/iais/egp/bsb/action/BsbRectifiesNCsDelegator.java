package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import sg.gov.moh.iais.egp.bsb.client.BsbFileClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.dto.file.*;
import sg.gov.moh.iais.egp.bsb.dto.inspection.RectifyFindingFormDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.RectifyInsReportDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.RectifyInsReportSaveDto;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author YiMing
 * 2022/2/11 13:10
 **/
@Slf4j
@Delegator("rectifiesNCsDelegator")
public class BsbRectifiesNCsDelegator {
    private final InspectionClient inspectionClient;
    private final FileRepoClient fileRepoClient;
    private final BsbFileClient bsbFileClient;
    private static final String KEY_RECTIFY_FINDING_FORM = "ncsPreData";
    private static final String KEY_RECTIFY_SAVED_DTO = "ncsSavedDto";
    private static final String KEY_RECTIFY_SAVED_DOC_DTO = "ncsSavedDocDto";
    private static final String KEY_APPLICATION_ID = "appId";
    private static final String KEY_ITEM_VALUE     = "itemValue";
    private static final String KEY_MASKED_ITEM_VALUE = "itemVal";
    private static final String KEY_ITEM_RECTIFY_MAP = "rectifyMap";
    private static final String KEY_ALL_ITEM_RECTIFY = "isAllRectify";

    public BsbRectifiesNCsDelegator(InspectionClient inspectionClient, FileRepoClient fileRepoClient, BsbFileClient bsbFileClient) {
        this.inspectionClient = inspectionClient;
        this.fileRepoClient = fileRepoClient;
        this.bsbFileClient = bsbFileClient;
    }

    public void start(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_APPLICATION_ID);
        request.getSession().removeAttribute(KEY_RECTIFY_SAVED_DTO);
        request.getSession().removeAttribute(KEY_RECTIFY_FINDING_FORM);
        request.getSession().removeAttribute(KEY_RECTIFY_SAVED_DOC_DTO);
        AuditTrailHelper.auditFunction("Applicant rectifies NCs", "Applicant rectifies NCs");
    }

    public void init(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        //get application id
        //search NCs list info
        RectifyFindingFormDto rectifyFindingFormDto = inspectionClient.getNonComplianceFindingFormDtoByAppId("B861509B-946F-EC11-BE74-000C298D317C").getEntity();
        //save basic info such as appId and config id
        if(rectifyFindingFormDto != null){
            RectifyInsReportSaveDto savedDto = getRectifyNCsSavedDto(request);
            RectifyInsReportDto reportDto = getRectifyNcsSavedDocDto(request);
            savedDto.setAppId(rectifyFindingFormDto.getAppId());
            savedDto.setConfigId(rectifyFindingFormDto.getConfigId());
            if(!rectifyFindingFormDto.getDocDtoList().isEmpty()){
                reportDto.setSavedDocMap(rectifyFindingFormDto.getDocDtoList().stream().collect(Collectors.toMap(DocRecordInfo::getRepoId, Function.identity())));
                ParamUtil.setSessionAttr(request,KEY_RECTIFY_SAVED_DOC_DTO,reportDto);
            }
            ParamUtil.setSessionAttr(request,KEY_RECTIFY_SAVED_DTO,savedDto);

            //load origin icon status
            loadOriginIconStatus(rectifyFindingFormDto.getItemDtoList(),reportDto,request);
            //data->session
            ParamUtil.setSessionAttr(request,KEY_RECTIFY_FINDING_FORM,rectifyFindingFormDto);
            //pay attention to clear session
        }
    }


    public void prepareNCsData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        //judge if all items is rectified
        boolean isAllRectified = true;
        Map<String,String> itemRectifyMap = getItemRectifyMap(request);
        for (Map.Entry<String, String> entry : itemRectifyMap.entrySet()) {
            if (MasterCodeConstants.NO.equals(entry.getValue())) {
                isAllRectified = false;
                break;
            }
        }
        ParamUtil.setSessionAttr(request,KEY_ALL_ITEM_RECTIFY,isAllRectified);
    }

    public void handleNCs(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String maskedItemValue = ParamUtil.getString(request,KEY_ITEM_VALUE);
        String itemValue = MaskUtil.unMaskValue(KEY_MASKED_ITEM_VALUE,maskedItemValue);
        ParamUtil.setSessionAttr(request,KEY_ITEM_VALUE,itemValue);
        actionJumpHandler(request);
    }

    public void submitNCs(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        //do doc sync
        RectifyInsReportDto docDto = getRectifyNcsSavedDocDto(request);
        RectifyInsReportSaveDto savedDto = getRectifyNCsSavedDto(request);
        List<NewFileSyncDto> newFilesToSync = saveNewUploadedDoc(docDto);
        if(!docDto.getSavedDocMap().isEmpty()){
            savedDto.setAttachmentList(new ArrayList<>(docDto.getSavedDocMap().values()));
            savedDto.setToBeDeletedDocIds(docDto.getToBeDeletedDocIds());
        }
        inspectionClient.saveInsNonComplianceReport(savedDto);
        //save all info into database
        try {
            // delete docs
            List<String> toBeDeletedRepoIds = deleteUnwantedDoc(docDto);
            // sync docs
            syncNewDocsAndDeleteFiles(newFilesToSync,toBeDeletedRepoIds);
        } catch (Exception e) {
            log.error("Fail to sync files to BE", e);
        }
    }

    public void prepareRectifyData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        RectifyFindingFormDto findingFormDto = (RectifyFindingFormDto) ParamUtil.getSessionAttr(request,KEY_RECTIFY_FINDING_FORM);
        String itemValue = (String) ParamUtil.getSessionAttr(request,KEY_ITEM_VALUE);
        RectifyFindingFormDto.RectifyFindingItemDto itemDto = findingFormDto.getRectifyFindingItemDtoByItemValue(itemValue);
        ParamUtil.setRequestAttr(request,"rectifyItemDto",itemDto);
    }

    public void handleRectifyPage(BaseProcessClass bpc){
        //request mapping item doc and item remarks info
        HttpServletRequest request = bpc.request;
        String actionType = ParamUtil.getString(request, ModuleCommonConstants.KEY_ACTION_TYPE);
        if(ModuleCommonConstants.KEY_SAVE.equals(actionType)){
            RectifyInsReportDto docDto = getRectifyNcsSavedDocDto(request);
            RectifyInsReportSaveDto savedDto = getRectifyNCsSavedDto(request);
            String itemValue = (String) ParamUtil.getSessionAttr(request,KEY_ITEM_VALUE);
            Assert.hasLength(itemValue,"item value -sectionId +'--v--'+configId is null");
            docDto.reqObjMapping(request, DocConstants.DOC_TYPE_INSPECTION_NON_COMPLIANCE,itemValue);
            savedDto.reqObjMapping(request,itemValue);
            log.info(LogUtil.escapeCrlf(docDto.toString()));
            log.info(LogUtil.escapeCrlf(savedDto.toString()));
            Map<String,String> itemRectifyMap = getItemRectifyMap(request);
            turnCurrentIconStatus(request,docDto,itemValue,itemRectifyMap);
            ParamUtil.setSessionAttr(request,KEY_RECTIFY_SAVED_DTO,savedDto);
            ParamUtil.setSessionAttr(request,KEY_RECTIFY_SAVED_DOC_DTO,docDto);
        }
        actionJumpHandler(request);
    }

    /**
     *loadOriginIconStatus
     * a method used to load origin icon status from database
     * @param itemDtoList document search from database contain docSubType
     * @param request HttpServletRequest
     * */
    public void loadOriginIconStatus(List<RectifyFindingFormDto.RectifyFindingItemDto> itemDtoList,RectifyInsReportDto reportDto,HttpServletRequest request){
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
            itemRectifyMap.put(itemValue,MasterCodeConstants.NO);
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

    public void actionJumpHandler(HttpServletRequest request){
        String actionType = ParamUtil.getString(request, ModuleCommonConstants.KEY_ACTION_TYPE);
        Assert.hasLength(actionType,"action_type is null");
        if(ModuleCommonConstants.KEY_NAV_RECTIFY.equals(actionType)){
            ParamUtil.setRequestAttr(request,ModuleCommonConstants.KEY_INDEED_ACTION_TYPE,ModuleCommonConstants.KEY_NAV_RECTIFY);
        }else if(ModuleCommonConstants.KEY_SUBMIT.equals(actionType)){
            ParamUtil.setRequestAttr(request,ModuleCommonConstants.KEY_INDEED_ACTION_TYPE,ModuleCommonConstants.KEY_SUBMIT);
        }else if(ModuleCommonConstants.KEY_SAVE.equals(actionType)){
            ParamUtil.setRequestAttr(request,ModuleCommonConstants.KEY_INDEED_ACTION_TYPE,ModuleCommonConstants.KEY_NAV_PREPARE);
        }else if(ModuleCommonConstants.KEY_NAV_BACK.equals(actionType)){
            ParamUtil.setRequestAttr(request,ModuleCommonConstants.KEY_INDEED_ACTION_TYPE,ModuleCommonConstants.KEY_NAV_PREPARE);
        }
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
