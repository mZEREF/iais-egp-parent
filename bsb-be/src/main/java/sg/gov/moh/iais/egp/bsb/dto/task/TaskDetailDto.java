package sg.gov.moh.iais.egp.bsb.dto.task;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tangtang
 * @date 2022/8/19 13:46
 */
@Data
public class TaskDetailDto implements Serializable {
    private String taskId;
    private String applicationId;
    private String applicationNo;
    private String applicationStatus;
    private String applicationType;
    private String processType;

    private String facilityClassification;
    private String facilityActivityType;
    private String facilityName;
    private String address;
    private String officer;
}
