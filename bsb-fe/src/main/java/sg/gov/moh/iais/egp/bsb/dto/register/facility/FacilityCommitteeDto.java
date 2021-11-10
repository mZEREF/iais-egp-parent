package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;
import sg.gov.moh.iais.egp.common.annotation.RfcAttributeDesc;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class FacilityCommitteeDto extends ValidatableNodeValue {
    @Data
    @NoArgsConstructor
    public static class BioSafetyCommitteePersonnel implements Serializable {
        private String committeeEntityId;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.FacilityCommittee.name")
        private String name;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.FacilityCommittee.nationality")
        private String nationality;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.FacilityCommittee.idType")
        private String idType;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.FacilityCommittee.idNumber")
        private String idNumber;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.FacilityCommittee.designation")
        private String designation;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.FacilityCommittee.contactNo")
        private String contactNo;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.FacilityCommittee.email")
        private String email;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.FacilityCommittee.employmentStartDate")
        private String employmentStartDate;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.FacilityCommittee.expertiseArea")
        private String expertiseArea;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.FacilityCommittee.role")
        private String role;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.FacilityCommittee.employee")
        private String employee;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.FacilityCommittee.externalCompName")
        private String externalCompName;
    }

    private String inputMethod;

    @RfcAttributeDesc(aliasName = "iais.bsbfe.FacilityCommittee.addOrDelete")
    private List<BioSafetyCommitteePersonnel> facCommitteePersonnelList;

    @JsonIgnore
    private ValidationResultDto validationResultDto;


    public FacilityCommitteeDto() {
        facCommitteePersonnelList = new ArrayList<>();
        facCommitteePersonnelList.add(new BioSafetyCommitteePersonnel());
    }

    @Override
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("facRegFeignClient", "validateFacilityCommittee", new Object[]{this});
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


    public List<BioSafetyCommitteePersonnel> getFacCommitteePersonnelList() {
        return new ArrayList<>(facCommitteePersonnelList);
    }

    public void clearCommitteePersonnel() {
        this.facCommitteePersonnelList.clear();
    }

    public void addCommitteePersonnel(BioSafetyCommitteePersonnel personnel) {
        this.facCommitteePersonnelList.add(personnel);
    }

    public void setFacCommitteePersonnelList(List<BioSafetyCommitteePersonnel> facCommitteePersonnelList) {
        this.facCommitteePersonnelList = new ArrayList<>(facCommitteePersonnelList);
    }

    public String getInputMethod() {
        return inputMethod;
    }

    public void setInputMethod(String inputMethod) {
        this.inputMethod = inputMethod;
    }


//    ---------------------------- request -> object ----------------------------------------------

    private static final String SEPARATOR = "--v--";
    private static final String KEY_INPUT_METHOD = "inputMethod";
    private static final String KEY_SECTION_AMT = "sectionAmt";
    private static final String KEY_PREFIX_NAME = "name";
    private static final String KEY_PREFIX_ID_TYPE = "idType";
    private static final String KEY_PREFIX_ID_NUMBER = "idNumber";
    private static final String KEY_PREFIX_NATIONALITY = "nationality";
    private static final String KEY_PREFIX_DESIGNATION = "designation";
    private static final String KEY_PREFIX_CONTACT_NO = "contactNo";
    private static final String KEY_PREFIX_EMAIL = "email";
    private static final String KEY_PREFIX_EMP_START_DT = "employmentStartDate";
    private static final String KEY_PREFIX_EXPERTISE_AREA = "expertiseArea";
    private static final String KEY_PREFIX_ROLE = "role";
    private static final String KEY_PREFIX_IS_EMPLOYEE = "employee";
    private static final String KEY_PREFIX_EXTERNAL_COMP_NAME = "externalCompName";

    public void reqObjMapping(HttpServletRequest request) {
        setInputMethod(ParamUtil.getString(request, KEY_INPUT_METHOD));
        int amt = ParamUtil.getInt(request, KEY_SECTION_AMT);
        clearCommitteePersonnel();
        for (int i = 0; i < amt; i++) {
            BioSafetyCommitteePersonnel personnel = new BioSafetyCommitteePersonnel();
            personnel.setName(ParamUtil.getString(request, KEY_PREFIX_NAME + SEPARATOR +i));
            personnel.setIdType(ParamUtil.getString(request, KEY_PREFIX_ID_TYPE + SEPARATOR +i));
            personnel.setIdNumber(ParamUtil.getString(request, KEY_PREFIX_ID_NUMBER + SEPARATOR +i));
            personnel.setNationality(ParamUtil.getString(request, KEY_PREFIX_NATIONALITY + SEPARATOR +i));
            personnel.setDesignation(ParamUtil.getString(request, KEY_PREFIX_DESIGNATION + SEPARATOR +i));
            personnel.setContactNo(ParamUtil.getString(request, KEY_PREFIX_CONTACT_NO + SEPARATOR +i));
            personnel.setEmail(ParamUtil.getString(request, KEY_PREFIX_EMAIL + SEPARATOR +i));
            personnel.setEmploymentStartDate(ParamUtil.getString(request, KEY_PREFIX_EMP_START_DT + SEPARATOR +i));
            personnel.setExpertiseArea(ParamUtil.getString(request, KEY_PREFIX_EXPERTISE_AREA + SEPARATOR +i));
            personnel.setRole(ParamUtil.getString(request, KEY_PREFIX_ROLE + SEPARATOR +i));
            personnel.setEmployee(ParamUtil.getString(request, KEY_PREFIX_IS_EMPLOYEE + SEPARATOR +i));
            if ("N".equals(personnel.getEmployee())) {
                personnel.setExternalCompName(ParamUtil.getString(request, KEY_PREFIX_EXTERNAL_COMP_NAME + SEPARATOR +i));
            }
            addCommitteePersonnel(personnel);
        }
    }
}
