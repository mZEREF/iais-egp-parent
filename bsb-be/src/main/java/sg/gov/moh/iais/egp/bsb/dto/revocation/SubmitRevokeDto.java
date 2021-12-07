package sg.gov.moh.iais.egp.bsb.dto.revocation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Zhu Tangtang
 */
@Data
public class SubmitRevokeDto implements Serializable {
    //approval
    private String approvalNo;
    private String approvalId;
    private String approvalStatus;
    //facility
    private String facName;
    private String facId;
    private String facAddress;
    private String facClassification;
    private String activityType;
    //application
    private String appId;
    private String processType;
    private String appType;
    private String status;
    private Date applicationDt;
    private String applicationNo;
    //applicationMisc
    private String reason;
    private String reasonContent;
    private String remarks;
    private String aoRemarks;
    private String doRemarks;
    //
    private String doReason;
    private String aoDecision;
    //
    private String loginUser;
    private String module;
    private String taskId;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    // validate
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("revokeFeignClient", "validateRevoke", new Object[]{this});
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
