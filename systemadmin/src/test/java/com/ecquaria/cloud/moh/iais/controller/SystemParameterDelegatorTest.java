package com.ecquaria.cloud.moh.iais.controller;

/*
 *author: yichen
 *date time:9/9/2019 12:34 PM
 *description:
 */


import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.dto.MessageDto;
import com.ecquaria.cloud.moh.iais.dto.SystemParameterDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.SystemParameterService;
import com.ecquaria.cloud.moh.iais.service.impl.SystemParameterServiceImpl;
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
@PrepareForTest({QueryHelp.class, ParamUtil.class,RestApiUtil.class, MiscUtil.class, IaisEGPHelper.class,
        WebValidationHelper.class})
public class SystemParameterDelegatorTest {
    @InjectMocks
    private SystemParameterDelegator delegator;

    @Spy
    private SystemParameterService service = new SystemParameterServiceImpl();

    @Mock
    private BaseProcessClass bpc;

    private MockHttpServletRequest request = new MockHttpServletRequest();

    private SystemParameterDto dto = new SystemParameterDto();

    @Spy
    private FilterParameter filterParameter = new FilterParameter();

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
    public void testStartStep(){
        try {
            delegator.startStep(bpc);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadData(){
        delegator.loadData(bpc);
        Assert.assertTrue(true);
    }

    @Test
    public void testDisableStatus(){
        String rowguId = "837BC1CA-245A-4709-BF83-E437E5B0224E";
        PowerMockito.when(ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_VALUE)).thenReturn(rowguId);
        PowerMockito.when(service.getParameterByRowguid(rowguId)).thenReturn(dto);
        delegator.disableStatus(bpc);
        Assert.assertTrue(true);
    }

    @Test
    public void testDisableStatus2(){
        PowerMockito.when(ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_VALUE)).thenReturn("");
        delegator.disableStatus(bpc);
        Assert.assertTrue(true);
    }

    @Test
    public void testPrepareSwitch(){
        PowerMockito.when(ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE)).thenReturn("test");
        delegator.prepareSwitch(bpc);
        Assert.assertTrue(true);
    }

    @Test
    public void testPrepareEdit(){
        PowerMockito.when(ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_VALUE)).thenReturn("11");
        delegator.prepareEdit(bpc);
    }

    @Test
    public void testSortRecords(){
        SearchParam param = new SearchParam(filterParameter.getClz().getName());
        PowerMockito.when(IaisEGPHelper.getSearchParam(request, filterParameter)).thenReturn(param);
        delegator.sortRecords(bpc);
        Assert.assertTrue(true);
    }
    @Test
    public void testDoPaging() {
        delegator.changePage(bpc);
        Assert.assertTrue(true);
    }

    @Test
    public void testDoQueryToSuccess(){
        PowerMockito.when(ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE)).thenReturn("doQuery");
        SearchParam param = new SearchParam(filterParameter.getClz().getName());
        PowerMockito.when(IaisEGPHelper.getSearchParam(request, true, filterParameter)).thenReturn(param);

        PowerMockito.when(ParamUtil.getString(request, "module")).thenReturn("New");
        PowerMockito.when(ParamUtil.getString(request, "status")).thenReturn("Y");
        PowerMockito.mockStatic(WebValidationHelper.class);
        ValidationResult validationResult = null;
        PowerMockito.when(WebValidationHelper.validateProperty(Mockito.anyObject(), Mockito.anyString())).thenReturn(validationResult);
        delegator.doQuery(bpc);
        Assert.assertTrue(true);
    }

    @Test
    public void testDoQueryToError(){
        PowerMockito.when(ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE)).thenReturn("errortest");
        delegator.doQuery(bpc);


        PowerMockito.mockStatic(WebValidationHelper.class);
        ValidationResult validationResult = new ValidationResult();
        validationResult.setHasErrors(true);

        PowerMockito.when(ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE)).thenReturn("doQuery");
        PowerMockito.when(ParamUtil.getString(request, "domainType")).thenReturn(null);
        PowerMockito.when(WebValidationHelper.validateProperty(Mockito.anyObject(), Mockito.anyString())).thenReturn(validationResult);
        delegator.doQuery(bpc);
        Assert.assertTrue(true);
    }

    @Test
    public void testDoEdit(){
        PowerMockito.when(ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE)).thenReturn("doEdit");
        PowerMockito.when(ParamUtil.getString(request, "domainType")).thenReturn("Common");
        PowerMockito.when(ParamUtil.getString(request, "status")).thenReturn("Y");
        PowerMockito.when(ParamUtil.getString(request, "module")).thenReturn("New");
        PowerMockito.when(ParamUtil.getString(request, "description")).thenReturn("testsetst");
        SystemParameterDto dto = new SystemParameterDto();
        PowerMockito.when(ParamUtil.getSessionAttr(request, "parameterRequestDto")).thenReturn(dto);
        delegator.doEdit(bpc);

        // error
        PowerMockito.when(ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE)).thenReturn("doEdit");
        PowerMockito.mockStatic(WebValidationHelper.class);
        ValidationResult validationResult = new ValidationResult();
        validationResult.setHasErrors(true);
        PowerMockito.when(ParamUtil.getString(request, "domainType")).thenReturn(null);
        PowerMockito.when(WebValidationHelper.validateProperty(Mockito.anyObject(), Mockito.anyString())).thenReturn(validationResult);



        delegator.doEdit(bpc);
    }


    @Test
    public void testDoEditToError(){
        PowerMockito.when(ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE)).thenReturn("err");
        delegator.doEdit(bpc);

        PowerMockito.when(ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE)).thenReturn("cancel");
        delegator.doEdit(bpc);
    }
}
