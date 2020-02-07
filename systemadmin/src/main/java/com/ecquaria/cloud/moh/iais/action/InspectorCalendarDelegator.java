package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.appointment.AppointmentConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.InspectorCalendarQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppointmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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

	@Autowired
	public InspectorCalendarDelegator(AppointmentService appointmentService){
		this.appointmentService = appointmentService;
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
		log.info("Step 1 ==============>" + bpc.request.getSession().getId());
		request.getSession().removeAttribute("userName");
		request.getSession().removeAttribute(AppointmentConstants.INSPECTOR_CALENDAR_QUERY_ATTR);
		AuditTrailHelper.auditFunction("InspectorCalendar", "View function");
	}

	private void preSelectOpt(HttpServletRequest request){
		List<SelectOption> dropYear = new ArrayList<>();
		Calendar date = Calendar.getInstance();
		int currentYear = date.get(Calendar.YEAR);
		for (int i = currentYear; i > currentYear - 10; i--){
			dropYear.add(new SelectOption(String.valueOf(i), String.valueOf(i)));
		}

		ParamUtil.setRequestAttr(request, "dropYearOpt", dropYear);
	}

	/**
	 * StartStep: preLoad
	 * @param bpc
	 * @throws IllegalAccessException
	 */
	public void preLoad(BaseProcessClass bpc){
		HttpServletRequest request = bpc.request;

		preSelectOpt(request);

		SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);

		QueryHelp.setMainSql("systemAdmin", "queryInspectorCalendar", searchParam);

		SearchResult<InspectorCalendarQueryDto> searchResult =
				appointmentService.queryInspectorCalendar(searchParam);

		ParamUtil.setSessionAttr(request, AppointmentConstants.INSPECTOR_CALENDAR_QUERY_ATTR, searchParam);
		ParamUtil.setSessionAttr(request, AppointmentConstants.INSPECTOR_CALENDAR_RESULT_ATTR, searchResult);
	}

	/**
	 * StartStep: doQuery
	 * @param bpc
	 * @throws IllegalAccessException
	 */
	public void doQuery(BaseProcessClass bpc){
		HttpServletRequest request = bpc.request;
		String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);

		String userName = ParamUtil.getString(request, "userName");
		String yearVal = ParamUtil.getString(request, "dropYearOpt");

		ParamUtil.setSessionAttr(request, "userName", userName);
		ParamUtil.setRequestAttr(request, "dropYear", yearVal);

		//Date year = IaisEGPHelper.parseToDate(yearVal, "yyyy-MM-dd");

		InspectorCalendarQueryDto queryDto = new InspectorCalendarQueryDto();
		queryDto.setUserName(userName);
		queryDto.setYear(yearVal);

		ValidationResult validationResult = WebValidationHelper.validateProperty(queryDto, "search");
		if(validationResult != null && validationResult.isHasErrors()) {
			Map<String, String> errorMap = validationResult.retrieveAll();
			ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
			ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, "N");
		}else {
			SearchParam searchParam = IaisEGPHelper.getSearchParam(request, true, filterParameter);

			if(!StringUtil.isEmpty(userName)){
				searchParam.addFilter("userName", userName, true);
			}

			if(!StringUtil.isEmpty(yearVal)){
				searchParam.addFilter("year", yearVal, true);
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
