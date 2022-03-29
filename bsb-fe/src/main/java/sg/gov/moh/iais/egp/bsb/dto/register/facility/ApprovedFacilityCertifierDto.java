package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import com.ecquaria.sz.commons.util.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApprovedFacilityCertifierDto extends ValidatableNodeValue {
    private String appointed;
    private String afc;
    private String selectReason;

    @JsonIgnore
    private ValidationResultDto validationResultDto;


    @Override
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("facRegFeignClient", "validateApprovedFacilityCertifier", new Object[]{this});
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

    private static final String KEY_HAS_FACILITY_APPOINTED_AN_APPROVED_FACILITY_CERTIFIER = "appointed";
    private static final String KEY_APPROVED_FACILITY_CERTIFIER_SELECTION = "afc";
    private static final String KEY_REASON_FOR_SELECT_THIS_AFC = "selectReason";
    public void reqObjectMapping(HttpServletRequest request){
        setAppointed(ParamUtil.getString(request,KEY_HAS_FACILITY_APPOINTED_AN_APPROVED_FACILITY_CERTIFIER));
        setAfc(ParamUtil.getString(request,KEY_APPROVED_FACILITY_CERTIFIER_SELECTION));
        setSelectReason(ParamUtil.getString(request,KEY_REASON_FOR_SELECT_THIS_AFC));
    }
}
