package sg.gov.moh.iais.egp.bsb.dto.register.approval;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import sg.gov.moh.iais.egp.bsb.common.node.simple.BranchedValidationNodeValue;
import sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class PreviewDto implements BranchedValidationNodeValue {
    private String processType;

    private String hasProcModeImport;

    private String remarks;
    //I will ensure that the packaging of the materials and the transfer are carried out in accordance with the requirements stipulated under the BATA Transportation Regulations, the BATA and any other related regulations.
    private String declare1;
    //I, hereby declare that all the information I have provided here is true and accurate. If any of the information given herein changes or becomes inaccurate in any way, I shall immediately notify MOH Biosafety Branch of such change or inaccuracy.
    private String declare2;

    private String declare3;


    @JsonIgnore
    private ValidationResultDto validationResultDto;
    @JsonIgnore
    private String validationProfile;

    @Override
    public String getValidationProfile() {
        return validationProfile;
    }

    @Override
    public void setValidationProfile(String profile) {
        this.validationProfile = profile;
    }

    @Override
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod(ApprovalBatAndActivityConstants.FEIGN_CLIENT, "validatePreviewDto", new Object[]{this});
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

    public String getDeclare1() {
        return declare1;
    }

    public void setDeclare1(String declare1) {
        this.declare1 = declare1;
    }

    public String getDeclare2() {
        return declare2;
    }

    public void setDeclare2(String declare2) {
        this.declare2 = declare2;
    }

    public String getDeclare3() {
        return declare3;
    }

    public void setDeclare3(String declare3) {
        this.declare3 = declare3;
    }

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }

    public String getHasProcModeImport() {
        return hasProcModeImport;
    }

    public void setHasProcModeImport(String hasProcModeImport) {
        this.hasProcModeImport = hasProcModeImport;
    }

    //    ---------------------------- request -> object ----------------------------------------------

    private static final String KEY_REMARKS = "remarks";
    private static final String KEY_DECLARE_1 = "declare1";
    private static final String KEY_DECLARE_2 = "declare2";
    private static final String KEY_DECLARE_3 = "declare3";

    public void reqObjMapping(HttpServletRequest request) {
        setProcessType((String) ParamUtil.getSessionAttr(request, ApprovalBatAndActivityConstants.KEY_PROCESS_TYPE));
        setRemarks(ParamUtil.getString(request, KEY_REMARKS));
        String[] declareArray1 = ParamUtil.getStrings(request, KEY_DECLARE_1);
        String[] declareArray2 = ParamUtil.getStrings(request, KEY_DECLARE_2);
        String[] declareArray3 = ParamUtil.getStrings(request, KEY_DECLARE_3);
        if (declareArray1 != null && declareArray1.length > 0) {
            setDeclare1("Y");
        } else {
            setDeclare1("");
        }
        if (declareArray2 != null && declareArray2.length > 0) {
            setDeclare2("Y");
        } else {
            setDeclare2("");
        }
        if (declareArray3 != null && declareArray3.length > 0) {
            setDeclare3("Y");
        } else {
            setDeclare3("");
        }
    }
}
