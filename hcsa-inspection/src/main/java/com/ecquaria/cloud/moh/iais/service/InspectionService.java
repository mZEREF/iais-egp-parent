package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionSubPoolQueryDto;
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
      * @Param: workGroupId
      * @return: List<TaskDto>
      * @Descripation: According to the group Id, get the work pool
      */
    List<TaskDto> getSupervisorPoolByGroupWordId(String workGroupId);

    /**
      * @author: shicheng
      * @Date 2019/12/2
      * @Param: inspectionTaskPoolListDtoList
      * @return: String[]
      * @Descripation: get Application No By InspectionTaskPoolListDto
      */
    String[] getApplicationNoListByPool(List<TaskDto> commPools);

    /**
      * @author: shicheng
      * @Date 2019/12/3
      * @Param: searchParam
      * @return: SearchResult<InspectionSubPoolQueryDto>
      * @Descripation: get Sup Pool By Param
      */
    SearchResult<InspectionSubPoolQueryDto> getSupPoolByParam(SearchParam searchParam);

    /**
      * @author: shicheng
      * @Date 2019/12/3
      * @Param: searchResult commPools
      * @return: SearchResult<InspectionSubPoolQueryDto>, List<TaskDto>
      * @Descripation: get Other Data For SearchResult
      */
    SearchResult<InspectionTaskPoolListDto> getOtherDataForSr(SearchResult<InspectionSubPoolQueryDto> searchResult, List<TaskDto> commPools);

    /**
      * @author: shicheng
      * @Date 2019/12/4
      * @Param: nameValue, inspectionTaskPoolListDto
      * @return: List<SelectOption>
      * @Descripation: get check Inspector
      */
    List<SelectOption> getCheckInspector(String[] nameValue, InspectionTaskPoolListDto inspectionTaskPoolListDto);

    /**
      * @author: shicheng
      * @Date 2019/12/4
      * @Param: inspectionTaskPoolListDto, commPools
      * @return: void
      * @Descripation: Assign Task For Inspectors
      */
    void assignTaskForInspectors(InspectionTaskPoolListDto inspectionTaskPoolListDto, List<TaskDto> commPools);

    HcsaServiceDto getHcsaServiceDtoByServiceId(String serviceId);
}
