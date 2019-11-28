package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecTaskCreAndAssDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCommonPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionTaskPoolListDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;

import java.util.List;

/**
 * @author Shicheng
 * @date 2019/11/22 10:19
 **/
public interface InspectionAssignTaskService {
    /**
     * @author: shicheng
     * @Date 2019/11/21
     * @Param: name
     * @return: List<TaskDto>
     * @Descripation: According to the group Id, get the work pool
     */
    List<TaskDto> getCommPoolByGroupWordId(String workGroupId);

    /**
      * @author: shicheng
      * @Date 2019/11/22
      * @Param: taskDtoList
      * @return: List<InspectionTaskPoolListDto>
      * @Descripation: Display Common Pools
      */
    List<InspectionTaskPoolListDto> getPoolListByTaskDto(List<TaskDto> taskDtoList);

    /**
      * @author: shicheng
      * @Date 2019/11/23
      * @Param: applicationNo
      * @return: InspecTaskCreAndAssQueryDto
      * @Descripation: Gets a single Common Pool of information for allocation
      */
    InspecTaskCreAndAssDto getInspecTaskCreAndAssDto(String applicationNo);

    /**
      * @author: shicheng
      * @Date 2019/11/27
      * @Param: searchParam
      * @return: SearchResult
      * @Descripation: Search results based on search criteria
      */
    SearchResult<InspectionCommonPoolQueryDto> getSearchResultByParam(SearchParam searchParam);

    /**
      * @author: shicheng
      * @Date 2019/11/27
      * @Param: inspectionTaskPoolListDtoList
      * @return: List<String>
      * @Descripation: get Application No By InspectionTaskPoolListDto
      */
    String[] getApplicationNoListByPool(List<InspectionTaskPoolListDto> inspectionTaskPoolListDtoList);

    /**
      * @author: shicheng
      * @Date 2019/11/27
      * @Param: null
      * @return: List<SelectOption>
      * @Descripation: getAppTypeOption
      */
    List<SelectOption> getAppTypeOption();

    /**
      * @author: shicheng
      * @Date 2019/11/27
      * @Param: null
      * @return: List<SelectOption>
      * @Descripation: getAppStatusOption
      */
    List<SelectOption> getAppStatusOption();
}
