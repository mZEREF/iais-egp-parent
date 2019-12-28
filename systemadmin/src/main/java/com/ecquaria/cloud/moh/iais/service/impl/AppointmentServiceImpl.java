package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptBlackoutDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptSrcSystemDto;
import com.ecquaria.cloud.moh.iais.service.AppointmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author: Hc
 * @Program: iais-parent
 * @Create: 2019-12-27 14:39
 **/
@Slf4j
@Service
public class AppointmentServiceImpl implements AppointmentService {

	@Override
	public Boolean createBlackedOutCalendar(ApptSrcSystemDto srcSystemDto) {
		return null;
	}

	@Override
	public Boolean updateBlackedOutCalendar(ApptBlackoutDateDto blackoutDateDto) {
		return null;
	}
}
