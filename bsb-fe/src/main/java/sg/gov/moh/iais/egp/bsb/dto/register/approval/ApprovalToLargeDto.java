package sg.gov.moh.iais.egp.bsb.dto.register.approval;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : LiRan
 * @date : 2022/3/17
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApprovalToLargeDto extends ValidatableNodeValue {

    @Data
    @NoArgsConstructor
    public static class BATInfo implements Serializable {
        private String schedule;
        private String batName;
        private String estimatedMaximumVolume;
        private String methodOrSystem;

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
        private String country;
        private String city;
        private String state;

        private String contactPersonName;
        private String emailAddress;
        private String contactNo;
        private String expectedDate;
        private String courierServiceProviderName;
        private String remarks;
    }

    private List<BATInfo> batInfos;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    public ApprovalToLargeDto() {
        batInfos = new ArrayList<>();
    }


    @Override
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod(ApprovalBatAndActivityConstants.FEIGN_CLIENT, "validateApprovalToLargeDto", new Object[]{this});
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
    private static final String KEY_PREFIX_ESTIMATED_MAXIMUM_VOLUME           = "estimatedMaximumVolume";
    private static final String KEY_PREFIX_METHOD_OR_SYSTEM                   = "methodOrSystem";
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
            info.setEstimatedMaximumVolume(ParamUtil.getString(request, KEY_PREFIX_ESTIMATED_MAXIMUM_VOLUME + SEPARATOR +idx));
            info.setMethodOrSystem(ParamUtil.getString(request, KEY_PREFIX_METHOD_OR_SYSTEM + SEPARATOR +idx));
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
