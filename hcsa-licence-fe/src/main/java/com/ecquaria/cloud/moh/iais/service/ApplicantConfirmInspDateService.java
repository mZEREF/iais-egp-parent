package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptFeConfirmDateDto;

/**
 * @author Shicheng
 * @date 2020/2/15 17:21
 **/
public interface ApplicantConfirmInspDateService {
    /**
     * System Date
     */


    /**
     * Specific Date
     */
    /**
      * @author: shicheng
      * @Date 2020/2/18
      * @Param: appPremCorrId
      * @return:
      * @Descripation:
      */
    ApptFeConfirmDateDto getSpecificDateDto(String appPremCorrId);
}
