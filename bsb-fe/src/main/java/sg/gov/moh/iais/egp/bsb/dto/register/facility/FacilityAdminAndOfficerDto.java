package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.common.edit.FieldEditableJudger;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.dto.info.common.EmployeeInfo;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.RequestObjectMappingUtil;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class FacilityAdminAndOfficerDto implements ValidatableNodeValue {
    private final EmployeeInfo mainAdmin;

    private final EmployeeInfo alternativeAdmin;

    private List<EmployeeInfo> officerList;

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

    public void setOfficerList(List<EmployeeInfo> officerList) {
        this.officerList = officerList;
    }

    public void clearOfficerList() {
        this.officerList.clear();
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
    private static final String KEY_DELETED_SECTION_IDXES = "deletedSectionIdx";
    private static final String SEPARATOR = "--v--";

    private static final String KEY_SUFFIX_MAIN_ADMIN = "M";
    private static final String KEY_SUFFIX_ALTERNATIVE_ADMIN = "A";

    @SneakyThrows({InstantiationException.class, IllegalAccessException.class})
    public void reqObjMapping(HttpServletRequest request, FieldEditableJudger editableJudger) {
        // read admin
        readMainAdminInfo(request, mainAdmin, editableJudger);
        readEmployeeInfo(request, KEY_SUFFIX_ALTERNATIVE_ADMIN, alternativeAdmin, editableJudger);

        // read officer
        String idxes = ParamUtil.getString(request, KEY_SECTION_IDXES);
        String deletedIdxes = ParamUtil.getString(request, KEY_DELETED_SECTION_IDXES);
        Set<Integer> idxSet = StringUtils.hasLength(idxes) ? Arrays.stream(idxes.trim().split(" +")).map(Integer::valueOf).collect(Collectors.toSet()) : Collections.emptySet();
        Set<Integer> deletedIdxSet = StringUtils.hasLength(deletedIdxes) ? Arrays.stream(deletedIdxes.trim().split(" +")).map(Integer::valueOf).collect(Collectors.toSet()) : Collections.emptySet();
        if (idxSet.isEmpty()) {
            clearOfficerList();
        } else {
            Map<Integer, EmployeeInfo> map = RequestObjectMappingUtil.readAndReuseSectionDto(EmployeeInfo.class, officerList, idxSet, deletedIdxSet);
            map.forEach((k, v) -> this.readEmployeeInfo(request, SEPARATOR + k, v, editableJudger));
            this.setOfficerList(new ArrayList<>(map.values()));
        }
    }


    /**
     * Will not read name, ID type and ID number
     */
    private void readMainAdminInfo(HttpServletRequest request, EmployeeInfo info, FieldEditableJudger editableJudger) {
        if (editableJudger.editable(KEY_PREFIX_SALUTATION + KEY_SUFFIX_MAIN_ADMIN)) {
            info.setSalutation(ParamUtil.getString(request, KEY_PREFIX_SALUTATION + KEY_SUFFIX_MAIN_ADMIN));
        }
        if (editableJudger.editable(KEY_PREFIX_NATIONALITY + KEY_SUFFIX_MAIN_ADMIN)) {
            info.setNationality(ParamUtil.getString(request, KEY_PREFIX_NATIONALITY + KEY_SUFFIX_MAIN_ADMIN));
        }
        if (editableJudger.editable(KEY_PREFIX_DESIGNATION + KEY_SUFFIX_MAIN_ADMIN)) {
            info.setDesignation(ParamUtil.getString(request, KEY_PREFIX_DESIGNATION + KEY_SUFFIX_MAIN_ADMIN));
        }
        if (editableJudger.editable(KEY_PREFIX_CONTACT_NO + KEY_SUFFIX_MAIN_ADMIN)) {
            info.setContactNo(ParamUtil.getString(request, KEY_PREFIX_CONTACT_NO + KEY_SUFFIX_MAIN_ADMIN));
        }
        if (editableJudger.editable(KEY_PREFIX_EMAIL + KEY_SUFFIX_MAIN_ADMIN)) {
            info.setEmail(ParamUtil.getString(request, KEY_PREFIX_EMAIL + KEY_SUFFIX_MAIN_ADMIN));
        }
        if (editableJudger.editable(KEY_PREFIX_EMP_START_DATE + KEY_SUFFIX_MAIN_ADMIN)) {
            info.setEmploymentStartDt(ParamUtil.getString(request, KEY_PREFIX_EMP_START_DATE + KEY_SUFFIX_MAIN_ADMIN));
        }
    }

    private void readEmployeeInfo(HttpServletRequest request, String suffix, EmployeeInfo info, FieldEditableJudger editableJudger) {
        if (editableJudger.editable(KEY_PREFIX_SALUTATION)) {
            info.setSalutation(ParamUtil.getString(request, KEY_PREFIX_SALUTATION + suffix));
        }
        if (editableJudger.editable(KEY_PREFIX_SALUTATION)) {
            info.setName(ParamUtil.getString(request, KEY_PREFIX_ADMIN_NAME + suffix));
        }
        if (editableJudger.editable(KEY_PREFIX_SALUTATION)) {
            info.setNationality(ParamUtil.getString(request, KEY_PREFIX_NATIONALITY + suffix));
        }
        if (editableJudger.editable(KEY_PREFIX_SALUTATION)) {
            info.setIdType(ParamUtil.getString(request, KEY_PREFIX_ID_TYPE + suffix));
        }
        if (editableJudger.editable(KEY_PREFIX_SALUTATION)) {
            info.setIdNumber(ParamUtil.getString(request, KEY_PREFIX_ID_NUMBER + suffix));
        }
        if (editableJudger.editable(KEY_PREFIX_SALUTATION)) {
            info.setDesignation(ParamUtil.getString(request, KEY_PREFIX_DESIGNATION + suffix));
        }
        if (editableJudger.editable(KEY_PREFIX_SALUTATION)) {
            info.setContactNo(ParamUtil.getString(request, KEY_PREFIX_CONTACT_NO + suffix));
        }
        if (editableJudger.editable(KEY_PREFIX_SALUTATION)) {
            info.setEmail(ParamUtil.getString(request, KEY_PREFIX_EMAIL + suffix));
        }
        if (editableJudger.editable(KEY_PREFIX_SALUTATION)) {
            info.setEmploymentStartDt(ParamUtil.getString(request, KEY_PREFIX_EMP_START_DATE + suffix));
        }
    }

}
