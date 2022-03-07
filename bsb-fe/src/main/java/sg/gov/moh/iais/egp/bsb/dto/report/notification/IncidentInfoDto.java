package sg.gov.moh.iais.egp.bsb.dto.report.notification;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author YiMing
 * @version 2021/12/2 9:24
 **/

@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IncidentInfoDto extends ValidatableNodeValue {

    private String draftAppNo;
    private String incidentEntityId;
    private String referenceNo;
    private String incidentReporting;
    private String incidentType;
    private String typeOtherDetail;
    private List<String> childTypes;
    private Map<String,String> typesMap;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    public IncidentInfoDto() {
        this.childTypes = new ArrayList<>();
    }

    @Override
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("reportableEventFeignClient", "validateIncidentInfo", new Object[]{this});
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

    public String getDraftAppNo() {
        return draftAppNo;
    }

    public void setDraftAppNo(String draftAppNo) {
        this.draftAppNo = draftAppNo;
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

    public Map<String, String> getTypesMap() {
        return typesMap;
    }

    public void setTypesMap(Map<String, String> typesMap) {
        this.typesMap = typesMap;
    }

    public void setChildTypes(List<String> childTypes) {
        this.childTypes = new ArrayList<>(childTypes);
    }

    private void clearChildTypes(){
        this.childTypes.clear();
    }
    private void addChildTypes(String[] childTypeStrings) {
        if (childTypeStrings != null && childTypeStrings.length > 0) {
            this.childTypes.addAll(Arrays.asList(childTypeStrings));
        }
    }


    public void replaceChildTypes(String[] childTypeStrings){
        clearChildTypes();
        addChildTypes(childTypeStrings);
    }

    private static final String KEY_REFERENCE_NO = "referenceNo";
    private static final String KEY_INCIDENT_REPORTING = "incidentReporting";
    private static final String KEY_INCIDENT_TYPE = "incidentType";
    private static final String KEY_INCIDENT_CHILD_SAFE_TYPES = "childSafeTypes";
    private static final String KEY_INCIDENT_CHILD_SECUR_TYPES = "childSecurTypes";
    private static final String KEY_INCIDENT_CHILD_GENER_TYPES = "childGenerTypes";
    private static final String KEY_TYPE_OTHER_DETAIL = "typeOtherDetail";

    public void reqObjMapping(HttpServletRequest request){
        this.referenceNo = ParamUtil.getString(request,KEY_REFERENCE_NO);
        this.incidentReporting = ParamUtil.getString(request,KEY_INCIDENT_REPORTING);
        String incType = ParamUtil.getString(request,KEY_INCIDENT_TYPE);
        this.incidentType = incType;
        this.typeOtherDetail = ParamUtil.getString(request,KEY_TYPE_OTHER_DETAIL);
        String[]  childTypeStrings = null;
        if(StringUtils.hasLength(incType)){
            switch (incType){
                case MasterCodeConstants.INCIDENT_TYPE_BIO_SAFETY:
                    childTypeStrings = ParamUtil.getStrings(request,KEY_INCIDENT_CHILD_SAFE_TYPES);
                    break;
                case MasterCodeConstants.INCIDENT_TYPE_BIO_SECURITY:
                    childTypeStrings = ParamUtil.getStrings(request,KEY_INCIDENT_CHILD_SECUR_TYPES);
                    break;
                case MasterCodeConstants.INCIDENT_TYPE_GENERAL_SAFETY:
                    childTypeStrings = ParamUtil.getStrings(request,KEY_INCIDENT_CHILD_GENER_TYPES);
                    break;
                default:break;
            }
        }
        replaceChildTypes(childTypeStrings);
    }
}
