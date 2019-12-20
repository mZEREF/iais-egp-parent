package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionAppGroupQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionAppInGroupQueryDto;
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
     * @author: guyin
     * @Date 2019/12/2
     * @Param: workGroupId
     * @return: List<TaskDto>
     * @Descripation: According to the group Id, get the work pool
     */
    List<TaskDto> getTasksByUserId(String workGroupId);


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
     * @Date 2019/12/4
     * @Param: nameValue, inspectionTaskPoolListDto
     * @return: List<SelectOption>
     * @Descripation: get check Inspector
     */
    List<SelectOption> getCheckInspector(String[] nameValue, InspectionTaskPoolListDto inspectionTaskPoolListDto);

    /**
     * @author: shicheng
     * @Date 2019/12/4
     * @Param: inspectionTaskPoolListDto, commPools, internalRemarks, applicationDto, taskDto, applicationViewDto
     * @return: void
     * @Descripation: Assign Task For Inspectors
     */
    void assignTaskForInspectors(InspectionTaskPoolListDto inspectionTaskPoolListDto, List<TaskDto> commPools,
                                 String internalRemarks, ApplicationDto applicationDto, TaskDto taskDto, ApplicationViewDto applicationViewDto);

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
     * @Param: inspectionTaskPoolListDto, internalRemarks, commPools
     * @return: void
     * @Descripation: routing Task By Pool
     */
    void routingTaskByPool(InspectionTaskPoolListDto inspectionTaskPoolListDto, List<TaskDto> commPools, String internalRemarks);

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

    SearchResult<InspectionAppGroupQueryDto> searchInspectionBeAppGroup(SearchParam searchParam);

    SearchResult<InspectionAppInGroupQueryDto> searchInspectionBeAppGroupAjax(SearchParam searchParam);
}
