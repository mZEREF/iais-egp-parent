package sg.gov.moh.iais.egp.bsb.dto.register.approval;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApprovalSelectionDto implements Serializable{
    private boolean isEnteredInbox;

    private String draftAppNo;

    private String facilityId;

    @JsonIgnore
    private String facilityName;

    private String processType;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto)  SpringReflectionUtils.invokeBeanMethod(ApprovalBatAndActivityConstants.FEIGN_CLIENT, "validateApprovalSelectionDto", new Object[]{this});
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


    public boolean isEnteredInbox() {
        return isEnteredInbox;
    }

    public void setEnteredInbox(boolean enteredInbox) {
        isEnteredInbox = enteredInbox;
    }

    public String getDraftAppNo() {
        return draftAppNo;
    }

    public void setDraftAppNo(String draftAppNo) {
        this.draftAppNo = draftAppNo;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }



    //    ---------------------------- request -> object ----------------------------------------------

    private static final String KEY_FACILITY_NAME     = "facilityName";
    private static final String KEY_PROCESS_TYPE      = "processType";

    public void reqObjMapping(HttpServletRequest request) {
        String newFacilityName = ParamUtil.getString(request, KEY_FACILITY_NAME);
        String newProcessType = ParamUtil.getString(request, KEY_PROCESS_TYPE);
        this.setFacilityName(newFacilityName);
        this.setProcessType(newProcessType);
    }
}
