package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;
import sg.gov.moh.iais.egp.common.annotation.RfcAttributeDesc;

import javax.servlet.http.HttpServletRequest;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class FacilityOfficerDto extends ValidatableNodeValue {
    private String officerEntityId;

    @RfcAttributeDesc(aliasName = "iais.bsbfe.facOfficer.name")
    private String officerName;

    @RfcAttributeDesc(aliasName = "iais.bsbfe.facOfficer.name")
    private String nationality;

    @RfcAttributeDesc(aliasName = "iais.bsbfe.facOfficer.name")
    private String idType;

    @RfcAttributeDesc(aliasName = "iais.bsbfe.facOfficer.name")
    private String idNumber;

    @RfcAttributeDesc(aliasName = "iais.bsbfe.facOfficer.name")
    private String designation;

    @RfcAttributeDesc(aliasName = "iais.bsbfe.facOfficer.name")
    private String contactNo;

    @RfcAttributeDesc(aliasName = "iais.bsbfe.facOfficer.name")
    private String email;

    @RfcAttributeDesc(aliasName = "iais.bsbfe.facOfficer.name")
    private String employmentStartDate;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    @Override
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("facRegFeignClient", "validateFacilityOfficer", new Object[]{this});
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


    public String getOfficerEntityId() {
        return officerEntityId;
    }

    public void setOfficerEntityId(String officerEntityId) {
        this.officerEntityId = officerEntityId;
    }

    public String getOfficerName() {
        return officerName;
    }

    public void setOfficerName(String officerName) {
        this.officerName = officerName;
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

    public String getEmploymentStartDate() {
        return employmentStartDate;
    }

    public void setEmploymentStartDate(String employmentStartDate) {
        this.employmentStartDate = employmentStartDate;
    }


//    ---------------------------- request -> object ----------------------------------------------

    private static final String KEY_OFFICIER_NAME = "officerName";
    private static final String KEY_ID_TYPE = "idType";
    private static final String KEY_ID_NUMBER = "idNumber";
    private static final String KEY_NATIONALITY = "nationality";
    private static final String KEY_DESIGNATION = "designation";
    private static final String KEY_CONTACT_NO = "contactNo";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_EMP_START_DATE = "employmentStartDate";

    public void reqObjMapping(HttpServletRequest request) {
        setOfficerName(ParamUtil.getString(request, KEY_OFFICIER_NAME));
        setIdType(ParamUtil.getString(request, KEY_ID_TYPE));
        setIdNumber(ParamUtil.getString(request, KEY_ID_NUMBER));
        setNationality(ParamUtil.getString(request, KEY_NATIONALITY));
        setDesignation(ParamUtil.getString(request, KEY_DESIGNATION));
        setContactNo(ParamUtil.getString(request, KEY_CONTACT_NO));
        setEmail(ParamUtil.getString(request, KEY_EMAIL));
        setEmploymentStartDate(ParamUtil.getString(request, KEY_EMP_START_DATE));
    }
}
