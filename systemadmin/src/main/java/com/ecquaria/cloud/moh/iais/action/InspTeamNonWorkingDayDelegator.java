package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.appointment.AppointmentConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptNonWorkingDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.service.AppointmentService;
import com.ecquaria.cloud.moh.iais.service.IntranetUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @author: yichen
 * @date time:1/7/2020 3:57 PM
 * @description:
 */
@Delegator(value = "inspTeamNonWorkingDayDelegator")
@Slf4j
public class InspTeamNonWorkingDayDelegator {
	private static final String NON_WKR_DAY_LIST_ATTR = "nonWkrinDayListAttr";
	private static final String NON_WKR_DAY_ATTR = "nonWorkingDayAttr";
	private static final String NON_WKR_DAY_ID_ATTR = "nonWkrDayId";
	private static final String AM_AVAILABILITY__ATTR = "amAvailability";
	private static final String PM_AVAILABILITY__ATTR = "pmAvailability";
	private static final String CURRENT_SHORT_NAME = "currentGroupId";

	private static final String AM_START = "9:00:00";
	private static final String AM_END = "13:00:00";
	private static final String PM_START = "14:00:00";
	private static final String PM_END = "18:00:00";


	@Autowired
	private IntranetUserService intranetUserService;

	@Autowired
	private AppointmentService appointmentService;

	/**
	 * StartStep: startStep
	 * @param bpc
	 * @throws IllegalAccessException
	 */
	public void startStep(BaseProcessClass bpc) {
		HttpServletRequest request = bpc.request;

		AuditTrailHelper.auditFunction("Appointment",
				"Non working day");
		ParamUtil.setSessionAttr(request, NON_WKR_DAY_LIST_ATTR, null);
		ParamUtil.setSessionAttr(request, CURRENT_SHORT_NAME, null);
	}

	/**
	 * StartStep: preLoad
	 * @param bpc
	 * @throws IllegalAccessException
	 */
	public void preLoad(BaseProcessClass bpc) {
		HttpServletRequest request = bpc.request;

		ParamUtil.setSessionAttr(request, NON_WKR_DAY_ATTR, null);

		//userid -->> current user group , find it lead to group
		LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);

		if (!Optional.ofNullable(loginContext).isPresent()){
			log.info("can not find current user");
			return;
		}

		String userId = loginContext.getUserId();

		SearchParam workingGroupQuery = new SearchParam(WorkingGroupQueryDto.class.getName());
		workingGroupQuery.addParam("userId", userId);
		QueryHelp.setMainSql("systemAdmin", "getWorkingGroupByUserId", workingGroupQuery);
		workingGroupQuery.addFilter("userId", userId, true);


		SearchResult<WorkingGroupQueryDto> searchResult = intranetUserService.getWorkingGroupBySearchParam(workingGroupQuery);
		if (searchResult == null || IaisCommonUtils.isEmpty(searchResult.getRows())){
			return;
		}

		List<WorkingGroupQueryDto> workingGroupQueryList = searchResult.getRows();

		List<ApptNonWorkingDateDto> nonWorkingDateListByWorkGroupId = null;
		String shotName = ParamUtil.getString(request, AppointmentConstants.APPOINTMENT_WORKING_GROUP_NAME_OPT);

		if (shotName != null){
			nonWorkingDateListByWorkGroupId = appointmentService.getNonWorkingDateListByWorkGroupId(shotName);
			ParamUtil.setSessionAttr(request, CURRENT_SHORT_NAME, shotName);
		}else {
			Optional<WorkingGroupQueryDto> wrkOtional = Optional.ofNullable(workingGroupQueryList.get(0));
			if (wrkOtional.isPresent()){
				String defualtId = wrkOtional.get().getId();
				nonWorkingDateListByWorkGroupId = appointmentService.getNonWorkingDateListByWorkGroupId(defualtId);
				ParamUtil.setSessionAttr(request, CURRENT_SHORT_NAME, defualtId);
			}
		}

		List<SelectOption> wrlGrpNameOpt = IaisCommonUtils.genNewArrayList();
		workingGroupQueryList.forEach(wkr -> {
			String groupId = wkr.getId();
			String groupName = wkr.getGroupName();
			wrlGrpNameOpt.add(new SelectOption(groupId, groupName));
		});

		if (IaisCommonUtils.isEmpty(nonWorkingDateListByWorkGroupId)){
			nonWorkingDateListByWorkGroupId = IaisCommonUtils.genNewArrayList();
		}

		ParamUtil.setSessionAttr(bpc.request, AppointmentConstants.APPOINTMENT_WORKING_GROUP_NAME_OPT, (Serializable) wrlGrpNameOpt);

		List<ApptNonWorkingDateDto> sortDayList = sortNonWorkingDay(nonWorkingDateListByWorkGroupId);

		ParamUtil.setSessionAttr(request, NON_WKR_DAY_LIST_ATTR, (Serializable) sortDayList);
	}

	/**
	 * @author: yichen
	 * @description: the code need to optimize
	 * @param:
	 * @return:
	 */
	private List<ApptNonWorkingDateDto> sortNonWorkingDay(List<ApptNonWorkingDateDto> nonWorkingDateList){
		List<String> wkrDays = new ArrayList<>(Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"));
		List<ApptNonWorkingDateDto> retList = new ArrayList<>(7);
		LinkedHashMap<String, ApptNonWorkingDateDto> map = new LinkedHashMap<>(7);
		for (int i = 0; i < wkrDays.size(); i++){
			ApptNonWorkingDateDto nonWorkingDateDto = new ApptNonWorkingDateDto();
			nonWorkingDateDto.setId(UUID.randomUUID().toString());
			nonWorkingDateDto.setSrcSystemId(AppointmentConstants.APPT_SRC_SYSTEM_PK_ID);
			nonWorkingDateDto.setRecursivceDate(wkrDays.get(i));
			nonWorkingDateDto.setNonWkrDay(false);
			nonWorkingDateDto.setDesc("");
			nonWorkingDateDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
			nonWorkingDateDto.setStartAt(Time.valueOf(AM_START));
			nonWorkingDateDto.setEndAt(Time.valueOf(PM_END));
			nonWorkingDateDto.setAm(true);
			nonWorkingDateDto.setPm(true);
			map.put(wkrDays.get(i), nonWorkingDateDto);

			for (ApptNonWorkingDateDto workingDateDto : nonWorkingDateList){
				String recursivceDate = workingDateDto.getRecursivceDate();
				if (wkrDays.get(i).equals(recursivceDate)){
					map.put(wkrDays.get(i), workingDateDto);
				}
			}
		}

		for (Map.Entry<String, ApptNonWorkingDateDto> entry : map.entrySet()) {
			retList.add(entry.getValue());
		}

		return retList;
	}

	/**
	 * StartStep: doSearch
	 * @param bpc
	 * @throws IllegalAccessException
	 */
	public void doSearch(BaseProcessClass bpc) {
		HttpServletRequest request = bpc.request;

		//do nothing
	}

	/**
	 * StartStep: switchAction
	 * @param bpc
	 * @throws IllegalAccessException
	 */
	public void switchAction(BaseProcessClass bpc) {
		HttpServletRequest request = bpc.request;

	}



}
