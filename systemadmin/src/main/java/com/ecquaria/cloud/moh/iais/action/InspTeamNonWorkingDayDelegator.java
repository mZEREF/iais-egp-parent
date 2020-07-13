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
import com.ecquaria.cloud.moh.iais.service.InspSupAddAvailabilityService;
import com.ecquaria.cloud.moh.iais.service.IntranetUserService;
import com.ecquaria.cloud.moh.iais.service.PublicHolidayService;
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
//	private static final String CURRENT_SHORT_NAME = "currentGroupName";
	private static final String CURRENT_SHORT_ID = "currentGroupId";

	private static final String AM_START = "9:00:00";
	private static final String AM_END = "13:00:00";
	private static final String PM_START = "14:00:00";
	private static final String PM_END = "18:00:00";


	@Autowired
	private IntranetUserService intranetUserService;

	@Autowired
	private AppointmentService appointmentService;

	@Autowired
    private PublicHolidayService publicHolidayService;

	@Autowired
	InspSupAddAvailabilityService inspSupAddAvailabilityService;

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
//		ParamUtil.setSessionAttr(request, CURRENT_SHORT_NAME, null);
		ParamUtil.setSessionAttr(request, CURRENT_SHORT_ID, null);
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
		String shotId = ParamUtil.getString(request, AppointmentConstants.APPOINTMENT_WORKING_GROUP_NAME_OPT);

		if (shotId != null){
			String groupName = inspSupAddAvailabilityService.getWorkGroupById(shotId).getGroupName();
			nonWorkingDateListByWorkGroupId = appointmentService.getNonWorkingDateListByWorkGroupId(groupName);
			ParamUtil.setSessionAttr(request, CURRENT_SHORT_ID, shotId);
		}else {
			Optional<WorkingGroupQueryDto> wrkOtional = Optional.ofNullable(workingGroupQueryList.get(0));
			if (wrkOtional.isPresent()){
				String defualtGroupId = wrkOtional.get().getId();
				String groupName = inspSupAddAvailabilityService.getWorkGroupById(defualtGroupId).getGroupName();
				nonWorkingDateListByWorkGroupId = appointmentService.getNonWorkingDateListByWorkGroupId(groupName);
				ParamUtil.setSessionAttr(request, CURRENT_SHORT_ID, defualtGroupId);
			}
		}

		List<SelectOption> wrlGrpNameOpt = IaisCommonUtils.genNewArrayList();
		workingGroupQueryList.forEach(wkr -> {
			String groupName = wkr.getGroupName();
			String groupId = wkr.getId();
			wrlGrpNameOpt.add(new SelectOption(groupId, groupName));
		});

		if (IaisCommonUtils.isEmpty(nonWorkingDateListByWorkGroupId)){
			String groupId = (String)ParamUtil.getSessionAttr(request, CURRENT_SHORT_ID);
			String groupName = inspSupAddAvailabilityService.getWorkGroupById(groupId).getGroupName();
			saveWeekend(groupId);
			nonWorkingDateListByWorkGroupId = appointmentService.getNonWorkingDateListByWorkGroupId(groupName);
		}

		ParamUtil.setSessionAttr(bpc.request, AppointmentConstants.APPOINTMENT_WORKING_GROUP_NAME_OPT, (Serializable) wrlGrpNameOpt);

		String groupId = (String)ParamUtil.getSessionAttr(request, CURRENT_SHORT_ID);
		List<ApptNonWorkingDateDto> sortDayList = sortNonWorkingDay(nonWorkingDateListByWorkGroupId,groupId);

		ParamUtil.setSessionAttr(request, NON_WKR_DAY_LIST_ATTR, (Serializable) sortDayList);
	}

	private void saveWeekend(String groupId){

		List<String> wkrDays = new ArrayList<>(Arrays.asList("Saturday", "Sunday"));
		for (int i = 0; i < wkrDays.size(); i++) {
			ApptNonWorkingDateDto nonWorkingDateDto = new ApptNonWorkingDateDto();
			nonWorkingDateDto.setId(UUID.randomUUID().toString());
			nonWorkingDateDto.setSrcSystemId(AppointmentConstants.APPT_SRC_SYSTEM_PK_ID);
			nonWorkingDateDto.setRecursivceDate(wkrDays.get(i));
			nonWorkingDateDto.setDesc("");
			String groupName = inspSupAddAvailabilityService.getWorkGroupById(groupId).getGroupName();
			nonWorkingDateDto.setShortName(groupName);
			nonWorkingDateDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
			nonWorkingDateDto.setStartAt(Time.valueOf(AM_START));
			nonWorkingDateDto.setEndAt(Time.valueOf(PM_END));
			nonWorkingDateDto.setNonWkrDay(true);
			nonWorkingDateDto.setAm(true);
			nonWorkingDateDto.setPm(true);
			appointmentService.updateNonWorkingDate(nonWorkingDateDto);
		}
	}

	/**
	 * @author: yichen
	 * @description: the code need to optimize
	 * @param:
	 * @return:
	 */
	private List<ApptNonWorkingDateDto> sortNonWorkingDay(List<ApptNonWorkingDateDto> nonWorkingDateList ,String groupId){

		List<String> wkrDays = new ArrayList<>(Arrays.asList("Sunday","Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"));
		List<ApptNonWorkingDateDto> retList = new ArrayList<>(7);
		LinkedHashMap<String, ApptNonWorkingDateDto> map = new LinkedHashMap<>(7);
		String groupName = inspSupAddAvailabilityService.getWorkGroupById(groupId).getGroupName();
        List<String> prohibitlist = publicHolidayService.getScheduleInCalender(groupName);
		for (int i = 0; i < wkrDays.size(); i++){
			ApptNonWorkingDateDto nonWorkingDateDto = new ApptNonWorkingDateDto();
			nonWorkingDateDto.setId(UUID.randomUUID().toString());
			nonWorkingDateDto.setSrcSystemId(AppointmentConstants.APPT_SRC_SYSTEM_PK_ID);
			nonWorkingDateDto.setRecursivceDate(wkrDays.get(i));
			nonWorkingDateDto.setDesc("");
			nonWorkingDateDto.setShortName(groupName);
			nonWorkingDateDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
			nonWorkingDateDto.setStartAt(Time.valueOf(AM_START));
			nonWorkingDateDto.setEndAt(Time.valueOf(PM_END));
			if(i < 6 && i >0){
				nonWorkingDateDto.setNonWkrDay(false);
				nonWorkingDateDto.setAm(false);
				nonWorkingDateDto.setPm(false);
			}else{
				nonWorkingDateDto.setNonWkrDay(true);
				nonWorkingDateDto.setAm(true);
				nonWorkingDateDto.setPm(true);
			}
			String weekend = Integer.toString(i);
            if(prohibitlist.contains(weekend)){
				nonWorkingDateDto.setProhibit(true);
            }else{
				nonWorkingDateDto.setProhibit(false);
			}
			map.put(wkrDays.get(i), nonWorkingDateDto);

			for (ApptNonWorkingDateDto workingDateDto : nonWorkingDateList){
				String recursivceDate = workingDateDto.getRecursivceDate();
				if (wkrDays.get(i).equals(recursivceDate)){
					if(prohibitlist.contains(weekend)){
						workingDateDto.setProhibit(true);
					}else{
						workingDateDto.setProhibit(false);
					}
					map.put(wkrDays.get(i), workingDateDto);
				}
			}


		}

		//sort
		for (Map.Entry<String, ApptNonWorkingDateDto> entry : map.entrySet()) {
			retList.add(entry.getValue());
		}
		retList.add(retList.get(0));
		retList.remove(0);


		return retList;
	}

	/**
	 * StartStep: doSearch
	 * @param bpc
	 * @throws IllegalAccessException
	 */
	public void doSearch(BaseProcessClass bpc) {
		HttpServletRequest request = bpc.request;
		String groupName = ParamUtil.getString(bpc.request,"wrlGrpNameOpt");
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
