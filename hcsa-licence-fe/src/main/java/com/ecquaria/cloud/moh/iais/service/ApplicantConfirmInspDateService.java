package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptFeConfirmDateDto;

import java.util.List;

/**
 * @author Shicheng
 * @date 2020/2/15 17:21
 **/
public interface ApplicantConfirmInspDateService {
    /**
     * System Date
     */
    /**
      * @author: shicheng
      * @Date 2020/2/18
      * @Param: appPremCorrId
      * @return: ApptFeConfirmDateDto
      * @Descripation: get Appt System Date
      */
    ApptFeConfirmDateDto getApptSystemDate(String appPremCorrId);

    /**
     * @author: shicheng
     * @Date 2020/2/18
     * @Param: apptFeConfirmDateDto
     * @return:
     * @Descripation:
     */
    void confirmInspectionDate(ApptFeConfirmDateDto apptFeConfirmDateDto);

    /**
      * @author: shicheng
      * @Date 2020/2/18
      * @Param: apptFeConfirmDateDto
      * @return: ApptFeConfirmDateDto
      * @Descripation: get Appt New System Date
      */
    ApptFeConfirmDateDto getApptNewSystemDate(ApptFeConfirmDateDto apptFeConfirmDateDto);

    /**
      * @author: shicheng
      * @Date 2020/2/18
      * @Param: null
      * @return: List<SelectOption>
      * @Descripation: get InspectionDate Hours
      */
    List<SelectOption> getInspectionDateHours();

    /**
      * @author: shicheng
      * @Date 2020/2/18
      * @Param: null
      * @return: List<SelectOption>
      * @Descripation: get AmPm Option
      */
    List<SelectOption> getAmPmOption();

    /**
      * @author: shicheng
      * @Date 2020/2/18
      * @Param: apptFeConfirmDateDto
      * @return: void
      * @Descripation: reject System Date And Create Task
      */
    void rejectSystemDateAndCreateTask(ApptFeConfirmDateDto apptFeConfirmDateDto);

    /**
     * Specific Date
     */
    /**
      * @author: shicheng
      * @Date 2020/2/18
      * @Param: appPremCorrId
      * @return: ApptFeConfirmDateDto
      * @Descripation: getSpecificDateDto
      */
    ApptFeConfirmDateDto getSpecificDateDto(String appPremCorrId);

    /**
      * @author: shicheng
      * @Date 2020/2/19
      * @Param: apptFeConfirmDateDto
      * @return: void
      * @Descripation: reject Specific Date
      */
    void rejectSpecificDate(ApptFeConfirmDateDto apptFeConfirmDateDto);

    /**
      * @author: shicheng
      * @Date 2020/2/21
      * @Param: apptFeConfirmDateDto
      * @return: ApptFeConfirmDateDto
      * @Descripation: confirm New Date
      */
    ApptFeConfirmDateDto confirmNewDate(ApptFeConfirmDateDto apptFeConfirmDateDto);

    /**
      * @author: shicheng
      * @Date 2020/2/21
      * @Param: apptFeConfirmDateDto
      * @return: void
      * @Descripation: saveAccSpecificDate
      */
    void saveAccSpecificDate(ApptFeConfirmDateDto apptFeConfirmDateDto);
}
