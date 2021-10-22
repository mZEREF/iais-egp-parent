package sg.gov.moh.iais.egp.bsb.dto.approvalApp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import java.util.List;

/**
 * @author : LiRan
 * @date : 2021/10/20
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActivityDto extends ValidatableNodeValue {
    private String facilityName;
    private String activity;
    private List<String> schedule;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    @Override
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("approvalAppFeignClient", "validateActivity", new Object[]{this});
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

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public List<String> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<String> schedule) {
        this.schedule = schedule;
    }

//    ---------------------------- request -> object ----------------------------------------------

    /*private static final String KEY_OFFICIER_NAME = "officerName";
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
    }*/
}
