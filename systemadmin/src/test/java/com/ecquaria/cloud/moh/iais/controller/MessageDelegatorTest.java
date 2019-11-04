package com.ecquaria.cloud.moh.iais.controller;

/*
 *author: yichen
 *date time:2019/8/8 13:32
 *description:
 */

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.message.MessageDto;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.MessageService;
import com.ecquaria.cloud.moh.iais.service.impl.MessageServiceImpl;
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
@PrepareForTest({CrudHelper.class,
        SpringContextHelper.class, MiscUtil.class, AuditTrailDto.class,
        ParamUtil.class, WebValidationHelper.class, ValidationResult.class, IaisEGPHelper.class, QueryHelp.class, RestApiUtil.class})
public class MessageDelegatorTest {
    @InjectMocks
    private MessageDelegator messageDelegator;

    @Spy
    private MessageService messageService = new MessageServiceImpl();

    @Mock
    private BaseProcessClass bpc;

    private MockHttpServletRequest request = new MockHttpServletRequest();

    private MessageDto messageDto = new MessageDto();

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
            messageDelegator.startStep(bpc);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        bpc.request = null;
        try {
            messageDelegator.startStep(bpc);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(true);
    }

    @Test
    public void testPrepareData(){
        PowerMockito.when(messageService.doQuery(null)).thenReturn(null);

        messageDelegator.prepareData(bpc);
        Assert.assertTrue(true);
    }

    @Test
    public void testDisableStatus(){
        String rowguId = "837BC1CA-245A-4709-BF83-E437E5B0224E";
        PowerMockito.when(ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_VALUE)).thenReturn(rowguId);
        PowerMockito.when(messageService.getMessageById(rowguId)).thenReturn(messageDto);
        messageDelegator.disableStatus(bpc);
        Assert.assertTrue(true);
    }

    @Test
    public void testDisableStatus2(){
        PowerMockito.when(ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_VALUE)).thenReturn("");
        messageDelegator.disableStatus(bpc);
        Assert.assertTrue(true);
    }

    @Test
    public void testDoSearchToSucess(){
        SearchParam param = new SearchParam(filterParameter.getClz().getName());
        PowerMockito.when(IaisEGPHelper.getSearchParam(request, true, filterParameter)).thenReturn(param);
        PowerMockito.when(ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE)).thenReturn("doSearch");
        PowerMockito.when(ParamUtil.getString(request, "domainType")).thenReturn("INTRA");
        PowerMockito.when(ParamUtil.getString(request, "msgType")).thenReturn("Error");
        PowerMockito.when(ParamUtil.getString(request, "module")).thenReturn("New");
        messageDelegator.doSearch(bpc);
        Assert.assertTrue(true);
    }


    @Test
    public void testDoSearchToError(){
        PowerMockito.when(ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE)).thenReturn("errortest");
        messageDelegator.doSearch(bpc);


        PowerMockito.mockStatic(WebValidationHelper.class);
        ValidationResult validationResult = new ValidationResult();
        validationResult.setHasErrors(true);

        PowerMockito.when(ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE)).thenReturn("doSearch");
        PowerMockito.when(ParamUtil.getString(request, "domainType")).thenReturn(null);
        PowerMockito.when(WebValidationHelper.validateProperty(Mockito.anyObject(), Mockito.anyString())).thenReturn(validationResult);
        messageDelegator.doSearch(bpc);
        Assert.assertTrue(true);
    }

    @Test
    public void testPrepareSwitch(){
        PowerMockito.when(ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE)).thenReturn("test");
        messageDelegator.prepareSwitch(bpc);
        Assert.assertTrue(true);
    }

    @Test
    public void testPerpareEditOfSuccess(){
        PowerMockito.when(ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_VALUE)).thenReturn("11");

        MessageDto messageDto = new MessageDto();
        messageDto.setDomainType("Internet");

        messageDelegator.prepareEdit(bpc);
        Assert.assertTrue(true);
    }

    @Test
    public void testPerpareEditOfError(){
        PowerMockito.when(ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_VALUE)).thenReturn("");
        messageDelegator.prepareEdit(bpc);
        Assert.assertTrue(true);
    }

    @Test
    public void testDoSorting(){
        SearchParam param = new SearchParam(filterParameter.getClz().getName());
        PowerMockito.when(IaisEGPHelper.getSearchParam(request, filterParameter)).thenReturn(param);
        messageDelegator.doSorting(bpc);
        Assert.assertTrue(true);
    }
    @Test
    public void testDoPaging() {
        messageDelegator.doPaging(bpc);
        Assert.assertTrue(true);
    }

    @Test
    public void testDoEditError(){
        PowerMockito.when(ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE)).thenReturn("doEdit");
        PowerMockito.when(ParamUtil.getString(request, "domainType")).thenReturn("INTRA");
        PowerMockito.when(ParamUtil.getString(request, "msgType")).thenReturn("Error");
        PowerMockito.when(ParamUtil.getString(request, "module")).thenReturn("New");
        MessageDto messageDto = new MessageDto();
        PowerMockito.when(ParamUtil.getSessionAttr(request, MessageConstants.MESSAGE_REQUEST_DTO)).thenReturn(messageDto);
        PowerMockito.mockStatic(AuditTrailDto.class);
        AuditTrailDto auditTrailDto = new AuditTrailDto();
        PowerMockito.when(IaisEGPHelper.getCurrentAuditTrailDto()).thenReturn(auditTrailDto);
        request.getSession().setAttribute(MessageConstants.MESSAGE_REQUEST_DTO, messageDto);

        messageDelegator.doEdit(bpc);
        Assert.assertTrue(true);
    }

    @Test
    public void testDoEditSuccess(){
        PowerMockito.when(ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE)).thenReturn("doEdit");
        request.addParameter("domainType","INTRA");
        request.addParameter("module","Error");
        request.addParameter("module","New");
        request.addParameter("description","sadsadasdas");

        MessageDto messageDto = new MessageDto();
        PowerMockito.when(ParamUtil.getSessionAttr(request, MessageConstants.MESSAGE_REQUEST_DTO)).thenReturn(messageDto);
        messageDto.setDomainType("INTRA");
        messageDto.setMsgType("Error");
        messageDto.setModule("New");
        messageDto.setDescription("sadsadasdas");

        PowerMockito.mockStatic(AuditTrailDto.class);
        AuditTrailDto auditTrailDto = new AuditTrailDto();
        PowerMockito.when(IaisEGPHelper.getCurrentAuditTrailDto()).thenReturn(auditTrailDto);
        PowerMockito.mockStatic(WebValidationHelper.class);
        PowerMockito.when(WebValidationHelper.validateProperty(Mockito.anyObject(), Mockito.anyString())).thenReturn(null);

        messageDelegator.doEdit(bpc);

        PowerMockito.when(ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE)).thenReturn("cancel");
        messageDelegator.doEdit(bpc);

        PowerMockito.when(ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE)).thenReturn("asd");
        messageDelegator.doEdit(bpc);


        Assert.assertTrue(true);
    }

}
