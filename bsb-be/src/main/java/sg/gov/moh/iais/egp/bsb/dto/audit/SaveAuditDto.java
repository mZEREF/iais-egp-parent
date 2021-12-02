package sg.gov.moh.iais.egp.bsb.dto.audit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @author Zhu Tangtang
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SaveAuditDto implements Serializable {

    @Data
    public static class SaveAudit implements Serializable {
        private String remarks;
        private String auditType;
        private String status;
        private String approvalId;
        private String processType;
        @JsonIgnore
        private String facName;
        @JsonIgnore
        private String facClassification;
        @JsonIgnore
        private String activityType;
    }

    private List<SaveAudit> saveAudits;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    // validate
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("auditBEFeignClient", "validateManualAudit", new Object[]{this});
        return validationResultDto.isPass();
    }

    public String retrieveValidationResult() {
        if (this.validationResultDto == null) {
            throw new IllegalStateException("This DTO is not validated");
        }
        return this.validationResultDto.toErrorMsg();
    }

    public void clearValidationResult() {
        this.validationResultDto = null;
    }

}
