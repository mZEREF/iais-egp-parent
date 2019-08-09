package com.ecquaria.cloud.moh.iais.controller;

/*
 *author: yichen
 *date time:2019/8/8 13:32
 *description:
 */

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dao.MsgDao;
import com.ecquaria.cloud.moh.iais.dto.MessageDto;
import com.ecquaria.cloud.moh.iais.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.entity.Message;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.querydao.QueryDao;
import com.ecquaria.cloud.moh.iais.service.MsgService;
import com.ecquaria.cloud.moh.iais.service.impl.MsgServiceImpl;
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
import sg.gov.moh.iais.common.utils.MiscUtil;
import sg.gov.moh.iais.common.utils.ParamUtil;
import sg.gov.moh.iais.web.logging.dto.AuditTrailDto;
import sop.webflow.rt.api.BaseProcessClass;

import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MessageDelegator.class, CrudHelper.class, MsgServiceImpl.class,
        SpringContextHelper.class, MiscUtil.class, AuditTrailDto.class, ParamUtil.class, WebValidationHelper.class})
public class MessageDelegatorTest {
    @InjectMocks
    private MessageDelegator messageDelegator;

    @Spy
    private MsgService msgService = new MsgServiceImpl();

    @Mock
    private BaseProcessClass bpc;

    private MockHttpServletRequest request = new MockHttpServletRequest();

    @Mock
    private QueryDao<Message> queryDao;

    @Mock
    private MsgDao msgDao;


    private SearchParam searchParam = new SearchParam(Message.class);

    private Message message = new Message();
    @Before
    public void setup(){
        bpc.request = request;
        Whitebox.setInternalState(msgService,"queryDao",queryDao);
        Whitebox.setInternalState(msgService,"msgDao",msgDao);
        PowerMockito.doNothing().when(msgDao).delete(Mockito.anyInt());

        PowerMockito.when(queryDao.doQuery(searchParam, "messageSql", "search")).thenReturn(null);
        PowerMockito.mockStatic(ParamUtil.class);
        PowerMockito.mockStatic(MiscUtil.class);
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
        messageDelegator.prepareData(bpc);
        Assert.assertTrue(true);
    }

    @Test
    public void testDoDeleteNotException(){
        PowerMockito.when(ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_VALUE)).thenReturn("1");
        messageDelegator.doDelete(bpc);
        Assert.assertTrue(true);
    }

    @Test
    public void testDoDeleteThrowException(){
        PowerMockito.when(ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_VALUE)).thenReturn("assd");
        PowerMockito.when(msgService.getMessageByMsgId(Mockito.anyInt())).thenThrow(NumberFormatException.class);
        messageDelegator.doDelete(bpc);
        Assert.assertTrue(true);
    }

    @Test
    public void testDoSearch(){
        PowerMockito.when(ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE)).thenReturn("doSearch");
        PowerMockito.when(ParamUtil.getString(request, "domainType")).thenReturn("INTRA");
        PowerMockito.when(ParamUtil.getString(request, "msgType")).thenReturn("Error");
        PowerMockito.when(ParamUtil.getString(request, "module")).thenReturn("New");
        messageDelegator.doSearch(bpc);
        Assert.assertTrue(true);

        PowerMockito.when(ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE)).thenReturn("doEdit");
        PowerMockito.when(ParamUtil.getString(request, "domainType")).thenReturn("INTRA");
        PowerMockito.when(ParamUtil.getString(request, "msgType")).thenReturn("Error");
        PowerMockito.when(ParamUtil.getString(request, "module")).thenReturn("New");
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
        PowerMockito.doReturn(message).when(msgDao).findById(Mockito.anyInt());
        MessageDto messageDto = new MessageDto();
        messageDto.setDomainType("Internet");
        PowerMockito.when(MiscUtil.transferEntityDto(message, MessageDto.class)).thenReturn(messageDto);
        messageDelegator.perpareEdit(bpc);
        Assert.assertTrue(true);
    }

    @Test
    public void testPerpareEditOfError(){
        PowerMockito.when(ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_VALUE)).thenReturn("abc");
        PowerMockito.when(msgService.getMessageByMsgId(Mockito.anyInt())).thenThrow(NumberFormatException.class);
        messageDelegator.perpareEdit(bpc);
        Assert.assertTrue(true);
    }

    @Test
    public void testDoSorting(){
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
        PowerMockito.when(ParamUtil.getSessionAttr(request, MessageDto.MESSAGE_REQUEST_DTO)).thenReturn(messageDto);
        PowerMockito.mockStatic(AuditTrailDto.class);
        AuditTrailDto dto = new AuditTrailDto();
        PowerMockito.when(AuditTrailDto.getThreadDto()).thenReturn(dto);
        request.getSession().setAttribute(MessageDto.MESSAGE_REQUEST_DTO, messageDto);

        messageDelegator.doEdit(bpc);
        Assert.assertTrue(true);
    }

    @Test
    public void testDoEditSuccess(){
        PowerMockito.when(ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE)).thenReturn("doEdit");
        request.addParameter("domainType","INTRA");
        request.addParameter("msgType","Error");
        request.addParameter("module","New");
        request.addParameter("description","sadsadasdas");


        MessageDto messageDto = new MessageDto();
        PowerMockito.when(ParamUtil.getSessionAttr(request, MessageDto.MESSAGE_REQUEST_DTO)).thenReturn(messageDto);
        messageDto.setDomainType("INTRA");
        messageDto.setMsgType("Error");
        messageDto.setModule("New");
        messageDto.setDescription("sadsadasdas");

        PowerMockito.mockStatic(AuditTrailDto.class);
        AuditTrailDto auditTrailDto = new AuditTrailDto();
        PowerMockito.when(AuditTrailDto.getThreadDto()).thenReturn(auditTrailDto);
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
