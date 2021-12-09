package sg.gov.moh.iais.egp.bsb.dto.audit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocMeta;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author Zhu Tangtang
 **/
@Data
public class FacilitySubmitSelfAuditDto implements Serializable {
    //facility
    private String facId;
    private String certifierRegId;
    private String facName;
    private String facAddress;
    private String facClassification;
    private String activityType;
    private String processType;
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
    private Collection<PrimaryDocDto.DocRecordInfo> docRecordInfos;

    @JsonIgnore
    private PrimaryDocDto primaryDocDto;

    private List<PrimaryDocDto.DocRecordInfo> savedInfos;

    private List<DocMeta> docMetas;

    @JsonIgnore
    private List<PrimaryDocDto.NewDocInfo> newDocInfos;
    @JsonIgnore
    private String docType;
    @JsonIgnore
    private String repoIdNewString;

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
