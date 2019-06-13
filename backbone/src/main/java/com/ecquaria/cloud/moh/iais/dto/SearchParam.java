package com.ecquaria.cloud.moh.iais.dto;


import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SearchParam implements Serializable {
    public static final String ASCENDING    = "ASC";
    public static final String DESCENDING   = "DESC";

    private HashMap<String, Object> params;     // for template SQL generation
    private LinkedHashMap<String, Object> filters;    // for SQL query
    private LinkedHashMap<String, String> sortMap;
    private int pageSize;
    private int pageNo;

    public SearchParam() {
        clear();
        filters = new LinkedHashMap<String, Object>();
        params = new HashMap<String, Object>();
        sortMap = new LinkedHashMap<String, String>();
    }

    public void addParam(String name, Object value) {
        params.put(name, value);
    }

    public void removeParam(String name) {
        if (params.get(name) != null)
            params.remove(name);
    }

    public void addFilter(String name, Object value) {
        filters.put(name, value);
    }

    public void addFilter(String name, Object value, boolean isTemplateParam) {
        filters.put(name, value);
        if (isTemplateParam) {
            params.put(name, value);
        }
    }

    public void clear() {
        filters = null;
        params = null;
        pageSize = 0;
        pageNo = 1;
        if (sortMap != null)
            sortMap.clear();
    }

    public Map<String, Object> getParams() {
        return params;
    }
    public void setParams(HashMap<String, Object> params) {
        this.params = params;
    }
    public void setSort(String sortField, String sortType) {
        sortMap.clear();
        if (!StringUtils.isEmpty(sortField) && !StringUtils.isEmpty(sortType))
            sortMap.put(sortField.toUpperCase(), sortType);
    }
    public void addSort(String sortField, String sortType) {
        if (!StringUtils.isEmpty(sortField))
            sortMap.put(sortField.toUpperCase(), sortType);
    }
    public Map<String, Object> getFilters() {
        return filters;
    }
    public void setFilters(LinkedHashMap<String, Object> filters) {
        this.filters = filters;
    }
    public void setSortField(String sortField) {
        sortMap.clear();
        sortMap.put(sortField.toUpperCase(), ASCENDING);
    }
    public void addSortField(String sortField) {
        if (!StringUtils.isEmpty(sortField) && !sortMap.containsKey(sortField.toUpperCase()))
            sortMap.put(sortField.toUpperCase(), ASCENDING);
    }
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public int getPageNo() {
        return pageNo;
    }
    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }
    public HashMap<String, String> getSortMap() {
        return sortMap;
    }
}
