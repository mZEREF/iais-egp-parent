package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import com.ecquaria.sz.commons.util.ParamUtil;
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


@JsonInclude(JsonInclude.Include.NON_NULL)
public class FacilityAdministratorDto extends ValidatableNodeValue {
    @Data
    @NoArgsConstructor
    public static class FacilityAdministratorInfo implements Serializable {
        private String adminEntityId;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facAdmin.name")
        private String adminName;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facAdmin.nationality")
        private String nationality;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facAdmin.idType")
        private String idType;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facAdmin.idNumber")
        private String idNumber;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facAdmin.designation")
        private String designation;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facAdmin.contactNo")
        private String contactNo;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facAdmin.email")
        private String email;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facAdmin.employmentStartDate")
        private String employmentStartDate;
    }

    @RfcAttributeDesc
    private FacilityAdministratorInfo mainAdmin;

    @RfcAttributeDesc
    private FacilityAdministratorInfo alternativeAdmin;

    @JsonIgnore
    private ValidationResultDto validationResultDto;


    @Override
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("facRegFeignClient", "validateFacilityAdmin", new Object[]{this});
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


    public FacilityAdministratorInfo getMainAdmin() {
        return mainAdmin;
    }

    public void setMainAdmin(FacilityAdministratorInfo mainAdmin) {
        this.mainAdmin = mainAdmin;
    }

    public FacilityAdministratorInfo getAlternativeAdmin() {
        return alternativeAdmin;
    }

    public void setAlternativeAdmin(FacilityAdministratorInfo alternativeAdmin) {
        this.alternativeAdmin = alternativeAdmin;
    }


//    ---------------------------- request -> object ----------------------------------------------

    private static final String KEY_PREFIX_ADMIN_NAME = "adminName";
    private static final String KEY_PREFIX_NATIONALITY = "nationality";
    private static final String KEY_PREFIX_ID_TYPE = "idType";
    private static final String KEY_PREFIX_ID_NUMBER = "idNumber";
    private static final String KEY_PREFIX_DESIGNATION = "designation";
    private static final String KEY_PREFIX_CONTACT_NO = "contactNo";
    private static final String KEY_PREFIX_EMAIL = "email";
    private static final String KEY_PREFIX_EMP_START_DATE = "employmentStartDate";

    private static final String KEY_SUFFIX_MAIN_ADMIN = "M";
    private static final String KEY_SUFFIX_ALTERNATIVE_ADMIN = "A";

    public void reqObjMapping(HttpServletRequest request) {
        FacilityAdministratorInfo mainAdminInfo = getAdminInfo(request, KEY_SUFFIX_MAIN_ADMIN);
        FacilityAdministratorInfo alternativeAdminInfo = getAdminInfo(request, KEY_SUFFIX_ALTERNATIVE_ADMIN);
        setMainAdmin(mainAdminInfo);
        setAlternativeAdmin(alternativeAdminInfo);
    }

    private FacilityAdministratorInfo getAdminInfo(HttpServletRequest request, String suffix) {
        FacilityAdministratorInfo adminInfo = new FacilityAdministratorInfo();
        adminInfo.setAdminName(ParamUtil.getString(request, KEY_PREFIX_ADMIN_NAME + suffix));
        adminInfo.setNationality(ParamUtil.getString(request, KEY_PREFIX_NATIONALITY + suffix));
        adminInfo.setIdType(ParamUtil.getString(request, KEY_PREFIX_ID_TYPE + suffix));
        adminInfo.setIdNumber(ParamUtil.getString(request, KEY_PREFIX_ID_NUMBER + suffix));
        adminInfo.setDesignation(ParamUtil.getString(request, KEY_PREFIX_DESIGNATION + suffix));
        adminInfo.setContactNo(ParamUtil.getString(request, KEY_PREFIX_CONTACT_NO + suffix));
        adminInfo.setEmail(ParamUtil.getString(request, KEY_PREFIX_EMAIL + suffix));
        adminInfo.setEmploymentStartDate(ParamUtil.getString(request, KEY_PREFIX_EMP_START_DATE + suffix));
        return adminInfo;
    }

}
