package sg.gov.moh.iais.egp.bsb.dto.register.approval;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author : LiRan
 * @date : 2022/3/17
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApprovalToActivityDto extends ValidatableNodeValue {
    private List<String> facActivityTypes;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    public ApprovalToActivityDto() {
        facActivityTypes = new ArrayList<>();
    }

    @Override
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto)  SpringReflectionUtils.invokeBeanMethod(ApprovalBatAndActivityConstants.FEIGN_CLIENT, "validateApprovalToActivityDto", new Object[]{this});
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

    public List<String> getFacActivityTypes() {
        return new ArrayList<>(facActivityTypes);
    }

    public void setFacActivityTypes(List<String> facActivityTypes) {
        this.facActivityTypes = new ArrayList<>(facActivityTypes);
    }

    public void replaceActivityTypes(String[] activityTypes) {
        clearActivityTypes();
        addActivityTypes(activityTypes);
    }

    public void clearActivityTypes() {
        this.facActivityTypes.clear();
    }

    public void addActivityTypes(String[] activityTypes) {
        if (activityTypes != null && activityTypes.length > 0) {
            this.facActivityTypes.addAll(Arrays.asList(activityTypes));
        }
    }

    //    ---------------------------- request -> object ----------------------------------------------

    private static final String KEY_FAC_ACTIVITY_TYPES = "facActivityTypes";

    public void reqObjMapping(HttpServletRequest request) {
        this.replaceActivityTypes(ParamUtil.getStrings(request, KEY_FAC_ACTIVITY_TYPES));
    }
}
