package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionTaskPoolListDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;

import java.util.List;

/**
 * @author Shicheng
 * @date 2019/11/14 18:04
 **/

public interface InspectionService {
    /**
      * @author: shicheng
      * @Date 2019/12/2
      * @Param: null
      * @return: List<SelectOption>
      * @Descripation: get App Type
      */
    List<SelectOption> getAppTypeOption();

    /**
      * @author: shicheng
      * @Date 2019/12/2
      * @Param: null
      * @return: List<SelectOption>
      * @Descripation: get App Status
      */
    List<SelectOption> getAppStatusOption();

    /**
      * @author: shicheng
      * @Date 2019/12/2
      * @Param: s
      * @return: List<TaskDto>
      * @Descripation: According to the group Id, get the work pool
      */
    List<TaskDto> getCommPoolByGroupWordId(String workGroupId);

    /**
      * @author: shicheng
      * @Date 2019/12/2
      * @Param: commPools
      * @return: List<InspectionTaskPoolListDto>
      * @Descripation: Display Common Pools
      */
    List<InspectionTaskPoolListDto> getPoolListByTaskDto(List<TaskDto> commPools);

    /**
      * @author: shicheng
      * @Date 2019/12/2
      * @Param: inspectionTaskPoolListDtoList
      * @return: String[]
      * @Descripation: get Application No By InspectionTaskPoolListDto
      */
    String[] getApplicationNoListByPool(List<InspectionTaskPoolListDto> inspectionTaskPoolListDtoList);
}
