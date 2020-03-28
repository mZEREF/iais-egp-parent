package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionAppGroupQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionAppInGroupQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionTaskPoolListDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;

import java.util.List;

/**
 * @author Shicheng
 * @date 2019/11/14 18:04
 **/

public interface InspectionMainService {
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
    List<SelectOption> getAppStatusOption(String role);

    /**
     * @author: guyin
     * @Date 2019/12/2
     * @Param: workGroupId
     * @return: List<TaskDto>
     * @Descripation: According to the group Id, get the work pool
     */
    List<TaskDto> getTasksByUserIdAndRole(String workGroupId,String curRole);

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
     * @Param: loginContext
     * @return: List<String>
     * @Descripation: get Work Group Ids By Login
     */
    List<String> getWorkGroupIdsByLogin(LoginContext loginContext);

    SearchResult<InspectionAppGroupQueryDto> searchInspectionBeAppGroup(SearchParam searchParam);

    SearchResult<InspectionAppInGroupQueryDto> searchInspectionBeAppGroupAjax(SearchParam searchParam);
}
