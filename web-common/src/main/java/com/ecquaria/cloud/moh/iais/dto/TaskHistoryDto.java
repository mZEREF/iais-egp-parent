package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * TaskHistoryDto
 *
 * @author suocheng
 * @date 12/14/2019
 */
@Getter
@Setter
public class TaskHistoryDto {
    private List<TaskDto> taskDtoList;
    private List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos;
}
