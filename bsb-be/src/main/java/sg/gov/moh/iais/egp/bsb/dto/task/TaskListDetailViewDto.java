package sg.gov.moh.iais.egp.bsb.dto.task;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.entity.TaskDto;

/**
 * @author tangtang
 * @date 2022/8/17 14:01
 */
@Data
public class TaskListDetailViewDto {
    private String mainAppNo;
    private TaskDto taskDto;
    private String facClassification;
    private String facName;
    private String facAddress;
    private String validityEndDate;
    private String status;
    private String inspectionDate;
}
