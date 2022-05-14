package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.dto.info.common.EmployeeInfo;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;
import sg.gov.moh.iais.egp.common.annotation.RfcAttributeDesc;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class FacilityAdminAndOfficerDto extends ValidatableNodeValue {
    @RfcAttributeDesc
    private final EmployeeInfo mainAdmin;

    @RfcAttributeDesc
    private final EmployeeInfo alternativeAdmin;

    private final List<EmployeeInfo> officerList;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    public FacilityAdminAndOfficerDto() {
        mainAdmin = new EmployeeInfo();
        alternativeAdmin = new EmployeeInfo();

        // officer amount limit is 3
        officerList = new ArrayList<>(3);

        // set default main admin info
        HttpServletRequest request = MiscUtil.getCurrentRequest();
        AuditTrailDto auditTrailDto = (AuditTrailDto) ParamUtil.getSessionAttr(request, AuditTrailConsts.SESSION_ATTR_PARAM_NAME);
        OrgUserDto orgUserDto = (OrgUserDto) SpringReflectionUtils.invokeBeanMethod("orgFeignClient", "retrieveOrgUserAccountById", new Object[]{auditTrailDto.getMohUserGuid()});
        mainAdmin.setName(orgUserDto.getDisplayName());
        mainAdmin.setIdType(orgUserDto.getIdType());
        mainAdmin.setIdNumber(orgUserDto.getIdNumber());
    }

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


    public EmployeeInfo getMainAdmin() {
        return mainAdmin;
    }


    public EmployeeInfo getAlternativeAdmin() {
        return alternativeAdmin;
    }


    public List<EmployeeInfo> getOfficerList() {
        return officerList;
    }

    public void clearOfficerList() {
        this.officerList.clear();
    }

    public void addOfficer(EmployeeInfo employeeInfo) {
        this.officerList.add(employeeInfo);
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

    private static final String KEY_SECTION_IDXES = "sectionIdx";
    private static final String SEPARATOR = "--v--";

    private static final String KEY_SUFFIX_MAIN_ADMIN = "M";
    private static final String KEY_SUFFIX_ALTERNATIVE_ADMIN = "A";

    public void reqObjMapping(HttpServletRequest request) {
        // read admin
        readMainAdminInfo(request, mainAdmin);
        readEmployeeInfo(request, KEY_SUFFIX_ALTERNATIVE_ADMIN, alternativeAdmin);

        // read officer
        String idxes = ParamUtil.getString(request, KEY_SECTION_IDXES);
        clearOfficerList();
        if (StringUtils.hasLength(idxes)) {
            String[] idxArr = idxes.trim().split(" +");
            for (String idx : idxArr) {
                EmployeeInfo employeeInfo = new EmployeeInfo();
                readEmployeeInfo(request, SEPARATOR + idx, employeeInfo);
                addOfficer(employeeInfo);
            }
        }
    }

    /** Will not read name, ID type and ID number  */
    private void readMainAdminInfo(HttpServletRequest request, EmployeeInfo info) {
        info.setSalutation(ParamUtil.getString(request,KEY_PREFIX_SALUTATION + KEY_SUFFIX_MAIN_ADMIN));
        info.setNationality(ParamUtil.getString(request, KEY_PREFIX_NATIONALITY + KEY_SUFFIX_MAIN_ADMIN));
        info.setDesignation(ParamUtil.getString(request, KEY_PREFIX_DESIGNATION + KEY_SUFFIX_MAIN_ADMIN));
        info.setContactNo(ParamUtil.getString(request, KEY_PREFIX_CONTACT_NO + KEY_SUFFIX_MAIN_ADMIN));
        info.setEmail(ParamUtil.getString(request, KEY_PREFIX_EMAIL + KEY_SUFFIX_MAIN_ADMIN));
        info.setEmploymentStartDt(ParamUtil.getString(request, KEY_PREFIX_EMP_START_DATE + KEY_SUFFIX_MAIN_ADMIN));
    }

    private void readEmployeeInfo(HttpServletRequest request, String suffix, EmployeeInfo info) {
        info.setSalutation(ParamUtil.getString(request,KEY_PREFIX_SALUTATION + suffix));
        info.setName(ParamUtil.getString(request, KEY_PREFIX_ADMIN_NAME + suffix));
        info.setNationality(ParamUtil.getString(request, KEY_PREFIX_NATIONALITY + suffix));
        info.setIdType(ParamUtil.getString(request, KEY_PREFIX_ID_TYPE + suffix));
        info.setIdNumber(ParamUtil.getString(request, KEY_PREFIX_ID_NUMBER + suffix));
        info.setDesignation(ParamUtil.getString(request, KEY_PREFIX_DESIGNATION + suffix));
        info.setContactNo(ParamUtil.getString(request, KEY_PREFIX_CONTACT_NO + suffix));
        info.setEmail(ParamUtil.getString(request, KEY_PREFIX_EMAIL + suffix));
        info.setEmploymentStartDt(ParamUtil.getString(request, KEY_PREFIX_EMP_START_DATE + suffix));
    }

}
