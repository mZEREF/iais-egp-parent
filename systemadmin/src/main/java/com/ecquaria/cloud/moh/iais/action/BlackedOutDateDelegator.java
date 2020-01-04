package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.appointment.AppointmentConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptBlackoutDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptBlackoutDateQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppointmentService;
import com.ecquaria.cloud.moh.iais.service.IntranetUserService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
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
    @Autowired
    private AppointmentService appointmentService;

    private TaskService taskService;

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
        ParamUtil.setSessionAttr(request, AppointmentConstants.APPOINTMENT_BLACKED_OUT_DATE_QUERY, null);
        ParamUtil.setSessionAttr(request, AppointmentConstants.APPOINTMENT_BLACKED_OUT_DATE_RESULT, null);
        ParamUtil.setSessionAttr(request, AppointmentConstants.APPOINTMENT_BLACKED_OUT_DATE_ATTR, null);
    }

    /**
     * StartStep: preLoad
     * @param bpc
     * @throws IllegalAccessException
     */
    public void preLoad(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

        //userid -->> current user group , find it lead to group

        String userId = "64F8BB01-F70C-EA11-BE7D-000C29F371DC";

        SearchParam workingGroupQuery = new SearchParam(WorkingGroupQueryDto.class.getName());
        QueryHelp.setMainSql("systemAdmin", "getWorkingGroupByUserId", workingGroupQuery);

        List<WorkingGroupQueryDto> workingGroupQueryList = intranetUserService.getWorkingGroupBySearchParam(workingGroupQuery)
                .getRows();

        if (IaisCommonUtils.isEmpty(workingGroupQueryList)){
            return;
        }

        String defualtId = workingGroupQueryList.stream().findFirst().get().getId();
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

        ParamUtil.setRequestAttr(bpc.request, "dropYearOpt", dropYear);

        SearchParam blackQuery = IaisEGPHelper.getSearchParam(request, filterParameter);

        String isNew = (String) ParamUtil.getSessionAttr(request, "isNewViewData");
        if (isNew.equals("true")){
            blackQuery.addParam("shortName", defualtId);
            blackQuery.addFilter("shortName", defualtId, true);
            ParamUtil.setRequestAttr(request, "shortName", defualtId);
            ParamUtil.setSessionAttr(request, "isNewViewData", "false");
        }

        QueryHelp.setMainSql("systemAdmin", "getBlackedOutDateList", blackQuery);


        List<ApptBlackoutDateQueryDto> blackoutDateQueryList  = appointmentService.doQuery(blackQuery).getRows();

        ParamUtil.setSessionAttr(bpc.request, AppointmentConstants.APPOINTMENT_WORKING_GROUP_NAME_OPT, (Serializable) wrlGrpNameOpt);
        ParamUtil.setSessionAttr(request, AppointmentConstants.APPOINTMENT_BLACKED_OUT_DATE_QUERY, blackQuery);
        ParamUtil.setSessionAttr(request, AppointmentConstants.APPOINTMENT_BLACKED_OUT_DATE_RESULT, (Serializable) blackoutDateQueryList);
    }

    /**
     * StartStep: preCreate
     * @param bpc
     * @throws IllegalAccessException
     */
    public void preCreate(BaseProcessClass bpc)  {
        HttpServletRequest request = bpc.request;

        ParamUtil.setSessionAttr(request, "switchPageAction", "create");
    }


    /**
     * StartStep: preUpdate
     * @param bpc
     * @throws IllegalAccessException
     */
    public void preUpdate(BaseProcessClass bpc)  {
        HttpServletRequest request = bpc.request;

        ParamUtil.setSessionAttr(request, AppointmentConstants.APPOINTMENT_SWITCH_ACTION, "update");
        String blackDateId = ParamUtil.getMaskedString(request, "blackDateId");
        if (blackDateId != null){
            List<ApptBlackoutDateQueryDto> blackoutDateQueryList = (List<ApptBlackoutDateQueryDto>) ParamUtil.getSessionAttr(request, AppointmentConstants.APPOINTMENT_BLACKED_OUT_DATE_RESULT);
            if (!IaisCommonUtils.isEmpty(blackoutDateQueryList)){
                ApptBlackoutDateQueryDto blackoutDateQueryDto = blackoutDateQueryList.stream().filter(item -> item.getId().equals(blackDateId)).findFirst().get();
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

    /**
     * StartStep: updateBlackedOutCalendar
     * @param bpc
     * @throws IllegalAccessException
     */
    public void updateBlackedOutCalendar(BaseProcessClass bpc)  {
        HttpServletRequest request = bpc.request;
        String ldate = ParamUtil.getString(request, "startDate");
        String rdate = ParamUtil.getString(request, "endDate");
        String desc = ParamUtil.getString(request, "desc");

        ApptBlackoutDateQueryDto blackoutDateQueryDto = (ApptBlackoutDateQueryDto) ParamUtil.getSessionAttr(request, AppointmentConstants.APPOINTMENT_BLACKED_OUT_DATE_ATTR);

        Objects.requireNonNull(blackoutDateQueryDto);
        ApptBlackoutDateDto apptBlackoutDateDto = new ApptBlackoutDateDto();
        apptBlackoutDateDto.setId(blackoutDateQueryDto.getId());
        apptBlackoutDateDto.setShortName(blackoutDateQueryDto.getShortName());
        apptBlackoutDateDto.setSrcSystemId(blackoutDateQueryDto.getSrcSystemId());
        apptBlackoutDateDto.setStatus(blackoutDateQueryDto.getStatus());

        Date startDate = IaisEGPHelper.parseToDate(ldate, "dd/MM/yyyy");
        Date endDate = IaisEGPHelper.parseToDate(rdate, "dd/MM/yyyy");
        apptBlackoutDateDto.setStartDate(startDate);
        apptBlackoutDateDto.setEndDate(endDate);

        apptBlackoutDateDto.setDesc(desc);

        ValidationResult validationResult = WebValidationHelper.validateProperty(apptBlackoutDateDto, "update");
        if(validationResult != null && validationResult.isHasErrors()) {
            Map<String, String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            return;
        }else {
            boolean comparatorValue = WebValidationHelper.cpmpareDate(startDate, endDate);
            if (comparatorValue){
                Map<String, String> errorMap = new HashMap<>();
                errorMap.put("customValidation", "CHKL_ERR002");
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                return ;
            }

            appointmentService.updateBlackedOutCalendar(apptBlackoutDateDto);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
        }
    }

    /**
     * StartStep: createBlackedOutCalendar
     * @param bpc
     * @throws IllegalAccessException
     */
    public void createBlackedOutCalendar(BaseProcessClass bpc)  {
        HttpServletRequest request = bpc.request;

        String groupName = ParamUtil.getString(request, AppointmentConstants.APPOINTMENT_WORKING_GROUP_NAME_OPT);
        String ldate = ParamUtil.getString(request, "startDate");
        String rdate = ParamUtil.getString(request, "endDate");
        String desc = ParamUtil.getString(request, "desc");
        String status = ParamUtil.getString(request, "status");

        Date startDate = IaisEGPHelper.parseToDate(ldate, "dd/MM/yyyy");
        Date endDate = IaisEGPHelper.parseToDate(rdate, "dd/MM/yyyy");

        /*List<TaskDto> commPoolByGroupWordId =  taskService.getCommPoolByGroupWordId(groupName);
        commPoolByGroupWordId.stream().forEach(i -> {
            Date dateAssigned = i.getDateAssigned();
        });*/

        ApptBlackoutDateDto blackoutDateDto = new ApptBlackoutDateDto();
        blackoutDateDto.setSrcSystemId(AppConsts.MOH_IAIS_SYSTEM_SRC_ID);
        blackoutDateDto.setShortName(groupName);
        blackoutDateDto.setStartDate(startDate);
        blackoutDateDto.setEndDate(endDate);
        blackoutDateDto.setStatus(status);
        blackoutDateDto.setDesc(desc);
        //blackoutDateDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());

        ValidationResult validationResult = WebValidationHelper.validateProperty(blackoutDateDto, "insert");
        if(validationResult != null && validationResult.isHasErrors()) {
            Map<String, String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            return;
        }else {
            boolean comparatorValue = WebValidationHelper.cpmpareDate(startDate, endDate);
            if (comparatorValue){
                Map<String, String> errorMap = new HashMap<>();
                errorMap.put("customValidation", "CHKL_ERR002");
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                return ;
            }

            boolean isCreate = appointmentService.createBlackedOutCalendar(blackoutDateDto);
            log.debug("createBlackedOutCalendar ===>>> " + blackoutDateDto.getShortName() + " result ==>>> "+ isCreate);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
        }
    }
}
