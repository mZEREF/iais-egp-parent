package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2019-12-04 15:00
 **/
@Slf4j
public class SearchResultHelper {

    public static SearchParam getSearchParam(HttpServletRequest request,boolean isNew, FilterParameter filter){
        SearchParam searchParam = null;
        if(!isNew){
            searchParam = (SearchParam) ParamUtil.getSessionAttr(request, filter.getSearchAttr());
        }else{
            try {
                if(searchParam == null || isNew){
                    searchParam = new SearchParam(filter.getClz().getName());
                    searchParam.setPageSize(filter.getPageSize());
                    searchParam.setPageNo(filter.getPageNo());
                    if (filter.getSortType() != null){
                        searchParam.setSort(filter.getSortField(), filter.getSortType());
                    }else {
                        searchParam.setSort(filter.getSortField(), SearchParam.ASCENDING);
                    }
                    if(filter.getFilters()!=null){
                        Map<String,Object> inboxMap = filter.getFilters();
                        for(Map.Entry<String, Object> entry : inboxMap.entrySet()){
                            String mapKey = entry.getKey();
                            Object mapValue = entry.getValue();
                            searchParam.addFilter(mapKey,mapValue,true);
                        }
                    }
                }
            }catch (NullPointerException e){
                log.info("getSearchParam ===>>>> " + e.getMessage());
            }
        }
        return searchParam;
    }
}
