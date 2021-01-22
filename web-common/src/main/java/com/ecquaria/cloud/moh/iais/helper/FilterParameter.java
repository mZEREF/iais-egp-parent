package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/*
 *author: yichen
 *date time:9/5/2019 10:44 AM
 *description:
 */

@Getter
@Setter
public final class FilterParameter {
    private int pageNo;
    private int pageSize;
    //for query dto
    private Class<? extends Serializable> clz;
    private String searchAttr;
    private String resultAttr;
    private String sortField;
    private LinkedHashMap<String, String> sortFieldMap;
    private String sortType;
    private Map<String,Object> filters;

    public static class Builder{
        private int pageNo = 1;
        private int pageSize = SystemParamUtil.getDefaultPageSize();
        private Class<? extends Serializable> clz;
        private String searchAttr;
        private String resultAttr;
        private String sortField;
        private String sortType = SearchParam.ASCENDING;
        private LinkedHashMap<String, String> sortFieldMap;

        public Builder clz(Class<? extends Serializable> val){
            clz = val;
            return this;
        }

        public Builder pageNo(Integer val){
            pageNo = val;
            return this;
        }

        public Builder pageSize(Integer val){
            pageSize = val;
            return this;
        }

        public Builder searchAttr(String val){
            searchAttr = val;
            return this;
        }

        public Builder resultAttr(String val){
            resultAttr = val;
            return this;
        }

        public Builder sortType(String val){
            sortType = val;
            return this;
        }

        public Builder sortField(String val){
            sortField = val;
            return this;
        }

        public Builder sortFieldToMap(String val, String sortType){
            if (StringUtil.isNotEmpty(val) && StringUtil.isNotEmpty(sortType)){
                if (IaisCommonUtils.isEmpty(sortFieldMap)){
                    sortFieldMap = new LinkedHashMap<>(10);
                }
                sortFieldMap.put(val, sortType);
            }
            return this;
        }

        public FilterParameter build() {
            return new FilterParameter(this);
        }
    }

    // use builder create object
    private FilterParameter(Builder builder) {
        pageNo = builder.pageNo;
        pageSize = builder.pageSize;
        clz = builder.clz;
        searchAttr = builder.searchAttr;
        resultAttr = builder.resultAttr;
        sortField  = builder.sortField;
        sortFieldMap  = builder.sortFieldMap;
        sortType = builder.sortType;
    }
}
