package sg.gov.moh.iais.egp.bsb.dto.report.investigation;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;

import javax.servlet.http.HttpServletRequest;

/**
 * @author YiMing
 * @version 2021/12/15 8:57
 **/

@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MedicalInvestDto extends ValidatableNodeValue {

    private String personnelName;
    private String medicalUpdate;
    private String testResult;
    private String medicalFollowup;
    private String fpDuration;
    private String isIdentified;
    private String addPersonnelName;
    private String involvementDesc;
    private String description;
    private String addTestResult;
    private String addMedicalFollowup;
    private String addFpDuration;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    @Override
    public boolean doValidation() {
        return true;
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

    public String getPersonnelName() {
        return personnelName;
    }

    public void setPersonnelName(String personnelName) {
        this.personnelName = personnelName;
    }

    public String getMedicalUpdate() {
        return medicalUpdate;
    }

    public void setMedicalUpdate(String medicalUpdate) {
        this.medicalUpdate = medicalUpdate;
    }

    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }

    public String getMedicalFollowup() {
        return medicalFollowup;
    }

    public void setMedicalFollowup(String medicalFollowup) {
        this.medicalFollowup = medicalFollowup;
    }

    public String getFpDuration() {
        return fpDuration;
    }

    public void setFpDuration(String fpDuration) {
        this.fpDuration = fpDuration;
    }

    public String getIsIdentified() {
        return isIdentified;
    }

    public void setIsIdentified(String isIdentified) {
        this.isIdentified = isIdentified;
    }

    public String getAddPersonnelName() {
        return addPersonnelName;
    }

    public void setAddPersonnelName(String addPersonnelName) {
        this.addPersonnelName = addPersonnelName;
    }

    public String getInvolvementDesc() {
        return involvementDesc;
    }

    public void setInvolvementDesc(String involvementDesc) {
        this.involvementDesc = involvementDesc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddTestResult() {
        return addTestResult;
    }

    public void setAddTestResult(String addTestResult) {
        this.addTestResult = addTestResult;
    }

    public String getAddMedicalFollowup() {
        return addMedicalFollowup;
    }

    public void setAddMedicalFollowup(String addMedicalFollowup) {
        this.addMedicalFollowup = addMedicalFollowup;
    }

    public String getAddFpDuration() {
        return addFpDuration;
    }

    public void setAddFpDuration(String addFpDuration) {
        this.addFpDuration = addFpDuration;
    }

    private static final String KEY_PERSONNEL_NAME = "personnelName";
    private static final String KEY_MEDICAL_UPDATE = "medicalUpdate";
    private static final String KEY_TEST_RESULT = "testResult";
    private static final String KEY_MEDICAL_FOLLOW_UP = "medicalFollowup";
    private static final String KEY_FOLLOW_UP_DURATION = "fpDuration";
    private static final String KEY_IS_IDENTIFIED= "isIdentified";
    private static final String KEY_ADDITIONAL_PERSONNEL_NAME = "addPersonnelName";
    private static final String KEY_INVOLVEMENT_DESCRIPTION= "involvementDesc";
    private static final String KEY_DESCRIPTION= "description";
    private static final String KEY_ADDITIONAL_TEST_RESULT = "addTestResult";
    private static final String KEY_ADDITIONAL_MEDICAL_FOLLOW_UP  = "addMedicalFollowup";
    private static final String KEY_ADDITIONAL_FOLLOW_UP_DURATION  = "addFpDuration";

    public void reqObjMapping(HttpServletRequest request){
        this.personnelName = ParamUtil.getString(request,KEY_PERSONNEL_NAME);
        this.medicalUpdate = ParamUtil.getString(request,KEY_MEDICAL_UPDATE);
        this.testResult = ParamUtil.getString(request,KEY_TEST_RESULT);
        this.medicalFollowup = ParamUtil.getString(request,KEY_MEDICAL_FOLLOW_UP);
        this.fpDuration = ParamUtil.getString(request,KEY_FOLLOW_UP_DURATION);
        this.isIdentified = ParamUtil.getString(request,KEY_IS_IDENTIFIED);
        this.addPersonnelName = ParamUtil.getString(request,KEY_ADDITIONAL_PERSONNEL_NAME);
        this.involvementDesc  = ParamUtil.getString(request,KEY_INVOLVEMENT_DESCRIPTION);
        this.description = ParamUtil.getString(request,KEY_DESCRIPTION);
        this.addTestResult = ParamUtil.getString(request,KEY_ADDITIONAL_TEST_RESULT);
        this.addMedicalFollowup = ParamUtil.getString(request,KEY_ADDITIONAL_MEDICAL_FOLLOW_UP);
        this.addFpDuration = ParamUtil.getString(request,KEY_ADDITIONAL_FOLLOW_UP_DURATION);

    }
}
