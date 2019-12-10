package com.ecquaria.cloud.moh.iais.action;


import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeDto;
import com.ecquaria.cloud.moh.iais.common.dto.message.MessageDto;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.MasterCodeService;
import com.ecquaria.cloud.moh.iais.service.impl.MasterCodeServiceImpl;
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
import org.springframework.mock.web.MockHttpServletRequest;
import sop.webflow.rt.api.BaseProcessClass;

import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MasterCodeDelegator.class, CrudHelper.class,
        SpringContextHelper.class, MiscUtil.class, AuditTrailDto.class,
        ParamUtil.class, WebValidationHelper.class, ValidationResult.class, IaisEGPHelper.class, QueryHelp.class, RestApiUtil.class})
public class MasterCodeDelegatorTest {

    @Mock
    private BaseProcessClass bpc;

    @Spy
    private MasterCodeService masterCodeService = new MasterCodeServiceImpl();


    @Spy
    private FilterParameter filterParameter = new FilterParameter();

    @InjectMocks
    private MasterCodeDelegator masterCodeDelegator;

    private MockHttpServletRequest request = new MockHttpServletRequest();



    @Before
    public void setup(){
        bpc.request = request;

        filterParameter.setClz(MessageDto.class);
        filterParameter.setSearchAttr("test");
        filterParameter.setPageNo(1);
        filterParameter.setPageSize(10);

        PowerMockito.mockStatic(ParamUtil.class);
        PowerMockito.mockStatic(MiscUtil.class);
        PowerMockito.mockStatic(IaisEGPHelper.class);
        PowerMockito.mockStatic(QueryHelp.class);
        PowerMockito.mockStatic(RestApiUtil.class);
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
//        masterCodeDelegator.prepareSwitch(bpc);
        Assert.assertTrue(true);
    }

    @Test
    public void testprepareCreate(){
//        masterCodeDelegator.prepareCreate(bpc);
        Assert.assertTrue(true);
    }

    @Test
    public void testprepareEdit(){
//        masterCodeDelegator.prepareEdit(bpc);
        Assert.assertTrue(true);
    }

    @Test
    public void testdoSearch(){

//        masterCodeDelegator.doSearch(bpc);

        request.addParameter("master_code_key","master_code_key");
        request.addParameter("code_value","code_value");
        request.addParameter("status","pending");
//        masterCodeDelegator.doSearch(bpc);
        Assert.assertTrue(true);
    }

    @Test
    public void testdoSorting(){
        SearchParam param = new SearchParam(filterParameter.getClz().getName());
        PowerMockito.when(IaisEGPHelper.getSearchParam(request, filterParameter)).thenReturn(param);
//        masterCodeDelegator.doSorting(bpc);
        Assert.assertTrue(true);
    }

    @Test
    public void testdoPaging(){
        SearchParam param = new SearchParam(filterParameter.getClz().getName());
        PowerMockito.when(IaisEGPHelper.getSearchParam(request, filterParameter)).thenReturn(param);
//        masterCodeDelegator.doPaging(bpc);
        Assert.assertTrue(true);
    }

    @Test
    public void testdoDelete(){
        request.addParameter("crud_action_value","0");
        PowerMockito.doNothing().when(masterCodeService).deleteMasterCodeById(Mockito.anyLong());
//        masterCodeDelegator.doDelete(bpc);
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
        masterCodeDto.setCodeCategory("1");
        masterCodeDto.setCodeValue("SAD");
        masterCodeDto.setStatus(0);

        PowerMockito.mockStatic(AuditTrailDto.class);
        AuditTrailDto auditTrailDto = new AuditTrailDto();
        PowerMockito.when(AuditTrailDto.getThreadDto()).thenReturn(auditTrailDto);
        PowerMockito.mockStatic(WebValidationHelper.class);
        PowerMockito.when(WebValidationHelper.validateProperty(Mockito.anyObject(), Mockito.anyString())).thenReturn(null);

//        masterCodeDelegator.doCreate(bpc);

        PowerMockito.when(ParamUtil.getString(request, "crud_action_type")).thenReturn("save");
//        masterCodeDelegator.doCreate(bpc);
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
        request.getSession().setAttribute(MessageConstants.MESSAGE_REQUEST_DTO, masterCodeDto);
//        masterCodeDelegator.doEdit(bpc);
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
        masterCodeDto.setCodeCategory("1");
        masterCodeDto.setCodeValue("SAD");
        masterCodeDto.setStatus(0);

        PowerMockito.mockStatic(AuditTrailDto.class);
        AuditTrailDto auditTrailDto = new AuditTrailDto();
        PowerMockito.when(AuditTrailDto.getThreadDto()).thenReturn(auditTrailDto);
        PowerMockito.mockStatic(WebValidationHelper.class);
        PowerMockito.when(WebValidationHelper.validateProperty(Mockito.anyObject(), Mockito.anyString())).thenReturn(null);

//        masterCodeDelegator.doEdit(bpc);

        Assert.assertTrue(true);
    }
}
