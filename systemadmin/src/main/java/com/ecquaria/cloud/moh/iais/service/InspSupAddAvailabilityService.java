package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptNonAvailabilityDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.GroupRoleFieldDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;

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
      * @Param: nonAvaId
      * @return: ApptNonAvailabilityDateDto
      * @Descripation: get ApptNonAvailabilityDateDto By Id
      */
    ApptNonAvailabilityDateDto getApptNonAvailabilityDateDtoById(String nonAvaId);

    /**
      * @author: shicheng
      * @Date 2020/2/3
      * @Param: removeId
      * @return: void
      * @Descripation: delete Non-Availability By Id
      */
    void deleteNonAvailabilityById(String removeId);

    /**
      * @author: shicheng
      * @Date 2020/2/3
      * @Param: apptNonAvailabilityDateDto
      * @return: ApptNonAvailabilityDateDto
      * @Descripation: create Non-Availability
      */
    List<ApptNonAvailabilityDateDto> createNonAvailability(ApptNonAvailabilityDateDto apptNonAvailabilityDateDto, GroupRoleFieldDto groupRoleFieldDto);

    /**
      * @author: shicheng
      * @Date 2020/2/3
      * @Param: apptNonAvailabilityDateDto
      * @return: ApptNonAvailabilityDateDto
      * @Descripation: update Non-Availability
      */
    ApptNonAvailabilityDateDto updateNonAvailability(ApptNonAvailabilityDateDto apptNonAvailabilityDateDto);

    /**
      * @author: shicheng
      * @Date 2020/2/4
      * @Param: apptNonAvailabilityDateDto
      * @return: String
      * @Descripation: date Is Contain Non Work
      */
    String dateIsContainNonWork(ApptNonAvailabilityDateDto apptNonAvailabilityDateDto);

    /**
     * @author: shicheng
     * @Date 2019/12/11
     * @Param: loginContext
     * @return: List<String>
     * @Descripation: get Work Group Ids By Login
     */
    List<String> getWorkGroupIdsByLogin(LoginContext loginContext);

    /**
     * @author: shicheng
     * @Date 2019/12/11
     * @Param: loginContext
     * @return: GroupRoleFieldDto
     * @Descripation: get Inspector Option By Login
     */
    GroupRoleFieldDto getInspectorOptionByLogin(LoginContext loginContext, List<String> workGroupIds, GroupRoleFieldDto groupRoleFieldDto);

    /**
      * @author: shicheng
      * @Date 2020/5/22
      * @Param: loginId
      * @return: String
      * @Descripation: getApptUserSysCorrIdByLoginId
      */
    List<String> getApptUserSysCorrIdByLoginId(String loginId, List<String> wrkGrpIdList);
}
