package sg.gov.moh.iais.egp.bsb.dto.register.approval;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;



@Data
public class ApprovalToSpecialDto implements Serializable {
    @Data
    @NoArgsConstructor
    public static class WorkActivity implements Serializable {
        private String intendedWorkActivity;
        private String activityStartDt;
        private String activityEndDt;
        private String activityRemarks;
    }

    private String schedule;
    private String batName;
    private String projectName;
    private String principalInvestigatorName;
    private List<WorkActivity> workActivities;
}
