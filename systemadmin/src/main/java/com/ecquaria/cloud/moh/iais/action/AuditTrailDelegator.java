package com.ecquaria.cloud.moh.iais.action;

/*
 *author: yichen
 *date time:9/16/2019 2:11 PM
 *description:
 */

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.audit.AuditTrailConstant;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.audit.AuditTrailExcelDto;
import com.ecquaria.cloud.moh.iais.common.dto.audit.AuditTrailQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.AuditLogDetailView;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

@Delegator(value = "auditTrailDelegator")
@Slf4j
public class AuditTrailDelegator {

    private FilterParameter filterParameter = new FilterParameter.Builder()
            .clz(AuditTrailQueryDto.class)
            .searchAttr(AuditTrailConstant.PARAM_SEARCH)
            .resultAttr(AuditTrailConstant.PARAM_SEARCHRESULT)
            .sortField("action_time").sortType(SearchParam.DESCENDING).build();


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
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_AUDIT_TRAIL,AuditTrailConsts.FUNCTION_AUDIT_TRAIL_VIEW);

        HttpServletRequest request = bpc.request;

        ParamUtil.setSessionAttr(request, "isFullMode", null);
        ParamUtil.setSessionAttr(request, AuditTrailConstant.PARAM_SEARCH, null);
        ParamUtil.setSessionAttr(request, AuditTrailConstant.PARAM_SEARCHRESULT, null);
    }

    /**
    * @AutoStep: prepareSwitch
    * @param:
    * @return:
    * @author: yichen
    */
    public void prepareSwitch(BaseProcessClass bpc) {
        log.debug("The prepareSwitch start ...");
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
        boolean isAdmin = AccessUtil.isAdministrator();
        preSelectOption(request);
        if (isAdmin){
            ParamUtil.setSessionAttr(request, AuditTrailConstant.IS_FULL_MODE, "Y");
        }else {
            ParamUtil.setSessionAttr(request, AuditTrailConstant.IS_FULL_MODE, "N");
        }
    }

    public void prepareFullMode(BaseProcessClass bpc){
    }

    public void prepareDataMode(BaseProcessClass bpc){
    }


    public void viewActivities(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String auditId = ParamUtil.getMaskedString(request, "auditId");
        AuditTrailDto att = auditTrailService.getAuditTrailById(auditId);
        ParamUtil.setRequestAttr(request, "auditLogDetailView", generateViewDetail(att));
        ParamUtil.setRequestAttr(request, AuditTrailConstant.PARAM_ACTION_DATA, att);
        log.info("audit id........ {} ", auditId);
    }

    private AuditLogDetailView generateViewDetail(AuditTrailDto atd) {
        AuditLogDetailView view = new AuditLogDetailView();
        if(atd.getOperation()==AuditTrailConsts.OPERATION_FOREIGN_INTERFACE){
            if (StringUtil.isNotEmpty(atd.getBeforeAction())) {
                view.setRequest(auditTrailService.genAuditLogRecList(atd.getBeforeAction()));
            }
            if (StringUtil.isNotEmpty(atd.getAfterAction())) {
                view.setResponse(auditTrailService.genAuditLogRecList(atd.getAfterAction()));
            }
        }else {
            if (StringUtil.isNotEmpty(atd.getBeforeAction())) {
                view.setBeforeChange(auditTrailService.genAuditLogRecList(atd.getBeforeAction()));
            }
            if (StringUtil.isNotEmpty(atd.getAfterAction())) {
                view.setAfterChange(auditTrailService.genAuditLogRecList(atd.getAfterAction()));
            }
        }
        if (StringUtil.isNotEmpty(atd.getViewParams())) {
            view.setSearchParam(auditTrailService.genAuditLogRecList(atd.getViewParams()));
        }
        if (StringUtil.isNotEmpty(atd.getValidationFail())) {
            view.setErrorMsg(auditTrailService.genAuditLogRecList(atd.getValidationFail()));
        }

        return view;
    }
    private void preSelectOption(HttpServletRequest request) {
        List<SelectOption> operationList =  IaisCommonUtils.genNewArrayList();
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_LOGIN), "Login"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_LOGOUT), "Logout"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_LOGIN_FAIL), "Login Failure"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_VIEW_RECORD), "View Record"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_INSERT), "Insert Record"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_UPDATE), "Update Record"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_DELETE), "Delete Record"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_VALIDATION_FAIL), "Data Validation Failure"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_USER_UPDATE), "User Account"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_SESSION_TIMEOUT), "Session Timeout"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_INACTIVE_RECORD), "Inactive Record"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_UNAUTHORISED_ACCESS_SOURCES), "Unauthorised Access Sources"));
        operationList.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_FOREIGN_INTERFACE), "Foreign Interface"));
        ParamUtil.setRequestAttr(request, "operationValueTypeSelect", operationList);

        List<SelectOption> dataActivites =  IaisCommonUtils.genNewArrayList();
        dataActivites.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_VIEW_RECORD), "Data Read"));
        dataActivites.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_INSERT), "Data Inserted"));
        dataActivites.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_UPDATE), "Data Updated"));
        dataActivites.add(new SelectOption(String.valueOf(AuditTrailConsts.OPERATION_DELETE), "Data Deleted"));
        ParamUtil.setRequestAttr(request, "dataActivitesTypeSelect", dataActivites);
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
        SearchResult<AuditTrailQueryDto> searchResult = new SearchResult<>();
        if (IaisCommonUtils.isNotEmpty(searchParam.getFilters())){
            searchParam.setPageNo(0);
            searchParam.setPageSize(Integer.MAX_VALUE);
            searchResult = auditTrailService.listAuditTrailDto(searchParam);
        }

        try {
            List<AuditTrailExcelDto> etList = IaisCommonUtils.genNewArrayList();
            List<AuditTrailQueryDto> atList = searchResult.getRows();
            for (AuditTrailQueryDto i : atList) {
                AuditTrailExcelDto atExcel = new AuditTrailExcelDto();
                auditTrailService.doSetAuditTrailExcel(atExcel, i);
                etList.add(atExcel);
            }
            File file = ExcelWriter.writerToExcel(etList, AuditTrailExcelDto.class, "Audit Trail Logging");
            FileUtils.writeFileResponseContent(response, file);
            FileUtils.deleteTempFile(file);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
    * @AutoStep: doQuery
    * @param:
    * @return:
    * @author: yichen
    */
    public void doQuery(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String curAct = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(!AuditTrailConstant.ACTION_QUERY.equals(curAct)){
            return;
        }

        String operationType =  ParamUtil.getString(request, AuditTrailConstant.PARAM_OPERATIONTYPE); // domain
        String operation = ParamUtil.getString(request, AuditTrailConstant.PARAM_OPERATION);
        String user = ParamUtil.getString(request, AuditTrailConstant.PARAM_USER);

        String startDate = ParamUtil.getString(request, AuditTrailConstant.PARAM_STARTDATE);
        String endDate = ParamUtil.getString(request, AuditTrailConstant.PARAM_ENDDATE);
        String dataActivites = ParamUtil.getString(request, "dataActivites");

        AuditTrailQueryDto queryDto = new AuditTrailQueryDto();
        if(StringUtil.isNotEmpty(operation)){
            //@Min(value = 1, message = "GENERAL_ERR0006", profiles = "query")
            queryDto.setOperation(Integer.parseInt(operation));
        }

	    if(StringUtil.isNotEmpty(operationType)) {
		    queryDto.setDomain(Integer.valueOf(operationType));
	    }

        queryDto.setDateStart(startDate);
        queryDto.setDateEnd(endDate);
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, true, filterParameter);
        ValidationResult vResult = WebValidationHelper.validateProperty(queryDto, "query");
        if(vResult != null && vResult.isHasErrors()) {
            Map<String, String> errorMap = vResult.retrieveAll();
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, "N");
        }else {
            //Corresponding XML param
            searchParam.addFilter(AuditTrailConstant.PARAM_OPERATIONTYPE, Integer.valueOf(operationType), true);

            if(StringUtil.isNotEmpty(operation)){
                searchParam.addFilter(AuditTrailConstant.PARAM_OPERATION, Integer.valueOf(operation), true);
            }

            if(StringUtil.isNotEmpty(operationType) && StringUtil.isNotEmpty(user)){
                int n = Integer.parseInt(operationType);
                switch (n){
                    case AuditTrailConsts.OPERATION_TYPE_INTERNET:
                        searchParam.addFilter(AuditTrailConstant.PARAM_USER_INTER, user, true);
                        break;
                    case AuditTrailConsts.OPERATION_TYPE_INTRANET:
                        searchParam.addFilter(AuditTrailConstant.PARAM_USER_INTRA, user, true);
                        break;
                    case AuditTrailConsts.OPERATION_TYPE_BATCH_JOB:
                        searchParam.addFilter(AuditTrailConstant.PARAM_USER_JOB, user, true);
                        break;
                    default:
                }
            }

            if(StringUtil.isNotEmpty(startDate)){
                Date d = IaisEGPHelper.parseToDate(startDate);
                startDate = IaisEGPHelper.parseToString(d, "yyyy-MM-dd HH:mm:ss");
                searchParam.addFilter(AuditTrailConstant.PARAM_STARTDATE, startDate, true);
            }

            if(StringUtil.isNotEmpty(endDate)){
                Date e = IaisEGPHelper.parseToDate(endDate);
                e = IaisEGPHelper.getLastSecond(e);
                endDate = IaisEGPHelper.parseToString(e, "yyyy-MM-dd HH:mm:ss");
                searchParam.addFilter(AuditTrailConstant.PARAM_ENDDATE, endDate, true);
            }

            if(StringUtil.isNotEmpty(dataActivites)){
                searchParam.addFilter(AuditTrailConstant.PARAM_OPERATION, Integer.valueOf(dataActivites), true);
            }

            setQuerySql(searchParam);
            queryResult(request, searchParam);
        }
    }

    /**
     * AutoStep: changePage
     * @param bpc
     */
    public void changePage(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        if (IaisCommonUtils.isNotEmpty(searchParam.getFilters())){
            setQuerySql(searchParam);
            CrudHelper.doPaging(searchParam,bpc.request);
            queryResult(request, searchParam);
        }
    }

    private void setQuerySql(SearchParam searchParam){
        boolean isAdmin = AccessUtil.isAdministrator();
        if (isAdmin){
            QueryHelp.setMainSql("systemAdmin", "queryFullModeAuditTrail", searchParam);
        }else {
            QueryHelp.setMainSql("systemAdmin", "queryDataMaskModeAuditTrail", searchParam);
        }
    }

    private void queryResult(HttpServletRequest request, SearchParam searchParam){
        SearchResult<AuditTrailQueryDto> trailDtoSearchResult = auditTrailService.listAuditTrailDto(searchParam);
        ParamUtil.setSessionAttr(request, AuditTrailConstant.PARAM_SEARCHRESULT, trailDtoSearchResult);
        ParamUtil.setSessionAttr(request, AuditTrailConstant.PARAM_SEARCH, searchParam);
    }
    /**
     * AutoStep: sortRecords
     * @param bpc
     */
    public void sortRecords(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        if (IaisCommonUtils.isNotEmpty(searchParam.getFilters())){
            setQuerySql(searchParam);
            CrudHelper.doSorting(searchParam,  request);
            queryResult(request, searchParam);
        }
    }
}
