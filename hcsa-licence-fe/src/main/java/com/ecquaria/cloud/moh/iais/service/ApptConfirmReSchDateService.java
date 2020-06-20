package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.appointment.ProcessReSchedulingDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;

/**
 * @author Shicheng
 * @date 2020/6/18 14:38
 **/
public interface ApptConfirmReSchDateService {
    /**
      * @author: shicheng
      * @Date 2020/6/19
      * @Param: appId
      * @return: String
      * @Descripation: getAppPremCorrIdByAppNo
      */
    String getAppPremCorrIdByAppId(String appId);

    /**
      * @author: shicheng
      * @Date 2020/6/19
      * @Param: applicationNo
      * @return: ApplicationDto
      * @Descripation: getApplicationDtoByAppNo
      */
    ApplicationDto getApplicationDtoByAppNo(String applicationNo);

    /**
      * @author: shicheng
      * @Date 2020/6/19
      * @Param: appPremCorrId
      * @return: ProcessReSchedulingDto
      * @Descripation: getApptSystemDateByCorrId
      */
    ProcessReSchedulingDto getApptSystemDateByCorrId(String appPremCorrId);

    /**
      * @author: shicheng
      * @Date 2020/6/19
      * @Param: processReSchedulingDto
      * @return: void
      * @Descripation: acceptReschedulingDate
      */
    void acceptReschedulingDate(ProcessReSchedulingDto processReSchedulingDto);

    /**
      * @author: shicheng
      * @Date 2020/6/19
      * @Param: processReSchedulingDto
      * @return: void
      * @Descripation: rejectReschedulingDate
      */
    void rejectReschedulingDate(ProcessReSchedulingDto processReSchedulingDto);
}
