package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.appointment.AppointmentConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptAppInfoShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptBlackoutDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptBlackoutDateQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptNonWorkingDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.InspectorCalendarQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UserGroupCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.ApptHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.AppointmentService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationBeClient;
import com.ecquaria.cloud.moh.iais.service.client.AppointmentClient;
import com.ecquaria.cloud.moh.iais.service.client.IntranetUserClient;
import com.ecquaria.cloud.moh.iais.service.client.OnlineApptClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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

	@Autowired
	private IntranetUserClient intranetUserClient;

	@Autowired
	private AppointmentClient appointmentClient;

	@Override
	@SearchTrack(catalog = "Appointment BlackOut Date", key = "search")
	public SearchResult<ApptBlackoutDateQueryDto> doQuery(SearchParam searchParam) {
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
		return onlineApptClient.getNonWorkingDateListByWorkGroupId(AppConsts.MOH_IAIS_SYSTEM_APPT_CLIENT_KEY, groupId).getEntity();
	}

	@Override
	public ApptNonWorkingDateDto updateNonWorkingDate(ApptNonWorkingDateDto nonWorkingDateDto) {
		return onlineApptClient.updateNonWorkingDate(nonWorkingDateDto).getEntity();
	}

	@Override
	public OrgUserDto getOrgUserDtoById(String userId) {
		return intranetUserClient.findIntranetUserById(userId).getEntity();
	}

	@Override
	public List<String> getCurrentUserWorkingGroupId(LoginContext loginContext) {
		if (!Optional.ofNullable(loginContext).isPresent()){
			return null;
		}

		List<UserGroupCorrelationDto> userGroupCorrelationDtos = intranetUserClient
				.getUserGroupCorreByUserId(loginContext.getUserId())
				.getEntity();

		if (!userGroupCorrelationDtos.isEmpty()){
			return Collections.emptyList();
		}

		return userGroupCorrelationDtos.stream().filter(Objects::nonNull).map(UserGroupCorrelationDto::getGroupId)
				.collect(Collectors.toList());
	}

	@Override
	public List<String> getInspectorGroupLeadByLoginUser(LoginContext loginContext) {
		List<String> leadGroupList = IaisCommonUtils.genNewArrayList();

		List<String> roleList = new ArrayList<>(loginContext.getRoleIds());
		if(roleList.contains(RoleConsts.USER_ROLE_INSPECTION_LEAD)){
			List<UserGroupCorrelationDto> userGroupCorrelationList = intranetUserClient
					.getUserGroupCorreByUserId(loginContext.getUserId()).getEntity();

			leadGroupList = userGroupCorrelationList.stream()
					.filter(ugc -> ugc.getIsLeadForGroup() == 1).map(UserGroupCorrelationDto::getGroupId).collect(Collectors.toList());

		}

		return leadGroupList;
	}

	@Override
	public Boolean isUnavailableTime(String groupShotName, Date startDate, Date endDate) {
		ApptAppInfoShowDto reqParam = new ApptAppInfoShowDto();
		reqParam.setGroupNames(Arrays.asList(groupShotName));
		reqParam.setClsStatus(Arrays.asList(AppointmentConstants.CALENDAR_STATUS_RESERVED));
		reqParam.setSrcSystemId(AppointmentConstants.APPT_SRC_SYSTEM_PK_ID);


		List<Date> unavailableTime = MiscUtil.getDateInPeriodByRecurrence(startDate,
				endDate);

		//current group
		List<ApptUserCalendarDto> result = appointmentClient.getAllCalendarBySrcIdAndGroupNameAndStatus(reqParam).getEntity();
		for(ApptUserCalendarDto auc : result){
			List<String> inspectionDate = ApptHelper.getInspDateByCalendar(auc);
			for (Date date : unavailableTime) {
				for (String inspDate : inspectionDate) {
					String val = IaisEGPHelper.parseToString(date, "yyyy-MM-dd");
					if (inspDate.equals(val)) {
						return Boolean.TRUE;
					}
				}
			}
		}

		return Boolean.FALSE;
	}



	@Override
	@SearchTrack(catalog = "Appointment Inspector Calendar", key = "search")
	public SearchResult<InspectorCalendarQueryDto> queryInspectorCalendar(SearchParam searchParam) {
		return onlineApptClient.queryInspectorCalendar(searchParam).getEntity();
	}

}
