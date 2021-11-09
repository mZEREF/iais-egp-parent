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
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        private PrimaryDocDto primaryDocDto;

        private List<PrimaryDocDto.DocRecordInfo> savedInfos;

        private List<PrimaryDocDto.DocMeta> docMetas;

        @JsonIgnore
        private List<PrimaryDocDto.NewDocInfo> newDocInfos;
        @JsonIgnore
        private String docType;
        @JsonIgnore
        private String repoIdNewString;

        public ConsumptionNot() {
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

        public void setDocMetas(List<PrimaryDocDto.DocMeta> docMetas){
            this.docMetas = new ArrayList<>(docMetas);
        }

        public List<PrimaryDocDto.NewDocInfo> getNewInfos(){
            return new ArrayList<>(this.newDocInfos);
        }

        public void setNewInfos(List<PrimaryDocDto.NewDocInfo> newDocInfos){
            this.newDocInfos = new ArrayList<>(newDocInfos);
        }
    }

    private List<ConsumptionNot> consumptionNotList;
    private String facId;
    private String remarks;
    private String ensure;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    public ConsumeNotificationDto() {
        consumptionNotList = new ArrayList<>();
        consumptionNotList.add(new ConsumptionNot());
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
        private List<PrimaryDocDto.DocRecordInfo> savedInfos;
        private List<PrimaryDocDto.DocMeta> docMetas;
    }

    @Data
    @NoArgsConstructor
    public static class ConsumeNotNeedR{
        private List<ConsumeNotNeed> consumeNotNeedList;
        private String facId;
        private String remarks;
        private String ensure;
    }

    public List<ConsumptionNot> getConsumptionNotList() { return new ArrayList<>(consumptionNotList);
    }

    public void clearConsumptionNotList(){
        this.consumptionNotList.clear();
    }

    public void addConsumptionNotList(ConsumptionNot consumptionLists){
        this.consumptionNotList.add(consumptionLists);
    }

    public void setConsumptionNotList(List<ConsumptionNot> consumptionLists) {
        this.consumptionNotList = new ArrayList<>(consumptionLists);
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

    // validate
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
     * @return Map<String,List<PrimaryDocDto.DocMeta>>
     * */
    public Map<String,List<PrimaryDocDto.DocMeta>> getAllDocMetaByDocType(){
        List<PrimaryDocDto.DocMeta> docMetas = this.consumptionNotList.stream().flatMap(i->i.getDocMetas().stream()).collect(Collectors.toList());
        return sg.gov.moh.iais.egp.bsb.util.CollectionUtils.groupCollectionToMap(docMetas,PrimaryDocDto.DocMeta::getDocType);
    }

    /**
     * This method is for downloading and contains all file information sorted by tmpId
     *getAllNewDocInfo
     * @return Map<String,PrimaryDocDto.NewDocInfo>
     * */
    public Map<String,PrimaryDocDto.NewDocInfo> getAllNewDocInfo(){
        Map<String,PrimaryDocDto.NewDocInfo> newRecordMap = new HashMap<>();
        if(CollectionUtils.isEmpty(this.consumptionNotList)){
            List<PrimaryDocDto.NewDocInfo> newDocInfos = consumptionNotList.stream().flatMap(i->i.getNewDocInfos().stream()).collect(Collectors.toList());
            newRecordMap = newDocInfos.stream().collect(Collectors.toMap(PrimaryDocDto.NewDocInfo::getTmpId, Function.identity()));
        }
        return newRecordMap;
    }

    /**
     * this method is used to consume useful data by feign
     * setConsumeNotNeedR
     * @return ConsumeNotNeedR
     * */
    public ConsumeNotNeedR getConsumeNotNeedR(){
        List<ConsumeNotNeed> consumeNotNeeds = consumptionNotList.stream().map(t->{
            ConsumeNotNeed consumeNotNeed = new ConsumeNotNeed();
            consumeNotNeed.setScheduleType(t.getScheduleType());
            consumeNotNeed.setBat(t.getBat());
            consumeNotNeed.setConsumeType(t.getConsumeType());
            consumeNotNeed.setConsumedQty(t.getConsumedQty());
            consumeNotNeed.setMeaUnit(t.getMeaUnit());
            consumeNotNeed.setDocMetas(t.getDocMetas());
            consumeNotNeed.setSavedInfos(t.getSavedInfos());
            return consumeNotNeed; }).collect(Collectors.toList());
        ConsumeNotNeedR consumeNotNeedR = new ConsumeNotNeedR();
        consumeNotNeedR.setConsumeNotNeedList(consumeNotNeeds);
        consumeNotNeedR.setEnsure(this.ensure);
        consumeNotNeedR.setRemarks(this.remarks);
        consumeNotNeedR.setFacId(this.facId);
        return consumeNotNeedR;
    }

    /**
     * reqObjectMapping
     * get value from request
     * */
    public void reqObjectMapping(HttpServletRequest request){
        int amt = ParamUtil.getInt(request, KEY_SECTION_AMT);
        clearConsumptionNotList();
        for (int i = 0; i < amt; i++) {
            ConsumptionNot consumptionNot = new ConsumptionNot();
            String scheduleType = ParamUtil.getString(request, KEY_PREFIX_SCHEDULE_TYPE + SEPARATOR + i);
            consumptionNot.setScheduleType(scheduleType);
            consumptionNot.setBat(ParamUtil.getString(request,KEY_PREFIX_BAT+SEPARATOR+i));
            consumptionNot.setConsumeType(ParamUtil.getString(request,KEY_PREFIX_CONSUME_TYPE+SEPARATOR+i));
            consumptionNot.setConsumedQty(ParamUtil.getString(request,KEY_PREFIX_CONSUME_QTY +SEPARATOR+i));
            consumptionNot.setMeaUnit(ParamUtil.getString(request,KEY_PREFIX_MEASUREMENT_UNIT+SEPARATOR+i));

            PrimaryDocDto primaryDocDto = new PrimaryDocDto();
            primaryDocDto.reqObjMapping(request,getDocType(scheduleType),String.valueOf(i));
            consumptionNot.setPrimaryDocDto(primaryDocDto);
            consumptionNot.setDocType(getDocType(scheduleType));
            //joint repoId exist
            String newRepoId = String.join(",", primaryDocDto.getNewDocMap().keySet());
            consumptionNot.setRepoIdNewString(newRepoId);
            //set newDocFiles
            consumptionNot.setNewDocInfos(primaryDocDto.getNewDocTypeList());
            //set need Validation value
            consumptionNot.setDocMetas(primaryDocDto.doValidation());

            addConsumptionNotList(consumptionNot);
        }
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
