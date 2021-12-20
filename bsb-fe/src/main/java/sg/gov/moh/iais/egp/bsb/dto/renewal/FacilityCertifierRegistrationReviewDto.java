package sg.gov.moh.iais.egp.bsb.dto.renewal;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : LiRan
 * @date : 2021/12/20
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FacilityCertifierRegistrationReviewDto extends ValidatableNodeValue  {
    private String remarks;
    private String require;
    private String accuracy;
    private String effectiveDateOfChange;

    private ValidationResultDto validationResultDto;

    @Override
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("cerRegFeignClient", "validateRenewalFacCerRegReview", new Object[]{this});
        return validationResultDto.isPass();
    }

    @Override
    public String retrieveValidationResult() {
        if (this.validationResultDto == null) {
            throw new IllegalStateException("This DTO is not validated");
        }
        return this.validationResultDto.toErrorMsg();
    }


    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRequire() {
        return require;
    }

    public void setRequire(String require) {
        this.require = require;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getEffectiveDateOfChange() {
        return effectiveDateOfChange;
    }

    public void setEffectiveDateOfChange(String effectiveDateOfChange) {
        this.effectiveDateOfChange = effectiveDateOfChange;
    }
    //    ---------------------------- request -> object ----------------------------------------------

    private static final String KEY_REMARKS = "remarks";
    private static final String KEY_REQUIRE= "require";
    private static final String KEY_ACCURACY = "accuracy";
    private static final String KEY_EFFECTIVE_DATE_OF_CHANGE = "effectiveDateOfChange";

    public void reqObjMapping(HttpServletRequest request) {
        setRemarks(ParamUtil.getString(request, KEY_REMARKS));
        setRequire(ParamUtil.getString(request, KEY_REQUIRE));
        setAccuracy(ParamUtil.getString(request, KEY_ACCURACY));
        setEffectiveDateOfChange(ParamUtil.getString(request, KEY_EFFECTIVE_DATE_OF_CHANGE));
    }
}
