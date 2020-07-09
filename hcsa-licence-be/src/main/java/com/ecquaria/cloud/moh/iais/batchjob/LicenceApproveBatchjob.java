package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.risk.RiskConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.SmsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPersonnelExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremPhOpenPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcKeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPremisesScopeAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPremisesScopeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.EventApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.DocumentDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.EventBusLicenceGroupDtos;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.KeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.KeyPersonnelExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicDocumentDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicDocumentRelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicFeeGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicKeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremPhOpenPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesScopeAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesScopeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesScopeGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicSvcSpecificPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.SuperLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicInspectionGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremInspGrpCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MessageTemplateUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationGroupService;
import com.ecquaria.cloud.moh.iais.service.InboxMsgService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.LicenceService;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.util.LicenceUtil;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private InboxMsgService inboxMsgService;

    @Autowired
    private ApplicationGroupService applicationGroupService;
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    private EmailClient emailClient;
    @Autowired
    private MsgTemplateClient msgTemplateClient;
    @Autowired
    private InspEmailService inspEmailService;
    @Value("${iais.email.sender}")
    private String mailSender;

    private Map<String, Integer> hciCodeVersion = new HashMap();
    private Map<String, Integer> keyPersonnelVersion = IaisCommonUtils.genNewHashMap();

    public void doBatchJob(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("The LicenceApproveBatchjob is start ..."));
        int day = systemParamConfig.getLicGenDay();
        //get can Generate Licence
        List<ApplicationLicenceDto> applicationLicenceDtos = licenceService.getCanGenerateApplications(day);
        if (applicationLicenceDtos == null || applicationLicenceDtos.size() == 0) {
            log.debug(StringUtil.changeForLog("This time do not have need Generate Licences."));
            return;
        }
        //get the all use serviceCode.
        List<String> serviceIds = getAllServiceId(applicationLicenceDtos);
        List<HcsaServiceDto> hcsaServiceDtos = licenceService.getHcsaServiceById(serviceIds);
        if (hcsaServiceDtos == null || hcsaServiceDtos.size() == 0) {
            log.error(StringUtil.changeForLog("This serviceIds can not get the HcsaServiceDto -->:" + serviceIds));
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
                        log.error(StringUtil.changeForLog("This  applicaiton group  have error -- >" + applicationGroupDto.getGroupNo()));
                        log.error(exception.getMessage(), exception);
                    }
                    toDoResult(licenceGroupDtos, generalGenerateResult, groupGenerateResult, success, fail, applicationGroupDto);
                    if (success.size() > 0) {
                        //update baseApplicationNo expiry Date
                        updateExpiryDateByBaseApplicationNo(licenceGroupDtos);
                        //
                        AuditTrailDto auditTrailDto = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTRANET);
                        EventBusLicenceGroupDtos eventBusLicenceGroupDtos = new EventBusLicenceGroupDtos();
                        String evenRefNum = String.valueOf(System.currentTimeMillis());
                        eventBusLicenceGroupDtos.setEventRefNo(evenRefNum);
                        eventBusLicenceGroupDtos.setLicenceGroupDtos(licenceGroupDtos);
                        eventBusLicenceGroupDtos.setAuditTrailDto(auditTrailDto);
                        //step1 create Licence to BE DB
                        licenceService.createSuperLicDto(eventBusLicenceGroupDtos);

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
                        applicationGroupService.updateEventApplicationGroupDto(eventApplicationGroupDto);

                    }

                }

            }
        }
        log.debug(StringUtil.changeForLog("The LicenceApproveBatchjob is end ..."));
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

    private LicenceDto getBaseIdForApplicationNo(List<SuperLicDto> superLicDtos,String baseApplicationNo){
        log.info(StringUtil.changeForLog("The getBaseIdForApplicationNo  start ..."));
        LicenceDto result = null;
        if(!StringUtil.isEmpty(baseApplicationNo) && !IaisCommonUtils.isEmpty(superLicDtos)){
            for (SuperLicDto superLicDto : superLicDtos){
                LicenceDto licenceDto = superLicDto.getLicenceDto();
                String svcType = licenceDto.getSvcType();
                String applicationNo = licenceDto.getApplicationNo();
                if(!StringUtil.isEmpty(svcType) && ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(svcType)
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
            log.error(StringUtil.changeForLog("The sepaApplication the applicationListDtoList is null"));
        }
        log.info(StringUtil.changeForLog("The sepaApplication is end ..."));
        return result;
    }

    private List<ApplicationDto> updateApplicationStatusToGenerated(List<ApplicationDto> applicationDtos) {
        List<ApplicationDto> result = IaisCommonUtils.genNewArrayList();
        if (IaisCommonUtils.isEmpty(applicationDtos)) {
            return result;
        }
        for (ApplicationDto applicationDto : applicationDtos) {
            applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_LICENCE_GENERATED);
            result.add(applicationDto);
        }
        return result;
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
                    log.error(StringUtil.changeForLog("The error is -->:" + value));
                }
            } else if (!isGroupSuccess) {
                Map<String, String> error = new HashMap();
                error.put(applicationGroupDto.getGroupNo(), groupResult.getErrorMessage());
                fail.add(error);
                for (Map.Entry<String, String> ent : error.entrySet()) {
                    String value = ent.getValue();
                    log.error(StringUtil.changeForLog("The error is -->:" + value));
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
                    log.error(StringUtil.changeForLog("The error is -->:" + value));
                }
            }
        }

    }

    private Map<String, List<ApplicationListDto>> tidyAppForGroupLicence(List<ApplicationListDto> applicationListDtoList) {
        Map<String, List<ApplicationListDto>> result = IaisCommonUtils.genNewHashMap();
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
                                log.error(StringUtil.changeForLog(errorMsg));
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
                LicenceDto originLicenceDto = deleteOriginLicenceDto(originLicenceId);
                log.info(StringUtil.changeForLog("The applicationType is -->:"+ApplicationConsts.APPLICATION_TYPE_RENEWAL));
                if(!ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(firstApplicationDto.getApplicationType())){
                    superLicDto.setOriginLicenceDto(originLicenceDto);
                }
                //create licence
//                String licenceNo = null;
//                if (firstApplicationDto.isNeedNewLicNo()) {
//                    licenceNo = licenceService.getGroupLicenceNo(hcsaServiceDto.getSvcCode(), appPremisesRecommendationDto, originLicenceId, applicationDtos.size());
//                }
//                log.debug(StringUtil.changeForLog("The licenceNo is -->;" + licenceNo));
//                if (StringUtil.isEmpty(licenceNo) && firstApplicationDto.isNeedNewLicNo()) {
//                    errorMessage = "The licenceNo is null .-->:" + hcsaServiceDto.getSvcCode() + ":" + applicationListDtos.size();
//                    break;
//                }

                LicenceDto licenceDto = getLicenceDto(hcsaServiceDto.getSvcName(), null, applicationGroupDto, appPremisesRecommendationDto,
                        originLicenceDto, firstApplicationDto, applicationDtos, true);
                licenceDto.setSvcCode(hcsaServiceDto.getSvcCode());
                licenceDto.setPremiseSize(applicationDtos.size());
                superLicDto.setLicenceDto(licenceDto);
                //if PostInspNeeded send email
                if (isPostInspNeeded == Integer.parseInt(AppConsts.YES)) {
                    sendEmailInspection(licenceDto);
                }
                //
                List<PremisesGroupDto> premisesGroupDtos = IaisCommonUtils.genNewArrayList();
                List<LicAppCorrelationDto> licAppCorrelationDtos = IaisCommonUtils.genNewArrayList();
                List<LicDocumentRelationDto> licDocumentRelationDtos = IaisCommonUtils.genNewArrayList();
                List<PersonnelsDto> personnelsDtos = IaisCommonUtils.genNewArrayList();
                for (ApplicationListDto applicationListDto : applicationListDtos) {
                    ApplicationDto applicationDto = applicationListDto.getApplicationDto();
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

                    List<PremisesGroupDto> premisesGroupDtos1 = getPremisesGroupDto(applicationLicenceDto, appGrpPremisesEntityDtos, appPremisesCorrelationDtos, appSvcPremisesScopeDtos,
                            appSvcPremisesScopeAllocationDtos, hcsaServiceDto, organizationId, isPostInspNeeded);
                    if (!IaisCommonUtils.isEmpty(premisesGroupDtos1)) {
                        premisesGroupDtos.addAll(premisesGroupDtos1);
                    }
                    //create key_personnel key_personnel_ext lic_key_personnel
                    List<AppGrpPersonnelDto> appGrpPersonnelDtos = applicationListDto.getAppGrpPersonnelDtos();
                    List<AppGrpPersonnelExtDto> appGrpPersonnelExtDtos = applicationListDto.getAppGrpPersonnelExtDtos();
                    List<AppSvcKeyPersonnelDto> appSvcKeyPersonnelDtos = applicationListDto.getAppSvcKeyPersonnelDtos();
                    if (!IaisCommonUtils.isEmpty(appSvcKeyPersonnelDtos)) {
                        List<PersonnelsDto> personnelsDto1s = getPersonnelsDto(appGrpPersonnelDtos, appGrpPersonnelExtDtos, appSvcKeyPersonnelDtos, organizationId);
                        if (personnelsDtos == null) {
                            errorMessage = "There is Error for AppGrpPersonnel -->: " + applicationDto.getApplicationNo();
                            break;
                        }
                        personnelsDtos.addAll(personnelsDto1s);
                    }

                    //create the lic_app_correlation
                    LicAppCorrelationDto licAppCorrelationDto = new LicAppCorrelationDto();
                    licAppCorrelationDto.setApplicationId(applicationListDto.getApplicationDto().getId());
                    licAppCorrelationDtos.add(licAppCorrelationDto);

                    //create the document and lic_document from the primary doc.
                    List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = applicationListDto.getAppGrpPrimaryDocDtos();
                    List<AppSvcDocDto> appSvcDocDtos = applicationListDto.getAppSvcDocDtos();
                    List<LicDocumentRelationDto> licDocumentRelationDto1s = getLicDocumentRelationDto(appGrpPrimaryDocDtos,
                            appSvcDocDtos, appPremisesCorrelationDtos, premisesGroupDtos);
                    licDocumentRelationDtos.addAll(licDocumentRelationDto1s);

                    //create the lic_fee_group_item
                    //do not need create in the Dto
                    //todo:lic_base_specified_correlation
                    //
                    //part premises
                    boolean isPartPremises = applicationDto.isPartPremises();
                    log.info(StringUtil.changeForLog("The generateGroupLicence isPartPremises is -->:" + isPartPremises));
                    boolean rfcTypeFlag = ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType);
                    if (rfcTypeFlag && isPartPremises) {
                        String everyOriginLicenceId = applicationDto.getOriginLicenceId();
                        log.info(StringUtil.changeForLog("The generateGroupLicence everyOriginLicenceId is -->:" + everyOriginLicenceId));
                        if (!StringUtil.isEmpty(everyOriginLicenceId)) {
                            List<PremisesGroupDto> premisesGroupDtoList = licenceService.getPremisesGroupDtoByOriginLicenceId(everyOriginLicenceId);
                            String msg = addPremisesGroupDtos(premisesGroupDtos, premisesGroupDtoList, applicationListDto);
                            if (!StringUtil.isEmpty(msg)) {
                                errorMessage = msg;
                                break;
                            }
                            // addDocumentToList(premisesGroupDtoList,licDocumentRelationDtos);
                        } else {
                            log.error(StringUtil.changeForLog("This Appno do not have the OriginLicenceId -- >" + applicationDto.getApplicationNo()));
                        }
                    }

                    if (applicationDto != null && originLicenceDto != null) {
                        sendEmailAndSms(applicationDto, licenceDto, oldLicenseeDto, originLicenceDto, serviceId);
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

    private void sendApproveEmail(LicenceDto licenceDto, String applicationNo, Map<String, Object> map, String serviceId) {
        String MAILMPLATEID_APPROVE = "19CAC7AB-E798-EA11-BE82-000C29F371DC";
        InspectionEmailTemplateDto rejectTemplateDto = inspEmailService.loadingEmailTemplate(MAILMPLATEID_APPROVE);
        EmailDto email = new EmailDto();
        String mesContext = null;
        try {
            mesContext = MsgUtil.getTemplateMessageByContent(rejectTemplateDto.getMessageContent(), map);
            email.setReqRefNum(applicationNo);
            email.setSubject(rejectTemplateDto.getSubject());
            email.setContent(mesContext);
            email.setSender(mailSender);
            email.setClientQueryCode(applicationNo);
            email.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenceDto.getLicenseeId()));
            licenceService.sendEmail(email);
            HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(serviceId);
            if(hcsaServiceDto!=null){
                String messageNo = inboxMsgService.getMessageNo();
                InterMessageDto interMessageDto = MessageTemplateUtil.getInterMessageDto(MessageConstants.MESSAGE_SUBJECT_REQUEST_FOR_INFORMATION, MessageConstants.MESSAGE_TYPE_ACTION_REQUIRED,
                        messageNo, hcsaServiceDto.getSvcCode()+'@', mesContext, licenceDto.getLicenseeId(), IaisEGPHelper.getCurrentAuditTrailDto());
                HashMap<String, String> mapParam = IaisCommonUtils.genNewHashMap();
                mapParam.put("appNo", applicationNo);
                interMessageDto.setMaskParams(mapParam);
                inboxMsgService.saveInterMessage(interMessageDto);
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void newApplicationApproveSendEmail(LicenceDto licenceDto, String applicationNo, String licenceNo, String loginUrl, boolean isNew, String uenNo) {
        Map<String, Object> tempMap = IaisCommonUtils.genNewHashMap();
        tempMap.put("MOH_AGENCY_NAME", AppConsts.MOH_AGENCY_NAME);
        tempMap.put("loginUrl", loginUrl);
        tempMap.put("licenceNumber", licenceNo);
        tempMap.put("applicationNumber", applicationNo);
        tempMap.put("isNewApplication", null);
        if (isNew) {
            tempMap.put("isNewApplication", "Y");
            tempMap.put("UEN_NO", uenNo);
        }
        String subject = " " + applicationNo + " is Approved ";
        sendEmailHelper(tempMap, MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_APPROVED_ID, subject, licenceDto.getLicenseeId(), licenceDto.getId());
    }


    private LicenceDto deleteOriginLicenceDto(String organizationId) {
        LicenceDto result = null;
        if (!StringUtil.isEmpty(organizationId)) {
            result = licenceService.getLicenceDto(organizationId);
            if (result != null) {
                result.setStatus(ApplicationConsts.LICENCE_STATUS_IACTIVE);
                log.info(StringUtil.changeForLog("The generateGroupLicence everyOriginLicenceId is --> active: " + organizationId));
            }else {
                result = licenceService.getCeasedGroupLicDto(organizationId);
                log.info(StringUtil.changeForLog("The generateGroupLicence everyOriginLicenceId is --> ceased:" + organizationId));
            }
        }
        return result;
    }

    private List<ApplicationDto> getApplicationDtos(List<ApplicationListDto> applicationListDtos) {
        List<ApplicationDto> result = IaisCommonUtils.genNewArrayList();
        if (applicationListDtos != null && applicationListDtos.size() > 0) {
            for (ApplicationListDto applicationListDto : applicationListDtos) {
                result.add(applicationListDto.getApplicationDto());
            }
        }
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
                //create lic_premises
                List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = applicationListDto.getAppPremisesCorrelationDtos();
                //create LicPremisesScopeDto
                List<AppSvcPremisesScopeDto> appSvcPremisesScopeDtos = applicationListDto.getAppSvcPremisesScopeDtos();
                List<AppSvcPremisesScopeAllocationDto> appSvcPremisesScopeAllocationDtos = applicationListDto.getAppSvcPremisesScopeAllocationDtos();

                List<PremisesGroupDto> premisesGroupDtos = getPremisesGroupDto(applicationLicenceDto, appGrpPremisesEntityDtos, appPremisesCorrelationDtos, appSvcPremisesScopeDtos,
                        appSvcPremisesScopeAllocationDtos, hcsaServiceDto, organizationId, isPostInspNeeded);
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

//                    //create licence
//                    if (applicationDto.isNeedNewLicNo()) {
//                        licenceNo = licenceService.getLicenceNo(premisesGroupDto.getPremisesDto().getHciCode(), hcsaServiceDto.getSvcCode(), appPremisesRecommendationDto);
//                    }
//                    log.debug(StringUtil.changeForLog("The licenceNo is -->;" + licenceNo));
//                    if (StringUtil.isEmpty(licenceNo) && applicationDto.isNeedNewLicNo()) {
//                        errorMessage = "The licenceNo is null .-->:" + premisesGroupDto.getPremisesDto().getHciCode() + ":" + hcsaServiceDto.getSvcCode();
//                        break;
//                    }
                }
                String originLicenceId = applicationDto.getOriginLicenceId();
                LicenceDto originLicenceDto = deleteOriginLicenceDto(originLicenceId);
                log.info(StringUtil.changeForLog("The applicationType is -->:"+ApplicationConsts.APPLICATION_TYPE_RENEWAL));
                if(!ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(applicationDto.getApplicationType())){
                    superLicDto.setOriginLicenceDto(originLicenceDto);
                }
                LicenceDto licenceDto = getLicenceDto(hcsaServiceDto.getSvcName(), hcsaServiceDto.getSvcType(), applicationGroupDto, appPremisesRecommendationDto,
                        originLicenceDto, applicationDto, null, false);
                licenceDto.setSvcCode(hcsaServiceDto.getSvcCode());
                superLicDto.setLicenceDto(licenceDto);

                //create the lic_app_correlation
                List<LicAppCorrelationDto> licAppCorrelationDtos = IaisCommonUtils.genNewArrayList();
                LicAppCorrelationDto licAppCorrelationDto = new LicAppCorrelationDto();
                licAppCorrelationDto.setApplicationId(applicationDto.getId());
                licAppCorrelationDtos.add(licAppCorrelationDto);
                superLicDto.setLicAppCorrelationDtos(licAppCorrelationDtos);

                //create the document and lic_document from the primary doc.
                List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = applicationListDto.getAppGrpPrimaryDocDtos();
                List<AppSvcDocDto> appSvcDocDtos = applicationListDto.getAppSvcDocDtos();
                List<LicDocumentRelationDto> licDocumentRelationDtos = getLicDocumentRelationDto(appGrpPrimaryDocDtos,
                        appSvcDocDtos, appPremisesCorrelationDtos, premisesGroupDtos);
                superLicDto.setLicDocumentRelationDto(licDocumentRelationDtos);

                //create key_personnel key_personnel_ext lic_key_personnel
                List<AppGrpPersonnelDto> appGrpPersonnelDtos = applicationListDto.getAppGrpPersonnelDtos();
                List<AppGrpPersonnelExtDto> appGrpPersonnelExtDtos = applicationListDto.getAppGrpPersonnelExtDtos();
                List<AppSvcKeyPersonnelDto> appSvcKeyPersonnelDtos = applicationListDto.getAppSvcKeyPersonnelDtos();
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
                if (isPostInspNeeded == Integer.parseInt(AppConsts.YES)) {
                    sendEmailInspection(licenceDto);
                }
                sendEmailAndSms(applicationDto, licenceDto, oldLicenseeDto, originLicenceDto, serviceId);
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


    private List<PremisesGroupDto> getPremisesGroupDto(ApplicationLicenceDto applicationLicenceDto,
                                                       List<AppGrpPremisesEntityDto> appGrpPremisesEntityDtos,
                                                       List<AppPremisesCorrelationDto> appPremisesCorrelationDtos,
                                                       List<AppSvcPremisesScopeDto> appSvcPremisesScopeDtos,
                                                       List<AppSvcPremisesScopeAllocationDto> appSvcPremisesScopeAllocationDtos,
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
            //premises
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
                appGrpPremisesEntityDto.setHciCode(hciCode);
            }
            PremisesDto premisesDto = MiscUtil.transferEntityDto(appGrpPremisesEntityDto, PremisesDto.class);
            premisesDto.setHciCode(hciCode);
            premisesDto.setVersion(getVersionByHciCode(hciCode));
            premisesDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            premisesDto.setOrganizationId(organizationId);
            List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodDtos = appGrpPremisesEntityDto.getAppPremPhOpenPeriodDtoList();
            List<LicPremPhOpenPeriodDto> licPremPhOpenPeriodDtos = IaisCommonUtils.genNewArrayList();
            if (!IaisCommonUtils.isEmpty(appPremPhOpenPeriodDtos)) {
                for (AppPremPhOpenPeriodDto appPremPhOpenPeriodDto : appPremPhOpenPeriodDtos) {
                    LicPremPhOpenPeriodDto licPremPhOpenPeriodDto = MiscUtil.transferEntityDto(appPremPhOpenPeriodDto, LicPremPhOpenPeriodDto.class);
                    licPremPhOpenPeriodDto.setPremId(null);
                    licPremPhOpenPeriodDtos.add(licPremPhOpenPeriodDto);
                }
            }
            premisesDto.setLicPremPhOpenPeriodDtos(licPremPhOpenPeriodDtos);
            premisesGroupDto.setPremisesDto(premisesDto);
            //create lic_premises
            String premisesId = appGrpPremisesEntityDto.getId();
            String appPremCorrecId = getAppPremCorrecId(appPremisesCorrelationDtos, premisesId);
            if (StringUtil.isEmpty(appPremCorrecId)) {
                continue;
            }
            AppPremisesRecommendationDto appPremisesRecommendationDto = licenceService.getTcu(appPremCorrecId);
            LicPremisesDto licPremisesDto = new LicPremisesDto();
            licPremisesDto.setPremisesId(premisesId);
            licPremisesDto.setIsPostInspNeeded(isPostInspNeeded);
            if (appPremisesRecommendationDto == null) {
                licPremisesDto.setIsTcuNeeded(Integer.valueOf(AppConsts.NO));
            } else {
                licPremisesDto.setIsTcuNeeded(Integer.valueOf(AppConsts.YES));
                licPremisesDto.setTcuDate(appPremisesRecommendationDto.getRecomInDate());
            }
            premisesGroupDto.setLicPremisesDto(licPremisesDto);
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
            List<AppSvcPremisesScopeDto> appSvcPremisesScopeDtoList = getAppSvcPremisesScopeDtoByCorrelationId(appSvcPremisesScopeDtos, appPremCorrecId);
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
                        LicPremisesScopeAllocationDto licPremisesScopeAllocationDto = new LicPremisesScopeAllocationDto();
                        licPremisesScopeAllocationDto.setLicCgoId(appSvcPremisesScopeAllocationDto.getAppSvcKeyPsnId());
                        licPremisesScopeGroupDto.setLicPremisesScopeAllocationDto(licPremisesScopeAllocationDto);
                    } else {
                        log.info(StringUtil.changeForLog("this appSvcPremisesScopeDto.getId() do not have the AppSvcPremisesScopeAllocationDto -->:" + appSvcPremisesScopeDto.getId()));
                    }
                    licPremisesScopeGroupDtoList.add(licPremisesScopeGroupDto);
                }
                premisesGroupDto.setLicPremisesScopeGroupDtoList(licPremisesScopeGroupDtoList);
            } else {
                log.info(StringUtil.changeForLog("This appPremCorrecId can not get the AppSvcPremisesScopeDto -->:" + appPremCorrecId));
            }
            reuslt.add(premisesGroupDto);
        }
        log.info(StringUtil.changeForLog("The licence Generate getPremisesGroupDto end ..."));
        return reuslt;
    }


    private List<AppSvcPremisesScopeDto> getAppSvcPremisesScopeDtoByCorrelationId(List<AppSvcPremisesScopeDto> appSvcPremisesScopeDtos, String appPremCorrecId) {
        List<AppSvcPremisesScopeDto> result = IaisCommonUtils.genNewArrayList();
        if (IaisCommonUtils.isEmpty(appSvcPremisesScopeDtos) || StringUtil.isEmpty(appPremCorrecId)) {
            return result;
        }

        for (AppSvcPremisesScopeDto appSvcPremisesScopeDto : appSvcPremisesScopeDtos) {
            if (appSvcPremisesScopeDto != null && appPremCorrecId.equals(appSvcPremisesScopeDto.getAppPremCorreId())) {
                result.add(appSvcPremisesScopeDto);
            }
        }
        return result;
    }

    private LicFeeGroupDto getLicFeeGroupDto(String amount) {
        LicFeeGroupDto licFeeGroupDto = new LicFeeGroupDto();
        licFeeGroupDto.setFeeAmount(amount);
        return licFeeGroupDto;
    }

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
            keyPersonnelDto.setVersion(getKeyPersonnelVersion(keyPersonnelDto.getIdNo(), organizationId));
            //todo: controller status
            keyPersonnelDto.setStatus("active");
            //: controller the Organization
            keyPersonnelDto.setOrganizationId(organizationId);
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

    private Integer getKeyPersonnelVersion(String idNo, String orgId) {
        Integer result = 1;
        if (StringUtil.isEmpty(idNo) || StringUtil.isEmpty(orgId)) {
            return result;
        }
        Integer version = keyPersonnelVersion.get(idNo + orgId);
        if (version == null) {
            KeyPersonnelDto keyPersonnelDto = licenceService.getLatestVersionKeyPersonnelByIdNoAndOrgId(idNo, orgId);
            if (keyPersonnelDto != null) {
                result = keyPersonnelDto.getVersion() + 1;
            }
        } else {
            result = version + 1;
        }
        keyPersonnelVersion.put(idNo + orgId, result);
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

    private List<LicDocumentRelationDto> getLicDocumentRelationDto(List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos, List<AppSvcDocDto> appSvcDocDtos,
                                                                   List<AppPremisesCorrelationDto> appPremisesCorrelationDtos, List<PremisesGroupDto> premisesGroupDtos) {
        log.info(StringUtil.changeForLog("The getLicDocumentRelationDto start ..."));
        List<LicDocumentRelationDto> licDocumentRelationDtos = IaisCommonUtils.genNewArrayList();
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
                LicDocumentRelationDto licDocumentRelationDto = new LicDocumentRelationDto();
                DocumentDto documentDto = MiscUtil.transferEntityDto(appSvcDocDto, DocumentDto.class);
                documentDto.setId(null);
                licDocumentRelationDto.setDocumentDto(documentDto);
                LicDocumentDto licDocumentDto = new LicDocumentDto();
                licDocumentDto.setSvcDocId(appSvcDocDto.getSvcDocId());
                licDocumentDto.setDocType(Integer.valueOf(ApplicationConsts.APPLICATION_DOC_TYPE_SERVICE));
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

    private String getAppPremCorrecId(List<AppPremisesCorrelationDto> appPremisesCorrelationDtos, String premisesId) {
        String result = null;
        if (appPremisesCorrelationDtos == null || appPremisesCorrelationDtos.size() == 0 || StringUtil.isEmpty(premisesId)) {
            return result;
        }
        for (AppPremisesCorrelationDto appPremisesCorrelationDto : appPremisesCorrelationDtos) {
            if (premisesId.equals(appPremisesCorrelationDto.getAppGrpPremId())) {
                result = appPremisesCorrelationDto.getId();
            }
        }
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
        if (applicationDto != null && originLicenceDto != null &&
                ((ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equalsIgnoreCase(applicationDto.getApplicationType()))
                || ApplicationConsts.APPLICATION_TYPE_CESSATION.equalsIgnoreCase(applicationDto.getApplicationType()))) {
            log.info(StringUtil.changeForLog("The  getLicenceDto APPType is RFC ..."));
            licenceDto.setStartDate(originLicenceDto.getStartDate());
            licenceDto.setExpiryDate(originLicenceDto.getExpiryDate());
            //licenceDto.setEndDate(originLicenceDto.getEndDate());
            licenceDto.setGrpLic(originLicenceDto.isGrpLic());
            licenceDto.setOriginLicenceId(originLicenceDto.getId());
            licenceDto.setMigrated(originLicenceDto.isMigrated());
            if (!applicationDto.isNeedNewLicNo()) {
                licenceDto.setLicenceNo(originLicenceDto.getLicenceNo());
                licenceDto.setVersion(originLicenceDto.getVersion() + 1);
            }else{
                licenceDto.setVersion(1);
            }
            licenceDto.setFeeRetroNeeded(originLicenceDto.isFeeRetroNeeded());
            licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_ACTIVE);
            if (applicationGroupDto != null) {
                licenceDto.setLicenseeId(applicationGroupDto.getLicenseeId());
            }
            //ceased    weilu
            try {
                String licenceNo = originLicenceDto.getLicenceNo();
                String[] split = licenceNo.split("/");
                if(split.length>5){
                    String runningNoS = split[5];
                    int runningNoI = Integer.parseInt(runningNoS);
                    String runningNo = String.valueOf(runningNoI + 1);
                    licenceDto.setCesedLicNo(runningNo);
                }
            }catch (Exception e){
                log.info(StringUtil.changeForLog("============ceased licNo=================="));
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
                                licenceDto.setRelLicenceId(relLicenceDto.getRelLicenceId());
                            }else{
                                log.error(StringUtil.changeForLog("This relLicenceNo can not get the relLicenceDto -->:"+relLicenceNo));
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
                                log.error(StringUtil.changeForLog("This relLicenceNo can not get the relLicenceDto -->:"+relLicenceNo));
                            }
                        }//baseApplicationNo
                        else if (!StringUtil.isEmpty(baseApplicationNo)) {
                            licenceDto.setBaseApplicationNo(baseApplicationNo);
                        }
                    }else{
                        log.error(StringUtil.changeForLog("Tha application is null ..."));
                    }

                }
                log.info(StringUtil.changeForLog("The expiryDate is -->:" + expiryDate));

                licenceDto.setStartDate(startDate);
                licenceDto.setExpiryDate(expiryDate);
                //licenceDto.setEndDate(licenceDto.getExpiryDate());
                licenceDto.setGrpLic(isGrpLic);
                licenceDto.setLicenseeId(applicationGroupDto.getLicenseeId());
            }
            int version = 1;
            if (originLicenceDto != null) {
                licenceDto.setOriginLicenceId(originLicenceDto.getId());
                licenceDto.setMigrated(originLicenceDto.isMigrated());
            } else {
                licenceDto.setMigrated(false);
            }
            //licenceDto.setLicenceNo(licenceNo);
            licenceDto.setVersion(version);
            licenceDto.setFeeRetroNeeded(false);
            //0065635
            if(applicationDto!=null&&ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(applicationDto.getApplicationType())){
                licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_APPROVED);
            }else{
                licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_ACTIVE);
            }
        }
        List<ApplicationDto> applicationDtos1 = IaisCommonUtils.genNewArrayList();
        if (applicationDto != null) {
            applicationDtos1.add(applicationDto);
        }
        if (applicationDtos != null) {
            applicationDtos1.addAll(applicationDtos);
        }
        licenceDto.setApplicationDtos(applicationDtos1);
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
                log.error(StringUtil.changeForLog("This licenseeId can not get he licensee -->:" + licenseeId));
            }
        } else {
            log.error(StringUtil.changeForLog("The  licenseeId is null ..."));
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

    //send email helper
    private void sendEmailHelper(Map<String, Object> tempMap, String msgTemplateId, String subject, String licenseeId, String clientQueryCode) {
        MsgTemplateDto msgTemplateDto = licenceService.getMsgTemplateById(msgTemplateId);
        if (tempMap == null || tempMap.isEmpty() || msgTemplateDto == null
                || StringUtil.isEmpty(msgTemplateId)
                || StringUtil.isEmpty(subject)
                || StringUtil.isEmpty(licenseeId)
                || StringUtil.isEmpty(clientQueryCode)) {
            return;
        }
        String mesContext = null;
        try {
            mesContext = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getMessageContent(), tempMap);

            EmailDto emailDto = new EmailDto();
            emailDto.setContent(mesContext);
            emailDto.setSubject(" " + msgTemplateDto.getTemplateName() + " " + subject);
            emailDto.setSender(mailSender);
            emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
            emailDto.setClientQueryCode(clientQueryCode);
            //send
            licenceService.sendEmail(emailDto);
        } catch (IOException | TemplateException e) {
            log.error(e.getMessage(), e);
        }
    }

    //send email
    private void sendEmailInspection(LicenceDto licenceDto) {
        if (licenceDto != null) {
            String serviceName = licenceDto.getSvcName();
            MsgTemplateDto msgTemplateDto = licenceService.getMsgTemplateById(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_POST_INSPECTION_IS_IDENTIFIED_ID);
            if (msgTemplateDto != null) {
                Map<String, Object> tempMap = IaisCommonUtils.genNewHashMap();
                tempMap.put("userName", StringUtil.viewHtml(serviceName));
                tempMap.put("MOH_AGENCY_NAME", AppConsts.MOH_AGENCY_NAME);
                String mesContext = null;
                try {
                    mesContext = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getMessageContent(), tempMap);

                    EmailDto emailDto = new EmailDto();
                    emailDto.setContent(mesContext);
                    emailDto.setSubject(" " + msgTemplateDto.getTemplateName() + " " + serviceName);
                    emailDto.setSender(mailSender);
                    emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenceDto.getLicenseeId()));
                    emailDto.setClientQueryCode(licenceDto.getId());
                    //send
                    licenceService.sendEmail(emailDto);
                } catch (IOException | TemplateException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }

    }

    private void sendEmailAndSms(ApplicationDto applicationDto, LicenceDto licenceDto,
                                 LicenseeDto oldLicenseeDto, LicenceDto originLicenceDto, String serviceId) {
        if(applicationDto == null ||
                licenceDto == null ||
                oldLicenseeDto == null ||
                originLicenceDto == null ||
                StringUtil.isEmpty(serviceId)){
            return;
        }
        log.info(StringUtil.changeForLog("The sendEmailAndSms start ..."));
        String applicationNo = applicationDto.getApplicationNo();
        String loginUrl = "#";
        String msgId = "";
        Map<String, Object> msgInfoMap = IaisCommonUtils.genNewHashMap();
        //new application send email
        //zhilin
        if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(applicationDto.getApplicationType())) {
            String uenNo = oldLicenseeDto.getUenNo();
            boolean isNew = false;
            if (StringUtil.isEmpty(uenNo)) {
                //todo set new uenNo
                uenNo = "new UEN";
                isNew = true;
            }
            //send sms
            try {
                //send email
                newApplicationApproveSendEmail(licenceDto, applicationNo, licenceDto.getLicenceNo(), loginUrl, isNew, uenNo);

                sendSMS(msgId, licenceDto.getLicenseeId(), msgInfoMap);
            } catch (Exception e) {
                log.error(StringUtil.changeForLog("send sms error"));
            }
         //huachong
        } else if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(applicationDto.getApplicationType())) {
            Map<String, Object> tempMap = IaisCommonUtils.genNewHashMap();
            tempMap.put("LICENCE", licenceDto.getLicenceNo());
            tempMap.put("APP_NO", applicationNo);
            String subject = " " + applicationNo + " - Approved ";
            sendEmailHelper(tempMap, MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_APPROVE, subject, licenceDto.getLicenseeId(), licenceDto.getId());
            //send sms
            try {
                sendSMS(msgId, licenceDto.getLicenseeId(), msgInfoMap);
                //send message
                String subject1 = "MOH IAIS – Renewal "+ applicationNo +" – Approved";
                String mesContext = "renew Approved message";
                HashMap<String, String> maskParams = IaisCommonUtils.genNewHashMap();
                sendMessage(subject1,licenceDto.getLicenseeId(),mesContext,maskParams,serviceId);
            } catch (Exception e) {
                log.error(StringUtil.changeForLog("send sms error"));
            }
         //guying
        } else if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationDto.getApplicationType())) {
            //Send notification to transferor when licence transfer application is approve
            Map<String, Object> notifyMap = IaisCommonUtils.genNewHashMap();
            LicenseeDto licDto = inspEmailService.getLicenseeDtoById(licenceDto.getLicenseeId());
            notifyMap.put("licensee", StringUtil.viewHtml(licDto.getUenNo()));
            notifyMap.put("licenceList", "<p>" + licenceDto.getLicenceNo() + " - " + licenceDto.getSvcName() + "</p>");
            notifyMap.put("status", "Is Approved.");
            //approve
            Map<String, Object> approveMap = IaisCommonUtils.genNewHashMap();
            approveMap.put("licensee", licDto.getUenNo());
            approveMap.put("licenceList", "<p>" + licenceDto.getLicenceNo() + " - " + licenceDto.getSvcName());

            sendApproveEmail(licenceDto, applicationNo, notifyMap, serviceId);
            try {
                //transfee
                sendSMS(msgId, licenceDto.getLicenseeId(), notifyMap);
                //transfor
                sendSMS(msgId, originLicenceDto.getLicenseeId(), notifyMap);
            } catch (Exception e) {
                log.error(StringUtil.changeForLog("send sms error"));
            }
        }
        log.info(StringUtil.changeForLog("The sendEmailAndSms end ..."));
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
            AuditTrailDto auditTrailDto = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTRANET);
            EventBusLicenceGroupDtos eventBusLicenceGroupDtos = new EventBusLicenceGroupDtos();
            String evenRefNum = String.valueOf(System.currentTimeMillis());
            eventBusLicenceGroupDtos.setEventRefNo(evenRefNum);
            eventBusLicenceGroupDtos.setLicenceGroupDtos(licenceGroupDtos);
            eventBusLicenceGroupDtos.setAuditTrailDto(auditTrailDto);
            //step1 create Licence to BE DB
            licenceService.createSuperLicDto(eventBusLicenceGroupDtos);
            //if create licence success
            //todo:update the success application group.
            //get the application
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
}
