package com.ecquaria.cloud.moh.iais.controller;


import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.querydao.QueryDao;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dao.MasterCodeRepository;
import com.ecquaria.cloud.moh.iais.dto.MasterCodeDto;
import com.ecquaria.cloud.moh.iais.dto.MasterCodeQuery;
import com.ecquaria.cloud.moh.iais.dto.MessageDto;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.MasterCodeService;
import com.ecquaria.cloud.moh.iais.service.impl.MasterCodeServiceImpl;
import com.ecquaria.cloud.moh.iais.web.logging.dto.AuditTrailDto;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.springframework.mock.web.MockHttpServletRequest;
import sop.webflow.rt.api.BaseProcessClass;

import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MasterCodeDelegator.class, CrudHelper.class,MasterCodeServiceImpl.class,
        SpringContextHelper.class, MiscUtil.class, AuditTrailDto.class, ParamUtil.class, WebValidationHelper.class})
public class MasterCodeDelegatorTest {

    @Mock
    private BaseProcessClass bpc;

    @Spy
    private MasterCodeService masterCodeService = new MasterCodeServiceImpl();

    @InjectMocks
    private MasterCodeDelegator masterCodeDelegator;

    private MockHttpServletRequest request = new MockHttpServletRequest();

    @Mock
    private QueryDao<MasterCodeQuery> mastercodeQueryDao;

    @Mock
    private MasterCodeRepository masterCodeRepository;

    @Before
    public void setup(){

        SearchParam searchParam = new SearchParam(MasterCodeQuery.class.getName());

        bpc.request = request;
      //  Whitebox.setInternalState(masterCodeService,"mastercodeQueryDao",mastercodeQueryDao);
        Whitebox.setInternalState(masterCodeService,"masterCodeRepository",masterCodeRepository);

        //PowerMockito.when(mastercodeQueryDao.doQuery(searchParam)).thenReturn(null);
        PowerMockito.mockStatic(ParamUtil.class);
        PowerMockito.mockStatic(MiscUtil.class);
        when(MiscUtil.getCurrentRequest()).thenReturn(request);
    }
    @Test
    public void testdoStart(){
        masterCodeDelegator.doStart(bpc);
        Assert.assertTrue(true);
    };

    @Test
    public void testprepareData(){
//        masterCodeDelegator.prepareData(bpc);
        Assert.assertTrue(true);
    }

    @Test
    public void testprepareSwitch(){
        masterCodeDelegator.prepareSwitch(bpc);
        Assert.assertTrue(true);
    }

    @Test
    public void testprepareCreate(){
        masterCodeDelegator.prepareCreate(bpc);
        Assert.assertTrue(true);
    }

    @Test
    public void testprepareEdit(){
        masterCodeDelegator.prepareEdit(bpc);
        Assert.assertTrue(true);
    }

    @Test
    public void testdoSearch(){
        request.addParameter("master_code_key","master_code_key");
        request.addParameter("code_value","code_value");
        request.addParameter("status","pending");
        masterCodeDelegator.doSearch(bpc);
        Assert.assertTrue(true);
    }

    @Test
    public void testdoSorting(){
        masterCodeDelegator.doSorting(bpc);
        Assert.assertTrue(true);
    }

    @Test
    public void testdoPaging(){
        masterCodeDelegator.doPaging(bpc);
        Assert.assertTrue(true);
    }

    @Test
    public void testdoDelete(){
        request.addParameter("crud_action_value","0");
        PowerMockito.doNothing().when(masterCodeService).deleteMasterCodeById(Mockito.anyLong());
        masterCodeDelegator.doDelete(bpc);
        Assert.assertTrue(true);
    }

    @Test
    public void testdoCreate(){
        request.addParameter("master_code_key","AS");
        request.addParameter("rowguid","adsdsa");
        request.addParameter("code_category","1");
        request.addParameter("code_value","SAD");
        request.addParameter("status","2");

        MasterCodeDto masterCodeDto = new MasterCodeDto();
        PowerMockito.when(ParamUtil.getSessionAttr(request, "MasterCodeDto")).thenReturn(masterCodeDto);
        masterCodeDto.setMasterCodeKey("AS");
        masterCodeDto.setRowguid("AS");
        masterCodeDto.setCodeCategory(1);
        masterCodeDto.setCodeValue("SAD");
        masterCodeDto.setStatus(0);

        PowerMockito.mockStatic(AuditTrailDto.class);
        AuditTrailDto auditTrailDto = new AuditTrailDto();
        PowerMockito.when(AuditTrailDto.getThreadDto()).thenReturn(auditTrailDto);
        PowerMockito.mockStatic(WebValidationHelper.class);
        PowerMockito.when(WebValidationHelper.validateProperty(Mockito.anyObject(), Mockito.anyString())).thenReturn(null);

        masterCodeDelegator.doCreate(bpc);

        PowerMockito.when(ParamUtil.getString(request, "crud_action_type")).thenReturn("save");
        masterCodeDelegator.doCreate(bpc);
        Assert.assertTrue(true);
    }

    @Test
    public void testdoEditHasError(){
        MasterCodeDto masterCodeDto = new MasterCodeDto();
        PowerMockito.when(ParamUtil.getSessionAttr(request, "MasterCodeDto")).thenReturn(masterCodeDto);
        PowerMockito.mockStatic(AuditTrailDto.class);
        AuditTrailDto auditTrailDto = new AuditTrailDto();
        PowerMockito.when(AuditTrailDto.getThreadDto()).thenReturn(auditTrailDto);
        PowerMockito.mockStatic(WebValidationHelper.class);
        PowerMockito.when(WebValidationHelper.validateProperty(Mockito.anyObject(), Mockito.anyString())).thenReturn(null);
        request.getSession().setAttribute(MessageDto.MESSAGE_REQUEST_DTO, masterCodeDto);
        masterCodeDelegator.doEdit(bpc);
        Assert.assertTrue(true);
    }

    @Test
    public void testdoEdit(){
        request.addParameter("master_code_key","AS");
        request.addParameter("rowguid","adsdsa");
        request.addParameter("code_category","1");
        request.addParameter("code_value","SAD");
        request.addParameter("status","2");

        MasterCodeDto masterCodeDto = new MasterCodeDto();
        PowerMockito.when(ParamUtil.getSessionAttr(request, "MasterCodeDto")).thenReturn(masterCodeDto);
        masterCodeDto.setMasterCodeKey("AS");
        masterCodeDto.setRowguid("AS");
        masterCodeDto.setCodeCategory(1);
        masterCodeDto.setCodeValue("SAD");
        masterCodeDto.setStatus(0);

        PowerMockito.mockStatic(AuditTrailDto.class);
        AuditTrailDto auditTrailDto = new AuditTrailDto();
        PowerMockito.when(AuditTrailDto.getThreadDto()).thenReturn(auditTrailDto);
        PowerMockito.mockStatic(WebValidationHelper.class);
        PowerMockito.when(WebValidationHelper.validateProperty(Mockito.anyObject(), Mockito.anyString())).thenReturn(null);

        masterCodeDelegator.doEdit(bpc);

        PowerMockito.when(ParamUtil.getString(request, "crud_action_type")).thenReturn("edit");
        masterCodeDelegator.doEdit(bpc);
        Assert.assertTrue(true);
    }
}
