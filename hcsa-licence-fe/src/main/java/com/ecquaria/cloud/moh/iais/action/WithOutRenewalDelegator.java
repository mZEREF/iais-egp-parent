package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.helper.ConfigHelper;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppFeeDetailsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationSubDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.RenewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.HcsaFeeBundleItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterInboxUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaAppConst;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.RfcConst;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AppDataHelper;
import com.ecquaria.cloud.moh.iais.helper.AppValidatorHelper;
import com.ecquaria.cloud.moh.iais.helper.ApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.helper.RfcHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppCommService;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.ConfigCommService;
import com.ecquaria.cloud.moh.iais.service.LicCommService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.WithOutRenewalService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationFeClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigFeClient;
import com.ecquaria.cloud.moh.iais.util.DealSessionUtil;
import com.ecquaria.cloud.moh.iais.validation.PaymentValidate;
import com.ecquaria.sz.commons.util.Calculator;
import com.ecquaria.sz.commons.util.MsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants.ACKNOWLEDGEMENT;
import static com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants.BACK;
import static com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants.CONTROL_SWITCH;
import static com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants.EDIT;
import static com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants.INSTRUCTIONS;
import static com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants.PAGE1;
import static com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants.PAGE2;
import static com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants.PAGE3;
import static com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants.PAGE4;
import static com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants.PAGE_SWITCH;
import static com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants.PAYMENT;
import static com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants.PREFIXTITLE;
import static com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants.REVIEW;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.LOADING_DRAFT;


/**
 * AutoRenewalDelegator
 *
 * @author caijing
 * @date 2020/1/6
 */

@Delegator("withOutRenewalDelegator")
@Slf4j
public class WithOutRenewalDelegator {

    @Autowired
    private WithOutRenewalService outRenewalService;

    @Autowired
    private AppSubmissionService appSubmissionService;

    @Autowired
    private ServiceConfigService serviceConfigService;

    @Autowired
    private HcsaConfigFeClient hcsaConfigFeClient;

    @Autowired
    RequestForChangeService requestForChangeService;

    @Autowired
    private ApplicationFeClient applicationFeClient;

    @Value("${iais.system.one.address}")
    private String systemAddressOne;

    @Autowired
    private NotificationHelper notificationHelper;

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private AppCommService appCommService;

    @Autowired
    private LicCommService licCommService;

    @Autowired
    private ConfigCommService configCommService;

    private static final String TOTAL_STR = "totalStr";

    private static final String TOTAL_AMOUNT = "totalAmount";

    private static final String USER_AGREEMENT = "userAgreement";

    private static final String OLD_RENEW_APPSUBMISSIONDTO = "oldRenewAppSubmissionDto";

    private static final String RENEW_APPSUBMISSIONDTOS = "renewAppSubmissionDtos";

    private static final String RFC_APPSUBMISSIONDTOS = "rfcAppSubmissionDtos";

    private static final String BACK_URL = "backUrl";

    private static final String APPEND_HTTP = "https://";

    private static final String TXN_DT = "txnDt";

    private static final String TXN_REFNO = "txnRefNo";

    private static final String ERROR_MSG = "errorMsg";

    private static final String HAS_DETAIL = "hasDetail";

    private static final String EDIT_VALUE = "EditValue";

    public void start(BaseProcessClass bpc) throws Exception {
        log.info("**** the non auto renwal  start ******");
        HcsaServiceCacheHelper.flushServiceMapping();
        String draftNo = ParamUtil.getMaskedString(bpc.request, HcsaAppConst.DRAFT_NUMBER);
        //init session
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_RENEW, AuditTrailConsts.FUNCTION_RENEW);
        ParamUtil.setSessionAttr(bpc.request, RenewalConstants.RENEW_DTO, null);
        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.APPSUBMISSIONDTO, null);
        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.PRIMARY_DOC_CONFIG, null);
        ParamUtil.setSessionAttr(bpc.request, TOTAL_STR, null);
        ParamUtil.setSessionAttr(bpc.request, TOTAL_AMOUNT, null);
        ParamUtil.setSessionAttr(bpc.request, USER_AGREEMENT, null);
        ParamUtil.setSessionAttr(bpc.request, PREFIXTITLE, "renewing");
        ParamUtil.setSessionAttr(bpc.request, "paymentMessageValidateMessage", null);
        ParamUtil.setSessionAttr(bpc.request, LOADING_DRAFT, null);
        ParamUtil.setSessionAttr(bpc.request, "oldAppSubmissionDto", null);
        ParamUtil.setSessionAttr(bpc.request, OLD_RENEW_APPSUBMISSIONDTO, null);
        ParamUtil.setSessionAttr(bpc.request, "requestInformationConfig", null);
        ParamUtil.setSessionAttr(bpc.request, "rfc_eqHciCode", null);
        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.DASHBOARDTITLE, "");
        ParamUtil.setSessionAttr(bpc.request, "seesion_files_map_ajax_feselectedDeclFile", null);
        ParamUtil.setSessionAttr(bpc.request, "pageShowFileDtos", null);
        ParamUtil.setSessionAttr(bpc.request, "selectedRENEWFileDocShowPageDto", null);
        ParamUtil.setSessionAttr(bpc.request, "selectedRFCFileDocShowPageDto", null);
        ParamUtil.setSessionAttr(bpc.request, "selectedNewFileDocShowPageDto", null);
        bpc.request.getSession().removeAttribute("seesion_files_map_ajax_feselectedRENEWFile");
        bpc.request.getSession().removeAttribute("seesion_files_map_ajax_feselectedRENEWFile_MaxIndex");
        bpc.request.getSession().removeAttribute("declaration_page_is");
        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.IS_VIEW, null);
        ParamUtil.setSessionAttr(bpc.request, IaisEGPConstant.GLOBAL_MAX_INDEX_SESSION_ATTR, 0);
        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.CURR_ORG_USER_ACCOUNT, null);
        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.LICPERSONSELECTMAP, null);
        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.PREMISES_HCI_LIST, null);
        ParamUtil.setSessionAttr(bpc.request, RENEW_APPSUBMISSIONDTOS, null);
        ParamUtil.setSessionAttr(bpc.request, RFC_APPSUBMISSIONDTOS, null);
        HttpServletRequest request = bpc.request;
        //init page value
        //instructions
        ParamUtil.setRequestAttr(bpc.request, "page_value", PAGE1);
        String licenceId = ParamUtil.getMaskedString(bpc.request, "licenceId");
        //init data
        List<String> licenceIdList = (List<String>) ParamUtil.getSessionAttr(bpc.request,
                RenewalConstants.WITHOUT_RENEWAL_LIC_ID_LIST_ATTR);
        if (licenceIdList == null) {
            licenceIdList = IaisCommonUtils.genNewArrayList();
        }
        if (licenceId != null) {
            licenceIdList = new ArrayList<>(1);
            licenceIdList.add(licenceId);
        }
        List<AppSubmissionDto> appSubmissionDtoList;
        if (StringUtil.isEmpty(draftNo)) {
            appSubmissionDtoList = outRenewalService.getAppSubmissionDtos(licenceIdList);
            log.info("can not find licence id for without renewal");
            ParamUtil.setSessionAttr(request, BACK_URL, "initLic");
        } else {
            appSubmissionDtoList = appSubmissionService.getAppSubmissionDtoDrafts(draftNo);
            if (IaisCommonUtils.isEmpty(appSubmissionDtoList)
                    || requestForChangeService.getLicenceById(appSubmissionDtoList.get(0).getLicenceId()) == null) {
                applicationFeClient.deleteDraftByNo(draftNo);
                RedirectUtil.redirect(new StringBuilder().append(APPEND_HTTP).append(bpc.request.getServerName())
                        .append("/main-web/eservice/INTERNET/MohInternetInbox").toString(), bpc.request, bpc.response);
                return;
            }
            ParamUtil.setSessionAttr(bpc.request, BACK_URL, "initApp");
            ParamUtil.setSessionAttr(bpc.request, LOADING_DRAFT, AppConsts.YES);
        }
        String appType = ApplicationConsts.APPLICATION_TYPE_RENEWAL;
        List<String> serviceNameTitleList = IaisCommonUtils.genNewArrayList();
        List<String> serviceNameList = IaisCommonUtils.genNewArrayList();

        List<HcsaSvcDocConfigDto> primaryDocConfig = serviceConfigService.getAllHcsaSvcDocs(null);
        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.PRIMARY_DOC_CONFIG, (Serializable) primaryDocConfig);
        if (StringUtil.isEmpty(draftNo)) {
            draftNo = appSubmissionService.getDraftNo(ApplicationConsts.APPLICATION_TYPE_RENEWAL);
            log.info(StringUtil.changeForLog("without renewal deafrt number =====>" + draftNo));
        }
        for (AppSubmissionDto appSubmissionDto : appSubmissionDtoList) {
            List<HcsaServiceDto> hcsaServiceDtoList = DealSessionUtil.getLatestServiceConfigsFormApp(appSubmissionDto);
            appSubmissionDto.setAppType(appType);
            appSubmissionDto.setDraftNo(draftNo);
            appSubmissionDto.setPaymentMethod(null);
            DealSessionUtil.init(appSubmissionDto, hcsaServiceDtoList, true, null);
            for (HcsaServiceDto hcsaServiceDto : hcsaServiceDtoList) {
                serviceNameList.add(hcsaServiceDto.getSvcName());
                serviceNameTitleList.add(hcsaServiceDto.getSvcName());
            }
            AppEditSelectDto appEditSelectDto = ApplicationHelper.createAppEditSelectDto(true);
            appEditSelectDto.setLicenseeEdit(false);
            appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
            appSubmissionDto.setOneLicDoRenew(false);
        }
        if (appSubmissionDtoList.size() == 1) {
            AppSubmissionDto appSubmissionDto = appSubmissionDtoList.get(0);
            // 133320
            //appSubmissionDto.setOneLicDoRenew(true);
            ParamUtil.setSessionAttr(bpc.request, RenewalConstants.IS_SINGLE, AppConsts.YES);
            ParamUtil.setSessionAttr(bpc.request, "renew_licence_no", appSubmissionDto.getLicenceNo());
        } else {
            ParamUtil.setSessionAttr(bpc.request, RenewalConstants.IS_SINGLE, AppConsts.NO);
        }
        AppSubmissionDto appSubmissionDto = appSubmissionDtoList.get(0);
        AppSubmissionDto oldAppSubmissionDto = appSubmissionDto.getOldAppSubmissionDto();
        if (oldAppSubmissionDto == null) {
            oldAppSubmissionDto = CopyUtil.copyMutableObject(appSubmissionDto);
            appSubmissionDto.setOldAppSubmissionDto(oldAppSubmissionDto);
        }
        ParamUtil.setSessionAttr(bpc.request, OLD_RENEW_APPSUBMISSIONDTO, oldAppSubmissionDto);
        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.APPSUBMISSIONDTO, appSubmissionDto);
        RenewDto renewDto = new RenewDto();
        renewDto.setAppSubmissionDtos(appSubmissionDtoList);
        List<AppSubmissionDto> cloneAppsbumissionDtos = IaisCommonUtils.genNewArrayList();
        CopyUtil.copyMutableObjectList(appSubmissionDtoList, cloneAppsbumissionDtos);
        ParamUtil.setSessionAttr(bpc.request, "oldSubmissionDtos", (Serializable) cloneAppsbumissionDtos);
        ParamUtil.setSessionAttr(bpc.request, RenewalConstants.RENEW_DTO, renewDto);

        String firstSvcName = serviceNameTitleList.remove(0);
        ParamUtil.setSessionAttr(bpc.request, "firstSvcName", firstSvcName);
        ParamUtil.setSessionAttr(bpc.request, "serviceNameTitleList", (Serializable) serviceNameTitleList);
        //serviceNameList
        ParamUtil.setSessionAttr(bpc.request, "serviceNames", (Serializable) serviceNameList);

        //init app submit
        ParamUtil.setSessionAttr(bpc.request, "hasAppSubmit", null);
        ParamUtil.setSessionAttr(bpc.request, TXN_DT, null);
        ParamUtil.setSessionAttr(bpc.request, TXN_REFNO, null);
        log.info("**** the non auto renwal  end ******");
    }

    /**
     * AutoStep: prepare
     */
    public void prepare(BaseProcessClass bpc) throws Exception {
        log.info("**** the  auto renwal  prepare start  ******");
        log.info("**** the  renwal  prepare  end ******");
        paymentCallBack(bpc.request);
    }

    private void paymentCallBack(HttpServletRequest request) {
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_RENEW, AuditTrailConsts.FUNCTION_RENEW);
        RenewDto renewDto = (RenewDto) ParamUtil.getSessionAttr(request, RenewalConstants.RENEW_DTO);
        if (renewDto == null || IaisCommonUtils.isEmpty(renewDto.getAppSubmissionDtos())) {
            return;
        }
        List<AppSubmissionDto> appSubmissionDtos = renewDto.getAppSubmissionDtos();
        String pay = appSubmissionDtos.get(0).getPaymentMethod();
        String result = ParamUtil.getMaskedString(request, "result");
        if (StringUtil.isEmpty(result) && StringUtil.isEmpty(pay)) {
            return;
        }
        log.info("**** Payment Call Back  start ******");
        String err24 = MessageUtil.getMessageDesc("NEW_ERR0024");
        if (!StringUtil.isEmpty(result)) {
            log.info(StringUtil.changeForLog("payment result:" + result));
            String pmtRefNo = ParamUtil.getMaskedString(request, "reqRefNo");
            if ("success".equals(result) && !StringUtil.isEmpty(pmtRefNo)) {
                String txnDt = ParamUtil.getMaskedString(request, TXN_DT);
                String txnRefNo = ParamUtil.getMaskedString(request, TXN_REFNO);

                ParamUtil.setSessionAttr(request, TXN_DT, txnDt);
                ParamUtil.setSessionAttr(request, TXN_REFNO, txnRefNo);
                //update status
                appSubmissionService.updatePayment(appSubmissionDtos.get(0), pmtRefNo);
                //jump page to acknowledgement
                //send email pay success
                try {
                    sendEmail(request, appSubmissionDtos);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    log.error(StringUtil.changeForLog("send email error=="));
                }
                ParamUtil.setRequestAttr(request, PAGE_SWITCH, PAGE4);
            } else {
                Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
                errorMap.put("pay", err24);
                ParamUtil.setRequestAttr(request, ERROR_MSG, WebValidationHelper.generateJsonStr(errorMap));
                //jump page to payment
                ParamUtil.setRequestAttr(request, PAGE_SWITCH, PAGE3);
            }
        } else if (pay.equals(ApplicationConsts.PAYMENT_METHOD_NAME_GIRO)) {
            ParamUtil.setRequestAttr(request, PAGE_SWITCH, PAGE4);
        } else if (pay.equals(ApplicationConsts.PAYMENT_METHOD_NAME_CREDIT)
                || pay.equals(ApplicationConsts.PAYMENT_METHOD_NAME_NETS)) {
            String switchValue = ParamUtil.getString(request, "switch_value");
            if (switchValue == null || PAYMENT.equals(switchValue)) {
                Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
                errorMap.put("pay", err24);
                ParamUtil.setRequestAttr(request, ERROR_MSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(request, PAGE_SWITCH, PAGE3);
            }
        }
        log.info("**** Payment Call Back  end ******");
    }

    public void prepareInstructions(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, HAS_DETAIL, "N");
    }

    //prepareLicenceReview
    public void prepareLicenceReview(BaseProcessClass bpc) throws Exception {
        ParamUtil.setRequestAttr(bpc.request, HAS_DETAIL, "Y");
        RenewDto renewDto = (RenewDto) ParamUtil.getSessionAttr(bpc.request, RenewalConstants.RENEW_DTO);
        List<AppSubmissionDto> newAppSubmissionDtos = renewDto.getAppSubmissionDtos();
        if (!IaisCommonUtils.isEmpty(newAppSubmissionDtos)) {
            for (AppSubmissionDto appSubmissionDto : newAppSubmissionDtos) {
                DealSessionUtil.initView(appSubmissionDto);
            }
            appSubmissionService.setPreviewDta(newAppSubmissionDtos.get(0), bpc);
        }
        //bug fix 76354
        ParamUtil.setRequestAttr(bpc.request, "doRenewViewYes", AppConsts.YES);
    }

    //preparePayment
    public void preparePayment(BaseProcessClass bpc) {
        RenewDto renewDto = (RenewDto) ParamUtil.getSessionAttr(bpc.request, RenewalConstants.RENEW_DTO);
        String paymentMessageValidateMessage = MessageUtil.replaceMessage("GENERAL_ERR0006", "Payment Method", "field");
        ParamUtil.setSessionAttr(bpc.request, "paymentMessageValidateMessage", paymentMessageValidateMessage);
        ParamUtil.setRequestAttr(bpc.request, HAS_DETAIL, "Y");
        if (renewDto == null) {
            return;
        }

        List<FeeInfoDto> feeInfoDtos = renewDto.getAppSubmissionDtos().get(0).getFeeInfoDtos();
        HashMap<String, List<FeeExtDto>> laterFeeDetailsMap = getLaterFeeDetailsMap(feeInfoDtos);
        ParamUtil.setRequestAttr(bpc.request, "laterFeeDetailsMap", laterFeeDetailsMap);
        ParamUtil.setRequestAttr(bpc.request, "feeInfoDtoList", feeInfoDtos);

        List<AppSubmissionDto> renewAppSubmissionDtos = (List<AppSubmissionDto>) ParamUtil.getSessionAttr(bpc.request,
                RENEW_APPSUBMISSIONDTOS);
        List<String> serviceNamesAck = IaisCommonUtils.genNewArrayList();

        for (AppSubmissionDto appSubmissionDto : renewAppSubmissionDtos) {
            String serviceName = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getServiceName();
            appSubmissionDto.setServiceName(serviceName);
            if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())) {
                serviceName = serviceName + " (Renewal)";
            } else {
                serviceName = serviceName + " (Amendment)";
            }
            serviceNamesAck.add(serviceName);
        }
        List<AppSubmissionDto> rfcAppSubmissionDtos = renewAppSubmissionDtos.stream()
                .filter(dto -> ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(dto.getAppType()))
                .collect(Collectors.toList());
        ParamUtil.setSessionAttr(bpc.request, RFC_APPSUBMISSIONDTOS, (Serializable) rfcAppSubmissionDtos);
        ParamUtil.setSessionAttr(bpc.request, "serviceNamesAck", (Serializable) serviceNamesAck);
        //has app submit
        ParamUtil.setSessionAttr(bpc.request, "hasAppSubmit", "Y");
        setGiroAcc(renewAppSubmissionDtos, bpc.request);
    }

    private void setGiroAcc(List<AppSubmissionDto> appSubmissionDtos, HttpServletRequest request) {
        boolean isGiroAcc = false;
        List<SelectOption> giroAccSel = ApplicationHelper.getGiroAccOptions(appSubmissionDtos, null);
        if (!IaisCommonUtils.isEmpty(giroAccSel)) {
            isGiroAcc = true;
            ParamUtil.setRequestAttr(request, "giroAccSel", giroAccSel);
        }
        ParamUtil.setRequestAttr(request, "IsGiroAcc", isGiroAcc);
    }

    private void setRfcSubInfo(AppSubmissionDto appSubmissionDtoNew, AppSubmissionDto dto, boolean isSingle) {
        if (isSingle) {
            dto.setEffectiveDateStr(appSubmissionDtoNew.getEffectiveDateStr());
            dto.setEffectiveDate(appSubmissionDtoNew.getEffectiveDate());
            if (appSubmissionDtoNew.getAppDeclarationMessageDto() != null) {
                dto.setAppDeclarationMessageDto(appSubmissionDtoNew.getAppDeclarationMessageDto());
                dto.setAppDeclarationDocDtos(appSubmissionDtoNew.getAppDeclarationDocDtos());
                if (appSubmissionDtoNew.getAppDeclarationMessageDto().getEffectiveDt() != null) {
                    dto.setEffectiveDate(appSubmissionDtoNew.getAppDeclarationMessageDto().getEffectiveDt());
                    dto.setEffectiveDateStr(Formatter.formatDate(appSubmissionDtoNew.getAppDeclarationMessageDto().getEffectiveDt()));
                }
            }
        } else {
            dto.setAppDeclarationMessageDto(null);
            dto.setAppDeclarationDocDtos(null);
        }
    }

    private void moreAppSubmissionDtoAction(List<AppSubmissionDto> appSubmissionDtos) {
        List<HcsaFeeBundleItemDto> hcsaFeeBundleItemDtos = hcsaConfigFeClient.getActiveBundleDtoList().getEntity();
        List<HcsaServiceDto> hcsaServiceDtoList = hcsaConfigFeClient.getActiveServices().getEntity();
        Map<String, HcsaServiceDto> map = new HashMap<>(10);
        Map<String, List<HcsaFeeBundleItemDto>> bundleMap = new HashMap<>(10);
        hcsaServiceDtoList.forEach(v -> map.put(v.getSvcName(), v));
        hcsaFeeBundleItemDtos.forEach(v -> {
            List<HcsaFeeBundleItemDto> feeBundleItemDtos = bundleMap.get(v.getBundleId());
            if (feeBundleItemDtos == null) {
                feeBundleItemDtos = new ArrayList<>(10);
                feeBundleItemDtos.add(v);
                bundleMap.put(v.getBundleId(), feeBundleItemDtos);
            } else {
                feeBundleItemDtos.add(v);
            }

        });
        Map<String, List<AppSubmissionDto>> appSubmitMap = new HashMap<>(10);
        for (AppSubmissionDto var1 : appSubmissionDtos) {
            String serviceName = var1.getAppSvcRelatedInfoDtoList().get(0).getServiceName();
            HcsaServiceDto hcsaServiceDto = map.get(serviceName);
            for (HcsaFeeBundleItemDto var2 : hcsaFeeBundleItemDtos) {
                if (var2.getSvcCode().equals(hcsaServiceDto.getSvcCode())) {
                    List<AppSubmissionDto> dtoList = appSubmitMap.get(var2.getBundleId());
                    if (dtoList == null) {
                        dtoList = new ArrayList<>(10);
                        dtoList.add(var1);
                        appSubmitMap.put(var2.getBundleId(), dtoList);
                    } else {
                        dtoList.add(var1);
                    }

                }
            }
        }
        appSubmitMap.forEach((k, v) -> {
            long l = System.currentTimeMillis();
            v.forEach(var1 -> var1.getAppSvcRelatedInfoDtoList().get(0).setAlignFlag(Long.toString(l)));
        });
    }

    private List<AppSubmissionDto> getLicChangeSubmissionDtos(AppSubmissionDto oldAppSubmissionDto) {
        SubLicenseeDto oldSublicenseeDto = oldAppSubmissionDto.getSubLicenseeDto();
        List<AppSubmissionDto> licenseeAffectedList = licCommService.getAppSubmissionDtosBySubLicensee(oldSublicenseeDto);
        Iterator<AppSubmissionDto> itemDtoIterator = licenseeAffectedList.iterator();
        while (itemDtoIterator.hasNext()) {
            AppSubmissionDto appSubmissionDto = itemDtoIterator.next();
            if (appSubmissionDto.getLicenceId().equalsIgnoreCase(oldAppSubmissionDto.getLicenceId())) {
                itemDtoIterator.remove();
                return licenseeAffectedList;
            }
        }
        return licenseeAffectedList;
    }

    private List<AppSubmissionDto> getAutoChangeLicAppSubmissions(AppSubmissionDto oldAppSubmissionDto, String groupNo,
            AppSubmissionDto appSubmissionDto) {
        FeeDto feeDto = new FeeDto();
        feeDto.setTotal(0.0d);
        List<AppSubmissionDto> appSubmissionDtos = getLicChangeSubmissionDtos(oldAppSubmissionDto);
        appSubmissionDtos.forEach(dto -> {
            dto.setSubLicenseeDto(MiscUtil.transferEntityDto(appSubmissionDto.getSubLicenseeDto(), SubLicenseeDto.class));
            AppEditSelectDto changeSelectDto = new AppEditSelectDto();
            changeSelectDto.setLicenseeEdit(true);
            appCommService.checkAffectedAppSubmissions(dto, null, feeDto, null, groupNo, changeSelectDto, null);
            dto.setAutoRfc(true);
        });
        return appSubmissionDtos;
    }

    private void setPreInspectionAndRequirement(AppSubmissionDto appSubmissionDtoByLicenceId) {
        PreOrPostInspectionResultDto preOrPostInspectionResultDto = appSubmissionService.judgeIsPreInspection(
                appSubmissionDtoByLicenceId);
        if (preOrPostInspectionResultDto == null) {
            appSubmissionDtoByLicenceId.setPreInspection(true);
            appSubmissionDtoByLicenceId.setRequirement(true);
        } else {
            appSubmissionDtoByLicenceId.setPreInspection(preOrPostInspectionResultDto.isPreInspection());
            appSubmissionDtoByLicenceId.setRequirement(preOrPostInspectionResultDto.isRequirement());
        }
    }

    public static void setSubmissionAmount(List<AppSubmissionDto> appSubmissionDtoList, FeeDto feeDto,
            List<AppFeeDetailsDto> appFeeDetailsDto, double amendTotal, BaseProcessClass bpc) {
        List<FeeInfoDto> detailFeeDtoList = feeDto.getFeeInfoDtos();
        double total = Calculator.add(feeDto.getTotal(), amendTotal);
        String totalString = Formatter.formatterMoney(total);
        ParamUtil.setSessionAttr(bpc.request, TOTAL_STR, totalString);
        ParamUtil.setSessionAttr(bpc.request, TOTAL_AMOUNT, total);
        if (IaisCommonUtils.isEmpty(detailFeeDtoList) || IaisCommonUtils.isEmpty(appSubmissionDtoList)) {
            return;
        }
        int index = 0;
        for (AppSubmissionDto appSubmissionDto : appSubmissionDtoList) {
            AppFeeDetailsDto appFeeDetailsDto1 = new AppFeeDetailsDto();
            Double lateFeeAmount = 0.0;
            double amount = 0.0;
            String lateFeeType = null;
            String appGrpNo = appSubmissionDtoList.get(0).getAppGrpNo();

            if(detailFeeDtoList.size()>index){
                FeeInfoDto feeInfoDto = detailFeeDtoList.get(index);

                if (feeInfoDto.getBaseSvcFeeExt() != null) {
                    if (feeInfoDto.getBaseSvcFeeExt().getLateFeeType() != null) {
                        lateFeeType = feeInfoDto.getBaseSvcFeeExt().getLateFeeType();
                        lateFeeAmount += feeInfoDto.getBaseSvcFeeExt().getLateFeeAmoumt();
                    }
                    appSubmissionDto.setRenewalFeeType(lateFeeType);
                    amount = setAppFeeDetails(feeInfoDto.getBaseSvcFeeExt(), lateFeeAmount, amount, lateFeeType, appGrpNo,
                            appSubmissionDto);
                }
                if (feeInfoDto.getThbSpecifiedFeeExt() != null) {
                    if (feeInfoDto.getThbSpecifiedFeeExt().getLateFeeType() != null) {
                        lateFeeAmount += feeInfoDto.getThbSpecifiedFeeExt().getLateFeeAmoumt();
                    }
                    amount = setAppFeeDetails(feeInfoDto.getThbSpecifiedFeeExt(), lateFeeAmount, amount, lateFeeType, appGrpNo,
                            appSubmissionDto);
                }
                if (feeInfoDto.getSimpleSpecifiedFeeExt() != null) {
                    if (feeInfoDto.getSimpleSpecifiedFeeExt().getLateFeeType() != null) {
                        lateFeeAmount += feeInfoDto.getSimpleSpecifiedFeeExt().getLateFeeAmoumt();
                    }
                    amount = setAppFeeDetails(feeInfoDto.getSimpleSpecifiedFeeExt(), lateFeeAmount, amount, lateFeeType, appGrpNo,
                            appSubmissionDto);
                }
                if (feeInfoDto.getComplexSpecifiedFeeExt() != null) {
                    if (feeInfoDto.getComplexSpecifiedFeeExt().getLateFeeType() != null) {
                        lateFeeAmount += feeInfoDto.getComplexSpecifiedFeeExt().getLateFeeAmoumt();
                    }
                    amount = setAppFeeDetails(feeInfoDto.getComplexSpecifiedFeeExt(), lateFeeAmount, amount, lateFeeType, appGrpNo,
                            appSubmissionDto);
                }
                if (feeInfoDto.getBundleSvcFeeExt() != null) {
                    if (feeInfoDto.getBundleSvcFeeExt().getLateFeeType() != null) {
                        if(!AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(feeInfoDto.getBundleSvcFeeExt().getSvcCode())){
                            lateFeeAmount += feeInfoDto.getBundleSvcFeeExt().getLateFeeAmoumt();
                        }
                    }
                    amount = setAppFeeDetails(feeInfoDto.getBundleSvcFeeExt(), lateFeeAmount, amount, lateFeeType, appGrpNo,
                            appSubmissionDto);
                }
                if(feeInfoDto.getIncludedSvcFeeExtList()!=null){
                    if (feeInfoDto.getBaseSvcFeeExt().getLateFeeType() != null) {
                        lateFeeAmount += 203.0*feeInfoDto.getIncludedSvcFeeExtList().size();
                    }
                    appSubmissionDto.setLateFee(lateFeeAmount);
                    appSubmissionDto.setLateFeeStr(Formatter.formatterMoney(lateFeeAmount));
                }
                if (feeInfoDto.getGradualFeeExt() != null) {
                    if (feeInfoDto.getGradualFeeExt().getLateFeeType() != null) {
                        lateFeeType = feeInfoDto.getGradualFeeExt().getLateFeeType();
                        lateFeeAmount += feeInfoDto.getGradualFeeExt().getLateFeeAmoumt();
                    }
                    appSubmissionDto.setRenewalFeeType(lateFeeType);
                    amount = setAppFeeDetails(feeInfoDto.getGradualFeeExt(), lateFeeAmount, amount, lateFeeType, appGrpNo,
                            appSubmissionDto);
                }
            }
            appSubmissionDto.setAmount(feeDto.getTotal());
            appSubmissionDto.setAmountStr(Formatter.formatterMoney(feeDto.getTotal()));
            appFeeDetailsDto1.setBaseFee(amount);
            appFeeDetailsDto1.setLaterFee(lateFeeAmount);
            appFeeDetailsDto1.setAdmentFee(0.0);
            appFeeDetailsDto1.setApplicationNo(10 > ++index ? (appGrpNo + "-0" + index) : (appGrpNo + "-" + index));
            appFeeDetailsDto.add(appFeeDetailsDto1);
        }
    }

    private static double setAppFeeDetails(FeeExtDto feeExtDto, Double lateFeeAmount, Double amount, String lateFeeType,
            String appGrpNo, AppSubmissionDto appSubmissionDto) {
        feeExtDto.setAppGroupNo(appGrpNo);
        appSubmissionDto.setLateFee(lateFeeAmount);
        appSubmissionDto.setLateFeeStr(Formatter.formatterMoney(lateFeeAmount));
        return amount + feeExtDto.getAmount();
    }

    public static HashMap<String, List<FeeExtDto>> getLaterFeeDetailsMap(List<FeeInfoDto> feeInfoDtos) {
        HashMap<String, List<FeeExtDto>> laterFeeDetailsMap = IaisCommonUtils.genNewHashMap();
        if (IaisCommonUtils.isEmpty(feeInfoDtos)) {
            return laterFeeDetailsMap;
        }
        for (FeeInfoDto info : feeInfoDtos) {
            if (info.getBaseSvcFeeExt() != null && info.getBaseSvcFeeExt().getLateFeeType() != null) {
                FeeExtDto laterFeeDetail = info.getBaseSvcFeeExt();
                laterFeeDetailSet(laterFeeDetail, laterFeeDetailsMap);
            }
            if (info.getBundleSvcFeeExt() != null && info.getBundleSvcFeeExt().getLateFeeType() != null) {
                FeeExtDto laterFeeDetail = info.getBundleSvcFeeExt();
                laterFeeDetailSet(laterFeeDetail, laterFeeDetailsMap);
            }
            if (info.getComplexSpecifiedFeeExt() != null && info.getComplexSpecifiedFeeExt().getLateFeeType() != null) {
                FeeExtDto laterFeeDetail = info.getComplexSpecifiedFeeExt();
                laterFeeDetailSet(laterFeeDetail, laterFeeDetailsMap);
            }
            if (info.getSimpleSpecifiedFeeExt() != null && info.getSimpleSpecifiedFeeExt().getLateFeeType() != null) {
                FeeExtDto laterFeeDetail = info.getSimpleSpecifiedFeeExt();
                laterFeeDetailSet(laterFeeDetail, laterFeeDetailsMap);
            }
            if (info.getThbSpecifiedFeeExt() != null && info.getThbSpecifiedFeeExt().getLateFeeType() != null) {
                FeeExtDto laterFeeDetail = info.getThbSpecifiedFeeExt();
                laterFeeDetailSet(laterFeeDetail, laterFeeDetailsMap);
            }
            if (info.getGradualFeeExt() != null && info.getGradualFeeExt().getLateFeeType() != null) {
                FeeExtDto laterFeeDetail = info.getGradualFeeExt();
                laterFeeDetailSet(laterFeeDetail, laterFeeDetailsMap);
            }
            if(info.getIncludedSvcFeeExtList()!=null){
                if (info.getBaseSvcFeeExt().getLateFeeType() != null) {
                    String targetLaterFeeType = info.getBaseSvcFeeExt().getLateFeeType();
                    List<FeeExtDto> list = laterFeeDetailsMap.get(targetLaterFeeType);
                    list.add(info.getIncludedSvcFeeExtList().get(0));
                    laterFeeDetailsMap.put(targetLaterFeeType, list);
                }
            }
        }
        return laterFeeDetailsMap;
    }

    private static void laterFeeDetailSet(FeeExtDto laterFeeDetail, HashMap<String, List<FeeExtDto>> laterFeeDetailsMap) {
        String targetLaterFeeType = laterFeeDetail.getLateFeeType();
        if (laterFeeDetailsMap.get(targetLaterFeeType) == null) {
            List<FeeExtDto> list = IaisCommonUtils.genNewArrayList();
            list.add(laterFeeDetail);
            laterFeeDetailsMap.put(laterFeeDetail.getLateFeeType(), list);
        } else {
            List<FeeExtDto> list = laterFeeDetailsMap.get(laterFeeDetail.getLateFeeType());
            list.add(laterFeeDetail);
            laterFeeDetailsMap.put(laterFeeDetail.getLateFeeType(), list);
        }
    }

    private void updateDraftStatus(AppSubmissionDto appSubmissionDto) {
        if (!StringUtil.isEmpty(appSubmissionDto.getLicenceId())) {
            List<ApplicationSubDraftDto> entity = applicationFeClient.getDraftByLicAppId(appSubmissionDto.getLicenceId()).getEntity();
            if (IaisCommonUtils.isEmpty(entity)) {
                entity = IaisCommonUtils.genNewArrayList();
            }
            List<ApplicationSubDraftDto> applicationSubDraftDtos = applicationFeClient.getDraftByLicAppIdAndStatus(
                    appSubmissionDto.getLicenceId(), ApplicationConsts.DRAFT_STATUS_PENDING_PAYMENT).getEntity();
            if (IaisCommonUtils.isNotEmpty(applicationSubDraftDtos)) {
                entity.addAll(applicationSubDraftDtos);
            }
            if (IaisCommonUtils.isNotEmpty(entity)) {
                for (ApplicationSubDraftDto applicationSubDraftDto : entity) {
                    applicationFeClient.deleteDraftByNo(applicationSubDraftDto.getDraftNo());
                }
            }
        }
    }

    //prepareAcknowledgement
    public void prepareAcknowledgement(BaseProcessClass bpc) throws Exception {
        InterInboxUserDto interInboxUserDto = (InterInboxUserDto) ParamUtil.getSessionAttr(bpc.request, "INTER_INBOX_USER_INFO");
        RenewDto renewDto = (RenewDto) ParamUtil.getSessionAttr(bpc.request, RenewalConstants.RENEW_DTO);
        List<AppSubmissionDto> appSubmissionDtos = renewDto.getAppSubmissionDtos();
        doDeleteMoreRenewDeclarations(appSubmissionDtos);
        for (AppSubmissionDto appSubmissionDto : appSubmissionDtos) {
            updateDraftStatus(appSubmissionDto);
        }
        List<AppSubmissionDto> rfcAppSubmissionDtos = (List<AppSubmissionDto>) bpc.request.getSession().getAttribute(
                RFC_APPSUBMISSIONDTOS);
        boolean b = false;
        if (IaisCommonUtils.isNotEmpty(rfcAppSubmissionDtos)) {
            for (AppSubmissionDto appSubmissionDto : rfcAppSubmissionDtos) {
                String appGrpNo = appSubmissionDto.getAppGrpNo();
                List<ApplicationDto> entity = appCommService.getApplicationsByGroupNo(appGrpNo);
                if (entity != null && !entity.isEmpty()) {
                    for (ApplicationDto applicationDto : entity) {
                        applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING);
                    }
                    String grpId = entity.get(0).getAppGrpId();
                    ApplicationGroupDto applicationGroupDto = applicationFeClient.getApplicationGroup(grpId).getEntity();
                    applicationGroupDto.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
                    applicationFeClient.updateApplicationList(entity);
                    applicationFeClient.updateAppGrpPmtStatus(applicationGroupDto);

                }
                if (!b && ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equalsIgnoreCase(appSubmissionDto.getAppType())) {
                    b = true;
                }
            }
        }

        //send rfc email
        if (b) {
            requestForChangeService.sendRfcSubmittedEmail(rfcAppSubmissionDtos, appSubmissionDtos.get(0).getPaymentMethod());
        }

        String licenseeId = null;
        if (interInboxUserDto != null) {
            licenseeId = interInboxUserDto.getLicenseeId();
        } else {
            log.debug(StringUtil.changeForLog("interInboxUserDto null"));
        }
        List<String> licenseeEmailAddrs = IaisEGPHelper.getLicenseeEmailAddrs(licenseeId);
        String emailAddress = ApplicationHelper.emailAddressesToString(licenseeEmailAddrs);
        ParamUtil.setSessionAttr(bpc.request, "emailAddress", emailAddress);
        ParamUtil.setRequestAttr(bpc.request, HAS_DETAIL, "N");
    }

    private void doDeleteMoreRenewDeclarations(List<AppSubmissionDto> appSubmissionDtos) {
        if (appSubmissionDtos.size() <= 1) {
            return;
        }
        AppSubmissionDto appSubmissionDto = appSubmissionDtos.get(0);
        String groupId = appSubmissionDto.getAppGrpId();
        if (StringUtil.isNotEmpty(groupId)) {
            List<AppDeclarationMessageDto> appDeclarationMessageDtos = applicationFeClient.getAppDeclarationMessageDto(
                    groupId).getEntity();
            if (IaisCommonUtils.isNotEmpty(appDeclarationMessageDtos)) {
                appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                applicationFeClient.inActiveDeclaration(appSubmissionDto);
            }
        }

    }

    //doInstructions
    public void doInstructions(BaseProcessClass bpc) {
        //go page2
        ParamUtil.setRequestAttr(bpc.request, PAGE_SWITCH, PAGE2);
    }

    public void jumpYeMian(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(
                APPEND_HTTP + request.getServerName() + "/main-web/eservice/INTERNET/MohInternetInbox", request);
        IaisEGPHelper.redirectUrl(response, tokenUrl);
    }

    /**
     * doLicenceReview
     *
     * @param bpc
     * @throws Exception
     */
    public void doLicenceReview(BaseProcessClass bpc) throws Exception {
        HttpServletRequest request = bpc.request;
        String crudActionType = ParamUtil.getString(request, "crud_action_additional");
        if ("exitSaveDraft".equals(crudActionType)) {
            jumpYeMian(request, bpc.response);
            return;
        }
        RenewDto renewDto = (RenewDto) ParamUtil.getSessionAttr(request, RenewalConstants.RENEW_DTO);
        List<AppSubmissionDto> appSubmissionDtos = renewDto.getAppSubmissionDtos();
        AppSubmissionDto firstSubmissionDto = appSubmissionDtos.get(0);
        boolean isSingle = appSubmissionDtos.size() == 1;
        String appType = ApplicationConsts.APPLICATION_TYPE_RENEWAL;
        // get data from page
        AppDataHelper.genRenewalData(firstSubmissionDto, isSingle, request);
        ParamUtil.setSessionAttr(request, RenewalConstants.RENEW_DTO, renewDto);
        Map<String, String> errorMap = AppValidatorHelper.doValidateRenewal(appSubmissionDtos, request);
        if (!errorMap.isEmpty()) {
            log.warn(StringUtil.changeForLog("Error msg: " + errorMap));
            return;
        }

        boolean isCharity = ApplicationHelper.isCharity(request);
        String licenseeId = ApplicationHelper.getLicenseeId(request);
        String appGrpNo = appCommService.getGroupNo(appType);
        String autoGrpNo = appSubmissionService.getGroupNo(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);

        List<AppSubmissionDto> autoAppSubmissionDtos = IaisCommonUtils.genNewArrayList();
        List<AppSubmissionDto> noAutoAppSubmissionDtos = IaisCommonUtils.genNewArrayList();

        AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) request.getSession().getAttribute(OLD_RENEW_APPSUBMISSIONDTO);
        if (isSingle) {
            AppEditSelectDto appEditSelectDto = firstSubmissionDto.getChangeSelectDto();
            if (appEditSelectDto == null) {
                appEditSelectDto = RfcHelper.rfcChangeModuleEvaluationDto(firstSubmissionDto, oldAppSubmissionDto);
            }
            appEditSelectDto.setNeedNewLicNo(true);
            appEditSelectDto.setAuto(AppConsts.NO);
            log.info(StringUtil.changeForLog(firstSubmissionDto.getLicenceNo() + " - App Edit Select Dto: "
                    + JsonUtil.parseToJson(appEditSelectDto)));
            RfcHelper.beforeSubmit(firstSubmissionDto, appEditSelectDto, appGrpNo, appType, request);
            Map<AppSubmissionDto, List<String>> errorListMap = checkOtherSubDto(appGrpNo, autoGrpNo, licenseeId, firstSubmissionDto,
                    oldAppSubmissionDto, autoAppSubmissionDtos, noAutoAppSubmissionDtos);
            if (!errorListMap.isEmpty()) {
                ParamUtil.setRequestAttr(request, RfcConst.SHOW_OTHER_ERROR, AppValidatorHelper.getErrorMsg(errorListMap));
                ParamUtil.setRequestAttr(request, PAGE_SWITCH, PAGE2);
                return;
            }
        } else if (appSubmissionDtos.size() > 1) {
            for (AppSubmissionDto appSubmissionDto : appSubmissionDtos) {
                AppEditSelectDto appEditSelectDto = ApplicationHelper.createAppEditSelectDto(false);
                appEditSelectDto.setNeedNewLicNo(true);
                appEditSelectDto.setAuto(AppConsts.NO);
                RfcHelper.beforeSubmit(appSubmissionDto, appEditSelectDto, appGrpNo, appType, request);
            }
            moreAppSubmissionDtoAction(appSubmissionDtos);
        }
        // fee
        List<AppFeeDetailsDto> appFeeDetailsDto = IaisCommonUtils.genNewArrayList();
        FeeDto renewalAmount;
        renewalAmount = appSubmissionService.getRenewalAmount(appSubmissionDtos, isCharity);
        double amendTotal = 0.0;
        for (AppSubmissionDto appSubmissionDto : noAutoAppSubmissionDtos) {
            FeeDto feeDto = configCommService.getGroupAmendAmount(appSubmissionDto, appSubmissionDto.getChangeSelectDto(),
                    isCharity);
            appSubmissionDto.setAmount(feeDto.getTotal());
            appSubmissionDto.setFeeInfoDtos(feeDto.getFeeInfoDtos());
            amendTotal = Calculator.add(amendTotal, feeDto.getTotal());
        }
        for (AppSubmissionDto appSubmissionDto : autoAppSubmissionDtos) {
            FeeDto feeDto = configCommService.getGroupAmendAmount(appSubmissionDto, appSubmissionDto.getChangeSelectDto(),
                    isCharity);
            appSubmissionDto.setAmount(feeDto.getTotal());
            appSubmissionDto.setFeeInfoDtos(feeDto.getFeeInfoDtos());
            amendTotal = Calculator.add(amendTotal, feeDto.getTotal());
        }
        setSubmissionAmount(appSubmissionDtos, renewalAmount, appFeeDetailsDto, amendTotal, bpc);
        for (AppFeeDetailsDto detailsDto : appFeeDetailsDto) {
            appSubmissionService.saveAppFeeDetails(detailsDto);
        }
        firstSubmissionDto.setFeeInfoDtos(renewalAmount.getFeeInfoDtos());

        List<AppSubmissionDto> nonAutoAppSubmissionDtoList = IaisCommonUtils.genNewArrayList();
        nonAutoAppSubmissionDtoList.addAll(appSubmissionDtos);
        boolean needSetOtherEff = isSingle && IaisCommonUtils.isNotEmpty(noAutoAppSubmissionDtos);
        if (needSetOtherEff) {
            Date effectiveDate = MiscUtil.addDays(oldAppSubmissionDto.getLicExpiryDate(), 1);
            String effectiveDateStr = Formatter.formatDate(effectiveDate);
            firstSubmissionDto.setEffectiveDate(effectiveDate);
            firstSubmissionDto.setEffectiveDateStr(effectiveDateStr);
            for (AppSubmissionDto appSubmissionDto : noAutoAppSubmissionDtos) {
                setRfcSubInfo(firstSubmissionDto, appSubmissionDto, true);
            }
        }
        nonAutoAppSubmissionDtoList.addAll(noAutoAppSubmissionDtos);

        if ((!autoAppSubmissionDtos.isEmpty() || !noAutoAppSubmissionDtos.isEmpty()) && isSingle) {
            AuditTrailHelper.auditFunctionWithLicNo(AuditTrailConsts.MODULE_RENEW, AuditTrailConsts.MODULE_RENEW,
                    firstSubmissionDto.getLicenceNo());
        }
        List<AppSubmissionDto> renewAppSubmissionDtos = IaisCommonUtils.genNewArrayList();
        String eventRefNo = String.valueOf(System.currentTimeMillis());
        String autoGroupId = null;
        String appGrpStatus = ApplicationConsts.APPLICATION_GROUP_STATUS_SUBMITED;
        if (!autoAppSubmissionDtos.isEmpty()) {
            for (AppSubmissionDto appSubmissionDto : autoAppSubmissionDtos) {
                setRfcSubInfo(firstSubmissionDto, appSubmissionDto, isSingle);
            }
            List<AppSubmissionDto> autoAppSubmissionList = outRenewalService.saveAppSubmissionList(autoAppSubmissionDtos,
                    eventRefNo, bpc);
            autoGroupId = autoAppSubmissionList.get(0).getAppGrpId();
            renewAppSubmissionDtos.addAll(autoAppSubmissionList);
            // re-set status
            appGrpStatus = ApplicationConsts.APPLICATION_GROUP_STATUS_PENDING_AUTO;
        }

        for (AppSubmissionDto appSubmissionDto : nonAutoAppSubmissionDtoList) {
            appSubmissionDto.setAppGrpStatus(appGrpStatus);
        }
        List<AppSubmissionDto> newAppSubmissionList = outRenewalService.saveAppSubmissionList(nonAutoAppSubmissionDtoList,
                eventRefNo, bpc);
        renewAppSubmissionDtos.addAll(newAppSubmissionList);
        String notAutoGroupId = newAppSubmissionList.get(0).getAppGrpId();
        // app group misc
        appCommService.saveAutoRfcLinkAppGroupMisc(notAutoGroupId, autoGroupId);
        appSubmissionDtos.forEach(dto -> dto.setAppGrpId(notAutoGroupId));
        // draft
        appSubmissionService.handleDraft(licenseeId, appSubmissionDtos);
        ParamUtil.setSessionAttr(request, RENEW_APPSUBMISSIONDTOS, (Serializable) renewAppSubmissionDtos);
        ParamUtil.setSessionAttr(request, RenewalConstants.RENEW_DTO, renewDto);

        //go page3
        ParamUtil.setRequestAttr(request, PAGE_SWITCH, PAGE3);
    }

    private Map<AppSubmissionDto, List<String>> checkOtherSubDto(String appGrpNo, String autoGrpNo, String licenseeId,
            AppSubmissionDto appSubmissionDto, AppSubmissionDto oldAppSubmissionDto,
            List<AppSubmissionDto> autoAppSubmissionDtos, List<AppSubmissionDto> noAutoAppSubmissionDtos) throws Exception {
        AppEditSelectDto appEditSelectDto = appSubmissionDto.getChangeSelectDto();
        Map<AppSubmissionDto, List<String>> errorListMap = IaisCommonUtils.genNewHashMap();
        // create rfc data
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        if (appGrpPremisesDtoList != null && appEditSelectDto.isPremisesEdit()) {
            AppEditSelectDto changeSelectDto = new AppEditSelectDto();
            changeSelectDto.setPremisesEdit(true);
            changeSelectDto.setPremisesListEdit(true);
            changeSelectDto.setChangeHciName(appEditSelectDto.isChangeHciName());
            changeSelectDto.setChangeInLocation(appEditSelectDto.isChangeInLocation());
            changeSelectDto.setChangeFloorUnits(appEditSelectDto.isChangeFloorUnits());
            for (AppGrpPremisesDto premisesDto : appGrpPremisesDtoList) {
                String[] selectedLicences = premisesDto.getSelectedLicences();
                List<LicenceDto> licenceDtos = null;
                List<LicenceDto> existLicences = premisesDto.getLicenceDtos();
                if (IaisCommonUtils.isNotEmpty(existLicences)) {
                    licenceDtos = existLicences.stream()
                            .filter(dto -> StringUtil.isIn(dto.getId(), selectedLicences))
                            .collect(Collectors.toList());
                }
                if (licenceDtos == null || licenceDtos.isEmpty()) {
                    continue;
                }
                String draftNo = appSubmissionDto.getDraftNo();
                changeSelectDto.setPremType(premisesDto.getPremisesType());
                boolean autoRfc = changeSelectDto.isAutoRfc();
                for (LicenceDto licenceDto : licenceDtos) {
                    AppSubmissionDto appSubmissionDtoByLicenceId = requestForChangeService.getAppSubmissionDtoByLicenceId(
                            licenceDto.getId());
                    // Premises
                    ApplicationHelper.reSetPremeses(appSubmissionDtoByLicenceId, premisesDto);
                    appSubmissionDtoByLicenceId.setDraftNo(draftNo);
                    AppEditSelectDto editSelectDto = MiscUtil.transferEntityDto(changeSelectDto, AppEditSelectDto.class);
                    RfcHelper.beforeSubmit(appSubmissionDtoByLicenceId, editSelectDto, autoRfc ? autoGrpNo : appGrpNo,
                            ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE, null);
                    if (autoRfc) {
                        autoAppSubmissionDtos.add(appSubmissionDtoByLicenceId);
                    } else {
                        noAutoAppSubmissionDtos.add(appSubmissionDtoByLicenceId);
                    }
                }
            }
        }
        log.info(StringUtil.changeForLog("---- premisesEdit autoAppSubmissionDtos size :" + autoAppSubmissionDtos.size()));
        if (!ConfigHelper.getBoolean("halp.rfc.split.flag", true)) {
            if (appEditSelectDto.isLicenseeEdit()) {
                //gen lic change rfc
                ApplicationHelper.addToAuto(getAutoChangeLicAppSubmissions(oldAppSubmissionDto, autoGrpNo, appSubmissionDto),
                        autoAppSubmissionDtos);
            }
            log.info(StringUtil.changeForLog("---- licenseeEdit autoAppSubmissionDtos size :" + autoAppSubmissionDtos.size()));

            if (appEditSelectDto.isServiceEdit()) {
                List<AppSubmissionDto> personAppSubmissionList = licCommService.personContact(licenseeId, appSubmissionDto,
                        oldAppSubmissionDto, 2);
                ApplicationHelper.addToAuto(personAppSubmissionList, autoAppSubmissionDtos);
            }
            log.info(StringUtil.changeForLog("---- serviceEdit autoAppSubmissionDtos size :" + autoAppSubmissionDtos.size()));
        }
        errorListMap.putAll(validateOtherSubDto(autoAppSubmissionDtos));
        errorListMap.putAll(validateOtherSubDto(noAutoAppSubmissionDtos));
        return errorListMap;
    }

    private Map<AppSubmissionDto, List<String>> validateOtherSubDto(List<AppSubmissionDto> appSubmissionDtos) {
        Map<AppSubmissionDto, List<String>> errorListMap = IaisCommonUtils.genNewHashMap();
        for (AppSubmissionDto dto : appSubmissionDtos) {
            List<String> errorList = AppValidatorHelper.doPreviewSubmitValidate(null, dto, false);
            if (!errorList.isEmpty()) {
                errorListMap.put(dto, errorList);
            }
        }
        return errorListMap;
    }

    //doPayment
    public void doPayment(BaseProcessClass bpc) throws Exception {
        PaymentValidate paymentValidate = new PaymentValidate();
        Map<String, String> errorMap = paymentValidate.validate(bpc.request);
        if (!errorMap.isEmpty()) {
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(bpc.request, ERROR_MSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, PAGE_SWITCH, PAGE3);
            return;
        }
        String backUrl = "/hcsa-licence-web/eservice/INTERNET/MohWithOutRenewal/1/preparatData";
        //set back url
        ParamUtil.setSessionAttr(bpc.request, BACK_URL, backUrl);
        String payMethod = ParamUtil.getString(bpc.request, "payMethod");
        Double totalAmount = (Double) ParamUtil.getSessionAttr(bpc.request, TOTAL_AMOUNT);
        RenewDto renewDto = (RenewDto) ParamUtil.getSessionAttr(bpc.request, RenewalConstants.RENEW_DTO);
        List<AppSubmissionDto> appSubmissionDtos = renewDto.getAppSubmissionDtos();
        //
        ParamUtil.setSessionAttr(bpc.request, RenewalConstants.RENEW_DTO, renewDto);
        String groupNo = "";
        String appGrpId = "";
        if (!IaisCommonUtils.isEmpty(appSubmissionDtos)) {
            groupNo = appSubmissionDtos.get(0).getAppGrpNo();
            appGrpId = appSubmissionDtos.get(0).getAppGrpId();
            for (AppSubmissionDto appSubmissionDto : appSubmissionDtos) {
                appSubmissionDto.setPaymentMethod(payMethod);
            }
        }
        if (ApplicationConsts.PAYMENT_METHOD_NAME_CREDIT.equals(payMethod)
                || ApplicationConsts.PAYMENT_METHOD_NAME_NETS.equals(payMethod)
                || ApplicationConsts.PAYMENT_METHOD_NAME_PAYNOW.equals(payMethod)) {
            try {
                String url = NewApplicationHelper.genBankUrl(payMethod, totalAmount, backUrl, payMethod,
                        groupNo + "_" + System.currentTimeMillis(), bpc.request);
                if (appSubmissionDtos != null && appSubmissionDtos.size() == 1) {
                    appSubmissionService.updateDraftStatus(appSubmissionDtos.get(0).getDraftNo(),
                            ApplicationConsts.DRAFT_STATUS_PENDING_PAYMENT);
                }
                IaisEGPHelper.redirectUrl(bpc.response, url);
                bpc.request.setAttribute("paymentAmount", totalAmount);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        } else if (ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(payMethod) && !StringUtil.isEmpty(appGrpId)) {
            log.info(StringUtil.changeForLog("start giro payment appGrpId {}" + appGrpId));
            setAmount(appSubmissionDtos);
            try {
                //renew
                sendEmail(bpc.request, appSubmissionDtos);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                log.error(StringUtil.changeForLog("==send email error=="));
            }
            AppSubmissionDto appSubmissionDto = appSubmissionDtos.get(0);
            String giroAccNum = ParamUtil.getString(bpc.request, "giroAccount");
            appSubmissionDto.setGiroAcctNum(giroAccNum);
            appSubmissionService.updatePayment(appSubmissionDto, null);

            String txnDt = DateUtil.formatDate(new Date(), IaisEGPConstant.DATE_FORMAT);
            ParamUtil.setSessionAttr(bpc.request, TXN_DT, txnDt);
            ParamUtil.setSessionAttr(bpc.request, TXN_REFNO, appSubmissionDto.getGiroTranNo());
            ParamUtil.setRequestAttr(bpc.request, PAGE_SWITCH, PAGE4);
            StringBuilder url = new StringBuilder();
            url.append(APPEND_HTTP).append(bpc.request.getServerName())
                    .append("/hcsa-licence-web/eservice/INTERNET/MohWithOutRenewal/1/preparatData");
            RedirectUtil.redirect(url.toString(), bpc.request, bpc.response);
        }

    }

    private Double setAmount(List<AppSubmissionDto> appSubmissionDtos) {
        Double d1 = 0.0;
        for (AppSubmissionDto dto : appSubmissionDtos) {
            d1 = add(add(d1, dto.getAmount()), dto.getLateFee());
        }
        appSubmissionDtos.get(0).setTotalAmountGroup(d1);
        return d1;
    }

    private double add(Double v1, Double v2) {
        v1 = (v1 == null ? Double.valueOf(0.0D) : v1);
        v2 = (v2 == null ? Double.valueOf(0.0D) : v2);
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    //doAcknowledgement
    public void doAcknowledgement(BaseProcessClass bpc) {
        String payMethod = ParamUtil.getString(bpc.request, "payMethod");
        String totalStr = (String) ParamUtil.getSessionAttr(bpc.request, TOTAL_STR);
        if ("Credit".equals(payMethod)) {
            // nothing to do
        } else if ("$0".equals(totalStr)) {
            RenewDto renewDto = (RenewDto) bpc.request.getSession().getAttribute(RenewalConstants.RENEW_DTO);
            if (renewDto != null) {
                List<AppSubmissionDto> appSubmissionDtos = renewDto.getAppSubmissionDtos();
                if (appSubmissionDtos != null) {
                    //send email
                    sendEmail(bpc.request, appSubmissionDtos);
                    for (AppSubmissionDto appSubmissionDto : appSubmissionDtos) {
                        String appGrpNo = appSubmissionDto.getAppGrpNo();
                        boolean autoRfc = appSubmissionDto.isAutoRfc();
                        List<ApplicationDto> applicationDtos = appCommService.getApplicationsByGroupNo(appGrpNo);
                        if (applicationDtos != null && !applicationDtos.isEmpty()) {
                            for (ApplicationDto applicationDto : applicationDtos) {
                                if (autoRfc) {
                                    applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_APPROVED);
                                } else {
                                    applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING);
                                }
                            }
                            applicationFeClient.updateApplicationList(applicationDtos);
                            String grpId = applicationDtos.get(0).getAppGrpId();
                            ApplicationGroupDto entity = applicationFeClient.getApplicationGroup(grpId).getEntity();
                            entity.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
                            applicationFeClient.updateAppGrpPmtStatus(entity);
                        }
                    }
                }
            }
            List<AppSubmissionDto> rfcAppSubmissionDtos = (List<AppSubmissionDto>) bpc.request.getSession().getAttribute(
                    RFC_APPSUBMISSIONDTOS);
            if (rfcAppSubmissionDtos != null) {
                for (AppSubmissionDto appSubmissionDto : rfcAppSubmissionDtos) {
                    String appGrpNo = appSubmissionDto.getAppGrpNo();
                    boolean autoRfc = appSubmissionDto.isAutoRfc();
                    List<ApplicationDto> entity = appCommService.getApplicationsByGroupNo(appGrpNo);
                    if (entity != null && !entity.isEmpty()) {
                        for (ApplicationDto applicationDto : entity) {
                            if (autoRfc) {
                                applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_APPROVED);
                            } else {
                                applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING);
                            }
                        }
                        String grpId = entity.get(0).getAppGrpId();
                        ApplicationGroupDto applicationGroupDto = applicationFeClient.getApplicationGroup(grpId).getEntity();
                        applicationGroupDto.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
                        applicationFeClient.updateAppGrpPmtStatus(applicationGroupDto);
                    }
                }
            }
            String txnDt = DateUtil.formatDate(new Date(), IaisEGPConstant.DATE_FORMAT);
            ParamUtil.setSessionAttr(bpc.request, TXN_DT, txnDt);
            ParamUtil.setRequestAttr(bpc.request, PAGE_SWITCH, PAGE4);
        } else {
            ParamUtil.setRequestAttr(bpc.request, PAGE_SWITCH, PAGE3);
        }
    }

    //controlSwitch
    public void controlSwitch(BaseProcessClass bpc) throws Exception {
        String switchValue = ParamUtil.getString(bpc.request, "switch_value");
        if (INSTRUCTIONS.equals(switchValue)) {
            //controlSwitch
            ParamUtil.setRequestAttr(bpc.request, CONTROL_SWITCH, switchValue);
        } else if (REVIEW.equals(switchValue)) {
            ParamUtil.setRequestAttr(bpc.request, CONTROL_SWITCH, switchValue);
        } else if (PAYMENT.equals(switchValue)) {
            ParamUtil.setRequestAttr(bpc.request, CONTROL_SWITCH, switchValue);
        } else if (ACKNOWLEDGEMENT.equals(switchValue)) {
            ParamUtil.setRequestAttr(bpc.request, CONTROL_SWITCH, switchValue);
        } else if (PAGE1.equals(switchValue)) {
            ParamUtil.setRequestAttr(bpc.request, CONTROL_SWITCH, BACK);
            ParamUtil.setRequestAttr(bpc.request, PAGE_SWITCH, switchValue);
            String userAgreement = ParamUtil.getString(bpc.request, "verifyInfoCheckbox");
            if (!StringUtil.isEmpty(userAgreement) && AppConsts.YES.equals(userAgreement)) {
                ParamUtil.setSessionAttr(bpc.request, USER_AGREEMENT, Boolean.TRUE);
            } else {
                ParamUtil.setSessionAttr(bpc.request, USER_AGREEMENT, Boolean.FALSE);
            }
        } else if (PAGE2.equals(switchValue)) {
            ParamUtil.setRequestAttr(bpc.request, CONTROL_SWITCH, BACK);
            ParamUtil.setRequestAttr(bpc.request, PAGE_SWITCH, switchValue);
        } else if (EDIT.equals(switchValue)) {
            ParamUtil.setRequestAttr(bpc.request, CONTROL_SWITCH, EDIT);
            ParamUtil.setRequestAttr(bpc.request, PAGE_SWITCH, PAGE2);
        } else if ("paymentBack".equals(switchValue)) {
            RenewDto renewDto = (RenewDto) ParamUtil.getSessionAttr(bpc.request, RenewalConstants.RENEW_DTO);
            if (renewDto != null) {
                List<AppSubmissionDto> appSubmissionDtos = renewDto.getAppSubmissionDtos();
                if (!IaisCommonUtils.isEmpty(appSubmissionDtos)) {
                    for (AppSubmissionDto appSubmissionDto : appSubmissionDtos) {
                        appSubmissionDto.setId(null);
                        appSubmissionDto.setAppGrpNo(null);
                        appSubmissionDto.setAppGrpId(null);
                    }
                    if (appSubmissionDtos.size() == 1) {
                        appSubmissionDtos.get(0).setAppEditSelectDto(null);
                    }
                }
                ParamUtil.setSessionAttr(bpc.request, RenewalConstants.RENEW_DTO, renewDto);
            }
            ParamUtil.setRequestAttr(bpc.request, CONTROL_SWITCH, BACK);
            ParamUtil.setRequestAttr(bpc.request, PAGE_SWITCH, PAGE2);
        }
    }

    /**
     * AutoStep: determineAutoRenewalEligibility
     *
     * @param bpc
     * @throws
     */
    public void determineAutoRenewalEligibility(BaseProcessClass bpc) {
        log.info("**** the determineAutoRenewalEligibility  prepare start  ******");
        String editValue = ParamUtil.getString(bpc.request, EDIT_VALUE);
        if (!StringUtil.isEmpty(editValue)) {
            RenewDto renewDto = (RenewDto) ParamUtil.getSessionAttr(bpc.request, RenewalConstants.RENEW_DTO);
            if (renewDto != null) {
                List<AppSubmissionDto> appSubmissionDtos = renewDto.getAppSubmissionDtos();
                if (!IaisCommonUtils.isEmpty(appSubmissionDtos) && appSubmissionDtos.size() == 1) {
                    ParamUtil.setRequestAttr(bpc.request, EDIT_VALUE, editValue);
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "jump");
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
    public void markPostInspection(BaseProcessClass bpc) {
        log.info("**** the markPostInspection start  ******");
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, "appSubmissionDto");
        setPreInspectionAndRequirement(appSubmissionDto);
        log.info("**** the markPostInspection end ******");
    }


    /**
     * AutoStep: prepareJump
     */
    public void prepareJump(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do prepareJump start ...."));
        String editValue = ParamUtil.getString(bpc.request, EDIT_VALUE);
        RenewDto renewDto = (RenewDto) ParamUtil.getSessionAttr(bpc.request, RenewalConstants.RENEW_DTO);
        List<AppSubmissionDto> appSubmissionDtos = renewDto.getAppSubmissionDtos();
        AppSubmissionDto appSubmissionDto = appSubmissionDtos.get(0);
        AppEditSelectDto appEditSelectDto = appSubmissionDto.getAppEditSelectDto() == null ? new AppEditSelectDto() : appSubmissionDto.getAppEditSelectDto();
        if (!StringUtil.isEmpty(editValue)) {
            ParamUtil.setSessionAttr(bpc.request, PREFIXTITLE, "amending");
            if (RfcConst.EDIT_PREMISES.equals(editValue)) {
                appEditSelectDto.setPremisesEdit(true);
                ParamUtil.setRequestAttr(bpc.request, RfcConst.RFC_CURRENT_EDIT, RfcConst.EDIT_PREMISES);
            } else if (RfcConst.EDIT_SPECIALISED.equals(editValue)) {
                appEditSelectDto.setSpecialisedEdit(true);
                ParamUtil.setRequestAttr(bpc.request, RfcConst.RFC_CURRENT_EDIT, RfcConst.EDIT_SPECIALISED);
            } else if (RfcConst.EDIT_SERVICE.equals(editValue)) {
                appEditSelectDto.setServiceEdit(true);
                ParamUtil.setRequestAttr(bpc.request, RfcConst.RFC_CURRENT_EDIT, RfcConst.EDIT_SERVICE);
            } else if (RfcConst.EDIT_LICENSEE.equalsIgnoreCase(editValue)) {
                appEditSelectDto.setLicenseeEdit(true);
                ParamUtil.setRequestAttr(bpc.request, RfcConst.RFC_CURRENT_EDIT, RfcConst.EDIT_LICENSEE);
            }
            appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
            appSubmissionDto.setClickEditPage(null);
            appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_RENEWAL);
            ParamUtil.setRequestAttr(bpc.request, RfcConst.APPSUBMISSIONDTORFCATTR, appSubmissionDto);
            ParamUtil.setRequestAttr(bpc.request, "appType", ApplicationConsts.APPLICATION_TYPE_RENEWAL);
        }


        log.info(StringUtil.changeForLog("the do prepareJump end ...."));
    }

    /**
     * AutoStep: toPrepareData
     */
    public void toPrepareData(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do toPrepareData start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, HcsaAppConst.APPSUBMISSIONDTO);
        RenewDto renewDto = new RenewDto();
        List<AppSubmissionDto> appSubmissionDtos = (List<AppSubmissionDto>) ParamUtil.getSessionAttr(bpc.request, "oldSubmissionDtos");
        if (IaisCommonUtils.isNotEmpty(appSubmissionDtos)) {
            for (int i = 0; i < appSubmissionDtos.size(); i++) {
                if (appSubmissionDtos.get(i).getLicenceId().equalsIgnoreCase(appSubmissionDto.getLicenceId())) {
                    appSubmissionDtos.set(i, appSubmissionDto);
                    break;
                }
            }

        } else {
            appSubmissionDtos = IaisCommonUtils.genNewArrayList();
        }
        renewDto.setAppSubmissionDtos(appSubmissionDtos);
        if (appSubmissionDto != null) {
            AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
            appEditSelectDto.setPremisesEdit(true);
            appEditSelectDto.setSpecialisedEdit(true);
            appEditSelectDto.setServiceEdit(true);
            appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = IaisCommonUtils.genNewArrayList();
            List<List<AppSvcPrincipalOfficersDto>> principalOfficersDtosList = IaisCommonUtils.genNewArrayList();
            List<List<AppSvcPrincipalOfficersDto>> deputyPrincipalOfficersDtosList = IaisCommonUtils.genNewArrayList();
            for (AppSubmissionDto appSubmissionDtoCirculation : appSubmissionDtos) {
                if (!appSubmissionDto.getAppSvcRelatedInfoDtoList().isEmpty()) {
                    AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSubmissionDtoCirculation.getAppSvcRelatedInfoDtoList().get(0);
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
            }
            ParamUtil.setSessionAttr(bpc.request, "currentPreviewSvcInfoList", (Serializable) appSvcRelatedInfoDtoList);
            ParamUtil.setSessionAttr(bpc.request, "ReloadPrincipalOfficersList", (Serializable) principalOfficersDtosList);
            ParamUtil.setSessionAttr(bpc.request, "deputyPrincipalOfficersDtosList", (Serializable) deputyPrincipalOfficersDtosList);
        }
        if (appSubmissionDtos.size() > 1) {
            ParamUtil.setSessionAttr(bpc.request, RenewalConstants.IS_SINGLE, AppConsts.NO);
        } else {
            ParamUtil.setSessionAttr(bpc.request, RenewalConstants.IS_SINGLE, AppConsts.YES);
        }
        AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) bpc.request.getSession().getAttribute(OLD_RENEW_APPSUBMISSIONDTO);
        if (oldAppSubmissionDto != null && appSubmissionDto != null) {
            boolean eqGrpPremises = RfcHelper.isChangeGrpPremises(appSubmissionDto.getAppGrpPremisesDtoList(),
                    oldAppSubmissionDto.getAppGrpPremisesDtoList());
            boolean eqServiceChange = RfcHelper.isChangeServiceInfo(appSubmissionDto.getAppSvcRelatedInfoDtoList(),
                    oldAppSubmissionDto.getAppSvcRelatedInfoDtoList());
            if (eqGrpPremises || eqServiceChange /*|| eqDocChange*/) {
                bpc.request.getSession().setAttribute(PREFIXTITLE, "amending");
            } else {
                bpc.request.getSession().setAttribute(PREFIXTITLE, "renewing");
            }
        }
        ParamUtil.setSessionAttr(bpc.request, RenewalConstants.RENEW_DTO, renewDto);
        ParamUtil.setRequestAttr(bpc.request, PAGE_SWITCH, PAGE2);
        log.info(StringUtil.changeForLog("the do toPrepareData end ...."));
    }

    //=============================================================================
    //private method
    //=============================================================================

    public void sendEmail(HttpServletRequest request, List<AppSubmissionDto> appSubmissionDtos) {
        if (!IaisCommonUtils.isEmpty(appSubmissionDtos)) {
            String paymentMethod = appSubmissionDtos.get(0).getPaymentMethod();
            String applicationTypeShow = MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_TYPE_RENEWAL);
            String mohName = AppConsts.MOH_AGENCY_NAME;
            String loginUrl = HmacConstants.HTTPS + "://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
            String groupNo = appSubmissionDtos.get(0).getAppGrpNo();
            LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
            String applicationName = loginContext.getUserName();
            log.info(StringUtil.changeForLog("send renewal application notification applicationName : " + applicationName));
            log.info(StringUtil.changeForLog("send renewal application notification paymentMethod : " + paymentMethod));
            int appNoIndex = 1;
            String appNo = groupNo;
            String appDate = Formatter.formatDateTime(new Date(), IaisEGPConstant.DATE_FORMAT);
            log.info(StringUtil.changeForLog("send email appSubmissionDtos size : " + appSubmissionDtos.size()));
            StringBuilder stringBuilderAPPNum = new StringBuilder();
            String temp = "have";
            for (AppSubmissionDto appSubmissionDto : appSubmissionDtos) {
                List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
                if (!IaisCommonUtils.isEmpty(appGrpPremisesDtoList)) {
                    for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
                        String hciName = appGrpPremisesDto.getHciName();
                        log.info(StringUtil.changeForLog("hci name : " + hciName));
                        appNo = getAppNo(groupNo, appNoIndex);
                        if (appNoIndex != 1) {
                            stringBuilderAPPNum.append(" and ");
                        }
                        stringBuilderAPPNum.append(appNo);
                        appNoIndex++;
                    }
                }
            }
            if (appNoIndex == 2) {
                temp = "has";
            }
            List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
            for (AppSubmissionDto appSubmissionDto : appSubmissionDtos) {
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(appSubmissionDto.getServiceName());
                if (hcsaServiceDto != null && !StringUtil.isEmpty(hcsaServiceDto.getSvcCode())
                        && !svcCodeList.contains(hcsaServiceDto.getSvcCode())) {
                    svcCodeList.add(hcsaServiceDto.getSvcCode());
                }
            }

            String amountStr = (String) ParamUtil.getSessionAttr(request, TOTAL_STR);
            String applicationNumber = stringBuilderAPPNum.toString();
            log.info(StringUtil.changeForLog("send renewal application notification application no : " + appNo));
            log.info(StringUtil.changeForLog("send renewal application notification subject no : " + applicationNumber));
            log.info(StringUtil.changeForLog("send renewal application notification amountStr : " + amountStr));
            Map<String, Object> map = IaisCommonUtils.genNewHashMap();
            map.put("ApplicantName", applicationName);
            map.put("ApplicationType", applicationTypeShow);
            map.put("ApplicationNumber", applicationNumber);
            map.put("ApplicationDate", appDate);
            map.put("paymentAmount", amountStr);
            map.put("systemLink", loginUrl);
            map.put("emailAddress", systemAddressOne);
            map.put("MOH_AGENCY_NAME", mohName);
            String paymentMethodName;
            if (ApplicationConsts.PAYMENT_METHOD_NAME_CREDIT.equals(paymentMethod)
                    || ApplicationConsts.PAYMENT_METHOD_NAME_NETS.equals(paymentMethod)
                    || ApplicationConsts.PAYMENT_METHOD_NAME_PAYNOW.equals(paymentMethod)) {
                paymentMethodName = "onlinePayment";
                map.put("paymentMethod", paymentMethodName);
                log.info(StringUtil.changeForLog("send renewal application notification paymentMethodName : " + paymentMethodName));
            } else if (ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(paymentMethod)) {
                //GIRO payment method
                paymentMethodName = "GIRO";
                map.put("usualDeduction", "next 7 working days");
                map.put("accountNumber", serviceConfigService.getGiroAccountByGroupNo(groupNo));
                map.put("paymentMethod", paymentMethodName);
            }
            try {
                Map<String, Object> subMap = IaisCommonUtils.genNewHashMap();
                subMap.put("ApplicationType", applicationTypeShow);
                subMap.put("ApplicationNumber", applicationNumber);
                subMap.put("temp", temp);
                String emailSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_SUBMITTED, subMap);
                String smsSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_SUBMITTED_SMS, subMap);
                String messageSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_SUBMITTED_MESSAGE, subMap);
                log.debug(StringUtil.changeForLog("emailSubject : " + emailSubject));
                log.debug(StringUtil.changeForLog("smsSubject : " + smsSubject));
                log.debug(StringUtil.changeForLog("messageSubject : " + messageSubject));
                EmailParam emailParam = new EmailParam();
                emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_SUBMITTED);
                emailParam.setTemplateContent(map);
                emailParam.setQueryCode(appNo);
                emailParam.setReqRefNum(appNo);
                emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_LICENSEE_ID);
                emailParam.setRefId(loginContext.getLicenseeId());
                emailParam.setSubject(emailSubject);
                //send email
                log.info(StringUtil.changeForLog("send renewal application email"));
                notificationHelper.sendNotification(emailParam);
                log.info(StringUtil.changeForLog("send renewal application email end"));
                //send sms
                EmailParam smsParam = new EmailParam();
                smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_SUBMITTED_SMS);
                smsParam.setSubject(smsSubject);
                smsParam.setQueryCode(appNo);
                smsParam.setReqRefNum(appNo);
                smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_LICENSEE_ID);
                smsParam.setRefId(loginContext.getLicenseeId());
                log.info(StringUtil.changeForLog("send renewal application sms"));
                notificationHelper.sendNotification(smsParam);
                log.info(StringUtil.changeForLog("send renewal application sms end"));
                //send message
                EmailParam messageParam = new EmailParam();
                messageParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_SUBMITTED_MESSAGE);
                messageParam.setTemplateContent(map);
                messageParam.setQueryCode(appNo);
                messageParam.setReqRefNum(appNo);
                messageParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
                messageParam.setRefId(appNo);
                messageParam.setSubject(messageSubject);
                messageParam.setSvcCodeList(svcCodeList);
                log.info(StringUtil.changeForLog("send renewal application message"));
                notificationHelper.sendNotification(messageParam);
                log.info(StringUtil.changeForLog("send renewal application message end"));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                log.error(StringUtil.changeForLog("=========send email error"));
            }
            log.info(StringUtil.changeForLog("send renewal application notification end"));
        } else {
            log.debug(StringUtil.changeForLog("send email error , appSubmissionDtos is null"));
        }
    }

    private String getEmailSubject(String templateId, Map<String, Object> subMap) {
        String subject = "-";
        if (!StringUtil.isEmpty(templateId)) {
            MsgTemplateDto emailTemplateDto = appSubmissionService.getMsgTemplateById(templateId);
            if (emailTemplateDto != null) {
                try {
                    if (!IaisCommonUtils.isEmpty(subMap)) {
                        subject = MsgUtil.getTemplateMessageByContent(emailTemplateDto.getTemplateName(), subMap);
                    } else {
                        subject = emailTemplateDto.getTemplateName();
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return subject;
    }

    private String getAppNo(String groupNo, int index) {
        StringBuilder appNo = new StringBuilder(groupNo);
        appNo.append('-');
        if (index > 9) {
            appNo.append(index);
        } else {
            appNo.append('0').append(index);
        }
        return appNo.toString();
    }

}
