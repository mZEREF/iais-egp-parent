package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.api.config.GatewayConstants;
import com.ecquaria.cloud.moh.iais.api.config.GatewayStripeConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppFeeDetailsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremSubSvcRelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.RenewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.AmendmentFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.LicenceFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaAppConst;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.dto.PmtReturnUrlDto;
import com.ecquaria.cloud.moh.iais.helper.AppValidatorHelper;
import com.ecquaria.cloud.moh.iais.helper.ApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.ConfigCommService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.util.DealSessionUtil;
import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author zixian
 * @date 2020/10/27 14:54
 * @description
 */
@Delegator("MohRetriggerGiroPaymentDelegator")
@Slf4j
public class RetriggerGiroPaymentDelegator {
    @Autowired
    private AppSubmissionService appSubmissionService;
    @Autowired
    private ServiceConfigService serviceConfigService;
    @Autowired
    private RequestForChangeService requestForChangeService;
    @Autowired
    private ConfigCommService configCommService;

    private static final String RETRIGGER_GIRO = "retriggerGiro";
    private static final String FEE_DETAIL = "FeeDetail";
    private static final String PAYMETHOD = "payMethod";
    private static final String TXN_REFNO = "txnRefNo";
    private static final String TXN_DT = "txnDt";

    private static final String SWITCH = "switch";
    private static final String SWITCH_VALUE_PRE_ACK = "preack";
    private static final String SWITCH_VALUE_PRE_PAYMENT = "prepayment";
    private static final String ISVALID_VALUE_PRE_PAYMENT = "prepayment";
    private static final String ISVALID_VALUE_TO_BANK = "tobank";
    private static final Set<String> APP_PMT_STATUSES = ImmutableSet.of(
            ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS,
            ApplicationConsts.PAYMENT_STATUS_PENDING_GIRO,
            ApplicationConsts.PAYMENT_STATUS_GIRO_PAY_SUCCESS,
            ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT
    );

    public void doStart(BaseProcessClass bpc) throws CloneNotSupportedException {
        log.info(StringUtil.changeForLog("the retrigger giro doStart start ...."));
        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.APPSUBMISSIONDTO, null);
        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.PRIMARY_DOC_CONFIG, null);
        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.ACK_TITLE, null);
        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.ACK_SMALL_TITLE, null);
        ParamUtil.setSessionAttr(bpc.request,HcsaAppConst.DASHBOARDTITLE,RETRIGGER_GIRO);
        ParamUtil.setRequestAttr(bpc.request,HcsaAppConst.DASHBOARDTITLE,RETRIGGER_GIRO);
        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.REQUESTINFORMATIONCONFIG,null);

        String appGrpNo = ParamUtil.getMaskedString(bpc.request,"appGrpNo");
        log.debug(StringUtil.changeForLog("appGrpNo:" +appGrpNo));
        //init data
        String switch2 = "topreview";
        AppSubmissionDto appSubmissionDto = appSubmissionService.getAppSubmissionDtoByAppGrpNo(appGrpNo);

        List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        String appType = appSubmissionDto.getAppType();
        String title= "";
        switch (appType){
            case ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE:title = "Amendment";
                AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_REQUEST_FOR_CHANGE, AuditTrailConsts.FUNCTION_REQUEST_FOR_CHANGE);break;
            case ApplicationConsts.APPLICATION_TYPE_RENEWAL:title = "Licence Renewal";
                AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_RENEW, AuditTrailConsts.FUNCTION_RENEW);break;
            case ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION:title = "New Licence Application";
                AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_NEW_APPLICATION, AuditTrailConsts.FUNCTION_NEW_APPLICATION);break;
            default:
        }

        if(IaisCommonUtils.isEmpty(appGrpPremisesDtos) || IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
            log.debug(StringUtil.changeForLog("You have already resubmitted the payment!!!!"));
            switch2 = SWITCH_VALUE_PRE_ACK;
            ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.APPSUBMISSIONDTO,appSubmissionDto);
            ParamUtil.setRequestAttr(bpc.request, SWITCH, switch2);
            ParamUtil.setSessionAttr(bpc.request,HcsaAppConst.ACK_TITLE, title);
            ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.ACKMESSAGE,"You have already resubmitted the payment!!!");
            return;
        }
        StringBuilder smallTitle = new StringBuilder();
        List<String> svcNames = getServiceNameList(appSubmissionDto.getAppSvcRelatedInfoDtoList());
        if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)){
            String licenceNo = "";
            String licenceId = appSvcRelatedInfoDtos.get(0).getOriginLicenceId();
            if(!StringUtil.isEmpty(licenceId)){
                LicenceDto licenceDto = appSubmissionService.getLicenceDtoById(licenceId);
                licenceNo = licenceDto.getLicenceNo();
            }
            String svcName = HcsaServiceCacheHelper.getServiceById(appSvcRelatedInfoDtos.get(0).getServiceId()).getSvcName();
            title = "Amendment";
            smallTitle.append("You are amending the ")
                    .append("<strong>")
                    .append(svcName)
                    .append(" licence (Licence No. ")
                    .append(licenceNo)
                    .append("</strong>)");
        }else if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)){
            title = "Licence Renewal";
            int renewCount = 0;
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSvcRelatedInfoDto.getApplicationType())){
                    renewCount++;
                }
            }
            if(renewCount == 1){
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(appSvcRelatedInfoDtos.get(0).getServiceId());
                String licenceNo = "";
                String licenceId = appSvcRelatedInfoDtos.get(0).getOriginLicenceId();
                if(!StringUtil.isEmpty(licenceId)){
                    LicenceDto licenceDto = appSubmissionService.getLicenceDtoById(licenceId);
                    licenceNo = licenceDto.getLicenceNo();
                }
                smallTitle.append("You are renewing the ")
                        .append("<strong>")
                        .append(hcsaServiceDto.getSvcName())
                        .append(" (Licence No. ")
                        .append(licenceNo)
                        .append(")</strong>");

            }else if(renewCount > 1){
                smallTitle.append("You are renewing these licences: ");
                int count = 0;
                for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                    HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(appSvcRelatedInfoDto.getServiceId());
                    smallTitle.append("<strong>")
                            .append(hcsaServiceDto.getSvcName())
                            .append("</strong>");
                    if(count != appSvcRelatedInfoDtos.size()-1){
                        smallTitle.append(" | ");
                    }
                    count++;
                }
            }
        }else if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)){
            title = "New Licence Application";
            smallTitle.append("You are applying for ");
            int count = 0;
            for(String svcName:svcNames){
                smallTitle.append("<strong>")
                        .append(svcName)
                        .append("</strong>");
                if(count != svcNames.size()-1){
                    smallTitle.append(" | ");
                }
                count ++;
            }
        }
        ParamUtil.setSessionAttr(bpc.request,HcsaAppConst.ACK_TITLE, title);
        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.ACK_SMALL_TITLE, smallTitle);

        if(APP_PMT_STATUSES.contains(appSubmissionDto.getPmtStatus())){
            log.debug(StringUtil.changeForLog("You have already resubmitted the payment!!"));
            switch2 = SWITCH_VALUE_PRE_ACK;
            ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.APPSUBMISSIONDTO,appSubmissionDto);
            ParamUtil.setRequestAttr(bpc.request, SWITCH, switch2);
            ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.ACKMESSAGE,"You have already resubmitted the payment!");
            return;
        }
        //set type for page display
        //remove page edit button
        appSubmissionDto.setAppEditSelectDto(new AppEditSelectDto());

        List<HcsaServiceDto> hcsaServiceDtoList = DealSessionUtil.getServiceConfigsFormApp(appSubmissionDto);
        DealSessionUtil.setHcsaServiceDtoList(hcsaServiceDtoList, bpc.request);
        DealSessionUtil.init(appSubmissionDto, hcsaServiceDtoList, false, bpc.request);

        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
            //set svc info
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                HcsaServiceDto hcsaServiceDto = serviceConfigService.getHcsaServiceDtoById(appSvcRelatedInfoDto.getServiceId());
                appSvcRelatedInfoDto.setServiceType(hcsaServiceDto.getSvcType());
                appSvcRelatedInfoDto.setServiceName(hcsaServiceDto.getSvcName());
                appSvcRelatedInfoDto.setServiceCode(hcsaServiceDto.getSvcCode());
            }

            /*for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                String relLicenceNo = appSvcRelatedInfoDto.getRelLicenceNo();
                if(!StringUtil.isEmpty(relLicenceNo)){
                    LicenceDto licenceDto = requestForChangeService.getLicenceDtoByLicenceId(relLicenceNo);
                    HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(licenceDto.getSvcName());
                    appSvcRelatedInfoDto.setBaseServiceId(hcsaServiceDto.getId());
                    appSvcRelatedInfoDto.setBaseServiceName(hcsaServiceDto.getSvcName());
                }
            }*/
        }else{
            log.info("appSvcRelatedInfoDtos is empty ...");
        }
        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.APPSUBMISSIONDTO,appSubmissionDto);
        ParamUtil.setRequestAttr(bpc.request, SWITCH, switch2);
        log.info(StringUtil.changeForLog("the retrigger giro doStart end ...."));
    }

    public void prepreview(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the prepreview start ...."));
        ParamUtil.setRequestAttr(bpc.request,RETRIGGER_GIRO,"test");
        log.info(StringUtil.changeForLog("the prepreview end ...."));
    }

    public void prePayment(BaseProcessClass bpc) throws Exception {
        log.info(StringUtil.changeForLog("the prePayment start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, HcsaAppConst.APPSUBMISSIONDTO);
        boolean isCharity = ApplicationHelper.isCharity(bpc.request);

        List<AppSubmissionDto> forGiroList = IaisCommonUtils.genNewArrayList();
        if(appSubmissionDto != null){
            String appType = appSubmissionDto.getAppType();
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)){
                FeeDto feeDto = appSubmissionService.getNewAppAmount(appSubmissionDto, ApplicationHelper.isCharity(bpc.request));
                appSubmissionDto.setFeeInfoDtos(feeDto.getFeeInfoDtos());
                String amountStr = Formatter.formatterMoney(appSubmissionDto.getAmount());
                appSubmissionDto.setAmountStr(amountStr);
                forGiroList.add(appSubmissionDto);
                if(feeDto.getFeeDetail()!=null){
                    ParamUtil.setSessionAttr(bpc.request, FEE_DETAIL, feeDto.getFeeDetail().toString());
                }else {
                    ParamUtil.setSessionAttr(bpc.request, FEE_DETAIL, null);
                }
            }else if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)){
                if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
                    for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                        appSubmissionDto.setLicenceId(appSvcRelatedInfoDto.getLicenceId());
                    }
                }
                AmendmentFeeDto amendmentFeeDto = new AmendmentFeeDto();
                amendmentFeeDto.setChangeInLicensee(Boolean.FALSE);
                amendmentFeeDto.setAdditionOrRemovalVehicles(Boolean.TRUE);
                //add ss fee
                List<AppPremSubSvcRelDto> appPremSubSvcRelDtoList=appSubmissionDto.getAppPremSpecialisedDtoList().get(0).getFlatAppPremSubSvcRelList(dto -> ApplicationConsts.RECORD_ACTION_CODE_ADD.equals(dto.getActCode()));
                if (IaisCommonUtils.isNotEmpty(appPremSubSvcRelDtoList)) {
                    amendmentFeeDto.setAdditionOrRemovalSpecialisedServices(Boolean.TRUE);
                    List<LicenceFeeDto> licenceFeeSpecDtos = IaisCommonUtils.genNewArrayList();
                    for (AppPremSubSvcRelDto subSvc : appPremSubSvcRelDtoList
                    ) {
                        if (subSvc.isChecked()) {
                            LicenceFeeDto specFeeDto = new LicenceFeeDto();
                            specFeeDto.setBundle(0);
                            specFeeDto.setBaseService(appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getServiceCode());
                            specFeeDto.setServiceCode(subSvc.getSvcCode());
                            specFeeDto.setServiceName(subSvc.getSvcName());
                            specFeeDto.setPremises(appSubmissionDto.getAppGrpPremisesDtoList().get(0).getAddress());
                            specFeeDto.setCharity(isCharity);
                            licenceFeeSpecDtos.add(specFeeDto);
                        }
                    }
                    amendmentFeeDto.setSpecifiedLicenceFeeDto(licenceFeeSpecDtos);
                }
                List<AppPremSubSvcRelDto> removalDtoList=appSubmissionDto.getAppPremSpecialisedDtoList().get(0).getFlatAppPremSubSvcRelList(dto -> ApplicationConsts.RECORD_ACTION_CODE_REMOVE.equals(dto.getActCode()));
                if (IaisCommonUtils.isNotEmpty(removalDtoList)) {
                    amendmentFeeDto.setAdditionOrRemovalSpecialisedServices(Boolean.TRUE);
                }
                amendmentFeeDto.setServiceCode(appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getServiceCode());
                LicenceDto licenceDto = requestForChangeService.getLicenceDtoByLicNo(appSubmissionDto.getLicenceNo());
                if (licenceDto != null) {
                    Date licExpiryDate = licenceDto.getExpiryDate();
                    amendmentFeeDto.setLicenceExpiryDate(licExpiryDate);
                }

                amendmentFeeDto.setIsCharity(isCharity);
                amendmentFeeDto.setAddress(appSubmissionDto.getAppGrpPremisesDtoList().get(0).getAddress());
                amendmentFeeDto.setServiceName(appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getServiceName());
                amendmentFeeDto.setAppGrpNo(appSubmissionDto.getAppGrpNo());
                FeeDto feeDto = configCommService.getGroupAmendAmount(amendmentFeeDto);
                appSubmissionDto.setFeeInfoDtos(feeDto.getFeeInfoDtos());
                appSubmissionDto.setAmount(feeDto.getTotal());
                forGiroList.add(appSubmissionDto);
                if(feeDto.getFeeDetail()!=null){
                    ParamUtil.setSessionAttr(bpc.request, FEE_DETAIL, feeDto.getFeeDetail().toString());
                }else {
                    ParamUtil.setSessionAttr(bpc.request, FEE_DETAIL, null);
                }
            }else if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)){
                List<AppSubmissionDto> rfcAppSubmissionDtos = IaisCommonUtils.genNewArrayList();
                RenewDto renewDto = new RenewDto();
                List<AppSubmissionDto> renewSubmisonDtos = IaisCommonUtils.genNewArrayList();
                List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
                if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos) && !IaisCommonUtils.isEmpty(appGrpPremisesDtos)){
                    for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                        String applicationType = appSvcRelatedInfoDto.getApplicationType();
                        appSubmissionDto.setLicenceId(appSvcRelatedInfoDto.getLicenceId());
                        AppSubmissionDto oneSvcSubmisonDto = CopyUtil.copyMutableObject(appSubmissionDto);
                        List<AppSvcRelatedInfoDto> oneSvcRelateInfoDto = IaisCommonUtils.genNewArrayList();
                        oneSvcRelateInfoDto.add(appSvcRelatedInfoDto);
                        oneSvcSubmisonDto.setAppSvcRelatedInfoDtoList(oneSvcRelateInfoDto);
                        oneSvcSubmisonDto.setServiceName(appSvcRelatedInfoDto.getServiceName());
                        oneSvcSubmisonDto.setLicenceId(appSvcRelatedInfoDto.getOriginLicenceId());
                        //set one premises
                        List<AppGrpPremisesDto> onePremisesDto = getOnePremisesListById(appGrpPremisesDtos,appSvcRelatedInfoDto.getAlignPremisesId());
                        oneSvcSubmisonDto.setAppGrpPremisesDtoList(onePremisesDto);
                        if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationType)){
                            oneSvcSubmisonDto.setAmountStr("$0");
                            rfcAppSubmissionDtos.add(oneSvcSubmisonDto);
                        }else if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(applicationType)){
                            renewSubmisonDtos.add(oneSvcSubmisonDto);
                        }
                    }
                    forGiroList.addAll(renewSubmisonDtos);
                }
                //set fee info
                FeeDto renewalAmount;
                renewalAmount = appSubmissionService.getRenewalAmount(renewSubmisonDtos,isCharity);
                List<AppFeeDetailsDto> appFeeDetailsDto = IaisCommonUtils.genNewArrayList();
                WithOutRenewalDelegator.setSubmissionAmount(renewSubmisonDtos,renewalAmount,appFeeDetailsDto, 0, bpc);

                HashMap<String, List<FeeExtDto>> laterFeeDetailsMap = WithOutRenewalDelegator.getLaterFeeDetailsMap(renewalAmount.getFeeInfoDtos());
                ParamUtil.setRequestAttr(bpc.request, "laterFeeDetailsMap", laterFeeDetailsMap);
                ParamUtil.setRequestAttr(bpc.request, "feeInfoDtos", renewalAmount.getFeeInfoDtos());

                renewDto.setAppSubmissionDtos(renewSubmisonDtos);
                bpc.request.getSession().setAttribute("renewAppSubmissionDtos", rfcAppSubmissionDtos);
                ParamUtil.setSessionAttr(bpc.request, RenewalConstants.RENEW_DTO, renewDto);
            }
            appSubmissionDto.setAmountStr(Formatter.formatterMoney(appSubmissionDto.getAmount()));
            ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.APPSUBMISSIONDTO,appSubmissionDto);
            ParamUtil.setRequestAttr(bpc.request,HcsaAppConst.ATTR_RELOAD_PAYMENT_METHOD,appSubmissionDto.getPaymentMethod());
        }
        boolean isGiroAcc = false;
        List<SelectOption> giroAccSel = ApplicationHelper.getGiroAccOptions(forGiroList, appSubmissionDto);
        if(!IaisCommonUtils.isEmpty(giroAccSel)){
            isGiroAcc = true;
            ParamUtil.setRequestAttr(bpc.request, "giroAccSel", giroAccSel);
        }
        ParamUtil.setRequestAttr(bpc.request,"IsGiroAcc",isGiroAcc);
        log.info(StringUtil.changeForLog("the prePayment end ...."));
    }

    public void jumpBank(BaseProcessClass bpc) throws IOException {
        log.info(StringUtil.changeForLog("the jumpBank start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, HcsaAppConst.APPSUBMISSIONDTO);
        RenewDto renewDto = (RenewDto) ParamUtil.getSessionAttr(bpc.request, RenewalConstants.RENEW_DTO);
        String payMethod = ParamUtil.getString(bpc.request, PAYMETHOD);
        appSubmissionDto.setPaymentMethod(payMethod);
        String giroAccNum = "";
        if(!StringUtil.isEmpty(payMethod) && ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(payMethod)){
            giroAccNum = ParamUtil.getString(bpc.request, "giroAccount");
        }
        appSubmissionDto.setGiroAcctNum(giroAccNum);
        if(renewDto != null){
            List<AppSubmissionDto> appSubmissionDtos = renewDto.getAppSubmissionDtos();
            if(!IaisCommonUtils.isEmpty(appSubmissionDtos)){
                for(AppSubmissionDto appSubmissionDto1:appSubmissionDtos){
                    appSubmissionDto1.setPaymentMethod(payMethod);
                }
            }
        }
        ParamUtil.setSessionAttr(bpc.request,RenewalConstants.RENEW_DTO,renewDto);
        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.APPSUBMISSIONDTO,appSubmissionDto);
        String action = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_VALUE);
        Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
        if("next".equals(action)){
            String payMethodErrMsg =  MessageUtil.replaceMessage("GENERAL_ERR0006", "Payment Method", "field");
            if (StringUtil.isEmpty(payMethod)) {
                errorMap.put(PAYMETHOD, payMethodErrMsg);
                errorMap.put("pay", payMethodErrMsg);
            }else if(ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(payMethod) && StringUtil.isEmpty(giroAccNum)){
                errorMap.put(PAYMETHOD, payMethodErrMsg);
                errorMap.put("pay",payMethodErrMsg);
            }
        }
        if(!"next".equals(action) || !errorMap.isEmpty()){
            ParamUtil.setRequestAttr(bpc.request,"isValid",ISVALID_VALUE_PRE_PAYMENT);
            AppValidatorHelper.setAudiErrMap(false,appSubmissionDto.getAppType(),errorMap,appSubmissionDto.getRfiAppNo(),
                    appSubmissionDto.getLicenceNo());
            ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
            return;
        }

        log.debug("payMethod is {}",payMethod);
        if (ApplicationConsts.PAYMENT_METHOD_NAME_CREDIT.equals(payMethod)
                || ApplicationConsts.PAYMENT_METHOD_NAME_NETS.equals(payMethod)
                || ApplicationConsts.PAYMENT_METHOD_NAME_PAYNOW.equals(payMethod)) {

            String amount = String.valueOf(appSubmissionDto.getAmount());
            Map<String, String> fieldMap = new HashMap<>();
            fieldMap.put(GatewayConstants.AMOUNT_KEY, amount);
            fieldMap.put(GatewayConstants.PYMT_DESCRIPTION_KEY, GatewayConstants.PYMT_DESCRIPTION_RETRIGGER_GIRO);
            fieldMap.put(GatewayConstants.SVCREF_NO, appSubmissionDto.getAppGrpNo()+"_"+System.currentTimeMillis());
            PmtReturnUrlDto pmtReturnUrlDto = new PmtReturnUrlDto();
            pmtReturnUrlDto.setCreditRetUrl(GatewayStripeConfig.retrigger_giro_paymeny_return_rul);
            pmtReturnUrlDto.setPayNowRetUrl(GatewayStripeConfig.retrigger_giro_paymeny_return_rul);
            pmtReturnUrlDto.setNetsRetUrl(GatewayStripeConfig.retrigger_giro_paymeny_return_rul);
            pmtReturnUrlDto.setOtherRetUrl(GatewayStripeConfig.retrigger_giro_paymeny_return_rul);
            try {
                String url = NewApplicationHelper.genBankUrl(bpc.request,payMethod,fieldMap,pmtReturnUrlDto);
                IaisEGPHelper.redirectUrl(bpc.response, url);
            } catch (Exception e) {
                log.info(e.getMessage(), e);
            }
        }else if(ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(payMethod)){
            String appGrpId = appSubmissionDto.getAppGrpId();
            ApplicationGroupDto appGrp = new ApplicationGroupDto();
            appGrp.setId(appGrpId);
            appGrp.setPmtStatus(serviceConfigService.giroPaymentXmlUpdateByGrpNo(appSubmissionDto).getPmtStatus());
            String giroTranNo = appSubmissionDto.getGiroTranNo();
            appGrp.setPmtRefNo(giroTranNo);
            appGrp.setPayMethod(payMethod);
            serviceConfigService.updateAppGrpPmtStatus(appGrp, giroAccNum);
            //eic
            serviceConfigService.saveAppGroupGiroSysnEic(appGrp);
            ParamUtil.setSessionAttr(bpc.request, TXN_REFNO, giroTranNo);

            StringBuilder url = new StringBuilder();
            url.append("https://")
                    .append(bpc.request.getServerName())
                    .append("/hcsa-licence-web/eservice/INTERNET/MohRetriggerGiroPayment/preAck");
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
            return;
        }
        ParamUtil.setRequestAttr(bpc.request,"isValid",ISVALID_VALUE_TO_BANK);
        log.info(StringUtil.changeForLog("the jumpBank end ...."));
    }

    public void tobank(BaseProcessClass bpc){

    }

    public void doPayment(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the doPayment start ...."));
        String switch2 = SWITCH_VALUE_PRE_PAYMENT;
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, HcsaAppConst.APPSUBMISSIONDTO);
        if(appSubmissionDto != null){
            String pmtMethod = appSubmissionDto.getPaymentMethod();

            String result = ParamUtil.getMaskedString(bpc.request, "result");
            String pmtRefNo = ParamUtil.getMaskedString(bpc.request, "reqRefNo");
            if (!StringUtil.isEmpty(result)) {
                log.info(StringUtil.changeForLog("payment result:" + result));
                if ("success".equals(result) && !StringUtil.isEmpty(pmtRefNo)) {
                    log.info("credit card payment success");
                    String txnDt = ParamUtil.getMaskedString(bpc.request, TXN_DT);
                    String txnRefNo = ParamUtil.getMaskedString(bpc.request, TXN_REFNO);
                    ParamUtil.setSessionAttr(bpc.request, TXN_DT, txnDt);
                    ParamUtil.setSessionAttr(bpc.request, TXN_REFNO, txnRefNo);
                    switch2 = SWITCH_VALUE_PRE_ACK;
                    //update grp status
                    String appGrpId = appSubmissionDto.getAppGrpId();
                    ApplicationGroupDto appGrp = new ApplicationGroupDto();
                    appGrp.setId(appGrpId);
                    appGrp.setPmtRefNo(pmtRefNo);
                    appGrp.setPaymentDt(new Date());
                    appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
                    appGrp.setPayMethod(pmtMethod);
                    appGrp = serviceConfigService.updateAppGrpPmtStatus(appGrp);
                    //eic
                    serviceConfigService.saveAppGroupGiroSysnEic(appGrp);
                }else{
                    if(!"cancelled".equals(result)){
                        Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
                        String err024 = MessageUtil.getMessageDesc("NEW_ERR0024");
                        errorMap.put("pay",err024);
                        errorMap.put(PAYMETHOD,err024);
                        ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
                    }
                    switch2 = SWITCH_VALUE_PRE_PAYMENT;
                }
            }else{
                switch2 = SWITCH_VALUE_PRE_PAYMENT;
            }
            boolean isGiroAcc = false;
            List<SelectOption> giroAccSel = ApplicationHelper.getGiroAccOptions(null, appSubmissionDto);
            if(!IaisCommonUtils.isEmpty(giroAccSel)){
                isGiroAcc = true;
                ParamUtil.setRequestAttr(bpc.request, "giroAccSel", giroAccSel);
            }
            ParamUtil.setRequestAttr(bpc.request,"IsGiroAcc",isGiroAcc);

        }
        ParamUtil.setRequestAttr(bpc.request, SWITCH, switch2);
        log.info(StringUtil.changeForLog("the doPayment end ...."));
    }

    public void preAck(BaseProcessClass bpc) throws Exception {
        log.info(StringUtil.changeForLog("the preAck start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, HcsaAppConst.APPSUBMISSIONDTO);
        String appType = appSubmissionDto.getAppType();
        String paymentMethod = appSubmissionDto.getPaymentMethod();
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String licenseeId = "";
        if(loginContext != null){
            licenseeId = loginContext.getLicenseeId();
        }
        if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)){

        }else if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)){
            List<AppSubmissionDto> appSubmissionDtoList = (List<AppSubmissionDto>) ParamUtil.getSessionAttr(bpc.request,"appSubmissionDtos");
            List<AppSubmissionDto> oneSubmsonDtoList = IaisCommonUtils.genNewArrayList();
            if(!IaisCommonUtils.isEmpty(appSubmissionDtoList)){
                AppSubmissionDto targetDto = CopyUtil.copyMutableObject(appSubmissionDtoList.get(0));
                targetDto.setPaymentMethod(paymentMethod);
                double rfcAmount = 100;
                if(ApplicationHelper.isCharity(bpc.request)){
                    rfcAmount=12;
                }
                targetDto.setAmount( (rfcAmount * appSubmissionDtoList.size()));
                targetDto.setAmountStr(Formatter.formatterMoney(targetDto.getAmount()));
                oneSubmsonDtoList.add(targetDto);
            }
            bpc.request.getSession().setAttribute("ackPageAppSubmissionDto",oneSubmsonDtoList);
        }else if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)){
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
                List<String> svcNames = IaisCommonUtils.genNewArrayList();
                for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                    String serviceName = appSvcRelatedInfoDto.getServiceName();
                    String currAppType = appSvcRelatedInfoDto.getApplicationType();
                    if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(currAppType)){
                        serviceName = serviceName + " (Renewal)" ;
                    }else {
                        serviceName = serviceName + " (Amendment)" ;
                    }
                    svcNames.add(serviceName);
                }
                ParamUtil.setSessionAttr(bpc.request, "serviceNamesAck", (Serializable) svcNames);
            }
        }

        String txnRefNo = (String) bpc.request.getSession().getAttribute(TXN_DT);
        if (StringUtil.isEmpty(txnRefNo)) {
            String txnDt = DateUtil.formatDate(new Date(), "dd/MM/yyyy");
            ParamUtil.setSessionAttr(bpc.request, TXN_DT, txnDt);
        }

        List<String> licenseeEmailAddrs = IaisEGPHelper.getLicenseeEmailAddrs(licenseeId);
        String emailAddress = ApplicationHelper.emailAddressesToString(licenseeEmailAddrs);
        ParamUtil.setSessionAttr(bpc.request, "emailAddress", emailAddress);
        if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())){
            ParamUtil.setRequestAttr(bpc.request,"renewAck", "test");
        }
        log.info(StringUtil.changeForLog("the preAck end ...."));
    }

    private List<AppGrpPremisesDto> getOnePremisesListById(List<AppGrpPremisesDto> targetDtoList, String premisesId){
        List<AppGrpPremisesDto> appGrpPremisesDtos = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(targetDtoList) && !StringUtil.isEmpty(premisesId)){
            for(AppGrpPremisesDto appGrpPremisesDto:targetDtoList){
                if(appGrpPremisesDto.getId().equals(premisesId)){
                    appGrpPremisesDtos.add(appGrpPremisesDto);
                    break;
                }
            }
        }
        return appGrpPremisesDtos;
    }

    private List<String> getServiceNameList(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos){
        List<String> serviceNameList = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
            List<String> baseSvcNames = IaisCommonUtils.genNewArrayList();
            List<String> specifiedSvcNames = IaisCommonUtils.genNewArrayList();
            List<String> otherSvcNames = IaisCommonUtils.genNewArrayList();
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(appSvcRelatedInfoDto.getServiceId());
                if(hcsaServiceDto != null){
                    if(HcsaConsts.SERVICE_TYPE_BASE.equals(hcsaServiceDto.getSvcType())){
                        baseSvcNames.add(hcsaServiceDto.getSvcName());
                    }else if(HcsaConsts.SERVICE_TYPE_SPECIFIED.equals(hcsaServiceDto.getSvcType())){
                        specifiedSvcNames.add(hcsaServiceDto.getSvcName());
                    }else{
                        otherSvcNames.add(hcsaServiceDto.getSvcName());
                    }
                }
            }
            serviceNameList.addAll(sortSvcNameList(baseSvcNames));
            serviceNameList.addAll(sortSvcNameList(specifiedSvcNames));
            serviceNameList.addAll(sortSvcNameList(otherSvcNames));
        }
        return serviceNameList;
    }

    private List<String> sortSvcNameList(List<String> svcNames){
        if(svcNames != null && svcNames.size() > 1){
            Collections.sort(svcNames, String::compareTo);
        }
        return svcNames;
    }

}
