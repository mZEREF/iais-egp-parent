package sg.gov.moh.iais.egp.bsb.dto.register.approval;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @author : LiRan
 * @date : 2022/3/17
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApprovalToSpecialDto extends ValidatableNodeValue {

    @Data
    @NoArgsConstructor
    public static class WorkActivity implements Serializable {
        private String intendedWorkActivity;
        private String activityStartDate;
        private String activityEndDate;
        private String activityRemarks;
    }

    private String schedule;
    private String batName;
    private String projectName;
    private String principalInvestigatorName;


    private List<WorkActivity> workActivities;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    public ApprovalToSpecialDto() {
        workActivities = new ArrayList<>();
        workActivities.add(new WorkActivity());
    }


    @Override
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod(ApprovalBatAndActivityConstants.FEIGN_CLIENT, "validateApprovalToSpecialDto", new Object[]{this});
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


    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getBatName() {
        return batName;
    }

    public void setBatName(String batName) {
        this.batName = batName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getPrincipalInvestigatorName() {
        return principalInvestigatorName;
    }

    public void setPrincipalInvestigatorName(String principalInvestigatorName) {
        this.principalInvestigatorName = principalInvestigatorName;
    }

    public List<WorkActivity> getWorkActivities() {
        return new ArrayList<>(workActivities);
    }

    public void clearWorkActivities() {
        this.workActivities.clear();
    }

    public void addWorkActivity(WorkActivity workActivity) {
        this.workActivities.add(workActivity);
    }

    public void setWorkActivities(List<WorkActivity> workActivities) {
        this.workActivities = new ArrayList<>(workActivities);
    }

    // ---------------------------- request -> object ----------------------------------------------

    private static final String SEPARATOR                                     = "--v--";
    private static final String KEY_SECTION_IDXES                             = "sectionIdx";
    private static final String KEY_PREFIX_INTENDED_WORK_ACTIVITY             = "intendedWorkActivity";
    private static final String KEY_PREFIX_START_DATE                         = "activityStartDate";
    private static final String KEY_PREFIX_END_DATE                           = "activityEndDate";
    private static final String KEY_PREFIX_ACTIVITY_REMARKS                   = "activityRemarks";
    private static final String KEY_PREFIX_SCHEDULE                           = "schedule";
    private static final String KEY_PREFIX_BAT_NAME                           = "batName";
    private static final String KEY_PREFIX_PROJECT_NAME                       = "projectName";
    private static final String KEY_PREFIX_PRINCIPAL_INVESTIGATOR_NAME        = "principalInvestigatorName";

    public void reqObjMapping(HttpServletRequest request) {
        clearWorkActivities();
        String idxes = ParamUtil.getString(request, KEY_SECTION_IDXES);
        String[] idxArr = idxes.trim().split(" +");
        for (String idx : idxArr) {
            WorkActivity activity = new WorkActivity();
            activity.setIntendedWorkActivity(ParamUtil.getString(request, KEY_PREFIX_INTENDED_WORK_ACTIVITY + SEPARATOR +idx));
            activity.setActivityStartDate(ParamUtil.getString(request, KEY_PREFIX_START_DATE + SEPARATOR +idx));
            activity.setActivityEndDate(ParamUtil.getString(request, KEY_PREFIX_END_DATE + SEPARATOR +idx));
            activity.setActivityRemarks(ParamUtil.getString(request, KEY_PREFIX_ACTIVITY_REMARKS + SEPARATOR +idx));
            addWorkActivity(activity);
        }
        setSchedule(ParamUtil.getString(request, KEY_PREFIX_SCHEDULE));
        setBatName(ParamUtil.getString(request, KEY_PREFIX_BAT_NAME));
        setProjectName(ParamUtil.getString(request, KEY_PREFIX_PROJECT_NAME));
        setPrincipalInvestigatorName(ParamUtil.getString(request, KEY_PREFIX_PRINCIPAL_INVESTIGATOR_NAME));
    }
}
