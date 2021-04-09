package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.api.config.GatewayConstants;
import com.ecquaria.cloud.moh.iais.api.config.GatewayStripeConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
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
import com.ecquaria.cloud.moh.iais.constant.RfcConst;
import com.ecquaria.cloud.moh.iais.dto.PmtReturnUrlDto;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    private static final String SWITCH_VALUE_PRE_ACK = "preack";
    private static final String SWITCH_VALUE_PRE_PAYMENT = "prepayment";
    private static final String ISVALID_VALUE_TO_PAYMENT = "topayment";
    private static final String ISVALID_VALUE_TO_BANK = "tobank";

    public void doStart(BaseProcessClass bpc) throws CloneNotSupportedException {
        log.info(StringUtil.changeForLog("the retrigger giro doStart start ...."));
        ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.APPSUBMISSIONDTO, null);
        ParamUtil.setSessionAttr(bpc.request,NewApplicationDelegator.PRIMARY_DOC_CONFIG, null);
        ParamUtil.setSessionAttr(bpc.request,HcsaLicenceFeConstant.DASHBOARDTITLE,"empty");
        ParamUtil.setRequestAttr(bpc.request,HcsaLicenceFeConstant.DASHBOARDTITLE,"empty");
        ParamUtil.setSessionAttr(bpc.request,NewApplicationDelegator.REQUESTINFORMATIONCONFIG,"test");

        String msgId = (String) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_INTER_INBOX_MESSAGE_ID);
        String appGrpNo = ParamUtil.getMaskedString(bpc.request,"appGrpNo");
        log.debug(StringUtil.changeForLog("appGrpNo:")+appGrpNo);
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_NEW_APPLICATION, AuditTrailConsts.FUNCTION_NEW_APPLICATION);
        //init data
        String switch2 = "topreview";
        AppSubmissionDto appSubmissionDto = appSubmissionService.getAppSubmissionDtoByAppGrpNo(appGrpNo);

        /*InterMessageDto interMessageDto = appSubmissionService.getInterMessageById(msgId);
        if (MessageConstants.MESSAGE_STATUS_RESPONSE.equals(interMessageDto.getStatus())) {
            log.debug(StringUtil.changeForLog("message had response"));
            switch2 = SWITCH_VALUE_PRE_ACK;
            ParamUtil.setRequestAttr(bpc.request, SWITCH, switch2);
            ParamUtil.setRequestAttr(bpc.request,NewApplicationDelegator.ACKMESSAGE,MessageUtil.getMessageDesc("INBOX_ERR010"));
            return;
        }
*/
        List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if(IaisCommonUtils.isEmpty(appGrpPremisesDtos) || IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
            log.debug(StringUtil.changeForLog("data error ..."));
            switch2 = SWITCH_VALUE_PRE_ACK;
            ParamUtil.setRequestAttr(bpc.request, SWITCH, switch2);
            ParamUtil.setRequestAttr(bpc.request,NewApplicationDelegator.ACKMESSAGE,"data error !!!");
            return;
        }
        //appSubmissionDto.setRfiMsgId(msgId);
        //set type for page display
        appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
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
            List<HcsaServiceDto> hcsaServiceDtoList = serviceConfigService.getHcsaServiceDtosById(serviceConfigIds);
            ParamUtil.setSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST, (Serializable) hcsaServiceDtoList);
            ParamUtil.setSessionAttr(bpc.request,AppServicesConsts.HCSASERVICEDTOLIST, (Serializable) hcsaServiceDtoList);

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
        appSubmissionDto.setPaymentMethod(payMethod);
        ParamUtil.setSessionAttr(bpc.request,NewApplicationDelegator.APPSUBMISSIONDTO,appSubmissionDto);
        String action = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_VALUE);
        Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
        if("next".equals(action)){
            if (StringUtil.isEmpty(payMethod)) {
                errorMap.put("payMethod",MessageUtil.replaceMessage("GENERAL_ERR0006", "Payment Method", "field"));
                errorMap.put("pay",MessageUtil.replaceMessage("GENERAL_ERR0006", "Payment Method", "field"));
                NewApplicationHelper.setAudiErrMap(false,appSubmissionDto.getAppType(),errorMap,appSubmissionDto.getRfiAppNo(),appSubmissionDto.getLicenceNo());
                ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
            }
        }
        if(!"next".equals(action) || !errorMap.isEmpty()){
            ParamUtil.setRequestAttr(bpc.request,"isValid",ISVALID_VALUE_TO_PAYMENT);
            return;
        }

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
                bpc.response.sendRedirect(url);
            } catch (Exception e) {
                log.info(e.getMessage(), e);
            }
        }
        ParamUtil.setRequestAttr(bpc.request,"isValid",ISVALID_VALUE_TO_BANK);
        log.info(StringUtil.changeForLog("the jumpBank end ...."));
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
                    //update
                    //appSubmissionService.updateMsgStatus(appSubmissionDto.getRfiMsgId(), MessageConstants.MESSAGE_STATUS_RESPONSE);
                    //eic
                    serviceConfigService.saveAppGroupGiroSysnEic(appGrp);
                }else{
                    switch2 = SWITCH_VALUE_PRE_PAYMENT;
                    ParamUtil.setRequestAttr(bpc.request,"RetriggerGiro","test");
                }
            }
            if (SWITCH_VALUE_PRE_ACK.equals(switch2)) {
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
