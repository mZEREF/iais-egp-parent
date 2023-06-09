package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.organization.OrganizationConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppFeeDetailsDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesDoQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppAlignAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGroupMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppLicBundleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremSpecialisedDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremSubSvcRelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionRequestInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcSuplmFormDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcSuplmItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationSubDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.HcsaFeeBundleItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.LicenceFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.AppAlignLicQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeIndividualDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.MenuLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RecommendInspectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpePremisesTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.dto.PageShowFileDto;
import com.ecquaria.cloud.moh.iais.helper.ApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppCommService;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.LicCommService;
import com.ecquaria.cloud.moh.iais.service.LicenseeService;
import com.ecquaria.cloud.moh.iais.service.client.AppCommClient;
import com.ecquaria.cloud.moh.iais.service.client.AppEicClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationFeClient;
import com.ecquaria.cloud.moh.iais.service.client.ComFileRepoClient;
import com.ecquaria.cloud.moh.iais.service.client.ConfigCommClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.FeMessageClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaServiceClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationLienceseeClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sop.webflow.rt.api.BaseProcessClass;
import sop.webflow.rt.api.Process;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * AppSubmisionServiceImpl
 *
 * @author suocheng
 * @date 11/6/2019
 */
@Service
@Slf4j
public class AppSubmissionServiceImpl implements AppSubmissionService {

    @Autowired
    private ApplicationFeClient applicationFeClient;
    @Autowired
    private ConfigCommClient configCommClient;
    @Autowired
    private LicenceClient licenceClient;
    @Autowired
    private EventBusHelper eventBusHelper;
    @Autowired
    private AppEicClient appEicClient;
    @Autowired
    private ComFileRepoClient comFileRepoClient;
    @Autowired
    private AppCommClient appCommClient;

    @Autowired
    private SystemAdminClient systemAdminClient;
    @Autowired
    private GenerateIdClient generateIdClient;
    @Autowired
    private FeEicGatewayClient feEicGatewayClient;
    @Autowired
    private FeMessageClient feMessageClient;
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    private NotificationHelper notificationHelper;
    @Autowired
    private ServiceConfigServiceImpl serviceConfigService;
    @Autowired
    private RequestForChangeServiceImpl requestForChangeService;
    @Autowired
    private OrganizationLienceseeClient organizationLienceseeClient;
    @Autowired
    private LicenseeService licenseeService;

    @Autowired
    private LicCommService licCommService;

    @Autowired
    private AppCommService appCommService;
    @Autowired
    private HcsaServiceClient hcsaServiceClient;
    private static final String APPLICATION_TYPE = "ApplicationType";
    private static final String APPLICATION_NUMBER = "ApplicationNumber";
    private static final String Lic_BUNDLE = "LicBundle";
    private static final String[] ALPHABET_ARRAY_PROTOTYPE = new String[]{"a", "b", "c", "d", "e", "f", "g",
            "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    private static final String EMPTY = "";

    @Override
    public AppSubmissionDto submit(AppSubmissionDto appSubmissionDto, Process process) {
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appSubmissionDto = applicationFeClient.saveSubmision(appSubmissionDto).getEntity();
        //asynchronous save the other data.
        AppSubmissionDto newDto = ApplicationHelper.toSlim(appSubmissionDto);
        eventBus(newDto, process);
        return appSubmissionDto;
    }

    private void eventBus(AppSubmissionDto appSubmissionDto, Process process) {
        //prepare request parameters
        appSubmissionDto.setEventRefNo(appSubmissionDto.getAppGrpNo());
        eventBusHelper.submitAsyncRequest(appSubmissionDto, generateIdClient.getSeqId().getEntity(),
                EventBusConsts.SERVICE_NAME_APPSUBMIT, EventBusConsts.OPERATION_NEW_APP_SUBMIT,
                appSubmissionDto.getEventRefNo(), "Submit Application",
                appSubmissionDto.getAppGrpId());
    }

    @Override
    public void sendEmailAndSMSAndMessage(AppSubmissionDto appSubmissionDto, String applicantName) {
        if (appSubmissionDto.getAppType().equals(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION)) {
            try {
                List<ApplicationDto> applicationDtos = appSubmissionDto.getApplicationDtos();
                if (IaisCommonUtils.isEmpty(applicationDtos)) {
                    applicationDtos = appCommService.getApplicationsByGroupNo(appSubmissionDto.getAppGrpNo());
                    appSubmissionDto.setApplicationDtos(applicationDtos);
                }
                for (ApplicationDto applicationDto:applicationDtos
                     ) {
                    AppSubmissionDto appSubmissionDto1 =getAppSubmissionDto(applicationDto.getApplicationNo());
                    String applicationType = MasterCodeUtil.getCodeDesc(applicationDto.getApplicationType());
                    String applicationNumber = applicationDto.getApplicationNo();
                    Map<String, Object> subMap = IaisCommonUtils.genNewHashMap();
                    subMap.put(APPLICATION_TYPE, applicationType);
                    subMap.put(APPLICATION_NUMBER, applicationNumber);
                    subMap.put("temp", "has");
                    String emailSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_EN_NAP_001_EMAIL, subMap);
                    String smsSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_EN_NAP_001_SMS, subMap);
                    String messageSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_EN_NAP_001_MSG, subMap);
                    log.debug(StringUtil.changeForLog("emailSubject : " + emailSubject));
                    log.debug(StringUtil.changeForLog("smsSubject : " + smsSubject));
                    log.debug(StringUtil.changeForLog("messageSubject : " + messageSubject));
                    Map<String, Object> templateContent = IaisCommonUtils.genNewHashMap();
                    templateContent.put("ApplicantName", applicantName);
                    templateContent.put(APPLICATION_TYPE, applicationType);
                    templateContent.put(APPLICATION_NUMBER, applicationNumber);
                    templateContent.put("applicationDate", Formatter.formatDateTime(new Date()));
                    HcsaServiceDto baseServiceDto = HcsaServiceCacheHelper.getServiceById(applicationDto.getServiceId());
                    AppGrpPremisesDto appGrpPremisesDto=appSubmissionDto1.getAppGrpPremisesDtoList().get(0);
                    for (AppGrpPremisesDto premisesDto:appSubmissionDto1.getAppGrpPremisesDtoList()) {
                        if (premisesDto.getPremisesIndexNo().equals(appSubmissionDto1.getAppPremSpecialisedDtoList().get(0).getPremisesVal())) {
                            appGrpPremisesDto = premisesDto;
                            break;
                        }
                    }
                    templateContent.put("svcNameMOSD", baseServiceDto.getSvcName()+" ("+appGrpPremisesDto.getAddress()+")");
                    templateContent.put("BusinessName", appGrpPremisesDto.getHciName());
                    templateContent.put("LicenseeName",  appSubmissionDto1.getSubLicenseeDto().getLicenseeName());
                    templateContent.put("isSpecial", "N");
                    List<AppPremSubSvcRelDto> appPremSubSvcRelDtos=appSubmissionDto1.getAppPremSpecialisedDtoList().get(0).getFlatAppPremSubSvcRelList(dto -> HcsaConsts.SERVICE_TYPE_SPECIFIED.equals(dto.getSvcType()));

                    if (!IaisCommonUtils.isEmpty(appPremSubSvcRelDtos)) {
                        int i=0;
                        StringBuilder svcNameLicNo = new StringBuilder();
                        for (AppPremSubSvcRelDto specSvc : appPremSubSvcRelDtos) {
                            HcsaServiceDto specServiceDto = HcsaServiceCacheHelper.getServiceById(specSvc.getSvcId());
                            String svcName1 = specServiceDto.getSvcName();
                            String index=ALPHABET_ARRAY_PROTOTYPE[i++];
                            svcNameLicNo.append("<p>(").append(index).append(")&nbsp;&nbsp;").append(svcName1).append("</p>");

                        }
                        templateContent.put("isSpecial", "Y");
                        String specialSvcSecName="Specified Services";
                        List<HcsaSvcSpePremisesTypeDto> hcsaSvcSpePremisesTypeDtos = hcsaServiceClient.getHcsaSvcSpePremisesTypeDtos(baseServiceDto.getSvcName(),
                                applicationDto.getServiceId()).getEntity();
                        for (HcsaSvcSpePremisesTypeDto spe:hcsaSvcSpePremisesTypeDtos
                             ) {
                            if(StringUtil.isNotEmpty(spe.getSpecialSvcSecName())&&spe.getPremisesType().equals(appGrpPremisesDto.getPremisesType())){
                                specialSvcSecName=spe.getSpecialSvcSecName();
                                break;
                            }
                        }
                        templateContent.put("ss1ss2Header", specialSvcSecName);
                        templateContent.put("ss1ss2", svcNameLicNo.toString());

                    }

                    templateContent.put("isSelfAssessment", "Y");
                    String loginUrl = HmacConstants.HTTPS + "://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
                    templateContent.put("systemLink", loginUrl);
                    String paymentMethodName = "noNeedPayment";
                    String payMethod = appSubmissionDto.getPaymentMethod();
                    if (ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(payMethod)) {
                        paymentMethodName = "GIRO";
                        templateContent.put("usualDeduction", "next 7 working days");
                        templateContent.put("accountNumber", appSubmissionDto.getGiroAcctNum());
                        //need change giro
                    } else if (ApplicationConsts.PAYMENT_METHOD_NAME_CREDIT.equals(payMethod)
                            || ApplicationConsts.PAYMENT_METHOD_NAME_NETS.equals(payMethod)
                            || ApplicationConsts.PAYMENT_METHOD_NAME_PAYNOW.equals(payMethod)) {
                        paymentMethodName = "onlinePayment";
                    }
                    templateContent.put("emailAddress", systemParamConfig.getSystemAddressOne());
                    templateContent.put("paymentMethod", paymentMethodName);
                    templateContent.put("paymentAmount",
                            appSubmissionDto.getTotalAmountGroup() == null ? appSubmissionDto.getAmountStr() : String.valueOf(
                                    appSubmissionDto.getTotalAmountGroup()));
                    String syName = "<b>" + AppConsts.MOH_AGENCY_NAM_GROUP + "<br/>" + AppConsts.MOH_AGENCY_NAME + "</b>";
                    templateContent.put("MOH_AGENCY_NAME", syName);
                    sendEmal(applicationDto, emailSubject, templateContent, MsgTemplateConstants.MSG_TEMPLATE_EN_NAP_001_EMAIL);

                    sendSms(applicationDto, smsSubject, MsgTemplateConstants.MSG_TEMPLATE_EN_NAP_001_SMS);

                    List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
                    svcCodeList.add(baseServiceDto.getSvcCode());
                    sendMessage(applicationDto, messageSubject, templateContent, svcCodeList, MsgTemplateConstants.MSG_TEMPLATE_EN_NAP_001_MSG);
                    log.info("end send email sms and msg");
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                log.info("send app sumbit email fail");
            }
        }

    }

    private void sendMessage(ApplicationDto applicationDto, String messageSubject, Map<String, Object> templateContent,
            List<String> svcCodeList, String templateId) {
        EmailParam msgParam = new EmailParam();
        msgParam.setTemplateId(templateId);
        msgParam.setTemplateContent(templateContent);
        msgParam.setSubject(messageSubject);
        msgParam.setQueryCode(applicationDto.getApplicationNo());
        msgParam.setReqRefNum(applicationDto.getApplicationNo());
        msgParam.setRefId(applicationDto.getApplicationNo());
        msgParam.setSvcCodeList(svcCodeList);
        msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
        notificationHelper.sendNotification(msgParam);
    }

    private void sendSms(ApplicationDto applicationDto, String smsSubject, String templateId) {
        EmailParam smsParam = new EmailParam();
        smsParam.setTemplateId(templateId);
        smsParam.setSubject(smsSubject);
        smsParam.setQueryCode(applicationDto.getApplicationNo());
        smsParam.setReqRefNum(applicationDto.getApplicationNo());
        smsParam.setRefId(applicationDto.getApplicationNo());
        smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
        notificationHelper.sendNotification(smsParam);
    }

    private void sendEmal(ApplicationDto applicationDto, String emailSubject, Map<String, Object> templateContent, String templateId) {
        EmailParam emailParam = new EmailParam();
        emailParam.setTemplateId(templateId);
        emailParam.setTemplateContent(templateContent);
        emailParam.setSubject(emailSubject);
        emailParam.setQueryCode(applicationDto.getApplicationNo());
        emailParam.setReqRefNum(applicationDto.getApplicationNo());
        emailParam.setRefId(applicationDto.getApplicationNo());
        emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
        notificationHelper.sendNotification(emailParam);
    }

    @Override
    public ApplicationDto getAppById(String appId) {
        return applicationFeClient.getApplicationById(appId).getEntity();
    }

    @Override
    public List<MenuLicenceDto> setPremAdditionalInfo(List<MenuLicenceDto> menuLicenceDtos) {
        return licenceClient.setPremAdditionalInfo(menuLicenceDtos).getEntity();
    }

    @Override
    public List<GiroAccountInfoDto> getGiroAccountByHciCodeAndOrgId(List<String> hciCode, String orgId) {
        log.debug("AppSubmissionServiceImpl getGiroAccount [hciCode] hciCode is empty: {},[orgId] orgId:{}",
                IaisCommonUtils.isEmpty(hciCode), orgId);
        return licenceClient.getGiroAccountByHciCodeAndOrgId(hciCode, orgId).getEntity();
    }

    @Override
    public List<String> saveFileList(List<File> fileList) {
        return comFileRepoClient.saveFileRepo(fileList);
    }

    @Override
    public void updateDraftStatus(String draftNo, String status) {
        log.debug(StringUtil.changeForLog("The doPaymentUpDate start ..."));
        applicationFeClient.updateDraftStatus(draftNo, status);
        log.debug(StringUtil.changeForLog("updateDraftStatus end ..."));
    }

    @Override
    public List<ApplicationSubDraftDto> getDraftListBySvcCodeAndStatus(List<String> svcCodeList, String status, String licenseeId,
            String appType) {
        return applicationFeClient.getDraftListBySvcCodeAndStatus(svcCodeList, licenseeId, status, appType).getEntity();
    }

    @Override
    public boolean canApplyEasOrMts(String licenseeId, List<HcsaServiceDto> hcsaServiceDtos) {
        log.debug(StringUtil.changeForLog("check can create eas or mts service start ..."));
        boolean canCreateEasOrMts = false;
        if (!StringUtil.isEmpty(licenseeId) && !IaisCommonUtils.isEmpty(hcsaServiceDtos)) {
            List<String> svcNames = IaisCommonUtils.genNewArrayList();
            for (HcsaServiceDto hcsaServiceDto : hcsaServiceDtos) {
                svcNames.add(hcsaServiceDto.getSvcName());
            }
            AppPremisesDoQueryDto appPremisesDoQueryDto = new AppPremisesDoQueryDto();
            List<HcsaServiceDto> hcsaServiceDtoList = configCommClient.getHcsaServiceByNames(svcNames).getEntity();
            List<String> svcIds = IaisCommonUtils.genNewArrayList();
            for (HcsaServiceDto hcsaServiceDto : hcsaServiceDtoList) {
                svcIds.add(hcsaServiceDto.getId());
            }
            appPremisesDoQueryDto.setLicenseeId(licenseeId);
            appPremisesDoQueryDto.setSvcIdList(svcIds);
            List<PremisesDto> premisesDtos = licCommService.getPremisesByLicseeIdAndSvcName(licenseeId, svcNames);
            List<AppGrpPremisesDto> appGrpPremisesDtos = appCommService.getPendAppPremises(appPremisesDoQueryDto);
            log.debug("licence record size {}", premisesDtos.size());
            log.debug("pending application record size {}", appGrpPremisesDtos.size());
            if (IaisCommonUtils.isEmpty(premisesDtos) && IaisCommonUtils.isEmpty(appGrpPremisesDtos)) {
                canCreateEasOrMts = true;
            }
        }
        log.debug(StringUtil.changeForLog("check can create eas or mts service end ..."));
        return canCreateEasOrMts;
    }

    @Deprecated
    @Override
    public List<AppDeclarationDocDto> getAppDeclarationDocDto(HttpServletRequest request) {
        Map<String, File> fileMap = (Map<String, File>) request.getSession().getAttribute("seesion_files_map_ajax_feselectedDeclFile");
        List<PageShowFileDto> pageShowFileDtos = new ArrayList<>(5);
        List<AppDeclarationDocDto> appDeclarationDocDtoList = new ArrayList<>(12);
        List<File> files = new ArrayList<>(5);
        if (fileMap != null && !fileMap.isEmpty()) {
            fileMap.forEach((k, v) -> {
                if (v != null) {
                    files.add(v);
                    String fileMd5 = FileUtils.getFileMd5(v);
                    PageShowFileDto pageShowFileDto = new PageShowFileDto();
                    pageShowFileDto.setFileName(v.getName());
                    String e = k.substring(k.lastIndexOf('e') + 1);
                    pageShowFileDto.setIndex(e);
                    pageShowFileDto.setFileMapId("selectedFileDiv" + e);
                    long l = v.length() / 1024;
                    pageShowFileDto.setSize(Integer.valueOf(Long.toString(l)));
                    pageShowFileDto.setMd5Code(fileMd5);
                    pageShowFileDtos.add(pageShowFileDto);
                    AppDeclarationDocDto appDeclarationDocDto = new AppDeclarationDocDto();
                    appDeclarationDocDto.setDocName(v.getName());
                    appDeclarationDocDto.setDocSize(Integer.valueOf(Long.toString(l)));
                    appDeclarationDocDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    appDeclarationDocDto.setMd5Code(fileMd5);
                    appDeclarationDocDto.setVersion(1);
                    appDeclarationDocDto.setSeqNum(Integer.valueOf(e));
                    appDeclarationDocDtoList.add(appDeclarationDocDto);
                }
            });
        }
        List<String> list = comFileRepoClient.saveFileRepo(files);
        if (list != null) {
            ListIterator<String> iterator = list.listIterator();
            for (int j = 0; j < appDeclarationDocDtoList.size(); j++) {
                String fileRepoId = appDeclarationDocDtoList.get(j).getFileRepoId();
                if (fileRepoId == null && iterator.hasNext()) {
                    String next = iterator.next();
                    pageShowFileDtos.get(j).setFileUploadUrl(next);
                    appDeclarationDocDtoList.get(j).setFileRepoId(next);
                    iterator.remove();
                }
            }
        }
        pageShowFileDtos.sort(Comparator.comparing(PageShowFileDto::getFileMapId));
        request.getSession().setAttribute("pageShowFileDtos", pageShowFileDtos);
        return appDeclarationDocDtoList;
    }

    @Override
    public LicenceDto getLicenceDtoById(String licenceId) {
        return licenceClient.getLicDtoById(licenceId).getEntity();
    }

    @Override
    public SubLicenseeDto getSubLicenseeByLicenseeId(String licenseeId, String uenNo) {
        LicenseeDto licenseeDto = organizationLienceseeClient.getLicenseeById(licenseeId).getEntity();
        Map<String, String> fieldMap = IaisCommonUtils.genNewHashMap();
        fieldMap.put("name", "licenseeName");
        fieldMap.put("organizationId", "orgId");
        fieldMap.put("officeTelNo", "telephoneNo");
        fieldMap.put("emilAddr", "emailAddr");
        SubLicenseeDto subLicenseeDto = MiscUtil.transferEntityDto(licenseeDto, SubLicenseeDto.class, fieldMap);
        if (subLicenseeDto == null) {
            subLicenseeDto = new SubLicenseeDto();
        }
        subLicenseeDto.setUenNo(uenNo);
        if (OrganizationConstants.LICENSEE_TYPE_CORPPASS.equals(subLicenseeDto.getLicenseeType())) {
            subLicenseeDto.setLicenseeType(OrganizationConstants.LICENSEE_SUB_TYPE_COMPANY);
        } else if (OrganizationConstants.LICENSEE_SUB_TYPE_SOLO.equals(subLicenseeDto.getLicenseeType())) {
            subLicenseeDto.setAssignSelect(IaisEGPConstant.ASSIGN_SELECT_ADD_NEW);
            LicenseeIndividualDto licenseeIndividualDto = Optional.ofNullable(licenseeDto)
                    .map(LicenseeDto::getLicenseeIndividualDto)
                    .orElseGet(LicenseeIndividualDto::new);
            subLicenseeDto.setIdType(licenseeIndividualDto.getIdType());
            subLicenseeDto.setIdNumber(licenseeIndividualDto.getIdNo());
            if (!StringUtil.isEmpty(licenseeDto.getMobileNo())) {
                subLicenseeDto.setTelephoneNo(licenseeDto.getMobileNo());
            }
            subLicenseeDto.setLicenseeType(OrganizationConstants.LICENSEE_SUB_TYPE_SOLO);
        }
        return subLicenseeDto;
    }

    @Override
    public boolean validateSubLicenseeDto(Map<String, String> errorMap, SubLicenseeDto subLicenseeDto, HttpServletRequest request) {
        if (subLicenseeDto == null) {
            if (errorMap != null) {
                errorMap.put("licenseeType", "Invalid Data");
            }
            return false;
        }
        Map<String, String> map = IaisCommonUtils.genNewHashMap();
        String propertyName = "save";
        if (OrganizationConstants.LICENSEE_SUB_TYPE_SOLO.equals(subLicenseeDto.getLicenseeType())) {
            propertyName = "soloSave";
        }
        ValidationResult result = WebValidationHelper.validateProperty(subLicenseeDto, propertyName);
        if (result != null) {
            map = result.retrieveAll();
        }

        // add log
        if (!map.isEmpty()) {
            log.info(StringUtil.changeForLog("Error Message For the Sub Licensee : " + map + " - " +
                    JsonUtil.parseToJson(subLicenseeDto)));
        }
        if (OrganizationConstants.LICENSEE_SUB_TYPE_COMPANY.equals(subLicenseeDto.getLicenseeType())) {
            if (!map.isEmpty() && errorMap != null) {
                errorMap.put("licenseeType", "Invalid Licensee Type");
            }
        } else {
            if (errorMap != null) {
                errorMap.putAll(map);
            }
        }
        return map.isEmpty();
    }

    @Override
    public void sendEmailForGiroFailAndSMSAndMessage(ApplicationGroupDto applicationGroupDto) {
        try {
            log.info(StringUtil.changeForLog(
                    "---------applicationGroupDto appgroupno : " + applicationGroupDto.getGroupNo() + " sendEmailForGiroFailAndSMSAndMessage start ----------"));
            AppSubmissionDto appSubmissionDto = getAppSubmissionDtoByAppNo(applicationGroupDto.getGroupNo() + "-01");
            List<ApplicationDto> applicationDtos = appSubmissionDto.getApplicationDtos();
            if (IaisCommonUtils.isEmpty(applicationDtos)) {
                applicationDtos = appCommService.getApplicationsByGroupNo(appSubmissionDto.getAppGrpNo());
                appSubmissionDto.setApplicationDtos(applicationDtos);
            }
            ApplicationDto applicationDto = applicationDtos.get(0);
            String applicationType = MasterCodeUtil.getCodeDesc(applicationDto.getApplicationType());
            int index = 0;
            StringBuilder stringBuilderAPPNum = new StringBuilder();
            for (ApplicationDto applicationDtoApp : appSubmissionDto.getApplicationDtos()) {
                if (index != 0) {
                    stringBuilderAPPNum.append(" and ");
                }
                stringBuilderAPPNum.append(applicationDtoApp.getApplicationNo());
                index++;
            }
            String applicationNumber = stringBuilderAPPNum.toString();
            Map<String, Object> subMap = IaisCommonUtils.genNewHashMap();
            subMap.put(APPLICATION_TYPE, applicationType);
            subMap.put(APPLICATION_NUMBER, applicationNumber);
            String emailSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_EN_FEP_006_EMAIL, subMap);
            String smsSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_EN_FEP_006_SMS, subMap);
            String messageSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_EN_FEP_006_MSG, subMap);
            log.debug(StringUtil.changeForLog("emailSubject : " + emailSubject));
            log.debug(StringUtil.changeForLog("smsSubject : " + smsSubject));
            log.debug(StringUtil.changeForLog("messageSubject : " + messageSubject));
            Map<String, Object> templateContent = IaisCommonUtils.genNewHashMap();

            templateContent.put("ApplicantName", getApplicantName(applicationGroupDto));
            templateContent.put(APPLICATION_TYPE, applicationType);
            templateContent.put(APPLICATION_NUMBER, applicationNumber);
            templateContent.put("ApplicationDate", Formatter.formatDateTime(new Date()));
            templateContent.put("monthOfGiro", Formatter.formatDateTime(MiscUtil.addDays(new Date(), 7), Formatter.DATE));
            templateContent.put("email", systemParamConfig.getSystemAddressOne());
            String syName = "<b>" + AppConsts.MOH_AGENCY_NAM_GROUP + "<br/>" + AppConsts.MOH_AGENCY_NAME + "</b>";
            templateContent.put("MOH_AGENCY_NAME", syName);
            sendEmal(applicationDto, emailSubject, templateContent, MsgTemplateConstants.MSG_TEMPLATE_EN_FEP_006_EMAIL);

            sendSms(applicationDto, smsSubject, MsgTemplateConstants.MSG_TEMPLATE_EN_FEP_006_SMS);

            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
            for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtoList) {
                if (!svcCodeList.contains(appSvcRelatedInfoDto.getServiceCode())) {
                    svcCodeList.add(appSvcRelatedInfoDto.getServiceCode());
                }
            }
            sendMessage(applicationDto, messageSubject, templateContent, svcCodeList, MsgTemplateConstants.MSG_TEMPLATE_EN_FEP_006_MSG);
            log.info("end send email sms and msg");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.info("send app sumbit email fail");
        }
    }

    private String getApplicantName(ApplicationGroupDto applicationGroupDto) {
        String applicantId = applicationGroupDto.getSubmitBy();
        if (ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION.equals(applicationGroupDto.getAppType()) ||
                ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(applicationGroupDto.getAppType())) {
            String licenseeId = applicationGroupDto.getLicenseeId();
            LicenseeDto licenseeDto = licenseeService.getLicenseeDtoById(licenseeId);
            return licenseeDto.getName() == null ? EMPTY : licenseeDto.getName();
        } else {
            OrgUserDto orgUserDto = organizationLienceseeClient.retrieveOneOrgUserAccount(applicantId).getEntity();
            return orgUserDto.getDisplayName() == null ? EMPTY : orgUserDto.getDisplayName();
        }
    }

    private String getEmailSubject(String templateId, Map<String, Object> subMap) {
        String subject = "-";
        if (!StringUtil.isEmpty(templateId)) {
            MsgTemplateDto emailTemplateDto = getMsgTemplateById(templateId);
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

    @Override
    public AppSubmissionDto submitRequestInformation(AppSubmissionRequestInformationDto appSubmissionRequestInformationDto,
            Process process) {
        appSubmissionRequestInformationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        AppSubmissionDto appSubmissionDto = appSubmissionRequestInformationDto.getAppSubmissionDto();
        //asynchronous save the other data.
        informationEventBus(appSubmissionRequestInformationDto, process);
        return appSubmissionDto;
    }

    @Override
    public AppSubmissionDto submitRequestRfcRenewInformation(AppSubmissionRequestInformationDto appSubmissionRequestInformationDto,
            Process process) {
        appSubmissionRequestInformationDto.setEventRefNo(UUID.randomUUID().toString());
        AppSubmissionDto appSubmissionDto = appSubmissionRequestInformationDto.getAppSubmissionDto();
        appSubmissionRequestInformationDto.setAppSubmissionDto(ApplicationHelper.toSlim(appSubmissionDto));
        appSubmissionRequestInformationDto.setOldAppSubmissionDto(
                ApplicationHelper.toSlim(appSubmissionRequestInformationDto.getOldAppSubmissionDto()));
        eventBusHelper.submitAsyncRequest(appSubmissionRequestInformationDto,
                generateIdClient.getSeqId().getEntity(),
                EventBusConsts.SERVICE_NAME_APPSUBMIT, EventBusConsts.OPERATION_REQUEST_RFC_RENEW_INFORMATION_SUBMIT,
                appSubmissionRequestInformationDto.getEventRefNo(), "Submit RFC Renew Application",
                appSubmissionRequestInformationDto.getAppSubmissionDto().getAppGrpId());
        return appSubmissionDto;
    }

    @Override
    public List<ApplicationDto> listApplicationByGroupId(String groupId) {
        return applicationFeClient.listApplicationByGroupId(groupId).getEntity();
    }

    @Override
    public List<AppSubmissionDto> saveAppsForRequestForGoupAndAppChangeByList(List<AppSubmissionDto> appSubmissionDtos) {
        if (IaisCommonUtils.isEmpty(appSubmissionDtos)) {
            return appSubmissionDtos;
        }
        return applicationFeClient.saveAppsForRequestForGoupAndAppChangeByList(appSubmissionDtos).getEntity();
    }

    @Override
    public AppSubmissionDto getAppSubmissionDtoDraft(String draftNo) {
        if (StringUtil.isEmpty(draftNo)) {
            return null;
        }
        return applicationFeClient.draftNumberGet(draftNo).getEntity();
    }

    @Override
    public List<AppSubmissionDto> getAppSubmissionDtoDrafts(String draftNo) {
        if (StringUtil.isEmpty(draftNo)) {
            return IaisCommonUtils.genNewArrayList();
        }
        return applicationFeClient.getAppSubmissionDtoDrafts(draftNo).getEntity();
    }

    @Override
    public void handleDraft(String draftNo, String licenseeId, AppSubmissionDto appSubmissionDto,
            List<AppSubmissionDto> appSubmissionDtoList) {
        doSaveDraft(appSubmissionDto);
        List<String> licenceIds = appSubmissionDtoList.parallelStream()
                .map(AppSubmissionDto::getLicenceId)
                .collect(Collectors.toList());
        updateDrafts(licenseeId, licenceIds, draftNo);
    }

    @Override
    public void handleDraft(String licenseeId, List<AppSubmissionDto> appSubmissionDtos) {
        if (IaisCommonUtils.isEmpty(appSubmissionDtos)) {
            log.info(StringUtil.changeForLog("There is no data to be saved!!!"));
            return;
        }
        applicationFeClient.saveDrafts(appSubmissionDtos);
        String draftNo = appSubmissionDtos.get(0).getDraftNo();
        List<String> licenceIds = appSubmissionDtos.parallelStream()
                .map(AppSubmissionDto::getLicenceId)
                .collect(Collectors.toList());
        updateDrafts(licenseeId, licenceIds, draftNo);
    }

    @Override
    public AppSubmissionDto doSaveDraft(AppSubmissionDto appSubmissionDto) {
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return applicationFeClient.saveDraft(appSubmissionDto).getEntity();
    }

    @Override
    public void updateDrafts(String licenseeId, List<String> licenceIds, String excludeDraftNo) {
        log.info(StringUtil.changeForLog(
                "Licensee Id: " + licenseeId + "Licence Ids: " + licenceIds + " - excludeDraftNo: " + excludeDraftNo));
        if (StringUtil.isEmpty(licenseeId) || IaisCommonUtils.isEmpty(licenceIds) || StringUtil.isEmpty(excludeDraftNo)) {
            return;
        }
        CompletableFuture.runAsync(() -> applicationFeClient.updateDrafts(licenseeId, licenceIds, excludeDraftNo));
    }

    @Override
    public void deleteDraftAsync(String draftNo, String appGrpId) {
        if (StringUtil.isEmpty(draftNo)) {
            return;
        }
        CompletableFuture.runAsync(() -> {
            boolean canDelete = true;
            if (!StringUtil.isEmpty(appGrpId)) {
                canDelete = IaisCommonUtils.isNotEmpty(appCommService.getAppGrpPremisesByGroupId(appGrpId));
            }
            log.info(StringUtil.changeForLog("Delete Draft: " + canDelete));
            if (canDelete) {
                applicationFeClient.deleteDraftByNo(draftNo);
            } else {
                updateDraftStatus(draftNo, AppConsts.COMMON_STATUS_IACTIVE);
            }
        });
    }

    @Override
    public String getDraftNo(String appType) {
        return systemAdminClient.draftNumber(appType).getEntity();
    }

    @Override
    public String getGroupNo(String appType) {

        return systemAdminClient.applicationNumber(appType).getEntity();
    }

    @Override
    public FeeDto getNewAppAmount(AppSubmissionDto appSubmissionDto, boolean isCharity) {
        log.debug(StringUtil.changeForLog("the getNewAppAmount start ...."));
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        List<AppPremSpecialisedDto> appPremSpecialisedDtos = appSubmissionDto.getAppPremSpecialisedDtoList();
        List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
        List<LicenceFeeDto> licenceFeeQuaryDtos = IaisCommonUtils.genNewArrayList();
        List<HcsaFeeBundleItemDto> hcsaFeeBundleItemDtos = configCommClient.getActiveBundleDtoList().getEntity();
        Set<String> bundleSvcCodes = IaisCommonUtils.genNewHashSet();
        if (IaisCommonUtils.isNotEmpty(hcsaFeeBundleItemDtos)) {
            hcsaFeeBundleItemDtos.forEach(o -> bundleSvcCodes.add(o.getSvcCode()));
        }
        List<AppLicBundleDto[]> appLicBundleDtos = appSubmissionDto.getAppLicBundleDtos();
        List<AppLicBundleDto> appLicBundleDtoList = IaisCommonUtils.genNewArrayList();
        if (IaisCommonUtils.isNotEmpty(appLicBundleDtos)) {
            for (AppLicBundleDto[] albs : appLicBundleDtos) {
                appLicBundleDtoList.addAll(Arrays.asList(albs));
            }
        }
        List<String[]> msList = IaisCommonUtils.genNewArrayList();
        String[] msPreOrConArray = {EMPTY, EMPTY, EMPTY};
        msList.add(msPreOrConArray);
        List<String[]> dsList = IaisCommonUtils.genNewArrayList();
        String[] dsPreOrConArray = {EMPTY, EMPTY, EMPTY};
        dsList.add(dsPreOrConArray);
        int otherBundleLicCount=0;
        if (IaisCommonUtils.isNotEmpty(appLicBundleDtoList)) {
            for (AppLicBundleDto alb : appLicBundleDtoList) {
                if (alb == null || StringUtil.isEmpty(alb.getLicenceId())) {
                    otherBundleLicCount++;
                    continue;
                }
                if (alb.getSvcCode().equals(AppServicesConsts.SERVICE_CODE_MEDICAL_SERVICE)) {
                    int index = 0;
                    if (alb.getPremisesType().equals(ApplicationConsts.PREMISES_TYPE_MOBILE)) {
                        index = 1;
                    }
                    if (alb.getPremisesType().equals(ApplicationConsts.PREMISES_TYPE_REMOTE)) {
                        index = 2;
                    }
                    boolean find = false;
                    for (String[] ms : msList
                    ) {
                        if (StringUtil.isEmpty(ms[index])) {
                            ms[index] = Lic_BUNDLE;
                            find = true;
                            break;
                        }
                    }
                    if (!find) {
                        String[] newArray = {EMPTY, EMPTY, EMPTY};
                        newArray[index] = Lic_BUNDLE;
                        msList.add(newArray);
                    }
                }
                if (alb.getSvcCode().equals(AppServicesConsts.SERVICE_CODE_DENTAL_SERVICE)) {
                    int index = 0;
                    if (alb.getPremisesType().equals(ApplicationConsts.PREMISES_TYPE_MOBILE)) {
                        index = 1;
                    }
                    if (alb.getPremisesType().equals(ApplicationConsts.PREMISES_TYPE_REMOTE)) {
                        index = 2;
                    }
                    boolean find = false;
                    for (String[] ms : dsList
                    ) {
                        if (StringUtil.isEmpty(ms[index])) {
                            ms[index] = Lic_BUNDLE;
                            find = true;
                            break;
                        }
                    }
                    if (!find) {
                        String[] newArray = {EMPTY, EMPTY, EMPTY};
                        newArray[index] = Lic_BUNDLE;
                        dsList.add(newArray);
                    }
                }
            }
        }
        if (!IaisCommonUtils.isEmpty(appGrpPremisesDtos)) {
            log.debug("appGrpPremisesDtos size {}", appGrpPremisesDtos.size());
            for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {

                boolean hadEas = false;
                boolean hadMts = false;
                boolean hadAch = false;

                for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                    String serviceCode = appSvcRelatedInfoDto.getServiceCode();
                    if (AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE.equals(serviceCode)) {
                        hadEas = true;
                    } else if (AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(serviceCode)) {
                        hadMts = true;
                    } else if (AppServicesConsts.SERVICE_CODE_ACUTE_HOSPITAL.equals(serviceCode)) {
                        hadAch = true;
                    }
                }
                List<LicenceFeeDto> achLicenceFeeDtoList = IaisCommonUtils.genNewArrayList();
                int easVehicleCount = getEasVehicleCount(appSvcRelatedInfoDtos);
                int mtsVehicleCount = getMtsVehicleCount(appSvcRelatedInfoDtos);
                log.debug("eas vehicle count is {}", easVehicleCount);
                log.debug("mts vehicle count is {}", mtsVehicleCount);
                for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                    LicenceFeeDto licenceFeeDto = new LicenceFeeDto();
                    licenceFeeDto.setBundle(0);
                    licenceFeeDto.setAppGrpNo(appSubmissionDto.getAppGrpNo());
                    String serviceCode = appSvcRelatedInfoDto.getServiceCode();
                    licenceFeeDto.setBaseService(serviceCode);
                    licenceFeeDto.setServiceCode(serviceCode);
                    licenceFeeDto.setServiceName(appSvcRelatedInfoDto.getServiceName());
                    licenceFeeDto.setPremises(appGrpPremisesDto.getAddress());
                    licenceFeeDto.setCharity(isCharity);

                    if (StringUtil.isNotEmpty(appSvcRelatedInfoDto.getAlignLicenceNo())) {
                        LicenceDto licenceDto = requestForChangeService.getLicenceDtoByLicNo(appSvcRelatedInfoDto.getAlignLicenceNo());
                        if (licenceDto != null) {
                            Date licExpiryDate = licenceDto.getExpiryDate();
                            licenceFeeDto.setExpiryDate(licExpiryDate);
                        }
                    }
                    log.info(StringUtil.changeForLog("svcName:" + appSvcRelatedInfoDto.getServiceName()));
                    //set mosd bundle
                    if (serviceCode.equals(AppServicesConsts.SERVICE_CODE_MEDICAL_SERVICE)) {
                        if (appGrpPremisesDto.getPremisesType().equals(
                                ApplicationConsts.PREMISES_TYPE_PERMANENT) || appGrpPremisesDto.getPremisesType().equals(
                                ApplicationConsts.PREMISES_TYPE_CONVEYANCE)) {
                            boolean find = false;
                            for (String[] ms : msList) {
                                if (StringUtil.isEmpty(ms[0])) {
                                    ms[0] = appGrpPremisesDto.getPremisesType();
                                    find = true;
                                    if (Lic_BUNDLE.equals(ms[1]) || Lic_BUNDLE.equals(ms[2])) {
                                        licenceFeeDto.setBundle(4);

                                        if(Lic_BUNDLE.equals(ms[1])&& EMPTY.equals(ms[2])) {
                                            licenceFeeDto.setBundle(3);
                                        }
                                        if(Lic_BUNDLE.equals(ms[2])&& EMPTY.equals(ms[1])) {
                                            licenceFeeDto.setBundle(4);
                                        }
                                    } else {
                                        licenceFeeDto.setBundle(0);
                                    }
                                    break;
                                }
                            }
                            if (!find) {
                                String[] newArray = {appGrpPremisesDto.getPremisesType(), EMPTY, EMPTY};
                                msList.add(newArray);
                            }
                        }

                        if (appGrpPremisesDto.getPremisesType().equals(ApplicationConsts.PREMISES_TYPE_MOBILE)) {
                            boolean find = false;
                            for (String[] ms : msList) {
                                if (StringUtil.isEmpty(ms[1])) {
                                    ms[1] = appGrpPremisesDto.getPremisesType();
                                    find = true;
                                    if (Lic_BUNDLE.equals(ms[0]) || Lic_BUNDLE.equals(ms[2])) {
                                        licenceFeeDto.setBundle(4);
                                        if (EMPTY.equals(ms[0]) || EMPTY.equals(ms[2])) {
                                            licenceFeeDto.setBundle(3);
                                        }
                                    } else if(!EMPTY.equals(ms[0]) || !EMPTY.equals(ms[2])){
                                        licenceFeeDto.setBundle(3);
                                    }
                                    break;
                                }
                            }
                            if (!find) {
                                String[] newArray = {EMPTY, appGrpPremisesDto.getPremisesType(), EMPTY};
                                msList.add(newArray);
                            }
                        }
                        if (appGrpPremisesDto.getPremisesType().equals(ApplicationConsts.PREMISES_TYPE_REMOTE)) {
                            boolean find = false;
                            for (String[] ms : msList
                            ) {
                                if (StringUtil.isEmpty(ms[2])) {
                                    ms[2] = appGrpPremisesDto.getPremisesType();
                                    find = true;
                                    if (Lic_BUNDLE.equals(ms[0]) || Lic_BUNDLE.equals(ms[1])) {
                                        licenceFeeDto.setBundle(4);
                                        if (EMPTY.equals(ms[0]) || EMPTY.equals(ms[1])) {
                                            licenceFeeDto.setBundle(3);
                                        }
                                    } else if(!EMPTY.equals(ms[0]) || !EMPTY.equals(ms[1])){
                                        licenceFeeDto.setBundle(3);
                                    }
                                    break;
                                }
                            }
                            if (!find) {
                                String[] newArray = {EMPTY, EMPTY, appGrpPremisesDto.getPremisesType()};
                                msList.add(newArray);
                            }
                        }
                    }
                    if (serviceCode.equals(AppServicesConsts.SERVICE_CODE_DENTAL_SERVICE)) {
                        if (appGrpPremisesDto.getPremisesType().equals(
                                ApplicationConsts.PREMISES_TYPE_PERMANENT) || appGrpPremisesDto.getPremisesType().equals(
                                ApplicationConsts.PREMISES_TYPE_CONVEYANCE)) {
                            boolean find = false;
                            for (String[] ms : dsList) {
                                if (StringUtil.isEmpty(ms[0])) {
                                    ms[0] = appGrpPremisesDto.getPremisesType();
                                    find = true;
                                    if (Lic_BUNDLE.equals(ms[1]) || Lic_BUNDLE.equals(ms[2])) {
                                        licenceFeeDto.setBundle(4);

                                        if(Lic_BUNDLE.equals(ms[1])&& EMPTY.equals(ms[2])) {
                                            licenceFeeDto.setBundle(3);
                                        }
                                        if(Lic_BUNDLE.equals(ms[2])&& EMPTY.equals(ms[1])) {
                                            licenceFeeDto.setBundle(4);
                                        }
                                    } else {
                                        licenceFeeDto.setBundle(0);
                                    }
                                    break;
                                }
                            }
                            if (!find) {
                                String[] newArray = {appGrpPremisesDto.getPremisesType(), EMPTY, EMPTY};
                                dsList.add(newArray);
                            }
                        }

                        if (appGrpPremisesDto.getPremisesType().equals(ApplicationConsts.PREMISES_TYPE_MOBILE)) {
                            boolean find = false;
                            for (String[] ms : dsList) {
                                if (StringUtil.isEmpty(ms[1])) {
                                    ms[1] = appGrpPremisesDto.getPremisesType();
                                    find = true;
                                    if (Lic_BUNDLE.equals(ms[0]) || Lic_BUNDLE.equals(ms[2])) {
                                        licenceFeeDto.setBundle(4);
                                        if (EMPTY.equals(ms[0]) || EMPTY.equals(ms[2])) {
                                            licenceFeeDto.setBundle(3);
                                        }
                                    } else if(!EMPTY.equals(ms[0]) || !EMPTY.equals(ms[2])){
                                        licenceFeeDto.setBundle(3);
                                    }
                                    break;
                                }
                            }
                            if (!find) {
                                String[] newArray = {EMPTY, appGrpPremisesDto.getPremisesType(), EMPTY};
                                dsList.add(newArray);
                            }
                        }
                        if (appGrpPremisesDto.getPremisesType().equals(ApplicationConsts.PREMISES_TYPE_REMOTE)) {
                            boolean find = false;
                            for (String[] ms : dsList
                            ) {
                                if (StringUtil.isEmpty(ms[2])) {
                                    ms[2] = appGrpPremisesDto.getPremisesType();
                                    find = true;
                                    if (Lic_BUNDLE.equals(ms[0]) || Lic_BUNDLE.equals(ms[1])) {
                                        licenceFeeDto.setBundle(4);
                                        if (EMPTY.equals(ms[0]) || EMPTY.equals(ms[1])) {
                                            licenceFeeDto.setBundle(3);
                                        }
                                    } else if(!EMPTY.equals(ms[0]) || !EMPTY.equals(ms[1])){
                                        licenceFeeDto.setBundle(3);
                                    }
                                    break;
                                }
                            }
                            if (!find) {
                                String[] newArray = {EMPTY, EMPTY, appGrpPremisesDto.getPremisesType()};
                                dsList.add(newArray);
                            }
                        }
                    }
                    //set EAS/MTS bundle
                    if (!IaisCommonUtils.isEmpty(hcsaFeeBundleItemDtos) && bundleSvcCodes.contains(serviceCode)) {
                        log.debug(StringUtil.changeForLog("set bundle info ..."));
                        if (AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE.equals(serviceCode)) {
                            if (hadEas && hadMts) {
                                //judge vehicle count
                                int matchingTh = configCommClient.getFeeMaxMatchingThByServiceCode(serviceCode).getEntity();
                                if (easVehicleCount + mtsVehicleCount <= matchingTh) {
                                    licenceFeeDto.setBundle(1);
                                } else {
                                    licenceFeeDto.setBundle(2);
                                }
                            } else {
                                setEasMtsBundleInfo(licenceFeeDto, appLicBundleDtoList, serviceCode, easVehicleCount,appSubmissionDto.getAppType());
                                List<String> bundleSvcNameList = IaisCommonUtils.genNewArrayList();
                                List<HcsaFeeBundleItemDto> bundleDtos = getBundleDtoBySvcCode(hcsaFeeBundleItemDtos, serviceCode);

                                for (HcsaFeeBundleItemDto hcsaFeeBundleItemDto : bundleDtos) {
                                    bundleSvcNameList.add(HcsaServiceCacheHelper.getServiceByCode(hcsaFeeBundleItemDto.getSvcCode()).getSvcName());
                                }
                                boolean ceaseEasMts=licenceClient.getBundleLicence("######",appSubmissionDto.getLicenseeId(),bundleSvcNameList).getEntity();
                                if(ceaseEasMts){
                                    licenceFeeDto.setBundle(4);
                                }
                            }
                        } else if (AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(serviceCode)) {
                            if (hadEas && hadMts) {
                                //new eas and mts
                                licenceFeeDto.setBundle(3);
                            } else {
                                setEasMtsBundleInfo(licenceFeeDto, appLicBundleDtoList, serviceCode, mtsVehicleCount,appSubmissionDto.getAppType());
                                List<String> bundleSvcNameList = IaisCommonUtils.genNewArrayList();
                                List<HcsaFeeBundleItemDto> bundleDtos = getBundleDtoBySvcCode(hcsaFeeBundleItemDtos, serviceCode);

                                for (HcsaFeeBundleItemDto hcsaFeeBundleItemDto : bundleDtos) {
                                    bundleSvcNameList.add(HcsaServiceCacheHelper.getServiceByCode(hcsaFeeBundleItemDto.getSvcCode()).getSvcName());
                                }
                                boolean ceaseEasMts=licenceClient.getBundleLicence("######",appSubmissionDto.getLicenseeId(),bundleSvcNameList).getEntity();
                                if(ceaseEasMts){
                                    licenceFeeDto.setBundle(4);
                                }
                            }
                        }
                    }

                    if (AppServicesConsts.SERVICE_CODE_COMMUNITY_HOSPITAL.equals(
                            serviceCode) || AppServicesConsts.SERVICE_CODE_ACUTE_HOSPITAL.equals(serviceCode)) {
                        checkBeds(appGrpPremisesDto, appSvcRelatedInfoDto, licenceFeeDto, serviceCode);
                        if (IaisCommonUtils.isNotEmpty(appLicBundleDtoList)) {
                            for (AppLicBundleDto alb : appLicBundleDtoList) {
                                if (alb == null || StringUtil.isEmpty(alb.getLicenceId())) {
                                    continue;
                                }
                                if (alb.getSvcCode().equals(
                                        AppServicesConsts.SERVICE_CODE_CLINICAL_LABORATORY) || alb.getSvcCode().equals(
                                        AppServicesConsts.SERVICE_CODE_RADIOLOGICAL_SERVICES)) {
                                    licenceFeeDto.setBundle(4);
                                    break;
                                }
                            }
                        }
                    }
                    if (AppServicesConsts.SERVICE_CODE_CLINICAL_LABORATORY.equals(
                            serviceCode) || AppServicesConsts.SERVICE_CODE_RADIOLOGICAL_SERVICES.equals(serviceCode)) {
                        if (IaisCommonUtils.isNotEmpty(appLicBundleDtoList)) {
                            for (AppLicBundleDto alb : appLicBundleDtoList) {
                                if (alb == null || StringUtil.isEmpty(alb.getLicenceId())) {
                                    continue;
                                }
                                if (alb.getSvcCode().equals(
                                        AppServicesConsts.SERVICE_CODE_ACUTE_HOSPITAL) ) {
                                    licenceFeeDto.setBundle(4);
                                    boolean hasBundle=false;
                                    if(IaisCommonUtils.isNotEmpty(licenceFeeQuaryDtos))
                                    for (LicenceFeeDto lf:licenceFeeQuaryDtos
                                         ) {
                                        if(lf.getBundle()==4 && !lf.getServiceCode().equals(licenceFeeDto.getServiceCode())){
                                            hasBundle=true;
                                        }
                                    }
                                    if(hasBundle){
                                        licenceFeeDto.setBundle(3);
                                    }

                                    break;
                                }
                            }
                        }
                    }
                    if (IaisCommonUtils.isNotEmpty(appPremSpecialisedDtos)) {
                        List<LicenceFeeDto> licenceFeeSpecDtos = IaisCommonUtils.genNewArrayList();

                        for (AppPremSpecialisedDto specSvc : appPremSpecialisedDtos
                        ) {
                            if (specSvc.getBaseSvcId().equals(appSvcRelatedInfoDto.getServiceId()) && specSvc.getPremisesVal().equals(
                                    appGrpPremisesDto.getPremisesIndexNo())) {
                                List<AppPremSubSvcRelDto> appPremSubSvcRelDtoList = specSvc.getCheckedAppPremSubSvcRelDtoList();
                                if (IaisCommonUtils.isNotEmpty(appPremSubSvcRelDtoList)) {
                                    for (AppPremSubSvcRelDto subSvc : appPremSubSvcRelDtoList
                                    ) {
                                        if (subSvc.isChecked()) {
                                            LicenceFeeDto specFeeDto = new LicenceFeeDto();
                                            specFeeDto.setBundle(0);
                                            specFeeDto.setBaseService(licenceFeeDto.getBaseService());
                                            specFeeDto.setServiceCode(subSvc.getSvcCode());
                                            specFeeDto.setServiceName(subSvc.getSvcName());
                                            specFeeDto.setPremises(appGrpPremisesDto.getAddress());
                                            specFeeDto.setCharity(isCharity);
                                            licenceFeeSpecDtos.add(specFeeDto);
                                        }
                                    }
                                }
                            }
                        }
                        if (IaisCommonUtils.isNotEmpty(licenceFeeSpecDtos)) {
                            licenceFeeDto.setSpecifiedLicenceFeeDto(licenceFeeSpecDtos);
                        }
                    }
                    if (hadAch && (AppServicesConsts.SERVICE_CODE_CLINICAL_LABORATORY.equals(serviceCode)
                            || AppServicesConsts.SERVICE_CODE_RADIOLOGICAL_SERVICES.equals(serviceCode))) {
                        achLicenceFeeDtoList.add(licenceFeeDto);
                    } else {
                        licenceFeeQuaryDtos.add(licenceFeeDto);
                    }
                }
                if (IaisCommonUtils.isNotEmpty(achLicenceFeeDtoList)) {
                    for (LicenceFeeDto svcFee : licenceFeeQuaryDtos
                    ) {
                        if (svcFee.getServiceCode().equals(AppServicesConsts.SERVICE_CODE_ACUTE_HOSPITAL)) {
                            svcFee.setMosdBundlesLicenceFeeDto(achLicenceFeeDtoList);
                            svcFee.setBundleOtherLicenceNumber(otherBundleLicCount);
                        }
                    }
                }
            }
        }
        log.info(StringUtil.changeForLog("licenceFeeQuaryDtos:" + JsonUtil.parseToJson(licenceFeeQuaryDtos)));
        log.debug(StringUtil.changeForLog("the getNewAppAmount end ...."));
        return configCommClient.newFee(licenceFeeQuaryDtos).getEntity();
    }

    private List<HcsaFeeBundleItemDto> getBundleDtoBySvcCode(List<HcsaFeeBundleItemDto> hcsaFeeBundleItemDtos, String svcCode) {
        List<HcsaFeeBundleItemDto> result = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(hcsaFeeBundleItemDtos) && !StringUtil.isEmpty(svcCode)) {
            //get target bundleId
            String bundleId = null;
            for (HcsaFeeBundleItemDto hcsaFeeBundleItemDto : hcsaFeeBundleItemDtos) {
                if (svcCode.equals(hcsaFeeBundleItemDto.getSvcCode())) {
                    bundleId = hcsaFeeBundleItemDto.getBundleId();
                    break;
                }
            }
            if (bundleId != null) {
                for (HcsaFeeBundleItemDto hcsaFeeBundleItemDto : hcsaFeeBundleItemDtos) {
                    if (bundleId.equals(hcsaFeeBundleItemDto.getBundleId()) && !svcCode.equals(hcsaFeeBundleItemDto.getSvcCode())) {
                        result.add(hcsaFeeBundleItemDto);
                    }
                }
            }

        }
        return result;
    }

    @Override
    public FeeDto getRenewalAmount(List<AppSubmissionDto> appSubmissionDtoList, boolean isCharity) {
        List<LicenceFeeDto> linenceFeeQuaryDtos = IaisCommonUtils.genNewArrayList();
        log.debug(StringUtil.changeForLog("the AppSubmisionServiceImpl getRenewalAmount start ...."));
        log.info(StringUtil.changeForLog("current account is charity:" + isCharity));
        List<HcsaFeeBundleItemDto> hcsaFeeBundleItemDtos = configCommClient.getActiveBundleDtoList().getEntity();
        Set<String> bundleSvcCodes = IaisCommonUtils.genNewHashSet();
        if (IaisCommonUtils.isNotEmpty(hcsaFeeBundleItemDtos)) {
            hcsaFeeBundleItemDtos.forEach(o -> bundleSvcCodes.add(o.getSvcCode()));
        }
        String hciCodeEas = null;
        String hciCodeMts = null;
        boolean hadEas = false;
        boolean hadMts = false;
        boolean hadAch = false;
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtosAll = IaisCommonUtils.genNewArrayList();
        for (AppSubmissionDto appSubmissionDto : appSubmissionDtoList) {
            appSvcRelatedInfoDtosAll.addAll(appSubmissionDto.getAppSvcRelatedInfoDtoList());
            List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
            String hciCode = appGrpPremisesDtos.get(0).getOldHciCode();
            for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSubmissionDto.getAppSvcRelatedInfoDtoList()) {
                String serviceCode = appSvcRelatedInfoDto.getServiceCode();
                if (AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE.equals(serviceCode)) {
                    hadEas = true;
                    hciCodeEas = hciCode;
                } else if (AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(serviceCode)) {
                    hadMts = true;
                    hciCodeMts = hciCode;
                } else if (AppServicesConsts.SERVICE_CODE_ACUTE_HOSPITAL.equals(serviceCode)) {
                    hadAch = true;
                }
            }

        }
        int easVehicleCount = getEasVehicleCount(appSvcRelatedInfoDtosAll);
        int mtsVehicleCount = getMtsVehicleCount(appSvcRelatedInfoDtosAll);
        List<LicenceFeeDto> achLicenceFeeDtoList = IaisCommonUtils.genNewArrayList();

        for (AppSubmissionDto appSubmissionDto : appSubmissionDtoList) {
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
            List<AppPremSpecialisedDto> appPremSpecialisedDtos = appSubmissionDto.getAppPremSpecialisedDtoList();
            List<String> baseServiceIds = IaisCommonUtils.genNewArrayList();
            List<AppLicBundleDto[]> appLicBundleDtos = appSubmissionDto.getAppLicBundleDtos();
            List<AppLicBundleDto> appLicBundleDtoList = IaisCommonUtils.genNewArrayList();
            if (IaisCommonUtils.isNotEmpty(appLicBundleDtos)) {
                for (AppLicBundleDto[] albs : appLicBundleDtos) {
                    appLicBundleDtoList.addAll(Arrays.asList(albs));
                }
            }
            log.debug("eas vehicle count is {}", easVehicleCount);
            log.debug("mts vehicle count is {}", mtsVehicleCount);

            for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                if (HcsaConsts.SERVICE_TYPE_BASE.equals(appSvcRelatedInfoDto.getServiceType())) {
                    baseServiceIds.add(appSvcRelatedInfoDto.getServiceId());
                }
            }
            List<String[]> msList = IaisCommonUtils.genNewArrayList();
            String[] msPreOrConArray = {EMPTY, EMPTY, EMPTY};
            msList.add(msPreOrConArray);
            List<String[]> dsList = IaisCommonUtils.genNewArrayList();
            String[] dsPreOrConArray = {EMPTY, EMPTY, EMPTY};
            dsList.add(dsPreOrConArray);

            if (IaisCommonUtils.isNotEmpty(appLicBundleDtoList)) {
                for (AppLicBundleDto alb : appLicBundleDtoList) {
                    if (alb.getSvcCode().equals(AppServicesConsts.SERVICE_CODE_MEDICAL_SERVICE)) {
                        if(!alb.getPremisesType().equals(appGrpPremisesDtos.get(0).getPremisesType())){
                            int index = 0;
                            if (alb.getPremisesType().equals(ApplicationConsts.PREMISES_TYPE_MOBILE)) {
                                index = 1;
                            }
                            if (alb.getPremisesType().equals(ApplicationConsts.PREMISES_TYPE_REMOTE)) {
                                index = 2;
                            }
                            boolean find = false;
                            for (String[] ms : msList
                            ) {
                                if (StringUtil.isEmpty(ms[index])) {
                                    ms[index] = Lic_BUNDLE;
                                    find = true;
                                    break;
                                }
                            }
                            if (!find) {
                                String[] newArray = {EMPTY, EMPTY, EMPTY};
                                newArray[index] = Lic_BUNDLE;
                                msList.add(newArray);
                            }
                        }
                    }
                    if (alb.getSvcCode().equals(AppServicesConsts.SERVICE_CODE_DENTAL_SERVICE)) {
                        if(!alb.getPremisesType().equals(appGrpPremisesDtos.get(0).getPremisesType())){
                            int index = 0;
                            if (alb.getPremisesType().equals(ApplicationConsts.PREMISES_TYPE_MOBILE)) {
                                index = 1;
                            }
                            if (alb.getPremisesType().equals(ApplicationConsts.PREMISES_TYPE_REMOTE)) {
                                index = 2;
                            }
                            boolean find = false;
                            for (String[] ms : dsList
                            ) {
                                if (StringUtil.isEmpty(ms[index])) {
                                    ms[index] = Lic_BUNDLE;
                                    find = true;
                                    break;
                                }
                            }
                            if (!find) {
                                String[] newArray = {EMPTY, EMPTY, EMPTY};
                                newArray[index] = Lic_BUNDLE;
                                dsList.add(newArray);
                            }
                        }

                    }
                }
            }

            for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
                for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                    LicenceFeeDto licenceFeeDto = new LicenceFeeDto();
                    licenceFeeDto.setAppGrpNo(appSubmissionDto.getAppGrpNo());
                    licenceFeeDto.setBundle(0);
                    licenceFeeDto.setHciCode(appGrpPremisesDto.getOldHciCode());
                    HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByCode(appSvcRelatedInfoDto.getServiceCode());
                    if (hcsaServiceDto == null) {
                        log.info(StringUtil.changeForLog("hcsaServiceDto is empty "));
                        continue;
                    }
                    licenceFeeDto.setBaseService(hcsaServiceDto.getSvcCode());
                    licenceFeeDto.setServiceCode(hcsaServiceDto.getSvcCode());
                    licenceFeeDto.setServiceName(hcsaServiceDto.getSvcName());
                    licenceFeeDto.setPremises(appGrpPremisesDto.getAddress());
                    licenceFeeDto.setCharity(isCharity);
                    String serviceCode = hcsaServiceDto.getSvcCode();

                    if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())) {
                        String licenceId = appSubmissionDto.getLicenceId();
                        LicenceDto licenceDto = requestForChangeService.getLicenceById(licenceId);
                        if (licenceDto != null) {
                            int migrated = licenceDto.getMigrated();
                            if (0 != migrated) {
                                licenceFeeDto.setOldLicenceId(licenceDto.getId());
                                licenceFeeDto.setMigrated(migrated);
                            }
                            Date licExpiryDate = licenceDto.getExpiryDate();
                            licenceFeeDto.setExpiryDate(licExpiryDate);
                        }
                    }
                    //set mosd bundle
                    if (serviceCode.equals(AppServicesConsts.SERVICE_CODE_MEDICAL_SERVICE)) {
                        if (appGrpPremisesDto.getPremisesType().equals(
                                ApplicationConsts.PREMISES_TYPE_PERMANENT) || appGrpPremisesDto.getPremisesType().equals(
                                ApplicationConsts.PREMISES_TYPE_CONVEYANCE)) {
                            boolean find = false;
                            for (String[] ms : msList) {
                                if (StringUtil.isEmpty(ms[0])) {
                                    ms[0]=appGrpPremisesDto.getPremisesType();
                                    find = true;
                                    break;
                                }
                            }
                            if (!find) {
                                String[] newArray = {appGrpPremisesDto.getPremisesType(), EMPTY, EMPTY};
                                msList.add(newArray);
                            }
                        }

                        if (appGrpPremisesDto.getPremisesType().equals(ApplicationConsts.PREMISES_TYPE_MOBILE)) {
                            boolean find = false;
                            for (String[] ms : msList) {
                                if (StringUtil.isEmpty(ms[1])) {
                                    find = true;
                                    ms[1]=appGrpPremisesDto.getPremisesType();
                                    if(!EMPTY.equals(ms[0]) || !EMPTY.equals(ms[2])){
                                        licenceFeeDto.setBundle(3);
                                    }
                                    break;
                                }
                            }
                            if (!find) {
                                String[] newArray = {EMPTY, appGrpPremisesDto.getPremisesType(), EMPTY};
                                msList.add(newArray);
                            }
                        }
                        if (appGrpPremisesDto.getPremisesType().equals(ApplicationConsts.PREMISES_TYPE_REMOTE)) {
                            boolean find = false;
                            for (String[] ms : msList
                            ) {
                                if (StringUtil.isEmpty(ms[2])) {
                                    find = true;
                                    ms[2]=appGrpPremisesDto.getPremisesType();
                                    if(!EMPTY.equals(ms[0]) || !EMPTY.equals(ms[1])){
                                        licenceFeeDto.setBundle(3);
                                    }
                                    break;
                                }
                            }
                            if (!find) {
                                String[] newArray = {EMPTY, EMPTY, appGrpPremisesDto.getPremisesType()};
                                msList.add(newArray);
                            }
                        }
                    }
                    if (serviceCode.equals(AppServicesConsts.SERVICE_CODE_DENTAL_SERVICE)) {
                        if (appGrpPremisesDto.getPremisesType().equals(
                                ApplicationConsts.PREMISES_TYPE_PERMANENT) || appGrpPremisesDto.getPremisesType().equals(
                                ApplicationConsts.PREMISES_TYPE_CONVEYANCE)) {
                            boolean find = false;
                            for (String[] ms : dsList) {
                                if (StringUtil.isEmpty(ms[0])) {
                                    ms[0]=appGrpPremisesDto.getPremisesType();
                                    find = true;
                                    break;
                                }
                            }
                            if (!find) {
                                String[] newArray = {appGrpPremisesDto.getPremisesType(), EMPTY, EMPTY};
                                dsList.add(newArray);
                            }
                        }

                        if (appGrpPremisesDto.getPremisesType().equals(ApplicationConsts.PREMISES_TYPE_MOBILE)) {
                            boolean find = false;
                            for (String[] ms : dsList) {
                                if (StringUtil.isEmpty(ms[1])) {
                                    find = true;
                                    ms[1]=appGrpPremisesDto.getPremisesType();
                                    if(!EMPTY.equals(ms[0]) || !EMPTY.equals(ms[2])){
                                        licenceFeeDto.setBundle(3);
                                    }
                                    break;
                                }
                            }
                            if (!find) {
                                String[] newArray = {EMPTY, appGrpPremisesDto.getPremisesType(), EMPTY};
                                dsList.add(newArray);
                            }
                        }
                        if (appGrpPremisesDto.getPremisesType().equals(ApplicationConsts.PREMISES_TYPE_REMOTE)) {
                            boolean find = false;
                            for (String[] ms : dsList
                            ) {
                                if (StringUtil.isEmpty(ms[2])) {
                                    find = true;
                                    ms[2]=appGrpPremisesDto.getPremisesType();
                                    if(!EMPTY.equals(ms[0]) || !EMPTY.equals(ms[1])){
                                        licenceFeeDto.setBundle(3);
                                    }
                                    break;
                                }
                            }
                            if (!find) {
                                String[] newArray = {EMPTY, EMPTY, appGrpPremisesDto.getPremisesType()};
                                dsList.add(newArray);
                            }
                        }
                    }
                    //set EAS/MTS bundle
                    if (!IaisCommonUtils.isEmpty(hcsaFeeBundleItemDtos) && bundleSvcCodes.contains(serviceCode)) {
                        log.debug(StringUtil.changeForLog("set bundle info ..."));
                        if (AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE.equals(serviceCode)) {
                            int matchingTh = configCommClient.getFeeMaxMatchingThByServiceCode(serviceCode).getEntity();
                            if (easVehicleCount <= matchingTh) {
                                licenceFeeDto.setBundle(1);
                            } else {
                                licenceFeeDto.setBundle(2);
                            }
                            if (hadEas && hadMts) {
                                if (hciCodeEas != null && hciCodeEas.equals(hciCodeMts)) {
                                    if (easVehicleCount + mtsVehicleCount <= matchingTh) {
                                        licenceFeeDto.setBundle(1);
                                    } else {
                                        licenceFeeDto.setBundle(2);
                                    }
                                }
                            } else {
                                setEasMtsBundleInfo(licenceFeeDto, appLicBundleDtoList, serviceCode, easVehicleCount,appSubmissionDto.getAppType());
                            }
                            licenceFeeDto.setConditionalNumber(easVehicleCount + mtsVehicleCount);
                        } else if (AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(serviceCode)) {
                            int matchingTh = configCommClient.getFeeMaxMatchingThByServiceCode(serviceCode).getEntity();
                            if (mtsVehicleCount <= matchingTh) {
                                licenceFeeDto.setBundle(1);
                            } else {
                                licenceFeeDto.setBundle(2);
                            }
                            if (hadEas && hadMts) {
                                if (hciCodeEas != null && hciCodeEas.equals(hciCodeMts)) {
                                    licenceFeeDto.setBundle(3);
                                }
                            } else {
                                setEasMtsBundleInfo(licenceFeeDto, appLicBundleDtoList, serviceCode, mtsVehicleCount,appSubmissionDto.getAppType());
                            }
                            licenceFeeDto.setConditionalNumber(easVehicleCount + mtsVehicleCount);
                        }
                    }

                    checkBeds(appGrpPremisesDto, appSvcRelatedInfoDto, licenceFeeDto, serviceCode);
                    if (IaisCommonUtils.isNotEmpty(appPremSpecialisedDtos)) {
                        List<LicenceFeeDto> licenceFeeSpecDtos = IaisCommonUtils.genNewArrayList();

                        for (AppPremSpecialisedDto specSvc : appPremSpecialisedDtos
                        ) {
                            if (specSvc.getBaseSvcId().equals(appSvcRelatedInfoDto.getServiceId()) && specSvc.getPremisesVal().equals(
                                    appGrpPremisesDto.getPremisesIndexNo())) {
                                List<AppPremSubSvcRelDto> appPremSubSvcRelDtoList = specSvc.getAppPremSubSvcRelDtoList();
                                if (IaisCommonUtils.isNotEmpty(appPremSubSvcRelDtoList)) {
                                    for (AppPremSubSvcRelDto subSvc : appPremSubSvcRelDtoList
                                    ) {
                                        if (subSvc.isChecked()) {
                                            LicenceFeeDto specFeeDto = new LicenceFeeDto();
                                            specFeeDto.setBundle(0);
                                            specFeeDto.setBaseService(licenceFeeDto.getBaseService());
                                            specFeeDto.setServiceCode(subSvc.getSvcCode());
                                            specFeeDto.setServiceName(subSvc.getSvcName());
                                            specFeeDto.setPremises(appGrpPremisesDto.getAddress());
                                            specFeeDto.setCharity(isCharity);
                                            licenceFeeSpecDtos.add(specFeeDto);
                                        }
                                    }
                                }
                            }
                        }
                        if (IaisCommonUtils.isNotEmpty(licenceFeeSpecDtos)) {
                            licenceFeeDto.setSpecifiedLicenceFeeDto(licenceFeeSpecDtos);
                        }
                    }
                    if ( appLicBundleDtoList.size()>1&&hadAch && (AppServicesConsts.SERVICE_CODE_CLINICAL_LABORATORY.equals(
                            serviceCode) || AppServicesConsts.SERVICE_CODE_RADIOLOGICAL_SERVICES.equals(serviceCode))) {
                        achLicenceFeeDtoList.add(licenceFeeDto);
                    }else {
                        linenceFeeQuaryDtos.add(licenceFeeDto);
                    }
                    if (IaisCommonUtils.isNotEmpty(achLicenceFeeDtoList)) {
                        for (LicenceFeeDto svcFee : linenceFeeQuaryDtos
                        ) {
                            if (svcFee.getServiceCode().equals(AppServicesConsts.SERVICE_CODE_ACUTE_HOSPITAL)) {
                                if(IaisCommonUtils.isEmpty(svcFee.getMosdBundlesLicenceFeeDto())&&IaisCommonUtils.isNotEmpty(achLicenceFeeDtoList)){
                                    List<LicenceFeeDto> cloneachLicenceFeeDtoList = IaisCommonUtils.genNewArrayList();
                                    cloneachLicenceFeeDtoList.add(achLicenceFeeDtoList.get(0));
                                    svcFee.setMosdBundlesLicenceFeeDto(cloneachLicenceFeeDtoList);
                                    achLicenceFeeDtoList.remove(cloneachLicenceFeeDtoList.get(0));
                                } else if(IaisCommonUtils.isNotEmpty(svcFee.getMosdBundlesLicenceFeeDto())&&svcFee.getMosdBundlesLicenceFeeDto().size()==1&&IaisCommonUtils.isNotEmpty(achLicenceFeeDtoList)){
                                    List<LicenceFeeDto> cloneachLicenceFeeDtoList = IaisCommonUtils.genNewArrayList();
                                    LicenceFeeDto cloneachLicenceFeeDto =null;
                                    for (LicenceFeeDto dto:achLicenceFeeDtoList
                                         ) {
                                        if(!svcFee.getMosdBundlesLicenceFeeDto().get(0).getServiceCode().equals(dto.getServiceCode())){
                                            cloneachLicenceFeeDto=dto;
                                        }
                                    }
                                    if(cloneachLicenceFeeDto!=null){
                                        cloneachLicenceFeeDtoList.add(cloneachLicenceFeeDto);
                                        svcFee.getMosdBundlesLicenceFeeDto().add(cloneachLicenceFeeDto);
                                        achLicenceFeeDtoList.remove(cloneachLicenceFeeDto);
                                    }
                                }
                            }
                        }
                    }
                }
                log.debug(StringUtil.changeForLog(
                        "the AppSubmisionServiceImpl linenceFeeQuaryDtos.size() is -->:" + linenceFeeQuaryDtos.size()));
            }
        }
        if(IaisCommonUtils.isNotEmpty(achLicenceFeeDtoList)){
            linenceFeeQuaryDtos.addAll(achLicenceFeeDtoList);
        }
        FeeDto entity;
        entity = configCommClient.renewFee(linenceFeeQuaryDtos).getEntity();
        log.debug(StringUtil.changeForLog("the AppSubmisionServiceImpl getGroupAmount end ...."));
        return entity;
    }

    private void checkBeds(AppGrpPremisesDto appGrpPremisesDto, AppSvcRelatedInfoDto appSvcRelatedInfoDto, LicenceFeeDto licenceFeeDto,
            String serviceCode) {
        if (!AppServicesConsts.SERVICE_CODE_COMMUNITY_HOSPITAL.equals(serviceCode)
            && !AppServicesConsts.SERVICE_CODE_ACUTE_HOSPITAL.equals(serviceCode)) {
            return;
        }
        String itemConfigId = null;
        int beds = 0;
        if (AppServicesConsts.SERVICE_CODE_COMMUNITY_HOSPITAL.equals(serviceCode)) {
            itemConfigId = "A0D77DA7-0A60-ED11-BE6E-000C29FAAE4D";
            beds = 100;
        }
        if (AppServicesConsts.SERVICE_CODE_ACUTE_HOSPITAL.equals(serviceCode)) {
            itemConfigId = "416802EF-6427-ED11-BE6D-000C29FAAE4D";
            beds = 1000;
        }
        List<AppSvcSuplmItemDto> svcSuplmItemDtos = getAppSvcSuplmItems(appSvcRelatedInfoDto.getAppSvcSuplmFormList(),
                appGrpPremisesDto.getPremisesIndexNo());
        if (IaisCommonUtils.isNotEmpty(svcSuplmItemDtos)) {
            for (AppSvcSuplmItemDto item : svcSuplmItemDtos
            ) {
                if (item.getItemConfigId().equals(itemConfigId)) {
                    int bedTotal = Integer.parseInt(item.getInputValue());
                    if (bedTotal >= beds) {
                        licenceFeeDto.setBundle(2);
                    } else {
                        licenceFeeDto.setBundle(1);
                    }
                    break;
                }
            }
        }
    }




    @Override
    public PreOrPostInspectionResultDto judgeIsPreInspection(AppSubmissionDto appSubmissionDto) {
        RecommendInspectionDto recommendInspectionDto = new RecommendInspectionDto();
        List<RiskAcceptiionDto> riskAcceptiionDtos = IaisCommonUtils.genNewArrayList();
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
            RiskAcceptiionDto riskAcceptiionDto = new RiskAcceptiionDto();
            riskAcceptiionDto.setScvCode(appSvcRelatedInfoDto.getServiceCode());
            riskAcceptiionDto.setSvcType(appSvcRelatedInfoDto.getServiceType());
            riskAcceptiionDto.setBaseServiceCodeList(appSvcRelatedInfoDto.getBaseServiceCodeList());
            riskAcceptiionDtos.add(riskAcceptiionDto);
        }

        return configCommClient.recommendIsPreInspection(recommendInspectionDto).getEntity();
    }

    @Override
    public void setRiskToDto(AppSubmissionDto appSubmissionDto) {
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        List<RiskAcceptiionDto> riskAcceptiionDtoList = IaisCommonUtils.genNewArrayList();
        for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
            RiskAcceptiionDto riskAcceptiionDto = new RiskAcceptiionDto();
            riskAcceptiionDto.setScvCode(appSvcRelatedInfoDto.getServiceCode());
            riskAcceptiionDto.setApptype(appSubmissionDto.getAppType());
            riskAcceptiionDtoList.add(riskAcceptiionDto);
        }
        List<RiskResultDto> riskResultDtoList = configCommClient.getRiskResult(riskAcceptiionDtoList).getEntity();
        for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
            String serviceCode = appSvcRelatedInfoDto.getServiceCode();
            RiskResultDto riskResultDto = getRiskResultDtoByServiceCode(riskResultDtoList, serviceCode);
            if (riskResultDto != null) {
                appSvcRelatedInfoDto.setScore(riskResultDto.getScore());
                appSvcRelatedInfoDto.setDoRiskDate(riskResultDto.getDoRiskDate());
            }
        }
    }

    @Override
    public RiskResultDto getRiskResultDtoByServiceCode(List<RiskResultDto> riskResultDtoList, String serviceCode) {
        RiskResultDto result = null;
        if (riskResultDtoList == null || StringUtil.isEmpty(serviceCode)) {
            return null;
        }
        for (RiskResultDto riskResultDto : riskResultDtoList) {
            if (serviceCode.equals(riskResultDto.getSvcCode())) {
                result = riskResultDto;
                break;
            }
        }
        return result;
    }

    @Override
    public AppSubmissionDto getAppSubmissionDtoByAppNo(String appNo) {
        return appCommService.getAppSubmissionDtoByAppNo(appNo);
    }

    @Override
    public AppSubmissionDto getAppSubmissionDto(String appNo) {
        return appCommService.getAppSubmissionDtoByAppNo(appNo);
    }

    private void informationEventBus(AppSubmissionRequestInformationDto appSubmissionRequestInformationDto, Process process) {
        //prepare request parameters
        appSubmissionRequestInformationDto.setEventRefNo(UUID.randomUUID().toString());
        eventBusHelper.submitAsyncRequest(appSubmissionRequestInformationDto,
                generateIdClient.getSeqId().getEntity(),
                EventBusConsts.SERVICE_NAME_APPSUBMIT, EventBusConsts.OPERATION_REQUEST_INFORMATION,
                appSubmissionRequestInformationDto.getEventRefNo(), "Submit Application",
                appSubmissionRequestInformationDto.getAppSubmissionDto().getAppGrpId());
    }

    @Override
    public AppSubmissionDto getAppSubmissionDtoByLicenceId(String licenceId) {
        return licCommService.getAppSubmissionDtoByLicenceId(licenceId);
    }

    @Override
    public AppSubmissionDto viewAppSubmissionDto(String licenceId) {
        return licCommService.viewAppSubmissionDto(licenceId);
    }


    @Override
    public AppSubmissionDto submitRenew(AppSubmissionDto appSubmissionDto) {
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appSubmissionDto = applicationFeClient.saveAppsForRenew(appSubmissionDto).getEntity();
        return appSubmissionDto;
    }

    @Override
    public MsgTemplateDto getMsgTemplateById(String id) {
        return systemAdminClient.getMsgTemplate(id).getEntity();
    }

    @Override
    public void feSendEmail(EmailDto emailDto) {
        feEicGatewayClient.callEicWithTrack(emailDto, feEicGatewayClient::sendEmail, "sendEmail");
    }

    @Override
    public void updateApplicationsStatus(String appGroupId, String stuts) {
        log.info(StringUtil.changeForLog("appGroupId: " + appGroupId));
        if (StringUtil.isEmpty(appGroupId)) {
            return;
        }
        List<ApplicationDto> applicationDtos = listApplicationByGroupId(appGroupId);
        for (ApplicationDto application : applicationDtos) {
            application.setStatus(stuts);
            applicationFeClient.updateApplication(application);
        }
    }

    @Override
    public void setDraftNo(AppSubmissionDto appSubmissionDto) {
        String appType = null;
        if (appSubmissionDto != null) {
            appType = appSubmissionDto.getAppType();
        }
        if (appType != null) {
            String draft = systemAdminClient.draftNumber(appType).getEntity();
            appSubmissionDto.setDraftNo(draft);
        }

    }

    @Override
    public void deleteOverdueDraft(String draftValidity) {
        applicationFeClient.deleteOverdueDraft(draftValidity);
    }

    @Override
    public AppFeeDetailsDto saveAppFeeDetails(AppFeeDetailsDto appFeeDetailsDto) {
        return applicationFeClient.saveAppFeeDetails(appFeeDetailsDto).getEntity();
    }

    @Override
    public ApplicationDto getMaxVersionApp(String appNo) {
        return applicationFeClient.getApplicationDtoByVersion(appNo).getEntity();
    }

    @Override
    public void updateMsgStatus(String msgId, String status) {
        feMessageClient.updateMsgStatus(msgId, status);
    }

    @Override
    public InterMessageDto getInterMessageById(String msgId) {
        return feMessageClient.getInterMessageById(msgId).getEntity();
    }

    @Override
    public List<AppAlignLicQueryDto> getAppAlignLicQueryDto(String licenseeId, List<String> svcNameList, List<String> premTypeList) {
        List<AppAlignLicQueryDto> appAlignLicQueryDtos = IaisCommonUtils.genNewArrayList();
        if (!StringUtil.isEmpty(licenseeId) && !IaisCommonUtils.isEmpty(svcNameList) && !IaisCommonUtils.isEmpty(premTypeList)) {
            String svcNames = JsonUtil.parseToJson(svcNameList);
            String premTypeStr = JsonUtil.parseToJson(premTypeList);
            appAlignLicQueryDtos = licenceClient.getAppAlignLicQueryDto(licenseeId, svcNames, premTypeStr).getEntity();
        }
        return appAlignLicQueryDtos;
    }

    @Override
    public List<AppAlignAppQueryDto> getAppAlignAppQueryDto(String licenseeId, List<String> svcIdList) {
        List<AppAlignAppQueryDto> appAlignLicQueryDtos = IaisCommonUtils.genNewArrayList();
        if (!StringUtil.isEmpty(licenseeId) && !IaisCommonUtils.isEmpty(svcIdList)) {
            appAlignLicQueryDtos = appCommClient.getActiveApplicationsAddress(licenseeId, svcIdList).getEntity();
        }
        return appAlignLicQueryDtos;
    }

    @Override
    public Boolean isNewLicensee(String licenseeId) {
        return licenceClient.checkIsNewLicsee(licenseeId).getEntity();
    }

    @Override
    public InterMessageDto getInterMessageBySubjectLike(String subject, String status) {
        return feMessageClient.getInterMessageBySubjectLike(subject, status).getEntity();

    }

    @Override
    public List<InterMessageDto> getInterMessageByRefNo(String refNo) {
        return feMessageClient.getInboxMsgByRefNo(refNo).getEntity();
    }

    @Override
    public AppSubmissionDto getAppSubmissionDtoByAppGrpNo(String appGrpNo) {
        return applicationFeClient.getAppSubmissionDtoByAppGrpNo(appGrpNo).getEntity();
    }

    @Override
    public void setPreviewDta(AppSubmissionDto appSubmissionDto, BaseProcessClass bpc) {
        if (appSubmissionDto != null) {
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)) {
                ParamUtil.setRequestAttr(bpc.request, "currentPreviewSvcInfo", appSvcRelatedInfoDtos.get(0));
            }
            AppEditSelectDto appEditSelectDto = ApplicationHelper.createAppEditSelectDto(true);
            boolean licenseeEdit = Optional.ofNullable(appSubmissionDto.getSubLicenseeDto())
                    .map(SubLicenseeDto::getLicenseeType)
                    .filter(licenseeType -> StringUtil.isEmpty(licenseeType)
                            || OrganizationConstants.LICENSEE_SUB_TYPE_INDIVIDUAL.equals(licenseeType))
                    .isPresent();
            appEditSelectDto.setLicenseeEdit(licenseeEdit);
            appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
        }
    }

    /**
     * Event bus call back method
     * <p>
     * EventbusCallBackDelegate#callbackMethod
     * <p>
     * EventBusConsts.OPERATION_REQUEST_RFC_RENEW_INFORMATION_SUBMIT
     *
     * @param eventRefNum
     * @param submissionId
     */
    public void updateInboxMsgStatus(String eventRefNum, String submissionId) {
        if (!StringUtil.isEmpty(eventRefNum)) {
            log.debug(StringUtil.changeForLog("--------------- releaseTimeForInsUserCallBack eventRefNum :"
                    + eventRefNum + " submissionId :" + submissionId + "--------------------"));
            EicRequestTrackingDto eicRequestTrackingDto = appEicClient.getPendingRecordByReferenceNumber(eventRefNum).getEntity();
            String jsonObj = eicRequestTrackingDto.getDtoObject();
            if (!StringUtil.isEmpty(jsonObj)) {
                AppSubmissionDto appSubmissionDto = JsonUtil.parseToObject(jsonObj, AppSubmissionDto.class);
                if (appSubmissionDto != null) {
                    String rfiMsgId = appSubmissionDto.getRfiMsgId();
                    log.debug(StringUtil.changeForLog("rfiMsgId:" + rfiMsgId));
                    feMessageClient.updateMsgStatus(rfiMsgId, MessageConstants.MESSAGE_STATUS_RESPONSE);
                }
            } else {
                log.debug(StringUtil.changeForLog("jsonObj is empty"));
            }
        }
    }

    private int getEasVehicleCount(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos) {
        int result = 0;
        if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)) {
            for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                String serviceCode = appSvcRelatedInfoDto.getServiceCode();
                if (AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE.equals(serviceCode)) {
                    List<AppSvcVehicleDto> appSvcVehicleDtos = appSvcRelatedInfoDto.getAppSvcVehicleDtoList();
                    if (!IaisCommonUtils.isEmpty(appSvcVehicleDtos)) {
                        result = result + appSvcVehicleDtos.size();
                    }
                }
            }
        }
        return result;
    }

    private int getMtsVehicleCount(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos) {
        int result = 0;
        if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)) {
            for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                String serviceCode = appSvcRelatedInfoDto.getServiceCode();
                if (AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(serviceCode)) {
                    List<AppSvcVehicleDto> appSvcVehicleDtos = appSvcRelatedInfoDto.getAppSvcVehicleDtoList();
                    if (!IaisCommonUtils.isEmpty(appSvcVehicleDtos)) {
                        result = result + appSvcVehicleDtos.size();
                    }
                }
            }
        }
        return result;
    }

    private void setEasMtsBundleInfo(LicenceFeeDto licenceFeeDto, List<AppLicBundleDto>  appLicBundleDtoList, String serviceCode,  int easMtsVehicleCount, String appType) {
        int matchingTh = configCommClient.getFeeMaxMatchingThByServiceCode(serviceCode).getEntity();
        if(IaisCommonUtils.isNotEmpty(appLicBundleDtoList)&& appType.equals(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION)){
            for (AppLicBundleDto alb : appLicBundleDtoList) {
                if (alb == null || StringUtil.isEmpty(alb.getLicenceId())) {
                    continue;
                }
                if (serviceCode.equals(AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE) && alb.getSvcCode().equals(AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE) &&StringUtil.isNotEmpty(alb.getLicenceId())) {
                    licenceFeeDto.setBundle(4);
                    break;
                }
                if (serviceCode.equals(AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE) && alb.getSvcCode().equals(AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE) &&StringUtil.isNotEmpty(alb.getLicenceId())) {
                    licenceFeeDto.setBundle(4);
                    break;
                }
            }
        }

        if(licenceFeeDto.getBundle()<=2){
            if (easMtsVehicleCount  <= matchingTh) {
                licenceFeeDto.setBundle(1);
            } else {
                licenceFeeDto.setBundle(2);
            }
        }
    }

    private List<AppSvcSuplmItemDto> getAppSvcSuplmItems(List<AppSvcSuplmFormDto> appSvcSuplmFormList, String premisesVal) {
        if (IaisCommonUtils.isEmpty(appSvcSuplmFormList)) {
            return IaisCommonUtils.genNewArrayList();
        }
        List<AppSvcSuplmItemDto> appSvcSuplmItemDtoList = appSvcSuplmFormList.stream()
                .filter(dto -> Objects.equals(premisesVal, dto.getPremisesVal()))
                .findAny()
                .map(AppSvcSuplmFormDto::getActiveAppSvcSuplmItemDtoList)
                .orElse(null);
        if (IaisCommonUtils.isEmpty(appSvcSuplmItemDtoList)) {
            return IaisCommonUtils.genNewArrayList();
        }
        return appSvcSuplmItemDtoList;
    }

    @Override
    public void updatePayment(AppSubmissionDto appSubmissionDto, String pmtRefNo) {
        if (appSubmissionDto == null) {
            return;
        }
        String appGrpId = appSubmissionDto.getAppGrpId();
        String appGrpNo = appSubmissionDto.getAppGrpNo();
        log.info(StringUtil.changeForLog("App Grp is " + appGrpNo + "[" + appGrpId + "]"));
        if (StringUtil.isEmpty(appGrpId)) {
            return;
        }
        String paymentMethod = appSubmissionDto.getPaymentMethod();
        ApplicationGroupDto appGrp = new ApplicationGroupDto();
        appGrp.setId(appGrpId);
        appGrp.setPmtRefNo(pmtRefNo);
        appGrp.setGroupNo(appGrpNo);
        appGrp.setAutoRfc(appSubmissionDto.isAutoRfc());
        Double amount = appSubmissionDto.getAmount();
        if (amount != null && !MiscUtil.doubleEquals(0.0, amount)) {
            if (ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(paymentMethod)) {
                appGrp.setPmtStatus(serviceConfigService.giroPaymentXmlUpdateByGrpNo(appSubmissionDto).getPmtStatus());
                appGrp.setPmtRefNo(appSubmissionDto.getGiroTranNo());
            } else {
                appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
            }
        } else {
            appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
        }
        appGrp.setPayMethod(paymentMethod);
        appGrp.setPaymentDt(new Date());
        saveAppGrpGiroAcct(appGrpId, appSubmissionDto.getGiroAcctNum());
        applicationFeClient.doPaymentUpDate(appGrp);
    }

    private void saveAppGrpGiroAcct(String appGrpId, String giroAccNo) {
        if (StringUtil.isEmpty(appGrpId) || StringUtil.isEmpty(giroAccNo)) {
            return;
        }
        log.info(StringUtil.changeForLog("Save Giro Acc No - " + giroAccNo));
        AppGroupMiscDto appGroupMiscDto = new AppGroupMiscDto();
        appGroupMiscDto.setAppGrpId(appGrpId);
        appGroupMiscDto.setMiscType(ApplicationConsts.APP_GROUP_MISC_TYPE_GIRO_ACCOUNT_NUMBER);
        appGroupMiscDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        appGroupMiscDto.setMiscValue(giroAccNo);
        appCommService.saveAppGrpMisc(appGroupMiscDto);
    }

    @Override
    public List<AppLicBundleDto> getBundleList(String item, boolean licOrApp) {
        if (StringUtil.isEmpty(item)) {
            return IaisCommonUtils.genNewArrayList();
        }
        List<AppLicBundleDto> result;
        if (licOrApp) {
            result = licCommService.getActiveGroupAppLicBundlesByLicId(item, true);
        } else {
            result = appCommClient.getBundleListByAppNo(item).getEntity();
        }
        return result;
    }

    @Override
    public List<ApplicationDto> getApplicationsByLicenseeId(String licenseeId) {
        if (StringUtil.isEmpty(licenseeId)){
            return IaisCommonUtils.genNewArrayList();
        }
        return appCommClient.getApplicationsByLicenseeId(licenseeId).getEntity();
    }

}