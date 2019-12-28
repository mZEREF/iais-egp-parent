package com.ecquaria.cloud.moh.iais.service;


import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptBlackoutDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptSrcSystemDto;

/**
 * @author anonymity
 */
public interface AppointmentService {

	Boolean createBlackedOutCalendar(ApptSrcSystemDto srcSystemDto);

	Boolean updateBlackedOutCalendar(ApptBlackoutDateDto blackoutDateDto);
}
