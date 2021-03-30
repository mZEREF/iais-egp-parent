package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptAppInfoShowDto;
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

    /**
      * @author: shicheng
      * @Date 2020/7/9
      * @Param: reschedulingOfficerDto
      * @return: void
      * @Descripation: reScheduleRoutingAudit
      */
    void reScheduleRoutingAudit(ReschedulingOfficerDto reschedulingOfficerDto);

    /**
      * @author: shicheng
      * @Date 2020/7/22
      * @Param: appointmentDto
      * @return: AppointmentDto
      * @Descripation: subtractEndHourByApptDto
      */
    AppointmentDto subtractEndHourByApptDto(AppointmentDto appointmentDto);

    /**
      * @author: shicheng
      * @Date 2021/3/29
      * @Param: reschedulingOfficerDto
      * @return: List<ApptAppInfoShowDto>
      * @Descripation: getReScheduleNewDateInfo
      */
    List<ApptAppInfoShowDto> getReScheduleNewDateInfo(ReschedulingOfficerDto reschedulingOfficerDto);

    /**
      * @author: shicheng
      * @Date 2021/3/30
      * @Param: apptAppInfoShowDtos, reschedulingOfficerDto
      * @return: List<ApptAppInfoShowDto>
      * @Descripation: setInfoByDateAndUserIdToSave
      */
    List<ApptAppInfoShowDto> setInfoByDateAndUserIdToSave(List<ApptAppInfoShowDto> apptAppInfoShowDtos, ReschedulingOfficerDto reschedulingOfficerDto);

    /**
      * @author: shicheng
      * @Date 2021/3/30
      * @Param: reschedulingOfficerDto
      * @return: void
      * @Descripation: sendEmailToApplicant
      */
    void sendEmailToApplicant(ReschedulingOfficerDto reschedulingOfficerDto);

    /**
      * @author: shicheng
      * @Date 2021/3/30
      * @Param: reschedulingOfficerDto, apptReSchAppInfoShowDtos
      * @return: void
      * @Descripation: changeInspectorAndDate
      */
    void changeInspectorAndDate(ReschedulingOfficerDto reschedulingOfficerDto, List<ApptAppInfoShowDto> apptReSchAppInfoShowDtos);
}
