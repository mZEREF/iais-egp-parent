package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.appointment.AppointmentConstants;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageCodeKey;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptBlackoutDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptBlackoutDateQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author: yichen
 * @date time:12/28/2019 2:25 PM
 * @description:
 */

@Delegator(value = "blackedOutDateDelegator")
@Slf4j
public class BlackedOutDateDelegator {
    private static final String CUSTOM_VALIDATEION_ATTR = "customValidation";
    private static final Integer UPDATE_ACTION = 1;
    private static final Integer CREATE_ACTION = 0;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private IntranetUserService intranetUserService;

    private  final FilterParameter filterParameter = new FilterParameter.Builder()
            .clz(ApptBlackoutDateQueryDto.class)
            .searchAttr(AppointmentConstants.APPOINTMENT_BLACKED_OUT_DATE_QUERY)
            .resultAttr(AppointmentConstants.APPOINTMENT_BLACKED_OUT_DATE_RESULT)
            .sortField("id").sortType(SearchParam.ASCENDING).build();

    /**
     * StartStep: startStep
     * @param bpc
     * @throws IllegalAccessException
     */
    public void startStep(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

        AuditTrailHelper.auditFunction("Appointment",
                "Blacked out date ");

        ParamUtil.setSessionAttr(request, "isNewViewData", "true");
        ParamUtil.setSessionAttr(bpc.request, AppointmentConstants.APPOINTMENT_WORKING_GROUP_NAME_OPT, null);
        ParamUtil.setSessionAttr(request, AppointmentConstants.APPOINTMENT_BLACKED_OUT_DATE_QUERY, null);
        ParamUtil.setSessionAttr(request, AppointmentConstants.APPOINTMENT_BLACKED_OUT_DATE_RESULT, null);

    }

    /**
     * StartStep: preLoad
     * @param bpc
     * @throws IllegalAccessException
     */
    public void preLoad(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, AppointmentConstants.APPOINTMENT_BLACKED_OUT_DATE_ATTR, null);
        //userid -->> current user group , find it lead to group
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);

        if (!Optional.ofNullable(loginContext).isPresent()){
            log.info("can not find current user");
            return;
        }

        String userId = loginContext.getUserId();

        SearchParam workingGroupQuery = new SearchParam(WorkingGroupQueryDto.class.getName());
        QueryHelp.setMainSql("systemAdmin", "getWorkingGroupByUserId", workingGroupQuery);
        workingGroupQuery.addFilter("userId", userId, true);

        List<WorkingGroupQueryDto> workingGroupQueryList = intranetUserService.getWorkingGroupBySearchParam(workingGroupQuery)
                .getRows();

        if (IaisCommonUtils.isEmpty(workingGroupQueryList)){
            return;
        }


        String defualtId = workingGroupQueryList.stream().findFirst().orElse(new WorkingGroupQueryDto()).getId();
        List<SelectOption> wrlGrpNameOpt = new ArrayList<>();
        workingGroupQueryList.stream().forEach(wkr -> {
            String groupId = wkr.getId();
            String groupName = wkr.getGroupName();
            wrlGrpNameOpt.add(new SelectOption(groupId, groupName));
        });

        List<SelectOption> dropYear = new ArrayList<>();
        Calendar date = Calendar.getInstance();
        int currentYear = date.get(Calendar.YEAR);
        for (int i = currentYear; i > currentYear - 10; i--){
            dropYear.add(new SelectOption(String.valueOf(i), String.valueOf(i)));
        }

        ParamUtil.setRequestAttr(bpc.request, AppointmentConstants.APPOINTMENT_DROP_YEAR_OPT, dropYear);

        SearchParam blackQuery = IaisEGPHelper.getSearchParam(request, filterParameter);

        String isNew = (String) ParamUtil.getSessionAttr(request, "isNewViewData");
        if (isNew.equals("true")){
            blackQuery.addParam("shortName", defualtId);
            blackQuery.addFilter("shortName", defualtId, true);
            ParamUtil.setRequestAttr(request, "shortName", defualtId);
            ParamUtil.setSessionAttr(request, "isNewViewData", "false");
        }

        QueryHelp.setMainSql("systemAdmin", "getBlackedOutDateList", blackQuery);


        SearchResult<ApptBlackoutDateQueryDto> blackoutDateQueryList  = appointmentService.doQuery(blackQuery);

        ParamUtil.setSessionAttr(bpc.request, AppointmentConstants.APPOINTMENT_WORKING_GROUP_NAME_OPT, (Serializable) wrlGrpNameOpt);
        ParamUtil.setSessionAttr(request, AppointmentConstants.APPOINTMENT_BLACKED_OUT_DATE_QUERY, blackQuery);
        ParamUtil.setSessionAttr(request, AppointmentConstants.APPOINTMENT_BLACKED_OUT_DATE_RESULT, blackoutDateQueryList);
    }

    /**
     * StartStep: preCreate
     * @param bpc
     * @throws IllegalAccessException
     */
    public void preCreate(BaseProcessClass bpc)  {
        HttpServletRequest request = bpc.request;

        ParamUtil.setSessionAttr(request, AppointmentConstants.APPOINTMENT_SWITCH_ACTION, "create");
    }


    /**
     * StartStep: preUpdate
     * @param bpc
     * @throws IllegalAccessException
     */
    public void preUpdate(BaseProcessClass bpc)  {
        HttpServletRequest request = bpc.request;

        ParamUtil.setSessionAttr(request, AppointmentConstants.APPOINTMENT_SWITCH_ACTION, "update");
        String blackDateId = ParamUtil.getMaskedString(request, AppointmentConstants.APPOINTMENT_BLACKED_OUT_DATE_ID);
        if (blackDateId != null){
            SearchResult<ApptBlackoutDateQueryDto> dtoSearchResult = (SearchResult<ApptBlackoutDateQueryDto>) ParamUtil.getSessionAttr(request, AppointmentConstants.APPOINTMENT_BLACKED_OUT_DATE_RESULT);
            if (dtoSearchResult != null && !IaisCommonUtils.isEmpty(dtoSearchResult.getRows())){
                List<ApptBlackoutDateQueryDto> blackoutDateQueryList = dtoSearchResult.getRows();
                ApptBlackoutDateQueryDto blackoutDateQueryDto = blackoutDateQueryList.stream()
                        .filter(item -> item.getId().equals(blackDateId)).findFirst().orElse(null);
                if (Optional.of(blackoutDateQueryDto).isPresent()){
                    ParamUtil.setSessionAttr(request, AppointmentConstants.APPOINTMENT_BLACKED_OUT_DATE_ATTR, blackoutDateQueryDto);
                }
            }
        }
    }

    /**
     * StartStep: nextAction
     * @param bpc
     * @throws IllegalAccessException
     */
    public void nextAction(BaseProcessClass bpc)  {
        HttpServletRequest request = bpc.request;
    }

    /**
     * StartStep: doSearch
     * @param bpc
     * @throws IllegalAccessException
     */
    public void doSearch(BaseProcessClass bpc)  {
        HttpServletRequest request = bpc.request;

        String groupName = ParamUtil.getString(request, AppointmentConstants.APPOINTMENT_WORKING_GROUP_NAME_OPT);
        String dropYear = ParamUtil.getString(request, AppointmentConstants.APPOINTMENT_DROP_YEAR_OPT);
        String startDate = ParamUtil.getString(request, "startDate");
        String endDate = ParamUtil.getString(request, "endDate");
        String status = ParamUtil.getString(request, "status");


        SearchParam blackQuery = IaisEGPHelper.getSearchParam(request, true, filterParameter);


        if (!StringUtils.isEmpty(groupName)){
            blackQuery.addFilter("shortName", groupName, true);
            ParamUtil.setRequestAttr(request, "shortName", groupName);
        }

        if (!StringUtils.isEmpty(dropYear)){
            ParamUtil.setRequestAttr(request, "dropYear", dropYear);
            blackQuery.addFilter("startDate", dropYear + "-01-01", true);
            blackQuery.addFilter("endDate", dropYear + "-12-31", true);
        }

        if (!StringUtils.isEmpty(startDate)){
            ParamUtil.setRequestAttr(request, "startDate", startDate);
            blackQuery.addFilter("bkStartDate", startDate, true);
        }

        if (!StringUtils.isEmpty(endDate)){
            ParamUtil.setRequestAttr(request, "endDate", endDate);
            blackQuery.addFilter("bkEndDate", endDate, true);
        }

        if (!StringUtils.isEmpty(status)){
            ParamUtil.setRequestAttr(request, "status", status);
            blackQuery.addFilter("status", status, true);
        }
    }

    /**
     * StartStep: doDelete
     * @param bpc
     * @throws IllegalAccessException
     */
    public void doDelete(BaseProcessClass bpc)  {
        HttpServletRequest request = bpc.request;

        String blackDateId = ParamUtil.getMaskedString(request, AppointmentConstants.APPOINTMENT_BLACKED_OUT_DATE_ID);
        ApptBlackoutDateDto blackoutDateDto = new ApptBlackoutDateDto();
        blackoutDateDto.setId(blackDateId);
        appointmentService.inActiveBlackedOutCalendar(blackoutDateDto);

        ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
    }

    private void doCreateOrUpdate(HttpServletRequest request, Integer bit){
        int ans = bit & 1;

        String propertName = "";
        String groupName = ParamUtil.getString(request, AppointmentConstants.APPOINTMENT_WORKING_GROUP_NAME_OPT);
        String ldate = ParamUtil.getString(request, "startDate");
        String rdate = ParamUtil.getString(request, "endDate");
        String desc = ParamUtil.getString(request, "desc");
        String status = ParamUtil.getString(request, "status");

        ParamUtil.setRequestAttr(request, "shortName", groupName);
        ParamUtil.setRequestAttr(request, "startDate", ldate);
        ParamUtil.setRequestAttr(request, "endDate", rdate);
        ParamUtil.setRequestAttr(request, "desc", desc);
        ParamUtil.setRequestAttr(request, "status", status);

        ApptBlackoutDateDto blackoutDateDto = new ApptBlackoutDateDto();
        if (ans == CREATE_ACTION) {
            propertName = "insert";
            blackoutDateDto.setSrcSystemId(AppConsts.MOH_IAIS_SYSTEM_APPT_CLIENT_KEY);
            blackoutDateDto.setShortName(groupName);
            blackoutDateDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);

        }else if ((ans == UPDATE_ACTION)){
            propertName = "update";
            ApptBlackoutDateQueryDto blackoutDateQueryDto = (ApptBlackoutDateQueryDto) ParamUtil.getSessionAttr(request, AppointmentConstants.APPOINTMENT_BLACKED_OUT_DATE_ATTR);
            Objects.requireNonNull(blackoutDateQueryDto);
            blackoutDateDto.setId(blackoutDateQueryDto.getId());
            blackoutDateDto.setShortName(blackoutDateQueryDto.getShortName());
            blackoutDateDto.setSrcSystemId(blackoutDateQueryDto.getSrcSystemId());
            blackoutDateDto.setStatus(status);
        }

        if (!StringUtils.isEmpty(ldate)){
            blackoutDateDto.setStartDate(IaisEGPHelper.parseToDate(ldate, "dd/MM/yyyy"));
        }

        if (!StringUtils.isEmpty(rdate)){
            blackoutDateDto.setEndDate(IaisEGPHelper.parseToDate(rdate, "dd/MM/yyyy"));
        }

        blackoutDateDto.setDesc(desc);

        blackoutDateDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());

        ValidationResult validationResult = WebValidationHelper.validateProperty(blackoutDateDto, propertName);
        Map<String, String> errorMap = new HashMap<>();
        if(validationResult != null && validationResult.isHasErrors()) {
            errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            return;
        }

        boolean isAfterDate = IaisEGPHelper.isAfterDate(IaisEGPHelper.parseToDate(ldate, "dd/MM/yyyy"),
                IaisEGPHelper.parseToDate(rdate, "dd/MM/yyyy"));

        if (!isAfterDate){
            errorMap.put(CUSTOM_VALIDATEION_ATTR, MessageCodeKey.USER_ERR007);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            return ;
        }


        //ERR004	There is an inspection scheduled on that date. This appointment must be rescheduled before this action may be performed.
       boolean canBloack = canBlock(blackoutDateDto.getShortName(), blackoutDateDto.getStartDate(), blackoutDateDto.getEndDate());
        if (!canBloack){
            errorMap.put(CUSTOM_VALIDATEION_ATTR, "There is an inspection scheduled on that date. This appointment must be rescheduled before this action may be performed.");
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            return ;
        }

        errorMap = null;

        switch (ans){
            case 0:
                boolean isCreate = appointmentService.createBlackedOutCalendar(blackoutDateDto);
                log.debug("createBlackedOutCalendar ===>>> " + blackoutDateDto.getShortName() + " result ==>>> "+ isCreate);
                ParamUtil.setRequestAttr(request,"ackMsg", "You have successfully create a block-out date.");
                break;
            case 1:
                appointmentService.updateBlackedOutCalendar(blackoutDateDto);
                ParamUtil.setRequestAttr(request,"ackMsg", "You have successfully update a block-out date.");
                break;
            default:
        }

        ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
    }



    /**
     * StartStep: updateBlackedOutCalendar
     * @param bpc
     * @throws IllegalAccessException
     */
    public void updateBlackedOutCalendar(BaseProcessClass bpc)  {
        HttpServletRequest request = bpc.request;

        doCreateOrUpdate(request, UPDATE_ACTION);
    }

    private Boolean canBlock(String groupName, Date startDate, Date endDate){
        log.info(startDate + "===" + endDate);
        List<Date> currentInspDate = appointmentService.getCurrentWorkingGroupInspectionDate(groupName);
        if (IaisCommonUtils.isEmpty(currentInspDate)){
            return true;
        }

        long reduceDay = (endDate.getTime() - startDate.getTime())/(1000*3600*24);
        log.info("reduceDay : " + reduceDay);

        Calendar calendar = Calendar.getInstance();

        List<Date> includeDayList = new ArrayList<>();
        for (int i = 0; i < reduceDay; i++) {
            calendar.setTime(startDate);
            calendar.add(Calendar.DAY_OF_MONTH, i);
            Date date = calendar.getTime();
            includeDayList.add(date);
        }

        for (Date inspDate : currentInspDate){
            if (includeDayList.contains(inspDate)){return false;}
        }

        return true;
    }

    /**
     * StartStep: createBlackedOutCalendar
     * @param bpc
     * @throws IllegalAccessException
     */
    public void createBlackedOutCalendar(BaseProcessClass bpc)  {
        HttpServletRequest request = bpc.request;

        doCreateOrUpdate(request, CREATE_ACTION);
    }

    /**
     * StartStep: doFilter
     * @param bpc
     * @throws IllegalAccessException
     */
    public void doFilter(BaseProcessClass bpc)  {
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        CrudHelper.doSorting(searchParam,bpc.request);
    }


    /**
     * StartStep: doPage
     * @param bpc
     * @throws IllegalAccessException
     */
    public void doPage(BaseProcessClass bpc)  {
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        CrudHelper.doPaging(searchParam,bpc.request);
    }
}
