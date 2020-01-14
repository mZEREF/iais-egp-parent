package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2019-12-04 15:00
 **/
@Slf4j
public class SearchResultHelper {

    public static SearchParam getSearchParam(HttpServletRequest request, FilterParameter filter) {
        return getSearchParam(request, filter,false);
    }

    public static SearchParam getSearchParam(HttpServletRequest request,FilterParameter filter,boolean isNew) {
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, filter.getSearchAttr());
        try {
            if (searchParam == null || isNew) {
                searchParam = new SearchParam(filter.getClz().getName());
                searchParam.setPageSize(filter.getPageSize());
                searchParam.setPageNo(filter.getPageNo());
                if (filter.getSortType() != null) {
                    searchParam.setSort(filter.getSortField(), filter.getSortType());
                } else {
                    searchParam.setSort(filter.getSortField(), SearchParam.ASCENDING);
                }
                if (filter.getFilters() != null && filter.getFilters().size()>0) {
                    Map<String, Object> inboxMap = filter.getFilters();
                    for (Map.Entry<String, Object> entry : inboxMap.entrySet()) {
                        String mapKey = entry.getKey();
                        Object mapValue = entry.getValue();
                        searchParam.addFilter(mapKey, mapValue,true);
                    }
                }

            }

        } catch (NullPointerException e) {
            log.info(e.getMessage());
        }
        return searchParam;
    }

    public static void doSort(HttpServletRequest request,FilterParameter filterParameter){
        String sortFieldName = ParamUtil.getString(request,"crud_action_value");
        String sortType = ParamUtil.getString(request,"crud_action_additional");
        if(!StringUtil.isEmpty(sortFieldName)&&!StringUtil.isEmpty(sortType)){
            filterParameter.setSortType(sortType);
            filterParameter.setSortField(sortFieldName);
        }
    }

    public static void doPage(HttpServletRequest request,FilterParameter filterParameter){
        String pageNo = ParamUtil.getString(request, "pageJumpNoTextchangePage");
        String pageSize = ParamUtil.getString(request, "pageJumpNoPageSize");
        if (!StringUtil.isEmpty(pageNo)) {
            filterParameter.setPageNo(Integer.parseInt(pageNo));
        }
        if (!StringUtil.isEmpty(pageSize)) {
            filterParameter.setPageSize(Integer.parseInt(pageSize));
        }
    }

    public static void getDateByWeekOfDay(Map<Date,String> dateOfWeekList,String day,boolean avaiAM,boolean avaiPM) {
        int i = 1;
        Calendar calendar = Calendar.getInstance();
        int THIS_YEAR = calendar.get(Calendar.YEAR);
        while (calendar.get(Calendar.YEAR) < THIS_YEAR + 1) {
            calendar.set(THIS_YEAR, 01, 01);
            calendar.set(Calendar.WEEK_OF_YEAR, i++);
            switch (day.toUpperCase()) {
                case "MONDAY":
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                    if (calendar.get(Calendar.YEAR) == THIS_YEAR) {
                        judgeDate(dateOfWeekList,calendar,avaiAM,avaiPM);
                    }
                    break;
                case "TUESDAY":
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                    if (calendar.get(Calendar.YEAR) == THIS_YEAR) {
                        judgeDate(dateOfWeekList,calendar,avaiAM,avaiPM);
                    }
                    break;
                case "WEDNESDAY":
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                    if (calendar.get(Calendar.YEAR) == THIS_YEAR) {
                        judgeDate(dateOfWeekList,calendar,avaiAM,avaiPM);
                    }
                    break;
                case "THURSDAY":
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                    if (calendar.get(Calendar.YEAR) == THIS_YEAR) {
                        judgeDate(dateOfWeekList,calendar,avaiAM,avaiPM);
                    }
                    break;
                case "FRIDAY":
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                    if (calendar.get(Calendar.YEAR) == THIS_YEAR) {
                        judgeDate(dateOfWeekList,calendar,avaiAM,avaiPM);
                    }
                    break;
                case "SATURDAY":
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                    if (calendar.get(Calendar.YEAR) == THIS_YEAR) {
                        judgeDate(dateOfWeekList,calendar,avaiAM,avaiPM);
                    }
                    break;
                default:
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                    if (calendar.get(Calendar.YEAR) == THIS_YEAR) {
                        judgeDate(dateOfWeekList,calendar,avaiAM,avaiPM);
                    }
                    break;
            }
        }
    }

    private static void judgeDate(Map<Date,String> dateOfWeekList,Calendar calendar,boolean avaiAM,boolean avaiPM) {
        if(avaiAM && avaiPM) {
            dateOfWeekList.put(calendar.getTime(), "ALL");
        }else if(avaiAM && !avaiPM) {
            dateOfWeekList.put(calendar.getTime(), "AM");
        }else {
            dateOfWeekList.put(calendar.getTime(), "PM");
        }
    }
}