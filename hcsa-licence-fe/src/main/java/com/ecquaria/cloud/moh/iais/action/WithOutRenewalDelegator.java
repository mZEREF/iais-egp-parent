package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.api.config.GatewayConstants;
import com.ecquaria.cloud.moh.iais.api.services.GatewayAPI;
import com.ecquaria.cloud.moh.iais.api.services.GatewayNetsAPI;
import com.ecquaria.cloud.moh.iais.api.services.GatewayStripeAPI;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppFeeDetailsDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppSvcPersonAndExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremPhOpenPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDisciplineAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationSubDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.RenewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.AmendmentFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicKeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterInboxUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.RfcConst;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.WithOutRenewalService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationFeClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationLienceseeClient;
import com.ecquaria.cloud.moh.iais.validation.PaymentValidate;
import com.ecquaria.sz.commons.util.MsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.util.CopyUtil;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * AutoRenewalDelegator
 *
 * @author caijing
 * @date 2020/1/6
 */

@Delegator("withOutRenewalDelegator")
@Slf4j //NOSONAR
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
    private static final String PREFIXTITLE = "prefixTitle";
    private static final String LOADING_DRAFT = "loadingDraft";
    @Autowired
    WithOutRenewalService outRenewalService;

    @Autowired
    AppSubmissionService appSubmissionService;

    @Autowired
    ServiceConfigService serviceConfigService;

    @Autowired
    NewApplicationDelegator newApplicationDelegator;

    @Autowired
    RequestForChangeService requestForChangeService;
    @Autowired
    private GenerateIdClient generateIdClient;
    @Autowired
    private EventBusHelper eventBusHelper;
    @Autowired
    private OrganizationLienceseeClient organizationLienceseeClient;
    @Autowired
    private ApplicationFeClient applicationFeClient;

    @Value("${iais.system.one.address}")
    private String systemAddressOne;

    @Autowired
    private NotificationHelper notificationHelper;

    @Autowired
    private SystemParamConfig systemParamConfig;

    public void start(BaseProcessClass bpc) throws CloneNotSupportedException {
        log.info("**** the non auto renwal  start ******");
        HcsaServiceCacheHelper.flushServiceMapping();
        String draftNo = ParamUtil.getMaskedString(bpc.request, "DraftNumber");
        //init session
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_RENEW, AuditTrailConsts.FUNCTION_RENEW);
        ParamUtil.setSessionAttr(bpc.request, RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR, null);
        ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.APPSUBMISSIONDTO, null);
        ParamUtil.setSessionAttr(bpc.request, "totalStr", null);
        ParamUtil.setSessionAttr(bpc.request, "totalAmount", null);
        ParamUtil.setSessionAttr(bpc.request, "userAgreement", null);
        ParamUtil.setSessionAttr(bpc.request, PREFIXTITLE, "renewing");
        ParamUtil.setSessionAttr(bpc.request,"paymentMessageValidateMessage",null);
        ParamUtil.setSessionAttr(bpc.request, LOADING_DRAFT, null);
        ParamUtil.setSessionAttr(bpc.request, "oldAppSubmissionDto", null);
        HashMap<String, String> coMap = new HashMap<>(4);
        coMap.put("premises", "");
        coMap.put("document", "");
        coMap.put("information", "");
        coMap.put("previewli", "");
        bpc.request.getSession().setAttribute("coMap", coMap);
        ParamUtil.setSessionAttr(bpc.request,NewApplicationDelegator.CURR_ORG_USER_ACCOUNT, null);
        ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.LICPERSONSELECTMAP, null);
        //inbox draft number

        //init page value
        //instructions
        ParamUtil.setRequestAttr(bpc.request, "page_value", PAGE1);
        String licenceId = ParamUtil.getMaskedString(bpc.request, "licenceId");
        //init data
        List<String> licenceIDList = (List<String>) ParamUtil.getSessionAttr(bpc.request, RenewalConstants.WITHOUT_RENEWAL_LIC_ID_LIST_ATTR);
        if (licenceIDList == null) {
            licenceIDList = IaisCommonUtils.genNewArrayList();
        }
        if (licenceId != null) {
            licenceIDList = new ArrayList<>(1);
            licenceIDList.add(licenceId);
        }
        List<AppSubmissionDto> appSubmissionDtoList = IaisCommonUtils.genNewArrayList();
        if (StringUtil.isEmpty(draftNo)) {
            appSubmissionDtoList = outRenewalService.getAppSubmissionDtos(licenceIDList);
            if (!IaisCommonUtils.isEmpty(appSubmissionDtoList) && appSubmissionDtoList.size() == 1) {
                log.info(StringUtil.changeForLog("appSubmissionDtoList size:" + appSubmissionDtoList.size()));
                appSubmissionDtoList.get(0).setOneLicDoRenew(true);
            }
            log.info("can not find licence id for without renewal");
            ParamUtil.setSessionAttr(bpc.request, "backUrl", "initLic");
        } else {
            AppSubmissionDto appSubmissionDtoDraft = serviceConfigService.getAppSubmissionDtoDraft(draftNo);
            ParamUtil.setSessionAttr(bpc.request, LOADING_DRAFT, "test");
            appSubmissionDtoDraft.setOneLicDoRenew(true);
            appSubmissionDtoList.add(appSubmissionDtoDraft);
            licenceIDList = new ArrayList<>(1);
            licenceIDList.add(appSubmissionDtoDraft.getLicenceId());
            ParamUtil.setSessionAttr(bpc.request, "backUrl", "initApp");
            loadCoMap(bpc, appSubmissionDtoDraft);
        }

        //get licensee ID
        InterInboxUserDto interInboxUserDto = (InterInboxUserDto) ParamUtil.getSessionAttr(bpc.request, "INTER_INBOX_USER_INFO");
        int index = 0;
        String firstSvcName = "";
        List<String> serviceNameTitleList = IaisCommonUtils.genNewArrayList();
        List<String> serviceNameList = IaisCommonUtils.genNewArrayList();

        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = IaisCommonUtils.genNewArrayList();
        List<Map<String, List<AppSvcDisciplineAllocationDto>>> reloadDisciplineAllocationMapList = IaisCommonUtils.genNewArrayList();
        List<List<AppSvcPrincipalOfficersDto>> principalOfficersDtosList = IaisCommonUtils.genNewArrayList();
        List<List<AppSvcPrincipalOfficersDto>> deputyPrincipalOfficersDtosList = IaisCommonUtils.genNewArrayList();
        AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
        List<String> serviceNames = IaisCommonUtils.genNewArrayList();
        for (AppSubmissionDto appSubmissionDto : appSubmissionDtoList) {
            List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
            if (!IaisCommonUtils.isEmpty(appGrpPremisesDtos)) {
                for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
                    NewApplicationHelper.setWrkTime(appGrpPremisesDto);
                }
            }
            Map<String, AppSvcPersonAndExtDto> licPersonMap = IaisCommonUtils.genNewHashMap();
            if(interInboxUserDto != null){
                //user account
                List<FeUserDto> feUserDtos = requestForChangeService.getFeUserDtoByLicenseeId(interInboxUserDto.getLicenseeId());
                ParamUtil.setSessionAttr(bpc.request,NewApplicationDelegator.CURR_ORG_USER_ACCOUNT, (Serializable) feUserDtos);
                //existing person
                List<PersonnelListQueryDto> licPersonList = requestForChangeService.getLicencePersonnelListQueryDto(interInboxUserDto.getLicenseeId());
                licPersonMap = NewApplicationHelper.getLicPsnIntoSelMap(feUserDtos,licPersonList,licPersonMap);
            }
            ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.LICPERSONSELECTMAP, (Serializable) licPersonMap);

            if (!appSubmissionDto.getAppSvcRelatedInfoDtoList().isEmpty()) {
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
                List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos = serviceConfigService.loadLaboratoryDisciplines(svcId);
//                NewApplicationHelper.setLaboratoryDisciplinesInfo(appSubmissionDto, hcsaSvcSubtypeOrSubsumedDtos);
                //use new config id
                appSubmissionService.changeSvcScopeIdByConfigName(hcsaSvcSubtypeOrSubsumedDtos,appSubmissionDto);
                Map<String, List<AppSvcDisciplineAllocationDto>> reloadDisciplineAllocationMap = NewApplicationHelper.getDisciplineAllocationDtoList(appSubmissionDto, svcId);
                reloadDisciplineAllocationMapList.add(reloadDisciplineAllocationMap);
                //set svc step
                List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemesByServiceId = serviceConfigService.getHcsaServiceStepSchemesByServiceId(svcId);
                appSvcRelatedInfoDto.setHcsaServiceStepSchemeDtos(hcsaServiceStepSchemesByServiceId);
                //set doc info
                List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionDto.getAppGrpPrimaryDocDtos();
                List<HcsaSvcDocConfigDto> primaryDocConfig = serviceConfigService.getAllHcsaSvcDocs(null);
                boolean isRfi = false;
                //rfc/renew for primary doc
                List<AppGrpPrimaryDocDto> newGrpPrimaryDocList = appSubmissionService.syncPrimaryDoc(ApplicationConsts.APPLICATION_TYPE_RENEWAL,isRfi,appGrpPrimaryDocDtos,primaryDocConfig);
                appSubmissionDto.setAppGrpPrimaryDocDtos(newGrpPrimaryDocList);
                if (!StringUtil.isEmpty(svcId)) {
                    List<AppSvcDocDto> appSvcDocDtos = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
                    List<HcsaSvcDocConfigDto> svcDocConfig = serviceConfigService.getAllHcsaSvcDocs(svcId);
                    NewApplicationHelper.setDocInfo(null, appSvcDocDtos, null, svcDocConfig);
                }

                List<AppSvcPrincipalOfficersDto> appSvcCgoDtos = NewApplicationHelper.transferCgoToPsnDtoList(appSvcRelatedInfoDto.getAppSvcCgoDtoList());
                String svcCode = appSvcRelatedInfoDto.getServiceCode();
                NewApplicationHelper.initSetPsnIntoSelMap(licPersonMap, appSvcCgoDtos, svcCode);
                //reset dto
                List<AppSvcCgoDto> newCgoDtoList = IaisCommonUtils.genNewArrayList();
                for (AppSvcPrincipalOfficersDto item : appSvcCgoDtos) {
                    newCgoDtoList.add(MiscUtil.transferEntityDto(item, AppSvcCgoDto.class));
                }
                appSvcRelatedInfoDto.setAppSvcCgoDtoList(newCgoDtoList);
                NewApplicationHelper.initSetPsnIntoSelMap(licPersonMap, appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList(), svcCode);
                NewApplicationHelper.initSetPsnIntoSelMap(licPersonMap, appSvcRelatedInfoDto.getAppSvcMedAlertPersonList(), svcCode);


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

                if (!StringUtil.isEmpty(serviceName)) {
                    if (index == 0) {
                        firstSvcName = serviceName;
                        index++;
                        ParamUtil.setSessionAttr(bpc.request, "AppSubmissionDto", appSubmissionDto);
                    } else {
                        serviceNameTitleList.add(serviceName);
                    }
                    serviceNameList.add(serviceName);
                }
                appSubmissionDto.setServiceName(serviceName);
                if (licenceIDList.size() == 1) {
                    appEditSelectDto.setPremisesEdit(true);
                    appEditSelectDto.setDocEdit(true);
                    appEditSelectDto.setServiceEdit(true);
                    ParamUtil.setSessionAttr(bpc.request, "isSingle", "Y");
                } else {
                    ParamUtil.setSessionAttr(bpc.request, "isSingle", "N");
                }
                appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
            }
            appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_RENEWAL);
            appSubmissionDto.setStatus(ApplicationConsts.APPLICATION_STATUS_RENEWAL);
            //set licensee ID
            String licenseeId = "";
            if(interInboxUserDto != null) {
                licenseeId = interInboxUserDto.getLicenseeId();
            }
            appSubmissionDto.setLicenseeId(licenseeId);
            LicenseeDto licenseeDto = organizationLienceseeClient.getLicenseeById(licenseeId).getEntity();
            String licenseeName = "-";
            if (licenseeDto != null) {
                licenseeName = licenseeDto.getName();
                ParamUtil.setSessionAttr(bpc.request, "licenseeName", licenseeName);
            }

            String groupNumber = appSubmissionService.getGroupNo(ApplicationConsts.APPLICATION_TYPE_RENEWAL);

            log.info(StringUtil.changeForLog("without renewal group number =====>" + groupNumber));
            if (StringUtil.isEmpty(draftNo)) {
                String draftNumber = appSubmissionService.getDraftNo(ApplicationConsts.APPLICATION_TYPE_RENEWAL);
                appSubmissionDto.setDraftNo(draftNumber);
                log.info(StringUtil.changeForLog("without renewal deafrt number =====>" + draftNumber));
            } else {
                appSubmissionDto.setDraftNo(draftNo);
            }

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
        if (!IaisCommonUtils.isEmpty(serviceNames)) {
            List<HcsaServiceDto> hcsaServiceDtoList = serviceConfigService.getHcsaServiceByNames(serviceNames);
            ParamUtil.setSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST, (Serializable) hcsaServiceDtoList);
        }
        renewDto.setAppSubmissionDtos(appSubmissionDtoList);
        List<AppSubmissionDto> cloneAppsbumissionDtos = IaisCommonUtils.genNewArrayList();
        CopyUtil.copyMutableObjectList(appSubmissionDtoList, cloneAppsbumissionDtos);
        ParamUtil.setSessionAttr(bpc.request, "oldSubmissionDtos", (Serializable) cloneAppsbumissionDtos);
        ParamUtil.setSessionAttr(bpc.request, RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR, renewDto);
        ParamUtil.setSessionAttr(bpc.request, "serviceNameTitleList", (Serializable) serviceNameTitleList);
        //serviceNameList
        ParamUtil.setSessionAttr(bpc.request, "serviceNames", (Serializable) serviceNameList);
        ParamUtil.setSessionAttr(bpc.request, "firstSvcName", firstSvcName);
        ParamUtil.setSessionAttr(bpc.request, "currentPreviewSvcInfoList", (Serializable) appSvcRelatedInfoDtoList);
        ParamUtil.setSessionAttr(bpc.request, "reloadDisciplineAllocationMapList", (Serializable) reloadDisciplineAllocationMapList);
        ParamUtil.setSessionAttr(bpc.request, "ReloadPrincipalOfficersList", (Serializable) principalOfficersDtosList);
        ParamUtil.setSessionAttr(bpc.request, "deputyPrincipalOfficersDtosList", (Serializable) deputyPrincipalOfficersDtosList);

        //init app submit
        ParamUtil.setSessionAttr(bpc.request, "hasAppSubmit", null);
        ParamUtil.setSessionAttr(bpc.request, "txnDt", null);
        ParamUtil.setSessionAttr(bpc.request, "txnRefNo", null);

        log.info("**** the non auto renwal  end ******");
    }

    private void loadCoMap(BaseProcessClass bpc, AppSubmissionDto appSubmissionDto) {
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
            bpc.getSession().setAttribute("coMap", coMap);
        }
    }

    /**
     * AutoStep: prepare
     *
     * @param bpc
     * @throws
     */
    public void prepare(BaseProcessClass bpc) throws Exception {
        log.info("**** the  auto renwal  prepare start  ******");
        ParamUtil.setRequestAttr(bpc.request, RfcConst.FIRSTVIEW, AppConsts.TRUE);
        //finish pay
        RenewDto renewDto = (RenewDto) ParamUtil.getSessionAttr(bpc.request, RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR);
        List<AppSubmissionDto> appSubmissionDtos = renewDto.getAppSubmissionDtos();
        String groupId = "";
        Double amount = 0.0;
        String licenseeId = null;
        if (appSubmissionDtos != null && appSubmissionDtos.size() != 0) {
            groupId = appSubmissionDtos.get(0).getAppGrpId();
            amount = appSubmissionDtos.get(0).getAmount();
            licenseeId = appSubmissionDtos.get(0).getLicenseeId();
            AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) bpc.request.getSession().getAttribute("oldAppSubmissionDto");
            if (oldAppSubmissionDto == null) {
                oldAppSubmissionDto = appSubmissionDtos.get(0);
            }
            Object loadingDraft = ParamUtil.getSessionAttr(bpc.request, LOADING_DRAFT);
            if (loadingDraft != null) {
                AppSubmissionDto oldAppSubmisDto = appSubmissionDtos.get(0).getOldAppSubmissionDto();
                if (oldAppSubmisDto != null) {
                    oldAppSubmissionDto = oldAppSubmisDto;
                }
            } else {
                //set oldAppSubmissionDto
                renewDto.getAppSubmissionDtos().get(0).setOldAppSubmissionDto(oldAppSubmissionDto);
            }
            requestForChangeService.svcDocToPresmise(oldAppSubmissionDto);
            requestForChangeService.svcDocToPresmise(appSubmissionDtos.get(0));
            requestForChangeService.svcDocToPresmise(oldAppSubmissionDto);
            if (!IaisCommonUtils.isEmpty(appSubmissionDtos)) {
                for (AppSubmissionDto appSubmissionDto : appSubmissionDtos) {
                    requestForChangeService.svcDocToPresmise(appSubmissionDto);
                }
            }
            bpc.request.getSession().setAttribute("oldAppSubmissionDto", oldAppSubmissionDto);
        }
        String result = ParamUtil.getMaskedString(bpc.request, "result");
        if (!StringUtil.isEmpty(result)) {
            log.info(StringUtil.changeForLog("payment result:" + result));
            String pmtRefNo = ParamUtil.getMaskedString(bpc.request, "reqRefNo");
            if ("success".equals(result) && !StringUtil.isEmpty(pmtRefNo)) {
                String txnDt = ParamUtil.getMaskedString(bpc.request, "txnDt");
                String txnRefNo = ParamUtil.getMaskedString(bpc.request, "txnRefNo");

                ParamUtil.setSessionAttr(bpc.request, "txnDt", txnDt);
                ParamUtil.setSessionAttr(bpc.request, "txnRefNo", txnRefNo);
                String pmtMethod = "";
                if(appSubmissionDtos != null && appSubmissionDtos.size() > 0){
                    pmtMethod = appSubmissionDtos.get(0).getPaymentMethod();
                }
                //update status
                ApplicationGroupDto appGrp = new ApplicationGroupDto();
                appGrp.setId(groupId);
                appGrp.setPmtRefNo(pmtRefNo);
                appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
                appGrp.setPayMethod(pmtMethod);
                serviceConfigService.updatePaymentStatus(appGrp);
                //update application status
                appSubmissionService.updateApplicationsStatus(groupId, ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING);
                //jump page to acknowledgement
                //send email pay success
                try {
                    sendEmail(bpc.request,appSubmissionDtos);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
                ParamUtil.setRequestAttr(bpc.request, PAGE_SWITCH, PAGE4);
            } else {
                //jump page to payment
                ParamUtil.setRequestAttr(bpc.request, PAGE_SWITCH, PAGE3);
            }
        }else if(appSubmissionDtos.get(0).getPaymentMethod()!=null&&appSubmissionDtos.get(0).getPaymentMethod().equals(ApplicationConsts.PAYMENT_METHOD_NAME_GIRO)){
            ParamUtil.setRequestAttr(bpc.request, PAGE_SWITCH, PAGE4);
        }
        if(!IaisCommonUtils.isEmpty(appSubmissionDtos)) {//NOSONAR
            for (AppSubmissionDto appSubmissionDto : appSubmissionDtos) {
                List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
                if (appGrpPremisesDtoList != null) {
                    for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
                        List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodList = appGrpPremisesDto.getAppPremPhOpenPeriodList();
                        if (appPremPhOpenPeriodList != null) {
                            for (AppPremPhOpenPeriodDto appPremPhOpenPeriodDto : appPremPhOpenPeriodList) {
                                appPremPhOpenPeriodDto.setDayName(MasterCodeUtil.getCodeDesc(appPremPhOpenPeriodDto.getPhDate()));
                            }
                        }
                    }
                }
            }
        }
        log.info("**** the  renwal  prepare  end ******");
    }

    public void prepareInstructions(BaseProcessClass bpc) throws Exception {
        ParamUtil.setRequestAttr(bpc.request, "hasDetail", "N");
    }

    //prepareLicenceReview
    public void prepareLicenceReview(BaseProcessClass bpc) throws Exception {
        ParamUtil.setRequestAttr(bpc.request, "hasDetail", "Y");
        List<AppSubmissionDto> oldSubmissionDtos = (List<AppSubmissionDto>) ParamUtil.getSessionAttr(bpc.request, "oldSubmissionDtos");
        RenewDto renewDto = (RenewDto) ParamUtil.getSessionAttr(bpc.request, RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR);
        List<AppSubmissionDto> newAppSubmissionDtos = renewDto.getAppSubmissionDtos();
        if (!IaisCommonUtils.isEmpty(oldSubmissionDtos) && !IaisCommonUtils.isEmpty(newAppSubmissionDtos)) {
            List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList = oldSubmissionDtos.get(0).getAppSvcRelatedInfoDtoList();
            List<AppGrpPremisesDto> oldAppGrpPremisesDtoList = oldSubmissionDtos.get(0).getAppGrpPremisesDtoList();
            List<AppSvcRelatedInfoDto> newAppSvcRelatedInfoDtoList = newAppSubmissionDtos.get(0).getAppSvcRelatedInfoDtoList();
            List<AppGrpPremisesDto> newAppGrpPremisesDtoList = newAppSubmissionDtos.get(0).getAppGrpPremisesDtoList();
            boolean replacePerson = outRenewalService.isReplace(newAppSvcRelatedInfoDtoList, oldAppSvcRelatedInfoDtoList);
            log.error(StringUtil.changeForLog("replacePerson"+replacePerson));
            boolean updatePerson = outRenewalService.isUpdate(newAppSvcRelatedInfoDtoList, oldAppSvcRelatedInfoDtoList);
            log.error(StringUtil.changeForLog("updatePerson"+updatePerson));
            boolean editDoc = outRenewalService.isEditDoc(newAppSubmissionDtos.get(0), oldSubmissionDtos.get(0));
            log.error(StringUtil.changeForLog("editDoc"+editDoc));
            List<AppSvcPrincipalOfficersDto> poAndDpo = newAppSvcRelatedInfoDtoList.get(0).getAppSvcPrincipalOfficersDtoList();
            if(!IaisCommonUtils.isEmpty(poAndDpo)){
                poAndDpo.sort((h1,h2)->h2.getPsnType().compareTo(h1.getPsnType()));
                newAppSvcRelatedInfoDtoList.get(0).setAppSvcPrincipalOfficersDtoList(poAndDpo);
            }
            boolean eqGrpPremisesResult = eqGrpPremises(newAppGrpPremisesDtoList, oldAppGrpPremisesDtoList);
            if (replacePerson || updatePerson || eqGrpPremisesResult ||editDoc) {
                ParamUtil.setRequestAttr(bpc.request, "changeRenew", "Y");
            }
        }
    }

    //preparePayment
    public void preparePayment(BaseProcessClass bpc) throws Exception {
        RenewDto renewDto = (RenewDto) ParamUtil.getSessionAttr(bpc.request, RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR);
        String paymentMessageValidateMessage = MessageUtil.replaceMessage("GENERAL_ERR0006","Payment Method", "field");
        ParamUtil.setSessionAttr(bpc.request,"paymentMessageValidateMessage",paymentMessageValidateMessage);
        ParamUtil.setRequestAttr(bpc.request, "hasDetail", "Y");
        if (renewDto == null) {
            return;
        }
        AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
        appEditSelectDto.setServiceEdit(true);
        appEditSelectDto.setPremisesEdit(true);
        appEditSelectDto.setDocEdit(true);
        appEditSelectDto.setPoEdit(true);
        appEditSelectDto.setDocEdit(true);
        AppFeeDetailsDto appFeeDetailsDto = new AppFeeDetailsDto();
        String hasSubmit = (String) ParamUtil.getSessionAttr(bpc.request, "hasAppSubmit");
        if ("Y".equals(hasSubmit)) {
            return;
        }
        List<AppSubmissionDto> appSubmissionDtos = renewDto.getAppSubmissionDtos();
        //app submit
        InterInboxUserDto interInboxUserDto = (InterInboxUserDto) ParamUtil.getSessionAttr(bpc.request, "INTER_INBOX_USER_INFO");
        String licenseeId = null;
        if (interInboxUserDto != null) {
            licenseeId = interInboxUserDto.getLicenseeId();
        } else {
            log.error(StringUtil.changeForLog("interInboxUserDto null"));
        }
        Double total = 0d;
        AmendmentFeeDto amendmentFeeDto = new AmendmentFeeDto();
        amendmentFeeDto.setChangeInLicensee(Boolean.FALSE);

        AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) bpc.request.getSession().getAttribute("oldAppSubmissionDto");
        List<AppGrpPremisesDto> oldAppSubmissionDtoAppGrpPremisesDtoList = oldAppSubmissionDto.getAppGrpPremisesDtoList();
        List<AppSubmissionDto> rfcAppSubmissionDtos = IaisCommonUtils.genNewArrayList();
        List<AppSubmissionDto> autoAppSubmissionDtos = IaisCommonUtils.genNewArrayList();
        List<AppSubmissionDto> noAutoAppSubmissionDtos = IaisCommonUtils.genNewArrayList();
        List<String> renewLicIds = IaisCommonUtils.genNewArrayList();
        List<FeeExtDto> laterFeeDetails = IaisCommonUtils.genNewArrayList();
        for (AppSubmissionDto appSubmissionDto : appSubmissionDtos) {
            appEditSelectDto.setPremisesEdit(false);
            appEditSelectDto.setServiceEdit(false);
            appEditSelectDto.setDocEdit(false);
            appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
            appSubmissionDto.setChangeSelectDto(appEditSelectDto);
            List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
            boolean eqGrpPremisesResult;
            if(appGrpPremisesDtoList != null){
                eqGrpPremisesResult = eqGrpPremises(appGrpPremisesDtoList, oldAppSubmissionDtoAppGrpPremisesDtoList);
            } else {
                eqGrpPremisesResult = false;
            }
            if (eqGrpPremisesResult && appSubmissionDtos.size() == 1) {
                if (appGrpPremisesDtoList != null) {
                    for (int i = 0; i < appGrpPremisesDtoList.size(); i++) {
                        List<LicenceDto> attribute = (List<LicenceDto>) bpc.request.getSession().getAttribute("selectLicence" + i);
                        if (attribute != null) {
                            for (LicenceDto licenceDto : attribute) {
                                AppSubmissionDto appSubmissionDtoByLicenceId = requestForChangeService.getAppSubmissionDtoByLicenceId(licenceDto.getId());
                                appSubmissionDtoByLicenceId.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
                                appSubmissionService.transform(appSubmissionDtoByLicenceId, licenseeId);
                                boolean groupLic = appSubmissionDtoByLicenceId.isGroupLic();
                                String address = appGrpPremisesDtoList.get(i).getAddress();
                                String premisesIndexNo = appSubmissionDtoByLicenceId.getAppGrpPremisesDtoList().get(0).getPremisesIndexNo();
                                boolean equals;
                                boolean flag;
                                if (groupLic) {
                                    AppGrpPremisesDto oldAppGrpPremisesDto = oldAppSubmissionDto.getAppGrpPremisesDtoList().get(i);
                                    boolean b = NewApplicationDelegator.compareHciName(oldAppGrpPremisesDto, appGrpPremisesDtoList.get(i));
                                    amendmentFeeDto.setChangeInHCIName(!b);
                                    String olAddress = oldAppGrpPremisesDto.getAddress();
                                    equals = olAddress.equals(address);
                                    if(equals&&b){
                                        flag=true;
                                    }else {
                                        flag=false;
                                    }
                                } else {
                                    String oldAddress = appSubmissionDtoByLicenceId.getAppGrpPremisesDtoList().get(0).getAddress();
                                    equals = oldAddress.equals(address);
                                    boolean b = NewApplicationDelegator.compareHciName(appSubmissionDtoByLicenceId.getAppGrpPremisesDtoList().get(0), appSubmissionDto.getAppGrpPremisesDtoList().get(i));
                                    amendmentFeeDto.setChangeInHCIName(!b);
                                    if(equals&&b){
                                        flag=true;
                                    }else {
                                        flag=false;
                                    }
                                }
                                amendmentFeeDto.setChangeInLocation(!equals);

                                if (flag) {
                                    List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDtoByLicenceId.getAppGrpPremisesDtoList();
                                    if (!IaisCommonUtils.isEmpty(appGrpPremisesDtos)) {
                                        for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
                                            appGrpPremisesDto.setNeedNewLicNo(Boolean.FALSE);
                                            appGrpPremisesDto.setSelfAssMtFlag(4);
                                        }
                                    }
                                }

                                appSubmissionDtoByLicenceId.setGroupLic(groupLic);
                                appSubmissionDtoByLicenceId.setPartPremise(groupLic);
                                appSubmissionDtoByLicenceId.setAmount(0.0);
                                AppGrpPremisesDto appGrpPremisesDto = appGrpPremisesDtoList.get(i);
                                List<AppGrpPremisesDto> appGrpPremisesDtos = new ArrayList<>(1);
                                AppGrpPremisesDto copyMutableObject = (AppGrpPremisesDto) CopyUtil.copyMutableObject(appGrpPremisesDto);
                                appGrpPremisesDtos.add(copyMutableObject);
                                if (groupLic) {
                                    appGrpPremisesDtos.get(0).setGroupLicenceFlag(licenceDto.getId());
                                }
                                appSubmissionDtoByLicenceId.setAppGrpPremisesDtoList(appGrpPremisesDtos);
                                appSubmissionDtoByLicenceId.getAppGrpPremisesDtoList().get(0).setPremisesIndexNo(premisesIndexNo);
                                appSubmissionDtoByLicenceId.setIsNeedNewLicNo(AppConsts.YES);
                                PreOrPostInspectionResultDto preOrPostInspectionResultDto = appSubmissionService.judgeIsPreInspection(appSubmissionDtoByLicenceId);
                                if (preOrPostInspectionResultDto == null) {
                                    appSubmissionDtoByLicenceId.setPreInspection(true);
                                    appSubmissionDtoByLicenceId.setRequirement(true);
                                } else {
                                    appSubmissionDtoByLicenceId.setPreInspection(preOrPostInspectionResultDto.isPreInspection());
                                    appSubmissionDtoByLicenceId.setRequirement(preOrPostInspectionResultDto.isRequirement());
                                }
                                appSubmissionDtoByLicenceId.setAutoRfc(flag);
                                appEditSelectDto.setPremisesEdit(true);
                                appEditSelectDto.setServiceEdit(false);
                                appEditSelectDto.setDocEdit(false);
                                appSubmissionDtoByLicenceId.setAppEditSelectDto(appEditSelectDto);
                                appSubmissionDtoByLicenceId.setChangeSelectDto(appEditSelectDto);
                                appSubmissionDtoByLicenceId.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
                                appSubmissionDtoByLicenceId.setStatus(ApplicationConsts.APPLICATION_STATUS_REQUEST_FOR_CHANGE_SUBMIT);
                                String draftNo = appSubmissionDtoByLicenceId.getDraftNo();
                                if (StringUtil.isEmpty(draftNo)) {
                                    appSubmissionService.setDraftNo(appSubmissionDtoByLicenceId);
                                }
                                if (!amendmentFeeDto.getChangeInHCIName() && !amendmentFeeDto.getChangeInLocation()) {
                                    appSubmissionDtoByLicenceId.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
                                    appSubmissionDtoByLicenceId.setAutoRfc(true);
                                } else {
                                    appSubmissionDtoByLicenceId.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_PENDING_PAYMENT);
                                    appSubmissionDtoByLicenceId.setAutoRfc(false);
                                    appSubmissionDtoByLicenceId.setCreateAuditPayStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING);
                                }
                                appSubmissionDtoByLicenceId.setAppGrpPrimaryDocDtos(null);
                                RequestForChangeMenuDelegator.oldPremiseToNewPremise(appSubmissionDtoByLicenceId);
                                requestForChangeService.premisesDocToSvcDoc(appSubmissionDtoByLicenceId);
                                if (appSubmissionDtoByLicenceId.isAutoRfc()) {
                                    autoAppSubmissionDtos.add(appSubmissionDtoByLicenceId);
                                } else {
                                    noAutoAppSubmissionDtos.add(appSubmissionDtoByLicenceId);
                                }
                            }
                        }
                    }
                }
            }
            String licenceId = appSubmissionDto.getLicenceId();
            renewLicIds.add(licenceId);
            //FeeDto feeDto = appSubmissionService.getGroupAmount(appSubmissionDto,NewApplicationHelper.isCharity(bpc.request));
            appSubmissionDto.setLicenseeId(licenseeId);
            //set fee detail
            //List<FeeExtDto> detailFeeDtos = feeDto.getDetailFeeDto();
//            Double lateFee = 0.0;
//            if (!IaisCommonUtils.isEmpty(detailFeeDtos)) {
//                appSubmissionDto.setDetailFeeDto(detailFeeDtos.get(0));
//                laterFeeDetails.add(detailFeeDtos.get(0));
//                for (FeeExtDto feeExtDto : detailFeeDtos) {
//                    Double lateFeeAmoumt = feeExtDto.getLateFeeAmoumt();
//                    if (lateFeeAmoumt != null) {
//                        lateFee += lateFeeAmoumt;
//                    }
//                }
//            } else {
//                log.error(StringUtil.changeForLog("feeDto detailFeeDtos null"));
//            }
            //Double amount = feeDto.getTotal();
            appFeeDetailsDto.setAdmentFee(0.0);
            //appFeeDetailsDto.setLaterFee(lateFee);
            //appFeeDetailsDto.setBaseFee(amount - lateFee);
            //appSubmissionDto.setLateFee(lateFee);
            //appSubmissionDto.setLateFeeStr(Formatter.formatterMoney(lateFee));
//            if (!StringUtil.isEmpty(amount)) {
//                total += amount;
//                appSubmissionDto.setAmount(amount - lateFee);
//                String amountStr = Formatter.formatterMoney(amount - lateFee);
//                appSubmissionDto.setAmountStr(amountStr);
//            }
            requestForChangeService.premisesDocToSvcDoc(appSubmissionDto);
        }
        boolean isCharity = NewApplicationHelper.isCharity(bpc.request);
        FeeDto renewalAmount = new FeeDto();
        if(isCharity){
            renewalAmount = appSubmissionService.getCharityRenewalAmount(appSubmissionDtos,isCharity);
        }else {
            renewalAmount = appSubmissionService.getRenewalAmount(appSubmissionDtos,isCharity);
        }
        setSubmissionAmount(appSubmissionDtos,renewalAmount,appFeeDetailsDto,bpc);
        HashMap<String, List<FeeExtDto>> laterFeeDetailsMap = getLaterFeeDetailsMap(renewalAmount.getDetailFeeDto());
        ParamUtil.setRequestAttr(bpc.request, "laterFeeDetailsMap", laterFeeDetailsMap);
        requestForChangeService.premisesDocToSvcDoc(oldAppSubmissionDto);
        List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList = oldAppSubmissionDto.getAppSvcRelatedInfoDtoList();
        List<AppSvcRelatedInfoDto> newAppSvcRelatedInfoDtoList = appSubmissionDtos.get(0).getAppSvcRelatedInfoDtoList();
        String appGroupNo = requestForChangeService.getApplicationGroupNumber(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
        //Boolean update = outRenewalService.isUpdate(newAppSvcRelatedInfoDtoList, oldAppSvcRelatedInfoDtoList);
        if (appSubmissionDtos.size() == 1) {
        Set<String> idNos = IaisCommonUtils.genNewHashSet();
        List<String> updateCgo = outRenewalService.isUpdateCgo(newAppSvcRelatedInfoDtoList, oldAppSvcRelatedInfoDtoList);
        List<String> updatePo = outRenewalService.isUpdatePo(newAppSvcRelatedInfoDtoList, oldAppSvcRelatedInfoDtoList);
        List<String> updateDpo = outRenewalService.isUpdateDpo(newAppSvcRelatedInfoDtoList, oldAppSvcRelatedInfoDtoList);
        List<String> updateMat = outRenewalService.isUpdateMat(newAppSvcRelatedInfoDtoList, oldAppSvcRelatedInfoDtoList);
        if (!IaisCommonUtils.isEmpty(updateCgo)) {
            for (String idNo : updateCgo) {
                idNos.add(idNo);
            }
        }
        if (!IaisCommonUtils.isEmpty(updatePo)) {
            for (String idNo : updatePo) {
                idNos.add(idNo);
            }
        }
        if (!IaisCommonUtils.isEmpty(updateDpo)) {
            for (String idNo : updateDpo) {
                idNos.add(idNo);
            }
        }
        if (!IaisCommonUtils.isEmpty(updateMat)) {
            for (String idNo : updateMat) {
                idNos.add(idNo);
            }
        }
        Map<String, List<String>> idNoPsnTypeMap = IaisCommonUtils.genNewHashMap();
        if (!IaisCommonUtils.isEmpty(idNos)) {
            for (String idNo : idNos) {
                if (updateCgo.contains(idNo)) {
                    List<String> psnTypesExit = idNoPsnTypeMap.get(idNo);
                    if (!IaisCommonUtils.isEmpty(psnTypesExit)) {
                        psnTypesExit.add(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO);
                    } else {
                        List<String> psnTypes = IaisCommonUtils.genNewArrayList();
                        psnTypes.add(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO);
                        idNoPsnTypeMap.put(idNo, psnTypes);
                    }
                }
                if (updatePo.contains(idNo)) {
                    List<String> psnTypesExit = idNoPsnTypeMap.get(idNo);
                    if (!IaisCommonUtils.isEmpty(psnTypesExit)) {
                        psnTypesExit.add(ApplicationConsts.PERSONNEL_PSN_TYPE_PO);
                    } else {
                        List<String> psnTypes = IaisCommonUtils.genNewArrayList();
                        psnTypes.add(ApplicationConsts.PERSONNEL_PSN_TYPE_PO);
                        idNoPsnTypeMap.put(idNo, psnTypes);
                    }
                }
                if (updateDpo.contains(idNo)) {
                    List<String> psnTypesExit = idNoPsnTypeMap.get(idNo);
                    if (!IaisCommonUtils.isEmpty(psnTypesExit)) {
                        psnTypesExit.add(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO);
                    } else {
                        List<String> psnTypes = IaisCommonUtils.genNewArrayList();
                        psnTypes.add(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO);
                        idNoPsnTypeMap.put(idNo, psnTypes);
                    }
                }
                if (updateMat.contains(idNo)) {
                    List<String> psnTypesExit = idNoPsnTypeMap.get(idNo);
                    if (!IaisCommonUtils.isEmpty(psnTypesExit)) {
                        psnTypesExit.add(ApplicationConsts.PERSONNEL_PSN_TYPE_MAP);
                    } else {
                        List<String> psnTypes = IaisCommonUtils.genNewArrayList();
                        psnTypes.add(ApplicationConsts.PERSONNEL_PSN_TYPE_MAP);
                        idNoPsnTypeMap.put(idNo, psnTypes);
                    }
                }
            }
        }
        String currentLicenceId = appSubmissionDtos.get(0).getLicenceId();
        String finalLicenseeId = licenseeId;
        idNoPsnTypeMap.forEach((idNo, psnTypes) -> {
            List<String> notReNewLicIds = IaisCommonUtils.genNewArrayList();
            notReNewLicIds.clear();
            List<String> personIds = requestForChangeService.getPersonnelIdsByIdNo(idNo);
            List<LicKeyPersonnelDto> licByPerId = requestForChangeService.getLicKeyPersonnelDtoByPerId(personIds);
            for (LicKeyPersonnelDto dto : licByPerId) {
                String licenceId = dto.getLicenceId();
                String licseeId = dto.getLicenseeId();
                if (finalLicenseeId.equals(licseeId) && !notReNewLicIds.contains(licenceId) && !currentLicenceId.equals(licenceId)) {
                    notReNewLicIds.add(licenceId);
                }
            }
            if (!IaisCommonUtils.isEmpty(notReNewLicIds)) {
                List<AppSubmissionDto> notReNewappSubmissionDtos = requestForChangeService.getAppSubmissionDtoByLicenceIds(notReNewLicIds);
                for (AppSubmissionDto appSubmissionDto : notReNewappSubmissionDtos) {
                    if (psnTypes.contains(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO)) {
                        List<AppSvcCgoDto> appSvcCgoDtoList = newAppSvcRelatedInfoDtoList.get(0).getAppSvcCgoDtoList();
                        appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).setAppSvcCgoDtoList(appSvcCgoDtoList);
                    }
                    if (psnTypes.contains(ApplicationConsts.PERSONNEL_PSN_TYPE_PO)) {
                        List<AppSvcPrincipalOfficersDto> newPOList = IaisCommonUtils.genNewArrayList();
                        List<AppSvcPrincipalOfficersDto> newAppSvcPrincipalOfficersDtoList = newAppSvcRelatedInfoDtoList.get(0).getAppSvcPrincipalOfficersDtoList();
                        if (!IaisCommonUtils.isEmpty(newAppSvcPrincipalOfficersDtoList)) {
                            for (int i = 0; i < newAppSvcPrincipalOfficersDtoList.size(); i++) {
                                AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = newAppSvcPrincipalOfficersDtoList.get(i);
                                String psnType = appSvcPrincipalOfficersDto.getPsnType();
                                if (ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(psnType)) {
                                    newPOList.add(appSvcPrincipalOfficersDto);
                                }
                            }
                        }
                        appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).setAppSvcPrincipalOfficersDtoList(newPOList);
                    }
                    if (psnTypes.contains(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO)) {
                        List<AppSvcPrincipalOfficersDto> newPOList = IaisCommonUtils.genNewArrayList();
                        List<AppSvcPrincipalOfficersDto> newAppSvcPrincipalOfficersDtoList = newAppSvcRelatedInfoDtoList.get(0).getAppSvcPrincipalOfficersDtoList();
                        if (!IaisCommonUtils.isEmpty(newAppSvcPrincipalOfficersDtoList)) {
                            for (int i = 0; i < newAppSvcPrincipalOfficersDtoList.size(); i++) {
                                AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = newAppSvcPrincipalOfficersDtoList.get(i);
                                String psnType = appSvcPrincipalOfficersDto.getPsnType();
                                if (ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(psnType)) {
                                    newPOList.add(appSvcPrincipalOfficersDto);
                                }
                            }
                        }
                        appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).setAppSvcPrincipalOfficersDtoList(newPOList);
                    }
                    if (psnTypes.contains(ApplicationConsts.PERSONNEL_PSN_TYPE_MAP)) {
                        List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList = newAppSvcRelatedInfoDtoList.get(0).getAppSvcMedAlertPersonList();
                        appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).setAppSvcMedAlertPersonList(appSvcMedAlertPersonList);
                    }
                    appSubmissionDto.setLicenseeId(finalLicenseeId);
                    try {
                        NewApplicationHelper.setSubmissionDtoSvcData(bpc.request, appSubmissionDto);
                    } catch (CloneNotSupportedException e) {
                        log.error(e.getMessage(), e);
                    }
                    appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
                    appSubmissionDto.setStatus(ApplicationConsts.APPLICATION_STATUS_REQUEST_FOR_CHANGE_SUBMIT);
                    appSubmissionDto.setAppGrpNo(appGroupNo);
                    appSubmissionDto.setAmount(0.0);
                    appSubmissionDto.setAutoRfc(true);
                    String draftNo = appSubmissionDto.getDraftNo();
                    if (StringUtil.isEmpty(draftNo)) {
                        appSubmissionService.setDraftNo(appSubmissionDto);
                    }
                    List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
                    if (!IaisCommonUtils.isEmpty(appGrpPremisesDtos)) {
                        for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
                            appGrpPremisesDto.setNeedNewLicNo(Boolean.FALSE);
                        }
                    }
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
                    appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    appSubmissionDto.setCreatAuditAppStatus(ApplicationConsts.APPLICATION_STATUS_APPROVED);
                    requestForChangeService.premisesDocToSvcDoc(appSubmissionDto);
                }
                autoAppSubmissionDtos.addAll(notReNewappSubmissionDtos);
            }
        });
    }
        AppSubmissionListDto appSubmissionListDto = new AppSubmissionListDto();
        String submissionId = generateIdClient.getSeqId().getEntity();
        Long l = System.currentTimeMillis();
        List<AppSubmissionDto> appSubmissionDtos1 = IaisCommonUtils.genNewArrayList();
        String totalStr = Formatter.formatterMoney(total);
        //do app submit
        String appGrpNo = appSubmissionDtos.get(0).getAppGrpNo();
        appSubmissionDtos1.addAll(appSubmissionDtos);
        /*ApplicationGroupDto applicationGroupDto = appSubmissionService.createApplicationDataByWithOutRenewal(renewDto);*/
        for(AppSubmissionDto appSubmissionDto : noAutoAppSubmissionDtos){
            appSubmissionDto.setAppGrpNo(appGrpNo);
        }
        appFeeDetailsDto.setApplicationNo(appGrpNo + "-01");
        appSubmissionService.saveAppFeeDetails(appFeeDetailsDto);

        appSubmissionDtos1.addAll(noAutoAppSubmissionDtos);
        AuditTrailDto currentAuditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        if(!autoAppSubmissionDtos.isEmpty() || !noAutoAppSubmissionDtos.isEmpty()){
            if(appSubmissionDtos.size()==1){
                AuditTrailHelper.auditFunctionWithLicNo(AuditTrailConsts.MODULE_RENEW,AuditTrailConsts.MODULE_RENEW,appSubmissionDtos.get(0).getLicenceNo());
            }
        }
        if (!autoAppSubmissionDtos.isEmpty()) {
            AppSubmissionListDto autoAppSubmissionListDto = new AppSubmissionListDto();
            String autoSubmissionId = generateIdClient.getSeqId().getEntity();
            Long auto = System.currentTimeMillis();
            autoAppSubmissionDtos.get(0).setAuditTrailDto(currentAuditTrailDto);
            List<AppSubmissionDto> saveutoAppSubmissionDto = requestForChangeService.saveAppsForRequestForGoupAndAppChangeByList(autoAppSubmissionDtos);
            String autoGrpNo = saveutoAppSubmissionDto.get(0).getAppGrpNo();
            for(AppSubmissionDto appSubmissionDto : autoAppSubmissionDtos){
                appSubmissionDto.setAppGrpNo(autoGrpNo);
                List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
                if(!IaisCommonUtils.isEmpty(appGrpPremisesDtoList)){
                    for(AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList){
                        appGrpPremisesDto.setSelfAssMtFlag(4);
                    }
                }
            }
            AuditTrailDto at = AuditTrailHelper.getCurrentAuditTrailDto();
            at.setModule(AuditTrailConsts.MODULE_RENEW);
            at.setFunctionName(AuditTrailConsts.FUNCTION_RENEW);
            autoAppSubmissionListDto.setAuditTrailDto(AuditTrailHelper.getCurrentAuditTrailDto());
            autoAppSubmissionListDto.setEventRefNo(auto.toString());
            autoAppSubmissionListDto.setAppSubmissionDtos(saveutoAppSubmissionDto);
            eventBusHelper.submitAsyncRequest(autoAppSubmissionListDto, autoSubmissionId, EventBusConsts.SERVICE_NAME_APPSUBMIT,
                    EventBusConsts.OPERATION_REQUEST_INFORMATION_SUBMIT, l.toString(), bpc.process);
        }

        for(AppSubmissionDto appSubmissionDto : appSubmissionDtos1){
            appSubmissionDto.setAuditTrailDto(currentAuditTrailDto);
        }
        List<AppSubmissionDto> appSubmissionDtos3 = requestForChangeService.saveAppsForRequestForGoupAndAppChangeByList(appSubmissionDtos1);
        appSubmissionListDto.setAppSubmissionDtos(appSubmissionDtos3);
        appSubmissionListDto.setAuditTrailDto(AuditTrailHelper.getCurrentAuditTrailDto());
        appSubmissionListDto.setEventRefNo(l.toString());
        eventBusHelper.submitAsyncRequest(appSubmissionListDto, submissionId, EventBusConsts.SERVICE_NAME_APPSUBMIT,
                EventBusConsts.OPERATION_REQUEST_INFORMATION_SUBMIT, l.toString(), bpc.process);
        rfcAppSubmissionDtos.addAll(noAutoAppSubmissionDtos);
        rfcAppSubmissionDtos.addAll(autoAppSubmissionDtos);
        List<AppSubmissionDto> renewAppSubmissionDtos = IaisCommonUtils.genNewArrayList();
        renewAppSubmissionDtos.addAll(rfcAppSubmissionDtos);
        renewAppSubmissionDtos.addAll(appSubmissionDtos3);
        List<String> serviceNamesAck = IaisCommonUtils.genNewArrayList();
        for (AppSubmissionDto appSubmissionDto : renewAppSubmissionDtos) {
            String serviceName = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getServiceName();
            String appType = appSubmissionDto.getAppType();
            if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)){
                serviceName = serviceName + " (Renewal)" ;
            }else {
                serviceName = serviceName + " (Amendment)" ;
            }
            serviceNamesAck.add(serviceName);
        }
        for (AppSubmissionDto appSubmissionDto : rfcAppSubmissionDtos) {
            appSubmissionDto.setServiceName(appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getServiceName());
            appSubmissionDto.setAmountStr("$0");
            appSubmissionDto.setAmount(0.0);
            appSubmissionDto.setId(appSubmissionDtos3.get(0).getId());
        }
        for (AppSubmissionDto appSubmissionDto : appSubmissionDtos) {
            appSubmissionDto.setAppGrpNo(appGrpNo);
            appSubmissionDto.setAppGrpId(appSubmissionDtos3.get(0).getAppGrpId());
        }
        //set group no.
        bpc.request.getSession().setAttribute("rfcAppSubmissionDtos", rfcAppSubmissionDtos);
        ParamUtil.setSessionAttr(bpc.request, RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR, renewDto);
        /*    ParamUtil.setRequestAttr(bpc.request,"applicationGroupDto",applicationGroupDto);*/
        ParamUtil.setSessionAttr(bpc.request, "serviceNamesAck", (Serializable) serviceNamesAck);
        //has app submit
        ParamUtil.setSessionAttr(bpc.request, "hasAppSubmit", "Y");
        boolean isGiroAcc = appSubmissionService.isGiroAccount(NewApplicationHelper.getLicenseeId(appSubmissionDtos));
        ParamUtil.setRequestAttr(bpc.request,"IsGiroAcc",isGiroAcc);
    }

    private void setSubmissionAmount(List<AppSubmissionDto> appSubmissionDtoList,FeeDto feeDto,AppFeeDetailsDto appFeeDetailsDto,BaseProcessClass bpc){
        List<FeeExtDto> detailFeeDtoList = feeDto.getDetailFeeDto();
        Double total = feeDto.getTotal();
        String totalString = Formatter.formatterMoney(total);
        ParamUtil.setSessionAttr(bpc.request, "totalStr", totalString);
        ParamUtil.setSessionAttr(bpc.request, "totalAmount", total);
        if(IaisCommonUtils.isEmpty(detailFeeDtoList) || IaisCommonUtils.isEmpty(appSubmissionDtoList)){
            return;
        }
        int index = 0;
        for(AppSubmissionDto appSubmissionDto : appSubmissionDtoList){
            FeeExtDto feeExtDto = detailFeeDtoList.get(index);
            Double lateFeeAmount = feeExtDto.getLateFeeAmoumt();
            Double amount = feeExtDto.getAmount();
            appSubmissionDto.setLateFee(lateFeeAmount);
            appSubmissionDto.setLateFeeStr(Formatter.formatterMoney(lateFeeAmount));
            appSubmissionDto.setAmount(amount);
            appSubmissionDto.setAmountStr(Formatter.formatterMoney(amount));
            appFeeDetailsDto.setBaseFee(amount);
            if(StringUtil.isEmpty(lateFeeAmount)){
                appFeeDetailsDto.setLaterFee(0.0);
            }else {
                appFeeDetailsDto.setLaterFee(lateFeeAmount);
            }
            appFeeDetailsDto.setAdmentFee(total);
            index ++;
        }
    }

    public static HashMap<String, List<FeeExtDto>> getLaterFeeDetailsMap(List<FeeExtDto> laterFeeDetails){
        HashMap<String, List<FeeExtDto>> laterFeeDetailsMap = IaisCommonUtils.genNewHashMap();
        if(laterFeeDetails == null || laterFeeDetails.size() == 0){
            return null;
        }

        for(FeeExtDto laterFeeDetail : laterFeeDetails){
            String targetLaterFeeType = laterFeeDetail.getLateFeeType();
            if(StringUtil.isEmpty(targetLaterFeeType)){
                continue;
            }

            if(laterFeeDetailsMap.get(targetLaterFeeType) == null){
                List<FeeExtDto> list = IaisCommonUtils.genNewArrayList();
                list.add(laterFeeDetail);
                laterFeeDetailsMap.put(laterFeeDetail.getLateFeeType(),list);
                continue;
            }else {
                List<FeeExtDto> list = laterFeeDetailsMap.get(laterFeeDetail.getLateFeeType());
                list.add(laterFeeDetail);
                laterFeeDetailsMap.put(laterFeeDetail.getLateFeeType(),list);
            }
        }
        return laterFeeDetailsMap;
    }

    private boolean eqGrpPremises(List<AppGrpPremisesDto> appGrpPremisesDtoList, List<AppGrpPremisesDto> oldAppGrpPremisesDtoList) {
        List<AppGrpPremisesDto> appGrpPremisesDtos = copyAppGrpPremises(appGrpPremisesDtoList);//NOSONAR
        List<AppGrpPremisesDto> oldAppGrpPremisesDtos = copyAppGrpPremises(oldAppGrpPremisesDtoList);
        if (!appGrpPremisesDtos.equals(oldAppGrpPremisesDtos)) {
            return true;
        }
        return false;
    }
    private AppGrpPremisesDto copyAppGrpPremisesDto(AppGrpPremisesDto appGrpPremisesDto){
        AppGrpPremisesDto copy=new AppGrpPremisesDto();
        copy.setPremisesType(appGrpPremisesDto.getPremisesType());
        copy.setPostalCode(appGrpPremisesDto.getPostalCode());
        copy.setAddrType(appGrpPremisesDto.getAddrType());
        copy.setBlkNo(appGrpPremisesDto.getBlkNo());
        copy.setFloorNo(appGrpPremisesDto.getFloorNo());
        copy.setUnitNo(appGrpPremisesDto.getUnitNo());
        copy.setStreetName(appGrpPremisesDto.getStreetName());
        copy.setBuildingName(appGrpPremisesDto.getBuildingName());
        copy.setOnsiteStartMM(appGrpPremisesDto.getOnsiteStartMM());
        copy.setOnsiteEndMM(appGrpPremisesDto.getOnsiteEndMM());
        copy.setOnsiteStartHH(appGrpPremisesDto.getOnsiteStartHH());
        copy.setOnsiteEndHH(appGrpPremisesDto.getOnsiteEndHH());

        copy.setOffSitePostalCode(appGrpPremisesDto.getOffSitePostalCode());
        copy.setOffSiteAddressType(appGrpPremisesDto.getOffSiteAddressType());
        copy.setOffSiteBlockNo(appGrpPremisesDto.getOffSiteBlockNo());
        copy.setOffSiteFloorNo(appGrpPremisesDto.getOffSiteFloorNo());
        copy.setOffSiteUnitNo(appGrpPremisesDto.getOffSiteUnitNo());
        copy.setOffSiteStreetName(appGrpPremisesDto.getOffSiteStreetName());
        copy.setOffSiteBuildingName(appGrpPremisesDto.getOffSiteBuildingName());
        copy.setOffSiteStartHH(appGrpPremisesDto.getOffSiteStartHH());
        copy.setOffSiteStartMM(appGrpPremisesDto.getOffSiteStartMM());
        copy.setOffSiteEndHH(appGrpPremisesDto.getOffSiteEndHH());
        copy.setOffSiteEndMM(appGrpPremisesDto.getOffSiteEndMM());

        copy.setConveyancePostalCode(appGrpPremisesDto.getConveyancePostalCode());
        copy.setConveyanceAddressType(appGrpPremisesDto.getConveyanceAddressType());
        copy.setConveyanceBlockNo(appGrpPremisesDto.getConveyanceBlockNo());
        copy.setConveyanceFloorNo(appGrpPremisesDto.getConveyanceFloorNo());
        copy.setConveyanceUnitNo(appGrpPremisesDto.getConveyanceUnitNo());
        copy.setConveyanceStreetName(appGrpPremisesDto.getConveyanceStreetName());
        copy.setConveyanceBuildingName(appGrpPremisesDto.getConveyanceBuildingName());
        copy.setConStartHH(appGrpPremisesDto.getConStartHH());
        copy.setConStartMM(appGrpPremisesDto.getConStartMM());
        copy.setConEndHH(appGrpPremisesDto.getConEndHH());
        copy.setConEndMM(appGrpPremisesDto.getConEndMM());
        if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(appGrpPremisesDto.getPremisesType())){
            copy.setHciName(appGrpPremisesDto.getHciName());
            copy.setOffTelNo(appGrpPremisesDto.getOffTelNo());
            copy.setLocateWithOthers(appGrpPremisesDto.getLocateWithOthers());
            copy.setScdfRefNo(appGrpPremisesDto.getScdfRefNo());
            copy.setCertIssuedDt(appGrpPremisesDto.getCertIssuedDt());
            if(StringUtil.isEmpty(appGrpPremisesDto.getCertIssuedDtStr())){
                copy.setCertIssuedDtStr(null);
            }else {
                copy.setCertIssuedDtStr(appGrpPremisesDto.getCertIssuedDtStr());
            }

        }else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(appGrpPremisesDto.getPremisesType())){

        }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())){
            copy.setConveyanceVehicleNo(appGrpPremisesDto.getConveyanceVehicleNo());
        }

        copy.setAppPremisesOperationalUnitDtos(appGrpPremisesDto.getAppPremisesOperationalUnitDtos());
        List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodList = appGrpPremisesDto.getAppPremPhOpenPeriodList();
        List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodDtos=new ArrayList<>();
        if(appPremPhOpenPeriodList!=null){
            for(AppPremPhOpenPeriodDto appPremPhOpenPeriodDto : appPremPhOpenPeriodList){
                AppPremPhOpenPeriodDto premPhOpenPeriodDto=new AppPremPhOpenPeriodDto();
                premPhOpenPeriodDto.setConvEndToMM(appPremPhOpenPeriodDto.getConvEndToMM());
                premPhOpenPeriodDto.setConvEndToHH(appPremPhOpenPeriodDto.getConvEndToHH());
                premPhOpenPeriodDto.setConvStartFromHH(appPremPhOpenPeriodDto.getConvStartFromHH());
                premPhOpenPeriodDto.setConvStartFromMM(appPremPhOpenPeriodDto.getConvStartFromMM());
                premPhOpenPeriodDto.setOnsiteStartFromMM(appPremPhOpenPeriodDto.getOnsiteStartFromMM());
                premPhOpenPeriodDto.setOnsiteStartFromHH(appPremPhOpenPeriodDto.getOnsiteStartFromHH());
                premPhOpenPeriodDto.setOnsiteEndToHH(appPremPhOpenPeriodDto.getOnsiteEndToHH());
                premPhOpenPeriodDto.setOnsiteEndToMM(appPremPhOpenPeriodDto.getOnsiteEndToMM());
                premPhOpenPeriodDto.setPhDate(appPremPhOpenPeriodDto.getPhDate());
                premPhOpenPeriodDto.setPhDateStr(appPremPhOpenPeriodDto.getPhDateStr());
                premPhOpenPeriodDto.setStartFrom(appPremPhOpenPeriodDto.getStartFrom());
                premPhOpenPeriodDto.setEndTo(appPremPhOpenPeriodDto.getEndTo());
                appPremPhOpenPeriodDtos.add(premPhOpenPeriodDto);
            }

        }
        copy.setAppPremPhOpenPeriodList(appPremPhOpenPeriodDtos);
        return copy;
    }
    private List<AppGrpPremisesDto> copyAppGrpPremises(List<AppGrpPremisesDto> appGrpPremisesDtoList)  {
        List<AppGrpPremisesDto> cpoyList=new ArrayList<>(appGrpPremisesDtoList.size());
        for(AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList){
            AppGrpPremisesDto copy = copyAppGrpPremisesDto(appGrpPremisesDto);
            cpoyList.add( copy);
        }

        return cpoyList;
    }

    //prepareAcknowledgement
    public void prepareAcknowledgement(BaseProcessClass bpc) throws Exception {
        InterInboxUserDto interInboxUserDto = (InterInboxUserDto) ParamUtil.getSessionAttr(bpc.request, "INTER_INBOX_USER_INFO");
        RenewDto renewDto = (RenewDto) ParamUtil.getSessionAttr(bpc.request, RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR);
        List<AppSubmissionDto> appSubmissionDtos = renewDto.getAppSubmissionDtos();
        for(AppSubmissionDto appSubmissionDto : appSubmissionDtos){
            if(!StringUtil.isEmpty(appSubmissionDto.getLicenceId())){
                List<ApplicationSubDraftDto> entity = applicationFeClient.getDraftByLicAppId(appSubmissionDto.getLicenceId()).getEntity();
                for(ApplicationSubDraftDto applicationSubDraftDto : entity){
                    String draftJson = applicationSubDraftDto.getDraftJson();
                    AppSubmissionDto appSubmissionDto1 = JsonUtil.parseToObject(draftJson, AppSubmissionDto.class);
                    appSubmissionDto1.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                    applicationFeClient.saveDraft(appSubmissionDto1);
                }
            }
        }
        String licenseeId = null;
        if (interInboxUserDto != null) {
            licenseeId = interInboxUserDto.getLicenseeId();
        } else {
            log.error(StringUtil.changeForLog("interInboxUserDto null"));
        }
        List<String> licenseeEmailAddrs = IaisEGPHelper.getLicenseeEmailAddrs(licenseeId);
        String emailAddress = emailAddressesToString(licenseeEmailAddrs);
        ParamUtil.setRequestAttr(bpc.request, "emailAddress", emailAddress);
        ParamUtil.setRequestAttr(bpc.request, "hasDetail", "N");
    }

    //doInstructions
    public void doInstructions(BaseProcessClass bpc) throws Exception {
        //go page2
        ParamUtil.setRequestAttr(bpc.request, PAGE_SWITCH, PAGE2);
    }
    public void jumpYeMian(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder url = new StringBuilder(10);
        url.append("https://").append(request.getServerName()).append("/main-web/eservice/INTERNET/MohInternetInbox");
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), request);
        response.sendRedirect(tokenUrl);
    }
    //doLicenceReview
    public void doLicenceReview(BaseProcessClass bpc) throws Exception {
        String crud_action_type = bpc.request.getParameter("crud_action_additional");
        if("exitSaveDraft".equals(crud_action_type)){
            jumpYeMian(bpc.request, bpc.response);
            return;
        }
        String rfc_err020 = MessageUtil.getMessageDesc("RFC_ERR020");
        AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) bpc.request.getSession().getAttribute("oldAppSubmissionDto");
        List<AppGrpPremisesDto> oldAppSubmissionDtoAppGrpPremisesDtoList = oldAppSubmissionDto.getAppGrpPremisesDtoList();
        RenewDto renewDto = (RenewDto) ParamUtil.getSessionAttr(bpc.request, RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR);
        String renewEffectiveDate = ParamUtil.getDate(bpc.request, "renewEffectiveDate");
        String userAgreement = ParamUtil.getString(bpc.request, "verifyInfoCheckbox");
        if (!StringUtil.isEmpty(userAgreement) && AppConsts.YES.equals(userAgreement)) {
            ParamUtil.setSessionAttr(bpc.request, "userAgreement", Boolean.TRUE);
        } else {
            ParamUtil.setSessionAttr(bpc.request, "userAgreement", Boolean.FALSE);
        }
        Map<String,  Map<String, String>> errMap = IaisCommonUtils.genNewHashMap();
        if (!StringUtil.isEmpty(renewEffectiveDate)) {
            Date date = Formatter.parseDate(renewEffectiveDate);
            if (date.before(new Date())||date.equals(new Date())) {
                ParamUtil.setRequestAttr(bpc.request, PAGE_SWITCH, PAGE2);
                Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
                errorMap.put("rfcEffectiveDate", "RFC_ERR012");
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                renewDto.getAppSubmissionDtos().get(0).setEffectiveDate(date);
                renewDto.getAppSubmissionDtos().get(0).setEffectiveDateStr(renewEffectiveDate);
                errMap.put("rfcEffectiveDate",errorMap);
            }
            renewDto.getAppSubmissionDtos().get(0).setEffectiveDate(date);
            renewDto.getAppSubmissionDtos().get(0).setEffectiveDateStr(renewEffectiveDate);
        }
        if(renewDto != null){
            List<AppSubmissionDto> appSubmissionDtos = renewDto.getAppSubmissionDtos();
            if(!IaisCommonUtils.isEmpty(appSubmissionDtos)){
                int count = 0;
                for(AppSubmissionDto appSubmissionDto:appSubmissionDtos){
                    Map<String,String> previewAndSubmitMap = IaisCommonUtils.genNewHashMap();
                    if(renewDto.getAppSubmissionDtos().size() > 1){
                        previewAndSubmitMap = appSubmissionService.doPreviewSubmitValidate(previewAndSubmitMap,appSubmissionDto,appSubmissionDto,bpc);
                    }else{
                        previewAndSubmitMap = appSubmissionService.doPreviewSubmitValidate(previewAndSubmitMap,appSubmissionDto,oldAppSubmissionDto,bpc);
                    }
                    errMap.put(appSubmissionDto.getServiceName()+count,previewAndSubmitMap);
                    count++;
                }
            }
        }
        boolean passValidate = true;
        Map<String,String> allErrMap = IaisCommonUtils.genNewHashMap();
        for(Map<String,String> errorMap:errMap.values()){
            if(!errorMap.isEmpty()){
                allErrMap.putAll(errorMap);
                passValidate = false;
            }
        }
        if (!passValidate) {
            if(renewDto.getAppSubmissionDtos().size() > 1){
                WebValidationHelper.saveAuditTrailForNoUseResult(allErrMap);
            }else{
                LicenceDto licenceDto = new LicenceDto();
                licenceDto.setLicenceNo(renewDto.getAppSubmissionDtos().get(0).getLicenceNo());
                WebValidationHelper.saveAuditTrailForNoUseResult(licenceDto,allErrMap);
            }
            ParamUtil.setRequestAttr(bpc.request,"needShowErr",AppConsts.TRUE);
            ParamUtil.setRequestAttr(bpc.request, "svcSecMaps", errMap);
            ParamUtil.setRequestAttr(bpc.request, PAGE_SWITCH, PAGE2);
            return;

        }


        if (renewDto != null) {
            List<AppSubmissionDto> appSubmissionDtos = renewDto.getAppSubmissionDtos();
            for (AppSubmissionDto appSubmissionDto : appSubmissionDtos) {
                List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
                Boolean otherOperation = requestForChangeService.isOtherOperation(appSubmissionDto.getLicenceId());
                if (!otherOperation) {
                    ParamUtil.setRequestAttr(bpc.request, PAGE_SWITCH, PAGE2);
                    return;
                }
                List<ApplicationDto> appByLicIdAndExcludeNew = requestForChangeService.getAppByLicIdAndExcludeNew(appSubmissionDto.getLicenceId());
                if (!IaisCommonUtils.isEmpty(appByLicIdAndExcludeNew)) {
                    bpc.request.setAttribute("rfcPendingApplication","errorRfcPendingApplication");
                    ParamUtil.setRequestAttr(bpc.request, PAGE_SWITCH, PAGE2);
                    return;
                }
                if(appSubmissionDtos.size()==1){
                    String licenceId = appSubmissionDto.getLicenceId();
                    LicenceDto licenceById = requestForChangeService.getLicenceById(licenceId);
                    if(licenceById.getSvcName()!=null){
                        HcsaServiceDto hcsaServiceDto = serviceConfigService.getActiveHcsaServiceDtoByName(licenceById.getSvcName());
                        List<String> serviceIds=IaisCommonUtils.genNewArrayList();
                        if(hcsaServiceDto!=null){
                            serviceIds.add(hcsaServiceDto.getId());
                            for(AppGrpPremisesDto appGrpPremisesDto : oldAppSubmissionDtoAppGrpPremisesDtoList){
                                boolean configIsChange = requestForChangeService.serviceConfigIsChange(serviceIds, appGrpPremisesDto.getPremisesType());
                                if(!configIsChange){
                                    rfc_err020=rfc_err020.replace("{ServiceName}",licenceById.getSvcName());
                                    bpc.request.setAttribute("SERVICE_CONFIG_CHANGE",rfc_err020);
                                    ParamUtil.setRequestAttr(bpc.request, PAGE_SWITCH, PAGE2);
                                    return;
                                }
                            }
                        }
                    }
                    boolean flag =NewApplicationDelegator.eqGrpPremises(appGrpPremisesDtoList, oldAppSubmissionDtoAppGrpPremisesDtoList);
                    log.info(StringUtil.changeForLog("flag is--"+flag));
                    if(flag){
                        for (int i = 0; i < appGrpPremisesDtoList.size(); i++) {
                            List<LicenceDto> licenceDtos = (List<LicenceDto>) bpc.request.getSession().getAttribute("selectLicence" + i);
                            if (licenceDtos != null) {
                                for (LicenceDto licenceDto : licenceDtos) {
                                    HcsaServiceDto hcsaServiceDto = serviceConfigService.getActiveHcsaServiceDtoByName(licenceDto.getSvcName());
                                    if(hcsaServiceDto!=null){
                                        List<String> serviceIds=IaisCommonUtils.genNewArrayList();
                                        if(hcsaServiceDto!=null){
                                            serviceIds.add(hcsaServiceDto.getId());
                                            boolean configIsChange = requestForChangeService.serviceConfigIsChange(serviceIds, appGrpPremisesDtoList.get(i).getPremisesType());
                                            if(!configIsChange){
                                                rfc_err020=rfc_err020.replace("{ServiceName}",licenceDto.getSvcName());
                                                bpc.request.setAttribute("SERVICE_CONFIG_CHANGE",rfc_err020);
                                                ParamUtil.setRequestAttr(bpc.request, PAGE_SWITCH, PAGE2);
                                                return;
                                            }
                                        }
                                    }
                                    List<ApplicationDto> appByLicIdAndExcludeNewOther = requestForChangeService.getAppByLicIdAndExcludeNew(licenceDto.getId());
                                    if (!IaisCommonUtils.isEmpty(appByLicIdAndExcludeNewOther)) {
                                        bpc.request.setAttribute("rfcPendingApplication","errorRfcPendingApplication");
                                        ParamUtil.setRequestAttr(bpc.request, PAGE_SWITCH, PAGE2);
                                        return;
                                    }
                                    Boolean otherLicenceOperation = requestForChangeService.isOtherOperation(licenceDto.getId());
                                    if (!otherLicenceOperation) {
                                        bpc.request.setAttribute("rfcPendingApplication","errorRfcPendingApplication");
                                        ParamUtil.setRequestAttr(bpc.request, PAGE_SWITCH, PAGE2);
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }else {
                    String licenceId = appSubmissionDto.getLicenceId();
                    LicenceDto licenceById = requestForChangeService.getLicenceById(licenceId);
                    if(licenceById.getSvcName()!=null){
                        HcsaServiceDto hcsaServiceDto = serviceConfigService.getActiveHcsaServiceDtoByName(licenceById.getSvcName());
                        List<String> serviceIds=IaisCommonUtils.genNewArrayList();
                        if(hcsaServiceDto!=null){
                            serviceIds.add(hcsaServiceDto.getId());
                            for(AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList){
                                boolean configIsChange = requestForChangeService.serviceConfigIsChange(serviceIds, appGrpPremisesDto.getPremisesType());
                                if(!configIsChange){
                                    rfc_err020=rfc_err020.replace("{ServiceName}",licenceById.getSvcName());
                                    bpc.request.setAttribute("SERVICE_CONFIG_CHANGE",rfc_err020);
                                    ParamUtil.setRequestAttr(bpc.request, PAGE_SWITCH, PAGE2);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
        //go page3
        ParamUtil.setRequestAttr(bpc.request, PAGE_SWITCH, PAGE3);
    }

    //doPayment
    public void doPayment(BaseProcessClass bpc) throws Exception {
        PaymentValidate paymentValidate = new PaymentValidate();
        Map<String, String> errorMap = paymentValidate.validate(bpc.request);
        if (!errorMap.isEmpty()) {
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, PAGE_SWITCH, PAGE3);
            return;
        }
        String backUrl = "/hcsa-licence-web/eservice/INTERNET/MohWithOutRenewal/1/preparatData";
        //set back url
        ParamUtil.setSessionAttr(bpc.request, "backUrl", backUrl);
        String payMethod = ParamUtil.getString(bpc.request, "payMethod");
        Double totalAmount = (Double) ParamUtil.getSessionAttr(bpc.request, "totalAmount");
        RenewDto renewDto = (RenewDto) ParamUtil.getSessionAttr(bpc.request, RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR);
        List<AppSubmissionDto> appSubmissionDtos = renewDto.getAppSubmissionDtos();
        for (AppSubmissionDto appSubmissionDto : appSubmissionDtos) {
            appSubmissionDto.setPaymentMethod(payMethod);
        }
        //
        ParamUtil.setSessionAttr(bpc.request, RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR, renewDto);
        String groupNo = "";
        String appGrpId = "";
        String licenseeId = null;
        if (appSubmissionDtos.size() != 0) {
            groupNo = appSubmissionDtos.get(0).getAppGrpNo();
            licenseeId = appSubmissionDtos.get(0).getLicenseeId();
            appGrpId = appSubmissionDtos.get(0).getAppGrpId();
        }
        if (ApplicationConsts.PAYMENT_METHOD_NAME_CREDIT.equals(payMethod)
                || ApplicationConsts.PAYMENT_METHOD_NAME_NETS.equals(payMethod)
                || ApplicationConsts.PAYMENT_METHOD_NAME_PAYNOW.equals(payMethod)) {
            Map<String, String> fieldMap = IaisCommonUtils.genNewHashMap();
            fieldMap.put(GatewayConstants.AMOUNT_KEY, String.valueOf(totalAmount));
            fieldMap.put(GatewayConstants.PYMT_DESCRIPTION_KEY, payMethod);
            fieldMap.put(GatewayConstants.SVCREF_NO, groupNo+"_"+System.currentTimeMillis());
            try {
                String html="";
                switch (payMethod){
                    case ApplicationConsts.PAYMENT_METHOD_NAME_CREDIT:
                        html = GatewayStripeAPI.create_partner_trade_by_buyer_url(fieldMap, bpc.request, backUrl);break;
                    case ApplicationConsts.PAYMENT_METHOD_NAME_NETS:
                        html = GatewayNetsAPI.create_partner_trade_by_buyer_url(fieldMap, bpc.request, backUrl);break;
                    case ApplicationConsts.PAYMENT_METHOD_NAME_PAYNOW:
                        html = GatewayAPI.create_partner_trade_by_buyer_url(fieldMap, bpc.request, backUrl);break;
                    default: html = GatewayAPI.create_partner_trade_by_buyer_url(fieldMap, bpc.request, backUrl);

                }
                bpc.response.sendRedirect(html);
                bpc.request.setAttribute("paymentAmount", totalAmount);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        } else if (ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(payMethod) && !StringUtil.isEmpty(appGrpId)) {
            ApplicationGroupDto appGrp = new ApplicationGroupDto();
            appGrp.setId(appGrpId);
            appGrp.setPmtStatus(serviceConfigService.giroPaymentXmlUpdateByGrpNo(appSubmissionDtos.get(0)).getPmtStatus());
            String giroTranNo = appSubmissionDtos.get(0).getGiroTranNo();
            appGrp.setPmtRefNo(giroTranNo);
            appGrp.setPayMethod(payMethod);
            serviceConfigService.updatePaymentStatus(appGrp);
            String txnDt = DateUtil.formatDate(new Date(), "dd/MM/yyyy");
            ParamUtil.setSessionAttr(bpc.request, "txnDt", txnDt);
            ParamUtil.setRequestAttr(bpc.request, PAGE_SWITCH, PAGE4);
            ParamUtil.setSessionAttr(bpc.request, "txnRefNo", giroTranNo);
            StringBuilder url = new StringBuilder();
            url.append("https://").append(bpc.request.getServerName())
                    .append("/hcsa-licence-web/eservice/INTERNET/MohWithOutRenewal/1/preparatData");
            RedirectUtil.redirect(url.toString(), bpc.request, bpc.response);
        }

    }

    //doAcknowledgement
    public void doAcknowledgement(BaseProcessClass bpc) throws Exception {
        String payMethod = ParamUtil.getString(bpc.request, "payMethod");
        String totalStr =(String)ParamUtil.getSessionAttr(bpc.request, "totalStr");
        if ("Credit".equals(payMethod)) {

        } else if("$0".equals(totalStr)){
            RenewDto renewDto= (RenewDto)bpc.request.getSession().getAttribute(RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR);
            if(renewDto!=null){
                List<AppSubmissionDto> appSubmissionDtos = renewDto.getAppSubmissionDtos();
                if(appSubmissionDtos!=null){
                    for(AppSubmissionDto appSubmissionDto : appSubmissionDtos){
                        String appGrpNo = appSubmissionDto.getAppGrpNo();
                        boolean autoRfc = appSubmissionDto.isAutoRfc();
                        List<ApplicationDto> applicationDtos = applicationFeClient.getApplicationsByGroupNo(appGrpNo).getEntity();
                        if(applicationDtos!=null&&!applicationDtos.isEmpty()){
                            for(ApplicationDto applicationDto : applicationDtos){
                                if(autoRfc){
                                    applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_APPROVED);
                                }else {
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
            List<AppSubmissionDto> rfcAppSubmissionDtos=(List<AppSubmissionDto>)bpc.request.getSession().getAttribute("rfcAppSubmissionDtos");
            if(rfcAppSubmissionDtos!=null){
                for(AppSubmissionDto appSubmissionDto : rfcAppSubmissionDtos){
                    String appGrpNo = appSubmissionDto.getAppGrpNo();
                    boolean autoRfc = appSubmissionDto.isAutoRfc();
                    List<ApplicationDto> entity = applicationFeClient.getApplicationsByGroupNo(appGrpNo).getEntity();
                    if(entity!=null&& !entity.isEmpty()){
                        for(ApplicationDto applicationDto : entity){
                            if(autoRfc){
                                applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_APPROVED);
                            }else {
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
            String txnDt = DateUtil.formatDate(new Date(), "dd/MM/yyyy");
            ParamUtil.setSessionAttr(bpc.request, "txnDt", txnDt);
            ParamUtil.setRequestAttr(bpc.request,PAGE_SWITCH,PAGE4);
        } else {
            ParamUtil.setRequestAttr(bpc.request, PAGE_SWITCH, PAGE3);
            return;
        }
    }

    //controlSwitch
    public void controlSwitch(BaseProcessClass bpc) throws Exception {
        String switch_value = ParamUtil.getString(bpc.request, "switch_value");
        if (INSTRUCTIONS.equals(switch_value)) {
            //controlSwitch
            ParamUtil.setRequestAttr(bpc.request, CONTROL_SWITCH, switch_value);
        } else if (REVIEW.equals(switch_value)) {
            ParamUtil.setRequestAttr(bpc.request, CONTROL_SWITCH, switch_value);
        } else if (PAYMENT.equals(switch_value)) {
            ParamUtil.setRequestAttr(bpc.request, CONTROL_SWITCH, switch_value);
        } else if (ACKNOWLEDGEMENT.equals(switch_value)) {
            ParamUtil.setRequestAttr(bpc.request, CONTROL_SWITCH, switch_value);
        } else if (PAGE1.equals(switch_value)) {
            ParamUtil.setRequestAttr(bpc.request, CONTROL_SWITCH, BACK);
            ParamUtil.setRequestAttr(bpc.request, PAGE_SWITCH, switch_value);
            String userAgreement = ParamUtil.getString(bpc.request, "verifyInfoCheckbox");
            if (!StringUtil.isEmpty(userAgreement) && AppConsts.YES.equals(userAgreement)) {
                ParamUtil.setSessionAttr(bpc.request, "userAgreement", Boolean.TRUE);
            } else {
                ParamUtil.setSessionAttr(bpc.request, "userAgreement", Boolean.FALSE);
            }
        } else if (PAGE2.equals(switch_value)) {
            ParamUtil.setRequestAttr(bpc.request, CONTROL_SWITCH, BACK);
            ParamUtil.setRequestAttr(bpc.request, PAGE_SWITCH, switch_value);
        } else if (EDIT.equals(switch_value)) {
            ParamUtil.setRequestAttr(bpc.request, CONTROL_SWITCH, EDIT);
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

        //todo: editvalue is not null and one licence to jump
        String editValue = ParamUtil.getString(bpc.request, "EditValue");
        if (!StringUtil.isEmpty(editValue)) {
            RenewDto renewDto = (RenewDto) ParamUtil.getSessionAttr(bpc.request, RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR);
            if (renewDto != null) {
                List<AppSubmissionDto> appSubmissionDtos = renewDto.getAppSubmissionDtos();
                if (!IaisCommonUtils.isEmpty(appSubmissionDtos) && appSubmissionDtos.size() == 1) {
                    ParamUtil.setRequestAttr(bpc.request, "EditValue", editValue);
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
    public void prepareJump(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do prepareJump start ...."));
        String editValue = (String) ParamUtil.getString(bpc.request, "EditValue");
        RenewDto renewDto = (RenewDto) ParamUtil.getSessionAttr(bpc.request, RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR);
        List<AppSubmissionDto> appSubmissionDtos = renewDto.getAppSubmissionDtos();
        AppSubmissionDto appSubmissionDto = appSubmissionDtos.get(0);
        AppEditSelectDto appEditSelectDto = appSubmissionDto.getAppEditSelectDto() == null ? new AppEditSelectDto() : appSubmissionDto.getAppEditSelectDto();
        if (!StringUtil.isEmpty(editValue)) {
            ParamUtil.setSessionAttr(bpc.request, PREFIXTITLE, "amending");
            if (RfcConst.EDIT_PREMISES.equals(editValue)) {
                appEditSelectDto.setPremisesEdit(true);
                ParamUtil.setRequestAttr(bpc.request, RfcConst.RFC_CURRENT_EDIT, RfcConst.EDIT_PREMISES);
            } else if (RfcConst.EDIT_PRIMARY_DOC.equals(editValue)) {
                appEditSelectDto.setDocEdit(true);
                ParamUtil.setRequestAttr(bpc.request, RfcConst.RFC_CURRENT_EDIT, RfcConst.EDIT_PRIMARY_DOC);
            } else if (RfcConst.EDIT_SERVICE.equals(editValue)) {
                appEditSelectDto.setServiceEdit(true);
                ParamUtil.setRequestAttr(bpc.request, RfcConst.RFC_CURRENT_EDIT, RfcConst.EDIT_SERVICE);
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
     *
     * @param bpc
     * @throws
     */
    public void toPrepareData(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do toPrepareData start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.APPSUBMISSIONDTO);
        if (appSubmissionDto != null) {
            AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
            appEditSelectDto.setPremisesEdit(true);
            appEditSelectDto.setDocEdit(true);
            appEditSelectDto.setServiceEdit(true);
            appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = IaisCommonUtils.genNewArrayList();
            List<Map<String, List<AppSvcDisciplineAllocationDto>>> reloadDisciplineAllocationMapList = IaisCommonUtils.genNewArrayList();
            List<List<AppSvcPrincipalOfficersDto>> principalOfficersDtosList = IaisCommonUtils.genNewArrayList();
            List<List<AppSvcPrincipalOfficersDto>> deputyPrincipalOfficersDtosList = IaisCommonUtils.genNewArrayList();
            List<String> serviceNames = IaisCommonUtils.genNewArrayList();
            if (!appSubmissionDto.getAppSvcRelatedInfoDtoList().isEmpty()) {
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

            ParamUtil.setSessionAttr(bpc.request, "currentPreviewSvcInfoList", (Serializable) appSvcRelatedInfoDtoList);
            ParamUtil.setSessionAttr(bpc.request, "reloadDisciplineAllocationMapList", (Serializable) reloadDisciplineAllocationMapList);
            ParamUtil.setSessionAttr(bpc.request, "ReloadPrincipalOfficersList", (Serializable) principalOfficersDtosList);
            ParamUtil.setSessionAttr(bpc.request, "deputyPrincipalOfficersDtosList", (Serializable) deputyPrincipalOfficersDtosList);
        }
        RenewDto renewDto = new RenewDto();
        List<AppSubmissionDto> appSubmissionDtos = IaisCommonUtils.genNewArrayList();
        appSubmissionDtos.add(appSubmissionDto);
        renewDto.setAppSubmissionDtos(appSubmissionDtos);
        ParamUtil.setSessionAttr(bpc.request, RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR, renewDto);
        ParamUtil.setRequestAttr(bpc.request, PAGE_SWITCH, PAGE2);
        ParamUtil.setRequestAttr(bpc.request, RfcConst.FIRSTVIEW, AppConsts.TRUE);
        log.info(StringUtil.changeForLog("the do toPrepareData end ...."));
    }

    public static String emailAddressesToString(List<String> emailAddresses) {
        StringBuilder emailAddress = new StringBuilder();
        if (emailAddresses.isEmpty()) {
            return emailAddress.toString();
        }
        if (emailAddresses.size() == 1) {
            emailAddress.append(emailAddresses.get(0));
        } else {
            for (int i = 0; i < emailAddresses.size(); i++) {
                if (i == emailAddresses.size() - 1) {
                    emailAddress.append(emailAddresses.get(i));
                } else {
                    emailAddress.append(emailAddresses.get(i)).append(", ");
                }
            }
        }
        return emailAddress.toString();
    }
    //=============================================================================
    //private method
    //=============================================================================

    public void sendEmail(HttpServletRequest request, List<AppSubmissionDto> appSubmissionDtos){
        if (!IaisCommonUtils.isEmpty(appSubmissionDtos)) {
            String paymentMethod = appSubmissionDtos.get(0).getPaymentMethod();
            String applicationTypeShow = MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_TYPE_RENEWAL);
            String MohName = AppConsts.MOH_AGENCY_NAME;
            String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
            String paymentMethodName = "onlinePayment";
            String groupNo = appSubmissionDtos.get(0).getAppGrpNo();
            LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
            String applicationName = loginContext.getUserName();
            log.info(StringUtil.changeForLog("send renewal application notification applicationName : " + applicationName));
            log.info(StringUtil.changeForLog("send renewal application notification paymentMethod : " + paymentMethod));
            int index = 1;
            String appNo = groupNo;
            String appDate = Formatter.formatDateTime(new Date(), "dd/MM/yyyy");
            for (AppSubmissionDto appSubmissionDto : appSubmissionDtos) {
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(appSubmissionDto.getServiceName());
                List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
                svcCodeList.add(hcsaServiceDto.getSvcCode());
                String amountStr = (String)ParamUtil.getSessionAttr(request, "totalStr");
                appNo = getAppNo(groupNo,index);
                log.info(StringUtil.changeForLog("send renewal application notification application no : " + appNo));
                log.info(StringUtil.changeForLog("send renewal application notification amountStr : " + amountStr));
                Map<String, Object> map = IaisCommonUtils.genNewHashMap();
                map.put("ApplicantName", applicationName);
                map.put("ApplicationType", applicationTypeShow);
                map.put("ApplicationNumber", appNo);
                map.put("ApplicationDate", appDate);
                map.put("paymentAmount", amountStr);
                map.put("systemLink", loginUrl);
                map.put("emailAddress", systemAddressOne);
                map.put("MOH_AGENCY_NAME", MohName);
                if (ApplicationConsts.PAYMENT_METHOD_NAME_CREDIT.equals(paymentMethod)
                        || ApplicationConsts.PAYMENT_METHOD_NAME_NETS.equals(paymentMethod)
                        || ApplicationConsts.PAYMENT_METHOD_NAME_PAYNOW.equals(paymentMethod)) {
                    paymentMethodName = "onlinePayment";
                    map.put("paymentMethod", paymentMethodName);
                    log.info(StringUtil.changeForLog("send renewal application notification paymentMethodName : " + paymentMethodName));
                } else if (ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(paymentMethod)) {
                    //todo GIRO payment method
//                    paymentMethodName = "GIRO";
                }
                try {
//                    String subject = "MOH HALP - Your "+ applicationTypeShow + ", "+ appNo +" has been submitted";
                    Map<String, Object> subMap = IaisCommonUtils.genNewHashMap();
                    subMap.put("ApplicationType", applicationTypeShow);
                    subMap.put("ApplicationNumber", appNo);
                    String emailSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_SUBMITTED,subMap);
                    String smsSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_SUBMITTED_SMS,subMap);
                    String messageSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_SUBMITTED_MESSAGE,subMap);
                    log.debug(StringUtil.changeForLog("emailSubject : " + emailSubject));
                    log.debug(StringUtil.changeForLog("smsSubject : " + smsSubject));
                    log.debug(StringUtil.changeForLog("messageSubject : " + messageSubject));
                    EmailParam emailParam = new EmailParam();
                    emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_SUBMITTED);
                    emailParam.setTemplateContent(map);
                    emailParam.setQueryCode(appNo);
                    emailParam.setReqRefNum(appNo);
                    emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
                    emailParam.setRefId(appNo);
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
                    smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
                    smsParam.setRefId(appNo);
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
                }catch (Exception e){
                    log.error(e.getMessage(), e);
                }
                index ++;
            }
            log.info(StringUtil.changeForLog("send renewal application notification end"));
        }
    }

    private String getEmailSubject(String templateId,Map<String, Object> subMap){
        String subject = "-";
        if(!StringUtil.isEmpty(templateId)){
            MsgTemplateDto emailTemplateDto =appSubmissionService.getMsgTemplateById(templateId);
            if(emailTemplateDto != null){
                try {
                    if(!IaisCommonUtils.isEmpty(subMap)){
                        subject = MsgUtil.getTemplateMessageByContent(emailTemplateDto.getTemplateName(),subMap);
                    }else{
                        subject = emailTemplateDto.getTemplateName();
                    }
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                }
            }
        }
        return subject;
    }

    private String getAppNo(String groupNo,int index){
        StringBuilder appNo = new StringBuilder(groupNo);
        appNo.append('-');
        if(index > 9){
            appNo.append(index);
        }else{
            appNo.append('0').append(index);
        }
        return appNo.toString();
    }

}
