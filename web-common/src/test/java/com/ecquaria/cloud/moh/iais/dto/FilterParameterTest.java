package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FilterParameter.class})
public class FilterParameterTest {

    @Spy
    FilterParameter entity = new FilterParameter.Builder().build();

    @Test
    public void testGetterSetter() {
        entity.getPageNo();
        entity.setPageNo(1);
        entity.getPageSize();
        entity.setPageSize(1);
        entity.getClz();
        entity.setClz(null);
        entity.getSearchAttr();
        entity.setSearchAttr(null);
        entity.getResultAttr();
        entity.setResultAttr(null);
        entity.getSortField();
        entity.setSortField(null);
        entity.getSortType();
        entity.setSortType(null);
        entity.getFilters();
        entity.setFilters(null);
        assertNotNull(entity);
    }
}