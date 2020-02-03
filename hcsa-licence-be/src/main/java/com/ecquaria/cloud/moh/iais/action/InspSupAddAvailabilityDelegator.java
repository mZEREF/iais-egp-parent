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
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.InspSupAddAvailabilityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
        AccessUtil.initLoginUserInfo(bpc.request);
        ParamUtil.setSessionAttr(bpc.request, "curRole", null);
        ParamUtil.setSessionAttr(bpc.request, "inspSupAddAvailabilityType", null);
        ParamUtil.setSessionAttr(bpc.request, "inspNonAvailabilityDto", null);
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
            ParamUtil.setSessionAttr(bpc.request, "inspSupAddAvailabilityType", actionValue);

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
        List<String> roleList =new ArrayList<>(loginContext.getRoleIds());
        if(roleList.contains(RoleConsts.USER_ROLE_INSPECTION_LEAD)){
            ParamUtil.setSessionAttr(bpc.request, "curRole", RoleConsts.USER_ROLE_INSPECTION_LEAD);
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
     * StartStep: inspSupAddAvailabilityVali
     *
     * @param bpc
     * @throws
     */
    public void inspSupAddAvailabilityVali(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspSupAddAvailabilityVali start ...."));
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
        ParamUtil.setSessionAttr(bpc.request, "inspNonAvailabilityDto", apptNonAvailabilityDateDto);
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
        if(InspectionConstants.SWITCH_ACTION_ADD.equals(actionValue)) {
            inspSupAddAvailabilityService.createNonAvailability(apptNonAvailabilityDateDto);
        } else if(InspectionConstants.SWITCH_ACTION_EDIT.equals(actionValue)) {
            inspSupAddAvailabilityService.updateNonAvailability(apptNonAvailabilityDateDto);
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
        List<String> roleList =new ArrayList<>(loginContext.getRoleIds());
        if(roleList.contains(RoleConsts.USER_ROLE_INSPECTION_LEAD)){
            ParamUtil.setSessionAttr(bpc.request, "curRole", RoleConsts.USER_ROLE_INSPECTION_LEAD);
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
    }
}
