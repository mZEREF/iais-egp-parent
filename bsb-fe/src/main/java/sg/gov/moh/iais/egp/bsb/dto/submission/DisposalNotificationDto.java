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
public class DisposalNotificationDto implements Serializable{
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

    private List<DisposalNot> disposalNotList;
    private List<PrimaryDocDto.NewDocInfo> otherNewInfos;
    private Map<String, PrimaryDocDto.NewDocInfo> allNewDocInfos;
    private Map<String,PrimaryDocDto.DocRecordInfo> savedDocInfos;
    private List<DocMeta> docMetaInfos;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    public DisposalNotificationDto() {
        disposalNotList = new ArrayList<>();
        disposalNotList.add(new DisposalNot());
        docMetaInfos = new ArrayList<>();
        otherNewInfos = new ArrayList<>();
        allNewDocInfos = new LinkedHashMap<>();
        savedDocInfos = new LinkedHashMap<>();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DisposalNotNeed{
        private String scheduleType;
        private String bat;
        private String disposedQty;
        private String meaUnit;
        private String destructMethod;
        private String destructDetails;
    }

    @Data
    @NoArgsConstructor
    public static class DisposalNotNeedR{
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

    public void clearDisposalLists(){
        this.disposalNotList.clear();
    }

    public void addDisposalLists(DisposalNot disposalNot){
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
        if(!CollectionUtils.isEmpty(this.disposalNotList)){
            List<PrimaryDocDto.NewDocInfo> newDocInfos = disposalNotList.stream().flatMap(i->i.getNewDocInfos().stream()).collect(Collectors.toList());
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
     * this method is used to disposal useful data by feign
     * setDisposalNotNeedR
     * @return DisposalNotNeedR
     * */
    public DisposalNotNeedR getDisposalNotNeedR(){
        //get doc need validation
        List<DisposalNotNeed> disposalNotNeeds = disposalNotList.stream().map(t->{
            DisposalNotNeed disposalNotNeed = new DisposalNotNeed();
            disposalNotNeed.setScheduleType(t.getScheduleType());
            disposalNotNeed.setBat(t.getBat());
            disposalNotNeed.setMeaUnit(t.getMeaUnit());
            disposalNotNeed.setDisposedQty(t.getDisposedQty());
            disposalNotNeed.setDestructDetails(t.getDestructDetails());
            disposalNotNeed.setDestructMethod(t.getDestructMethod());
            return disposalNotNeed; }).collect(Collectors.toList());
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
        clearDisposalLists();
        String[] idxArr = idxes.trim().split(" +");
        for (String idx : idxArr) {
            DisposalNot disposalNot = new DisposalNot();
            String scheduleType = ParamUtil.getString(request, KEY_PREFIX_SCHEDULE_TYPE + SEPARATOR +idx);
            disposalNot.setScheduleType(scheduleType);
            disposalNot.setBat(ParamUtil.getString(request,KEY_PREFIX_BAT+SEPARATOR+idx));
            disposalNot.setDisposedQty(ParamUtil.getString(request,KEY_PREFIX_DISPOSE_QTY+SEPARATOR+idx));
            disposalNot.setMeaUnit(ParamUtil.getString(request,KEY_PREFIX_MEASUREMENT_UNIT+ SEPARATOR+idx));
            disposalNot.setDestructMethod(ParamUtil.getString(request,KEY_PREFIX_DESTRUCT_METHOD+SEPARATOR+idx));
            disposalNot.setDestructDetails(ParamUtil.getString(request,KEY_PREFIX_DESTRUCT_DETAILS+SEPARATOR+idx));

            PrimaryDocDto primaryDocDto = new PrimaryDocDto();
            primaryDocDto.reqObjMapping(mulReq,request,getDocType(scheduleType),String.valueOf(idx));
            disposalNot.setDocType(getDocType(scheduleType));
            //joint repoId exist
            String newRepoId = String.join(",", primaryDocDto.getNewDocMap().keySet());
            disposalNot.setRepoIdNewString(newRepoId);
            //set newDocFiles
            disposalNot.setNewDocInfos(primaryDocDto.getNewDocTypeList());
            //set need Validation value
            addDisposalLists(disposalNot);
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
