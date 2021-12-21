package sg.gov.moh.iais.egp.bsb.dto.report.investigation;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author YiMing
 * @version 2021/12/14 20:45
 **/

@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IncidentInvestDto extends ValidatableNodeValue {

    @Data
    @NoArgsConstructor
    public static class IncidentCauseDto implements Serializable {
        private String incidentCause;
        private String explainCause;
        private String otherCause;
        private String measure;
        private String implementDate;
    }
    private String backgroundInfo;
    private String incidentDesc;
    private List<IncidentCauseDto> incidentCauses;

    public IncidentInvestDto() {
        incidentCauses = new ArrayList<>();
    }

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

    public List<IncidentCauseDto> getIncidentCauses() {
        return incidentCauses;
    }

    public void setIncidentCauses(List<IncidentCauseDto> incidentCauses) {
        this.incidentCauses = incidentCauses;
    }


    public Map<String,IncidentCauseDto> getIncidentCauseMap(){
        if(org.springframework.util.CollectionUtils.isEmpty(this.incidentCauses)){
            return new LinkedHashMap<>();
        }
        return CollectionUtils.uniqueIndexMap(incidentCauses,IncidentCauseDto::getIncidentCause);
    }

    public Set<String> getCauseSet(){
        if(org.springframework.util.CollectionUtils.isEmpty(this.incidentCauses)){
            return new HashSet<>();
        }
        return incidentCauses.stream().map(IncidentCauseDto::getIncidentCause).collect(Collectors.toSet());
    }

    private static final String KEY_BACKGROUND_INFORMATION = "backgroundInfo";
    private static final String KEY_INCIDENT_DESCRIPTION = "incidentDesc";
    private static final String KEY_INCIDENT_CAUSES = "incidentCauses";
    private static final String KEY_EXPLAIN_CAUSE = "explainCause";
    private static final String KEY_OTHER_CAUSE = "otherCause";
    private static final String SEPARATOR = "--v--";
    private static final String KEY_MEASURE= "measure";
    private static final String KEY_IMPLEMENT_DATE = "implementDate";
    public void reqObjMapping(HttpServletRequest request){
        this.backgroundInfo = ParamUtil.getString(request,KEY_BACKGROUND_INFORMATION);
        this.incidentDesc = ParamUtil.getString(request,KEY_INCIDENT_DESCRIPTION);
        String[] causes = ParamUtil.getStrings(request,KEY_INCIDENT_CAUSES);
        if(causes != null && causes.length > 0){
            for (String cause : causes) {
                IncidentCauseDto incidentCauseDto = new IncidentCauseDto();
                incidentCauseDto.setIncidentCause(cause);
                incidentCauseDto.setExplainCause(ParamUtil.getString(request,KEY_EXPLAIN_CAUSE+SEPARATOR+cause));
                incidentCauseDto.setMeasure(ParamUtil.getString(request,KEY_MEASURE+SEPARATOR+cause));
                incidentCauseDto.setImplementDate(ParamUtil.getString(request,KEY_IMPLEMENT_DATE+SEPARATOR+cause));
                if(MasterCodeConstants.CAUSE_OF_INCIDENT_OTHERS.equals(cause)){
                    String otherCause = ParamUtil.getString(request,KEY_OTHER_CAUSE);
                    incidentCauseDto.setOtherCause(otherCause);
                }
                this.incidentCauses.add(incidentCauseDto);
            }
        }
    }
}
