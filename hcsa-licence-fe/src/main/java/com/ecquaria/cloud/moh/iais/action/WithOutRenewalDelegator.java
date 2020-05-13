package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDisciplineAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.RenewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterInboxUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.RfcConst;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.WithOutRenewalService;
import com.ecquaria.cloud.moh.iais.validation.PaymentValidate;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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
    private static final String EDIT = "doEdit";
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
        ParamUtil.setSessionAttr(bpc.request,"totalStr",null);
        ParamUtil.setSessionAttr(bpc.request,"totalAmount",null);
        ParamUtil.setSessionAttr(bpc.request,"userAgreement",null);

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
        //get licensee ID
        InterInboxUserDto interInboxUserDto = (InterInboxUserDto) ParamUtil.getSessionAttr(bpc.request,"INTER_INBOX_USER_INFO");
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
            List<AppGrpPremisesDto> appGrpPremisesDtos= appSubmissionDto.getAppGrpPremisesDtoList();
            if(!IaisCommonUtils.isEmpty(appGrpPremisesDtos)){
                for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
                    NewApplicationHelper.setWrkTime(appGrpPremisesDto);
                }
            }
            if(!appSubmissionDto.getAppSvcRelatedInfoDtoList().isEmpty()) {
                String serviceName = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getServiceName();
                //add to service name list
                serviceNames.add(serviceName);
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(serviceName);
                String svcId = hcsaServiceDto.getId();
                //set svc info(svcId,svcName,svcCode,svcCode)
                AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
                appSvcRelatedInfoDto.setServiceId(hcsaServiceDto.getId());
                appSvcRelatedInfoDto.setServiceCode(hcsaServiceDto.getSvcCode());
                appSvcRelatedInfoDto.setServiceType(hcsaServiceDto.getSvcType());
                //set AppSvcRelatedInfoDtoList chkName
                List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos= serviceConfigService.loadLaboratoryDisciplines(svcId);
                NewApplicationHelper.setLaboratoryDisciplinesInfo(appSubmissionDto,hcsaSvcSubtypeOrSubsumedDtos);
                Map<String,List<AppSvcDisciplineAllocationDto>> reloadDisciplineAllocationMap= NewApplicationHelper.getDisciplineAllocationDtoList(appSubmissionDto,svcId);
                reloadDisciplineAllocationMapList.add(reloadDisciplineAllocationMap);
                //set svc step
                List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemesByServiceId = serviceConfigService.getHcsaServiceStepSchemesByServiceId(svcId);
                appSvcRelatedInfoDto.setHcsaServiceStepSchemeDtos(hcsaServiceStepSchemesByServiceId);
                //set doc info
                List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionDto.getAppGrpPrimaryDocDtos();
                List<HcsaSvcDocConfigDto> primaryDocConfig = serviceConfigService.getAllHcsaSvcDocs(null);
                if(!StringUtil.isEmpty(svcId)){
                    List<AppSvcDocDto> appSvcDocDtos = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
                    List<HcsaSvcDocConfigDto> svcDocConfig = serviceConfigService.getAllHcsaSvcDocs(svcId);
                    NewApplicationHelper.setDocInfo(appGrpPrimaryDocDtos,appSvcDocDtos,primaryDocConfig,svcDocConfig);
                }
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
                if(licenceIDList.size() == 1){
                    appEditSelectDto.setPremisesEdit(true);
                    appEditSelectDto.setDocEdit(true);
                    appEditSelectDto.setServiceEdit(true);
                    ParamUtil.setSessionAttr(bpc.request,"isSingle", "Y");
                }else{
                    ParamUtil.setSessionAttr(bpc.request,"isSingle", "N");
                }
                appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
            }
            appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_RENEWAL);
            appSubmissionDto.setStatus(ApplicationConsts.APPLICATION_STATUS_RENEWAL);
            //set licensee ID
            String licenseeId = interInboxUserDto.getLicenseeId();
            appSubmissionDto.setLicenseeId(licenseeId);

            String draftNumber = appSubmissionService.getDraftNo(ApplicationConsts.APPLICATION_TYPE_RENEWAL);
            String groupNumber =  appSubmissionService.getGroupNo(ApplicationConsts.APPLICATION_TYPE_RENEWAL);

            log.info(StringUtil.changeForLog("without renewal deafrt number =====>" + draftNumber));
            log.info(StringUtil.changeForLog("without renewal group number =====>" + groupNumber));

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
        log.info(StringUtil.changeForLog("without renewal submission json info " + parseToJson));

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
        ParamUtil.setRequestAttr(bpc.request,RfcConst.FIRSTVIEW,AppConsts.TRUE);
//finish pay
        RenewDto renewDto = (RenewDto)ParamUtil.getSessionAttr(bpc.request,RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR);
        List<AppSubmissionDto> appSubmissionDtos = renewDto.getAppSubmissionDtos();
        String groupId = "";
        Double amount=0.0;
        String licenseeId=null;
        if(appSubmissionDtos.size() != 0){
            groupId = appSubmissionDtos.get(0).getAppGrpId();
            amount = appSubmissionDtos.get(0).getAmount();
            licenseeId = appSubmissionDtos.get(0).getLicenseeId();
            bpc.request.getSession().setAttribute("oldAppSubmissionDto",appSubmissionDtos.get(0));

        }
        String result = ParamUtil.getMaskedString(bpc.request,"result");
        if (!StringUtil.isEmpty(result)) {
            log.info(StringUtil.changeForLog("payment result:" + result));
            String pmtRefNo = ParamUtil.getMaskedString(bpc.request,"reqRefNo");
            if ("success".equals(result) && !StringUtil.isEmpty(pmtRefNo)) {
                String txnDt = ParamUtil.getMaskedString(bpc.request,"txnDt");
                String txnRefNo = ParamUtil.getMaskedString(bpc.request,"txnRefNo");

                ParamUtil.setSessionAttr(bpc.request,"txnDt",txnDt);
                ParamUtil.setSessionAttr(bpc.request,"txnRefNo",txnRefNo);

                //update status
                ApplicationGroupDto appGrp = new ApplicationGroupDto();
                appGrp.setId(groupId);
                appGrp.setPmtRefNo(pmtRefNo);
                appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
                serviceConfigService.updatePaymentStatus(appGrp);
                //update application status
                appSubmissionService.updateApplicationsStatus(groupId,ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING);
                //jump page to acknowledgement
                //send email pay success
                try {

                    sendEmail(bpc.request,groupId,"successfulOnlinePayment",licenseeId,amount,"xxxx-xxxx-xxxx");
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                }
                ParamUtil.setRequestAttr(bpc.request,PAGE_SWITCH,PAGE4);
            }else{
                //jump page to payment
                ParamUtil.setRequestAttr(bpc.request,PAGE_SWITCH,PAGE3);
            }
        }
        log.info("**** the  renwal  prepare  end ******");
    }

    public void prepareInstructions(BaseProcessClass bpc)throws Exception{
        ParamUtil.setRequestAttr(bpc.request,"hasDetail","N");
    }

    //prepareLicenceReview
    public void prepareLicenceReview(BaseProcessClass bpc)throws Exception{
        ParamUtil.setRequestAttr(bpc.request,"hasDetail","Y");

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
        InterInboxUserDto interInboxUserDto = (InterInboxUserDto)ParamUtil.getSessionAttr(bpc.request,"INTER_INBOX_USER_INFO");
        String licenseeId = null;
        if(interInboxUserDto!=null){
            licenseeId = interInboxUserDto.getLicenseeId();
        }else{
            log.error(StringUtil.changeForLog("interInboxUserDto null"));
        }
        Double total = 0d;
        for(AppSubmissionDto appSubmissionDto : appSubmissionDtos){
            AppSubmissionDto oldAppSubmissionDto  =(AppSubmissionDto)bpc.request.getSession().getAttribute("oldAppSubmissionDto");
            AppSubmissionDto premisesSubmissionDto =  oldAppSubmissionDto;
            AppSubmissionDto personnelSubmissionDto = oldAppSubmissionDto;
            List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            premisesSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtoList);
            personnelSubmissionDto.setAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtoList);
            FeeDto feeDto = appSubmissionService.getGroupAmount(appSubmissionDto);
            appSubmissionDto.setLicenseeId(licenseeId);
            //set fee detail
            List<FeeExtDto> detailFeeDtos = feeDto.getDetailFeeDto();
            if(!IaisCommonUtils.isEmpty(detailFeeDtos)){
                appSubmissionDto.setDetailFeeDto(detailFeeDtos.get(0));
            }else{
                log.error(StringUtil.changeForLog("feeDto detailFeeDtos null"));
            }
            Double amount = feeDto.getTotal();
            if(!StringUtil.isEmpty(amount)){
                total +=amount;
                appSubmissionDto.setAmount(amount);
                String amountStr = Formatter.formatCurrency(amount);
                appSubmissionDto.setAmountStr(amountStr);
            }
        }
        String totalStr = Formatter.formatCurrency(total);
        //do app submit
//        ApplicationGroupDto applicationGroupDto = appSubmissionService.createApplicationDataByWithOutRenewal(renewDto);
//        //set group no.
//        for(AppSubmissionDto appSubmissionDto : appSubmissionDtos){
//            appSubmissionDto.setAppGrpNo(applicationGroupDto.getGroupNo());
//            appSubmissionDto.setAppGrpId(applicationGroupDto.getId());
//        }
        ParamUtil.setSessionAttr(bpc.request,RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR,renewDto);
        //ParamUtil.setRequestAttr(bpc.request,"applicationGroupDto",applicationGroupDto);
        ParamUtil.setSessionAttr(bpc.request,"totalStr",totalStr);
        ParamUtil.setSessionAttr(bpc.request,"totalAmount",total);
        //has app submit
        ParamUtil.setSessionAttr(bpc.request,"hasAppSubmit","Y");
    }

    //prepareAcknowledgement
    public void prepareAcknowledgement(BaseProcessClass bpc)throws Exception{
        InterInboxUserDto interInboxUserDto = (InterInboxUserDto)ParamUtil.getSessionAttr(bpc.request,"INTER_INBOX_USER_INFO");
        String licenseeId = null;
        if(interInboxUserDto!=null){
            licenseeId = interInboxUserDto.getLicenseeId();
        }else{
            log.error(StringUtil.changeForLog("interInboxUserDto null"));
        }
        List<String> licenseeEmailAddrs = IaisEGPHelper.getLicenseeEmailAddrs(licenseeId);
        String emailAddress = emailAddressesToString(licenseeEmailAddrs);
        ParamUtil.setRequestAttr(bpc.request,"emailAddress",emailAddress);
        ParamUtil.setRequestAttr(bpc.request,"hasDetail","N");
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
        String licenseeId=null;
        if(appSubmissionDtos.size() != 0){
            groupNo = appSubmissionDtos.get(0).getAppGrpNo();
            licenseeId = appSubmissionDtos.get(0).getLicenseeId();
        }
        if("Credit".equals(payMethod)){
            StringBuffer url = new StringBuffer();
            url.append("https://").append(bpc.request.getServerName())
                    .append("/payment-web/eservice/INTERNET/PaymentRequest")
                    .append("?amount=").append(MaskUtil.maskValue("amount",String.valueOf(totalAmount)))
                    .append("&payMethod=").append(MaskUtil.maskValue("payMethod",payMethod))
                    .append("&reqNo=").append(MaskUtil.maskValue("reqNo",groupNo))
                    .append("&backUrl=").append(MaskUtil.maskValue("backUrl",backUrl));


            String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);
            try {
                bpc.request.setAttribute("paymentAmount",totalAmount);

                sendEmail(bpc.request,groupNo,"onlinePayment",licenseeId,totalAmount,"xxxx-xxxx-xxxx");

            }catch (Exception e){
                log.error(e.getMessage(),e);
            }

        }else if("GIRO".equals(payMethod)){
            try {
                sendEmail(bpc.request,groupNo,"GIRO",licenseeId,totalAmount,"xxxx-xxxx-xxxx");

            }catch (Exception e){
                log.error(e.getMessage(),e);
            }

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
            String userAgreement = ParamUtil.getString(bpc.request, "verifyInfoCheckbox");
            if(!StringUtil.isEmpty(userAgreement) && AppConsts.YES.equals(userAgreement)){
                ParamUtil.setSessionAttr(bpc.request,"userAgreement",true);
            }else{
                ParamUtil.setSessionAttr(bpc.request,"userAgreement",false);
            }
        }else if(PAGE2.equals(switch_value)){
            ParamUtil.setRequestAttr(bpc.request,CONTROL_SWITCH,BACK);
            ParamUtil.setRequestAttr(bpc.request,PAGE_SWITCH,switch_value);
        }else if(EDIT.equals(switch_value)){
            ParamUtil.setRequestAttr(bpc.request,CONTROL_SWITCH,EDIT);
            ParamUtil.setRequestAttr(bpc.request,PAGE_SWITCH,PAGE2);
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
        String editValue = (String) ParamUtil.getString(bpc.request,"EditValue");
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
        if(appSubmissionDto != null){
            AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
            appEditSelectDto.setPremisesEdit(true);
            appEditSelectDto.setDocEdit(true);
            appEditSelectDto.setServiceEdit(true);
            appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = IaisCommonUtils.genNewArrayList();
            List<Map<String,List<AppSvcDisciplineAllocationDto>>> reloadDisciplineAllocationMapList = IaisCommonUtils.genNewArrayList();
            List<List<AppSvcPrincipalOfficersDto>> principalOfficersDtosList = IaisCommonUtils.genNewArrayList();
            List<List<AppSvcPrincipalOfficersDto>> deputyPrincipalOfficersDtosList = IaisCommonUtils.genNewArrayList();
            List<String> serviceNames = IaisCommonUtils.genNewArrayList();
            if(!appSubmissionDto.getAppSvcRelatedInfoDtoList().isEmpty()) {
                String serviceName = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getServiceName();
                //add to service name list
                serviceNames.add(serviceName);
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(serviceName);
                String svcId = hcsaServiceDto.getId();
                Map<String, List<AppSvcDisciplineAllocationDto>> reloadDisciplineAllocationMap = NewApplicationHelper.getDisciplineAllocationDtoList(appSubmissionDto, svcId);
                reloadDisciplineAllocationMapList.add(reloadDisciplineAllocationMap);

                //set AppSvcRelatedInfoDtoList chkName
                List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos = serviceConfigService.loadLaboratoryDisciplines(svcId);
                NewApplicationHelper.setLaboratoryDisciplinesInfo(appSubmissionDto, hcsaSvcSubtypeOrSubsumedDtos);
                AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
                appSvcRelatedInfoDtoList.add(appSvcRelatedInfoDto);

                List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtos = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
                List<AppSvcPrincipalOfficersDto> principalOfficersDtos = IaisCommonUtils.genNewArrayList();
                List<AppSvcPrincipalOfficersDto> deputyPrincipalOfficersDtos = IaisCommonUtils.genNewArrayList();
                if (appSvcPrincipalOfficersDtos != null && !appSvcPrincipalOfficersDtos.isEmpty()) {
                    for (AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto : appSvcPrincipalOfficersDtos) {
                        if (ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(appSvcPrincipalOfficersDto.getPsnType())) {
                            principalOfficersDtos.add(appSvcPrincipalOfficersDto);
                        } else if (ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(appSvcPrincipalOfficersDto.getPsnType())) {
                            deputyPrincipalOfficersDtos.add(appSvcPrincipalOfficersDto);
                        }
                    }
                }
                principalOfficersDtosList.add(principalOfficersDtos);
                deputyPrincipalOfficersDtosList.add(deputyPrincipalOfficersDtos);
            }

            ParamUtil.setSessionAttr(bpc.request, "currentPreviewSvcInfoList", (Serializable)appSvcRelatedInfoDtoList);
            ParamUtil.setSessionAttr(bpc.request, "reloadDisciplineAllocationMapList", (Serializable)reloadDisciplineAllocationMapList);
            ParamUtil.setSessionAttr(bpc.request, "ReloadPrincipalOfficersList", (Serializable)principalOfficersDtosList);
            ParamUtil.setSessionAttr(bpc.request, "deputyPrincipalOfficersDtosList", (Serializable)deputyPrincipalOfficersDtosList);
        }
        RenewDto renewDto = new RenewDto();
        List<AppSubmissionDto> appSubmissionDtos = IaisCommonUtils.genNewArrayList();
        appSubmissionDtos.add(appSubmissionDto);
        renewDto.setAppSubmissionDtos(appSubmissionDtos);
        ParamUtil.setSessionAttr(bpc.request,RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR, renewDto);
        ParamUtil.setRequestAttr(bpc.request,PAGE_SWITCH,PAGE2);
        ParamUtil.setRequestAttr(bpc.request,RfcConst.FIRSTVIEW,AppConsts.TRUE);
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
                    emailAddress += emailAddresses.get(i) + ", ";
                }
            }
        }
        return emailAddress;
    }




    public void sendEmail(HttpServletRequest request,String applicationNumber, String type, String licenseeId, Double amount,  String GIROAccountNumber ) throws IOException, TemplateException {
        List<HcsaServiceDto> hcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(request,AppServicesConsts.HCSASERVICEDTOLIST);
        List<String> serviceNames = new ArrayList<String>();
        for(HcsaServiceDto hcsaServiceDto : hcsaServiceDtos){
            String svcName = hcsaServiceDto.getSvcName();
            if(!StringUtil.isEmpty(svcName)){
                serviceNames.add(svcName);
            }
        }

        MsgTemplateDto msgTemplateDto=null;
        String mesContext ;

        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        String subject="";
        if("GIRO".equals(type)){
            msgTemplateDto = appSubmissionService.getMsgTemplateById("10FF81AF-267D-EA11-BE7A-000C29D29DB0");

            map.put("paymentAmount",Formatter.formatNumber(amount));
            map.put("serviceLicenceName", serviceNames.toString());
            map.put("GIROAccountNumber",GIROAccountNumber);
            subject="MOH IAIS – Successful Submission of Renewal "+applicationNumber;

        }else if("onlinePayment".equals(type)){
            msgTemplateDto = appSubmissionService.getMsgTemplateById("F77860C0-687D-EA11-BE7A-000C29D29DB0");

            map.put("paymentAmount",Formatter.formatNumber(amount));
            map.put("serviceLicenceName", serviceNames.toString());
            subject="MOH IAIS – Successful Submission of Renewal "+applicationNumber;

        }else if("successfulOnlinePayment".equals(type)){
            msgTemplateDto = appSubmissionService.getMsgTemplateById("A4CE953C-6A7D-EA11-BE7A-000C29D29DB0");
            map.put("paymentAmount",Formatter.formatNumber(amount));
            map.put("serviceLicenceName", serviceNames.toString());
            subject="MOH IAIS – Successful Submission of Auto Renewal Application "+applicationNumber;

        }else if("emailLink".equals(type)){
            msgTemplateDto = appSubmissionService.getMsgTemplateById("2C775ADE-6B7D-EA11-BE7A-000C29D29DB0");
            map.put("paymentAmount",Formatter.formatNumber(amount));
            map.put("serviceLicenceName", serviceNames);
            map.put("GIROAccountNumber",GIROAccountNumber);

        }
        mesContext = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getMessageContent(), map);
        EmailDto emailDto = new EmailDto();
        emailDto.setContent(mesContext);
        emailDto.setSender(AppConsts.MOH_AGENCY_NAME);
        emailDto.setSubject(subject);
        emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
        emailDto.setClientQueryCode(applicationNumber);
        appSubmissionService.feSendEmail(emailDto);
    }
}
