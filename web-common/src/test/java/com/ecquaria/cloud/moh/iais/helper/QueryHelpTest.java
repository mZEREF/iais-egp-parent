package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.sql.SqlMap;
import freemarker.template.TemplateException;
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

    @Test(expected = IaisRuntimeException.class)
    public void testsetMainSqlException(){
        SearchParam searchParam = new SearchParam();
        QueryHelp.setMainSql("catalog","key",searchParam);
    }

    @Test
    public void testsetMainSql() throws IOException, TemplateException {
        SearchParam searchParam = new SearchParam();
        Whitebox.setInternalState(SqlMap.class,"INSTANCE",sqlMap);
        PowerMockito.when(sqlMap.getSql(Mockito.anyString(),Mockito.anyString(),Mockito.anyMap())).thenReturn("");
        QueryHelp.setMainSql("catalog","key",searchParam);
    }
}
