package sg.gov.moh.iais.egp.bsb.dto.register.afc;

import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.dto.info.common.EmployeeInfo;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;
import sg.gov.moh.iais.egp.common.annotation.RfcAttributeDesc;

import javax.servlet.http.HttpServletRequest;


/**
 *@author YiMing
 * @version 2021/10/15 14:16
 **/
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdministratorDto extends ValidatableNodeValue {

    @RfcAttributeDesc
    private final EmployeeInfo mainAdmin;

    @RfcAttributeDesc
    private final EmployeeInfo alternativeAdmin;


    private ValidationResultDto validationResultDto;

    public AdministratorDto() {
        mainAdmin = new EmployeeInfo();
        alternativeAdmin = new EmployeeInfo();

        // set default main admin info
        HttpServletRequest request = MiscUtil.getCurrentRequest();
        AuditTrailDto auditTrailDto = (AuditTrailDto) com.ecquaria.cloud.moh.iais.common.utils.ParamUtil.getSessionAttr(request, AuditTrailConsts.SESSION_ATTR_PARAM_NAME);
        OrgUserDto orgUserDto = (OrgUserDto) SpringReflectionUtils.invokeBeanMethod("orgFeignClient", "retrieveOrgUserAccountById", new Object[]{auditTrailDto.getMohUserGuid()});
        mainAdmin.setName(orgUserDto.getDisplayName());
        mainAdmin.setIdType(orgUserDto.getIdType());
        mainAdmin.setIdNumber(orgUserDto.getIdNumber());
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
    public void clearValidationResult() {
        this.validationResultDto = null;
    }

    public EmployeeInfo getMainAdmin() {
        return mainAdmin;
    }

    public EmployeeInfo getAlternativeAdmin() {
        return alternativeAdmin;
    }

    public ValidationResultDto getValidationResultDto() {
        return validationResultDto;
    }
    //    ---------------------------- request -> object ----------------------------------------------

    private static final String KEY_PREFIX_SALUTATION = "salutation";
    private static final String KEY_PREFIX_ADMIN_NAME = "employeeName";
    private static final String KEY_PREFIX_NATIONALITY = "nationality";
    private static final String KEY_PREFIX_ID_TYPE = "idType";
    private static final String KEY_PREFIX_ID_NUMBER = "idNumber";
    private static final String KEY_PREFIX_DESIGNATION = "designation";
    private static final String KEY_PREFIX_CONTACT_NO = "contactNo";
    private static final String KEY_PREFIX_EMAIL = "email";
    private static final String KEY_PREFIX_EMP_START_DATE = "employmentStartDt";

    private static final String KEY_SUFFIX_MAIN_ADMIN = "M";
    private static final String KEY_SUFFIX_ALTERNATIVE_ADMIN = "A";

    public void reqObjMapping(HttpServletRequest request) {
        readEmployeeInfo(request,KEY_SUFFIX_MAIN_ADMIN,mainAdmin);
        readEmployeeInfo(request,KEY_SUFFIX_ALTERNATIVE_ADMIN,alternativeAdmin);

    }

    /** Will not read name, ID type and ID number  */
    private void readEmployeeInfo(HttpServletRequest request,String suffix, EmployeeInfo info) {
        info.setSalutation(ParamUtil.getString(request,KEY_PREFIX_SALUTATION + suffix));
        info.setNationality(ParamUtil.getString(request, KEY_PREFIX_NATIONALITY + suffix));
        if(KEY_SUFFIX_ALTERNATIVE_ADMIN.equals(suffix)){
            info.setName(ParamUtil.getString(request, KEY_PREFIX_ADMIN_NAME + suffix));
            info.setIdType(ParamUtil.getString(request, KEY_PREFIX_ID_TYPE + suffix));
            info.setIdNumber(ParamUtil.getString(request, KEY_PREFIX_ID_NUMBER + suffix));
        }
        info.setDesignation(ParamUtil.getString(request, KEY_PREFIX_DESIGNATION + suffix));
        info.setContactNo(ParamUtil.getString(request, KEY_PREFIX_CONTACT_NO + suffix));
        info.setEmail(ParamUtil.getString(request, KEY_PREFIX_EMAIL + suffix));
        info.setEmploymentStartDt(ParamUtil.getString(request, KEY_PREFIX_EMP_START_DATE + suffix));
    }

}
