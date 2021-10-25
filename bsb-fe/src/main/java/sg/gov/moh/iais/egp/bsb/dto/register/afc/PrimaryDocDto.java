package sg.gov.moh.iais.egp.bsb.dto.register.afc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Maps;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.Map;



@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"name", "available", "validated", "dependNodes", "validationResultDto"})
public class PrimaryDocDto extends ValidatableNodeValue {
    private Map<String, File> uploadedFiles;

    private ValidationResultDto validationResultDto;

    public PrimaryDocDto() {
        uploadedFiles = Maps.newHashMapWithExpectedSize(0);
    }

    @Override
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("cerRegFeignClient", "validateCerPrimaryDocs", new Object[]{this});
        return validationResultDto.isPass();
    }

    @Override
    public String retrieveValidationResult() {
        if (this.validationResultDto == null) {
            throw new IllegalStateException("This DTO is not validated");
        }
        return this.validationResultDto.toErrorMsg();
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
