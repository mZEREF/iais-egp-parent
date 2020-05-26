package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.client.EicClient;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGroupMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionRequestInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcLaboratoryDisciplinesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.RenewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.AmendmentFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.LicenceFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.HcsaLicenceGroupFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RecommendInspectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.client.AppConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import com.ecquaria.cloud.submission.client.model.SubmitResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sop.webflow.rt.api.Process;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * AppSubmisionServiceImpl
 *
 * @author suocheng
 * @date 11/6/2019
 */
@Service
@Slf4j
public class AppSubmissionServiceImpl implements AppSubmissionService {
    String draftUrl =  RestApiUrlConsts.HCSA_APP + RestApiUrlConsts.HCSA_APP_SUBMISSION_DRAFT;
    String submission = RestApiUrlConsts.HCSA_APP + RestApiUrlConsts.HCSA_APP_SUBMISSION;

    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private AppConfigClient appConfigClient;
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    private LicenceClient licenceClient;
    @Autowired
    private EventBusHelper eventBusHelper;
    @Autowired
    private HcsaLicenClient hcsaLicenClient;

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Autowired
    private SystemAdminClient systemAdminClient;
    @Autowired
    private GenerateIdClient generateIdClient;
    @Autowired
    private FeEicGatewayClient feEicGatewayClient;


    @Override
    public AppSubmissionDto submit(AppSubmissionDto appSubmissionDto, Process process) {
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appSubmissionDto= applicationClient.saveSubmision(appSubmissionDto).getEntity();
        //asynchronous save the other data.
        eventBus(appSubmissionDto, process);
        return appSubmissionDto;
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
    public AppSubmissionDto submitPremisesListRequestInformation(AppSubmissionRequestInformationDto appSubmissionRequestInformationDto, Process process) {
        appSubmissionRequestInformationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        AppSubmissionDto appSubmissionDto = appSubmissionRequestInformationDto.getAppSubmissionDto();
        //asynchronous save the other data.
        premisesListInformationEventBus(appSubmissionRequestInformationDto, process);
        return appSubmissionDto;
    }

    @Override
    public List<ApplicationDto> listApplicationByGroupId(String groupId) {
        return applicationClient.listApplicationByGroupId(groupId).getEntity();
    }

    @Override
    public AppSubmissionDto doSaveDraft(AppSubmissionDto appSubmissionDto) {
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return applicationClient.saveDraft(appSubmissionDto).getEntity();
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
    public FeeDto getGroupAmount(AppSubmissionDto appSubmissionDto) {
        log.debug(StringUtil.changeForLog("the AppSubmisionServiceImpl getGroupAmount start ...."));
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
        List<LicenceFeeDto> linenceFeeQuaryDtos = new ArrayList();
        List<String> premisessTypes =  new ArrayList();

        for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
            premisessTypes.add(appGrpPremisesDto.getPremisesType());
        }
        List<String> baseServiceIds = IaisCommonUtils.genNewArrayList();
        List<HcsaServiceCorrelationDto> hcsaServiceCorrelationDtos = appConfigClient.serviceCorrelation().getEntity();

        if(appSubmissionDto.isOnlySpecifiedSvc() && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())){
            baseServiceIds = appSubmissionDto.getExistBaseServiceList();
            for(String baseSvcId:baseServiceIds){
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(baseSvcId);
                LicenceFeeDto licenceFeeDto = new LicenceFeeDto();
                licenceFeeDto.setBaseService(hcsaServiceDto.getSvcCode());
                licenceFeeDto.setIncludeBase(false);
                licenceFeeDto.setServiceCode(hcsaServiceDto.getSvcCode());
                licenceFeeDto.setServiceName(hcsaServiceDto.getSvcName());
                licenceFeeDto.setPremises(premisessTypes);
                linenceFeeQuaryDtos.add(licenceFeeDto);
            }
        }else{
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                if(ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(appSvcRelatedInfoDto.getServiceType())){
                    baseServiceIds.add(appSvcRelatedInfoDto.getServiceId());
                }else if(ApplicationConsts.SERVICE_CONFIG_TYPE_SUBSUMED.equals(appSvcRelatedInfoDto.getServiceType())){
                    for(HcsaServiceCorrelationDto hcsaServiceCorrelationDto:hcsaServiceCorrelationDtos){
                        if(appSvcRelatedInfoDto.getServiceId().equals(hcsaServiceCorrelationDto.getSpecifiedSvcId())){
                            baseServiceIds.add(hcsaServiceCorrelationDto.getBaseSvcId());
                        }
                    }
                }
            }
        }
        log.info("base service size:"+baseServiceIds.size());

        for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
            LicenceFeeDto licenceFeeDto = new LicenceFeeDto();
            HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByCode(appSvcRelatedInfoDto.getServiceCode());
            if(hcsaServiceDto == null){
                log.info(StringUtil.changeForLog("hcsaServiceDto is empty "));
                continue;
            }
            if(ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(hcsaServiceDto.getSvcType())){
                licenceFeeDto.setBaseService(hcsaServiceDto.getSvcCode());
                licenceFeeDto.setIncludeBase(true);
            }else if(ApplicationConsts.SERVICE_CONFIG_TYPE_SUBSUMED.equals(hcsaServiceDto.getSvcType())){
                for(HcsaServiceCorrelationDto hcsaServiceCorrelationDto:hcsaServiceCorrelationDtos){
                    if(hcsaServiceDto.getId().equals(hcsaServiceCorrelationDto.getSpecifiedSvcId())){
                        String baseSvcId = hcsaServiceCorrelationDto.getBaseSvcId();
                        if(baseServiceIds.contains(baseSvcId)){
                            log.info("can match base service ...");
                            HcsaServiceDto baseService = HcsaServiceCacheHelper.getServiceById(baseSvcId);
                            licenceFeeDto.setBaseService(baseService.getSvcCode());
                        }
                    }
                }
                if(appSubmissionDto.isOnlySpecifiedSvc()){
                    licenceFeeDto.setIncludeBase(false);
                }else{
                    licenceFeeDto.setIncludeBase(true);
                }
            }
            licenceFeeDto.setServiceCode(hcsaServiceDto.getSvcCode());
            licenceFeeDto.setServiceName(hcsaServiceDto.getSvcName());
            licenceFeeDto.setPremises(premisessTypes);

            if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())){
                licenceFeeDto.setRenewCount(1);
                String licenceId = appSubmissionDto.getLicenceId();
                List<String> licenceIds = IaisCommonUtils.genNewArrayList();
                licenceIds.add(licenceId);
                List<HcsaLicenceGroupFeeDto> hcsaLicenceGroupFeeDtos =  hcsaLicenClient.retrieveHcsaLicenceGroupFee(licenceIds).getEntity();
                if(!IaisCommonUtils.isEmpty(hcsaLicenceGroupFeeDtos)){
                    HcsaLicenceGroupFeeDto hcsaLicenceGroupFeeDto =  hcsaLicenceGroupFeeDtos.get(0);
                    licenceFeeDto.setGroupId(hcsaLicenceGroupFeeDto.getGroupId());
                    licenceFeeDto.setMigrated(hcsaLicenceGroupFeeDto.isMigrated());
                    licenceFeeDto.setOldAmount(hcsaLicenceGroupFeeDto.getAmount());
                    licenceFeeDto.setExpiryDate(hcsaLicenceGroupFeeDto.getExpiryDate());
                    if(hcsaLicenceGroupFeeDto.isMigrated()){
                        licenceFeeDto.setRenewCount(hcsaLicenceGroupFeeDto.getCount());
                    }else{
                        licenceFeeDto.setRenewCount(0);
                    }
                }
                // licenceFeeDto.setCharity();
            }
            linenceFeeQuaryDtos.add(licenceFeeDto);
        }
        log.debug(StringUtil.changeForLog("the AppSubmisionServiceImpl linenceFeeQuaryDtos.size() is -->:"+linenceFeeQuaryDtos.size()));
        String appTYpe = appSubmissionDto.getAppType();
        FeeDto entity ;
        if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appTYpe)){
            entity = appConfigClient.renewFee(linenceFeeQuaryDtos).getEntity();
        }else{
            entity = appConfigClient.newFee(linenceFeeQuaryDtos).getEntity();
        }
        //Double amount = entity.getTotal();
        //log.debug(StringUtil.changeForLog("the AppSubmisionServiceImpl amount is -->:"+amount));
        log.debug(StringUtil.changeForLog("the AppSubmisionServiceImpl getGroupAmount end ...."));
        return  entity;
    }

    @Override
    public PreOrPostInspectionResultDto judgeIsPreInspection(AppSubmissionDto appSubmissionDto) {
        RecommendInspectionDto recommendInspectionDto = new RecommendInspectionDto();
        List<RiskAcceptiionDto> riskAcceptiionDtos = new ArrayList();
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
            RiskAcceptiionDto riskAcceptiionDto = new RiskAcceptiionDto();
            riskAcceptiionDto.setScvCode(appSvcRelatedInfoDto.getServiceCode());
            riskAcceptiionDto.setSvcType(appSvcRelatedInfoDto.getServiceType());
            riskAcceptiionDto.setBaseServiceCodeList(appSvcRelatedInfoDto.getBaseServiceCodeList());
            riskAcceptiionDtos.add(riskAcceptiionDto);
        }

        return appConfigClient.recommendIsPreInspection(recommendInspectionDto).getEntity();
    }

    @Override
    public void setRiskToDto(AppSubmissionDto appSubmissionDto) {
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        List<RiskAcceptiionDto> riskAcceptiionDtoList = new ArrayList();
        for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
            RiskAcceptiionDto riskAcceptiionDto = new RiskAcceptiionDto();
            riskAcceptiionDto.setScvCode(appSvcRelatedInfoDto.getServiceCode());
            riskAcceptiionDto.setApptype(appSubmissionDto.getAppType());
            riskAcceptiionDtoList.add(riskAcceptiionDto);
        }
        List<RiskResultDto> riskResultDtoList = appConfigClient.getRiskResult(riskAcceptiionDtoList).getEntity();
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
        return applicationClient.getAppSubmissionDtoByAppNo(appNo).getEntity();
    }

    @Override
    public AppSubmissionDto getAppSubmissionDto(String appNo) {
        return applicationClient.getAppSubmissionDto(appNo).getEntity();
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
        appSubmissionRequestInformationDto.setEventRefNo(appSubmissionRequestInformationDto.getAppSubmissionDto().getAppGrpNo());
        SubmitResp submitResp = eventBusHelper.submitAsyncRequest(appSubmissionRequestInformationDto,
                generateIdClient.getSeqId().getEntity(),
                EventBusConsts.SERVICE_NAME_APPSUBMIT, EventBusConsts.OPERATION_REQUEST_INFORMATION,
                appSubmissionRequestInformationDto.getEventRefNo(), process);
    }

    private void premisesListInformationEventBus(AppSubmissionRequestInformationDto appSubmissionRequestInformationDto,
                                                 Process process){
        appSubmissionRequestInformationDto.setEventRefNo(appSubmissionRequestInformationDto.getAppSubmissionDto().getAppGrpNo());
        SubmitResp submitResp = eventBusHelper.submitAsyncRequest(appSubmissionRequestInformationDto,
                generateIdClient.getSeqId().getEntity(),
                EventBusConsts.SERVICE_NAME_APPSUBMIT,
                EventBusConsts.OPERATION_REQUEST_INFORMATION,
                appSubmissionRequestInformationDto.getEventRefNo(), process);
    }

    private  void eventBus(AppSubmissionDto appSubmissionDto, Process process){
        //prepare request parameters
        appSubmissionDto.setEventRefNo(appSubmissionDto.getAppGrpNo());

        SubmitResp submitResp = eventBusHelper.submitAsyncRequest(appSubmissionDto, generateIdClient.getSeqId().getEntity(),
                EventBusConsts.SERVICE_NAME_APPSUBMIT,
                EventBusConsts.OPERATION_NEW_APP_SUBMIT,
                appSubmissionDto.getEventRefNo(), process);
    }

    @Override
    public AppSubmissionDto getAppSubmissionDtoByLicenceId(String licenceId) {
        return licenceClient.getAppSubmissionDto(licenceId).getEntity();
    }

    @Override
    public FeeDto getGroupAmendAmount(AmendmentFeeDto amendmentFeeDto) {
        return appConfigClient.amendmentFee(amendmentFeeDto).getEntity();
    }

    @Override
    public AppSubmissionDto submitRequestChange(AppSubmissionDto appSubmissionDto, Process process) {
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appSubmissionDto = applicationClient.saveAppsForRequestForChange(appSubmissionDto).getEntity();
        //eventBus(appSubmissionDto, process);
        return appSubmissionDto;
    }

    @Override
    public AppSubmissionDto submitRenew(AppSubmissionDto appSubmissionDto) {
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appSubmissionDto = applicationClient.saveAppsForRenew(appSubmissionDto).getEntity();
        return appSubmissionDto;
    }

    @Override
    public MsgTemplateDto getMsgTemplateById(String id) {
        MsgTemplateDto msgTemplateDto = systemAdminClient.getMsgTemplate(id).getEntity();
        return msgTemplateDto;
    }

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;
    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Autowired
    private EicClient eicClient;

    @Value("${spring.application.name}")
    private String currentApp;
    @Value("${iais.current.domain}")
    private String currentDomain;

    @Override
    public void feSendEmail(EmailDto emailDto) {
        String moduleName = currentApp + "-" + currentDomain;
        String refNo = String.valueOf(System.currentTimeMillis());
        EicRequestTrackingDto dto = new EicRequestTrackingDto();
        dto.setStatus(AppConsts.EIC_STATUS_PENDING_PROCESSING);
        dto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        dto.setActionClsName(this.getClass().getName());
        dto.setActionMethod("callEicSendEmail");
        dto.setDtoClsName(emailDto.getClass().getName());
        dto.setDtoObject(JsonUtil.parseToJson(emailDto));
        dto.setRefNo(refNo);
        dto.setModuleName(moduleName);
        eicClient.saveEicTrack(dto);
        callEicSendEmail(emailDto);
        dto = eicClient.getPendingRecordByReferenceNumber(refNo).getEntity();
        Date now = new Date();
        dto.setProcessNum(1);
        dto.setFirstActionAt(now);
        dto.setLastActionAt(now);
        dto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
        List<EicRequestTrackingDto> list = IaisCommonUtils.genNewArrayList(1);
        list.add(dto);
        eicClient.updateStatus(list);
    }

    public void callEicSendEmail(EmailDto emailDto){
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        feEicGatewayClient.feSendEmail(emailDto,signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());
    }

    @Override
    public ApplicationGroupDto createApplicationDataByWithOutRenewal(RenewDto renewDto) {
        return applicationClient.createApplicationDataByWithOutRenewal(renewDto).getEntity();
    }

    @Override
    public void updateApplicationsStatus(String appGroupId, String stuts) {
        List<ApplicationDto> applicationDtos = listApplicationByGroupId(appGroupId);
        for(ApplicationDto application : applicationDtos){
            application.setStatus(stuts);
            applicationClient.updateApplication(application);
        }
    }

    @Override
    public boolean checkRenewalStatus(String licenceId) {
        boolean flag = true;
        List<ApplicationDto> apps = applicationClient.getAppByLicIdAndExcludeNew(licenceId).getEntity();
        for(ApplicationDto app : apps){
            if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(app.getApplicationType())
                    && !(ApplicationConsts.APPLICATION_STATUS_APPROVED.equals(app.getStatus()))){
                flag = false;
            }
        }
        return flag;
    }

    @Override
    public AppSubmissionDto getExistBaseSvcInfo(List<String> licenceIds) {
        return licenceClient.getExistBaseSvcInfo(licenceIds).getEntity();
    }

    @Override
    public void transform(AppSubmissionDto appSubmissionDto, String licenseeId) {
        Double amount = 0.0;
        AuditTrailDto internet = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTERNET);
        String grpNo = systemAdminClient.applicationNumber(ApplicationConsts.APPLICATION_TYPE_CESSATION).getEntity();
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        for(AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtoList){
            String serviceName = appSvcRelatedInfoDto.getServiceName();
            HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(serviceName);
            String svcId = hcsaServiceDto.getId();
            String svcCode = hcsaServiceDto.getSvcCode();
            appSvcRelatedInfoDto.setServiceId(svcId);
            appSvcRelatedInfoDto.setServiceCode(svcCode);
        }

        appSubmissionDto.setAppGrpNo(grpNo);
        appSubmissionDto.setFromBe(false);
        appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
        appSubmissionDto.setAmount(amount);
        appSubmissionDto.setAuditTrailDto(internet);
        appSubmissionDto.setPreInspection(true);
        appSubmissionDto.setRequirement(true);
        appSubmissionDto.setLicenseeId(licenseeId);
        appSubmissionDto.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
        appSubmissionDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_SUBMITED);
        appSubmissionDto.setAppGrpId(null);
        setRiskToDto(appSubmissionDto);
    }

    @Override
    public void saveAppsubmission(AppSubmissionDto appSubmissionDto) {
        AppSubmissionDto entity = applicationClient.saveSubmision(appSubmissionDto).getEntity();
        applicationClient.saveApps(entity).getEntity();
    }
    @Override
    public boolean compareAndSendEmail(AppSubmissionDto appSubmissionDto, AppSubmissionDto oldAppSubmissionDto){
        boolean isAuto = true;

        AppEditSelectDto appEditSelectDto = appSubmissionDto.getAppEditSelectDto();
        if(appEditSelectDto != null ){
            if(appEditSelectDto.isPremisesEdit()){
                if(!appSubmissionDto.getAppGrpPremisesDtoList().equals(oldAppSubmissionDto)){
                    isAuto =  compareHciName(appSubmissionDto.getAppGrpPremisesDtoList(), oldAppSubmissionDto.getAppGrpPremisesDtoList());
                    if(isAuto){
                        isAuto = compareLocation(appSubmissionDto.getAppGrpPremisesDtoList(), oldAppSubmissionDto.getAppGrpPremisesDtoList());
                    }
                    //send eamil

                }
            }

            if(appEditSelectDto.isMedAlertEdit()){
                //todo:
            }
            //one svc
            AppSvcRelatedInfoDto appSvcRelatedInfoDtoList = getAppSvcRelatedInfoDto(appSubmissionDto.getAppSvcRelatedInfoDtoList());
            AppSvcRelatedInfoDto oldAppSvcRelatedInfoDtoList = getAppSvcRelatedInfoDto(oldAppSubmissionDto.getAppSvcRelatedInfoDtoList());
            if(appEditSelectDto.isPoEdit() || appEditSelectDto.isDpoEdit() || appEditSelectDto.isServiceEdit()){
                //remove
                List<AppSvcPrincipalOfficersDto> newAppSvcPrincipalOfficersDto  = appSvcRelatedInfoDtoList.getAppSvcPrincipalOfficersDtoList();
                List<AppSvcPrincipalOfficersDto> oldAppSvcPrincipalOfficersDto = oldAppSvcRelatedInfoDtoList.getAppSvcPrincipalOfficersDtoList();
                if(newAppSvcPrincipalOfficersDto.size() > oldAppSvcPrincipalOfficersDto.size()){
                    isAuto = false;
                    //send eamil
                }
                //change
                List<String> newPoIdNos = IaisCommonUtils.genNewArrayList();
                List<String> oldPoIdNos = IaisCommonUtils.genNewArrayList();
                List<String> newDpoIdNos = IaisCommonUtils.genNewArrayList();
                List<String> olddDpoIdNos = IaisCommonUtils.genNewArrayList();

                for(AppSvcPrincipalOfficersDto item:newAppSvcPrincipalOfficersDto){
                    if(ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(item.getPsnType())){
                        newPoIdNos.add(item.getIdNo());
                    }else if(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(item.getPsnType())){
                        newDpoIdNos.add(item.getIdNo());
                    }
                }
                for(AppSvcPrincipalOfficersDto item:oldAppSvcPrincipalOfficersDto){
                    if(ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(item.getPsnType())){
                        oldPoIdNos.add(item.getIdNo());
                    }else if(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(item.getPsnType())){
                        olddDpoIdNos.add(item.getIdNo());
                    }
                }
                if(!newPoIdNos.equals(oldPoIdNos)){
                    isAuto = false;
                }
                if(!newDpoIdNos.equals(olddDpoIdNos)){
                    isAuto = false;
                }
            }

            if(appEditSelectDto.isServiceEdit()){
                List<AppSvcCgoDto> newAppSvcCgoDto =  appSvcRelatedInfoDtoList.getAppSvcCgoDtoList();
                List<AppSvcCgoDto> oldAppSvcCgoDto =  oldAppSvcRelatedInfoDtoList.getAppSvcCgoDtoList();
                List<String> newCgoIdNos = IaisCommonUtils.genNewArrayList();
                List<String> oldCgoIdNos = IaisCommonUtils.genNewArrayList();
                if(!IaisCommonUtils.isEmpty(newAppSvcCgoDto) && !IaisCommonUtils.isEmpty(oldAppSvcCgoDto)){
                    for(AppSvcCgoDto item:newAppSvcCgoDto){
                        newCgoIdNos.add(item.getIdNo());
                    }
                    for(AppSvcCgoDto item:oldAppSvcCgoDto){
                        oldCgoIdNos.add(item.getIdNo());
                    }
                    if(!newCgoIdNos.equals(oldCgoIdNos)){
                        isAuto = false;
                    }

                }

                List<AppSvcLaboratoryDisciplinesDto> newDisciplinesDto = appSvcRelatedInfoDtoList.getAppSvcLaboratoryDisciplinesDtoList();
                List<AppSvcLaboratoryDisciplinesDto> oldDisciplinesDto = oldAppSvcRelatedInfoDtoList.getAppSvcLaboratoryDisciplinesDtoList();

                if(!IaisCommonUtils.isEmpty(newDisciplinesDto) && !IaisCommonUtils.isEmpty(oldDisciplinesDto)){
                    for(AppSvcLaboratoryDisciplinesDto item:newDisciplinesDto){
                        String hciName = item.getPremiseVal();
                        AppSvcLaboratoryDisciplinesDto oldAppSvcLaboratoryDisciplinesDto = getDisciplinesDto(oldDisciplinesDto,hciName);
                        List<AppSvcChckListDto> newCheckList =  item.getAppSvcChckListDtoList();
                        List<AppSvcChckListDto> oldCheckList = oldAppSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList();
                        if(!IaisCommonUtils.isEmpty(newCheckList) && !IaisCommonUtils.isEmpty(oldCheckList)){
                            List<String> newCheckListIds = IaisCommonUtils.genNewArrayList();
                            List<String> oldCheckListIds = IaisCommonUtils.genNewArrayList();
                            for(AppSvcChckListDto checBox:oldCheckList){
                                oldCheckListIds.add(checBox.getChkLstConfId());
                            }
                            for(AppSvcChckListDto checBox:newCheckList){
                                if(!oldCheckListIds.contains(checBox.getChkLstConfId())){
                                    isAuto = false;
                                    break;
                                }
                            }
                        }
                    }
                    if(!newDisciplinesDto.equals(oldDisciplinesDto)){

                    }

                }


            }

            if(appEditSelectDto.isDocEdit()){
            /*if(!appSubmissionDto.getAppGrpPrimaryDocDtos().equals(oldAppSubmissionDto.getAppGrpPrimaryDocDtos()) ||
                    !appSvcRelatedInfoDtoList.getAppSvcDocDtoLit().equals(oldAppSvcRelatedInfoDtoList.getAppSvcDocDtoLit())){
                //send eamil

            }*/

            }
        }

        return isAuto;
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
    public void saveAppGrpMisc(AppGroupMiscDto appGroupMiscDto) {
        applicationClient.saveAppGroupMiscDto(appGroupMiscDto);
    }

    @Override
    public List<AppSubmissionDto> getAppSubmissionDtoByGroupNo(String groupNo) {
        List<ApplicationDto> applicationDtos = applicationClient.getApplicationsByGroupNo(groupNo).getEntity();
        List<AppSubmissionDto> appSubmissionDtos =IaisCommonUtils.genNewArrayList();
        for(ApplicationDto applicationDto : applicationDtos){
            AppSubmissionDto appSubmissionDto = applicationClient.getAppSubmissionDtoByAppNo(applicationDto.getApplicationNo()).getEntity();
            appSubmissionDtos.add(appSubmissionDto);
        }
        return appSubmissionDtos;
    }

    @Override
    public void deleteOverdueDraft(String draftValidity) {
        applicationClient.deleteOverdueDraft(draftValidity);
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
}