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
public class DisposalNotificationDto implements Serializable {
    @Data
    public static class DisposalNot implements Serializable {
        private String scheduleType;
        private String bat;
        private String disposedQty;
        private String meaUnit;
        private String destructMethod;
        private String destructDetails;

        @JsonIgnore
        private List<PrimaryDocDto.NewDocInfo> newDocInfos;
        @JsonIgnore
        private String docType;
        @JsonIgnore
        private String repoIdNewString;

        public DisposalNot() {
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
    private String remarks;
    private String ensure;

    private List<DisposalNot> disposalNotList;
    private List<PrimaryDocDto.NewDocInfo> otherNewInfos;
    private Map<Integer,List<PrimaryDocDto.NewDocInfo>> keyNewInfos;
    private Map<String, PrimaryDocDto.NewDocInfo> allNewDocInfos;
    private Map<String, PrimaryDocDto.DocRecordInfo> savedDocInfos;
    private List<DocMeta> docMetaInfos;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    public DisposalNotificationDto() {
        disposalNotList = new ArrayList<>();
        disposalNotList.add(new DisposalNot());
        docMetaInfos = new ArrayList<>();
        otherNewInfos = new ArrayList<>();
        keyNewInfos = new LinkedHashMap<>();
        allNewDocInfos = new LinkedHashMap<>();
        savedDocInfos = new LinkedHashMap<>();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DisposalNotNeed {
        private String scheduleType;
        private String bat;
        private String disposedQty;
        private String meaUnit;
        private String destructMethod;
        private String destructDetails;
    }

    @Data
    @NoArgsConstructor
    public static class DisposalNotNeedR {
        private List<DisposalNotNeed> needList;
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

    public List<DisposalNot> getDisposalNotList() {
        return disposalNotList;
    }

    public void clearDisposalLists() {
        this.disposalNotList.clear();
    }

    public void addDisposalLists(DisposalNot disposalNot) {
        this.disposalNotList.add(disposalNot);
    }

    public void setDisposalNotList(List<DisposalNot> disposalNotList) {
        this.disposalNotList = disposalNotList;
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
        if (!CollectionUtils.isEmpty(this.disposalNotList)) {
            List<PrimaryDocDto.NewDocInfo> newDocInfos = disposalNotList.stream().flatMap(i -> i.getNewDocInfos().stream()).collect(Collectors.toList());
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
     * this method is used to disposal useful data by feign
     * setDisposalNotNeedR
     *
     * @return DisposalNotNeedR
     */
    public DisposalNotNeedR getDisposalNotNeedR() {
        //get doc need validation
        List<DisposalNotNeed> disposalNotNeeds = disposalNotList.stream().map(t -> {
            DisposalNotNeed disposalNotNeed = new DisposalNotNeed();
            disposalNotNeed.setScheduleType(t.getScheduleType());
            disposalNotNeed.setBat(t.getBat());
            disposalNotNeed.setMeaUnit(t.getMeaUnit());
            disposalNotNeed.setDisposedQty(t.getDisposedQty());
            disposalNotNeed.setDestructDetails(t.getDestructDetails());
            disposalNotNeed.setDestructMethod(t.getDestructMethod());
            return disposalNotNeed;
        }).collect(Collectors.toList());
        DisposalNotNeedR disposalNotNeedR = new DisposalNotNeedR();
        disposalNotNeedR.setNeedList(disposalNotNeeds);
        disposalNotNeedR.setEnsure(this.ensure);
        disposalNotNeedR.setRemarks(this.remarks);
        disposalNotNeedR.setFacId(this.facId);
        disposalNotNeedR.setDocInfos(new ArrayList<>(savedDocInfos.values()));
        disposalNotNeedR.setDocMetas(this.docMetaInfos);
        return disposalNotNeedR;
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
        DisposalNotNeedR needR = getDisposalNotNeedR();
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("dataSubmissionFeignClient", "validateDisposalNot", new Object[]{needR});
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
        clearDisposalLists();
        String[] idxArr = idxes.trim().split(" +");
        int keyFlag = 0;
        for (String idx : idxArr) {
            DisposalNot disposalNot = new DisposalNot();
            String scheduleType = ParamUtil.getString(request, KEY_PREFIX_SCHEDULE_TYPE + SEPARATOR + idx);
            disposalNot.setScheduleType(scheduleType);
            disposalNot.setBat(ParamUtil.getString(request, KEY_PREFIX_BAT + SEPARATOR + idx));
            disposalNot.setDisposedQty(ParamUtil.getString(request, KEY_PREFIX_DISPOSE_QTY + SEPARATOR + idx));
            disposalNot.setMeaUnit(ParamUtil.getString(request, KEY_PREFIX_MEASUREMENT_UNIT + SEPARATOR + idx));
            disposalNot.setDestructMethod(ParamUtil.getString(request, KEY_PREFIX_DESTRUCT_METHOD + SEPARATOR + idx));
            disposalNot.setDestructDetails(ParamUtil.getString(request, KEY_PREFIX_DESTRUCT_DETAILS + SEPARATOR + idx));

            List<PrimaryDocDto.NewDocInfo> newDocInfoList = PrimaryDocDto.reqObjMapping(mulReq,request,getDocType(scheduleType),String.valueOf(idx),this.allNewDocInfos);
            disposalNot.setDocType(getDocType(scheduleType));
            disposalNot.setNewDocInfos(newDocInfoList);
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
                    disposalNot.setNewDocInfos(oldDocInfo);
                    newRepoId = oldDocInfo.stream().map(PrimaryDocDto.NewDocInfo::getTmpId).collect(Collectors.joining(","));
                }
            }
            disposalNot.setRepoIdNewString(newRepoId);
            //set need Validation value
            addDisposalLists(disposalNot);
        }
        List<PrimaryDocDto.NewDocInfo> newOtherList = PrimaryDocDto.reqOtherMapping(mulReq,request,"others",this.allNewDocInfos);
        this.setOtherNewInfos(newOtherList);
        //get all new doc
        PrimaryDocDto.deleteNewFiles(mulReq,this.allNewDocInfos);
        //When a section is deleted, all files corresponding to it are deleted
        removeTempIdByKeyMap(request);
        //get all
        getDocMetaInfoFromNew();
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
        String deleteIdx = ParamUtil.getString(request,"deleteIdx");
        if(StringUtils.hasLength(deleteIdx)){
            List<Integer> deleteIds = Arrays.stream(deleteIdx.split(","))
                    .map(Integer::valueOf)
                    .collect(Collectors.toList());
            deleteIds.forEach(this.keyNewInfos::remove);
        }
//        Set<Integer> keySet = this.keyNewInfos.keySet();
//        if(CollectionUtils.isEmpty(keySet)){
//            return;
//        }
//        for (Integer key : keySet) {
//            //Determine which section no was deleted
//            if(!arrayContainsKey(idxArr,String.valueOf(key))){
//                //Retrieve the ids of the files in the deleted section and remove them from Map allNewDocInfo
//                List<String> tempId  = this.keyNewInfos.get(key).stream()
//                        .map(PrimaryDocDto.NewDocInfo::getTmpId)
//                        .collect(Collectors.toList());
//                tempId.forEach(this.allNewDocInfos::remove);
//                //keyMap delete the section no,to prevent add a new section number equal keyNewInfos key and show value in page
//                this.keyNewInfos.remove(key);
//            }
//        }
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
