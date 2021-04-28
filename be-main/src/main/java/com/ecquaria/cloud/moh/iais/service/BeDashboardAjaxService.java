package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;

import java.util.Map;

/**
 * @author Shicheng
 * @date 2021/4/19 10:26
 **/
public interface BeDashboardAjaxService {

    /**
      * @author: shicheng
      * @Date 2021/4/28
      * @Param: taskDto
      * @return: String
      * @Descripation: search kpi color
      */
    String getKpiColorByTask(TaskDto taskDto);

    /**
      * @author: shicheng
      * @Date 2021/4/19
      * @Param: groupNo, loginContext, map, actionValue, dashFilterAppNo
      * @return: map
      * @Descripation: Common Pool Dropdown
      */
    Map<String, Object> getCommonDropdownResult(String groupNo, LoginContext loginContext, Map<String, Object> map, String actionValue, String dashFilterAppNo);

    /**
      * @author: shicheng
      * @Date 2021/4/28
      * @Param: groupNo, loginContext, map, actionValue, dashFilterAppNo
      * @return: map
      * @Descripation: Kpi Pool Dropdown
      */
    Map<String, Object> getKpiDropdownResult(String groupNo, LoginContext loginContext, Map<String, Object> map, String switchAction, String dashFilterAppNo);
}
