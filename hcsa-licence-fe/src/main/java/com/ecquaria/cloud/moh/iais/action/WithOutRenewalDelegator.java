package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterInboxUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.*;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.RfcConst;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.WithOutRenewalService;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ecquaria.cloud.moh.iais.validation.PaymentValidate;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import com.ecquaria.sz.commons.util.MsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;


/**
 * AutoRenewalDelegator
 *
 * @author caijing
 * @date 2020/1/6
 */

@Delegator("withOutRenewalDelegator")
@Slf4j
public class WithOutRenewalDelegator {
    private static final String PAGE1 = "instructions";
    private static final String PAGE2 = "licenceReview";
    private static final String PAGE3 = "payment";
    private static final String PAGE4 = "acknowledgement";
    private static final String INSTRUCTIONS = "doInstructions";
    private static final String REVIEW = "doLicenceReview";
    private static final String PAYMENT = "doPayment";
    private static final String BACK = "back";
    private static final String ACKNOWLEDGEMENT = "doAcknowledgement";
    private static final String PAGE_SWITCH = "page_value";
    private static final String CONTROL_SWITCH = "controlSwitch";
    @Autowired
    WithOutRenewalService outRenewalService;

    @Autowired
    AppSubmissionService appSubmissionService;

    @Autowired
    ServiceConfigService serviceConfigService;

    public void start(BaseProcessClass bpc){
        log.info("**** the non auto renwal  start ******");
        //init session
        ParamUtil.setSessionAttr(bpc.request,RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR, null);
        ParamUtil.setSessionAttr(bpc.request,NewApplicationDelegator.APPSUBMISSIONDTO,null);

        //init page value
        //instructions
        ParamUtil.setRequestAttr(bpc.request,"page_value",PAGE1);

        //init data
        List<String> licenceIDList = (List<String>) ParamUtil.getSessionAttr(bpc.request, RenewalConstants.WITHOUT_RENEWAL_LIC_ID_LIST_ATTR);
/*        licenceIDList = IaisCommonUtils.genNewArrayList();
        licenceIDList.add("B1DC1835-E161-EA11-BE7F-000C29F371DC");*/
        if (licenceIDList == null || IaisCommonUtils.isEmpty(licenceIDList)){
            log.info("can not find licence id for without renewal");
            return;
        }
        int index = 0;
        String firstSvcName = "";
        List<String> serviceNameTitleList = IaisCommonUtils.genNewArrayList();
        List<String> serviceNameList = IaisCommonUtils.genNewArrayList();
        List<AppSubmissionDto> appSubmissionDtoList = outRenewalService.getAppSubmissionDtos(licenceIDList);
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = IaisCommonUtils.genNewArrayList();
        List<Map<String,List<AppSvcDisciplineAllocationDto>>> reloadDisciplineAllocationMapList = IaisCommonUtils.genNewArrayList();
        List<List<AppSvcPrincipalOfficersDto>> principalOfficersDtosList = IaisCommonUtils.genNewArrayList();
        List<List<AppSvcPrincipalOfficersDto>> deputyPrincipalOfficersDtosList = IaisCommonUtils.genNewArrayList();
        AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
        List<String> serviceNames = IaisCommonUtils.genNewArrayList();
        for (AppSubmissionDto appSubmissionDto: appSubmissionDtoList) {
            if(!appSubmissionDto.getAppSvcRelatedInfoDtoList().isEmpty()) {
                String serviceName = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getServiceName();
                //add to service name list
                serviceNames.add(serviceName);
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(serviceName);
                String svcId = hcsaServiceDto.getId();
                Map<String,List<AppSvcDisciplineAllocationDto>> reloadDisciplineAllocationMap= NewApplicationHelper.getDisciplineAllocationDtoList(appSubmissionDto,svcId);
                reloadDisciplineAllocationMapList.add(reloadDisciplineAllocationMap);

                //set AppSvcRelatedInfoDtoList chkName
                List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos= serviceConfigService.loadLaboratoryDisciplines(svcId);
                NewApplicationHelper.setLaboratoryDisciplinesInfo(appSubmissionDto,hcsaSvcSubtypeOrSubsumedDtos);
                AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
                appSvcRelatedInfoDtoList.add(appSvcRelatedInfoDto);

                List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtos = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
                List<AppSvcPrincipalOfficersDto> principalOfficersDtos = IaisCommonUtils.genNewArrayList();
                List<AppSvcPrincipalOfficersDto> deputyPrincipalOfficersDtos = IaisCommonUtils.genNewArrayList();
                if(appSvcPrincipalOfficersDtos != null && ! appSvcPrincipalOfficersDtos.isEmpty()){
                    for(AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto:appSvcPrincipalOfficersDtos){
                        if(ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(appSvcPrincipalOfficersDto.getPsnType())){
                            principalOfficersDtos.add(appSvcPrincipalOfficersDto);
                        }else if(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(appSvcPrincipalOfficersDto.getPsnType())){
                            deputyPrincipalOfficersDtos.add(appSvcPrincipalOfficersDto);
                        }
                    }
                }
                principalOfficersDtosList.add(principalOfficersDtos);
                deputyPrincipalOfficersDtosList.add(deputyPrincipalOfficersDtos);

                if(!StringUtil.isEmpty(serviceName)){
                    if(index ==0){
                        firstSvcName = serviceName;
                        index ++;
                        ParamUtil.setSessionAttr(bpc.request,"AppSubmissionDto", appSubmissionDto);
                    }else{
                        serviceNameTitleList.add(serviceName);
                    }
                    serviceNameList.add(serviceName);
                }
                appSubmissionDto.setServiceName(serviceName);
                appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
            }
            appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_RENEWAL);
            appSubmissionDto.setStatus(ApplicationConsts.APPLICATION_STATUS_RENEWAL);

            String draftNumber = appSubmissionService.getDraftNo(ApplicationConsts.APPLICATION_TYPE_RENEWAL);
            String groupNumber =  appSubmissionService.getGroupNo(ApplicationConsts.APPLICATION_TYPE_RENEWAL);

            log.info("without renewal deafrt number =====>" + draftNumber);
            log.info("without renewal group number =====>" + groupNumber);

            appSubmissionDto.setDraftNo(draftNumber);
            appSubmissionDto.setAppGrpNo(groupNumber);
            appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());

            try {
                NewApplicationHelper.setSubmissionDtoSvcData(bpc.request, appSubmissionDto);
            } catch (CloneNotSupportedException e) {
                log.error(e.getMessage());
            }

            appSubmissionService.setRiskToDto(appSubmissionDto);
        }

        RenewDto renewDto = new RenewDto();

        String parseToJson = JsonUtil.parseToJson(appSubmissionDtoList);
        log.info("without renewal submission json info " + parseToJson);

        if(!IaisCommonUtils.isEmpty(serviceNames)) {
            List<HcsaServiceDto> hcsaServiceDtoList = serviceConfigService.getHcsaServiceByNames(serviceNames);
            ParamUtil.setSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST, (Serializable) hcsaServiceDtoList);
        }

        renewDto.setAppSubmissionDtos(appSubmissionDtoList);
        ParamUtil.setSessionAttr(bpc.request,RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR, renewDto);
        ParamUtil.setSessionAttr(bpc.request,"serviceNameTitleList", (Serializable)serviceNameTitleList);
        //serviceNameList
        ParamUtil.setSessionAttr(bpc.request,"serviceNames", (Serializable)serviceNameList);
        ParamUtil.setSessionAttr(bpc.request,"firstSvcName", firstSvcName);
        ParamUtil.setSessionAttr(bpc.request, "currentPreviewSvcInfoList", (Serializable)appSvcRelatedInfoDtoList);
        ParamUtil.setSessionAttr(bpc.request, "reloadDisciplineAllocationMapList", (Serializable)reloadDisciplineAllocationMapList);
        ParamUtil.setSessionAttr(bpc.request, "ReloadPrincipalOfficersList", (Serializable)principalOfficersDtosList);
        ParamUtil.setSessionAttr(bpc.request, "deputyPrincipalOfficersDtosList", (Serializable)deputyPrincipalOfficersDtosList);

        //init app submit
        ParamUtil.setSessionAttr(bpc.request,"hasAppSubmit",null);
        ParamUtil.setSessionAttr(bpc.request,"txnDt",null);
        ParamUtil.setSessionAttr(bpc.request,"txnRefNo",null);

        log.info("**** the non auto renwal  end ******");
    }

    /**
     * AutoStep: prepare
     *
     * @param bpc
     * @throws
     */
    public void prepare(BaseProcessClass bpc)throws Exception{
        log.info("**** the  auto renwal  prepare start  ******");
//finish pay
        RenewDto renewDto = (RenewDto)ParamUtil.getSessionAttr(bpc.request,RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR);
        List<AppSubmissionDto> appSubmissionDtos = renewDto.getAppSubmissionDtos();
        String groupId = "";
        if(appSubmissionDtos.size() != 0){
            groupId = appSubmissionDtos.get(0).getAppGrpId();
        }
        String result = ParamUtil.getString(bpc.request,"result");
        if (!StringUtil.isEmpty(result)) {
            log.info(StringUtil.changeForLog("payment result:" + result));
            String pmtRefNo = ParamUtil.getString(bpc.request,"reqRefNo");
            if ("success".equals(result) && !StringUtil.isEmpty(pmtRefNo)) {
                String txnDt = ParamUtil.getString(bpc.request,"txnDt");
                String txnRefNo = ParamUtil.getString(bpc.request,"txnRefNo");

                ParamUtil.setSessionAttr(bpc.request,"txnDt",txnDt);
                ParamUtil.setSessionAttr(bpc.request,"txnRefNo",txnRefNo);

                //update status
                ApplicationGroupDto appGrp = new ApplicationGroupDto();
                appGrp.setId(groupId);
                appGrp.setPmtRefNo(pmtRefNo);
                appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
                serviceConfigService.updatePaymentStatus(appGrp);
                //update application status

                //jump page to acknowledgement
                ParamUtil.setRequestAttr(bpc.request,PAGE_SWITCH,PAGE4);
            }else{
                //jump page to payment
                ParamUtil.setRequestAttr(bpc.request,PAGE_SWITCH,PAGE3);
            }
        }
        log.info("**** the  renwal  prepare  end ******");
    }

    public void prepareInstructions(BaseProcessClass bpc)throws Exception{

    }

    //prepareLicenceReview
    public void prepareLicenceReview(BaseProcessClass bpc)throws Exception{

    }

    //preparePayment
    public void preparePayment(BaseProcessClass bpc)throws Exception{
        RenewDto renewDto = (RenewDto)ParamUtil.getSessionAttr(bpc.request,RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR);
        if(renewDto == null){
            return;
        }
        //
        String hasSubmit = (String)ParamUtil.getSessionAttr(bpc.request,"hasAppSubmit");
        if("Y".equals(hasSubmit)){
            return;
        }
        List<AppSubmissionDto> appSubmissionDtos = renewDto.getAppSubmissionDtos();
        //app submit
        InterInboxUserDto interInboxUserDto = (InterInboxUserDto)ParamUtil.getSessionAttr(bpc.request,"inter-inbox-user-info");
        String licenseeId = interInboxUserDto.getLicenseeId();
        Double total = 0d;
        for(AppSubmissionDto appSubmissionDto : appSubmissionDtos){
            FeeDto feeDto = appSubmissionService.getGroupAmount(appSubmissionDto);
            appSubmissionDto.setLicenseeId(licenseeId);
            Double amount = feeDto.getTotal();
            if(!StringUtil.isEmpty(amount)){
                total +=amount;
                appSubmissionDto.setAmount(amount);
                String amountStr = Formatter.formatterMoney(amount);
                appSubmissionDto.setAmountStr(amountStr);
            }
        }
        String totalStr = Formatter.formatterMoney(total);
        //do app submit
        ApplicationGroupDto applicationGroupDto = appSubmissionService.createApplicationDataByWithOutRenewal(renewDto);
        //set group no.
        for(AppSubmissionDto appSubmissionDto : appSubmissionDtos){
            appSubmissionDto.setAppGrpNo(applicationGroupDto.getGroupNo());
            appSubmissionDto.setAppGrpId(applicationGroupDto.getId());
        }
        ParamUtil.setSessionAttr(bpc.request,RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR,renewDto);
        ParamUtil.setRequestAttr(bpc.request,"applicationGroupDto",applicationGroupDto);
        ParamUtil.setSessionAttr(bpc.request,"totalStr",totalStr);
        ParamUtil.setSessionAttr(bpc.request,"totalAmount",total);
        //has app submit
        ParamUtil.setSessionAttr(bpc.request,"hasAppSubmit","Y");
    }

    //prepareAcknowledgement
    public void prepareAcknowledgement(BaseProcessClass bpc)throws Exception{
        InterInboxUserDto interInboxUserDto = (InterInboxUserDto)ParamUtil.getSessionAttr(bpc.request,"inter-inbox-user-info");
        String licenseeId = interInboxUserDto.getLicenseeId();
        List<String> licenseeEmailAddrs = IaisEGPHelper.getLicenseeEmailAddrs(licenseeId);
        String emailAddress = emailAddressesToString(licenseeEmailAddrs);
        ParamUtil.setRequestAttr(bpc.request,"emailAddress",emailAddress);
    }

    //doInstructions
    public void doInstructions(BaseProcessClass bpc)throws Exception{
        //go page2
        ParamUtil.setRequestAttr(bpc.request,PAGE_SWITCH,PAGE2);
    }

    //doLicenceReview
    public void doLicenceReview(BaseProcessClass bpc)throws Exception{
        //go page3
        ParamUtil.setRequestAttr(bpc.request,PAGE_SWITCH,PAGE3);
    }

    //doPayment
    public void doPayment(BaseProcessClass bpc)throws Exception{
        PaymentValidate paymentValidate = new PaymentValidate();
        Map<String, String> errorMap = paymentValidate.validate(bpc.request);
        if(!errorMap.isEmpty()){
            ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request,PAGE_SWITCH,PAGE3);
            return;
        }
        String backUrl = "hcsa-licence-web/eservice/INTERNET/MohWithOutRenewal/1/preparatData";
        //set back url
        ParamUtil.setSessionAttr(bpc.request,"backUrl",backUrl);
        String payMethod = ParamUtil.getString(bpc.request, "payMethod");
        Double totalAmount = (Double)ParamUtil.getSessionAttr(bpc.request,"totalAmount");
        RenewDto renewDto = (RenewDto)ParamUtil.getSessionAttr(bpc.request,RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR);
        List<AppSubmissionDto> appSubmissionDtos = renewDto.getAppSubmissionDtos();
        for(AppSubmissionDto appSubmissionDto : appSubmissionDtos){
            appSubmissionDto.setPaymentMethod(payMethod);
        }
        //
        ParamUtil.setSessionAttr(bpc.request,RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR,renewDto);
        String groupNo = "";
        if(appSubmissionDtos.size() != 0){
            groupNo = appSubmissionDtos.get(0).getAppGrpNo();
        }
        if("Credit".equals(payMethod)){
            StringBuffer url = new StringBuffer();
            url.append("https://").append(bpc.request.getServerName())
                    .append("/payment-web/eservice/INTERNET/PaymentRequest")
                    .append("?amount=").append(totalAmount)
                    .append("&payMethod=").append(payMethod)
                    .append("&reqNo=").append(groupNo)
                    .append("&backUrl=").append(backUrl);
            String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);


        }

    }

    //doAcknowledgement
    public void doAcknowledgement(BaseProcessClass bpc)throws Exception{
        String payMethod = ParamUtil.getString(bpc.request,"payMethod");
        if("Credit".equals(payMethod)){

        }else{
            ParamUtil.setRequestAttr(bpc.request,PAGE_SWITCH,PAGE3);
            return;
        }
    }

    //controlSwitch
    public void controlSwitch(BaseProcessClass bpc)throws Exception{
        String switch_value = ParamUtil.getString(bpc.request,"switch_value");
        if(INSTRUCTIONS.equals(switch_value)){
            //controlSwitch
            ParamUtil.setRequestAttr(bpc.request,CONTROL_SWITCH,switch_value);
        }else if(REVIEW.equals(switch_value)){
            ParamUtil.setRequestAttr(bpc.request,CONTROL_SWITCH,switch_value);
        }else if(PAYMENT.equals(switch_value)){
            ParamUtil.setRequestAttr(bpc.request,CONTROL_SWITCH,switch_value);
        }else if(ACKNOWLEDGEMENT.equals(switch_value)){
            ParamUtil.setRequestAttr(bpc.request,CONTROL_SWITCH,switch_value);
        }else if(PAGE1.equals(switch_value)){
            ParamUtil.setRequestAttr(bpc.request,CONTROL_SWITCH,BACK);
            ParamUtil.setRequestAttr(bpc.request,PAGE_SWITCH,switch_value);
        }else if(PAGE2.equals(switch_value)){
            ParamUtil.setRequestAttr(bpc.request,CONTROL_SWITCH,BACK);
            ParamUtil.setRequestAttr(bpc.request,PAGE_SWITCH,switch_value);
        }
    }


    /**
     * AutoStep: determineAutoRenewalEligibility
     *
     * @param bpc
     * @throws
     */
    public void determineAutoRenewalEligibility(BaseProcessClass bpc){
        log.info("**** the determineAutoRenewalEligibility  prepare start  ******");

        //todo: editvalue is not null and one licence to jump
        String editValue = ParamUtil.getString(bpc.request,"EditValue");
        if(!StringUtil.isEmpty(editValue)){
            RenewDto renewDto  = (RenewDto) ParamUtil.getSessionAttr(bpc.request,RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR);
            if(renewDto != null){
                List<AppSubmissionDto> appSubmissionDtos = renewDto.getAppSubmissionDtos();
                if(!IaisCommonUtils.isEmpty(appSubmissionDtos) && appSubmissionDtos.size() == 1){
                    ParamUtil.setRequestAttr(bpc.request,"EditValue",editValue);
                    ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE,"jump");
                }
            }
        }



        log.info("**** the determineAutoRenewalEligibility  prepare  end ******");
    }


    /**
     * AutoStep: markPostInspection
     *
     * @param bpc
     * @throws
     */
    public void markPostInspection(BaseProcessClass bpc){
        log.info("**** the markPostInspection start  ******");
        AppSubmissionDto appSubmissionDto=(AppSubmissionDto)ParamUtil.getSessionAttr(bpc.request,"appSubmissionDto");
        PreOrPostInspectionResultDto preOrPostInspectionResultDto = appSubmissionService.judgeIsPreInspection(appSubmissionDto);
        if (preOrPostInspectionResultDto == null) {
            appSubmissionDto.setPreInspection(true);
            appSubmissionDto.setRequirement(true);
        } else {
            appSubmissionDto.setPreInspection(preOrPostInspectionResultDto.isPreInspection());
            appSubmissionDto.setRequirement(preOrPostInspectionResultDto.isRequirement());
        }
        log.info("**** the markPostInspection end ******");
    }


    /**
     * AutoStep: prepareJump
     *
     * @param bpc
     * @throws
     */
    public void prepareJump(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the do prepareJump start ...."));
        String editValue = (String) ParamUtil.getRequestAttr(bpc.request,"EditValue");
        RenewDto renewDto  = (RenewDto) ParamUtil.getSessionAttr(bpc.request,RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR);
        List<AppSubmissionDto> appSubmissionDtos = renewDto.getAppSubmissionDtos();
        AppSubmissionDto appSubmissionDto = appSubmissionDtos.get(0);
        AppEditSelectDto appEditSelectDto = appSubmissionDto.getAppEditSelectDto()==null? new AppEditSelectDto():appSubmissionDto.getAppEditSelectDto();
        if(!StringUtil.isEmpty(editValue)){
            if(RfcConst.EDIT_PREMISES.equals(editValue)){
                appEditSelectDto.setPremisesEdit(true);
                ParamUtil.setRequestAttr(bpc.request,RfcConst.RFC_CURRENT_EDIT,RfcConst.EDIT_PREMISES);
            }else if(RfcConst.EDIT_PRIMARY_DOC.equals(editValue)){
                appEditSelectDto.setDocEdit(true);
                ParamUtil.setRequestAttr(bpc.request,RfcConst.RFC_CURRENT_EDIT,RfcConst.EDIT_PRIMARY_DOC);
            }else if(RfcConst.EDIT_SERVICE.equals(editValue)){
                appEditSelectDto.setServiceEdit(true);
                ParamUtil.setRequestAttr(bpc.request,RfcConst.RFC_CURRENT_EDIT,RfcConst.EDIT_SERVICE);
            }
            appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
            appSubmissionDto.setClickEditPage(null);
            appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_RENEWAL);
            ParamUtil.setRequestAttr(bpc.request,RfcConst.APPSUBMISSIONDTORFCATTR,appSubmissionDto);
            ParamUtil.setRequestAttr(bpc.request,"appType",ApplicationConsts.APPLICATION_TYPE_RENEWAL);
        }


        log.info(StringUtil.changeForLog("the do prepareJump end ...."));
    }

    /**
     * AutoStep: toPrepareData
     *
     * @param bpc
     * @throws
     */
    public void toPrepareData(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the do toPrepareData start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request,NewApplicationDelegator.APPSUBMISSIONDTO);
        RenewDto renewDto = new RenewDto();
        List<AppSubmissionDto> appSubmissionDtos = IaisCommonUtils.genNewArrayList();
        appSubmissionDtos.add(appSubmissionDto);
        renewDto.setAppSubmissionDtos(appSubmissionDtos);
        ParamUtil.setSessionAttr(bpc.request,RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR, renewDto);
        ParamUtil.setRequestAttr(bpc.request,"jumpEdit","Y");
        log.info(StringUtil.changeForLog("the do toPrepareData end ...."));
    }


    //=============================================================================
    //private method
    //=============================================================================

    private String emailAddressesToString(List<String> emailAddresses){
        String emailAddress = "";
        if(emailAddresses.isEmpty()){
            return emailAddress;
        }

        if(emailAddresses.size() == 1){
            emailAddress += emailAddresses.get(0);
        }else{
            for(int i = 0;i < emailAddresses.size(); i++){
                if(i == emailAddresses.size() -1){
                    emailAddress += emailAddresses.get(i);
                }else{
                    emailAddress += emailAddresses.get(i) + ",";
                }
            }
        }
        return emailAddress;
    }

    private void sendEmailGIRO(HttpServletRequest request) throws IOException, TemplateException {
        MsgTemplateDto msgTemplateDto = appSubmissionService.getMsgTemplateById("");
        if(msgTemplateDto!=null){
            Double amount =(Double)request.getAttribute("amount");
            String licenseeId =(String)request.getAttribute("licenseeId");
            String appNumber=(String)request.getAttribute("appNumber");
            List<HcsaServiceDto> hcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(request,AppServicesConsts.HCSASERVICEDTOLIST);
            List<String> serviceNames = new ArrayList<String>();
            String appGrpNo =(String)request.getAttribute("appGrpNo");
            String  GIROAccountNumber=(String)request.getAttribute("GIROAccountNumber");
            String appGrpId= (String) request.getAttribute("appGrpId");
            Map<String, Object> map = IaisCommonUtils.genNewHashMap();
            map.put("serviceNames", serviceNames);
            map.put("paymentAmount",Formatter.formatNumber(amount));
            map.put("GIROAccountNumber",GIROAccountNumber);
            String  mesContext = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getMessageContent(), map);
            EmailDto emailDto = new EmailDto();
            emailDto.setContent(mesContext);
            emailDto.setSubject("MOH IAIS – Successful Submission of New Application - "+appNumber);
            emailDto.setSender(AppConsts.MOH_AGENCY_NAME);
            emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
            emailDto.setClientQueryCode(appGrpId);
            appSubmissionService.feSendEmail(emailDto);
        }

    }

    private void snedEmailOnlinePayment(HttpServletRequest request){

        MsgTemplateDto msgTemplateDto = appSubmissionService.getMsgTemplateById(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_PAYMENT_ONLINE_ID);
        if(msgTemplateDto != null) {
            Double amount =(Double)request.getAttribute("amount");
            String licenseeId =(String)request.getAttribute("licenseeId");
            List<HcsaServiceDto> hcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(request,AppServicesConsts.HCSASERVICEDTOLIST);
            List<String> serviceNames = new ArrayList<String>();
            for(HcsaServiceDto hcsaServiceDto : hcsaServiceDtos){
                String svcName = hcsaServiceDto.getSvcName();
                if(!StringUtil.isEmpty(svcName)){
                    serviceNames.add(svcName);
                }
            }
            String appGrpNo =(String)request.getAttribute("appGrpNo");
            String appGrpId= (String) request.getAttribute("appGrpId");
            Map<String, Object> map = IaisCommonUtils.genNewHashMap();
            map.put("serviceNames", serviceNames);
            map.put("paymentAmount",Formatter.formatNumber(amount));
            map.put("MOH_AGENCY_NAME", AppConsts.MOH_AGENCY_NAME);
            String mesContext = null;
            try {
                mesContext = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getMessageContent(), map);
            } catch (IOException | TemplateException e) {
                log.error(e.getMessage(),e);
            }
            EmailDto emailDto = new EmailDto();
            emailDto.setContent(mesContext);
            emailDto.setSubject(" " + msgTemplateDto.getTemplateName() + " " + appGrpNo);
            emailDto.setSender(AppConsts.MOH_AGENCY_NAME);
            emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
            emailDto.setClientQueryCode(appGrpId);
            //send email
            appSubmissionService.feSendEmail(emailDto);
        }
    }

    private void sendEmailsuccessfulOnlinePayment(HttpServletRequest request){

        MsgTemplateDto msgTemplateDto = appSubmissionService.getMsgTemplateById("");
        if(msgTemplateDto != null) {
            Double amount =(Double)request.getAttribute("amount");
            String licenseeId =(String)request.getAttribute("licenseeId");
            List<HcsaServiceDto> hcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(request,AppServicesConsts.HCSASERVICEDTOLIST);
            List<String> serviceNames = new ArrayList<String>();
            for(HcsaServiceDto hcsaServiceDto : hcsaServiceDtos){
                String svcName = hcsaServiceDto.getSvcName();
                if(!StringUtil.isEmpty(svcName)){
                    serviceNames.add(svcName);
                }
            }
            String appGrpNo =(String)request.getAttribute("appGrpNo");
            String appGrpId= (String) request.getAttribute("appGrpId");
            Map<String, Object> map = IaisCommonUtils.genNewHashMap();
            map.put("serviceNames", serviceNames);
            map.put("paymentAmount",Formatter.formatNumber(amount));
            map.put("MOH_AGENCY_NAME", AppConsts.MOH_AGENCY_NAME);
            String mesContext = null;
            try {
                mesContext = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getMessageContent(), map);
            } catch (IOException | TemplateException e) {
                log.error(e.getMessage(),e);
            }
            EmailDto emailDto = new EmailDto();
            emailDto.setContent(mesContext);
            emailDto.setSubject(" " + msgTemplateDto.getTemplateName() + " " + appGrpNo);
            emailDto.setSender(AppConsts.MOH_AGENCY_NAME);
            emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
            emailDto.setClientQueryCode(appGrpId);
            //send email
            appSubmissionService.feSendEmail(emailDto);
        }

    }


    public void sendEmail(HttpServletRequest request) throws IOException, TemplateException {
        String type=(String)request.getAttribute("type");
        MsgTemplateDto msgTemplateDto=null;
        String mesContext = null;
        String licenseeId =(String)request.getAttribute("licenseeId");
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        if("GIRO".equals(type)){
            List<HcsaServiceDto> hcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(request,AppServicesConsts.HCSASERVICEDTOLIST);
            List<String> serviceNames = new ArrayList<String>();
            for(HcsaServiceDto hcsaServiceDto : hcsaServiceDtos){
                String svcName = hcsaServiceDto.getSvcName();
                if(!StringUtil.isEmpty(svcName)){
                    serviceNames.add(svcName);
                }
            }
            Double amount =(Double)request.getAttribute("amount");
            map.put("paymentAmount",Formatter.formatNumber(amount));
            map.put("serviceNames", serviceNames);
        }else if("onlinePayment".equals(type)){
            List<HcsaServiceDto> hcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(request,AppServicesConsts.HCSASERVICEDTOLIST);
            List<String> serviceNames = new ArrayList<String>();
            for(HcsaServiceDto hcsaServiceDto : hcsaServiceDtos){
                String svcName = hcsaServiceDto.getSvcName();
                if(!StringUtil.isEmpty(svcName)){
                    serviceNames.add(svcName);
                }
            }
             msgTemplateDto = appSubmissionService.getMsgTemplateById("");
            Double amount =(Double)request.getAttribute("amount");
            map.put("paymentAmount",Formatter.formatNumber(amount));
            map.put("serviceNames", serviceNames);
        }else if("successfulOnlinePayment".equals(type)){
            List<HcsaServiceDto> hcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(request,AppServicesConsts.HCSASERVICEDTOLIST);
            List<String> serviceNames = new ArrayList<String>();
            for(HcsaServiceDto hcsaServiceDto : hcsaServiceDtos){
                String svcName = hcsaServiceDto.getSvcName();
                if(!StringUtil.isEmpty(svcName)){
                    serviceNames.add(svcName);
                }
            }
            Double amount =(Double)request.getAttribute("amount");
            msgTemplateDto = appSubmissionService.getMsgTemplateById("");
            map.put("paymentAmount",Formatter.formatNumber(amount));
            map.put("serviceNames", serviceNames);
        }else if("routesBack".equals(type)){
            List<HcsaServiceDto> hcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(request,AppServicesConsts.HCSASERVICEDTOLIST);
            List<String> serviceNames = new ArrayList<String>();
            for(HcsaServiceDto hcsaServiceDto : hcsaServiceDtos){
                String svcName = hcsaServiceDto.getSvcName();
                if(!StringUtil.isEmpty(svcName)){
                    serviceNames.add(svcName);
                }
            }
            msgTemplateDto = appSubmissionService.getMsgTemplateById("");
            Double amount =(Double)request.getAttribute("amount");
            map.put("paymentAmount",Formatter.formatNumber(amount));
            map.put("serviceNames", serviceNames);
        }else if("earlyGIROPayment".equals(type)){
            List<HcsaServiceDto> hcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(request,AppServicesConsts.HCSASERVICEDTOLIST);
            List<String> serviceNames = new ArrayList<String>();
            for(HcsaServiceDto hcsaServiceDto : hcsaServiceDtos){
                String svcName = hcsaServiceDto.getSvcName();
                if(!StringUtil.isEmpty(svcName)){
                    serviceNames.add(svcName);
                }
            }
            msgTemplateDto = appSubmissionService.getMsgTemplateById("");
            Double amount =(Double)request.getAttribute("amount");
            map.put("paymentAmount",Formatter.formatNumber(amount));
            map.put("serviceNames", serviceNames);
        }else if("earlyOnlinepayment ".equals(type)){
            List<HcsaServiceDto> hcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(request,AppServicesConsts.HCSASERVICEDTOLIST);
            List<String> serviceNames = new ArrayList<String>();
            for(HcsaServiceDto hcsaServiceDto : hcsaServiceDtos){
                String svcName = hcsaServiceDto.getSvcName();
                if(!StringUtil.isEmpty(svcName)){
                    serviceNames.add(svcName);
                }
            }
            msgTemplateDto = appSubmissionService.getMsgTemplateById("");
            Double amount =(Double)request.getAttribute("amount");
            map.put("paymentAmount",Formatter.formatNumber(amount));
            map.put("serviceNames", serviceNames);
        }else if("pickedUpEarlyRenewal".equals(type)){

            msgTemplateDto = appSubmissionService.getMsgTemplateById("");

        }else if("emailLink".equals(type)){

        }
        mesContext = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getMessageContent(), map);
        EmailDto emailDto = new EmailDto();
        emailDto.setContent(mesContext);
        emailDto.setSender(AppConsts.MOH_AGENCY_NAME);
        emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
        appSubmissionService.feSendEmail(emailDto);
    }
}
