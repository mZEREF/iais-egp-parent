package sg.gov.moh.iais.egp.bsb.dto.submission;

import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
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
public class ExportNotificationDto implements Serializable {
    @Data
    public static class ExportNot implements Serializable {
        private String scheduleType;
        private String bat;
        private String transferType;
        private String transferQty;
        private String meaUnit;

        @JsonIgnore
        private List<PrimaryDocDto.NewDocInfo> newDocInfos;
        @JsonIgnore
        private String docType;
        @JsonIgnore
        private String repoIdNewString;

        public ExportNot() {
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
    private String receivedFacility;
    private String receivedCountry;
    private String exportDate;
    private String provider;
    private String flightNo;
    private String remarks;
    private String ensure;

    private List<ExportNot> exportNotList;
    private List<PrimaryDocDto.NewDocInfo> otherNewInfos;
    private Map<Integer,List<PrimaryDocDto.NewDocInfo>> oldKeyNewInfos;
    private Map<Integer,List<PrimaryDocDto.NewDocInfo>> newKeyNewInfos;
    private Map<String, PrimaryDocDto.NewDocInfo> allNewDocInfos;
    private Map<String, PrimaryDocDto.DocRecordInfo> savedDocInfos;
    private List<DocMeta> docMetaInfos;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    public ExportNotificationDto() {
        exportNotList = new ArrayList<>();
        exportNotList.add(new ExportNot());
        docMetaInfos = new ArrayList<>();
        otherNewInfos = new ArrayList<>();
        oldKeyNewInfos = new LinkedHashMap<>();
        newKeyNewInfos = new LinkedHashMap<>();
        allNewDocInfos = new LinkedHashMap<>();
        savedDocInfos = new LinkedHashMap<>();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExportNotNeed {
        private String scheduleType;
        private String bat;
        private String transferType;
        private String transferQty;
        private String meaUnit;
    }

    @Data
    @NoArgsConstructor
    public static class ExportNotNeedR {
        private List<ExportNotNeed> needList;
        private String facId;
        private String receivedFacility;
        private String receivedCountry;
        private String exportDate;
        private String provider;
        private String flightNo;
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

    public String getReceivedFacility() {
        return receivedFacility;
    }

    public void setReceivedFacility(String receivedFacility) {
        this.receivedFacility = receivedFacility;
    }

    public String getReceivedCountry() {
        return receivedCountry;
    }

    public void setReceivedCountry(String receivedCountry) {
        this.receivedCountry = receivedCountry;
    }

    public String getExportDate() {
        return exportDate;
    }

    public void setExportDate(String exportDate) {
        this.exportDate = exportDate;
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

    public List<ExportNot> getExportNotList() {
        return exportNotList;
    }

    public void clearExportLists() {
        this.exportNotList.clear();
    }

    public void addExportLists(ExportNot exportNot) {
        this.exportNotList.add(exportNot);
    }

    public void setExportNotList(List<ExportNot> exportNotList) {
        this.exportNotList = exportNotList;
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
        if (!CollectionUtils.isEmpty(this.exportNotList)) {
            List<PrimaryDocDto.NewDocInfo> newDocInfos = exportNotList.stream().flatMap(i -> i.getNewDocInfos().stream()).collect(Collectors.toList());
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
     * setExportNotNeedR
     *
     * @return ExportNotNeedR
     */
    public ExportNotNeedR getExportNotNeedR() {
        List<ExportNotNeed> exportNotNeeds = exportNotList.stream().map(t -> {
            ExportNotNeed exportNotNeed = new ExportNotNeed();
            exportNotNeed.setScheduleType(t.getScheduleType());
            exportNotNeed.setBat(t.getBat());
            exportNotNeed.setTransferType(t.getTransferType());
            exportNotNeed.setTransferQty(t.getTransferQty());
            exportNotNeed.setMeaUnit(t.getMeaUnit());
            return exportNotNeed;
        }).collect(Collectors.toList());
        ExportNotNeedR exportNotNeedR = new ExportNotNeedR();
        exportNotNeedR.setNeedList(exportNotNeeds);
        exportNotNeedR.setReceivedFacility(this.receivedFacility);
        exportNotNeedR.setReceivedCountry(this.receivedCountry);
        exportNotNeedR.setExportDate(this.exportDate);
        exportNotNeedR.setProvider(this.provider);
        exportNotNeedR.setFlightNo(this.flightNo);
        exportNotNeedR.setRemarks(this.remarks);
        exportNotNeedR.setFacId(this.facId);
        exportNotNeedR.setEnsure(this.ensure);
        exportNotNeedR.setDocInfos(new ArrayList<>(savedDocInfos.values()));
        exportNotNeedR.setDocMetas(this.docMetaInfos);
        return exportNotNeedR;
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
        ExportNotNeedR exportNotNeedR = getExportNotNeedR();
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("dataSubmissionFeignClient", "validateExportNot", new Object[]{exportNotNeedR});
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
        //When a section is deleted, all files corresponding to it are deleted
        removeTempIdByKeyMap(request);
        clearExportLists();
        String[] idxArr = idxes.trim().split(" +");
        int keyFlag = 0;
        for (String idx : idxArr) {
            ExportNot exportNot = new ExportNot();
            String scheduleType = ParamUtil.getString(request, KEY_PREFIX_SCHEDULE_TYPE + SEPARATOR + idx);
            exportNot.setScheduleType(scheduleType);
            exportNot.setBat(ParamUtil.getString(request, KEY_PREFIX_BAT + SEPARATOR + idx));
            exportNot.setTransferType(ParamUtil.getString(request, KEY_PREFIX_TRANSFER_TYPE + SEPARATOR + idx));
            exportNot.setTransferQty(ParamUtil.getString(request, KEY_PREFIX_TRANSFER_QTY + SEPARATOR + idx));
            exportNot.setMeaUnit(ParamUtil.getString(request, KEY_PREFIX_MEASUREMENT_UNIT + SEPARATOR + idx));

            List<PrimaryDocDto.NewDocInfo> newDocInfoList = PrimaryDocDto.reqObjMapping(mulReq,request,getDocType(scheduleType),String.valueOf(idx),this.allNewDocInfos);
            exportNot.setDocType(getDocType(scheduleType));
            exportNot.setNewDocInfos(newDocInfoList);
            // NewRepoId is a String used to concatenate all the ids in the current list
            String newRepoId = "";
            //keyMap is deal with problem document is not show in page
            if(!CollectionUtils.isEmpty(newDocInfoList)){
                this.newKeyNewInfos.put(keyFlag++,newDocInfoList);
                newRepoId = newDocInfoList.stream().map(PrimaryDocDto.NewDocInfo::getTmpId).map(i-> MaskUtil.maskValue("file",i)).collect(Collectors.joining(","));
            }else{
                keyFlag++;
                //Check whether the previous file data exists
                List<PrimaryDocDto.NewDocInfo> oldDocInfo  = this.oldKeyNewInfos.get(Integer.valueOf(idx));
                if(!CollectionUtils.isEmpty(oldDocInfo)){
                    //Populate the list with previous data if it exists
                    exportNot.setNewDocInfos(oldDocInfo);
                    newRepoId = oldDocInfo.stream().map(PrimaryDocDto.NewDocInfo::getTmpId).map(i-> MaskUtil.maskValue("file",i)).collect(Collectors.joining(","));
                }
            }
            exportNot.setRepoIdNewString(newRepoId);
            //set need Validation value
            addExportLists(exportNot);
        }
        List<PrimaryDocDto.NewDocInfo> newOtherList = PrimaryDocDto.reqOtherMapping(mulReq,request,"others",this.allNewDocInfos);
        this.setOtherNewInfos(newOtherList);
        //get all new doc
        PrimaryDocDto.deleteNewFiles(mulReq,this.allNewDocInfos);
        //get all
        getDocMetaInfoFromNew();
        this.setReceivedFacility(ParamUtil.getString(request, KEY_PREFIX_RECEIVED_FACILITY));
        this.setReceivedCountry(ParamUtil.getString(request, KEY_PREFIX_RECEIVED_COUNTRY));
        this.setExportDate(ParamUtil.getString(request, KEY_PREFIX_EXPORT_DATE));
        this.setProvider(ParamUtil.getString(request, KEY_PREFIX_PROVIDER));
        this.setFlightNo(ParamUtil.getString(request, KEY_PREFIX_FLIGHT_NO));
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
     * section no[1,3,4]->[1,2,3] del2,[1,3]
     * */

    public void removeTempIdByKeyMap(HttpServletRequest request){
        //When changes occur, the value of the new map is assigned to the value of the old map
        if(CollectionUtils.isEmpty(this.newKeyNewInfos)){
            return;
        }
        this.oldKeyNewInfos.clear();
        for (Map.Entry<Integer, List<PrimaryDocDto.NewDocInfo>> entry : this.newKeyNewInfos.entrySet()) {
            this.oldKeyNewInfos.put(entry.getKey(),entry.getValue());
        }
        String deleteIdx = ParamUtil.getString(request,"deleteIdx");
        if(StringUtils.hasLength(deleteIdx)){
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
        Assert.notEmpty(keyList,"key list is empty");
        for (Integer intKey : keyList) {
            this.newKeyNewInfos.put(newKeyFlag++,this.oldKeyNewInfos.get(intKey));
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
