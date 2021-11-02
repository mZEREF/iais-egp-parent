package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.api.config.GatewayConstants;
import com.ecquaria.cloud.moh.iais.api.services.GatewayAPI;
import com.ecquaria.cloud.moh.iais.api.services.GatewayNetsAPI;
import com.ecquaria.cloud.moh.iais.api.services.GatewayPayNowAPI;
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
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppFeeDetailsDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppSvcPersonAndExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremPhOpenPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesOperationalUnitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDisciplineAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationSubDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.RenewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.HcsaFeeBundleItemDto;
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
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceFeConstant;
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
import com.ecquaria.cloud.moh.iais.rfcutil.EqRequestForChangeSubmitResultChange;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.WithOutRenewalService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationFeClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigFeClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationLienceseeClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import com.ecquaria.cloud.moh.iais.service.impl.ServiceInfoChangeEffectPersonForRFC;
import com.ecquaria.cloud.moh.iais.validate.declarationsValidate.DeclarationsUtil;
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
import java.util.Iterator;
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
    private static final String PREFIXTITLE = "prefixTitle";
    private static final String LOADING_DRAFT = "loadingDraft";
    private static final String SINGLE_SERVICE = "isSingle";
    @Autowired
    WithOutRenewalService outRenewalService;

    @Autowired
    AppSubmissionService appSubmissionService;

    @Autowired
    ServiceConfigService serviceConfigService;
    @Autowired
    private SystemAdminClient systemAdminClient;
    @Autowired
    NewApplicationDelegator newApplicationDelegator;
    @Autowired
    private HcsaConfigFeClient hcsaConfigFeClient;
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

    @Autowired
    private LicenceClient licenceClient;

    @Autowired
    private ServiceInfoChangeEffectPersonForRFC serviceInfoChangeEffectPersonForRFC;

    public void start(BaseProcessClass bpc) throws Exception {
        log.info("**** the non auto renwal  start ******");
        HcsaServiceCacheHelper.flushServiceMapping();
        String draftNo = ParamUtil.getMaskedString(bpc.request, "DraftNumber");
        //init session
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_RENEW, AuditTrailConsts.FUNCTION_RENEW);
        ParamUtil.setSessionAttr(bpc.request, RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR, null);
        ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.APPSUBMISSIONDTO, null);
        ParamUtil.setSessionAttr(bpc.request,NewApplicationDelegator.PRIMARY_DOC_CONFIG, null);
        ParamUtil.setSessionAttr(bpc.request, "totalStr", null);
        ParamUtil.setSessionAttr(bpc.request, "totalAmount", null);
        ParamUtil.setSessionAttr(bpc.request, "userAgreement", null);
        ParamUtil.setSessionAttr(bpc.request, PREFIXTITLE, "renewing");
        ParamUtil.setSessionAttr(bpc.request,"paymentMessageValidateMessage",null);
        ParamUtil.setSessionAttr(bpc.request, LOADING_DRAFT, null);
        ParamUtil.setSessionAttr(bpc.request, "oldAppSubmissionDto", null);
        ParamUtil.setSessionAttr(bpc.request, "oldRenewAppSubmissionDto", null);
        ParamUtil.setSessionAttr(bpc.request, "requestInformationConfig", null);
        ParamUtil.setSessionAttr(bpc.request, "rfc_eqHciCode", null);
        ParamUtil.setSessionAttr(bpc.request, HcsaLicenceFeConstant.DASHBOARDTITLE,"");
        ParamUtil.setSessionAttr(bpc.request, "seesion_files_map_ajax_feselectedDeclFile", null);
        ParamUtil.setSessionAttr(bpc.request, "pageShowFileDtos", null);
        ParamUtil.setSessionAttr(bpc.request, "selectedRENEWFileDocShowPageDto", null);
        ParamUtil.setSessionAttr(bpc.request, "selectedRFCFileDocShowPageDto", null);
        ParamUtil.setSessionAttr(bpc.request, "selectedNewFileDocShowPageDto", null);
        bpc.request.getSession().removeAttribute("seesion_files_map_ajax_feselectedRENEWFile");
        bpc.request.getSession().removeAttribute("seesion_files_map_ajax_feselectedRENEWFile_MaxIndex");
        bpc.request.getSession().removeAttribute("declaration_page_is");
        ParamUtil.setSessionAttr(bpc.request, "viewPrint",null);
        ParamUtil.setSessionAttr(bpc.request,HcsaFileAjaxController.GLOBAL_MAX_INDEX_SESSION_ATTR,0);
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
                //set max file index into session
                Integer maxFileIndex = appSubmissionDtoList.get(0).getMaxFileIndex();
                if(maxFileIndex == null){
                    maxFileIndex = 0;
                }else{
                    maxFileIndex ++;
                }
                ParamUtil.setSessionAttr(bpc.request,HcsaFileAjaxController.GLOBAL_MAX_INDEX_SESSION_ATTR,maxFileIndex);
            }
            log.info("can not find licence id for without renewal");
            ParamUtil.setSessionAttr(bpc.request, "backUrl", "initLic");
        } else {
            AppSubmissionDto appSubmissionDtoDraft = serviceConfigService.getAppSubmissionDtoDraft(draftNo);
            appSubmissionService.initDeclarationFiles(appSubmissionDtoDraft.getAppDeclarationDocDtos(),appSubmissionDtoDraft.getAppType(),bpc.request);
            requestForChangeService.svcDocToPresmise(appSubmissionDtoDraft);
            ParamUtil.setSessionAttr(bpc.request, LOADING_DRAFT, "test");
            appSubmissionDtoDraft.setOneLicDoRenew(true);
            appSubmissionDtoList.add(appSubmissionDtoDraft);
            licenceIDList = new ArrayList<>(1);
            licenceIDList.add(appSubmissionDtoDraft.getLicenceId());
            ParamUtil.setSessionAttr(bpc.request, "backUrl", "initApp");
            loadCoMap(bpc, appSubmissionDtoDraft);
            //set max file index into session
            Integer maxFileIndex = appSubmissionDtoDraft.getMaxFileIndex();
            if(maxFileIndex == null){
                maxFileIndex = 0;
            }else{
                maxFileIndex ++;
            }
            ParamUtil.setSessionAttr(bpc.request,HcsaFileAjaxController.GLOBAL_MAX_INDEX_SESSION_ATTR,maxFileIndex);
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
        List<HcsaSvcDocConfigDto> primaryDocConfig = serviceConfigService.getAllHcsaSvcDocs(null);
        ParamUtil.setSessionAttr(bpc.request,NewApplicationDelegator.PRIMARY_DOC_CONFIG, (Serializable) primaryDocConfig);
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
                appSvcRelatedInfoDto.setServiceName(hcsaServiceDto.getSvcName());
                appSvcRelatedInfoDto.setServiceType(hcsaServiceDto.getSvcType());
                //set AppSvcRelatedInfoDtoList chkName
                List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos = serviceConfigService.loadLaboratoryDisciplines(svcId);
//                NewApplicationHelper.setLaboratoryDisciplinesInfo(appSubmissionDto, hcsaSvcSubtypeOrSubsumedDtos);
                //use new config id
                appSubmissionService.changeSvcScopeIdByConfigName(hcsaSvcSubtypeOrSubsumedDtos,appSubmissionDto);
                //set address
                NewApplicationHelper.setPremAddress(appSubmissionDto);
                Map<String, List<AppSvcDisciplineAllocationDto>> reloadDisciplineAllocationMap = appSubmissionService.getDisciplineAllocationDtoList(appSubmissionDto, svcId);
                reloadDisciplineAllocationMapList.add(reloadDisciplineAllocationMap);
                //set svc step
                List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemesByServiceId = serviceConfigService.getHcsaServiceStepSchemesByServiceId(svcId);
                appSvcRelatedInfoDto.setHcsaServiceStepSchemeDtos(hcsaServiceStepSchemesByServiceId);
                //set doc info
                requestForChangeService.svcDocToPresmise(appSubmissionDto);
                List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionDto.getAppGrpPrimaryDocDtos();
                boolean isRfi = false;
                //rfc/renew for primary doc
                List<AppGrpPrimaryDocDto> newGrpPrimaryDocList = appSubmissionService.syncPrimaryDoc(ApplicationConsts.APPLICATION_TYPE_RENEWAL,isRfi,appGrpPrimaryDocDtos,primaryDocConfig);
                appSubmissionDto.setAppGrpPrimaryDocDtos(newGrpPrimaryDocList);

                if (!StringUtil.isEmpty(svcId)) {
                    List<AppSvcDocDto> appSvcDocDtos = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
                    List<String> svcDocConfigIdList = IaisCommonUtils.genNewArrayList();
                    if(!IaisCommonUtils.isEmpty(appSvcDocDtos)){
                        for(AppSvcDocDto appSvcDocDto:appSvcDocDtos){
                            svcDocConfigIdList.add(appSvcDocDto.getSvcDocId());
                        }
                    }
                    List<HcsaSvcDocConfigDto>  oldSvcDocConfigDtos = serviceConfigService.getPrimaryDocConfigByIds(svcDocConfigIdList);
                    List<HcsaSvcDocConfigDto> svcDocConfig = serviceConfigService.getAllHcsaSvcDocs(svcId);
                    appSvcDocDtos = requestForChangeService.updateSvcDoc(appSvcDocDtos,oldSvcDocConfigDtos,svcDocConfig);
                    //svc doc set align
                    if(appGrpPremisesDtos != null && appGrpPremisesDtos.size() > 0){
                        String premTye = appGrpPremisesDtos.get(0).getPremisesType();
                        String premVal = appGrpPremisesDtos.get(0).getPremisesIndexNo();
                        if(!IaisCommonUtils.isEmpty(appSvcDocDtos)){
                            for(AppSvcDocDto appSvcDocDto:appSvcDocDtos){
                                HcsaSvcDocConfigDto docConfig = NewApplicationHelper.getHcsaSvcDocConfigDtoById(svcDocConfig,appSvcDocDto.getSvcDocId());
                                if(docConfig != null && "1".equals(docConfig.getDupForPrem())){
                                    appSvcDocDto.setPremisesVal(premVal);
                                    appSvcDocDto.setPremisesType(premTye);
                                }
                            }
                        }
                    }
                    appSvcRelatedInfoDto.setAppSvcDocDtoLit(appSvcDocDtos);
                }

                List<AppSvcPrincipalOfficersDto> appSvcCgoDtos = NewApplicationHelper.transferCgoToPsnDtoList(appSvcRelatedInfoDto.getAppSvcCgoDtoList());
                String svcCode = appSvcRelatedInfoDto.getServiceCode();
                NewApplicationHelper.initSetPsnIntoSelMap(licPersonMap, appSvcCgoDtos, svcCode);
                //reset dto
                List<AppSvcPrincipalOfficersDto> newCgoDtoList = IaisCommonUtils.genNewArrayList();
                for (AppSvcPrincipalOfficersDto item : appSvcCgoDtos) {
                    newCgoDtoList.add(MiscUtil.transferEntityDto(item, AppSvcPrincipalOfficersDto.class));
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
                    ParamUtil.setSessionAttr(bpc.request, SINGLE_SERVICE, "Y");
                    ParamUtil.setSessionAttr(bpc.request,"renew_licence_no",appSubmissionDto.getLicenceNo());
                } else {
                    ParamUtil.setSessionAttr(bpc.request, SINGLE_SERVICE, "N");
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
        if (appSubmissionDtos != null && appSubmissionDtos.size() != 0) {
            groupId = appSubmissionDtos.get(0).getAppGrpId();
            AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) bpc.request.getSession().getAttribute("oldRenewAppSubmissionDto");
            boolean flag=false;
            if (oldAppSubmissionDto == null) {
                oldAppSubmissionDto = appSubmissionDtos.get(0);
                flag=true;
            }
            Object loadingDraft = ParamUtil.getSessionAttr(bpc.request, LOADING_DRAFT);
            if (loadingDraft != null) {
                AppSubmissionDto oldAppSubmisDto = appSubmissionDtos.get(0).getOldRenewAppSubmissionDto();
                if (oldAppSubmisDto != null) {
                    oldAppSubmissionDto = oldAppSubmisDto;
                }
            }
            requestForChangeService.svcDocToPresmise(oldAppSubmissionDto);
            if (!IaisCommonUtils.isEmpty(appSubmissionDtos)) {
                for (AppSubmissionDto appSubmissionDto : appSubmissionDtos) {
                    requestForChangeService.svcDocToPresmise(appSubmissionDto);
                }
            }
            if(flag){
                if(renewDto.getAppSubmissionDtos().get(0).getOldRenewAppSubmissionDto()==null){
                    Object object = CopyUtil.copyMutableObject(oldAppSubmissionDto);
                    renewDto.getAppSubmissionDtos().get(0).setOldRenewAppSubmissionDto((AppSubmissionDto)object);
                }
                bpc.request.getSession().setAttribute("oldRenewAppSubmissionDto", oldAppSubmissionDto);
            }
        }
        String result = ParamUtil.getMaskedString(bpc.request, "result");
        if(appSubmissionDtos != null && appSubmissionDtos.size() > 0){
            appSubmissionService.updateDraftStatus( appSubmissionDtos.get(0).getDraftNo(),AppConsts.COMMON_STATUS_ACTIVE);
        }
        String err24 = MessageUtil.getMessageDesc("NEW_ERR0024");
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
                    log.error(StringUtil.changeForLog("send email error"));
                }
                ParamUtil.setRequestAttr(bpc.request, PAGE_SWITCH, PAGE4);
            } else {
                Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
                errorMap.put("pay", err24);
                ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
                //jump page to payment
                ParamUtil.setRequestAttr(bpc.request, PAGE_SWITCH, PAGE3);
            }
        }else if(appSubmissionDtos.get(0).getPaymentMethod()!=null&&appSubmissionDtos.get(0).getPaymentMethod().equals(ApplicationConsts.PAYMENT_METHOD_NAME_GIRO)){
            ParamUtil.setRequestAttr(bpc.request, PAGE_SWITCH, PAGE4);
        }else if(appSubmissionDtos.get(0).getPaymentMethod()!=null&&(appSubmissionDtos.get(0).getPaymentMethod().equals(ApplicationConsts.PAYMENT_METHOD_NAME_CREDIT)||appSubmissionDtos.get(0).getPaymentMethod().equals(ApplicationConsts.PAYMENT_METHOD_NAME_NETS))) {
            String switch_value = ParamUtil.getString(bpc.request, "switch_value");
            if(switch_value==null||PAYMENT.equals(switch_value)){
                Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
                errorMap.put("pay", err24);
                ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, PAGE_SWITCH, PAGE3);
            }

        }
        if(!IaisCommonUtils.isEmpty(appSubmissionDtos)) {
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
        if(!IaisCommonUtils.isEmpty(newAppSubmissionDtos)){
            List<HcsaSvcDocConfigDto> primaryDocConfig = (List<HcsaSvcDocConfigDto>) ParamUtil.getSessionAttr(bpc.request,NewApplicationDelegator.PRIMARY_DOC_CONFIG);
            for(AppSubmissionDto appSubmissionDto:newAppSubmissionDtos){

                List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
                List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionDto.getAppGrpPrimaryDocDtos();
                if(IaisCommonUtils.isEmpty(primaryDocConfig) && appGrpPrimaryDocDtos != null && appGrpPrimaryDocDtos.size() > 0){
                    primaryDocConfig = serviceConfigService.getPrimaryDocConfigById(appGrpPrimaryDocDtos.get(0).getSvcComDocId());
                }
                //add align for dup for prem doc
                NewApplicationHelper.addPremAlignForPrimaryDoc(primaryDocConfig,appGrpPrimaryDocDtos,appGrpPremisesDtos);
                //set primary doc title
                Map<String,List<AppGrpPrimaryDocDto>> reloadPrimaryDocMap = NewApplicationHelper.genPrimaryDocReloadMap(primaryDocConfig,appGrpPremisesDtos,appGrpPrimaryDocDtos);
                appSubmissionDto.setMultipleGrpPrimaryDoc(reloadPrimaryDocMap);

                List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
                if(appSvcRelatedInfoDtos != null && appSvcRelatedInfoDtos.size() > 0){
                    AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSvcRelatedInfoDtos.get(0);
                    String svcId = appSvcRelatedInfoDto.getServiceId();
                    if(!StringUtil.isEmpty(svcId)){
                        List<AppSvcDocDto> appSvcDocDtos = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
                        List<HcsaSvcDocConfigDto> svcDocConfig = serviceConfigService.getAllHcsaSvcDocs(svcId);
                        appSvcRelatedInfoDto.setSvcDocConfig(svcDocConfig);
                        //set dupForPsn attr
                        NewApplicationHelper.setDupForPersonAttr(bpc.request,appSvcRelatedInfoDto);
                        //svc doc add align for dup for prem
                        NewApplicationHelper.addPremAlignForSvcDoc(svcDocConfig,appSvcDocDtos,appGrpPremisesDtos);
                        appSvcRelatedInfoDto.setAppSvcDocDtoLit(appSvcDocDtos);
                        //set svc doc title
                        Map<String,List<AppSvcDocDto>> reloadSvcDocMap = NewApplicationHelper.genSvcDocReloadMap(svcDocConfig,appGrpPremisesDtos,appSvcRelatedInfoDto);
                        appSvcRelatedInfoDto.setMultipleSvcDoc(reloadSvcDocMap);
                        List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
                        if(appSvcPrincipalOfficersDtoList!=null){
                            List<AppSvcPrincipalOfficersDto> poList=new ArrayList<>(appSvcPrincipalOfficersDtoList.size());
                            List<AppSvcPrincipalOfficersDto> dpoList=new ArrayList<>(appSvcPrincipalOfficersDtoList.size());
                            for(AppSvcPrincipalOfficersDto aspoDto : appSvcPrincipalOfficersDtoList){
                                if(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(aspoDto.getPsnType())){
                                    dpoList.add(aspoDto);
                                }else if (ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(aspoDto.getPsnType())){
                                    poList.add(aspoDto);
                                }
                            }
                            appSvcRelatedInfoDto.setPoList(poList);
                            appSvcRelatedInfoDto.setDpoList(dpoList);
                        }
                    }
                }

            }
           if(newAppSubmissionDtos.size()==1){
               appSubmissionService.initDeclarationFiles(newAppSubmissionDtos.get(0).getAppDeclarationDocDtos(),ApplicationConsts.APPLICATION_TYPE_RENEWAL,bpc.request);
           }
            appSubmissionService.setPreviewDta(newAppSubmissionDtos.get(0),bpc);
        }
        if (!IaisCommonUtils.isEmpty(oldSubmissionDtos) && !IaisCommonUtils.isEmpty(newAppSubmissionDtos)) {
            List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList = oldSubmissionDtos.get(0).getAppSvcRelatedInfoDtoList();
            List<AppGrpPremisesDto> oldAppGrpPremisesDtoList = oldSubmissionDtos.get(0).getAppGrpPremisesDtoList();
            List<AppSvcRelatedInfoDto> newAppSvcRelatedInfoDtoList = newAppSubmissionDtos.get(0).getAppSvcRelatedInfoDtoList();
            List<AppGrpPremisesDto> newAppGrpPremisesDtoList = newAppSubmissionDtos.get(0).getAppGrpPremisesDtoList();
            boolean replacePerson = outRenewalService.isReplace(newAppSvcRelatedInfoDtoList, oldAppSvcRelatedInfoDtoList);
            log.debug(StringUtil.changeForLog("replacePerson"+replacePerson));
            boolean updatePerson = outRenewalService.isUpdate(newAppSvcRelatedInfoDtoList, oldAppSvcRelatedInfoDtoList);
            log.debug(StringUtil.changeForLog("updatePerson"+updatePerson));
            boolean editDoc = outRenewalService.isEditDoc(newAppSubmissionDtos.get(0), oldSubmissionDtos.get(0));
            log.debug(StringUtil.changeForLog("editDoc"+editDoc));
            List<AppSvcPrincipalOfficersDto> poAndDpo = newAppSvcRelatedInfoDtoList.get(0).getAppSvcPrincipalOfficersDtoList();
            if(!IaisCommonUtils.isEmpty(poAndDpo)){
                poAndDpo.sort((h1,h2)->h2.getPsnType().compareTo(h1.getPsnType()));
                newAppSvcRelatedInfoDtoList.get(0).setAppSvcPrincipalOfficersDtoList(poAndDpo);
            }
            boolean eqGrpPremisesResult = EqRequestForChangeSubmitResultChange.isChangeGrpPremises(newAppGrpPremisesDtoList,
                    oldAppGrpPremisesDtoList);
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
        AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) bpc.request.getSession().getAttribute("oldRenewAppSubmissionDto");
        List<AppSubmissionDto> appSubmissionDtos = renewDto.getAppSubmissionDtos();
        List<AppFeeDetailsDto> appFeeDetailsDto = IaisCommonUtils.genNewArrayList();
        boolean needDec = true;

        AppEditSelectDto appEditSelectDto = EqRequestForChangeSubmitResultChange.rfcChangeModuleEvaluationDto( appSubmissionDtos.get(0),oldAppSubmissionDto);

        NewApplicationHelper.reSetAdditionalFields(appSubmissionDtos.get(0),appEditSelectDto);

        setMacFileIndex(appSubmissionDtos,bpc.request);

        //app submit
         String licenseeId  = getLicenseeIdByLoginInfo(bpc.request);

        List<AppGrpPremisesDto> oldAppSubmissionDtoAppGrpPremisesDtoList = oldAppSubmissionDto.getAppGrpPremisesDtoList();
        List<AppSubmissionDto> rfcAppSubmissionDtos = IaisCommonUtils.genNewArrayList();
        List<AppSubmissionDto> autoAppSubmissionDtos = IaisCommonUtils.genNewArrayList();
        List<AppSubmissionDto> noAutoAppSubmissionDtos = IaisCommonUtils.genNewArrayList();

        for (AppSubmissionDto appSubmissionDto : appSubmissionDtos) {

            if(StringUtil.isEmpty(appSubmissionDto.getAppGrpNo())){
                appSubmissionDto.setAppGrpNo(systemAdminClient.applicationNumber(ApplicationConsts.APPLICATION_TYPE_RENEWAL).getEntity());
            }
            requestForChangeService.setRelatedInfoBaseServiceId(appSubmissionDto);
            setPreInspectionAndRequirement(appSubmissionDto);
            appSubmissionDto.setLicenseeId(licenseeId);
            appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
            appSubmissionDto.setChangeSelectDto(appEditSelectDto);
            requestForChangeService.premisesDocToSvcDoc(appSubmissionDto);
        }

        String autoGrpNo = appSubmissionService.getGroupNo(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);

        if(appSubmissionDtos.size() == 1){
            AppSubmissionDto appSubmissionDto = appSubmissionDtos.get(0);
            // create rfc data
            List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
                if(appGrpPremisesDtoList != null){
                   /* List<AppGrpPremisesDto> autoPremisesDtos = EqRequestForChangeSubmitResultChange.generateDtosForAutoFields(
                            appGrpPremisesDtoList,oldAppSubmissionDtoAppGrpPremisesDtoList, appEditSelectDto);
                    boolean changeAutoFields = EqRequestForChangeSubmitResultChange.isChangeGrpPremises(autoPremisesDtos,
                            oldAppSubmissionDtoAppGrpPremisesDtoList);*/
                    AppSubmissionDto autoAppSubmissionDto = getAutoSubForRenewSelf(appEditSelectDto,appSubmissionDto);
                    AppEditSelectDto autoChangeSelectDto =  autoAppSubmissionDto != null ? autoAppSubmissionDto.getChangeSelectDto() : null;
                    int isAutoPremises = isAutoPremises(appEditSelectDto);
                    if (appEditSelectDto.isPremisesEdit()) {
                        for (int i = 0; i < appGrpPremisesDtoList.size(); i++) {
                            setRfcHciNameChanged(appGrpPremisesDtoList,oldAppSubmissionDtoAppGrpPremisesDtoList,i);
                            List<LicenceDto> licenceDtos = (List<LicenceDto>) bpc.request.getSession().getAttribute("selectLicence" + i);
                            if (licenceDtos != null) {
                                for (LicenceDto licenceDto : licenceDtos) {
                                    AppSubmissionDto appSubmissionDtoByLicenceId = requestForChangeService.getAppSubmissionDtoByLicenceId(licenceDto.getId());
                                    setRfcPremisesSubmissionDto(appSubmissionDtoByLicenceId,licenseeId,appGrpPremisesDtoList,appSubmissionDto,i,MiscUtil.transferEntityDto(appEditSelectDto,AppEditSelectDto.class));
                                    if (appSubmissionDtoByLicenceId.isAutoRfc()) {
                                        autoAppSubmissionDtos.add(appSubmissionDtoByLicenceId);
                                    } else {
                                        noAutoAppSubmissionDtos.add(appSubmissionDtoByLicenceId);
                                    }
                                }
                            }
                        }
                    }

                    if(appEditSelectDto.isLicenseeEdit()){
                        //gen lic change rfc
                        NewApplicationHelper.addToAuto(getAutoChangeLicAppSubmissions(oldAppSubmissionDto,autoGrpNo,appSubmissionDto), autoAppSubmissionDtos);
                        // re-set change edit select dto
                        if (autoAppSubmissionDto != null) {
                            autoChangeSelectDto.setLicenseeEdit(true);
                            appEditSelectDto.setLicenseeEdit(false);
                        }
                    }

                    if(appEditSelectDto.isServiceEdit()){
                        List<AppSubmissionDto> personAppSubmissionList = serviceInfoChangeEffectPersonForRFC.personContact(licenseeId, appSubmissionDtos.get(0), oldAppSubmissionDto);
                        NewApplicationHelper.addToAuto(personAppSubmissionList, autoAppSubmissionDtos);
                        List<String> changeList = appSubmissionDto.getChangeSelectDto().getPersonnelEditList();
                        if (! appEditSelectDto.isAutoRfc() && !IaisCommonUtils.isEmpty(changeList) && autoAppSubmissionDto == null) {
                            autoAppSubmissionDto = (AppSubmissionDto) com.ecquaria.cloud.moh.iais.common.utils.CopyUtil.copyMutableObject(appSubmissionDto);
                            autoAppSubmissionDto.setAmount(0.0);
                            autoChangeSelectDto = new AppEditSelectDto();
                            autoAppSubmissionDto.setChangeSelectDto(autoChangeSelectDto);
                        }
                        if (autoAppSubmissionDto != null) {
                            autoChangeSelectDto.setServiceEdit(true);
                            autoAppSubmissionDto.setAppSvcRelatedInfoDtoList(
                                    serviceInfoChangeEffectPersonForRFC.generateDtosForAutoFields(autoAppSubmissionDto, oldAppSubmissionDto,
                                            changeList, appSubmissionDto.getAppEditSelectDto().getPersonnelEditList()));
                            // re-set change edit select dto
                            if (!appEditSelectDto.isChangeBusinessName() && !appEditSelectDto.isChangeVehicle() && !appEditSelectDto.isChangePersonnel()) {
                                appEditSelectDto.setServiceEdit(false);
                            }
                        }
                    }

                    if (appEditSelectDto.isDocEdit() && autoAppSubmissionDto != null) {
                        appEditSelectDto.setDocEdit(false);
                        autoChangeSelectDto.setDocEdit(true);
                    }
                    // re-set change edit select dto
                    if (1 == isAutoPremises) {
                        appEditSelectDto.setPremisesEdit(false);
                        appEditSelectDto.setPremisesListEdit(false);
                    }
                    // add the current auto app submission
                    if (autoAppSubmissionDto != null) {
                        if (1 == isAutoPremises || 2 == isAutoPremises) {
                            autoChangeSelectDto.setPremisesEdit(true);
                            autoChangeSelectDto.setPremisesListEdit(true);
                        }
                        NewApplicationHelper.reSetAdditionalFields(autoAppSubmissionDto, autoChangeSelectDto,autoGrpNo);
                        autoAppSubmissionDto.setChangeSelectDto(autoChangeSelectDto);
                        autoAppSubmissionDtos.add(0, autoAppSubmissionDto);
                    }
                }
        }else if(appSubmissionDtos.size() > 1){
            needDec = false;
            moreAppSubmissionDtoAction(appSubmissionDtos);
        }
        boolean isCharity = NewApplicationHelper.isCharity(bpc.request);
        FeeDto renewalAmount ;
        if(isCharity){
            renewalAmount = appSubmissionService.getCharityRenewalAmount(appSubmissionDtos,isCharity);
        }else {
            renewalAmount = appSubmissionService.getRenewalAmount(appSubmissionDtos,isCharity);
        }
        setSubmissionAmount(appSubmissionDtos,renewalAmount,appFeeDetailsDto,bpc);

        List<FeeExtDto> gradualFeeList = IaisCommonUtils.genNewArrayList();
        List<FeeExtDto> normalFeeList = IaisCommonUtils.genNewArrayList();
        HashMap<String, List<FeeExtDto>> laterFeeDetailsMap = getLaterFeeDetailsMap(renewalAmount.getDetailFeeDto(),gradualFeeList,normalFeeList);
        ParamUtil.setRequestAttr(bpc.request, "laterFeeDetailsMap", laterFeeDetailsMap);
        if(!IaisCommonUtils.isEmpty(gradualFeeList)){
            ParamUtil.setRequestAttr(bpc.request, "gradualFeeList", gradualFeeList);
        }
        if(!IaisCommonUtils.isEmpty(normalFeeList)){
            ParamUtil.setRequestAttr(bpc.request, "normalFeeList", normalFeeList);
        }
        requestForChangeService.premisesDocToSvcDoc(oldAppSubmissionDto);
        AppSubmissionListDto appSubmissionListDto = new AppSubmissionListDto();
        String submissionId = generateIdClient.getSeqId().getEntity();
        Long l = System.currentTimeMillis();
        List<AppSubmissionDto> appSubmissionDtos1 = IaisCommonUtils.genNewArrayList();
        //do app submit
        String appGrpNo = appSubmissionDtos.get(0).getAppGrpNo();
        appSubmissionDtos1.addAll(appSubmissionDtos);
        for(AppSubmissionDto appSubmissionDto : noAutoAppSubmissionDtos){
            appSubmissionDto.setAppGrpNo(appGrpNo);
            setRfcSubInfo(appSubmissionDtos.get(0),appSubmissionDto,null,needDec);
        }
        int i=0;
        for( AppFeeDetailsDto detailsDto : appFeeDetailsDto){
            i=i+1;
            if(i<10){
                detailsDto.setApplicationNo(appGrpNo+"-0"+i);
            }else {
                detailsDto.setApplicationNo(appGrpNo+"-"+i);
            }
            appSubmissionService.saveAppFeeDetails(detailsDto);
        }


        appSubmissionDtos1.addAll(noAutoAppSubmissionDtos);
        AuditTrailDto currentAuditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        if(!autoAppSubmissionDtos.isEmpty() || !noAutoAppSubmissionDtos.isEmpty()){
            if(appSubmissionDtos.size()==1){
                AuditTrailHelper.auditFunctionWithLicNo(AuditTrailConsts.MODULE_RENEW,AuditTrailConsts.MODULE_RENEW,appSubmissionDtos.get(0).getLicenceNo());
            }
        }
        String autoGroupId = null;
        String appGrpStatus = ApplicationConsts.APPLICATION_GROUP_STATUS_SUBMITED;
        if (!autoAppSubmissionDtos.isEmpty()) {
            AppSubmissionListDto autoAppSubmissionListDto = new AppSubmissionListDto();
            String autoSubmissionId = generateIdClient.getSeqId().getEntity();
            Long auto = System.currentTimeMillis();
            autoAppSubmissionDtos.get(0).setAuditTrailDto(currentAuditTrailDto);
            for(AppSubmissionDto appSubmissionDto : autoAppSubmissionDtos){
                appSubmissionDto.setAppGroupAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
                appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
                setRfcSubInfo(appSubmissionDtos.get(0),appSubmissionDto,autoGrpNo,needDec);
            }
            List<AppSubmissionDto> saveutoAppSubmissionDto = requestForChangeService.saveAppsForRequestForGoupAndAppChangeByList(autoAppSubmissionDtos);
            autoGroupId = saveutoAppSubmissionDto.get(0).getAppGrpId();
            appGrpStatus = ApplicationConsts.APPLICATION_GROUP_STATUS_PENDING_AUTO;
            AuditTrailDto at = AuditTrailHelper.getCurrentAuditTrailDto();
            at.setModule(AuditTrailConsts.MODULE_RENEW);
            at.setFunctionName(AuditTrailConsts.FUNCTION_RENEW);
            autoAppSubmissionListDto.setAuditTrailDto(AuditTrailHelper.getCurrentAuditTrailDto());
            autoAppSubmissionListDto.setEventRefNo(auto.toString());
            setCheckRepeatAppData(saveutoAppSubmissionDto);
            autoAppSubmissionListDto.setAppSubmissionDtos(saveutoAppSubmissionDto);
            eventBusHelper.submitAsyncRequest(autoAppSubmissionListDto, autoSubmissionId, EventBusConsts.SERVICE_NAME_APPSUBMIT,
                    EventBusConsts.OPERATION_REQUEST_INFORMATION_SUBMIT, l.toString(), bpc.process);
        }

        for(AppSubmissionDto appSubmissionDto : appSubmissionDtos1){
            appSubmissionDto.setAutoRfc(false);
            appSubmissionDto.setAppGrpStatus(appGrpStatus);
            appSubmissionDto.setAuditTrailDto(currentAuditTrailDto);
        }
        List<AppSubmissionDto> appSubmissionDtos3 = requestForChangeService.saveAppsForRequestForGoupAndAppChangeByList(appSubmissionDtos1);
        String notAutoGroupId = appSubmissionDtos3.get(0).getAppGrpId();
        appSubmissionListDto.setAppSubmissionDtos(appSubmissionDtos3);
        appSubmissionListDto.setAuditTrailDto(AuditTrailHelper.getCurrentAuditTrailDto());
        appSubmissionListDto.setEventRefNo(l.toString());
        setCheckRepeatAppData(appSubmissionDtos3);
        eventBusHelper.submitAsyncRequest(appSubmissionListDto, submissionId, EventBusConsts.SERVICE_NAME_APPSUBMIT,
                EventBusConsts.OPERATION_REQUEST_INFORMATION_SUBMIT, l.toString(), bpc.process);
        rfcAppSubmissionDtos.addAll(noAutoAppSubmissionDtos);
        rfcAppSubmissionDtos.addAll(autoAppSubmissionDtos);
        List<AppSubmissionDto> renewAppSubmissionDtos = IaisCommonUtils.genNewArrayList();
        renewAppSubmissionDtos.addAll(rfcAppSubmissionDtos);
        renewAppSubmissionDtos.addAll(appSubmissionDtos);
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
            appSubmissionDto.setId(notAutoGroupId);
        }
        for (AppSubmissionDto appSubmissionDto : appSubmissionDtos) {
            appSubmissionDto.setAppGrpNo(appGrpNo);
            appSubmissionDto.setAppGrpId(notAutoGroupId);
        }

        // app group misc
        appSubmissionService.saveAutoRFCLinkAppGroupMisc(notAutoGroupId,autoGroupId);

        ParamUtil.setSessionAttr(bpc.request,"rfcAppSubmissionDtos", (Serializable) rfcAppSubmissionDtos);
        ParamUtil.setSessionAttr(bpc.request, "serviceNamesAck", (Serializable) serviceNamesAck);
        //has app submit
        ParamUtil.setSessionAttr(bpc.request, "hasAppSubmit", "Y");
        setGiroAcc(renewAppSubmissionDtos,bpc.request);
        ParamUtil.setSessionAttr(bpc.request, RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR,renewDto);
    }

    private AppSubmissionDto getAutoSubForRenewSelf(AppEditSelectDto appEditSelectDto,AppSubmissionDto appSubmissionDto){
        AppSubmissionDto autoAppSubmissionDto = null;
        if ( (appEditSelectDto.isAutoRfc() && appEditSelectDto.isPremisesEdit()) ||
                (!appEditSelectDto.isAutoRfc() && appEditSelectDto.isLicenseeEdit() || appEditSelectDto.isDocEdit())) {
            autoAppSubmissionDto = (AppSubmissionDto) com.ecquaria.cloud.moh.iais.common.utils.CopyUtil.copyMutableObject(appSubmissionDto);
            autoAppSubmissionDto.setAmount(0.0);
            autoAppSubmissionDto.setChangeSelectDto(new AppEditSelectDto());
        }
        return autoAppSubmissionDto;
    }

    private int isAutoPremises(AppEditSelectDto appEditSelectDto){
            if(appEditSelectDto.isPremisesEdit()){
                AppEditSelectDto changeSelectDto = new AppEditSelectDto();
                changeSelectDto.setPremisesEdit(true);
                changeSelectDto.setPremisesListEdit(true);
                changeSelectDto.setChangeHciName(appEditSelectDto.isChangeHciName());
                changeSelectDto.setChangeInLocation(appEditSelectDto.isChangeInLocation());
                changeSelectDto.setChangeAddFloorUnit(appEditSelectDto.isChangeAddFloorUnit());
                return changeSelectDto.isAutoRfc() ? 1 : 0 ;
            }
            return -1;
    }

    private void setRfcSubInfo(AppSubmissionDto appSubmissionDtoNew,AppSubmissionDto dto,String autoGrpNo,boolean needDec){
        if(StringUtil.isNotEmpty(autoGrpNo)){
            dto.setAppGrpNo(autoGrpNo);
            List<AppGrpPremisesDto> appGrpPremisesDtoList = dto.getAppGrpPremisesDtoList();
            if(IaisCommonUtils.isNotEmpty(appGrpPremisesDtoList)){
                for(AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList){
                    appGrpPremisesDto.setSelfAssMtFlag(4);
                }
            }
        }
        if(needDec){
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
        }else {
            dto.setAppDeclarationMessageDto(null);
            dto.setAppDeclarationDocDtos(null);
        }
    }

   private void setGiroAcc(List<AppSubmissionDto> appSubmissionDtos, HttpServletRequest request){
       boolean isGiroAcc = false;
       List<SelectOption> giroAccSel = NewApplicationHelper.getGiroAccOptions(appSubmissionDtos, null);
       if (!IaisCommonUtils.isEmpty(giroAccSel)) {
           isGiroAcc = true;
           ParamUtil.setRequestAttr(request, "giroAccSel", giroAccSel);
       }
       ParamUtil.setRequestAttr(request,"IsGiroAcc",isGiroAcc);
   }


    private void setMacFileIndex(List<AppSubmissionDto> appSubmissionDtos, HttpServletRequest request){
        if(appSubmissionDtos.size() == 1){
            Integer maxFileIndex = (Integer) ParamUtil.getSessionAttr(request,HcsaFileAjaxController.GLOBAL_MAX_INDEX_SESSION_ATTR);
            if(maxFileIndex == null){
                maxFileIndex = 0;
            }
            appSubmissionDtos.get(0).setMaxFileIndex(maxFileIndex);
        }
    }

    private String getLicenseeIdByLoginInfo( HttpServletRequest request){
        InterInboxUserDto interInboxUserDto = (InterInboxUserDto) ParamUtil.getSessionAttr(request, "INTER_INBOX_USER_INFO");
        String licenseeId = null;
        if (interInboxUserDto != null) {
            return  interInboxUserDto.getLicenseeId();
        } else {
            log.debug(StringUtil.changeForLog("interInboxUserDto null"));
        }
        return  licenseeId;
    }


    private void moreAppSubmissionDtoAction( List<AppSubmissionDto> appSubmissionDtos){
        List<HcsaFeeBundleItemDto> hcsaFeeBundleItemDtos = hcsaConfigFeClient.getActiveBundleDtoList().getEntity();
        List<HcsaServiceDto> hcsaServiceDtoList = hcsaConfigFeClient.getActiveServices().getEntity();
        Map<String,HcsaServiceDto> map=new HashMap<>(10);
        Map<String,List<HcsaFeeBundleItemDto>> bundleMap=new HashMap<>(10);
        hcsaServiceDtoList.forEach((v)->{
            map.put(v.getSvcName(),v);
        });
        hcsaFeeBundleItemDtos.forEach((v)->{
            List<HcsaFeeBundleItemDto> feeBundleItemDtos = bundleMap.get(v.getBundleId());
            if(feeBundleItemDtos==null){
                feeBundleItemDtos=new ArrayList<>(10);
                feeBundleItemDtos.add(v);
                bundleMap.put(v.getBundleId(),feeBundleItemDtos);
            }else {
                feeBundleItemDtos.add(v);
            }

        });
        Map<String,List<AppSubmissionDto>> appSubmitMap=new HashMap<>(10);
        for (AppSubmissionDto var1 : appSubmissionDtos) {
            String serviceName = var1.getAppSvcRelatedInfoDtoList().get(0).getServiceName();
            HcsaServiceDto hcsaServiceDto = map.get(serviceName);
            for (HcsaFeeBundleItemDto var2 : hcsaFeeBundleItemDtos) {
                if(var2.getSvcCode().equals(hcsaServiceDto.getSvcCode())){
                    List<AppSubmissionDto> dtoList = appSubmitMap.get(var2.getBundleId());
                    if(dtoList==null){
                        dtoList=new ArrayList<>(10);
                        dtoList.add(var1);
                        appSubmitMap.put(var2.getBundleId(),dtoList);
                    }else {
                        dtoList.add(var1);
                    }

                }
            }
        }
        appSubmitMap.forEach((k,v)->{
            long l = System.currentTimeMillis();
            v.forEach(var1-> var1.getAppSvcRelatedInfoDtoList().get(0).setAlignFlag(Long.toString(l)));
        });
    }

    private List<AppSubmissionDto> getLicChangeSubmissionDtos(AppSubmissionDto oldAppSubmissionDto){
        SubLicenseeDto oldSublicenseeDto = oldAppSubmissionDto.getSubLicenseeDto();
        List<AppSubmissionDto> licenseeAffectedList = licenceClient.getAppSubmissionDtosBySubLicensee(oldSublicenseeDto).getEntity();
        Iterator<AppSubmissionDto> itemDtoIterator = licenseeAffectedList.iterator();
        while(itemDtoIterator.hasNext()){
            AppSubmissionDto appSubmissionDto = itemDtoIterator.next();
            if( appSubmissionDto.getLicenceId().equalsIgnoreCase(oldAppSubmissionDto.getLicenceId())){
                itemDtoIterator.remove();
                 return licenseeAffectedList;
            }
        }
        return licenseeAffectedList;
    }

    private List<AppSubmissionDto> getAutoChangeLicAppSubmissions(AppSubmissionDto oldAppSubmissionDto,String groupNo, AppSubmissionDto appSubmissionDto) throws Exception {
        List<AppSubmissionDto> appSubmissionDtos = getLicChangeSubmissionDtos(oldAppSubmissionDto);
        appSubmissionDtos.stream().forEach(dto -> {
            dto.setSubLicenseeDto(MiscUtil.transferEntityDto(appSubmissionDto.getSubLicenseeDto(), SubLicenseeDto.class));
            AppEditSelectDto changeSelectDto = new AppEditSelectDto();
            changeSelectDto.setLicenseeEdit(true);
            requestForChangeService.checkAffectedAppSubmissions(dto, null, 0.0, null, groupNo,
                    changeSelectDto, null, null);
        });
        for(AppSubmissionDto appSubmissionDtoChange : appSubmissionDtos){
                appSubmissionDtoChange.setAutoRfc(true);
            }
        return appSubmissionDtos;
    }
    private void setRfcHciNameChanged(List<AppGrpPremisesDto> appGrpPremisesDtoList,List<AppGrpPremisesDto>  oldAppSubmissionDtoAppGrpPremisesDtoList,int i){
        if(appGrpPremisesDtoList.size() == oldAppSubmissionDtoAppGrpPremisesDtoList.size()){
            boolean eqHciNameChange = EqRequestForChangeSubmitResultChange.eqHciNameChange(appGrpPremisesDtoList.get(i), oldAppSubmissionDtoAppGrpPremisesDtoList.get(i));
            if(eqHciNameChange){
                appGrpPremisesDtoList.get(i).setHciNameChanged(1);
            }
        }
    }

    private void setRfcPremisesSubmissionDto(AppSubmissionDto appSubmissionDtoByLicenceId,String licenseeId, List<AppGrpPremisesDto> appGrpPremisesDtoList, AppSubmissionDto appSubmissionDto,int i,AppEditSelectDto rfcAppEditSelectDto) throws Exception {
        initRfcSubmissionDto(appSubmissionDtoByLicenceId,licenseeId);
        String address = appGrpPremisesDtoList.get(i).getAddress();
        List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtos1 = appGrpPremisesDtoList.get(i).getAppPremisesOperationalUnitDtos();
        String premisesIndexNo = appSubmissionDtoByLicenceId.getAppGrpPremisesDtoList().get(0).getPremisesIndexNo();
        boolean equalsAddress;
        boolean flag;
        boolean eqUnitDto;
        int hciNameChange;
        String oldAddress = appSubmissionDtoByLicenceId.getAppGrpPremisesDtoList().get(0).getAddress();
        equalsAddress = oldAddress.equals(address);
        boolean eqHciNameChange = EqRequestForChangeSubmitResultChange.eqHciNameChange(appSubmissionDtoByLicenceId.getAppGrpPremisesDtoList().get(0), appSubmissionDto.getAppGrpPremisesDtoList().get(i));
        hciNameChange=appSubmissionDtoByLicenceId.getAppGrpPremisesDtoList().get(0).getHciNameChanged();
        if(eqHciNameChange){
            hciNameChange=0;
        }
        boolean b = EqRequestForChangeSubmitResultChange.compareHciName(appSubmissionDtoByLicenceId.getAppGrpPremisesDtoList().get(0), appSubmissionDto.getAppGrpPremisesDtoList().get(i));
        List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtos = appSubmissionDtoByLicenceId.getAppGrpPremisesDtoList().get(0).getAppPremisesOperationalUnitDtos();
        eqUnitDto = EqRequestForChangeSubmitResultChange.eqOperationalUnitDtoList(appPremisesOperationalUnitDtos1, appPremisesOperationalUnitDtos);

        if( equalsAddress && b && !eqUnitDto){
            flag=true;
        }else {
            flag=false;
        }

        setPremisesEditAndReSetAppGrpPremisesDto(appSubmissionDtoByLicenceId,appGrpPremisesDtoList.get(i),flag,rfcAppEditSelectDto.isPremisesEdit());
        appSubmissionDtoByLicenceId.getAppGrpPremisesDtoList().get(0).setPremisesIndexNo(premisesIndexNo);
        appSubmissionDtoByLicenceId.getAppGrpPremisesDtoList().get(0).setHciNameChanged(hciNameChange);
        appSubmissionDtoByLicenceId.setIsNeedNewLicNo( !flag ? AppConsts.YES : AppConsts.NO );
        setPreInspectionAndRequirement(appSubmissionDtoByLicenceId);
        appSubmissionDtoByLicenceId.setAutoRfc(flag);
        appSubmissionDtoByLicenceId.setAppEditSelectDto(rfcAppEditSelectDto);
        appSubmissionDtoByLicenceId.setChangeSelectDto(rfcAppEditSelectDto);
        appSubmissionDto.setCreatAuditAppStatus(ApplicationConsts.APPLICATION_STATUS_NOT_PAYMENT);
        RequestForChangeMenuDelegator.oldPremiseToNewPremise(appSubmissionDtoByLicenceId);
        requestForChangeService.premisesDocToSvcDoc(appSubmissionDtoByLicenceId);
    }


    private void initRfcSubmissionDto(AppSubmissionDto appSubmissionDtoByLicenceId,String licenseeId) throws Exception {
        requestForChangeService.setRelatedInfoBaseServiceId(appSubmissionDtoByLicenceId);
        appSubmissionDtoByLicenceId.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
        appSubmissionService.transform(appSubmissionDtoByLicenceId, licenseeId);
        appSubmissionDtoByLicenceId.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
        appSubmissionDtoByLicenceId.setStatus(ApplicationConsts.APPLICATION_STATUS_REQUEST_FOR_CHANGE_SUBMIT);
        String draftNo = appSubmissionDtoByLicenceId.getDraftNo();
        if (StringUtil.isEmpty(draftNo)) {
            appSubmissionService.setDraftNo(appSubmissionDtoByLicenceId);
        }
        appSubmissionDtoByLicenceId.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_PENDING_PAYMENT);
        appSubmissionDtoByLicenceId.setGroupLic(false);
        appSubmissionDtoByLicenceId.setPartPremise(false);
        appSubmissionDtoByLicenceId.setAmount(0.0);
    }



    private void setPremisesEditAndReSetAppGrpPremisesDto(AppSubmissionDto appSubmissionDtoByLicenceId,AppGrpPremisesDto appGrpPremisesDto,boolean flag,boolean premisesEdit) throws CloneNotSupportedException {
        if(premisesEdit){
            List<AppGrpPremisesDto> appGrpPremisesDtos = new ArrayList<>(1);
            AppGrpPremisesDto copyMutableObject = (AppGrpPremisesDto) CopyUtil.copyMutableObject(appGrpPremisesDto);
            appGrpPremisesDtos.add(copyMutableObject);
            appSubmissionDtoByLicenceId.setAppGrpPremisesDtoList(appGrpPremisesDtos);
        }
        List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDtoByLicenceId.getAppGrpPremisesDtoList();

        if (flag) {
            if (!IaisCommonUtils.isEmpty(appGrpPremisesDtos)) {
                for (AppGrpPremisesDto appGrpPremisesDto1 : appGrpPremisesDtos) {
                    appGrpPremisesDto1.setNeedNewLicNo(Boolean.FALSE);
                    appGrpPremisesDto1.setSelfAssMtFlag(4);
                }
            }
        }
    }
    private void setPreInspectionAndRequirement(AppSubmissionDto appSubmissionDtoByLicenceId){
        PreOrPostInspectionResultDto preOrPostInspectionResultDto = appSubmissionService.judgeIsPreInspection(appSubmissionDtoByLicenceId);
        if (preOrPostInspectionResultDto == null) {
            appSubmissionDtoByLicenceId.setPreInspection(true);
            appSubmissionDtoByLicenceId.setRequirement(true);
        } else {
            appSubmissionDtoByLicenceId.setPreInspection(preOrPostInspectionResultDto.isPreInspection());
            appSubmissionDtoByLicenceId.setRequirement(preOrPostInspectionResultDto.isRequirement());
        }
    }
    private void setCheckRepeatAppData(List<AppSubmissionDto> saveutoAppSubmissionDto){
        if(IaisCommonUtils.isNotEmpty(saveutoAppSubmissionDto)){
            boolean checkRepeatAppData = false;
            for(AppSubmissionDto appSubmissionDto : saveutoAppSubmissionDto){
                if(ApplicationConsts. APPLICATION_TYPE_RENEWAL.equalsIgnoreCase(appSubmissionDto.getAppType())){
                    checkRepeatAppData = true;
                }
            }

            if(checkRepeatAppData){
                for(AppSubmissionDto appSubmissionDto : saveutoAppSubmissionDto){
                    appSubmissionDto.setCheckRepeatAppData(checkRepeatAppData);
                }
            }
        }
    }
    public static void setSubmissionAmount(List<AppSubmissionDto> appSubmissionDtoList, FeeDto feeDto, List<AppFeeDetailsDto> appFeeDetailsDto, BaseProcessClass bpc){
        List<FeeExtDto> detailFeeDtoList = feeDto.getDetailFeeDto();
        Double total = feeDto.getTotal();
        String totalString = Formatter.formatterMoney(total);
        ParamUtil.setSessionAttr(bpc.request, "totalStr", totalString);
        ParamUtil.setSessionAttr(bpc.request, "totalAmount", total);
        if(IaisCommonUtils.isEmpty(detailFeeDtoList) || IaisCommonUtils.isEmpty(appSubmissionDtoList)){
            return;
        }
        int index = 0;
        int mix_g = 0;
        int mix_n = 0;
        boolean isBundledFee=false;
        for(AppSubmissionDto appSubmissionDto : appSubmissionDtoList){
            AppFeeDetailsDto appFeeDetailsDto1=new AppFeeDetailsDto();
            FeeExtDto feeExtDto = detailFeeDtoList.get(index);
            feeExtDto.setAppGroupNo(appSubmissionDtoList.get(0).getAppGrpNo());
            String lateFeeType = feeExtDto.getLateFeeType();
            if("gradualFee".equals(lateFeeType)){
                mix_g ++;
            }else{
                mix_n ++;
            }
            if(mix_g > 0 && mix_n >0){
                ParamUtil.setRequestAttr(bpc.request,"mix","mix");
            }
            try {
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(feeExtDto.getSvcNames().get(0));
                feeExtDto.setSvcCode(hcsaServiceDto.getSvcCode());
                if(MiscUtil.doubleEquals(feeExtDto.getAmount(), 0.0)&&(feeExtDto.getSvcCode().equals(AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE)||feeExtDto.getSvcCode().equals(AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE))){
                    appSubmissionDto.setIsBundledFee(1);
                    isBundledFee=true;
                }
                if(hcsaServiceDto.getSvcCode().equals(AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_ASSAY)||hcsaServiceDto.getSvcCode().equals(AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_IMAGING)){
                    appSubmissionDto.setIsSpecifiedFee(1);
                }
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }

            appSubmissionDto.setRenewalFeeType(lateFeeType);
            Double lateFeeAmount = feeExtDto.getLateFeeAmoumt();
            Double amount = feeExtDto.getAmount();
            appSubmissionDto.setLateFee(lateFeeAmount);
            appSubmissionDto.setLateFeeStr(Formatter.formatterMoney(lateFeeAmount));
            appSubmissionDto.setAmount(amount);
            appSubmissionDto.setAmountStr(Formatter.formatterMoney(amount));
            appFeeDetailsDto1.setBaseFee(amount);
            if(StringUtil.isEmpty(lateFeeAmount)){
                appFeeDetailsDto1.setLaterFee(0.0);
            }else {
                appFeeDetailsDto1.setLaterFee(lateFeeAmount);
            }
            appFeeDetailsDto.add(appFeeDetailsDto1);
            appFeeDetailsDto1.setAdmentFee(total);
            index ++;
        }
        if(isBundledFee){
            index = 0;
            for(AppSubmissionDto appSubmissionDto : appSubmissionDtoList){
                FeeExtDto feeExtDto = detailFeeDtoList.get(index);
                if(!MiscUtil.doubleEquals(feeExtDto.getAmount(), 0.0)&&(feeExtDto.getSvcCode().equals(AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE)||feeExtDto.getSvcCode().equals(AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE))){
                    appSubmissionDto.setIsBundledFee(1);
                }
                index ++;
            }
        }

    }

    public static HashMap<String, List<FeeExtDto>> getLaterFeeDetailsMap(List<FeeExtDto> laterFeeDetails,List<FeeExtDto> gradualFeeList,List<FeeExtDto> normalFeeList){
        HashMap<String, List<FeeExtDto>> laterFeeDetailsMap = IaisCommonUtils.genNewHashMap();
        if(laterFeeDetails == null || laterFeeDetails.size() == 0){
            return null;
        }

        for(FeeExtDto laterFeeDetail : laterFeeDetails){
            String targetLaterFeeType = laterFeeDetail.getLateFeeType();
            if(StringUtil.isEmpty(targetLaterFeeType)){
                normalFeeList.add(laterFeeDetail);
                continue;
            }else if("gradualFee".equals(targetLaterFeeType)){
                Double amount = laterFeeDetail.getAmount();
                if(amount != null && !MiscUtil.doubleEquals(amount, 0d)){
                    gradualFeeList.add(laterFeeDetail);
                }
                continue;
            }
            normalFeeList.add(laterFeeDetail);
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


    private void updateDraftStatus(AppSubmissionDto appSubmissionDto){
        if(!StringUtil.isEmpty(appSubmissionDto.getLicenceId())){
            List<ApplicationSubDraftDto> entity = applicationFeClient.getDraftByLicAppId(appSubmissionDto.getLicenceId()).getEntity();
            if(IaisCommonUtils.isEmpty(entity)){
                entity = IaisCommonUtils.genNewArrayList();
            }
            List<ApplicationSubDraftDto> applicationSubDraftDtos = applicationFeClient.getDraftByLicAppIdAndStatus(appSubmissionDto.getLicenceId(),ApplicationConsts.DRAFT_STATUS_PENDING_PAYMENT).getEntity();
            if(IaisCommonUtils.isNotEmpty(applicationSubDraftDtos)){
                entity.addAll(applicationSubDraftDtos);
            }
            if(IaisCommonUtils.isNotEmpty(entity)){
                for(ApplicationSubDraftDto applicationSubDraftDto : entity){
                    String draftJson = applicationSubDraftDto.getDraftJson();
                    AppSubmissionDto appSubmissionDto1 = JsonUtil.parseToObject(draftJson, AppSubmissionDto.class);
                    applicationFeClient.deleteDraftByNo(appSubmissionDto1.getDraftNo());
                }
            }
        }
    }
    //prepareAcknowledgement
    public void prepareAcknowledgement(BaseProcessClass bpc) throws Exception {
        InterInboxUserDto interInboxUserDto = (InterInboxUserDto) ParamUtil.getSessionAttr(bpc.request, "INTER_INBOX_USER_INFO");
        RenewDto renewDto = (RenewDto) ParamUtil.getSessionAttr(bpc.request, RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR);
        List<AppSubmissionDto> appSubmissionDtos = renewDto.getAppSubmissionDtos();
        doDeleteMoreRenewDeclarations(appSubmissionDtos);
        for(AppSubmissionDto appSubmissionDto : appSubmissionDtos){
            updateDraftStatus(appSubmissionDto);
        }
        List<AppSubmissionDto> rfcAppSubmissionDtos=(List<AppSubmissionDto>)bpc.request.getSession().getAttribute("rfcAppSubmissionDtos");
        boolean b=false;
        if(IaisCommonUtils.isNotEmpty(rfcAppSubmissionDtos)){
            for(AppSubmissionDto appSubmissionDto : rfcAppSubmissionDtos){
                String appGrpNo = appSubmissionDto.getAppGrpNo();
                List<ApplicationDto> entity = applicationFeClient.getApplicationsByGroupNo(appGrpNo).getEntity();
                if(entity!=null&& !entity.isEmpty()){
                    for(ApplicationDto applicationDto : entity){
                        applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING);
                    }
                    String grpId = entity.get(0).getAppGrpId();
                    ApplicationGroupDto applicationGroupDto = applicationFeClient.getApplicationGroup(grpId).getEntity();
                    applicationGroupDto.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
                    applicationFeClient.updateApplicationList(entity);
                    applicationFeClient.updateAppGrpPmtStatus(applicationGroupDto);

                }
                if(!b && ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equalsIgnoreCase(appSubmissionDto.getAppType())){
                    b = true;
                }
            }
        }

        //send rfc email
        if(b){
            rfcAppSubmissionDtos.get(0).setAppGrpId(appSubmissionDtos.get(0).getAppGrpId());
            requestForChangeService.sendRfcSubmittedEmail(rfcAppSubmissionDtos,appSubmissionDtos.get(0).getPaymentMethod());
        }

        String licenseeId = null;
        if (interInboxUserDto != null) {
            licenseeId = interInboxUserDto.getLicenseeId();
        } else {
            log.debug(StringUtil.changeForLog("interInboxUserDto null"));
        }
        List<String> licenseeEmailAddrs = IaisEGPHelper.getLicenseeEmailAddrs(licenseeId);
        String emailAddress = emailAddressesToString(licenseeEmailAddrs);
        ParamUtil.setSessionAttr(bpc.request, "emailAddress", emailAddress);
        ParamUtil.setRequestAttr(bpc.request, "hasDetail", "N");
    }

    private void doDeleteMoreRenewDeclarations(List<AppSubmissionDto> appSubmissionDtos){
        if(appSubmissionDtos .size() <=1 ){
            return;
        }
        appSubmissionDtos.forEach(app -> {
            if(!ApplicationConsts.APPLICATION_TYPE_RENEWAL.equalsIgnoreCase(app.getAppType())){
                return;
            }
        });
        AppSubmissionDto appSubmissionDto = appSubmissionDtos.get(0);
        String groupId = appSubmissionDto.getAppGrpId();
        if(StringUtil.isNotEmpty(groupId)){
            List<AppDeclarationMessageDto> appDeclarationMessageDtos = applicationFeClient.getAppDeclarationMessageDto(groupId).getEntity();
            if(IaisCommonUtils.isNotEmpty(appDeclarationMessageDtos)){
                appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                applicationFeClient.inActiveDeclaration(appSubmissionDto);
            }
        }

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
        IaisEGPHelper.redirectUrl(response, tokenUrl);
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
        Map<String,String> allErrMap = IaisCommonUtils.genNewHashMap();
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
                if(appSubmissionDtos.size()==1){
                    AppDeclarationMessageDto appDeclarationMessageDto = appSubmissionService.getAppDeclarationMessageDto(bpc.request, ApplicationConsts.APPLICATION_TYPE_RENEWAL);
                    DeclarationsUtil.declarationsValidate(allErrMap,appDeclarationMessageDto,ApplicationConsts.APPLICATION_TYPE_RENEWAL);
                    appSubmissionDtos.get(0).setAppDeclarationMessageDto(appDeclarationMessageDto);
                    appSubmissionDtos.get(0).setAppDeclarationDocDtos(appSubmissionService.getDeclarationFiles(ApplicationConsts.APPLICATION_TYPE_RENEWAL, bpc.request));
                    appSubmissionService.initDeclarationFiles(appSubmissionDtos.get(0).getAppDeclarationDocDtos(),ApplicationConsts.APPLICATION_TYPE_RENEWAL,bpc.request);
                    String preQuesKindly =  appSubmissionDtos.get(0).getAppDeclarationMessageDto().getPreliminaryQuestionKindly();
                    appSubmissionService.validateDeclarationDoc(allErrMap,appSubmissionService.getFileAppendId(ApplicationConsts.APPLICATION_TYPE_RENEWAL),
                            preQuesKindly ==null ? false : "0".equals(preQuesKindly), bpc.request);
                }
            }
        }
        boolean passValidate = true;
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
            ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(allErrMap));
            ParamUtil.setRequestAttr(bpc.request, PAGE_SWITCH, PAGE2);
            return;

        }
        if(!allErrMap.isEmpty()){
            ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(allErrMap));
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
                    if(goGolicenceReview(bpc.request,appSubmissionDto,oldAppSubmissionDtoAppGrpPremisesDtoList,rfc_err020)){
                        return;
                    }
                    boolean flag =EqRequestForChangeSubmitResultChange.isChangeGrpPremises(appGrpPremisesDtoList,
                            oldAppSubmissionDtoAppGrpPremisesDtoList);
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
                                    boolean b = requestForChangeService.baseSpecLicenceRelation(licenceDto);
                                    if(!b){
                                        rfc_err020=rfc_err020.replace("{ServiceName}",licenceDto.getSvcName());
                                        bpc.request.setAttribute("SERVICE_CONFIG_CHANGE",rfc_err020);
                                        ParamUtil.setRequestAttr(bpc.request, PAGE_SWITCH, PAGE2);
                                        return;
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
                   if(goGolicenceReview(bpc.request,appSubmissionDto,appGrpPremisesDtoList,rfc_err020)){
                       return;
                   }
                }
                requestForChangeService.setRelatedInfoBaseServiceId(appSubmissionDto);
                String baseServiceId = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getBaseServiceId();
                if(StringUtil.isEmpty(baseServiceId)){
                    rfc_err020=rfc_err020.replace("{ServiceName}",appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getServiceName());
                    bpc.request.setAttribute("SERVICE_CONFIG_CHANGE",rfc_err020);
                    ParamUtil.setRequestAttr(bpc.request, PAGE_SWITCH, PAGE2);
                    return;
                }
            }
        }
        //go page3
        ParamUtil.setRequestAttr(bpc.request, PAGE_SWITCH, PAGE3);
    }

    private boolean goGolicenceReview(HttpServletRequest request,AppSubmissionDto appSubmissionDto, List<AppGrpPremisesDto> appGrpPremisesDtoList,String rfc_err020){
        String licenceId = appSubmissionDto.getLicenceId();
        LicenceDto licenceById = requestForChangeService.getLicenceDtoIncludeMigrated(licenceId);
        if(licenceById.getSvcName()!=null){
            HcsaServiceDto hcsaServiceDto = serviceConfigService.getActiveHcsaServiceDtoByName(licenceById.getSvcName());
            List<String> serviceIds=IaisCommonUtils.genNewArrayList();
            if(hcsaServiceDto!=null){
                serviceIds.add(hcsaServiceDto.getId());
                for(AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList){
                    boolean configIsChange = requestForChangeService.serviceConfigIsChange(serviceIds, appGrpPremisesDto.getPremisesType());
                    if(!configIsChange){
                        rfc_err020=rfc_err020.replace("{ServiceName}",licenceById.getSvcName());
                        request.setAttribute("SERVICE_CONFIG_CHANGE",rfc_err020);
                        ParamUtil.setRequestAttr(request, PAGE_SWITCH, PAGE2);
                        return true;
                    }
                }
            }
        }
        return false;
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
        //
        ParamUtil.setSessionAttr(bpc.request, RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR, renewDto);
        String groupNo = "";
        String appGrpId = "";
        if ( !IaisCommonUtils.isEmpty(appSubmissionDtos)) {
            groupNo = appSubmissionDtos.get(0).getAppGrpNo();
            appGrpId = appSubmissionDtos.get(0).getAppGrpId();
            for (AppSubmissionDto appSubmissionDto : appSubmissionDtos) {
                appSubmissionDto.setPaymentMethod(payMethod);
            }
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
                        html = GatewayPayNowAPI.create_partner_trade_by_buyer_url(fieldMap, bpc.request, backUrl);break;
                    default: html = GatewayAPI.create_partner_trade_by_buyer_url(fieldMap, bpc.request, backUrl);

                }
                if(appSubmissionDtos != null && appSubmissionDtos.size() == 1){
                    appSubmissionService.updateDraftStatus(appSubmissionDtos.get(0).getDraftNo(),ApplicationConsts.DRAFT_STATUS_PENDING_PAYMENT);
                }
                IaisEGPHelper.redirectUrl(bpc.response, html);
                bpc.request.setAttribute("paymentAmount", totalAmount);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        } else if (ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(payMethod) && !StringUtil.isEmpty(appGrpId)) {
            log.info(StringUtil.changeForLog("start giro payment appGrpId {}"+appGrpId));
            if(appSubmissionDtos.size() > 1){
                Double a = 0.0;
                for (AppSubmissionDto appSubmissionDto : appSubmissionDtos) {
                    if(appSubmissionDto.getAmount()!= null){
                        a = a + appSubmissionDto.getAmount();
                    }
                }
                appSubmissionDtos.get(0).setTotalAmountGroup(a);
            }
            try {
                //renew
                sendEmail(bpc.request,appSubmissionDtos);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                log.error(StringUtil.changeForLog("send email error"));
            }
            String giroAccNum = "";
            if(!StringUtil.isEmpty(payMethod) && ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(payMethod)){
                giroAccNum = ParamUtil.getString(bpc.request, "giroAccount");
            }
            appSubmissionDtos.get(0).setGiroAcctNum(giroAccNum);
            ApplicationGroupDto appGrp = new ApplicationGroupDto();
            appGrp.setId(appGrpId);
            appGrp.setPmtStatus(serviceConfigService.giroPaymentXmlUpdateByGrpNo(appSubmissionDtos.get(0)).getPmtStatus());
            String giroTranNo = appSubmissionDtos.get(0).getGiroTranNo();
            appGrp.setPmtRefNo(giroTranNo);
            appGrp.setPayMethod(payMethod);
            serviceConfigService.updateAppGrpPmtStatus(appGrp, giroAccNum);
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
                    //send email
                    sendEmail(bpc.request,appSubmissionDtos);
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
        }else if("paymentBack".equals(switch_value)){
            RenewDto renewDto = (RenewDto) ParamUtil.getSessionAttr(bpc.request, RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR);
            if(renewDto != null){
                List<AppSubmissionDto> appSubmissionDtos =renewDto.getAppSubmissionDtos();
                if (!IaisCommonUtils.isEmpty(appSubmissionDtos)) {
                    for(AppSubmissionDto appSubmissionDto:appSubmissionDtos){
                        appSubmissionDto.setId(null);
                        appSubmissionDto.setAppGrpNo(null);
                        appSubmissionDto.setAppGrpId(null);
                        requestForChangeService.svcDocToPresmise(appSubmissionDto);
                    }
                    if(appSubmissionDtos.size()==1){
                        appSubmissionDtos.get(0).setAppEditSelectDto(null);
                    }
                }
                ParamUtil.setSessionAttr(bpc.request, RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR,renewDto);
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
        setPreInspectionAndRequirement(appSubmissionDto);
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
            }else if(RfcConst.EDIT_LICENSEE.equalsIgnoreCase(editValue)){
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
     *
     * @param bpc
     * @throws
     */
    public void toPrepareData(BaseProcessClass bpc) throws Exception {
        log.info(StringUtil.changeForLog("the do toPrepareData start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.APPSUBMISSIONDTO);
        RenewDto renewDto = new RenewDto();
        List<AppSubmissionDto> appSubmissionDtos = ( List<AppSubmissionDto>) ParamUtil.getSessionAttr(bpc.request, "oldSubmissionDtos");
        if(IaisCommonUtils.isNotEmpty(appSubmissionDtos )){
            for(int i = 0 ;i< appSubmissionDtos.size();i++){
                if(appSubmissionDtos.get(i).getLicenceId().equalsIgnoreCase(appSubmissionDto.getLicenceId())){
                    appSubmissionDtos.set(i,appSubmissionDto);
                    break;
                }
            }

        }else {
            appSubmissionDtos = IaisCommonUtils.genNewArrayList();
        }
        renewDto.setAppSubmissionDtos(appSubmissionDtos);
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
            for(AppSubmissionDto appSubmissionDtoCirculation : appSubmissionDtos){
                if (!appSubmissionDto.getAppSvcRelatedInfoDtoList().isEmpty()) {
                    String serviceName = appSubmissionDtoCirculation.getAppSvcRelatedInfoDtoList().get(0).getServiceName();
                    //add to service name list;
                    HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(serviceName);
                    String svcId = hcsaServiceDto.getId();
                    Map<String, List<AppSvcDisciplineAllocationDto>> reloadDisciplineAllocationMap = appSubmissionService.getDisciplineAllocationDtoList(appSubmissionDtoCirculation, svcId);
                    reloadDisciplineAllocationMapList.add(reloadDisciplineAllocationMap);

                    //set AppSvcRelatedInfoDtoList chkName
                    List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos = serviceConfigService.loadLaboratoryDisciplines(svcId);
                    NewApplicationHelper.setLaboratoryDisciplinesInfo(appSubmissionDtoCirculation, hcsaSvcSubtypeOrSubsumedDtos);
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
            ParamUtil.setSessionAttr(bpc.request, "reloadDisciplineAllocationMapList", (Serializable) reloadDisciplineAllocationMapList);
            ParamUtil.setSessionAttr(bpc.request, "ReloadPrincipalOfficersList", (Serializable) principalOfficersDtosList);
            ParamUtil.setSessionAttr(bpc.request, "deputyPrincipalOfficersDtosList", (Serializable) deputyPrincipalOfficersDtosList);
        }
        if(appSubmissionDtos.size() >1){
            ParamUtil.setSessionAttr(bpc.request, SINGLE_SERVICE, "N");
        }
        AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) bpc.request.getSession().getAttribute("oldRenewAppSubmissionDto");
        if (oldAppSubmissionDto != null && appSubmissionDto != null) {
            boolean eqGrpPremises = EqRequestForChangeSubmitResultChange.isChangeGrpPremises(appSubmissionDto.getAppGrpPremisesDtoList(),
                    oldAppSubmissionDto.getAppGrpPremisesDtoList());
            boolean eqServiceChange = EqRequestForChangeSubmitResultChange.eqServiceChange(appSubmissionDto.getAppSvcRelatedInfoDtoList(), oldAppSubmissionDto.getAppSvcRelatedInfoDtoList());
            boolean eqDocChange = EqRequestForChangeSubmitResultChange.eqDocChange(appSubmissionDto.getAppGrpPrimaryDocDtos(), oldAppSubmissionDto.getAppGrpPrimaryDocDtos());
            if(eqGrpPremises || eqServiceChange || eqDocChange){
                bpc.request.getSession().setAttribute(PREFIXTITLE,"amending");
            }else {
                bpc.request.getSession().setAttribute(PREFIXTITLE,"renewing");
            }
        }
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
            int appNoIndex = 1;
            String appNo = groupNo;
            String appDate = Formatter.formatDateTime(new Date(), "dd/MM/yyyy");
            log.info(StringUtil.changeForLog("send email appSubmissionDtos size : " + appSubmissionDtos.size()));
            StringBuilder stringBuilderAPPNum = new StringBuilder();
            String temp = "have";
            for (AppSubmissionDto appSubmissionDto : appSubmissionDtos) {
                List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
                if(!IaisCommonUtils.isEmpty(appGrpPremisesDtoList)){
                    for(AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList){
                        String hciName = appGrpPremisesDto.getHciName();
                        log.info(StringUtil.changeForLog("hci name : " + hciName));
                        appNo = getAppNo(groupNo,appNoIndex);
                        if(appNoIndex == 1){
                            stringBuilderAPPNum.append(appNo);
                        }else {
                            stringBuilderAPPNum.append(" and ");
                            stringBuilderAPPNum.append(appNo);
                        }
                        appNoIndex ++;
                    }
                }
            }
             if(appNoIndex == 2){
                temp = "has";
            }
            List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
            for(AppSubmissionDto appSubmissionDto : appSubmissionDtos){
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(appSubmissionDto.getServiceName());
                if( !StringUtil.isEmpty(hcsaServiceDto.getSvcCode()) && !svcCodeList.contains(hcsaServiceDto.getSvcCode())){
                    svcCodeList.add(hcsaServiceDto.getSvcCode());
                }
            }

            String amountStr = (String)ParamUtil.getSessionAttr(request, "totalStr");
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
            map.put("MOH_AGENCY_NAME", MohName);
            if (ApplicationConsts.PAYMENT_METHOD_NAME_CREDIT.equals(paymentMethod)
                    || ApplicationConsts.PAYMENT_METHOD_NAME_NETS.equals(paymentMethod)
                    || ApplicationConsts.PAYMENT_METHOD_NAME_PAYNOW.equals(paymentMethod)) {
                paymentMethodName = "onlinePayment";
                map.put("paymentMethod", paymentMethodName);
                log.info(StringUtil.changeForLog("send renewal application notification paymentMethodName : " + paymentMethodName));
            } else if (ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(paymentMethod)) {
                //GIRO payment method
                paymentMethodName = "GIRO";
                map.put("usualDeduction","next 7 working days");
                //OrgGiroAccountInfoDto entity = organizationLienceseeClient.getGiroAccByLicenseeId(appSubmissionDtos.get(0).getLicenseeId()).getEntity();
                map.put("accountNumber", serviceConfigService.getGiroAccountByGroupNo(groupNo));
                map.put("paymentMethod", paymentMethodName);
            }
            try {
//                    String subject = "MOH HALP - Your "+ applicationTypeShow + ", "+ appNo +" has been submitted";
                Map<String, Object> subMap = IaisCommonUtils.genNewHashMap();
                subMap.put("ApplicationType", applicationTypeShow);
                subMap.put("ApplicationNumber", applicationNumber);
                subMap.put("temp", temp);
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
                log.error(StringUtil.changeForLog("send email error"));
            }
            log.info(StringUtil.changeForLog("send renewal application notification end"));
        }else{
            log.debug(StringUtil.changeForLog("send email error , appSubmissionDtos is null"));
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
