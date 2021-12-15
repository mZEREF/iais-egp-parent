package sg.gov.moh.iais.egp.bsb.dto.report.investigation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;

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
}
