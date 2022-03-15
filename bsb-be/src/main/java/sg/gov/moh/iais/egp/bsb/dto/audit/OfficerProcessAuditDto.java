package sg.gov.moh.iais.egp.bsb.dto.audit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Zhu Tangtang
 **/
@Data
public class OfficerProcessAuditDto implements Serializable {
    //facility
    private String facName;
    private String facAddress;
    private String facClassification;
    private String activityType;
    //audit
    private String remarks;
    private String changeReason;
    private String auditType;
    private Date auditDate;
    private String auditId;
    private String auditStatus;
    private String auditOutCome;
    private String finalRemarks;
    private String cancelReason;
    //audit app
    private Date requestAuditDate;
    private String doReason;
    private String doRemarks;
    private String doDecision;
    private String auditAppId;
    private String auditAppStatus;
    private String aoReason;
    private String aoRemarks;
    private String aoDecision;
    //task
    private String taskId;
    //application
    private String appStatus;
    private String applicationNo;
    //audit app history
    private String actionBy;
    private String processDecision;

    private String module;

    private List<ProcessHistoryDto> historyDtos;
    private List<DocDisplayDto> displayDtos;

    @Data
    public static class ProcessHistoryDto implements Serializable{
        private String userName;
        private String statusUpdate;
        private String remarks;
        private Date lastUpdated;
    }

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    // validate
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("auditBEFeignClient", "validateOfficerAuditDt", new Object[]{this});
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
