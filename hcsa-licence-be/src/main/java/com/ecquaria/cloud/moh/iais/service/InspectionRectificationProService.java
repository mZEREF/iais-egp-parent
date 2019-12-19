package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionPreTaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;

import java.util.List;

/**
 * @author Shicheng
 * @date 2019/12/18 14:26
 **/
public interface InspectionRectificationProService {
    /**
      * @author: shicheng
      * @Date 2019/12/18
      * @Param: appNo, stageId
      * @return: AppPremisesRoutingHistoryDto
      * @Descripation: get ppPremisesRoutingHistoryDto By appNo
      */
    AppPremisesRoutingHistoryDto getAppHistoryByTask(String appNo, String stageId);

    /**
      * @author: shicheng
      * @Date 2019/12/18
      * @Param: null
      * @return:
      * @Descripation:
      */
    List<SelectOption> getProcessRecDecOption();

    /**
      * @author: shicheng
      * @Date 2019/12/19
      * @Param: taskDto, inspectionPreTaskDto
      * @return: void
      * @Descripation: routing Task To Report
      */
    void routingTaskToReport(TaskDto taskDto, InspectionPreTaskDto inspectionPreTaskDto);
}
