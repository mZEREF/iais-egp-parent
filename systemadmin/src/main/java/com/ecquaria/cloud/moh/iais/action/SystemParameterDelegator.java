package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranet.user.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemParameterConstant;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterDto;
import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
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
import java.util.Optional;

/*
    @author yichen_guo@ecquaria.com

 */
@Delegator(value = "systemParameterDelegator")
@Slf4j
public class SystemParameterDelegator {
    private final static String PRE_SAVE_USER_ID = "preSaveUserId";

    private final FilterParameter filterParameter = new FilterParameter.Builder()
            .clz(SystemParameterQueryDto.class)
            .searchAttr(SystemParameterConstant.PARAM_SEARCH)
            .resultAttr(SystemParameterConstant.PARAM_SEARCHRESULT)
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
        // nothing to do
    }

    /**
     * AutoStep: Step1
     * @param bpc
     */
    public void startStep(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_SYSTEM_CONFIG, AuditTrailConsts.FUNCTION_SYSTEM_PARAMETER_MANAGEMENT);
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, SystemParameterConstant.PARAM_DOMAIN_TYPE, null);
        ParamUtil.setSessionAttr(request, SystemParameterConstant.PARAM_MODULE, null);
        ParamUtil.setSessionAttr(request, SystemParameterConstant.PARAM_STATUS, null);
        ParamUtil.setSessionAttr(request, SystemParameterConstant.PARAM_DESCRIPTION, null);
        ParamUtil.setSessionAttr(request, SystemParameterConstant.PARAM_SEARCH, null);
        ParamUtil.setSessionAttr(request, SystemParameterConstant.PARAM_SEARCHRESULT, null);
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
        ParamUtil.setSessionAttr(request, SystemParameterConstant.PARAM_SEARCH, searchParam);
        ParamUtil.setSessionAttr(request, SystemParameterConstant.PARAM_SEARCHRESULT, searchResult);
    }


    /**
     * AutoStep: doSearch
     * @param bpc
     */
    public void doQuery(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String curAct = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(!"doQuery".equals(curAct)){
            return;
        }

        SystemParameterQueryDto query = new SystemParameterQueryDto();
        String domainType = ParamUtil.getString(request, SystemParameterConstant.PARAM_DOMAIN_TYPE);
        String module = ParamUtil.getString(request, SystemParameterConstant.PARAM_MODULE);
        String status = ParamUtil.getString(request, SystemParameterConstant.PARAM_STATUS);
        String description = ParamUtil.getString(request, SystemParameterConstant.PARAM_DESCRIPTION);
        query.setDomainType(domainType);
        query.setModule(module);
        query.setStatus(status);
        query.setDescription(description);
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, true, filterParameter);
        ParamUtil.setSessionAttr(request, SystemParameterConstant.PARAM_DOMAIN_TYPE, domainType);
        ParamUtil.setSessionAttr(request, SystemParameterConstant.PARAM_MODULE, module);
        ParamUtil.setSessionAttr(request, SystemParameterConstant.PARAM_STATUS, status);
        ParamUtil.setSessionAttr(request, SystemParameterConstant.PARAM_DESCRIPTION, description);

        ValidationResult vr = WebValidationHelper.validateProperty(query, "search");
        if(vr != null && vr.isHasErrors()) {
            Map<String, String> errorMap = vr.retrieveAll();
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, "N");
        }else {
            searchParam.addFilter(SystemParameterConstant.PARAM_DOMAIN_TYPE, domainType, true);
            if(StringUtil.isNotEmpty(description)){
                searchParam.addFilter(SystemParameterConstant.PARAM_DESCRIPTION, description, true);
            }

            if(StringUtil.isNotEmpty(module)){
                searchParam.addFilter(SystemParameterConstant.PARAM_MODULE, module, true);
            }

            if(StringUtil.isNotEmpty(status)){
                searchParam.addFilter(SystemParameterConstant.PARAM_STATUS, status, true);
            }
        }
    }

    private void beforeSave(SystemParameterDto dto){
        String paramType = dto.getParamType();
        if (SystemParameterConstant.PARAM_TYPE_FILE_TYPE_FOR_UPLOADING.equals(paramType)) {
            String value = dto.getValue().toUpperCase();
            dto.setValue(value);
        }
    }

    /**
     * AutoStep: doEdit
     * user do edit with message management
     * @param bpc
     */
    public void doEdit(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String curAct = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(!"doEdit".equals(curAct)){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"Y");
            return;
        }

        String value = ParamUtil.getString(request, SystemParameterConstant.PARAM_VALUE);
        String description = ParamUtil.getString(request, SystemParameterConstant.PARAM_DESCRIPTION);
        SystemParameterDto editDto = (SystemParameterDto) ParamUtil.getSessionAttr(request, SystemParameterConstant.PARAMETER_REQUEST_DTO);
        AuditTrailDto att = IaisEGPHelper.getCurrentAuditTrailDto();
        editDto.setAuditTrailDto(att);
        editDto.setValue(value);
        editDto.setDescription(description);
        ValidationResult vResult = WebValidationHelper.validateProperty(editDto, "edit");
        if(vResult != null && vResult.isHasErrors()){
            Map<String,String> errorMap = vResult.retrieveAll();
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
        }else {
            boolean status =  parameterService.getPropertyOffsetStatus(editDto.getPropertiesKey());
            if(status){
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("customErrorMessage",
                        MessageUtil.getMessageDesc("SYSPAM_ERROR0009")));
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            }else {
                beforeSave(editDto);
                parameterService.setPropertyOffset(editDto.getPropertiesKey(), true);
                parameterService.saveSystemParameter(editDto);
                parameterService.setPropertyOffset(editDto.getPropertiesKey(), false);
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
                ParamUtil.setRequestAttr(request,"ackMsg", MessageUtil.dateIntoMessage("SYSPAM_ACK001"));
                ParamUtil.setSessionAttr(request, SystemParameterConstant.PARAMETER_REQUEST_DTO, editDto);
            }
        }
    }

    /**
     * AutoStep: back
     * @param bpc
     */
    public void back(BaseProcessClass bpc){
        // nothing to do
    }


    /**
     * AutoStep: disableStatus
     * @param bpc
     */
    public void disableStatus(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String pid = ParamUtil.getString(request,IaisEGPConstant.CRUD_ACTION_VALUE);
        if(StringUtil.isNotEmpty(pid)) {
            SystemParameterDto dto = parameterService.getParameterByPid(pid);
            dto.setStatus(AppConsts.COMMON_STATUS_DELETED);
            parameterService.saveSystemParameter(dto);
        }
    }

    /**
     * AutoStep: prepareEdit
     * @param bpc
     */
    public void prepareEdit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, PRE_SAVE_USER_ID, null);
        String pid = ParamUtil.getString(bpc.request, IntranetUserConstant.CRUD_ACTION_VALUE);
        if (StringUtils.isEmpty(pid)) {
            return;
        }
        SearchResult<SystemParameterQueryDto> result = (SearchResult<SystemParameterQueryDto>) ParamUtil.getSessionAttr(request,
                SystemParameterConstant.PARAM_SEARCHRESULT);
        if (result == null) {
            return;
        }
        List<SystemParameterQueryDto> parameterQueryDtos = result.getRows();
        for (SystemParameterQueryDto query : parameterQueryDtos) {
            if (!pid.equals(query.getId())) {
                continue;
            }
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
            systemParameterDto.setValueType(query.getValueType());

            LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
            if (Optional.ofNullable(loginContext).isPresent()) {
                systemParameterDto.setModifiedByName(loginContext.getUserName());
                systemParameterDto.setModifiedBy(loginContext.getUserId());
            } else {
                systemParameterDto.setModifiedByName("System");
                systemParameterDto.setModifiedBy(AppConsts.USER_ID_SYSTEM);
            }

            ParamUtil.setSessionAttr(request, SystemParameterConstant.PARAMETER_REQUEST_DTO, systemParameterDto);
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
