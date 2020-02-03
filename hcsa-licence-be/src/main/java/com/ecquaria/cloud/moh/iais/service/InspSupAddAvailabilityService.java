package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptNonAvailabilityDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;

import java.util.List;

/**
 * @author Shicheng
 * @date 2020/1/13 13:54
 **/
public interface InspSupAddAvailabilityService {
    /**
      * @author: shicheng
      * @Date 2020/2/3
      * @Param: null
      * @return: List<SelectOption>
      * @Descripation: get RecurrenceOption
      */
    List<SelectOption> getRecurrenceOption();

    /**
      * @author: shicheng
      * @Date 2020/2/3
      * @Param: userId
      * @return: OrgUserDto
      * @Descripation: get OrgUserDto By Id
      */
    OrgUserDto getOrgUserDtoById(String userId);

    /**
      * @author: shicheng
      * @Date 2020/2/3
      * @Param: 
      * @return: 
      * @Descripation: 
      */
    ApptNonAvailabilityDateDto getApptNonAvailabilityDateDtoById(String nonAvaId);
}
