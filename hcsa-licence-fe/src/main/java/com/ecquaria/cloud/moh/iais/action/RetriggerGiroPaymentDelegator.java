package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.api.config.GatewayConstants;
import com.ecquaria.cloud.moh.iais.api.config.GatewayStripeConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremPhOpenPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceFeConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.PmtReturnUrlDto;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void doStart(BaseProcessClass bpc) throws CloneNotSupportedException {
        log.info(StringUtil.changeForLog("the doStart start ...."));
        ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.APPSUBMISSIONDTO, null);
        ParamUtil.setSessionAttr(bpc.request,HcsaLicenceFeConstant.DASHBOARDTITLE,"empty");
        ParamUtil.setRequestAttr(bpc.request,HcsaLicenceFeConstant.DASHBOARDTITLE,"empty");

        String appGrpNo = ParamUtil.getString(bpc.request,"appGrpNo");
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_NEW_APPLICATION, AuditTrailConsts.FUNCTION_NEW_APPLICATION);
        //init data
        String switch2 = "topreview";
        AppSubmissionDto appSubmissionDto = appSubmissionService.getAppSubmissionDtoByAppGrpNo(appGrpNo);
        List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if(IaisCommonUtils.isEmpty(appGrpPremisesDtos) || IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
            switch2 = "preack";
            ParamUtil.setRequestAttr(bpc.request, SWITCH, switch2);
            ParamUtil.setRequestAttr(bpc.request,NewApplicationDelegator.ACKMESSAGE,"error !!!");
            return;
        }

        appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
        //remove page edit button
        appSubmissionDto.setAppEditSelectDto(new AppEditSelectDto());

        //set premises info
        List<SelectOption> publicHolidayList = serviceConfigService.getPubHolidaySelect();
        if (!IaisCommonUtils.isEmpty(appGrpPremisesDtos)) {
            for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
                appGrpPremisesDto = NewApplicationHelper.setWrkTime(appGrpPremisesDto);
                List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodDtos = appGrpPremisesDto.getAppPremPhOpenPeriodList();
                //set ph name
                NewApplicationHelper.setPhName(appPremPhOpenPeriodDtos,publicHolidayList);
                appGrpPremisesDto.setAppPremPhOpenPeriodList(appPremPhOpenPeriodDtos);
            }
        }
        //set doc name
        List<HcsaSvcDocConfigDto> primaryDocConfig = serviceConfigService.getAllHcsaSvcDocs(null);
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionDto.getAppGrpPrimaryDocDtos();
        NewApplicationHelper.setDocInfo(appGrpPrimaryDocDtos, null, primaryDocConfig, null);
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

            List<String> serviceConfigIds = IaisCommonUtils.genNewArrayList();
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                String currentSvcId = appSvcRelatedInfoDto.getServiceId();
                serviceConfigIds.add(appSvcRelatedInfoDto.getServiceId());
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
            }
            List<HcsaServiceDto> hcsaServiceDtoList = serviceConfigService.getHcsaServiceDtosById(serviceConfigIds);
            ParamUtil.setSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST, (Serializable) hcsaServiceDtoList);
            ParamUtil.setSessionAttr(bpc.request,AppServicesConsts.HCSASERVICEDTOLIST, (Serializable) hcsaServiceDtoList);

        }else{
            log.info("appSvcRelatedInfoDtos is empty ...");
        }
        ParamUtil.setSessionAttr(bpc.request,NewApplicationDelegator.APPSUBMISSIONDTO,appSubmissionDto);
        ParamUtil.setRequestAttr(bpc.request, SWITCH, switch2);
        log.info(StringUtil.changeForLog("the doStart end ...."));
    }

    public void prepreview(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the prepreview start ...."));

        log.info(StringUtil.changeForLog("the prepreview end ...."));
    }

    public void prePayment(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the prePayment start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request,NewApplicationDelegator.APPSUBMISSIONDTO);
        if(appSubmissionDto != null){
            FeeDto feeDto = appSubmissionService.getNewAppAmount(appSubmissionDto,NewApplicationHelper.isCharity(bpc.request));
            appSubmissionDto.setFeeInfoDtos(feeDto.getFeeInfoDtos());
            String amountStr = Formatter.formatterMoney(appSubmissionDto.getAmount());
            appSubmissionDto.setAmountStr(amountStr);
            ParamUtil.setSessionAttr(bpc.request,NewApplicationDelegator.APPSUBMISSIONDTO,appSubmissionDto);
            ParamUtil.setRequestAttr(bpc.request,"RetriggerGiro","test");
        }

        log.info(StringUtil.changeForLog("the prePayment end ...."));
    }

    public void jumpBank(BaseProcessClass bpc) throws IOException {
        log.info(StringUtil.changeForLog("the jumpBank start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request,NewApplicationDelegator.APPSUBMISSIONDTO);
        String payMethod = ParamUtil.getString(bpc.request, "payMethod");
        if (StringUtil.isEmpty(payMethod)) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE, "payment");
            Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
            errorMap.put("payMethod","payMethod is empty");
            NewApplicationHelper.setAudiErrMap(false,appSubmissionDto.getAppType(),errorMap,appSubmissionDto.getRfiAppNo(),appSubmissionDto.getLicenceNo());
            return;
        }

        Double totalAmount = appSubmissionDto.getAmount();
        if (totalAmount == 0.0) {
            StringBuilder url = new StringBuilder();
            url.append("https://")
                    .append(bpc.request.getServerName())
                    .append("/hcsa-licence-web/eservice/INTERNET/MohNewApplication/PrepareAckPage");
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);
            return;
        }
        if (ApplicationConsts.PAYMENT_METHOD_NAME_CREDIT.equals(payMethod)
                || ApplicationConsts.PAYMENT_METHOD_NAME_NETS.equals(payMethod)
                || ApplicationConsts.PAYMENT_METHOD_NAME_PAYNOW.equals(payMethod)) {

            String amount = String.valueOf(appSubmissionDto.getAmount());
            Map<String, String> fieldMap = new HashMap<String, String>();
            fieldMap.put(GatewayConstants.AMOUNT_KEY, amount);
            fieldMap.put(GatewayConstants.PYMT_DESCRIPTION_KEY, payMethod);
            fieldMap.put(GatewayConstants.SVCREF_NO, appSubmissionDto.getAppGrpNo()+"_"+System.currentTimeMillis());
            PmtReturnUrlDto pmtReturnUrlDto = new PmtReturnUrlDto();
            pmtReturnUrlDto.setCreditRetUrl(GatewayStripeConfig.retrigger_giro_paymeny_return_rul);
            pmtReturnUrlDto.setPayNowRetUrl(GatewayStripeConfig.retrigger_giro_paymeny_return_rul);
            pmtReturnUrlDto.setNetsRetUrl(GatewayStripeConfig.retrigger_giro_paymeny_return_rul);
            pmtReturnUrlDto.setOtherRetUrl(GatewayStripeConfig.retrigger_giro_paymeny_return_rul);
            try {
                String url = NewApplicationHelper.genBankUrl(bpc.request,payMethod,fieldMap,pmtReturnUrlDto);
                bpc.response.sendRedirect(url);
            } catch (Exception e) {
                log.info(e.getMessage(), e);
            }

        }else if (ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(payMethod)) {
            String appGrpId = appSubmissionDto.getAppGrpId();
            ApplicationGroupDto appGrp = new ApplicationGroupDto();
            appGrp.setId(appGrpId);
            appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_GIRO_PAY_SUCCESS);
            serviceConfigService.updateAppGrpPmtStatus(appGrp);
            ParamUtil.setRequestAttr(bpc.request, "PmtStatus", ApplicationConsts.PAYMENT_METHOD_NAME_GIRO);
            StringBuilder url = new StringBuilder();
            url.append("https://")
                    .append(bpc.request.getServerName())
                    .append("/hcsa-licence-web/eservice/INTERNET/MohRetriggerGiroPayment/preAck");
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);
            return;
        }

        log.info(StringUtil.changeForLog("the jumpBank end ...."));
    }

    public void doPayment(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the doPayment start ...."));
        String switch2 = "prepayment";
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request,NewApplicationDelegator.APPSUBMISSIONDTO);
        if(appSubmissionDto != null){
            String pmtMethod = appSubmissionDto.getPaymentMethod();

            if (!StringUtil.isEmpty(pmtMethod) && ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(pmtMethod)) {
                switch2 = "preack";
                String txnDt = DateUtil.formatDate(new Date(), "dd/MM/yyyy");
                ParamUtil.setSessionAttr(bpc.request, "txnDt", txnDt);
            }
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
                    switch2 = "preack";
                    //update status
                    String appGrpId = appSubmissionDto.getAppGrpId();
                    ApplicationGroupDto appGrp = new ApplicationGroupDto();
                    appGrp.setId(appGrpId);
                    appGrp.setPmtRefNo(pmtRefNo);
                    appGrp.setPaymentDt(new Date());
                    appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
                    serviceConfigService.updateAppGrpPmtStatus(appGrp);
                }else{
                    switch2 = "prepayment";
                    ParamUtil.setRequestAttr(bpc.request,"RetriggerGiro","test");
                }
            }
            if ("preack".equals(switch2)) {
                ParamUtil.setRequestAttr(bpc.request, NewApplicationDelegator.ACKMESSAGE, "payment success !!!");
            }
        }
        ParamUtil.setRequestAttr(bpc.request, SWITCH, switch2);
        log.info(StringUtil.changeForLog("the doPayment end ...."));
    }

    public void preAck(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the preAck start ...."));


        log.info(StringUtil.changeForLog("the preAck end ...."));
    }

}
