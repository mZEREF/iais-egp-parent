package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.dao.MasterCodeRepository;
import com.ecquaria.cloud.moh.iais.dto.MasterCodeQuery;
import com.ecquaria.cloud.moh.iais.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.entity.MasterCode;
import com.ecquaria.cloud.moh.iais.querydao.QueryDao;
import com.ecquaria.cloud.moh.iais.service.impl.MasterCodeServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.mockpolicies.Slf4jMockPolicy;
import org.powermock.core.classloader.annotations.MockPolicy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@MockPolicy(Slf4jMockPolicy.class)
@PrepareForTest({MasterCodeService.class})
public class MasterCodeServiceImplTest {
    @InjectMocks
    private MasterCodeServiceImpl masterCodeServiceImpl;
    @Mock
    private MasterCodeRepository masterCodeRepository;

    @Mock
    private QueryDao<MasterCodeQuery> queryQueryDao;

    @Test
    public void testSaveMasterCode(){
        MasterCode masterCode = new MasterCode();
        masterCode.setId(1L);
        masterCodeServiceImpl.saveMasterCode(masterCode);
        Assert.assertTrue(true);
    }

    @Test
    public void testDeleteMasterCodeById(){
        masterCodeServiceImpl.deleteMasterCodeById(1L);
        Assert.assertTrue(true);
    }

    @Test
    public void testfindMasterCodeByRowguid(){
        MasterCode masterCode = new MasterCode();
        Mockito.doReturn(masterCode).when(masterCodeRepository).findMasterCodeByRowguid("ax");
        masterCode = masterCodeServiceImpl.findMasterCodeByRowguid("ax");
        Assert.assertNotNull(masterCode);
    }

    @Test
    public void testDoQuery(){
        SearchResult<MasterCodeQuery> masterCodeQuerySearchResult = new SearchResult<>();
        SearchParam searchParam = new SearchParam(MasterCodeQuery.class);
        Mockito.doReturn(masterCodeQuerySearchResult).when(queryQueryDao).doQuery(searchParam,"","");
        masterCodeQuerySearchResult = masterCodeServiceImpl.doQuery(searchParam,"","");
        Assert.assertNotNull(masterCodeQuerySearchResult);
    }
}
