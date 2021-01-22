package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditSystemPotentialDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditTaskDataFillterDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceBeConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.AuditAssginListValidateDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AuditSystemListService;
import com.ecquaria.cloud.moh.iais.service.AuditSystemPotitalListService;
import com.ecquaria.cloud.moh.iais.util.LicenceUtil;
import com.ecquaria.cloud.moh.iais.validation.AuditAssginListValidate;
import com.ecquaria.cloud.moh.iais.validation.AuditCancelTaskValidate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author: jiahao
 * @Date: 2020/2/20 15:08
 */
@Slf4j
@Delegator("auditTcuListDelegator")
public class AuditTcuListDelegator {
    @Autowired
    AuditSystemListService auditSystemListService;
    @Autowired
    AuditSystemPotitalListService auditSystemPotitalListService;
    private String SUBMIT_MESSAGE_SUCCESS = "submit_message_success";
    private String MAIN_URL = "mainUrl";
    private String SESSION_AUDIT_SYSTEM_POTENTIAL_DTO_FOR_SEARCH_NAME = "auditSystemPotentialDtoForSearch";

    public void start(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_AUDIT_INSPECTION, AuditTrailConsts.FUNCTION_TCU_AUDIT_LIST);
        ParamUtil.setSessionAttr(request, HcsaLicenceBeConstant.SESSION_ROLEIDS_FOR_AUDIT,null);
        ParamUtil.setSessionAttr(request, HcsaLicenceBeConstant.SESSION_ROLEIDS_FOR_AUDIT_SELECT, null);
    }

    public void init(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        AuditSystemPotentialDto dto = auditSystemPotitalListService.initDtoForSearch();
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<SelectOption> roleIds = auditSystemListService.getCanViewAuditRoles(loginContext.getRoleIds());
        if(!IaisCommonUtils.isEmpty(roleIds)){
            ParamUtil.setSessionAttr(request, HcsaLicenceBeConstant.SESSION_ROLEIDS_FOR_AUDIT, (Serializable) roleIds);
            String selectRole =  roleIds.get(0).getValue();
            ParamUtil.setSessionAttr(request, HcsaLicenceBeConstant.SESSION_ROLEIDS_FOR_AUDIT_SELECT,selectRole);
            dto.setSelectRole(selectRole);
        }
        ParamUtil.setSessionAttr(request, HcsaLicenceBeConstant.SEARCH_PRAM_FOR_AUDIT_LIST,null);
        ParamUtil.setSessionAttr(request, HcsaLicenceBeConstant.SEARCH_PRAM_FOR_AUDIT_LIST_TRUE_RESULT,null);
        ParamUtil.setSessionAttr(request, SESSION_AUDIT_SYSTEM_POTENTIAL_DTO_FOR_SEARCH_NAME, null);
        ParamUtil.setSessionAttr(request,"ISTUC",Boolean.TRUE);
        List<SelectOption> aduitTypeOp = auditSystemListService.getAuditOp();
        ParamUtil.setSessionAttr(request, "aduitTypeOp", (Serializable) aduitTypeOp);
        getData(dto,request);
        ParamUtil.setSessionAttr(request, "modulename", AuditTrailConsts.FUNCTION_TCU_AUDIT_LIST);
    }

    private void getData( AuditSystemPotentialDto dto,HttpServletRequest request){
        List<AuditTaskDataFillterDto> auditTaskDataDtos = auditSystemPotitalListService.getSystemPotentailAdultListForAuditTcu(dto);
        auditTaskDataDtos = auditSystemListService.getInspectors(auditTaskDataDtos);
        ParamUtil.setSessionAttr(request, SESSION_AUDIT_SYSTEM_POTENTIAL_DTO_FOR_SEARCH_NAME, dto);
        ParamUtil.setSessionAttr(request, HcsaLicenceBeConstant.SEARCH_PRAM_FOR_AUDIT_LIST_RESULT, (Serializable) auditTaskDataDtos);
        ParamUtil.setSessionAttr(request, HcsaLicenceBeConstant.SEARCH_PRAM_FOR_AUDIT_LIST,dto.getSearchParam());
        ParamUtil.setSessionAttr(request, HcsaLicenceBeConstant.SEARCH_PRAM_FOR_AUDIT_LIST_TRUE_RESULT,dto.getSearchResult());
        if(!IaisCommonUtils.isEmpty(auditTaskDataDtos)) {
            for(AuditTaskDataFillterDto auditTaskDataFillterDto : auditTaskDataDtos ){
                ParamUtil.setSessionAttr(request, "inspectors"+auditTaskDataFillterDto.getWorkGroupId(), (Serializable) auditTaskDataFillterDto.getInspectors());
            }
        }
    }

    public void searchRole(BaseProcessClass bpc){
        log.info("------searchRole------------");
        HttpServletRequest request = bpc.request;
        AuditSystemPotentialDto dto = auditSystemPotitalListService.initDtoForSearch();
        String selectRole = ParamUtil.getString(request,HcsaLicenceBeConstant.SESSION_ROLEIDS_FOR_AUDIT_SELECT);
        List<SelectOption> roles = ( List<SelectOption>) ParamUtil.getSessionAttr(request, HcsaLicenceBeConstant.SESSION_ROLEIDS_FOR_AUDIT);
        if(!auditSystemListService.rightControlForRole(roles,selectRole)){
            log.info(StringUtil.changeForLog("--Illegal Role ID :" + selectRole+"---------"));
            dto.setSelectRole(roles.get(0).getValue());
        }else {
            dto.setSelectRole(selectRole);
            ParamUtil.setSessionAttr(request, HcsaLicenceBeConstant.SESSION_ROLEIDS_FOR_AUDIT_SELECT,selectRole);
        }
        ParamUtil.setSessionAttr(request, SESSION_AUDIT_SYSTEM_POTENTIAL_DTO_FOR_SEARCH_NAME, dto);
        getData(dto,request);
        log.info("------end searchRole------------");
    }


    public void doPage(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doPage start ...."));
        HttpServletRequest request = bpc.request;
        String pageNo = ParamUtil.getString(request, "pageJumpNoTextchangePage");
        String pageSize = ParamUtil.getString(request, "pageJumpNoPageSize");
        AuditSystemPotentialDto dto = ( AuditSystemPotentialDto)   ParamUtil.getSessionAttr(request, SESSION_AUDIT_SYSTEM_POTENTIAL_DTO_FOR_SEARCH_NAME);
        if (!StringUtil.isEmpty(pageNo)) {
            dto.setPageNo(Integer.parseInt(pageNo));
        }
        if (!StringUtil.isEmpty(pageSize)) {
            dto.setPageSize(Integer.parseInt(pageSize));
        }
        ParamUtil.setSessionAttr(request, SESSION_AUDIT_SYSTEM_POTENTIAL_DTO_FOR_SEARCH_NAME,dto);
        getData(dto,request);
    }

    public void pre(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
    }

    public void preconfirm(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        getListData(request);
        AuditAssginListValidate auditAssginListValidate = new AuditAssginListValidate();
        Map<String, String> errMap = auditAssginListValidate.validate(request);
        if (errMap.isEmpty()) {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setSessionAttr(request, "actionCancel", "back");
            ParamUtil.setSessionAttr(request, "actionConfirm", "confirm");
        } else {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
        }
    }

    public AuditAssginListValidateDto getValueFromPage(HttpServletRequest request) {
        AuditAssginListValidateDto dto = new AuditAssginListValidateDto();
        getListData(request);
        return dto;
    }

    public void confirm(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the confirm start ...."));
        HttpServletRequest request = bpc.request;
        List<AuditTaskDataFillterDto> auditTaskDataDtos = (List<AuditTaskDataFillterDto>) ParamUtil.getSessionAttr(request, HcsaLicenceBeConstant.SEARCH_PRAM_FOR_AUDIT_LIST_RESULT);
        auditSystemListService.doSubmit(auditTaskDataDtos);
        ParamUtil.setRequestAttr(request, SUBMIT_MESSAGE_SUCCESS,MessageUtil.getMessageDesc("AUDIT_ACK001"));
        ParamUtil.setRequestAttr(request, MAIN_URL, "MohAduitTcuList");
    }

    public void cancelTask(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        getListData(request);
        AuditAssginListValidate auditAssginListValidate = new AuditAssginListValidate();
        Map<String, String> errMap = auditAssginListValidate.validate(request);
        if (errMap.isEmpty()) {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setSessionAttr(request, "actionCancel", "back");
            ParamUtil.setSessionAttr(request, "actionCancelAudit", "cancel");
        } else {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
        }
    }

    public void cancel(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        List<AuditTaskDataFillterDto> auditTaskDataDtos = (List<AuditTaskDataFillterDto>) ParamUtil.getSessionAttr(request, HcsaLicenceBeConstant.SEARCH_PRAM_FOR_AUDIT_LIST_RESULT);
        AuditCancelTaskValidate auditCancelTaskValidate = new AuditCancelTaskValidate();
        Map<String, String> errorMap = auditCancelTaskValidate.validate(request);
        if (errorMap != null && errorMap.size() > 0) {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
        } else {
            ParamUtil.setRequestAttr(request, SUBMIT_MESSAGE_SUCCESS, MessageUtil.getMessageDesc("AUDIT_ACK002"));
            ParamUtil.setRequestAttr(request, MAIN_URL, "MohAduitTcuList");
            AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_AUDIT_INSPECTION, AuditTrailConsts.FUNCTION_CANCEL_AUDIT_INSP);
            // save cancel task
             auditSystemListService.doCancel(auditTaskDataDtos);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);

        }
    }

    private void getListData(HttpServletRequest request) {
        List<AuditTaskDataFillterDto> auditTaskDataDtos = (List<AuditTaskDataFillterDto>) ParamUtil.getSessionAttr(request, HcsaLicenceBeConstant.SEARCH_PRAM_FOR_AUDIT_LIST_RESULT);
        if (!IaisCommonUtils.isEmpty(auditTaskDataDtos)) {
            for (int i = 0; i < auditTaskDataDtos.size(); i++) {
                String auditType = ParamUtil.getString(request, i + "auditType");
                String inspectorId;
                try {
                    inspectorId = ParamUtil.getMaskedString(request, i + "insOp");
                }catch (Exception e){
                    inspectorId = ParamUtil.getRequestString(request, i + "insOp");
                }
                String inspectorName =  LicenceUtil.getSelectOptionTextFromSelectOptions(auditTaskDataDtos.get(i).getInspectors(),inspectorId);
                String forad = ParamUtil.getString(request, i + "selectForAd");
                String number = ParamUtil.getString(request, i + "number");
                if(!auditTaskDataDtos.get(i).isAudited()) {
                    auditTaskDataDtos.get(i).setAuditType(auditType);
                    auditTaskDataDtos.get(i).setInspectorId(inspectorId);
                    auditTaskDataDtos.get(i).setInspector(inspectorName);
                }
                if (!StringUtil.isEmpty(forad)) {
                    auditTaskDataDtos.get(i).setSelectedForAudit(true);
                } else {
                    auditTaskDataDtos.get(i).setSelectedForAudit(false);
                }
                if (!StringUtil.isEmpty(number)) {
                    auditTaskDataDtos.get(i).setSelected(true);
                } else {
                    auditTaskDataDtos.get(i).setSelected(false);
                }
            }
        }
        ParamUtil.setSessionAttr(request, HcsaLicenceBeConstant.SEARCH_PRAM_FOR_AUDIT_LIST_RESULT, (Serializable) auditTaskDataDtos);
    }





}
