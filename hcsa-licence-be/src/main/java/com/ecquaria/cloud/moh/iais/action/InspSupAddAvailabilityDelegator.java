package com.ecquaria.cloud.moh.iais.action;

/**
 * @Process: MohInspSupAddAvailability
 *
 * @author Shicheng
 * @date 2019/11/14 18:01
 **/

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptNonAvailabilityDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.InspSupAddAvailabilityService;
import com.ecquaria.cloud.moh.iais.service.InspectionService;
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

@Delegator("inspSupAddAvailabilityDelegator")
@Slf4j
public class InspSupAddAvailabilityDelegator {

    @Autowired
    private InspSupAddAvailabilityService inspSupAddAvailabilityService;

    @Autowired
    private InspectionService inspectionService;

    @Autowired
    private InspSupAddAvailabilityDelegator(InspectionService inspectionService, InspSupAddAvailabilityService inspSupAddAvailabilityService){
        this.inspSupAddAvailabilityService = inspSupAddAvailabilityService;
        this.inspectionService = inspectionService;
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
        AccessUtil.initLoginUserInfo(bpc.request);
        ParamUtil.setSessionAttr(bpc.request, "curRole", null);
        ParamUtil.setSessionAttr(bpc.request, "inspSupAddAvailabilityType", null);
        ParamUtil.setSessionAttr(bpc.request, "inspNonAvailabilityDto", null);
        ParamUtil.setSessionAttr(bpc.request, "nonAvaUserName", null);
    }

    /**
     * StartStep: inspSupAddAvailabilityPre
     *
     * @param bpc
     * @throws
     */
    public void inspSupAddAvailabilityPre(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspSupAddAvailabilityPre start ...."));
        String actionValue = (String)ParamUtil.getRequestAttr(bpc.request, "actionValue");
        if(InspectionConstants.SWITCH_ACTION_ADD.equals(actionValue)) {
            ApptNonAvailabilityDateDto apptNonAvailabilityDateDto = new ApptNonAvailabilityDateDto();
            ParamUtil.setSessionAttr(bpc.request, "inspSupAddAvailabilityType", actionValue);
            ParamUtil.setSessionAttr(bpc.request, "inspNonAvailabilityDto", apptNonAvailabilityDateDto);

        } else if(InspectionConstants.SWITCH_ACTION_EDIT.equals(actionValue)) {
            String nonAvaId = ParamUtil.getRequestString(bpc.request, "nonAvaId");
            ApptNonAvailabilityDateDto apptNonAvailabilityDateDto = inspSupAddAvailabilityService.getApptNonAvailabilityDateDtoById(nonAvaId);
            ParamUtil.setSessionAttr(bpc.request, "inspSupAddAvailabilityType", actionValue);
            ParamUtil.setSessionAttr(bpc.request, "inspNonAvailabilityDto", apptNonAvailabilityDateDto);

        } else if(InspectionConstants.SWITCH_ACTION_DELETE.equals(actionValue)) {
            String nonAvaId = ParamUtil.getRequestString(bpc.request, "nonAvaId");
            ParamUtil.setSessionAttr(bpc.request, "inspSupAddAvailabilityType", actionValue);
            ParamUtil.setRequestAttr(bpc.request, "removeId", nonAvaId);
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
        List<String> roleList = new ArrayList<>(loginContext.getRoleIds());
        if(roleList.contains(RoleConsts.USER_ROLE_INSPECTION_LEAD)){
            List<String> workGroupIds = inspectionService.getWorkGroupIdsByLogin(loginContext);
            List<SelectOption> inspectorOption = inspectionService.getInspectorOptionByLogin(loginContext, workGroupIds);
            ParamUtil.setSessionAttr(bpc.request, "curRole", RoleConsts.USER_ROLE_INSPECTION_LEAD);
            ParamUtil.setSessionAttr(bpc.request, "nonAvaUserName", (Serializable) inspectorOption);
        } else {
            ParamUtil.setSessionAttr(bpc.request, "curRole", RoleConsts.USER_ROLE_INSPECTIOR);
            OrgUserDto oDto = inspSupAddAvailabilityService.getOrgUserDtoById(loginContext.getUserId());
            apptNonAvailabilityDateDto.setUserCorrId(loginContext.getUserId());
            ParamUtil.setSessionAttr(bpc.request, "userName", oDto.getDisplayName());
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
        if(!(InspectionConstants.SWITCH_ACTION_BACK.equals(nonActionValue))){
            LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
            List<String> roleList = new ArrayList<>(loginContext.getRoleIds());
            String nonAvaStart = ParamUtil.getRequestString(bpc.request, "nonAvaStartDate");
            String nonAvaEnd = ParamUtil.getRequestString(bpc.request, "nonAvaEndDate");
            String nonDescription = ParamUtil.getRequestString(bpc.request, "blockOutDesc");
            String recurrence = ParamUtil.getRequestString(bpc.request, "recurrence");
            Date nonAvaStartDate = nonAvaStringToDate(nonAvaStart);
            Date nonAvaEndDate = nonAvaStringToDate(nonAvaEnd);
            apptNonAvailabilityDateDto.setBlockOutStart(nonAvaStartDate);
            apptNonAvailabilityDateDto.setBlockOutEnd(nonAvaEndDate);
            apptNonAvailabilityDateDto.setId(null);
            apptNonAvailabilityDateDto.setRecurrence(recurrence);
            apptNonAvailabilityDateDto.setNonAvaDescription(nonDescription);
            apptNonAvailabilityDateDto.setNonAvaStatus(AppConsts.COMMON_STATUS_ACTIVE);
            apptNonAvailabilityDateDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            ValidationResult validationResult;
            if(roleList.contains(RoleConsts.USER_ROLE_INSPECTION_LEAD)){
                String userId = ParamUtil.getRequestString(bpc.request, "nonAvaUserNameId");
                apptNonAvailabilityDateDto.setUserCorrId(userId);
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
            }
        }else{
            ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
        }

        ParamUtil.setSessionAttr(bpc.request, "inspNonAvailabilityDto", apptNonAvailabilityDateDto);
    }

    private Date nonAvaStringToDate(String stringDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = null;
        try {
            date1 = sdf.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        String date2 = sdf2.format(date1);
        Date date3 = null;
        try {
            date3 = sdf2.parse(date2);
        } catch (ParseException e) {
            e.printStackTrace();
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
        String actionValue = (String)ParamUtil.getSessionAttr(bpc.request, "inspSupAddAvailabilityType");
        ApptNonAvailabilityDateDto apptNonAvailabilityDateDto = (ApptNonAvailabilityDateDto) ParamUtil.getSessionAttr(bpc.request, "inspNonAvailabilityDto");
        String nonActionValue = ParamUtil.getRequestString(bpc.request, "nonActionValue");
        if(InspectionConstants.SWITCH_ACTION_SUBMIT.equals(nonActionValue)) {
            if (InspectionConstants.SWITCH_ACTION_ADD.equals(actionValue)) {
                inspSupAddAvailabilityService.createNonAvailability(apptNonAvailabilityDateDto);
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
        String removeId = (String) ParamUtil.getRequestAttr(bpc.request, "removeId");
        inspSupAddAvailabilityService.deleteNonAvailabilityById(removeId);
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
        List<String> roleList = new ArrayList<>(loginContext.getRoleIds());
        if(roleList.contains(RoleConsts.USER_ROLE_INSPECTION_LEAD)){
            List<String> workGroupIds = inspectionService.getWorkGroupIdsByLogin(loginContext);
            List<SelectOption> inspectorOption = inspectionService.getInspectorOptionByLogin(loginContext, workGroupIds);
            ParamUtil.setSessionAttr(bpc.request, "curRole", RoleConsts.USER_ROLE_INSPECTION_LEAD);
            ParamUtil.setSessionAttr(bpc.request, "nonAvaUserName", (Serializable) inspectorOption);
        } else {
            ParamUtil.setSessionAttr(bpc.request, "curRole", RoleConsts.USER_ROLE_INSPECTIOR);
            OrgUserDto oDto = inspSupAddAvailabilityService.getOrgUserDtoById(loginContext.getUserId());
            ParamUtil.setSessionAttr(bpc.request, "userName", oDto.getDisplayName());
        }
        List<SelectOption> recurrenceOption = inspSupAddAvailabilityService.getRecurrenceOption();
        ParamUtil.setSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER, loginContext);
        ParamUtil.setSessionAttr(bpc.request, "recurrenceOption", (Serializable) recurrenceOption);
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
            LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
            List<String> roleList = new ArrayList<>(loginContext.getRoleIds());
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
            apptNonAvailabilityDateDto.setNonAvaStatus(AppConsts.COMMON_STATUS_ACTIVE);
            apptNonAvailabilityDateDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            ValidationResult validationResult;
            if(roleList.contains(RoleConsts.USER_ROLE_INSPECTION_LEAD)){
                String userId = ParamUtil.getRequestString(bpc.request, "nonAvaUserNameId");
                apptNonAvailabilityDateDto.setUserCorrId(userId);
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
            }
        }else{
            ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
        }

        ParamUtil.setSessionAttr(bpc.request, "inspNonAvailabilityDto", apptNonAvailabilityDateDto);
    }
}
