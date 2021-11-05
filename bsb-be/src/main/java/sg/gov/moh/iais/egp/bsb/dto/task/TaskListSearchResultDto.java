package sg.gov.moh.iais.egp.bsb.dto.task;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.entity.TaskView;
import java.util.List;


@Data
public class TaskListSearchResultDto {
    private PageInfo pageInfo;
    private List<TaskView> tasks;
}
