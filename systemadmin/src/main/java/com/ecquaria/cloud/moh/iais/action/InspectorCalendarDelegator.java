package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.appointment.AppointmentConstants;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.InspectorCalendarQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.ApptHelper;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
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
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author: yichen
 * @date time:2/4/2020 11:06 AM
 * @description:   relate to {@link InspSupAddAvailabilityDelegator}
 */

@Delegator(value = "inspectorCalendarDelegator")
@Slf4j
public class InspectorCalendarDelegator {

	private AppointmentService appointmentService;

	private IntranetUserService intranetUserService;

	private static final String INSPECTOR_CALENDAR_GROUP_ID_MAP = "inspector_calendar_group_id_map";

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
		ParamUtil.setSessionAttr(request, AppointmentConstants.USER_NAME_ATTR, null);
		ParamUtil.setSessionAttr(request, AppointmentConstants.INSPECTOR_CALENDAR_QUERY_ATTR, null);
		ParamUtil.setSessionAttr(request, AppointmentConstants.ACTION_VALUE, null);
		ParamUtil.setSessionAttr(request, AppointmentConstants.IS_NEW_VIEW_DATA, AppConsts.TRUE);
		ParamUtil.setSessionAttr(request, AppointmentConstants.INSPECTOR_CALENDAR_RESULT_ATTR, null);
		ParamUtil.setSessionAttr(request, AppointmentConstants.SHORT_NAME_ATTR, null);
		ParamUtil.setSessionAttr(request, AppointmentConstants.APPOINTMENT_DROP_YEAR_OPT, null);
		AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_ONLINE_APPOINTMENT, AuditTrailConsts.FUNCTION_INSPECTOR_CLANDAR);
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

	private List<WorkingGroupQueryDto> getWorkingGroupList(HttpServletRequest request,String userId){
		LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request,
				AppConsts.SESSION_ATTR_LOGIN_USER);
		SearchResult<WorkingGroupQueryDto> searchResult = null;
		if (Optional.ofNullable(loginContext).isPresent()){
			SearchParam workingGroupQuery = new SearchParam(WorkingGroupQueryDto.class.getName());
			workingGroupQuery.addParam("userId", userId);
			QueryHelp.setMainSql("systemAdmin", "getInspWorkingGroup", workingGroupQuery);
			workingGroupQuery.addFilter("userId", userId, true);
			searchResult = intranetUserService.getWorkingGroupBySearchParam(workingGroupQuery);
		}
		return searchResult != null ? searchResult.getRows() : null;
	}

	/**
	 * StartStep: preLoad
	 * @param bpc
	 * @throws IllegalAccessException
	 */
	public void preLoad(BaseProcessClass bpc){
		HttpServletRequest request = bpc.request;
		ParamUtil.setSessionAttr(request, AppointmentConstants.IS_GROUP_LEAD_ATTR, null);
		LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request,
				AppConsts.SESSION_ATTR_LOGIN_USER);

		if (loginContext == null){
			log.info("InspectorCalendarDelegator [preLoad] return");
			return;
		}

		String userId = loginContext.getUserId();
		String userName = loginContext.getUserName();
		List<WorkingGroupQueryDto> workingGroupQueryList = getWorkingGroupList(request, userId);

		Map<String, String> groupIdMap = IaisCommonUtils.genNewHashMap();
		List<SelectOption> wrlGrpNameOpt = IaisCommonUtils.genNewArrayList();
		workingGroupQueryList.forEach(wkr -> {
			String groupName = wkr.getGroupName();
			groupIdMap.put(groupName, wkr.getId());
			wrlGrpNameOpt.add(new SelectOption(groupName, groupName));
		});

		SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
		initCurrentGroupResult(request, workingGroupQueryList, searchParam, groupIdMap, userName);

		SearchResult<InspectorCalendarQueryDto> calendarSearchResult = appointmentService.queryInspectorCalendar(searchParam);
		setYearDrop(request, calendarSearchResult);

		ParamUtil.setRequestAttr(request, AppointmentConstants.INSPECTOR_CALENDAR_RESULT_ATTR, calendarSearchResult);
		ParamUtil.setSessionAttr(request, AppointmentConstants.IS_NEW_VIEW_DATA, AppConsts.FALSE);
		ParamUtil.setSessionAttr(request, INSPECTOR_CALENDAR_GROUP_ID_MAP, (Serializable) groupIdMap);
		ParamUtil.setSessionAttr(request, AppointmentConstants.APPOINTMENT_WORKING_GROUP_NAME_OPT, (Serializable) wrlGrpNameOpt);
		ParamUtil.setSessionAttr(request, AppointmentConstants.INSPECTOR_CALENDAR_QUERY_ATTR, searchParam);
	}

	private boolean isGroupLead(HttpServletRequest request, String groupId){
		LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request,
				AppConsts.SESSION_ATTR_LOGIN_USER);
		List<String> leadGroupList = appointmentService.getInspectorGroupLeadByLoginUser(loginContext);
		return leadGroupList.contains(groupId);
	}

	private void initCurrentGroupResult(HttpServletRequest request, List<WorkingGroupQueryDto> workingGroupQueryList, SearchParam searchParam,
										Map<String, String> groupIdMap, String currentUserName){
		String groupName = workingGroupQueryList.get(0).getGroupName();
		String isNew = (String) ParamUtil.getSessionAttr(request, AppointmentConstants.IS_NEW_VIEW_DATA);
		String selectedGroup = ParamUtil.getString(request, AppointmentConstants.APPOINTMENT_WORKING_GROUP_NAME_OPT);
		String groupId;
		boolean isGroupLead;
		switch (isNew){
			case AppConsts.TRUE:
				groupId = groupIdMap.containsKey(groupName) ? groupIdMap.get(groupName) : "";
				if (isGroupLead = isGroupLead(request, groupId)){
					ParamUtil.setRequestAttr(request, AppointmentConstants.IS_GROUP_LEAD_ATTR, IaisEGPConstant.YES);
					searchParam.addFilter(AppointmentConstants.WRK_GROUP_ATTR, groupName, true);
				}else {
					ParamUtil.setRequestAttr(request, AppointmentConstants.IS_GROUP_LEAD_ATTR, IaisEGPConstant.NO);
				}
				ParamUtil.setRequestAttr(request, "Display_Group_Name_For_New", groupName);
				break;
			default:
				groupId = groupIdMap.containsKey(selectedGroup)
						? groupIdMap.get(selectedGroup) : selectedGroup;
				if (isGroupLead = isGroupLead(request, groupId)){
					ParamUtil.setRequestAttr(request, AppointmentConstants.IS_GROUP_LEAD_ATTR, IaisEGPConstant.YES);
				}else {
					ParamUtil.setRequestAttr(request, AppointmentConstants.IS_GROUP_LEAD_ATTR, IaisEGPConstant.NO);
				}
		}

		if (isGroupLead){
			ParamUtil.setSessionAttr(request, AppointmentConstants.IS_GROUP_LEAD_ATTR, IaisEGPConstant.YES);
			QueryHelp.setMainSql("systemAdmin", "queryCalendarByLead", searchParam);
		}else {
			searchParam.addFilter(AppointmentConstants.USER_NAME_ATTR, currentUserName, true);
			QueryHelp.setMainSql("systemAdmin", "queryCalendarByInspector", searchParam);
		}
	}

	private void setYearDrop(HttpServletRequest request, SearchResult<InspectorCalendarQueryDto> searchResult){
		List<SelectOption> dropYear = (List<SelectOption>) ParamUtil.getSessionAttr(request, AppointmentConstants.APPOINTMENT_DROP_YEAR_OPT);
		ParamUtil.setRequestAttr(request, AppointmentConstants.APPOINTMENT_DROP_YEAR_OPT, dropYear);
		if (IaisCommonUtils.isEmpty(dropYear)){
			if (Optional.ofNullable(searchResult).isPresent()
					&& IaisCommonUtils.isNotEmpty(searchResult.getRows())){
				List<InspectorCalendarQueryDto> queryDto = searchResult.getRows();
				List<Date> dateList = queryDto.stream().map(InspectorCalendarQueryDto::getUserBlockDateEnd).collect(Collectors.toList());
				Date start = Collections.min(dateList);
				Date end = Collections.max(dateList);
				ApptHelper.preYearOption(request, start, end);
			}
		}
	}

	/**
	 * StartStep: doQuery
	 * @param bpc
	 * @throws IllegalAccessException
	 */
	public void doQuery(BaseProcessClass bpc) throws ParseException {
		HttpServletRequest request = bpc.request;
		String groupName = ParamUtil.getString(request, AppointmentConstants.APPOINTMENT_WORKING_GROUP_NAME_OPT);
		String userName = ParamUtil.getString(request, AppointmentConstants.USER_NAME_ATTR);
		String yearVal = ParamUtil.getString(request, AppointmentConstants.APPOINTMENT_DROP_YEAR_OPT);
		String blockDateStart = ParamUtil.getString(request, AppointmentConstants.USER_BLOCK_DATE_START_ATTR);
		String blockDateEnd = ParamUtil.getString(request, AppointmentConstants.USER_BLOCK_DATE_END_ATTR);
		String userBlockDateDescription = ParamUtil.getString(request, AppointmentConstants.USER_BLOCK_DATE_DESCRIPTION_ATTR);
		String recurrence = ParamUtil.getString(request, AppointmentConstants.RECURRENCE_ATTR);
		String recurrenceEndDate = ParamUtil.getString(request, AppointmentConstants.RECURRENCE_END_DATE_ATTR);

		InspectorCalendarQueryDto queryDto = new InspectorCalendarQueryDto();
		queryDto.setGroupName(groupName);
		queryDto.setUserName(userName);
		queryDto.setYearVal(yearVal);
		queryDto.setDescription(userBlockDateDescription);

		if (StringUtil.isNotEmpty(blockDateStart) && StringUtil.isNotEmpty(blockDateEnd)){
			Date std = IaisEGPHelper.parseToDate(blockDateStart);
			Date etd = IaisEGPHelper.parseToDate(blockDateEnd);
			queryDto.setUserBlockDateStart(std);
			queryDto.setUserBlockDateEnd(etd);
		}

		SearchParam searchParam = IaisEGPHelper.getSearchParam(request, true, filterParameter);
		ParamUtil.setRequestAttr(request, AppointmentConstants.INSPECTOR_CALENDAR_VALIDATION_ATTR, queryDto);
		Map<String, String> groupIdMap = (Map<String, String>) ParamUtil.getSessionAttr(request, INSPECTOR_CALENDAR_GROUP_ID_MAP);
		groupIdMap = Optional.ofNullable(groupIdMap).orElseGet(() -> new HashMap<>());

		if(StringUtil.isNotEmpty(groupName)){
			searchParam.addFilter(AppointmentConstants.WRK_GROUP_ATTR, groupName, true);
		}

		boolean isGroupLead = isGroupLead(request, groupIdMap.get(groupName));
		if (isGroupLead){
			ValidationResult validationResult = WebValidationHelper.validateProperty(queryDto, "search");
			if(validationResult != null && validationResult.isHasErrors()) {
				Map<String, String> errorMap = validationResult.retrieveAll();
				ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
				ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
				return;
			}
		}


		if(StringUtil.isNotEmpty(userName)){
			searchParam.addFilter(AppointmentConstants.USER_NAME_ATTR, userName, true);
		}

		if(StringUtil.isNotEmpty(yearVal)){
			searchParam.addFilter(AppointmentConstants.YEAR_ATTR, yearVal, true);
		}

		if(StringUtil.isNotEmpty(blockDateStart)){
			String convertStartDate = Formatter.formatDateTime(IaisEGPHelper.parseToDate(blockDateStart), SystemAdminBaseConstants.DATE_FORMAT);
			searchParam.addFilter(AppointmentConstants.USER_BLOCK_DATE_START_ATTR, convertStartDate, true);
		}

		if(StringUtil.isNotEmpty(blockDateEnd)){
			String convertEndDate = Formatter.formatDateTime(IaisEGPHelper.parseToDate(blockDateEnd), SystemAdminBaseConstants.DATE_FORMAT);
			searchParam.addFilter(AppointmentConstants.USER_BLOCK_DATE_END_ATTR, convertEndDate, true);
		}

		if(StringUtil.isNotEmpty(userBlockDateDescription)){
			searchParam.addFilter(AppointmentConstants.USER_BLOCK_DATE_DESCRIPTION_ATTR, userBlockDateDescription, true);
		}

		if(StringUtil.isNotEmpty(recurrence)){
			searchParam.addFilter(AppointmentConstants.RECURRENCE_ATTR, recurrence, true);
		}

		if(StringUtil.isNotEmpty(recurrenceEndDate)){
			String convertRecurrenceEndDate = Formatter.formatDateTime(IaisEGPHelper.parseToDate(recurrenceEndDate), SystemAdminBaseConstants.DATE_FORMAT);
			searchParam.addFilter(AppointmentConstants.RECURRENCE_END_DATE_ATTR, convertRecurrenceEndDate, true);
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
