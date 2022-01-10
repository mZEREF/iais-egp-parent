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
public class FacilityAuthoriserDto extends ValidatableNodeValue {
    @Data
    @NoArgsConstructor
    public static class FacilityAuthorisedPersonnel implements Serializable {
        private String authEntityId;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facAuthoriser.name")
        private String name;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facAuthoriser.nationality")
        private String nationality;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facAuthoriser.idType")
        private String idType;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facAuthoriser.idNumber")
        private String idNumber;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facAuthoriser.designation")
        private String designation;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facAuthoriser.contactNo")
        private String contactNo;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facAuthoriser.email")
        private String email;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facAuthoriser.employmentStartDate")
        private String employmentStartDt;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facAuthoriser.employmentPeriod")
        private String employmentPeriod;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facAuthoriser.workArea")
        private String workArea;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facAuthoriser.securityClearanceDate")
        private String securityClearanceDt;
    }

    private String inputMethod;

    @RfcAttributeDesc(aliasName = "iais.bsbfe.facAuthoriser.addOrDelete")
    private List<FacilityAuthorisedPersonnel> facAuthPersonnelList;

    private String isProtectedPlace;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    public FacilityAuthoriserDto() {
        facAuthPersonnelList = new ArrayList<>();
        facAuthPersonnelList.add(new FacilityAuthorisedPersonnel());
    }

    @Override
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("facRegFeignClient", "validateFacilityAuthoriser", new Object[]{this});
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


    public List<FacilityAuthorisedPersonnel> getFacAuthPersonnelList() {
        return new ArrayList<>(facAuthPersonnelList);
    }

    public void setFacAuthPersonnelList(List<FacilityAuthorisedPersonnel> facAuthPersonnelList) {
        this.facAuthPersonnelList = new ArrayList<>(facAuthPersonnelList);
    }

    public void clearAuthPersonnel() {
        this.facAuthPersonnelList.clear();
    }

    public void addAuthPersonnel(FacilityAuthorisedPersonnel personnel) {
        this.facAuthPersonnelList.add(personnel);
    }


    public String getInputMethod() {
        return inputMethod;
    }

    public void setInputMethod(String inputMethod) {
        this.inputMethod = inputMethod;
    }

    public String getIsProtectedPlace() {
        return isProtectedPlace;
    }

    public void setIsProtectedPlace(String isProtectedPlace) {
        this.isProtectedPlace = isProtectedPlace;
    }


//    ---------------------------- request -> object ----------------------------------------------

    private static final String SEPARATOR = "--v--";
    private static final String KEY_INPUT_METHOD = "inputMethod";
    private static final String KEY_SECTION_IDXES = "sectionIdx";
    private static final String KEY_PREFIX_NAME = "name";
    private static final String KEY_PREFIX_ID_TYPE = "idType";
    private static final String KEY_PREFIX_ID_NUMBER = "idNumber";
    private static final String KEY_PREFIX_NATIONALITY = "nationality";
    private static final String KEY_PREFIX_DESIGNATION = "designation";
    private static final String KEY_PREFIX_CONTACT_NO = "contactNo";
    private static final String KEY_PREFIX_EMAIL = "email";
    private static final String KEY_PREFIX_EMP_START_DT = "employmentStartDt";
    private static final String KEY_PREFIX_EMP_PERIOD = "employmentPeriod";
    private static final String KEY_PREFIX_SEC_CL_DT = "securityClearanceDt";
    private static final String KEY_PREFIX_WORK_AREA = "workArea";



    public void reqObjMapping(HttpServletRequest request) {
        setInputMethod(ParamUtil.getString(request, KEY_INPUT_METHOD));
        String idxes = ParamUtil.getString(request, KEY_SECTION_IDXES);
        clearAuthPersonnel();
        String[] idxArr = idxes.trim().split(" +");
        for (String idx : idxArr) {
            FacilityAuthorisedPersonnel personnel = new FacilityAuthorisedPersonnel();
            personnel.setName(ParamUtil.getString(request, KEY_PREFIX_NAME + SEPARATOR + idx));
            personnel.setIdType(ParamUtil.getString(request, KEY_PREFIX_ID_TYPE + SEPARATOR + idx));
            personnel.setIdNumber(ParamUtil.getString(request, KEY_PREFIX_ID_NUMBER + SEPARATOR + idx));
            personnel.setNationality(ParamUtil.getString(request, KEY_PREFIX_NATIONALITY + SEPARATOR + idx));
            personnel.setDesignation(ParamUtil.getString(request, KEY_PREFIX_DESIGNATION + SEPARATOR + idx));
            personnel.setContactNo(ParamUtil.getString(request, KEY_PREFIX_CONTACT_NO + SEPARATOR + idx));
            personnel.setEmail(ParamUtil.getString(request, KEY_PREFIX_EMAIL + SEPARATOR + idx));
            personnel.setEmploymentStartDt(ParamUtil.getString(request, KEY_PREFIX_EMP_START_DT + SEPARATOR + idx));
            personnel.setEmploymentPeriod(ParamUtil.getString(request, KEY_PREFIX_EMP_PERIOD + SEPARATOR + idx));
            personnel.setSecurityClearanceDt(ParamUtil.getString(request, KEY_PREFIX_SEC_CL_DT + SEPARATOR + idx));
            personnel.setWorkArea(ParamUtil.getString(request, KEY_PREFIX_WORK_AREA + SEPARATOR + idx));
            addAuthPersonnel(personnel);
        }
    }
}
