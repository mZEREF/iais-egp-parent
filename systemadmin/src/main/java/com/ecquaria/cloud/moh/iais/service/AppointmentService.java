package com.ecquaria.cloud.moh.iais.service;


import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptBlackoutDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptBlackoutDateQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptNonWorkingDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.InspectorCalendarQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;

import java.util.Date;
import java.util.List;

/**
 * @author anonymity
 */
public interface AppointmentService {
	SearchResult<ApptBlackoutDateQueryDto> doQuery(SearchParam searchParam);

	Boolean createBlackedOutCalendar(ApptBlackoutDateDto blackoutDateDto);

	Boolean updateBlackedOutCalendar(ApptBlackoutDateDto blackoutDateDto);

	Boolean inActiveBlackedOutCalendar(ApptBlackoutDateDto blackoutDateDto);

	List<Date> getInspectionRecomInDateByCorreId(List<String> taskRefNum);

	List<ApptNonWorkingDateDto> getNonWorkingDateListByWorkGroupId(String groupId);

	ApptNonWorkingDateDto updateNonWorkingDate(ApptNonWorkingDateDto nonWorkingDateDto);

	OrgUserDto getOrgUserDtoById(String userId);

	List<String> getCurrentUserWorkingGroupId(LoginContext loginContext);

	List<String> getInspectorGroupLeadByLoginUser(LoginContext loginContext);


	Boolean isUnavailableTime(String groupShotName, Date startDate, Date endDate);

	SearchResult<InspectorCalendarQueryDto> queryInspectorCalendar(SearchParam searchParam);



}
