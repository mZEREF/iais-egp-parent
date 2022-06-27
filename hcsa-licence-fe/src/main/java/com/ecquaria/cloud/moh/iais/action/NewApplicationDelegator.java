package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.api.config.GatewayConfig;
import com.ecquaria.cloud.moh.iais.api.config.GatewayConstants;
import com.ecquaria.cloud.moh.iais.api.config.GatewayStripeConfig;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremPhOpenPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionRequestInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDisciplineAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcLaboratoryDisciplinesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationSubDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.RenewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessHciDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppSpecifiedLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.AmendmentFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterInboxUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaAppConst;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.AppSelectSvcDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.dto.PmtReturnUrlDto;
import com.ecquaria.cloud.moh.iais.helper.AppDataHelper;
import com.ecquaria.cloud.moh.iais.helper.AppValidatorHelper;
import com.ecquaria.cloud.moh.iais.helper.ApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.RfcHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.rfcutil.EqRequestForChangeSubmitResultChange;
import com.ecquaria.cloud.moh.iais.service.AppCommService;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.client.AppGrpPaymentClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationFeClient;
import com.ecquaria.cloud.moh.iais.service.client.CessationClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigFeClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.util.DealSessionUtil;
import com.ecquaria.sz.commons.util.MsgUtil;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.ACKMESSAGE;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.ACKSTATUS;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.ACK_APP_SUBMISSIONS;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.APPSUBMISSIONDTO;
import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.APP_SUBMISSIONS;

/**
 * egator
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
    private HcsaConfigFeClient hcsaConfigFeClient;
    @Autowired
    private AppSubmissionService appSubmissionService;
    @Autowired
    private RequestForChangeService requestForChangeService;
    @Autowired
    private LicenceClient licenceClient;
    @Autowired
    private GenerateIdClient generateIdClient;
    @Autowired
    private CessationClient cessationClient;

    @Autowired
    private ApplicationFeClient applicationFeClient;
    @Autowired
    private EventBusHelper eventBusHelper;

    @Autowired
    private SystemParamConfig systemParamConfig;
    
    @Autowired
    AppGrpPaymentClient appGrpPaymentClient;

    @Autowired
    private FeEicGatewayClient feEicGatewayClient;

    @Autowired
    private AppCommService appCommService;

    /**
     * StartStep: Start
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc) throws CloneNotSupportedException {
        super.doStart(bpc);
    }

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
                if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())
                        || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())) {
                    RfcHelper.svcDocToPresmise(appSubmissionDto);
                    RfcHelper.svcDocToPresmise(appSubmissionDto.getOldAppSubmissionDto());
                }
                //set max file index into session
                ApplicationHelper.reSetMaxFileIndex(appSubmissionDto.getMaxFileIndex());
                // check premise select
                List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
                if (!IaisCommonUtils.isEmpty(appGrpPremisesDtoList)) {
                    Map<String, AppGrpPremisesDto> premisesDtoMap = ApplicationHelper.checkPremisesMap(appGrpPremisesDtoList, false,
                            false, request);
                    for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
                        if (HcsaAppConst.NEW_PREMISES.equals(appGrpPremisesDto.getPremisesSelect())) {
                            String premisesSelect = ApplicationHelper.getPremisesKey(appGrpPremisesDto);
                            AppGrpPremisesDto premises = premisesDtoMap.get(premisesSelect);
                            if (premises != null) {
                                appGrpPremisesDto.setPremisesSelect(premisesSelect);
                            }
                        }
                    }
                }

                List<String> stepColor = appSubmissionDto.getStepColor();
                if (stepColor != null) {
                    HashMap<String, String> coMap = new HashMap<>(5);
                    coMap.put(HcsaAppConst.SECTION_LICENSEE, "");
                    coMap.put(HcsaAppConst.SECTION_PREMISES, "");
                    coMap.put(HcsaAppConst.SECTION_DOCUMENT, "");
                    coMap.put(HcsaAppConst.SECTION_SVCINFO, "");
                    coMap.put(HcsaAppConst.SECTION_PREVIEW, "");
                    if (!stepColor.isEmpty()) {
                        for (String str : stepColor) {
                            if (HcsaAppConst.SECTION_LICENSEE.equals(str)) {
                                coMap.put(HcsaAppConst.SECTION_LICENSEE, str);
                            } else if (HcsaAppConst.SECTION_PREMISES.equals(str)) {
                                coMap.put(HcsaAppConst.SECTION_PREMISES, str);
                            } else if (HcsaAppConst.SECTION_DOCUMENT.equals(str)) {
                                coMap.put(HcsaAppConst.SECTION_DOCUMENT, str);
                            } else if (HcsaAppConst.SECTION_SVCINFO.equals(str)) {
                                coMap.put(HcsaAppConst.SECTION_SVCINFO, str);
                            } else if (HcsaAppConst.SECTION_PREVIEW.equals(str)) {
                                coMap.put(HcsaAppConst.SECTION_PREVIEW, str);
                            } else {
                                ParamUtil.setSessionAttr(request, "serviceConfig", str);
                            }
                        }
                    }
                    ParamUtil.setSessionAttr(request, HcsaAppConst.CO_MAP, coMap);
                }
                if (appSubmissionDto.getAppGrpPremisesDtoList() != null && appSubmissionDto.getAppGrpPremisesDtoList().size() > 0) {
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
            if (!StringUtil.isEmpty(entryType) && entryType.equals("assessment")) {
                ParamUtil.setSessionAttr(request, HcsaAppConst.ASSESSMENTCONFIG, "test");
            }
        }
        log.info(StringUtil.changeForLog("the do loadingDraft end ...."));
    }

    protected void requestForInformationLoading(HttpServletRequest request, String appNo) {
        log.info(StringUtil.changeForLog("the do requestForInformationLoading start ...."));
        String msgId = (String) ParamUtil.getSessionAttr(request, AppConsts.SESSION_INTER_INBOX_MESSAGE_ID);
        //msgId = "415199C2-4AAA-42BF-B068-9B019BF1ED1C";
        log.info(StringUtil.changeForLog("MsgId: " + msgId));
        if (!StringUtil.isEmpty(appNo) && !StringUtil.isEmpty(msgId)) {
            AppSubmissionDto appSubmissionDto = appCommService.getRfiAppSubmissionDtoByAppNo(appNo);
            if (appSubmissionDto != null) {
                appSubmissionDto.setAmountStr("N/A");
                if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(
                        appSubmissionDto.getAppType()) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(
                        appSubmissionDto.getAppType())) {
                    RfcHelper.svcDocToPresmise(appSubmissionDto);
                }
            }
            InterMessageDto interMessageDto = appSubmissionService.getInterMessageById(msgId);
            if (MessageConstants.MESSAGE_STATUS_RESPONSE.equals(interMessageDto.getStatus())) {
                appSubmissionDto = null;
            }

            if (appSubmissionDto != null) {
                loadingRfiGrpServiceConfig(appSubmissionDto, request);
                svcRelatedInfoRFI(appSubmissionDto, appNo);
                //set max file index into session
                ApplicationHelper.reSetMaxFileIndex(appSubmissionDto.getMaxFileIndex());

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
                DealSessionUtil.initCoMap(request);
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

    protected void loadingNewAppInfo(HttpServletRequest request) {
        log.info(StringUtil.changeForLog("the do loadingSpecifiedInfo start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request, APPSUBMISSIONDTO);
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = (List<AppSvcRelatedInfoDto>) ParamUtil.getSessionAttr(request,
                HcsaAppConst.APP_SVC_RELATED_INFO_LIST);
        AppSelectSvcDto appSelectSvcDto = (AppSelectSvcDto) ParamUtil.getSessionAttr(request, HcsaAppConst.APP_SELECT_SERVICE);
        if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos) && appSubmissionDto == null && appSelectSvcDto != null) {
            String entryType = ParamUtil.getString(request, "entryType");
            if (!StringUtil.isEmpty(entryType) && entryType.equals("assessment")) {
                ParamUtil.setSessionAttr(request, HcsaAppConst.ASSESSMENTCONFIG, "test");
            }
            appSubmissionDto = new AppSubmissionDto();
            appSubmissionDto.setLicenseeId(ApplicationHelper.getLicenseeId(request));
            appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
            appSubmissionDto.setAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtos);
            String premisesId = "";
            for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                String premId = appSvcRelatedInfoDto.getLicPremisesId();
                if (!StringUtil.isEmpty(premId) && !"-1".equals(premId)) {
                    premisesId = premId;
                    break;
                }
            }
            if (!StringUtil.isEmpty(premisesId)) {
                List<AppGrpPremisesDto> appGrpPremisesDtos = licCommService.getLicPremisesInfo(premisesId);
                appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtos);
            } else {
                List<AppGrpPremisesDto> appGrpPremisesDtos = IaisCommonUtils.genNewArrayList();
                AppGrpPremisesDto appGrpPremisesDto = new AppGrpPremisesDto();
                appGrpPremisesDtos.add(appGrpPremisesDto);
                appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtos);
            }
            ParamUtil.setSessionAttr(request, APPSUBMISSIONDTO, appSubmissionDto);
            ParamUtil.setSessionAttr(request, IaisEGPConstant.GLOBAL_MAX_INDEX_SESSION_ATTR, 0);
        }
        log.info(StringUtil.changeForLog("the do loadingSpecifiedInfo start ...."));
    }

    /**
     * StartStep: Prepare
     *
     * @param bpc
     * @throws
     */
    public void prepare(BaseProcessClass bpc) {
        super.prepare(bpc);
    }

    /**
     * Process: MohNewApplication
     * Step: PrepareSubLicensee
     * <p>
     * Prepare licensee detail
     *
     * @param bpc
     */
    public void prepareSubLicensee(BaseProcessClass bpc) {
        super.prepareSubLicensee(bpc);
    }

    /**
     * Process: MohNewApplication
     * Step: PrepareSubLicensee
     * <p>
     * Do licensee detail
     *
     * @param bpc
     */
    public void doSubLicensee(BaseProcessClass bpc) {
        super.doSubLicensee(bpc);
    }


    /**
     * StartStep: PreparePremises
     *
     * @param bpc
     * @throws
     */
    public void preparePremises(BaseProcessClass bpc) {
        super.preparePremises(bpc);
    }

    /**
     * StartStep: PrepareDocuments
     *
     * @param bpc
     * @throws
     * @throwsdo
     */
    public void prepareDocuments(BaseProcessClass bpc) {
        super.prepareDocuments(bpc);
    }

    /**
     * StartStep: PrepareForms
     *
     * @param bpc
     * @throws
     */
    public void prepareForms(BaseProcessClass bpc) {
        super.prepareForms(bpc);
    }

    /**
     * StartStep: PreparePreview
     *
     * @param bpc
     * @throws
     */
    public void preparePreview(BaseProcessClass bpc) {
        super.preparePreview(bpc);
    }


    /**
     * StartStep: PreparePayment
     *
     * @param bpc
     * @throws
     */
    public void preparePayment(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do preparePayment start ...."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        List<AppSubmissionDto> appSubmissionDtos = (List<AppSubmissionDto>) bpc.request.getSession().getAttribute(APP_SUBMISSIONS);
        HashMap<String, String> coMap = bpc.request.getSession().getAttribute(HcsaAppConst.CO_MAP) == null ?
                null : (HashMap<String, String>) bpc.request.getSession().getAttribute(HcsaAppConst.CO_MAP);

        String paymentMethod;

        String serviceConfig = (String) bpc.request.getSession().getAttribute("serviceConfig");

        ApplicationHelper.setStepColor(coMap, serviceConfig, appSubmissionDto);

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
                if (premTypes.size() == 1 && premTypes.contains(ApplicationConsts.PREMISES_TYPE_OFF_SITE)) {
                    boolean flag = true;
                    ParamUtil.setRequestAttr(bpc.request, "onlyOffsite", flag);
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
    public void doPremises(BaseProcessClass bpc) {
        super.doPremises(bpc);
    }

    /**
     * StartStep: DoDocument
     *
     * @param bpc
     * @throws
     */
    public void doDocument(BaseProcessClass bpc) throws IOException {
        super.doDocument(bpc);
    }

    /**
     * StartStep: doForms
     *
     * @param bpc
     * @throws
     */
    public void doForms(BaseProcessClass bpc) {
        super.doForms(bpc);
    }

    /**
     * StartStep: doPreview
     *
     * @param bpc
     * @throws
     */
    public void doPreview(BaseProcessClass bpc) {
        super.doPreview(bpc);
    }

    /**
     * StartStep: doPreview
     *
     * @param bpc
     * @throws
     */
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
                        ApplicationGroupDto appGrp = new ApplicationGroupDto();
                        appGrp.setId(appSubmissionDto1.getAppGrpId());
                        appGrp.setPmtRefNo(pmtRefNo);
                        appGrp.setGroupNo(appSubmissionDto1.getAppGrpNo());
                        appGrp.setAutoRfc(appSubmissionDto1.isAutoRfc());
                        Double amount = appSubmissionDto1.getAmount();
                        if (amount != null && !MiscUtil.doubleEquals(0.0, amount)) {
                            appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
                            appGrp.setPayMethod(appSubmissionDto.getPaymentMethod());
                        } else {
                            appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
                            appGrp.setPayMethod(appSubmissionDto.getPaymentMethod());
                        }
                        log.info(StringUtil.changeForLog("Payment response data is " + JsonUtil.parseToJson(appGrp)));
                        applicationFeClient.updatePaymentByAppGrp(appGrp);
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
                    ApplicationGroupDto appGrp = new ApplicationGroupDto();
                    appGrp.setId(appGrpId);
                    appGrp.setPmtRefNo(pmtRefNo);
                    appGrp.setPaymentDt(new Date());
                    appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
                    appGrp.setPayMethod(appSubmissionDto.getPaymentMethod());
                    serviceConfigService.updatePaymentStatus(appGrp);
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
                //appSubmissionService.updateDraftStatus(appSubmissionDto.getDraftNo(),AppConsts.COMMON_STATUS_ACTIVE);
                switch2 = "loading";
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "payment");
            }
        } else {
            PaymentRequestDto paymentRequestDtoSuss = null;
            List<PaymentRequestDto> paymentRequestDtos = appGrpPaymentClient.getPaymentRequestDtoByReqRefNoLike(
                    AppConsts.MOH_IAIS_SYSTEM_PAYMENT_CLIENT_KEY, appSubmissionDto.getAppGrpNo()).getEntity();
            if (paymentRequestDtos != null) {
                for (PaymentRequestDto paymentRequestDto : paymentRequestDtos
                ) {
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
                        ApplicationGroupDto appGrp = new ApplicationGroupDto();
                        appGrp.setId(appSubmissionDto1.getAppGrpId());
                        appGrp.setPmtRefNo(pmtRefNo);
                        appGrp.setGroupNo(appSubmissionDto1.getAppGrpNo());
                        appGrp.setAutoRfc(appSubmissionDto1.isAutoRfc());
                        Double amount = appSubmissionDto1.getAmount();
                        if (amount != null && !MiscUtil.doubleEquals(0.0, amount)) {
                            appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
                            appGrp.setPayMethod(appSubmissionDto.getPaymentMethod());
                        } else {
                            appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
                            appGrp.setPayMethod(appSubmissionDto.getPaymentMethod());
                        }
                        log.info(StringUtil.changeForLog("Payment response data is " + JsonUtil.parseToJson(appGrp)));
                        applicationFeClient.updatePaymentByAppGrp(appGrp);
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
                    ApplicationGroupDto appGrp = new ApplicationGroupDto();
                    appGrp.setId(appGrpId);
                    appGrp.setPmtRefNo(pmtRefNo);
                    appGrp.setPaymentDt(new Date());
                    appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
                    appGrp.setPayMethod(appSubmissionDto.getPaymentMethod());
                    serviceConfigService.updatePaymentStatus(appGrp);
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
                //appSubmissionService.updateDraftStatus(appSubmissionDto.getDraftNo(),AppConsts.COMMON_STATUS_ACTIVE);
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
     * StartStep: preInvoke
     *
     * @param bpc
     * @throws
     */
    public void preInvoke(BaseProcessClass bpc) throws IOException {
        super.preInvoke(bpc);
    }

    /**
     * StartStep: doSaveDraft
     *
     * @param bpc
     * @throws
     */
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
        HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute(HcsaAppConst.CO_MAP);

        String serviceConfig = (String) bpc.request.getSession().getAttribute("serviceConfig");

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

        ApplicationHelper.setStepColor(coMap, serviceConfig, appSubmissionDto);
        Integer maxFileIndex = (Integer) ParamUtil.getSessionAttr(bpc.request, HcsaFileAjaxController.GLOBAL_MAX_INDEX_SESSION_ATTR);
        if (maxFileIndex == null) {
            maxFileIndex = 0;
        }
        appSubmissionDto.setMaxFileIndex(maxFileIndex);
        //set psn dropdown
        setPsnDroTo(appSubmissionDto, bpc);
        appSubmissionDto = appSubmissionService.doSaveDraft(appSubmissionDto);
        if ("exitSaveDraft".equals(crud_action_additional)) {
            jumpYeMian(bpc.request, bpc.response);
            return;
        }
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        bpc.request.setAttribute("saveDraftSuccess", "success");
        log.info(StringUtil.changeForLog("the do doSaveDraft end ...."));
    }

    public void jumpYeMian(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder url = new StringBuilder(10);
        url.append("https://").append(request.getServerName()).append("/main-web/eservice/INTERNET/MohInternetInbox");
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), request);
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

    public void inboxToPreview(BaseProcessClass bpc) throws Exception {

        // View and Print
        ParamUtil.setSessionAttr(bpc.request, "viewPrint", "Y");
        String appNo = ParamUtil.getMaskedString(bpc.request, "appNo");
        if (!StringUtil.isEmpty(appNo)) {
            ApplicationDto applicationDto = appCommService.getApplicationDtoByAppNo(appNo);
            if (applicationDto != null) {
                if (ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION.equals(applicationDto.getStatus())) {
                    MsgTemplateDto autoEntity = generateIdClient.getMsgTemplate(
                            MsgTemplateConstants.MSG_TEMPLATE_APP_RFI_MSG).getEntity();
                    Map<String, Object> subjectMap = IaisCommonUtils.genNewHashMap();
                    subjectMap.put("ApplicationType", MasterCodeUtil.getCodeDesc(applicationDto.getApplicationType()));
                    subjectMap.put("ApplicationNumber", StringUtil.viewHtml(appNo));
                    String msgSubject = MsgUtil.getTemplateMessageByContent(autoEntity.getTemplateName(), subjectMap);
                    InterMessageDto interMessageBySubjectLike = appSubmissionService.getInterMessageBySubjectLike(msgSubject.trim(),
                            MessageConstants.MESSAGE_STATUS_RESPONSE);
                    if (interMessageBySubjectLike.getId() != null) {
                        List<AppEditSelectDto> entity = applicationFeClient.getAppEditSelectDtos(applicationDto.getId(),
                                ApplicationConsts.APPLICATION_EDIT_TYPE_RFI).getEntity();
                        String url = "";
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
                /**
                 * cessation
                 */

                if (ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(applicationDto.getApplicationType())) {
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
                    AppGrpPremisesEntityDto appGrpPremisesEntityDto = appCommService.getPremisesByAppNo(applicationDto.getApplicationNo());
                    List<AppDeclarationMessageDto> appDeclarationMessageDtoList = applicationFeClient.getAppDeclarationMessageDto(
                            appGrpPremisesEntityDto.getAppGrpId()).getEntity();
                    List<AppDeclarationDocDto> appDeclarationDocDtoList = applicationFeClient.getAppDeclarationDocDto(
                            appGrpPremisesEntityDto.getAppGrpId()).getEntity();
                    if (appDeclarationMessageDtoList != null && appDeclarationMessageDtoList.size() > 0) {
                        appCessLicDto.setAppDeclarationMessageDto(appDeclarationMessageDtoList.get(0));
                        appCessLicDto.setAppDeclarationDocDtos(appDeclarationDocDtoList);
                    }
                    //appSubmissionService.initDeclarationFiles(appDeclarationDocDtoList,ApplicationConsts.APPLICATION_TYPE_CESSATION,bpc.request);
                    String blkNo = appGrpPremisesEntityDto.getBlkNo();
                    String premisesId = appGrpPremisesEntityDto.getId();
                    String streetName = appGrpPremisesEntityDto.getStreetName();
                    String buildingName = appGrpPremisesEntityDto.getBuildingName();
                    String floorNo = appGrpPremisesEntityDto.getFloorNo();
                    String unitNo = appGrpPremisesEntityDto.getUnitNo();
                    String postalCode = appGrpPremisesEntityDto.getPostalCode();
                    String hciAddress = MiscUtil.getAddressForApp(blkNo, streetName, buildingName, floorNo, unitNo, postalCode,
                            appGrpPremisesEntityDto.getAppPremisesOperationalUnitDtos());
                    AppCessHciDto appCessHciDto = new AppCessHciDto();
                    String hciName = appGrpPremisesEntityDto.getHciName();
                    String hciCode = appGrpPremisesEntityDto.getHciCode();
                    appCessHciDto.setHciCode(hciCode);
                    appCessHciDto.setHciName(hciName);
                    appCessHciDto.setPremiseId(premisesId);
                    appCessHciDto.setHciAddress(hciAddress);
                    Date effectiveDate = appCessMiscDto.getEffectiveDate();
                    String reason = appCessMiscDto.getReason();
                    String otherReason = appCessMiscDto.getOtherReason();
                    String patTransType = appCessMiscDto.getPatTransType();
                    String patTransTo = appCessMiscDto.getPatTransTo();
                    Boolean patNeedTrans = appCessMiscDto.getPatNeedTrans();
                    String mobileNo = appCessMiscDto.getMobileNo();
                    String emailAddress = appCessMiscDto.getEmailAddress();
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
                        List<AppSpecifiedLicDto> appSpecifiedLicDtos = IaisCommonUtils.genNewArrayList();
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
                                appSpecifiedLicDtos.add(appSpecifiedLicDto);
                            }
                        }
                        ParamUtil.setRequestAttr(bpc.request, "specLicInfo", appSpecifiedLicDtos);
                        ParamUtil.setSessionAttr(bpc.request, "specLicInfoPrint", (Serializable) appSpecifiedLicDtos);
                    }
                    List<SelectOption> reasonOption = ApplicationHelper.getReasonOption();
                    List<SelectOption> patientsOption = ApplicationHelper.getPatientsOption();
                    ParamUtil.setRequestAttr(bpc.request, "reasonOption", reasonOption);
                    ParamUtil.setRequestAttr(bpc.request, "patientsOption", patientsOption);
                    ParamUtil.setRequestAttr(bpc.request, "applicationDto", applicationDto);
                    List<AppCessLicDto> appCessLicDtos = IaisCommonUtils.genNewArrayList();
                    appCessLicDtos.add(appCessLicDto);
                    ParamUtil.setRequestAttr(bpc.request, "confirmDtos", appCessLicDtos);
                    ParamUtil.setRequestAttr(bpc.request, "printFlag", "Y");

                    ParamUtil.setSessionAttr(bpc.request, "appCessationDtos", (Serializable) appCessLicDtos);
                    ParamUtil.setSessionAttr(bpc.request, "reasonOptionPrint", (Serializable) reasonOption);
                    ParamUtil.setSessionAttr(bpc.request, "patientsOptionPrint", (Serializable) patientsOption);
                    ParamUtil.setRequestAttr(bpc.request, "applicationDtoPrint", applicationDto);
                    ParamUtil.setSessionAttr(bpc.request, "confirmPrintDtos", (Serializable) appCessLicDtos);

                    ParamUtil.setSessionAttr(bpc.request, "declaration_page_is", "cessation");
                    return;
                }
                AppSubmissionDto appSubmissionDto = appSubmissionService.getAppSubmissionDto(appNo);
                if (appSubmissionDto != null) {
                    /**
                     * preview
                     */
                    if (!IaisCommonUtils.isEmpty(appSubmissionDto.getAppSvcRelatedInfoDtoList())) {
                        svcRelatedInfoView(appSubmissionDto, bpc.request, applicationDto.getServiceId(), appNo);
                        if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())
                                || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())
                                || ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION.equals(appSubmissionDto.getAppType())) {
                            RfcHelper.svcDocToPresmise(appSubmissionDto);
                        }
                    }
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
                            bpc.request.setAttribute("renewDto", renewDto);
                            AppDataHelper.initDeclarationFiles(appSubmissionDto.getAppDeclarationDocDtos(),
                                    appSubmissionDto.getAppType(), bpc.request);
                            bpc.request.getSession().setAttribute("isSingle", "Y");
                        } else {
                            bpc.request.getSession().setAttribute("isSingle", "N");
                        }
                    }
                    if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())
                            || ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())) {
                        AppDataHelper.initDeclarationFiles(appSubmissionDto.getAppDeclarationDocDtos(),
                                appSubmissionDto.getAppType(), bpc.request);
                    }
                    premiseView(appSubmissionDto, applicationDto, bpc.request);
                }
                ParamUtil.setRequestAttr(bpc.request, "cessationForm", "Application Details");
                ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
            }
        }
    }

    private void premiseView(AppSubmissionDto appSubmissionDto, ApplicationDto applicationDto, HttpServletRequest request)
            throws CloneNotSupportedException {
        if (appSubmissionDto == null || applicationDto == null) {
            return;
        }
        if (!IaisCommonUtils.isEmpty(appSubmissionDto.getAppGrpPremisesDtoList())) {
            if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(applicationDto.getApplicationType())
                    || ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationDto.getApplicationType())
                    || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(applicationDto.getApplicationType())
                    || ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION.equals(applicationDto.getApplicationType())) {
                log.info(StringUtil.changeForLog("InboxToView AppNo -->" + applicationDto.getApplicationNo()));
                List<AppGrpPremisesDto> newPremisesDtos = IaisCommonUtils.genNewArrayList();
                filtrationAppGrpPremisesDtos(applicationDto.getApplicationNo(), appSubmissionDto, newPremisesDtos);
                appSubmissionDto.setAppGrpPremisesDtoList(newPremisesDtos);
                List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = filterPrimaryDocs(appSubmissionDto.getAppGrpPrimaryDocDtos(),
                        newPremisesDtos);
                String svcId = applicationDto.getServiceId();
                if (!StringUtil.isEmpty(svcId) && !StringUtil.isEmpty(applicationDto.getApplicationNo())) {
                    List<AppSvcRelatedInfoDto> newSvcRelatedInfoDtos = IaisCommonUtils.genNewArrayList();
                    //set doc name
                    List<HcsaSvcDocConfigDto> primaryDocConfig = null;
                    if (appGrpPrimaryDocDtos != null && appGrpPrimaryDocDtos.size() > 0) {
                        primaryDocConfig = serviceConfigService.getPrimaryDocConfigById(appGrpPrimaryDocDtos.get(0).getSvcComDocId());
                    }
                    Optional<AppSvcRelatedInfoDto> optional = appSubmissionDto.getAppSvcRelatedInfoDtoList().stream()
                            .filter(dto -> applicationDto.getApplicationNo().equals(dto.getAppNo()))
                            .findAny();
                    if (!optional.isPresent()) {
                        optional = appSubmissionDto.getAppSvcRelatedInfoDtoList().stream()
                                .filter(dto -> svcId.equals(dto.getServiceId()))
                                .findAny();
                    }
                    if (optional.isPresent()) {
                        AppSvcRelatedInfoDto appSvcRelatedInfoDto = optional.get();
                        HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(svcId);
                        appSvcRelatedInfoDto.setServiceCode(hcsaServiceDto.getSvcCode());
                        appSvcRelatedInfoDto.setServiceName(hcsaServiceDto.getSvcName());
                        appSvcRelatedInfoDto.setServiceType(hcsaServiceDto.getSvcType());
                        List<AppSvcDocDto> appSvcDocDtos = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
                        List<HcsaSvcDocConfigDto> svcDocConfig = serviceConfigService.getAllHcsaSvcDocs(svcId);
                        ApplicationHelper.setDocInfo(appGrpPrimaryDocDtos, appSvcDocDtos, primaryDocConfig, svcDocConfig);
                        ParamUtil.setSessionAttr(request, HcsaAppConst.SVC_DOC_CONFIG, (Serializable) svcDocConfig);
                        //set dupForPsn attr
                        ApplicationHelper.setDupForPersonAttr(request, appSvcRelatedInfoDto);
                        //svc doc add align for dup for prem
                        ApplicationHelper.addPremAlignForSvcDoc(svcDocConfig, appSvcDocDtos, newPremisesDtos);
                        appSvcRelatedInfoDto.setAppSvcDocDtoLit(appSvcDocDtos);
                        //set svc doc title
                        Map<String, List<AppSvcDocDto>> reloadSvcDocMap = ApplicationHelper.genSvcDocReloadMap(svcDocConfig,
                                newPremisesDtos, appSvcRelatedInfoDto);
                        appSvcRelatedInfoDto.setMultipleSvcDoc(reloadSvcDocMap);
                        appSvcRelatedInfoDto.setAppSvcDocDtoLit(appSvcDocDtos);
                        // sort po and dpo
                        appSvcRelatedInfoDto.setAppSvcPrincipalOfficersDtoList(
                                ApplicationHelper.sortKeyPersonnel(appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList()));
                        newSvcRelatedInfoDtos.add(appSvcRelatedInfoDto);
                    }

                    ApplicationHelper.addPremAlignForPrimaryDoc(primaryDocConfig, appGrpPrimaryDocDtos, newPremisesDtos);
                    //set primary doc title
                    Map<String, List<AppGrpPrimaryDocDto>> reloadPrimaryDocMap = ApplicationHelper.genPrimaryDocReloadMap(
                            primaryDocConfig, newPremisesDtos, appGrpPrimaryDocDtos);
                    appSubmissionDto.setMultipleGrpPrimaryDoc(reloadPrimaryDocMap);
                    ParamUtil.setSessionAttr(request, HcsaAppConst.PRIMARY_DOC_CONFIG, (Serializable) primaryDocConfig);
                    List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos = serviceConfigService.loadLaboratoryDisciplines(
                            svcId);
                    ApplicationHelper.setLaboratoryDisciplinesInfo(appSubmissionDto, hcsaSvcSubtypeOrSubsumedDtos);
                    appSubmissionDto.setAppSvcRelatedInfoDtoList(newSvcRelatedInfoDtos);
                }
                //set DisciplineAllocationMap
                Map<String, List<AppSvcDisciplineAllocationDto>> reloadDisciplineAllocationMap = ApplicationHelper.getDisciplineAllocationDtoList(
                        appSubmissionDto, svcId);
                ParamUtil.setRequestAttr(request, "reloadDisciplineAllocationMap", reloadDisciplineAllocationMap);
            }
            for (AppGrpPremisesDto appGrpPremisesDto : appSubmissionDto.getAppGrpPremisesDtoList()) {
                ApplicationHelper.setWrkTime(appGrpPremisesDto);
            }
        }
    }

    private void svcRelatedInfoView(AppSubmissionDto appSubmissionDto, HttpServletRequest request, String serviceId, String appNo) {
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfoDtoByServiceId(appSubmissionDto.getAppSvcRelatedInfoDtoList(),
                serviceId, appNo);
        List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemesByServiceId = serviceConfigService.getHcsaServiceStepSchemesByServiceId(
                appSvcRelatedInfoDto.getServiceId());
        appSvcRelatedInfoDto.setHcsaServiceStepSchemeDtos(hcsaServiceStepSchemesByServiceId);
        String svcId = appSvcRelatedInfoDto.getServiceId();
        HcsaServiceDto hcsaServiceDto = serviceConfigService.getHcsaServiceDtoById(svcId);
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        if (appGrpPremisesDtoList != null) {
            for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
                List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodList = appGrpPremisesDto.getAppPremPhOpenPeriodList();
                if (appPremPhOpenPeriodList != null) {
                    for (AppPremPhOpenPeriodDto appPremPhOpenPeriodDto : appPremPhOpenPeriodList) {
                        appPremPhOpenPeriodDto.setDayName(MasterCodeUtil.getCodeDesc(appPremPhOpenPeriodDto.getPhDate()));
                    }
                }
                String premises = appGrpPremisesDto.getPremisesIndexNo();
                List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList = appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
                List<AppSvcDisciplineAllocationDto> appSvcDisciplineAllocationDtoList = appSvcRelatedInfoDto.getAppSvcDisciplineAllocationDtoList();
                if (IaisCommonUtils.isNotEmpty(appSvcLaboratoryDisciplinesDtoList)) {
                    List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoListExclude = getAppSvcLaboratoryDisciplinesDtosByPremises(
                            appSvcLaboratoryDisciplinesDtoList, premises);
                    if (IaisCommonUtils.isNotEmpty(appSvcLaboratoryDisciplinesDtoListExclude)) {
                        for (AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto : appSvcLaboratoryDisciplinesDtoListExclude) {
                            List<AppSvcChckListDto> appSvcChckListDtoList = appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList();
                            if (appSvcChckListDtoList != null) {
                                List<AppSvcChckListDto> entity = hcsaConfigFeClient.getAppSvcChckListDto(
                                        appSvcChckListDtoList).getEntity();
                                if (appSvcDisciplineAllocationDtoList != null) {
                                    for (AppSvcChckListDto appSvcChckListDto : entity) {
                                        for (AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto : appSvcDisciplineAllocationDtoList) {
                                            String chkLstConfId = appSvcDisciplineAllocationDto.getChkLstConfId();
                                            if (appSvcChckListDto.getChkLstConfId().equals(chkLstConfId)) {
                                                appSvcDisciplineAllocationDto.setChkLstName(appSvcChckListDto.getChkName());
                                                break;
                                            }
                                        }
                                    }
                                }
                                appSvcLaboratoryDisciplinesDto.setAppSvcChckListDtoList(entity);
                            }
                        }
                        appSvcRelatedInfoDto.setAppSvcLaboratoryDisciplinesDtoList(appSvcLaboratoryDisciplinesDtoListExclude);
                    }
                }
            }
        }
        ParamUtil.setRequestAttr(request, HcsaAppConst.HCSASERVICEDTO, hcsaServiceDto);
        ParamUtil.setSessionAttr(request, "currentPreviewSvcInfo", appSvcRelatedInfoDto);
    }

    private List<AppSvcLaboratoryDisciplinesDto> getAppSvcLaboratoryDisciplinesDtosByPremises(
            List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList, String premises) {
        List<AppSvcLaboratoryDisciplinesDto> appSvcDisciplineAllocationDtos = IaisCommonUtils.genNewArrayList();
        if (IaisCommonUtils.isNotEmpty(appSvcLaboratoryDisciplinesDtoList)) {
            for (AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto : appSvcLaboratoryDisciplinesDtoList) {
                if (appSvcLaboratoryDisciplinesDto.getPremiseVal().equals(premises)) {
                    appSvcDisciplineAllocationDtos.add(appSvcLaboratoryDisciplinesDto);
                }
            }
        }
        return appSvcDisciplineAllocationDtos;
    }


    /**
     * StartStep: doReDquestInformationSubmit
     * prepare
     *
     * @param bpc
     * @throws
     */
    public void doRequestInformationSubmit(BaseProcessClass bpc) throws Exception {
        super.doRequestInformationSubmit(bpc);
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
                    if (ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING.equals(
                            appPremisesRoutingHistoryDto.getAppStatus())) {
                        status = ApplicationConsts.PENDING_ASO_REPLY;
                    } else if (ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING.equals(
                            appPremisesRoutingHistoryDto.getAppStatus())) {
                        status = ApplicationConsts.PENDING_PSO_REPLY;
                    } else if (ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS.equals(
                            appPremisesRoutingHistoryDto.getAppStatus())) {
                        status = ApplicationConsts.PENDING_INP_REPLY;
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
        log.info(StringUtil.changeForLog("the do doRenewSubmit start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        List<ApplicationDto> applicationDtos = requestForChangeService.getAppByLicIdAndExcludeNew(appSubmissionDto.getLicenceId());
        String rfcErrOne = MessageUtil.getMessageDesc("RFC_ERR001");
        if (!applicationDtos.isEmpty()) {
            ParamUtil.setRequestAttr(bpc.request, "isrfiSuccess", "Y");
            ParamUtil.setRequestAttr(bpc.request, ACKMESSAGE, "error");
            ParamUtil.setRequestAttr(bpc.request, "content", rfcErrOne);
            return;
        }
        AppSubmissionDto oldAppSubmissionDto = ApplicationHelper.getOldAppSubmissionDto(bpc.request);
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        List<AppGrpPremisesDto> oldAppSubmissionDtoAppGrpPremisesDtoList = oldAppSubmissionDto.getAppGrpPremisesDtoList();
        boolean grpPremiseChange = false;
        if (appGrpPremisesDtoList.equals(oldAppSubmissionDtoAppGrpPremisesDtoList)) {
            grpPremiseChange = true;
        }
        if (!grpPremiseChange) {
            for (int i = 0; i < appGrpPremisesDtoList.size(); i++) {
                // all latest licence under the current hci code and licensee, except the current original licence.
                List<LicenceDto> attribute = (List<LicenceDto>) bpc.request.getSession().getAttribute("selectLicence" + i);
                if (attribute != null && !attribute.isEmpty()) {
                    for (LicenceDto licenceDto : attribute) {
                        List<ApplicationDto> appByLicIdAndExcludeNew =
                                requestForChangeService.getAppByLicIdAndExcludeNew(licenceDto.getId());
                        if (!IaisCommonUtils.isEmpty(appByLicIdAndExcludeNew)) {
                            ParamUtil.setRequestAttr(bpc.request, "isrfiSuccess", "Y");
                            ParamUtil.setRequestAttr(bpc.request, ACKMESSAGE, "error");
                            ParamUtil.setRequestAttr(bpc.request, "content", rfcErrOne);
                            return;
                        }
                    }
                }
            }
        }
        boolean isAutoRfc = RfcHelper.compareNotChangePersonnel(appSubmissionDto, oldAppSubmissionDto);
        if (isAutoRfc) {
            boolean changeHciName = RfcHelper.isChangeHciName(appSubmissionDto.getAppGrpPremisesDtoList(),
                    oldAppSubmissionDto.getAppGrpPremisesDtoList());
            boolean changeInLocation = !RfcHelper.compareLocation(
                    appSubmissionDto.getAppGrpPremisesDtoList(),
                    oldAppSubmissionDto.getAppGrpPremisesDtoList());
            boolean eqAddFloorNo = RfcHelper.isChangeFloorUnit(appSubmissionDto, oldAppSubmissionDto);
            log.info(StringUtil.changeForLog("changeHciName: " + changeHciName + " - changeInLocation: " + changeInLocation + " - " +
                    "eqAddFloorNo: " + eqAddFloorNo));
            isAutoRfc = !changeInLocation && !eqAddFloorNo && !changeHciName;
        }
        log.info(StringUtil.changeForLog("isAutoRfc: " + isAutoRfc));
        appSubmissionDto.setAutoRfc(isAutoRfc);
        Map<String, String> map = AppValidatorHelper.doPreviewAndSumbit(bpc);
        if (!map.isEmpty()) {
            ParamUtil.setRequestAttr(bpc.request, "isrfiSuccess", "N");
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "preview");
            return;
        }

        String draftNo = appSubmissionDto.getDraftNo();
        if (StringUtil.isEmpty(draftNo)) {
            draftNo = appSubmissionService.getDraftNo(appSubmissionDto.getAppType());
            appSubmissionDto.setDraftNo(draftNo);
        }
        //get appGroupNo
        String appGroupNo = appSubmissionService.getGroupNo(appSubmissionDto.getAppType());
        log.info(StringUtil.changeForLog("the appGroupNo is -->:" + appGroupNo));
        appSubmissionDto.setAppGrpNo(appGroupNo);
        //get Amount
        FeeDto feeDto = appSubmissionService.getGroupAmount(appSubmissionDto, ApplicationHelper.isCharity(bpc.request));
        appSubmissionDto.setFeeInfoDtos(feeDto.getFeeInfoDtos());
        Double amount = feeDto.getTotal();
        log.info(StringUtil.changeForLog("the amount is -->:" + amount));
        appSubmissionDto.setAmount(amount);
        //judge is the preInspection
        PreOrPostInspectionResultDto preOrPostInspectionResultDto = appSubmissionService.judgeIsPreInspection(appSubmissionDto);
        if (preOrPostInspectionResultDto == null) {
            appSubmissionDto.setPreInspection(true);
            appSubmissionDto.setRequirement(true);
        } else {
            appSubmissionDto.setPreInspection(preOrPostInspectionResultDto.isPreInspection());
            appSubmissionDto.setRequirement(preOrPostInspectionResultDto.isRequirement());
        }

        //set Risk Score
        appSubmissionService.setRiskToDto(appSubmissionDto);

        appSubmissionDto = appSubmissionService.submitRenew(appSubmissionDto);
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);

        //back to renewal licence view page
        ParamUtil.setRequestAttr(bpc.request, "isrfiSuccess", "N");
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "jump");
        ParamUtil.setRequestAttr(bpc.request, "jumpPmt", "Y");

        log.info(StringUtil.changeForLog("the do doRenewSubmit end ...."));
    }

    /**
     * StartStep: doRequestForChangeSubmit
     *
     * @param bpc
     */
    public void doRequestForChangeSubmit(BaseProcessClass bpc) throws Exception {
        super.doRequestForChangeSubmit(bpc);
    }

    @Override
    protected List<AppSubmissionDto> submitRequestForChange(List<AppSubmissionDto> appSubmissionDtoList, boolean isAutoRfc) {
        AppSubmissionListDto appSubmissionListDto = new AppSubmissionListDto();
        appSubmissionListDto.setEventRefNo(String.valueOf(System.currentTimeMillis()));
        appSubmissionListDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        List<AppSubmissionDto> newAppSubmissionDtos =
                requestForChangeService.saveAppsForRequestForGoupAndAppChangeByList(appSubmissionDtoList);
        // save other data via event bus
        appSubmissionListDto.setAppSubmissionDtos(newAppSubmissionDtos);
        String projectName = isAutoRfc ? "RFC Auto Approve Submit" : "RFC Non-Auto Approve Submit";
        eventBusHelper.submitAsyncRequest(appSubmissionListDto, appCommService.getSeqId(), EventBusConsts.SERVICE_NAME_APPSUBMIT,
                EventBusConsts.OPERATION_REQUEST_INFORMATION_SUBMIT, appSubmissionListDto.getEventRefNo(),
                projectName, newAppSubmissionDtos.get(0).getAppGrpId());
        return newAppSubmissionDtos;
    }

    @Override
    protected void handleDraft(String draftNo, String licenseeId, AppSubmissionDto appSubmissionDto,
            List<AppSubmissionDto> appSubmissionDtoList) {
        appSubmissionService.doSaveDraft(appSubmissionDto);
        List<String> licenceIds = appSubmissionDtoList.parallelStream()
                .map(AppSubmissionDto::getLicenceId)
                .collect(Collectors.toList());
        appSubmissionService.updateDrafts(licenseeId, licenceIds, draftNo);
    }


    public void reSubmit(BaseProcessClass bpc) throws Exception {
        super.reSubmit(bpc);
    }

    public void doPayValidate(BaseProcessClass bpc) throws Exception {
        super.doPayValidate(bpc);
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
        Object requestInformationConfig = ParamUtil.getSessionAttr(bpc.request, HcsaAppConst.REQUESTINFORMATIONCONFIG);
        if ((ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())
                || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(
                appSubmissionDto.getAppType())) && requestInformationConfig == null) {
            String crud_action_additional = ParamUtil.getString(bpc.request, "crud_action_additional");
            if ("rfcSaveDraft".equals(crud_action_additional)) {
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
        List<String> ids = new ArrayList<>();
        //68099
        List<AppSubmissionDto> ackPageAppSubmissionDto = (List<AppSubmissionDto>) ParamUtil.getSessionAttr(bpc.request,
                ACK_APP_SUBMISSIONS);
        if (!IaisCommonUtils.isEmpty(ackPageAppSubmissionDto)) {
            for (AppSubmissionDto appSubmissionDto1 : ackPageAppSubmissionDto) {
                if (!MiscUtil.doubleEquals(appSubmissionDto1.getAmount(), 0.0)) {
                    appSubmissionDto1.setPaymentMethod(payMethod);
                } else {
                    log.info(StringUtil.changeForLog(
                            "--- " + appSubmissionDto1.getAppGrpNo() + " : " + appSubmissionDto1.getAppGrpId() + " ---"));
                    ids.add(appSubmissionDto1.getAppGrpId());
                    ApplicationGroupDto appGrp = new ApplicationGroupDto();
                    appGrp.setId(appSubmissionDto1.getAppGrpId());
                    appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
                    appGrp.setPayMethod(payMethod);
                    serviceConfigService.updatePaymentStatus(appGrp);
                }
            }
            ParamUtil.setSessionAttr(bpc.request, ACK_APP_SUBMISSIONS, (Serializable) ackPageAppSubmissionDto);
        }
        Double totalAmount = appSubmissionDto.getAmount();
        if (MiscUtil.doubleEquals(totalAmount, 0.0)) {
            if (StringUtil.isNotEmpty(appGrpId) && !ids.contains(appGrpId)) {
                ApplicationGroupDto appGrp = new ApplicationGroupDto();
                appGrp.setId(appGrpId);
                appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
                appGrp.setPayMethod(payMethod);
                serviceConfigService.updatePaymentStatus(appGrp);
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
            //send email
            try {
                //inspectionDateSendNewApplicationPaymentOnlineEmail(appSubmissionDto, bpc);
            } catch (Exception e) {
                log.error(StringUtil.changeForLog("send email error ...." + e.getMessage()), e);
            }
            String amount = String.valueOf(appSubmissionDto.getAmount());
            Map<String, String> fieldMap = new HashMap<String, String>();
            fieldMap.put(GatewayConstants.AMOUNT_KEY, amount);
            fieldMap.put(GatewayConstants.PYMT_DESCRIPTION_KEY, payMethod);
            fieldMap.put(GatewayConstants.SVCREF_NO, appSubmissionDto.getAppGrpNo() + "_" + System.currentTimeMillis());
            PmtReturnUrlDto pmtReturnUrlDto = new PmtReturnUrlDto();
            pmtReturnUrlDto.setCreditRetUrl(GatewayStripeConfig.return_url);
            pmtReturnUrlDto.setPayNowRetUrl(GatewayConfig.return_url);
            pmtReturnUrlDto.setNetsRetUrl(GatewayConfig.return_url);
            pmtReturnUrlDto.setOtherRetUrl(GatewayConfig.return_url);
            try {
                appSubmissionService.updateDraftStatus(appSubmissionDto.getDraftNo(), ApplicationConsts.DRAFT_STATUS_PENDING_PAYMENT);
                String url = NewApplicationHelper.genBankUrl(bpc.request, payMethod, fieldMap, pmtReturnUrlDto);
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
            ApplicationGroupDto appGrp = new ApplicationGroupDto();
            appGrp.setId(appGrpId);
            appGrp.setPmtStatus(serviceConfigService.giroPaymentXmlUpdateByGrpNo(appSubmissionDto).getPmtStatus());
            String giroTranNo = appSubmissionDto.getGiroTranNo();
            appGrp.setPmtRefNo(giroTranNo);
            appGrp.setPayMethod(payMethod);
            serviceConfigService.updateAppGrpPmtStatus(appGrp, appSubmissionDto.getGiroAcctNum());
            serviceConfigService.updatePaymentStatus(appGrp);
            /*List<ApplicationDto> entity = applicationFeClient.getApplicationsByGroupNo(appGrp.getGroupNo()).getEntity();
            if (entity!=null && !entity.isEmpty()) {
                for(ApplicationDto applicationDto : entity){
                    applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING);
                }
                applicationFeClient.saveApplicationDtos(entity);
            }*/
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
     * StartStep: PrePareErrorAckPage
     *
     * @param bpc
     * @throws
     */
    public void prepareErrorAck(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do prepareErrorAck start ...."));

        log.info(StringUtil.changeForLog("the do prepareErrorAck end ...."));
    }

    public void doErrorAck(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do doErrorAck start ...."));


        log.info(StringUtil.changeForLog("the do doErrorAck end ...."));
    }

    /**
     * StartStep: PrepareAckPage
     *
     * @param bpc
     * @throws
     */
    public void prepareAckPage(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do prepareAckPage start ...."));
        String txnRefNo = (String) bpc.request.getSession().getAttribute("txnDt");
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        List<AppSubmissionDto> ackPageAppSubmissionDto = (List<AppSubmissionDto>) ParamUtil.getSessionAttr(bpc.request,
                ACK_APP_SUBMISSIONS);
        String draftNo = "";
        if (appSubmissionDto != null) {
            draftNo = appSubmissionDto.getDraftNo();
            if (ackPageAppSubmissionDto == null) {
                List<AppSubmissionDto> ackPageAppSubmission = new ArrayList<>(1);
                ackPageAppSubmission.add(appSubmissionDto);
                bpc.request.getSession().setAttribute(ACK_APP_SUBMISSIONS, ackPageAppSubmission);
            }
        }
        if (draftNo != null) {
            applicationFeClient.deleteDraftByNo(draftNo);
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
        if (isRfi && "error".equals(ackStatus)) {
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
    public void prepareJump(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do prepareJump start ...."));
        String action = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
        if (StringUtil.isEmpty(action)) {
            action = ParamUtil.getRequestString(bpc.request, "nextStep");
        }
        if (RfcConst.RFC_BTN_OPTION_UNDO_ALL_CHANGES.equals(action)) {
            AppSubmissionDto oldAppSubmissionDto = NewApplicationHelper.getOldAppSubmissionDto(bpc.request);
            AppSubmissionDto oldDto = (AppSubmissionDto) CopyUtil.copyMutableObject(oldAppSubmissionDto);
            AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
            AppEditSelectDto appEditSelectDto = appSubmissionDto.getAppEditSelectDto();
            if (appEditSelectDto.isLicenseeEdit()) {
                appSubmissionDto.setSubLicenseeDto(oldDto.getSubLicenseeDto());
            }
            if (appEditSelectDto.isPremisesEdit()) {
                appSubmissionDto.setAppGrpPremisesDtoList(oldDto.getAppGrpPremisesDtoList());
            }
            if (appEditSelectDto.isDocEdit()) {
                appSubmissionDto.setAppGrpPrimaryDocDtos(oldDto.getAppGrpPrimaryDocDtos());
            }
            if (appEditSelectDto.isServiceEdit()) {
                appSubmissionDto.setAppSvcRelatedInfoDtoList(oldDto.getAppSvcRelatedInfoDtoList());
            }
            ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.APPSUBMISSIONDTO, appSubmissionDto);
        }

        log.info(StringUtil.changeForLog("the do prepareJump end ...."));
    }

    private Map<String, String> doComChange(AppSubmissionDto appSubmissionDto, AppSubmissionDto oldAppSubmissionDto) {
        Map<String, String> result = IaisCommonUtils.genNewHashMap();
        AppEditSelectDto appEditSelectDto = appSubmissionDto.getAppEditSelectDto();

        if (appEditSelectDto != null) {
            if (!appEditSelectDto.isPremisesEdit()) {
                List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
                for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
                    appGrpPremisesDto.setLicenceDtos(null);
                }
                boolean eqGrpPremises = EqRequestForChangeSubmitResultChange.isChangeGrpPremises(appSubmissionDto.getAppGrpPremisesDtoList(), oldAppSubmissionDto.getAppGrpPremisesDtoList());
                if (eqGrpPremises) {
                    log.info(StringUtil.changeForLog("appGrpPremisesDto" + JsonUtil.parseToJson(appSubmissionDto.getAppGrpPremisesDtoList())));
                    log.info(StringUtil.changeForLog("oldappGrpPremisesDto" + JsonUtil.parseToJson(oldAppSubmissionDto.getAppGrpPremisesDtoList())));
                    result.put("premiss",MessageUtil.replaceMessage("GENERAL_ERR0006","premiss","field"));
                }
            }
            List<AppGrpPrimaryDocDto> dtoAppGrpPrimaryDocDtos = oldAppSubmissionDto.getAppGrpPrimaryDocDtos();
            if (dtoAppGrpPrimaryDocDtos != null) {
                for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : dtoAppGrpPrimaryDocDtos) {
                    appGrpPrimaryDocDto.setPassValidate(true);
                }
            }
            List<AppGrpPrimaryDocDto> dtoAppGrpPrimaryDocDtos1 = appSubmissionDto.getAppGrpPrimaryDocDtos();
            if (dtoAppGrpPrimaryDocDtos1 != null) {
                for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : dtoAppGrpPrimaryDocDtos1) {
                    appGrpPrimaryDocDto.setPassValidate(true);
                }
            }
            if (!appEditSelectDto.isDocEdit()) {
                boolean b = EqRequestForChangeSubmitResultChange.eqDocChange(appSubmissionDto.getAppGrpPrimaryDocDtos(), oldAppSubmissionDto.getAppGrpPrimaryDocDtos());
                if (b) {
                    log.info(StringUtil.changeForLog("appGrpPrimaryDocDto" + JsonUtil.parseToJson(appSubmissionDto.getAppGrpPrimaryDocDtos())));
                    log.info(StringUtil.changeForLog("oldAppGrpPrimaryDocDto" + JsonUtil.parseToJson(oldAppSubmissionDto.getAppGrpPrimaryDocDtos())));
                    result.put("document",MessageUtil.replaceMessage("GENERAL_ERR0006","document","field"));
                }
            }

            if (!appEditSelectDto.isServiceEdit()) {
                boolean b = EqRequestForChangeSubmitResultChange.eqServiceChange(appSubmissionDto.getAppSvcRelatedInfoDtoList(), oldAppSubmissionDto.getAppSvcRelatedInfoDtoList());
                if (b) {
                    log.info(StringUtil.changeForLog("AppSvcRelatedInfoDtoList" + JsonUtil.parseToJson(appSubmissionDto.getAppSvcRelatedInfoDtoList())));
                    log.info(StringUtil.changeForLog("oldAppSvcRelatedInfoDtoList" + JsonUtil.parseToJson(oldAppSubmissionDto.getAppSvcRelatedInfoDtoList())));
                    result.put("serviceId",MessageUtil.replaceMessage("GENERAL_ERR0006","serviceId","field"));
                }
            }
        }
        return result;
    }

    private AmendmentFeeDto getAmendmentFeeDto(AppEditSelectDto appEditSelectDto, boolean isCharity) {
        return getAmendmentFeeDto(appEditSelectDto.isChangeHciName(),
                appEditSelectDto.isChangeInLocation() || appEditSelectDto.isChangeAddFloorUnit(),
                appEditSelectDto.isChangeVehicle(), isCharity, appEditSelectDto.isChangeBusinessName());
    }

    private AmendmentFeeDto getAmendmentFeeDto(boolean changeHciName, boolean changeLocation, boolean changeVehicles,
            boolean isCharity, boolean changeBusiness) {
        AmendmentFeeDto amendmentFeeDto = new AmendmentFeeDto();
        amendmentFeeDto.setChangeInLicensee(Boolean.FALSE);
        amendmentFeeDto.setChangeInHCIName(changeHciName);
        amendmentFeeDto.setChangeInLocation(changeLocation);
        if (changeVehicles) {
            amendmentFeeDto.setChangeInHCIName(Boolean.TRUE);
        }
        amendmentFeeDto.setIsCharity(isCharity);
        amendmentFeeDto.setChangeBusinessName(changeBusiness);
        return amendmentFeeDto;
    }

    public void prepareJump(BaseProcessClass bpc) throws Exception {
        super.prepareJump(bpc);
    }

    private void setPsnDroTo(AppSubmissionDto appSubmissionDto, BaseProcessClass bpc) {
        Map<String, AppSvcPrincipalOfficersDto> personMap = (Map<String, AppSvcPrincipalOfficersDto>) ParamUtil.getSessionAttr(
                bpc.request, HcsaAppConst.PERSONSELECTMAP);
        String personMapStr = JsonUtil.parseToJson(personMap);
        appSubmissionDto.setDropDownPsnMapStr(personMapStr);
    }

    /**
     * @description: get data from page
     * @author: zixian
     * @date: 11/6/2019 5:05 PM
     * @param: request
     * @return: AppGrpPremisesDto
     */
    public static List<AppGrpPremisesDto> genAppGrpPremisesDtoList(HttpServletRequest request) {
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request, APPSUBMISSIONDTO);
        boolean onlySpecifiedSvc = false;
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)) {
            for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                if (!StringUtil.isEmpty(appSvcRelatedInfoDto.getRelLicenceNo()) || !StringUtil.isEmpty(appSvcRelatedInfoDto.getAlignLicenceNo())) {
                    onlySpecifiedSvc = true;
                    break;
                }
            }
        }
        if (onlySpecifiedSvc) {
            return appSubmissionDto.getAppGrpPremisesDtoList();
        }

        boolean isRfi = NewApplicationHelper.checkIsRfi(request);
        List<AppGrpPremisesDto> appGrpPremisesDtoList = IaisCommonUtils.genNewArrayList();
        List<HcsaServiceDto> hcsaServiceDtoList = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(request, AppServicesConsts.HCSASERVICEDTOLIST);
        boolean isMultiPremService = NewApplicationHelper.isMultiPremService(hcsaServiceDtoList);
        int count = 0;
        String[] premisesType = ParamUtil.getStrings(request, "premType");
        String[] hciName = ParamUtil.getStrings(request, "onSiteHciName");
        if (premisesType != null) {
            count = premisesType.length;
        }
        if(!isMultiPremService){
            count = 1;
        }
        String[] premisesIndexNo = ParamUtil.getStrings(request, "premisesIndexNo");
        String[] rfiCanEdit = ParamUtil.getStrings(request,"rfiCanEdit");
        //onsite
        String[] premisesSelect = ParamUtil.getStrings(request, "onSiteSelect");
        String[] postalCode = ParamUtil.getStrings(request, "onSitePostalCode");
        String[] blkNo = ParamUtil.getStrings(request, "onSiteBlkNo");
        String[] streetName = ParamUtil.getStrings(request, "onSiteStreetName");
        String[] floorNo = ParamUtil.getStrings(request, "onSiteFloorNo");
        String[] unitNo = ParamUtil.getStrings(request, "onSiteUnitNo");
        String[] buildingName = ParamUtil.getStrings(request, "onSiteBuildingName");
        String[] siteAddressType = ParamUtil.getStrings(request, "onSiteAddressType");
        String[] siteEmail = ParamUtil.getStrings(request, "onSiteEmail");
        String[] offTelNo = ParamUtil.getStrings(request, "onSiteOffTelNo");
        String[] scdfRefNo = ParamUtil.getStrings(request, "onSiteScdfRefNo");
        String[] isOtherLic = ParamUtil.getStrings(request, "onSiteIsOtherLic");
        //conveyance
        String[] conveyanceHciName = ParamUtil.getStrings(request, "conveyanceHciName");
        String[] conPremisesSelect = ParamUtil.getStrings(request, "conveyanceSelect");
        String[] conVehicleNo = ParamUtil.getStrings(request, "conveyanceVehicleNo");
        String[] conPostalCode = ParamUtil.getStrings(request, "conveyancePostalCode");
        String[] conBlkNo = ParamUtil.getStrings(request, "conveyanceBlkNo");
        String[] conStreetName = ParamUtil.getStrings(request, "conveyanceStreetName");
        String[] conFloorNo = ParamUtil.getStrings(request, "conveyanceFloorNo");
        String[] conUnitNo = ParamUtil.getStrings(request, "conveyanceUnitNo");
        String[] conBuildingName = ParamUtil.getStrings(request, "conveyanceBuildingName");
        String[] conEmail = ParamUtil.getStrings(request, "conveyanceEmail");
        String[] conSiteAddressType = ParamUtil.getStrings(request, "conveyanceAddrType");
        //offSite
        String[] offSiteHciName = ParamUtil.getStrings(request, "offSiteHciName");
        String[] offSitePremisesSelect = ParamUtil.getStrings(request, "offSiteSelect");
        String[] offSitePostalCode = ParamUtil.getStrings(request, "offSitePostalCode");
        String[] offSiteBlkNo = ParamUtil.getStrings(request, "offSiteBlkNo");
        String[] offSiteStreetName = ParamUtil.getStrings(request, "offSiteStreetName");
        String[] offSiteFloorNo = ParamUtil.getStrings(request, "offSiteFloorNo");
        String[] offSiteUnitNo = ParamUtil.getStrings(request, "offSiteUnitNo");
        String[] offSiteBuildingName = ParamUtil.getStrings(request, "offSiteBuildingName");
        String[] offSiteEmail = ParamUtil.getStrings(request, "offSiteEmail");
        String[] offSiteSiteAddressType = ParamUtil.getStrings(request, "offSiteAddrType");

        String[] easMtsPremisesSelect = ParamUtil.getStrings(request, "easMtsSelect");
        //every prem's ph length
        String[] phLengths = ParamUtil.getStrings(request, "phLength");
        String[] premValue = ParamUtil.getStrings(request, "premValue");
        String[] isParyEdit = ParamUtil.getStrings(request, "isPartEdit");
        String[] chooseExistData = ParamUtil.getStrings(request, "chooseExistData");
        String[] opLengths = ParamUtil.getStrings(request,"opLength");
        String[] retrieveflag = ParamUtil.getStrings(request,"retrieveflag");
        String[] weeklyLengths = ParamUtil.getStrings(request,"weeklyLength");
        String[] eventLengths = ParamUtil.getStrings(request,"eventLength");
        for (int i = 0; i < count; i++) {
            AppGrpPremisesDto appGrpPremisesDto = new AppGrpPremisesDto();
            String premisesSel = "";
            if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premisesType[i])) {
                premisesSel = premisesSelect[i];
            } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesType[i])) {
                premisesSel = conPremisesSelect[i];
            } else if (ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(premisesType[i])) {
                premisesSel = offSitePremisesSelect[i];
            } else if (ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE.equals(premisesType[i])){
                premisesSel = easMtsPremisesSelect[i];
            }
            String premIndexNo = "";
            try {
                premIndexNo = premisesIndexNo[i];
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            if (StringUtil.isEmpty(premIndexNo)) {
                log.info(StringUtil.changeForLog("New premise index"));
                premIndexNo = UUID.randomUUID().toString();
            }
            String appType = appSubmissionDto.getAppType();
            boolean newApp = !isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType);
            if (newApp) {
                if (!StringUtil.isEmpty(premisesSel) && !premisesSel.equals("-1")
                        && !premisesSel.equals(ApplicationConsts.NEW_PREMISES) && AppConsts.YES.equals(chooseExistData[i])) {
                    AppGrpPremisesDto licPremise = NewApplicationHelper.getPremisesFromMap(premisesSel, request);
                    if (licPremise != null) {
                        appGrpPremisesDto = (AppGrpPremisesDto) CopyUtil.copyMutableObject(licPremise);
                    } else {
                        log.info(StringUtil.changeForLog("can not found this existing premises data ...."));
                    }
                    if (appGrpPremisesDto != null) {
                        //get value for jsp page
                        appGrpPremisesDto.setExistingData(chooseExistData[i]);
                        NewApplicationHelper.setPremise(appGrpPremisesDto, premIndexNo, appSubmissionDto);
                        appGrpPremisesDtoList.add(appGrpPremisesDto);
                    }
                    continue;
                }
            } else if (AppConsts.YES.equals(chooseExistData[i])) {
                appGrpPremisesDto = NewApplicationHelper.getPremisesFromMap(premisesSel, request);
                if (appGrpPremisesDto != null) {
                    if (AppConsts.TRUE.equals(rfiCanEdit[i])) {
                        appGrpPremisesDto.setRfiCanEdit(true);
                    } else {
                        appGrpPremisesDto.setRfiCanEdit(false);
                    }
                    if (!AppConsts.YES.equals(isParyEdit[i])) {
                        appGrpPremisesDto.setExistingData(chooseExistData[i]);
                        NewApplicationHelper.setPremise(appGrpPremisesDto, premIndexNo, appSubmissionDto);
                        appGrpPremisesDtoList.add(appGrpPremisesDto);
                        continue;
                    } else {
                        log.info(StringUtil.changeForLog("Get data from page for " + StringUtil.clarify(premisesSel)));
                    }
                } else {
                    log.warn(StringUtil.changeForLog("##### warn Data: " + premIndexNo));
                    appGrpPremisesDto = new AppGrpPremisesDto();
                }
            }
            appGrpPremisesDto.setExistingData(chooseExistData[i]);
            NewApplicationHelper.setPremise(appGrpPremisesDto, premIndexNo, appSubmissionDto);
            // set premise type
            appGrpPremisesDto.setPremisesType(premisesType[i]);
            //List<AppPremPhOpenPeriodDto> appPremPhOpenPeriods = IaisCommonUtils.genNewArrayList();
            List<OperationHoursReloadDto> weeklyDtoList = IaisCommonUtils.genNewArrayList();
            List<OperationHoursReloadDto> phDtoList = IaisCommonUtils.genNewArrayList();
            List<AppPremEventPeriodDto> eventList = IaisCommonUtils.genNewArrayList();
            /*int length = 0;
            try {
                length = Integer.parseInt(phLength[i]);
            } catch (Exception e) {
                log.error(StringUtil.changeForLog("length can not parse to int"));
            }*/
            int opLength = 0;
            try{
                opLength = Integer.parseInt(opLengths[i]);
            }catch(Exception e){
                log.error(StringUtil.changeForLog("operation length can not parse to int"));
            }
            int weeklyLength = 0;
            try{
                weeklyLength = Integer.parseInt(weeklyLengths[i]);
            }catch(Exception e){
                log.error(StringUtil.changeForLog("weekly length can not parse to int"));
            }
            int phLength = 0;
            try{
                phLength = Integer.parseInt(phLengths[i]);
            }catch(Exception e){
                log.error(StringUtil.changeForLog("ph length can not parse to int"));
            }
            int eventLength = 0;
            try{
                eventLength = Integer.parseInt(eventLengths[i]);
            }catch(Exception e){
                log.error(StringUtil.changeForLog("event length can not parse to int"));
            }
            if(AppConsts.TRUE.equals(rfiCanEdit[i])){
                appGrpPremisesDto.setRfiCanEdit(true);
            }else{
                appGrpPremisesDto.setRfiCanEdit(false);
            }
            if(AppConsts.YES.equals(retrieveflag[i])){
                appGrpPremisesDto.setClickRetrieve(true);
            }else{
                appGrpPremisesDto.setClickRetrieve(false);
            }
            List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtos = IaisCommonUtils.genNewArrayList();
            if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premisesType[i])) {
                String premVal = premValue[i];
                String fireSafetyCertIssuedDateStr = ParamUtil.getString(request, premVal+"onSiteFireSafetyCertIssuedDate");
                appGrpPremisesDto.setPremisesSelect(premisesSelect[i]);
                appGrpPremisesDto.setHciName(hciName[i]);
                appGrpPremisesDto.setPostalCode(postalCode[i]);
                appGrpPremisesDto.setBlkNo(blkNo[i]);
                appGrpPremisesDto.setStreetName(streetName[i]);
                appGrpPremisesDto.setFloorNo(floorNo[i]);
                appGrpPremisesDto.setUnitNo(unitNo[i]);
                appGrpPremisesDto.setBuildingName(buildingName[i]);
                appGrpPremisesDto.setEasMtsPubEmail(siteEmail[i]);
                appGrpPremisesDto.setScdfRefNo(scdfRefNo[i]);
                appGrpPremisesDto.setAddrType(siteAddressType[i]);
                appGrpPremisesDto.setOffTelNo(offTelNo[i]);
                Date fireSafetyCertIssuedDateDate = DateUtil.parseDate(fireSafetyCertIssuedDateStr, Formatter.DATE);
                appGrpPremisesDto.setCertIssuedDt(fireSafetyCertIssuedDateDate);
                String certIssuedDtStr = Formatter.formatDate(fireSafetyCertIssuedDateDate);
                appGrpPremisesDto.setCertIssuedDtStr(certIssuedDtStr);
                if (AppConsts.YES.equals(isOtherLic[i])) {
                    appGrpPremisesDto.setLocateWithOthers(AppConsts.YES);
                } else if (AppConsts.NO.equals(isOtherLic[i])) {
                    appGrpPremisesDto.setLocateWithOthers(AppConsts.NO);
                }

                //weekly
                for(int j = 0;j<weeklyLength;j++){
                    OperationHoursReloadDto weeklyDto = new OperationHoursReloadDto();
                    String[] weeklyVal = ParamUtil.getStrings(request,genPageName(premVal,"onSiteWeekly",j));
                    String allDay = ParamUtil.getString(request,genPageName(premVal,"onSiteWeeklyAllDay",j));
                    //reload
                    String weeklySelect = StringUtil.arrayToString(weeklyVal);
                    weeklyDto.setSelectVal(weeklySelect);
                    if(weeklyVal != null){
                        List<String> selectValList = Arrays.asList(weeklyVal);
                        weeklyDto.setSelectValList(selectValList);
                    }
                    if(AppConsts.TRUE.equals(allDay)){
                        weeklyDto.setSelectAllDay(true);
                        weeklyDto.setStartFromHH(null);
                        weeklyDto.setStartFromMM(null);
                        weeklyDto.setEndToHH(null);
                        weeklyDto.setEndToMM(null);
                    }else{
                        String weeklyStartHH = ParamUtil.getString(request,genPageName(premVal,"onSiteWeeklyStartHH",j));
                        String weeklyStartMM = ParamUtil.getString(request,genPageName(premVal,"onSiteWeeklyStartMM",j));
                        String weeklyEndHH = ParamUtil.getString(request,genPageName(premVal,"onSiteWeeklyEndHH",j));
                        String weeklyEndMM = ParamUtil.getString(request,genPageName(premVal,"onSiteWeeklyEndMM",j));
                        weeklyDto.setStartFromHH(weeklyStartHH);
                        weeklyDto.setStartFromMM(weeklyStartMM);
                        weeklyDto.setEndToHH(weeklyEndHH);
                        weeklyDto.setEndToMM(weeklyEndMM);
                    }
                    weeklyDtoList.add(weeklyDto);
                }
                //ph
                for(int j = 0;j < phLength;j++){
                    OperationHoursReloadDto phDto = new OperationHoursReloadDto();
                    String[] phVal = ParamUtil.getStrings(request,genPageName(premVal,"onSitePubHoliday",j));
                    String allDay = ParamUtil.getString(request,genPageName(premVal,"onSitePhAllDay",j));
                    //reload
                    String phSelect = StringUtil.arrayToString(phVal);
                    phDto.setSelectVal(phSelect);
                    if(phSelect != null){
                        List<String> selectValList = Arrays.asList(phVal);
                        phDto.setSelectValList(selectValList);
                    }
                    if(AppConsts.TRUE.equals(allDay)){
                        phDto.setSelectAllDay(true);
                        phDto.setStartFromHH(null);
                        phDto.setStartFromMM(null);
                        phDto.setEndToHH(null);
                        phDto.setEndToMM(null);
                        phDtoList.add(phDto);
                    }else{
                        String phStartHH = ParamUtil.getString(request,genPageName(premVal,"onSitePhStartHH",j));
                        String phStartMM = ParamUtil.getString(request,genPageName(premVal,"onSitePhStartMM",j));
                        String phEndHH = ParamUtil.getString(request,genPageName(premVal,"onSitePhEndHH",j));
                        String phEndMM = ParamUtil.getString(request,genPageName(premVal,"onSitePhEndMM",j));
                        phDto.setStartFromHH(phStartHH);
                        phDto.setStartFromMM(phStartMM);
                        phDto.setEndToHH(phEndHH);
                        phDto.setEndToMM(phEndMM);
                        if(phLength >1 || !StringUtil.isEmpty(phSelect) || !StringUtil.isEmpty(phStartHH) || !StringUtil.isEmpty(phStartMM) || !StringUtil.isEmpty(phEndHH)|| !StringUtil.isEmpty(phEndMM)){
                            phDtoList.add(phDto);
                        }
                    }

                }
                //event
                for(int j = 0;j < eventLength;j++){
                    AppPremEventPeriodDto appPremEventPeriodDto = new AppPremEventPeriodDto();
                    String eventName = ParamUtil.getString(request,genPageName(premVal,"onSiteEvent",j));
                    String eventStartStr = ParamUtil.getString(request,genPageName(premVal,"onSiteEventStart",j));
                    Date eventStart = DateUtil.parseDate(eventStartStr, Formatter.DATE);
                    String eventEndStr = ParamUtil.getString(request,genPageName(premVal,"onSiteEventEnd",j));
                    Date eventEnd = DateUtil.parseDate(eventEndStr, Formatter.DATE);
                    appPremEventPeriodDto.setEventName(eventName);
                    appPremEventPeriodDto.setStartDate(eventStart);
                    appPremEventPeriodDto.setStartDateStr(eventStartStr);
                    appPremEventPeriodDto.setEndDate(eventEnd);
                    appPremEventPeriodDto.setEndDateStr(eventEndStr);
                    if(eventLength >1 || !StringUtil.isEmpty(eventName) || !StringUtil.isEmpty(eventStartStr) || !StringUtil.isEmpty(eventEndStr)){
                        eventList.add(appPremEventPeriodDto);
                    }
                }
                addFloorNoAndUnitNo(premValue[i], "onSiteFloorNo", "onSiteUnitNo", opLength,
                        appPremisesOperationalUnitDtos, request);
            } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesType[i])) {

                appGrpPremisesDto.setConveyanceHciName(conveyanceHciName[i]);
                appGrpPremisesDto.setPremisesSelect(conPremisesSelect[i]);
                appGrpPremisesDto.setConveyanceVehicleNo(conVehicleNo[i]);
                appGrpPremisesDto.setConveyancePostalCode(conPostalCode[i]);
                appGrpPremisesDto.setConveyanceBlockNo(conBlkNo[i]);
                appGrpPremisesDto.setConveyanceStreetName(conStreetName[i]);
                appGrpPremisesDto.setConveyanceFloorNo(conFloorNo[i]);
                appGrpPremisesDto.setConveyanceUnitNo(conUnitNo[i]);
                appGrpPremisesDto.setConveyanceBuildingName(conBuildingName[i]);
                appGrpPremisesDto.setConveyanceEmail(conEmail[i]);
                appGrpPremisesDto.setEasMtsPubEmail(conEmail[i]);
                appGrpPremisesDto.setConveyanceAddressType(conSiteAddressType[i]);

                //weekly
                String premVal = premValue[i];
                for(int j = 0;j<weeklyLength;j++){
                    OperationHoursReloadDto weeklyDto = new OperationHoursReloadDto();
                    String[] weeklyVal = ParamUtil.getStrings(request,genPageName(premVal,"conveyanceWeekly",j));
                    String allDay = ParamUtil.getString(request,genPageName(premVal,"conveyanceWeeklyAllDay",j));
                    //reload
                    String weeklySelect = StringUtil.arrayToString(weeklyVal);
                    weeklyDto.setSelectVal(weeklySelect);
                    if(weeklyVal != null){
                        List<String> selectValList = Arrays.asList(weeklyVal);
                        weeklyDto.setSelectValList(selectValList);
                    }
                    if(AppConsts.TRUE.equals(allDay)){
                        weeklyDto.setSelectAllDay(true);
                        weeklyDto.setStartFromHH(null);
                        weeklyDto.setStartFromMM(null);
                        weeklyDto.setEndToHH(null);
                        weeklyDto.setEndToMM(null);
                    }else{
                        String weeklyStartHH = ParamUtil.getString(request,genPageName(premVal,"conveyanceWeeklyStartHH",j));
                        String weeklyStartMM = ParamUtil.getString(request,genPageName(premVal,"conveyanceWeeklyStartMM",j));
                        String weeklyEndHH = ParamUtil.getString(request,genPageName(premVal,"conveyanceWeeklyEndHH",j));
                        String weeklyEndMM = ParamUtil.getString(request,genPageName(premVal,"conveyanceWeeklyEndMM",j));
                        weeklyDto.setStartFromHH(weeklyStartHH);
                        weeklyDto.setStartFromMM(weeklyStartMM);
                        weeklyDto.setEndToHH(weeklyEndHH);
                        weeklyDto.setEndToMM(weeklyEndMM);
                    }
                    weeklyDtoList.add(weeklyDto);
                }
                //ph
                for(int j = 0;j < phLength;j++){
                    OperationHoursReloadDto phDto = new OperationHoursReloadDto();
                    String[] phVal = ParamUtil.getStrings(request,genPageName(premVal,"conveyancePubHoliday",j));
                    String allDay = ParamUtil.getString(request,genPageName(premVal,"conveyancePhAllDay",j));
                    //reload
                    String phSelect = StringUtil.arrayToString(phVal);
                    phDto.setSelectVal(phSelect);
                    if(phSelect != null){
                        List<String> selectValList = Arrays.asList(phVal);
                        phDto.setSelectValList(selectValList);
                    }
                    if(AppConsts.TRUE.equals(allDay)){
                        phDto.setSelectAllDay(true);
                        phDto.setStartFromHH(null);
                        phDto.setStartFromMM(null);
                        phDto.setEndToHH(null);
                        phDto.setEndToMM(null);
                        phDtoList.add(phDto);
                    }else{
                        String phStartHH = ParamUtil.getString(request,genPageName(premVal,"conveyancePhStartHH",j));
                        String phStartMM = ParamUtil.getString(request,genPageName(premVal,"conveyancePhStartMM",j));
                        String phEndHH = ParamUtil.getString(request,genPageName(premVal,"conveyancePhEndHH",j));
                        String phEndMM = ParamUtil.getString(request,genPageName(premVal,"conveyancePhEndMM",j));
                        phDto.setStartFromHH(phStartHH);
                        phDto.setStartFromMM(phStartMM);
                        phDto.setEndToHH(phEndHH);
                        phDto.setEndToMM(phEndMM);
                        if(phLength >1 || !StringUtil.isEmpty(phSelect) || !StringUtil.isEmpty(phStartHH) || !StringUtil.isEmpty(phStartMM) || !StringUtil.isEmpty(phEndHH)|| !StringUtil.isEmpty(phEndMM)){
                            phDtoList.add(phDto);
                        }
                    }

                }
                //event
                for(int j = 0;j < eventLength;j++){
                    AppPremEventPeriodDto appPremEventPeriodDto = new AppPremEventPeriodDto();
                    String eventName = ParamUtil.getString(request,genPageName(premVal,"conveyanceEvent",j));
                    String eventStartStr = ParamUtil.getString(request,genPageName(premVal,"conveyanceEventStart",j));
                    Date eventStart = DateUtil.parseDate(eventStartStr, Formatter.DATE);
                    String eventEndStr = ParamUtil.getString(request,genPageName(premVal,"conveyanceEventEnd",j));
                    Date eventEnd = DateUtil.parseDate(eventEndStr, Formatter.DATE);
                    appPremEventPeriodDto.setEventName(eventName);
                    appPremEventPeriodDto.setStartDate(eventStart);
                    appPremEventPeriodDto.setStartDateStr(eventStartStr);
                    appPremEventPeriodDto.setEndDate(eventEnd);
                    appPremEventPeriodDto.setEndDateStr(eventEndStr);
                    if(eventLength >1 || !StringUtil.isEmpty(eventName) || !StringUtil.isEmpty(eventStartStr) || !StringUtil.isEmpty(eventEndStr)){
                        eventList.add(appPremEventPeriodDto);
                    }
                }
                addFloorNoAndUnitNo(premValue[i], "conveyanceFloorNo", "conveyanceUnitNo", opLength,
                        appPremisesOperationalUnitDtos, request);
            } else if (ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(premisesType[i])) {
                appGrpPremisesDto.setOffSiteHciName(offSiteHciName[i]);
                appGrpPremisesDto.setPremisesSelect(offSitePremisesSelect[i]);
                appGrpPremisesDto.setOffSitePostalCode(offSitePostalCode[i]);
                appGrpPremisesDto.setOffSiteBlockNo(offSiteBlkNo[i]);
                appGrpPremisesDto.setOffSiteStreetName(offSiteStreetName[i]);
                appGrpPremisesDto.setOffSiteFloorNo(offSiteFloorNo[i]);
                appGrpPremisesDto.setOffSiteUnitNo(offSiteUnitNo[i]);
                appGrpPremisesDto.setOffSiteBuildingName(offSiteBuildingName[i]);
                appGrpPremisesDto.setOffSiteEmail(offSiteEmail[i]);
                appGrpPremisesDto.setEasMtsPubEmail(offSiteEmail[i]);
                appGrpPremisesDto.setOffSiteAddressType(offSiteSiteAddressType[i]);
                //weekly
                String premVal = premValue[i];
                for(int j = 0;j<weeklyLength;j++){
                    OperationHoursReloadDto weeklyDto = new OperationHoursReloadDto();
                    String[] weeklyVal = ParamUtil.getStrings(request,genPageName(premVal,"offSiteWeekly",j));
                    String allDay = ParamUtil.getString(request,genPageName(premVal,"offSiteWeeklyAllDay",j));
                    //reload
                    String weeklySelect = StringUtil.arrayToString(weeklyVal);
                    weeklyDto.setSelectVal(weeklySelect);
                    if(weeklyVal != null){
                        List<String> selectValList = Arrays.asList(weeklyVal);
                        weeklyDto.setSelectValList(selectValList);
                    }
                    if(AppConsts.TRUE.equals(allDay)){
                        weeklyDto.setSelectAllDay(true);
                        weeklyDto.setStartFromHH(null);
                        weeklyDto.setStartFromMM(null);
                        weeklyDto.setEndToHH(null);
                        weeklyDto.setEndToMM(null);
                    }else{
                        String weeklyStartHH = ParamUtil.getString(request,genPageName(premVal,"offSiteWeeklyStartHH",j));
                        String weeklyStartMM = ParamUtil.getString(request,genPageName(premVal,"offSiteWeeklyStartMM",j));
                        String weeklyEndHH = ParamUtil.getString(request,genPageName(premVal,"offSiteWeeklyEndHH",j));
                        String weeklyEndMM = ParamUtil.getString(request,genPageName(premVal,"offSiteWeeklyEndMM",j));
                        weeklyDto.setStartFromHH(weeklyStartHH);
                        weeklyDto.setStartFromMM(weeklyStartMM);
                        weeklyDto.setEndToHH(weeklyEndHH);
                        weeklyDto.setEndToMM(weeklyEndMM);
                    }
                    weeklyDtoList.add(weeklyDto);
                }
                //ph
                for(int j = 0;j < phLength;j++){
                    OperationHoursReloadDto phDto = new OperationHoursReloadDto();
                    String[] phVal = ParamUtil.getStrings(request,genPageName(premVal,"offSitePubHoliday",j));
                    String allDay = ParamUtil.getString(request,genPageName(premVal,"offSitePhAllDay",j));
                    //reload
                    String phSelect = StringUtil.arrayToString(phVal);
                    phDto.setSelectVal(phSelect);
                    if(phSelect != null){
                        List<String> selectValList = Arrays.asList(phVal);
                        phDto.setSelectValList(selectValList);
                    }
                    if(AppConsts.TRUE.equals(allDay)){
                        phDto.setSelectAllDay(true);
                        phDto.setStartFromHH(null);
                        phDto.setStartFromMM(null);
                        phDto.setEndToHH(null);
                        phDto.setEndToMM(null);
                        phDtoList.add(phDto);
                    }else{
                        String phStartHH = ParamUtil.getString(request,genPageName(premVal,"offSitePhStartHH",j));
                        String phStartMM = ParamUtil.getString(request,genPageName(premVal,"offSitePhStartMM",j));
                        String phEndHH = ParamUtil.getString(request,genPageName(premVal,"offSitePhEndHH",j));
                        String phEndMM = ParamUtil.getString(request,genPageName(premVal,"offSitePhEndMM",j));
                        phDto.setStartFromHH(phStartHH);
                        phDto.setStartFromMM(phStartMM);
                        phDto.setEndToHH(phEndHH);
                        phDto.setEndToMM(phEndMM);
                        if(phLength >1 || !StringUtil.isEmpty(phSelect) || !StringUtil.isEmpty(phStartHH) || !StringUtil.isEmpty(phStartMM) || !StringUtil.isEmpty(phEndHH)|| !StringUtil.isEmpty(phEndMM)){
                            phDtoList.add(phDto);
                        }
                    }

                }
                //event
                for(int j = 0;j < eventLength;j++){
                    AppPremEventPeriodDto appPremEventPeriodDto = new AppPremEventPeriodDto();
                    String eventName = ParamUtil.getString(request,genPageName(premVal,"offSiteEvent",j));
                    String eventStartStr = ParamUtil.getString(request,genPageName(premVal,"offSiteEventStart",j));
                    Date eventStart = DateUtil.parseDate(eventStartStr, Formatter.DATE);
                    String eventEndStr = ParamUtil.getString(request,genPageName(premVal,"offSiteEventEnd",j));
                    Date eventEnd = DateUtil.parseDate(eventEndStr, Formatter.DATE);
                    appPremEventPeriodDto.setEventName(eventName);
                    appPremEventPeriodDto.setStartDate(eventStart);
                    appPremEventPeriodDto.setStartDateStr(eventStartStr);
                    appPremEventPeriodDto.setEndDate(eventEnd);
                    appPremEventPeriodDto.setEndDateStr(eventEndStr);
                    if(eventLength >1 || !StringUtil.isEmpty(eventName) || !StringUtil.isEmpty(eventStartStr) || !StringUtil.isEmpty(eventEndStr)){
                        eventList.add(appPremEventPeriodDto);
                    }
                }
                addFloorNoAndUnitNo(premValue[i], "offSiteFloorNo", "offSiteUnitNo", opLength,
                        appPremisesOperationalUnitDtos, request);
            }else if(ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE.equals(premisesType[i])){
                String easMtsHciName = ParamUtil.getString(request, "easMtsHciName");
                String easMtsPostalCode = ParamUtil.getString(request, "easMtsPostalCode");
                String easMtsBlkNo = ParamUtil.getString(request, "easMtsBlkNo");
                String easMtsStreetName = ParamUtil.getString(request, "easMtsStreetName");
                String easMtsFloorNo = ParamUtil.getString(request, "easMtsFloorNo");
                String easMtsUnitNo = ParamUtil.getString(request, "easMtsUnitNo");
                String easMtsBuildingName = ParamUtil.getString(request, "easMtsBuildingName");
                String easMtsAddressType = ParamUtil.getString(request, "easMtsAddrType");
                String easMtsUseOnly = ParamUtil.getString(request, "easMtsUseOnlyVal");
                String easMtsPubEmail = ParamUtil.getString(request, "easMtsPubEmail");
                String easMtsPubHotline = ParamUtil.getString(request, "easMtsPubHotline");
                appGrpPremisesDto.setEasMtsHciName(easMtsHciName);
                appGrpPremisesDto.setPremisesSelect(easMtsPremisesSelect[i]);
                appGrpPremisesDto.setEasMtsPostalCode(easMtsPostalCode);
                appGrpPremisesDto.setEasMtsAddressType(easMtsAddressType);
                appGrpPremisesDto.setEasMtsBlockNo(easMtsBlkNo);
                appGrpPremisesDto.setEasMtsFloorNo(easMtsFloorNo);
                appGrpPremisesDto.setEasMtsUnitNo(easMtsUnitNo);
                appGrpPremisesDto.setEasMtsStreetName(easMtsStreetName);
                appGrpPremisesDto.setEasMtsBuildingName(easMtsBuildingName);
                appGrpPremisesDto.setEasMtsUseOnly(easMtsUseOnly);
                appGrpPremisesDto.setEasMtsPubEmail(easMtsPubEmail);
                appGrpPremisesDto.setEasMtsPubHotline(easMtsPubHotline);
                addFloorNoAndUnitNo(premValue[i], "easMtsFloorNo", "easMtsUnitNo", opLength,
                        appPremisesOperationalUnitDtos, request);
            }
            //appGrpPremisesDto.setAppPremPhOpenPeriodList(appPremPhOpenPeriods);
            appGrpPremisesDto.setAppPremisesOperationalUnitDtos(appPremisesOperationalUnitDtos);
            appGrpPremisesDto.setWeeklyDtoList(weeklyDtoList);
            appGrpPremisesDto.setPhDtoList(phDtoList);
            appGrpPremisesDto.setEventDtoList(eventList);
            appGrpPremisesDtoList.add(appGrpPremisesDto);
        }
        /*if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType()) ||
                ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())) {
            //set premises edit status
            NewApplicationHelper.setPremEditStatus(appGrpPremisesDtoList, getAppGrpPremisesDtos(appSubmissionDto.getOldAppSubmissionDto()));
        }*/
        return appGrpPremisesDtoList;
    }

    private static void addFloorNoAndUnitNo(String prefix, String floorNoName, String unitNoNmae, int opLength,
            List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtos, HttpServletRequest request) {
        log.info(StringUtil.changeForLog("The length of additional floor and unit: " + opLength));
        if (opLength <= 0) {
            return;
        }
        for (int j = 0; j < opLength; j++) {
            String opFloorNo = ParamUtil.getString(request, prefix + floorNoName + j);
            String opUnitNo = ParamUtil.getString(request, prefix + unitNoNmae + j);
            if (StringUtil.isEmpty(opFloorNo) && StringUtil.isEmpty(opUnitNo)) {
                continue;
            }
            AppPremisesOperationalUnitDto operationalUnitDto = new AppPremisesOperationalUnitDto();
            operationalUnitDto.setFloorNo(opFloorNo);
            operationalUnitDto.setUnitNo(opUnitNo);
            operationalUnitDto.setSeqNum(j);
            appPremisesOperationalUnitDtos.add(operationalUnitDto);
        }
    }

    private void loadingDraft(BaseProcessClass bpc, String draftNo) {
        log.info(StringUtil.changeForLog("the do loadingDraft start ...."));
        Object draftNumber = bpc.request.getSession().getAttribute(DRAFT_NUMBER);
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
                if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())
                        || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())) {
                    requestForChangeService.svcDocToPresmise(appSubmissionDto);
                    requestForChangeService.svcDocToPresmise(appSubmissionDto.getOldAppSubmissionDto());
                }
                //set max file index into session
                NewApplicationHelper.reSetMaxFileIndex(appSubmissionDto.getMaxFileIndex());

                List<String> stepColor = appSubmissionDto.getStepColor();
                if (stepColor != null) {
                    HashMap<String, String> coMap = new HashMap<>(4);
                    coMap.put("premises", "");
                    coMap.put("document", "");
                    coMap.put("information", "");
                    coMap.put("previewli", "");
                    if (!stepColor.isEmpty()) {
                        for (String str : stepColor) {
                            if ("premises".equals(str)) {
                                coMap.put("premises", str);
                            } else if ("document".equals(str)) {
                                coMap.put("document", str);
                            } else if ("information".equals(str)) {
                                coMap.put("information", str);
                            } else if ("previewli".equals(str)) {
                                coMap.put("previewli", str);
                            } else {
                                bpc.request.getSession().setAttribute("serviceConfig", str);
                            }
                        }
                    }
                    bpc.getSession().setAttribute(NewApplicationConstant.CO_MAP, coMap);
                }
                if (appSubmissionDto.getAppGrpPremisesDtoList() != null && appSubmissionDto.getAppGrpPremisesDtoList().size() > 0) {
                    ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
                } else {
                    ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, null);
                }
                ParamUtil.setSessionAttr(bpc.request, DRAFTCONFIG, "test");
            }
            bpc.request.getSession().setAttribute(SELECT_DRAFT_NO, null);
        }
        if (draftNumber != null) {

            ParamUtil.setSessionAttr(bpc.request, DRAFTCONFIG, null);
            String entryType = ParamUtil.getString(bpc.request,"entryType");
            if(!StringUtil.isEmpty(entryType) && entryType.equals("assessment")){
                ParamUtil.setSessionAttr(bpc.request,ASSESSMENTCONFIG,"test");
            }
        }
        log.info(StringUtil.changeForLog("the do loadingDraft end ...."));
    }

    private void requestForChangeOrRenewLoading(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do requestForChangeLoading start ...."));
        String appType = (String) ParamUtil.getRequestAttr(bpc.request, "appType");
        String currentEdit = (String) ParamUtil.getRequestAttr(bpc.request, RfcConst.RFC_CURRENT_EDIT);
        boolean canDoEdit = (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType));
        if(!canDoEdit || StringUtil.isEmpty(currentEdit)){
            return;
        }

        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getRequestAttr(bpc.request, RfcConst.APPSUBMISSIONDTORFCATTR);
        if (canDoEdit && appSubmissionDto != null) {
            AuditTrailHelper.setAuditTrailInfoByAppType(appType);
            ParamUtil.setSessionAttr(bpc.request, "hasDetail", "Y");
            ParamUtil.setSessionAttr(bpc.request, "isSingle", "Y");
            AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
            if (RfcConst.EDIT_LICENSEE.equals(currentEdit)) {
                appEditSelectDto.setLicenseeEdit(true);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "licensee");
            } else if (RfcConst.EDIT_PREMISES.equals(currentEdit)) {
                appEditSelectDto.setPremisesEdit(true);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "premises");
            } else if (RfcConst.EDIT_PRIMARY_DOC.equals(currentEdit)) {
                appEditSelectDto.setDocEdit(true);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "documents");
            } else if (RfcConst.EDIT_SERVICE.equals(currentEdit)) {
                appEditSelectDto.setServiceEdit(true);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "serviceForms");
            }
            appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
            appSubmissionDto.setNeedEditController(true);
            ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
            HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute(NewApplicationConstant.CO_MAP);
            coMap.put("licensee", "licensee");
            coMap.put("premises", "premises");
            coMap.put("document", "document");
            coMap.put("information", "information");
            coMap.put("previewli", "previewli");
            bpc.request.getSession().setAttribute(NewApplicationConstant.CO_MAP, coMap);
        }
        log.info(StringUtil.changeForLog("the do requestForChangeLoading end ...."));
    }

    private void requestForInformationLoading(BaseProcessClass bpc, String appNo) {
        log.info(StringUtil.changeForLog("the do requestForInformationLoading start ...."));
        String msgId = (String) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_INTER_INBOX_MESSAGE_ID);
        //msgId = "415199C2-4AAA-42BF-B068-9B019BF1ED1C";
        log.info(StringUtil.changeForLog("MsgId: " + msgId));
        if (!StringUtil.isEmpty(appNo) && !StringUtil.isEmpty(msgId)) {
//            appNo = "AN210511010651A-01";
            AppSubmissionDto appSubmissionDto = appSubmissionService.getAppSubmissionDtoByAppNo(appNo);
            if (appSubmissionDto != null) {
                appSubmissionDto.setAmountStr("N/A");
                // rfiLoadingCheckImplForRenew.checkPremiseInfo(bpc.request,appSubmissionDto,appNo);
                if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType()) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())) {
                    requestForChangeService.svcDocToPresmise(appSubmissionDto);
                }else {
                  /*  rfiLoadingExc.checkPremiseInfo(appSubmissionDto,appNo);*/
                }
            }
            InterMessageDto interMessageDto = appSubmissionService.getInterMessageById(msgId);
            if (MessageConstants.MESSAGE_STATUS_RESPONSE.equals(interMessageDto.getStatus())) {
                appSubmissionDto = null;
            }

            if (appSubmissionDto != null) {
                svcRelatedInfoRFI(appSubmissionDto, appNo);
                //set max file index into session
                NewApplicationHelper.reSetMaxFileIndex(appSubmissionDto.getMaxFileIndex());

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
                boolean isRenewalOrRfc = ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType) || ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType);
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
                ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
                //ParamUtil.setSessionAttr(bpc.request,OLDAPPSUBMISSIONDTO,oldAppSubmissionDto);
                HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute(NewApplicationConstant.CO_MAP);
                coMap.put("premises", "premises");
                coMap.put("document", "document");
                coMap.put("information", "information");
                coMap.put("previewli", "previewli");
                bpc.request.getSession().setAttribute(NewApplicationConstant.CO_MAP, coMap);
                //control premises edit
                List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
                AppGrpPremisesEntityDto rfiPremises = appSubmissionService.getPremisesByAppNo(appNo);
                String rfiPremHci = NewApplicationHelper.getPremisesKey(rfiPremises);
                if (!IaisCommonUtils.isEmpty(appGrpPremisesDtos)) {
                    for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
                        appGrpPremisesDto.setFromDB(true);
                        appGrpPremisesDto.setExistingData(AppConsts.NO);
                        String premHci = NewApplicationHelper.getPremisesKey(appGrpPremisesDto);
                        if(rfiPremHci.equals(premHci)){
                            appGrpPremisesDto.setRfiCanEdit(true);
                        }
                        //clear ph id
                        List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodDtos = appGrpPremisesDto.getAppPremPhOpenPeriodList();
                        if(!IaisCommonUtils.isEmpty(appPremPhOpenPeriodDtos)){
                            for(AppPremPhOpenPeriodDto phDto:appPremPhOpenPeriodDtos){
                                phDto.setId(null);
                                phDto.setPremId(null);
                            }
                            appGrpPremisesDto.setAppPremPhOpenPeriodList(appPremPhOpenPeriodDtos);
                        }
                        //clear operation premId
                        List<AppPremisesOperationalUnitDto> operationalUnitDtos = appGrpPremisesDto.getAppPremisesOperationalUnitDtos();
                        if(!IaisCommonUtils.isEmpty(operationalUnitDtos)){
                            for(AppPremisesOperationalUnitDto operationalUnitDto:operationalUnitDtos){
                                operationalUnitDto.setId(null);
                                operationalUnitDto.setPremisesId(null);
                            }
                        }
                    }
                    appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtos);
                }
                //filter other premise info
                if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType()) && appGrpPremisesDtos.size() > 1){
                    ApplicationDto applicationDto = applicationFeClient.getApplicationDtoByAppNo(appNo).getEntity();
                    List<AppGrpPremisesDto> newAppGrpPremisesDtoList = IaisCommonUtils.genNewArrayList();
                    filtrationAppGrpPremisesDtos(applicationDto, appSubmissionDto, newAppGrpPremisesDtoList);
                    appSubmissionDto.setAppGrpPremisesDtoList(newAppGrpPremisesDtoList);
                    appSubmissionDto.setAppGrpPrimaryDocDtos(filterPrimaryDocs(appSubmissionDto.getAppGrpPrimaryDocDtos(), newAppGrpPremisesDtoList));
                }
                ParamUtil.setSessionAttr(bpc.request,APPSUBMISSIONDTO,appSubmissionDto);
            } else {
                ApplicationDto applicationDto = appSubmissionService.getMaxVersionApp(appNo);
                if (applicationDto != null) {
                    HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(applicationDto.getServiceId());
                    if (hcsaServiceDto != null) {
                        List<HcsaServiceDto> hcsaServiceDtoList = IaisCommonUtils.genNewArrayList();
                        hcsaServiceDtoList.add(hcsaServiceDto);
                        ParamUtil.setSessionAttr(bpc.request, RFI_REPLY_SVC_DTO, (Serializable) hcsaServiceDtoList);
                    }
                    bpc.request.setAttribute("APPLICATION_TYPE",applicationDto.getApplicationType());
                    String errMsg = MessageUtil.getMessageDesc("INBOX_ERR001");
                    jumpToAckPage(bpc, NewApplicationConstant.ACK_STATUS_ERROR, errMsg);
                }
            }
            ParamUtil.setSessionAttr(bpc.request, REQUESTINFORMATIONCONFIG, "test");
        }
        log.info(StringUtil.changeForLog("the do requestForInformationLoading end ...."));
    }

    private void svcRelatedInfoRFI(AppSubmissionDto appSubmissionDto, String appNo) {
        if (!ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())) {
            return;
        }
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfoDtoByServiceId(appSubmissionDto.getAppSvcRelatedInfoDtoList(),
                null, appNo);
        if (appSvcRelatedInfoDto == null) {
            return;
        }
        String serviceId = appSvcRelatedInfoDto.getServiceId();
        List<AppSvcRelatedInfoDto> otherList = getOtherAppSvcRelatedInfoDtos(appSubmissionDto.getAppSvcRelatedInfoDtoList(),
                serviceId, appNo);
        List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemesByServiceId = serviceConfigService.getHcsaServiceStepSchemesByServiceId(appSvcRelatedInfoDto.getServiceId());
        appSvcRelatedInfoDto.setHcsaServiceStepSchemeDtos(hcsaServiceStepSchemesByServiceId);
        if (otherList != null && !otherList.isEmpty()) {
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            appSvcRelatedInfoDtos.removeAll(otherList);
            clearOtherServiceSvcRelate(appSvcRelatedInfoDtos);
            appSubmissionDto.setAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtos);
        }
    }

    private void clearOtherServiceSvcRelate(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos) {
        List<String> serviceIdList = IaisCommonUtils.genNewArrayList();
        for (int i = appSvcRelatedInfoDtos.size() - 1; i>=0; i--){
            if (serviceIdList.contains(appSvcRelatedInfoDtos.get(i).getServiceId())){
                appSvcRelatedInfoDtos.remove(i);
            }else {
                serviceIdList.add(appSvcRelatedInfoDtos.get(i).getServiceId());
            }
        }
    }

    private void setAppSvcDisciplineAllocationDtoSlIndex(List<AppSvcPersonnelDto> appSvcPersonnelDtoList, List<AppSvcDisciplineAllocationDto> otherAppSvcDisciplineAllocationDtoList) {
        if (IaisCommonUtils.isEmpty(appSvcPersonnelDtoList) || IaisCommonUtils.isEmpty(otherAppSvcDisciplineAllocationDtoList)){
            return;
        }
        for (AppSvcDisciplineAllocationDto otherAppSvcDisciplineAllocationDto : otherAppSvcDisciplineAllocationDtoList) {
            for (AppSvcPersonnelDto appSvcPersonnelDto : appSvcPersonnelDtoList){
                if (StringUtil.isNotEmpty(otherAppSvcDisciplineAllocationDto.getSectionLeaderName()) &&
                        otherAppSvcDisciplineAllocationDto.getSectionLeaderName().equals(appSvcPersonnelDto.getName())) {
                    otherAppSvcDisciplineAllocationDto.setSlIndex(appSvcPersonnelDto.getIndexNo());
                }
            }
        }
    }

    private boolean isContainAppSvcPersonnelDto(List<AppSvcPersonnelDto> appSvcPersonnelDtoList, AppSvcPersonnelDto otherAppSvcPersonnelDto) {
        if (IaisCommonUtils.isEmpty(appSvcPersonnelDtoList) || otherAppSvcPersonnelDto == null){
            return false;
        }
        boolean isContain = false;
        for (AppSvcPersonnelDto appSvcPersonnelDto : appSvcPersonnelDtoList){
            if (StringUtil.isNotEmpty(otherAppSvcPersonnelDto.getName()) &&
                    otherAppSvcPersonnelDto.getName().equals(appSvcPersonnelDto.getName())) {
                isContain = true;
                break;
            }
        }
        return isContain;
    }

    private String getNewPsnIndexNo(List<PersonnelDto> srcPersonnels, List<PersonnelDto> tarPersonnels, String srcPsnIndexNo) {
        String psnIndexNo = srcPsnIndexNo;
        if (srcPersonnels == null || tarPersonnels == null) {
            return psnIndexNo;
        }
        Optional<PersonnelDto> any = srcPersonnels.stream()
                .filter(dto -> Objects.equals(srcPsnIndexNo, dto.getPsnIndexNo()))
                .findAny();
        if (any.isPresent()) {
            PersonnelDto p = any.get();
            psnIndexNo = tarPersonnels.stream()
                    .filter(dto -> Objects.equals(p.getPersonType(), dto.getPersonType())
                            && Objects.equals(p.getIdType(), dto.getIdType())
                            && Objects.equals(p.getIdNo(), dto.getIdNo())
                            && Objects.equals(p.getName(), dto.getName()))
                    .findAny()
                    .map(PersonnelDto::getPsnIndexNo)
                    .orElse(srcPsnIndexNo);
        }
        return psnIndexNo;
    }

    private boolean loadingServiceConfig(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do loadingServiceConfig start ...."));
        //loading the service
        List<String> serviceConfigIds = IaisCommonUtils.genNewArrayList();
        List<String> names = IaisCommonUtils.genNewArrayList();
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        if (appSubmissionDto != null) {
            // from draft,rfi
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtoList)) {
                for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtoList) {
                    if (!StringUtil.isEmpty(appSvcRelatedInfoDto.getServiceId())) {
                        serviceConfigIds.add(appSvcRelatedInfoDto.getServiceId());
                    }
                    //if get the data from licence, only have the serviceName
                    if (!StringUtil.isEmpty(appSvcRelatedInfoDto.getServiceName())) {
                        names.add(appSvcRelatedInfoDto.getServiceName());
                    }

                }
            }
        } else {
            List<String> licenceIds = (List<String>) ParamUtil.getSessionAttr(bpc.request, "licence");
            List<String> baseServiceIds = (List<String>) ParamUtil.getSessionAttr(bpc.request, "baseServiceChecked");
            List<String> specifiedServiceIds = (List<String>) ParamUtil.getSessionAttr(bpc.request, "specifiedServiceChecked");
            if (IaisCommonUtils.isEmpty(licenceIds)) {
                if (!IaisCommonUtils.isEmpty(baseServiceIds)) {
                    serviceConfigIds.addAll(baseServiceIds);
                }
                if (!IaisCommonUtils.isEmpty(specifiedServiceIds)) {
                    serviceConfigIds.addAll(specifiedServiceIds);
                }
            }
        }

        if (IaisCommonUtils.isEmpty(serviceConfigIds) && IaisCommonUtils.isEmpty(names)) {
            log.info(StringUtil.changeForLog("service id is empty"));
            String errMsg = "you have encountered some problems, please contact the administrator !!!";
            jumpToAckPage(bpc, NewApplicationConstant.ACK_STATUS_ERROR, errMsg);
            return false;
        }

        List<HcsaServiceDto> hcsaServiceDtoList = null;
        if (!serviceConfigIds.isEmpty()) {
            hcsaServiceDtoList = serviceConfigService.getHcsaServiceDtosById(serviceConfigIds);
        } else if (!names.isEmpty()) {
            hcsaServiceDtoList = serviceConfigService.getActiveHcsaSvcByNames(names);
        }
        if (hcsaServiceDtoList != null) {
            hcsaServiceDtoList = NewApplicationHelper.sortHcsaServiceDto(hcsaServiceDtoList);
        }
        ParamUtil.setSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST, (Serializable) hcsaServiceDtoList);
        log.info(StringUtil.changeForLog("the do loadingServiceConfig end ...."));
        return true;
    }

    private static AppSubmissionDto getAppSubmissionDto(HttpServletRequest request) {
        return NewApplicationHelper.getAppSubmissionDto(request);
    }

    private void loadingNewAppInfo(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do loadingSpecifiedInfo start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = (List<AppSvcRelatedInfoDto>) ParamUtil.getSessionAttr(bpc.request, ServiceMenuDelegator.APP_SVC_RELATED_INFO_LIST);
        AppSelectSvcDto appSelectSvcDto = (AppSelectSvcDto) ParamUtil.getSessionAttr(bpc.request, ServiceMenuDelegator.APP_SELECT_SERVICE);
        if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos) && appSubmissionDto == null && appSelectSvcDto != null) {
            String entryType = ParamUtil.getString(bpc.request,"entryType");
            if(!StringUtil.isEmpty(entryType) && entryType.equals("assessment")){
                ParamUtil.setSessionAttr(bpc.request,ASSESSMENTCONFIG,"test");
            }
            appSubmissionDto = new AppSubmissionDto();
            appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
            appSubmissionDto.setAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtos);
            String premisesId = "";
            for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                String premId = appSvcRelatedInfoDto.getLicPremisesId();
                if (!StringUtil.isEmpty(premId) && !"-1".equals(premId)) {
                    premisesId = premId;
                    break;
                }
            }
            if (!StringUtil.isEmpty(premisesId)) {
                List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionService.getLicPremisesInfo(premisesId);
                /*for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
                    appGrpPremisesDto.setPremisesSelect(NewApplicationConstant.NEW_PREMISES);
                }*/
                appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtos);
            } else {
                List<AppGrpPremisesDto> appGrpPremisesDtos = IaisCommonUtils.genNewArrayList();
                AppGrpPremisesDto appGrpPremisesDto = new AppGrpPremisesDto();
                appGrpPremisesDtos.add(appGrpPremisesDto);
                appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtos);
            }
            ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
            ParamUtil.setSessionAttr(bpc.request,HcsaFileAjaxController.GLOBAL_MAX_INDEX_SESSION_ATTR,0);
        }
        log.info(StringUtil.changeForLog("the do loadingSpecifiedInfo start ...."));
    }

    private static List<AppGrpPremisesDto> getAppGrpPremisesDtos(AppSubmissionDto appSubmissionDto) {
        List<AppGrpPremisesDto> appGrpPremisesDtos = IaisCommonUtils.genNewArrayList();
        if (appSubmissionDto != null) {
            appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
        }
        return appGrpPremisesDtos;
    }

    private static void jumpToAckPage(BaseProcessClass bpc, String ackStatus, String errorMsg) {
        String actionType = (String) ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if (StringUtil.isEmpty(actionType)) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "errorAck");
            if (NewApplicationConstant.ACK_STATUS_ERROR.equals(ackStatus)) {
                ParamUtil.setRequestAttr(bpc.request, ACKSTATUS, "error");
                ParamUtil.setRequestAttr(bpc.request, ACKMESSAGE, errorMsg);
            }
        }
    }

    private void setPsnDroTo(AppSubmissionDto appSubmissionDto, BaseProcessClass bpc) {
        Map<String, AppSvcPrincipalOfficersDto> personMap = (Map<String, AppSvcPrincipalOfficersDto>) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.PERSONSELECTMAP);
        String personMapStr = JsonUtil.parseToJson(personMap);
        appSubmissionDto.setDropDownPsnMapStr(personMapStr);
    }

    private Integer getAppGrpPrimaryDocVersion(String configDocId,List<AppGrpPrimaryDocDto> oldDocs,boolean isRfi,String md5Code,String appGrpId,String appNo,String appType,int seqNum,String dupForPrem){
        log.info(StringUtil.changeForLog("AppGrpPrimaryDocVersion start..."));
        Integer version = 1;
        if(StringUtil.isEmpty(configDocId) || IaisCommonUtils.isEmpty(oldDocs) || StringUtil.isEmpty(md5Code)){
            return version;
        }
        log.info(StringUtil.changeForLog("isRfi:"+isRfi));
        log.info(StringUtil.changeForLog("appType:"+appType));
        log.info(StringUtil.changeForLog("seqNum:"+seqNum));
        if(isRfi){
            log.info(StringUtil.changeForLog("rfi appNo:"+appNo));
            boolean canFound = false;
            for(AppGrpPrimaryDocDto appGrpPrimaryDocDto:oldDocs){
                Integer oldVersion = appGrpPrimaryDocDto.getVersion();
                if(configDocId.equals(appGrpPrimaryDocDto.getSvcComDocId()) && seqNum == appGrpPrimaryDocDto.getSeqNum()){
                    canFound = true;
                    if(MessageDigest.isEqual(md5Code.getBytes(StandardCharsets.UTF_8),
                            appGrpPrimaryDocDto.getMd5Code().getBytes(StandardCharsets.UTF_8))){
                        if(!StringUtil.isEmpty(oldVersion)){
                            version = oldVersion;
                        }
                    }else{
                        version = getVersion(appGrpId,configDocId,appNo,appType,dupForPrem,seqNum);
                    }
                    break;
                }
            }
            if(!canFound){
                //last doc is null new rfi not use app no
                version = getVersion(appGrpId,configDocId,appNo,appType,dupForPrem,seqNum);
            }
        }
        log.info(StringUtil.changeForLog("AppGrpPrimaryDocVersion end..."));
        return version;
    }

    private Integer getVersion(String appGrpId,String configDocId,String appNo,String appType,String dupForPrem,int seqNum){
        Integer version = 1;

        //common doc
        if("0".equals(dupForPrem)){
            if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)){
                AppGrpPrimaryDocDto maxVersionDocDto = appSubmissionService.getMaxVersionPrimaryComDoc(appGrpId,configDocId,String.valueOf(seqNum));
                Integer maxVersion = maxVersionDocDto.getVersion();
                String fileRepoId = maxVersionDocDto.getFileRepoId();
                if(!StringUtil.isEmpty(maxVersion) &&  !StringUtil.isEmpty(fileRepoId)){
                    version = maxVersionDocDto.getVersion() + 1;
                }
            }else{
                AppSvcDocDto maxVersionDocDto = appSubmissionService.getMaxVersionSvcComDoc(appGrpId,configDocId,String.valueOf(seqNum));
                if(!StringUtil.isEmpty(maxVersionDocDto.getVersion()) && !StringUtil.isEmpty(maxVersionDocDto.getFileRepoId())){
                    version = maxVersionDocDto.getVersion() + 1;
                }
            }

        }else if("1".equals(dupForPrem)){
            if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)){
                AppGrpPrimaryDocDto maxVersionDocDto = appSubmissionService.getMaxVersionPrimarySpecDoc(appGrpId,configDocId,appNo,String.valueOf(seqNum));
                if(!StringUtil.isEmpty(maxVersionDocDto.getVersion()) && !StringUtil.isEmpty(maxVersionDocDto.getFileRepoId())){
                    version = maxVersionDocDto.getVersion() + 1;
                }
            }else{
                AppSvcDocDto searchDto = new AppSvcDocDto();
                searchDto.setAppGrpId(appGrpId);
                searchDto.setSvcDocId(configDocId);
                searchDto.setSeqNum(seqNum);
                AppSvcDocDto maxVersionDocDto = appSubmissionService.getMaxVersionSvcSpecDoc(searchDto,appNo);
                if(!StringUtil.isEmpty(maxVersionDocDto.getVersion()) && !StringUtil.isEmpty(maxVersionDocDto.getFileRepoId())){
                    version = maxVersionDocDto.getVersion() + 1;
                }
            }
        }
        return version;
    }

    private static String  genPageName(Object prefix,String name,Object suffix){
        return prefix + name + suffix;
    }

    private static AppGrpPrimaryDocDto getAppGrpPrimaryDocByConfigIdAndSeqNum(List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos,String configId,int seqNum,String premVal,String premType){
        log.debug(StringUtil.changeForLog("getAppGrpPrimaryDocByConfigIdAndSeqNum start..."));
        AppGrpPrimaryDocDto appGrpPrimaryDocDto = null;
        if(!IaisCommonUtils.isEmpty(appGrpPrimaryDocDtos)){
            log.debug("configId is {}", configId);
            log.debug("seqNum is {}", seqNum);
            log.debug("premIndex is {}", premVal);
            for(AppGrpPrimaryDocDto appGrpPrimaryDocDto1:appGrpPrimaryDocDtos){
                String currPremVal = "";
                String currPremType = "";
                if(!StringUtil.isEmpty(appGrpPrimaryDocDto1.getPremisessName())){
                    currPremVal = appGrpPrimaryDocDto1.getPremisessName();
                }
                if(!StringUtil.isEmpty(appGrpPrimaryDocDto1.getPremisessType())){
                    currPremType = appGrpPrimaryDocDto1.getPremisessType();
                }
                if(!StringUtil.isEmpty(appGrpPrimaryDocDto1.getFileRepoId())
                        && configId.equals(appGrpPrimaryDocDto1.getSvcComDocId())
                        && seqNum == appGrpPrimaryDocDto1.getSeqNum()
                        && premVal.equals(currPremVal)
                        && premType.equals(currPremType)){
                    appGrpPrimaryDocDto = (AppGrpPrimaryDocDto) CopyUtil.copyMutableObject(appGrpPrimaryDocDto1);
                    break;
                }
            }
        }
        return appGrpPrimaryDocDto;
    }


    private void genPrimaryDoc(Map<String, File> fileMap, String docKey, HcsaSvcDocConfigDto hcsaSvcDocConfigDto,
            Map<String, File> saveFileMap, List<AppGrpPrimaryDocDto> currDocDtoList,
            List<AppGrpPrimaryDocDto> newAppGrpPrimaryDocDtoList,
            String premVal, String premType, boolean isRfi, List<AppGrpPrimaryDocDto> oldPrimaryDotList, String appGrpId, String appNo,
            String appType, String dupForPrem) {
        if (fileMap != null) {
            Set<Integer> nums = IaisCommonUtils.genNewHashSet();
            for (Map.Entry<String, File> entry : fileMap.entrySet()) {
                String k = entry.getKey();
                File v = entry.getValue();
                int index = k.indexOf(docKey);
                String seqNumStr = k.substring(index + docKey.length());
                int seqNum = -1;
                try {
                    seqNum = Integer.parseInt(seqNumStr);
                } catch (Exception e) {
                    log.error(StringUtil.changeForLog("doc seq num can not parse to int"));
                }
                while (!nums.add(seqNum)) {
                    seqNum++;
                }
                AppGrpPrimaryDocDto primaryDocDto = new AppGrpPrimaryDocDto();
                if (v != null) {
                    primaryDocDto.setSvcComDocId(hcsaSvcDocConfigDto.getId());
                    primaryDocDto.setSvcComDocName(hcsaSvcDocConfigDto.getDocTitle());
                    primaryDocDto.setDocName(v.getName());
                    primaryDocDto.setRealDocSize(v.length());
                    long size = v.length() / 1024;
                    primaryDocDto.setDocSize(Integer.valueOf(String.valueOf(size)));
                    String md5Code = SingeFileUtil.getInstance().getFileMd5(v);
                    primaryDocDto.setMd5Code(md5Code);
                    //if  common ==> set null
                    primaryDocDto.setPremisessName(premVal);
                    primaryDocDto.setPremisessType(premType);
                    primaryDocDto.setSeqNum(seqNum);
                    primaryDocDto.setVersion(
                            getAppGrpPrimaryDocVersion(hcsaSvcDocConfigDto.getId(), oldPrimaryDotList, isRfi, md5Code, appGrpId, appNo,
                                    appType, seqNum, dupForPrem));
                    saveFileMap.put(premVal + hcsaSvcDocConfigDto.getId() + seqNum, v);
                } else {
                    primaryDocDto = getAppGrpPrimaryDocByConfigIdAndSeqNum(currDocDtoList, hcsaSvcDocConfigDto.getId(), seqNum,
                            premVal, premType);
                }
                //the data is retrieved from the DTO a second time
                fileMap.put(k, null);
                if (primaryDocDto != null) {
                    newAppGrpPrimaryDocDtoList.add(primaryDocDto);
                }
            }
        }
    }

    private void saveFileAndSetFileId(List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList,Map<String,File> saveFileMap) {
        Map<String, File> passValidateFileMap = IaisCommonUtils.genNewLinkedHashMap();
        for (AppGrpPrimaryDocDto primaryDocDto : appGrpPrimaryDocDtoList) {
            if (primaryDocDto.isPassValidate()) {
                String premIndexNo = primaryDocDto.getPremisessName();
                if (StringUtil.isEmpty(premIndexNo)) {
                    premIndexNo = "";
                }
                String fileMapKey = premIndexNo + primaryDocDto.getSvcComDocId() + primaryDocDto.getSeqNum();
                File file = saveFileMap.get(fileMapKey);
                if (file != null) {
                    passValidateFileMap.put(fileMapKey, file);
                }
            }
        }
        log.info(StringUtil.changeForLog("Primary Doc size: " + appGrpPrimaryDocDtoList.size()));
        if (saveFileMap.size() != appGrpPrimaryDocDtoList.size()) {
            log.info(StringUtil.changeForLog("Save file size: " + saveFileMap.size()));
        }
        if (passValidateFileMap.size() > 0) {
            List<File> fileList = new ArrayList<>(passValidateFileMap.values());
            List<String> fileRepoIdList = appSubmissionService.saveFileList(fileList);
            int fileRepo = fileRepoIdList.size();
            log.info(StringUtil.changeForLog("File Repo size: " + fileRepo));
            int i = 0;
            for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : appGrpPrimaryDocDtoList) {
                String premIndexNo = appGrpPrimaryDocDto.getPremisessName();
                if (StringUtil.isEmpty(premIndexNo)) {
                    premIndexNo = "";
                }
                String saveFileMapKey = premIndexNo + appGrpPrimaryDocDto.getSvcComDocId() + appGrpPrimaryDocDto.getSeqNum();
                File file = passValidateFileMap.get(saveFileMapKey);
                if (file != null) {
                    appGrpPrimaryDocDto.setFileRepoId(fileRepoIdList.get(i));
                    i++;
                }
            }
        }

    }

    private AppGrpPrimaryDocDto getAppGrpPrimaryDocByConfigIdAndPremIndex(List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos,String config,String premIndex){
        AppGrpPrimaryDocDto appGrpPrimaryDocDto = null;
        if(!IaisCommonUtils.isEmpty(appGrpPrimaryDocDtos)){
            for(AppGrpPrimaryDocDto appGrpPrimaryDocDto1:appGrpPrimaryDocDtos){
                String currPremVal = appGrpPrimaryDocDto1.getPremisessName();
                if(StringUtil.isEmpty(currPremVal)){
                    currPremVal = "";
                }
                if(config.equals(appGrpPrimaryDocDto1.getSvcComDocId()) && premIndex.equals(currPremVal)){
                    appGrpPrimaryDocDto = appGrpPrimaryDocDto1;
                    break;
                }
            }
        }
        return appGrpPrimaryDocDto;
    }
}


