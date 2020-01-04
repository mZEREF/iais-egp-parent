package com.ecquaria.cloud.moh.iais.service;


import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptBlackoutDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptBlackoutDateQueryDto;

/**
 * @author anonymity
 */
public interface AppointmentService {
	SearchResult<ApptBlackoutDateQueryDto> doQuery(SearchParam searchParam);

	Boolean createBlackedOutCalendar(ApptBlackoutDateDto blackoutDateDto);

	Boolean updateBlackedOutCalendar(ApptBlackoutDateDto blackoutDateDto);

	Boolean inActiveBlackedOutCalendar(ApptBlackoutDateDto blackoutDateDto);
}
