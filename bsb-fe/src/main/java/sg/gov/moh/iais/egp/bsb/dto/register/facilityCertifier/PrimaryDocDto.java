package sg.gov.moh.iais.egp.bsb.dto.register.facilityCertifier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Maps;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.FacCertifierRegisterConstants.NODE_NAME_FAC_PRIMARY_DOCUMENT;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"name", "available", "validated", "dependNodes", "validationResultDto"})
public class PrimaryDocDto extends Node {
    private Map<String, File> uploadedFiles;

    private ValidationResultDto validationResultDto;

    public PrimaryDocDto(String name, Node[] dependNodes) {
        super(name, dependNodes);
        uploadedFiles = Maps.newHashMapWithExpectedSize(0);
    }

    public static PrimaryDocDto getInstance(Node[] dependNodes) {
        return new PrimaryDocDto(NODE_NAME_FAC_PRIMARY_DOCUMENT, dependNodes);
    }


    @Override
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("facRegFeignClient", "validateFacilityPrimaryDocs", new Object[]{this});
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
    public void needValidation() {
        super.needValidation();
        this.validationResultDto = null;
    }


    public Map<String, File> getUploadedFiles() {
        return new HashMap<>(uploadedFiles);
    }

    public void setUploadedFiles(Map<String, File> uploadedFiles) {
        this.uploadedFiles = new HashMap<>(uploadedFiles);
    }



//    ---------------------------- request -> object ----------------------------------------------


    public void reqObjMapping(HttpServletRequest request) {
        // do nothing now
    }
}
