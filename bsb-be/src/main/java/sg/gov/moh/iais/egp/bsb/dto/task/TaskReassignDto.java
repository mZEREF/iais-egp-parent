package sg.gov.moh.iais.egp.bsb.dto.task;

import lombok.Data;

import java.io.Serializable;

@Data
public class TaskReassignDto implements Serializable {
    private String appId;
    private String taskId;
    private String userId;
    private String curRoleId;
}
