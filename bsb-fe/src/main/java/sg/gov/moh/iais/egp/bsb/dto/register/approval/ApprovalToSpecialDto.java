package sg.gov.moh.iais.egp.bsb.dto.register.approval;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
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
public class ApprovalToSpecialDto extends ValidatableNodeValue {

    @Data
    @NoArgsConstructor
    public static class WorkActivity implements Serializable {
        private String intendedWorkActivity;
        private String startDate;
        private String endDate;
        private String activityRemarks;
    }

    private String schedule;
    private String batName;
    private String projectName;
    private String principalInvestigatorName;

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

    private String activityEntityId;
    private String activityType;

    private List<WorkActivity> workActivities;

    private ValidationResultDto validationResultDto;

    public ApprovalToSpecialDto() {
        workActivities = new ArrayList<>();
    }

    public ApprovalToSpecialDto(String activityType) {
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

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getBatName() {
        return batName;
    }

    public void setBatName(String batName) {
        this.batName = batName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getPrincipalInvestigatorName() {
        return principalInvestigatorName;
    }

    public void setPrincipalInvestigatorName(String principalInvestigatorName) {
        this.principalInvestigatorName = principalInvestigatorName;
    }

    public String getProcurementMode() {
        return procurementMode;
    }

    public void setProcurementMode(String procurementMode) {
        this.procurementMode = procurementMode;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getBlockNo() {
        return blockNo;
    }

    public void setBlockNo(String blockNo) {
        this.blockNo = blockNo;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getFloorNo() {
        return floorNo;
    }

    public void setFloorNo(String floorNo) {
        this.floorNo = floorNo;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(String expectedDate) {
        this.expectedDate = expectedDate;
    }

    public String getCourierServiceProviderName() {
        return courierServiceProviderName;
    }

    public void setCourierServiceProviderName(String courierServiceProviderName) {
        this.courierServiceProviderName = courierServiceProviderName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<WorkActivity> getWorkActivities() {
        return new ArrayList<>(workActivities);
    }

    public void clearWorkActivities() {
        this.workActivities.clear();
    }

    public void addWorkActivity(WorkActivity workActivity) {
        this.workActivities.add(workActivity);
    }

    public void setWorkActivities(List<WorkActivity> workActivities) {
        this.workActivities = new ArrayList<>(workActivities);
    }

    // ---------------------------- request -> object ----------------------------------------------

    private static final String SEPARATOR                                     = "--v--";
    private static final String KEY_SECTION_IDXES                             = "sectionIdx";
    private static final String KEY_PREFIX_INTENDED_WORK_ACTIVITY             = "intendedWorkActivity";
    private static final String KEY_PREFIX_START_DATE                         = "startDate";
    private static final String KEY_PREFIX_END_DATE                           = "endDate";
    private static final String KEY_PREFIX_ACTIVITY_REMARKS                   = "activityRemarks";
    private static final String KEY_PREFIX_SCHEDULE                           = "schedule";
    private static final String KEY_PREFIX_BAT_NAME                           = "batName";
    private static final String KEY_PREFIX_PROJECT_NAME                       = "projectName";
    private static final String KEY_PREFIX_PRINCIPAL_INVESTIGATOR_NAME        = "principalInvestigatorName";
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
        clearWorkActivities();
        String idxes = ParamUtil.getString(request, KEY_SECTION_IDXES);
        String[] idxArr = idxes.trim().split(" +");
        for (String idx : idxArr) {
            WorkActivity activity = new WorkActivity();
            activity.setIntendedWorkActivity(ParamUtil.getString(request, KEY_PREFIX_INTENDED_WORK_ACTIVITY + SEPARATOR +idx));
            activity.setStartDate(ParamUtil.getString(request, KEY_PREFIX_START_DATE + SEPARATOR +idx));
            activity.setEndDate(ParamUtil.getString(request, KEY_PREFIX_END_DATE + SEPARATOR +idx));
            activity.setActivityRemarks(ParamUtil.getString(request, KEY_PREFIX_ACTIVITY_REMARKS + SEPARATOR +idx));
            addWorkActivity(activity);
        }
        setSchedule(ParamUtil.getString(request, KEY_PREFIX_SCHEDULE));
        setBatName(ParamUtil.getString(request, KEY_PREFIX_BAT_NAME));
        setProjectName(ParamUtil.getString(request, KEY_PREFIX_PROJECT_NAME));
        setPrincipalInvestigatorName(ParamUtil.getString(request, KEY_PREFIX_PRINCIPAL_INVESTIGATOR_NAME));
        setProcurementMode(ParamUtil.getString(request, KEY_PREFIX_PROCUREMENT_MODE));
        setFacilityName(ParamUtil.getString(request, KEY_PREFIX_FACILITY_NAME));
        setPostalCode(ParamUtil.getString(request, KEY_PREFIX_POSTAL_CODE));
        setAddressType(ParamUtil.getString(request, KEY_PREFIX_ADDRESS_TYPE));
        setBlockNo(ParamUtil.getString(request, KEY_PREFIX_BLOCK_NO));
        setHouseNo(ParamUtil.getString(request, KEY_PREFIX_HOUSE_NO));
        setFloorNo(ParamUtil.getString(request, KEY_PREFIX_FLOOR_NO));
        setUnitNo(ParamUtil.getString(request, KEY_PREFIX_UNIT_NO));
        setStreetName(ParamUtil.getString(request, KEY_PREFIX_STREET_NAME));
        setBuildingName(ParamUtil.getString(request, KEY_PREFIX_BUILDING_NAME));
        setContactPersonName(ParamUtil.getString(request, KEY_PREFIX_CONTACT_PERSON_NAME));
        setEmailAddress(ParamUtil.getString(request, KEY_PREFIX_EMAIL_ADDRESS));
        setContactNo(ParamUtil.getString(request, KEY_PREFIX_CONTACT_NO));
        setExpectedDate(ParamUtil.getString(request, KEY_PREFIX_EXPECTED_DATE));
        setCourierServiceProviderName(ParamUtil.getString(request, KEY_PREFIX_COURIER_SERVICE_PROVIDER_NAME));
        setRemarks(ParamUtil.getString(request, KEY_PREFIX_REMARKS));
    }
}
