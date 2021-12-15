package sg.gov.moh.iais.egp.bsb.dto.submission;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
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

import static sg.gov.moh.iais.egp.bsb.constant.DataSubmissionConstants.*;

/**
 * @author Zhu Tangtang
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConsumeNotificationDto implements Serializable {
    @Data
    public static class ConsumptionNot implements Serializable {
        private String scheduleType;
        private String bat;
        private String consumeType;
        private String consumedQty;
        private String meaUnit;

        @JsonIgnore
        private List<PrimaryDocDto.NewDocInfo> newDocInfos;
        @JsonIgnore
        private String docType;
        @JsonIgnore
        private String repoIdNewString;

        public ConsumptionNot() {
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
    private String remarks;
    private String ensure;

    private List<ConsumptionNot> consumptionNotList;
    private List<PrimaryDocDto.NewDocInfo> otherNewInfos;
    private Map<String, PrimaryDocDto.NewDocInfo> allNewDocInfos;
    private Map<String,PrimaryDocDto.DocRecordInfo> savedDocInfos;
    private List<DocMeta> docMetaInfos;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    public ConsumeNotificationDto() {
        consumptionNotList = new ArrayList<>();
        consumptionNotList.add(new ConsumptionNot());
        docMetaInfos = new ArrayList<>();
        otherNewInfos = new ArrayList<>();
        allNewDocInfos = new LinkedHashMap<>();
        savedDocInfos = new LinkedHashMap<>();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConsumeNotNeed{
        private String scheduleType;
        private String bat;
        private String consumeType;
        private String consumedQty;
        private String meaUnit;
    }

    @Data
    @NoArgsConstructor
    public static class ConsumeNotNeedR{
        private List<ConsumeNotNeed> needList;
        private String facId;
        private String remarks;
        private String ensure;
        private List<PrimaryDocDto.DocRecordInfo> docInfos;
        private List<DocMeta> docMetas;
    }

    public String getFacId() {
        return facId;
    }

    public void setFacId(String facId) {
        this.facId = facId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getEnsure() {
        return ensure;
    }

    public void setEnsure(String ensure) {
        this.ensure = ensure;
    }

    public List<ConsumptionNot> getConsumptionNotList() {
        return consumptionNotList;
    }

    public void clearConsumptionNotList(){
        this.consumptionNotList.clear();
    }

    public void addConsumptionNotList(ConsumptionNot consumptionNot){
        this.consumptionNotList.add(consumptionNot);
    }

    public void setConsumptionNotList(List<ConsumptionNot> consumptionNotList) {
        this.consumptionNotList = consumptionNotList;
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
        if(!CollectionUtils.isEmpty(this.consumptionNotList)){
            List<PrimaryDocDto.NewDocInfo> newDocInfos = consumptionNotList.stream().flatMap(i->i.getNewDocInfos().stream()).collect(Collectors.toList());
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
     * this method is used to Consume useful data by feign
     * setConsumeNotNeedR
     * @return ConsumeNotNeedR
     * */
    public ConsumeNotNeedR getConsumeNotNeedR(){
        //get doc need validation
        List<ConsumeNotNeed> consumeNotNeeds = consumptionNotList.stream().map(t->{
            ConsumeNotNeed consumeNotNeed = new ConsumeNotNeed();
            consumeNotNeed.setScheduleType(t.getScheduleType());
            consumeNotNeed.setConsumeType(t.getConsumeType());
            consumeNotNeed.setConsumedQty(t.getConsumedQty());
            consumeNotNeed.setBat(t.getBat());
            consumeNotNeed.setMeaUnit(t.getMeaUnit());
            return consumeNotNeed; }).collect(Collectors.toList());
        ConsumeNotNeedR consumeNotNeedR = new ConsumeNotNeedR();
        consumeNotNeedR.setNeedList(consumeNotNeeds);
        consumeNotNeedR.setEnsure(this.ensure);
        consumeNotNeedR.setRemarks(this.remarks);
        consumeNotNeedR.setFacId(this.facId);
        consumeNotNeedR.setDocInfos(new ArrayList<>(savedDocInfos.values()));
        consumeNotNeedR.setDocMetas(this.docMetaInfos);
        return consumeNotNeedR;
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
        ConsumeNotNeedR consumeNotNeedR = getConsumeNotNeedR();
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("dataSubmissionFeignClient", "validateConsumeNot", new Object[]{consumeNotNeedR});
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

    /**
     * reqObjectMapping
     * get value from request
     * */
    public void reqObjectMapping(HttpServletRequest request){
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String idxes = ParamUtil.getString(request, KEY_SECTION_IDXES);
        PrimaryDocDto.deleteNewFiles(mulReq,this.allNewDocInfos);
        clearConsumptionNotList();
        String[] idxArr = idxes.trim().split(" +");
        for (String idx : idxArr) {
            ConsumptionNot consumptionNot = new ConsumptionNot();
            String scheduleType = ParamUtil.getString(request, KEY_PREFIX_SCHEDULE_TYPE + SEPARATOR +idx);
            consumptionNot.setScheduleType(scheduleType);
            consumptionNot.setBat(ParamUtil.getString(request,KEY_PREFIX_BAT+SEPARATOR+idx));
            consumptionNot.setConsumeType(ParamUtil.getString(request,KEY_PREFIX_CONSUME_TYPE+SEPARATOR+idx));
            consumptionNot.setConsumedQty(ParamUtil.getString(request,KEY_PREFIX_CONSUME_QTY +SEPARATOR+idx));
            consumptionNot.setMeaUnit(ParamUtil.getString(request,KEY_PREFIX_MEASUREMENT_UNIT+SEPARATOR+idx));

            PrimaryDocDto primaryDocDto = new PrimaryDocDto();
            primaryDocDto.reqObjMapping(mulReq,request,getDocType(scheduleType),String.valueOf(idx));
            consumptionNot.setDocType(getDocType(scheduleType));
            //joint repoId exist
            String newRepoId = String.join(",", primaryDocDto.getNewDocMap().keySet());
            consumptionNot.setRepoIdNewString(newRepoId);
            //set newDocFiles
            consumptionNot.setNewDocInfos(primaryDocDto.getNewDocTypeList());
            //set need Validation value
            addConsumptionNotList(consumptionNot);
        }
        PrimaryDocDto primaryDocDto = new PrimaryDocDto();
        primaryDocDto.reqOtherMapping(mulReq,request,"others");
        this.setOtherNewInfos(primaryDocDto.getNewDocTypeList());
        //get all new doc
        fillAllNewDocInfo();
        //get all
        getDocMetaInfoFromNew();
        this.setRemarks(ParamUtil.getString(request,KEY_PREFIX_REMARKS));
        this.setFacId((String) ParamUtil.getSessionAttr(request,KEY_FAC_ID));
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
