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
public class ExportNotificationDto implements Serializable{
    @Data
    public static class ExportNot implements Serializable {
        private String scheduleType;
        private String bat;
        private String transferType;
        private String transferQty;
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

        public ExportNot() {
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

    private List<ExportNot> exportNotList;
    private String facId;
    private String receivedFacility;
    private String receivedCountry;
    private String exportDate;
    private String provider;
    private String flightNo;
    private String remarks;
    private String ensure;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    public ExportNotificationDto() {
        exportNotList = new ArrayList<>();
        exportNotList.add(new ExportNot());
    }

    public List<ExportNot> getExportNotList() { return new ArrayList<>(exportNotList);
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

    public String getEnsure() {
        return ensure;
    }

    public void setEnsure(String ensure) {
        this.ensure = ensure;
    }

    public void clearExportLists(){
        this.exportNotList.clear();
    }

    public void addExportLists(ExportNot exportNot){
        this.exportNotList.add(exportNot);
    }

    public void setExportLists(List<ExportNot> exportNotList) {
        this.exportNotList = new ArrayList<>(exportNotList);
    }

    // validate
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("dataSubmissionFeignClient", "validateExportNot", new Object[]{this});
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
        clearExportLists();

        for (int i = 0; i < amt; i++) {
            ExportNot exportNot = new ExportNot();
            exportNot.setScheduleType(ParamUtil.getString(request,KEY_PREFIX_SCHEDULE_TYPE+SEPARATOR+i));
            exportNot.setBat(ParamUtil.getString(request,KEY_PREFIX_BAT+ SEPARATOR+i));
            exportNot.setTransferType(ParamUtil.getString(request,KEY_PREFIX_TRANSFER_TYPE+ SEPARATOR+i));
            exportNot.setTransferQty(ParamUtil.getString(request,KEY_PREFIX_TRANSFER_QTY + SEPARATOR+i));
            exportNot.setMeaUnit(ParamUtil.getString(request, KEY_PREFIX_MEASUREMENT_UNIT+ SEPARATOR+i));
            addExportLists(exportNot);
        }
        this.setReceivedFacility(ParamUtil.getString(request,KEY_PREFIX_RECEIVED_FACILITY));
        this.setReceivedCountry(ParamUtil.getString(request,KEY_PREFIX_RECEIVED_COUNTRY));
        this.setExportDate(ParamUtil.getString(request,KEY_PREFIX_EXPORT_DATE));
        this.setProvider(ParamUtil.getString(request,KEY_PREFIX_PROVIDER));
        this.setFlightNo(ParamUtil.getString(request,KEY_PREFIX_FLIGHT_NO));
        this.setRemarks(ParamUtil.getString(request,KEY_PREFIX_REMARKS));
        this.setFacId((String) ParamUtil.getSessionAttr(request,KEY_FAC_ID));
    }
}
