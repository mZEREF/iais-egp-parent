package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.appointment.AppointmentConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptNonWorkingDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppointmentService;
import com.ecquaria.cloud.moh.iais.service.IntranetUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
	private static final String NON_WKR_DAY_ATTR = "nonWarkingDto";
	private static final String NON_WKR_DAY_ID_ATTR = "nonWkrDayId";
	private static final String AM_AVAILABILITY__ATTR = "amAvailability";
	private static final String PM_AVAILABILITY__ATTR = "pmAvailability";

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
	}

	/**
	 * StartStep: preLoad
	 * @param bpc
	 * @throws IllegalAccessException
	 */
	public void preLoad(BaseProcessClass bpc) {
		HttpServletRequest request = bpc.request;

		ParamUtil.setSessionAttr(request, NON_WKR_DAY_ATTR, null);

		SearchParam workingGroupQuery = new SearchParam(WorkingGroupQueryDto.class.getName());
		QueryHelp.setMainSql("systemAdmin", "getWorkingGroupByUserId", workingGroupQuery);

		List<WorkingGroupQueryDto> workingGroupQueryList = intranetUserService.getWorkingGroupBySearchParam(workingGroupQuery)
				.getRows();

		if (IaisCommonUtils.isEmpty(workingGroupQueryList)){
			return;
		}

		List<ApptNonWorkingDateDto> nonWorkingDateListByWorkGroupId;
		String shotName = ParamUtil.getString(request, AppointmentConstants.APPOINTMENT_WORKING_GROUP_NAME_OPT);

		if (shotName != null){
			nonWorkingDateListByWorkGroupId = appointmentService.getNonWorkingDateListByWorkGroupId(shotName);
			ParamUtil.setSessionAttr(request, "currentGroupId", shotName);
		}else {
			String defualtId = workingGroupQueryList.stream().findFirst().get().getId();
			nonWorkingDateListByWorkGroupId = appointmentService.getNonWorkingDateListByWorkGroupId(defualtId);
			ParamUtil.setSessionAttr(request, "currentGroupId", defualtId);
		}

		List<SelectOption> wrlGrpNameOpt = new ArrayList<>();
		workingGroupQueryList.stream().forEach(wkr -> {
			String groupId = wkr.getId();
			String groupName = wkr.getGroupName();
			wrlGrpNameOpt.add(new SelectOption(groupId, groupName));
		});


		ParamUtil.setSessionAttr(bpc.request, AppointmentConstants.APPOINTMENT_WORKING_GROUP_NAME_OPT, (Serializable) wrlGrpNameOpt);

		additionWorkingDay(nonWorkingDateListByWorkGroupId);

		ParamUtil.setSessionAttr(request, NON_WKR_DAY_LIST_ATTR, (Serializable) nonWorkingDateListByWorkGroupId);
	}

	private void additionWorkingDay(List<ApptNonWorkingDateDto> nonWorkingDateList){
		if (IaisCommonUtils.isEmpty(nonWorkingDateList)){
			return;
		}

		List<String> wkrDays = new ArrayList<>(Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"));
		String[] nonWkrDays = new String[nonWorkingDateList.size()];
		for (int i = 0; i < nonWorkingDateList.size(); i++){
			nonWkrDays[i] = nonWorkingDateList.get(i).getRecursivceDate();
		}

		wkrDays.removeAll(Arrays.asList(nonWkrDays));
		wkrDays.stream().forEach(s -> {
			ApptNonWorkingDateDto nonWorkingDateDto = new ApptNonWorkingDateDto();
			nonWorkingDateDto.setId(UUID.randomUUID().toString());
			nonWorkingDateDto.setSrcSystemId(AppConsts.MOH_IAIS_SYSTEM_SRC_ID);
			nonWorkingDateDto.setRecursivceDate(s);
			nonWorkingDateDto.setNonWkrDay(false);
			nonWorkingDateDto.setDesc("");
			nonWorkingDateDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
			nonWorkingDateDto.setStartAt(Time.valueOf("9:00:00"));
			nonWorkingDateDto.setEndAt(Time.valueOf("17:00:00"));
			nonWorkingDateList.add(nonWorkingDateDto);
		});


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

	/**
	 * StartStep: preUpdate
	 * @param bpc
	 * @throws IllegalAccessException
	 */
	public void preUpdate(BaseProcessClass bpc) {
		HttpServletRequest request = bpc.request;

		String nonWkrDayId = ParamUtil.getMaskedString(request, NON_WKR_DAY_ID_ATTR);

		if (!StringUtils.isEmpty(nonWkrDayId)){
			List<ApptNonWorkingDateDto> nonWorkingDateListByWorkGroupId = (List<ApptNonWorkingDateDto>) ParamUtil.getSessionAttr(request, NON_WKR_DAY_LIST_ATTR);
			ApptNonWorkingDateDto nonWorkingDateDto = nonWorkingDateListByWorkGroupId.stream().filter(apptNonWorkingDateDto ->  !StringUtils.isEmpty(apptNonWorkingDateDto.getId()))
					.filter(apptNonWorkingDateDto -> apptNonWorkingDateDto.getId().equals(nonWkrDayId)).findFirst().get();
			ParamUtil.setSessionAttr(request, NON_WKR_DAY_ATTR, nonWorkingDateDto);
		}
	}


	/**
	 * StartStep: updateNonWorkingDay
	 * @param bpc
	 * @throws IllegalAccessException
	 */
	public void updateNonWorkingDay(BaseProcessClass bpc) {
		HttpServletRequest request = bpc.request;
		String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
		if ("doBack".equals(currentAction)){
			ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.YES);
			return;
		}

		String shortName = (String) ParamUtil.getSessionAttr(request, "currentGroupId");
		String amAvailability = ParamUtil.getString(request, AM_AVAILABILITY__ATTR);
		String pmAvailability = ParamUtil.getString(request, PM_AVAILABILITY__ATTR);
		String status = ParamUtil.getString(request, "status");

		ApptNonWorkingDateDto nonWorkingDateDto = (ApptNonWorkingDateDto) ParamUtil.getSessionAttr(request, NON_WKR_DAY_ATTR);

		nonWorkingDateDto.setShortName(shortName);
		nonWorkingDateDto.setStatus(status);

		if ("Y".equals(amAvailability) && "Y".equals(pmAvailability)){
			nonWorkingDateDto.setStartAt(Time.valueOf("9:00:00"));
			nonWorkingDateDto.setEndAt(Time.valueOf("17:00:00"));
			nonWorkingDateDto.setStatus(AppConsts.COMMON_STATUS_DELETED);
		}else if("Y".equals(amAvailability) && "N".equals(pmAvailability)){
			nonWorkingDateDto.setStartAt(Time.valueOf("9:00:00"));
			nonWorkingDateDto.setEndAt(Time.valueOf("13:00:00"));
			nonWorkingDateDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
		}else if("N".equals(amAvailability) && "Y".equals(pmAvailability)){
			nonWorkingDateDto.setStartAt(Time.valueOf("13:00:00"));
			nonWorkingDateDto.setEndAt(Time.valueOf("17:00:00"));
			nonWorkingDateDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
		}else if("N".equals(amAvailability) && "N".equals(pmAvailability)){
			nonWorkingDateDto.setStartAt(Time.valueOf("9:00:00"));
			nonWorkingDateDto.setEndAt(Time.valueOf("17:00:00"));
			nonWorkingDateDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
		}

		ValidationResult validationResult = WebValidationHelper.validateProperty(nonWorkingDateDto, "update");
		if(validationResult != null && validationResult.isHasErrors()) {
			Map<String, String> errorMap = validationResult.retrieveAll();
			ParamUtil.setRequestAttr(request,SystemAdminBaseConstants.ERROR_MSG, WebValidationHelper.generateJsonStr(errorMap));
			ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.NO);
			return;
		}else {
			appointmentService.updateNonWorkingDate(nonWorkingDateDto);
		}
		ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.YES);
	}



}
