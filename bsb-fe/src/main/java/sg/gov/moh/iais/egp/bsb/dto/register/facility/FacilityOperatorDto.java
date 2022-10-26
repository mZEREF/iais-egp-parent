package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import sg.gov.moh.iais.egp.bsb.common.edit.FieldEditableJudger;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class FacilityOperatorDto implements ValidatableNodeValue {

    private String facOperator;

    private String salutation;

    private String designeeName;

    private String nationality;

    private String idType;

    private String idNumber;

    private String designation;

    private String contactNo;

    private String email;

    private String employmentStartDt;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    @Override
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("facRegFeignClient", "validateFacilityOperator", new Object[]{this});
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


    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getFacOperator() {
        return facOperator;
    }

    public void setFacOperator(String facOperator) {
        this.facOperator = facOperator;
    }

    public String getDesigneeName() {
        return designeeName;
    }

    public void setDesigneeName(String designeeName) {
        this.designeeName = designeeName;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmploymentStartDt() {
        return employmentStartDt;
    }

    public void setEmploymentStartDt(String employmentStartDt) {
        this.employmentStartDt = employmentStartDt;
    }

    //    ---------------------------- request -> object ----------------------------------------------
    private static final String KEY_FAC_OPERATOR = "facOperator";
    private static final String KEY_SALUTATION = "salutation";
    private static final String KEY_OP_NAME = "operatorName";
    private static final String KEY_ID_TYPE = "idType";
    private static final String KEY_ID_NUMBER = "idNumber";
    private static final String KEY_NATIONALITY = "nationality";
    private static final String KEY_DESIGNATION = "designation";
    private static final String KEY_CONTACT_NO = "contactNo";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_EMP_START_DATE = "employmentStartDt";

    public void reqObjMapping(HttpServletRequest request, FieldEditableJudger editableJudger) {
        if (editableJudger.editable(KEY_FAC_OPERATOR)) {
            setFacOperator(ParamUtil.getString(request, KEY_FAC_OPERATOR));
        }
        if (editableJudger.editable(KEY_SALUTATION)) {
            setSalutation(ParamUtil.getString(request, KEY_SALUTATION));
        }
        if (editableJudger.editable(KEY_OP_NAME)) {
            setDesigneeName(ParamUtil.getString(request, KEY_OP_NAME));
        }
        if (editableJudger.editable(KEY_ID_TYPE)) {
            setIdType(ParamUtil.getString(request, KEY_ID_TYPE));
        }
        if (editableJudger.editable(KEY_ID_NUMBER)) {
            setIdNumber(ParamUtil.getString(request, KEY_ID_NUMBER));
        }
        if (editableJudger.editable(KEY_NATIONALITY)) {
            setNationality(ParamUtil.getString(request, KEY_NATIONALITY));
        }
        if (editableJudger.editable(KEY_DESIGNATION)) {
            setDesignation(ParamUtil.getString(request, KEY_DESIGNATION));
        }
        if (editableJudger.editable(KEY_CONTACT_NO)) {
            setContactNo(ParamUtil.getString(request, KEY_CONTACT_NO));
        }
        if (editableJudger.editable(KEY_EMAIL)) {
            setEmail(ParamUtil.getString(request, KEY_EMAIL));
        }
        if (editableJudger.editable(KEY_EMP_START_DATE)) {
            setEmploymentStartDt(ParamUtil.getString(request, KEY_EMP_START_DATE));
        }
    }
}
