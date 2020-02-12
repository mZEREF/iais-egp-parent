package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptInspectionDateDto;

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
      * @return: List<String>
      * @Descripation: get Inspection Date
      */
    List<String> getInspectionDate(String taskId, ApptInspectionDateDto apptInspectionDateDto);

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
}
