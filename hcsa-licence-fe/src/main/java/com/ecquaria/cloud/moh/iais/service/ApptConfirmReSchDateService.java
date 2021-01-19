package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.appointment.ProcessReSchedulingDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptViewDto;

import java.util.List;

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
    ProcessReSchedulingDto getApptSystemDateByCorrId(String appPremCorrId, String appStatus);

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

    void updateAppStatusCommPool(List<ApptViewDto> apptViewDtos);

    ProcessReSchedulingDto getApptComPolSystemDateByCorrId(String appPremCorrId);

    void comPolReschedulingDate(ProcessReSchedulingDto processReSchedulingDto);
}
