package sg.gov.moh.iais.egp.bsb.dto.report.notification;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YiMing
 * @version 2021/12/2 9:24
 **/

@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IncidentInfoDto extends ValidatableNodeValue {

    private String incidentEntityId;
    private String referenceNo;
    private String incidentReporting;
    private String incidentType;
    private String typeOtherDetail;
    private List<String> childTypes;

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

    public String getIncidentEntityId() {
        return incidentEntityId;
    }

    public void setIncidentEntityId(String incidentEntityId) {
        this.incidentEntityId = incidentEntityId;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getIncidentReporting() {
        return incidentReporting;
    }

    public void setIncidentReporting(String incidentReporting) {
        this.incidentReporting = incidentReporting;
    }

    public String getIncidentType() {
        return incidentType;
    }

    public void setIncidentType(String incidentType) {
        this.incidentType = incidentType;
    }

    public String getTypeOtherDetail() {
        return typeOtherDetail;
    }

    public void setTypeOtherDetail(String typeOtherDetail) {
        this.typeOtherDetail = typeOtherDetail;
    }

    public List<String> getChildTypes() {
        return new ArrayList<>(childTypes);
    }

    public void setChildTypes(List<String> childTypes) {
        this.childTypes = new ArrayList<>(childTypes);
    }

    private static final String KEY_REFERENCE_NO = "referenceNo";
    private static final String KEY_INCIDENT_REPORTING = "incidentReporting";
    private static final String KEY_INCIDENT_TYPE = "incidentType";
    private static final String KEY_INCIDENT_CHILD_TYPES = "childTypes";
    private static final String KEY_TYPE_OTHER_DETAIL = "typeOtherDetail";

    public void reqObjMapping(HttpServletRequest request){
        this.referenceNo = ParamUtil.getString(request,KEY_REFERENCE_NO);
        this.incidentReporting = ParamUtil.getString(request,KEY_INCIDENT_REPORTING);
        this.incidentType = ParamUtil.getString(request,KEY_INCIDENT_TYPE);
        this.typeOtherDetail = ParamUtil.getString(request,KEY_TYPE_OTHER_DETAIL);
//        this.childTypes = new ArrayList<>(ParamUtil.getListStrings(request,KEY_INCIDENT_CHILD_TYPES));
    }
}
