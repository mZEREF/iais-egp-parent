package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.service.RequestForInformationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * RequestForInformationDelegator
 *
 * @author junyu
 * @date 2019/12/14
 */
@Slf4j
@Delegator("requestForInformationDelegator")
public class RequestForInformationDelegator {
    @Autowired
    RequestForInformationService requestForInformationService;

    public void start(BaseProcessClass bpc) {
        log.info("=======>>>>>start>>>>>>>>>>>>>>>>requestForInformation");

        // 		Start->OnStepProcess
    }

    public void doSearch(BaseProcessClass bpc) {
        log.info("=======>>>>>doSearch>>>>>>>>>>>>>>>>requestForInformation");
        // 		doSearch->OnStepProcess
    }

    public void preBasicSearch(BaseProcessClass bpc) {
        log.info("=======>>>>>preBasicSearch>>>>>>>>>>>>>>>>requestForInformation");
        // 		preBasicSearch->OnStepProcess
    }

    public void doBasicSearch(BaseProcessClass bpc) {
        log.info("=======>>>>>doBasicSearch>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        String searchNo=ParamUtil.getString(request,"search_no");
        request.setAttribute(IaisEGPConstant.CRUD_ACTION_TYPE, currentAction);
        ParamUtil.setSessionAttr(request,"searchNo",searchNo);

        // 		doBasicSearch->OnStepProcess
    }

    public void preSearchLicence(BaseProcessClass bpc) {
        log.info("=======>>>>>preSearchLicence>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request = bpc.request;
        List<SelectOption> licSvcTypeOption =requestForInformationService.getLicSvcTypeOption();
        List<SelectOption> licStatusOption = requestForInformationService.getLicStatusOption();
        ParamUtil.setRequestAttr(request,"licSvcTypeOption", licSvcTypeOption);
        ParamUtil.setRequestAttr(request,"licStatusOption", licStatusOption);
        // 		preSearchLicence->OnStepProcess
    }

    public void preSearchApplication(BaseProcessClass bpc) {
        log.info("=======>>>>>preSearchApplication>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request = bpc.request;
        List<SelectOption> appTypeOption = requestForInformationService.getAppTypeOption();
        List<SelectOption> appStatusOption =requestForInformationService.getAppStatusOption();
        ParamUtil.setRequestAttr(request,"appTypeOption", appTypeOption);
        ParamUtil.setRequestAttr(request,"appStatusOption", appStatusOption);

        // 		preSearchApplication->OnStepProcess
    }

    public void doSearchApplication(BaseProcessClass bpc) {
        log.info("=======>>>>>doSearchApplication>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request = bpc.request;
        List<SelectOption> appTypeOption = requestForInformationService.getAppTypeOption();
        List<SelectOption> appStatusOption =requestForInformationService.getAppStatusOption();


        String searchNo= (String) ParamUtil.getSessionAttr(request,"searchNo");


        String application_no = ParamUtil.getString(bpc.request, "application_no");
        if(StringUtil.isEmpty(application_no)){
            application_no = searchNo;
        }

        String application_type = ParamUtil.getString(bpc.request, "application_type");
        String status = ParamUtil.getString(bpc.request, "application_status");
        String sub_date = ParamUtil.getString(bpc.request, "sub_date");
        String to_date = ParamUtil.getString(bpc.request, "to_date");






        ParamUtil.setRequestAttr(request,"appTypeOption", appTypeOption);
        ParamUtil.setRequestAttr(request,"appStatusOption", appStatusOption);
        // 		doSearchApplication->OnStepProcess
    }

    public void doSearchLicence(BaseProcessClass bpc) {
        log.info("=======>>>>>doSearchLicence>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request = bpc.request;
        List<SelectOption> licSvcTypeOption =requestForInformationService.getLicSvcTypeOption();
        List<SelectOption> licStatusOption = requestForInformationService.getLicStatusOption();
        String searchNo= (String) ParamUtil.getSessionAttr(request,"searchNo");


        String licence_no = ParamUtil.getString(bpc.request, "licence_no");
        if(StringUtil.isEmpty(licence_no)){
            licence_no = searchNo;
        }
        String service_licence_type = ParamUtil.getString(bpc.request, "service_licence_type");
        String licence_status = ParamUtil.getString(bpc.request, "licence_status");
        String sub_date = ParamUtil.getString(bpc.request, "sub_date");
        String to_date = ParamUtil.getString(bpc.request, "to_date");







        ParamUtil.setRequestAttr(request,"licSvcTypeOption", licSvcTypeOption);
        ParamUtil.setRequestAttr(request,"licStatusOption", licStatusOption);
        // 		doSearchLicence->OnStepProcess
    }

    public void doSearchLicenceAfter(BaseProcessClass bpc) {
        log.info("=======>>>>>doSearchLicenceAfter>>>>>>>>>>>>>>>>requestForInformation");
        // 		doSearchLicenceAfter->OnStepProcess
    }

    public void doSearchApplicationAfter(BaseProcessClass bpc) {
        log.info("=======>>>>>doSearchApplicationAfter>>>>>>>>>>>>>>>>requestForInformation");
        // 		doSearchApplicationAfter->OnStepProcess
    }

    public void preAppInfo(BaseProcessClass bpc) {
        log.info("=======>>>>>preAppInfo>>>>>>>>>>>>>>>>requestForInformation");
        // 		preAppInfo->OnStepProcess
    }

    public void preReqForInfo(BaseProcessClass bpc) {
        log.info("=======>>>>>preReqForInfo>>>>>>>>>>>>>>>>requestForInformation");
        // 		preReqForInfo->OnStepProcess
    }

    public void preSearch(BaseProcessClass bpc) {
        log.info("=======>>>>>preSearch>>>>>>>>>>>>>>>>requestForInformation");
        // 		preSearch->OnStepProcess
    }

    public void doCloseRfi(BaseProcessClass bpc) {
        log.info("=======>>>>>doCloseRfi>>>>>>>>>>>>>>>>requestForInformation");
        // 		doCloseRfi->OnStepProcess
    }

    public void doReqForInfo(BaseProcessClass bpc) {
        log.info("=======>>>>>doReqForInfo>>>>>>>>>>>>>>>>requestForInformation");
        // 		doReqForInfo->OnStepProcess
    }
}
