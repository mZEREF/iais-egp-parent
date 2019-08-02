package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.dao.PostCodeDao;
import com.ecquaria.cloud.moh.iais.dto.PostCodeDto;
import com.ecquaria.cloud.moh.iais.entity.PostCode;
import com.ecquaria.cloud.moh.iais.service.impl.PostCodeServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.mockpolicies.Slf4jMockPolicy;
import org.powermock.core.classloader.annotations.MockPolicy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import sop.util.Assert;

import java.util.List;

@RunWith(PowerMockRunner.class)
@MockPolicy(Slf4jMockPolicy.class)
@PrepareForTest({PostCodeServiceImpl.class})
public class PostCodeServiceImplTest {
    private static final String POSTCODE = "019191";
    @InjectMocks
    private PostCodeServiceImpl postCodeServiceImpl;

    @Mock
    private PostCodeDao postCodeDao;

    @Mock
    private List list;

    @Before
    public void setup(){
        PostCode postCode = new PostCode();
        postCode.setPostalCode(POSTCODE);
        Mockito.doReturn(postCode).when(postCodeDao).getPostCodeByCode(POSTCODE);
        Mockito.doReturn(postCode).when(postCodeDao).saveAndFlush(postCode);
        Mockito.doNothing().when(postCodeDao).deleteAll();
        Mockito.doReturn(list).when(postCodeDao).save(list);
    }

    //getPostCodeByCode
    @Test
    public void testgetPostCodeByCode(){
        PostCode pc = postCodeServiceImpl.getPostCodeByCode(POSTCODE);
        Assert.assertEquals(pc.getPostalCode(),POSTCODE);
    }

    //savePostCode
    @Test
    public void testsavePostCode(){
        PostCodeDto postCodet = new PostCodeDto();
        postCodet.setPostalCode(POSTCODE);
        String  cpde = postCodeServiceImpl.savePostCode(postCodet);
        Assert.assertEquals(cpde,POSTCODE);
    }

    //clean
    @Test
    public void testclean(){
        postCodeServiceImpl.clean();
    }
    //createAll
    @Test
    public void testcreateAll(){
        postCodeServiceImpl.createAll(list);
    }

}
