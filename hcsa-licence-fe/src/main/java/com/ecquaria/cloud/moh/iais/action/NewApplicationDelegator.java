package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.api.config.GatewayConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppLicBundleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionRequestInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationSubDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.RenewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessHciDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppSpecifiedLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterInboxUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaAppConst;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.AppSelectSvcDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AppValidatorHelper;
import com.ecquaria.cloud.moh.iais.helper.ApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.SelfAssessmentService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.client.AppGrpPaymentClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationFeClient;
import com.ecquaria.cloud.moh.iais.service.client.CessationClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.util.DealSessionUtil;
import com.ecquaria.sz.commons.util.Calculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.ACKMESSAGE;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.ACKSTATUS;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.ACK_APP_SUBMISSIONS;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.APPSUBMISSIONDTO;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.APP_SUBMISSIONS;

/**
 * Process: MohNewApplication
 *
 * @author suocheng
 * @date 9/23/2019
 */
@Delegator("newApplicationDelegator")
@Slf4j
public class NewApplicationDelegator extends AppCommDelegator {

    @Autowired
    private ServiceConfigService serviceConfigService;
    @Autowired
    private AppSubmissionService appSubmissionService;
    @Autowired
    private RequestForChangeService requestForChangeService;
    @Autowired
    private LicenceClient licenceClient;
    @Autowired
    private CessationClient cessationClient;

    @Autowired
    private ApplicationFeClient applicationFeClient;

    @Autowired
    private AppGrpPaymentClient appGrpPaymentClient;

    @Autowired
    private FeEicGatewayClient feEicGatewayClient;

    @Autowired
    private SelfAssessmentService selfAssessmentService;

    @Override
    protected void loadingDraft(HttpServletRequest request, String draftNo) {
        log.info(StringUtil.changeForLog("the do loadingDraft start ...."));
        Object draftNumber = request.getSession().getAttribute(HcsaAppConst.DRAFT_NUMBER);
        if (draftNumber != null) {
            draftNo = (String) draftNumber;
        }
        //draftNo = "DN191118000001";
        if (!StringUtil.isEmpty(draftNo)) {
            log.info(StringUtil.changeForLog("draftNo is not empty"));
            AppSubmissionDto appSubmissionDto = serviceConfigService.getAppSubmissionDtoDraft(draftNo);
            if (appSubmissionDto != null) {
                if (IaisCommonUtils.isEmpty(appSubmissionDto.getAppSvcRelatedInfoDtoList())) {
                    log.info(StringUtil.changeForLog("appSvcRelatedInfoDtoList is empty"));
                }
                if (appSubmissionDto.getAppGrpPremisesDtoList() != null && appSubmissionDto.getAppGrpPremisesDtoList().size() > 0) {
                    resolveReadonly(appSubmissionDto.getAppType(), appSubmissionDto);
                    ParamUtil.setSessionAttr(request, APPSUBMISSIONDTO, appSubmissionDto);
                } else {
                    ParamUtil.setSessionAttr(request, APPSUBMISSIONDTO, null);
                }
                ParamUtil.setSessionAttr(request, HcsaAppConst.DRAFTCONFIG, "test");
            }
            ParamUtil.setSessionAttr(request, HcsaAppConst.SELECT_DRAFT_NO, null);
        }
        if (draftNumber != null) {
            //ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.DRAFTCONFIG, null);
            String entryType = ParamUtil.getString(request, "entryType");
            if (!StringUtil.isEmpty(entryType) && "assessment".equals(entryType)) {
                ParamUtil.setSessionAttr(request, HcsaAppConst.ASSESSMENTCONFIG, "test");
            }
        }
        log.info(StringUtil.changeForLog("the do loadingDraft end ...."));
    }

    @Override
    protected void requestForInformationLoading(HttpServletRequest request, String appNo) {
        log.info(StringUtil.changeForLog("the do requestForInformationLoading start ...."));
        String msgId = (String) ParamUtil.getSessionAttr(request, AppConsts.SESSION_INTER_INBOX_MESSAGE_ID);
        //msgId = "415199C2-4AAA-42BF-B068-9B019BF1ED1C";
        log.info(StringUtil.changeForLog("MsgId: " + msgId));
        if (!StringUtil.isEmpty(appNo) && !StringUtil.isEmpty(msgId)) {
            AppSubmissionDto appSubmissionDto = appCommService.getRfiAppSubmissionDtoByAppNo(appNo);
            if (appSubmissionDto != null) {
                appSubmissionDto.setAmountStr("N/A");
            }
            InterMessageDto interMessageDto = appSubmissionService.getInterMessageById(msgId);
            if (MessageConstants.MESSAGE_STATUS_RESPONSE.equals(interMessageDto.getStatus())) {
                appSubmissionDto = null;
            }

            if (appSubmissionDto != null) {
                loadingRfiGrpServiceConfig(appSubmissionDto, request);
                svcRelatedInfoRFI(appSubmissionDto, appNo);
                appSubmissionDto.setRfiAppNo(appNo);
                //clear svcDoc id
                List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
                if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)) {
                    for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                        List<AppSvcDocDto> appSvcDocDtos = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
                        if (!IaisCommonUtils.isEmpty(appSvcDocDtos)) {
                            for (AppSvcDocDto appSvcDocDto : appSvcDocDtos) {
                                appSvcDocDto.setId(null);
                            }
                            appSvcRelatedInfoDto.setAppSvcDocDtoLit(appSvcDocDtos);
                        }
                    }
                    appSubmissionDto.setAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtos);
                }
                String appType = appSubmissionDto.getAppType();
                boolean isRenewalOrRfc = ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(
                        appType) || ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType);
                appSubmissionDto.setNeedEditController(true);
                AppEditSelectDto appEditSelectDto = appSubmissionDto.getAppEditSelectDto();
                if (appEditSelectDto == null) {
                    appEditSelectDto = new AppEditSelectDto();
                    appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
                }
                if (isRenewalOrRfc) {
                    // set the required information
                    String licenceId = appSubmissionDto.getLicenceId();
                    LicenceDto licenceById = requestForChangeService.getLicenceById(licenceId);
                    if (licenceById != null) {
                        appSubmissionDto.setLicenceNo(licenceById.getLicenceNo());
                    } else {
                        log.warn(StringUtil.changeForLog("##### No Active Licence for this ID: " + licenceId));
                    }
                }
                ParamUtil.setSessionAttr(request, APPSUBMISSIONDTO, appSubmissionDto);
                //DealSessionUtil.initCoMap(request);
                //control premises edit
                handlePremises(appSubmissionDto, appNo);
                ParamUtil.setSessionAttr(request, APPSUBMISSIONDTO, appSubmissionDto);
            } else {
                ApplicationDto applicationDto = appSubmissionService.getMaxVersionApp(appNo);
                if (applicationDto != null) {
                    HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(applicationDto.getServiceId());
                    if (hcsaServiceDto != null) {
                        List<HcsaServiceDto> hcsaServiceDtoList = IaisCommonUtils.genNewArrayList();
                        hcsaServiceDtoList.add(hcsaServiceDto);
                        ParamUtil.setSessionAttr(request, HcsaAppConst.RFI_REPLY_SVC_DTO, (Serializable) hcsaServiceDtoList);
                    }
                    ParamUtil.setRequestAttr(request, "APPLICATION_TYPE", applicationDto.getApplicationType());
                    String errMsg = MessageUtil.getMessageDesc("INBOX_ERR001");
                    jumpToAckPage(request, HcsaAppConst.ACK_STATUS_ERROR, errMsg);
                }
            }
            ParamUtil.setSessionAttr(request, HcsaAppConst.REQUESTINFORMATIONCONFIG, "test");
        }
        log.info(StringUtil.changeForLog("the do requestForInformationLoading end ...."));
    }

    private static void jumpToAckPage(HttpServletRequest request, String ackStatus, String errorMsg) {
        String actionType = (String) ParamUtil.getRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if (StringUtil.isEmpty(actionType)) {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, "errorAck");
            if (HcsaAppConst.ACK_STATUS_ERROR.equals(ackStatus)) {
                ParamUtil.setRequestAttr(request, ACKSTATUS, "error");
                ParamUtil.setRequestAttr(request, ACKMESSAGE, errorMsg);
            }
        }
    }

    @Override
    protected void loadingNewAppInfo(HttpServletRequest request) {
        log.info(StringUtil.changeForLog("the do loadingSpecifiedInfo start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request, APPSUBMISSIONDTO);
        List<AppLicBundleDto> appLicBundleDtoList = (List<AppLicBundleDto>) ParamUtil.getSessionAttr(request,
                HcsaAppConst.APP_LIC_BUNDLE_LIST);
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = (List<AppSvcRelatedInfoDto>) ParamUtil.getSessionAttr(request,
                HcsaAppConst.APP_SVC_RELATED_INFO_LIST);
        AppSelectSvcDto appSelectSvcDto = (AppSelectSvcDto) ParamUtil.getSessionAttr(request, HcsaAppConst.APP_SELECT_SERVICE);
        if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos) && appSelectSvcDto != null && !ApplicationHelper.checkIsRfi(request)) {
            boolean fromLic = true;
            if (appSubmissionDto == null) {
                String entryType = ParamUtil.getString(request, "entryType");
                if (!StringUtil.isEmpty(entryType) && "assessment".equals(entryType)) {
                    ParamUtil.setSessionAttr(request, HcsaAppConst.ASSESSMENTCONFIG, "test");
                }
                String appType = ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION;
                appSubmissionDto = new AppSubmissionDto();
                appSubmissionDto.setLicenseeId(ApplicationHelper.getLicenseeId(request));
                appSubmissionDto.setAppType(appType);
                appSubmissionDto.setAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtos);
                ParamUtil.setSessionAttr(request, IaisEGPConstant.GLOBAL_MAX_INDEX_SESSION_ATTR, 0);
            }
            String appType = appSubmissionDto.getAppType();
            if (!ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                return;
            }
            boolean isMsBundle = false;
            if (IaisCommonUtils.isNotEmpty(appLicBundleDtoList)) {
                AppLicBundleDto appLicBundleDto = appLicBundleDtoList.get(0);
                fromLic = appLicBundleDto.isLicOrApp();
                isMsBundle = AppServicesConsts.SERVICE_CODE_MEDICAL_SERVICE.equals(appLicBundleDto.getSvcCode())||AppServicesConsts.SERVICE_CODE_DENTAL_SERVICE.equals(appLicBundleDto.getSvcCode());
                //appSubmissionDto.setAppLicBundleDtoList(appLicBundleDtoList);
            }
            String premisesId = "";
            for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSubmissionDto.getAppSvcRelatedInfoDtoList()) {
                String premId = appSvcRelatedInfoDto.getLicPremisesId();
                if (!StringUtil.isEmpty(premId) && !"-1".equals(premId)) {
                    premisesId = premId;
                    break;
                }
            }
            if (StringUtil.isEmpty(premisesId)) {
                for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                    String premId = appSvcRelatedInfoDto.getLicPremisesId();
                    if (!StringUtil.isEmpty(premId) && !"-1".equals(premId)) {
                        premisesId = premId;
                        break;
                    }
                }
                appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).setLicPremisesId(premisesId);
            }
            boolean checkReadonly = false;
            if (!StringUtil.isEmpty(premisesId) && !isMsBundle) {
                List<AppGrpPremisesDto> appGrpPremisesDtos;
                if (fromLic) {
                    appGrpPremisesDtos = licCommService.getLicPremisesInfo(premisesId);
                } else {
                    appGrpPremisesDtos = IaisCommonUtils.genNewArrayListWithData(appCommService.getAppGrpPremisesById(premisesId));
                }
                appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtos);
                checkReadonly = true;
            } else if (IaisCommonUtils.isEmpty(appSubmissionDto.getAppGrpPremisesDtoList())) {
                List<AppGrpPremisesDto> appGrpPremisesDtos = IaisCommonUtils.genNewArrayList();
                AppGrpPremisesDto appGrpPremisesDto = new AppGrpPremisesDto();
                appGrpPremisesDto.setPremisesIndexNo(UUID.randomUUID().toString());
                appGrpPremisesDtos.add(appGrpPremisesDto);
                appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtos);
            }
            appSubmissionDto.setAppLicBundleDtos(genAppLicBundleDtos(appSubmissionDto, appLicBundleDtoList));
            if (checkReadonly) {
                resolveReadonly(appType, appSubmissionDto);
            }
            ParamUtil.setSessionAttr(request, APPSUBMISSIONDTO, appSubmissionDto);
        }
        log.info(StringUtil.changeForLog("the do loadingSpecifiedInfo start ...."));
    }

    private void resolveReadonly(String appType, AppSubmissionDto appSubmissionDto) {
        if (!ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType) || appSubmissionDto == null) {
            return;
        }
        if (IaisCommonUtils.isEmpty(appSubmissionDto.getAppSvcRelatedInfoDtoList())) {
            return;
        }
        List<String> svcNames = appSubmissionDto.getAppSvcRelatedInfoDtoList().stream()
                .map(AppSvcRelatedInfoDto::getServiceCode)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (svcNames.contains(AppServicesConsts.SERVICE_CODE_MEDICAL_SERVICE)
                || svcNames.contains(AppServicesConsts.SERVICE_CODE_DENTAL_SERVICE)
                && !svcNames.contains(AppServicesConsts.SERVICE_CODE_ACUTE_HOSPITAL)) {
            appSubmissionDto.setReadonlyPrem(false);
            return;
        }

        String licPremisesId = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getLicPremisesId();
        if (StringUtil.isEmpty(licPremisesId)) {
            appSubmissionDto.setReadonlyPrem(false);
            return;
        }
        List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
        if (IaisCommonUtils.isEmpty(appGrpPremisesDtos) || !ApplicationHelper.isSpecialValue(
                appGrpPremisesDtos.get(0).getPremisesSelect())) {
            appSubmissionDto.setReadonlyPrem(false);
            return;
        }
        appGrpPremisesDtos.get(0).setExistingData(AppConsts.YES);
        if (StringUtil.isEmpty(appGrpPremisesDtos.get(0).getPremisesIndexNo())) {
            appGrpPremisesDtos.get(0).setPremisesIndexNo(UUID.randomUUID().toString());
        }
        appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtos);
        appSubmissionDto.setReadonlyPrem(true);
    }

    /**
     * StartStep: PreparePayment
     *
     * @param bpc
     * @throws
     */
    @Override
    public void preparePayment(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do preparePayment start ...."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        List<AppSubmissionDto> appSubmissionDtos = (List<AppSubmissionDto>) bpc.request.getSession().getAttribute(APP_SUBMISSIONS);
        String paymentMethod;
        //get transfer info
        AppSubmissionDto tranferSub = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, "app-rfc-tranfer");
        if (tranferSub != null) {
            if (appSubmissionDtos == null) {
                appSubmissionDtos = new ArrayList<>(1);
                List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = tranferSub.getAppSvcRelatedInfoDtoList();
                if (appSvcRelatedInfoDtoList != null) {
                    for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtoList) {
                        appSvcRelatedInfoDto.setGroupNo(tranferSub.getAppGrpNo());
                    }
                }
                appSubmissionDtos.add(tranferSub);
            }
            String transferFlag = appSubmissionDto.getTransferFlag();
            appSubmissionDto = tranferSub;
            appSubmissionDto.setTransferFlag(transferFlag);
            //reload transfer payment method
            paymentMethod = tranferSub.getPaymentMethod();
        } else {
            //reload new/rfc payment method
            paymentMethod = appSubmissionDto.getPaymentMethod();
        }
        Double total = 0.0;
        if (!ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(
                appSubmissionDto.getAppType()) && !ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(
                appSubmissionDto.getAppType())) {
            total += appSubmissionDto.getAmount();
        }
        if (appSubmissionDtos != null && !appSubmissionDtos.isEmpty()) {
            for (AppSubmissionDto appSubmissionDto1 : appSubmissionDtos) {
                Double amount = appSubmissionDto1.getAmount();
                if (amount != null) {
                    total = total + amount;
                }
                String amountStr = Formatter.formatterMoney(appSubmissionDto1.getAmount());
                appSubmissionDto1.setAmountStr(amountStr);
                appSubmissionDto1.setServiceName(appSubmissionDto1.getAppSvcRelatedInfoDtoList().get(0).getServiceName());
            }
        }
        appSubmissionDto.setAmount(total);
        if (!StringUtil.isEmpty(appSubmissionDto.getAmount())) {
            String amountStr = Formatter.formatterMoney(appSubmissionDto.getAmount());
            log.info(StringUtil.changeForLog("The amountStr is -->:" + amountStr));
            appSubmissionDto.setAmountStr(amountStr);
        }
        if (appSubmissionDtos != null) {
            bpc.request.getSession().setAttribute(APP_SUBMISSIONS, appSubmissionDtos);
        }
        if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())) {
            Set<String> premTypes = IaisCommonUtils.genNewHashSet();
            List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
            if (!IaisCommonUtils.isEmpty(appGrpPremisesDtos)) {
                for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
                    premTypes.add(appGrpPremisesDto.getPremisesType());
                }
            }
        }
        String flag = bpc.request.getParameter("flag");
        if (!StringUtil.isEmpty(flag)) {
            appSubmissionDto.setTransferFlag(flag);
        }
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        bpc.request.setAttribute("flag", appSubmissionDto.getTransferFlag());
        bpc.request.setAttribute("transfer", appSubmissionDto.getTransferFlag());
        ParamUtil.setRequestAttr(bpc.request, "IsCharity", ApplicationHelper.isCharity(bpc.request));
        boolean isGiroAcc = false;
        List<SelectOption> giroAccSel = ApplicationHelper.getGiroAccOptions(appSubmissionDtos, appSubmissionDto);
        if (!IaisCommonUtils.isEmpty(giroAccSel)) {
            isGiroAcc = true;
            ParamUtil.setRequestAttr(bpc.request, "giroAccSel", giroAccSel);
        }
        ParamUtil.setRequestAttr(bpc.request, "IsGiroAcc", isGiroAcc);
        ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.ATTR_RELOAD_PAYMENT_METHOD, paymentMethod);
        log.info(StringUtil.changeForLog("the do preparePayment end ...."));
    }

    /**
     * StartStep: DoPremises
     *
     * @param bpc
     * @throws
     */
    @Override
    public void doPremises(BaseProcessClass bpc) {
        super.doPremises(bpc);
    }

    /**
     * StartStep: doPreview
     *
     * @param bpc
     * @throws
     */
    @Override
    public void doPayment(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do doPayment start ...."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        //for relation Licences when RFC the premises.
        List<AppSubmissionDto> ackSubmissionDtos = (List<AppSubmissionDto>) bpc.request.getSession().getAttribute(ACK_APP_SUBMISSIONS);
        String switch2 = "loading";
        String pmtMethod = appSubmissionDto.getPaymentMethod();
        if (StringUtil.isEmpty(pmtMethod)) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "payment");
            return;
        }
        if (!StringUtil.isEmpty(pmtMethod) && ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(pmtMethod)) {
            switch2 = "ack";
            String txnDt = Formatter.formatDate(new Date());
            ParamUtil.setSessionAttr(bpc.request, "txnDt", txnDt);
        }
        String result = ParamUtil.getMaskedString(bpc.request, "result");
        String pmtRefNo = ParamUtil.getMaskedString(bpc.request, "reqRefNo");
        log.info(StringUtil.changeForLog("Payment result: " + result + "; reqRefNo: " + pmtRefNo
                + "; Draft No.: " + appSubmissionDto.getDraftNo() + "; App Group No: " + appSubmissionDto.getAppGrpNo()));
        if (!StringUtil.isEmpty(result)) {
            if ("success".equals(result) && !StringUtil.isEmpty(pmtRefNo)) {
                log.debug(StringUtil.changeForLog("online payment success ..."));
                try {
                    if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())) {
                        log.info(StringUtil.changeForLog("RFC Email Sending ..."));
                        List<AppSubmissionDto> appSubmissionDtos1 = (List<AppSubmissionDto>) ParamUtil.getSessionAttr(bpc.request,
                                APP_SUBMISSIONS);
                        if (appSubmissionDtos1 == null || appSubmissionDtos1.size() == 0) {
                            appSubmissionDtos1 = IaisCommonUtils.genNewArrayList();
                            appSubmissionDtos1.add(appSubmissionDto);
                        }
                        requestForChangeService.sendRfcSubmittedEmail(appSubmissionDtos1, appSubmissionDto.getPaymentMethod());
                    }
                } catch (Exception e) {
                    log.info(StringUtil.changeForLog(e.getMessage()), e);
                }
                String txnDt = ParamUtil.getMaskedString(bpc.request, "txnDt");
                String txnRefNo = ParamUtil.getMaskedString(bpc.request, "txnRefNo");
                ParamUtil.setSessionAttr(bpc.request, "txnDt", txnDt);
                ParamUtil.setSessionAttr(bpc.request, "txnRefNo", txnRefNo);

                List<String> appGrpIds = IaisCommonUtils.genNewArrayList();
                if (ackSubmissionDtos != null) {
                    for (AppSubmissionDto appSubmissionDto1 : ackSubmissionDtos) {
                        appSubmissionService.updatePayment(appSubmissionDto1, pmtRefNo);
                        appGrpIds.add(appSubmissionDto1.getAppGrpId());
                    }
                }
                switch2 = "ack";

                //update status for transfor payment
                String appGrpId = appSubmissionDto.getAppGrpId();
                if (!appGrpIds.contains(appGrpId)) {
                    appSubmissionService.updatePayment(appSubmissionDto, pmtRefNo);
                }
                //send email
                if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())) {
                    try {
                        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request,
                                AppConsts.SESSION_ATTR_LOGIN_USER);
                        appSubmissionService.sendEmailAndSMSAndMessage(appSubmissionDto, loginContext.getUserName());
                    } catch (Exception e) {
                        log.error(StringUtil.changeForLog("send email error ...."));
                    }
                }
            } else {
                appSubmissionService.updateDraftStatus(appSubmissionDto.getDraftNo(), AppConsts.COMMON_STATUS_ACTIVE);
                if (!"cancelled".equals(result)) {
                    Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
                    errorMap.put("pay", MessageUtil.getMessageDesc("NEW_ERR0024"));
                    AppValidatorHelper.setAudiErrMap(ApplicationHelper.checkIsRfi(bpc.request), appSubmissionDto.getAppType(),
                            errorMap,
                            appSubmissionDto.getRfiAppNo(), appSubmissionDto.getLicenceNo());
                    ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
                }
                switch2 = "loading";
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "payment");
            }
        } else {
            PaymentRequestDto paymentRequestDtoSuss = null;
            List<PaymentRequestDto> paymentRequestDtos = appGrpPaymentClient.getPaymentRequestDtoByReqRefNoLike(
                    AppConsts.MOH_IAIS_SYSTEM_PAYMENT_CLIENT_KEY, appSubmissionDto.getAppGrpNo()).getEntity();
            if (paymentRequestDtos != null) {
                for (PaymentRequestDto paymentRequestDto : paymentRequestDtos) {
                    if ("success".equals(paymentRequestDto.getStatus())) {
                        paymentRequestDtoSuss = paymentRequestDto;
                        break;
                    }
                }
            }

            PaymentDto paymentDto = appGrpPaymentClient.getPaymentDtoByReqRefNo(
                    AppConsts.MOH_IAIS_SYSTEM_PAYMENT_CLIENT_KEY, appSubmissionDto.getAppGrpNo()).getEntity();
            if (paymentRequestDtoSuss != null) {
                PaymentDto paymentDtoSuss = appGrpPaymentClient.getPaymentDtoByReqRefNo(
                        AppConsts.MOH_IAIS_SYSTEM_PAYMENT_CLIENT_KEY, paymentRequestDtoSuss.getReqRefNo()
                ).getEntity();
                if (paymentDtoSuss != null) {
                    paymentDto = paymentDtoSuss;
                }
            }
            if (paymentDto != null && "success".equals(paymentDto.getPmtStatus())) {
                pmtRefNo = paymentDto.getReqRefNo();
                log.debug(StringUtil.changeForLog("online payment success ..."));
                try {
                    if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())) {
                        List<AppSubmissionDto> appSubmissionDtos1 = (List<AppSubmissionDto>) ParamUtil.getSessionAttr(bpc.request,
                                APP_SUBMISSIONS);
                        if (appSubmissionDtos1 == null || appSubmissionDtos1.size() == 0) {
                            appSubmissionDtos1 = IaisCommonUtils.genNewArrayList();
                            appSubmissionDtos1.add(appSubmissionDto);
                        }
                        requestForChangeService.sendRfcSubmittedEmail(appSubmissionDtos1, appSubmissionDto.getPaymentMethod());
                    }
                } catch (Exception e) {
                    log.info(StringUtil.changeForLog(e.getMessage()), e);
                }

                List<String> appGrpIds = IaisCommonUtils.genNewArrayList();
                if (ackSubmissionDtos != null) {
                    for (AppSubmissionDto appSubmissionDto1 : ackSubmissionDtos) {
                        appSubmissionService.updatePayment(appSubmissionDto1, pmtRefNo);
                        appGrpIds.add(appSubmissionDto1.getAppGrpId());
                    }
                }
                String txnDt = ParamUtil.getMaskedString(bpc.request, "txnDt");
                String txnRefNo = ParamUtil.getMaskedString(bpc.request, "txnRefNo");
                ParamUtil.setSessionAttr(bpc.request, "txnDt", txnDt);
                ParamUtil.setSessionAttr(bpc.request, "txnRefNo", txnRefNo);
                switch2 = "ack";

                //update status for transfor payment
                String appGrpId = appSubmissionDto.getAppGrpId();
                if (!appGrpIds.contains(appGrpId)) {
                    appSubmissionService.updatePayment(appSubmissionDto, pmtRefNo);
                }
                //send email
                if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())) {
                    try {
                        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request,
                                AppConsts.SESSION_ATTR_LOGIN_USER);
                        appSubmissionService.sendEmailAndSMSAndMessage(appSubmissionDto, loginContext.getUserName());
                    } catch (Exception e) {
                        log.error(StringUtil.changeForLog("send email error ...."));
                    }
                }
            } else {
                appSubmissionService.updateDraftStatus(appSubmissionDto.getDraftNo(), AppConsts.COMMON_STATUS_ACTIVE);
                log.debug(StringUtil.changeForLog("result is empty"));
                switch2 = "loading";
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "payment");
            }
        }

        if ("ack".equals(switch2)) {
            ParamUtil.setRequestAttr(bpc.request, ACKMESSAGE, "payment success !!!");
        }
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE, switch2);
        log.info(StringUtil.changeForLog("the do doPayment end ...."));
    }

    /**
     * StartStep: doSaveDraft
     *
     * @param bpc
     * @throws
     */
    @Override
    public void doSaveDraft(BaseProcessClass bpc) throws IOException {
        log.info(StringUtil.changeForLog("the do doSaveDraft start ...."));
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(
                HttpHandler.SOP6_MULTIPART_REQUEST);
        String crud_action_additional;
        if (mulReq != null) {
            crud_action_additional = mulReq.getParameter("crud_action_additional");
        } else {
            crud_action_additional = bpc.request.getParameter("crud_action_additional");
        }
        if ("jumpPage".equals(crud_action_additional)) {
            jumpYeMian(bpc.request, bpc.response);
            return;
        }

        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        if (StringUtil.isEmpty(appSubmissionDto.getDraftNo())) {
            String draftNo = appSubmissionService.getDraftNo(appSubmissionDto.getAppType());
            log.info(StringUtil.changeForLog("the draftNo -->:" + draftNo));
            appSubmissionDto.setDraftNo(draftNo);
        } else {
            appSubmissionDto.setOldDraftNo(null);
        }
        String oldDraftNo = (String) bpc.request.getSession().getAttribute(HcsaAppConst.SELECT_DRAFT_NO);
        bpc.request.getSession().removeAttribute(HcsaAppConst.SELECT_DRAFT_NO);
        appSubmissionDto.setOldDraftNo(oldDraftNo);

        DealSessionUtil.initMaxFileIndex(appSubmissionDto, bpc.request);
        //set psn dropdown
        //setPsnDroTo(appSubmissionDto, bpc);
        appSubmissionDto = appSubmissionService.doSaveDraft(appSubmissionDto);
        if ("exitSaveDraft".equals(crud_action_additional)) {
            jumpYeMian(bpc.request, bpc.response);
            return;
        }
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        bpc.request.setAttribute("saveDraftSuccess", "success");
        log.info(StringUtil.changeForLog("the do doSaveDraft end ...."));
    }

    @Override
    public void jumpYeMian(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(
                "https://" + request.getServerName() + "/main-web/eservice/INTERNET/MohInternetInbox", request);
        IaisEGPHelper.redirectUrl(response, tokenUrl);
    }

    private void sendURL(HttpServletRequest request, HttpServletResponse response, String url) {
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url, request);
        try {
            IaisEGPHelper.redirectUrl(response, tokenUrl);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void inboxToPreview(BaseProcessClass bpc) {
        // View and Print
        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.IS_VIEW, "Y");
        String appNo = ParamUtil.getMaskedString(bpc.request, "appNo");
        if (!StringUtil.isEmpty(appNo)) {
            ApplicationDto applicationDto = appCommService.getApplicationDtoByAppNo(appNo);
            if (applicationDto != null) {
                if (ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION.equals(applicationDto.getStatus())) {
                    String applicationMsgNo = getApplicationMsgNo(appNo);
                    if (StringUtil.isNotEmpty(applicationMsgNo)) {
                        List<InterMessageDto> interMessageDtos = appSubmissionService.getInterMessageByRefNo(applicationMsgNo);
                        Optional<InterMessageDto> interMessageDtoOptional = interMessageDtos.stream().filter(
                                interMessageDto -> !MessageConstants.MESSAGE_STATUS_RESPONSE.equals(
                                        interMessageDto.getStatus())).findFirst();
                        if (interMessageDtoOptional.isPresent()) {
                            InterMessageDto interMessageBySubjectLike = interMessageDtoOptional.get();
                            List<AppEditSelectDto> entity = applicationFeClient.getAppEditSelectDtos(applicationDto.getId(),
                                    ApplicationConsts.APPLICATION_EDIT_TYPE_RFI).getEntity();
                            String url;
                            String s = MaskUtil.maskValue("appNo", applicationDto.getApplicationNo());
                            if (!entity.isEmpty()) {
                                boolean rfcFlag = ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(
                                        applicationDto.getApplicationType());
                                boolean premisesListEdit = entity.get(0).isPremisesListEdit();
                                if (rfcFlag && premisesListEdit) {
                                    url = HmacConstants.HTTPS + "://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_CALL_BACK_URL_PREMISES_LIST + s;
                                    sendURL(bpc.request, bpc.response, url);
                                    bpc.request.getSession().setAttribute(AppConsts.SESSION_INTER_INBOX_MESSAGE_ID,
                                            interMessageBySubjectLike.getId());
                                    return;
                                }
                            }
                            url = HmacConstants.HTTPS + "://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_CALL_BACK_URL_NEWAPPLICATION + s;
                            sendURL(bpc.request, bpc.response, url);
                            bpc.request.getSession().setAttribute(AppConsts.SESSION_INTER_INBOX_MESSAGE_ID,
                                    interMessageBySubjectLike.getId());
                            return;
                        }
                    }
                }
                /*
                 * cessation
                 */
                if (ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(applicationDto.getApplicationType())) {
                    viewSessation(bpc, applicationDto);
                    return;
                }
                AppSubmissionDto appSubmissionDto = appSubmissionService.getAppSubmissionDto(appNo);
                if (appSubmissionDto != null) {
                    if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())) {
                        AppDeclarationMessageDto appDeclarationMessageDto = appSubmissionDto.getAppDeclarationMessageDto();
                        if (appDeclarationMessageDto != null) {
                            bpc.request.setAttribute("RFC_eqHciNameChange", "RFC_eqHciNameChange");
                        }
                        if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equalsIgnoreCase(appSubmissionDto.getAppGroupAppType())) {
                            bpc.request.setAttribute("group_renewal_app_rfc", "1");
                        }
                    }
                    if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())) {
                        AppDeclarationMessageDto appDeclarationMessageDto = appSubmissionDto.getAppDeclarationMessageDto();
                        if (appDeclarationMessageDto != null) {
                            RenewDto renewDto = new RenewDto();
                            renewDto.setAppSubmissionDtos(Collections.singletonList(appSubmissionDto));
                            bpc.request.setAttribute(RenewalConstants.RENEW_DTO, renewDto);
                            bpc.request.getSession().setAttribute(RenewalConstants.IS_SINGLE, AppConsts.YES);
                        } else {
                            bpc.request.getSession().setAttribute(RenewalConstants.IS_SINGLE, AppConsts.NO);
                        }
                    }
                    DealSessionUtil.initView(appSubmissionDto);
                    ParamUtil.setRequestAttr(bpc.request, "currentPreviewSvcInfo",
                            appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0));
                }
                ParamUtil.setRequestAttr(bpc.request, "cessationForm", "Application Details");
                ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
            }
        }
    }

    private void viewSessation(BaseProcessClass bpc, ApplicationDto applicationDto) {
        String cess_ack002 = MessageUtil.getMessageDesc("CESS_ACK002");
        ParamUtil.setSessionAttr(bpc.request, "cess_ack002", cess_ack002);
        AppCessLicDto appCessLicDto = new AppCessLicDto();
        String originLicenceId = applicationDto.getOriginLicenceId();
        LicenceDto licenceDto = licenceClient.getLicDtoById(originLicenceId).getEntity();
        String svcName = licenceDto.getSvcName();
        String licenceNo = licenceDto.getLicenceNo();
        appCessLicDto.setLicenceNo(licenceNo);
        appCessLicDto.setSvcName(svcName);
        AppCessMiscDto appCessMiscDto = cessationClient.getAppMiscDtoByAppId(applicationDto.getId()).getEntity();
        AppGrpPremisesDto appGrpPremisesDto = appCommService.getActivePremisesByAppNo(applicationDto.getApplicationNo());
        List<AppDeclarationMessageDto> appDeclarationMessageDtoList = applicationFeClient.getAppDeclarationMessageDto(
                appGrpPremisesDto.getAppGrpId()).getEntity();
        List<AppDeclarationDocDto> appDeclarationDocDtoList = applicationFeClient.getAppDeclarationDocDto(
                appGrpPremisesDto.getAppGrpId()).getEntity();
        if (appDeclarationMessageDtoList != null && appDeclarationMessageDtoList.size() > 0) {
            appCessLicDto.setAppDeclarationMessageDto(appDeclarationMessageDtoList.get(0));
            appCessLicDto.setAppDeclarationDocDtos(appDeclarationDocDtoList);
        }
        //appSubmissionService.initDeclarationFiles(appDeclarationDocDtoList,ApplicationConsts.APPLICATION_TYPE_CESSATION,bpc.request);
        String premisesId = appGrpPremisesDto.getId();
        String hciAddress = IaisCommonUtils.getAddress(appGrpPremisesDto);
        AppCessHciDto appCessHciDto = new AppCessHciDto();
        String hciName = appGrpPremisesDto.getHciName();
        String hciCode = appGrpPremisesDto.getHciCode();
        appCessHciDto.setHciCode(hciCode);
        appCessHciDto.setHciName(hciName);
        appCessHciDto.setPremiseId(premisesId);
        appCessHciDto.setHciAddress(hciAddress);
        Date effectiveDate = appCessMiscDto.getEffectiveDate();
        String reason = appCessMiscDto.getReason();
        String otherReason = appCessMiscDto.getOtherReason();
        String patTransType = appCessMiscDto.getPatTransType();
        Boolean patNeedTrans = appCessMiscDto.getPatNeedTrans();
        appCessHciDto.setPatientSelect(patTransType);
        appCessHciDto.setReason(reason);
        appCessHciDto.setOtherReason(otherReason);
        appCessHciDto.setEffectiveDate(effectiveDate);
        appCessHciDto.setTransferDetail(appCessMiscDto.getTransferDetail());
        appCessHciDto.setTransferredWhere(appCessMiscDto.getTransferredWhere());
        if (patNeedTrans) {
            appCessHciDto.setPatNeedTrans(Boolean.TRUE);
        } else {
            String remarks = appCessMiscDto.getPatNoReason();
            appCessHciDto.setPatNoRemarks(remarks);
            appCessHciDto.setPatNoConfirm("no");
            appCessHciDto.setPatNeedTrans(Boolean.FALSE);
        }
        List<AppCessHciDto> appCessHciDtos = IaisCommonUtils.genNewArrayList();
        appCessHciDtos.add(appCessHciDto);
        appCessLicDto.setAppCessHciDtos(appCessHciDtos);
        //spec
        String applicationNo = applicationDto.getApplicationNo();
        List<ApplicationDto> specApps = cessationClient.getAppByBaseAppNo(applicationNo).getEntity();
        if (!IaisCommonUtils.isEmpty(specApps)) {
            //List<AppSpecifiedLicDto> appSpecifiedLicDtos = IaisCommonUtils.genNewArrayList();
            for (ApplicationDto specApp : specApps) {
                String specId = specApp.getOriginLicenceId();
                LicenceDto specLicenceDto = licenceClient.getLicDtoById(specId).getEntity();
                if (specLicenceDto != null) {
                    AppSpecifiedLicDto appSpecifiedLicDto = new AppSpecifiedLicDto();
                    LicenceDto baseLic = licenceClient.getLicDtoById(originLicenceId).getEntity();
                    String specLicenceNo = specLicenceDto.getLicenceNo();
                    String licenceDtoId = specLicenceDto.getId();
                    String specSvcName = specLicenceDto.getSvcName();
                    appSpecifiedLicDto.setBaseLicNo(baseLic.getLicenceNo());
                    appSpecifiedLicDto.setBaseSvcName(baseLic.getSvcName());
                    appSpecifiedLicDto.setSpecLicNo(specLicenceNo);
                    appSpecifiedLicDto.setSpecSvcName(specSvcName);
                    appSpecifiedLicDto.setSpecLicId(licenceDtoId);
                }
            }
        }
        List<SelectOption> reasonOption = ApplicationHelper.getReasonOption();
        List<SelectOption> patientsOption = ApplicationHelper.getPatientsOption();
        ParamUtil.setRequestAttr(bpc.request, "reasonOption", reasonOption);
        ParamUtil.setRequestAttr(bpc.request, "patientsOption", patientsOption);
        ParamUtil.setRequestAttr(bpc.request, "applicationDto", applicationDto);
        List<AppCessLicDto> appCessLicDtos = IaisCommonUtils.genNewArrayList();
        appCessLicDtos.add(appCessLicDto);
        ParamUtil.setRequestAttr(bpc.request, "confirmDtos", appCessLicDtos);
        ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.IS_PRINT, "Y");

        ParamUtil.setSessionAttr(bpc.request, "appCessationDtos", (Serializable) appCessLicDtos);
        ParamUtil.setSessionAttr(bpc.request, "reasonOptionPrint", (Serializable) reasonOption);
        ParamUtil.setSessionAttr(bpc.request, "patientsOptionPrint", (Serializable) patientsOption);
        ParamUtil.setRequestAttr(bpc.request, "applicationDtoPrint", applicationDto);
        ParamUtil.setSessionAttr(bpc.request, "confirmPrintDtos", (Serializable) appCessLicDtos);

        ParamUtil.setSessionAttr(bpc.request, "declaration_page_is", "cessation");
    }

    private String getApplicationMsgNo(String appNo) {
        if (StringUtil.isNotEmpty(appNo)) {
            AppPremisesCorrelationDto correlation = selfAssessmentService.getCorrelationByAppNo(appNo);
            if (correlation != null) {
                AppPremiseMiscDto appPremiseMiscDto = cessationClient.getAppPremiseMiscDtoListByCon(correlation.getId(),
                        ApplicationConsts.APPLICATION_RFI_MSG).getEntity();
                if (appPremiseMiscDto != null) {
                    return appPremiseMiscDto.getOtherReason();
                }
            }
        }
        return null;
    }

    protected List<AppPremisesRoutingHistoryDto> getRoutingHistoryDtos(String appNo) {
        Map<String, Object> params = IaisCommonUtils.genNewHashMap(1);
        params.put("appNo", appNo);
        return feEicGatewayClient.getRoutingHistoryDtos(params).getEntity();
    }

    @Override
    protected Map<String, String> checkNextStatusOnRfi(String appGrpNo, String appNo) {
        log.info(StringUtil.changeForLog("App Grp No: " + appGrpNo + " - App No: " + appNo));
        Map<String, String> map = IaisCommonUtils.genNewHashMap();
        String rfiAppNo = null;
        List<ApplicationDto> entity = appCommService.getApplicationsByGroupNo(appGrpNo);
        for (ApplicationDto applicationDto : entity) {
            if ((ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION.equals(applicationDto.getStatus()))) {
                rfiAppNo = applicationDto.getApplicationNo();
                break;
            }
        }
        String status = null;
        if (!Objects.equals(appNo, rfiAppNo)) {
            map.put(HcsaAppConst.ERROR_APP, "The application has been changed, please try it from the beginning");
        } else {
            List<AppPremisesRoutingHistoryDto> hisList = getRoutingHistoryDtos(appNo);
            if (hisList != null) {
                for (AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto : hisList) {
                    if (!ApplicationConsts.PROCESSING_DECISION_REQUEST_FOR_INFORMATION.equals(
                            appPremisesRoutingHistoryDto.getProcessDecision())
                            && !InspectionConstants.PROCESS_DECI_REQUEST_FOR_INFORMATION.equals(
                            appPremisesRoutingHistoryDto.getProcessDecision())) {
                        continue;
                    }
                    switch (appPremisesRoutingHistoryDto.getAppStatus()){
                        case ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING:
                        case ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_ASO:status = ApplicationConsts.PENDING_ASO_REPLY;break;
                        case ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING:
                        case ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_PSO:status = ApplicationConsts.PENDING_PSO_REPLY;break;
                        case ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS:
                        case ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_INSPECTOR:status = ApplicationConsts.PENDING_INP_REPLY;break;
                        case ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01:
                        case ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_AO:status = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01;break;
                        default:
                    }
                }
            }
        }
        log.info(StringUtil.changeForLog("NextStatusOnRfi: " + status));
        map.put(HcsaAppConst.STATUS_APP, status);
        return map;
    }

    @Override
    protected AppSubmissionDto submitRequestInformation(AppSubmissionRequestInformationDto appSubmissionRequestInformationDto,
            String appType) {
        AppSubmissionDto appSubmissionDto;
        if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(
                appType)) {
            appSubmissionDto = appSubmissionService.submitRequestRfcRenewInformation(appSubmissionRequestInformationDto, null);
        } else {
            appSubmissionDto = appSubmissionService.submitRequestInformation(appSubmissionRequestInformationDto, null);
        }
        return appSubmissionDto;
    }

    public void doRenewSubmit(BaseProcessClass bpc) {
        throw new RuntimeException("Invalid Step!!!!!");
    }

    @Override
    protected List<AppSubmissionDto> submitRequestForChange(List<AppSubmissionDto> appSubmissionDtoList, String eventRefNo,
            BaseProcessClass bpc) {
        return requestForChangeService.saveAppSubmissionList(appSubmissionDtoList, eventRefNo, bpc);
    }

    @Override
    protected void handleDraft(String draftNo, String licenseeId, AppSubmissionDto appSubmissionDto,
            List<AppSubmissionDto> appSubmissionDtoList) {
        appSubmissionService.handleDraft(draftNo, licenseeId, appSubmissionDto, appSubmissionDtoList);
    }

    @Override
    protected void sendRfcSubmittedEmail(List<AppSubmissionDto> appSubmissionDtos, String pmtMethod) throws Exception {
        requestForChangeService.sendRfcSubmittedEmail(appSubmissionDtos, null);
    }

    /**
     * StartStep: doSubmit
     *
     * @param bpc
     * @throws
     */
    @Override
    public void doSubmit(BaseProcessClass bpc) throws IOException {
        super.doSubmit(bpc);
    }

    @Override
    protected AppSubmissionDto submit(AppSubmissionDto appSubmissionDto) {
        return appSubmissionService.submit(appSubmissionDto, null);
    }

    @Override
    protected FeeDto getNewAppAmount(AppSubmissionDto appSubmissionDto, boolean charity) {
        return appSubmissionService.getNewAppAmount(appSubmissionDto, charity);
    }

    /**
     * StartStep: ControlSwitch
     *
     * @param bpc
     * @throws
     */
    @Override
    public void controlSwitch(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do controlSwitch start ...."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        String switch2 = "loading";
        String crudActionValue = (String) ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.ISVALID);
        if (StringUtil.isEmpty(crudActionValue)) {
            crudActionValue = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
            if (StringUtil.isEmpty(crudActionValue)) {
                crudActionValue = (String) ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
            }
        }
        boolean isRfi = ApplicationHelper.checkIsRfi(bpc.request);
        Object requestInformationConfig = ParamUtil.getSessionAttr(bpc.request, HcsaAppConst.REQUESTINFORMATIONCONFIG);
        if ((ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())
                || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())) && !isRfi) {
            Object error = ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG);
            String crud_action_additional = ParamUtil.getString(bpc.request, "crud_action_additional");
            if (error == null && "rfcSaveDraft".equals(crud_action_additional)) {
                crudActionValue = "saveDraft";
            }
        }
        if ("saveDraft".equals(crudActionValue) || "ack".equals(crudActionValue) || "payFailed".equals(crudActionValue)) {
            switch2 = crudActionValue;
        } else if ("doSubmit".equals(crudActionValue)) {
            if (requestInformationConfig == null) {
                switch2 = crudActionValue;
                if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())) {
                    switch2 = "requstChange";
                } else if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())) {
                    switch2 = "renew";
                }
            } else {
                switch2 = HcsaAppConst.ACTION_RFI;
            }
        } else if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())) {
            // 72106
            String action = ParamUtil.getRequestString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
            if (!"licensee".equals(action) && !"premises".equals(action) && !"jump".equals(action)) {
                AppGrpPremisesDto premisse = appSubmissionDto.getAppGrpPremisesDtoList() != null
                        && appSubmissionDto.getAppGrpPremisesDtoList().size() > 0 ?
                        appSubmissionDto.getAppGrpPremisesDtoList().get(0) : null;
                if (premisse == null || !premisse.isFilled()) {
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "premises");
                }
            }
        }
        ParamUtil.setRequestAttr(bpc.request, "Switch2", switch2);
        log.info(StringUtil.changeForLog("the do controlSwitch end ...."));
    }

    /**
     * StartStep: jumpbank
     *
     * @param bpc
     * @throws
     */
    public void jumpBank(BaseProcessClass bpc) throws IOException {
        log.info(StringUtil.changeForLog("the do jumpBank start ...."));
        String payMethod = ParamUtil.getString(bpc.request, "payMethod");
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        String appGrpId = appSubmissionDto.getAppGrpId();
        log.info(StringUtil.changeForLog("The AppGrpNo: " + appSubmissionDto.getAppGrpNo() + "; payment method: "
                + appSubmissionDto.getPaymentMethod() + "; the amount: " + appSubmissionDto.getAmount()
                + " - " + appSubmissionDto.getAppGrpId()));
        double totalAmount = appSubmissionDto.getAmount();
        //68099
        List<AppSubmissionDto> ackPageAppSubmissionDto = (List<AppSubmissionDto>) ParamUtil.getSessionAttr(bpc.request,
                ACK_APP_SUBMISSIONS);
        if (!IaisCommonUtils.isEmpty(ackPageAppSubmissionDto)) {
            for (AppSubmissionDto appSubmissionDto1 : ackPageAppSubmissionDto) {
                if (!MiscUtil.doubleEquals(appSubmissionDto1.getAmount(), 0.0)) {
                    totalAmount = Calculator.add(totalAmount, appSubmissionDto1.getAmount());
                    appSubmissionDto1.setPaymentMethod(payMethod);
                }
            }
            ParamUtil.setSessionAttr(bpc.request, ACK_APP_SUBMISSIONS, (Serializable) ackPageAppSubmissionDto);
        }
        if (MiscUtil.doubleEquals(totalAmount, 0.0)) {
            List<String> ids = new ArrayList<>();
            if (!IaisCommonUtils.isEmpty(ackPageAppSubmissionDto)) {
                for (AppSubmissionDto appSubmissionDto1 : ackPageAppSubmissionDto) {
                    appSubmissionService.updatePayment(appSubmissionDto1, null);
                    ids.add(appSubmissionDto1.getAppGrpId());
                }
                ParamUtil.setSessionAttr(bpc.request, ACK_APP_SUBMISSIONS, (Serializable) ackPageAppSubmissionDto);
            }
            if (StringUtil.isNotEmpty(appGrpId) && !ids.contains(appGrpId)) {
                appSubmissionService.updatePayment(appSubmissionDto, null);
                if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())) {
                    LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request,
                            AppConsts.SESSION_ATTR_LOGIN_USER);
                    appSubmissionService.sendEmailAndSMSAndMessage(appSubmissionDto, loginContext.getUserName());
                }
            }
            StringBuilder url = new StringBuilder();
            url.append("https://")
                    .append(bpc.request.getServerName())
                    .append("/hcsa-licence-web/eservice/INTERNET/MohNewApplication/PrepareAckPage");
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
            return;
        }
        appSubmissionDto.setPaymentMethod(payMethod);
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        if (ApplicationConsts.PAYMENT_METHOD_NAME_CREDIT.equals(payMethod)
                || ApplicationConsts.PAYMENT_METHOD_NAME_NETS.equals(payMethod)
                || ApplicationConsts.PAYMENT_METHOD_NAME_PAYNOW.equals(payMethod)) {
            try {
                String url = NewApplicationHelper.genBankUrl(payMethod, appSubmissionDto.getAmount(), GatewayConfig.return_url,
                        payMethod, appSubmissionDto.getAppGrpNo() + "_" + System.currentTimeMillis(), bpc.request);
                appSubmissionService.updateDraftStatus(appSubmissionDto.getDraftNo(), ApplicationConsts.DRAFT_STATUS_PENDING_PAYMENT);
                IaisEGPHelper.redirectUrl(bpc.response, url);
                //ParamUtil.setRequestAttr(bpc.request, "jumpHtml", html);
            } catch (Exception e) {
                log.info(StringUtil.changeForLog(e.getMessage()), e);
            }
        } else if (ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(payMethod)) {
            //send email
            try {
                //sendNewApplicationPaymentGIROEmail(appSubmissionDto, bpc);
                if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())) {
                    LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request,
                            AppConsts.SESSION_ATTR_LOGIN_USER);
                    appSubmissionService.sendEmailAndSMSAndMessage(appSubmissionDto, loginContext.getUserName());
                }
                if (ackPageAppSubmissionDto == null) {
                    ackPageAppSubmissionDto = IaisCommonUtils.genNewArrayList();
                    ackPageAppSubmissionDto.add(appSubmissionDto);
                }
                if (ackPageAppSubmissionDto.get(0).getAppType().equals(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE)) {
                    requestForChangeService.sendRfcSubmittedEmail(ackPageAppSubmissionDto,
                            ackPageAppSubmissionDto.get(0).getPaymentMethod());
                }
            } catch (Exception e) {
                log.error(StringUtil.changeForLog("send email error ...." + e.getMessage()), e);
            }
            String giroTranNo = null;
            List<String> ids = new ArrayList<>();
            if (!IaisCommonUtils.isEmpty(ackPageAppSubmissionDto)) {
                for (AppSubmissionDto appSubmissionDto1 : ackPageAppSubmissionDto) {
                    appSubmissionService.updatePayment(appSubmissionDto1, null);
                    ids.add(appSubmissionDto1.getAppGrpId());
                    if (StringUtil.isEmpty(giroTranNo)) {
                        giroTranNo = appSubmissionDto1.getGiroTranNo();
                    }
                }
                ParamUtil.setSessionAttr(bpc.request, ACK_APP_SUBMISSIONS, (Serializable) ackPageAppSubmissionDto);
            }
            if (StringUtil.isNotEmpty(appGrpId) && !ids.contains(appGrpId)) {
                appSubmissionService.updatePayment(appSubmissionDto, null);
                if (StringUtil.isEmpty(giroTranNo)) {
                    giroTranNo = appSubmissionDto.getGiroTranNo();
                }
                if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())) {
                    LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request,
                            AppConsts.SESSION_ATTR_LOGIN_USER);
                    appSubmissionService.sendEmailAndSMSAndMessage(appSubmissionDto, loginContext.getUserName());
                }
            }
            ParamUtil.setRequestAttr(bpc.request, "PmtStatus", ApplicationConsts.PAYMENT_METHOD_NAME_GIRO);
            ParamUtil.setSessionAttr(bpc.request, "txnRefNo", giroTranNo);
            // change
            StringBuilder url = new StringBuilder();
            url.append("https://")
                    .append(bpc.request.getServerName())
                    .append("/hcsa-licence-web/eservice/INTERNET/MohNewApplication/PrepareAckPage");
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
            return;
        }
        log.info(StringUtil.changeForLog("the do jumpBank end ...."));
    }

    /**
     * StartStep: PrepareAckPage
     *
     * @param bpc
     * @throws
     */
    @Override
    public void prepareAckPage(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do prepareAckPage start ...."));
        String txnRefNo = (String) bpc.request.getSession().getAttribute("txnDt");
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        List<AppSubmissionDto> ackPageAppSubmissionDto = (List<AppSubmissionDto>) ParamUtil.getSessionAttr(bpc.request,
                ACK_APP_SUBMISSIONS);
        String draftNo = "";
        String appGrpId = null;
        if (appSubmissionDto != null) {
            draftNo = appSubmissionDto.getDraftNo();
            appGrpId = appSubmissionDto.getAppGrpId();
            if (ackPageAppSubmissionDto == null) {
                List<AppSubmissionDto> ackPageAppSubmission = new ArrayList<>(1);
                ackPageAppSubmission.add(appSubmissionDto);
                bpc.request.getSession().setAttribute(ACK_APP_SUBMISSIONS, ackPageAppSubmission);
            }
        }
        if (draftNo != null) {
            appSubmissionService.deleteDraftAsync(draftNo, appGrpId);
        }
        if (!StringUtil.isEmpty(appSubmissionDto) && !StringUtil.isEmpty(appSubmissionDto.getLicenceId())) {
            List<ApplicationSubDraftDto> entity = applicationFeClient.getDraftByLicAppId(appSubmissionDto.getLicenceId()).getEntity();
            for (ApplicationSubDraftDto applicationSubDraftDto : entity) {
                if (!applicationSubDraftDto.getDraftNo().equals(draftNo)) {
                    applicationFeClient.deleteDraftByNo(applicationSubDraftDto.getDraftNo());
                } else {
                    if (AppConsts.COMMON_STATUS_ACTIVE.equals(applicationSubDraftDto.getStatus())) {
                        applicationFeClient.deleteDraftByNo(applicationSubDraftDto.getDraftNo());
                    }
                }
            }
        }
        if (StringUtil.isEmpty(txnRefNo)) {
            String txnDt = Formatter.formatDate(new Date());
            ParamUtil.setSessionAttr(bpc.request, "txnDt", txnDt);
        }
        InterInboxUserDto interInboxUserDto = (InterInboxUserDto) ParamUtil.getSessionAttr(bpc.request, "INTER_INBOX_USER_INFO");
        String licenseeId = null;
        if (interInboxUserDto != null) {
            licenseeId = interInboxUserDto.getLicenseeId();
        } else {
            log.debug(StringUtil.changeForLog("interInboxUserDto null"));
        }
        List<String> licenseeEmailAddrs = IaisEGPHelper.getLicenseeEmailAddrs(licenseeId);
        String emailAddress = ApplicationHelper.emailAddressesToString(licenseeEmailAddrs);
        ParamUtil.setSessionAttr(bpc.request, "emailAddress", emailAddress);
        String ackStatus = (String) ParamUtil.getRequestAttr(bpc.request, ACKSTATUS);
        boolean isRfi = ApplicationHelper.checkIsRfi(bpc.request);
        if (isRfi && HcsaAppConst.ACK_STATUS_ERROR.equals(ackStatus)) {
            List<HcsaServiceDto> hcsaServiceDtoList = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request,
                    HcsaAppConst.RFI_REPLY_SVC_DTO);
            ParamUtil.setSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST, (Serializable) hcsaServiceDtoList);
        }
        log.info(StringUtil.changeForLog("the do prepareAckPage end ...."));
    }

    /**
     * StartStep: prepareJumpv
     *
     * @param bpc
     * @throws
     */
    @Override
    public void prepareJump(BaseProcessClass bpc) throws Exception {
        super.prepareJump(bpc);
    }

}


