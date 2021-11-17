package sg.gov.moh.iais.egp.bsb.dto.submission;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sg.gov.moh.iais.egp.bsb.common.multipart.ByteArrayMultipartFile;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.LogUtil;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;
import sop.servlet.webflow.HttpHandler;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author YiMing
 * @version 2021/11/13 15:58
 **/
@Slf4j
public class AckTransferReceiptDto implements Serializable {

    @Data
    public static class AckReceiptBat implements Serializable{
        private String scheduleType;
        private String batName;
        private String bioId;
        private String transferredType;
        private String transferredUnit;
        private String transferredQty;
        private String receivedQty;
        private String receivedBatQty;
        private String receivedUnit;
        private String discrepancyReason;
    }

    @Data
    public static class AckTransferReceiptSaved implements Serializable {
        private List<AckReceiptBat>  ackReceiptBats;
        private List<PrimaryDocDto.DocRecordInfo> docRecordInfos;
        private String facId;
        private String receivingFacId;
        private String receivingFacName;
        private String actualReceiptDate;
        private String actualReceiptTime;
        private String remark;
        private String ensure;

        public void clearAckReceiptBatList(){
            this.ackReceiptBats.clear();
        }

        public void addAckReceiptBatList(AckReceiptBat ackReceiptBat){
            this.ackReceiptBats.add(ackReceiptBat);
        }
    }




    @Data
    public static class AckTransferReceiptMeta implements Serializable {
        private List<AckReceiptBat>  ackReceiptBats;
        private List<PrimaryDocDto.DocMeta> metas;
        private String actualReceiptDate;
        private String actualReceiptTime;
    }

    //a Map used to saved new file info which file has not saved into database
    private Map<String,PrimaryDocDto.NewDocInfo> newDocInfoMap;

    private Map<String,PrimaryDocDto.DocRecordInfo> savedDocMap;

    //used to get AckTransferReceiptSaved and DataSubmission No
    private Map<String,AckTransferReceiptSaved> receiptSavedMap;

    private AckTransferReceiptSaved ackTransferReceiptSaved;

    private AckTransferReceiptMeta ackTransferReceiptMeta;


    public AckTransferReceiptDto() {
        this.newDocInfoMap = new LinkedHashMap<>();
        this.savedDocMap = new LinkedHashMap<>();
    }

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    public Map<String, AckTransferReceiptSaved> getReceiptSavedMap() {
        return receiptSavedMap;
    }

    public void setReceiptSavedMap(Map<String, AckTransferReceiptSaved> receiptSavedMap) {
        this.receiptSavedMap = receiptSavedMap;
    }

    public Map<String, PrimaryDocDto.NewDocInfo> getNewDocInfoMap() {
        return newDocInfoMap;
    }

    public void setNewDocInfoMap(Map<String, PrimaryDocDto.NewDocInfo> newDocInfoMap) {
        this.newDocInfoMap = newDocInfoMap;
    }

    public AckTransferReceiptSaved getAckTransferReceiptSaved() {
        return ackTransferReceiptSaved;
    }

    public void setAckTransferReceiptSaved(AckTransferReceiptSaved ackTransferReceiptSaved) {
        this.ackTransferReceiptSaved = ackTransferReceiptSaved;
    }

    public AckTransferReceiptMeta getAckTransferReceiptMeta() {
        return ackTransferReceiptMeta;
    }

    public void setAckTransferReceiptMeta(AckTransferReceiptMeta ackTransferReceiptMeta) {
        this.ackTransferReceiptMeta = ackTransferReceiptMeta;
    }

    public Map<String, PrimaryDocDto.DocRecordInfo> getSavedDocMap() {
        return savedDocMap;
    }

    public void setSavedDocMap(Map<String, PrimaryDocDto.DocRecordInfo> savedDocMap) {
        this.savedDocMap = savedDocMap;
    }

    /**
     * get a structure used to display new selected docs
     * these docs have not been saved into DB, if user wants to show it
     * @return a map, the key is the doc index, the value is the new doc info list
     */
    public Map<String,List<PrimaryDocDto.NewDocInfo>> getExistNewDocInfoIndexMap(){
        if(CollectionUtils.isEmpty(this.newDocInfoMap)){
            return Collections.emptyMap();
        }
        return sg.gov.moh.iais.egp.bsb.util.CollectionUtils.groupCollectionToMap(this.newDocInfoMap.values(), PrimaryDocDto.NewDocInfo::getIndex);
    }

    /**
     * get a structure used to display new selected docs
     * these docs have not been saved into DB, if user wants to download it, we send the data from current data structure
     * @return a map, the key is the doc type, the value is the new doc info list
     */
    public Map<String, List<PrimaryDocDto.NewDocInfo>> getNewDocTypeMap() {
        return sg.gov.moh.iais.egp.bsb.util.CollectionUtils.groupCollectionToMap(this.newDocInfoMap.values(), PrimaryDocDto.NewDocInfo::getDocType);
    }

    /**
     * this is a method to get all file list of masked tmpId
     * */
    public String getRepoIdNewString(){
        if(CollectionUtils.isEmpty(this.newDocInfoMap.keySet())){
            log.info("has no new file");
            return null;
        }
        Set<String> ids = this.newDocInfoMap.keySet();
        return ids.stream().map(i-> MaskUtil.maskValue("file",i)).collect(Collectors.joining(","));
    }

    /**
     * This file is called when new uploaded files are saved and we get the repo Ids
     * ATTENTION!!!
     * This method is dangerous! The relationship between the ids and the files in this dto is fragile!
     * We rely on the order is not changed! So we use a LinkedHashMap to save our data.
     */
    public void newFileSaved(List<String> repoIds) {
        Iterator<String> repoIdIt = repoIds.iterator();
        Iterator<PrimaryDocDto.NewDocInfo> newDocIt = newDocInfoMap.values().iterator();
        while (repoIdIt.hasNext() && newDocIt.hasNext()) {
            String repoId = repoIdIt.next();
            PrimaryDocDto.NewDocInfo newDocInfo = newDocIt.next();
            PrimaryDocDto.DocRecordInfo docRecordInfo = new PrimaryDocDto.DocRecordInfo();
            docRecordInfo.setDocType(newDocInfo.getDocType());
            docRecordInfo.setFilename(newDocInfo.getFilename());
            docRecordInfo.setSize(newDocInfo.getSize());
            docRecordInfo.setRepoId(repoId);
            docRecordInfo.setSubmitBy(newDocInfo.getSubmitBy());
            docRecordInfo.setSubmitDate(newDocInfo.getSubmitDate());
            savedDocMap.put(repoId, docRecordInfo);
        }
    }



    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("transferFeignClient", "validateTransferReceipt", new Object[]{this.ackTransferReceiptMeta});
        return validationResultDto.isPass();
    }

    public String retrieveValidationResult() {
        if (this.validationResultDto == null) {
            throw new IllegalStateException("This DTO is not validated");
        }
        return this.validationResultDto.toErrorMsg();
    }

    public void clearValidationResult() {
        this.validationResultDto = null;
    }

    private static final String SEPARATOR                       = "--v--";
    private static final String KEY_PREFIX_RECEIVED_QTY         = "receivedQty";
    private static final String KEY_PREFIX_RECEIVED_BAT_QTY     = "receivedBatQty";
    private static final String KEY_PREFIX_RECEIVED_UNIT        = "receivedUnit";
    private static final String KEY_PREFIX_DISCREPANCY_REASON   = "discrepancyReason";
    private static final String KEY_DOC_TYPE_INVENTORY_TOXIN    ="ityToxin";
    private static final String KEY_DOC_TYPE_INVENTORY_BAT      ="ityBat";
    private static final String KEY_ACTUAL_RECEIPT_DATE         ="actualReceiptDate";
    private static final String KEY_ACTUAL_RECEIPT_TIME         ="actualReceiptTime";
    private static final String KEY_REMARK                      = "remark";
    private static final String KEY_DELETED_NEW_FILES           = "deleteNewFiles";
    private static final String MASK_PARAM                      = "file";
    public void reqObjectMapping(HttpServletRequest request,AckTransferReceiptSaved receiptSaved) {
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        LoginContext loginContext = (LoginContext) com.ecquaria.cloud.moh.iais.common.utils.ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<AckReceiptBat> bats = receiptSaved.getAckReceiptBats();
        if (!CollectionUtils.isEmpty(bats)) {
            for (int i = 0; i < bats.size(); i++) {
                AckReceiptBat bat = bats.get(i);
                bat.setReceivedQty(ParamUtil.getString(request, KEY_PREFIX_RECEIVED_QTY + SEPARATOR + i));
                bat.setReceivedBatQty(ParamUtil.getString(request,KEY_PREFIX_RECEIVED_BAT_QTY +SEPARATOR + i));
                bat.setReceivedUnit(ParamUtil.getString(request, KEY_PREFIX_RECEIVED_UNIT + SEPARATOR + i));
                bat.setDiscrepancyReason(ParamUtil.getString(request, KEY_PREFIX_DISCREPANCY_REASON + SEPARATOR + i));
                //have one bat start one reqDocMapping method
                reqDocMapping(mulReq,loginContext,getDocType(bat.getScheduleType()),String.valueOf(i));
            }
        }
        String actualReceiptDate = ParamUtil.getString(request,KEY_ACTUAL_RECEIPT_DATE);
        String actualReceiptTime = ParamUtil.getString(request,KEY_ACTUAL_RECEIPT_TIME);
        receiptSaved.setActualReceiptTime(actualReceiptTime);
        receiptSaved.setActualReceiptDate(actualReceiptDate);
        receiptSaved.setRemark(ParamUtil.getString(request,KEY_REMARK));

        //prepare data for validation
        AckTransferReceiptMeta meta = new AckTransferReceiptMeta();
        meta.setAckReceiptBats(bats);
        meta.setActualReceiptDate(actualReceiptDate);
        meta.setActualReceiptTime(actualReceiptTime);
        List<PrimaryDocDto.DocMeta> metaDtoList = new ArrayList<>(this.newDocInfoMap.size());
        this.newDocInfoMap.values().forEach(i -> {
            PrimaryDocDto.DocMeta docMeta = new PrimaryDocDto.DocMeta(i.getTmpId(), i.getDocType(), i.getFilename(), i.getSize(), "dataSub");
            metaDtoList.add(docMeta);
        });
        meta.setMetas(metaDtoList);
        this.ackTransferReceiptMeta = meta;
    }

    // a method to fill doc
    private void reqDocMapping (MultipartHttpServletRequest mulReq,LoginContext loginContext, String docType, String amt){
        //delete new files
        deleteNewFiles(mulReq);

        // read new uploaded files
        Iterator<String> inputNameIt = mulReq.getFileNames();
        Date currentDate = new Date();
        while (inputNameIt.hasNext()) {
            String inputName = inputNameIt.next();
            String index = inputName.split(SEPARATOR)[1];
            if (StringUtils.hasLength(inputName) && amt.equals(index)) {
                List<MultipartFile> files = mulReq.getFiles(inputName);
                for (MultipartFile f : files) {
                    if (log.isInfoEnabled()) {
                        log.info("input name: {}; filename: {}", LogUtil.escapeCrlf(inputName), LogUtil.escapeCrlf(f.getOriginalFilename()));
                    }
                    if (f.isEmpty()) {
                        log.warn("File is empty, ignore it");
                    } else {
                        PrimaryDocDto.NewDocInfo newDocInfo = new PrimaryDocDto.NewDocInfo();
                        String tmpId = inputName + f.getSize() + System.nanoTime();
                        newDocInfo.setIndex(index);
                        newDocInfo.setTmpId(tmpId);
                        newDocInfo.setDocType(docType);
                        newDocInfo.setFilename(f.getOriginalFilename());
                        newDocInfo.setSize(f.getSize());
                        newDocInfo.setSubmitDate(currentDate);
                        newDocInfo.setSubmitBy(loginContext.getUserId());

                        byte[] bytes = new byte[0];
                        try {
                            bytes = f.getBytes();
                        } catch (IOException e) {
                            log.warn("Fail to read bytes for file {}, tmpId {}", f.getOriginalFilename(), tmpId);
                        }
                        ByteArrayMultipartFile multipartFile = new ByteArrayMultipartFile(f.getName(), f.getOriginalFilename(), f.getContentType(), bytes);
                        newDocInfo.setMultipartFile(multipartFile);
                        this.newDocInfoMap.put(tmpId, newDocInfo);
                    }
                }
            }
        }
    }


    //this method is used to delete new saved file
    public void deleteNewFiles (MultipartHttpServletRequest mulReq){
        String deleteNewFilesString = ParamUtil.getString(mulReq, KEY_DELETED_NEW_FILES);
        if (log.isInfoEnabled()) {
            log.info("deleteNewFilesString: {}", LogUtil.escapeCrlf(deleteNewFilesString));
        }
        if (StringUtils.hasLength(deleteNewFilesString)) {
            List<String> deleteFileTmpIds = Arrays.stream(deleteNewFilesString.split(","))
                    .map(f -> MaskUtil.unMaskValue(MASK_PARAM, f))
                    .collect(Collectors.toList());
            deleteFileTmpIds.forEach(this.newDocInfoMap::remove);
        }
    }

    //this method is used to get docType by scheduleType
    private String getDocType(String scheduleType){
        String docType = "";
        if(StringUtils.hasLength(scheduleType)){
            switch (scheduleType){
                case MasterCodeConstants.FIRST_SCHEDULE_PART_I:
                case MasterCodeConstants.FIRST_SCHEDULE_PART_II:
                case MasterCodeConstants.SECOND_SCHEDULE:
                case MasterCodeConstants.THIRD_SCHEDULE:
                case MasterCodeConstants.FOURTH_SCHEDULE:
                    docType = KEY_DOC_TYPE_INVENTORY_BAT;
                    break;
                case MasterCodeConstants.FIFTH_SCHEDULE:
                    docType = KEY_DOC_TYPE_INVENTORY_TOXIN;
                    break;
                default:
                    break;
            }
        }
        return docType;
    }


}
