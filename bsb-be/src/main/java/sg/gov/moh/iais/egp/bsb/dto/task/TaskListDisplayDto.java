package sg.gov.moh.iais.egp.bsb.dto.task;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.entity.AppAndMiscDto;

import java.util.List;

/**
 * @author tangtang
 * @date 2022/8/19 17:17
 */
@Data
public class TaskListDisplayDto {
    private AppAndMiscDto appAndMiscDto;
    private List<TaskListDetailViewDto> taskListDetailViewDtos;
}
