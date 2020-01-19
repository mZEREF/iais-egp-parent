package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptBlackoutDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptBlackoutDateQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptNonWorkingDateDto;
import com.ecquaria.cloud.moh.iais.service.AppointmentService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationBeClient;
import com.ecquaria.cloud.moh.iais.service.client.OnlineApptClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: Hc
 * @Program: iais-parent
 * @Create: 2019-12-27 14:39
 **/
@Slf4j
@Service
public class AppointmentServiceImpl implements AppointmentService {
	@Autowired
	private OnlineApptClient onlineApptClient;

	@Autowired
	private ApplicationBeClient applicationBeClient;

	@Override
	public SearchResult<ApptBlackoutDateQueryDto> doQuery(SearchParam searchParam) {

		List<Date> dates = applicationBeClient.getInspectionRecomInDateByCorreId(null).getEntity();

		return onlineApptClient.doQuery(searchParam).getEntity();
	}

    @Override
	public Boolean createBlackedOutCalendar(ApptBlackoutDateDto blackoutDateDto) {
		return onlineApptClient.createBlackedOutCalendar(blackoutDateDto).getEntity();
	}

	@Override
	public Boolean updateBlackedOutCalendar(ApptBlackoutDateDto blackoutDateDto) {
		return onlineApptClient.updateBlackedOutCalendar(blackoutDateDto).getEntity();
	}

	@Override
	public Boolean inActiveBlackedOutCalendar(ApptBlackoutDateDto blackoutDateDto) {
		return onlineApptClient.inActiveBlackedOutCalendar(blackoutDateDto).getEntity();
	}

	@Override
	public List<Date> getInspectionRecomInDateByCorreId(List<String> taskRefNum){
		return applicationBeClient.getInspectionRecomInDateByCorreId(taskRefNum).getEntity();
	}

	@Override
	public List<ApptNonWorkingDateDto> getNonWorkingDateListByWorkGroupId(String groupId) {
		return onlineApptClient.getNonWorkingDateListByWorkGroupId(groupId).getEntity();
	}

	@Override
	public ApptNonWorkingDateDto updateNonWorkingDate(ApptNonWorkingDateDto nonWorkingDateDto) {
		return onlineApptClient.updateNonWorkingDate(nonWorkingDateDto).getEntity();
	}

	@Override
	public Map<String,List<Date>> getUnavailableTime(AppointmentDto appointmentDto) {
		return onlineApptClient.getUserCalendarByUserId(appointmentDto).getEntity();
	}

}
