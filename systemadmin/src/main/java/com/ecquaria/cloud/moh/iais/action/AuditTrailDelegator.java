package com.ecquaria.cloud.moh.iais.action;

/*
 *author: yichen
 *date time:9/16/2019 2:11 PM
 *description:
 */

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.audit.AuditTrailConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.audit.AuditTrailQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelWriter;
import com.ecquaria.cloud.moh.iais.service.AuditTrailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Delegator(value = "auditTrailDelegator")
@Slf4j
public class AuditTrailDelegator {

    private FilterParameter filterParameter = new FilterParameter.Builder()
            .clz(AuditTrailQueryDto.class)
            .searchAttr(AuditTrailConstants.PARAM_SEARCH)
            .resultAttr(AuditTrailConstants.PARAM_SEARCHRESULT)
            .sortField("audit_id").sortType(SearchParam.ASCENDING).build();


    private AuditTrailService auditTrailService;

    @Autowired
    public AuditTrailDelegator(AuditTrailService auditTrailService){
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

        ParamUtil.setSessionAttr(request, "isFullMode", null);
        ParamUtil.setSessionAttr(request, AuditTrailConstants.PARAM_SEARCH, null);
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

        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request,
                AppConsts.SESSION_ATTR_LOGIN_USER);

        if (!Optional.ofNullable(loginContext).isPresent()){
            log.info(StringUtil.changeForLog("===>> don't have loginContext" + loginContext));
        }

        boolean isAdmin = AccessUtil.isAdministrator();
        preSelectOption(request);
        if (isAdmin){
            ParamUtil.setSessionAttr(request, "isFullMode", "Y");
        }else {
            ParamUtil.setSessionAttr(request, "isFullMode", "N");
        }
    }

    public void prepareFullMode(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        log.info("entry prepareFullMode");
    }

    public void prepareDataMode(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        log.info("entry prepareDataMode");
    }

    private void preSelectOption(HttpServletRequest request) {
        List<SelectOption> operationTypeList = IaisCommonUtils.genNewArrayList();
        operationTypeList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_TYPE_INTERNET), "Internet"));
        operationTypeList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_TYPE_INTRANET), "Intranet"));
        operationTypeList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_TYPE_BATCH_JOB), "System Batch Job"));
        ParamUtil.setRequestAttr(request, "operationTypeSelect", operationTypeList);

        List<SelectOption> operationList =  IaisCommonUtils.genNewArrayList();
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_LOGIN), "Login"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_LOGOUT), "Logout"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_LOGIN_FAIL), "Login Failure"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_VIEW_RECORD), "View Record"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_VALIDATION_FAIL), "Data Validation Failure"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_INSERT), "Insert Record"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_UPDATE), "Update Record"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_DELETE), "Delete Record"));

        ParamUtil.setRequestAttr(request, "operationValueTypeSelect", operationList);

    }

    /**
     * @AutoStep: doExport
     * @param:
     * @description: Export Excel
     * @return:
     * @author: yichen
     */
    @GetMapping(value = "audit-trail-file")
    public @ResponseBody void fileHandler(HttpServletRequest request, HttpServletResponse response){
        log.debug(StringUtil.changeForLog("fileHandler start ...."));

        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);

        if (searchParam.getFilters().isEmpty()){
            log.info("don't have filter params");
            return;
        }

        //searchParam.setPageNo(0);
        searchParam.setPageSize(Integer.MAX_VALUE);
        SearchResult<AuditTrailQueryDto> searchResult = auditTrailService.listAuditTrailDto(searchParam);
        if (searchResult == null){
            log.info("==export audit trail log , the record is empty>>>>");
            return;
        }

        File file = ExcelWriter.exportExcel(searchResult.getRows(), AuditTrailQueryDto.class, "Audit Trail Logging");
        try {
            FileUtils.writeFileResponseContent(response, file);
            FileUtils.deleteTempFile(file);
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
        log.debug(StringUtil.changeForLog("fileHandler end ...."));

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

        String operationType =  ParamUtil.getString(request, AuditTrailConstants.PARAM_OPERATIONTYPE); // domain
        String operation = ParamUtil.getString(request, AuditTrailConstants.PARAM_OPERATION);
        String user = ParamUtil.getString(request, AuditTrailConstants.PARAM_USER);

        String startDate = ParamUtil.getString(request, AuditTrailConstants.PARAM_STARTDATE);
        String endDate = ParamUtil.getString(request, AuditTrailConstants.PARAM_ENDDATE);

        AuditTrailQueryDto queryDto = new AuditTrailQueryDto();
        if(operation != null){
            queryDto.setOperation(Integer.parseInt(operation));
        }

	    if(!StringUtil.isEmpty(operationType)) {
		    queryDto.setDomain(Integer.valueOf(operationType));
	    }

        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, true, filterParameter);
        ValidationResult validationResult = WebValidationHelper.validateProperty(queryDto, "query");
        if(validationResult != null && validationResult.isHasErrors()) {
            Map<String, String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, "N");
            return;
        }else {
            //Corresponding XML param
            searchParam.addFilter(AuditTrailConstants.PARAM_OPERATIONTYPE, Integer.valueOf(operationType), true);

            if(!StringUtil.isEmpty(operation)){
                searchParam.addFilter(AuditTrailConstants.PARAM_OPERATION, Integer.valueOf(operation), true);
            }

            if(!StringUtil.isEmpty(user)){
                searchParam.addFilter(AuditTrailConstants.PARAM_USER, user, true);
            }

            if(!StringUtil.isEmpty(startDate)){
                searchParam.addFilter(AuditTrailConstants.PARAM_STARTDATE, startDate, true);
            }

            if(!StringUtil.isEmpty(endDate)){
                searchParam.addFilter(AuditTrailConstants.PARAM_ENDDATE, endDate, true);
            }
        }

        SearchResult<AuditTrailQueryDto> trailDtoSearchResult = null;

        String isFullMode = (String) ParamUtil.getSessionAttr(request, "isFullMode");
        switch (isFullMode){
            case "Y":
                QueryHelp.setMainSql("systemAdmin", "queryFullModeAuditTrail", searchParam);
                trailDtoSearchResult = auditTrailService.listAuditTrailDto(searchParam);
                break;
            case "N":
                QueryHelp.setMainSql("systemAdmin", "queryDataMaskModeAuditTrail", searchParam);
                trailDtoSearchResult = auditTrailService.listAuditTrailDto(searchParam);
                break;
            default:
        }

        ParamUtil.setSessionAttr(request, AuditTrailConstants.PARAM_SEARCH, searchParam);
        ParamUtil.setRequestAttr(request, AuditTrailConstants.PARAM_SEARCHRESULT, trailDtoSearchResult);
    }

    /**
     * AutoStep: changePage
     * @param bpc
     */
    public void changePage(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        CrudHelper.doPaging(searchParam,bpc.request);
        SearchResult<AuditTrailQueryDto> trailDtoSearchResult = auditTrailService.listAuditTrailDto(searchParam);
        ParamUtil.setRequestAttr(request, AuditTrailConstants.PARAM_SEARCHRESULT, trailDtoSearchResult);
    }

    /**
     * AutoStep: sortRecords
     * @param bpc
     */
    public void sortRecords(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        CrudHelper.doSorting(searchParam,  request);
        SearchResult<AuditTrailQueryDto> trailDtoSearchResult = auditTrailService.listAuditTrailDto(searchParam);
        ParamUtil.setRequestAttr(request, AuditTrailConstants.PARAM_SEARCHRESULT, trailDtoSearchResult);
    }

}
