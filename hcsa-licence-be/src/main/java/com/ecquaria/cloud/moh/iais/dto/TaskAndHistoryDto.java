package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskAndHistoryDto {
    TaskDto taskDto;
    AppPremisesRoutingHistoryDto startHistory;
    AppPremisesRoutingHistoryDto endHistory;
}
