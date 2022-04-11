package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxLicenceQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class HalpSearchResultHelper {
    public static SearchParam getSearchParam(HttpServletRequest request, String searchClassName) {
        return getSearchParam(request, searchClassName,false);
    }



    public static SearchParam gainSearchParam(HttpServletRequest request,String paramName,String searchClassName,String sortField,String sortRule,boolean isNew){
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, paramName);
        if(searchParam == null || isNew){
            searchParam = new SearchParam(searchClassName);
            searchParam.setPageSize(SystemParamUtil.getDefaultPageSize());
            searchParam.setPageNo(1);
            searchParam.setSort(sortField, sortRule);
            ParamUtil.setSessionAttr(request,paramName, searchParam);
        }
        return searchParam;
    }

    public static SearchParam getSearchParam(HttpServletRequest request,String searchClassName,boolean isNew) {
        int defaultPageSize = SystemParamUtil.getDefaultPageSize();
        SearchParam searchParam = null;
        switch (searchClassName) {
            case "inboxMsg":
                searchParam = (SearchParam) ParamUtil.getSessionAttr(request, InboxConst.INBOX_PARAM);
                if (searchParam == null || isNew) {
                    searchParam = new SearchParam(InboxQueryDto.class.getName());
                    searchParam.setPageSize(defaultPageSize);
                    searchParam.setPageNo(1);
                    searchParam.setSort("created_dt", SearchParam.DESCENDING);
                    ParamUtil.setSessionAttr(request,InboxConst.INBOX_PARAM, searchParam);
                }
                break;
            case "inboxApp":
                searchParam = (SearchParam) ParamUtil.getSessionAttr(request, InboxConst.APP_PARAM);
                if (searchParam == null || isNew) {
                    searchParam = new SearchParam(InboxAppQueryDto.class.getName());
                    searchParam.setPageNo(1);
                    searchParam.setPageSize(defaultPageSize);
                    searchParam.setSort("created_dt", SearchParam.DESCENDING);
                    ParamUtil.setSessionAttr(request,InboxConst.APP_PARAM, searchParam);
                }
                break;
            case "inboxLic":
                searchParam = (SearchParam) ParamUtil.getSessionAttr(request, InboxConst.LIC_PARAM);
                if (searchParam == null || isNew) {
                    searchParam = new SearchParam(InboxLicenceQueryDto.class.getName());
                    searchParam.setSort("START_DATE", SearchParam.DESCENDING);
                    searchParam.setPageNo(1);
                    searchParam.setPageSize(defaultPageSize);
                    ParamUtil.setSessionAttr(request,InboxConst.LIC_PARAM, searchParam);
                }
                break;
        }
        return searchParam;
    }

    public static void doSort(HttpServletRequest request,SearchParam searchParam){
        String sortFieldName = ParamUtil.getString(request,"crud_action_value");
        String sortType = ParamUtil.getString(request,"crud_action_additional");
        if(!StringUtil.isEmpty(sortFieldName)&&!StringUtil.isEmpty(sortType)){
            searchParam.setSort(sortFieldName,sortType);
        }
    }

    public static void doSearch(SearchParam searchParam,Map<String,Object> SearchMap){
        if (SearchMap != null && SearchMap.size()>0){
            for (Map.Entry<String, Object> entry : SearchMap.entrySet()) {
                String mapKey = entry.getKey();
                Object mapValue = entry.getValue();
                searchParam.addFilter(mapKey, mapValue,true);
            }
        }
    }

    public static void doPage(HttpServletRequest request,SearchParam searchParam){
        String pageNo = ParamUtil.getString(request, "pageJumpNoTextchangePage");
        String pageSize = ParamUtil.getString(request, "pageJumpNoPageSize");
        if (!StringUtil.isEmpty(pageNo)) {
            searchParam.setPageNo(Integer.parseInt(pageNo));
        }
        if (!StringUtil.isEmpty(pageSize)) {
            searchParam.setPageSize(Integer.parseInt(pageSize));
        }
    }

}
