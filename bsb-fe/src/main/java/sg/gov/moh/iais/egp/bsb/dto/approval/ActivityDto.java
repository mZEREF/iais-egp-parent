package sg.gov.moh.iais.egp.bsb.dto.approval;

import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;
import sg.gov.moh.iais.egp.common.annotation.RfcAttributeDesc;

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

    private String draftAppNo;
    private String processType;

    @RfcAttributeDesc
    private String facilityId;

    private String facilityName;

    @RfcAttributeDesc
    private String activityId;

    private String activityType;

    @RfcAttributeDesc
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

    public void addSchedules(String[] schedules){
        if (schedules != null && schedules.length > 0) {
            this.schedules.addAll(Arrays.asList(schedules));
        }
    }

    public void replaceSchedules(String[] schedules) {
        clearSchedules();
        addSchedules(schedules);
    }

    public String getDraftAppNo() {
        return draftAppNo;
    }

    public void setDraftAppNo(String draftAppNo) {
        this.draftAppNo = draftAppNo;
    }

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }

    public void clearSchedules() {
        this.schedules.clear();
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public List<String> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<String> schedules) {
        this.schedules = schedules;
    }

    //    ---------------------------- request -> object ----------------------------------------------

    private static final String KEY_ACTIVITY_FACILITY_ID = "facilityId";
    private static final String KEY_ACTIVITY_FACILITY_NAME = "facilityName";
    private static final String KEY_ACTIVITY_ACTIVITY_ID = "activityId";
    private static final String KEY_ACTIVITY_ACTIVITY_TYPE = "activityType";
    private static final String KEY_ACTIVITY_SCHEDULE = "schedules";

    private static final String PROCESS_TYPE = "processType";

    public void reqObjMapping(HttpServletRequest request) {
        String maskFacilityId = ParamUtil.getString(request, KEY_ACTIVITY_FACILITY_ID);
        String newFacilityName = ParamUtil.getString(request,KEY_ACTIVITY_FACILITY_NAME);
        String maskActivityId = ParamUtil.getString(request,KEY_ACTIVITY_ACTIVITY_ID);
        String newActivityType = ParamUtil.getString(request,KEY_ACTIVITY_ACTIVITY_TYPE);
        String newFacilityId = "";
        String newActivityId = "";
        if (!maskFacilityId.equals("Please Select") && !maskActivityId.equals("Please Select")){
            newFacilityId = MaskUtil.unMaskValue(KEY_ACTIVITY_FACILITY_ID, maskFacilityId);
            newActivityId = MaskUtil.unMaskValue(KEY_ACTIVITY_ACTIVITY_ID, maskActivityId);
        }
        this.setFacilityId(newFacilityId);
        this.setFacilityName(newFacilityName);
        this.setActivityId(newActivityId);
        this.setActivityType(newActivityType);
        String[] scheduleArray = ParamUtil.getStrings(request, KEY_ACTIVITY_SCHEDULE);
        this.replaceSchedules(scheduleArray);
        String newProcessType = (String) ParamUtil.getSessionAttr(request, PROCESS_TYPE);
        this.setProcessType(newProcessType);
    }
}
