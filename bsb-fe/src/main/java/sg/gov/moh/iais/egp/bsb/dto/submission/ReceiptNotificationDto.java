package sg.gov.moh.iais.egp.bsb.dto.submission;

import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.googlecode.jmapper.annotations.JMap;
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
import java.util.function.Function;
import java.util.stream.Collectors;

import static sg.gov.moh.iais.egp.bsb.constant.DataSubmissionConstants.*;

/**
 * @author Zhu Tangtang
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReceiptNotificationDto implements Serializable {
    @Data
    public static class ReceiptNot implements Serializable {
        @JMap
        private String scheduleType;
        @JMap
        private String bat;
        @JMap
        private String receiveQty;
        @JMap
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

    @JMap
    private String facId;
    @JMap
    private String modeProcurement;
    @JMap
    private String sourceFacilityName;
    @JMap
    private String sourceFacilityAddress;
    @JMap
    private String sourceFacilityContactPerson;
    @JMap
    private String contactPersonEmail;
    @JMap
    private String contactPersonTel;
    @JMap
    private String provider;
    @JMap
    private String flightNo;
    @JMap
    private String actualArrivalDate;
    @JMap
    private String actualArrivalTime;
    @JMap
    private String remarks;
    @JMap
    private String ensure;
    @JMap
    private String draftAppNo;
    @JMap
    private String dataSubmissionType;

    @JMap("needList")
    private List<ReceiptNot> receiptNotList;
    private List<PrimaryDocDto.NewDocInfo> otherNewInfos;
    private Map<Integer, List<PrimaryDocDto.NewDocInfo>> oldKeyNewInfos;
    private Map<Integer, List<PrimaryDocDto.NewDocInfo>> newKeyNewInfos;
    private Map<String, PrimaryDocDto.NewDocInfo> allNewDocInfos;
    private Map<String, PrimaryDocDto.DocRecordInfo> savedDocInfos;
    private List<DocMeta> docMetaInfos;
    //key is index
    private Map<Integer, List<PrimaryDocDto.DocRecordInfo>> oldKeySavedInfos;
    private List<PrimaryDocDto.DocRecordInfo> otherSavedInfos;

    @JsonIgnore
    private PrimaryDocDto primaryDocDto;
    @JsonIgnore
    private ValidationResultDto validationResultDto;

    public ReceiptNotificationDto() {
        receiptNotList = new ArrayList<>();
        receiptNotList.add(new ReceiptNot());
        docMetaInfos = new ArrayList<>();
        otherNewInfos = new ArrayList<>();
        oldKeyNewInfos = new LinkedHashMap<>();
        newKeyNewInfos = new LinkedHashMap<>();
        allNewDocInfos = new LinkedHashMap<>();
        savedDocInfos = new LinkedHashMap<>();
        //
        oldKeySavedInfos = new LinkedHashMap<>();
        otherSavedInfos = new ArrayList<>();
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
        private String dataSubmissionType;
        private String draftAppNo;
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

    public PrimaryDocDto getPrimaryDocDto() {
        return primaryDocDto;
    }

    public void setPrimaryDocDto(PrimaryDocDto primaryDocDto) {
        this.primaryDocDto = primaryDocDto;
    }

    public String getDataSubmissionType() {
        return dataSubmissionType;
    }

    public void setDataSubmissionType(String dataSubmissionType) {
        this.dataSubmissionType = dataSubmissionType;
    }

    public Map<Integer, List<PrimaryDocDto.DocRecordInfo>> getOldKeySavedInfos() {
        return oldKeySavedInfos;
    }

    public void setOldKeySavedInfos(Map<Integer, List<PrimaryDocDto.DocRecordInfo>> oldKeySavedInfos) {
        this.oldKeySavedInfos = oldKeySavedInfos;
    }

    public List<PrimaryDocDto.DocRecordInfo> getOtherSavedInfos() {
        return otherSavedInfos;
    }

    public void setOtherSavedInfos(List<PrimaryDocDto.DocRecordInfo> otherSavedInfos) {
        this.otherSavedInfos = otherSavedInfos;
    }

    public String getDraftAppNo() {
        return draftAppNo;
    }

    public void setDraftAppNo(String draftAppNo) {
        this.draftAppNo = draftAppNo;
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

    public Map<Integer, List<PrimaryDocDto.NewDocInfo>> getOldKeyNewInfos() {
        return oldKeyNewInfos;
    }

    public void setOldKeyNewInfos(Map<Integer, List<PrimaryDocDto.NewDocInfo>> oldKeyNewInfos) {
        this.oldKeyNewInfos = oldKeyNewInfos;
    }

    public Map<Integer, List<PrimaryDocDto.NewDocInfo>> getNewKeyNewInfos() {
        return newKeyNewInfos;
    }

    public void setNewKeyNewInfos(Map<Integer, List<PrimaryDocDto.NewDocInfo>> newKeyNewInfos) {
        this.newKeyNewInfos = newKeyNewInfos;
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

    public void getDocMetaInfoToValidate() {
        this.docMetaInfos.clear();
        this.allNewDocInfos.values().forEach(i -> {
            DocMeta docMeta = new DocMeta(i.getTmpId(), i.getDocType(), i.getFilename(), i.getSize(), "dataSub");
            addDocMetaInfos(docMeta);
        });
        this.savedDocInfos.values().forEach(i -> {
            DocMeta docMeta = new DocMeta(i.getRepoId(), i.getDocType(), i.getFilename(), i.getSize(), "dataSub");
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
        allNewDocInfos.clear();
        newKeyNewInfos.clear();
        otherNewInfos.clear();
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
     *
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
        if (StringUtils.hasLength(idxes)) {
            //When a section is deleted, all files corresponding to it are deleted
            removeTempIdByKeyMap(request);
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

                List<PrimaryDocDto.NewDocInfo> newDocInfoList = PrimaryDocDto.reqObjMapping(mulReq, request, getDocType(scheduleType), String.valueOf(idx), this.allNewDocInfos, keyFlag);
                receiptNot.setDocType(getDocType(scheduleType));
                receiptNot.setNewDocInfos(newDocInfoList);
                // NewRepoId is a String used to concatenate all the ids in the current list
                String newRepoId = "";
                //keyMap is deal with problem document is not show in page
                if (!CollectionUtils.isEmpty(newDocInfoList)) {
                    this.newKeyNewInfos.put(keyFlag++, newDocInfoList);
                    newRepoId = newDocInfoList.stream().map(PrimaryDocDto.NewDocInfo::getTmpId).map(i -> MaskUtil.maskValue("file", i)).collect(Collectors.joining(","));
                } else {
                    keyFlag++;
                    //Check whether the previous file data exists
                    List<PrimaryDocDto.NewDocInfo> oldDocInfo = this.oldKeyNewInfos.get(Integer.valueOf(idx));
                    if (!CollectionUtils.isEmpty(oldDocInfo)) {
                        //Populate the list with previous data if it exists
                        receiptNot.setNewDocInfos(oldDocInfo);
                        newRepoId = oldDocInfo.stream().map(PrimaryDocDto.NewDocInfo::getTmpId).map(i -> MaskUtil.maskValue("file", i)).collect(Collectors.joining(","));
                    }
                }
                receiptNot.setRepoIdNewString(newRepoId);
                //set need Validation value
                addReceiptLists(receiptNot);
            }
            List<PrimaryDocDto.NewDocInfo> newOtherList = PrimaryDocDto.reqOtherMapping(mulReq, request, "others", this.allNewDocInfos);
            this.setOtherNewInfos(newOtherList);
            //get all new doc
            PrimaryDocDto.deleteNewFiles(mulReq, this.allNewDocInfos);
            //Reassign to savedDocMap
            draftDocToMap(new ArrayList<>(this.savedDocInfos.values()));
            //get all
            getDocMetaInfoToValidate();
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
     * section no[1,3,4]->[1,2,3] del2,[1,3]
     */

    public void removeTempIdByKeyMap(HttpServletRequest request) {
        //When changes occur, the value of the new map is assigned to the value of the old map
        if (CollectionUtils.isEmpty(this.newKeyNewInfos)) {
            return;
        }
        this.oldKeyNewInfos.clear();
        for (Map.Entry<Integer, List<PrimaryDocDto.NewDocInfo>> entry : this.newKeyNewInfos.entrySet()) {
            this.oldKeyNewInfos.put(entry.getKey(), entry.getValue());
        }
        String deleteIdx = ParamUtil.getString(request, "deleteIdx");
        if (StringUtils.hasLength(deleteIdx)) {
            List<Integer> deleteIds = Arrays.stream(deleteIdx.split(","))
                    .map(Integer::valueOf)
                    .collect(Collectors.toList());
            deleteIds.forEach(this.oldKeyNewInfos::remove);
        }

        //Reassign the map of the new data
        this.newKeyNewInfos.clear();
        int newKeyFlag = 0;
        //Retrieve oldKeyMap keys and sort them in order
        List<Integer> keyList = new ArrayList<>(this.oldKeyNewInfos.keySet());
        Collections.sort(keyList);
        Assert.notEmpty(keyList, "key list is empty");
        for (Integer intKey : keyList) {
            this.newKeyNewInfos.put(newKeyFlag++, this.oldKeyNewInfos.get(intKey));
        }
    }

    /**
     * The array_value () method is used to determine whether a value is contained in an array
     * arrayContainsKey
     *
     * @param idxArr - array contains section no
     * @param key    - value need to search from array idxArr
     */
    public boolean arrayContainsKey(String[] idxArr, String key) {
        Assert.notNull(idxArr, "Array idxArr is null");
        Assert.hasLength(key, "enter key is null");
        return Arrays.asList(idxArr).contains(key);
    }

    /**
     * Take out the files saved during the Save Draft and put them into the Map as required
     * Put the saved docType 'ityBat','ityToxin' file into the Map with key index
     */
    public void draftDocToMap(List<PrimaryDocDto.DocRecordInfo> docInfos) {
        //key is repoId
        Map<String, PrimaryDocDto.DocRecordInfo> savedConsumeDocMap = docInfos.stream().collect(Collectors.toMap(PrimaryDocDto.DocRecordInfo::getRepoId, Function.identity()));
        this.setSavedDocInfos(savedConsumeDocMap);
        //key is docType
        Map<String, List<PrimaryDocDto.DocRecordInfo>> savedDocMap = sg.gov.moh.iais.egp.bsb.util.CollectionUtils.groupCollectionToMap(docInfos, PrimaryDocDto.DocRecordInfo::getDocType);
        //get bat docs
        List<PrimaryDocDto.DocRecordInfo> ityBatSavedDocs = savedDocMap.get("ityBat");
        List<PrimaryDocDto.DocRecordInfo> ityToxinSavedDocs = savedDocMap.get("ityToxin");
        List<PrimaryDocDto.DocRecordInfo> docs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(ityBatSavedDocs)) {
            docs.addAll(ityBatSavedDocs);
        }
        if (!CollectionUtils.isEmpty(ityToxinSavedDocs)) {
            docs.addAll(ityToxinSavedDocs);
        }
        //key is index,used to display on page
        Map<Integer, List<PrimaryDocDto.DocRecordInfo>> oldKeySavedMap = sg.gov.moh.iais.egp.bsb.util.CollectionUtils.groupCollectionToMap(docs, PrimaryDocDto.DocRecordInfo::getIndex);
        this.setOldKeySavedInfos(oldKeySavedMap);
        //get others saved doc list
        List<PrimaryDocDto.DocRecordInfo> othersSavedDocs = savedDocMap.get("others");
        if (!CollectionUtils.isEmpty(othersSavedDocs)) {
            this.setOtherSavedInfos(othersSavedDocs);
        }
    }
}