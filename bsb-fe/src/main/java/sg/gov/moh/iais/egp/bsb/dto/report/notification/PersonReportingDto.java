package sg.gov.moh.iais.egp.bsb.dto.report.notification;

import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author YiMing
 * @version 2021/12/2 10:43
 **/

@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonReportingDto extends ValidatableNodeValue {
    private String personReportEntityId;
    private String name;
    private String orgName;
    private String address;
    private String officeTelNo;
    private String mobileTelNo;
    private String email;
    private String roleDesignation;
    private String facName;
    private String facId;
    private String facType;
    private String activityType;
    private String incidentDate;
    private String occurrenceTime;
    private String location;
    private List<String> batName;
    private Map<String,String> batNameMap;
    private String incidentDesc;
    private String batReleasePossibility;
    private String releaseExtent;
    private String releaseMode;
    private String incidentPersonInvolved;
    private String incidentPersonInvolvedCount;
    private String emergencyResponse;
    private String immCorrectiveAction;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    public PersonReportingDto() {
        batName = new ArrayList<>();
    }

    @Override
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("incidentFeignClient", "validatePersonReporting", new Object[]{this});
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

    public String getPersonReportEntityId() {
        return personReportEntityId;
    }

    public void setPersonReportEntityId(String personReportEntityId) {
        this.personReportEntityId = personReportEntityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOfficeTelNo() {
        return officeTelNo;
    }

    public void setOfficeTelNo(String officeTelNo) {
        this.officeTelNo = officeTelNo;
    }

    public String getMobileTelNo() {
        return mobileTelNo;
    }

    public void setMobileTelNo(String mobileTelNo) {
        this.mobileTelNo = mobileTelNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRoleDesignation() {
        return roleDesignation;
    }

    public void setRoleDesignation(String roleDesignation) {
        this.roleDesignation = roleDesignation;
    }

    public String getFacName() {
        return facName;
    }

    public void setFacName(String facName) {
        this.facName = facName;
    }

    public String getFacType() {
        return facType;
    }

    public void setFacType(String facType) {
        this.facType = facType;
    }

    public String getFacId() {
        return facId;
    }

    public void setFacId(String facId) {
        this.facId = facId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getIncidentDate() {
        return incidentDate;
    }

    public void setIncidentDate(String incidentDate) {
        this.incidentDate = incidentDate;
    }

    public String getOccurrenceTime() {
        return occurrenceTime;
    }

    public void setOccurrenceTime(String occurrenceTime) {
        this.occurrenceTime = occurrenceTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getBatName() {
        return new ArrayList<>(batName);
    }

    public void setBatName(List<String> batName) {
        if(batName != null && !batName.isEmpty()){
            this.batName = new ArrayList<>(batName);
        }
    }

    public String getIncidentDesc() {
        return incidentDesc;
    }

    public void setIncidentDesc(String incidentDesc) {
        this.incidentDesc = incidentDesc;
    }

    public String getBatReleasePossibility() {
        return batReleasePossibility;
    }

    public void setBatReleasePossibility(String batReleasePossibility) {
        this.batReleasePossibility = batReleasePossibility;
    }

    public String getReleaseExtent() {
        return releaseExtent;
    }

    public void setReleaseExtent(String releaseExtent) {
        this.releaseExtent = releaseExtent;
    }

    public String getReleaseMode() {
        return releaseMode;
    }

    public void setReleaseMode(String releaseMode) {
        this.releaseMode = releaseMode;
    }

    public String getIncidentPersonInvolved() {
        return incidentPersonInvolved;
    }

    public void setIncidentPersonInvolved(String incidentPersonInvolved) {
        this.incidentPersonInvolved = incidentPersonInvolved;
    }

    public String getIncidentPersonInvolvedCount() {
        return incidentPersonInvolvedCount;
    }

    public void setIncidentPersonInvolvedCount(String incidentPersonInvolvedCount) {
        this.incidentPersonInvolvedCount = incidentPersonInvolvedCount;
    }

    public String getEmergencyResponse() {
        return emergencyResponse;
    }

    public void setEmergencyResponse(String emergencyResponse) {
        this.emergencyResponse = emergencyResponse;
    }

    public String getImmCorrectiveAction() {
        return immCorrectiveAction;
    }

    public void setImmCorrectiveAction(String immCorrectiveAction) {
        this.immCorrectiveAction = immCorrectiveAction;
    }

    public Map<String, String> getBatNameMap() {
        return batNameMap;
    }

    public void setBatNameMap(Map<String, String> batNameMap) {
        this.batNameMap = batNameMap;
    }

    private static final String KEY_REPORTING_PERSON_NAME = "name";
    private static final String KEY_REPORTING_PERSON_ORG  = "orgName";
    private static final String KEY_REPORTING_PERSON_ADDRESS = "address";
    private static final String KEY_REPORTING_PERSON_OFFICE_NO = "officeTelNo";
    private static final String KEY_REPORTING_PERSON_MOBILE_NO = "mobileTelNo";
    private static final String KEY_REPORTING_PERSON_EMAIL = "email";
    private static final String KEY_REPORTING_PERSON_ROLE = "roleDesignation";
    private static final String KEY_FACILITY_NAME = "facName";
    private static final String KEY_FACILITY_TYPE = "facType";
    private static final String KEY_ACTIVITY_TYPE = "activityType";
    private static final String KEY_INCIDENT_DATE = "incidentDate";
    private static final String KEY_OCCURRENCE_TIME_HH = "occurrenceTimeH";
    private static final String KEY_OCCURRENCE_TIME_MM = "occurrenceTimeM";
    private static final String KEY_INCIDENT_LOCATION = "location";
    private static final String KEY_INCIDENT_BAT = "batName";
    private static final String KEY_INCIDENT_DESCRIPTION= "incidentDesc";
    private static final String KEY_BAT_RELEASE_POSSIBILITY = "batReleasePossibility";
    private static final String KEY_RELEASE_EXTENT = "releaseExtent";
    private static final String KEY_RELEASE_MODE = "releaseMode";
    private static final String KEY_INCIDENT_PERSON_INVOLVED = "incidentPersonInvolved";
    private static final String KEY_INCIDENT_PERSON_INVOLVED_COUNT = "incidentPersonInvolvedCount";
    private static final String KEY_EMERGENCY_RESPONSE = "emergencyResponse";
    private static final String KEY_IMMEDIATE_CORRECTIVE_ACTION = "immCorrectiveAction";

    public void reqObjMapping(HttpServletRequest request){
        this.name = ParamUtil.getString(request,KEY_REPORTING_PERSON_NAME);
        this.orgName = ParamUtil.getString(request,KEY_REPORTING_PERSON_ORG);
        this.address = ParamUtil.getString(request,KEY_REPORTING_PERSON_ADDRESS);
        this.officeTelNo = ParamUtil.getString(request,KEY_REPORTING_PERSON_OFFICE_NO);
        this.mobileTelNo = ParamUtil.getString(request,KEY_REPORTING_PERSON_MOBILE_NO);
        this.email = ParamUtil.getString(request,KEY_REPORTING_PERSON_EMAIL);
        this.roleDesignation = ParamUtil.getString(request,KEY_REPORTING_PERSON_ROLE);
        this.facName = ParamUtil.getString(request,KEY_FACILITY_NAME);
        this.facId = MaskUtil.unMaskValue("id",this.facName);
        this.facType = ParamUtil.getString(request,KEY_FACILITY_TYPE);
        this.activityType = ParamUtil.getString(request,KEY_ACTIVITY_TYPE);
        this.incidentDate = ParamUtil.getString(request,KEY_INCIDENT_DATE);
        String occurrenceTimeH = ParamUtil.getString(request,KEY_OCCURRENCE_TIME_HH);
        String occurrenceTimeM = ParamUtil.getString(request,KEY_OCCURRENCE_TIME_MM);
        this.occurrenceTime = occurrenceTimeH+":"+occurrenceTimeM;
        this.location = ParamUtil.getString(request,KEY_INCIDENT_LOCATION);
        String[] batNames = ParamUtil.getStrings(request,KEY_INCIDENT_BAT);
        if(batNames != null && batNames.length>0){
            this.batName = new ArrayList<>(Arrays.asList(ParamUtil.getStrings(request,KEY_INCIDENT_BAT)));
        }
        this.incidentDesc = ParamUtil.getString(request,KEY_INCIDENT_DESCRIPTION);
        this.batReleasePossibility = ParamUtil.getString(request,KEY_BAT_RELEASE_POSSIBILITY);
        this.releaseExtent = ParamUtil.getString(request,KEY_RELEASE_EXTENT);
        this.releaseMode = ParamUtil.getString(request,KEY_RELEASE_MODE);
        this.incidentPersonInvolved = ParamUtil.getString(request,KEY_INCIDENT_PERSON_INVOLVED);
        this.incidentPersonInvolvedCount = ParamUtil.getString(request,KEY_INCIDENT_PERSON_INVOLVED_COUNT);
        this.emergencyResponse = ParamUtil.getString(request,KEY_EMERGENCY_RESPONSE);
        this.immCorrectiveAction = ParamUtil.getString(request,KEY_IMMEDIATE_CORRECTIVE_ACTION);
    }

}
