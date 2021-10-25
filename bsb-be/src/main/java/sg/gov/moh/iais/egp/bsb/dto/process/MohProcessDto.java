package sg.gov.moh.iais.egp.bsb.dto.process;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import java.util.Date;

/**
 * @author : LiRan
 * @date : 2021/8/23
 */
@Data
public class MohProcessDto {

    private String applicationId;

    private String processStatus;

    private Date approvalDate;

    private String remarks;

    private String reason;

    private String finalRemarks;

    private String riskLevel;

    private String riskLevelComments;

    private String erpReportDt;

    private String redTeamingReportDt;

    private String lentivirusReportDt;

    private String internalInspectionReportDt;

    private String validityStartDt;

    private String validityEndDt;

    private String selectedAfc;

    private String applicationNo;

    private String appStatus;

    private String processDecision;

    private String actionBy;

    private String processType;

    private String profiles;

    private ValidationResultDto validationResultDto;

    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("processClient", "validateMohProcessDto", new Object[]{this});
        return validationResultDto.isPass();
    }

    public String retrieveValidationResult() {
        if (this.validationResultDto == null) {
            throw new IllegalStateException("This DTO is not validated");
        }
        return this.validationResultDto.toErrorMsg();
    }
}
