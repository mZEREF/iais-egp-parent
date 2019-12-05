package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecTaskCreAndAssDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCommonPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;

import java.util.List;

/**
 * @author Shicheng
 * @date 2019/11/22 10:19
 **/
public interface InspectionAssignTaskService {
    /**
     * @author: shicheng
     * @Date 2019/11/21
     * @Param: loginContext
     * @return: List<TaskDto>
     * @Descripation: According to the group Id, get the work pool
     */
    List<TaskDto> getCommPoolByGroupWordId(LoginContext loginContext);

    /**
      * @author: shicheng
      * @Date 2019/11/23
      * @Param: applicationNo, commPools, loginContext
      * @return: InspecTaskCreAndAssQueryDto
      * @Descripation: Gets a single Common Pool of information for allocation
      */
    InspecTaskCreAndAssDto getInspecTaskCreAndAssDto(String applicationNo, List<TaskDto> commPools, LoginContext loginContext);

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
      * @Param: commPools
      * @return: List<String>
      * @Descripation: get Application No By commPools
      */
    String[] getApplicationNoListByPool(List<TaskDto> commPools);

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
      * @Date 2019/11/28
      * @Param: nameValue, inspecTaskCreAndAssDto
      * @return: InspecTaskCreAndAssDto
      * @Descripation: get checked Inspector
      */
    List<SelectOption> getCheckInspector(String[] nameValue, InspecTaskCreAndAssDto inspecTaskCreAndAssDto);

    /**
      * @author: shicheng
      * @Date 2019/11/29
      * @Param: commPools inspecTaskCreAndAssDto
      * @return: void
      * @Descripation: update Common Pool and create Inspector Task
      */
    void assignTaskForInspectors(List<TaskDto> commPools, InspecTaskCreAndAssDto inspecTaskCreAndAssDto);

    /**
      * @author: shicheng
      * @Date 2019/12/2
      * @Param: searchResult
      * @return: SearchResult
      * @Descripation: get Address By Result
      */
    SearchResult<InspectionCommonPoolQueryDto> getAddressByResult(SearchResult<InspectionCommonPoolQueryDto> searchResult);
}
