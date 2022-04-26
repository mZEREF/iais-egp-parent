package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
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

        private List<String> workType;

        private String sampleWorkDetail;

        private String procurementMode;

        private String postalCodeT;
        private String addressTypeT;
        private String blockNoT;
        private String floorNoT;
        private String unitNoT;
        private String streetNameT;
        private String buildingNameT;

        private String contactPersonNameT;
        private String emailAddressT;
        private String contactNoT;
        private String expectedDateT;
        private String courierServiceProviderNameT;
        private String remarksT;

        private String facNameE;
        private String postalCodeE;
        private String blockNoE;
        private String floorNoE;
        private String unitNoE;
        private String streetNameE;
        private String countryE;
        private String stateE;

        private String contactPersonNameE;
        private String emailAddressE;
        private String contactNoE;
        private String expectedDateE;
        private String courierServiceProviderNameE;
        private String remarksE;

        private String toxinRegulationDeclare;
    }

    private String activityEntityId;
    private String activityType;

    @RfcAttributeDesc(aliasName = "iais.bsbfe.facBat.addOrDelete")
    private List<BATInfo> batInfos;


    @JsonIgnore
    private ValidationResultDto validationResultDto;


    public BiologicalAgentToxinDto() {
        batInfos = new ArrayList<>();
        BATInfo batInfo = new BATInfo();
        batInfo.sampleType = new ArrayList<>();
        batInfo.workType = new ArrayList<>();
        batInfos.add(batInfo);
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



//    ---------------------------- request -> object ----------------------------------------------

    private static final String SEPARATOR                                     = "--v--";
    private static final String KEY_SECTION_IDXES                             = "sectionIdx";
    private static final String KEY_PREFIX_SCHEDULE                           = "schedule";
    private static final String KEY_PREFIX_BAT_NAME                           = "batName";
    private static final String KEY_PREFIX_SAMPLE_TYPE                        = "sampleType";
    private static final String KEY_PREFIX_WORK_TYPE                          = "workType";
    private static final String KEY_PREFIX_SAMPLE_WORK_DETAIL                 = "sampleWorkDetail";
    private static final String KEY_PREFIX_PROCUREMENT_MODE                   = "procurementMode";
    private static final String KEY_PREFIX_TOXIN_REGULATION_DECLARE           = "toxinRegulationDeclare";

    private static final String KEY_PREFIX_TRANSFERRING_POSTAL_CODE                        = "postalCodeT";
    private static final String KEY_PREFIX_TRANSFERRING_ADDRESS_TYPE                       = "addressTypeT";
    private static final String KEY_PREFIX_TRANSFERRING_BLOCK_NO                           = "blockNoT";
    private static final String KEY_PREFIX_TRANSFERRING_FLOOR_NO                           = "floorNoT";
    private static final String KEY_PREFIX_TRANSFERRING_UNIT_NO                            = "unitNoT";
    private static final String KEY_PREFIX_TRANSFERRING_STREET_NAME                        = "streetNameT";
    private static final String KEY_PREFIX_TRANSFERRING_BUILDING_NAME                      = "buildingNameT";
    private static final String KEY_PREFIX_TRANSFERRING_CONTACT_PERSON_NAME                = "contactPersonNameT";
    private static final String KEY_PREFIX_TRANSFERRING_EMAIL_ADDRESS                      = "emailAddressT";
    private static final String KEY_PREFIX_TRANSFERRING_CONTACT_NO                         = "contactNoT";
    private static final String KEY_PREFIX_TRANSFERRING_EXPECTED_DATE                      = "expectedDateT";
    private static final String KEY_PREFIX_TRANSFERRING_COURIER_SERVICE_PROVIDER_NAME      = "courierServiceProviderNameT";
    private static final String KEY_PREFIX_TRANSFERRING_REMARKS                            = "remarksT";

    private static final String KEY_PREFIX_EXPORTING_FACILITY_NAME                      = "facNameE";
    private static final String KEY_PREFIX_EXPORTING_POSTAL_CODE                        = "postalCodeE";
    private static final String KEY_PREFIX_EXPORTING_BLOCK_NO                           = "blockNoE";
    private static final String KEY_PREFIX_EXPORTING_FLOOR_NO                           = "floorNoE";
    private static final String KEY_PREFIX_EXPORTING_UNIT_NO                            = "unitNoE";
    private static final String KEY_PREFIX_EXPORTING_STREET_NAME                        = "streetNameE";
    private static final String KEY_PREFIX_EXPORTING_COUNTRY                            = "countryE";
    private static final String KEY_PREFIX_EXPORTING_STATE                              = "stateE";
    private static final String KEY_PREFIX_EXPORTING_CONTACT_PERSON_NAME                = "contactPersonNameE";
    private static final String KEY_PREFIX_EXPORTING_EMAIL_ADDRESS                      = "emailAddressE";
    private static final String KEY_PREFIX_EXPORTING_CONTACT_NO                         = "contactNoE";
    private static final String KEY_PREFIX_EXPORTING_EXPECTED_DATE                      = "expectedDateE";
    private static final String KEY_PREFIX_EXPORTING_COURIER_SERVICE_PROVIDER_NAME      = "courierServiceProviderNameE";
    private static final String KEY_PREFIX_EXPORTING_REMARKS                            = "remarksE";


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
            String[] workTypes = ParamUtil.getStrings(request, KEY_PREFIX_WORK_TYPE + SEPARATOR +idx);
            if (workTypes != null && workTypes.length > 0) {
                info.setWorkType(new ArrayList<>(Arrays.asList(workTypes)));
            } else {
                info.setWorkType(new ArrayList<>(0));
            }
            info.setSampleWorkDetail(ParamUtil.getString(request, KEY_PREFIX_SAMPLE_WORK_DETAIL + SEPARATOR +idx));
            info.setProcurementMode(ParamUtil.getString(request, KEY_PREFIX_PROCUREMENT_MODE + SEPARATOR +idx));
            if (MasterCodeConstants.PROCUREMENT_MODE_LOCAL_TRANSFER.equals(info.getProcurementMode())) {
                info.setPostalCodeT(ParamUtil.getString(request, KEY_PREFIX_TRANSFERRING_POSTAL_CODE + SEPARATOR +idx));
                info.setAddressTypeT(ParamUtil.getString(request, KEY_PREFIX_TRANSFERRING_ADDRESS_TYPE + SEPARATOR +idx));
                info.setBlockNoT(ParamUtil.getString(request, KEY_PREFIX_TRANSFERRING_BLOCK_NO + SEPARATOR +idx));
                info.setFloorNoT(ParamUtil.getString(request, KEY_PREFIX_TRANSFERRING_FLOOR_NO + SEPARATOR +idx));
                info.setUnitNoT(ParamUtil.getString(request, KEY_PREFIX_TRANSFERRING_UNIT_NO + SEPARATOR +idx));
                info.setStreetNameT(ParamUtil.getString(request, KEY_PREFIX_TRANSFERRING_STREET_NAME + SEPARATOR +idx));
                info.setBuildingNameT(ParamUtil.getString(request, KEY_PREFIX_TRANSFERRING_BUILDING_NAME + SEPARATOR +idx));
                info.setContactPersonNameT(ParamUtil.getString(request, KEY_PREFIX_TRANSFERRING_CONTACT_PERSON_NAME + SEPARATOR +idx));
                info.setEmailAddressT(ParamUtil.getString(request, KEY_PREFIX_TRANSFERRING_EMAIL_ADDRESS + SEPARATOR +idx));
                info.setContactNoT(ParamUtil.getString(request, KEY_PREFIX_TRANSFERRING_CONTACT_NO + SEPARATOR +idx));
                info.setExpectedDateT(ParamUtil.getString(request, KEY_PREFIX_TRANSFERRING_EXPECTED_DATE + SEPARATOR +idx));
                info.setCourierServiceProviderNameT(ParamUtil.getString(request, KEY_PREFIX_TRANSFERRING_COURIER_SERVICE_PROVIDER_NAME + SEPARATOR +idx));
                info.setRemarksT(ParamUtil.getString(request, KEY_PREFIX_TRANSFERRING_REMARKS + SEPARATOR +idx));
            } else if (MasterCodeConstants.PROCUREMENT_MODE_IMPORT.equals(info.getProcurementMode())) {
                info.setFacNameE(ParamUtil.getString(request, KEY_PREFIX_EXPORTING_FACILITY_NAME + SEPARATOR + idx));
                info.setPostalCodeE(ParamUtil.getString(request, KEY_PREFIX_EXPORTING_POSTAL_CODE + SEPARATOR +idx));
                info.setBlockNoE(ParamUtil.getString(request, KEY_PREFIX_EXPORTING_BLOCK_NO + SEPARATOR +idx));
                info.setFloorNoE(ParamUtil.getString(request, KEY_PREFIX_EXPORTING_FLOOR_NO + SEPARATOR +idx));
                info.setUnitNoE(ParamUtil.getString(request, KEY_PREFIX_EXPORTING_UNIT_NO + SEPARATOR +idx));
                info.setStreetNameE(ParamUtil.getString(request, KEY_PREFIX_EXPORTING_STREET_NAME + SEPARATOR +idx));
                info.setCountryE(ParamUtil.getString(request, KEY_PREFIX_EXPORTING_COUNTRY + SEPARATOR + idx));
                info.setStateE(ParamUtil.getString(request, KEY_PREFIX_EXPORTING_STATE + SEPARATOR + idx));
                info.setContactPersonNameE(ParamUtil.getString(request, KEY_PREFIX_EXPORTING_CONTACT_PERSON_NAME + SEPARATOR +idx));
                info.setEmailAddressE(ParamUtil.getString(request, KEY_PREFIX_EXPORTING_EMAIL_ADDRESS + SEPARATOR +idx));
                info.setContactNoE(ParamUtil.getString(request, KEY_PREFIX_EXPORTING_CONTACT_NO + SEPARATOR +idx));
                info.setExpectedDateE(ParamUtil.getString(request, KEY_PREFIX_EXPORTING_EXPECTED_DATE + SEPARATOR +idx));
                info.setCourierServiceProviderNameE(ParamUtil.getString(request, KEY_PREFIX_EXPORTING_COURIER_SERVICE_PROVIDER_NAME + SEPARATOR +idx));
                info.setRemarksE(ParamUtil.getString(request, KEY_PREFIX_EXPORTING_REMARKS + SEPARATOR +idx));
            }
            info.setToxinRegulationDeclare(ParamUtil.getString(request, KEY_PREFIX_TOXIN_REGULATION_DECLARE + SEPARATOR + idx));
            addBatInfo(info);
        }
    }
}
