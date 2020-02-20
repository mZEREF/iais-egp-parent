package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptInspectionDateDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;

import java.util.List;

/**
 * @author Shicheng
 * @date 2020/2/11 11:32
 **/
public interface ApptInspectionDateService {

    /**
      * @author: shicheng
      * @Date 2020/2/11
      * @Param: taskId, apptInspectionDateDto
      * @return: ApptInspectionDateDto
      * @Descripation: get Inspection Date
      */
    ApptInspectionDateDto getInspectionDate(String taskId, ApptInspectionDateDto apptInspectionDateDto);

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
    void saveLeadSpecificDate(ApptInspectionDateDto apptInspectionDateDto);

    /**
      * @author: shicheng
      * @Date 2020/2/11
      * @Param: apptInspectionDateDto
      * @return: void
      * @Descripation: save System Inspection Date
      */
    void saveSystemInspectionDate(ApptInspectionDateDto apptInspectionDateDto);

    /**
      * @author: shicheng
      * @Date 2020/2/19
      * @Param:
      * @return:
      * @Descripation: get Re-Scheduling ProcessDec List
      */
    List<SelectOption> getReShProcessDecList();

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
}
