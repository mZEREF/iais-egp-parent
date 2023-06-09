package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.appointment.AppointmentConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptBlackoutDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptBlackoutDateQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.ApptHelper;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppointmentService;
import com.ecquaria.cloud.moh.iais.service.IntranetUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.Op;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_ONLINE_APPOINTMENT, AuditTrailConsts.FUNCTION_BLACKOUT_DATE);
        ParamUtil.setSessionAttr(request, "isNewViewData", "true");
        ParamUtil.setSessionAttr(bpc.request, AppointmentConstants.APPOINTMENT_WORKING_GROUP_NAME_OPT, null);
        ParamUtil.setSessionAttr(request, AppointmentConstants.APPOINTMENT_BLACKED_OUT_DATE_QUERY, null);
        ParamUtil.setSessionAttr(request, AppointmentConstants.APPOINTMENT_BLACKED_OUT_DATE_RESULT, null);
        ParamUtil.setSessionAttr(request, AppointmentConstants.APPOINTMENT_DROP_YEAR_OPT, null);
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
        LoginContext lc = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);

        if (lc == null){
            log.info("can not find current user");
            return;
        }

        String userId = lc.getUserId();
        Map<String, String> groupNameToIdMap = IaisCommonUtils.genNewHashMap();
        SearchParam workingGroupQuery = new SearchParam(WorkingGroupQueryDto.class.getName());
        workingGroupQuery.addParam("userId", userId);
        QueryHelp.setMainSql("systemAdmin", "getWorkingGroupByUserId", workingGroupQuery);
        workingGroupQuery.addFilter("userId", userId, true);

        SearchResult<WorkingGroupQueryDto> searchResult = intranetUserService.getWorkingGroupBySearchParam(workingGroupQuery);
        if (searchResult == null || IaisCommonUtils.isEmpty(searchResult.getRows())){
            return;
        }

        List<WorkingGroupQueryDto> workingGroupQueryList = searchResult.getRows();
        String defaultName = workingGroupQueryList.stream().findFirst().orElseGet(() ->new WorkingGroupQueryDto()).getGroupName();
        List<SelectOption> wrlGrpNameOpt = IaisCommonUtils.genNewArrayList();
        workingGroupQueryList.forEach(wkr -> {
            //String groupId = wkr.getId();
            String groupName = wkr.getGroupName();
            groupNameToIdMap.put(groupName, wkr.getId());
            wrlGrpNameOpt.add(new SelectOption(groupName, groupName));
        });

        String isValid = (String) ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.ISVALID);
        if (!IaisEGPConstant.NO.equals(isValid)){
            SearchParam blackQuery = IaisEGPHelper.getSearchParam(request, filterParameter);

            String isNew = (String) ParamUtil.getSessionAttr(request, "isNewViewData");
            if (isNew.equals("true")){
                blackQuery.addParam("shortName", defaultName);
                blackQuery.addFilter("shortName", defaultName, true);
                ParamUtil.setSessionAttr(request, "isNewViewData", "false");
            }

            ParamUtil.setRequestAttr(request, "shortName", defaultName);
            QueryHelp.setMainSql("systemAdmin", "getBlackedOutDateList", blackQuery);

            SearchResult<ApptBlackoutDateQueryDto> blackoutDateQueryList  = appointmentService.doQuery(blackQuery);

            List<SelectOption> dropYear = (List<SelectOption>) ParamUtil.getSessionAttr(request, AppointmentConstants.APPOINTMENT_DROP_YEAR_OPT);
            ParamUtil.setRequestAttr(bpc.request, AppointmentConstants.APPOINTMENT_DROP_YEAR_OPT, dropYear);
            if (IaisCommonUtils.isEmpty(dropYear)){
                if (blackoutDateQueryList != null && !IaisCommonUtils.isEmpty(blackoutDateQueryList.getRows())){
                    List<ApptBlackoutDateQueryDto> queryDto = blackoutDateQueryList.getRows();
                    List<Date> dateList = queryDto.stream().map(ApptBlackoutDateQueryDto::getEndDate).collect(Collectors.toList());

                    Date start = Collections.min(dateList);
                    Date end = Collections.max(dateList);
                    ApptHelper.preYearOption(request, start, end);
                }
            }

            ParamUtil.setSessionAttr(request, AppointmentConstants.APPOINTMENT_BLACKED_OUT_DATE_QUERY, blackQuery);
            ParamUtil.setSessionAttr(request, AppointmentConstants.APPOINTMENT_BLACKED_OUT_DATE_RESULT, blackoutDateQueryList);
        }

        ParamUtil.setSessionAttr(request, AppointmentConstants.APPOINTMENT_WORKING_GROUP_NAME_OPT, (Serializable) wrlGrpNameOpt);
        ParamUtil.setSessionAttr(bpc.request, AppointmentConstants.APPOINTMENT_GROUP_NAME_TO_ID_MAP, (Serializable) groupNameToIdMap);
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
                ApptBlackoutDateQueryDto bd = blackoutDateQueryList.stream()
                        .filter(item -> item.getId().equals(blackDateId)).findFirst().orElse(null);
                if (Optional.ofNullable(bd).isPresent()){
                    ParamUtil.setRequestAttr(request, "shortName", bd.getShortName());
                    ParamUtil.setRequestAttr(request, "startDate", IaisEGPHelper.parseToString(bd.getStartDate(), AppConsts.DEFAULT_DATE_FORMAT));
                    ParamUtil.setRequestAttr(request, "endDate", IaisEGPHelper.parseToString(bd.getEndDate(), AppConsts.DEFAULT_DATE_FORMAT));
                    ParamUtil.setRequestAttr(request, "desc", bd.getDesc());
                    ParamUtil.setRequestAttr(request, "status", bd.getStatus());
                    ParamUtil.setSessionAttr(request, AppointmentConstants.APPOINTMENT_BLACKED_OUT_DATE_ATTR, bd);
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
        String desc = ParamUtil.getString(request, "desc");
        String endDate = ParamUtil.getString(request, "endDate");
        String status = ParamUtil.getString(request, "status");

        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        SearchParam blackQuery = IaisEGPHelper.getSearchParam(request, true, filterParameter);

        boolean anySelect = StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate);
        boolean mandatory = !StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate);
        ParamUtil.setRequestAttr(request, "startDate", startDate);
        ParamUtil.setRequestAttr(request, "endDate", endDate);
        if (!anySelect){
            if (StringUtils.isEmpty(startDate)){
                errorMap.put("inspectionStartDate",MessageUtil.replaceMessage("GENERAL_ERR0006","Blacked Out Date Start","field"));
            }

            if (StringUtils.isEmpty(endDate)){
                errorMap.put("inspectionEndDate",MessageUtil.replaceMessage("GENERAL_ERR0006","Blacked Out Date End","field"));
            }

            if (mandatory){
               Date fromDate =  IaisEGPHelper.parseToDate(startDate);
               Date toDate =  IaisEGPHelper.parseToDate(endDate);
               if (fromDate.after(toDate)){
                   errorMap.put("inspectionStartDate", "GENERAL_ERR0032");
               }else {
                   String convertStartDate = Formatter.formatDateTime(fromDate, SystemAdminBaseConstants.DATE_FORMAT);
                   blackQuery.addFilter("startDate", convertStartDate, true);
                   String convertEndDate = Formatter.formatDateTime(toDate, SystemAdminBaseConstants.DATE_FORMAT);
                   blackQuery.addFilter("endDate", convertEndDate, true);
               }
            }
        }

        if (StringUtils.isNotEmpty(groupName)){
            blackQuery.addFilter("shortName", groupName, true);
        }

        if (StringUtils.isNotEmpty(dropYear)){
            blackQuery.addFilter("year", dropYear, true);
        }

        if (StringUtils.isNotEmpty(desc)){
            blackQuery.addFilter("desc", desc, true);
            ParamUtil.setRequestAttr(request, "desc", desc);
        }

        if (StringUtils.isNotEmpty(status)){
            blackQuery.addFilter("status", status, true);
            ParamUtil.setRequestAttr(request, "status", status);
        }

        if (IaisCommonUtils.isNotEmpty(errorMap)){
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
        }else {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
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

        String propertyName = "";
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
            propertyName = "insert";
            blackoutDateDto.setSrcSystemId(AppointmentConstants.APPT_SRC_SYSTEM_PK_ID);
            blackoutDateDto.setShortName(groupName);
            blackoutDateDto.setStatus(status);
        }else if ((ans == UPDATE_ACTION)){
            propertyName = "update";
            ApptBlackoutDateQueryDto blackoutDateQueryDto = (ApptBlackoutDateQueryDto) ParamUtil.getSessionAttr(request, AppointmentConstants.APPOINTMENT_BLACKED_OUT_DATE_ATTR);
            Objects.requireNonNull(blackoutDateQueryDto);
            blackoutDateDto.setId(blackoutDateQueryDto.getId());
            blackoutDateDto.setShortName(blackoutDateQueryDto.getShortName());
            blackoutDateDto.setSrcSystemId(blackoutDateQueryDto.getSrcSystemId());
            blackoutDateDto.setStatus(status);
        }

        if (StringUtils.isNotEmpty(ldate)){
            blackoutDateDto.setStartDate(IaisEGPHelper.parseToDate(ldate, AppConsts.DEFAULT_DATE_FORMAT));
        }

        if (StringUtils.isNotEmpty(rdate)){
            blackoutDateDto.setEndDate(IaisEGPHelper.parseToDate(rdate, AppConsts.DEFAULT_DATE_FORMAT));
        }

        blackoutDateDto.setDesc(desc);
        blackoutDateDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        ValidationResult vResult = WebValidationHelper.validateProperty(blackoutDateDto, propertyName);
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        if(vResult != null && vResult.isHasErrors()) {
            errorMap = vResult.retrieveAll();
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            return;
        }

        boolean isAfterDate = IaisEGPHelper.isAfterDate(IaisEGPHelper.parseToDate(ldate, AppConsts.DEFAULT_DATE_FORMAT),
                IaisEGPHelper.parseToDate(rdate, AppConsts.DEFAULT_DATE_FORMAT));

        if (!isAfterDate){
            errorMap.put("startDate", "OAPPT_ERR011");
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            return ;
        }

        //ERR004 There is an inspection scheduled on that date. This appointment must be rescheduled before this action may be performed.
       boolean isUnavailableTime = appointmentService.isUnavailableTime(groupName, blackoutDateDto.getStartDate(), blackoutDateDto.getEndDate());
        if (isUnavailableTime){
            errorMap.put(CUSTOM_VALIDATEION_ATTR, "OAPPT_ERR004");
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            return ;
        }

        switch (ans){
            case 0:
                appointmentService.createBlackedOutCalendar(blackoutDateDto);
                ParamUtil.setRequestAttr(request,"ackMsg", MessageUtil.getMessageDesc("OAPPT_ACK023"));
                break;
            case 1:
                appointmentService.updateBlackedOutCalendar(blackoutDateDto);
                ParamUtil.setRequestAttr(request,"ackMsg", MessageUtil.getMessageDesc("OAPPT_ACK024"));
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
