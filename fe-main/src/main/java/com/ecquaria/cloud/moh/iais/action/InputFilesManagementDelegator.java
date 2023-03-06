package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.inputFiles.SearchInputFilesDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.service.impl.InputFilesManagementServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



@Slf4j
@Delegator("inputFilesManagementDelegator")
public class InputFilesManagementDelegator {

    @Autowired
    private InputFilesManagementServiceImpl inputFilesManagementService;

    private static final String STR_SEARCH_PARAM_ATTR = "inputFilesSearchParam";
    private static final String DATE_TO = "dateTo";

    /**
     * AutoStep: Init
     *
     * @param bpc
     * @throws
     */
    public void init(BaseProcessClass bpc){
        List<SelectOption> fileTypeOpts = IaisCommonUtils.genNewArrayList();
        fileTypeOpts.add(new SelectOption("INFI001","INFI001"));
        ParamUtil.setSessionAttr(bpc.request, "fileTypeOptions", (Serializable) fileTypeOpts);

        List<SelectOption> statusOpts = IaisCommonUtils.genNewArrayList();
        statusOpts.add(new SelectOption("FSTAT01","FSTAT01"));
        ParamUtil.setSessionAttr(bpc.request, "statusOptions", (Serializable) statusOpts);

        ParamUtil.setSessionAttr(bpc.request, STR_SEARCH_PARAM_ATTR, initSearchParam(bpc.request));
    }

    /**
     * AutoStep: Search
     *
     * @param bpc
     * @throws
     */
    public void search(BaseProcessClass bpc){
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, STR_SEARCH_PARAM_ATTR);
        QueryHelp.setMainSql("inputFilesManagement","searchInputFiles", searchParam);
        SearchResult<SearchInputFilesDto> searchResult = inputFilesManagementService.searchInputFiles(searchParam);
        ParamUtil.setSessionAttr(bpc.request, "inputFilesSearchResult", searchResult);
    }

    /**
     * AutoStep: Sorting
     *
     * @param bpc
     * @throws
     */
    public void sorting(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, STR_SEARCH_PARAM_ATTR);
        CrudHelper.doSorting(searchParam,bpc.request);
    }

    /**
     * AutoStep: PreSearch
     *
     * @param bpc
     * @throws
     */
    public void preSearch(BaseProcessClass bpc) throws ParseException {
        SearchParam searchParam = initSearchParam(bpc.request);

        String fileName = ParamUtil.getString(bpc.request, "fileName");
        String fileType = ParamUtil.getString(bpc.request, "fileType");
        String status = ParamUtil.getString(bpc.request, "status");
        String dateFrom = ParamUtil.getString(bpc.request, "dateFrom");
        String dateTo = ParamUtil.getString(bpc.request, DATE_TO);

        if (StringUtil.isNotEmpty(fileName)) {
            searchParam.addFilter("fileName", fileName, true);
        }
        if (StringUtil.isNotEmpty(fileType)) {
            searchParam.addFilter("fileType", fileType, true);
        }
        if (StringUtil.isNotEmpty(status)) {
            searchParam.addFilter("status", status, true);
        }
        if (StringUtil.isNotEmpty(dateFrom)) {
            searchParam.addFilter("dateFrom", dateFrom, true);
        }
        if (StringUtil.isNotEmpty(dateTo)) {
            Date submitDateTo = Formatter.parseDate(dateTo);
            Calendar cal = Calendar.getInstance();
            cal.setTime(submitDateTo);
            cal.add(Calendar.DAY_OF_MONTH, 1);
            searchParam.addParam(DATE_TO, dateTo);
            searchParam.addFilter(DATE_TO, Formatter.formatDate(cal.getTime()));
        }

        ParamUtil.setSessionAttr(bpc.request, STR_SEARCH_PARAM_ATTR, searchParam);
    }

    /**
     * AutoStep: Page
     *
     * @param bpc
     * @throws
     */
    public void page(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, STR_SEARCH_PARAM_ATTR);
        CrudHelper.doPaging(searchParam,bpc.request);
    }

    /**
     * AutoStep: Back
     *
     * @param bpc
     * @throws
     */
    public void back(BaseProcessClass bpc){
        log.info("BaseProcessClass : ->{}",bpc.getClass().getName());

    }

    private SearchParam initSearchParam(HttpServletRequest request) {
        SearchParam searchParam = new SearchParam(SearchInputFilesDto.class.getName());
        searchParam.setPageSize(SystemParamUtil.getDefaultPageSize());
        searchParam.setPageNo(1);
        searchParam.setSortField("ID");
        String orgId = AccessUtil.getLoginUser(request).getOrgId();
        searchParam.addFilter("originalId",orgId,true);
        return searchParam;
    }
}
