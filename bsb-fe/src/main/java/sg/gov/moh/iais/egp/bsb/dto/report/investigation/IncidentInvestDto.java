package sg.gov.moh.iais.egp.bsb.dto.report.investigation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;

import java.util.List;

/**
 * @author YiMing
 * @version 2021/12/14 20:45
 **/

@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IncidentInvestDto extends ValidatableNodeValue {
    private String backgroundInfo;
    private String incidentDesc;
    private List<String> incidentCauses;
    private String otherCause;
    private String explainCause;
    private String measure;
    private String implementDate;

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

    public String getBackgroundInfo() {
        return backgroundInfo;
    }

    public void setBackgroundInfo(String backgroundInfo) {
        this.backgroundInfo = backgroundInfo;
    }

    public String getIncidentDesc() {
        return incidentDesc;
    }

    public void setIncidentDesc(String incidentDesc) {
        this.incidentDesc = incidentDesc;
    }

    public List<String> getIncidentCauses() {
        return incidentCauses;
    }

    public void setIncidentCauses(List<String> incidentCauses) {
        this.incidentCauses = incidentCauses;
    }

    public String getOtherCause() {
        return otherCause;
    }

    public void setOtherCause(String otherCause) {
        this.otherCause = otherCause;
    }

    public String getExplainCause() {
        return explainCause;
    }

    public void setExplainCause(String explainCause) {
        this.explainCause = explainCause;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getImplementDate() {
        return implementDate;
    }

    public void setImplementDate(String implementDate) {
        this.implementDate = implementDate;
    }
}
