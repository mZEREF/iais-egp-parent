package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sg.gov.moh.iais.egp.bsb.client.BsbFileClient;
import sg.gov.moh.iais.egp.bsb.client.DataSubmissionClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.client.TransferClient;
import sg.gov.moh.iais.egp.bsb.constant.DataSubmissionConstants;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.entity.DraftDto;
import sg.gov.moh.iais.egp.bsb.dto.file.FileRepoSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.submission.*;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.DataSubmissionConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_IND_AFTER_SAVE_AS_DRAFT;

/**
 * @author YiMing
 * @version 2021/11/16 15:15
 **/
@Slf4j
@Delegator("ackOfTransferReceiptDelegator")
public class AckOfReceiptOfTransferDelegator {
    private final DataSubmissionClient dataSubmissionClient;
    private final BsbSubmissionCommon subCommon;
    private final FileRepoClient fileRepoClient;
    private final TransferClient transferClient;
    private final BsbFileClient bsbFileClient;
    public static final String KEY_FAC_ID = "facId";
    public static final String KEY_DATA_SUB_NO = "dataSubNo";
    private static final String ACK_TRANSFER_RECEIPT = "receiptSavedMap";
    private static final String ACK_TRANSFER_RECEIPT_DTO = "ackTransferReceiptDto";
    private static final String KEY_FACILITY_INFO = "facilityInfo";
    private static final String RECEIPT_SAVED  = "receiptSaved";
    private static final String DATA_SYNC_ERROR_MSG = "Fail to sync files to BE";
    public static final String KEY_DRAFT = "draft";


    public AckOfReceiptOfTransferDelegator(DataSubmissionClient dataSubmissionClient, BsbSubmissionCommon subCommon, FileRepoClient fileRepoClient, TransferClient transferClient, BsbFileClient bsbFileClient) {
        this.dataSubmissionClient = dataSubmissionClient;
        this.subCommon = subCommon;
        this.fileRepoClient = fileRepoClient;
        this.transferClient = transferClient;
        this.bsbFileClient = bsbFileClient;
    }

    public void start(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,RECEIPT_SAVED, null);
        ParamUtil.setSessionAttr(request,KEY_FACILITY_INFO, null);
        ParamUtil.setSessionAttr(request,ACK_TRANSFER_RECEIPT_DTO, null);
        ParamUtil.setSessionAttr(request,KEY_FAC_ID,null);
        request.getSession().removeAttribute(KEY_DATA_SUB_NO);
        AuditTrailHelper.auditFunction("Data Submission", "Data Submission");
    }

    public void prepareData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        AckTransferReceiptDto.AckTransferReceiptSaved receiptSaved  = (AckTransferReceiptDto.AckTransferReceiptSaved) ParamUtil.getSessionAttr(request,RECEIPT_SAVED);
        ParamUtil.setRequestAttr(request,RECEIPT_SAVED,receiptSaved);
        FacListDto.FacList facList = subCommon.getFacInfo(request);
        ParamUtil.setSessionAttr(request,KEY_FACILITY_INFO,facList);
        AckTransferReceiptDto dto= getTransferReceipt(request);
        Map<Integer,List<PrimaryDocDto.NewDocInfo>> keyNewDocDto  = dto.getExistNewDocInfoIndexMap();
        Map<Integer,List<PrimaryDocDto.DocRecordInfo>> keySavedDocDto = dto.getExistSavedDocInfoIndexMap();
        ParamUtil.setRequestAttr(request,"keyNewMap",keyNewDocDto);
        ParamUtil.setRequestAttr(request,"keySavedMap",keySavedDocDto);
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request,ValidationConstants.KEY_SHOW_ERROR_SWITCH);
        if(Boolean.TRUE.equals(needShowError)){
            ParamUtil.setRequestAttr(request,ValidationConstants.KEY_VALIDATION_ERRORS,dto.retrieveValidationResult());
        }
        ParamUtil.setSessionAttr(request,KEY_SUBMISSION_TYPE,KEY_DATA_SUBMISSION_ACKNOWLEDGEMENT_OF_RECEIPT_OF_TRANSFER);
    }

    public void init(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        DraftDto draft = (DraftDto) ParamUtil.getSessionAttr(request, KEY_DRAFT);
        String dataSubmissionType = (String) ParamUtil.getSessionAttr(request, KEY_SUBMISSION_TYPE);
        if (draft != null && dataSubmissionType != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                AckTransferReceiptDto.AckTransferReceiptSaved saved = mapper.readValue(draft.getDraftData(), AckTransferReceiptDto.AckTransferReceiptSaved.class);
                AckTransferReceiptDto dto = new AckTransferReceiptDto();
                dto.setAckTransferReceiptSaved(saved);
                dto.setSavedDocMap(sg.gov.moh.iais.egp.bsb.util.CollectionUtils.uniqueIndexMap(saved.getDocRecordInfos(), PrimaryDocDto.DocRecordInfo::getRepoId));
                ParamUtil.setSessionAttr(request,KEY_FAC_ID,saved.getFacId());
                ParamUtil.setSessionAttr(request,RECEIPT_SAVED,saved);
                ParamUtil.setSessionAttr(request,ACK_TRANSFER_RECEIPT_DTO,dto);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveAndPreConfirm(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        AckTransferReceiptDto ackTransferReceiptDto  = getTransferReceipt(request);
        AckTransferReceiptDto.AckTransferReceiptSaved saved = getTransferReceiptSaved(request);
        ackTransferReceiptDto.reqObjectMapping(request,saved);
        ackTransferReceiptDto.setAckTransferReceiptSaved(saved);
        doValidation(ackTransferReceiptDto,request);
        //show receipt of transfer info
        ParamUtil.setSessionAttr(request,ACK_TRANSFER_RECEIPT_DTO,ackTransferReceiptDto);
        ParamUtil.setSessionAttr(request,RECEIPT_SAVED,saved);
        Map<String, List<PrimaryDocDto.NewDocInfo>> newDocInfo = ackTransferReceiptDto.getNewDocTypeMap();
        Map<String,List<PrimaryDocDto.DocRecordInfo>> savedDocInfo = ackTransferReceiptDto.getSavedDocTypeMap();
        ParamUtil.setRequestAttr(request,"newDocMeta",newDocInfo);
        ParamUtil.setRequestAttr(request,"savedDocMeta",savedDocInfo);
    }

    public void save(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        AckTransferReceiptDto ackTransferReceiptDto  = getTransferReceipt(request);
        AckTransferReceiptDto.AckTransferReceiptSaved saved = getTransferReceiptSaved(request);

        Map<String,PrimaryDocDto.NewDocInfo> infoMap = ackTransferReceiptDto.getNewDocInfoMap();
        List<NewFileSyncDto> newFilesToSync = null;
        if(CollectionUtils.isEmpty(infoMap)){
            log.info("must be should you have committed you file");
        }else{
            MultipartFile[] files = infoMap.values().stream().map(PrimaryDocDto.NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
            List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
            newFilesToSync = ackTransferReceiptDto.newFileSaved(repoIds);
            saved.setDocRecordInfos(new ArrayList<>(ackTransferReceiptDto.getSavedDocMap().values()));
        }
        //complete simple save file to db and save data to dto for show in jsp
        String ensure = ParamUtil.getString(request,"ensure");
        saved.setEnsure(ensure);
        ackTransferReceiptDto.setAckTransferReceiptSaved(saved);
        transferClient.saveTransferReceipt(saved);
        try {
            // sync files to BE file-repo (save new added files, delete useless files)
            if (newFilesToSync!= null && !newFilesToSync.isEmpty()) {
                /* Ignore the failure of sync files currently.
                 * We should add a mechanism to retry synchronization of files in the future */
                FileRepoSyncDto syncDto = new FileRepoSyncDto();
                syncDto.setNewFiles(newFilesToSync);
                bsbFileClient.saveFiles(syncDto);
            }
        } catch (Exception e) {
            log.error("Fail to sync files to BE", e);
        }
        ParamUtil.setSessionAttr(request,ACK_TRANSFER_RECEIPT_DTO,ackTransferReceiptDto);
        ParamUtil.setSessionAttr(request,RECEIPT_SAVED,saved);
    }


    public void prepareSwitch1(BaseProcessClass bpc){
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>user");
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String actionType = request.getParameter("action_type");
        ParamUtil.setSessionAttr(bpc.request, "action_type", actionType);
    }


    public void preSelfFacSelect(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        selectOption(request);
    }

    public void preSwitch(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,KEY_FAC_ID,null);
        String facId = ParamUtil.getRequestString(request,"facSelect");
        facId = MaskUtil.unMaskValue("id",facId);
        ParamUtil.setSessionAttr(request,KEY_FAC_ID,facId);
        //get info of dataSubmission by submission no
        String dataSubNo = ParamUtil.getString(request,KEY_DATA_SUB_NO);
        ParamUtil.setSessionAttr(request,KEY_DATA_SUB_NO,dataSubNo);
        if(StringUtils.hasLength(dataSubNo)){
            HashMap<String,AckTransferReceiptDto.AckTransferReceiptSaved> savedHashMap =
                    ((HashMap<String,AckTransferReceiptDto.AckTransferReceiptSaved>)ParamUtil.getSessionAttr(request,ACK_TRANSFER_RECEIPT));
            if(!CollectionUtils.isEmpty(savedHashMap)){
                AckTransferReceiptDto.AckTransferReceiptSaved receiptSaved = savedHashMap.get(dataSubNo);
                ParamUtil.setSessionAttr(request,RECEIPT_SAVED,receiptSaved);
            }

        }
    }

    public void saveDraft(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AckTransferReceiptDto dto = getTransferReceipt(request);
        AckTransferReceiptDto.AckTransferReceiptSaved saved = getTransferReceiptSaved(request);
        saved.setDataSubmissionType(KEY_DATA_SUBMISSION_ACKNOWLEDGEMENT_OF_RECEIPT_OF_TRANSFER);
        dto.reqObjectMapping(request,saved);
        doDraftDocument(dto,saved);
        ParamUtil.setRequestAttr(request, KEY_IND_AFTER_SAVE_AS_DRAFT, Boolean.TRUE);
    }

    public void saveConfirmDraft(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AckTransferReceiptDto dto = getTransferReceipt(request);
        AckTransferReceiptDto.AckTransferReceiptSaved saved = getTransferReceiptSaved(request);
        saved.setDataSubmissionType(KEY_DATA_SUBMISSION_ACKNOWLEDGEMENT_OF_RECEIPT_OF_TRANSFER);
        doDraftDocument(dto,saved);
        ParamUtil.setRequestAttr(request, KEY_IND_AFTER_SAVE_AS_DRAFT, Boolean.TRUE);
    }


    public void doDraftDocument(AckTransferReceiptDto dto,AckTransferReceiptDto.AckTransferReceiptSaved saved){
        List<NewFileSyncDto> newFilesToSync = null;
        if (!dto.getNewDocInfoMap().isEmpty()) {
            //complete simple save file to db and save data to dto for show in jsp
            MultipartFile[] files = dto.getNewDocInfoMap().values().stream().map(PrimaryDocDto.NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
            List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
            newFilesToSync = dto.newFileSaved(repoIds);
            saved.setDocRecordInfos(new ArrayList<>(dto.getSavedDocMap().values()));
        }
        //save draft
        String draftAppNo = transferClient.saveTransferReceiptDraft(saved);
        saved.setDraftAppNo(draftAppNo);
        try {
            // delete docs
            List<String> toBeDeletedRepoIds = deleteUnwantedDoc(dto);
            // sync docs
            syncNewDocsAndDeleteFiles(newFilesToSync, toBeDeletedRepoIds);
        } catch (Exception e) {
            log.error(DATA_SYNC_ERROR_MSG, e);
        }
    }



    /**
     * This method is used to query all Facility info
     */
    private void selectOption(HttpServletRequest request) {
        ParamUtil.setSessionAttr(request,KEY_FAC_LISTS,null);
        FacListDto facListDto = dataSubmissionClient.queryAllApprovalFacList().getEntity();
        List<FacListDto.FacList> facLists = facListDto.getFacLists();
        //Removes the newly created object where is null
        facLists.remove(0);
        List<SelectOption> selectModel = new ArrayList<>(facLists.size()+1);
        selectModel.add(new SelectOption("","Please Select"));
        for (FacListDto.FacList fac : facLists) {
            selectModel.add(new SelectOption(fac.getFacId(), fac.getFacName()));
        }
        ParamUtil.setRequestAttr(request, KEY_FAC_SELECTION, selectModel);
        //Put in session called for later operations
        ParamUtil.setSessionAttr(request,KEY_FAC_LISTS,(Serializable) facLists);
    }

    /**
     * this method just used to charge if dto exist
     * @return TransferNotification
     * */
    private AckTransferReceiptDto.AckTransferReceiptSaved getTransferReceiptSaved(HttpServletRequest request){
        AckTransferReceiptDto.AckTransferReceiptSaved saved = (AckTransferReceiptDto.AckTransferReceiptSaved) ParamUtil.getSessionAttr(request,RECEIPT_SAVED);
        return saved == null?getDefaultDto():saved;
    }

    private AckTransferReceiptDto.AckTransferReceiptSaved getDefaultDto() {
        return new AckTransferReceiptDto.AckTransferReceiptSaved();
    }

    private AckTransferReceiptDto getTransferReceipt(HttpServletRequest request){
        AckTransferReceiptDto saved = (AckTransferReceiptDto) ParamUtil.getSessionAttr(request,ACK_TRANSFER_RECEIPT_DTO);
        return saved == null?getAckDefaultDto():saved;
    }

    private AckTransferReceiptDto getAckDefaultDto() {
        return new AckTransferReceiptDto();
    }

    /**
     * just a method to do simple valid,maybe update in the future
     * doValidation
     * */
    private void doValidation(AckTransferReceiptDto dto, HttpServletRequest request){
        if(dto.doValidation()){
            ParamUtil.setRequestAttr(request, ValidationConstants.IS_VALID,ValidationConstants.YES);
        }else{
            ParamUtil.setRequestAttr(request, ValidationConstants.IS_VALID,ValidationConstants.NO);
            ParamUtil.setRequestAttr(request,ValidationConstants.KEY_SHOW_ERROR_SWITCH,Boolean.TRUE);
        }
    }

    /**
     * Delete unwanted documents in FE file repo.
     * This method will remove the repoId from the toBeDeletedRepoIds set after a call of removal.
     *
     * @param dto document DTO have the specific structure
     * @return a list of repo IDs deleted in FE file repo
     */
    public List<String> deleteUnwantedDoc(AckTransferReceiptDto dto) {
        /* Ignore the failure when try to delete FE files because this is not a big issue.
         * The not deleted file won't be retrieved, so it's just a waste of disk space */
        List<String> toBeDeletedRepoIds = new ArrayList<>(dto.getToBeDeletedRepoIds());
        for (String id : toBeDeletedRepoIds) {
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
