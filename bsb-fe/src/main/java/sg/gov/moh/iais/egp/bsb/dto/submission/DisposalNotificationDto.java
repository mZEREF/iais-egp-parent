package sg.gov.moh.iais.egp.bsb.dto.submission;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("dataSubmissionFeignClient", "validateDisposalNot", new Object[]{this});
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
     * reqObjectMapping
     * get value from request
     * */
    public void reqObjectMapping(HttpServletRequest request){
        int amt = ParamUtil.getInt(request,KEY_SECTION_AMT);
        clearDisposalLists();
        for (int i = 0; i < amt; i++) {
            DisposalNot disposalNot = new DisposalNot();
            disposalNot.setScheduleType(ParamUtil.getString(request,KEY_PREFIX_SCHEDULE_TYPE+SEPARATOR+i));
            disposalNot.setBat(ParamUtil.getString(request,KEY_PREFIX_BAT+SEPARATOR+i));
            disposalNot.setDisposedQty(ParamUtil.getString(request,KEY_PREFIX_DISPOSE_QTY+SEPARATOR+i));
            disposalNot.setMeaUnit(ParamUtil.getString(request,KEY_PREFIX_MEASUREMENT_UNIT+ SEPARATOR+i));
            disposalNot.setDestructMethod(ParamUtil.getString(request,KEY_PREFIX_DESTRUCT_METHOD+SEPARATOR+i));
            disposalNot.setDestructDetails(ParamUtil.getString(request,KEY_PREFIX_DESTRUCT_DETAILS+SEPARATOR+i));
            addDisposalLists(disposalNot);
        }
        this.setRemarks(ParamUtil.getString(request,KEY_PREFIX_REMARKS));
        this.setFacId((String) ParamUtil.getSessionAttr(request,KEY_FAC_ID));
    }
}
