package sg.gov.moh.iais.egp.bsb.dto.approvalApp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : LiRan
 * @date : 2021/10/20
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PrimaryDocDto extends ValidatableNodeValue {
    private Map<String, File> uploadedFiles;

    @JsonIgnore
    private ValidationResultDto validationResultDto;


    public PrimaryDocDto() {
        uploadedFiles = new HashMap<>();
    }

    @Override
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("approvalAppFeignClient", "validateFacilityPrimaryDocs", new Object[]{this});
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
