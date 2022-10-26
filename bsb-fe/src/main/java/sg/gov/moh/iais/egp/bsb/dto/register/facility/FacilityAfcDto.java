package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import com.ecquaria.sz.commons.util.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import sg.gov.moh.iais.egp.bsb.common.edit.FieldEditableJudger;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class FacilityAfcDto implements ValidatableNodeValue {
    private String appointed;
    private String afc;
    private String selectReason;
    private Set<String> last2AfcSet;
    private String certAppId;
    private String hasDraft;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    public FacilityAfcDto() {
        last2AfcSet = new HashSet<>();
    }

    @Override
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("facRegFeignClient", "validateFacilityAfc", new Object[]{this});
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

    public String getAppointed() {
        return appointed;
    }

    public void setAppointed(String appointed) {
        this.appointed = appointed;
    }

    public String getAfc() {
        return afc;
    }

    public void setAfc(String afc) {
        this.afc = afc;
    }

    public String getSelectReason() {
        return selectReason;
    }

    public void setSelectReason(String selectReason) {
        this.selectReason = selectReason;
    }

    public Set<String> getLast2AfcSet() {
        return last2AfcSet;
    }

    public void setLast2AfcSet(Set<String> last2AfcSet) {
        this.last2AfcSet = last2AfcSet;
    }

    public String getCertAppId() {
        return certAppId;
    }

    public void setCertAppId(String certAppId) {
        this.certAppId = certAppId;
    }

    public String getHasDraft() {
        return hasDraft;
    }

    public void setHasDraft(String hasDraft) {
        this.hasDraft = hasDraft;
    }

    private static final String KEY_HAS_FACILITY_APPOINTED_AN_APPROVED_FACILITY_CERTIFIER = "appointed";
    private static final String KEY_APPROVED_FACILITY_CERTIFIER_SELECTION = "afc";
    private static final String KEY_REASON_FOR_SELECT_THIS_AFC = "selectReason";

    public void reqObjectMapping(HttpServletRequest request, FieldEditableJudger editableJudger) {
        if (editableJudger.editable(KEY_HAS_FACILITY_APPOINTED_AN_APPROVED_FACILITY_CERTIFIER)) {
            this.appointed = ParamUtil.getString(request, KEY_HAS_FACILITY_APPOINTED_AN_APPROVED_FACILITY_CERTIFIER);
        }
        if (MasterCodeConstants.YES.equals(this.appointed)) {
            if (editableJudger.editable(KEY_APPROVED_FACILITY_CERTIFIER_SELECTION)) {
                this.afc = ParamUtil.getString(request, KEY_APPROVED_FACILITY_CERTIFIER_SELECTION);
            }
            if (last2AfcSet != null && last2AfcSet.contains(this.afc)) {
                if (editableJudger.editable(KEY_REASON_FOR_SELECT_THIS_AFC)) {
                    setSelectReason(ParamUtil.getString(request, KEY_REASON_FOR_SELECT_THIS_AFC));
                }
            }
        }
    }
}
