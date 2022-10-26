package sg.gov.moh.iais.egp.bsb.dto.register.approval;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import sg.gov.moh.iais.egp.bsb.common.node.simple.BranchedValidationNodeValue;
import sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants;
import sg.gov.moh.iais.egp.bsb.dto.register.bat.BATInfoPageDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApprovalToPossessDto extends BATInfoPageDto implements BranchedValidationNodeValue {
    /* For validation */
    @Setter @Getter
    private String facilityId;

    @JsonIgnore
    private ValidationResultDto validationResultDto;
    @JsonIgnore
    private String validationProfile;

    public ApprovalToPossessDto() {
    }

    public ApprovalToPossessDto(String profile) {
        this.validationProfile = profile;
    }

    @Override
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod(ApprovalBatAndActivityConstants.FEIGN_CLIENT, "validateApprovalToPossessBatDto", new Object[]{this, validationProfile});
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

    public void setValidationResultDto(ValidationResultDto validationResultDto) {
        this.validationResultDto = validationResultDto;
    }

    @Override
    public String getValidationProfile() {
        return validationProfile;
    }

    @Override
    public void setValidationProfile(String profile) {
        this.validationProfile = profile;
    }
}
