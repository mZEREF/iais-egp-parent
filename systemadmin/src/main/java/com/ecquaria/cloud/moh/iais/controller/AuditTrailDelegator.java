package com.ecquaria.cloud.moh.iais.controller;

/*
 *author: yichen
 *date time:9/16/2019 2:11 PM
 *description:
 */

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.audit.AuditTrailConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.audit.AuditTrailQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.ExcelWriter;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AuditTrailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Delegator(value = "auditTrailDelegator")
@Slf4j
public class AuditTrailDelegator {

    private FilterParameter filterParameter;
    private AuditTrailService auditTrailService;

    @Autowired
    public AuditTrailDelegator(FilterParameter filterParameter, AuditTrailService auditTrailService){
        this.filterParameter = filterParameter;
        this.auditTrailService = auditTrailService;
    }

    /**
     * @AutoStep: step1
     * @param:
     * @return:
     * @author: yichen
     */
    public void startStep(BaseProcessClass bpc) throws IllegalAccessException {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>PARAM_SEARCH");
        AuditTrailHelper.auditFunction("AuditTrail",
                "Implement logging mechanisms to enable the timely detection and investigation" +
                        " of events that can lead to ICT security violations or incidents.");
        HttpServletRequest request = bpc.request;
        IaisEGPHelper.clearSessionAttr(request, AuditTrailConstants.class);
    }

    /**
    * @AutoStep: prepareSwitch
    * @param:
    * @return:
    * @author: yichen
    */
    public void prepareSwitch(BaseProcessClass bpc) {
        log.debug("The prepareSwitch start ...");
        String crudAction = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        log.debug("*******************crudAction-->:" + crudAction);
        log.debug("The prepareSwitch end ...");
    }

    /**
    * @AutoStep: prepareData
    * @param: bpc
    * @return: 
    * @author: yichen 
    */
    public void prepareData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        filterParameter.setClz(AuditTrailQueryDto.class);
        filterParameter.setSearchAttr(AuditTrailConstants.PARAM_SEARCH);
        filterParameter.setResultAttr(AuditTrailConstants.PARAM_SEARCHRESULT);
        filterParameter.setSortField("audit_id");

        SearchParam trailDtoSearchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        SearchResult<AuditTrailQueryDto> trailDtoSearchResult = null;
        boolean isAdmin = AccessUtil.isAdministrator();
        if(isAdmin){
            preSelectOptionOfFull(request);
            QueryHelp.setMainSql("systemAdmin", "queryFullModeAuditTrail", trailDtoSearchParam);
            trailDtoSearchResult = auditTrailService.listAuditTrailDto(trailDtoSearchParam);
            ParamUtil.setRequestAttr(request, "isFullMode", "Y");
        }else{
            preSelectOptionOfMask(request);
            QueryHelp.setMainSql("systemAdmin", "queryDataMaskModeAuditTrail", trailDtoSearchParam);
            ParamUtil.setRequestAttr(request, "isFullMode", "N");
            trailDtoSearchResult = auditTrailService.listAuditTrailDto(trailDtoSearchParam);
        }

        ParamUtil.setSessionAttr(request, AuditTrailConstants.PARAM_SEARCH, trailDtoSearchParam);
        ParamUtil.setRequestAttr(request, AuditTrailConstants.PARAM_SEARCHRESULT, trailDtoSearchResult);
    }

    public void prepareFullMode(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        log.info("entry prepareFullMode");
    }

    public void prepareDataMode(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        log.info("entry prepareDataMode");
    }

    private void preSelectOptionOfMask(HttpServletRequest request) {
        List<SelectOption> operationTypeList = new ArrayList<>();
        operationTypeList.add(new SelectOption("INTER", "Internet"));
        operationTypeList.add(new SelectOption("INTRA", "Intranet"));
        operationTypeList.add(new SelectOption("System Batch Job", "System Batch Job"));
        ParamUtil.setRequestAttr(request, "operationTypeSelect", operationTypeList);

        List<SelectOption> operationList =  new ArrayList<>();
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_INTERNET_LOGIN), "Internet Login"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_INTERNET_LOGOUT), "Internet Logout"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_INTERNET_LOGIN_FAIL), "Internet Login Failure"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_INTERNET_VALIDATION_FAIL), "Data Validation Failure – Internet"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_INTRANET_LOGIN), "Intranet Login"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_INTRANET_LOGOUT), "Intranet Logout"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_INTRANET_LOGIN_FAIL), "Intranet Login Failure"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_INTRANET_ALIDATION_FAIL), "Data Validation Failure – Intranet"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_INACTIVE_BATCHJOB), "Inactive Record – System Batch Job"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_UNAUTHORISED_INTERNET), "Unauthorised Attempts to Access Sources – Internet"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_UNAUTHORISED_INTRANET), "Unauthorised Attempts to Access Sources – Intranet"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_PASSWORD_CHANGE_INTERNET), "User account and password changes – Internet"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_PASSWORD_CHANGE_INTRANET), "User account and password changes – Intranet"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_USER_ACTIVITIES_INTERNET), "User Administrator Activities (Internet System Admin)"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_SYSTEM_ACTIVITIES_INTRANET), "System Administrator Activities (Intranet System Admin)"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_ACCOUNT_SUSPENDED), "User accounts suspended"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_ACCOUNT_LOCK), "User accounts Locked"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_ACCOUNT_INACTIVATED), "User accounts Inactivated"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_ACCOUNT_ACTIVE_INTERNET), "Active Internet user accounts with its access rights"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_ACCOUNT_ACTIVE_INTRANET), "Active Intranet user accounts with its access rights"));

        ParamUtil.setRequestAttr(request, "operationValueTypeSelect", operationList);

        List<SelectOption> userList =  new ArrayList<>();
       /* userList.add(new SelectOption("CorpPass ID", "CorpPass ID"));
        userList.add(new SelectOption("CorpPass NRIC", "CorpPass NRIC"));
        userList.add(new SelectOption("SingPass ID", "SingPass ID"));
        userList.add(new SelectOption("MOH Active Directory ID", "MOH Active Directory ID"));
        userList.add(new SelectOption("MOH System Account ID", "MOH System Account ID"));
        userList.add(new SelectOption("Batch Job ID", "Batch Job ID"));*/
        ParamUtil.setRequestAttr(request, "userTypeSelect", userList);
    }

    private void preSelectOptionOfFull(HttpServletRequest request) {
        List<SelectOption> operationTypeList = new ArrayList<>();
       /* operationTypeList.add(new SelectOption("INTER", "Internet"));
        operationTypeList.add(new SelectOption("INTRA", "Intranet"));
        operationTypeList.add(new SelectOption("System Batch Job", "System Batch Job"));*/
        ParamUtil.setRequestAttr(request, "operationTypeSelect", operationTypeList);

        List<SelectOption> operationList =  new ArrayList<>();
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_INTERNET_LOGIN), "Internet Login"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_INTERNET_LOGOUT), "Internet Logout"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_INTERNET_LOGIN_FAIL), "Internet Login Failure"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_INTERNET_VALIDATION_FAIL), "Data Validation Failure – Internet"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_INTRANET_LOGIN), "Intranet Login"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_INTRANET_LOGOUT), "Intranet Logout"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_INTRANET_LOGIN_FAIL), "Intranet Login Failure"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_INTRANET_ALIDATION_FAIL), "Data Validation Failure – Intranet"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_VIEW_ALLUSER), "View Record (for all user)"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_INSERT_ALLUSER), "Insert Record (for all user)"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_UPDATE_ALLUSER), "Update Record (for all user)"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_DELETE_ALLUSER), "Delete Record (for all user)"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_INSERT_BATCHJOB), "Insert Record – System Batch Job"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_UPDATE_BATCHJOB), "Update Record – System Batch Job"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_DELETE_BATCHJOB), "Delete Record – System Batch Job"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_INACTIVE_BATCHJOB), "Inactive Record – System Batch Job"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_UNAUTHORISED_INTERNET), "Unauthorised Attempts to Access Sources – Internet"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_UNAUTHORISED_INTRANET), "Unauthorised Attempts to Access Sources – Intranet"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_PASSWORD_CHANGE_INTERNET), "User account and password changes – Internet"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_PASSWORD_CHANGE_INTRANET), "User account and password changes – Intranet"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_USER_ACTIVITIES_INTERNET), "User Administrator Activities (Internet System Admin)"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_SYSTEM_ACTIVITIES_INTRANET), "System Administrator Activities (Intranet System Admin)"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_ACCOUNT_SUSPENDED), "User accounts suspended"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_ACCOUNT_LOCK), "User accounts Locked"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_ACCOUNT_INACTIVATED), "User accounts Inactivated"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_ACCOUNT_ACTIVE_INTERNET), "Active Internet user accounts with its access rights"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_ACCOUNT_ACTIVE_INTRANET), "Active Intranet user accounts with its access rights"));

        ParamUtil.setRequestAttr(request, "operationValueTypeSelect", operationList);

        List<SelectOption> userList =  new ArrayList<>();
      /*  userList.add(new SelectOption("CorpPass ID", "CorpPass ID"));
        userList.add(new SelectOption("CorpPass NRIC", "CorpPass NRIC"));
        userList.add(new SelectOption("SingPass ID", "SingPass ID"));
        userList.add(new SelectOption("MOH Active Directory ID", "MOH Active Directory ID"));
        userList.add(new SelectOption("MOH System Account ID", "MOH System Account ID"));
        userList.add(new SelectOption("Batch Job ID", "Batch Job ID"));*/
        ParamUtil.setRequestAttr(request, "userTypeSelect", userList);
    }

    /**
     * @AutoStep: doExport
     * @param:
     * @description: Export Excel
     * @return:
     * @author: yichen
     */
    public void doExport(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(!AuditTrailConstants.ACTION_EXPORT_EXCL.equals(currentAction)){
            return;
        }

        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        searchParam.setPageNo(0);
        searchParam.setPageNo(0);

        SearchResult<AuditTrailQueryDto> searchResult = auditTrailService.listAuditTrailDto(searchParam);
        List<AuditTrailQueryDto> auditTrailQueryDtoList = null;
        String jsonStr = JsonUtil.parseToJson(searchResult.getRows());
        auditTrailQueryDtoList = JsonUtil.parseToList(jsonStr, AuditTrailQueryDto.class);

        ExcelWriter excelWriter = new ExcelWriter("AuditDto", null);
        excelWriter.exportXls(auditTrailQueryDtoList);

    }


    /**
    * @AutoStep: doQuery
    * @param:
    * @return:
    * @author: yichen
    */
    public void doQuery(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(!AuditTrailConstants.ACTION_QUERY.equals(currentAction)){
            return;
        }

        //String operationType =  ParamUtil.getString(request, AuditTrailConstants.PARAM_OPERATIONTYPE);
        String operation = ParamUtil.getString(request, AuditTrailConstants.PARAM_OPERATION);
        //String user = ParamUtil.getString(request, AuditTrailConstants.PARAM_USER);

        String startDate = ParamUtil.getDate(request, AuditTrailConstants.PARAM_STARTDATE);
        String endDate = ParamUtil.getDate(request, AuditTrailConstants.PARAM_ENDDATE);

        AuditTrailQueryDto queryDto = new AuditTrailQueryDto();
        if(operation != null){
            queryDto.setOperation(Integer.valueOf(operation));
        }

        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, true, filterParameter);

        ValidationResult validationResult = WebValidationHelper.validateProperty(queryDto, "query");
        if(validationResult != null && validationResult.isHasErrors()) {
            Map<String, String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMAP, errorMap);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, "N");
        }else{
            //Corresponding XML param
            if(!StringUtil.isEmpty(operation)){
                searchParam.addFilter(AuditTrailConstants.PARAM_OPERATION, operation, true);
            }

            if(!StringUtil.isEmpty(startDate)){
                searchParam.addFilter(AuditTrailConstants.PARAM_STARTDATE, startDate, true);
            }

            if(!StringUtil.isEmpty(endDate)){
                searchParam.addFilter(AuditTrailConstants.PARAM_ENDDATE, endDate, true);
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
