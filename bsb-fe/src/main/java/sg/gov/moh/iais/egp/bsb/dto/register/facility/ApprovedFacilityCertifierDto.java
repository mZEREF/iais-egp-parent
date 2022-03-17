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
    private String hasAppointedCertifier;
    private String certifierSelection;
    private String afcSelectedReason;

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

    public String getHasAppointedCertifier() {
        return hasAppointedCertifier;
    }

    public void setHasAppointedCertifier(String hasAppointedCertifier) {
        this.hasAppointedCertifier = hasAppointedCertifier;
    }

    public String getCertifierSelection() {
        return certifierSelection;
    }

    public void setCertifierSelection(String certifierSelection) {
        this.certifierSelection = certifierSelection;
    }

    public String getAfcSelectedReason() {
        return afcSelectedReason;
    }

    public void setAfcSelectedReason(String afcSelectedReason) {
        this.afcSelectedReason = afcSelectedReason;
    }

    private static final String KEY_HAS_FACILITY_APPOINTED_AN_APPROVED_FACILITY_CERTIFIER = "isAppointedCertifier";
    private static final String KEY_APPROVED_FACILITY_CERTIFIER_SELECTION = "certifierSelection";
    private static final String KEY_REASON_FOR_SELECT_THIS_AFC = "afcSelectedReason";
    public void reqObjectMapping(HttpServletRequest request){
        setHasAppointedCertifier(ParamUtil.getString(request,KEY_HAS_FACILITY_APPOINTED_AN_APPROVED_FACILITY_CERTIFIER));
        setCertifierSelection(ParamUtil.getString(request,KEY_APPROVED_FACILITY_CERTIFIER_SELECTION));
        setAfcSelectedReason(ParamUtil.getString(request,KEY_REASON_FOR_SELECT_THIS_AFC));
    }
}
