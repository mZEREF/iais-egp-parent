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
public class ReceiptNotificationDto implements Serializable{
    @Data
    public static class ReceiptNot implements Serializable {
        private String scheduleType;
        private String bat;
        private String receiveQty;
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

        public ReceiptNot() {
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

    private List<ReceiptNot> receiptNotList;
    private String facId;
    private String modeProcurement;
    private String sourceFacilityName;
    private String sourceFacilityAddress;
    private String sourceFacilityContactPerson;
    private String contactPersonEmail;
    private String contactPersonTel;
    private String provider;
    private String flightNo;
    private String actualArrivalDate;
    private String actualArrivalTime;
    private String remarks;
    private String ensure;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    public ReceiptNotificationDto() {
        receiptNotList = new ArrayList<>();
        receiptNotList.add(new ReceiptNot());
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReceiptNotNeed{
        private String scheduleType;
        private String bat;
        private String receiveQty;
        private String meaUnit;
        private List<PrimaryDocDto.DocRecordInfo> savedInfos;
        private List<PrimaryDocDto.DocMeta> docMetas;
    }

    @Data
    @NoArgsConstructor
    public static class ReceiptNotNeedR{
        private List<ReceiptNotNeed> needList;
        private String facId;
        private String modeProcurement;
        private String sourceFacilityName;
        private String sourceFacilityAddress;
        private String sourceFacilityContactPerson;
        private String contactPersonEmail;
        private String contactPersonTel;
        private String provider;
        private String flightNo;
        private String actualArrivalDate;
        private String actualArrivalTime;
        private String remarks;
        private String ensure;
    }

    public List<ReceiptNot> getReceiptNotList() { return new ArrayList<>(receiptNotList);
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

    public String getModeProcurement() {
        return modeProcurement;
    }

    public void setModeProcurement(String modeProcurement) {
        this.modeProcurement = modeProcurement;
    }

    public String getSourceFacilityName() {
        return sourceFacilityName;
    }

    public void setSourceFacilityName(String sourceFacilityName) {
        this.sourceFacilityName = sourceFacilityName;
    }

    public String getSourceFacilityAddress() {
        return sourceFacilityAddress;
    }

    public void setSourceFacilityAddress(String sourceFacilityAddress) {
        this.sourceFacilityAddress = sourceFacilityAddress;
    }

    public String getSourceFacilityContactPerson() {
        return sourceFacilityContactPerson;
    }

    public void setSourceFacilityContactPerson(String sourceFacilityContactPerson) {
        this.sourceFacilityContactPerson = sourceFacilityContactPerson;
    }

    public String getContactPersonEmail() {
        return contactPersonEmail;
    }

    public void setContactPersonEmail(String contactPersonEmail) {
        this.contactPersonEmail = contactPersonEmail;
    }

    public String getContactPersonTel() {
        return contactPersonTel;
    }

    public void setContactPersonTel(String contactPersonTel) {
        this.contactPersonTel = contactPersonTel;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public String getActualArrivalDate() {
        return actualArrivalDate;
    }

    public void setActualArrivalDate(String actualArrivalDate) {
        this.actualArrivalDate = actualArrivalDate;
    }

    public String getActualArrivalTime() {
        return actualArrivalTime;
    }

    public void setActualArrivalTime(String actualArrivalTime) {
        this.actualArrivalTime = actualArrivalTime;
    }

    public void clearReceiptLists(){
        this.receiptNotList.clear();
    }

    public void addReceiptLists(ReceiptNot receiptNot){
        this.receiptNotList.add(receiptNot);
    }

    public void setConsumptionLists(List<ReceiptNot> receiptNotList) {
        this.receiptNotList = new ArrayList<>(receiptNotList);
    }

    // validate
    public boolean doValidation() {
        ReceiptNotNeedR receiptNotNeedR = getReceiptNotNeedR();
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("dataSubmissionFeignClient", "validateReceiptNot", new Object[]{receiptNotNeedR});
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
        List<PrimaryDocDto.DocMeta> docMetas = this.receiptNotList.stream().flatMap(i->i.getDocMetas().stream()).collect(Collectors.toList());
        return sg.gov.moh.iais.egp.bsb.util.CollectionUtils.groupCollectionToMap(docMetas,PrimaryDocDto.DocMeta::getDocType);
    }

    /**
     * This method is for downloading and contains all file information sorted by tmpId
     *getAllNewDocInfo
     * @return Map<String,PrimaryDocDto.NewDocInfo>
     * */
    public Map<String,PrimaryDocDto.NewDocInfo> getAllNewDocInfo(){
        Map<String,PrimaryDocDto.NewDocInfo> newRecordMap = new HashMap<>();
        if(CollectionUtils.isEmpty(this.receiptNotList)){
            List<PrimaryDocDto.NewDocInfo> newDocInfos = receiptNotList.stream().flatMap(i->i.getNewDocInfos().stream()).collect(Collectors.toList());
            newRecordMap = newDocInfos.stream().collect(Collectors.toMap(PrimaryDocDto.NewDocInfo::getTmpId, Function.identity()));
        }
        return newRecordMap;
    }

    /**
     * this method is used to consume useful data by feign
     * setReceiptNotNeedR
     * @return ReceiptNotNeedR
     * */
    public ReceiptNotNeedR getReceiptNotNeedR(){
        List<ReceiptNotNeed> receiptNotNeeds = receiptNotList.stream().map(t->{
            ReceiptNotNeed receiptNotNeed = new ReceiptNotNeed();
            receiptNotNeed.setScheduleType(t.getScheduleType());
            receiptNotNeed.setBat(t.getBat());
            receiptNotNeed.setReceiveQty(t.getReceiveQty());
            receiptNotNeed.setMeaUnit(t.getMeaUnit());
            receiptNotNeed.setDocMetas(t.getDocMetas());
            receiptNotNeed.setSavedInfos(t.getSavedInfos());
            return receiptNotNeed; }).collect(Collectors.toList());
        ReceiptNotNeedR receiptNotNeedR = new ReceiptNotNeedR();
        receiptNotNeedR.setNeedList(receiptNotNeeds);
        receiptNotNeedR.setEnsure(this.ensure);
        receiptNotNeedR.setModeProcurement(this.modeProcurement);
        receiptNotNeedR.setSourceFacilityName(this.sourceFacilityName);
        receiptNotNeedR.setSourceFacilityAddress(this.sourceFacilityAddress);
        receiptNotNeedR.setSourceFacilityContactPerson(this.sourceFacilityContactPerson);
        receiptNotNeedR.setContactPersonEmail(this.contactPersonEmail);
        receiptNotNeedR.setContactPersonTel(this.contactPersonTel);
        receiptNotNeedR.setFlightNo(this.flightNo);
        receiptNotNeedR.setProvider(this.provider);
        receiptNotNeedR.setActualArrivalDate(this.actualArrivalDate);
        receiptNotNeedR.setActualArrivalTime(this.actualArrivalTime);
        receiptNotNeedR.setRemarks(this.remarks);
        receiptNotNeedR.setFacId(this.facId);
        return receiptNotNeedR;
    }


    /**
     * reqObjectMapping
     * get value from request
     * */
    public void reqObjectMapping(HttpServletRequest request){
        clearReceiptLists();
        String idxes = ParamUtil.getString(request, KEY_SECTION_IDXES);
        String[] idxArr = idxes.trim().split(" +");
        for (String idx : idxArr) {
            ReceiptNot receiptNot = new ReceiptNot();
            String scheduleType = ParamUtil.getString(request, KEY_PREFIX_SCHEDULE_TYPE + SEPARATOR +idx);
            receiptNot.setScheduleType(scheduleType);
            receiptNot.setBat(ParamUtil.getString(request,KEY_PREFIX_BAT+SEPARATOR+idx));
            receiptNot.setReceiveQty(ParamUtil.getString(request,KEY_PREFIX_RECEIVE_QTY+SEPARATOR+idx));
            receiptNot.setMeaUnit(ParamUtil.getString(request,KEY_PREFIX_MEASUREMENT_UNIT+SEPARATOR+idx));
            PrimaryDocDto primaryDocDto = new PrimaryDocDto();
            primaryDocDto.reqObjMapping(request,getDocType(scheduleType),String.valueOf(idx));
            receiptNot.setPrimaryDocDto(primaryDocDto);
            receiptNot.setDocType(getDocType(scheduleType));
            //joint repoId exist
            String newRepoId = String.join(",", primaryDocDto.getNewDocMap().keySet());
            receiptNot.setRepoIdNewString(newRepoId);
            //set newDocFiles
            receiptNot.setNewDocInfos(primaryDocDto.getNewDocTypeList());
            //set need Validation value
            receiptNot.setDocMetas(primaryDocDto.doValidation());
            addReceiptLists(receiptNot);
        }
        this.setModeProcurement(ParamUtil.getString(request,KEY_PREFIX_MODE_PROCUREMENT));
        this.setSourceFacilityName(ParamUtil.getString(request,KEY_PREFIX_SOURCE_FACILITY_NAME));
        this.setSourceFacilityAddress(ParamUtil.getString(request,KEY_PREFIX_SOURCE_FACILITY_ADDRESS));
        this.setSourceFacilityContactPerson(ParamUtil.getString(request,KEY_PREFIX_SOURCE_FACILITY_CONTACT_PERSON));
        this.setContactPersonEmail(ParamUtil.getString(request,KEY_PREFIX_CONTACT_PERSON_EMAIL));
        this.setContactPersonTel(ParamUtil.getString(request,KEY_PREFIX_CONTACT_PERSON_TEL));
        this.setFlightNo(ParamUtil.getString(request,KEY_PREFIX_FLIGHT_NO));
        this.setProvider(ParamUtil.getString(request,KEY_PREFIX_PROVIDER));
        this.setActualArrivalDate(ParamUtil.getString(request,KEY_PREFIX_ACTUAL_ARRIVAL_DATE));
        this.setActualArrivalTime(ParamUtil.getString(request,KEY_PREFIX_ACTUAL_ARRIVAL_TIME));
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
