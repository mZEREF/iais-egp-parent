package sg.gov.moh.iais.egp.bsb.dto.register.approval;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sg.gov.moh.iais.egp.bsb.common.edit.FieldEditableJudger;
import sg.gov.moh.iais.egp.bsb.common.node.simple.BranchedValidationNodeValue;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApprovalToSpecialDto implements BranchedValidationNodeValue {

    @Data
    @NoArgsConstructor
    public static class WorkActivity implements Serializable {
        private String intendedWorkActivity;
        private String activityStartDt;
        private String activityEndDt;
        private String activityRemarks;
    }
    @Getter @Setter
    private String id;
    @Getter @Setter
    private String schedule;

    @Getter @Setter
    private String batName;
    @Getter @Setter
    private String projectName;
    @Getter @Setter
    private String principalInvestigatorName;
    @Getter @Setter
    private List<WorkActivity> workActivities;

    /* For validation */

    @Setter @Getter
    private boolean protectedPlace;
    @Getter @Setter
    private String facilityId;
    @JsonIgnore
    private ValidationResultDto validationResultDto;
    @JsonIgnore
    private String validationProfile;

    public ApprovalToSpecialDto(String validationProfile) {
        this.validationProfile = validationProfile;
        workActivities = new ArrayList<>();
        workActivities.add(new WorkActivity());
    }

    public ApprovalToSpecialDto() {
        workActivities = new ArrayList<>();
        workActivities.add(new WorkActivity());
    }

    @Override
    public String getValidationProfile() {
        return validationProfile;
    }

    @Override
    public void setValidationProfile(String profile) {
        this.validationProfile = profile;
    }


    @Override
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod(ApprovalBatAndActivityConstants.FEIGN_CLIENT, "validateApprovalToSpecialBatDto", new Object[]{this, validationProfile});
        return validationResultDto.isPass();
    }

    @Override
    public String retrieveValidationResult() {
        if (this.validationResultDto == null) {
            throw new IllegalStateException("This DTO is not validated");
        }
        return this.validationResultDto.toErrorMsg();
    }

    public void setValidationResultDto(ValidationResultDto validationResultDto) {
        this.validationResultDto = validationResultDto;
    }

    @Override
    public void clearValidationResult() {
        this.validationResultDto = null;
    }

    public void clearWorkActivities() {
        this.workActivities.clear();
    }

    public void addWorkActivity(WorkActivity workActivity) {
        this.workActivities.add(workActivity);
    }


    // ---------------------------- request -> object ----------------------------------------------

    private static final String SEPARATOR                                     = "--v--";
    private static final String KEY_SECTION_IDXES                             = "sectionIdx";
    private static final String KEY_PREFIX_INTENDED_WORK_ACTIVITY             = "intendedWorkActivity";
    private static final String KEY_PREFIX_START_DATE                         = "activityStartDt";
    private static final String KEY_PREFIX_END_DATE                           = "activityEndDt";
    private static final String KEY_PREFIX_ACTIVITY_REMARKS                   = "activityRemarks";
    private static final String KEY_PREFIX_BAT_NAME                           = "batName";
    private static final String KEY_PREFIX_PROJECT_NAME                       = "projectName";
    private static final String KEY_PREFIX_PRINCIPAL_INVESTIGATOR_NAME        = "principalInvestigatorName";

    public void reqObjMapping(HttpServletRequest request, FieldEditableJudger editableJudger) {
        clearWorkActivities();
        String idxes = ParamUtil.getString(request, KEY_SECTION_IDXES);
        String[] idxArr = idxes.trim().split(" +");
        for (String idx : idxArr) {
            WorkActivity activity = new WorkActivity();
            if (editableJudger.editable(KEY_PREFIX_INTENDED_WORK_ACTIVITY)) {
                activity.setIntendedWorkActivity(ParamUtil.getString(request, KEY_PREFIX_INTENDED_WORK_ACTIVITY + SEPARATOR + idx));
            }
            if (editableJudger.editable(KEY_PREFIX_START_DATE)) {
                activity.setActivityStartDt(ParamUtil.getString(request, KEY_PREFIX_START_DATE + SEPARATOR + idx));
            }
            if (editableJudger.editable(KEY_PREFIX_END_DATE)) {
                activity.setActivityEndDt(ParamUtil.getString(request, KEY_PREFIX_END_DATE + SEPARATOR + idx));
            }
            if (editableJudger.editable(KEY_PREFIX_ACTIVITY_REMARKS)) {
                activity.setActivityRemarks(ParamUtil.getString(request, KEY_PREFIX_ACTIVITY_REMARKS + SEPARATOR + idx));
            }
            addWorkActivity(activity);
        }
        //only second schedule
        setSchedule(MasterCodeConstants.SECOND_SCHEDULE);
        if (editableJudger.editable(KEY_PREFIX_BAT_NAME)) {
            setBatName(ParamUtil.getString(request, KEY_PREFIX_BAT_NAME));
        }
        if (editableJudger.editable(KEY_PREFIX_PROJECT_NAME)) {
            setProjectName(ParamUtil.getString(request, KEY_PREFIX_PROJECT_NAME));
        }
        if (editableJudger.editable(KEY_PREFIX_PRINCIPAL_INVESTIGATOR_NAME)) {
            setPrincipalInvestigatorName(ParamUtil.getString(request, KEY_PREFIX_PRINCIPAL_INVESTIGATOR_NAME));
        }
    }
}
