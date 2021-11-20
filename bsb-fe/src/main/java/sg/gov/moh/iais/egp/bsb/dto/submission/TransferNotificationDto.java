package sg.gov.moh.iais.egp.bsb.dto.submission;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocMeta;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

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
        private PrimaryDocDto primaryDocDto;

        private List<PrimaryDocDto.DocRecordInfo> savedInfos;

        private List<DocMeta> docMetas;

        @JsonIgnore
        private List<PrimaryDocDto.NewDocInfo> newDocInfos;
        @JsonIgnore
        private String docType;
        @JsonIgnore
        private String repoIdNewString;

        public TransferNot() {
            this.docMetas = new ArrayList<>();
            this.savedInfos = new ArrayList<>();
            this.newDocInfos = new ArrayList<>();
        }

        public List<PrimaryDocDto.DocRecordInfo> getSavedInfos(){
            return new ArrayList<>(this.savedInfos);
        }

        public void setSavedInfos(List<PrimaryDocDto.DocRecordInfo> docRecordInfos){
            this.savedInfos = new ArrayList<>(docRecordInfos);
        }

        public void setDocMetas(List<DocMeta> docMetas){
            this.docMetas = new ArrayList<>(docMetas);
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

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    public TransferNotificationDto() {
        //Initialize the transferNotList
        transferNotList = new ArrayList<>();
        transferNotList.add(new TransferNot());
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
        private List<PrimaryDocDto.DocRecordInfo> savedInfos;
        private List<DocMeta> docMetas;
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

    /**
     * This method is for downloading and contains all file information sorted by tmpId
     *getAllNewDocInfo
     * @return Map<String,PrimaryDocDto.NewDocInfo>
     * */
    public Map<String,PrimaryDocDto.NewDocInfo> getAllNewDocInfo(){
        Map<String,PrimaryDocDto.NewDocInfo> newRecordMap = new HashMap<>();
        if(CollectionUtils.isEmpty(this.transferNotList)){
           List<PrimaryDocDto.NewDocInfo> newDocInfos = transferNotList.stream().flatMap(i->i.getNewDocInfos().stream()).collect(Collectors.toList());
           newRecordMap = newDocInfos.stream().collect(Collectors.toMap(PrimaryDocDto.NewDocInfo::getTmpId, Function.identity()));
        }
        return newRecordMap;
    }

    /**
     * this method is used to transfer useful data by feign
     * setTransferNotNeedR
     * @return TransferNotNeedR
     * */
    public TransferNotNeedR getTransferNotNeedR(){
        List<TransferNotNeed> transferNotNeeds = transferNotList.stream().map(t->{
            TransferNotNeed transferNotNeed = new TransferNotNeed();
            transferNotNeed.setScheduleType(t.getScheduleType());
            transferNotNeed.setBatCode(t.getBatCode());
            transferNotNeed.setTransferType(t.getTransferType());
            transferNotNeed.setTransferQty(t.getTransferQty());
            transferNotNeed.setBatQty(t.getBatQty());
            transferNotNeed.setMstUnit(t.getMstUnit());
            transferNotNeed.setDocMetas(t.getDocMetas());
            transferNotNeed.setSavedInfos(t.getSavedInfos());
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
        return transferNotNeedR;
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
        List<DocMeta> docMetas = this.transferNotList.stream().flatMap(i->i.getDocMetas().stream()).collect(Collectors.toList());
        return sg.gov.moh.iais.egp.bsb.util.CollectionUtils.groupCollectionToMap(docMetas,DocMeta::getDocType);
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
            primaryDocDto.reqObjMapping(request,getDocType(scheduleType),String.valueOf(idx));
            transferNot.setPrimaryDocDto(primaryDocDto);
            transferNot.setDocType(getDocType(scheduleType));

            //joint repoId exist
            String newRepoId = String.join(",", primaryDocDto.getNewDocMap().keySet());
            transferNot.setRepoIdNewString(newRepoId);

            //set newDocFiles
            transferNot.setNewDocInfos(primaryDocDto.getNewDocTypeList());

            //set need Validation value
            transferNot.setDocMetas(primaryDocDto.doValidation());
            addTransferNotList(transferNot);
        }
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
