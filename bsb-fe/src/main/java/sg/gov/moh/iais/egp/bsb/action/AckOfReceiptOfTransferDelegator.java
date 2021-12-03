package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sg.gov.moh.iais.egp.bsb.client.BsbFileClient;
import sg.gov.moh.iais.egp.bsb.client.DataSubmissionClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.client.TransferClient;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.file.FileRepoSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.submission.AckTransferReceiptDto;
import sg.gov.moh.iais.egp.bsb.dto.submission.FacListDto;
import sg.gov.moh.iais.egp.bsb.dto.submission.PrimaryDocDto;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.DataSubmissionConstants.*;

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
        AuditTrailHelper.auditFunction("Data Submission", "Data Submission");
    }

    public void prepareData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        AckTransferReceiptDto.AckTransferReceiptSaved receiptSaved  = (AckTransferReceiptDto.AckTransferReceiptSaved) ParamUtil.getSessionAttr(request,RECEIPT_SAVED);
        ParamUtil.setRequestAttr(request,RECEIPT_SAVED,receiptSaved);
        FacListDto.FacList facList = subCommon.getFacInfo(request);
        ParamUtil.setSessionAttr(request,KEY_FACILITY_INFO,facList);
        AckTransferReceiptDto dto= getTransferReceipt(request);
        Map<String,List<PrimaryDocDto.NewDocInfo>> keyNewDocDto  = dto.getExistNewDocInfoIndexMap();
        ParamUtil.setRequestAttr(request,"keyMap",keyNewDocDto);
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request,ValidationConstants.KEY_SHOW_ERROR_SWITCH);
        if(Boolean.TRUE.equals(needShowError)){
            ParamUtil.setRequestAttr(request,ValidationConstants.KEY_VALIDATION_ERRORS,dto.retrieveValidationResult());
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
        ParamUtil.setRequestAttr(request,"docMeta",newDocInfo);
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

    public void saveDraft(BaseProcessClass bpc){
        // this model will do in the future
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
        if(StringUtils.hasLength(dataSubNo)){
            HashMap<String,AckTransferReceiptDto.AckTransferReceiptSaved> savedHashMap =
                    ((HashMap<String,AckTransferReceiptDto.AckTransferReceiptSaved>)ParamUtil.getSessionAttr(request,ACK_TRANSFER_RECEIPT));
            AckTransferReceiptDto.AckTransferReceiptSaved receiptSaved = savedHashMap.get(dataSubNo);
            ParamUtil.setSessionAttr(request,RECEIPT_SAVED,receiptSaved);
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
        List<SelectOption> selectModel = new ArrayList<>(facLists.size());
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
}
