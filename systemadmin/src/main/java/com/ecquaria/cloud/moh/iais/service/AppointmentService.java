package com.ecquaria.cloud.moh.iais.service;


import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptBlackoutDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptBlackoutDateQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptSrcSystemDto;

/**
 * @author anonymity
 */
public interface AppointmentService {
	SearchResult<ApptBlackoutDateQueryDto> doQuery(SearchParam searchParam);

	void getAssignTaskInspectionDateByGroup();


	Boolean createBlackedOutCalendar(ApptSrcSystemDto srcSystemDto);

	Boolean updateBlackedOutCalendar(ApptBlackoutDateDto blackoutDateDto);
}
