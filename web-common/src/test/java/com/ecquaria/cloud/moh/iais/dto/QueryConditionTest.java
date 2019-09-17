package com.ecquaria.cloud.moh.iais.dto;

/*
 *author: yichen
 *date time:9/6/2019 2:06 PM
 *description:
 */

import org.junit.Test;
import org.mockito.Spy;

import static org.junit.Assert.assertNotNull;

public class QueryConditionTest {
    @Spy
    QueryCondition queryCondition = new QueryCondition();

    @Test
    public void testGetterSetter() {
        queryCondition.getClz();
        queryCondition.getPageNo();
        queryCondition.getPageSize();
        queryCondition.getResultAttr();
        queryCondition.getSearchAttr();
        queryCondition.getSortField();

        queryCondition.setClz(null);
        queryCondition.setPageNo(0);
        queryCondition.setPageSize(0);
        queryCondition.setResultAttr(null);
        queryCondition.setSearchAttr(null);
        queryCondition.setSortField(null);
        assertNotNull(queryCondition);
    }
}
