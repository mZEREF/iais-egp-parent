package sg.gov.moh.iais.egp.bsb.dto.facilityapproval;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.FacilityBiologicalAgentInfo;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.FacilityDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Map;

@Data
public class FacApprovalProcessDto implements Serializable {
    private String taskId;
    private String appId;
    private String appStatus;
    private String prevRole;
    private String lastRecommendation;
    private String lastRemarks;

    private Map<String,String> batAgentIdApprovedMap;

    private String remarks;

    private String selectMohUser;

    private String processingDecision;

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
    private static final String KEY_PROCESSING_DECISION = "processingDecision";
    private static final String KEY_SELECT_MOH_USER = "selectMohUser";
    public void reqObjMapping(HttpServletRequest request){
        FacilityDetailsInfo facilityDetailsInfo = (FacilityDetailsInfo) ParamUtil.getSessionAttr(request, ModuleCommonConstants.KEY_FACILITY_DETAILS_INFO);
        for (FacilityBiologicalAgentInfo info : facilityDetailsInfo.getFacilityBiologicalAgentInfoList()) {
            batAgentIdApprovedMap.put(info.getId(),ParamUtil.getString(request,info.getId()));
        }
        this.remarks = ParamUtil.getString(request,KEY_REMARKS);
        this.processingDecision = ParamUtil.getString(request,KEY_PROCESSING_DECISION);
        this.selectMohUser = ParamUtil.getString(request,KEY_SELECT_MOH_USER);
    }
}
