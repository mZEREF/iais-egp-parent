package sg.gov.moh.iais.egp.bsb.dto.incident;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.incident.entity.MohInfoDto;
import sg.gov.moh.iais.egp.bsb.dto.incident.entity.ProcessHistoryDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * @author YiMing
 * @version 2021/12/28 9:08
 **/
@Data
public class ProcessingDto implements Serializable {
    private String applicationId;
    private String taskId;
    private String currentStatus;
    private String remarks;
    private String decision;
    private String approvalOfficer;
    private Map<String, MohInfoDto> newRemarkMap;
    private List<ProcessHistoryDto> processHistoryList;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("incidentFeignClient", "validateProcessingDto", new Object[]{this});
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



    private static final String KEY_REMARKS = "remarks";
    private static final String KEY_DECISION = "decision";
    private static final String KEY_APPROVAL_OFFICER = "approvalOfficer";
    public void reqObjMapping(HttpServletRequest request){
        this.remarks = ParamUtil.getString(request,KEY_REMARKS);
        this.decision = ParamUtil.getString(request,KEY_DECISION);
        this.approvalOfficer = ParamUtil.getString(request,KEY_APPROVAL_OFFICER);
    }
}
