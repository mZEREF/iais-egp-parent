package sg.gov.moh.iais.egp.bsb.dto.approvalApp;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : LiRan
 * @date : 2021/10/20
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PreviewSubmitDto extends ValidatableNodeValue {
    private String remarks;
    private String approvedFacCertifier;
    private String reason;
    private String declare;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    @Override
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("approvalAppFeignClient", "validateFacilityPreviewSubmit", new Object[]{this});
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getApprovedFacCertifier() {
        return approvedFacCertifier;
    }

    public void setApprovedFacCertifier(String approvedFacCertifier) {
        this.approvedFacCertifier = approvedFacCertifier;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDeclare() {
        return declare;
    }

    public void setDeclare(String declare) {
        this.declare = declare;
    }



//    ---------------------------- request -> object ----------------------------------------------

    private static final String KEY_REMARKS = "remarks";
    private static final String KEY_APROOVED_CERTIFIER = "approvedFacCertifier";
    private static final String KEY_REASON = "reason";
    private static final String KEY_DECLARE = "declare";

    public void reqObjMapping(HttpServletRequest request) {
        setRemarks(ParamUtil.getString(request, KEY_REMARKS));
        setApprovedFacCertifier(ParamUtil.getString(request, KEY_APROOVED_CERTIFIER));
        setReason(ParamUtil.getString(request, KEY_REASON));
        String[] declareCheckBoxValues = ParamUtil.getStrings(request, KEY_DECLARE);
        if (declareCheckBoxValues != null && declareCheckBoxValues.length > 0) {
            setDeclare("Y");
        } else {
            setDeclare("");
        }
    }
}
