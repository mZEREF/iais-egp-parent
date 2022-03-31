package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class PreviewSubmitDto extends ValidatableNodeValue {
    private String remarks;
    private String regulationDeclare;
    private String accuracyDeclare;

    @JsonIgnore
    private ValidationResultDto validationResultDto;


    @Override
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("facRegFeignClient", "validateFacilityPreviewSubmit", new Object[]{this});
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



    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRegulationDeclare() {
        return regulationDeclare;
    }

    public void setRegulationDeclare(String regulationDeclare) {
        this.regulationDeclare = regulationDeclare;
    }

    public String getAccuracyDeclare() {
        return accuracyDeclare;
    }

    public void setAccuracyDeclare(String accuracyDeclare) {
        this.accuracyDeclare = accuracyDeclare;
    }

    //    ---------------------------- request -> object ----------------------------------------------
    private static final String KEY_REMARKS = "remarks";
    private static final String KEY_REGULATION_DECLARE = "regulationDeclare";
    private static final String KEY_ACCURACY_DECLARE = "accuracyDeclare";

    public void reqObjMapping(HttpServletRequest request) {
        setRemarks(ParamUtil.getString(request, KEY_REMARKS));
        String[] regulationDeclareCheckBoxValues = ParamUtil.getStrings(request, KEY_REGULATION_DECLARE);
        if (regulationDeclareCheckBoxValues != null && regulationDeclareCheckBoxValues.length > 0) {
            setRegulationDeclare("Y");
        } else {
            setRegulationDeclare("");
        }
        String[] accuracyDeclareCheckBoxValues = ParamUtil.getStrings(request, KEY_ACCURACY_DECLARE);
        if (accuracyDeclareCheckBoxValues != null && accuracyDeclareCheckBoxValues.length > 0) {
            setAccuracyDeclare("Y");
        } else {
            setAccuracyDeclare("");
        }
    }
}
