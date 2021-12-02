package sg.gov.moh.iais.egp.bsb.dto.audit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Zhu Tangtang
 **/
@Data
public class FacilitySubmitSelfAuditDto implements Serializable {
    //facility
    private String facName;
    private String facAddress;
    private String facClassification;
    private String activityType;
    //audit
    @JsonIgnore
    private Date lastAuditDate;
    private String remarks;
    private String changeReason;
    private String auditType;
    private Date auditDate;
    private String auditId;
    private String auditStatus;
    private String scenarioCategory;
    //audit app
    private Date requestAuditDate;
    private String doReason;
    private String doRemarks;
    private String doDecision;
    private String auditAppId;
    private String auditAppStatus;
    private String aoReason;
    private String aoRemarks;
    //task
    private String taskId;
    //application
    private String appStatus;

    //just use to determine which function it is
    private String module;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    // validate
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("auditFeignClient", "validateAuditDt", new Object[]{this});
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
