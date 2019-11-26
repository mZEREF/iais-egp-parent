package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.sql.SqlMap;
import freemarker.template.TemplateException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.mockito.mockpolicies.Slf4jMockPolicy;
import org.powermock.core.classloader.annotations.MockPolicy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * QueryHelpTest
 *
 * @author suocheng
 * @date 8/22/2019
 */
@RunWith(PowerMockRunner.class)
@MockPolicy(Slf4jMockPolicy.class)
@PrepareForTest({QueryHelp.class,SqlMap.class})
public class QueryHelpTest {
    @Mock
    private SqlMap sqlMap;

    @Test(expected = IllegalStateException.class)
    public void testConstructor() throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        Class cls = QueryHelp.class;
        Constructor<QueryHelp> con = cls.getDeclaredConstructor(null);
        con.setAccessible(true);
        con.newInstance(null);
    }

    @Test(expected = IaisRuntimeException.class)
    public void testsetMainSqlException() throws IOException, TemplateException {
        SearchParam searchParam = new SearchParam("");
        Whitebox.setInternalState(SqlMap.class,"INSTANCE",sqlMap);
        PowerMockito.when(sqlMap.getSql(Mockito.anyString(),Mockito.anyString(),Mockito.anyMap())).thenThrow(new IOException());
        QueryHelp.setMainSql("catalog","key",searchParam);
    }

    @Test
    public void testsetMainSql() throws IOException, TemplateException {
        SearchParam searchParam = new SearchParam("");
        Whitebox.setInternalState(SqlMap.class,"INSTANCE",sqlMap);
        PowerMockito.when(sqlMap.getSql(Mockito.anyString(),Mockito.anyString(),Mockito.anyMap())).thenReturn("");
        QueryHelp.setMainSql("catalog","key",searchParam);
        Assert.assertTrue(true);
    }
}
