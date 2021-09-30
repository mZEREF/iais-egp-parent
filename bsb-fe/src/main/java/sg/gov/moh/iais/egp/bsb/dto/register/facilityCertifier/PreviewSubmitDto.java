package sg.gov.moh.iais.egp.bsb.dto.register.facilityCertifier;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.constant.FacCertifierRegisterConstants;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;


import javax.servlet.http.HttpServletRequest;




@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"name", "available", "validated", "dependNodes", "validationResultDto"})
public class PreviewSubmitDto extends Node {
    private String remarks;
    private String require;
    private String accuracy;

    private ValidationResultDto validationResultDto;

    public PreviewSubmitDto(String name, Node[] dependNodes) {
        super(name, dependNodes);
    }

    public static PreviewSubmitDto getInstance(Node[] dependNodes) {
        return new PreviewSubmitDto(FacCertifierRegisterConstants.NODE_NAME_CER_PREVIEW_SUBMIT, dependNodes);
    }

    @Override
    public boolean doValidation() {
//        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("facRegFeignClient", "validateFacilityPreviewSubmit", new Object[]{this});
//        return validationResultDto.isPass();
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
    public void needValidation() {
        super.needValidation();
        this.validationResultDto = null;
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
    //    ---------------------------- request -> object ----------------------------------------------

    private static final String KEY_REMARKS = "remarks";
    private static final String KEY_REQUIRE= "require";
    private static final String KEY_ACCURACY = "accuracy";

    public void reqObjMapping(HttpServletRequest request) {
        setRemarks(ParamUtil.getString(request, KEY_REMARKS));
        setRequire(ParamUtil.getString(request, KEY_REQUIRE));
        setAccuracy(ParamUtil.getString(request, KEY_ACCURACY));
    }
}
