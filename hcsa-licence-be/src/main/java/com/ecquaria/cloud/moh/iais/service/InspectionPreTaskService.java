package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;

import java.util.List;

/**
 * @author Shicheng
 * @date 2019/12/9 9:58
 **/
public interface InspectionPreTaskService {

    /**
      * @author: shicheng
      * @Date 2019/12/9
      * @Param: taskDto
      * @return: String
      * @Descripation: get Application Status By Task Id
      */
    String getAppStatusByTaskId(TaskDto taskDto);

    /**
      * @author: shicheng
      * @Date 2019/12/9
      * @Param: null
      * @return: List<SelectOption>
      * @Descripation: get Processing Decision Option
      */
    List<SelectOption> getProcessDecOption();

    /**
      * @author: shicheng
      * @Date 2019/12/12
      * @Param: taskDto, preInspecRemarks
      * @return: void
      * @Descripation: routing Task
      */
    void routingTask(TaskDto taskDto, String preInspecRemarks);

    /**
      * @author: shicheng
      * @Date 2020/1/7
      * @Param: taskDto, reMarks
      * @return: void
      * @Descripation: routing back
      */
    void routingBack(TaskDto taskDto, String reMarks);
}
