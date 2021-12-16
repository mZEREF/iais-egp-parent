package sg.gov.moh.iais.egp.bsb.dto.submission;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.jsonwebtoken.lang.Assert;
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
import java.util.stream.Collectors;

import static sg.gov.moh.iais.egp.bsb.constant.DataSubmissionConstants.*;

/**
 * @author Zhu Tangtang
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReceiptNotificationDto implements Serializable {
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

        public List<PrimaryDocDto.NewDocInfo> getNewDocInfos() {
            return new ArrayList<>(this.newDocInfos);
        }

        public void setNewDocInfos(List<PrimaryDocDto.NewDocInfo> newDocInfos) {
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
    private Map<Integer,List<PrimaryDocDto.NewDocInfo>> keyNewInfos;
    private Map<String, PrimaryDocDto.NewDocInfo> allNewDocInfos;
    private Map<String, PrimaryDocDto.DocRecordInfo> savedDocInfos;
    private List<DocMeta> docMetaInfos;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    public ReceiptNotificationDto() {
        receiptNotList = new ArrayList<>();
        receiptNotList.add(new ReceiptNot());
        docMetaInfos = new ArrayList<>();
        otherNewInfos = new ArrayList<>();
        keyNewInfos = new LinkedHashMap<>();
        allNewDocInfos = new LinkedHashMap<>();
        savedDocInfos = new LinkedHashMap<>();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReceiptNotNeed {
        private String scheduleType;
        private String bat;
        private String receiveQty;
        private String meaUnit;
    }

    @Data
    @NoArgsConstructor
    public static class ReceiptNotNeedR {
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

    public void clearReceiptLists() {
        this.receiptNotList.clear();
    }

    public void addReceiptLists(ReceiptNot receiptNot) {
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

    public Map<Integer, List<PrimaryDocDto.NewDocInfo>> getKeyNewInfos() {
        return keyNewInfos;
    }

    public void setKeyNewInfos(Map<Integer, List<PrimaryDocDto.NewDocInfo>> keyNewInfos) {
        this.keyNewInfos = keyNewInfos;
    }

    public List<DocMeta> getDocMetaInfos() {
        return docMetaInfos;
    }

    public void setDocMetaInfos(List<DocMeta> docMetaInfos) {
        this.docMetaInfos = docMetaInfos;
    }

    public void addDocMetaInfos(DocMeta docMeta) {
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
     * getAllNewDocInfo
     */
    public void fillAllNewDocInfo() {
        if (!CollectionUtils.isEmpty(this.receiptNotList)) {
            List<PrimaryDocDto.NewDocInfo> newDocInfos = receiptNotList.stream().flatMap(i -> i.getNewDocInfos().stream()).collect(Collectors.toList());
            newDocInfos.addAll(this.otherNewInfos);
            for (PrimaryDocDto.NewDocInfo newDocInfo : newDocInfos) {
                this.allNewDocInfos.put(newDocInfo.getTmpId(), newDocInfo);
            }
        }
    }

    public void getDocMetaInfoFromNew() {
        this.docMetaInfos.clear();
        this.allNewDocInfos.values().forEach(i -> {
            DocMeta docMeta = new DocMeta(i.getTmpId(), i.getDocType(), i.getFilename(), i.getSize(), "dataSub");
            addDocMetaInfos(docMeta);
        });
    }

    /**
     * this method is used to consume useful data by feign
     * setReceiptNotNeedR
     *
     * @return ReceiptNotNeedR
     */
    public ReceiptNotNeedR getReceiptNotNeedR() {
        List<ReceiptNotNeed> receiptNotNeeds = receiptNotList.stream().map(t -> {
            ReceiptNotNeed receiptNotNeed = new ReceiptNotNeed();
            receiptNotNeed.setScheduleType(t.getScheduleType());
            receiptNotNeed.setBat(t.getBat());
            receiptNotNeed.setReceiveQty(t.getReceiveQty());
            receiptNotNeed.setMeaUnit(t.getMeaUnit());
            return receiptNotNeed;
        }).collect(Collectors.toList());
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
     *
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
     *
     * @return Map<String, List < DocMeta>>
     */
    public Map<String, List<DocMeta>> getAllDocMetaByDocType() {
        return sg.gov.moh.iais.egp.bsb.util.CollectionUtils.groupCollectionToMap(docMetaInfos, DocMeta::getDocType);
    }

    /**
     * get a structure used to display new selected docs
     * these docs have not been saved into DB, if user wants to download it, we send the data from current data structure
     * @return a map, the key is the doc type, the value is the new doc info list
     */
    public Map<String, List<PrimaryDocDto.NewDocInfo>> getNewDocTypeMap() {
        return sg.gov.moh.iais.egp.bsb.util.CollectionUtils.groupCollectionToMap(this.allNewDocInfos.values(), PrimaryDocDto.NewDocInfo::getDocType);
    }

    /**
     * reqObjectMapping
     * get value from request
     */
    public void reqObjectMapping(HttpServletRequest request) {
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String idxes = ParamUtil.getString(request, KEY_SECTION_IDXES);
        clearReceiptLists();
        String[] idxArr = idxes.trim().split(" +");
        int keyFlag = 0;
        for (String idx : idxArr) {
            ReceiptNot receiptNot = new ReceiptNot();
            String scheduleType = ParamUtil.getString(request, KEY_PREFIX_SCHEDULE_TYPE + SEPARATOR + idx);
            receiptNot.setScheduleType(scheduleType);
            receiptNot.setBat(ParamUtil.getString(request, KEY_PREFIX_BAT + SEPARATOR + idx));
            receiptNot.setReceiveQty(ParamUtil.getString(request, KEY_PREFIX_RECEIVE_QTY + SEPARATOR + idx));
            receiptNot.setMeaUnit(ParamUtil.getString(request, KEY_PREFIX_MEASUREMENT_UNIT + SEPARATOR + idx));

            List<PrimaryDocDto.NewDocInfo> newDocInfoList = PrimaryDocDto.reqObjMapping(mulReq,request,getDocType(scheduleType),String.valueOf(idx),this.allNewDocInfos);
            receiptNot.setDocType(getDocType(scheduleType));
            receiptNot.setNewDocInfos(newDocInfoList);
            // NewRepoId is a String used to concatenate all the ids in the current list
            String newRepoId = "";
            //keyMap is deal with problem document is not show in page
            if(!CollectionUtils.isEmpty(newDocInfoList)){
                this.keyNewInfos.put(keyFlag++,newDocInfoList);
                newRepoId = newDocInfoList.stream().map(PrimaryDocDto.NewDocInfo::getTmpId).collect(Collectors.joining(","));
            }else{
                //Check whether the previous file data exists
                List<PrimaryDocDto.NewDocInfo> oldDocInfo  = this.keyNewInfos.get(Integer.valueOf(idx));
                if(!CollectionUtils.isEmpty(oldDocInfo)){
                    //Populate the list with previous data if it exists
                    receiptNot.setNewDocInfos(oldDocInfo);
                    newRepoId = oldDocInfo.stream().map(PrimaryDocDto.NewDocInfo::getTmpId).collect(Collectors.joining(","));
                }
            }
            receiptNot.setRepoIdNewString(newRepoId);
            //set need Validation value
            addReceiptLists(receiptNot);
        }
        List<PrimaryDocDto.NewDocInfo> newOtherList = PrimaryDocDto.reqOtherMapping(mulReq,request,"others",this.allNewDocInfos);
        this.setOtherNewInfos(newOtherList);
        //get all new doc
        PrimaryDocDto.deleteNewFiles(mulReq,this.allNewDocInfos);
        //When a section is deleted, all files corresponding to it are deleted
        removeTempIdByKeyMap(idxArr);
        //get all
        getDocMetaInfoFromNew();
        this.setModeProcurement(ParamUtil.getString(request, KEY_PREFIX_MODE_PROCUREMENT));
        this.setSourceFacilityName(ParamUtil.getString(request, KEY_PREFIX_SOURCE_FACILITY_NAME));
        this.setSourceFacilityAddress(ParamUtil.getString(request, KEY_PREFIX_SOURCE_FACILITY_ADDRESS));
        this.setSourceFacilityContactPerson(ParamUtil.getString(request, KEY_PREFIX_SOURCE_FACILITY_CONTACT_PERSON));
        this.setContactPersonEmail(ParamUtil.getString(request, KEY_PREFIX_CONTACT_PERSON_EMAIL));
        this.setContactPersonTel(ParamUtil.getString(request, KEY_PREFIX_CONTACT_PERSON_TEL));
        this.setFlightNo(ParamUtil.getString(request, KEY_PREFIX_FLIGHT_NO));
        this.setProvider(ParamUtil.getString(request, KEY_PREFIX_PROVIDER));
        this.setActualArrivalDate(ParamUtil.getString(request, KEY_PREFIX_ACTUAL_ARRIVAL_DATE));
        this.setActualArrivalTime(ParamUtil.getString(request, KEY_PREFIX_ACTUAL_ARRIVAL_TIME));
        this.setRemarks(ParamUtil.getString(request, KEY_PREFIX_REMARKS));
        this.setFacId((String) ParamUtil.getSessionAttr(request, KEY_FAC_ID));
    }

    public String getDocType(String scheduleType) {
        String docType = "";
        if (StringUtils.hasLength(scheduleType)) {
            switch (scheduleType) {
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

    /**
     * This method is used to assign the doc in the list and concatenate the id of the file to prepare for deletion.
     * At the same time, if the module is deleted, the key of the corresponding module in the corresponding keyMap will be deleted in case the module has no file
     * but still displays the bug when a new section is added next time. There are certain problems with this method. Future changes may be made ！！！！！
     * removeTempIdByKeyMap
     * @param idxArr - section no[1,3,4]->[1,2,3] del2,[1,3]
     * */

    public void removeTempIdByKeyMap(String[] idxArr){
        Set<Integer> keySet = this.keyNewInfos.keySet();
        if(CollectionUtils.isEmpty(keySet)){
            return;
        }
        for (Integer key : keySet) {
            //Determine which section no was deleted
            if(!arrayContainsKey(idxArr,String.valueOf(key))){
                //Retrieve the ids of the files in the deleted section and remove them from Map allNewDocInfo
                List<String> tempId  = this.keyNewInfos.get(key).stream()
                        .map(PrimaryDocDto.NewDocInfo::getTmpId)
                        .collect(Collectors.toList());
                tempId.forEach(this.allNewDocInfos::remove);
                //keyMap delete the section no,to prevent add a new section number equal keyNewInfos key and show value in page
                this.keyNewInfos.remove(key);
            }
        }
    }

    /**
     * The array_value () method is used to determine whether a value is contained in an array
     * arrayContainsKey
     * @param idxArr - array contains section no
     * @param key - value need to search from array idxArr
     * */
    public boolean arrayContainsKey(String[] idxArr,String key){
        Assert.notNull(idxArr,"Array idxArr is null");
        Assert.hasLength(key,"enter key is null");
        return Arrays.asList(idxArr).contains(key);
    }
}