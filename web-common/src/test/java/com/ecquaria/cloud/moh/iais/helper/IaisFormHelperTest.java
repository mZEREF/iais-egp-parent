/*
 *   This file is generated by ECQ project skeleton automatically.
 *
 *   Copyright 2019-2049, Ecquaria Technologies Pte Ltd. All rights reserved.
 *
 *   No part of this material may be copied, reproduced, transmitted,
 *   stored in a retrieval system, reverse engineered, decompiled,
 *   disassembled, localised, ported, adapted, varied, modified, reused,
 *   customised or translated into any language in any form or by any means,
 *   electronic, mechanical, photocopying, recording or otherwise,
 *   without the prior written permission of Ecquaria Technologies Pte Ltd.
 */

package com.ecquaria.cloud.moh.iais.helper;


import com.ecquaria.cloud.ServerConfig;
import com.ecquaria.cloud.helper.EngineHelper;
import com.ecquaria.egov.core.agency.Agency;
import com.ecquaria.egov.core.agency.AgencyService;
import com.ecquaria.egov.core.common.constants.AppConstants;
import com.ecquaria.egov.core.svcreg.ServiceRegistry;
import com.ecquaria.egov.mc.service.JsonLabel;
import com.ecquaria.egp.api.*;
import com.ecquaria.egp.core.application.Application;
import com.ecquaria.egp.core.bat.AppStatusHelper;
import com.ecquaria.egp.core.bat.FormHelper;
import com.ecquaria.egp.core.forms.instance.FormInstance;
import com.ecquaria.egp.core.helper.ServiceRegistryHelper;
import ecq.commons.exception.BaseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.mockito.mockpolicies.Slf4jMockPolicy;
import org.powermock.core.classloader.annotations.MockPolicy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import sg.gov.moh.iais.common.exception.IaisRuntimeException;
import sop.config.ConfigUtil;
import sop.i18n.MultiLangUtil;
import sop.webflow.eservice.EGPCase;
import sop.webflow.eservice.EGPCaseData;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * IaisFormHelperTest
 *
 * @author suocheng
 * @date 7/19/2019
 */
@RunWith(PowerMockRunner.class)
@MockPolicy(Slf4jMockPolicy.class)
@PrepareForTest({IaisFormHelper.class,ServiceRegistryHelper.class,AgencyService.class,
        AppStatusHelper.class,EGPCaseHelper.class,ServerConfig.class,ConfigUtil.class,
        EGPHelper.class,MultiLangUtil.class,MessageCenterHelper.class,EngineHelper.class,FormHelper.class})
public class IaisFormHelperTest {
    private static final String projectName ="projectName";
    private static final String processName =  "processName";
    private static final String callStepName = "callStepName";
    private static final String formName = "formName";
    @Mock
    private BaseProcessClass bpc;

    @Mock
    private HttpServletRequest request;

    @Mock
    private EGPCase egpCase;

    @Mock
    private EGPCaseData egpCaseData;

    @Mock
    private ServiceRegistry serviceRegistry;

    @Mock
    private AgencyService agencyService;

    @Mock
    private Agency agency;

    @Mock
    private AppStatusHelper appStatusHelper;

    @Mock
    private AppStatus appStatus;

    @Mock
    private Submitter submitter;

    @Mock
    private Applicant applicant;

    @Mock
    private EGPCaseData submitEgpCaseData;

    @Mock
    private EGPCaseData applicationEgpCaseData;

    @Mock
    private ServerConfig serverConfig;

    @Mock
    private Application applicaiton;

    @Mock
    private MessageCenterHelper.ResponseInfo resInfo;

    @Mock
    private FormInstance formIns;

    @Before
    public void setup(){
        bpc.currentCase=egpCase;
        bpc.request=request;
        PowerMockito.mockStatic(ServiceRegistryHelper.class);
        PowerMockito.mockStatic(AgencyService.class);
        PowerMockito.mockStatic(AppStatusHelper.class);
        //for IaisEGPCaseHelper.saveEGPCase(bpc.currentCase);
        PowerMockito.mockStatic(EGPCaseHelper.class);
        //for getFormDetailUrl()
        PowerMockito.mockStatic(ServerConfig.class);
        PowerMockito.mockStatic(ConfigUtil.class);
        // for IaisEGPHelper.getTinyUrl(callback.toString());
        PowerMockito.mockStatic(EGPHelper.class);
        //MultiLangUtil.translate
        PowerMockito.mockStatic(MultiLangUtil.class);
        //
        PowerMockito.mockStatic(MessageCenterHelper.class);
        //EngineHelper.getContextPath()
        PowerMockito.mockStatic(EngineHelper.class);
        PowerMockito.mockStatic(FormHelper.class);
    }
    //test for the draft No is not Empty
    @Test
    public void testdoSaveDraft() throws BaseException {
        //for the getApplicationDraftNo()
        Mockito.doReturn(egpCaseData).when(egpCase).getCaseData(IaisFormHelper.ATTR_APP_DRAFT_NO);
        Mockito.doReturn("ApplicationNo").when(egpCaseData).getValue();
        // for the bindApplication();
          //for the loadServiceInfo
        Mockito.doReturn(egpCaseData).when(egpCase).getCaseData(EGPConstants.ATTR_SERVICE_INFO);
        PowerMockito.when(IaisServiceRegistryHelper.getServiceRegistryFromCase(egpCase)).thenReturn(serviceRegistry);
        // for   AgencyService.getInstance().retrieveByShortName
        PowerMockito.when(AgencyService.getInstance()).thenReturn(agencyService);
        Mockito.doReturn(agency).when(agencyService).retrieveByShortName(null);
         // for AppStatusHelper.getInstance().getStartStatus().getCode()
        PowerMockito.when(AppStatusHelper.getInstance()).thenReturn(appStatusHelper);
        appStatus.setCode("110");
        Mockito.doReturn(appStatus).when(appStatusHelper).getStartStatus();
          //for bpc.currentCase.getCaseData(EGPConstants.SUBMITTER);
        Mockito.doReturn(submitEgpCaseData).when(egpCase).getCaseData(EGPConstants.SUBMITTER);
        Mockito.doReturn(submitter).when(submitEgpCaseData).getValue();
          //for  bpc.currentCase.getCaseData(EGPConstants.APPLICANT);
        Mockito.doReturn(applicationEgpCaseData).when(egpCase).getCaseData(EGPConstants.APPLICANT);
        Mockito.doReturn(applicant).when(applicationEgpCaseData).getValue();
         //method getFormDetailUrl()
        PowerMockito.when(ServerConfig.getInstance()).thenReturn(serverConfig);
        String frontednURL = "https://egpcloudfe/";
        Mockito.doReturn("").when(serverConfig).getFrontendURL();
        String formDetailURL = "cr/process/EGPCLOUD/FormDetails_MC";
        PowerMockito.when(ConfigUtil.getString(AppConstants.CONFIG_FORM_DETAILS_URL)).thenReturn(formDetailURL);
        Mockito.doReturn(IaisFormHelper.FORM_NAME).when(request).getParameter(IaisFormHelper.FORM_NAME);
         // for IaisEGPHelper.getTinyUrl(callback.toString());
        PowerMockito.when(IaisEGPHelper.getTinyUrl("null/eservice/projectName/processName/callStepName?caseid=0&formname=formName")).thenReturn("TEST");
        //MultiLangUtil.translate
         PowerMockito.when(MultiLangUtil.translate(bpc.request, AppConstants.KEY_TRANSLATION_MODULE_MESSAGE, "DraftSaveSuccess", "Draft Form data Saved Successfully."))
                 .thenReturn("Draft Form data Saved Successfully.");
        IaisFormHelper.doSaveDraft(bpc,projectName,processName,callStepName);
        Assert.assertTrue(true);
    }
     //to test the draftNo is te null
    @Test
    public void testdoSaveDraftNull() throws BaseException {
        //for the getApplicationDraftNo()
        Mockito.doReturn(egpCaseData).when(egpCase).getCaseData(IaisFormHelper.ATTR_APP_DRAFT_NO);
        Mockito.doReturn(null).when(egpCaseData).getValue();
        // for the bindApplication();
        //for the loadServiceInfo
        Mockito.doReturn(egpCaseData).when(egpCase).getCaseData(EGPConstants.ATTR_SERVICE_INFO);
        PowerMockito.when(IaisServiceRegistryHelper.getServiceRegistryFromCase(egpCase)).thenReturn(serviceRegistry);
        // for   AgencyService.getInstance().retrieveByShortName
        PowerMockito.when(AgencyService.getInstance()).thenReturn(agencyService);
        Mockito.doReturn(agency).when(agencyService).retrieveByShortName(null);
        // for AppStatusHelper.getInstance().getStartStatus().getCode()
        PowerMockito.when(AppStatusHelper.getInstance()).thenReturn(appStatusHelper);
        appStatus.setCode("110");
        Mockito.doReturn(appStatus).when(appStatusHelper).getStartStatus();
        //for bpc.currentCase.getCaseData(EGPConstants.SUBMITTER);
        Mockito.doReturn(submitEgpCaseData).when(egpCase).getCaseData(EGPConstants.SUBMITTER);
        Mockito.doReturn(submitter).when(submitEgpCaseData).getValue();
        //for  bpc.currentCase.getCaseData(EGPConstants.APPLICANT);
        Mockito.doReturn(applicationEgpCaseData).when(egpCase).getCaseData(EGPConstants.APPLICANT);
        Mockito.doReturn(applicant).when(applicationEgpCaseData).getValue();
        IaisFormHelper.doSaveDraft(bpc,projectName,processName,callStepName);
        Assert.assertTrue(true);
    }

    @Test(expected = IaisRuntimeException.class )
    public void testsaveAppToMC() throws BaseException {
        //MessageCenterHelper.isMCEnabled()
        PowerMockito.when(MessageCenterHelper.isMCEnabled()).thenReturn(true);
        //ServerConfig.getInstance().getFrontendURL()
        PowerMockito.when(ServerConfig.getInstance()).thenReturn(serverConfig);
        String frontednURL = "https://egpcloudfe/";
        Mockito.doReturn("").when(serverConfig).getFrontendURL();
        // EngineHelper.getContextPath()
        String contextPath= "contextPath";
        PowerMockito.when(EngineHelper.getContextPath()).thenReturn(contextPath);
        // MessageCenterHelper.saveApplication(app, propMap);
        Map<String, Object> propMap = new HashMap<String, Object>();
        propMap.put(JsonLabel.SVC_CALLBACK_URL, null);
        propMap.put(JsonLabel.APP_STATUS,"Draft");

        propMap.put(JsonLabel.FORM_DETAILS_URL, "null?caseid=0&formname=null");
        propMap.put(JsonLabel.FORM_HTML, null);
        PowerMockito.when( MessageCenterHelper.saveApplication(applicaiton,propMap)).thenReturn(resInfo);
        IaisFormHelper.saveAppToMC(bpc,applicaiton,projectName,processName,callStepName);
        Assert.assertTrue(true);
    }

    @Test
    public void testdeleteDraft() throws BaseException {
        //for the getApplicationDraftNo()
        Mockito.doReturn(egpCaseData).when(egpCase).getCaseData(IaisFormHelper.ATTR_APP_DRAFT_NO);
        Mockito.doReturn("ApplicationNo").when(egpCaseData).getValue();
        //Applicant applicant = getApplication(bpc.currentCase);
        Mockito.doReturn(applicationEgpCaseData).when(egpCase).getCaseData(EGPConstants.APPLICANT);
        Mockito.doReturn(applicant).when(applicationEgpCaseData).getValue();
        IaisFormHelper.deleteDraft(bpc);
        Assert.assertTrue(true);
    }
    //Test the getApplicationDraftNo  return  null
    @Test
    public void testgetApplicationDraftNo(){
        String applicaitonDraftNo0 =  IaisFormHelper.getApplicationDraftNo(egpCase);
        String applicaitonDraftNo1 = IaisFormHelper.getApplicationDraftNo(null);
        Assert.assertNull(applicaitonDraftNo0);
        Assert.assertNull(applicaitonDraftNo1);

    }

    // Test the loadServiceInfo throw exception
    @Test(expected = IaisRuntimeException.class)
    public void testloadServiceInfo() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
       bpc.currentCase=egpCase;
       Mockito.doReturn(null).when(egpCase).getCaseData(EGPConstants.ATTR_SERVICE_INFO);
       Method method = IaisFormHelper.class.getDeclaredMethod("loadServiceInfo",BaseProcessClass.class);
       method.setAccessible(true);
       method.invoke(IaisFormHelper.class,new Object[]{bpc});
       Assert.assertTrue(true);
    }

    //test the getFormDetailUrl
    @Test
    public void testgetFormDetailUrl(){
        PowerMockito.when(ServerConfig.getInstance()).thenReturn(serverConfig);
        String frontednURL = "https://egpcloudfe/";
        Mockito.doReturn("").when(serverConfig).getFrontendURL();
        String formDetailURL = "cr/process/EGPCLOUD/FormDetails_MC?appNo=DL_2019_00000017_test";
        PowerMockito.when(ConfigUtil.getString(AppConstants.CONFIG_FORM_DETAILS_URL)).thenReturn(formDetailURL);
        Mockito.doReturn(IaisFormHelper.FORM_NAME).when(request).getParameter(IaisFormHelper.FORM_NAME);
        String url =  IaisFormHelper.getFormDetailUrl(bpc);
       Assert.assertNotNull(url);
    }

    @Test(expected = IaisRuntimeException.class)
    public void testgetBase64FormHtml() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        bpc.currentCase=egpCase;
        PowerMockito.when(IaisFormHelper.getFormInstanceFromCase(bpc.currentCase,formName)).thenReturn(formIns);
        Method method = IaisFormHelper.class.getDeclaredMethod("getBase64FormHtml",String.class,BaseProcessClass.class);
        method.setAccessible(true);
        method.invoke(IaisFormHelper.class,new Object[]{formName,bpc});
        Assert.assertTrue(true);
    }

}
