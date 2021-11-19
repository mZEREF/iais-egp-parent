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
        private PrimaryDocDto primaryDocDto;

        private List<PrimaryDocDto.DocRecordInfo> savedInfos;

        private List<PrimaryDocDto.DocMeta> docMetas;

        @JsonIgnore
        private List<PrimaryDocDto.NewDocInfo> newDocInfos;
        @JsonIgnore
        private String docType;
        @JsonIgnore
        private String repoIdNewString;

        public DisposalNot() {
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
    private List<DisposalNot> disposalNotList;
    private String facId;
    private String remarks;
    private String ensure;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    public DisposalNotificationDto() {
        disposalNotList = new ArrayList<>();
        disposalNotList.add(new DisposalNot());
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
        private List<PrimaryDocDto.DocRecordInfo> savedInfos;
        private List<PrimaryDocDto.DocMeta> docMetas;
    }

    @Data
    @NoArgsConstructor
    public static class DisposalNotNeedR{
        private List<DisposalNotNeed> needList;
        private String facId;
        private String remarks;
        private String ensure;
    }

    public List<DisposalNot> getDisposalNotList() { return new ArrayList<>(disposalNotList);
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

    public void clearDisposalLists(){
        this.disposalNotList.clear();
    }

    public void addDisposalLists(DisposalNot disposalNot){
        this.disposalNotList.add(disposalNot);
    }

    public void setDisposalLists(List<DisposalNot> disposalNotList) {
        this.disposalNotList = new ArrayList<>(disposalNotList);
    }
    // validate
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
     * @return Map<String,List<PrimaryDocDto.DocMeta>>
     * */
    public Map<String,List<PrimaryDocDto.DocMeta>> getAllDocMetaByDocType(){
        List<PrimaryDocDto.DocMeta> docMetas = this.disposalNotList.stream().flatMap(i->i.getDocMetas().stream()).collect(Collectors.toList());
        return sg.gov.moh.iais.egp.bsb.util.CollectionUtils.groupCollectionToMap(docMetas,PrimaryDocDto.DocMeta::getDocType);
    }

    /**
     * This method is for downloading and contains all file information sorted by tmpId
     *getAllNewDocInfo
     * @return Map<String,PrimaryDocDto.NewDocInfo>
     * */
    public Map<String,PrimaryDocDto.NewDocInfo> getAllNewDocInfo(){
        Map<String,PrimaryDocDto.NewDocInfo> newRecordMap = new HashMap<>();
        if(CollectionUtils.isEmpty(this.disposalNotList)){
            List<PrimaryDocDto.NewDocInfo> newDocInfos = disposalNotList.stream().flatMap(i->i.getNewDocInfos().stream()).collect(Collectors.toList());
            newRecordMap = newDocInfos.stream().collect(Collectors.toMap(PrimaryDocDto.NewDocInfo::getTmpId, Function.identity()));
        }
        return newRecordMap;
    }

    /**
     * this method is used to consume useful data by feign
     * setDisposalNotNeedR
     * @return DisposalNotNeedR
     * */
    public DisposalNotNeedR getDisposalNotNeedR(){
        List<DisposalNotNeed> disposalNotNeeds = disposalNotList.stream().map(t->{
            DisposalNotNeed disposalNoyNeed = new DisposalNotNeed();
            disposalNoyNeed.setScheduleType(t.getScheduleType());
            disposalNoyNeed.setBat(t.getBat());
            disposalNoyNeed.setMeaUnit(t.getMeaUnit());
            disposalNoyNeed.setDisposedQty(t.getDisposedQty());
            disposalNoyNeed.setDestructMethod(t.getDestructMethod());
            disposalNoyNeed.setDestructDetails(t.getDestructDetails());
            disposalNoyNeed.setDocMetas(t.getDocMetas());
            disposalNoyNeed.setSavedInfos(t.getSavedInfos());
            return disposalNoyNeed; }).collect(Collectors.toList());
        DisposalNotNeedR disposalNoyNeedR = new DisposalNotNeedR();
        disposalNoyNeedR.setNeedList(disposalNotNeeds);
        disposalNoyNeedR.setEnsure(this.ensure);
        disposalNoyNeedR.setRemarks(this.remarks);
        disposalNoyNeedR.setFacId(this.facId);
        return disposalNoyNeedR;
    }

    /**
     * reqObjectMapping
     * get value from request
     * */
    public void reqObjectMapping(HttpServletRequest request){
        String idxes = ParamUtil.getString(request, KEY_SECTION_IDXES);
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
            primaryDocDto.reqObjMapping(request,getDocType(scheduleType),String.valueOf(idx));
            disposalNot.setPrimaryDocDto(primaryDocDto);
            disposalNot.setDocType(getDocType(scheduleType));
            //joint repoId exist
            String newRepoId = String.join(",", primaryDocDto.getNewDocMap().keySet());
            disposalNot.setRepoIdNewString(newRepoId);
            //set newDocFiles
            disposalNot.setNewDocInfos(primaryDocDto.getNewDocTypeList());
            //set need Validation value
            disposalNot.setDocMetas(primaryDocDto.doValidation());

            addDisposalLists(disposalNot);
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
