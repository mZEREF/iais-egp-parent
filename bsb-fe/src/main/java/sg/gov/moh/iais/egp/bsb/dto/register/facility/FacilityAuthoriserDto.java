package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_FAC_AUTH;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"name", "available", "validated", "dependNodes", "validationResultDto"})
public class FacilityAuthoriserDto extends Node {
    @Data
    @NoArgsConstructor
    public static class FacilityAuthorisedPersonnel implements Serializable {
        private String name;
        private String nationality;
        private String idType;
        private String idNumber;
        private String designation;
        private String contactNo;
        private String email;
        private String employmentStartDate;
        private String employmentPeriod;
        private String workArea;
        private String securityClearanceDate;
    }

    private String inputMethod;
    private final List<FacilityAuthorisedPersonnel> facAuthPersonnelList;
    private String isProtectedPlace;

    private ValidationResultDto validationResultDto;

    public FacilityAuthoriserDto(String name, Node[] dependNodes) {
        super(name, dependNodes);
        facAuthPersonnelList = new ArrayList<>();
        facAuthPersonnelList.add(new FacilityAuthorisedPersonnel());
    }

    public static FacilityAuthoriserDto getInstance(Node[] dependNodes) {
        return new FacilityAuthoriserDto(NODE_NAME_FAC_AUTH, dependNodes);
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
    public void needValidation() {
        super.needValidation();
        this.validationResultDto = null;
    }


    public List<FacilityAuthorisedPersonnel> getFacAuthPersonnelList() {
        return new ArrayList<>(facAuthPersonnelList);
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
    private static final String KEY_SECTION_AMT = "sectionAmt";
    private static final String KEY_PREFIX_NAME = "name";
    private static final String KEY_PREFIX_ID_TYPE = "idType";
    private static final String KEY_PREFIX_ID_NUMBER = "idNumber";
    private static final String KEY_PREFIX_NATIONALITY = "nationality";
    private static final String KEY_PREFIX_DESIGNATION = "designation";
    private static final String KEY_PREFIX_CONTACT_NO = "contactNo";
    private static final String KEY_PREFIX_EMAIL = "email";
    private static final String KEY_PREFIX_EMP_START_DT = "employmentStartDate";
    private static final String KEY_PREFIX_EMP_PERIOD = "employmentPeriod";
    private static final String KEY_PREFIX_SEC_CL_DT = "securityClearanceDate";
    private static final String KEY_PREFIX_WORK_AREA = "workArea";


    public void reqObjMapping(HttpServletRequest request) {
        setInputMethod(ParamUtil.getString(request, KEY_INPUT_METHOD));
        int amt = ParamUtil.getInt(request, KEY_SECTION_AMT);
        clearAuthPersonnel();
        for (int i = 0; i < amt; i++) {
            FacilityAuthorisedPersonnel personnel = new FacilityAuthorisedPersonnel();
            personnel.setName(ParamUtil.getString(request, KEY_PREFIX_NAME + SEPARATOR + i));
            personnel.setIdType(ParamUtil.getString(request, KEY_PREFIX_ID_TYPE + SEPARATOR + i));
            personnel.setIdNumber(ParamUtil.getString(request, KEY_PREFIX_ID_NUMBER + SEPARATOR + i));
            personnel.setNationality(ParamUtil.getString(request, KEY_PREFIX_NATIONALITY + SEPARATOR + i));
            personnel.setDesignation(ParamUtil.getString(request, KEY_PREFIX_DESIGNATION + SEPARATOR + i));
            personnel.setContactNo(ParamUtil.getString(request, KEY_PREFIX_CONTACT_NO + SEPARATOR + i));
            personnel.setEmail(ParamUtil.getString(request, KEY_PREFIX_EMAIL + SEPARATOR + i));
            personnel.setEmploymentStartDate(ParamUtil.getString(request, KEY_PREFIX_EMP_START_DT + SEPARATOR + i));
            personnel.setEmploymentPeriod(ParamUtil.getString(request, KEY_PREFIX_EMP_PERIOD + SEPARATOR + i));
            personnel.setSecurityClearanceDate(ParamUtil.getString(request, KEY_PREFIX_SEC_CL_DT + SEPARATOR + i));
            personnel.setWorkArea(ParamUtil.getString(request, KEY_PREFIX_WORK_AREA +SEPARATOR + i));
            addAuthPersonnel(personnel);
        }
    }
}
