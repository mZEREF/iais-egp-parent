package sg.gov.moh.iais.egp.bsb.dto.approvalApp;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author : LiRan
 * @date : 2021/10/20
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActivityDto extends ValidatableNodeValue {
    private String facilityName;
    private String activity;
    private List<String> schedules;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    public ActivityDto(){
        schedules = new ArrayList<>();
    }

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

    public void addSchedule(String schedule){
        this.validationResultDto = null;
    }

    public void addSchedules(String[] schedules){
        if (schedules != null && schedules.length > 0) {
            this.schedules.addAll(Arrays.asList(schedules));
        }
    }

    public void replaceSchedules(String[] schedules) {
        clearSchedules();
        addSchedules(schedules);
    }

    public void clearSchedules() {
        this.schedules.clear();
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

    public List<String> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<String> schedule) {
        this.schedules = schedule;
    }

//    ---------------------------- request -> object ----------------------------------------------

    private static final String KEY_ACTIVITY_FACILITY_ID = "facilityId";
    private static final String KEY_ACTIVITY_ACTIVITY_TYPE = "activityType";
    private static final String KEY_ACTIVITY_SCHEDULE = "schedule";

    public void reqObjMapping(HttpServletRequest request) {
        String newFacilityId = ParamUtil.getString(request, KEY_ACTIVITY_FACILITY_ID);
        String newActivityType = ParamUtil.getString(request,KEY_ACTIVITY_ACTIVITY_TYPE);
        this.setFacilityName(newFacilityId);
        this.setActivity(newActivityType);
        String[] schedules = ParamUtil.getStrings(request, KEY_ACTIVITY_SCHEDULE);
        this.replaceSchedules(schedules);
    }
}
