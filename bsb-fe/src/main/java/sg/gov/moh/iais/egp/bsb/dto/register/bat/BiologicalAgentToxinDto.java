package sg.gov.moh.iais.egp.bsb.dto.register.bat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import sg.gov.moh.iais.egp.bsb.common.node.simple.BranchedValidationNodeValue;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class BiologicalAgentToxinDto extends BATInfoPageDto implements BranchedValidationNodeValue {
    @Getter @Setter
    private String activityEntityId;
    @Getter @Setter
    private String activityType;

    @JsonIgnore
    private String validationProfile = ValidationConstants.VALIDATION_PROFILE_NEW;
    @JsonIgnore
    private ValidationResultDto validationResultDto;


    public BiologicalAgentToxinDto() {
        super();
    }

    public BiologicalAgentToxinDto(String activityType) {
        this();
        this.activityType = activityType;
    }


    @Override
    public String getValidationProfile() {
        return validationProfile;
    }

    @Override
    public void setValidationProfile(String profile) {
        validationProfile = profile;
    }

    @Override
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("facRegFeignClient", "validateFacilityBiologicalAgentToxin", new Object[]{this, validationProfile});
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
}
