package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ReschedulingOfficerDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ReschedulingOfficerQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;

import java.util.List;

/**
 * @author Shicheng
 * @date 2020/6/30 13:57
 **/
public interface OfficersReSchedulingService {
    /**
      * @author: shicheng
      * @Date 2020/7/2
      * @Param: loginContext, reschedulingOfficerDto
      * @return: List<SelectOption>
      * @Descripation: getInspWorkGroupByLogin
      */
    List<SelectOption> getInspWorkGroupByLogin(LoginContext loginContext, ReschedulingOfficerDto reschedulingOfficerDto);

    /**
      * @author: shicheng
      * @Date 2020/7/2
      * @Param: workGroupId
      * @return: List<SelectOption>
      * @Descripation: getInspectorByWorkGroupId
      */
    List<SelectOption> getInspectorByWorkGroupId(String workGroupId, ReschedulingOfficerDto reschedulingOfficerDto, String workGroupNo);

    /**
      * @author: shicheng
      * @Date 2020/7/3
      * @Param: reschedulingOfficerDto,workGroupOption
      * @return: void
      * @Descripation: allInspectorFromGroupList
      */
    List<String> allInspectorFromGroupList(ReschedulingOfficerDto reschedulingOfficerDto, List<SelectOption> workGroupOption);

    /**
      * @author: shicheng
      * @Date 2020/7/3
      * @Param: reschedulingOfficerDto
      * @return: List<String>
      * @Descripation: getAppNoByInspectorAndConditions
      */
    List<String> getAppNoByInspectorAndConditions(ReschedulingOfficerDto reschedulingOfficerDto);

    /**
      * @author: shicheng
      * @Date 2020/7/6
      * @Param: searchParam
      * @return: SearchResult<ReschedulingOfficerQueryDto>
      * @Descripation: getOfficersSearch
      */
    SearchResult<ReschedulingOfficerQueryDto> getOfficersSearch(SearchParam searchParam);

    /**
      * @author: shicheng
      * @Date 2020/7/6
      * @Param: searchResult, reschedulingOfficerDto
      * @return: SearchResult<ReschedulingOfficerQueryDto>
      * @Descripation: setInspectorsAndServices
      */
    SearchResult<ReschedulingOfficerQueryDto> setInspectorsAndServices(SearchResult<ReschedulingOfficerQueryDto> searchResult, ReschedulingOfficerDto reschedulingOfficerDto);

    /**
      * @author: shicheng
      * @Date 2020/7/7
      * @Param: reschedulingOfficerDto, workGroupCheck, inspectorCheck
      * @return: List<String>
      * @Descripation: appNoListByGroupAndUserCheck
      */
    List<String> appNoListByGroupAndUserCheck(ReschedulingOfficerDto reschedulingOfficerDto, String workGroupCheck, String inspectorCheck);

    /**
      * @author: shicheng
      * @Date 2020/7/7
      * @Param: applicationNo
      * @return: ApplicationDto
      * @Descripation: getApplicationByAppNo
      */
    ApplicationDto getApplicationByAppNo(String applicationNo);

    /**
      * @author: shicheng
      * @Date 2020/7/8
      * @Param: reschedulingOfficerDto
      * @return: void
      * @Descripation: reScheduRoutingTask
      */
    void reScheduleRoutingTask(ReschedulingOfficerDto reschedulingOfficerDto);

    /**
      * @author: shicheng
      * @Date 2020/7/9
      * @Param: 
      * @return: 
      * @Descripation: 
      */
    AppointmentDto getInspDateValidateData(ReschedulingOfficerDto reschedulingOfficerDto);
}
