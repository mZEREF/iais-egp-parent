package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;

import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_FAC_OFFICER;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"name", "available", "validated", "dependNodes", "validationResultDto"})
public class FacilityOfficerDto extends Node {
    private String officerName;
    private String nationality;
    private String idType;
    private String idNumber;
    private String designation;
    private String contactNo;
    private String email;
    private String employmentStartDate;

    private ValidationResultDto validationResultDto;

    public FacilityOfficerDto(String name, Node[] dependNodes) {
        super(name, dependNodes);
    }

    public static FacilityOfficerDto getInstance(Node[] dependNodes) {
        return new FacilityOfficerDto(NODE_NAME_FAC_OFFICER, dependNodes);
    }


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
    public void needValidation() {
        super.needValidation();
        this.validationResultDto = null;
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
