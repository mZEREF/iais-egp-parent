package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.dao.MasterCodeRepository;
import com.ecquaria.cloud.moh.iais.service.impl.MasterCodeServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.mockpolicies.Slf4jMockPolicy;
import org.powermock.core.classloader.annotations.MockPolicy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@MockPolicy(Slf4jMockPolicy.class)
@PrepareForTest({MasterCodeServiceImpl.class})
public class MasterCodeServiceImplTest {
    @InjectMocks
    private MasterCodeServiceImpl masterCodeServiceImpl;
    @Mock
    private MasterCodeRepository masterCodeRepository;;

    @Test
    public void test(){

    }
}
