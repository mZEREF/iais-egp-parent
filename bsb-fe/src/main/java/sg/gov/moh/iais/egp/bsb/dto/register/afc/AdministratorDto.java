package sg.gov.moh.iais.egp.bsb.dto.register.afc;

import com.ecquaria.sz.commons.util.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;

import java.io.Serializable;

import static sg.gov.moh.iais.egp.bsb.constant.FacCertifierRegisterConstants.NODE_NAME_ORG_FAC_ADMINISTRATOR;

/**
 * @author : YiMing
 * @date :2021/9/26 15:13
 * DESCRIPTION: TODO
 **/

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"name", "available", "validated", "dependNodes", "validationResultDto"})
public class AdministratorDto extends Node {
    @Data
    @NoArgsConstructor
    public static class FacilityAdministratorInfo implements Serializable {
        private String adminName;
        private String nationality;
        private String idType;
        private String idNo;
        private String designation;
        private String contactNo;
        private String email;
        private String employmentStartDate;
    }

    private FacilityAdministratorInfo mainAdmin;
    private FacilityAdministratorInfo alternativeAdmin;


    private ValidationResultDto validationResultDto;

    public AdministratorDto(String name, Node[] dependNodes) {
        super(name, dependNodes);
    }

    public static AdministratorDto getInstance(Node[] dependNodes) {
        return new AdministratorDto(NODE_NAME_ORG_FAC_ADMINISTRATOR, dependNodes);
    }

    @Override
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("cerRegFeignClient", "validateFacilityAdmin", new Object[]{this});
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
    private static final String KEY_PREFIX_ID_NUMBER = "idNo";
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
        adminInfo.setIdNo(ParamUtil.getString(request, KEY_PREFIX_ID_NUMBER + suffix));
        adminInfo.setDesignation(ParamUtil.getString(request, KEY_PREFIX_DESIGNATION + suffix));
        adminInfo.setContactNo(ParamUtil.getString(request, KEY_PREFIX_CONTACT_NO + suffix));
        adminInfo.setEmail(ParamUtil.getString(request, KEY_PREFIX_EMAIL + suffix));
        adminInfo.setEmploymentStartDate(ParamUtil.getString(request, KEY_PREFIX_EMP_START_DATE + suffix));
        return adminInfo;
    }

}
