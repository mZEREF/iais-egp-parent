package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.api.config.GatewayConstants;
import com.ecquaria.cloud.moh.iais.api.config.GatewayStripeConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppFeeDetailsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremPhOpenPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.RenewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceFeConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.NewApplicationConstant;
import com.ecquaria.cloud.moh.iais.constant.RfcConst;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.dto.PmtReturnUrlDto;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
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
        ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.APPSUBMISSIONDTO, null);
        ParamUtil.setSessionAttr(bpc.request,NewApplicationDelegator.PRIMARY_DOC_CONFIG, null);
        ParamUtil.setSessionAttr(bpc.request,NewApplicationConstant.ACK_TITLE, null);
        ParamUtil.setSessionAttr(bpc.request, NewApplicationConstant.ACK_SMALL_TITLE, null);
        ParamUtil.setSessionAttr(bpc.request,HcsaLicenceFeConstant.DASHBOARDTITLE,"retriggerGiro");
        ParamUtil.setRequestAttr(bpc.request,HcsaLicenceFeConstant.DASHBOARDTITLE,"retriggerGiro");
        ParamUtil.setSessionAttr(bpc.request,NewApplicationDelegator.REQUESTINFORMATIONCONFIG,null);

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
            log.debug(StringUtil.changeForLog("You have already resubmitted the payment!"));
            switch2 = SWITCH_VALUE_PRE_ACK;
            ParamUtil.setSessionAttr(bpc.request,NewApplicationDelegator.APPSUBMISSIONDTO,appSubmissionDto);
            ParamUtil.setRequestAttr(bpc.request, SWITCH, switch2);
            ParamUtil.setSessionAttr(bpc.request,NewApplicationConstant.ACK_TITLE, title);
            ParamUtil.setRequestAttr(bpc.request,NewApplicationDelegator.ACKMESSAGE,"You have already resubmitted the payment!");
            return;
        }
        StringBuilder smallTitle = new StringBuilder();
        List<String> svcNames = getServiceNameList(appSubmissionDto.getAppSvcRelatedInfoDtoList());
        if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)){
            requestForChangeService.svcDocToPresmise(appSubmissionDto);
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
            requestForChangeService.svcDocToPrimaryForGiroDeduction(appSubmissionDto);
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
        ParamUtil.setSessionAttr(bpc.request,NewApplicationConstant.ACK_TITLE, title);
        ParamUtil.setSessionAttr(bpc.request, NewApplicationConstant.ACK_SMALL_TITLE, smallTitle);

        if(APP_PMT_STATUSES.contains(appSubmissionDto.getPmtStatus())){
            log.debug(StringUtil.changeForLog("You have already resubmitted the payment!"));
            switch2 = SWITCH_VALUE_PRE_ACK;
            ParamUtil.setSessionAttr(bpc.request,NewApplicationDelegator.APPSUBMISSIONDTO,appSubmissionDto);
            ParamUtil.setRequestAttr(bpc.request, SWITCH, switch2);
            ParamUtil.setRequestAttr(bpc.request,NewApplicationDelegator.ACKMESSAGE,"You have already resubmitted the payment!");
            return;
        }
        //appSubmissionDto.setRfiMsgId(msgId);
        //set type for page display
        //appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
        //remove page edit button
        appSubmissionDto.setAppEditSelectDto(new AppEditSelectDto());

        //set premises info
        if (!IaisCommonUtils.isEmpty(appGrpPremisesDtos)) {
            for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
                appGrpPremisesDto = NewApplicationHelper.setWrkTime(appGrpPremisesDto);
                List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodDtos = appGrpPremisesDto.getAppPremPhOpenPeriodList();
                //set ph name
                NewApplicationHelper.setPhName(appPremPhOpenPeriodDtos);
                appGrpPremisesDto.setAppPremPhOpenPeriodList(appPremPhOpenPeriodDtos);
            }
        }

        //set doc name
        List<HcsaSvcDocConfigDto> primaryDocConfig = null;
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionDto.getAppGrpPrimaryDocDtos();
        if(appGrpPrimaryDocDtos != null && appGrpPrimaryDocDtos.size() > 0){
            primaryDocConfig = serviceConfigService.getPrimaryDocConfigById(appGrpPrimaryDocDtos.get(0).getSvcComDocId());
            ParamUtil.setSessionAttr(bpc.request,NewApplicationDelegator.PRIMARY_DOC_CONFIG, (Serializable) primaryDocConfig);
        }
        NewApplicationHelper.setDocInfo(appGrpPrimaryDocDtos, null, primaryDocConfig, null);
        //add align for dup for prem doc
        NewApplicationHelper.addPremAlignForPrimaryDoc(primaryDocConfig,appGrpPrimaryDocDtos,appGrpPremisesDtos);
        //set primary doc title
        Map<String,List<AppGrpPrimaryDocDto>> reloadPrimaryDocMap = NewApplicationHelper.genPrimaryDocReloadMap(primaryDocConfig,appGrpPremisesDtos,appGrpPrimaryDocDtos);
        appSubmissionDto.setMultipleGrpPrimaryDoc(reloadPrimaryDocMap);
        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
            //set svc info
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                HcsaServiceDto hcsaServiceDto = serviceConfigService.getHcsaServiceDtoById(appSvcRelatedInfoDto.getServiceId());
                if(hcsaServiceDto.getSvcType().equals(ApplicationConsts.SERVICE_CONFIG_TYPE_BASE)){
                    appSvcRelatedInfoDto.setBaseServiceId(hcsaServiceDto.getId());
                }
                appSvcRelatedInfoDto.setServiceType(hcsaServiceDto.getSvcType());
                appSvcRelatedInfoDto.setServiceName(hcsaServiceDto.getSvcName());
                appSvcRelatedInfoDto.setServiceCode(hcsaServiceDto.getSvcCode());
            }

//            List<String> serviceConfigIds = IaisCommonUtils.genNewArrayList();
            List<HcsaServiceDto> hcsaServiceDtoList = IaisCommonUtils.genNewArrayList();
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                String currentSvcId = appSvcRelatedInfoDto.getServiceId();
//                serviceConfigIds.add(appSvcRelatedInfoDto.getServiceId());
                hcsaServiceDtoList.add(HcsaServiceCacheHelper.getServiceById(currentSvcId));
                String relLicenceNo = appSvcRelatedInfoDto.getRelLicenceNo();
                if(!StringUtil.isEmpty(relLicenceNo)){
                    LicenceDto licenceDto = requestForChangeService.getLicenceDtoByLicenceId(relLicenceNo);
                    HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(licenceDto.getSvcName());
                    appSvcRelatedInfoDto.setBaseServiceId(hcsaServiceDto.getId());
                    appSvcRelatedInfoDto.setBaseServiceName(hcsaServiceDto.getSvcName());
                }
                //set doc name
                List<AppSvcDocDto> appSvcDocDtos = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
                List<HcsaSvcDocConfigDto> svcDocConfig = serviceConfigService.getAllHcsaSvcDocs(currentSvcId);
                NewApplicationHelper.setDocInfo(null, appSvcDocDtos, null, svcDocConfig);
                //set AppSvcLaboratoryDisciplinesDto
                List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos = serviceConfigService.loadLaboratoryDisciplines(currentSvcId);
                if (!IaisCommonUtils.isEmpty(hcsaSvcSubtypeOrSubsumedDtos)) {
                    NewApplicationHelper.setLaboratoryDisciplinesInfo(appGrpPremisesDtos, appSvcRelatedInfoDto, hcsaSvcSubtypeOrSubsumedDtos);
                }
                //set dpo select flag
                List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtos = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
                if (!IaisCommonUtils.isEmpty(appSvcPrincipalOfficersDtos)) {
                    for (AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto : appSvcPrincipalOfficersDtos) {
                        if (ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(appSvcPrincipalOfficersDto.getPsnType())) {
                            appSvcRelatedInfoDto.setDeputyPoFlag(AppConsts.YES);
                            break;
                        }
                    }
                }
                ParamUtil.setSessionAttr(bpc.request,NewApplicationDelegator.SVC_DOC_CONFIG, (Serializable) svcDocConfig);
                //set dupForPsn attr
                NewApplicationHelper.setDupForPersonAttr(bpc.request,appSvcRelatedInfoDto);
                //svc doc add align for dup for prem
                NewApplicationHelper.addPremAlignForSvcDoc(svcDocConfig,appSvcDocDtos,appGrpPremisesDtos);
                appSvcRelatedInfoDto.setAppSvcDocDtoLit(appSvcDocDtos);
                //set svc doc title
                Map<String,List<AppSvcDocDto>> reloadSvcDocMap = NewApplicationHelper.genSvcDocReloadMap(svcDocConfig,appGrpPremisesDtos,appSvcRelatedInfoDto);
                appSvcRelatedInfoDto.setMultipleSvcDoc(reloadSvcDocMap);

            }
//            List<HcsaServiceDto> hcsaServiceDtoList = serviceConfigService.getHcsaServiceDtosById(serviceConfigIds);
            //do sort
            if(!IaisCommonUtils.isEmpty(hcsaServiceDtoList)){
                hcsaServiceDtoList = NewApplicationHelper.sortHcsaServiceDto(hcsaServiceDtoList);
            }
            ParamUtil.setSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST, (Serializable) hcsaServiceDtoList);

        }else{
            log.info("appSvcRelatedInfoDtos is empty ...");
        }
        ParamUtil.setSessionAttr(bpc.request,NewApplicationDelegator.APPSUBMISSIONDTO,appSubmissionDto);
        ParamUtil.setRequestAttr(bpc.request, SWITCH, switch2);
        log.info(StringUtil.changeForLog("the retrigger giro doStart end ...."));
    }

    public void prepreview(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the prepreview start ...."));
        ParamUtil.setRequestAttr(bpc.request,RfcConst.FIRSTVIEW,AppConsts.TRUE);
        ParamUtil.setRequestAttr(bpc.request,"needShowErr",AppConsts.FALSE);
        ParamUtil.setRequestAttr(bpc.request,"retriggerGiro","test");
        log.info(StringUtil.changeForLog("the prepreview end ...."));
    }

    public void prePayment(BaseProcessClass bpc) throws Exception {
        log.info(StringUtil.changeForLog("the prePayment start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request,NewApplicationDelegator.APPSUBMISSIONDTO);
        List<AppSubmissionDto> forGiroList = IaisCommonUtils.genNewArrayList();
        if(appSubmissionDto != null){
            String appType = appSubmissionDto.getAppType();
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)){
                FeeDto feeDto = appSubmissionService.getNewAppAmount(appSubmissionDto,NewApplicationHelper.isCharity(bpc.request));
                appSubmissionDto.setFeeInfoDtos(feeDto.getFeeInfoDtos());
                String amountStr = Formatter.formatterMoney(appSubmissionDto.getAmount());
                appSubmissionDto.setAmountStr(amountStr);
                forGiroList.add(appSubmissionDto);
            }else if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)){
                List<AppSubmissionDto> appSubmissionDtoList = IaisCommonUtils.genNewArrayList();
                if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
                    double rfcAmount = 100;
                    if(NewApplicationHelper.isCharity(bpc.request)){
                        rfcAmount=12;
                    }
                    for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                        appSubmissionDto.setLicenceId(appSvcRelatedInfoDto.getLicenceId());
                        AppSubmissionDto oneSvcSubmisonDto = (AppSubmissionDto) CopyUtil.copyMutableObject(appSubmissionDto);
                        List<AppSvcRelatedInfoDto> oneSvcRelateInfoDto = IaisCommonUtils.genNewArrayList();
                        oneSvcRelateInfoDto.add(appSvcRelatedInfoDto);
                        oneSvcSubmisonDto.setAppSvcRelatedInfoDtoList(oneSvcRelateInfoDto);
                        oneSvcSubmisonDto.setServiceName(appSvcRelatedInfoDto.getServiceName());
                        String amountStr  = Formatter.formatterMoney(rfcAmount);
                        oneSvcSubmisonDto.setAmountStr(amountStr);
                        appSubmissionDtoList.add(oneSvcSubmisonDto);
                    }
                }
                forGiroList.add(appSubmissionDto);
                ParamUtil.setSessionAttr(bpc.request,"appSubmissionDtos", (Serializable) appSubmissionDtoList);
            }else if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)){
                List<AppSubmissionDto> rfcAppSubmissionDtos = IaisCommonUtils.genNewArrayList();
                RenewDto renewDto = new RenewDto();
                List<AppSubmissionDto> renewSubmisonDtos = IaisCommonUtils.genNewArrayList();
                List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
                if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos) && !IaisCommonUtils.isEmpty(appGrpPremisesDtos)){
                    for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                        String applicationType = appSvcRelatedInfoDto.getApplicationType();
                        appSubmissionDto.setLicenceId(appSvcRelatedInfoDto.getLicenceId());
                        AppSubmissionDto oneSvcSubmisonDto = (AppSubmissionDto) CopyUtil.copyMutableObject(appSubmissionDto);
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
                boolean isCharity = NewApplicationHelper.isCharity(bpc.request);
                FeeDto renewalAmount;
                if(isCharity){
                    renewalAmount = appSubmissionService.getCharityRenewalAmount(renewSubmisonDtos,isCharity);
                }else {
                    renewalAmount = appSubmissionService.getRenewalAmount(renewSubmisonDtos,isCharity);
                }
                List<AppFeeDetailsDto> appFeeDetailsDto = IaisCommonUtils.genNewArrayList();
                WithOutRenewalDelegator.setSubmissionAmount(renewSubmisonDtos,renewalAmount,appFeeDetailsDto,bpc);
                List<FeeExtDto> gradualFeeList = IaisCommonUtils.genNewArrayList();
                List<FeeExtDto> normalFeeList = IaisCommonUtils.genNewArrayList();
                HashMap<String, List<FeeExtDto>> laterFeeDetailsMap = WithOutRenewalDelegator.getLaterFeeDetailsMap(renewalAmount.getDetailFeeDto(),gradualFeeList,normalFeeList);
                ParamUtil.setRequestAttr(bpc.request, "laterFeeDetailsMap", laterFeeDetailsMap);
                if(!IaisCommonUtils.isEmpty(gradualFeeList)){
                    ParamUtil.setRequestAttr(bpc.request, "gradualFeeList", gradualFeeList);
                }
                if(!IaisCommonUtils.isEmpty(normalFeeList)){
                    ParamUtil.setRequestAttr(bpc.request, "normalFeeList", normalFeeList);
                }

                renewDto.setAppSubmissionDtos(renewSubmisonDtos);
                bpc.request.getSession().setAttribute("rfcAppSubmissionDtos", rfcAppSubmissionDtos);
                ParamUtil.setSessionAttr(bpc.request, RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR, renewDto);
            }
            appSubmissionDto.setAmountStr(Formatter.formatterMoney(appSubmissionDto.getAmount()));
            ParamUtil.setSessionAttr(bpc.request,NewApplicationDelegator.APPSUBMISSIONDTO,appSubmissionDto);
            ParamUtil.setRequestAttr(bpc.request,NewApplicationConstant.ATTR_RELOAD_PAYMENT_METHOD,appSubmissionDto.getPaymentMethod());
        }
        boolean isGiroAcc = false;
        List<SelectOption> giroAccSel = NewApplicationHelper.getGiroAccOptions(forGiroList, appSubmissionDto);
        if(!IaisCommonUtils.isEmpty(giroAccSel)){
            isGiroAcc = true;
            ParamUtil.setRequestAttr(bpc.request, "giroAccSel", giroAccSel);
        }
        ParamUtil.setRequestAttr(bpc.request,"IsGiroAcc",isGiroAcc);
        log.info(StringUtil.changeForLog("the prePayment end ...."));
    }

    public void jumpBank(BaseProcessClass bpc) throws IOException {
        log.info(StringUtil.changeForLog("the jumpBank start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request,NewApplicationDelegator.APPSUBMISSIONDTO);
        RenewDto renewDto = (RenewDto) ParamUtil.getSessionAttr(bpc.request, RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR);
        String payMethod = ParamUtil.getString(bpc.request, "payMethod");
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
        ParamUtil.setSessionAttr(bpc.request,RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR,renewDto);
        ParamUtil.setSessionAttr(bpc.request,NewApplicationDelegator.APPSUBMISSIONDTO,appSubmissionDto);
        String action = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_VALUE);
        Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
        if("next".equals(action)){
            String payMethodErrMsg =  MessageUtil.replaceMessage("GENERAL_ERR0006", "Payment Method", "field");
            if (StringUtil.isEmpty(payMethod)) {
                errorMap.put("payMethod", payMethodErrMsg);
                errorMap.put("pay", payMethodErrMsg);
            }else if(ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(payMethod) && StringUtil.isEmpty(giroAccNum)){
                errorMap.put("payMethod", payMethodErrMsg);
                errorMap.put("pay",payMethodErrMsg);
            }
        }
        if(!"next".equals(action) || !errorMap.isEmpty()){
            ParamUtil.setRequestAttr(bpc.request,"isValid",ISVALID_VALUE_PRE_PAYMENT);
            NewApplicationHelper.setAudiErrMap(false,appSubmissionDto.getAppType(),errorMap,appSubmissionDto.getRfiAppNo(),appSubmissionDto.getLicenceNo());
            ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
            return;
        }

        log.debug("payMethod is {}",payMethod);
        //for test
        //payMethod = ApplicationConsts.PAYMENT_METHOD_NAME_GIRO;
        if (ApplicationConsts.PAYMENT_METHOD_NAME_CREDIT.equals(payMethod)
                || ApplicationConsts.PAYMENT_METHOD_NAME_NETS.equals(payMethod)
                || ApplicationConsts.PAYMENT_METHOD_NAME_PAYNOW.equals(payMethod)) {

            String amount = String.valueOf(appSubmissionDto.getAmount());
            Map<String, String> fieldMap = new HashMap<String, String>();
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
            ParamUtil.setSessionAttr(bpc.request, "txnRefNo", giroTranNo);

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
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request,NewApplicationDelegator.APPSUBMISSIONDTO);
        if(appSubmissionDto != null){
            String pmtMethod = appSubmissionDto.getPaymentMethod();

            String result = ParamUtil.getMaskedString(bpc.request, "result");
            String pmtRefNo = ParamUtil.getMaskedString(bpc.request, "reqRefNo");
            if (!StringUtil.isEmpty(result)) {
                log.info(StringUtil.changeForLog("payment result:" + result));
                if ("success".equals(result) && !StringUtil.isEmpty(pmtRefNo)) {
                    log.info("credit card payment success");
                    String txnDt = ParamUtil.getMaskedString(bpc.request, "txnDt");
                    String txnRefNo = ParamUtil.getMaskedString(bpc.request, "txnRefNo");
                    ParamUtil.setSessionAttr(bpc.request, "txnDt", txnDt);
                    ParamUtil.setSessionAttr(bpc.request, "txnRefNo", txnRefNo);
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
                        errorMap.put("payMethod",err024);
                        ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
                    }
                    switch2 = SWITCH_VALUE_PRE_PAYMENT;
                }
            }else{
                switch2 = SWITCH_VALUE_PRE_PAYMENT;
            }
            boolean isGiroAcc = false;
            List<SelectOption> giroAccSel = NewApplicationHelper.getGiroAccOptions(null, appSubmissionDto);
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
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request,NewApplicationDelegator.APPSUBMISSIONDTO);
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
                AppSubmissionDto targetDto = (AppSubmissionDto) CopyUtil.copyMutableObject(appSubmissionDtoList.get(0));
                targetDto.setPaymentMethod(paymentMethod);
                targetDto.setAmount((double) (100 * appSubmissionDtoList.size()));
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

        String txnRefNo = (String) bpc.request.getSession().getAttribute("txnDt");
        if (StringUtil.isEmpty(txnRefNo)) {
            String txnDt = DateUtil.formatDate(new Date(), "dd/MM/yyyy");
            ParamUtil.setSessionAttr(bpc.request, "txnDt", txnDt);
        }

        List<String> licenseeEmailAddrs = IaisEGPHelper.getLicenseeEmailAddrs(licenseeId);
        String emailAddress = WithOutRenewalDelegator.emailAddressesToString(licenseeEmailAddrs);
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
                    if(ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(hcsaServiceDto.getSvcType())){
                        baseSvcNames.add(hcsaServiceDto.getSvcName());
                    }else if(ApplicationConsts.SERVICE_CONFIG_TYPE_SUBSUMED.equals(hcsaServiceDto.getSvcType())){
                        specifiedSvcNames.add(hcsaServiceDto.getSvcName());
                    }else{
                        otherSvcNames.add(hcsaServiceDto.getSvcName());
                    }
                }
            }
            baseSvcNames = sortSvcNameList(baseSvcNames);
            specifiedSvcNames = sortSvcNameList(specifiedSvcNames);
            otherSvcNames = sortSvcNameList(otherSvcNames);
            serviceNameList.addAll(baseSvcNames);
            serviceNameList.addAll(specifiedSvcNames);
            serviceNameList.addAll(otherSvcNames);
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
