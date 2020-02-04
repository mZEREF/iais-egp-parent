package com.ecquaria.cloud.moh.iais.service;


import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptBlackoutDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptBlackoutDateQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptNonWorkingDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.InspectorCalendarQueryDto;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

	/**
	 *
	 * @param appointmentDto
	 * @return
	 */
	Map<String,List<Date>> getUnavailableTime(AppointmentDto appointmentDto);


	SearchResult<InspectorCalendarQueryDto> queryInspectorCalendar(SearchParam searchParam);

}
