package sg.gov.moh.iais.egp.bsb.dto.register.approval;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;

/**
 * @author : LiRan
 * @date : 2022/3/17
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApprovalToPossessDto extends ValidatableNodeValue {

    @Data
    @NoArgsConstructor
    public static class BATInfo implements Serializable {
        private String schedule;
        private String batName;
        private List<String> sampleType;
        private String otherSampleType;
        private List<String> workType;
        private String otherWorkType;

        private String procurementMode;

        private String facilityName;
        private String postalCode;
        private String addressType;
        private String blockNo;
        private String houseNo;
        private String floorNo;
        private String unitNo;
        private String streetName;
        private String buildingName;

        private String contactPersonName;
        private String emailAddress;
        private String contactNo;
        private String expectedDate;
        private String courierServiceProviderName;
        private String remarks;

        private Map<String, String> sampleEntityIdMap;
        private Map<String, String> workEntityIdMap;
    }

    private String activityEntityId;
    private String activityType;

    private List<BATInfo> batInfos;

    private ValidationResultDto validationResultDto;

    public ApprovalToPossessDto() {
        batInfos = new ArrayList<>();
        BATInfo batInfo = new BATInfo();
        batInfo.sampleType = new ArrayList<>();
        batInfo.workType = new ArrayList<>();
        batInfo.sampleEntityIdMap = new HashMap<>();
        batInfo.workEntityIdMap = new HashMap<>();
        batInfos.add(batInfo);
    }

    public ApprovalToPossessDto(String activityType) {
        this();
        this.activityType = activityType;
    }

    @Override
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod(ApprovalBatAndActivityConstants.FEIGN_CLIENT, "", new Object[]{this});
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

    public String getActivityEntityId() {
        return activityEntityId;
    }

    public void setActivityEntityId(String activityEntityId) {
        this.activityEntityId = activityEntityId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
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

    // ---------------------------- request -> object ----------------------------------------------

    private static final String SEPARATOR                                     = "--v--";
    private static final String KEY_SECTION_IDXES                             = "sectionIdx";
    private static final String KEY_PREFIX_SCHEDULE                           = "schedule";
    private static final String KEY_PREFIX_BAT_NAME                           = "batName";
    private static final String KEY_PREFIX_SAMPLE_TYPE                        = "sampleType";
    private static final String KEY_PREFIX_OTHER_SAMPLE_TYPE                  = "otherSampleType";
    private static final String KEY_PREFIX_WORK_TYPE                          = "workType";
    private static final String KEY_PREFIX_OTHER_WORK_TYPE                    = "otherWorkType";
    private static final String KEY_PREFIX_PROCUREMENT_MODE                   = "procurementMode";
    private static final String KEY_PREFIX_FACILITY_NAME                      = "facilityName";
    private static final String KEY_PREFIX_POSTAL_CODE                        = "postalCode";
    private static final String KEY_PREFIX_ADDRESS_TYPE                       = "addressType";
    private static final String KEY_PREFIX_BLOCK_NO                           = "blockNo";
    private static final String KEY_PREFIX_HOUSE_NO                           = "houseNo";
    private static final String KEY_PREFIX_FLOOR_NO                           = "floorNo";
    private static final String KEY_PREFIX_UNIT_NO                            = "unitNo";
    private static final String KEY_PREFIX_STREET_NAME                        = "streetName";
    private static final String KEY_PREFIX_BUILDING_NAME                      = "buildingName";
    private static final String KEY_PREFIX_CONTACT_PERSON_NAME                = "contactPersonName";
    private static final String KEY_PREFIX_EMAIL_ADDRESS                      = "emailAddress";
    private static final String KEY_PREFIX_CONTACT_NO                         = "contactNo";
    private static final String KEY_PREFIX_EXPECTED_DATE                      = "expectedDate";
    private static final String KEY_PREFIX_COURIER_SERVICE_PROVIDER_NAME      = "courierServiceProviderName";
    private static final String KEY_PREFIX_REMARKS                            = "remarks";

    public void reqObjMapping(HttpServletRequest request) {
        clearBatInfos();
        String idxes = ParamUtil.getString(request, KEY_SECTION_IDXES);
        String[] idxArr = idxes.trim().split(" +");
        for (String idx : idxArr) {
            BATInfo info = new BATInfo();
            info.setSchedule(ParamUtil.getString(request, KEY_PREFIX_SCHEDULE + SEPARATOR +idx));
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
            String[] workTypes = ParamUtil.getStrings(request, KEY_PREFIX_WORK_TYPE + SEPARATOR +idx);
            if (workTypes != null && workTypes.length > 0) {
                info.setWorkType(new ArrayList<>(Arrays.asList(workTypes)));
            } else {
                info.setWorkType(new ArrayList<>(0));
            }
            // TODO: update to masterCode
            if (info.getWorkType().contains("BNOTW006")) {
                info.setOtherWorkType(ParamUtil.getString(request, KEY_PREFIX_OTHER_WORK_TYPE + SEPARATOR +idx));
            }
            info.setProcurementMode(ParamUtil.getString(request, KEY_PREFIX_PROCUREMENT_MODE + SEPARATOR +idx));
            info.setFacilityName(ParamUtil.getString(request, KEY_PREFIX_FACILITY_NAME + SEPARATOR +idx));
            info.setPostalCode(ParamUtil.getString(request, KEY_PREFIX_POSTAL_CODE + SEPARATOR +idx));
            info.setAddressType(ParamUtil.getString(request, KEY_PREFIX_ADDRESS_TYPE + SEPARATOR +idx));
            info.setBlockNo(ParamUtil.getString(request, KEY_PREFIX_BLOCK_NO + SEPARATOR +idx));
            info.setHouseNo(ParamUtil.getString(request, KEY_PREFIX_HOUSE_NO + SEPARATOR +idx));
            info.setFloorNo(ParamUtil.getString(request, KEY_PREFIX_FLOOR_NO + SEPARATOR +idx));
            info.setUnitNo(ParamUtil.getString(request, KEY_PREFIX_UNIT_NO + SEPARATOR +idx));
            info.setStreetName(ParamUtil.getString(request, KEY_PREFIX_STREET_NAME + SEPARATOR +idx));
            info.setBuildingName(ParamUtil.getString(request, KEY_PREFIX_BUILDING_NAME + SEPARATOR +idx));
            info.setContactPersonName(ParamUtil.getString(request, KEY_PREFIX_CONTACT_PERSON_NAME + SEPARATOR +idx));
            info.setEmailAddress(ParamUtil.getString(request, KEY_PREFIX_EMAIL_ADDRESS + SEPARATOR +idx));
            info.setContactNo(ParamUtil.getString(request, KEY_PREFIX_CONTACT_NO + SEPARATOR +idx));
            info.setExpectedDate(ParamUtil.getString(request, KEY_PREFIX_EXPECTED_DATE + SEPARATOR +idx));
            info.setCourierServiceProviderName(ParamUtil.getString(request, KEY_PREFIX_COURIER_SERVICE_PROVIDER_NAME + SEPARATOR +idx));
            info.setRemarks(ParamUtil.getString(request, KEY_PREFIX_REMARKS + SEPARATOR +idx));
            addBatInfo(info);
        }
    }
}
