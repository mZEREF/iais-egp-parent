package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptBlackoutDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptBlackoutDateQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptSrcSystemDto;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SqlHelper;
import com.ecquaria.cloud.moh.iais.service.AppointmentService;
import com.ecquaria.cloud.moh.iais.service.client.OnlineApptClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	private OnlineApptClient IntranetUserClient;

	@Override
	public SearchResult<ApptBlackoutDateQueryDto> doQuery(SearchParam searchParam) {
		return onlineApptClient.doQuery(searchParam).getEntity();
	}

    @Override
    public void getAssignTaskInspectionDateByGroup() {

    }

    @Override
	public Boolean createBlackedOutCalendar(ApptSrcSystemDto srcSystemDto) {
		return null;
	}

	@Override
	public Boolean updateBlackedOutCalendar(ApptBlackoutDateDto blackoutDateDto) {
		return null;
	}
}
