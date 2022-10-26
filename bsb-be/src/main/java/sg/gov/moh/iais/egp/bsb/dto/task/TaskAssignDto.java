package sg.gov.moh.iais.egp.bsb.dto.task;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TaskAssignDto {
    private String taskId;
    private String userId;
    private String appNo;
    private boolean success;
    private String roleId;
    private String appId;

    public TaskAssignDto(String taskId, String userId, String roleId) {
        this.taskId = taskId;
        this.userId = userId;
        this.roleId = roleId;
    }

    public TaskAssignDto(String taskId, String userId, String roleId, String appId) {
        this.taskId = taskId;
        this.userId = userId;
        this.roleId = roleId;
        this.appId = appId;
    }
}
