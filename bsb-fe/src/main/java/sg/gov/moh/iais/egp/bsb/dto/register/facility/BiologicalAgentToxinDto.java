package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;
import sg.gov.moh.iais.egp.common.annotation.RfcAttributeDesc;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class BiologicalAgentToxinDto extends ValidatableNodeValue {
    @Data
    @NoArgsConstructor
    public static class BATInfo implements Serializable {
        @RfcAttributeDesc(aliasName = "iais.bsbfe.facBat.schedule")
        private String schedule;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facBat.name")
        private String batName;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facBat.sample.addOrDelete")
        private List<String> sampleType;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facBat.sample.other")
        private String otherSampleType;
        /* The key is the sample type, the value is the entity ID related to it */
        private Map<String, String> sampleEntityIdMap;
    }

    private String activityEntityId;
    private String activityType;

    @RfcAttributeDesc(aliasName = "iais.bsbfe.facBat.addOrDelete")
    private List<BATInfo> batInfos;

    private List<String> workType;
    private String sampleWorkDetail;
    private String procurementMode;

    private String facName;
    private String facBlkNo;
    private String facFloor;
    private String facUnit;
    private String facStreet;
    private String facPostalCode;

    private String contactPersonName;
    private String contactPersonEmail;
    private String contactPersonContactNo;

    private String expectedTransferDate;
    private String courierServiceProviderName;
    private String remark;

    @JsonIgnore
    private ValidationResultDto validationResultDto;


    public BiologicalAgentToxinDto() {
        batInfos = new ArrayList<>();
        BATInfo batInfo = new BATInfo();
        batInfo.sampleType = new ArrayList<>();
        batInfo.sampleEntityIdMap  = new HashMap<>();
        batInfos.add(batInfo);
        workType = new ArrayList<>();
    }

    public BiologicalAgentToxinDto(String activityType) {
        this();
        this.activityType = activityType;
    }


    @Override
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("facRegFeignClient", "validateFacilityBiologicalAgentToxin", new Object[]{this});
        return validationResultDto.isPass();
    }

    @Override
    public String retrieveValidationResult() {
        if (this.validationResultDto == null) {
            throw new IllegalStateException("This DTO is not validated");
        }
        return this.validationResultDto.toErrorMsg();
    }

    @Override
    public void clearValidationResult() {
        this.validationResultDto = null;
    }



    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getActivityEntityId() {
        return activityEntityId;
    }

    public void setActivityEntityId(String activityEntityId) {
        this.activityEntityId = activityEntityId;
    }

    public List<BATInfo> getBatInfos() {
        return new ArrayList<>(batInfos);
    }

    public void clearBatInfos() {
        this.batInfos.clear();
    }

    public void addBatInfo(BATInfo info) {
        this.batInfos.add(info);
    }

    public void setBatInfos(List<BATInfo> batInfos) {
        this.batInfos = new ArrayList<>(batInfos);
    }

    public String getProcurementMode() {
        return procurementMode;
    }

    public void setProcurementMode(String procurementMode) {
        this.procurementMode = procurementMode;
    }

    public String getContactPersonEmail() {
        return contactPersonEmail;
    }

    public void setContactPersonEmail(String contactPersonEmail) {
        this.contactPersonEmail = contactPersonEmail;
    }

    public List<String> getWorkType() {
        return new ArrayList<>(workType);
    }

    public void setWorkType(List<String> workType) {
        this.workType = new ArrayList<>(workType);
    }

    public String getSampleWorkDetail() {
        return sampleWorkDetail;
    }

    public void setSampleWorkDetail(String sampleWorkDetail) {
        this.sampleWorkDetail = sampleWorkDetail;
    }

    public String getFacName() {
        return facName;
    }

    public void setFacName(String facName) {
        this.facName = facName;
    }

    public String getFacBlkNo() {
        return facBlkNo;
    }

    public void setFacBlkNo(String facBlkNo) {
        this.facBlkNo = facBlkNo;
    }

    public String getFacFloor() {
        return facFloor;
    }

    public void setFacFloor(String facFloor) {
        this.facFloor = facFloor;
    }

    public String getFacUnit() {
        return facUnit;
    }

    public void setFacUnit(String facUnit) {
        this.facUnit = facUnit;
    }

    public String getFacStreet() {
        return facStreet;
    }

    public void setFacStreet(String facStreet) {
        this.facStreet = facStreet;
    }

    public String getFacPostalCode() {
        return facPostalCode;
    }

    public void setFacPostalCode(String facPostalCode) {
        this.facPostalCode = facPostalCode;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public String getContactPersonContactNo() {
        return contactPersonContactNo;
    }

    public void setContactPersonContactNo(String contactPersonContactNo) {
        this.contactPersonContactNo = contactPersonContactNo;
    }

    public String getExpectedTransferDate() {
        return expectedTransferDate;
    }

    public void setExpectedTransferDate(String expectedTransferDate) {
        this.expectedTransferDate = expectedTransferDate;
    }

    public String getCourierServiceProviderName() {
        return courierServiceProviderName;
    }

    public void setCourierServiceProviderName(String courierServiceProviderName) {
        this.courierServiceProviderName = courierServiceProviderName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public ValidationResultDto getValidationResultDto() {
        return validationResultDto;
    }

    public void setValidationResultDto(ValidationResultDto validationResultDto) {
        this.validationResultDto = validationResultDto;
    }


//    ---------------------------- request -> object ----------------------------------------------

    private static final String SEPARATOR = "--v--";
    private static final String KEY_SECTION_IDXES = "sectionIdx";
    private static final String KEY_PREFIX_SHCEDULE = "schedule";
    private static final String KEY_PREFIX_BAT_NAME = "batName";
    private static final String KEY_PREFIX_SAMPLE_TYPE = "sampleType";
    private static final String KEY_PREFIX_OTHER_SAMPLE_TYPE = "otherSampleType";

    public void reqObjMapping(HttpServletRequest request) {
        clearBatInfos();
        String idxes = ParamUtil.getString(request, KEY_SECTION_IDXES);
        String[] idxArr = idxes.trim().split(" +");
        for (String idx : idxArr) {
            BATInfo info = new BATInfo();
            info.setSchedule(ParamUtil.getString(request, KEY_PREFIX_SHCEDULE + SEPARATOR +idx));
            info.setBatName(ParamUtil.getString(request, KEY_PREFIX_BAT_NAME + SEPARATOR +idx));
            String[] sampleTypes = ParamUtil.getStrings(request, KEY_PREFIX_SAMPLE_TYPE + SEPARATOR +idx);
            if (sampleTypes != null && sampleTypes.length > 0) {
                info.setSampleType(new ArrayList<>(Arrays.asList(sampleTypes)));
            } else {
                info.setSampleType(new ArrayList<>(0));
            }
            if (info.getSampleType().contains(MasterCodeConstants.SAMPLE_NATURE_OTHER)) {
                info.setOtherSampleType(ParamUtil.getString(request, KEY_PREFIX_OTHER_SAMPLE_TYPE + SEPARATOR +idx));
            }
            addBatInfo(info);
        }
    }
}
