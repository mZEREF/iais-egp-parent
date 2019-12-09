package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;

import java.util.List;

/**
 * @author Shicheng
 * @date 2019/12/9 9:58
 **/
public interface InspectionPreTaskService {

    /**
      * @author: shicheng
      * @Date 2019/12/9
      * @Param: taskId
      * @return: String
      * @Descripation: get Application Status By Task Id
      */
    String getAppStatusByTaskId(String taskId);

    /**
      * @author: shicheng
      * @Date 2019/12/9
      * @Param: null
      * @return: List<SelectOption>
      * @Descripation: get Processing Decision Option
      */
    List<SelectOption> getProcessDecOption();
}
