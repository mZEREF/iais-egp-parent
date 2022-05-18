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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessHciDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppSpecifiedLicDto;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
                            request);
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
                HashMap<String, String> coMap = (HashMap<String, String>) ParamUtil.getSessionAttr(request, HcsaAppConst.CO_MAP);
                coMap.put(HcsaAppConst.SECTION_LICENSEE, HcsaAppConst.SECTION_LICENSEE);
                coMap.put(HcsaAppConst.SECTION_PREMISES, HcsaAppConst.SECTION_PREMISES);
                coMap.put(HcsaAppConst.SECTION_DOCUMENT, HcsaAppConst.SECTION_PREMISES);
                coMap.put(HcsaAppConst.SECTION_SVCINFO, HcsaAppConst.SECTION_PREMISES);
                coMap.put(HcsaAppConst.SECTION_PREVIEW, HcsaAppConst.SECTION_PREVIEW);
                ParamUtil.setSessionAttr(request, HcsaAppConst.CO_MAP, coMap);
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
            appSubmissionDto.setLicenceId(ApplicationHelper.getLicenseeId(request));
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

    // This is a common method, only do that get data from page
    public static SubLicenseeDto getSubLicenseeDtoDetailFromPage(HttpServletRequest request) {
        String idType = ParamUtil.getString(request, "idType");
        String idNumber = ParamUtil.getString(request, "idNumber");
        String licenseeName = ParamUtil.getString(request, "licenseeName");
        String postalCode = ParamUtil.getString(request, "postalCode");
        String addrType = ParamUtil.getString(request, "addrType");
        String blkNo = ParamUtil.getString(request, "blkNo");
        String floorNo = ParamUtil.getString(request, "floorNo");
        String unitNo = ParamUtil.getString(request, "unitNo");
        String streetName = ParamUtil.getString(request, "streetName");
        String buildingName = ParamUtil.getString(request, "buildingName");
        String telephoneNo = ParamUtil.getString(request, "telephoneNo");
        String emailAddr = ParamUtil.getString(request, "emailAddr");
        String nationality = ParamUtil.getString(request, "nationality");

        SubLicenseeDto dto = new SubLicenseeDto();
        dto.setIdType(idType);
        dto.setIdNumber(StringUtil.toUpperCase(idNumber));
        dto.setLicenseeName(licenseeName);
        dto.setPostalCode(postalCode);
        dto.setAddrType(addrType);
        dto.setBlkNo(blkNo);
        dto.setFloorNo(floorNo);
        dto.setUnitNo(unitNo);
        dto.setStreetName(streetName);
        dto.setBuildingName(buildingName);
        dto.setTelephoneNo(telephoneNo);
        dto.setEmailAddr(emailAddr);
        dto.setNationality(nationality);
        return dto;
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
        // clear session
        DealSessionUtil.clearSession(bpc.request);
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
                filtrationAppGrpPremisesDtos(applicationDto.getApplicationNo(), appSubmissionDto, null, newPremisesDtos);
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
        map.put(HcsaAppConst.MAP_KEY_STATUS, status);
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

    /*private void initAction(String action, Map<String, String> errorMap, AppSubmissionDto appSubmissionDto,
            HttpServletRequest request) {
        if (IaisCommonUtils.isNotEmpty(errorMap)) {
            boolean isRfi = ApplicationHelper.checkIsRfi(request);
            if (isRfi) {
                ApplicationHelper.setAudiErrMap(isRfi, appSubmissionDto.getAppType(), errorMap, appSubmissionDto.getRfiAppNo(),
                        appSubmissionDto.getLicenceNo());
            }
        }
        ParamUtil.setRequestAttr(request, "Msg", errorMap);
        ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, action);
        ParamUtil.setRequestAttr(request, "isrfiSuccess", "N");
    }

    private String getCurrentRfiAppId(AppSubmissionDto oldAppSubmissionDto) {
        if (oldAppSubmissionDto == null || IaisCommonUtils.isEmpty(oldAppSubmissionDto.getAppSvcRelatedInfoDtoList())) {
            return null;
        }
        for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : oldAppSubmissionDto.getAppSvcRelatedInfoDtoList()) {
            if (Objects.equals(oldAppSubmissionDto.getRfiAppNo(), appSvcRelatedInfoDto.getAppNo())) {
                return appSvcRelatedInfoDto.getAppId();
            }
        }
        return null;
    }

    private void resetRelatedInfoRFI(AppSvcRelatedInfoDto appSvcRelatedInfoDto, AppSubmissionDto appSubmissionDto, List<AppGrpPremisesDto> appGrpPremisesDtoList) {
        if (!ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())) {
            return;
        }
        if (appSvcRelatedInfoDto == null) {
            return;
        }
        List<AppSvcRelatedInfoDto> otherList = getOtherAppSvcRelatedInfoDtos(appSubmissionDto.getAppSvcRelatedInfoDtoList(),
                appSvcRelatedInfoDto.getServiceId(), appSvcRelatedInfoDto.getAppNo());
        List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemesByServiceId = serviceConfigService.getHcsaServiceStepSchemesByServiceId(appSvcRelatedInfoDto.getServiceId());
        appSvcRelatedInfoDto.setHcsaServiceStepSchemeDtos(hcsaServiceStepSchemesByServiceId);
        if (otherList != null && !otherList.isEmpty()) {
            otherList.forEach(dto -> {
                List<AppSvcPersonnelDto> appSvcPersonnelDtoList = appSvcRelatedInfoDto.getAppSvcSectionLeaderList();
                List<AppSvcPersonnelDto> otherAppSvcPersonnelDtoList = dto.getAppSvcSectionLeaderList();
                if (appSvcPersonnelDtoList != null && otherAppSvcPersonnelDtoList != null) {
                    for (AppSvcPersonnelDto otherAppSvcPersonnelDto : otherAppSvcPersonnelDtoList){
                        if (!isContainAppSvcPersonnelDto(appSvcPersonnelDtoList, otherAppSvcPersonnelDto)){
                            appSvcPersonnelDtoList.add(otherAppSvcPersonnelDto);
                        }
                    }
                }

                List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList = appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
                if (appSvcLaboratoryDisciplinesDtoList != null && dto.getAppSvcLaboratoryDisciplinesDtoList() != null) {
                    appSvcLaboratoryDisciplinesDtoList.addAll(dto.getAppSvcLaboratoryDisciplinesDtoList());
                    appSvcRelatedInfoDto.setAppSvcLaboratoryDisciplinesDtoList(appSvcLaboratoryDisciplinesDtoList);
                }
                List<AppSvcDisciplineAllocationDto> appSvcDisciplineAllocationDtoList = appSvcRelatedInfoDto.getAppSvcDisciplineAllocationDtoList();
                List<AppSvcDisciplineAllocationDto> otherAppSvcDisciplineAllocationDtoList = dto.getAppSvcDisciplineAllocationDtoList();
                if (appSvcDisciplineAllocationDtoList != null && otherAppSvcDisciplineAllocationDtoList != null) {
                    setAppSvcDisciplineAllocationDtoSlIndex(appSvcPersonnelDtoList, otherAppSvcDisciplineAllocationDtoList);
                    appSvcDisciplineAllocationDtoList.addAll(otherAppSvcDisciplineAllocationDtoList);
                    appSvcRelatedInfoDto.setAppSvcDisciplineAllocationDtoList(appSvcDisciplineAllocationDtoList);
                }

                List<AppSvcDocDto> appSvcDocDtoLit = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
                List<AppSvcDocDto> otherAppSvcDocDtoLit = dto.getAppSvcDocDtoLit();
                if (otherAppSvcDocDtoLit != null && appSvcDocDtoLit != null) {
                    otherAppSvcDocDtoLit.forEach(doc -> {
                        if (doc.getAppSvcPersonId() != null || doc.getAppGrpPersonId() != null) {
                            doc.setPsnIndexNo(getNewPsnIndexNo(dto.getPersonnels(), appSvcRelatedInfoDto.getPersonnels(), doc.getPsnIndexNo()));
                            appSvcDocDtoLit.add(doc);
                        }
                    });
                    appSvcRelatedInfoDto.setAppSvcDocDtoLit(appSvcDocDtoLit);
                }
                // check personnels
                List<PersonnelDto> personnels = appSvcRelatedInfoDto.getPersonnels();
                if (personnels == null) {
                    personnels = IaisCommonUtils.genNewArrayList();
                }
                if (dto.getPersonnels() != null) {
                    personnels.addAll(dto.getPersonnels());
                }
                appSvcRelatedInfoDto.setPersonnels(personnels);
                //set Laboratory Disciplines Info
                String currentSvcId = dto.getServiceId();
                List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos = null;
                if (!StringUtil.isEmpty(currentSvcId)) {
                    hcsaSvcSubtypeOrSubsumedDtos = serviceConfigService.loadLaboratoryDisciplines(currentSvcId);
                }
                if (!IaisCommonUtils.isEmpty(hcsaSvcSubtypeOrSubsumedDtos)) {
                    ApplicationHelper.setLaboratoryDisciplinesInfo(appGrpPremisesDtoList, appSvcRelatedInfoDto, hcsaSvcSubtypeOrSubsumedDtos);
                }
            });
        }
    }*/

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
    public void prepareJump(BaseProcessClass bpc) throws Exception {
        super.prepareJump(bpc);
    }

    private static AppSubmissionDto getAppSubmissionDto(HttpServletRequest request) {
        return ApplicationHelper.getAppSubmissionDto(request);
    }

    private void setPsnDroTo(AppSubmissionDto appSubmissionDto, BaseProcessClass bpc) {
        Map<String, AppSvcPrincipalOfficersDto> personMap = (Map<String, AppSvcPrincipalOfficersDto>) ParamUtil.getSessionAttr(
                bpc.request, HcsaAppConst.PERSONSELECTMAP);
        String personMapStr = JsonUtil.parseToJson(personMap);
        appSubmissionDto.setDropDownPsnMapStr(personMapStr);
    }

}


