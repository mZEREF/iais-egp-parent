package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.inputFiles.SearchInputFilesDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.service.impl.InputFilesManagementServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;



@Slf4j
@Delegator("inputFilesManagementDelegator")
public class InputFilesManagementDelegator {

    @Autowired
    private InputFilesManagementServiceImpl inputFilesManagementService;

    private static final String STR_SEARCH_PARAM_ATTR = "inputFilesSearchParam";

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

        ParamUtil.setSessionAttr(bpc.request, STR_SEARCH_PARAM_ATTR, initSearchParam());
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
    public void preSearch(BaseProcessClass bpc){
        SearchParam searchParam = initSearchParam();
        HashMap<String,String> filters = new HashMap<>();
        filters.put("fileName",ParamUtil.getString(bpc.request, "fileName"));
        filters.put("fileType",ParamUtil.getString(bpc.request, "fileType"));
        filters.put("status",ParamUtil.getString(bpc.request, "status"));
        filters.put("dateFrom",ParamUtil.getString(bpc.request, "dateFrom"));
        filters.put("dateTo",ParamUtil.getString(bpc.request, "dateTo"));

        filters.forEach((key,value) -> {
                    if(StringUtil.isNotEmpty(value)){
                        searchParam.addFilter(key,value,true);
                    }
                }
        );

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

    }

    private SearchParam initSearchParam() {
        SearchParam searchParam = new SearchParam(SearchInputFilesDto.class.getName());
        searchParam.setPageSize(SystemParamUtil.getDefaultPageSize());
        searchParam.setPageNo(1);
        searchParam.setSortField("ID");
        return searchParam;
    }
}
