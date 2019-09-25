package com.ecquaria.cloud.moh.iais.controller;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.SystemParameterConstant;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.dto.SystemParameterDto;
import com.ecquaria.cloud.moh.iais.dto.SystemParameterQueryDto;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.service.SystemParameterService;
import com.ecquaria.cloud.moh.iais.tags.SelectOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    @author yichen_guo@ecquaria.com

 */

@Delegator(value = "systemParameterDelegator")
@Slf4j
public class SystemParameterDelegator {

    private final FilterParameter filterParameter;
    private final SystemParameterService parameterService;

    @Autowired
    public SystemParameterDelegator(FilterParameter filterParameter, SystemParameterService parameterService){
        this.filterParameter = filterParameter;
        this.parameterService = parameterService;
    }

    /**
     * setup option to web page
     * @param request
     */
    private void preSelectOption(HttpServletRequest request){
        List<SelectOption> domainOptionList = new ArrayList<>();
        domainOptionList.add(new SelectOption("Common", "Common"));
        domainOptionList.add(new SelectOption("Process", "Process"));
        ParamUtil.setRequestAttr(request, "domainTypeSelect", domainOptionList);

        List<SelectOption> moduleList =  new ArrayList<>();
        moduleList.add(new SelectOption("General", "General"));
        moduleList.add(new SelectOption("New", "New"));
        moduleList.add(new SelectOption("Renewal", "Renewal"));
        moduleList.add(new SelectOption("RequestforChange", "Request for Change"));
        moduleList.add(new SelectOption("Suspension", "Suspension"));
        moduleList.add(new SelectOption("OnlineAppointment", "Online Appointment"));
        ParamUtil.setRequestAttr(request, "moduleTypeSelect", moduleList);

        List<SelectOption> statusList =  new ArrayList<>();
        statusList.add(new SelectOption("Y", "Active"));
        statusList.add(new SelectOption("N", "Inactive"));
        ParamUtil.setRequestAttr(request, "statusTypeSelect", statusList);
    }

    /**
     * AutoStep: PrepareSwitch
     *
     * @param bpc
     * @throws
     */
    public void prepareSwitch(BaseProcessClass bpc) {
        log.debug("The prepareSwitch start ...");
        String crudAction = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        log.debug("*******************crudAction-->:" + crudAction);
        log.debug("The prepareSwitch end ...");
    }

    /**
     * AutoStep: Step1
     * @param bpc
     */
    public void startStep(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction("System Parameter", "This module provides a form of search and update functions for System Administrator to maintain the set of system parameters use by entire system");
        HttpServletRequest request = bpc.request;
        IaisEGPHelper.clearSessionAttr(request, SystemParameterConstant.class);
    }

    /**
     * AutoStep: loadData
     * @param bpc
     */
    public void loadData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        preSelectOption(request);

        filterParameter.setClz(SystemParameterQueryDto.class);
        filterParameter.setSearchAttr(SystemParameterConstant.PARAM_SEARCH);
        filterParameter.setResultAttr(SystemParameterConstant.PARAM_SEARCHRESULT);
        filterParameter.setSortField("pid");

        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        QueryHelp.setMainSql("systemAdmin", "querySystemParam", searchParam);

        SearchResult searchResult = parameterService.doQuery(searchParam);
        ParamUtil.setSessionAttr(request, SystemParameterConstant.PARAM_SEARCH, searchParam);
        ParamUtil.setRequestAttr(request, SystemParameterConstant.PARAM_SEARCHRESULT, searchResult);
    }


    /**
     * AutoStep: doSearch
     * @param bpc
     */
    public void doQuery(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(!"doQuery".equals(currentAction)){
            return;
        }

        SystemParameterQueryDto queryDto = new SystemParameterQueryDto();

        String domainType = ParamUtil.getString(request, SystemParameterConstant.PARAM_DOMAIN_TYPE);
        String module = ParamUtil.getString(request, SystemParameterConstant.PARAM_MODULE);
        String status = ParamUtil.getString(request, SystemParameterConstant.PARAM_STATUS);
        String description = ParamUtil.getString(request, SystemParameterConstant.PARAM_DESCRIPTION);

        queryDto.setDomainType(domainType);
        queryDto.setModule(module);
        queryDto.setStatus(status != null ? status.charAt(0) : null);
        queryDto.setDescription(description);
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, true, filterParameter);

        ValidationResult validationResult = WebValidationHelper.validateProperty(queryDto, "search");
        if(validationResult != null && validationResult.isHasErrors()) {
            Map<String, String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMAP, errorMap);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, "N");
        }else {
            searchParam.addFilter(SystemParameterConstant.PARAM_DOMAIN_TYPE, domainType, true);
            if(!StringUtil.isEmpty(description)){
                searchParam.addFilter(SystemParameterConstant.PARAM_DESCRIPTION, description, true);
            }

            if(!StringUtil.isEmpty(module)){
                searchParam.addFilter(SystemParameterConstant.PARAM_MODULE, module, true);
            }
            if(!StringUtil.isEmpty(status)){
                searchParam.addFilter(SystemParameterConstant.PARAM_STATUS, status, true);
            }
        }
    }

    /**
     * AutoStep: doEdit
     * user do edit with message management
     * @param bpc
     */
    public void doEdit(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if("cancel".equals(currentAction)){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"Y");
            return;
        }else if(!"doEdit".equals(currentAction)){
            return;
        }

        String domainType = ParamUtil.getString(request, SystemParameterConstant.PARAM_DOMAIN_TYPE);
        String module = ParamUtil.getString(request, SystemParameterConstant.PARAM_MODULE);
        String value = ParamUtil.getString(request, SystemParameterConstant.PARAM_VALUE);
        String description = ParamUtil.getString(request, SystemParameterConstant.PARAM_DESCRIPTION);

        SystemParameterDto editDto = (SystemParameterDto) ParamUtil.getSessionAttr(request, SystemParameterConstant.PARAMETER_REQUEST_DTO);
        editDto.setDomainType(domainType);
        editDto.setDomainType(domainType);
        editDto.setModule(module);
        editDto.setValue(value);
        editDto.setDescription(description);

        ValidationResult validationResult = WebValidationHelper.validateProperty(editDto, "edit");
        if(validationResult != null && validationResult.isHasErrors()){
            Map<String,String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMAP,errorMap);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"N");
        }else {
            Map<String,String> successMap = new HashMap<>();
            successMap.put("edit parameter","suceess");
            parameterService.saveSystemParameter(editDto);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"Y");
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMAP,successMap);
        }

    }

    /**
     * AutoStep: disableStatus
     * @param bpc
     */
    public void disableStatus(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String rowguid = ParamUtil.getString(request,IaisEGPConstant.CRUD_ACTION_VALUE);
        if(!StringUtil.isEmpty(rowguid)) {
            SystemParameterDto dto = parameterService.getParameterByRowguid(rowguid);
            dto.setStatus(SystemParameterConstant.STATUS_DEACTIVATED);
            parameterService.saveSystemParameter(dto);
        }
    }

    /**
     * AutoStep: prepareEdit
     * @param bpc
     */
    public void prepareEdit(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String rowguid = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_VALUE);
        preSelectOption(request);
        if(!StringUtil.isEmpty(rowguid)){
            SystemParameterDto dto = parameterService.getParameterByRowguid(rowguid);
            ParamUtil.setSessionAttr(request, SystemParameterConstant.PARAMETER_REQUEST_DTO, dto);
        }
    }

    /**
     * AutoStep: changePage
     * @param bpc
     */
    public void changePage(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        CrudHelper.doPaging(searchParam,bpc.request);
    }

    /**
     * AutoStep: sortRecords
     * @param bpc
     */
    public void sortRecords(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        CrudHelper.doSorting(searchParam,  request);
    }
}
