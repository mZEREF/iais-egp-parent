package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageCodeKey;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemParameterConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterDto;
import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.SystemParameterService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/*
    @author yichen_guo@ecquaria.com

 */

@Delegator(value = "systemParameterDelegator")
@Slf4j
public class SystemParameterDelegator {
    private final static String PRE_SAVE_USER_ID = "preSaveUserId";

    private final FilterParameter filterParameter = new FilterParameter.Builder()
            .clz(SystemParameterQueryDto.class)
            .searchAttr(SystemParameterConstants.PARAM_SEARCH)
            .resultAttr(SystemParameterConstants.PARAM_SEARCHRESULT)
            .sortField("pid").build();
    private final SystemParameterService parameterService;

    @Autowired
    public SystemParameterDelegator(SystemParameterService parameterService){
        this.parameterService = parameterService;
    }

    /**
     * AutoStep: PrepareSwitch
     *
     * @param bpc
     * @throws
     */
    public void prepareSwitch(BaseProcessClass bpc) {
        log.debug("The prepareSwitch start ...");
        log.debug("The prepareSwitch end ...");
    }

    /**
     * AutoStep: Step1
     * @param bpc
     */
    public void startStep(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction("System Parameter",
                "This module provides a form of search and update " +
                        "functions for System Administrator to maintain the set " +
                        "of system parameters use by entire system");
        HttpServletRequest request = bpc.request;

        ParamUtil.setSessionAttr(request, SystemParameterConstants.PARAM_SEARCH, null);
        ParamUtil.setSessionAttr(request, SystemParameterConstants.PARAM_SEARCHRESULT, null);
    }

    /**
     * AutoStep: loadData
     * @param bpc
     */
    public void loadData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

        //flush cache
        //SystemParamCacheHelper.flush();

        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        QueryHelp.setMainSql("systemAdmin", "querySystemParam", searchParam);

        SearchResult searchResult = parameterService.doQuery(searchParam);
        ParamUtil.setSessionAttr(request, SystemParameterConstants.PARAM_SEARCH, searchParam);
        ParamUtil.setSessionAttr(request, SystemParameterConstants.PARAM_SEARCHRESULT, searchResult);
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

        String domainType = ParamUtil.getString(request, SystemParameterConstants.PARAM_DOMAIN_TYPE);
        String module = ParamUtil.getString(request, SystemParameterConstants.PARAM_MODULE);
        String status = ParamUtil.getString(request, SystemParameterConstants.PARAM_STATUS);
        String description = ParamUtil.getString(request, SystemParameterConstants.PARAM_DESCRIPTION);

        queryDto.setDomainType(domainType);
        queryDto.setModule(module);
        queryDto.setStatus(status);
        queryDto.setDescription(description);
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, true, filterParameter);

        ParamUtil.setRequestAttr(request, SystemParameterConstants.PARAM_DOMAIN_TYPE, domainType);
        ParamUtil.setRequestAttr(request, SystemParameterConstants.PARAM_MODULE, module);
        ParamUtil.setRequestAttr(request, SystemParameterConstants.PARAM_STATUS, status);
        ParamUtil.setRequestAttr(request, SystemParameterConstants.PARAM_DESCRIPTION, description);

        ValidationResult validationResult = WebValidationHelper.validateProperty(queryDto, "search");
        if(validationResult != null && validationResult.isHasErrors()) {
            Map<String, String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, "N");
        }else {
            searchParam.addFilter(SystemParameterConstants.PARAM_DOMAIN_TYPE, domainType, true);
            if(!StringUtil.isEmpty(description)){
                searchParam.addFilter(SystemParameterConstants.PARAM_DESCRIPTION, description, true);
            }

            if(!StringUtil.isEmpty(module)){
                searchParam.addFilter(SystemParameterConstants.PARAM_MODULE, module, true);
            }
            if(!StringUtil.isEmpty(status)){
                searchParam.addFilter(SystemParameterConstants.PARAM_STATUS, status, true);
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
        if(!"doEdit".equals(currentAction)){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"Y");
            return;
        }

        String value = ParamUtil.getString(request, SystemParameterConstants.PARAM_VALUE);
        String description = ParamUtil.getString(request, SystemParameterConstants.PARAM_DESCRIPTION);

        SystemParameterDto editDto = (SystemParameterDto) ParamUtil.getSessionAttr(request, SystemParameterConstants.PARAMETER_REQUEST_DTO);
        AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        editDto.setAuditTrailDto(auditTrailDto);
        editDto.setValue(value);
        editDto.setDescription(description);

        ValidationResult validationResult = WebValidationHelper.validateProperty(editDto, "edit");
        if(validationResult != null && validationResult.isHasErrors()){
            Map<String,String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
        }else {
            parameterService.saveSystemParameter(editDto);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID, IaisEGPConstant.YES);

            ParamUtil.setRequestAttr(request,"ackMsg", MessageUtil.dateIntoMessage(MessageCodeKey.ACKSPM001));
            ParamUtil.setSessionAttr(request, SystemParameterConstants.PARAMETER_REQUEST_DTO, editDto);

        }

    }

    /**
     * AutoStep: back
     * @param bpc
     */
    public void back(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
    }


    /**
     * AutoStep: disableStatus
     * @param bpc
     */
    public void disableStatus(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String pid = ParamUtil.getString(request,IaisEGPConstant.CRUD_ACTION_VALUE);
        if(!StringUtil.isEmpty(pid)) {
            SystemParameterDto dto = parameterService.getParameterByPid(pid);
            dto.setStatus(AppConsts.COMMON_STATUS_DELETED);
            parameterService.saveSystemParameter(dto);
        }
    }

    /**
     * AutoStep: prepareEdit
     * @param bpc
     */
    public void prepareEdit(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, PRE_SAVE_USER_ID, null);
        String pid = ParamUtil.getString(bpc.request, IntranetUserConstant.CRUD_ACTION_VALUE);


        if (!StringUtils.isEmpty(pid)){
            SearchResult<SystemParameterQueryDto> result = (SearchResult<SystemParameterQueryDto>) ParamUtil.getSessionAttr(request, SystemParameterConstants.PARAM_SEARCHRESULT);
            if (result != null){
                List<SystemParameterQueryDto> parameterQueryDtos =  result.getRows();
                for (SystemParameterQueryDto query : parameterQueryDtos){
                    if (query.getId().equals(pid)){
                        SystemParameterDto systemParameterDto = new SystemParameterDto();
                        systemParameterDto.setId(query.getId());
                        systemParameterDto.setDomainType(query.getDomainType());
                        systemParameterDto.setModule(query.getModule());
                        systemParameterDto.setDescription(query.getDescription());
                        systemParameterDto.setUnits(query.getUnits());
                        systemParameterDto.setParamType(query.getParamType());
                        systemParameterDto.setValue(query.getValue());
                        systemParameterDto.setMandatory(query.getMandatory());
                        systemParameterDto.setUpdate(query.getUpdate());
                        systemParameterDto.setMaxlength(query.getMaxlength());
                        systemParameterDto.setStatus(query.getStatus());
                        systemParameterDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                        systemParameterDto.setModifiedAt(query.getModifiedAt());
                        systemParameterDto.setPropertiesKey(query.getPropertiesKey());

                        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
                        if (loginContext != null){
                            systemParameterDto.setModifiedByName(loginContext.getUserName());
                            systemParameterDto.setModifiedBy(loginContext.getUserId());
                        }else {
                            systemParameterDto.setModifiedByName("System");
                            systemParameterDto.setModifiedBy(AppConsts.USER_ID_SYSTEM);
                        }

                        ParamUtil.setSessionAttr(request, SystemParameterConstants.PARAMETER_REQUEST_DTO, systemParameterDto);
                    }
                }
            }
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
