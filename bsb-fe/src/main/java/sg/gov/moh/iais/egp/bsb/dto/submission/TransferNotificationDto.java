package sg.gov.moh.iais.egp.bsb.dto.submission;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocMeta;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;
import sop.servlet.webflow.HttpHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author YiMing
 * @version 2021/11/2 14:21
 **/

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransferNotificationDto implements Serializable {

    @Data
    public static class TransferNot implements Serializable{
        private String scheduleType;
        private String batCode;
        private String transferType;
        private String batQty;
        private String transferQty;
        private String mstUnit;

        @JsonIgnore
        private List<PrimaryDocDto.NewDocInfo> newDocInfos;
        @JsonIgnore
        private String docType;
        @JsonIgnore
        private String repoIdNewString;

        public TransferNot() {
            this.newDocInfos = new ArrayList<>();
        }

        public List<PrimaryDocDto.NewDocInfo> getNewInfos(){
            return new ArrayList<>(this.newDocInfos);
        }

        public void setNewInfos(List<PrimaryDocDto.NewDocInfo> newDocInfos){
            this.newDocInfos = new ArrayList<>(newDocInfos);
        }
    }
    private String facId;
    private String receiveFacility;
    private String expectedTfDate;
    private String expArrivalTime;
    private String providerName;
    private String remarks;
    private String ensure;

    private List<TransferNot> transferNotList;
    private List<PrimaryDocDto.NewDocInfo> otherNewInfos;
    private Map<String, PrimaryDocDto.NewDocInfo> allNewDocInfos;
    private Map<String,PrimaryDocDto.DocRecordInfo> savedDocInfos;
    private List<DocMeta> docMetaInfos;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    public TransferNotificationDto() {
        //Initialize the transferNotList
        transferNotList = new ArrayList<>();
        transferNotList.add(new TransferNot());

        docMetaInfos = new ArrayList<>();

        otherNewInfos = new ArrayList<>();

        allNewDocInfos = new LinkedHashMap<>();
        savedDocInfos = new LinkedHashMap<>();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class  TransferNotNeed{
        private String scheduleType;
        private String batCode;
        private String transferType;
        private String batQty;
        private String transferQty;
        private String mstUnit;
    }

    @Data
    @NoArgsConstructor
    public static class TransferNotNeedR{
        private List<TransferNotNeed> needList;
        private String facId;
        private String receiveFacility;
        private String expectedTfDate;
        private String expArrivalTime;
        private String providerName;
        private String remarks;
        private String ensure;
        private List<PrimaryDocDto.DocRecordInfo> docInfos;
        private List<DocMeta> docMetas;
    }



    public String getExpectedTfDate() {
        return expectedTfDate;
    }

    public void setExpectedTfDate(String expectedTfDate) {
        this.expectedTfDate = expectedTfDate;
    }

    public String getExpArrivalTime() {
        return expArrivalTime;
    }

    public void setExpArrivalTime(String expArrivalTime) {
        this.expArrivalTime = expArrivalTime;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getReceiveFacility() {
        return receiveFacility;
    }

    public void setReceiveFacility(String receiveFacility) {
        this.receiveFacility = receiveFacility;
    }

    public String getFacId() {
        return facId;
    }

    public void setFacId(String facId) {
        this.facId = facId;
    }

    public String getEnsure() {
        return ensure;
    }

    public void setEnsure(String ensure) {
        this.ensure = ensure;
    }

    //transferNotList getter setter
    public List<TransferNot> getTransferNotList() { return new ArrayList<>(transferNotList);
    }

    public void clearTransferNotList(){
        this.transferNotList.clear();
    }

    public void addTransferNotList(TransferNot transferNot){
        this.transferNotList.add(transferNot);
    }

    public void setTransferNotList(List<TransferNot> transferLists) {
        this.transferNotList = new ArrayList<>(transferLists);
    }

    public List<PrimaryDocDto.NewDocInfo> getOtherNewInfos() {
        return new ArrayList<>(this.otherNewInfos);
    }

    public void setOtherNewInfos(List<PrimaryDocDto.NewDocInfo> otherNewInfos) {
        this.otherNewInfos = new ArrayList<>(otherNewInfos);
    }

    public Map<String, PrimaryDocDto.NewDocInfo> getAllNewDocInfos() {
        return allNewDocInfos;
    }

    public void setAllNewDocInfos(Map<String, PrimaryDocDto.NewDocInfo> allNewDocInfos) {
        this.allNewDocInfos = allNewDocInfos;
    }

    public List<DocMeta> getDocMetaInfos() {
        return docMetaInfos;
    }

    public void setDocMetaInfos(List<DocMeta> docMetaInfos) {
        this.docMetaInfos = docMetaInfos;
    }

    public void addDocMetaInfos(DocMeta docMeta){
        this.docMetaInfos.add(docMeta);
    }

    public Map<String, PrimaryDocDto.DocRecordInfo> getSavedDocInfos() {
        return savedDocInfos;
    }

    public void setSavedDocInfos(Map<String, PrimaryDocDto.DocRecordInfo> savedDocInfos) {
        this.savedDocInfos = savedDocInfos;
    }

    /**
     * This method is for downloading and contains all file information sorted by tmpId
     *getAllNewDocInfo
     * */
    public void fillAllNewDocInfo(){
        if(!CollectionUtils.isEmpty(this.transferNotList)){
           List<PrimaryDocDto.NewDocInfo> newDocInfos = transferNotList.stream().flatMap(i->i.getNewDocInfos().stream()).collect(Collectors.toList());
           newDocInfos.addAll(this.otherNewInfos);
           this.allNewDocInfos = newDocInfos.stream().collect(Collectors.toMap(PrimaryDocDto.NewDocInfo::getTmpId, Function.identity()));
        }
    }

    public void getDocMetaInfoFromNew(){
        this.allNewDocInfos.values().forEach(i -> {
            DocMeta docMeta = new DocMeta(i.getTmpId(), i.getDocType(), i.getFilename(), i.getSize(), "dataSub");
            addDocMetaInfos(docMeta);
        });
    }

    /**
     * this method is used to transfer useful data by feign
     * setTransferNotNeedR
     * @return TransferNotNeedR
     * */
    public TransferNotNeedR getTransferNotNeedR(){
        //get doc need validation
        List<TransferNotNeed> transferNotNeeds = transferNotList.stream().map(t->{
            TransferNotNeed transferNotNeed = new TransferNotNeed();
            transferNotNeed.setScheduleType(t.getScheduleType());
            transferNotNeed.setBatCode(t.getBatCode());
            transferNotNeed.setTransferType(t.getTransferType());
            transferNotNeed.setTransferQty(t.getTransferQty());
            transferNotNeed.setBatQty(t.getBatQty());
            transferNotNeed.setMstUnit(t.getMstUnit());
            return transferNotNeed; }).collect(Collectors.toList());
        TransferNotNeedR transferNotNeedR = new TransferNotNeedR();
        transferNotNeedR.setNeedList(transferNotNeeds);
        transferNotNeedR.setEnsure(this.ensure);
        transferNotNeedR.setRemarks(this.remarks);
        transferNotNeedR.setFacId(this.facId);
        transferNotNeedR.setExpectedTfDate(this.expectedTfDate);
        transferNotNeedR.setProviderName(this.providerName);
        transferNotNeedR.setExpArrivalTime(this.expArrivalTime);
        transferNotNeedR.setReceiveFacility(this.receiveFacility);
        transferNotNeedR.setDocInfos(new ArrayList<>(savedDocInfos.values()));
        transferNotNeedR.setDocMetas(this.docMetaInfos);
        return transferNotNeedR;
    }

    /**
     * This method will put new added files to the important data structure which is used to update the FacilityDoc.
     * This file is called when new uploaded files are saved and we get the repo Ids.
     * ATTENTION!!!
     * This method is dangerous! The relationship between the ids and the files in this dto is fragile!
     * We rely on the order is not changed! So we use a LinkedHashMap to save our data.
     * <p>
     * This method will generate id-bytes pairs at the same time, the result will be used to sync files to BE.
     * @return a list of file data to be synchronized to BE
     */
    public List<NewFileSyncDto> newFileSaved(List<String> repoIds) {
        Iterator<String> repoIdIt = repoIds.iterator();
        Iterator<PrimaryDocDto.NewDocInfo> newDocIt = allNewDocInfos.values().iterator();

        List<NewFileSyncDto> newFileSyncDtoList = new ArrayList<>(repoIds.size());
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
            savedDocInfos.put(repoId, docRecordInfo);

            NewFileSyncDto newFileSyncDto = new NewFileSyncDto();
            newFileSyncDto.setId(repoId);
            newFileSyncDto.setData(newDocInfo.getMultipartFile().getBytes());
            newFileSyncDtoList.add(newFileSyncDto);
        }
        return newFileSyncDtoList;
    }

    //------------------------------------------Validation---------------------------------------------

    public boolean doValidation() {
        TransferNotNeedR transferNotNeedR = getTransferNotNeedR();
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("transferFeignClient", "validateTransferNot", new Object[]{transferNotNeedR});
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


    /**
     * This method is for JSP shows and contains all file information sorted by type
     * getAllDocMetaByDocType
     * @return Map<String,List<DocMeta>>
     * */
    public Map<String,List<DocMeta>> getAllDocMetaByDocType(){
        return sg.gov.moh.iais.egp.bsb.util.CollectionUtils.groupCollectionToMap(docMetaInfos,DocMeta::getDocType);
    }




    //----------------------request-->object----------------------------------
    private static final String SEPARATOR                   = "--v--";
    private static final String KEY_SECTION_IDXES           = "sectionIdx";
    private static final String KEY_PREFIX_SCHEDULE_TYPE    = "scheduleType";
    private static final String KEY_PREFIX_BAT_CODE         = "batCode";
    private static final String KEY_PREFIX_BAT_QTY          = "transferType";
    private static final String KEY_PREFIX_TRANSFER_TYPE    = "batQty";
    private static final String KEY_PREFIX_TRANSFER_QTY     = "transferQty";
    private static final String KEY_PREFIX_MEASUREMENT_UNIT = "mstUnit";
    private static final String KEY_EXPECTED_TRANSFER_DATE  = "expectedTfDate";
    private static final String KEY_RECEIVE_FACILITY        = "receiveFacility";
    private static final String KEY_EXPECTED_ARRIVAL_TIME   = "expArrivalTime";
    private static final String KEY_PROVIDER_NAME           = "providerName";
    private static final String KEY_REMARK                  = "remarks";
    private static final String KEY_DOC_TYPE_INVENTORY_TOXIN="ityToxin";
    private static final String KEY_DOC_TYPE_INVENTORY_BAT  ="ityBat";



    /**
     * reqObjectMapping
     * get value from request
     * */
    public void reqObjectMapping(HttpServletRequest request){
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String idxes = ParamUtil.getString(request, KEY_SECTION_IDXES);
        clearTransferNotList();
        String[] idxArr = idxes.trim().split(" +");
        for (String idx : idxArr) {
            TransferNot transferNot = new TransferNot();
            String scheduleType = ParamUtil.getString(request,KEY_PREFIX_SCHEDULE_TYPE+SEPARATOR+idx);
            transferNot.setScheduleType(scheduleType);
            transferNot.setBatCode(ParamUtil.getString(request,KEY_PREFIX_BAT_CODE+SEPARATOR+idx));
            transferNot.setTransferType(ParamUtil.getString(request,KEY_PREFIX_BAT_QTY+SEPARATOR+idx));
            transferNot.setBatQty(ParamUtil.getString(request,KEY_PREFIX_TRANSFER_TYPE +SEPARATOR+idx));
            transferNot.setTransferQty(ParamUtil.getString(request,KEY_PREFIX_TRANSFER_QTY+SEPARATOR+idx));
            transferNot.setMstUnit(ParamUtil.getString(request,KEY_PREFIX_MEASUREMENT_UNIT+SEPARATOR+idx));
            PrimaryDocDto primaryDocDto = new PrimaryDocDto();
            primaryDocDto.reqObjMapping(mulReq,request,getDocType(scheduleType),String.valueOf(idx));
            transferNot.setDocType(getDocType(scheduleType));

            //joint repoId exist
            String newRepoId = String.join(",", primaryDocDto.getNewDocMap().keySet());
            transferNot.setRepoIdNewString(newRepoId);

            //set newDocFiles
            transferNot.setNewDocInfos(primaryDocDto.getNewDocTypeList());

            //set need Validation value
            addTransferNotList(transferNot);
        }
        PrimaryDocDto primaryDocDto = new PrimaryDocDto();
        primaryDocDto.reqOtherMapping(mulReq,request,"others");
        this.setOtherNewInfos(primaryDocDto.getNewDocTypeList());
        //get all new doc
        fillAllNewDocInfo();
        //get all
        getDocMetaInfoFromNew();
        this.setExpectedTfDate(ParamUtil.getString(request,KEY_EXPECTED_TRANSFER_DATE));
        this.setExpArrivalTime(ParamUtil.getString(request,KEY_EXPECTED_ARRIVAL_TIME));
        this.setProviderName(ParamUtil.getString(request,KEY_PROVIDER_NAME));
        this.setRemarks(ParamUtil.getString(request,KEY_REMARK));
        this.setReceiveFacility(ParamUtil.getString(request,KEY_RECEIVE_FACILITY));
    }

    public String getDocType(String scheduleType){
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
