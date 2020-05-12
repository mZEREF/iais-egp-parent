package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.appointment.AppointmentConstants;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.InspectorCalendarQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppointmentService;
import com.ecquaria.cloud.moh.iais.service.IntranetUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @author: yichen
 * @date time:2/4/2020 11:06 AM
 * @description:
 */

@Delegator(value = "inspectorCalendarDelegator")
@Slf4j
public class InspectorCalendarDelegator {

	private AppointmentService appointmentService;

	private IntranetUserService intranetUserService;

	@Autowired
	public InspectorCalendarDelegator(AppointmentService appointmentService, IntranetUserService intranetUserService){
		this.appointmentService = appointmentService;
		this.intranetUserService = intranetUserService;
	}


	private FilterParameter filterParameter = new FilterParameter.Builder()
			.clz(InspectorCalendarQueryDto.class)
			.searchAttr(AppointmentConstants.INSPECTOR_CALENDAR_QUERY_ATTR)
			.resultAttr(AppointmentConstants.INSPECTOR_CALENDAR_RESULT_ATTR)
			.sortField("id").build();

	/**
	 * StartStep: startStep
	 * @param bpc
	 * @throws IllegalAccessException
	 */
	public void startStep(BaseProcessClass bpc){
		HttpServletRequest request = bpc.request;
		log.debug(StringUtil.changeForLog("the inspector calendar start ...."));
		request.getSession().removeAttribute(AppointmentConstants.USER_NAME_ATTR);
		request.getSession().removeAttribute(AppointmentConstants.INSPECTOR_CALENDAR_QUERY_ATTR);
		request.getSession().removeAttribute(AppointmentConstants.ACTION_VALUE);

		ParamUtil.setSessionAttr(request, AppointmentConstants.IS_NEW_VIEW_DATA, AppConsts.TRUE);
		ParamUtil.setSessionAttr(request, AppointmentConstants.SHORT_NAME_ATTR, null);
		AuditTrailHelper.auditFunction("InspectorCalendar", "View function");
	}


	/**
	 * StartStep: switchAction
	 * @param bpc
	 * @throws IllegalAccessException
	 */
	public void switchAction(BaseProcessClass bpc){
		HttpServletRequest request = bpc.request;
		String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
		//Go InspSupAddAvailabilityDelegator.inspSupAddAvailabilityPre()
		if ("edit".equals(currentAction)){
			ParamUtil.setSessionAttr(request, AppointmentConstants.ACTION_VALUE, InspectionConstants.SWITCH_ACTION_EDIT);
		}else if ("delete".equals(currentAction)){
			ParamUtil.setSessionAttr(request, AppointmentConstants.ACTION_VALUE, InspectionConstants.SWITCH_ACTION_DELETE);
		}else if ("add".equals(currentAction)){
			ParamUtil.setSessionAttr(request, AppointmentConstants.ACTION_VALUE, InspectionConstants.SWITCH_ACTION_ADD);
		}
	}

	private void preSelectOpt(HttpServletRequest request){
		List<SelectOption> dropYear = IaisCommonUtils.genNewArrayList();


		Calendar date = Calendar.getInstance();
		int currentYear = date.get(Calendar.YEAR);
		for (int i = currentYear; i > currentYear - 10; i--){
			dropYear.add(new SelectOption(String.valueOf(i), String.valueOf(i)));
		}

		ParamUtil.setRequestAttr(request, AppointmentConstants.APPOINTMENT_DROP_YEAR_OPT, dropYear);
	}

	/**
	 * StartStep: preLoad
	 * @param bpc
	 * @throws IllegalAccessException
	 */
	public void preLoad(BaseProcessClass bpc){
		HttpServletRequest request = bpc.request;

		preSelectOpt(request);

		LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request,
				AppConsts.SESSION_ATTR_LOGIN_USER);

		if (loginContext == null){
			log.info(StringUtil.changeForLog("===>> don't have loginContext" + loginContext));
			return;
		}

		String currentUserId = loginContext.getUserId();

		List<String> leadGroupList = appointmentService.getInspectorGroupLeadByLoginUser(loginContext);

		SearchParam workingGroupQuery = new SearchParam(WorkingGroupQueryDto.class.getName());
		workingGroupQuery.addParam("userId", currentUserId);
		QueryHelp.setMainSql("systemAdmin", "getInspWorkingGroup", workingGroupQuery);
		workingGroupQuery.addFilter("userId", currentUserId, true);

		SearchResult<WorkingGroupQueryDto> workingGroupBySearchParam = intranetUserService.getWorkingGroupBySearchParam(workingGroupQuery);

		if (workingGroupBySearchParam == null || IaisCommonUtils.isEmpty(workingGroupBySearchParam.getRows())){
			return;
		}

		List<WorkingGroupQueryDto> workingGroupQueryList = intranetUserService.getWorkingGroupBySearchParam(workingGroupQuery)
				.getRows();

		String isNew = (String) ParamUtil.getSessionAttr(request, AppointmentConstants.IS_NEW_VIEW_DATA);

		Map<String, String> groupNameToIdMap = IaisCommonUtils.genNewHashMap();
		List<SelectOption> wrlGrpNameOpt = IaisCommonUtils.genNewArrayList();
		workingGroupQueryList.forEach(wkr -> {
			//String groupId = wkr.getId();
			String groupName = wkr.getGroupName();
			groupNameToIdMap.put(groupName, wkr.getId());
			wrlGrpNameOpt.add(new SelectOption(groupName, groupName));
		});

		SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);

		String groupName = workingGroupQueryList.get(0).getGroupName();
		String afterSelectWrkGroup = (String) ParamUtil.getScopeAttr(request, AppointmentConstants.SHORT_NAME_ATTR);
		String groupId = groupNameToIdMap.containsKey(groupName) == true ? groupNameToIdMap.get(groupName) : "";
		boolean isGroupLead = false;
		switch (isNew){
			case AppConsts.TRUE:
				if (leadGroupList.contains(groupId)){
					isGroupLead = true;
					ParamUtil.setRequestAttr(request, AppointmentConstants.IS_GROUP_LEAD_ATTR, IaisEGPConstant.YES);
				}else {
					ParamUtil.setRequestAttr(request, AppointmentConstants.IS_GROUP_LEAD_ATTR, IaisEGPConstant.NO);
				}

				searchParam.addFilter(AppointmentConstants.WRK_GROUP_ATTR, groupName, true);
				ParamUtil.setSessionAttr(request, AppointmentConstants.SHORT_NAME_ATTR, groupName);
				ParamUtil.setSessionAttr(request, AppointmentConstants.IS_NEW_VIEW_DATA, "false");
			break;
			default:
				afterSelectWrkGroup = groupNameToIdMap.containsKey(afterSelectWrkGroup) == true
						? groupNameToIdMap.get(afterSelectWrkGroup) : afterSelectWrkGroup;
				if (leadGroupList.contains(afterSelectWrkGroup)){
					isGroupLead = true;
					ParamUtil.setRequestAttr(request, AppointmentConstants.IS_GROUP_LEAD_ATTR, IaisEGPConstant.YES);
				}else {
					ParamUtil.setRequestAttr(request, AppointmentConstants.IS_GROUP_LEAD_ATTR, IaisEGPConstant.NO);
				}
		}

		//For non-lead Inspectors, they will only be able to see their own non-availability.
		if (!isGroupLead){
			searchParam.addFilter("userId", currentUserId, true);
		}

		QueryHelp.setMainSql("systemAdmin", "queryInspectorCalendar", searchParam);
		SearchResult<InspectorCalendarQueryDto> searchResult =
				appointmentService.queryInspectorCalendar(searchParam);

		ParamUtil.setSessionAttr(bpc.request, AppointmentConstants.APPOINTMENT_WORKING_GROUP_NAME_OPT, (Serializable) wrlGrpNameOpt);
		ParamUtil.setSessionAttr(request, AppointmentConstants.INSPECTOR_CALENDAR_QUERY_ATTR, searchParam);
		ParamUtil.setRequestAttr(request, AppointmentConstants.INSPECTOR_CALENDAR_RESULT_ATTR, searchResult);
	}

	/**
	 * StartStep: doQuery
	 * @param bpc
	 * @throws IllegalAccessException
	 */
	public void doQuery(BaseProcessClass bpc){
		HttpServletRequest request = bpc.request;

		String groupName = ParamUtil.getString(request, AppointmentConstants.APPOINTMENT_WORKING_GROUP_NAME_OPT);
		String userName = ParamUtil.getString(request, AppointmentConstants.USER_NAME_ATTR);
		String yearVal = ParamUtil.getString(request, AppointmentConstants.APPOINTMENT_DROP_YEAR_OPT);
		String userBlockDateStart = ParamUtil.getString(request, AppointmentConstants.USER_BLOCK_DATE_START_ATTR);
		String userBlockDateEnd = ParamUtil.getString(request, AppointmentConstants.USER_BLOCK_DATE_END_ATTR);
		String userBlockDateDescription = ParamUtil.getString(request, AppointmentConstants.USER_BLOCK_DATE_DESCRIPTION_ATTR);
		String recurrence = ParamUtil.getString(request, AppointmentConstants.RECURRENCE_ATTR);
		String recurrenceEndDate = ParamUtil.getString(request, AppointmentConstants.RECURRENCE_END_DATE_ATTR);

		InspectorCalendarQueryDto queryDto = new InspectorCalendarQueryDto();
		queryDto.setGroupName(groupName);
		queryDto.setUserName(userName);
		queryDto.setYear(yearVal);
		queryDto.setDescription(userBlockDateDescription);
		ValidationResult validationResult = WebValidationHelper.validateProperty(queryDto, "search");
		if(validationResult != null && validationResult.isHasErrors()) {
			Map<String, String> errorMap = validationResult.retrieveAll();
			ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
			ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
		}else {
			SearchParam searchParam = IaisEGPHelper.getSearchParam(request, true, filterParameter);

			if(!StringUtil.isEmpty(userName)){
				searchParam.addFilter(AppointmentConstants.USER_NAME_ATTR, userName, true);
			}

			if(!StringUtil.isEmpty(yearVal)){
				searchParam.addFilter(AppointmentConstants.YEAR_ATTR, yearVal, true);
			}

			if(!StringUtil.isEmpty(groupName)){
				searchParam.addFilter(AppointmentConstants.WRK_GROUP_ATTR, groupName, true);
			}

			if(!StringUtil.isEmpty(userBlockDateStart)){
				searchParam.addFilter(AppointmentConstants.USER_BLOCK_DATE_START_ATTR, userBlockDateStart, true);
			}

			if(!StringUtil.isEmpty(userBlockDateEnd)){
				searchParam.addFilter(AppointmentConstants.USER_BLOCK_DATE_END_ATTR, userBlockDateEnd, true);
			}

			if(!StringUtil.isEmpty(userBlockDateDescription)){
				searchParam.addFilter(AppointmentConstants.USER_BLOCK_DATE_DESCRIPTION_ATTR, userBlockDateDescription, true);
			}

			if(!StringUtil.isEmpty(recurrence)){
				searchParam.addFilter(AppointmentConstants.RECURRENCE_ATTR, recurrence, true);
			}

			if(!StringUtil.isEmpty(recurrenceEndDate)){
				searchParam.addFilter(AppointmentConstants.RECURRENCE_END_DATE_ATTR, recurrenceEndDate, true);
			}

		}

	}

	/**
	 * StartStep: doPage
	 * @param bpc
	 * @throws IllegalAccessException
	 */
	public void doPaging(BaseProcessClass bpc){
		HttpServletRequest request = bpc.request;
		SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
		CrudHelper.doPaging(searchParam,bpc.request);
	}

	/**
	 * StartStep: doSorting
	 * @param bpc
	 * @throws IllegalAccessException
	 */
	public void doSorting(BaseProcessClass bpc){
		HttpServletRequest request = bpc.request;
		SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
		CrudHelper.doSorting(searchParam,bpc.request);
	}
}
