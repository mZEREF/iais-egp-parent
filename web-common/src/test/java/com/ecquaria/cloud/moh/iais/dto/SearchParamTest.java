package com.ecquaria.cloud.moh.iais.dto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.mockpolicies.Slf4jMockPolicy;
import org.powermock.core.classloader.annotations.MockPolicy;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import sop.util.Assert;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RunWith(PowerMockRunner.class)
@MockPolicy(Slf4jMockPolicy.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({SearchParam.class})
public class SearchParamTest {
    private static final String PARAMKEY = "paramkey";
    private static final String PARAMVALUE = "paramvalue";
    private static final String FILTERSKEY = "filterskey";
    private static final String FILTERSVALUE = "filtersvalue";
    private static final String SORTE = "sort";
    private static final String SORTEDFILED = "sortfiled";
    private static final int PAGSIZE = 1;
    private static final int PAGENO = 2;

    @Mock
    private HashMap<String, Object> params;
    @Mock
    private LinkedHashMap<String, Object> filters;

    @Test
    public void test(){
        SearchParam sp = new SearchParam(SearchParam.class);
        // test the filter
        sp.addFilter(FILTERSKEY,FILTERSVALUE);
        Map<String, Object> foilters = sp.getFilters();
        Assert.assertEquals(FILTERSVALUE,foilters.get(FILTERSKEY));
        sp.addFilter(FILTERSKEY,FILTERSVALUE,true);
        sp.removeFilter(FILTERSKEY);
        //test the param
        sp.addParam(PARAMKEY,PARAMVALUE);
        Map<String, Object> param = sp.getParams();
        Assert.assertEquals(PARAMVALUE,param.get(PARAMKEY));
        Assert.assertEquals(FILTERSVALUE,param.get(FILTERSKEY));
        sp.removeParam(PARAMKEY);
        sp.removeParam(FILTERSKEY);
        //test the sort
        sp.addSort(SORTE,SearchParam.DESCENDING);
        sp.addSortField(SORTEDFILED);
        Map<String, String> sortMap = sp.getSortMap();
        Assert.assertEquals(sortMap.get(SORTE.toUpperCase()),(SearchParam.DESCENDING));
        Assert.assertEquals(sortMap.get(SORTEDFILED.toUpperCase()),(SearchParam.ASCENDING));
        sp.setSort(SORTE,SearchParam.DESCENDING);
        Map<String, String> sortMap1 = sp.getSortMap();
        Assert.assertEquals(sortMap1.get(SORTE.toUpperCase()),(SearchParam.DESCENDING));
        sp.setSortField(SORTEDFILED);
        Map<String, String> sortMap2 = sp.getSortMap();
        Assert.assertEquals(sortMap2.get(SORTEDFILED.toUpperCase()),(SearchParam.ASCENDING));

        //test test the set get method
         sp.setPageNo(PAGENO);
         Assert.assertEquals(sp.getPageNo(),PAGENO);
         sp.setPageSize(PAGSIZE);
         Assert.assertEquals(sp.getPageSize(),PAGSIZE);
         Assert.assertEquals(SearchParam.class,sp.getEntityCls());
         sp.clear();
         sp.setParams(params);
         sp.setFilters(filters);


    }


}
