package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.organization.OrganizationConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppFeeDetailsDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesDoQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionRequestInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDisciplineAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcLaboratoryDisciplinesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationSubDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.AmendmentFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.HcsaFeeBundleItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.LicenceFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.AppAlignLicQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeIndividualDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.MenuLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RecommendInspectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
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
import com.ecquaria.cloud.moh.iais.service.client.ConfigCommClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationFeClient;
import com.ecquaria.cloud.moh.iais.service.client.ComFileRepoClient;
import com.ecquaria.cloud.moh.iais.service.client.EicClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.FeMessageClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationLienceseeClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import com.ecquaria.cloud.moh.iais.validation.ValidateCharges;
import com.ecquaria.cloud.moh.iais.validation.ValidateClincalDirector;
import com.ecquaria.cloud.moh.iais.validation.ValidateVehicle;
import com.ecquaria.cloud.submission.client.model.SubmitResp;
import com.ecquaria.sz.commons.util.MsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sop.webflow.rt.api.BaseProcessClass;
import sop.webflow.rt.api.Process;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * AppSubmisionServiceImpl
 *
 * @author suocheng
 * @date 11/6/2019
 */
@Service
@Slf4j
public class AppSubmissionServiceImpl implements AppSubmissionService {
   // String draftUrl =  RestApiUrlConsts.HCSA_APP + RestApiUrlConsts.HCSA_APP_SUBMISSION_DRAFT;
    //String submission = RestApiUrlConsts.HCSA_APP + RestApiUrlConsts.HCSA_APP_SUBMISSION;

    @Autowired
    private ApplicationFeClient applicationFeClient;
    @Autowired
    private ConfigCommClient configCommClient;
    @Autowired
    private LicenceClient licenceClient;
    @Autowired
    private EventBusHelper eventBusHelper;
    @Autowired
    private ComFileRepoClient comFileRepoClient;
    @Autowired
    private ValidateCharges validateCharges;
    @Autowired
    private ValidateVehicle validateVehicle;
    @Autowired
    private ValidateClincalDirector validateClincalDirector;
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

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

    @Value("${moh.halp.prs.enable}")
    private String prsFlag;

    @Override
    public AppSubmissionDto submit(AppSubmissionDto appSubmissionDto, Process process) {
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appSubmissionDto= applicationFeClient.saveSubmision(appSubmissionDto).getEntity();
        //asynchronous save the other data.
        eventBus(appSubmissionDto, process);
        return appSubmissionDto;
    }
    @Override
    public void sendEmailAndSMSAndMessage(AppSubmissionDto appSubmissionDto,String applicantName){
        if(appSubmissionDto.getAppType().equals(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION)){
            try{
                List<ApplicationDto> applicationDtos = appSubmissionDto.getApplicationDtos();
                if(IaisCommonUtils.isEmpty(applicationDtos)){
                    applicationDtos = appCommService.getApplicationsByGroupNo(appSubmissionDto.getAppGrpNo());
                    appSubmissionDto.setApplicationDtos(applicationDtos);
                }
                ApplicationDto applicationDto =  applicationDtos.get(0);
                String applicationType =  MasterCodeUtil.getCodeDesc(applicationDto.getApplicationType());
                int index = 0;
                StringBuilder stringBuilderAPPNum = new StringBuilder();
                String temp = "have";
                if(appSubmissionDto.getApplicationDtos().size() == 1){
                    temp = "has";
                }
                for(ApplicationDto applicationDtoApp : appSubmissionDto.getApplicationDtos()){
                    if(index == 0){
                        stringBuilderAPPNum.append(applicationDtoApp.getApplicationNo());
                    }else {
                        stringBuilderAPPNum.append(" and ");
                        stringBuilderAPPNum.append(applicationDtoApp.getApplicationNo());
                    }
                    index++;
                }
                String applicationNumber = stringBuilderAPPNum.toString();
                Map<String, Object> subMap = IaisCommonUtils.genNewHashMap();
                subMap.put("ApplicationType", applicationType);
                subMap.put("ApplicationNumber", applicationNumber);
                subMap.put("temp", temp);
                String emailSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_EN_NAP_001_EMAIL,subMap);
                String smsSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_EN_NAP_001_SMS,subMap);
                String messageSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_EN_NAP_001_MSG,subMap);
                log.debug(StringUtil.changeForLog("emailSubject : " + emailSubject));
                log.debug(StringUtil.changeForLog("smsSubject : " + smsSubject));
                log.debug(StringUtil.changeForLog("messageSubject : " + messageSubject));
                Map<String, Object> templateContent = IaisCommonUtils.genNewHashMap();
                templateContent.put("ApplicantName", applicantName);
                templateContent.put("ApplicationType",  applicationType);
                templateContent.put("ApplicationNumber", applicationNumber);
                templateContent.put("ApplicationDate", Formatter.formatDateTime(new Date()));
                templateContent.put("isSelfAssessment","No");
                String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
                templateContent.put("systemLink", loginUrl);
                String paymentMethodName = "noNeedPayment";
                String payMethod = appSubmissionDto.getPaymentMethod();
                if (ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(payMethod)) {
                    paymentMethodName = "GIRO";
                    templateContent.put("usualDeduction","next 7 working days");
                    templateContent.put("accountNumber",appSubmissionDto.getGiroAcctNum());
                    //need change giro
                }else if (ApplicationConsts.PAYMENT_METHOD_NAME_CREDIT.equals(payMethod)
                        || ApplicationConsts.PAYMENT_METHOD_NAME_NETS.equals(payMethod)
                        || ApplicationConsts.PAYMENT_METHOD_NAME_PAYNOW.equals(payMethod)) {
                    paymentMethodName = "onlinePayment";
                }
                templateContent.put("emailAddress", systemParamConfig.getSystemAddressOne());
                templateContent.put("paymentMethod", paymentMethodName);
                templateContent.put("paymentAmount", appSubmissionDto.getTotalAmountGroup() == null ?  appSubmissionDto.getAmountStr() : String.valueOf(appSubmissionDto.getTotalAmountGroup()));
                String syName = "<b>"+AppConsts.MOH_AGENCY_NAM_GROUP+"<br/>"+AppConsts.MOH_AGENCY_NAME+"</b>";
                templateContent.put("MOH_AGENCY_NAME",syName);
                EmailParam emailParam = new EmailParam();
                emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_NAP_001_EMAIL);
                emailParam.setTemplateContent(templateContent);
                emailParam.setSubject(emailSubject);
                emailParam.setQueryCode(applicationDto.getApplicationNo());
                emailParam.setReqRefNum(applicationDto.getApplicationNo());
                emailParam.setRefId(applicationDto.getApplicationNo());
                emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
                notificationHelper.sendNotification(emailParam);

                EmailParam smsParam = new EmailParam();
                smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_NAP_001_SMS);
                smsParam.setSubject(smsSubject);
                smsParam.setQueryCode(applicationDto.getApplicationNo());
                smsParam.setReqRefNum(applicationDto.getApplicationNo());
                smsParam.setRefId(applicationDto.getApplicationNo());
                smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
                notificationHelper.sendNotification(smsParam);

                EmailParam msgParam = new EmailParam();
                msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_NAP_001_MSG);
                msgParam.setTemplateContent(templateContent);
                msgParam.setSubject(messageSubject);
                msgParam.setQueryCode(applicationDto.getApplicationNo());
                msgParam.setReqRefNum(applicationDto.getApplicationNo());
                msgParam.setRefId(applicationDto.getApplicationNo());
                List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
                List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
                for(AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtoList){
                    if( !svcCodeList.contains(appSvcRelatedInfoDto.getServiceCode())){
                        svcCodeList.add(appSvcRelatedInfoDto.getServiceCode());
                    }
                }
                msgParam.setSvcCodeList(svcCodeList);
                msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
                notificationHelper.sendNotification(msgParam);
                log.info("end send email sms and msg");
            }catch (Exception e){
                log.error(e.getMessage(),e);
                log.info("send app sumbit email fail");
            }
        }

    }

    @Override
    public List<LicAppCorrelationDto> getLicDtoByLicId(String licId) {
        return licenceClient.getLicCorrBylicId(licId).getEntity();
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
        log.debug("AppSubmissionServiceImpl getGiroAccount [hciCode] hciCode is empty: {},[orgId] orgId:{}",IaisCommonUtils.isEmpty(hciCode) ,orgId);
        return licenceClient.getGiroAccountByHciCodeAndOrgId(hciCode,orgId).getEntity();
    }

    @Override
    public List<String> saveFileList(List<File> fileList) {
        return comFileRepoClient.saveFileRepo(fileList);
    }

    @Override
    public void updateDraftStatus(String draftNo, String status) {
        log.debug(StringUtil.changeForLog("The doPaymentUpDate start ..."));
        applicationFeClient.updateDraftStatus(draftNo,status);
        log.debug(StringUtil.changeForLog("updateDraftStatus end ..."));
    }

    @Override
    public List<ApplicationSubDraftDto> getDraftListBySvcCodeAndStatus(List<String> svcCodeList, String status, String licenseeId,String appType) {
        return applicationFeClient.getDraftListBySvcCodeAndStatus(svcCodeList,licenseeId,status,appType).getEntity();
    }

    @Override
    public boolean canApplyEasOrMts(String licenseeId, List<HcsaServiceDto> hcsaServiceDtos) {
        log.debug(StringUtil.changeForLog("check can create eas or mts service start ..."));
        boolean canCreateEasOrMts = false;
        if(!StringUtil.isEmpty(licenseeId) && !IaisCommonUtils.isEmpty(hcsaServiceDtos)){
            List<String> svcNames = IaisCommonUtils.genNewArrayList();
            for(HcsaServiceDto hcsaServiceDto:hcsaServiceDtos){
                svcNames.add(hcsaServiceDto.getSvcName());
            }
            AppPremisesDoQueryDto appPremisesDoQueryDto = new AppPremisesDoQueryDto();
            List<HcsaServiceDto>  HcsaServiceDtoList= configCommClient.getHcsaServiceByNames(svcNames).getEntity();
            List<String> svcIds = IaisCommonUtils.genNewArrayList();
            for(HcsaServiceDto hcsaServiceDto:HcsaServiceDtoList){
                svcIds.add(hcsaServiceDto.getId());
            }
            appPremisesDoQueryDto.setLicenseeId(licenseeId);
            appPremisesDoQueryDto.setSvcIdList(svcIds);
            List<PremisesDto> premisesDtos = licCommService.getPremisesByLicseeIdAndSvcName(licenseeId, svcNames);
            List<AppGrpPremisesEntityDto> appGrpPremisesEntityDtos = appCommService.getPendAppPremises(appPremisesDoQueryDto);
            log.debug("licence record size {}",premisesDtos.size());
            log.debug("pending application record size {}",appGrpPremisesEntityDtos.size());
            if(IaisCommonUtils.isEmpty(premisesDtos) && IaisCommonUtils.isEmpty(appGrpPremisesEntityDtos)){
                canCreateEasOrMts = true;
            }
        }
        log.debug(StringUtil.changeForLog("check can create eas or mts service end ..."));
        return canCreateEasOrMts;
    }

    @Deprecated
    @Override
    public List<AppDeclarationDocDto> getAppDeclarationDocDto(HttpServletRequest request) {
        Map<String, File> fileMap = (Map<String, File>)request.getSession().getAttribute("seesion_files_map_ajax_feselectedDeclFile");
        List<PageShowFileDto> pageShowFileDtos =new ArrayList<>(5);
        List<AppDeclarationDocDto> appDeclarationDocDtoList=new ArrayList<>(12);
        List<File> files=new ArrayList<>(5);
        if(fileMap!=null&&!fileMap.isEmpty()){
            fileMap.forEach((k,v)->{
                if(v!=null){
                    files.add(v);
                    String fileMd5 = FileUtils.getFileMd5(v);
                    PageShowFileDto pageShowFileDto =new PageShowFileDto();
                    pageShowFileDto.setFileName(v.getName());
                    String e = k.substring(k.lastIndexOf('e') + 1);
                    pageShowFileDto.setIndex(e);
                    pageShowFileDto.setFileMapId("selectedFileDiv"+e);
                    Long l = v.length() / 1024;
                    pageShowFileDto.setSize(Integer.valueOf(l.toString()));
                    pageShowFileDto.setMd5Code(fileMd5);
                    pageShowFileDtos.add(pageShowFileDto);
                    AppDeclarationDocDto appDeclarationDocDto=new AppDeclarationDocDto();
                    appDeclarationDocDto.setDocName(v.getName());
                    appDeclarationDocDto.setDocSize(Integer.valueOf(l.toString()));
                    appDeclarationDocDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    appDeclarationDocDto.setMd5Code(fileMd5);
                    appDeclarationDocDto.setVersion(Integer.valueOf(1));
                    appDeclarationDocDto.setSeqNum(Integer.valueOf(e));
                    appDeclarationDocDtoList.add(appDeclarationDocDto);
                }else {

                }
            });
        }
        List<String> list = comFileRepoClient.saveFileRepo(files);
        if(list!=null){
            ListIterator<String> iterator = list.listIterator();
            for(int j=0;j< appDeclarationDocDtoList.size();j++){
                String fileRepoId = appDeclarationDocDtoList.get(j).getFileRepoId();
                if(fileRepoId==null){
                    if(iterator.hasNext()){
                        String next = iterator.next();
                        pageShowFileDtos.get(j).setFileUploadUrl(next);
                        appDeclarationDocDtoList.get(j).setFileRepoId(next);
                        iterator.remove();
                    }
                }
            }
        }
        Collections.sort(pageShowFileDtos,(s1,s2)->s1.getFileMapId().compareTo(s2.getFileMapId()));
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
        if (OrganizationConstants.LICENSEE_SUB_TYPE_SOLO.equals(subLicenseeDto.getLicenseeType())){
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
            return map.isEmpty();
        } else {
            if (errorMap != null) {
                errorMap.putAll(map);
            }
            return map.isEmpty();
        }
    }

    @Override
    public void sendEmailForGiroFailAndSMSAndMessage( ApplicationGroupDto applicationGroupDto) {
        try{
            log.info(StringUtil.changeForLog("---------applicationGroupDto appgroupno : " + applicationGroupDto.getGroupNo() +" sendEmailForGiroFailAndSMSAndMessage start ----------"));
            AppSubmissionDto appSubmissionDto = getAppSubmissionDtoByAppNo(applicationGroupDto.getGroupNo()+"-01");
            List<ApplicationDto> applicationDtos = appSubmissionDto.getApplicationDtos();
            if(IaisCommonUtils.isEmpty(applicationDtos)){
                applicationDtos = appCommService.getApplicationsByGroupNo(appSubmissionDto.getAppGrpNo());
                appSubmissionDto.setApplicationDtos(applicationDtos);
            }
            ApplicationDto applicationDto =  applicationDtos.get(0);
            String applicationType =  MasterCodeUtil.getCodeDesc(applicationDto.getApplicationType());
            int index = 0;
            StringBuilder stringBuilderAPPNum = new StringBuilder();
            for(ApplicationDto applicationDtoApp : appSubmissionDto.getApplicationDtos()){
                if(index == 0){
                    stringBuilderAPPNum.append(applicationDtoApp.getApplicationNo());
                }else {
                    stringBuilderAPPNum.append(" and ");
                    stringBuilderAPPNum.append(applicationDtoApp.getApplicationNo());
                }
                index++;
            }
            String applicationNumber = stringBuilderAPPNum.toString();
            Map<String, Object> subMap = IaisCommonUtils.genNewHashMap();
            subMap.put("ApplicationType", applicationType);
            subMap.put("ApplicationNumber", applicationNumber);
            String emailSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_EN_FEP_006_EMAIL,subMap);
            String smsSubject = getEmailSubject(MsgTemplateConstants. MSG_TEMPLATE_EN_FEP_006_SMS ,subMap);
            String messageSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_EN_FEP_006_MSG,subMap);
            log.debug(StringUtil.changeForLog("emailSubject : " + emailSubject));
            log.debug(StringUtil.changeForLog("smsSubject : " + smsSubject));
            log.debug(StringUtil.changeForLog("messageSubject : " + messageSubject));
            Map<String, Object> templateContent = IaisCommonUtils.genNewHashMap();

            templateContent.put("ApplicantName", getApplicantName(applicationGroupDto));
            templateContent.put("ApplicationType",  applicationType);
            templateContent.put("ApplicationNumber", applicationNumber);
            templateContent.put("ApplicationDate", Formatter.formatDateTime(new Date()));
            long time = new Date().getTime() + 1000 * 60 * 60 * 24 *7L;
            templateContent.put("monthOfGiro",Formatter.formatDateTime(new Date(time),Formatter.DATE));
            templateContent.put("email", systemParamConfig.getSystemAddressOne());
            String syName = "<b>"+AppConsts.MOH_AGENCY_NAM_GROUP+"<br/>"+AppConsts.MOH_AGENCY_NAME+"</b>";
            templateContent.put("MOH_AGENCY_NAME",syName);
            EmailParam emailParam = new EmailParam();
            emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_FEP_006_EMAIL);
            emailParam.setTemplateContent(templateContent);
            emailParam.setSubject(emailSubject);
            emailParam.setQueryCode(applicationDto.getApplicationNo());
            emailParam.setReqRefNum(applicationDto.getApplicationNo());
            emailParam.setRefId(applicationDto.getApplicationNo());
            emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
            notificationHelper.sendNotification(emailParam);

            EmailParam smsParam = new EmailParam();
            smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_FEP_006_SMS);
            smsParam.setSubject(smsSubject);
            smsParam.setQueryCode(applicationDto.getApplicationNo());
            smsParam.setReqRefNum(applicationDto.getApplicationNo());
            smsParam.setRefId(applicationDto.getApplicationNo());
            smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
            notificationHelper.sendNotification(smsParam);

            EmailParam msgParam = new EmailParam();
            msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_FEP_006_MSG);
            msgParam.setTemplateContent(templateContent);
            msgParam.setSubject(messageSubject);
            msgParam.setQueryCode(applicationDto.getApplicationNo());
            msgParam.setReqRefNum(applicationDto.getApplicationNo());
            msgParam.setRefId(applicationDto.getApplicationNo());
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtoList){
                if( !svcCodeList.contains(appSvcRelatedInfoDto.getServiceCode())){
                    svcCodeList.add(appSvcRelatedInfoDto.getServiceCode());
                }
            }
            msgParam.setSvcCodeList(svcCodeList);
            msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
            notificationHelper.sendNotification(msgParam);
            log.info("end send email sms and msg");
        }catch (Exception e){
            log.error(e.getMessage(),e);
            log.info("send app sumbit email fail");
        }
    }

    private String getApplicantName(ApplicationGroupDto applicationGroupDto){
        String applicantId = applicationGroupDto.getSubmitBy();
        if(ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION.equals(applicationGroupDto.getAppType()) ||
                ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(applicationGroupDto.getAppType())) {
            String licenseeId = applicationGroupDto.getLicenseeId();
            LicenseeDto licenseeDto = licenseeService.getLicenseeDtoById(licenseeId);
            return licenseeDto.getName() == null ? "" :licenseeDto.getName();
        }else{
            OrgUserDto orgUserDto = organizationLienceseeClient.retrieveOneOrgUserAccount(applicantId).getEntity();
           return orgUserDto.getDisplayName() == null ? "" : orgUserDto.getDisplayName();
        }
    }

    private String getEmailSubject(String templateId, Map<String, Object> subMap){
        String subject = "-";
        if(!StringUtil.isEmpty(templateId)){
            MsgTemplateDto emailTemplateDto = getMsgTemplateById(templateId);
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

    @Override
    public AppSubmissionDto submitRequestInformation(AppSubmissionRequestInformationDto appSubmissionRequestInformationDto, Process process) {
        appSubmissionRequestInformationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        AppSubmissionDto appSubmissionDto = appSubmissionRequestInformationDto.getAppSubmissionDto();
        //asynchronous save the other data.
        informationEventBus(appSubmissionRequestInformationDto, process);
        return appSubmissionDto;
    }

    @Override
    public AppSubmissionDto submitRequestRfcRenewInformation(AppSubmissionRequestInformationDto appSubmissionRequestInformationDto, Process process) {
        appSubmissionRequestInformationDto.setEventRefNo(UUID.randomUUID().toString());
        eventBusHelper.submitAsyncRequest(appSubmissionRequestInformationDto,
                generateIdClient.getSeqId().getEntity(),
                EventBusConsts.SERVICE_NAME_APPSUBMIT, EventBusConsts.OPERATION_REQUEST_RFC_RENEW_INFORMATION_SUBMIT,
                appSubmissionRequestInformationDto.getEventRefNo(), "Submit RFC Renew Application",
                appSubmissionRequestInformationDto.getAppSubmissionDto().getAppGrpId());
        AppSubmissionDto appSubmissionDto = appSubmissionRequestInformationDto.getAppSubmissionDto();
        return appSubmissionDto;
    }

    @Override
    public List<ApplicationDto> listApplicationByGroupId(String groupId) {
        return applicationFeClient.listApplicationByGroupId(groupId).getEntity();
    }

    @Override
    public AppSubmissionDto doSaveDraft(AppSubmissionDto appSubmissionDto) {
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return applicationFeClient.saveDraft(appSubmissionDto).getEntity();
    }

    @Override
    public void updateDrafts(String licenseeId, List<String> licenceIds, String excludeDraftNo) {
        log.info(StringUtil.changeForLog("Licensee Id: " + licenseeId + "Licence Ids: " + licenceIds + " - excludeDraftNo: " + excludeDraftNo));
        if (StringUtil.isEmpty(licenseeId) || IaisCommonUtils.isEmpty(licenceIds) || StringUtil.isEmpty(excludeDraftNo)) {
            return;
        }
        CompletableFuture.runAsync(() ->applicationFeClient.updateDrafts(licenseeId, licenceIds, excludeDraftNo));
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
    public FeeDto getNewAppAmount(AppSubmissionDto appSubmissionDto,boolean isCharity) {
        log.debug(StringUtil.changeForLog("the getNewAppAmount start ...."));
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
        List<String> premisesTypes = IaisCommonUtils.genNewArrayList();
        List<LicenceFeeDto> licenceFeeQuaryDtos = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(appGrpPremisesDtos)){
            log.debug("appGrpPremisesDtos size {}",appGrpPremisesDtos.size());
            for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
                String premType = appGrpPremisesDto.getPremisesType();
                if(!StringUtil.isEmpty(premType)){
                    premisesTypes.add(premType);
                }
            }
            boolean hadEas = false;
            boolean hadMts = false;
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                String serviceCode = appSvcRelatedInfoDto.getServiceCode();
                if(AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE.equals(serviceCode)){
                    hadEas = true;
                }else if(AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(serviceCode)){
                    hadMts = true;
                }
            }
            int easVehicleCount = getEasVehicleCount(appSvcRelatedInfoDtos);
            int mtsVehicleCount = getMtsVehicleCount(appSvcRelatedInfoDtos);
            log.debug("eas vehicle count is {}",easVehicleCount);
            log.debug("mts vehicle count is {}",mtsVehicleCount);
            List<HcsaFeeBundleItemDto> hcsaFeeBundleItemDtos = configCommClient.getActiveBundleDtoList().getEntity();
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                LicenceFeeDto licenceFeeDto = new LicenceFeeDto();
                licenceFeeDto.setBundle(0);
                String serviceCode = appSvcRelatedInfoDto.getServiceCode();
                if(ApplicationConsts.SERVICE_TYPE_BASE.equals(appSvcRelatedInfoDto.getServiceType())){
                    licenceFeeDto.setBaseService(serviceCode);
                }else if(ApplicationConsts.SERVICE_TYPE_SPECIFIED.equals(appSvcRelatedInfoDto.getServiceType())){
                    if(!StringUtil.isEmpty(appSvcRelatedInfoDto.getBaseServiceId())){
                        HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(appSvcRelatedInfoDto.getBaseServiceId());
                        if(hcsaServiceDto != null) {
                            licenceFeeDto.setBaseService(hcsaServiceDto.getSvcCode());
                        }else{
                            log.info(StringUtil.changeForLog("current svc"+serviceCode+"'s baseSvcInfo is empty"));
                        }
                    }else{
                        log.info("base svcId is empty");
                    }
                }
                licenceFeeDto.setServiceCode(serviceCode);
                licenceFeeDto.setServiceName(appSvcRelatedInfoDto.getServiceName());
                licenceFeeDto.setPremises(premisesTypes);
//                licenceFeeDto.setOnlyNewSpecified(onlySpecifiedSvc);
                if(!StringUtil.isEmpty(appSvcRelatedInfoDto.getRelLicenceNo())){
                    licenceFeeDto.setOnlyNewSpecified(true);
                }else if(ApplicationConsts.SERVICE_TYPE_BASE.equals(appSvcRelatedInfoDto.getServiceType())){
                    licenceFeeDto.setIncludeBase(true);
                }
                licenceFeeDto.setCharity(isCharity);
                //exiting offSite licence
                log.info(StringUtil.changeForLog("svcName:"+appSvcRelatedInfoDto.getServiceName()));
                Boolean existingOnSiteLic = licenceClient.existingOnSiteOrConveLic(appSvcRelatedInfoDto.getServiceName(),appSubmissionDto.getLicenseeId()).getEntity();
                log.info(StringUtil.changeForLog("existing onSite licncence:"+existingOnSiteLic));
                licenceFeeDto.setExistOnsite(existingOnSiteLic);
                //set bundle
                if(!IaisCommonUtils.isEmpty(hcsaFeeBundleItemDtos)){
                    int matchingTh = configCommClient.getFeeMaxMatchingThByServiceCode(serviceCode).getEntity();
                    log.debug(StringUtil.changeForLog("set bundle info ..."));
                    if(AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE.equals(serviceCode)){
                        if(hadEas && hadMts){
                            //judge vehicle count
                            if(easVehicleCount+mtsVehicleCount <= matchingTh ){
                                licenceFeeDto.setBundle(1);
                            }else {
                                licenceFeeDto.setBundle(2);
                            }
                        }else{
                            setEasMtsBundleInfo(licenceFeeDto, appGrpPremisesDtos.get(0), hcsaFeeBundleItemDtos, serviceCode, appSubmissionDto.getLicenseeId(), easVehicleCount,appSubmissionDto.getAppType());
                        }
                    } else if(AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(serviceCode)){
                        if(hadEas && hadMts){
                            //new eas and mts
                            licenceFeeDto.setBundle(3);
                        }else{
                            setEasMtsBundleInfo(licenceFeeDto, appGrpPremisesDtos.get(0), hcsaFeeBundleItemDtos, serviceCode, appSubmissionDto.getLicenseeId(), mtsVehicleCount,appSubmissionDto.getAppType());
                        }
                    } else{
                        licenceFeeDto.setBundle(0);
                    }
                }else{
                    licenceFeeDto.setBundle(0);
                }
                licenceFeeQuaryDtos.add(licenceFeeDto);
            }
        }
        log.info(StringUtil.changeForLog("licenceFeeQuaryDtos:"+JsonUtil.parseToJson(licenceFeeQuaryDtos)));
        log.debug(StringUtil.changeForLog("the getNewAppAmount end ...."));
        return configCommClient.newFee(licenceFeeQuaryDtos).getEntity();
    }

    @Override
    public FeeDto getGroupAmount(AppSubmissionDto appSubmissionDto,boolean isCharity) {
        log.debug(StringUtil.changeForLog("the AppSubmisionServiceImpl getGroupAmount start ...."));
        log.info(StringUtil.changeForLog("current account is charity:"+isCharity));
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
        List<LicenceFeeDto> linenceFeeQuaryDtos = IaisCommonUtils.genNewArrayList();
        List<String> premisessTypes =  IaisCommonUtils.genNewArrayList();

        for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
            premisessTypes.add(appGrpPremisesDto.getPremisesType());
        }
        List<String> baseServiceIds = IaisCommonUtils.genNewArrayList();
        List<HcsaServiceCorrelationDto> hcsaServiceCorrelationDtos = configCommClient.serviceCorrelation().getEntity();
        boolean onlySpecifiedSvc = false;
        for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
            if(!StringUtil.isEmpty(appSvcRelatedInfoDto.getRelLicenceNo())){
                onlySpecifiedSvc = true;
            }
        }
       if(onlySpecifiedSvc && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())){
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                LicenceFeeDto licenceFeeDto = new LicenceFeeDto(); licenceFeeDto.setBundle(0);
                if(ApplicationConsts.SERVICE_TYPE_BASE.equals(appSvcRelatedInfoDto.getServiceType())){
                    licenceFeeDto.setIncludeBase(true);
                }
                HcsaServiceDto baseServiceDto = HcsaServiceCacheHelper.getServiceById(appSvcRelatedInfoDto.getBaseServiceId());
                licenceFeeDto.setBaseService(baseServiceDto.getSvcCode());
                licenceFeeDto.setServiceCode(appSvcRelatedInfoDto.getServiceCode());
                licenceFeeDto.setServiceName(appSvcRelatedInfoDto.getServiceName());
                licenceFeeDto.setPremises(premisessTypes);
                if(!StringUtil.isEmpty(appSvcRelatedInfoDto.getRelLicenceNo())){
                    licenceFeeDto.setOnlyNewSpecified(true);
                }else{
                    licenceFeeDto.setIncludeBase(true);
                }
                licenceFeeDto.setCharity(isCharity);
                linenceFeeQuaryDtos.add(licenceFeeDto);
            }
        }else{
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                if(ApplicationConsts.SERVICE_TYPE_BASE.equals(appSvcRelatedInfoDto.getServiceType())){
                    baseServiceIds.add(appSvcRelatedInfoDto.getServiceId());
                }else if(ApplicationConsts.SERVICE_TYPE_SPECIFIED.equals(appSvcRelatedInfoDto.getServiceType())){
                    for(HcsaServiceCorrelationDto hcsaServiceCorrelationDto:hcsaServiceCorrelationDtos){
                        if(appSvcRelatedInfoDto.getServiceId().equals(hcsaServiceCorrelationDto.getSpecifiedSvcId())){
                            baseServiceIds.add(hcsaServiceCorrelationDto.getBaseSvcId());
                        }
                    }
                }
            }
        }
        log.info(StringUtil.changeForLog("base service size:"+baseServiceIds.size()));
        if(!onlySpecifiedSvc) {
            for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                LicenceFeeDto licenceFeeDto = new LicenceFeeDto(); licenceFeeDto.setBundle(0);
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByCode(appSvcRelatedInfoDto.getServiceCode());
                if (hcsaServiceDto == null) {
                    log.info(StringUtil.changeForLog("hcsaServiceDto is empty "));
                    continue;
                }
                if (ApplicationConsts.SERVICE_TYPE_BASE.equals(hcsaServiceDto.getSvcType())) {
                    licenceFeeDto.setBaseService(hcsaServiceDto.getSvcCode());
                    licenceFeeDto.setIncludeBase(true);
                } else if (ApplicationConsts.SERVICE_TYPE_SPECIFIED.equals(hcsaServiceDto.getSvcType())) {
                    for (HcsaServiceCorrelationDto hcsaServiceCorrelationDto : hcsaServiceCorrelationDtos) {
                        if (hcsaServiceDto.getId().equals(hcsaServiceCorrelationDto.getSpecifiedSvcId())) {
                            String baseSvcId = hcsaServiceCorrelationDto.getBaseSvcId();
                            if (baseServiceIds.contains(baseSvcId)) {
                                log.info("can match base service ...");
                                HcsaServiceDto baseService = HcsaServiceCacheHelper.getServiceById(baseSvcId);
                                licenceFeeDto.setBaseService(baseService.getSvcCode());
                            }
                        }
                    }
                }
                licenceFeeDto.setServiceCode(hcsaServiceDto.getSvcCode());
                licenceFeeDto.setServiceName(hcsaServiceDto.getSvcName());
                licenceFeeDto.setPremises(premisessTypes);
                licenceFeeDto.setCharity(isCharity);
                if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())) {
                    String licenceId = appSubmissionDto.getLicenceId();
                    LicenceDto licenceDto = requestForChangeService.getLicenceById(licenceId);
                    Date licExpiryDate = licenceDto.getExpiryDate();
                    licenceFeeDto.setExpiryDate(licExpiryDate);
                    licenceFeeDto.setLicenceId(licenceId);
                }
                linenceFeeQuaryDtos.add(licenceFeeDto);
            }
            log.debug(StringUtil.changeForLog("the AppSubmisionServiceImpl linenceFeeQuaryDtos.size() is -->:" + linenceFeeQuaryDtos.size()));
        }
        String appTYpe = appSubmissionDto.getAppType();
        FeeDto entity ;
        if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appTYpe)){
            entity = configCommClient.renewFee(linenceFeeQuaryDtos).getEntity();
        }else{
            entity = configCommClient.newFee(linenceFeeQuaryDtos).getEntity();
        }
        log.debug(StringUtil.changeForLog("the AppSubmisionServiceImpl getGroupAmount end ...."));
        return  entity;
    }
    @Override
    public FeeDto getRenewalAmount(List<AppSubmissionDto> appSubmissionDtoList,boolean isCharity){
        List<LicenceFeeDto> linenceFeeQuaryDtos = IaisCommonUtils.genNewArrayList();
        log.debug(StringUtil.changeForLog("the AppSubmisionServiceImpl getRenewalAmount start ...."));
        log.info(StringUtil.changeForLog("current account is charity:"+isCharity));
        String hciCodeEas = null;
        String hciCodeMts = null;
        boolean hadEas = false;
        boolean hadMts = false;
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtosAll=IaisCommonUtils.genNewArrayList();
        for(AppSubmissionDto appSubmissionDto : appSubmissionDtoList){
             appSvcRelatedInfoDtosAll.addAll(appSubmissionDto.getAppSvcRelatedInfoDtoList()) ;
            List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
            String hciCode = appGrpPremisesDtos.get(0).getOldHciCode();
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSubmissionDto.getAppSvcRelatedInfoDtoList()){
                String serviceCode = appSvcRelatedInfoDto.getServiceCode();
                if(AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE.equals(serviceCode)){
                    hadEas = true;
                    hciCodeEas = hciCode;
                }else if(AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(serviceCode)){
                    hadMts = true;
                    hciCodeMts = hciCode;
                }
            }

        }
        int easVehicleCount = getEasVehicleCount(appSvcRelatedInfoDtosAll);
        int mtsVehicleCount = getMtsVehicleCount(appSvcRelatedInfoDtosAll);
        for(AppSubmissionDto appSubmissionDto : appSubmissionDtoList){
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
            List<String> premisessTypes =  IaisCommonUtils.genNewArrayList();
            if(isCharity){
                List<AppGrpPremisesDto> result = removeDuplicates(appGrpPremisesDtos);
                for(AppGrpPremisesDto appGrpPremisesDto:result){
                    premisessTypes.add(appGrpPremisesDto.getPremisesType());
                }
            }else{
                for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
                    premisessTypes.add(appGrpPremisesDto.getPremisesType());
                }
            }
            List<String> baseServiceIds = IaisCommonUtils.genNewArrayList();
            List<HcsaServiceCorrelationDto> hcsaServiceCorrelationDtos = configCommClient.serviceCorrelation().getEntity();
            boolean onlySpecifiedSvc = false;
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                if(!StringUtil.isEmpty(appSvcRelatedInfoDto.getRelLicenceNo())){
                    onlySpecifiedSvc = true;
                }
            }


            log.debug("eas vehicle count is {}",easVehicleCount);
            log.debug("mts vehicle count is {}",mtsVehicleCount);
            List<HcsaFeeBundleItemDto> hcsaFeeBundleItemDtos = configCommClient.getActiveBundleDtoList().getEntity();

            if(onlySpecifiedSvc && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())){
                for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                    LicenceFeeDto licenceFeeDto = new LicenceFeeDto(); licenceFeeDto.setBundle(0);
                    if(ApplicationConsts.SERVICE_TYPE_BASE.equals(appSvcRelatedInfoDto.getServiceType())){
                        licenceFeeDto.setIncludeBase(true);
                    }
                    licenceFeeDto.setHciCode(appGrpPremisesDtos.get(0).getOldHciCode());
                    HcsaServiceDto baseServiceDto = HcsaServiceCacheHelper.getServiceById(appSvcRelatedInfoDto.getBaseServiceId());
                    licenceFeeDto.setBaseService(baseServiceDto.getSvcCode());
                    licenceFeeDto.setServiceCode(appSvcRelatedInfoDto.getServiceCode());
                    licenceFeeDto.setServiceName(appSvcRelatedInfoDto.getServiceName());
                    licenceFeeDto.setPremises(premisessTypes);
                    if(!StringUtil.isEmpty(appSvcRelatedInfoDto.getRelLicenceNo())){
                        licenceFeeDto.setOnlyNewSpecified(true);
                    }else{
                        licenceFeeDto.setIncludeBase(true);
                    }
                    licenceFeeDto.setCharity(isCharity);
                    //set bundle
                    if(!IaisCommonUtils.isEmpty(hcsaFeeBundleItemDtos)){
                        String serviceCode=baseServiceDto.getSvcCode();
                        int matchingTh = configCommClient.getFeeMaxMatchingThByServiceCode(serviceCode).getEntity();
                        log.debug(StringUtil.changeForLog("set bundle info ..."));
                        if(AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE.equals(serviceCode)){
                            if(easVehicleCount <= matchingTh){
                                licenceFeeDto.setBundle(1);
                            }else {
                                licenceFeeDto.setBundle(2);
                            }
                            if(hadEas && hadMts){
                                if(hciCodeEas!=null&&hciCodeMts!=null){
                                    if(hciCodeEas.equals(hciCodeMts)){
                                        if(easVehicleCount+mtsVehicleCount <= matchingTh ){
                                            licenceFeeDto.setBundle(1);
                                        }else {
                                            licenceFeeDto.setBundle(2);
                                        }
                                    }
                                }
                            }else{
                                setEasMtsBundleInfo(licenceFeeDto, appGrpPremisesDtos.get(0), hcsaFeeBundleItemDtos, serviceCode, appSubmissionDto.getLicenseeId(), easVehicleCount,appSubmissionDto.getAppType());
                            }
                        } else if(AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(serviceCode)){
                            if(mtsVehicleCount <= matchingTh){
                                licenceFeeDto.setBundle(1);
                            }else {
                                licenceFeeDto.setBundle(2);
                            }
                            if(hadEas && hadMts){
                                if(hciCodeEas!=null&&hciCodeMts!=null){
                                    if(hciCodeEas.equals(hciCodeMts)){
                                        licenceFeeDto.setBundle(3);
                                    }
                                }
                            }else{
                                setEasMtsBundleInfo(licenceFeeDto, appGrpPremisesDtos.get(0), hcsaFeeBundleItemDtos, serviceCode, appSubmissionDto.getLicenseeId(), mtsVehicleCount,appSubmissionDto.getAppType());
                            }
                        } else{
                            licenceFeeDto.setBundle(0);
                        }
                    }else{
                        licenceFeeDto.setBundle(0);
                    }
                    linenceFeeQuaryDtos.add(licenceFeeDto);
                }
            }else{
                for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                    if(ApplicationConsts.SERVICE_TYPE_BASE.equals(appSvcRelatedInfoDto.getServiceType())){
                        baseServiceIds.add(appSvcRelatedInfoDto.getServiceId());
                    }else if(ApplicationConsts.SERVICE_TYPE_SPECIFIED.equals(appSvcRelatedInfoDto.getServiceType())){
                        for(HcsaServiceCorrelationDto hcsaServiceCorrelationDto:hcsaServiceCorrelationDtos){
                            if(appSvcRelatedInfoDto.getServiceId().equals(hcsaServiceCorrelationDto.getSpecifiedSvcId())){
                                baseServiceIds.add(hcsaServiceCorrelationDto.getBaseSvcId());
                            }
                        }
                    }
                }
            }
            log.info(StringUtil.changeForLog("base service size:"+baseServiceIds.size()));
            if(!onlySpecifiedSvc) {
                for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                    LicenceFeeDto licenceFeeDto = new LicenceFeeDto(); licenceFeeDto.setBundle(0);
                    licenceFeeDto.setHciCode(appGrpPremisesDtos.get(0).getOldHciCode());
                    HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByCode(appSvcRelatedInfoDto.getServiceCode());
                    if (hcsaServiceDto == null) {
                        log.info(StringUtil.changeForLog("hcsaServiceDto is empty "));
                        continue;
                    }
                    if (ApplicationConsts.SERVICE_TYPE_BASE.equals(hcsaServiceDto.getSvcType())) {
                        licenceFeeDto.setBaseService(hcsaServiceDto.getSvcCode());
                        licenceFeeDto.setIncludeBase(true);
                    } else if (ApplicationConsts.SERVICE_TYPE_SPECIFIED.equals(hcsaServiceDto.getSvcType())) {
                        for (HcsaServiceCorrelationDto hcsaServiceCorrelationDto : hcsaServiceCorrelationDtos) {
                            if (hcsaServiceDto.getId().equals(hcsaServiceCorrelationDto.getSpecifiedSvcId())) {
                                String baseSvcId = hcsaServiceCorrelationDto.getBaseSvcId();
                                if (baseServiceIds.contains(baseSvcId)) {
                                    log.info("can match base service ...");
                                    HcsaServiceDto baseService = HcsaServiceCacheHelper.getServiceById(baseSvcId);
                                    licenceFeeDto.setBaseService(baseService.getSvcCode());
                                }
                            }
                        }
                    }
                    licenceFeeDto.setServiceCode(hcsaServiceDto.getSvcCode());
                    licenceFeeDto.setServiceName(hcsaServiceDto.getSvcName());
                    licenceFeeDto.setPremises(premisessTypes);
                    licenceFeeDto.setCharity(isCharity);
                    Boolean existingOnSiteLic = licenceClient.existingOnSiteOrConveLic(appSvcRelatedInfoDto.getServiceName(),appSubmissionDto.getLicenseeId()).getEntity();
                    licenceFeeDto.setExistOnsite(existingOnSiteLic);
                    if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())) {
                        String licenceId = appSubmissionDto.getLicenceId();
                        LicenceDto licenceDto = requestForChangeService.getLicenceById(licenceId);
                        if(licenceDto != null){
                            int migrated = licenceDto.getMigrated();
                            if(0 != migrated){
                                licenceFeeDto.setOldLicenceId(licenceDto.getId());
                                licenceFeeDto.setMigrated(migrated);
                            }
                            Date licExpiryDate = licenceDto.getExpiryDate();
                            licenceFeeDto.setExpiryDate(licExpiryDate);
                        }
                        licenceFeeDto.setLicenceId(licenceId);
                    }
                    //set bundle
                    if(!IaisCommonUtils.isEmpty(hcsaFeeBundleItemDtos)){
                        String serviceCode=hcsaServiceDto.getSvcCode();
                        log.debug(StringUtil.changeForLog("set bundle info ..."));
                        int matchingTh = configCommClient.getFeeMaxMatchingThByServiceCode(serviceCode).getEntity();
                        if(AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE.equals(serviceCode)){
                            if(easVehicleCount <= matchingTh ){
                                licenceFeeDto.setBundle(1);
                            }else {
                                licenceFeeDto.setBundle(2);
                            }
                            if(hadEas && hadMts){
                                if(hciCodeEas!=null&&hciCodeMts!=null){
                                    if(hciCodeEas.equals(hciCodeMts)){
                                        if(easVehicleCount+mtsVehicleCount <= matchingTh ){
                                            licenceFeeDto.setBundle(1);
                                        }else {
                                            licenceFeeDto.setBundle(2);
                                        }
                                    }
                                }
                            }else{
                                setEasMtsBundleInfo(licenceFeeDto, appGrpPremisesDtos.get(0), hcsaFeeBundleItemDtos, serviceCode, appSubmissionDto.getLicenseeId(), easVehicleCount,appSubmissionDto.getAppType());
                            }
                        } else if(AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(serviceCode)){
                            if(mtsVehicleCount <= matchingTh ){
                                licenceFeeDto.setBundle(1);
                            }else {
                                licenceFeeDto.setBundle(2);
                            }
                            if(hadEas && hadMts){
                                if(hciCodeEas!=null&&hciCodeMts!=null){
                                    if(hciCodeEas.equals(hciCodeMts)){
                                        licenceFeeDto.setBundle(3);
                                    }
                                }
                            }else{
                                setEasMtsBundleInfo(licenceFeeDto, appGrpPremisesDtos.get(0), hcsaFeeBundleItemDtos, serviceCode, appSubmissionDto.getLicenseeId(), mtsVehicleCount,appSubmissionDto.getAppType());
                            }
                        } else{
                            licenceFeeDto.setBundle(0);
                        }
                    }else{
                        licenceFeeDto.setBundle(0);
                    }
                    linenceFeeQuaryDtos.add(licenceFeeDto);
                }
                log.debug(StringUtil.changeForLog("the AppSubmisionServiceImpl linenceFeeQuaryDtos.size() is -->:" + linenceFeeQuaryDtos.size()));
            }
        }
        FeeDto entity ;
        entity = configCommClient.renewFee(linenceFeeQuaryDtos).getEntity();
        log.debug(StringUtil.changeForLog("the AppSubmisionServiceImpl getGroupAmount end ...."));
        return  entity;
    }
    @Override
    public FeeDto getCharityRenewalAmount(List<AppSubmissionDto> appSubmissionDtoList,boolean isCharity){
        List<LicenceFeeDto> linenceFeeQuaryDtos = IaisCommonUtils.genNewArrayList();
        log.debug(StringUtil.changeForLog("the AppSubmisionServiceImpl getCharityRenewalAmount start ...."));
        log.info(StringUtil.changeForLog("current account is charity:"+isCharity));
        List<String> premises = IaisCommonUtils.genNewArrayList();
        for(AppSubmissionDto appSubmissionDto : appSubmissionDtoList){
            List<String> premisessTypes =  IaisCommonUtils.genNewArrayList();
            String licenceId1 = appSubmissionDto.getLicenceId();
            List<PremisesDto> PremisesDtoList = licenceClient.getPremisesDto(licenceId1).getEntity();
            for(PremisesDto dto : PremisesDtoList){
                boolean flag = true;
                String premisesType = dto.getPremisesType();
                if(premises.size() == 0){
                    premises.add(dto.getHciCode());
                    premisessTypes.add(premisesType);
                    flag = false;
                }else{
                    for(String premisesCode : premises){
                        if(premisesCode.equals(dto.getHciCode())){
                            flag = false;
                            break;
                        }
                    }
                }
                if(flag){
                    premisessTypes.add(premisesType);
                    premises.add(dto.getHciCode());
                }
            }
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            List<String> baseServiceIds = IaisCommonUtils.genNewArrayList();
            List<HcsaServiceCorrelationDto> hcsaServiceCorrelationDtos = configCommClient.serviceCorrelation().getEntity();
            boolean onlySpecifiedSvc = false;
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                if(!StringUtil.isEmpty(appSvcRelatedInfoDto.getRelLicenceNo())){
                    onlySpecifiedSvc = true;
                }
            }
            if(onlySpecifiedSvc && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())){
                for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                    LicenceFeeDto licenceFeeDto = new LicenceFeeDto(); licenceFeeDto.setBundle(0);
                    if(ApplicationConsts.SERVICE_TYPE_BASE.equals(appSvcRelatedInfoDto.getServiceType())){
                        licenceFeeDto.setIncludeBase(true);
                    }
                    licenceFeeDto.setHciCode(appSubmissionDto.getAppGrpPremisesDtoList().get(0).getOldHciCode());
                    HcsaServiceDto baseServiceDto = HcsaServiceCacheHelper.getServiceById(appSvcRelatedInfoDto.getBaseServiceId());
                    licenceFeeDto.setBaseService(baseServiceDto.getSvcCode());
                    licenceFeeDto.setServiceCode(appSvcRelatedInfoDto.getServiceCode());
                    licenceFeeDto.setServiceName(appSvcRelatedInfoDto.getServiceName());
                    licenceFeeDto.setPremises(premisessTypes);
                    if(!StringUtil.isEmpty(appSvcRelatedInfoDto.getRelLicenceNo())){
                        licenceFeeDto.setOnlyNewSpecified(true);
                    }else{
                        licenceFeeDto.setIncludeBase(true);
                    }
                    licenceFeeDto.setCharity(isCharity);
                    linenceFeeQuaryDtos.add(licenceFeeDto);
                }
            }else{
                for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                    if(ApplicationConsts.SERVICE_TYPE_BASE.equals(appSvcRelatedInfoDto.getServiceType())){
                        baseServiceIds.add(appSvcRelatedInfoDto.getServiceId());
                    }else if(ApplicationConsts.SERVICE_TYPE_SPECIFIED.equals(appSvcRelatedInfoDto.getServiceType())){
                        for(HcsaServiceCorrelationDto hcsaServiceCorrelationDto:hcsaServiceCorrelationDtos){
                            if(appSvcRelatedInfoDto.getServiceId().equals(hcsaServiceCorrelationDto.getSpecifiedSvcId())){
                                baseServiceIds.add(hcsaServiceCorrelationDto.getBaseSvcId());
                            }
                        }
                    }
                }
            }
            log.info(StringUtil.changeForLog("base service size:"+baseServiceIds.size()));
            if(!onlySpecifiedSvc) {
                for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                    LicenceFeeDto licenceFeeDto = new LicenceFeeDto(); licenceFeeDto.setBundle(0);
                    licenceFeeDto.setHciCode(appSubmissionDto.getAppGrpPremisesDtoList().get(0).getOldHciCode());
                    HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByCode(appSvcRelatedInfoDto.getServiceCode());
                    if (hcsaServiceDto == null) {
                        log.info(StringUtil.changeForLog("hcsaServiceDto is empty "));
                        continue;
                    }
                    if (ApplicationConsts.SERVICE_TYPE_BASE.equals(hcsaServiceDto.getSvcType())) {
                        licenceFeeDto.setBaseService(hcsaServiceDto.getSvcCode());
                        licenceFeeDto.setIncludeBase(true);
                    } else if (ApplicationConsts.SERVICE_TYPE_SPECIFIED.equals(hcsaServiceDto.getSvcType())) {
                        for (HcsaServiceCorrelationDto hcsaServiceCorrelationDto : hcsaServiceCorrelationDtos) {
                            if (hcsaServiceDto.getId().equals(hcsaServiceCorrelationDto.getSpecifiedSvcId())) {
                                String baseSvcId = hcsaServiceCorrelationDto.getBaseSvcId();
                                if (baseServiceIds.contains(baseSvcId)) {
                                    log.info("can match base service ...");
                                    HcsaServiceDto baseService = HcsaServiceCacheHelper.getServiceById(baseSvcId);
                                    licenceFeeDto.setBaseService(baseService.getSvcCode());
                                }
                            }
                        }
                    }
                    licenceFeeDto.setServiceCode(hcsaServiceDto.getSvcCode());
                    licenceFeeDto.setServiceName(hcsaServiceDto.getSvcName());
                    licenceFeeDto.setPremises(premisessTypes);
                    licenceFeeDto.setCharity(isCharity);
                    Boolean existingOnSiteLic = licenceClient.existingOnSiteOrConveLic(appSvcRelatedInfoDto.getServiceName(),appSubmissionDto.getLicenseeId()).getEntity();
                    if(premisessTypes.contains(ApplicationConsts.PREMISES_TYPE_OFF_SITE)){
                        licenceFeeDto.setExistOnsite(existingOnSiteLic);
                    }
                    if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())) {
                        String licenceId = appSubmissionDto.getLicenceId();
                        LicenceDto licenceDto = requestForChangeService.getLicenceById(licenceId);
                        if(licenceDto != null){
                            int migrated = licenceDto.getMigrated();
                            if(0 != migrated){
                                licenceFeeDto.setOldLicenceId(licenceDto.getId());
                                licenceFeeDto.setMigrated(migrated);
                            }
                            Date licExpiryDate = licenceDto.getExpiryDate();
                            licenceFeeDto.setExpiryDate(licExpiryDate);
                        }
                        licenceFeeDto.setLicenceId(licenceId);
                    }
                    linenceFeeQuaryDtos.add(licenceFeeDto);
                }
                log.debug(StringUtil.changeForLog("the AppSubmisionServiceImpl linenceFeeQuaryDtos.size() is -->:" + linenceFeeQuaryDtos.size()));
            }
        }
        FeeDto entity ;
        entity = configCommClient.renewFee(linenceFeeQuaryDtos).getEntity();
        log.debug(StringUtil.changeForLog("the AppSubmisionServiceImpl getGroupAmount end ...."));
        return  entity;
    }

    private List<AppGrpPremisesDto> removeDuplicates(List<AppGrpPremisesDto> appGrpPremisesDtoList){
        if(IaisCommonUtils.isEmpty(appGrpPremisesDtoList)){
            return null;
        }
        List<AppGrpPremisesDto> result = IaisCommonUtils.genNewArrayList();
        if(result.size() == 0){
            result.add(appGrpPremisesDtoList.get(0));
        }else{
            for(AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList){
                boolean flag = true;
                for(AppGrpPremisesDto temp : result){
                    if(temp.getOldHciCode().equals(appGrpPremisesDto.getOldHciCode())){
                        flag = false;
                        break;
                    }
                }
                if(flag){
                    result.add(appGrpPremisesDto);
                }
            }
        }
        return result;
    }

    @Override
    public PreOrPostInspectionResultDto judgeIsPreInspection(AppSubmissionDto appSubmissionDto) {
        RecommendInspectionDto recommendInspectionDto = new RecommendInspectionDto();
        List<RiskAcceptiionDto> riskAcceptiionDtos = IaisCommonUtils.genNewArrayList();
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
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
        for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
            RiskAcceptiionDto riskAcceptiionDto = new RiskAcceptiionDto();
            riskAcceptiionDto.setScvCode(appSvcRelatedInfoDto.getServiceCode());
            riskAcceptiionDto.setApptype(appSubmissionDto.getAppType());
            riskAcceptiionDtoList.add(riskAcceptiionDto);
        }
        List<RiskResultDto> riskResultDtoList = configCommClient.getRiskResult(riskAcceptiionDtoList).getEntity();
        for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
            String serviceCode = appSvcRelatedInfoDto.getServiceCode();
            RiskResultDto riskResultDto = getRiskResultDtoByServiceCode(riskResultDtoList,serviceCode);
            if(riskResultDto!= null){
                appSvcRelatedInfoDto.setScore(riskResultDto.getScore());
                appSvcRelatedInfoDto.setDoRiskDate(riskResultDto.getDoRiskDate());
            }
        }
    }

    @Override
    public AppSubmissionDto getAppSubmissionDtoByAppNo(String appNo) {
        return appCommService.getAppSubmissionDtoByAppNo(appNo);
    }

    @Override
    public AppSubmissionDto getAppSubmissionDto(String appNo) {
        return appCommService.getAppSubmissionDtoByAppNo(appNo);
    }

    private RiskResultDto getRiskResultDtoByServiceCode(List<RiskResultDto> riskResultDtoList,String serviceCode){
        RiskResultDto result = null;
        if(riskResultDtoList == null || StringUtil.isEmpty(serviceCode)){
            return null;
        }
        for(RiskResultDto riskResultDto : riskResultDtoList){
            if(serviceCode.equals(riskResultDto.getSvcCode())){
                result = riskResultDto ;
                break;
            }
        }
        return result;
    }

    private void  informationEventBus(AppSubmissionRequestInformationDto appSubmissionRequestInformationDto, Process process){
        //prepare request parameters
        appSubmissionRequestInformationDto.setEventRefNo(UUID.randomUUID().toString());
        SubmitResp submitResp = eventBusHelper.submitAsyncRequest(appSubmissionRequestInformationDto,
                generateIdClient.getSeqId().getEntity(),
                EventBusConsts.SERVICE_NAME_APPSUBMIT, EventBusConsts.OPERATION_REQUEST_INFORMATION,
                appSubmissionRequestInformationDto.getEventRefNo(), "Submit Application",
                appSubmissionRequestInformationDto.getAppSubmissionDto().getAppGrpId());
    }

    private void premisesListInformationEventBus(AppSubmissionRequestInformationDto appSubmissionRequestInformationDto,
                                                 Process process){
        appSubmissionRequestInformationDto.setEventRefNo(appSubmissionRequestInformationDto.getAppSubmissionDto().getAppGrpNo());
        SubmitResp submitResp = eventBusHelper.submitAsyncRequest(appSubmissionRequestInformationDto,
                generateIdClient.getSeqId().getEntity(),
                EventBusConsts.SERVICE_NAME_APPSUBMIT,
                EventBusConsts.OPERATION_REQUEST_INFORMATION,
                appSubmissionRequestInformationDto.getEventRefNo(), "Submit Application Premises List",
                appSubmissionRequestInformationDto.getAppSubmissionDto().getAppGrpId());
    }

    private  void eventBus(AppSubmissionDto appSubmissionDto, Process process){
        //prepare request parameters
        appSubmissionDto.setEventRefNo(appSubmissionDto.getAppGrpNo());

        SubmitResp submitResp = eventBusHelper.submitAsyncRequest(appSubmissionDto, generateIdClient.getSeqId().getEntity(),
                EventBusConsts.SERVICE_NAME_APPSUBMIT,
                EventBusConsts.OPERATION_NEW_APP_SUBMIT,
                appSubmissionDto.getEventRefNo(), "Submit Application",
                appSubmissionDto.getAppGrpId());
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
    public FeeDto getGroupAmendAmount(AmendmentFeeDto amendmentFeeDto) {
        return configCommClient.amendmentFee(amendmentFeeDto).getEntity();
    }

    @Override
    public AppSubmissionDto submitRenew(AppSubmissionDto appSubmissionDto) {
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appSubmissionDto = applicationFeClient.saveAppsForRenew(appSubmissionDto).getEntity();
        return appSubmissionDto;
    }

    @Override
    public MsgTemplateDto getMsgTemplateById(String id) {
        MsgTemplateDto msgTemplateDto = systemAdminClient.getMsgTemplate(id).getEntity();
        return msgTemplateDto;
    }

    @Autowired
    private EicClient eicClient;

    @Override
    public void feSendEmail(EmailDto emailDto) {
        feEicGatewayClient.callEicWithTrack(emailDto, feEicGatewayClient::sendEmail, "sendEmail");
    }

    @Override
    public void updateApplicationsStatus(String appGroupId, String stuts) {
        List<ApplicationDto> applicationDtos = listApplicationByGroupId(appGroupId);
        for(ApplicationDto application : applicationDtos){
            application.setStatus(stuts);
            applicationFeClient.updateApplication(application);
        }
    }

    @Override
    public void setDraftNo(AppSubmissionDto appSubmissionDto) {
        String appType=null;
        if(appSubmissionDto!=null){
             appType = appSubmissionDto.getAppType();
        }
        if(appType!=null){
            String draft = systemAdminClient.draftNumber(appType).getEntity();
            appSubmissionDto.setDraftNo(draft);
        }

    }

    @Override
    public void deleteOverdueDraft(String draftValidity) {
        applicationFeClient.deleteOverdueDraft(draftValidity);
    }

    @Override
    public List<AppGrpPremisesDto> getAppGrpPremisesDto(String appNo) {
        List<AppGrpPremisesDto> entity = applicationFeClient.getAppGrpPremisesDtoByAppGroId(appNo).getEntity();
        return entity;
    }

    @Override
    public AppFeeDetailsDto saveAppFeeDetails(AppFeeDetailsDto appFeeDetailsDto) {
        return   applicationFeClient.saveAppFeeDetails(appFeeDetailsDto).getEntity();
    }

    @Override
    public ApplicationDto getMaxVersionApp(String appNo) {
        return applicationFeClient.getApplicationDtoByVersion(appNo).getEntity();
    }

    @Override
    public void updateMsgStatus(String msgId, String status) {
        feMessageClient.updateMsgStatus(msgId,status);
    }

    @Override
    public InterMessageDto getInterMessageById(String msgId) {
        return feMessageClient.getInterMessageById(msgId).getEntity();
    }

    @Override
    public List<AppAlignLicQueryDto> getAppAlignLicQueryDto(String licenseeId, List<String> svcNameList,List<String> premTypeList) {
        List<AppAlignLicQueryDto> appAlignLicQueryDtos = IaisCommonUtils.genNewArrayList();
        if(!StringUtil.isEmpty(licenseeId) && !IaisCommonUtils.isEmpty(svcNameList) && !IaisCommonUtils.isEmpty(premTypeList)) {
            String svcNames = JsonUtil.parseToJson(svcNameList);
            String premTypeStr = JsonUtil.parseToJson(premTypeList);
            appAlignLicQueryDtos = licenceClient.getAppAlignLicQueryDto(licenseeId,svcNames,premTypeStr).getEntity();
        }
        return appAlignLicQueryDtos;
    }

    @Override
    public Boolean isNewLicensee(String licenseeId) {
        return licenceClient.checkIsNewLicsee(licenseeId).getEntity();
    }

    @Override
    public InterMessageDto getInterMessageBySubjectLike(String subject, String status) {
        return feMessageClient.getInterMessageBySubjectLike(subject,status).getEntity();

    }

    @Override
    public AppSubmissionDto getAppSubmissionDtoByAppGrpNo(String appGrpNo) {
        return applicationFeClient.getAppSubmissionDtoByAppGrpNo(appGrpNo).getEntity();
    }

    @Override
    public void setPreviewDta(AppSubmissionDto appSubmissionDto, BaseProcessClass bpc) throws CloneNotSupportedException {
        if (appSubmissionDto != null) {
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)) {
                String svcId = (String) ParamUtil.getSessionAttr(bpc.request, "SvcId");
                ParamUtil.setRequestAttr(bpc.request, "currentPreviewSvcInfo", appSvcRelatedInfoDtos.get(0));
                Map<String, List<AppSvcDisciplineAllocationDto>> reloadDisciplineAllocationMap =
                        ApplicationHelper.getDisciplineAllocationDtoList(appSubmissionDto, svcId);
                ParamUtil.setRequestAttr(bpc.request, "reloadDisciplineAllocationMap", (Serializable) reloadDisciplineAllocationMap);
                //PO/DPO
                if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)) {
                    ApplicationHelper.setPreviewPo(appSvcRelatedInfoDtos.get(0), bpc.request);
                }
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



    private AppSvcRelatedInfoDto getAppSvcRelatedInfoDto(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos){
        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
            return appSvcRelatedInfoDtos.get(0);
        }
        return new AppSvcRelatedInfoDto();
    }

    private boolean compareHciName(List<AppGrpPremisesDto> appGrpPremisesDtos,List<AppGrpPremisesDto> oldAppGrpPremisesDtos){
        int length = appGrpPremisesDtos.size();
        int oldLength = oldAppGrpPremisesDtos.size();
        if(length == oldLength){
            for(int i=0; i<length; i++){
                AppGrpPremisesDto appGrpPremisesDto = appGrpPremisesDtos.get(0);
                AppGrpPremisesDto oldAppGrpPremisesDto = oldAppGrpPremisesDtos.get(0);
                if(!getHciName(appGrpPremisesDto).equals(getHciName(oldAppGrpPremisesDto))){
                    return false;
                }
            }
        }
        //is same
        return true;
    }
    private String getHciName(AppGrpPremisesDto appGrpPremisesDto){
        String hciName = "";
        if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(appGrpPremisesDto.getPremisesType())){
            hciName = appGrpPremisesDto.getHciName();
        }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())){
            hciName = appGrpPremisesDto.getConveyanceVehicleNo();
        }
        return hciName;
    }

    private AppSvcLaboratoryDisciplinesDto getDisciplinesDto(List<AppSvcLaboratoryDisciplinesDto> disciplinesDto,String hciName){
        for(AppSvcLaboratoryDisciplinesDto iten:disciplinesDto){
            if(hciName.equals(iten.getPremiseVal())){
                return iten;
            }
        }
        return new AppSvcLaboratoryDisciplinesDto();
    }
    private boolean compareLocation(List<AppGrpPremisesDto> appGrpPremisesDtos,List<AppGrpPremisesDto> oldAppGrpPremisesDtos){
        int length = appGrpPremisesDtos.size();
        int oldLength = oldAppGrpPremisesDtos.size();
        if(length == oldLength){
            for(int i=0; i<length; i++){
                AppGrpPremisesDto appGrpPremisesDto = appGrpPremisesDtos.get(0);
                AppGrpPremisesDto oldAppGrpPremisesDto = oldAppGrpPremisesDtos.get(0);
                if(!appGrpPremisesDto.getAddress().equals(oldAppGrpPremisesDto.getAddress())){
                    return false;
                }
            }
        }
        //is same
        return true;
    }



    private void turnId(List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos, Map<String, HcsaSvcSubtypeOrSubsumedDto> allCheckListMap) {

        for (HcsaSvcSubtypeOrSubsumedDto dto : hcsaSvcSubtypeOrSubsumedDtos) {
            allCheckListMap.put(dto.getId(), dto);
            if (dto.getList() != null && dto.getList().size() > 0) {
                turnId(dto.getList(), allCheckListMap);
            }
        }
    }

    private void recursingChooseLabUpward(Map<String, HcsaSvcSubtypeOrSubsumedDto> map,String targetSvcScopeId,List<String> svcScopeIdList,List<AppSvcChckListDto> newSvcScopeList){
        HcsaSvcSubtypeOrSubsumedDto hcsaSvcSubtypeOrSubsumedDto = map.get(targetSvcScopeId);
        if(hcsaSvcSubtypeOrSubsumedDto != null){
            String id = hcsaSvcSubtypeOrSubsumedDto.getId();
            if(!svcScopeIdList.contains(id)){
                //check this parent checkbox
                AppSvcChckListDto appSvcChckListDto = new AppSvcChckListDto();
                appSvcChckListDto.setChkLstConfId(id);
                appSvcChckListDto.setChkLstType(hcsaSvcSubtypeOrSubsumedDto.getType());
                appSvcChckListDto.setChkName(hcsaSvcSubtypeOrSubsumedDto.getName());
                appSvcChckListDto.setParentName(hcsaSvcSubtypeOrSubsumedDto.getParentId());
                appSvcChckListDto.setChildrenName(hcsaSvcSubtypeOrSubsumedDto.getChildrenId());
                newSvcScopeList.add(appSvcChckListDto);
                svcScopeIdList.add(id);
            }
            String parentId  = hcsaSvcSubtypeOrSubsumedDto.getParentId();
            if(!StringUtil.isEmpty(parentId)){
                if(!svcScopeIdList.contains(parentId)){
                    //turn
                    recursingChooseLabUpward(map,parentId,svcScopeIdList,newSvcScopeList);
                }
            }
        }

    }

    private boolean getBundleLicenceByHciCode(String hciCode, String licenseeId, List<String> svcNameList) {
        if( StringUtil.isEmpty(licenseeId) || IaisCommonUtils.isEmpty(svcNameList)){
            return false;
        }
        if(StringUtil.isEmpty(hciCode)){
            hciCode="###";
        }
        return licenceClient.getBundleLicence(hciCode,licenseeId,svcNameList).getEntity();
    }

    private List<HcsaFeeBundleItemDto> getBundleDtoBySvcCode(List<HcsaFeeBundleItemDto> hcsaFeeBundleItemDtos, String svcCode){
        List<HcsaFeeBundleItemDto> result = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(hcsaFeeBundleItemDtos) && !StringUtil.isEmpty(svcCode)){
            //get target bundleId
            String bundleId = null;
            for(HcsaFeeBundleItemDto hcsaFeeBundleItemDto:hcsaFeeBundleItemDtos){
                if(svcCode.equals(hcsaFeeBundleItemDto.getSvcCode())){
                    bundleId = hcsaFeeBundleItemDto.getBundleId();
                    break;
                }
            }
            if (bundleId != null) {
                for(HcsaFeeBundleItemDto hcsaFeeBundleItemDto:hcsaFeeBundleItemDtos){
                    if(bundleId.equals(hcsaFeeBundleItemDto.getBundleId()) && !svcCode.equals(hcsaFeeBundleItemDto.getSvcCode())){
                        result.add(hcsaFeeBundleItemDto);
                    }
                }
            }

        }
        return result;
    }

    private  int getEasVehicleCount(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos){
        int result = 0;
        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                String serviceCode = appSvcRelatedInfoDto.getServiceCode();
                if(AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE.equals(serviceCode) ){
                    List<AppSvcVehicleDto> appSvcVehicleDtos = appSvcRelatedInfoDto.getAppSvcVehicleDtoList();
                    if(!IaisCommonUtils.isEmpty(appSvcVehicleDtos)){
                        result = result + appSvcVehicleDtos.size();
                    }
                }
            }
        }
        return result;
    }
    private  int getMtsVehicleCount(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos){
        int result = 0;
        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                String serviceCode = appSvcRelatedInfoDto.getServiceCode();
                if(AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(serviceCode)){
                    List<AppSvcVehicleDto> appSvcVehicleDtos = appSvcRelatedInfoDto.getAppSvcVehicleDtoList();
                    if(!IaisCommonUtils.isEmpty(appSvcVehicleDtos)){
                        result = result + appSvcVehicleDtos.size();
                    }
                }
            }
        }
        return result;
    }

    private void setEasMtsBundleInfo(LicenceFeeDto licenceFeeDto, AppGrpPremisesDto appGrpPremisesDto, List<HcsaFeeBundleItemDto> hcsaFeeBundleItemDtos, String serviceCode, String licenseeId, int easMtsVehicleCount,String appType){
        List<HcsaFeeBundleItemDto> bundleDtos = getBundleDtoBySvcCode(hcsaFeeBundleItemDtos,serviceCode);
        boolean hadBundleLicence = false;
        boolean hadBundleSvc = false;
        int matchingTh = configCommClient.getFeeMaxMatchingThByServiceCode(serviceCode).getEntity();
        if(!IaisCommonUtils.isEmpty(bundleDtos)){
            log.debug(StringUtil.changeForLog("get bundle licence ..."));
            log.debug("hciCode is {}",appGrpPremisesDto.getOldHciCode());
            log.debug("licenseeId is {}",licenseeId);
            List<String> bundleSvcNameList = IaisCommonUtils.genNewArrayList();
            for(HcsaFeeBundleItemDto hcsaFeeBundleItemDto:bundleDtos){
                bundleSvcNameList.add(HcsaServiceCacheHelper.getServiceByCode(hcsaFeeBundleItemDto.getSvcCode()).getSvcName());
            }
            hadBundleLicence = getBundleLicenceByHciCode(appGrpPremisesDto.getOldHciCode(), licenseeId, bundleSvcNameList);
            if(IaisCommonUtils.isNotEmpty(appGrpPremisesDto.getRelatedServices())){
                for (String relatedSvc:appGrpPremisesDto.getRelatedServices()
                ) {
                    if(bundleSvcNameList.contains(relatedSvc)){
                        hadBundleSvc=true;
                    }
                }
            }

        }
        log.debug("hadBundleLicence is {}",hadBundleLicence);
        log.debug("hadBundleSvc is {}",hadBundleSvc);
        if((hadBundleLicence||hadBundleSvc)&&appType.equals(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION)){
            //found bundle licence
            licenceFeeDto.setBundle(4);
        }else{
            //judge vehicle count
            if(easMtsVehicleCount <= matchingTh ){
                licenceFeeDto.setBundle(1);
            }else {
                licenceFeeDto.setBundle(2);
            }
        }
    }

}