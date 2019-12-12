package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionSubPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionTaskPoolListDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;

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
      * @Param: searchResult, commPools, loginContext
      * @return: SearchResult<InspectionSubPoolQueryDto>
      * @Descripation: get Other Data For SearchResult
      */
    SearchResult<InspectionTaskPoolListDto> getOtherDataForSr(SearchResult<InspectionSubPoolQueryDto> searchResult, List<TaskDto> commPools, LoginContext loginContext);



    /**
     * @author: shicheng
     * @Date 2019/11/22
     * @Param: serviceId
     * @return: HcsaServiceDto
     * @Descripation: get HcsaServiceDto By Service Id
     */
    HcsaServiceDto getHcsaServiceDtoByServiceId(String serviceId);


    /**
      * @author: shicheng
      * @Date 2019/12/11
      * @Param: loginContext
      * @return: List<SelectOption>
      * @Descripation: get Inspector Option By Login
      */
    List<SelectOption> getInspectorOptionByLogin(LoginContext loginContext, List<String> workGroupIds);

    /**
      * @author: shicheng
      * @Date 2019/12/11
      * @Param: loginContext
      * @return: List<String>
      * @Descripation: get Work Group Ids By Login
      */
    List<String> getWorkGroupIdsByLogin(LoginContext loginContext);

    /**
      * @author: shicheng
      * @Date 2019/12/12
      * @Param: inspectionTaskPoolListDto, loginContext
      * @return: InspectionTaskPoolListDto
      * @Descripation: input Inspector Option
      */
    InspectionTaskPoolListDto inputInspectorOption(InspectionTaskPoolListDto inspectionTaskPoolListDto, LoginContext loginContext);
}
