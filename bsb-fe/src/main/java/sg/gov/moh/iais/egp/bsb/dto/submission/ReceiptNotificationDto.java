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
public class ReceiptNotificationDto implements Serializable{
    @Data
    public static class ReceiptNot implements Serializable {
        private String scheduleType;
        private String bat;
        private String receiveQty;
        private String meaUnit;

        @JsonIgnore
        private List<PrimaryDocDto.NewDocInfo> newDocInfos;
        @JsonIgnore
        private String docType;
        @JsonIgnore
        private String repoIdNewString;

        public ReceiptNot() {
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

    private List<ReceiptNot> receiptNotList;
    private List<PrimaryDocDto.NewDocInfo> otherNewInfos;
    private Map<String, PrimaryDocDto.NewDocInfo> allNewDocInfos;
    private Map<String,PrimaryDocDto.DocRecordInfo> savedDocInfos;
    private List<DocMeta> docMetaInfos;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    public ReceiptNotificationDto() {
        receiptNotList = new ArrayList<>();
        receiptNotList.add(new ReceiptNot());
        docMetaInfos = new ArrayList<>();
        otherNewInfos = new ArrayList<>();
        allNewDocInfos = new LinkedHashMap<>();
        savedDocInfos = new LinkedHashMap<>();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReceiptNotNeed{
        private String scheduleType;
        private String bat;
        private String receiveQty;
        private String meaUnit;
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
        private List<PrimaryDocDto.DocRecordInfo> docInfos;
        private List<DocMeta> docMetas;
    }

    public String getFacId() {
        return facId;
    }

    public void setFacId(String facId) {
        this.facId = facId;
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

    public List<ReceiptNot> getReceiptNotList() {
        return receiptNotList;
    }

    public void clearReceiptLists(){
        this.receiptNotList.clear();
    }

    public void addReceiptLists(ReceiptNot receiptNot){
        this.receiptNotList.add(receiptNot);
    }

    public void setReceiptNotList(List<ReceiptNot> receiptNotList) {
        this.receiptNotList = receiptNotList;
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
        if(!CollectionUtils.isEmpty(this.receiptNotList)){
            List<PrimaryDocDto.NewDocInfo> newDocInfos = receiptNotList.stream().flatMap(i->i.getNewDocInfos().stream()).collect(Collectors.toList());
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
        receiptNotNeedR.setDocInfos(new ArrayList<>(savedDocInfos.values()));
        receiptNotNeedR.setDocMetas(this.docMetaInfos);
        return receiptNotNeedR;
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
        clearReceiptLists();
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
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
            primaryDocDto.reqObjMapping(mulReq,request,getDocType(scheduleType),String.valueOf(idx));
            receiptNot.setDocType(getDocType(scheduleType));
            //joint repoId exist
            String newRepoId = String.join(",", primaryDocDto.getNewDocMap().keySet());
            receiptNot.setRepoIdNewString(newRepoId);
            //set newDocFiles
            receiptNot.setNewDocInfos(primaryDocDto.getNewDocTypeList());
            //set need Validation value
            addReceiptLists(receiptNot);
        }
        PrimaryDocDto primaryDocDto = new PrimaryDocDto();
        primaryDocDto.reqOtherMapping(mulReq,request,"others");
        this.setOtherNewInfos(primaryDocDto.getNewDocTypeList());
        //get all new doc
        fillAllNewDocInfo();
        //get all
        getDocMetaInfoFromNew();
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
