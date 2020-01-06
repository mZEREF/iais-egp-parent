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
    private static final String CUSTOM_VALIDATEION_ATTR = "customValidation";
    private static final Integer UPDATE_ACTION = 1;
    private static final Integer CREATE_ACTION = 0;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
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

    private void doCreateOrUpdate(HttpServletRequest request, Integer bit){
        int ans = bit & 1;
        ApptBlackoutDateDto blackoutDateDto = null;
        String propertName = "";
        String ldate = ParamUtil.getString(request, "startDate");
        String rdate = ParamUtil.getString(request, "endDate");
        String desc = ParamUtil.getString(request, "desc");

        if (ans == CREATE_ACTION) {
            propertName = "insert";
            blackoutDateDto = new ApptBlackoutDateDto();
            String groupName = ParamUtil.getString(request, AppointmentConstants.APPOINTMENT_WORKING_GROUP_NAME_OPT);
            String status = ParamUtil.getString(request, "status");
            blackoutDateDto.setSrcSystemId(AppConsts.MOH_IAIS_SYSTEM_SRC_ID);
            blackoutDateDto.setShortName(groupName);
            blackoutDateDto.setStatus(status);

        }else if ((ans == UPDATE_ACTION)){
            propertName = "update";
            ApptBlackoutDateQueryDto blackoutDateQueryDto = (ApptBlackoutDateQueryDto) ParamUtil.getSessionAttr(request, AppointmentConstants.APPOINTMENT_BLACKED_OUT_DATE_ATTR);
            Objects.requireNonNull(blackoutDateQueryDto);

            blackoutDateDto = new ApptBlackoutDateDto();
            blackoutDateDto.setId(blackoutDateQueryDto.getId());
            blackoutDateDto.setShortName(blackoutDateQueryDto.getShortName());
            blackoutDateDto.setSrcSystemId(blackoutDateQueryDto.getSrcSystemId());
            blackoutDateDto.setStatus(blackoutDateQueryDto.getStatus());
        }

        if (!StringUtils.isEmpty(ldate)){
            blackoutDateDto.setStartDate(IaisEGPHelper.parseToDate(ldate, "dd/MM/yyyy"));
        }

        if (!StringUtils.isEmpty(rdate)){
            blackoutDateDto.setEndDate(IaisEGPHelper.parseToDate(ldate, "dd/MM/yyyy"));
        }

        blackoutDateDto.setDesc(desc);

         //blackoutDateDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());

        ValidationResult validationResult = WebValidationHelper.validateProperty(blackoutDateDto, propertName);
        Map<String, String> errorMap = new HashMap<>();
        if(validationResult != null && validationResult.isHasErrors()) {
            errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            return;
        }

       /* boolean canFlushBlackDate = canFlushBlackDate(blackoutDateDto.getShortName(), blackoutDateDto.getStartDate(), blackoutDateDto.getEndDate());
        if (!canFlushBlackDate){
            errorMap.put(CUSTOM_VALIDATEION_ATTR, "Inspection task is on that day. Black out date cannot be created.");
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            return ;
        }*/

        errorMap = null;

        switch (ans){
            case 0:
                boolean isCreate = appointmentService.createBlackedOutCalendar(blackoutDateDto);
                log.debug("createBlackedOutCalendar ===>>> " + blackoutDateDto.getShortName() + " result ==>>> "+ isCreate);
                break;
            case 1:
                appointmentService.updateBlackedOutCalendar(blackoutDateDto);
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

    private Boolean canFlushBlackDate(String groupName, Date startDate, Date endDate){
        List<String> taskRefNumList = taskService.getDistincTaskRefNumByCurrentGroup(groupName);

        Date testDate = IaisEGPHelper.parseToDate("2020-01-04 16:19:42", "yyyy-MM-DD");
        List<Date> recomDay = new ArrayList<>();
        recomDay.add(testDate);

        for (Date date : recomDay){
            if (date.compareTo(startDate) > 0 || date.compareTo(endDate) < 0){
                return false;
            }
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
}
