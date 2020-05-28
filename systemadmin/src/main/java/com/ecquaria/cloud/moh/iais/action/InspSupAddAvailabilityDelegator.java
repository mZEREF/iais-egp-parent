package com.ecquaria.cloud.moh.iais.action;

/**
 * @Process: MohInspSupAddAvailability
 *
 * @author Shicheng
 * @date 2019/11/14 18:01
 **/

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.appointment.AppointmentConstants;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptAgencyUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptNonAvailabilityDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.InspectorCalendarQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.GroupRoleFieldDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.InspSupAddAvailabilityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Delegator("inspSupAddAvailabilityDelegator")
@Slf4j
public class InspSupAddAvailabilityDelegator {

    @Autowired
    private InspSupAddAvailabilityService inspSupAddAvailabilityService;

    @Autowired
    private InspSupAddAvailabilityDelegator(InspSupAddAvailabilityService inspSupAddAvailabilityService){
        this.inspSupAddAvailabilityService = inspSupAddAvailabilityService;
    }

    /**
     * StartStep: inspSupAddAvailabilityStart
     *
     * @param bpc
     * @throws
     */
    public void inspSupAddAvailabilityStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspSupAddAvailabilityStart start ...."));
        AuditTrailHelper.auditFunction("Inspection Sup Add Availability", "Inspection Sup Add Availability");
    }

    /**
     * StartStep: inspSupAddAvailabilityInit
     *
     * @param bpc
     * @throws
     */
    public void inspSupAddAvailabilityInit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspSupAddAvailabilityInit start ...."));
        SearchParam searchParam = (SearchParam)ParamUtil.getSessionAttr(bpc.request, AppointmentConstants.INSPECTOR_CALENDAR_QUERY_ATTR);
        SearchResult<InspectorCalendarQueryDto> searchResult = (SearchResult<InspectorCalendarQueryDto>)ParamUtil.getSessionAttr(bpc.request, AppointmentConstants.INSPECTOR_CALENDAR_RESULT_ATTR);
        ParamUtil.setSessionAttr(bpc.request, AppointmentConstants.INSPECTOR_CALENDAR_QUERY_ATTR, searchParam);
        ParamUtil.setSessionAttr(bpc.request, AppointmentConstants.INSPECTOR_CALENDAR_RESULT_ATTR, searchResult);
        ParamUtil.setSessionAttr(bpc.request, "curRole", null);
        ParamUtil.setSessionAttr(bpc.request, "inspSupAddAvailabilityType", null);
        ParamUtil.setSessionAttr(bpc.request, "inspNonAvailabilityDto", null);
        ParamUtil.setSessionAttr(bpc.request, "nonAvaUserName", null);
        ParamUtil.setSessionAttr(bpc.request, "groupRoleFieldDto", null);
    }

    /**
     * StartStep: inspSupAddAvailabilityPre
     *
     * @param bpc
     * @throws
     */
    public void inspSupAddAvailabilityPre(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspSupAddAvailabilityPre start ...."));
        String actionValue = (String)ParamUtil.getSessionAttr(bpc.request, "actionValue");
        if(InspectionConstants.SWITCH_ACTION_ADD.equals(actionValue)) {
            ApptNonAvailabilityDateDto apptNonAvailabilityDateDto = new ApptNonAvailabilityDateDto();
            ParamUtil.setSessionAttr(bpc.request, "inspSupAddAvailabilityType", actionValue);
            ParamUtil.setSessionAttr(bpc.request, "inspNonAvailabilityDto", apptNonAvailabilityDateDto);

        } else if(InspectionConstants.SWITCH_ACTION_EDIT.equals(actionValue)) {
            String nonAvaId = ParamUtil.getMaskedString(bpc.request, "nonAvailId");
            ApptNonAvailabilityDateDto apptNonAvailabilityDateDto = inspSupAddAvailabilityService.getApptNonAvailabilityDateDtoById(nonAvaId);
            ParamUtil.setSessionAttr(bpc.request, "inspSupAddAvailabilityType", actionValue);
            ParamUtil.setSessionAttr(bpc.request, "inspNonAvailabilityDto", apptNonAvailabilityDateDto);

        } else if(InspectionConstants.SWITCH_ACTION_DELETE.equals(actionValue)) {
            String nonAvaId = ParamUtil.getMaskedString(bpc.request, "nonAvailId");
            ApptNonAvailabilityDateDto apptNonAvailabilityDateDto = inspSupAddAvailabilityService.getApptNonAvailabilityDateDtoById(nonAvaId);
            ParamUtil.setSessionAttr(bpc.request, "inspSupAddAvailabilityType", actionValue);
            ParamUtil.setSessionAttr(bpc.request, "inspNonAvailabilityDto", apptNonAvailabilityDateDto);
        } else {
            ParamUtil.setSessionAttr(bpc.request, "inspSupAddAvailabilityType", InspectionConstants.SWITCH_ACTION_BACK);
        }
        ParamUtil.setSessionAttr(bpc.request, "actionValue", actionValue);
    }

    /**
     * StartStep: inspSupAddAvailabilityStep
     *
     * @param bpc
     * @throws
     */
    public void inspSupAddAvailabilityStep(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspSupAddAvailabilityStep start ...."));
        SearchParam searchParam = (SearchParam)ParamUtil.getSessionAttr(bpc.request, AppointmentConstants.INSPECTOR_CALENDAR_QUERY_ATTR);
        SearchResult<InspectorCalendarQueryDto> searchResult = (SearchResult<InspectorCalendarQueryDto>)ParamUtil.getSessionAttr(bpc.request, AppointmentConstants.INSPECTOR_CALENDAR_RESULT_ATTR);
        ParamUtil.setSessionAttr(bpc.request, AppointmentConstants.INSPECTOR_CALENDAR_QUERY_ATTR, searchParam);
        ParamUtil.setSessionAttr(bpc.request, AppointmentConstants.INSPECTOR_CALENDAR_RESULT_ATTR, searchResult);
    }

    /**
     * StartStep: inspSupAddAvailabilityAdd
     *
     * @param bpc
     * @throws
     */
    public void inspSupAddAvailabilityAdd(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspSupAddAvailabilityAdd start ...."));
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        ApptNonAvailabilityDateDto apptNonAvailabilityDateDto = (ApptNonAvailabilityDateDto) ParamUtil.getSessionAttr(bpc.request, "inspNonAvailabilityDto");
        List<String> workGroupIds = inspSupAddAvailabilityService.getWorkGroupIdsByLogin(loginContext);
        if(!IaisCommonUtils.isEmpty(workGroupIds)){
            GroupRoleFieldDto groupRoleFieldDto = new GroupRoleFieldDto();
            groupRoleFieldDto = inspSupAddAvailabilityService.getInspectorOptionByLogin(loginContext, workGroupIds, groupRoleFieldDto);
            List<SelectOption> inspectorOption = groupRoleFieldDto.getMemberOption();
            ParamUtil.setSessionAttr(bpc.request, "curRole", "lead");
            ParamUtil.setSessionAttr(bpc.request, "groupRoleFieldDto", groupRoleFieldDto);
            ParamUtil.setSessionAttr(bpc.request, "nonAvaUserName", (Serializable) inspectorOption);
        } else {
            OrgUserDto oDto = inspSupAddAvailabilityService.getOrgUserDtoById(loginContext.getUserId());
            String loginId = oDto.getUserId();
            Set<String> wrkGrpIds = loginContext.getWrkGrpIds();
            List<String> wrkGrpIdList = new ArrayList<>(wrkGrpIds);
            List<String> apptUserSysCorrIds = inspSupAddAvailabilityService.getApptUserSysCorrIdByLoginId(loginId, wrkGrpIdList);
            apptNonAvailabilityDateDto.setUserSysCorrIds(apptUserSysCorrIds);
            ParamUtil.setSessionAttr(bpc.request, "curRole", "member");
            ParamUtil.setSessionAttr(bpc.request, "userName", oDto.getUserId());
        }
        List<SelectOption> recurrenceOption = inspSupAddAvailabilityService.getRecurrenceOption();
        ParamUtil.setSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER, loginContext);
        ParamUtil.setSessionAttr(bpc.request, "recurrenceOption", (Serializable) recurrenceOption);
        ParamUtil.setSessionAttr(bpc.request, "inspNonAvailabilityDto", apptNonAvailabilityDateDto);
    }

    /**
     * StartStep: inspSupAddAvailabilityVali
     *
     * @param bpc
     * @throws
     */
    public void inspSupAddAvailabilityVali(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspSupAddAvailabilityVali start ...."));
        ApptNonAvailabilityDateDto apptNonAvailabilityDateDto = (ApptNonAvailabilityDateDto) ParamUtil.getSessionAttr(bpc.request, "inspNonAvailabilityDto");
        String nonActionValue = ParamUtil.getRequestString(bpc.request, "nonActionValue");
        GroupRoleFieldDto groupRoleFieldDto = (GroupRoleFieldDto)ParamUtil.getSessionAttr(bpc.request, "groupRoleFieldDto");
        if(!(InspectionConstants.SWITCH_ACTION_BACK.equals(nonActionValue))){
            String curRole = (String)ParamUtil.getSessionAttr(bpc.request, "curRole");
            String nonAvaStart = ParamUtil.getRequestString(bpc.request, "nonAvaStartDate");
            String nonAvaEnd = ParamUtil.getRequestString(bpc.request, "nonAvaEndDate");
            String nonDescription = ParamUtil.getRequestString(bpc.request, "blockOutDesc");
            String recurrence = ParamUtil.getRequestString(bpc.request, "recurrence");
            Date nonAvaStartDate = nonAvaStringToDate(nonAvaStart);
            Date nonAvaEndDate = nonAvaStringToDate(nonAvaEnd);
            apptNonAvailabilityDateDto.setBlockOutStart(nonAvaStartDate);
            apptNonAvailabilityDateDto.setBlockOutEnd(nonAvaEndDate);
            apptNonAvailabilityDateDto.setRecurrence(recurrence);
            apptNonAvailabilityDateDto.setNonAvaDescription(nonDescription);
            ValidationResult validationResult;
            if("lead".equals(curRole)){
                String userLoginId = ParamUtil.getRequestString(bpc.request, "nonAvaUserNameId");
                apptNonAvailabilityDateDto.setCheckUserName(userLoginId);
                apptNonAvailabilityDateDto = inspSupAddAvailabilityService.setUserSysCorrIdsByDto(apptNonAvailabilityDateDto, groupRoleFieldDto);
                validationResult = WebValidationHelper.validateProperty(apptNonAvailabilityDateDto,"lead");
            } else {
                validationResult = WebValidationHelper.validateProperty(apptNonAvailabilityDateDto,"inspector");
            }
            if (validationResult.isHasErrors()) {
                Map<String, String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
            } else {
                ParamUtil.setRequestAttr(bpc.request,"flag", AppConsts.TRUE);
                ParamUtil.setRequestAttr(bpc.request,"lastActionValue", InspectionConstants.SWITCH_ACTION_ADD);
            }
        }else{
            ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
        }

        ParamUtil.setSessionAttr(bpc.request, "inspNonAvailabilityDto", apptNonAvailabilityDateDto);
    }

    private Date nonAvaStringToDate(String stringDate) {
        if(StringUtil.isEmpty(stringDate)){
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT);
        Date date1 = null;
        try {
            date1 = sdf.parse(stringDate);
        } catch (ParseException e) {
            log.debug(StringUtil.changeForLog("date error ...."), e);
        }
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        String date2 = sdf2.format(date1);
        Date date3 = null;
        try {
            date3 = sdf2.parse(date2);
        } catch (ParseException e) {
            log.debug(StringUtil.changeForLog("date error ...."), e);
        }
        return date3;
    }

    /**
     * StartStep: inspSupAddAvailabilityConfirm
     *
     * @param bpc
     * @throws
     */
    public void inspSupAddAvailabilityConfirm(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspSupAddAvailabilityConfirm start ...."));
        ApptNonAvailabilityDateDto apptNonAvailabilityDateDto = (ApptNonAvailabilityDateDto) ParamUtil.getSessionAttr(bpc.request, "inspNonAvailabilityDto");
        String containDate = inspSupAddAvailabilityService.dateIsContainNonWork(apptNonAvailabilityDateDto);
        ParamUtil.setSessionAttr(bpc.request, "inspNonAvailabilityDto", apptNonAvailabilityDateDto);
        ParamUtil.setSessionAttr(bpc.request, "containDate", containDate);
    }

    /**
     * StartStep: inspSupAddAvailabilityStep2
     *
     * @param bpc
     * @throws
     */
    public void inspSupAddAvailabilityStep2(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspSupAddAvailabilityStep2 start ...."));
        String actionValue = (String)ParamUtil.getSessionAttr(bpc.request, "actionValue");
        GroupRoleFieldDto groupRoleFieldDto = (GroupRoleFieldDto)ParamUtil.getSessionAttr(bpc.request, "groupRoleFieldDto");
        ApptNonAvailabilityDateDto apptNonAvailabilityDateDto = (ApptNonAvailabilityDateDto) ParamUtil.getSessionAttr(bpc.request, "inspNonAvailabilityDto");
        String nonActionValue = ParamUtil.getRequestString(bpc.request, "nonActionValue");
        //The 'back' is go to search page, and step can do save or update
        if(InspectionConstants.SWITCH_ACTION_BACK.equals(nonActionValue)) {
            if (InspectionConstants.SWITCH_ACTION_ADD.equals(actionValue)) {
                inspSupAddAvailabilityService.createNonAvailability(apptNonAvailabilityDateDto, groupRoleFieldDto);
            } else if (InspectionConstants.SWITCH_ACTION_EDIT.equals(actionValue)) {
                inspSupAddAvailabilityService.updateNonAvailability(apptNonAvailabilityDateDto);
            }
        }
        ParamUtil.setSessionAttr(bpc.request, "inspNonAvailabilityDto", apptNonAvailabilityDateDto);
    }

    /**
     * StartStep: inspSupAddAvailabilityDelete
     *
     * @param bpc
     * @throws
     */
    public void inspSupAddAvailabilityDelete(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspSupAddAvailabilityDelete start ...."));
        ApptNonAvailabilityDateDto apptNonAvailabilityDateDto = (ApptNonAvailabilityDateDto) ParamUtil.getSessionAttr(bpc.request, "inspNonAvailabilityDto");
        String apptRefNo = apptNonAvailabilityDateDto.getRefNo();
        inspSupAddAvailabilityService.deleteNonAvailabilityByApptRefNo(apptRefNo);
    }

    /**
     * StartStep: inspSupAddAvailabilityEdit
     *
     * @param bpc
     * @throws
     */
    public void inspSupAddAvailabilityEdit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspSupAddAvailabilityEdit start ...."));
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        ApptNonAvailabilityDateDto apptNonAvailabilityDateDto = (ApptNonAvailabilityDateDto) ParamUtil.getSessionAttr(bpc.request, "inspNonAvailabilityDto");
        String userSysCorrId = apptNonAvailabilityDateDto.getUserCorrId();
        ApptAgencyUserDto apptAgencyUserDto = inspSupAddAvailabilityService.getAgencyUserByUserSysCorrId(userSysCorrId);
        String userName = "";
        if(apptAgencyUserDto != null){
            userName = apptAgencyUserDto.getLoginUserId();
            String useAgencyId = apptAgencyUserDto.getId();
            List<String> userSysCorrIds = inspSupAddAvailabilityService.getUserSysCorrIdByAgencyId(useAgencyId);
            apptNonAvailabilityDateDto.setUserSysCorrIds(userSysCorrIds);
        }
        List<SelectOption> recurrenceOption = inspSupAddAvailabilityService.getRecurrenceOption();
        ParamUtil.setSessionAttr(bpc.request, "userName", userName);
        ParamUtil.setSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER, loginContext);
        ParamUtil.setSessionAttr(bpc.request, "recurrenceOption", (Serializable) recurrenceOption);
        ParamUtil.setSessionAttr(bpc.request, "inspNonAvailabilityDto", apptNonAvailabilityDateDto);
    }

    /**
     * StartStep: inspSupAddAvailabilityEditVali
     *
     * @param bpc
     * @throws
     */
    public void inspSupAddAvailabilityEditVali(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspSupAddAvailabilityEditVali start ...."));
        ApptNonAvailabilityDateDto apptNonAvailabilityDateDto = (ApptNonAvailabilityDateDto) ParamUtil.getSessionAttr(bpc.request, "inspNonAvailabilityDto");
        String nonActionValue = ParamUtil.getRequestString(bpc.request, "nonActionValue");
        if(!(InspectionConstants.SWITCH_ACTION_BACK.equals(nonActionValue))){
            String nonAvaStart = ParamUtil.getRequestString(bpc.request, "nonAvaStartDate");
            String nonAvaEnd = ParamUtil.getRequestString(bpc.request, "nonAvaEndDate");
            String nonDescription = ParamUtil.getRequestString(bpc.request, "blockOutDesc");
            String recurrence = ParamUtil.getRequestString(bpc.request, "recurrence");
            Date nonAvaStartDate = nonAvaStringToDate(nonAvaStart);
            Date nonAvaEndDate = nonAvaStringToDate(nonAvaEnd);
            apptNonAvailabilityDateDto.setBlockOutStart(nonAvaStartDate);
            apptNonAvailabilityDateDto.setBlockOutEnd(nonAvaEndDate);
            apptNonAvailabilityDateDto.setRecurrence(recurrence);
            apptNonAvailabilityDateDto.setNonAvaDescription(nonDescription);
            ValidationResult validationResult = WebValidationHelper.validateProperty(apptNonAvailabilityDateDto,"inspector");
            if (validationResult.isHasErrors()) {
                Map<String, String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
            } else {
                ParamUtil.setRequestAttr(bpc.request,"flag", AppConsts.TRUE);
                ParamUtil.setRequestAttr(bpc.request,"lastActionValue", InspectionConstants.SWITCH_ACTION_EDIT);
            }
        }else{
            ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
        }

        ParamUtil.setSessionAttr(bpc.request, "inspNonAvailabilityDto", apptNonAvailabilityDateDto);
    }
}
