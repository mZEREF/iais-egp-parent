package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptAppInfoShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptInspectionDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;

import java.util.List;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2020/2/11 11:32
 **/
public interface ApptInspectionDateService {

    /**
      * @author: shicheng
      * @Date 2020/2/11
      * @Param: taskDto, apptInspectionDateDto, applicationViewDto
      * @return: ApptInspectionDateDto
      * @Descripation: get Inspection Date
      */
    ApptInspectionDateDto getInspectionDate(TaskDto taskDto, ApptInspectionDateDto apptInspectionDateDto, ApplicationViewDto applicationViewDto);

    /**
      * @author: shicheng
      * @Date 2020/2/11
      * @Param: null
      * @return: List<SelectOption>
      * @Descripation: get Process Decision
      */
    List<SelectOption> getProcessDecList();

    /**
      * @author: shicheng
      * @Date 2020/2/11
      * @Param: null
      * @return: List<SelectOption>
      * @Descripation: get Inspection Date Hour
      */
    List<SelectOption> getInspectionDateHours();

    /**
     * @author: shicheng
     * @Date 2020/2/11
     * @Param: null
     * @return: List<SelectOption>
     * @Descripation: get Inspection Date Hour
     */
    List<SelectOption> getInspectionDateEndHours();

    /**
      * @author: shicheng
      * @Date 2020/2/11
      * @Param: null
      * @return: List<SelectOption>
      * @Descripation: get am or pm option
      */
    List<SelectOption> getAmPmOption();

    /**
      * @author: shicheng
      * @Date 2020/2/11
      * @Param: apptInspectionDateDto
      * @return: void
      * @Descripation: save Lead Specific Date
      */
    void saveLeadSpecificDate(ApptInspectionDateDto apptInspectionDateDto, ApplicationViewDto applicationViewDto);

    /**
      * @author: shicheng
      * @Date 2020/2/11
      * @Param: apptInspectionDateDto
      * @return: void
      * @Descripation: save System Inspection Date
      */
    void saveSystemInspectionDate(ApptInspectionDateDto apptInspectionDateDto, ApplicationViewDto applicationViewDto);

    /**
      * @author: shicheng
      * @Date 2020/2/19
      * @Param: apptInspectionDateDto
      * @return:
      * @Descripation: get Re-Scheduling ProcessDec List
      */
    List<SelectOption> getReShProcessDecList(ApptInspectionDateDto apptInspectionDateDto);

    /**
      * @author: shicheng
      * @Date 2020/2/19
      * @Param: taskId, apptInspectionDateDto
      * @return: ApptInspectionDateDto
      * @Descripation: get Applicant choose Specific Date
      */
    ApptInspectionDateDto getApptSpecificDate(String taskId, ApptInspectionDateDto apptInspectionDateDto);

    /**
      * @author: shicheng
      * @Date 2020/2/19
      * @Param: apptInspectionDateDto, loginContext
      * @return: void
      * @Descripation: save Specific Date Last
      */
    void saveSpecificDateLast(ApptInspectionDateDto apptInspectionDateDto, LoginContext loginContext);

    /**
      * @author: shicheng
      * @Date 2020/3/12
      * @Param: premCorrIds, taskDtoList, corrAppMap
      * @return: ApptAppInfoShowDto
      * @Descripation: getApplicationInfoToShow
      */
    List<ApptAppInfoShowDto> getApplicationInfoToShow(List<String> premCorrIds, List<TaskDto> taskDtoList, Map<String, ApplicationDto> corrAppMap);

    /**
      * @author: shicheng
      * @Date 2020/3/12
      * @Param: 
      * @return: 
      * @Descripation: All task(From The Same Premises) is go to inspection(some of them jump over Inspection),
      *                can do get Inspection Date
      */
    String getActionButtonFlag(ApptInspectionDateDto apptInspectionDateDto, ApplicationDto applicationDto);

    /**
      * @author: shicheng
      * @Date 2020/4/10
      * @Param: apptInspectionDateDto, applicationViewDto
      * @return: void
      * @Descripation: saveAuditInspectionDate
      */
    void saveAuditInspectionDate(ApptInspectionDateDto apptInspectionDateDto, ApplicationViewDto applicationViewDto);

    /**
      * @author: shicheng
      * @Date 2020/7/10
      * @Param: apptInspectionDateDto
      * @return: void
      * @Descripation: saveAppUserCorrelation
      */
    void saveAppUserCorrelation(ApptInspectionDateDto apptInspectionDateDto);
}
