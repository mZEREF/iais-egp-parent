package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.risk.RiskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.SmsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPersonnelExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremEventPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremOpenPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremPhOpenPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesOperationalUnitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChargesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcKeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPremisesScopeAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPremisesScopeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.EventApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.GenerateLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.DocumentDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.EventBusLicenceGroupDtos;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.KeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.KeyPersonnelExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppPremCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicBaseSpecifiedCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicDocumentDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicDocumentRelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicKeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremEventPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremOpenPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremPhOpenPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesScopeAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesScopeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesScopeGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicSubLicenseeInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicSvcChargesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicSvcSpecificPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesOperationalUnitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.SuperLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicInspectionGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremInspGrpCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicationGroupService;
import com.ecquaria.cloud.moh.iais.service.InboxMsgService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.LicenceFileDownloadService;
import com.ecquaria.cloud.moh.iais.service.LicenceService;
import com.ecquaria.cloud.moh.iais.service.client.AcraUenBeClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import com.ecquaria.cloud.moh.iais.util.LicenceUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * LicenceApproveBatchjob
 *
 * @author suocheng
 * @date 11/26/2019
 */
@Delegator("licenceApproveBatchjob")
@Slf4j
public class LicenceApproveBatchjob {
    private final String GENERALLICENCE = "generalLicence";
    private final String GROUPLICENCE = "groupLicence";

    @Autowired
    private LicenceService licenceService;
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    private InboxMsgService inboxMsgService;
    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    private ApplicationGroupService applicationGroupService;
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    private EmailClient emailClient;
    @Autowired
    private AcraUenBeClient acraUenBeClient;
    @Autowired
    FillUpCheckListGetAppClient fillUpCheckListGetAppClient;
    @Autowired
    OrganizationClient organizationClient;
    @Autowired
    SystemBeLicClient systemBeLicClient;
    @Autowired
    private InspEmailService inspEmailService;

    @Value("${iais.email.sender}")
    private String mailSender;

    @Autowired
    private BeEicGatewayClient beEicGatewayClient;

    @Autowired
    private LicenceFileDownloadService licenceFileDownloadService;
    private Map<String, Integer> hciCodeVersion = new HashMap();
    private Map<String, Integer> keyPersonnelVersion = IaisCommonUtils.genNewHashMap();

    public void doBatchJob(BaseProcessClass bpc) {
        jobExecute();
    }

    public void jobExecute() {
        log.debug(StringUtil.changeForLog("The LicenceApproveBatchjob is start ..."));
        AuditTrailDto auditTrailDto = AuditTrailHelper.getCurrentAuditTrailDto();
        int day = systemParamConfig.getLicGenDay();
        //get can Generate Licence
        GenerateLicenceDto generateLicenceDto = new GenerateLicenceDto();
        generateLicenceDto.setAuditTrailDto(auditTrailDto);
        generateLicenceDto.setDay(day);
        List<ApplicationLicenceDto> applicationLicenceDtos = licenceService.getCanGenerateApplications(generateLicenceDto);
        if (applicationLicenceDtos == null || applicationLicenceDtos.size() == 0) {
            log.debug(StringUtil.changeForLog("This time do not have need Generate Licences."));
            return;
        }
        //get the all use serviceCode.
        List<String> serviceIds = getAllServiceId(applicationLicenceDtos);
        List<HcsaServiceDto> hcsaServiceDtos = licenceService.getHcsaServiceById(serviceIds);
        if (hcsaServiceDtos == null || hcsaServiceDtos.size() == 0) {
            log.debug(StringUtil.changeForLog("This serviceIds can not get the HcsaServiceDto -->:" + serviceIds));
            return;
        }

        for (ApplicationLicenceDto applicationLicenceDto : applicationLicenceDtos) {
            if (applicationLicenceDto != null) {
                ApplicationGroupDto applicationGroupDto = applicationLicenceDto.getApplicationGroupDto();
                if (applicationGroupDto != null) {
                    List<LicenceGroupDto> licenceGroupDtos = IaisCommonUtils.genNewArrayList();
                    List<ApplicationGroupDto> success = IaisCommonUtils.genNewArrayList();
                    List<Map<String, String>> fail = IaisCommonUtils.genNewArrayList();
                    // delete the reject applicaiton
                    List<ApplicationListDto> applicationListDtoList = applicationLicenceDto.getApplicationListDtoList();
                    deleteRejectApplication(applicationListDtoList);
                    log.debug(StringUtil.changeForLog("The application group no is -->;" + applicationGroupDto.getGroupNo()));
                    Map<String, ApplicationLicenceDto> applicationLicenceDtoMap = sepaApplication(applicationLicenceDto);
                    ApplicationLicenceDto generalApplicationLicenceDto = applicationLicenceDtoMap.get(GENERALLICENCE);
                    ApplicationLicenceDto groupApplicationLicenceDto = applicationLicenceDtoMap.get(GROUPLICENCE);
                    GenerateResult generalGenerateResult = null;
                    GenerateResult groupGenerateResult = null;
                    try {
                        if (groupApplicationLicenceDto != null) {
                            //generate the Group licence
                            groupGenerateResult = generateGroupLicence(groupApplicationLicenceDto, hcsaServiceDtos);
                        }
                        if (generalApplicationLicenceDto != null) {
                            //generate the general licence
                            generalGenerateResult = generateLIcence(generalApplicationLicenceDto, hcsaServiceDtos);
                        }
                    } catch (Exception exception) {
                        log.debug(StringUtil.changeForLog("This  applicaiton group  have error -- >" + applicationGroupDto.getGroupNo()));
                        log.error(exception.getMessage(), exception);
                    }
                    toDoResult(licenceGroupDtos, generalGenerateResult, groupGenerateResult, success, fail, applicationGroupDto);
                    if (success.size() > 0) {
                        //update the align Flag
                        updateExpiryDateByAlignFlag(licenceGroupDtos);
                        //update baseApplicationNo expiry Date
                        updateExpiryDateByBaseApplicationNo(licenceGroupDtos);
                        //add the originLicenceLicBaseSpecifiedCorrelationDtos
                        setOriginLicenceLicBaseSpecifiedCorrelationDtos(licenceGroupDtos);

                        EventBusLicenceGroupDtos eventBusLicenceGroupDtos = new EventBusLicenceGroupDtos();
                        String evenRefNum = String.valueOf(System.currentTimeMillis());
                        eventBusLicenceGroupDtos.setEventRefNo(evenRefNum);
                        eventBusLicenceGroupDtos.setLicenceGroupDtos(licenceGroupDtos);
                        eventBusLicenceGroupDtos.setAuditTrailDto(auditTrailDto);


                        //if create licence success
                        //todo:update the success application group.
                        //get the application
                        List<ApplicationDto> applicationDtos = getApplications(licenceGroupDtos);
                        //
                        EventApplicationGroupDto eventApplicationGroupDto = new EventApplicationGroupDto();
                        eventApplicationGroupDto.setEventRefNo(evenRefNum);
                        eventApplicationGroupDto.setRollBackApplicationGroupDtos(success);
                        eventApplicationGroupDto.setApplicationGroupDtos(updateStatusToGenerated(success));
                        eventApplicationGroupDto.setRollBackApplicationDto(applicationDtos);
                        eventApplicationGroupDto.setApplicationDto(updateApplicationStatusToGenerated(applicationDtos));
                        eventApplicationGroupDto.setAuditTrailDto(auditTrailDto);
                        //step1 create Licence to BE DB
                        eventBusLicenceGroupDtos.setApplicationDto(eventApplicationGroupDto.getApplicationDto());
                        licenceService.createSuperLicDto(eventBusLicenceGroupDtos);
                        //
                        applicationGroupService.updateEventApplicationGroupDto(eventApplicationGroupDto);
                        try {
                            for (ApplicationDto applicationDto:applicationDtos
                                 ) {
                                licenceFileDownloadService.sendRfc008Email(applicationGroupDto,applicationDto);
                            }
                        }catch (Exception e){
                            log.error(e.getMessage());
                        }
                    }

                }

            }
        }
        log.debug(StringUtil.changeForLog("The LicenceApproveBatchjob is end ..."));
    }

    private void updateAppealApplicationStatus(List<ApplicationDto> applicationDtos) {
        if (applicationDtos != null) {
            List<String> appId = new ArrayList<>(applicationDtos.size());
            for (ApplicationDto applicationDto : applicationDtos) {
                appId.add(applicationDto.getId());
            }
            List<ApplicationDto> applicationDtoList = applicationClient.getAppealApplicationByApplicationIds(appId).getEntity();
            for (ApplicationDto applicationDto : applicationDtoList) {
                if (ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationDto.getApplicationType())) {
                    applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_LICENCE_GENERATED);
                    applicationClient.updateApplication(applicationDto);
                    beEicGatewayClient.callEicWithTrack(applicationDto, beEicGatewayClient::updateApplication, "updateApplication");
                }
            }
        }
    }
    private void setOriginLicenceLicBaseSpecifiedCorrelationDtos(List<LicenceGroupDto> licenceGroupDtos){
        log.info(StringUtil.changeForLog("The setOriginLicenceLicBaseSpecifiedCorrelationDtos is strat ..."));
        if(!IaisCommonUtils.isEmpty(licenceGroupDtos)){
            log.info(StringUtil.changeForLog("The setOriginLicenceLicBaseSpecifiedCorrelationDtos licenceGroupDtos.size() is -->:"+licenceGroupDtos.size()));
            for (LicenceGroupDto licenceGroupDto : licenceGroupDtos){
                List<SuperLicDto>  superLicDtos = licenceGroupDto.getSuperLicDtos();
                if(!IaisCommonUtils.isEmpty(superLicDtos)){
                    log.info(StringUtil.changeForLog("The setOriginLicenceLicBaseSpecifiedCorrelationDtos superLicDtos.size() is -->:"+superLicDtos.size()));
                    for(SuperLicDto superLicDto : superLicDtos){
                        LicenceDto licenceDto = superLicDto.getLicenceDto();
                        String originLicenceId = licenceDto.getOriginLicenceId();
                        String svcType = licenceDto.getSvcType();
                        log.info(StringUtil.changeForLog("The licenceDto.getLicenceNo() is -->:"+licenceDto.getLicenceNo()));
                        log.info(StringUtil.changeForLog("The originLicenceId is -->:"+originLicenceId));
                        log.info(StringUtil.changeForLog("The svcType is -->:"+svcType));
                        if(!StringUtil.isEmpty(originLicenceId)){
                            List<LicBaseSpecifiedCorrelationDto> licBaseSpecifiedCorrelationDtos = licenceService.getLicBaseSpecifiedCorrelationDtos(svcType,originLicenceId);
                            if(!IaisCommonUtils.isEmpty(licBaseSpecifiedCorrelationDtos)){
                                superLicDto.setOriginLicenceLicBaseSpecifiedCorrelationDtos(licBaseSpecifiedCorrelationDtos);
                            }
                        }
                    }
                }
            }
        }
        log.info(StringUtil.changeForLog("The setOriginLicenceLicBaseSpecifiedCorrelationDtos is end ..."));
    }
    private void updateExpiryDateByAlignFlag(List<LicenceGroupDto> licenceGroupDtos){
        log.info(StringUtil.changeForLog("The updateExpiryDateByAlignFlag is strat ..."));
        if(!IaisCommonUtils.isEmpty(licenceGroupDtos)){
            log.info(StringUtil.changeForLog("The updateExpiryDateByAlignFlag licenceGroupDtos.size() is -->:"+licenceGroupDtos.size()));
            for (LicenceGroupDto licenceGroupDto : licenceGroupDtos){
                List<SuperLicDto>  superLicDtos = licenceGroupDto.getSuperLicDtos();
                if(!IaisCommonUtils.isEmpty(superLicDtos)){
                    log.info(StringUtil.changeForLog("The updateExpiryDateByAlignFlag superLicDtos.size() is -->:"+superLicDtos.size()));
                    for(SuperLicDto superLicDto : superLicDtos){
                        LicenceDto licenceDto = superLicDto.getLicenceDto();
                        String alignFlag = licenceDto.getAlignFlag();
                        log.info(StringUtil.changeForLog("The updateExpiryDateByAlignFlag alignFlag is -->:"+alignFlag));
                        if(!StringUtil.isEmpty(alignFlag)){
                            Date expiryDate = getEarliestExpiryDate(superLicDtos,alignFlag);
                            if(expiryDate!=null){
                                licenceDto.setExpiryDate(expiryDate);
                            }
                        }
                    }
                }
            }
        }
        log.info(StringUtil.changeForLog("The updateExpiryDateByAlignFlag is end ..."));
    }

    private void updateExpiryDateByBaseApplicationNo(List<LicenceGroupDto> licenceGroupDtos){
        log.info(StringUtil.changeForLog("The updateExpiryDateByBaseApplicationNo is strat ..."));
        if(!IaisCommonUtils.isEmpty(licenceGroupDtos)){
            log.info(StringUtil.changeForLog("The updateExpiryDateByBaseApplicationNo licenceGroupDtos.size() is -->:"+licenceGroupDtos.size()));
            for (LicenceGroupDto licenceGroupDto : licenceGroupDtos){
                List<SuperLicDto>  superLicDtos = licenceGroupDto.getSuperLicDtos();
                if(!IaisCommonUtils.isEmpty(superLicDtos)){
                    log.info(StringUtil.changeForLog("The updateExpiryDateByBaseApplicationNo superLicDtos.size() is -->:"+superLicDtos.size()));
                    for(SuperLicDto superLicDto : superLicDtos){
                        LicenceDto licenceDto = superLicDto.getLicenceDto();
                        String baseApplicationNo = licenceDto.getBaseApplicationNo();
                        log.info(StringUtil.changeForLog("The updateExpiryDateByBaseApplicationNo licenceDto.getLicenceNo() is -->:"+licenceDto.getLicenceNo()));
                        log.info(StringUtil.changeForLog("The updateExpiryDateByBaseApplicationNo baseApplicationNo is -->:"+baseApplicationNo));
                        if(!StringUtil.isEmpty(baseApplicationNo)){
                            LicenceDto baseLicenceDto = getBaseIdForApplicationNo(superLicDtos,baseApplicationNo);
                            if(baseLicenceDto != null) {
                                log.info(StringUtil.changeForLog("The updateExpiryDateByBaseApplicationNo baseLicenceDto.getLicenceNo() is -->:"+baseLicenceDto.getLicenceNo()));
                                if(licenceDto.getExpiryDate().after(baseLicenceDto.getExpiryDate())){
                                    licenceDto.setExpiryDate(baseLicenceDto.getExpiryDate());
                                }
                            }
                        }
                    }
                }
            }
        }
        log.info(StringUtil.changeForLog("The updateExpiryDateByBaseApplicationNo is end ..."));
    }

    private Date getEarliestExpiryDate(List<SuperLicDto> superLicDtos,String alignFlag){
        log.info(StringUtil.changeForLog("The getEarliestExpiryDate  start ..."));
        Date result = null;
        if(!StringUtil.isEmpty(alignFlag) && !IaisCommonUtils.isEmpty(superLicDtos)){
            for (SuperLicDto superLicDto : superLicDtos){
                LicenceDto licenceDto = superLicDto.getLicenceDto();
                String alignFlagEvery = licenceDto.getAlignFlag();
                Date  expiryDate = licenceDto.getExpiryDate();
                log.info(StringUtil.changeForLog("The getEarliestExpiryDate  alignFlagEvery is -->:"+alignFlagEvery));
                log.info(StringUtil.changeForLog("The getEarliestExpiryDate  expiryDate is -->:"+expiryDate));
                if(!StringUtil.isEmpty(alignFlagEvery) && alignFlag.equals(alignFlagEvery)){
                    if(result == null){
                        result =  expiryDate;
                    }else if(expiryDate.before(result)){
                        result =  expiryDate;
                    }
                }
            }
        }
        log.info(StringUtil.changeForLog("The getEarliestExpiryDate  result is -->:"+result));
        log.info(StringUtil.changeForLog("The getEarliestExpiryDate  end ..."));
        return result;
    }

    private LicenceDto getBaseIdForApplicationNo(List<SuperLicDto> superLicDtos,String baseApplicationNo){
        log.info(StringUtil.changeForLog("The getBaseIdForApplicationNo  start ..."));
        LicenceDto result = null;
        if(!StringUtil.isEmpty(baseApplicationNo) && !IaisCommonUtils.isEmpty(superLicDtos)){
            for (SuperLicDto superLicDto : superLicDtos){
                LicenceDto licenceDto = superLicDto.getLicenceDto();
                String svcType = licenceDto.getSvcType();
                String applicationNo = licenceDto.getApplicationNo();
                log.info(StringUtil.changeForLog("The getBaseIdForApplicationNo  svcType is -->:"+svcType));
                log.info(StringUtil.changeForLog("The getBaseIdForApplicationNo  applicationNo is -->:"+applicationNo));
                if(!StringUtil.isEmpty(svcType) && ApplicationConsts.SERVICE_TYPE_BASE.equals(svcType)
                        && baseApplicationNo.equals(applicationNo)){
                    result = licenceDto;
                    break;
                }
            }
        }
        log.info(StringUtil.changeForLog("The getBaseIdForApplicationNo  end ..."));
        return result;
    }

    private Map<String, ApplicationLicenceDto> sepaApplication(ApplicationLicenceDto applicationLicenceDto) {
        log.info(StringUtil.changeForLog("The sepaApplication is strat ..."));
        Map<String, ApplicationLicenceDto> result = new HashMap();
        ApplicationGroupDto applicationGroupDto = applicationLicenceDto.getApplicationGroupDto();
        List<ApplicationListDto> applicationListDtoList = applicationLicenceDto.getApplicationListDtoList();
        if (!IaisCommonUtils.isEmpty(applicationListDtoList)) {
            log.info(StringUtil.changeForLog("The sepaApplication applicationListDtoList.size() is -->: " + applicationListDtoList.size()));
            List<ApplicationListDto> generalLicence = IaisCommonUtils.genNewArrayList();
            List<ApplicationListDto> groupLicence = IaisCommonUtils.genNewArrayList();
            for (ApplicationListDto applicationListDto : applicationListDtoList) {
                ApplicationDto applicationDto = applicationListDto.getApplicationDto();
                if (applicationDto.isGrpLic()) {
                    groupLicence.add(applicationListDto);
                } else {
                    generalLicence.add(applicationListDto);
                }
            }
            log.info(StringUtil.changeForLog("The sepaApplication generateLicence.size() is -->: " + generalLicence.size()));
            if (!IaisCommonUtils.isEmpty(generalLicence)) {
                ApplicationLicenceDto generateApplicationLicenceDto = new ApplicationLicenceDto();
                generateApplicationLicenceDto.setApplicationGroupDto(applicationGroupDto);
                generateApplicationLicenceDto.setApplicationListDtoList(generalLicence);
                result.put(GENERALLICENCE, generateApplicationLicenceDto);
            } else {
                result.put(GENERALLICENCE, null);
            }
            log.info(StringUtil.changeForLog("The sepaApplication groupLicence.size() is -->: " + groupLicence.size()));
            if (!IaisCommonUtils.isEmpty(groupLicence)) {
                ApplicationLicenceDto groupApplicationLicenceDto = new ApplicationLicenceDto();
                groupApplicationLicenceDto.setApplicationGroupDto(applicationGroupDto);
                groupApplicationLicenceDto.setApplicationListDtoList(groupLicence);
                result.put(GROUPLICENCE, groupApplicationLicenceDto);
            } else {
                result.put(GROUPLICENCE, null);
            }
        } else {
            log.debug(StringUtil.changeForLog("The sepaApplication the applicationListDtoList is null"));
        }
        log.info(StringUtil.changeForLog("The sepaApplication is end ..."));
        return result;
    }

    private List<ApplicationDto> updateApplicationStatusToGenerated(List<ApplicationDto> applicationDtos) {
        List<ApplicationDto> result = IaisCommonUtils.genNewArrayList();
        if (IaisCommonUtils.isEmpty(applicationDtos)) {
            return result;
        }
        boolean lastToCreatLicence = isLastToCreatLicence(applicationDtos.get(0));
        for (ApplicationDto applicationDto : applicationDtos) {
            String applicationType = applicationDto.getApplicationType();
            String status = applicationDto.getStatus();
            if(ApplicationConsts.APPLICATION_STATUS_TRANSFER_ORIGIN.equals(applicationDto.getStatus())){
                applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_TRANSFER_ORIGIN_GENERATED);
            }else{
                applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_LICENCE_GENERATED);
            }
            if(ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(applicationType)){
                if(lastToCreatLicence){
                    applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_LICENCE_GENERATED_CEASED);
                }else {
                    applicationDto.setStatus(status);
                }
            }
            result.add(applicationDto);
        }
        return result;
    }

    private boolean isLastToCreatLicence(ApplicationDto applicationDto){
        int count = 0 ;
        String appGrpId = applicationDto.getAppGrpId();
        List<ApplicationDto> applicationDtos = applicationClient.getAppDtosByAppGrpId(appGrpId).getEntity();
        for (ApplicationDto app : applicationDtos) {
            String status = app.getStatus();
            if(ApplicationConsts.APPLICATION_STATUS_CESSATION_TEMPORARY_LICENCE.equals(status)){
                count ++;
            }
        }
        if(count==0){
            return true;
        }
        return false;
    }

    private void deleteRejectApplication(List<ApplicationListDto> applicationListDtoList) {
        List<ApplicationListDto> removeList = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(applicationListDtoList)) {
            for (ApplicationListDto applicationListDto : applicationListDtoList) {
                if (applicationListDto != null) {
                    AppPremisesRecommendationDto appPremisesRecommendationDto = applicationListDto.getAppPremisesRecommendationDto();
                    boolean isReject = isApplicaitonReject(appPremisesRecommendationDto);
                    if (isReject) {
                        removeList.add(applicationListDto);
                    }
                }
            }
            applicationListDtoList.removeAll(removeList);
        }
    }

    private List<ApplicationDto> getApplications(List<LicenceGroupDto> licenceGroupDtos) {
        List<ApplicationDto> result = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(licenceGroupDtos)) {
            for (LicenceGroupDto licenceGroupDto : licenceGroupDtos) {
                List<SuperLicDto> superLicDtos = licenceGroupDto.getSuperLicDtos();
                if (!IaisCommonUtils.isEmpty(superLicDtos)) {
                    for (SuperLicDto superLicDto : superLicDtos) {
                        LicenceDto licenceDto = superLicDto.getLicenceDto();
                        if (licenceDto != null) {
                            List<ApplicationDto> applicationDtos = licenceDto.getApplicationDtos();
                            if (!IaisCommonUtils.isEmpty(applicationDtos)) {
                                result.addAll(applicationDtos);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    private List<ApplicationGroupDto> updateStatusToGenerated(List<ApplicationGroupDto> applicationGroupDtos) {
        List<ApplicationGroupDto> result = IaisCommonUtils.genNewArrayList();
        if (IaisCommonUtils.isEmpty(applicationGroupDtos)) {
            return result;
        }
        for (ApplicationGroupDto applicationGroupDto : applicationGroupDtos) {
            applicationGroupDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_LICENCE_GENERATED);
            result.add(applicationGroupDto);
        }
        return result;
    }

    private void toDoResult(List<LicenceGroupDto> licenceGroupDtos, GenerateResult generalResult, GenerateResult groupResult,
                            List<ApplicationGroupDto> success,
                            List<Map<String, String>> fail, ApplicationGroupDto applicationGroupDto) {
        log.info(StringUtil.changeForLog("The toDoResult is start ..."));
        if (generalResult != null && groupResult != null) {
            boolean isGeneralSuccess = generalResult.isSuccess();
            boolean isGroupSuccess = groupResult.isSuccess();
            if (isGeneralSuccess && isGroupSuccess) {
                if (applicationGroupDto != null) {
                    success.add(applicationGroupDto);
                } else {
                    log.info(StringUtil.changeForLog("There is not the applicationGroupDto for this job"));
                }
                LicenceGroupDto generalLicenceGroupDto = generalResult.getLicenceGroupDto();
                if (generalLicenceGroupDto != null) {
                    licenceGroupDtos.add(generalLicenceGroupDto);
                }
                LicenceGroupDto groupLicenceGroupDto = groupResult.getLicenceGroupDto();
                if (generalLicenceGroupDto != null) {
                    licenceGroupDtos.add(groupLicenceGroupDto);
                }
            } else if (!isGeneralSuccess) {
                Map<String, String> error = new HashMap();
                error.put(applicationGroupDto.getGroupNo(), generalResult.getErrorMessage());
                fail.add(error);
                for (Map.Entry<String, String> ent : error.entrySet()) {
                    String value = ent.getValue();
                    log.debug(StringUtil.changeForLog("The error is -->:" + value));
                }
            } else if (!isGroupSuccess) {
                Map<String, String> error = new HashMap();
                error.put(applicationGroupDto.getGroupNo(), groupResult.getErrorMessage());
                fail.add(error);
                for (Map.Entry<String, String> ent : error.entrySet()) {
                    String value = ent.getValue();
                    log.debug(StringUtil.changeForLog("The error is -->:" + value));
                }
            }
        } else if (generalResult != null) {
            toDoResult(licenceGroupDtos, generalResult, success, fail, applicationGroupDto);
        } else if (groupResult != null) {
            toDoResult(licenceGroupDtos, groupResult, success, fail, applicationGroupDto);
        }
        log.info(StringUtil.changeForLog("The toDoResult is end ..."));

    }

    private void toDoResult(List<LicenceGroupDto> licenceGroupDtos, GenerateResult generateResult, List<ApplicationGroupDto> success,
                            List<Map<String, String>> fail, ApplicationGroupDto applicationGroupDto) {
        if (generateResult != null) {
            boolean isSuccess = generateResult.isSuccess();
            if (isSuccess) {
                if (applicationGroupDto != null) {
                    success.add(applicationGroupDto);
                } else {
                    log.info(StringUtil.changeForLog("There is not the applicationGroupDto for this job"));
                }
                LicenceGroupDto licenceGroupDto = generateResult.getLicenceGroupDto();
                if (licenceGroupDto != null) {
                    licenceGroupDtos.add(licenceGroupDto);
                }
            } else {
                Map<String, String> error = new HashMap();
                error.put(applicationGroupDto.getGroupNo(), generateResult.getErrorMessage());
                fail.add(error);
                for (Map.Entry<String, String> ent : error.entrySet()) {
                    String value = ent.getValue();
                    log.debug(StringUtil.changeForLog("The error is -->:" + value));
                }
            }
        }

    }

    private Map<String, List<ApplicationListDto>> tidyAppForGroupLicence(List<ApplicationListDto> applicationListDtoList) {
        Map<String, List<ApplicationListDto>> result = IaisCommonUtils.genNewLinkedHashMap();
        if (IaisCommonUtils.isEmpty(applicationListDtoList)) {
            return result;
        }
        for (ApplicationListDto applicationListDto : applicationListDtoList) {
            ApplicationDto applicationDto = applicationListDto.getApplicationDto();
            String groupLicenceFlag = applicationDto.getGroupLicenceFlag();
            log.debug(StringUtil.changeForLog("The groupLicenceFlag is -->:" + groupLicenceFlag));
            List<ApplicationListDto> applicationListDtos = result.get(groupLicenceFlag);
            if (applicationListDtos == null) {
                applicationListDtos = IaisCommonUtils.genNewArrayList();
            }
            applicationListDtos.add(applicationListDto);
            result.put(groupLicenceFlag, applicationListDtos);
        }
        return result;
    }

    private AppPremisesRecommendationDto getAppPremisesRecommendationDto(List<ApplicationListDto> applicationListDtos) {
        log.info(StringUtil.changeForLog("The getAppPremisesRecommendationDto start ..."));
        AppPremisesRecommendationDto result = null;
        if (!IaisCommonUtils.isEmpty(applicationListDtos)) {
            for (ApplicationListDto applicationListDto : applicationListDtos) {
                AppPremisesRecommendationDto appPremisesRecommendationDto = applicationListDto.getAppPremisesRecommendationDto();
                if (result == null) {
                    result = appPremisesRecommendationDto;
                } else {
                    //Licence start date
                    if (appPremisesRecommendationDto.getRecomInDate() != null) {
                        if (result.getRecomInDate() == null || appPremisesRecommendationDto.getRecomInDate().before(result.getRecomInDate())) {
                            result.setRecomInDate(appPremisesRecommendationDto.getRecomInDate());
                        }
                    }
                    //RecomInNumber
                    if (!StringUtil.isEmpty(appPremisesRecommendationDto.getChronoUnit()) && appPremisesRecommendationDto.getRecomInNumber() != null) {
                        if (appPremisesRecommendationDto.getChronoUnit().equals(result.getChronoUnit())) {
                            if (appPremisesRecommendationDto.getRecomInNumber() < result.getRecomInNumber()) {
                                result.setRecomInNumber(appPremisesRecommendationDto.getRecomInNumber());
                            }
                        } else if (RiskConsts.YEAR.equals(result.getChronoUnit())) {
                            result.setRecomInNumber(appPremisesRecommendationDto.getRecomInNumber());
                            result.setChronoUnit(appPremisesRecommendationDto.getChronoUnit());
                        } else if (RiskConsts.MONTH.equals(result.getChronoUnit()) && RiskConsts.WEEK.equals(result.getChronoUnit())) {
                            result.setRecomInNumber(appPremisesRecommendationDto.getRecomInNumber());
                            result.setChronoUnit(appPremisesRecommendationDto.getChronoUnit());
                        }
                    }
                }
            }
        }
        if (result != null) {
            log.info(StringUtil.changeForLog("The Licence Start Date -->:" + result.getRecomInDate()));
            log.info(StringUtil.changeForLog("The RecomInNumber -->:" + result.getRecomInNumber()));
            log.info(StringUtil.changeForLog("The ChronoUnit -->:" + result.getChronoUnit()));
        }
        log.info(StringUtil.changeForLog("The getAppPremisesRecommendationDto end ..."));
        return result;
    }

    private void addDocumentToList(List<PremisesGroupDto> premisesGroupDtos, List<LicDocumentRelationDto> licDocumentRelationDtos) {
        log.info(StringUtil.changeForLog("The addDocumentToList start ..."));
        log.info(StringUtil.changeForLog("The addDocumentToList licDocumentRelationDtos.size() is-->:" + licDocumentRelationDtos.size()));
        if (!IaisCommonUtils.isEmpty(premisesGroupDtos)) {
            log.info(StringUtil.changeForLog("The addDocumentToList premisesGroupDtos.size() is-->:" + premisesGroupDtos.size()));
            for (PremisesGroupDto premisesGroupDto : premisesGroupDtos) {
                List<LicDocumentRelationDto> licDocumentRelationDtoList = premisesGroupDto.getLicDocumentRelationDtos();
                if (!IaisCommonUtils.isEmpty(licDocumentRelationDtoList)) {
                    log.info(StringUtil.changeForLog("The addDocumentToList licDocumentRelationDtoList.size() is-->:" + licDocumentRelationDtoList.size()));
                    for (LicDocumentRelationDto licDocumentRelationDto : licDocumentRelationDtoList) {
                        if (!isExist(licDocumentRelationDtos, licDocumentRelationDto.getDocumentDto().getFileRepoId())) {
                            licDocumentRelationDtos.add(licDocumentRelationDto);
                        }
                    }
                }
            }
        }
        log.info(StringUtil.changeForLog("The addDocumentToList licDocumentRelationDtos.size() is-->:" + licDocumentRelationDtos.size()));
        log.info(StringUtil.changeForLog("The addDocumentToList end ..."));
    }

    private String addPremisesGroupDtos(List<PremisesGroupDto> allPremisesGroupDtos, List<PremisesGroupDto> premisesGroupDtos,
                                        ApplicationListDto applicationListDto) {
        log.info(StringUtil.changeForLog("The allPremisesGroupDtos start ..."));
        String errorMsg = null;
        if (!IaisCommonUtils.isEmpty(premisesGroupDtos)) {
            for (PremisesGroupDto premisesGroupDto : premisesGroupDtos) {
                PremisesDto premisesDto = premisesGroupDto.getPremisesDto();
                String hciCode = premisesDto.getHciCode();
                log.info(StringUtil.changeForLog("The allPremisesGroupDtos hciCode is -->:" + hciCode));
                boolean isExist = isExistPremisess(allPremisesGroupDtos, hciCode);
                log.info(StringUtil.changeForLog("The allPremisesGroupDtos isExist is -->:" + isExist));
                if (!isExist) {
                    premisesDto.setVersion(getVersionByHciCode(hciCode));
                    List<LicPremisesScopeGroupDto> licPremisesScopeGroupDtoList = premisesGroupDto.getLicPremisesScopeGroupDtoList();
                    if (!IaisCommonUtils.isEmpty(licPremisesScopeGroupDtoList)) {
                        for (LicPremisesScopeGroupDto licPremisesScopeGroupDto : licPremisesScopeGroupDtoList) {
                            LicPremisesScopeAllocationDto licPremisesScopeAllocationDto = licPremisesScopeGroupDto.getLicPremisesScopeAllocationDto();
                            KeyPersonnelDto keyPersonnelDto = licPremisesScopeGroupDto.getKeyPersonnelDto();
                            String idNo = keyPersonnelDto.getIdNo();
                            log.info(StringUtil.changeForLog("The allPremisesGroupDtos idNo is -->:" + idNo));
                            String appSvcKeyPsnId = getAppSvcKeyPsnId(applicationListDto, idNo);
                            if (!StringUtil.isEmpty(appSvcKeyPsnId)) {
                                licPremisesScopeAllocationDto.setLicCgoId(appSvcKeyPsnId);
                            } else {
                                errorMsg = "can not find  the CGO for idNo is -->:" + idNo;
                                log.debug(StringUtil.changeForLog(errorMsg));
                            }
                        }
                    }
                    allPremisesGroupDtos.add(premisesGroupDto);
                }
            }
        }
        log.info(StringUtil.changeForLog("The allPremisesGroupDtos end ..."));
        return errorMsg;
    }

    private String getAppSvcKeyPsnId(ApplicationListDto applicationListDto, String idNo) {
        log.info(StringUtil.changeForLog("The getAppSvcKeyPsnId start ..."));
        String result = null;
        if (!StringUtil.isEmpty(idNo)) {
            List<String> personnelIds = IaisCommonUtils.genNewArrayList();
            List<AppGrpPersonnelDto> appGrpPersonnelDtos = applicationListDto.getAppGrpPersonnelDtos();
            if (!IaisCommonUtils.isEmpty(appGrpPersonnelDtos)) {
                log.info(StringUtil.changeForLog("The getAppSvcKeyPsnId appGrpPersonnelDtos.size() is -- >:" + appGrpPersonnelDtos.size()));
                for (AppGrpPersonnelDto appGrpPersonnelDtos1 : appGrpPersonnelDtos) {
                    if (idNo.equals(appGrpPersonnelDtos1.getIdNo())) {
                        personnelIds.add(appGrpPersonnelDtos1.getId());
                    }
                }
            }

            if (!IaisCommonUtils.isEmpty(personnelIds)) {
                log.info(StringUtil.changeForLog("The getAppSvcKeyPsnId personnelIds.size() is -- >:" + personnelIds.size()));
                List<AppSvcKeyPersonnelDto> appSvcKeyPersonnelDtos = applicationListDto.getAppSvcKeyPersonnelDtos();
                if (!IaisCommonUtils.isEmpty(appSvcKeyPersonnelDtos)) {
                    log.info(StringUtil.changeForLog("The getAppSvcKeyPsnId appSvcKeyPersonnelDtos.size() is -- >:" + appSvcKeyPersonnelDtos.size()));
                    for (String personnelId : personnelIds) {
                        for (AppSvcKeyPersonnelDto appSvcKeyPersonnelDto : appSvcKeyPersonnelDtos) {
                            if (personnelId.equals(appSvcKeyPersonnelDto.getAppGrpPsnId()) && ApplicationConsts.PERSONNEL_PSN_TYPE_CGO.equals(appSvcKeyPersonnelDto.getPsnType())) {
                                result = appSvcKeyPersonnelDto.getId();
                                break;
                            }
                        }
                        if (!StringUtil.isEmpty(result)) {
                            break;
                        }
                    }
                }
            }
        }
        log.info(StringUtil.changeForLog("The getAppSvcKeyPsnId result is -- >:" + result));
        log.info(StringUtil.changeForLog("The getAppSvcKeyPsnId end ..."));
        return result;
    }

    private boolean isExistPremisess(List<PremisesGroupDto> allPremisesGroupDtos, String hciCode) {
        boolean result = false;
        if (!IaisCommonUtils.isEmpty(allPremisesGroupDtos) && !StringUtil.isEmpty(hciCode)) {
            for (PremisesGroupDto premisesGroupDto : allPremisesGroupDtos) {
                if (hciCode.equals(premisesGroupDto.getPremisesDto().getHciCode())) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    public GenerateResult generateGroupLicence(ApplicationLicenceDto applicationLicenceDto, List<HcsaServiceDto> hcsaServiceDtos) {
        log.debug(StringUtil.changeForLog("The generateGroupLicence is start ..."));
        GenerateResult result = new GenerateResult();
        LicenceGroupDto licenceGroupDto = new LicenceGroupDto();
        List<ApplicationListDto> applicationListDtoList = applicationLicenceDto.getApplicationListDtoList();
        ApplicationGroupDto applicationGroupDto = applicationLicenceDto.getApplicationGroupDto();
        Integer isPostInspNeeded = isPostInspNeeded(applicationGroupDto);
        log.debug(StringUtil.changeForLog("The isPostInspNeeded is -->:" + isPostInspNeeded));
        String appGroupType = applicationGroupDto.getAppType();
        log.debug(StringUtil.changeForLog("The appGroupType is -->:" + appGroupType));
        licenceGroupDto.setAppType(appGroupType);

        LicenseeDto oldLicenseeDto = getOrganizationIdBylicenseeId(applicationGroupDto.getLicenseeId());
        //get organizationId
        String organizationId = oldLicenseeDto.getOrganizationId();
        log.debug(StringUtil.changeForLog("The organizationId is -->:" + organizationId));
        if (IaisCommonUtils.isEmpty(applicationListDtoList)) {
            result.setSuccess(false);
            result.setErrorMessage("The applicationListDtoList is null ...");
        } else {
            log.debug(StringUtil.changeForLog("The applicationListDtoList size is -->:" + applicationListDtoList.size()));
            //tidy up Application for Group Licence use
            Map<String, List<ApplicationListDto>> applications = tidyAppForGroupLicence(applicationListDtoList);
            List<SuperLicDto> superLicDtos = IaisCommonUtils.genNewArrayList();
            String errorMessage = null;
            if (applications.size() <= 0) {
                return result;
            }
            for (Map.Entry<String, List<ApplicationListDto>> key : applications.entrySet()) {
                SuperLicDto superLicDto = new SuperLicDto();
                superLicDto.setAppType(applicationGroupDto.getAppType());
                log.debug(StringUtil.changeForLog("The key is -->:" + key.getKey()));
                List<ApplicationListDto> applicationListDtos = applications.get(key.getKey());
                if (IaisCommonUtils.isEmpty(applicationListDtos)) {
                    continue;
                }
                List<ApplicationDto> applicationDtos = getApplicationDtos(applicationListDtos);
                ApplicationDto firstApplicationDto = applicationDtos.get(0);
                String applicationType = firstApplicationDto.getApplicationType();
                //to check this applicaiton is approve
                //get recommedation logic
                AppPremisesRecommendationDto appPremisesRecommendationDto = getAppPremisesRecommendationDto(applicationListDtos);
                //get service code
                String serviceId = firstApplicationDto.getServiceId();
                log.debug(StringUtil.changeForLog("The serviceId is -->:" + serviceId));
                HcsaServiceDto hcsaServiceDto = getHcsaServiceDtoByServiceId(hcsaServiceDtos, serviceId);
                if (hcsaServiceDto == null) {
                    errorMessage = "This ServiceId can not get the HcsaServiceDto -->:" + serviceId;
                    break;
                }
                String originLicenceId = firstApplicationDto.getOriginLicenceId();
                LicenceDto originLicenceDto = licenceService.getLicenceDto(originLicenceId);
                LicenceDto licenceDto = getLicenceDto(hcsaServiceDto.getSvcName(), hcsaServiceDto.getSvcType(), applicationGroupDto, appPremisesRecommendationDto,
                        originLicenceDto, firstApplicationDto, applicationDtos, true);
                licenceDto.setSvcCode(hcsaServiceDto.getSvcCode());
                licenceDto.setPremiseSize(applicationDtos.size());
                superLicDto.setLicenceDto(licenceDto);

                 originLicenceDto = deleteOriginLicenceDto(originLicenceDto,firstApplicationDto,licenceDto.getStatus());
                log.info(StringUtil.changeForLog("The applicationType is -->:"+ApplicationConsts.APPLICATION_TYPE_RENEWAL));
                if(!ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(firstApplicationDto.getApplicationType())){
                    if(originLicenceDto != null && !ApplicationConsts.LICENCE_STATUS_REVOKED.equals(originLicenceDto.getStatus())){
                        superLicDto.setOriginLicenceDto(originLicenceDto);
                    }else{
                        log.info(StringUtil.changeForLog("can not update the originLicenceDto"));
                    }
                }
                //if PostInspNeeded send email
                if (isPostInspNeeded == Integer.parseInt(AppConsts.YES)) {
//                    sendEmailInspection(licenceDto);
                }
                //
                List<PremisesGroupDto> premisesGroupDtos = IaisCommonUtils.genNewArrayList();
                List<LicAppCorrelationDto> licAppCorrelationDtos = IaisCommonUtils.genNewArrayList();
                List<LicDocumentRelationDto> licDocumentRelationDtos = IaisCommonUtils.genNewArrayList();
                List<PersonnelsDto> personnelsDtos = IaisCommonUtils.genNewArrayList();
                //create key_personnel key_personnel_ext lic_key_personnel
                List<AppGrpPersonnelDto> appGrpPersonnelDtos = applicationListDtos.get(0).getAppGrpPersonnelDtos();
                List<AppGrpPersonnelExtDto> appGrpPersonnelExtDtos = applicationListDtos.get(0).getAppGrpPersonnelExtDtos();
                List<AppSvcKeyPersonnelDto> appSvcKeyPersonnelDtos = applicationListDtos.get(0).getAppSvcKeyPersonnelDtos();
                if (!IaisCommonUtils.isEmpty(appSvcKeyPersonnelDtos)) {
                    List<PersonnelsDto> personnelsDto1s = getPersonnelsDto(appGrpPersonnelDtos, appGrpPersonnelExtDtos, appSvcKeyPersonnelDtos, organizationId);
                    if (personnelsDtos == null) {
                        errorMessage = "There is Error for AppGrpPersonnel -->: " + firstApplicationDto.getApplicationNo();
                        break;
                    }
                    personnelsDtos.addAll(personnelsDto1s);
                }
                for (ApplicationListDto applicationListDto : applicationListDtos) {
                    ApplicationDto applicationDto = applicationListDto.getApplicationDto();
                    if(applicationDto == null){
                     continue;
                    }
                    String appType = applicationDto.getApplicationType();
                    log.debug(StringUtil.changeForLog("The appType is -->:" + appType));
                    //create Premises
                    List<AppGrpPremisesEntityDto> appGrpPremisesEntityDtos = applicationListDto.getAppGrpPremisesEntityDtos();
                    if (appGrpPremisesEntityDtos == null || appGrpPremisesEntityDtos.size() == 0) {
                        errorMessage = "The AppGrpPremises is null for ApplicationNo" + applicationDto.getApplicationNo();
                        break;
                    }
                    log.debug(StringUtil.changeForLog("The appGrpPremisesDtos.size() is -->;" + appGrpPremisesEntityDtos.size()));
                    //create lic_premises
                    List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = applicationListDto.getAppPremisesCorrelationDtos();
                    //create LicPremisesScopeDto
                    List<AppSvcPremisesScopeDto> appSvcPremisesScopeDtos = applicationListDto.getAppSvcPremisesScopeDtos();
                    List<AppSvcPremisesScopeAllocationDto> appSvcPremisesScopeAllocationDtos = applicationListDto.getAppSvcPremisesScopeAllocationDtos();
                    List<AppGrpPersonnelDto> appGrpPersonnelDtosE = applicationListDto.getAppGrpPersonnelDtos();
                    List<AppSvcKeyPersonnelDto> appSvcKeyPersonnelDtosE = applicationListDto.getAppSvcKeyPersonnelDtos();

                    List<PremisesGroupDto> premisesGroupDtos1 = getPremisesGroupDto(applicationListDto,applicationLicenceDto, appGrpPremisesEntityDtos, appPremisesCorrelationDtos, appSvcPremisesScopeDtos,
                            appSvcPremisesScopeAllocationDtos,appGrpPersonnelDtosE,appSvcKeyPersonnelDtosE, hcsaServiceDto, organizationId, isPostInspNeeded);
                    if (!IaisCommonUtils.isEmpty(premisesGroupDtos1)) {
                        premisesGroupDtos.addAll(premisesGroupDtos1);
                    }

                    //create the lic_app_correlation
                    LicAppCorrelationDto licAppCorrelationDto = new LicAppCorrelationDto();
                    licAppCorrelationDto.setApplicationId(applicationListDto.getApplicationDto().getId());
                    licAppCorrelationDtos.add(licAppCorrelationDto);

                    //create the document and lic_document from the primary doc.
                    List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = applicationListDto.getAppGrpPrimaryDocDtos();
                    List<AppSvcDocDto> appSvcDocDtos = applicationListDto.getAppSvcDocDtos();
                    List<LicDocumentRelationDto> licDocumentRelationDto1s = getLicDocumentRelationDto(licDocumentRelationDtos,appGrpPrimaryDocDtos,
                            appSvcDocDtos, appPremisesCorrelationDtos, premisesGroupDtos);
                    //licDocumentRelationDtos.addAll(licDocumentRelationDto1s);

                    //create the lic_fee_group_item
                    //do not need create in the Dto
                    //todo:lic_base_specified_correlation
                    //

                        sendEmailAndSms(applicationDto, licenceDto, oldLicenseeDto, originLicenceDto, serviceId,applicationListDto.getAppPremisesRecommendationDto());
                }
                //part premises
                boolean isPartPremises = firstApplicationDto.isPartPremises();
                log.info(StringUtil.changeForLog("The generateGroupLicence isPartPremises is -->:" + isPartPremises));
                boolean rfcTypeFlag = ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(firstApplicationDto.getApplicationType());
                if (rfcTypeFlag && isPartPremises) {
                    String everyOriginLicenceId = firstApplicationDto.getOriginLicenceId();
                    log.info(StringUtil.changeForLog("The generateGroupLicence everyOriginLicenceId is -->:" + everyOriginLicenceId));
                    if (!StringUtil.isEmpty(everyOriginLicenceId)) {
                        List<PremisesGroupDto> premisesGroupDtoList = licenceService.getPremisesGroupDtoByOriginLicenceId(everyOriginLicenceId);
                        String msg = addPremisesGroupDtos(premisesGroupDtos, premisesGroupDtoList, applicationListDtos.get(0));
                        if (!StringUtil.isEmpty(msg)) {
                            errorMessage = msg;
                            break;
                        }
                        // addDocumentToList(premisesGroupDtoList,licDocumentRelationDtos);
                    } else {
                        log.debug(StringUtil.changeForLog("This Appno do not have the OriginLicenceId -- >" + firstApplicationDto.getApplicationNo()));
                    }
                }

                //create LicSvcSpecificPersonnelDto
                List<AppSvcPersonnelDto> appSvcPersonnelDtos = applicationListDtos.get(0).getAppSvcPersonnelDtos();
                List<LicSvcSpecificPersonnelDto> licSvcSpecificPersonnelDtos = getLicSvcSpecificPersonnelDtos(appSvcPersonnelDtos);
                superLicDto.setLicSvcSpecificPersonnelDtos(licSvcSpecificPersonnelDtos);

                superLicDto.setPremisesGroupDtos(premisesGroupDtos);
                superLicDto.setLicAppCorrelationDtos(licAppCorrelationDtos);
                superLicDto.setLicDocumentRelationDto(licDocumentRelationDtos);
                superLicDto.setPersonnelsDtos(personnelsDtos);
                superLicDto.setLicAppCorrelationDtos(licAppCorrelationDtos);
                superLicDtos.add(superLicDto);
            }

            licenceGroupDto.setSuperLicDtos(superLicDtos);
            if (StringUtil.isEmpty(errorMessage)) {
                result.setSuccess(true);
                result.setLicenceGroupDto(licenceGroupDto);
            } else {
                result.setSuccess(false);
                result.setErrorMessage(errorMessage);
            }
        }
        log.debug(StringUtil.changeForLog("The generateGroupLicence is end ..."));
        return result;
    }

    private LicenceDto deleteOriginLicenceDto(LicenceDto originLicenceDto,ApplicationDto applicationDto,String licenceStatus) {
        log.info(StringUtil.changeForLog("The deleteOriginLicenceDto start ..."));
        log.info(StringUtil.changeForLog("The deleteOriginLicenceDto licenceStatus is -->:"+licenceStatus));
        String appType = applicationDto.getApplicationType();
        log.info(StringUtil.changeForLog("The deleteOriginLicenceDto appType is -->:"+appType));
        String groupLicenceFlag = applicationDto.getGroupLicenceFlag();
        log.info(StringUtil.changeForLog("The deleteOriginLicenceDto groupLicenceFlag is -->:"+groupLicenceFlag));
        if (originLicenceDto != null) {
            //67406
            if(ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(appType)){
                originLicenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_CEASED);
            }else if(ApplicationConsts.LICENCE_STATUS_ACTIVE.equals(licenceStatus)){
                if(ApplicationConsts.GROUP_LICENCE_FLAG_ORIGIN.equals(groupLicenceFlag) || ApplicationConsts.GROUP_LICENCE_FLAG_All_TRANSFER.equals(groupLicenceFlag)){
                    originLicenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_TRANSFERRED);
                }else if(ApplicationConsts.GROUP_LICENCE_FLAG_TRANSFER.equals(groupLicenceFlag)){
                    originLicenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_REVOKED);
                }else{
                    originLicenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_IACTIVE);
                }
            }
            log.info(StringUtil.changeForLog("The deleteOriginLicenceDto originLicenceDto.getStatus() is -->:"+originLicenceDto.getStatus()));
        }else{
            log.info(StringUtil.changeForLog("The deleteOriginLicenceDto originLicenceDto is null..."));
        }

        log.info(StringUtil.changeForLog("The deleteOriginLicenceDto end ..."));
        return originLicenceDto;
    }

    private List<ApplicationDto> getApplicationDtos(List<ApplicationListDto> applicationListDtos) {
        List<ApplicationDto> result = IaisCommonUtils.genNewArrayList();
        if (applicationListDtos != null && applicationListDtos.size() > 0) {
            for (ApplicationListDto applicationListDto : applicationListDtos) {
                result.add(applicationListDto.getApplicationDto());
            }
        }
        Collections.sort(result,new Comparator<ApplicationDto>(){
            @Override
            public int compare(ApplicationDto applicationDto1, ApplicationDto applicationDto2) {
               int diff = applicationDto1.getApplicationNo().compareTo(applicationDto2.getApplicationNo());
                if (diff > 0) {
                    return 1;
                }else  if (diff < 0) {
                    return -1;
                }
                return 0;
            }
        });
        return result;
    }

    private GenerateResult generateLIcence(ApplicationLicenceDto applicationLicenceDto, List<HcsaServiceDto> hcsaServiceDtos) {
        log.debug(StringUtil.changeForLog("The generateLIcence is start ..."));
        GenerateResult result = new GenerateResult();
        LicenceGroupDto licenceGroupDto = new LicenceGroupDto();
        List<ApplicationListDto> applicationListDtoList = applicationLicenceDto.getApplicationListDtoList();
        ApplicationGroupDto applicationGroupDto = applicationLicenceDto.getApplicationGroupDto();
        Integer isPostInspNeeded = isPostInspNeeded(applicationGroupDto);
        log.debug(StringUtil.changeForLog("The isPostInspNeeded is -->:" + isPostInspNeeded));
        String appGroupType = applicationGroupDto.getAppType();
        log.debug(StringUtil.changeForLog("The appGroupType is -->:" + appGroupType));
        licenceGroupDto.setAppType(appGroupType);
        LicenseeDto oldLicenseeDto = getOrganizationIdBylicenseeId(applicationGroupDto.getLicenseeId());
        //get organizationId
        String organizationId = oldLicenseeDto.getOrganizationId();
        log.debug(StringUtil.changeForLog("The organizationId is -->:" + organizationId));

        if (applicationListDtoList == null || applicationListDtoList.size() == 0) {
            result.setSuccess(false);
            result.setErrorMessage("The applicationListDtoList is null ...");
        } else {
            log.debug(StringUtil.changeForLog("The applicationListDtoList size is -->:" + applicationListDtoList.size()));

            List<SuperLicDto> superLicDtos = IaisCommonUtils.genNewArrayList();
            String errorMessage = null;
            for (ApplicationListDto applicationListDto : applicationListDtoList) {
                SuperLicDto superLicDto = new SuperLicDto();
                //get service code
                ApplicationDto applicationDto = applicationListDto.getApplicationDto();
                if (applicationDto == null) {
                    errorMessage = "There is a ApplicationDto is null";
                    break;
                }
                String appType = applicationDto.getApplicationType();
                log.debug(StringUtil.changeForLog("The appType is -->:" + appType));
                superLicDto.setAppType(appType);
                //to check this applicaiton is approve
                AppPremisesRecommendationDto appPremisesRecommendationDto = applicationListDto.getAppPremisesRecommendationDto();
                String serviceId = applicationDto.getServiceId();
                log.debug(StringUtil.changeForLog("The serviceId is -->:" + serviceId));
                HcsaServiceDto hcsaServiceDto = getHcsaServiceDtoByServiceId(hcsaServiceDtos, serviceId);
                if (hcsaServiceDto == null) {
                    errorMessage = "There is a ApplicationDto is null";
                    break;
                }
                //create Premises
                List<AppGrpPremisesEntityDto> appGrpPremisesEntityDtos = applicationListDto.getAppGrpPremisesEntityDtos();
                if (appGrpPremisesEntityDtos == null || appGrpPremisesEntityDtos.size() == 0) {
                    errorMessage = "The AppGrpPremises is null for ApplicationNo" + applicationDto.getApplicationNo();
                    break;
                }
                log.debug(StringUtil.changeForLog("The appGrpPremisesDtos.size() is -->;" + appGrpPremisesEntityDtos.size()));
                //
                //create lic_premises
                List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = applicationListDto.getAppPremisesCorrelationDtos();
                //create LicPremisesScopeDto
                List<AppSvcPremisesScopeDto> appSvcPremisesScopeDtos = applicationListDto.getAppSvcPremisesScopeDtos();
                List<AppSvcPremisesScopeAllocationDto> appSvcPremisesScopeAllocationDtos = applicationListDto.getAppSvcPremisesScopeAllocationDtos();
                List<AppGrpPersonnelDto> appGrpPersonnelDtos = applicationListDto.getAppGrpPersonnelDtos();
                List<AppSvcKeyPersonnelDto> appSvcKeyPersonnelDtos = applicationListDto.getAppSvcKeyPersonnelDtos();


                List<PremisesGroupDto> premisesGroupDtos = getPremisesGroupDto(applicationListDto,applicationLicenceDto, appGrpPremisesEntityDtos, appPremisesCorrelationDtos, appSvcPremisesScopeDtos,
                        appSvcPremisesScopeAllocationDtos,appGrpPersonnelDtos,appSvcKeyPersonnelDtos, hcsaServiceDto, organizationId, isPostInspNeeded);
                //String licenceNo = null;
                //get the yearLenth.
//                int yearLength = getYearLength(appPremisesRecommendationDto);
                if (!IaisCommonUtils.isEmpty(premisesGroupDtos)) {
                    PremisesGroupDto premisesGroupDto = premisesGroupDtos.get(0);
                    if (premisesGroupDto.isHasError()) {
                        errorMessage = premisesGroupDto.getErrorMessage();
                        break;
                    }
                    superLicDto.setPremisesGroupDtos(premisesGroupDtos);
                }

                String originLicenceId = applicationDto.getOriginLicenceId();
                String applicationType = applicationDto.getApplicationType();

                // LicenceDto originLicenceDto = licenceService.getLicenceDto(originLicenceId);
                LicenceDto originLicenceDto = licenceService.getLicDtoById(originLicenceId);
                LicenceDto licenceDto = getLicenceDto(hcsaServiceDto.getSvcName(), hcsaServiceDto.getSvcType(), applicationGroupDto, appPremisesRecommendationDto,
                        originLicenceDto, applicationDto, null, false);
                licenceDto.setSvcCode(hcsaServiceDto.getSvcCode());
                superLicDto.setLicenceDto(licenceDto);

                originLicenceDto = deleteOriginLicenceDto(originLicenceDto, applicationDto, licenceDto.getStatus());
                log.info(StringUtil.changeForLog("The applicationType is -->:" + ApplicationConsts.APPLICATION_TYPE_RENEWAL));
                if (!ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(applicationType)) {
                    if (originLicenceDto != null && ApplicationConsts.LICENCE_STATUS_REVOKED.equals(originLicenceDto.getStatus())) {
                        log.info(StringUtil.changeForLog("The originLicenceDto.getStatus() is -->:" + originLicenceDto.getStatus()));
                        originLicenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_TRANSFERRED);
                    }
                    superLicDto.setOriginLicenceDto(originLicenceDto);
                }

                if(originLicenceDto != null){
                    if (originLicenceDto.isMigrated()
                            && IaisEGPHelper.isActiveMigrated()
                            && (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(applicationDto.getApplicationType()) ||
                            ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationDto.getApplicationType()))
                            ) {
                        if(StringUtil.isEmpty(applicationGroupDto.getNewLicenseeId())){
                            originLicenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_IACTIVE);
                        }else{
                            originLicenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_TRANSFERRED);
                        }

                    }
                }
                //create LicSubLicenseeInfoDto
                LicSubLicenseeInfoDto licSubLicenseeInfoDto = getLicSubLicenseeInfoDto(applicationListDto);
//                if(licSubLicenseeInfoDto != null){
//                    licSubLicenseeInfoDto.setOrgId(organizationId);
//                }
                superLicDto.setLicSubLicenseeInfoDto(licSubLicenseeInfoDto);
                //create the lic_app_correlation
                List<LicAppCorrelationDto> licAppCorrelationDtos = IaisCommonUtils.genNewArrayList();
                LicAppCorrelationDto licAppCorrelationDto = new LicAppCorrelationDto();
                licAppCorrelationDto.setApplicationId(applicationDto.getId());
                licAppCorrelationDtos.add(licAppCorrelationDto);
                superLicDto.setLicAppCorrelationDtos(licAppCorrelationDtos);

                //create the document and lic_document from the primary doc.
                List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = applicationListDto.getAppGrpPrimaryDocDtos();
                List<AppSvcDocDto> appSvcDocDtos = applicationListDto.getAppSvcDocDtos();
                List<LicDocumentRelationDto> licDocumentRelationDtos = getLicDocumentRelationDto(null,appGrpPrimaryDocDtos,
                        appSvcDocDtos, appPremisesCorrelationDtos, premisesGroupDtos);
                superLicDto.setLicDocumentRelationDto(licDocumentRelationDtos);

                //create key_personnel key_personnel_ext lic_key_personnel
                List<AppGrpPersonnelExtDto> appGrpPersonnelExtDtos = applicationListDto.getAppGrpPersonnelExtDtos();
                if (!IaisCommonUtils.isEmpty(appSvcKeyPersonnelDtos)) {
                    List<PersonnelsDto> personnelsDtos = getPersonnelsDto(appGrpPersonnelDtos, appGrpPersonnelExtDtos, appSvcKeyPersonnelDtos, organizationId);
                    if (IaisCommonUtils.isEmpty(personnelsDtos)) {
                        errorMessage = "There is Error for AppGrpPersonnel -->: " + applicationDto.getApplicationNo();
                        break;
                    }
                    superLicDto.setPersonnelsDtos(personnelsDtos);
                }

                //create the lic_fee_group_item
                //do not need create in the Dto
                //todo:lic_base_specified_correlation
                //

                //create LicSvcSpecificPersonnelDto
                List<AppSvcPersonnelDto> appSvcPersonnelDtos = applicationListDto.getAppSvcPersonnelDtos();
                List<LicSvcSpecificPersonnelDto> licSvcSpecificPersonnelDtos = getLicSvcSpecificPersonnelDtos(appSvcPersonnelDtos);
                superLicDto.setLicSvcSpecificPersonnelDtos(licSvcSpecificPersonnelDtos);

                superLicDtos.add(superLicDto);

                //if PostInspNeeded send email
//                if (isPostInspNeeded == Integer.parseInt(AppConsts.YES)) {
//                    sendEmailInspection(licenceDto);
//                }
//                sendEmailAndSms(applicationDto, licenceDto, oldLicenseeDto, originLicenceDto, serviceId,applicationListDto.getAppPremisesRecommendationDto());
            }
            licenceGroupDto.setSuperLicDtos(superLicDtos);
            if (StringUtil.isEmpty(errorMessage)) {
                result.setSuccess(true);
                result.setLicenceGroupDto(licenceGroupDto);
            } else {
                result.setSuccess(false);
                result.setErrorMessage(errorMessage);
            }
        }
        log.debug(StringUtil.changeForLog("The generateLIcence is end ..."));
        return result;
    }


    private LicSubLicenseeInfoDto getLicSubLicenseeInfoDto(ApplicationListDto applicationListDto){
        log.info(StringUtil.changeForLog("The getLicSubLicenseeInfoDto start ..."));
        LicSubLicenseeInfoDto result = null;
        List<SubLicenseeDto> subLicenseeDtos = applicationListDto.getSubLicenseeDtos();
        if(!IaisCommonUtils.isEmpty(subLicenseeDtos)){
            SubLicenseeDto subLicenseeDto = subLicenseeDtos.get(0);
            result = MiscUtil.transferEntityDto(subLicenseeDto,LicSubLicenseeInfoDto.class);
            result.setId(null);
        }
        log.info(StringUtil.changeForLog("The getLicSubLicenseeInfoDto end ..."));
        return result;
    }

    private boolean isApplicaitonReject(AppPremisesRecommendationDto appPremisesRecommendationDto) {
        boolean result = false;
        if (appPremisesRecommendationDto != null) {
            Integer number = appPremisesRecommendationDto.getRecomInNumber();
            if (number != null) {
                if (number == 0) {
                    result = true;
                }
            }
        }
        return result;
    }

    private List<LicSvcSpecificPersonnelDto> getLicSvcSpecificPersonnelDtos(List<AppSvcPersonnelDto> appSvcPersonnelDtos) {
        List<LicSvcSpecificPersonnelDto> result = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(appSvcPersonnelDtos)) {
            for (AppSvcPersonnelDto appSvcPersonnelDto : appSvcPersonnelDtos) {
                LicSvcSpecificPersonnelDto licSvcSpecificPersonnelDto = MiscUtil.transferEntityDto(appSvcPersonnelDto, LicSvcSpecificPersonnelDto.class);
                licSvcSpecificPersonnelDto.setAppSvcPsnId(licSvcSpecificPersonnelDto.getId());
                licSvcSpecificPersonnelDto.setId(null);
                result.add(licSvcSpecificPersonnelDto);
            }
        }
        return result;
    }

    private String getHciCodeFromSameApplicaitonGroup(ApplicationLicenceDto applicationLicenceDto, AppGrpPremisesEntityDto appGrpPremisesEntityDto) {
        log.info(StringUtil.changeForLog("The getHciCodeFromSameApplicaitonGroup start ..."));
        String hciCode = null;
        if (applicationLicenceDto != null && appGrpPremisesEntityDto != null) {
            List<ApplicationListDto> applicationListDtoList = applicationLicenceDto.getApplicationListDtoList();
            if (!IaisCommonUtils.isEmpty(applicationListDtoList)) {
                for (ApplicationListDto applicationListDto : applicationListDtoList) {
                    List<AppGrpPremisesEntityDto> appGrpPremisesEntityDtos = applicationListDto.getAppGrpPremisesEntityDtos();
                    if (!IaisCommonUtils.isEmpty(appGrpPremisesEntityDtos)) {
                        for (AppGrpPremisesEntityDto appGrpPremisesEntityDto1 : appGrpPremisesEntityDtos) {
                            if (appGrpPremisesEntityDto.getPremiseKey().equals(appGrpPremisesEntityDto1.getPremiseKey()) &&
                                    !StringUtil.isEmpty(appGrpPremisesEntityDto1.getHciCode())) {
                                hciCode = appGrpPremisesEntityDto1.getHciCode();
                                break;
                            }
                        }
                    }
                    if (!StringUtil.isEmpty(hciCode)) {
                        break;
                    }
                }
            }
        }
        log.info(StringUtil.changeForLog("The licence Generate getHciCodeFromSameApplicaitonGroup hciCode is -->:"+hciCode));
        log.info(StringUtil.changeForLog("The getHciCodeFromSameApplicaitonGroup end ..."));
        return hciCode;

    }

    private AppSvcKeyPersonnelDto getAppSvcKeyPersonnelDtoById(List<AppSvcKeyPersonnelDto> appSvcKeyPersonnelDtos,String id){
        log.info(StringUtil.changeForLog("The licence Generate getAppSvcKeyPersonnelDtoById start ..."));
        log.info(StringUtil.changeForLog("The licence Generate getAppSvcKeyPersonnelDtoById appSvcKeyPersonnel id is -->："+id));
        AppSvcKeyPersonnelDto result = null;
        if(!IaisCommonUtils.isEmpty(appSvcKeyPersonnelDtos) && !StringUtil.isEmpty(id)){
            log.info(StringUtil.changeForLog("The licence Generate getAppSvcKeyPersonnelDtoById appSvcKeyPersonnelDtos.size() is -->："+appSvcKeyPersonnelDtos.size()));
            for (AppSvcKeyPersonnelDto appSvcKeyPersonnelDto : appSvcKeyPersonnelDtos){
                if(id.equals(appSvcKeyPersonnelDto.getId())){
                    result = appSvcKeyPersonnelDto;
                    break;
                }
            }
        }
        log.info(StringUtil.changeForLog("The licence Generate getAppSvcKeyPersonnelDtoById end ..."));
        return result;
    }

    private List<PremisesGroupDto> getPremisesGroupDto(ApplicationListDto applicationListDto,
                                                       ApplicationLicenceDto applicationLicenceDto,
                                                       List<AppGrpPremisesEntityDto> appGrpPremisesEntityDtos,
                                                       List<AppPremisesCorrelationDto> appPremisesCorrelationDtos,
                                                       List<AppSvcPremisesScopeDto> appSvcPremisesScopeDtos,
                                                       List<AppSvcPremisesScopeAllocationDto> appSvcPremisesScopeAllocationDtos,
                                                       List<AppGrpPersonnelDto> appGrpPersonnelDtos,
                                                       List<AppSvcKeyPersonnelDto> appSvcKeyPersonnelDtos,
                                                       HcsaServiceDto hcsaServiceDto,
                                                       String organizationId,
                                                       Integer isPostInspNeeded) {
        log.info(StringUtil.changeForLog("The licence Generate getPremisesGroupDto start ..."));
        List<PremisesGroupDto> reuslt = IaisCommonUtils.genNewArrayList();
        if (IaisCommonUtils.isEmpty(appGrpPremisesEntityDtos)) {
            return reuslt;
        }
        for (AppGrpPremisesEntityDto appGrpPremisesEntityDto : appGrpPremisesEntityDtos) {
            PremisesGroupDto premisesGroupDto = new PremisesGroupDto();
            premisesGroupDto.setHasError(false);
            boolean isNewHciCode = false;
            //premises
            String premisesId = appGrpPremisesEntityDto.getId();
            AppPremisesCorrelationDto appPremisesCorrelationDto = getAppPremCorrecId(appPremisesCorrelationDtos, premisesId);
            if(appPremisesCorrelationDto != null) {
                String licHciCode = hcsaLicenceClient.getHciCodeByCorrId(appPremisesCorrelationDto.getId()).getEntity();
                if (!StringUtil.isEmpty(licHciCode)) {
                    appGrpPremisesEntityDto.setHciCode(licHciCode);
                }
            }
            String hciCode = appGrpPremisesEntityDto.getHciCode();
            log.info(StringUtil.changeForLog("The licence Generate getPremisesGroupDto hciCode is -->:"+hciCode));
            if (StringUtil.isEmpty(hciCode)) {
                hciCode = getHciCodeFromSameApplicaitonGroup(applicationLicenceDto, appGrpPremisesEntityDto);
                if (StringUtil.isEmpty(hciCode)) {
                    PremisesDto hciCodePremisesDto = licenceService.getHciCode(appGrpPremisesEntityDto);
                    if(hciCodePremisesDto != null){
                        hciCode = hciCodePremisesDto.getHciCode();
                    }else {
                        log.info(StringUtil.changeForLog("The licence Generate getPremisesGroupDto do not get ou the hciCode from DB"));
                    }
                }
                if (StringUtil.isEmpty(hciCode)) {
                    hciCode = licenceService.getHciCode(hcsaServiceDto.getSvcCode());
                }
                log.info(StringUtil.changeForLog("The licence Generate getPremisesGroupDto finale hciCode is -->:"+hciCode));
                isNewHciCode = true;
                appGrpPremisesEntityDto.setHciCode(hciCode);
            }
            PremisesDto premisesDto = MiscUtil.transferEntityDto(appGrpPremisesEntityDto, PremisesDto.class);
            premisesDto.setHciCode(hciCode);
            premisesDto.setNewHciCode(isNewHciCode);
            premisesDto.setVersion(getVersionByHciCode(hciCode));
            premisesDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            premisesDto.setOrganizationId(organizationId);
            List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodDtos = appGrpPremisesEntityDto.getAppPremPhOpenPeriodDtoList();
            List<LicPremPhOpenPeriodDto> licPremPhOpenPeriodDtos = IaisCommonUtils.genNewArrayList();
            if (!IaisCommonUtils.isEmpty(appPremPhOpenPeriodDtos)) {
                log.info(StringUtil.changeForLog("The licence Generate appPremPhOpenPeriodDtos.size() is -->:"+appPremPhOpenPeriodDtos.size()));
                for (AppPremPhOpenPeriodDto appPremPhOpenPeriodDto : appPremPhOpenPeriodDtos) {
                    LicPremPhOpenPeriodDto licPremPhOpenPeriodDto = MiscUtil.transferEntityDto(appPremPhOpenPeriodDto, LicPremPhOpenPeriodDto.class);
                    licPremPhOpenPeriodDto.setId(null);
                    licPremPhOpenPeriodDto.setPremId(null);
                    licPremPhOpenPeriodDtos.add(licPremPhOpenPeriodDto);
                }
            }
            List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtos = appGrpPremisesEntityDto.getAppPremisesOperationalUnitDtos();
            List<PremisesOperationalUnitDto> premisesOperationalUnitDtos = IaisCommonUtils.genNewArrayList();
            if (!IaisCommonUtils.isEmpty(appPremisesOperationalUnitDtos)) {
                log.info(StringUtil.changeForLog("The licence Generate appPremisesOperationalUnitDtos.size() is -->:"+appPremisesOperationalUnitDtos.size()));
                for (AppPremisesOperationalUnitDto appPremisesOperationalUnitDto : appPremisesOperationalUnitDtos) {
                    PremisesOperationalUnitDto premisesOperationalUnitDto = MiscUtil.transferEntityDto(appPremisesOperationalUnitDto, PremisesOperationalUnitDto.class);
                    premisesOperationalUnitDto.setId(null);
                    premisesOperationalUnitDto.setPremisesId(null);
                    premisesOperationalUnitDtos.add(premisesOperationalUnitDto);
                }
            }
            //weekly
            List<AppPremOpenPeriodDto> appWeeklyDtos = appGrpPremisesEntityDto.getWeeklyDtos();
            List<LicPremOpenPeriodDto> licWeeklyDtos = IaisCommonUtils.genNewArrayList();
            if(!IaisCommonUtils.isEmpty(appWeeklyDtos)){
                log.info(StringUtil.changeForLog("The licence Generate appWeeklyDtos.size() is -->:"+appWeeklyDtos.size()));
                for(AppPremOpenPeriodDto appWeeklyDto:appWeeklyDtos){
                    LicPremOpenPeriodDto licWeeklyDto= MiscUtil.transferEntityDto(appWeeklyDto, LicPremOpenPeriodDto.class);
                    licWeeklyDto.setId(null);
                    licWeeklyDto.setPremId(null);
                    licWeeklyDtos.add(licWeeklyDto);
                }
            }

            //event
            List<AppPremEventPeriodDto> appEventDtos = appGrpPremisesEntityDto.getEventDtos();
            List<LicPremEventPeriodDto> licEventDtos = IaisCommonUtils.genNewArrayList();
            if(!IaisCommonUtils.isEmpty(appEventDtos)){
                log.info(StringUtil.changeForLog("The licence Generate appEventDtos.size() is -->:"+appEventDtos.size()));
                for(AppPremEventPeriodDto appEventDto:appEventDtos){
                    LicPremEventPeriodDto licEventDto = MiscUtil.transferEntityDto(appEventDto,LicPremEventPeriodDto.class);
                    licEventDto.setId(null);
                    licEventDto.setPremId(null);
                    licEventDtos.add(licEventDto);
                }
            }

            premisesDto.setLicPremPhOpenPeriodDtos(licPremPhOpenPeriodDtos);
            premisesDto.setPremisesOperationalUnitDtos(premisesOperationalUnitDtos);
            premisesDto.setWeeklyDtos(licWeeklyDtos);
            premisesDto.setEventDtos(licEventDtos);
            premisesGroupDto.setPremisesDto(premisesDto);
            //create lic_premises
            //String premisesId = appGrpPremisesEntityDto.getId();
            if (appPremisesCorrelationDto == null) {
                continue;
            }
            AppPremisesRecommendationDto appPremisesRecommendationDto = licenceService.getTcu(appPremisesCorrelationDto.getId());
            LicPremisesDto licPremisesDto = new LicPremisesDto();
            licPremisesDto.setPremisesId(premisesId);
            licPremisesDto.setIsPostInspNeeded(isPostInspNeeded);
            log.info(StringUtil.changeForLog("The BusinessName is-->："+appPremisesCorrelationDto.getBusinessName()));
            licPremisesDto.setBusinessName(appPremisesCorrelationDto.getBusinessName());
            if (appPremisesRecommendationDto == null || appPremisesRecommendationDto.getRecomInDate() == null) {
                licPremisesDto.setIsTcuNeeded(Integer.valueOf(AppConsts.NO));
            } else {
                licPremisesDto.setIsTcuNeeded(Integer.valueOf(AppConsts.YES));
                licPremisesDto.setTcuDate(appPremisesRecommendationDto.getRecomInDate());
            }
            premisesGroupDto.setLicPremisesDto(licPremisesDto);
            //set LicAppPremCorrelationDto
            LicAppPremCorrelationDto licAppPremCorrelationDto = new LicAppPremCorrelationDto();
            licAppPremCorrelationDto.setAppCorrId(appPremisesCorrelationDto.getId());
            premisesGroupDto.setLicAppPremCorrelationDto(licAppPremCorrelationDto);
            //set LicSvcVehicleDto
            List<LicSvcVehicleDto> licSvcVehicleDtos = IaisCommonUtils.genNewArrayList();
            List<AppSvcVehicleDto> appSvcVehicleDtos = applicationListDto.getAppSvcVehicleDtoList();
            if(!IaisCommonUtils.isEmpty(appSvcVehicleDtos)){
              for(AppSvcVehicleDto appSvcVehicleDto : appSvcVehicleDtos){
                  //todo:filter the reject Vehicle
                  LicSvcVehicleDto licSvcVehicleDto = MiscUtil.transferEntityDto(appSvcVehicleDto,LicSvcVehicleDto.class);
                  licSvcVehicleDtos.add(licSvcVehicleDto);
              }
                premisesGroupDto.setLicSvcVehicleDtos(licSvcVehicleDtos);
            }
            //set LicSvcChargesDto
            List<LicSvcChargesDto> licSvcChargesDtos = IaisCommonUtils.genNewArrayList();
            List<AppSvcChargesDto> appSvcChargesDtos = applicationListDto.getAppSvcChargesDtos();
            if(!IaisCommonUtils.isEmpty(appSvcChargesDtos)){
                for(AppSvcChargesDto appSvcChargesDto: appSvcChargesDtos){
                    LicSvcChargesDto licSvcChargesDto = MiscUtil.transferEntityDto(appSvcChargesDto,LicSvcChargesDto.class);
                    licSvcChargesDtos.add(licSvcChargesDto);
                }
                premisesGroupDto.setLicSvcChargesDtos(licSvcChargesDtos);
            }
            //set LicSvcClinicalDirectorDto
            /*List<LicSvcClinicalDirectorDto> licSvcClinicalDirectorDtos = IaisCommonUtils.genNewArrayList();
            List<AppSvcClinicalDirectorDto> appSvcClinicalDirectorDtos = applicationListDto.getAppSvcClinicalDirectorDtoList();
            if(!IaisCommonUtils.isEmpty(appSvcClinicalDirectorDtos)){
                for(AppSvcClinicalDirectorDto appSvcClinicalDirectorDto : appSvcClinicalDirectorDtos){
                    LicSvcClinicalDirectorDto licSvcClinicalDirectorDto = MiscUtil.transferEntityDto(appSvcClinicalDirectorDto,LicSvcClinicalDirectorDto.class);
                    licSvcClinicalDirectorDtos.add(licSvcClinicalDirectorDto);
                }
                premisesGroupDto.setLicSvcClinicalDirectorDtos(licSvcClinicalDirectorDtos);
            }*/
            if (1 == isPostInspNeeded) {
                //create the LicInspectionGroupDto
                LicInspectionGroupDto licInspectionGroupDto = new LicInspectionGroupDto();
                licInspectionGroupDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                premisesGroupDto.setLicInspectionGroupDto(licInspectionGroupDto);
                //create the LicPremInspGrpCorrelationDto
                LicPremInspGrpCorrelationDto licPremInspGrpCorrelationDto = new LicPremInspGrpCorrelationDto();
                licPremInspGrpCorrelationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                premisesGroupDto.setLicPremInspGrpCorrelationDto(licPremInspGrpCorrelationDto);
            }

            //create LicPremisesScopeDto
            List<AppSvcPremisesScopeDto> appSvcPremisesScopeDtoList = getAppSvcPremisesScopeDtoByCorrelationId(appSvcPremisesScopeDtos, appPremisesCorrelationDto.getId());
            if (!IaisCommonUtils.isEmpty(appSvcPremisesScopeDtoList)) {
                List<LicPremisesScopeGroupDto> licPremisesScopeGroupDtoList = IaisCommonUtils.genNewArrayList();
                for (AppSvcPremisesScopeDto appSvcPremisesScopeDto : appSvcPremisesScopeDtoList) {
                    LicPremisesScopeGroupDto licPremisesScopeGroupDto = new LicPremisesScopeGroupDto();
                    LicPremisesScopeDto licPremisesScopeDto = MiscUtil.transferEntityDto(appSvcPremisesScopeDto, LicPremisesScopeDto.class);
                    licPremisesScopeDto.setId(null);
                    licPremisesScopeGroupDto.setLicPremisesScopeDto(licPremisesScopeDto);
                    //create LicPremisesScopeAllocationDto
                    AppSvcPremisesScopeAllocationDto appSvcPremisesScopeAllocationDto = getAppSvcPremisesScopeAllocationDto(appSvcPremisesScopeAllocationDtos,
                            appSvcPremisesScopeDto.getId());
                    if (appSvcPremisesScopeAllocationDto != null) {
                        AppSvcKeyPersonnelDto appSvcKeyPersonnelDto = getAppSvcKeyPersonnelDtoById(appSvcKeyPersonnelDtos, appSvcPremisesScopeAllocationDto.getAppSvcKeyPsnId());
                        if(appSvcKeyPersonnelDto != null){
                            AppGrpPersonnelDto appGrpPersonnelDto = getAppGrpPersonnelDtoById(appGrpPersonnelDtos, appSvcKeyPersonnelDto.getAppGrpPsnId());
                            if(appGrpPersonnelDto!= null){
                                LicPremisesScopeAllocationDto licPremisesScopeAllocationDto = new LicPremisesScopeAllocationDto();
                                licPremisesScopeAllocationDto.setLicCgoId(appGrpPersonnelDto.getIdNo());
                                //licPremisesScopeAllocationDto.setLicPremScopeId(appSvcPremisesScopeAllocationDto.getAppSvcPremScopeId());
                                licPremisesScopeAllocationDto.setLicPremSvcPersonId(appSvcPremisesScopeAllocationDto.getAppSvcPersonId());
                                licPremisesScopeGroupDto.setLicPremisesScopeAllocationDto(licPremisesScopeAllocationDto);
                            }else{
                                log.debug(StringUtil.changeForLog("this appSvcKeyPersonnelDto.getAppGrpPsnId() do not have the AppGrpPersonnelDto -->:"
                                        + appSvcKeyPersonnelDto.getAppGrpPsnId()));
                            }
                        }else{
                            log.debug(StringUtil.changeForLog("this appSvcPremisesScopeAllocationDto.getAppSvcKeyPsnId() do not have the AppSvcKeyPersonnelDto -->:"
                                    + appSvcPremisesScopeAllocationDto.getAppSvcKeyPsnId()));
                        }
                    } else {
                        log.debug(StringUtil.changeForLog("this appSvcPremisesScopeDto.getId() do not have the AppSvcPremisesScopeAllocationDto -->:" + appSvcPremisesScopeDto.getId()));
                    }
                    licPremisesScopeGroupDtoList.add(licPremisesScopeGroupDto);
                }
                premisesGroupDto.setLicPremisesScopeGroupDtoList(licPremisesScopeGroupDtoList);
            } else {
                log.info(StringUtil.changeForLog("This appPremCorrecId can not get the AppSvcPremisesScopeDto -->:" + appPremisesCorrelationDto.getId()));
            }
            reuslt.add(premisesGroupDto);
        }
        log.info(StringUtil.changeForLog("The licence Generate getPremisesGroupDto end ..."));
        return reuslt;
    }


    private List<AppSvcPremisesScopeDto> getAppSvcPremisesScopeDtoByCorrelationId(List<AppSvcPremisesScopeDto> appSvcPremisesScopeDtos, String appPremCorrecId) {
        List<AppSvcPremisesScopeDto> result = IaisCommonUtils.genNewArrayList();
        if (IaisCommonUtils.isEmpty(appSvcPremisesScopeDtos) || appPremCorrecId == null || StringUtil.isEmpty(appPremCorrecId)) {
            return result;
        }

        for (AppSvcPremisesScopeDto appSvcPremisesScopeDto : appSvcPremisesScopeDtos) {
            if (appSvcPremisesScopeDto != null && appPremCorrecId.equals(appSvcPremisesScopeDto.getAppPremCorreId())) {
                result.add(appSvcPremisesScopeDto);
            }
        }
        return result;
    }

//    private LicFeeGroupDto getLicFeeGroupDto(String amount) {
//        LicFeeGroupDto licFeeGroupDto = new LicFeeGroupDto();
//        licFeeGroupDto.setFeeAmount(amount);
//        return licFeeGroupDto;
//    }

    private Integer isPostInspNeeded(ApplicationGroupDto applicationGroupDto) {
        log.debug(StringUtil.changeForLog("The isPostInspNeeded is start ..."));
        int inspectionNeed = applicationGroupDto.getIsInspectionNeeded();
        log.debug(StringUtil.changeForLog("The inspectionNeed is -->:" + inspectionNeed));
        int isPreInspection = applicationGroupDto.getIsPreInspection();
        log.debug(StringUtil.changeForLog("The isPreInspection is -->:" + isPreInspection));
        Integer isPostInspNeeded = Integer.valueOf(AppConsts.NO);
        if (inspectionNeed == 1 && isPreInspection == 0) {
            isPostInspNeeded = Integer.valueOf(AppConsts.YES);
        }
        log.debug(StringUtil.changeForLog("The isPostInspNeeded is end ..."));
        return isPostInspNeeded;
    }

    private List<PersonnelsDto> getPersonnelsDto(List<AppGrpPersonnelDto> appGrpPersonnelDtos, List<AppGrpPersonnelExtDto> appGrpPersonnelExtDtos,
                                                 List<AppSvcKeyPersonnelDto> appSvcKeyPersonnelDtos,
                                                 String organizationId) {
        List<PersonnelsDto> result = IaisCommonUtils.genNewArrayList();
        if (IaisCommonUtils.isEmpty(appSvcKeyPersonnelDtos)) {
            return result;
        }
        for (AppSvcKeyPersonnelDto appSvcKeyPersonnelDto : appSvcKeyPersonnelDtos) {
            PersonnelsDto personnelsDto = new PersonnelsDto();
            //create AppGrpPersonnelDto
            String appGrpPsnId = appSvcKeyPersonnelDto.getAppGrpPsnId();
            AppGrpPersonnelDto appGrpPersonnelDto = getAppGrpPersonnelDtoById(appGrpPersonnelDtos, appGrpPsnId);
            if (appGrpPersonnelDto == null) {
                return result;
            }
            KeyPersonnelDto keyPersonnelDto = MiscUtil.transferEntityDto(appGrpPersonnelDto, KeyPersonnelDto.class);
            //:controller the psersonnel version
            keyPersonnelDto.setVersion(getKeyPersonnelVersion(keyPersonnelDto.getIdNo(), organizationId,keyPersonnelDto.getNationality()));
            //todo: controller status
            keyPersonnelDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            //: controller the Organization
            keyPersonnelDto.setOrganizationId(organizationId);
            keyPersonnelDto.setAppPsnId(keyPersonnelDto.getId());
            personnelsDto.setKeyPersonnelDto(keyPersonnelDto);
            //create AppGrpPersonnelExtDto
            String appGrpPsnExtId = appSvcKeyPersonnelDto.getAppGrpPsnExtId();
            AppGrpPersonnelExtDto appGrpPersonnelExtDto = getAppGrpPersonnelExtDtoById(appGrpPersonnelExtDtos, appGrpPsnExtId);
            KeyPersonnelExtDto keyPersonnelExtDto = MiscUtil.transferEntityDto(appGrpPersonnelExtDto, KeyPersonnelExtDto.class);
            if (keyPersonnelExtDto != null) {
                keyPersonnelExtDto.setId(null);
            }
            personnelsDto.setKeyPersonnelExtDto(keyPersonnelExtDto);
            LicKeyPersonnelDto licKeyPersonnelDto = new LicKeyPersonnelDto();
            //to use in the create to get the Relation.
            licKeyPersonnelDto.setId(appSvcKeyPersonnelDto.getId());
            licKeyPersonnelDto.setPsnType(appSvcKeyPersonnelDto.getPsnType());
            personnelsDto.setLicKeyPersonnelDto(licKeyPersonnelDto);
            result.add(personnelsDto);
        }
        return result;
    }

    private Integer getKeyPersonnelVersion(String idNo, String orgId,String nationality) {
        Integer result = 1;
        if (StringUtil.isEmpty(idNo) || StringUtil.isEmpty(orgId)) {
            return result;
        }
        Integer version = keyPersonnelVersion.get(idNo + orgId+nationality);
        if (version == null) {
            KeyPersonnelDto keyPersonnelDto = licenceService.getLatestVersionKeyPersonnelByIdNoAndOrgId(idNo, orgId,nationality);
            if (keyPersonnelDto != null) {
                result = keyPersonnelDto.getVersion() + 1;
            }
        } else {
            result = version + 1;
        }
        keyPersonnelVersion.put(idNo + orgId+nationality, result);
        return result;
    }

    private AppGrpPersonnelExtDto getAppGrpPersonnelExtDtoById(List<AppGrpPersonnelExtDto> appGrpPersonnelExtDtos, String appGrpPsnExtId) {
        AppGrpPersonnelExtDto result = null;
        if (appGrpPersonnelExtDtos == null || appGrpPersonnelExtDtos.size() == 0 || StringUtil.isEmpty(appGrpPsnExtId)) {
            return result;
        }
        for (AppGrpPersonnelExtDto appGrpPersonnelExtDto : appGrpPersonnelExtDtos) {
            if (appGrpPsnExtId.equals(appGrpPersonnelExtDto.getId())) {
                result = appGrpPersonnelExtDto;
                break;
            }
        }
        return result;
    }

    private AppGrpPersonnelDto getAppGrpPersonnelDtoById(List<AppGrpPersonnelDto> appGrpPersonnelDtos, String appGrpPsnId) {
        AppGrpPersonnelDto result = null;
        if (appGrpPersonnelDtos == null || appGrpPersonnelDtos.size() == 0 || StringUtil.isEmpty(appGrpPsnId)) {
            return result;
        }
        for (AppGrpPersonnelDto appGrpPersonnelDto : appGrpPersonnelDtos) {
            if (appGrpPsnId.equals(appGrpPersonnelDto.getId())) {
                result = appGrpPersonnelDto;
                break;
            }
        }
        return result;
    }

    private AppSvcPremisesScopeAllocationDto getAppSvcPremisesScopeAllocationDto(List<AppSvcPremisesScopeAllocationDto> appSvcPremisesScopeAllocationDtos,
                                                                                 String appSvcPremisesScopeId) {
        AppSvcPremisesScopeAllocationDto result = null;
        if (StringUtil.isEmpty(appSvcPremisesScopeId) || appSvcPremisesScopeAllocationDtos == null || appSvcPremisesScopeAllocationDtos.size() == 0) {
            return result;
        }
        for (AppSvcPremisesScopeAllocationDto appSvcPremisesScopeAllocationDto : appSvcPremisesScopeAllocationDtos) {
            if (appSvcPremisesScopeId.equals(appSvcPremisesScopeAllocationDto.getAppSvcPremScopeId())) {
                result = appSvcPremisesScopeAllocationDto;
                break;
            }
        }
        return result;
    }

    private boolean isExist(List<LicDocumentRelationDto> licDocumentRelationDtos, String fileRepoId) {
        boolean result = false;
        log.info(StringUtil.changeForLog("The isExist start ..."));
        log.info(StringUtil.changeForLog("The fileRepoId is -->:" + fileRepoId));
        log.info(StringUtil.changeForLog("The licDocumentRelationDtos.size() is -->:" + licDocumentRelationDtos.size()));
        if (!IaisCommonUtils.isEmpty(licDocumentRelationDtos) && !StringUtil.isEmpty(fileRepoId)) {
            for (LicDocumentRelationDto licDocumentRelationDto : licDocumentRelationDtos) {
                DocumentDto documentDto = licDocumentRelationDto.getDocumentDto();
                if (fileRepoId.equals(documentDto.getFileRepoId())) {
                    result = true;
                }
            }
        }
        log.info(StringUtil.changeForLog("The result is -->:" + result));
        log.info(StringUtil.changeForLog("The isExist end ..."));
        return result;
    }

    private List<LicDocumentRelationDto> getLicDocumentRelationDto(List<LicDocumentRelationDto> licDocumentRelationDtos,
                                                                   List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos, List<AppSvcDocDto> appSvcDocDtos,
                                                                   List<AppPremisesCorrelationDto> appPremisesCorrelationDtos, List<PremisesGroupDto> premisesGroupDtos) {
        log.info(StringUtil.changeForLog("The getLicDocumentRelationDto start ..."));
        if(licDocumentRelationDtos==null){
            licDocumentRelationDtos = IaisCommonUtils.genNewArrayList();
        }
        if (appGrpPrimaryDocDtos != null) {
            for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : appGrpPrimaryDocDtos) {
                if (isExist(licDocumentRelationDtos, appGrpPrimaryDocDto.getFileRepoId())) {
                    continue;
                }
                if (!IaisCommonUtils.isEmpty(premisesGroupDtos)) {
                    for (PremisesGroupDto premisesGroupDto : premisesGroupDtos) {
                        PremisesDto premisesDto = premisesGroupDto.getPremisesDto();
                        LicDocumentRelationDto licDocumentRelationDto = new LicDocumentRelationDto();
                        DocumentDto documentDto = MiscUtil.transferEntityDto(appGrpPrimaryDocDto, DocumentDto.class);
                        documentDto.setId(null);
                        licDocumentRelationDto.setDocumentDto(documentDto);

                        LicDocumentDto licDocumentDto = new LicDocumentDto();
                        licDocumentDto.setSvcDocId(appGrpPrimaryDocDto.getSvcDocId());
                        licDocumentDto.setDocType(Integer.valueOf(ApplicationConsts.APPLICATION_DOC_TYPE_PARIMARY));
                        licDocumentDto.setSeqNum(appGrpPrimaryDocDto.getSeqNum());
                        //set the old premises Id ,get the releation when the save.
                        if (StringUtil.isEmpty(appGrpPrimaryDocDto.getAppGrpPremId())) {
                            licDocumentDto.setLicPremId(premisesDto.getId());
                            licDocumentRelationDto.setLicDocumentDto(licDocumentDto);
                            licDocumentRelationDtos.add(licDocumentRelationDto);
                        } else if (appGrpPrimaryDocDto.getAppGrpPremId().equals(premisesDto.getId())) {
                            licDocumentDto.setLicPremId(appGrpPrimaryDocDto.getAppGrpPremId());
                            licDocumentRelationDto.setLicDocumentDto(licDocumentDto);
                            licDocumentRelationDtos.add(licDocumentRelationDto);
                        }
                    }
                }
            }
        }
        if (appSvcDocDtos != null) {
            for (AppSvcDocDto appSvcDocDto : appSvcDocDtos) {
                if (isExist(licDocumentRelationDtos, appSvcDocDto.getFileRepoId())) {
                    continue;
                }
                LicDocumentRelationDto licDocumentRelationDto = new LicDocumentRelationDto();
                DocumentDto documentDto = MiscUtil.transferEntityDto(appSvcDocDto, DocumentDto.class);
                documentDto.setId(null);
                licDocumentRelationDto.setDocumentDto(documentDto);
                LicDocumentDto licDocumentDto = new LicDocumentDto();
                licDocumentDto.setSvcDocId(appSvcDocDto.getSvcDocId());
                licDocumentDto.setDocType(Integer.valueOf(ApplicationConsts.APPLICATION_DOC_TYPE_SERVICE));
                licDocumentDto.setAppPersonId(appSvcDocDto.getAppGrpPersonId());
                licDocumentDto.setSeqNum(appSvcDocDto.getSeqNum());
                licDocumentDto.setLicPersonType(appSvcDocDto.getPersonType());
                licDocumentDto.setLicPersonTypeNum(appSvcDocDto.getPersonTypeNum());
                licDocumentDto.setLicSvcSpePsnId(appSvcDocDto.getAppSvcPersonId());
                //set the old premises Id ,get the releation when the save.
                String premisesId = getPremisesByAppPremCorreId(appPremisesCorrelationDtos, appSvcDocDto.getAppPremCorreId());
                if (StringUtil.isEmpty(premisesId)) {
                    log.info(StringUtil.changeForLog("The premisesId is null"));
                    continue;
                }
                licDocumentDto.setLicPremId(premisesId);
                licDocumentRelationDto.setLicDocumentDto(licDocumentDto);
                licDocumentRelationDtos.add(licDocumentRelationDto);
            }
        }
        log.info(StringUtil.changeForLog("The getLicDocumentRelationDto end ..."));
        return licDocumentRelationDtos;
    }

    private String getPremisesByAppPremCorreId(List<AppPremisesCorrelationDto> appPremisesCorrelationDtos, String appPremCorreId) {
        String result = null;
        if (StringUtil.isEmpty(appPremCorreId) || appPremisesCorrelationDtos == null || appPremisesCorrelationDtos.size() == 0) {
            return result;
        }
        for (AppPremisesCorrelationDto appPremisesCorrelationDto : appPremisesCorrelationDtos) {
            if (appPremCorreId.equals(appPremisesCorrelationDto.getId())) {
                result = appPremisesCorrelationDto.getAppGrpPremId();
                break;
            }
        }
        return result;

    }

    private AppPremisesCorrelationDto getAppPremCorrecId(List<AppPremisesCorrelationDto> appPremisesCorrelationDtos, String premisesId) {
        AppPremisesCorrelationDto result = null;
        if (appPremisesCorrelationDtos == null || appPremisesCorrelationDtos.size() == 0 || StringUtil.isEmpty(premisesId)) {
            return null;
        }
        for (AppPremisesCorrelationDto appPremisesCorrelationDto : appPremisesCorrelationDtos) {
            if (premisesId.equals(appPremisesCorrelationDto.getAppGrpPremId())) {
                result = appPremisesCorrelationDto;
                break;
            }
        }
        return result;
    }

    private  String getLicenceStatus(LicenceDto licenceDto,ApplicationGroupDto applicationGroupDto){
        log.info(StringUtil.changeForLog("The  getLicenceStatus start ..."));
        String result =  ApplicationConsts.LICENCE_STATUS_ACTIVE;;
        if( applicationGroupDto == null){
            log.info("----getLicenceStatus applicationGroupDto is null  ------");
            return result;
        }
        Date effectiveDate = applicationGroupDto.getEffectDate();
        Date startDate = licenceDto.getStartDate();
        Date today=new Date();
        log.info(StringUtil.changeForLog("The effectiveBoolean is -->:"+effectiveDate));
        log.info(StringUtil.changeForLog("The startDate is -->:"+startDate));
        log.info(StringUtil.changeForLog("The today is -->:"+today));
        if(effectiveDate != null){
            if(today.before(effectiveDate)){
                result = ApplicationConsts.LICENCE_STATUS_APPROVED;
            }else if(today.after(startDate)){
                result = ApplicationConsts.LICENCE_STATUS_ACTIVE;
            }else{
                result = ApplicationConsts.LICENCE_STATUS_APPROVED;
            }
        }else{
            if(today.before(startDate)){
                result = ApplicationConsts.LICENCE_STATUS_APPROVED;
            }else{
                result = ApplicationConsts.LICENCE_STATUS_ACTIVE;
            }
        }
        //0065635
        log.info(StringUtil.changeForLog("The result is -->:"+result));
        log.info(StringUtil.changeForLog("The  getLicenceStatus end ..."));
        return result;
    }
    private LicenceDto getLicenceDto(String svcName, String svcType, ApplicationGroupDto applicationGroupDto,
                                     AppPremisesRecommendationDto appPremisesRecommendationDto,
                                     LicenceDto originLicenceDto,
                                     ApplicationDto applicationDto,
                                     List<ApplicationDto> applicationDtos,
                                     boolean isGrpLic) {
        log.info(StringUtil.changeForLog("The  getLicenceDto start ..."));
        LicenceDto licenceDto = new LicenceDto();
        licenceDto.setSvcName(svcName);
        if (!StringUtil.isEmpty(svcType)) {
            licenceDto.setSvcType(svcType);
        }
        String licenseeId = "";
        if(applicationGroupDto != null){
            Date effectiveDate = applicationGroupDto.getEffectDate();
            licenceDto.setEffectiveDate(effectiveDate);
            licenseeId = applicationGroupDto.getLicenseeId();
            String flag = "";
            if(applicationDto != null){
                flag = applicationDto.getGroupLicenceFlag();
                if(ApplicationConsts.APPLICATION_STATUS_TRANSFER_ORIGIN.equals(applicationDto.getStatus())){
                 log.info(StringUtil.changeForLog("This is the transfer origin application"+applicationDto.getApplicationNo()));
                 licenceDto.setIncreasedLicenceNo(true);
                }
            }
            log.info(StringUtil.changeForLog("The  getLicenceDto  licenseeId is " + licenseeId));
            log.info(StringUtil.changeForLog("The  getLicenceDto  flag is " + flag));
            if(!StringUtil.isEmpty(applicationGroupDto.getNewLicenseeId())&&
                    (ApplicationConsts.GROUP_LICENCE_FLAG_TRANSFER.equals(flag)
                    ||ApplicationConsts.GROUP_LICENCE_FLAG_All_TRANSFER.equals(flag))){
                licenseeId =  applicationGroupDto.getNewLicenseeId();
                log.info(StringUtil.changeForLog("The  getLicenceDto  newlicenseeId is " + licenseeId));
            }
        }

        if (applicationDto != null && originLicenceDto != null && ((ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equalsIgnoreCase(applicationDto.getApplicationType())) || ApplicationConsts.APPLICATION_TYPE_CESSATION.equalsIgnoreCase(applicationDto.getApplicationType()))) {
            log.info(StringUtil.changeForLog("The  getLicenceDto APPType is RFC ..."));
            licenceDto.setStartDate(originLicenceDto.getStartDate());
            licenceDto.setExpiryDate(originLicenceDto.getExpiryDate());
            licenceDto.setGrpLic(originLicenceDto.isGrpLic());
            licenceDto.setOriginLicenceId(originLicenceDto.getId());
            licenceDto.setMigrated(originLicenceDto.getMigrated());
            licenceDto.setMigratedDt(originLicenceDto.getMigratedDt());
            if (!applicationDto.isNeedNewLicNo()) {
                licenceDto.setLicenceNo(originLicenceDto.getLicenceNo());
                licenceDto.setVersion(originLicenceDto.getVersion() + 1);
            }else{
                licenceDto.setVersion(1);
            }
            licenceDto.setFeeRetroNeeded(originLicenceDto.isFeeRetroNeeded());
            licenceDto.setLicenseeId(licenseeId);
            //ceased    weilu
            if(ApplicationConsts.APPLICATION_TYPE_CESSATION.equalsIgnoreCase(applicationDto.getApplicationType())){
                try {
                    int premiseSize = 0 ;
                    List<ApplicationDto> listApplicationDtos = applicationClient.getAppDtosByAppGrpId(applicationDto.getAppGrpId()).getEntity();
                    if(!IaisCommonUtils.isEmpty(listApplicationDtos)){
                        for(ApplicationDto applicationDto1 :listApplicationDtos){
                            String status = applicationDto1.getStatus();
                            if(!ApplicationConsts.APPLICATION_STATUS_CESSATION_NOT_LICENCE.equals(status)){
                                premiseSize++;
                            }
                        }
                    }
                    String licenceNo = originLicenceDto.getLicenceNo();
                    log.info(StringUtil.changeForLog("============premiseSize=================="+premiseSize));
                    String s = hcsaLicenceClient.groupLicenceRunningNumber(licenceNo).getEntity();
                    String ceasedLicNo = systemBeLicClient.groupLicenceByGroupLicenceNo(licenceNo, s,premiseSize).getEntity();
                    licenceDto.setCesedLicNo(ceasedLicNo);
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                    log.info(StringUtil.changeForLog("============ceased licNo=================="));
                }
            }
        } else {
            if (applicationGroupDto != null) {
                Date startDate = null;
                Date expiryDate = null;
                if (applicationDto != null && originLicenceDto != null && ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(applicationDto.getApplicationType())) {
                    log.info(StringUtil.changeForLog("The  getLicenceDto APPType is Renew ..."));
                    //startDate
                    startDate = originLicenceDto.getExpiryDate();
                    log.info(StringUtil.changeForLog("The  getLicenceDto originLicenceDto expiryday is " + startDate));
                    startDate = DateUtils.addDays(startDate, 1);
                    log.info(StringUtil.changeForLog("The  getLicenceDto Renew startDate is " + startDate));
                    if (startDate == null) {
                        startDate = new Date();
                    }
                    //expiryDate
                    expiryDate = LicenceUtil.getExpiryDate(startDate, appPremisesRecommendationDto);
                    log.info(StringUtil.changeForLog("The  getLicenceDto Renew expiryDate is " + expiryDate));
                } else {
                    //startDate
                    Date paymentDt = applicationGroupDto.getPaymentDt();
                    log.info(StringUtil.changeForLog("The  getLicenceDto paymentDt is " + paymentDt));
                    Date ao3ApprovedDt = applicationGroupDto.getAo3ApprovedDt();
                    log.info(StringUtil.changeForLog("The  getLicenceDto ao3ApprovedDt is " + ao3ApprovedDt));
                    Date recommendDate = null;
                    if (appPremisesRecommendationDto != null) {
                        recommendDate = appPremisesRecommendationDto.getRecomInDate();
                    }
                    log.info(StringUtil.changeForLog("The  getLicenceDto recommendDate is " + recommendDate));
                    startDate = LicenceUtil.getLasterDate(paymentDt, ao3ApprovedDt, recommendDate);
                    log.debug(StringUtil.changeForLog("The getLicenceDto new startDate is -->:" + startDate));
                    if (startDate == null) {
                        startDate = new Date();
                    }
                    //expiryDate
                    expiryDate = LicenceUtil.getExpiryDate(startDate, appPremisesRecommendationDto);
                    log.info(StringUtil.changeForLog("The  getLicenceDto new expiryDate is " + expiryDate));
                    if(applicationDto != null){
                        licenceDto.setApplicationNo(applicationDto.getApplicationNo());
                        //relLicenceNo
                        String relLicenceNo = applicationDto.getRelLicenceNo();
                        log.debug(StringUtil.changeForLog("The getLicenceDto new relLicenceNo is -->:" + relLicenceNo));
                        String alignLicenceNo = applicationDto.getAlignLicenceNo();
                        log.debug(StringUtil.changeForLog("The getLicenceDto new alignLicenceNo is -->:" + alignLicenceNo));
                        String baseApplicationNo = applicationDto.getBaseApplicationNo();
                        log.debug(StringUtil.changeForLog("The getLicenceDto new baseApplicationNo is -->:" + baseApplicationNo));
                        if (!StringUtil.isEmpty(relLicenceNo)) {
                            LicenceDto relLicenceDto = licenceService.getLicenceDtoByLicNo(relLicenceNo);
                            if (relLicenceDto != null) {
                                Date relExpiryDate = relLicenceDto.getExpiryDate();
                                log.info(StringUtil.changeForLog("The relExpiryDate is -->:" + relExpiryDate));
                                if (expiryDate.after(relExpiryDate)) {
                                    expiryDate = relExpiryDate;
                                }
                                log.debug(StringUtil.changeForLog("The getLicenceDto new relLicenceDto.getId() is -->:" + relLicenceDto.getId()));
                                licenceDto.setRelLicenceId(relLicenceDto.getId());
                            }else{
                                log.debug(StringUtil.changeForLog("This relLicenceNo can not get the relLicenceDto -->:"+relLicenceNo));
                            }
                        }
                        //alignLicenceNo
                        else if (!StringUtil.isEmpty(alignLicenceNo)) {
                            LicenceDto alignLicenceDto = licenceService.getLicenceDtoByLicNo(alignLicenceNo);
                            if (alignLicenceDto != null) {
                                Date alignExpiryDate = alignLicenceDto.getExpiryDate();
                                log.info(StringUtil.changeForLog("The alignExpiryDate is -->:" + alignExpiryDate));
                                if (expiryDate.after(alignExpiryDate)){
                                    expiryDate = alignExpiryDate;
                                }
                            }else{
                                log.debug(StringUtil.changeForLog("This relLicenceNo can not get the relLicenceDto -->:"+relLicenceNo));
                            }
                        }//baseApplicationNo
                        else if (!StringUtil.isEmpty(baseApplicationNo)) {
                            licenceDto.setBaseApplicationNo(baseApplicationNo);
                        }

                    }else{
                        log.debug(StringUtil.changeForLog("Tha application is null ..."));
                    }
                }
                if(applicationDto != null){
                    String alignFlag = applicationDto.getAlignFlag();
                    log.debug(StringUtil.changeForLog("The getLicenceDto new alignFlag is -->:" + alignFlag));
                    //alignFlag
                    if (!StringUtil.isEmpty(alignFlag)) {
                        licenceDto.setAlignFlag(alignFlag);
                    }
                }
                log.info(StringUtil.changeForLog("The expiryDate is -->:" + expiryDate));

                licenceDto.setStartDate(startDate);
                licenceDto.setExpiryDate(expiryDate);
                //licenceDto.setEndDate(licenceDto.getExpiryDate());
                licenceDto.setGrpLic(isGrpLic);
                licenceDto.setLicenseeId(licenseeId);
            }
            int version = 1;
            if (originLicenceDto != null) {
                //appeal add cgo not need new licence no
                if(applicationDto!=null){
                    if(!applicationDto.isNeedNewLicNo()&&applicationDto.getOriginLicenceId()!=null){
                        //the new generated licences should increase the serial no. 69002
                        //licenceDto.setLicenceNo(originLicenceDto.getLicenceNo());
                        version=originLicenceDto.getVersion()+1;
                    }
                }
                licenceDto.setOriginLicenceId(originLicenceDto.getId());
                licenceDto.setMigrated(originLicenceDto.getMigrated());
                licenceDto.setMigratedDt(originLicenceDto.getMigratedDt());
            } else {
                licenceDto.setMigrated(0);
            }
            licenceDto.setVersion(version);
            licenceDto.setFeeRetroNeeded(false);

        }
        List<ApplicationDto> applicationDtos1 = IaisCommonUtils.genNewArrayList();

        if (applicationDtos != null) {
            applicationDtos1.addAll(applicationDtos);
        }else if (applicationDto != null) {
                applicationDtos1.add(applicationDto);
        }
        if(applicationDto != null && (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(applicationDto.getApplicationType()) ||
                ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationDto.getApplicationType()))
                && IaisEGPHelper.isActiveMigrated()
                && originLicenceDto.isMigrated()){
            licenceDto.setStatus(originLicenceDto.getStatus());
        }else{
            //status
            licenceDto.setStatus(getLicenceStatus(licenceDto,applicationGroupDto));
        }

        licenceDto.setApplicationDtos(applicationDtos1);
        log.info(StringUtil.changeForLog("The  licenceDto.getLicenceNo() is -->:"+licenceDto.getLicenceNo()));
        log.info(StringUtil.changeForLog("The  getLicenceDto end ..."));
        return licenceDto;
    }

    private LicenseeDto getOrganizationIdBylicenseeId(String licenseeId) {
        log.info(StringUtil.changeForLog("The  getOrganizationIdBylicenseeId start ..."));
        //todo:get the organizationid , if do not exist need create the Organizaton.
        String organizationId = "29ABCF6D-770B-EA11-BE7D-000C29F371DC";
        LicenseeDto licenseeDto = null;

        if (!StringUtil.isEmpty(licenseeId)) {
            licenseeDto = inspEmailService.getLicenseeDtoById(licenseeId);
            if (licenseeDto != null) {
                organizationId = licenseeDto.getOrganizationId();
                if (StringUtil.isEmpty(organizationId)) {
                    licenseeDto.setOrganizationId(organizationId);
                }
            } else {
                log.debug(StringUtil.changeForLog("This licenseeId can not get he licensee -->:" + licenseeId));
            }
        } else {
            log.debug(StringUtil.changeForLog("The  licenseeId is null ..."));
        }
        if (licenseeDto == null) {
            licenseeDto = new LicenseeDto();
        }
        log.info(StringUtil.changeForLog("The  getOrganizationIdBylicenseeId end ..."));
        return licenseeDto;
    }


    private Integer getVersionByHciCode(String hciCode) {
        Integer result = 1;
        Integer version = hciCodeVersion.get(hciCode);
        if (version == null) {
            PremisesDto premisesDto = licenceService.getLatestVersionPremisesByHciCode(hciCode);
            if (premisesDto != null) {
                result = premisesDto.getVersion() + 1;
            }
        } else {
            result = version + 1;
        }
        hciCodeVersion.put(hciCode, result);
        return result;
    }

    //getAllServiceId
    public List<String> getAllServiceId(List<ApplicationLicenceDto> applicationLicenceDtos) {
        log.debug(StringUtil.changeForLog("The getAllServiceId is start ..."));
        List<String> result = IaisCommonUtils.genNewArrayList();
        Set<String> set = new HashSet();
        if (IaisCommonUtils.isEmpty(applicationLicenceDtos)) {
            return result;
        }
        for (ApplicationLicenceDto applicationLicenceDto : applicationLicenceDtos) {
            if (applicationLicenceDto != null) {
                List<ApplicationListDto> applicationListDtoList = applicationLicenceDto.getApplicationListDtoList();
                if (applicationListDtoList != null && applicationListDtoList.size() > 0) {
                    for (ApplicationListDto applicationListDto : applicationListDtoList) {
                        if (applicationListDto != null) {
                            ApplicationDto applicationDto = applicationListDto.getApplicationDto();
                            set.add(applicationDto.getServiceId());
                        } else {
                            log.warn(StringUtil.changeForLog("There is the null for the ApplicationDto"));
                        }
                    }
                } else {
                    log.warn(StringUtil.changeForLog("There is the null in the List<ApplicationListDto>"));
                }
            } else {
                log.warn(StringUtil.changeForLog("There is the null in the List<ApplicationLicenceDto>"));
            }
        }
        result.addAll(set);
        log.debug(StringUtil.changeForLog("The getAllServiceId is start ..."));
        return result;
    }

    private HcsaServiceDto getHcsaServiceDtoByServiceId(List<HcsaServiceDto> hcsaServiceDtos, String serviceId) {
        HcsaServiceDto result = null;
        if (StringUtil.isEmpty(serviceId)) {
            return result;
        }
        for (HcsaServiceDto hcsaServiceDto : hcsaServiceDtos) {
            if (serviceId.equals(hcsaServiceDto.getId())) {
                result = hcsaServiceDto;
                break;
            }
        }
        return result;
    }

    private void sendSMS(String msgId, String licenseeId, Map<String, Object> msgInfoMap){
        //MsgTemplateDto msgTemplateDto = msgTemplateClient.getMsgTemplate(msgId).getEntity();
        //String templateMessageByContent = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getMessageContent(), msgInfoMap);
        String templateMessageByContent = "send sms";
        SmsDto smsDto = new SmsDto();
        smsDto.setContent(templateMessageByContent);
        smsDto.setSender(mailSender);
        smsDto.setOnlyOfficeHour(true);
        String refNo = inboxMsgService.getMessageNo();
        List<String> recipts = IaisEGPHelper.getLicenseeMobiles(licenseeId);
        if (!IaisCommonUtils.isEmpty(recipts)) {
            emailClient.sendSMS(recipts,smsDto,refNo);
        }
    }

    private void sendEmailAndSms(ApplicationDto applicationDto, LicenceDto licenceDto,
                                 LicenseeDto oldLicenseeDto, LicenceDto originLicenceDto, String serviceId,AppPremisesRecommendationDto recommendationDto) {
        log.info(StringUtil.changeForLog("The sendEmailAndSms start ..."));
        String applicationNo = applicationDto.getApplicationNo();
        String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
        String corpPassUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + "/main-web/eservice/INTERNET/FE_Landing";
        HcsaServiceDto svcDto = hcsaConfigClient.getHcsaServiceDtoByServiceId(applicationDto.getServiceId()).getEntity();
        List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
        svcCodeList.add(svcDto.getSvcCode());
        String licenceNo = licenceDto.getLicenceNo();
        if(originLicenceDto != null){
            licenceNo = originLicenceDto.getLicenceNo();
        }
        String licenseeId = licenceDto.getLicenseeId();
        String applicationTypeShow = MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_TYPE_RENEWAL);
        AppPremisesRecommendationDto inspectionRecommendation = null;
        if(recommendationDto != null){
            inspectionRecommendation = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(recommendationDto.getAppPremCorreId(), InspectionConstants.RECOM_TYPE_INSPCTION_FOLLOW_UP_ACTION).getEntity();
        }
        String msgId = "";
        LicenseeDto licenseeDto = organizationClient.getLicenseeDtoById(licenseeId).getEntity();
        ApplicationGroupDto applicationGroupDto = applicationGroupService.getApplicationGroupDtoById(applicationDto.getAppGrpId());
        OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(applicationGroupDto.getSubmitBy()).getEntity();
        if(orgUserDto != null){
            String applicantName = orgUserDto.getDisplayName();
            String organizationId = licenseeDto.getOrganizationId();
            OrganizationDto organizationDto = organizationClient.getOrganizationById(organizationId).getEntity();
            String appDate = Formatter.formatDate(new Date());
            String MohName = AppConsts.MOH_AGENCY_NAME;
            log.info(StringUtil.changeForLog("send notification applicantName : " + applicantName));
            //new application send email zhilin
            if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(applicationDto.getApplicationType())) {
//                applicationTypeShow = MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
//                Map<String, Object> map = IaisCommonUtils.genNewHashMap();
//                map.put("ApplicantName", applicantName);
//                map.put("ApplicationType", applicationTypeShow);
//                map.put("ApplicationNumber", applicationNo);
//                map.put("applicationDate", appDate);
//                map.put("licenceNumber", licenceNo);
//                map.put("isSpecial", "N");
//                map.put("isCorpPass", "N");
//                if(inspectionRecommendation != null){
//                    map.put("inInspection", "Y");
//                    map.put("inspectionText", inspectionRecommendation.getRemarks());
//                }else {
//                    map.put("inInspection", "N");
//                }
//                if(organizationDto != null){
//                    if(StringUtil.isEmpty(organizationDto.getUenNo())){
//                        map.put("isCorpPass", "Y");
//                        map.put("corpPassLink", corpPassUrl);
//                    }
//                }
//                map.put("systemLink", loginUrl);
//
//                map.put("createHyperlink", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_CREATE_LINK));
//                map.put("regulationLink", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_REGULATIONS_LINK));
//                map.put("link", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_LINK));
//                map.put("scdfLink", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_SCDF_LINK));
//                map.put("momLink", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_MOM_LINK));
//
//                map.put("phoneNumber", systemPhoneNumber);
//                map.put("emailAddress1", systemAddressOne);
//                map.put("emailAddress2", systemAddressTwo);
//                map.put("MOH_AGENCY_NAME", MohName);
//
//                try {
//                    String subject = "MOH HALP - Your "+ applicationTypeShow + ", "+ applicationNo +" is approved ";
//                    EmailParam emailParam = new EmailParam();
//                    emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_APPROVED_ID);
//                    emailParam.setTemplateContent(map);
//                    emailParam.setQueryCode(applicationNo);
//                    emailParam.setReqRefNum(applicationNo);
//                    emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
//                    emailParam.setRefId(applicationNo);
//                    emailParam.setSubject(subject);
//                    //send email
//                    log.info(StringUtil.changeForLog("send new application email"));
//                    notificationHelper.sendNotification(emailParam);
//                    log.info(StringUtil.changeForLog("send new application email end"));
//                    //send sms
//                    EmailParam smsParam = new EmailParam();
//                    smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_APPROVED_SMS_ID);
//                    smsParam.setSubject(subject);
//                    smsParam.setQueryCode(applicationNo);
//                    smsParam.setReqRefNum(applicationNo);
//                    smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
//                    smsParam.setRefId(applicationNo);
//                    log.info(StringUtil.changeForLog("send new application sms"));
//                    notificationHelper.sendNotification(smsParam);
//                    log.info(StringUtil.changeForLog("send new application sms end"));
//                    //send message
//                    EmailParam messageParam = new EmailParam();
//                    messageParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_APPROVED_MESSAGE_ID);
//                    messageParam.setTemplateContent(map);
//                    messageParam.setQueryCode(applicationNo);
//                    messageParam.setReqRefNum(applicationNo);
//                    messageParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
//                    messageParam.setRefId(applicationNo);
//                    messageParam.setSubject(subject);
//                    messageParam.setSvcCodeList(svcCodeList);
//                    log.info(StringUtil.changeForLog("send new application message"));
//                    notificationHelper.sendNotification(messageParam);
//                    log.info(StringUtil.changeForLog("send new application message end"));
//                }catch (Exception e){
//                    log.error(e.getMessage(), e);
//                }

                //zhilin
            } else if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(applicationDto.getApplicationType())) {
//                Map<String, Object> map = IaisCommonUtils.genNewHashMap();
//                map.put("ApplicantName", applicantName);
//                map.put("ApplicationType", applicationTypeShow);
//                map.put("ApplicationNumber", applicationNo);
//                map.put("applicationDate", appDate);
//                map.put("licenceNumber", licenceNo);
//                map.put("isSpecial", "N");
//                if(inspectionRecommendation != null){
//                    map.put("inInspection", "Y");
//                    map.put("inspectionText", inspectionRecommendation.getRemarks());
//                }else {
//                    map.put("inInspection", "N");
//                }
//                map.put("systemLink", loginUrl);
//
//                map.put("createHyperlink", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_CREATE_LINK));
//                map.put("regulationLink", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_REGULATIONS_LINK));
//                map.put("link", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_LINK));
//                map.put("scdfLink", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_SCDF_LINK));
//                map.put("momLink", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_MOM_LINK));
//                map.put("irasLink", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_IRAS_LINK));
//
//                map.put("phoneNumber", systemPhoneNumber);
//                map.put("emailAddress1", systemAddressOne);
//                map.put("emailAddress2", systemAddressTwo);
//                map.put("MOH_AGENCY_NAME", MohName);
//                try {
//                    String subject = "MOH HALP - Your "+ applicationTypeShow + ", "+ applicationNo +" is approved ";
//                    EmailParam emailParam = new EmailParam();
//                    emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_APPROVE);
//                    emailParam.setTemplateContent(map);
//                    emailParam.setQueryCode(applicationNo);
//                    emailParam.setReqRefNum(applicationNo);
//                    emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
//                    emailParam.setRefId(applicationNo);
//                    emailParam.setSubject(subject);
//                    //send email
//                    log.info(StringUtil.changeForLog("send renewal application email"));
//                    notificationHelper.sendNotification(emailParam);
//                    log.info(StringUtil.changeForLog("send renewal application email end"));
//                    //send sms
//                    EmailParam smsParam = new EmailParam();
//                    smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_APPROVE_SMS);
//                    smsParam.setSubject(subject);
//                    smsParam.setQueryCode(applicationNo);
//                    smsParam.setReqRefNum(applicationNo);
//                    smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
//                    smsParam.setRefId(applicationNo);
//                    log.info(StringUtil.changeForLog("send renewal application sms"));
//                    notificationHelper.sendNotification(smsParam);
//                    log.info(StringUtil.changeForLog("send renewal application sms end"));
//                    //send message
//                    EmailParam messageParam = new EmailParam();
//                    messageParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_APPROVE_MESSAGE);
//                    messageParam.setTemplateContent(map);
//                    messageParam.setQueryCode(applicationNo);
//                    messageParam.setReqRefNum(applicationNo);
//                    messageParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
//                    messageParam.setRefId(applicationNo);
//                    messageParam.setSubject(subject);
//                    messageParam.setSvcCodeList(svcCodeList);
//                    log.info(StringUtil.changeForLog("send renewal application message"));
//                    notificationHelper.sendNotification(messageParam);
//                    log.info(StringUtil.changeForLog("send renewal application message end"));
//                }catch (Exception e){
//                    log.error(e.getMessage(), e);
//                }
                //guying
            } else if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationDto.getApplicationType())) {
                //Send notification to transferor when licence transfer application is approve
//                sendApproveEmail(licenceDto, applicationNo, notifyMap, serviceId);

            }
            log.info(StringUtil.changeForLog("The sendEmailAndSms end ..."));
        }
    }

    @Setter
    @Getter
    static class GenerateResult {
        private boolean success;
        private String errorMessage;
        private LicenceGroupDto licenceGroupDto;
    }

    public void createCessLicence(ApplicationGroupDto applicationGroupDto,GenerateResult generalGenerateResult  ,GenerateResult groupGenerateResult){
        List<LicenceGroupDto> licenceGroupDtos = IaisCommonUtils.genNewArrayList();
        List<ApplicationGroupDto> success = IaisCommonUtils.genNewArrayList();
        List<Map<String, String>> fail = IaisCommonUtils.genNewArrayList();
        toDoResult(licenceGroupDtos, generalGenerateResult, groupGenerateResult, success, fail, applicationGroupDto);
        if (success.size() > 0) {
            updateExpiryDateByAlignFlag(licenceGroupDtos);
            updateExpiryDateByBaseApplicationNo(licenceGroupDtos);
            setOriginLicenceLicBaseSpecifiedCorrelationDtos(licenceGroupDtos);
            AuditTrailDto auditTrailDto = AuditTrailHelper.getCurrentAuditTrailDto();
            EventBusLicenceGroupDtos eventBusLicenceGroupDtos = new EventBusLicenceGroupDtos();
            String evenRefNum = String.valueOf(System.currentTimeMillis());
            eventBusLicenceGroupDtos.setEventRefNo(evenRefNum);
            eventBusLicenceGroupDtos.setLicenceGroupDtos(licenceGroupDtos);
            eventBusLicenceGroupDtos.setAuditTrailDto(auditTrailDto);
            licenceService.createSuperLicDto(eventBusLicenceGroupDtos);
            List<ApplicationDto> applicationDtos = getApplications(licenceGroupDtos);
            EventApplicationGroupDto eventApplicationGroupDto = new EventApplicationGroupDto();
            eventApplicationGroupDto.setEventRefNo(evenRefNum);
            eventApplicationGroupDto.setRollBackApplicationGroupDtos(success);
            eventApplicationGroupDto.setApplicationGroupDtos(updateStatusToGenerated(success));
            eventApplicationGroupDto.setRollBackApplicationDto(applicationDtos);
            eventApplicationGroupDto.setApplicationDto(updateApplicationStatusToGenerated(applicationDtos));
            eventApplicationGroupDto.setAuditTrailDto(auditTrailDto);
            applicationGroupService.updateEventApplicationGroupDto(eventApplicationGroupDto);
            }
    }

    private void sendMessage(String subject, String licenseeId, String templateMessageByContent, HashMap<String, String> maskParams, String serviceId){
        HcsaServiceDto serviceDto = HcsaServiceCacheHelper.getServiceById(serviceId);
        InterMessageDto interMessageDto = new InterMessageDto();
        interMessageDto.setSrcSystemId(AppConsts.MOH_IAIS_SYSTEM_INBOX_CLIENT_KEY);
        interMessageDto.setSubject(subject);
        interMessageDto.setMessageType(MessageConstants.MESSAGE_TYPE_NOTIFICATION);
        String refNo = inboxMsgService.getMessageNo();
        interMessageDto.setRefNo(refNo);
        if(serviceDto != null){
            interMessageDto.setService_id(serviceDto.getSvcCode()+'@');
        }
        interMessageDto.setUserId(licenseeId);
        interMessageDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        interMessageDto.setMsgContent(templateMessageByContent);
        interMessageDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        interMessageDto.setMaskParams(maskParams);
        inboxMsgService.saveInterMessage(interMessageDto);
    }
    private synchronized String seqNumber(Integer number ,Integer length){
        StringBuilder strB=new StringBuilder();
        String numStr = number.toString();
        char zero = "0".charAt(0);
        for(int i=0;i<length-numStr.length();i++){
            strB.append(zero);
        }
        strB.append(numStr);
        return strB.toString();
    }
}
