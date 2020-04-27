package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.risk.RiskConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicFeeGroupItemDto;
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
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicInspectionGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremInspGrpCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationGroupService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.LicenceService;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.util.LicenceUtil;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import java.io.IOException;
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

    @Autowired
    private LicenceService licenceService;

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

    private Map<String,Integer> hciCodeVersion = new HashMap();
    private Map<String,Integer> keyPersonnelVersion = IaisCommonUtils.genNewHashMap();

    public void doBatchJob(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("The LicenceApproveBatchjob is start ..."));
        int day = systemParamConfig.getLicGenDay();
       //get can Generate Licence
        List<ApplicationLicenceDto> applicationLicenceDtos =licenceService.getCanGenerateApplications(day);
        if(applicationLicenceDtos == null || applicationLicenceDtos.size() == 0 ){
           log.debug(StringUtil.changeForLog("This time do not have need Generate Licences."));
           return;
        }
        //get the all use serviceCode.
        List<String> serviceIds = getAllServiceId(applicationLicenceDtos);
        List<HcsaServiceDto> hcsaServiceDtos = licenceService.getHcsaServiceById(serviceIds);
        if(hcsaServiceDtos == null || hcsaServiceDtos.size() == 0){
            log.error(StringUtil.changeForLog("This serviceIds can not get the HcsaServiceDto -->:"+serviceIds));
            return;
        }

        for(ApplicationLicenceDto applicationLicenceDto : applicationLicenceDtos ){
            if(applicationLicenceDto != null){
                ApplicationGroupDto applicationGroupDto = applicationLicenceDto.getApplicationGroupDto();
                if(applicationGroupDto != null){

                    List<LicenceGroupDto> licenceGroupDtos = IaisCommonUtils.genNewArrayList();
                    List<ApplicationGroupDto> success = IaisCommonUtils.genNewArrayList();
                    List<Map<String,String>> fail = IaisCommonUtils.genNewArrayList();
                    // delete the reject applicaiton
                    List<ApplicationListDto> applicationListDtoList = applicationLicenceDto.getApplicationListDtoList();
                    deleteRejectApplication(applicationListDtoList);
                    boolean isGrpLic = applicationGroupDto.isGrpLic();
                    log.debug(StringUtil.changeForLog("The application group no is -->;"+applicationGroupDto.getGroupNo()) );
                    log.debug(StringUtil.changeForLog("The isGrpLic is -->;"+isGrpLic));
                    GenerateResult generateResult =null;
                    try{
                        if(isGrpLic){
                            //generate the Group licence
                            generateResult = generateGroupLicence(applicationLicenceDto,hcsaServiceDtos);
                        }else{
                            //generate licence
                            generateResult = generateLIcence(applicationLicenceDto,hcsaServiceDtos);
                        }
                    }catch (Exception exception){
                        log.error(StringUtil.changeForLog("This  applicaiton group  have error -- >"+applicationGroupDto.getGroupNo()));
                        log.error(exception.getMessage(), exception);
                    }

                    toDoResult(licenceGroupDtos,generateResult,success,fail,applicationGroupDto);

                    if(success.size() > 0){
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
                        //step2 save licence to Fe DB
//               EventBusLicenceGroupDtos eventBusLicenceGroupDtos1 =  licenceService.getEventBusLicenceGroupDtosByRefNo(eventRefNo);
//               licenceService.createFESuperLicDto(eventBusLicenceGroupDtos1);
                   }

                }

            }
        }

        //todo:send the email to admin for fail Data.
        //else{ rollback step1}
        //step2 save licence to Fe DB
         //if(step2) success  completed
        //else{roll back step1 and step 2}

        log.debug(StringUtil.changeForLog("The LicenceApproveBatchjob is end ..."));
    }
    private List<ApplicationDto> updateApplicationStatusToGenerated(List<ApplicationDto> applicationDtos){
        List<ApplicationDto> result = IaisCommonUtils.genNewArrayList();
        if(IaisCommonUtils.isEmpty(applicationDtos)){
            return result;
        }
        for(ApplicationDto applicationDto : applicationDtos){
            applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_LICENCE_GENERATED);
            result.add(applicationDto);
        }
        return  result;
    }
    private void deleteRejectApplication(List<ApplicationListDto> applicationListDtoList){
        List<ApplicationListDto> removeList = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(applicationListDtoList)){
            for (ApplicationListDto applicationListDto : applicationListDtoList){
                if(applicationListDto!=null){
                    AppPremisesRecommendationDto appPremisesRecommendationDto = applicationListDto.getAppPremisesRecommendationDto();
                    boolean isReject =  isApplicaitonReject(appPremisesRecommendationDto);
                    if(isReject){
                        removeList.add(applicationListDto);
                    }
                }
            }
            applicationListDtoList.removeAll(removeList);
        }
    }

   private List<ApplicationDto> getApplications(List<LicenceGroupDto> licenceGroupDtos){
       List<ApplicationDto> result = IaisCommonUtils.genNewArrayList();
       if(!IaisCommonUtils.isEmpty(licenceGroupDtos)){
         for (LicenceGroupDto licenceGroupDto : licenceGroupDtos){
             List<SuperLicDto>  superLicDtos =  licenceGroupDto.getSuperLicDtos();
             if(!IaisCommonUtils.isEmpty(superLicDtos)){
               for (SuperLicDto superLicDto : superLicDtos){
                   LicenceDto licenceDto = superLicDto.getLicenceDto();
                   if(licenceDto!=null){
                       List<ApplicationDto> applicationDtos = licenceDto.getApplicationDtos();
                       if(!IaisCommonUtils.isEmpty(applicationDtos)){
                           result.addAll(applicationDtos);
                       }
                   }
               }
             }
         }
       }
       return  result;
   }
    private List<ApplicationGroupDto> updateStatusToGenerated(List<ApplicationGroupDto> applicationGroupDtos){
        List<ApplicationGroupDto> result = IaisCommonUtils.genNewArrayList();
        if(IaisCommonUtils.isEmpty(applicationGroupDtos)){
             return result;
        }
        for(ApplicationGroupDto applicationGroupDto : applicationGroupDtos){
            applicationGroupDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_LICENCE_GENERATED);
            result.add(applicationGroupDto);
        }
        return  result;
    }

    private void toDoResult(List<LicenceGroupDto> licenceGroupDtos,GenerateResult generateResult,List<ApplicationGroupDto> success,
                            List<Map<String,String>> fail, ApplicationGroupDto applicationGroupDto){
        if(generateResult!=null){
            boolean isSuccess = generateResult.isSuccess();
            if(isSuccess){
                if(applicationGroupDto!=null){
                    success.add(applicationGroupDto);
                }else{
                    log.info(StringUtil.changeForLog("There is not the applicationGroupDto for this job"));
                }
                LicenceGroupDto licenceGroupDto = generateResult.getLicenceGroupDto();
                if(licenceGroupDto!=null){
                    licenceGroupDtos.add(licenceGroupDto);
                }
            }else{
                Map<String,String> error = new HashMap();
                error.put(applicationGroupDto.getGroupNo(),generateResult.getErrorMessage());
                fail.add(error);
                for(Map.Entry<String,String> ent : error.entrySet()){
                    String value = ent.getValue();
                    log.error(StringUtil.changeForLog("The error is -->:"+value));
                }
            }
        }

    }

    private Map<String,List<ApplicationListDto>> tidyAppForGroupLicence(List<ApplicationListDto> applicationListDtoList){
        Map<String,List<ApplicationListDto>> result = IaisCommonUtils.genNewHashMap();
        if(IaisCommonUtils.isEmpty(applicationListDtoList)){
            return  result;
        }
        for(ApplicationListDto applicationListDto : applicationListDtoList){
            ApplicationDto applicationDto = applicationListDto.getApplicationDto();
            String groupLicenceFlag = applicationDto.getGroupLicenceFlag();
            log.debug(StringUtil.changeForLog("The groupLicenceFlag is -->:" + groupLicenceFlag));
            List<ApplicationListDto> applicationListDtos = result.get(groupLicenceFlag);
            if(applicationListDtos == null){
                applicationListDtos = IaisCommonUtils.genNewArrayList();
            }
            applicationListDtos.add(applicationListDto);
            result.put(groupLicenceFlag,applicationListDtos);
        }
        return result;
    }

    private  AppPremisesRecommendationDto getAppPremisesRecommendationDto(List<ApplicationListDto> applicationListDtos){
        log.info(StringUtil.changeForLog("The getAppPremisesRecommendationDto start ..."));
        AppPremisesRecommendationDto result = null;
        if(!IaisCommonUtils.isEmpty(applicationListDtos)){
            for(ApplicationListDto applicationListDto : applicationListDtos){
                AppPremisesRecommendationDto appPremisesRecommendationDto =  applicationListDto.getAppPremisesRecommendationDto();
                if(result ==  null){
                    result = appPremisesRecommendationDto;
                }else{
                    //Licence start date
                    if(appPremisesRecommendationDto.getRecomInDate() != null){
                        if(result.getRecomInDate() == null || appPremisesRecommendationDto.getRecomInDate().before(result.getRecomInDate())){
                            result.setRecomInDate(appPremisesRecommendationDto.getRecomInDate());
                        }
                    }
                    //RecomInNumber
                    if(!StringUtil.isEmpty(appPremisesRecommendationDto.getChronoUnit()) && appPremisesRecommendationDto.getRecomInNumber() != null){
                        if(appPremisesRecommendationDto.getChronoUnit().equals(result.getChronoUnit())){
                            if(appPremisesRecommendationDto.getRecomInNumber() < result.getRecomInNumber()){
                                result.setRecomInNumber(appPremisesRecommendationDto.getRecomInNumber());
                            }
                        }else if(RiskConsts.YEAR.equals(result.getChronoUnit())){
                            result.setRecomInNumber(appPremisesRecommendationDto.getRecomInNumber());
                            result.setChronoUnit(appPremisesRecommendationDto.getChronoUnit());
                        }else if(RiskConsts.MONTH.equals(result.getChronoUnit()) && RiskConsts.WEEK.equals(result.getChronoUnit())){
                            result.setRecomInNumber(appPremisesRecommendationDto.getRecomInNumber());
                            result.setChronoUnit(appPremisesRecommendationDto.getChronoUnit());
                        }
                    }
                }
            }
        }
        if(result != null){
          log.info(StringUtil.changeForLog("The Licence Start Date -->:"+result.getRecomInDate()));
          log.info(StringUtil.changeForLog("The RecomInNumber -->:"+result.getRecomInNumber()));
          log.info(StringUtil.changeForLog("The ChronoUnit -->:"+result.getChronoUnit()));
        }
        log.info(StringUtil.changeForLog("The getAppPremisesRecommendationDto end ..."));
        return result;
    }
    private GenerateResult generateGroupLicence(ApplicationLicenceDto applicationLicenceDto,List<HcsaServiceDto> hcsaServiceDtos){
        log.debug(StringUtil.changeForLog("The generateGroupLicence is start ..."));
        GenerateResult result = new GenerateResult();
        LicenceGroupDto licenceGroupDto = new LicenceGroupDto();
        List<ApplicationListDto> applicationListDtoList = applicationLicenceDto.getApplicationListDtoList();
        ApplicationGroupDto applicationGroupDto = applicationLicenceDto.getApplicationGroupDto();
        Integer isPostInspNeeded = isPostInspNeeded(applicationGroupDto);
        log.debug(StringUtil.changeForLog("The isPostInspNeeded is -->:"+isPostInspNeeded));

        //create licence group fee
        LicFeeGroupDto licFeeGroupDto = getLicFeeGroupDto(applicationGroupDto.getAmount().toString());
        licenceGroupDto.setLicFeeGroupDto(licFeeGroupDto);

        LicenseeDto oldLicenseeDto = getOrganizationIdBylicenseeId(applicationGroupDto.getLicenseeId());
        //get organizationId
        String organizationId = oldLicenseeDto.getOrganizationId();
        log.debug(StringUtil.changeForLog("The organizationId is -->:"+organizationId));
        if(IaisCommonUtils.isEmpty(applicationListDtoList)){
            result.setSuccess(false);
            result.setErrorMessage("The applicationListDtoList is null ...");
        }else{
            log.debug(StringUtil.changeForLog("The applicationListDtoList size is -->:"+applicationListDtoList.size()));
            //tidy up Application for Group Licence use
            Map<String,List<ApplicationListDto>> applications = tidyAppForGroupLicence(applicationListDtoList);
            List<SuperLicDto> superLicDtos = IaisCommonUtils.genNewArrayList();
            String errorMessage = null;
            if(applications.size()<=0){
                return result;
            }
            for (String key : applications.keySet()){
                SuperLicDto superLicDto = new SuperLicDto();
                superLicDto.setAppType(applicationGroupDto.getAppType());
                List<ApplicationListDto> applicationListDtos = applications.get(key);
                if(IaisCommonUtils.isEmpty(applicationListDtos)){
                    continue;
                }
                //to check this applicaiton is approve
                //get recommedation logic
                AppPremisesRecommendationDto appPremisesRecommendationDto = getAppPremisesRecommendationDto(applicationListDtos);
                //get service code
                log.debug(StringUtil.changeForLog("The key is -->:" + key));
                String serviceId = applicationListDtos.get(0).getApplicationDto().getServiceId();
                log.debug(StringUtil.changeForLog("The serviceId is -->:" + serviceId));
                HcsaServiceDto hcsaServiceDto = getHcsaServiceDtoByServiceId(hcsaServiceDtos,serviceId);
                if(hcsaServiceDto ==  null){
                    errorMessage = "This ServiceId can not get the HcsaServiceDto -->:"+serviceId;
                    break;
                }
                List<ApplicationDto> applicationDtos =  getApplicationDtos(applicationListDtos);
                String originLicenceId = applicationDtos.get(0).getOriginLicenceId();
                LicenceDto originLicenceDto = deleteOriginLicenceDto(originLicenceId);
                superLicDto.setOriginLicenceDto(originLicenceDto);
                //create licence
                String licenceNo = null;
                if(applicationListDtos.get(0).getApplicationDto().isNeedNewLicNo()) {
                    licenceNo = licenceService.getGroupLicenceNo(hcsaServiceDto.getSvcCode(), appPremisesRecommendationDto,originLicenceId);
                }
                log.debug(StringUtil.changeForLog("The licenceNo is -->;"+licenceNo));
                if(StringUtil.isEmpty(licenceNo)&& applicationListDtos.get(0).getApplicationDto().isNeedNewLicNo()){
                    errorMessage = "The licenceNo is null .-->:" + hcsaServiceDto.getSvcCode() + ":" + applicationListDtos.size() ;
                    break;
                }

                LicenceDto licenceDto = getLicenceDto(licenceNo,hcsaServiceDto.getSvcName(),null,applicationGroupDto,appPremisesRecommendationDto,
                        originLicenceDto,null,applicationDtos);
                superLicDto.setLicenceDto(licenceDto);
                //if PostInspNeeded send email
                if(isPostInspNeeded == Integer.parseInt(AppConsts.YES)){
                    sendEmailInspection(licenceDto);
                }
                //
                List<PremisesGroupDto> premisesGroupDtos = IaisCommonUtils.genNewArrayList();
                List<LicAppCorrelationDto> licAppCorrelationDtos = IaisCommonUtils.genNewArrayList();
                List<LicDocumentRelationDto> licDocumentRelationDtos = IaisCommonUtils.genNewArrayList();
                List<PersonnelsDto> personnelsDtos = IaisCommonUtils.genNewArrayList();
                for(ApplicationListDto applicationListDto : applicationListDtos){
                    //create Premises
                    List<AppGrpPremisesEntityDto> appGrpPremisesEntityDtos = applicationListDto.getAppGrpPremisesEntityDtos();
                    if(appGrpPremisesEntityDtos == null || appGrpPremisesEntityDtos.size() == 0){
                        errorMessage = "The AppGrpPremises is null for ApplicationNo" + applicationListDto.getApplicationDto().getApplicationNo();
                        break;
                    }
                    log.debug(StringUtil.changeForLog("The appGrpPremisesDtos.size() is -->;"+appGrpPremisesEntityDtos.size()));
                    //create lic_premises
                    List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = applicationListDto.getAppPremisesCorrelationDtos();
                    //create LicPremisesScopeDto
                    List<AppSvcPremisesScopeDto> appSvcPremisesScopeDtos = applicationListDto.getAppSvcPremisesScopeDtos();
                    List<AppSvcPremisesScopeAllocationDto> appSvcPremisesScopeAllocationDtos = applicationListDto.getAppSvcPremisesScopeAllocationDtos();

                    List<PremisesGroupDto> premisesGroupDtos1 = getPremisesGroupDto(applicationLicenceDto,appGrpPremisesEntityDtos,appPremisesCorrelationDtos,appSvcPremisesScopeDtos,
                            appSvcPremisesScopeAllocationDtos, hcsaServiceDto,organizationId,isPostInspNeeded);
                    if(!IaisCommonUtils.isEmpty(premisesGroupDtos1)){
                        PremisesGroupDto premisesGroupDto = premisesGroupDtos1.get(0);
                        if(premisesGroupDto.isHasError()){
                            errorMessage = premisesGroupDto.getErrorMessage();
                            break;
                        }
                        premisesGroupDtos.addAll(premisesGroupDtos1);
                    }
                    //create key_personnel key_personnel_ext lic_key_personnel
                    List<AppGrpPersonnelDto> appGrpPersonnelDtos = applicationListDto.getAppGrpPersonnelDtos();
                    List<AppGrpPersonnelExtDto> appGrpPersonnelExtDtos = applicationListDto.getAppGrpPersonnelExtDtos();
                    List<AppSvcKeyPersonnelDto> appSvcKeyPersonnelDtos = applicationListDto.getAppSvcKeyPersonnelDtos();
                    if(!IaisCommonUtils.isEmpty(appSvcKeyPersonnelDtos)){
                        List<PersonnelsDto> personnelsDto1s = getPersonnelsDto(appGrpPersonnelDtos,appGrpPersonnelExtDtos,appSvcKeyPersonnelDtos,organizationId);
                        if(personnelsDtos == null){
                            errorMessage = "There is Error for AppGrpPersonnel -->: "+applicationListDto.getApplicationDto().getApplicationNo();
                            break;
                        }
                        personnelsDtos.addAll(personnelsDto1s);
                    }
                    ApplicationDto applicationDto = applicationListDto.getApplicationDto();
                    if(applicationDto != null){
                        String appType = applicationDto.getApplicationType();
                        String applicationNo = applicationDto.getApplicationNo();
                        String loginUrl = "#";
                        //new application send email
                        if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)){
                            String uenNo = oldLicenseeDto.getUenNo();
                            boolean isNew = false;
                            if(StringUtil.isEmpty(uenNo)){
                                //todo set new uenNo
                                uenNo = "new UEN";
                                isNew = true;
                            }
                            //send email
                            newApplicationApproveSendEmail(licenceDto,applicationNo,licenceNo,loginUrl,isNew,uenNo);
                        }
                    }


                    //create the lic_app_correlation
                    LicAppCorrelationDto licAppCorrelationDto = new LicAppCorrelationDto();
                    licAppCorrelationDto.setApplicationId(applicationListDto.getApplicationDto().getId());
                    licAppCorrelationDtos.add(licAppCorrelationDto);

                    //create the document and lic_document from the primary doc.
                    List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = applicationListDto.getAppGrpPrimaryDocDtos();
                    List<AppSvcDocDto> appSvcDocDtos = applicationListDto.getAppSvcDocDtos();
                    List<LicDocumentRelationDto> licDocumentRelationDto1s = getLicDocumentRelationDto(appGrpPrimaryDocDtos,
                            appSvcDocDtos,appPremisesCorrelationDtos,premisesGroupDtos);
                    licDocumentRelationDtos.addAll(licDocumentRelationDto1s);

                    //create the lic_fee_group_item
                    //do not need create in the Dto
                    //todo:lic_base_specified_correlation
                    //
                }
                //create LicFeeGroupItemDto
                List<LicFeeGroupItemDto> licFeeGroupItemDtos = IaisCommonUtils.genNewArrayList();
                LicFeeGroupItemDto licFeeGroupItemDto = new LicFeeGroupItemDto();
                licFeeGroupItemDtos.add(licFeeGroupItemDto);
                superLicDto.setLicFeeGroupItemDtos(licFeeGroupItemDtos);

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
            if(StringUtil.isEmpty(errorMessage)){
                result.setSuccess(true);
                result.setLicenceGroupDto(licenceGroupDto);
            }else{
                result.setSuccess(false);
                result.setErrorMessage(errorMessage);
            }
        }
        log.debug(StringUtil.changeForLog("The generateGroupLicence is end ..."));
       return result;
    }

    private void newApplicationApproveSendEmail(LicenceDto licenceDto,String applicationNo,String licenceNo,String loginUrl,boolean isNew,String uenNo){
        Map<String ,Object> tempMap = IaisCommonUtils.genNewHashMap();
        tempMap.put("MOH_AGENCY_NAME",AppConsts.MOH_AGENCY_NAME);
        tempMap.put("loginUrl",loginUrl);
        tempMap.put("licenceNumber",licenceNo);
        tempMap.put("applicationNumber",applicationNo);
        tempMap.put("isNewApplication",null);
        if(isNew){
            tempMap.put("isNewApplication","Y");
            tempMap.put("UEN_NO",uenNo);
        }
        String subject = " " + applicationNo + " is Approved ";
        sendEmailHelper(tempMap,MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_APPROVED_ID,subject,licenceDto.getLicenseeId(),licenceDto.getId());
    }
    //send email helper
    private void sendEmailHelper(Map<String ,Object> tempMap,String msgTemplateId,String subject,String licenseeId,String clientQueryCode){
        MsgTemplateDto msgTemplateDto = licenceService.getMsgTemplateById(msgTemplateId);
        if(tempMap == null || tempMap.isEmpty() || msgTemplateDto == null
                         || StringUtil.isEmpty(msgTemplateId)
                         || StringUtil.isEmpty(subject)
                         || StringUtil.isEmpty(licenseeId)
                         || StringUtil.isEmpty(clientQueryCode)){
            return;
        }
        String mesContext = null;
        try {
            mesContext = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getMessageContent(), tempMap);
        } catch (IOException | TemplateException e) {
            log.error(e.getMessage(),e);
        }
        EmailDto emailDto = new EmailDto();
        emailDto.setContent(mesContext);
        emailDto.setSubject(" " + msgTemplateDto.getTemplateName() + " " + subject);
        emailDto.setSender(AppConsts.MOH_AGENCY_NAME);
        emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
        emailDto.setClientQueryCode(clientQueryCode);
        //send
        licenceService.sendEmail(emailDto);
    }

    //send email
    private void sendEmailInspection(LicenceDto licenceDto){
        if(licenceDto != null){
            String serviceName = licenceDto.getSvcName();
            MsgTemplateDto msgTemplateDto = licenceService.getMsgTemplateById(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_POST_INSPECTION_IS_IDENTIFIED_ID);
            if(msgTemplateDto != null){
                Map<String ,Object> tempMap = IaisCommonUtils.genNewHashMap();
                tempMap.put("userName",StringUtil.viewHtml(serviceName));
                tempMap.put("MOH_AGENCY_NAME",AppConsts.MOH_AGENCY_NAME);
                String mesContext = null;
                try {
                    mesContext = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getMessageContent(), tempMap);
                } catch (IOException | TemplateException e) {
                    log.error(e.getMessage(),e);
                }
                EmailDto emailDto = new EmailDto();
                emailDto.setContent(mesContext);
                emailDto.setSubject(" " + msgTemplateDto.getTemplateName() + " " + serviceName);
                emailDto.setSender(AppConsts.MOH_AGENCY_NAME);
                emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenceDto.getLicenseeId()));
                emailDto.setClientQueryCode(licenceDto.getId());
                //send
                licenceService.sendEmail(emailDto);
            }
        }
        
    }

    private LicenceDto deleteOriginLicenceDto(String organizationId){
        LicenceDto result = null;
        if(!StringUtil.isEmpty(organizationId)){
            result = licenceService.getLicenceDto(organizationId);
            if(result!=null){
                result.setStatus(ApplicationConsts.LICENCE_STATUS_IACTIVE);
            }
        }
        return result;
    }

    private List<ApplicationDto> getApplicationDtos(List<ApplicationListDto> applicationListDtos){
        List<ApplicationDto> result = IaisCommonUtils.genNewArrayList();
        if(applicationListDtos!=null && applicationListDtos.size() > 0){
            for (ApplicationListDto applicationListDto : applicationListDtos){
                result.add(applicationListDto.getApplicationDto());
            }
        }
        return  result;
    }

    private GenerateResult generateLIcence(ApplicationLicenceDto applicationLicenceDto,List<HcsaServiceDto> hcsaServiceDtos){
        log.debug(StringUtil.changeForLog("The generateLIcence is start ..."));
        GenerateResult result = new GenerateResult();
        LicenceGroupDto licenceGroupDto = new LicenceGroupDto();
        List<ApplicationListDto> applicationListDtoList = applicationLicenceDto.getApplicationListDtoList();
        ApplicationGroupDto applicationGroupDto = applicationLicenceDto.getApplicationGroupDto();
        Integer isPostInspNeeded = isPostInspNeeded(applicationGroupDto);
        log.debug(StringUtil.changeForLog("The isPostInspNeeded is -->:"+isPostInspNeeded));

        //create licence group fee
        LicFeeGroupDto licFeeGroupDto = getLicFeeGroupDto(applicationGroupDto.getAmount().toString());
        licenceGroupDto.setLicFeeGroupDto(licFeeGroupDto);

        LicenseeDto oldLicenseeDto = getOrganizationIdBylicenseeId(applicationGroupDto.getLicenseeId());
        //get organizationId
        String organizationId = oldLicenseeDto.getOrganizationId();
        log.debug(StringUtil.changeForLog("The organizationId is -->:"+organizationId));

        if(applicationListDtoList == null || applicationListDtoList.size() == 0){
            result.setSuccess(false);
            result.setErrorMessage("The applicationListDtoList is null ...");
        }else{
            log.debug(StringUtil.changeForLog("The applicationListDtoList size is -->:"+applicationListDtoList.size()));

            List<SuperLicDto> superLicDtos = IaisCommonUtils.genNewArrayList();
            String errorMessage = null;
            for(ApplicationListDto applicationListDto : applicationListDtoList){
                SuperLicDto superLicDto = new SuperLicDto();
                superLicDto.setAppType(applicationGroupDto.getAppType());
                //get service code
                ApplicationDto applicationDto = applicationListDto.getApplicationDto();
                if(applicationDto == null){
                    errorMessage = "There is a ApplicationDto is null";
                    break;
                }

                //to check this applicaiton is approve
                AppPremisesRecommendationDto appPremisesRecommendationDto = applicationListDto.getAppPremisesRecommendationDto();
                String serviceId = applicationDto.getServiceId();
                log.debug(StringUtil.changeForLog("The serviceId is -->:" + serviceId));
                HcsaServiceDto hcsaServiceDto = getHcsaServiceDtoByServiceId(hcsaServiceDtos,serviceId);
                 if(hcsaServiceDto ==  null){
                     errorMessage = "There is a ApplicationDto is null";
                     break;
                 }
                //create Premises
                List<AppGrpPremisesEntityDto> appGrpPremisesEntityDtos = applicationListDto.getAppGrpPremisesEntityDtos();
                if(appGrpPremisesEntityDtos == null || appGrpPremisesEntityDtos.size() == 0){
                    errorMessage = "The AppGrpPremises is null for ApplicationNo" + applicationDto.getApplicationNo();
                    break;
                }
                log.debug(StringUtil.changeForLog("The appGrpPremisesDtos.size() is -->;"+appGrpPremisesEntityDtos.size()));
                //create lic_premises
                List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = applicationListDto.getAppPremisesCorrelationDtos();
                //create LicPremisesScopeDto
                List<AppSvcPremisesScopeDto> appSvcPremisesScopeDtos = applicationListDto.getAppSvcPremisesScopeDtos();
                List<AppSvcPremisesScopeAllocationDto> appSvcPremisesScopeAllocationDtos = applicationListDto.getAppSvcPremisesScopeAllocationDtos();

                List<PremisesGroupDto> premisesGroupDtos = getPremisesGroupDto(applicationLicenceDto,appGrpPremisesEntityDtos,appPremisesCorrelationDtos,appSvcPremisesScopeDtos,
                        appSvcPremisesScopeAllocationDtos, hcsaServiceDto,organizationId,isPostInspNeeded);
                String licenceNo = null;
                //get the yearLenth.
//                int yearLength = getYearLength(appPremisesRecommendationDto);
                if(!IaisCommonUtils.isEmpty(premisesGroupDtos)){
                    PremisesGroupDto premisesGroupDto =premisesGroupDtos.get(0);
                    if(premisesGroupDto.isHasError()){
                        errorMessage = premisesGroupDto.getErrorMessage();
                        break;
                    }
                    superLicDto.setPremisesGroupDtos(premisesGroupDtos);

                    //create licence
                    if(applicationDto.isNeedNewLicNo()){
                        licenceNo = licenceService.getLicenceNo(premisesGroupDto.getPremisesDto().getHciCode(),hcsaServiceDto.getSvcCode(),appPremisesRecommendationDto);
                    }
                    log.debug(StringUtil.changeForLog("The licenceNo is -->;"+licenceNo));
                    if(StringUtil.isEmpty(licenceNo) && applicationDto.isNeedNewLicNo()){
                        errorMessage = "The licenceNo is null .-->:" + premisesGroupDto.getPremisesDto().getHciCode() + ":" + hcsaServiceDto.getSvcCode();
                        break;
                    }
                }
                String originLicenceId = applicationDto.getOriginLicenceId();
                LicenceDto originLicenceDto = deleteOriginLicenceDto(originLicenceId);
                superLicDto.setOriginLicenceDto(originLicenceDto);
                LicenceDto licenceDto = getLicenceDto(licenceNo,hcsaServiceDto.getSvcName(),hcsaServiceDto.getSvcType(),applicationGroupDto,appPremisesRecommendationDto,
                        originLicenceDto,applicationDto,null);
                superLicDto.setLicenceDto(licenceDto);
                //if PostInspNeeded send email
                if(isPostInspNeeded == Integer.parseInt(AppConsts.YES)){
                    sendEmailInspection(licenceDto);
                }
                String appType = applicationDto.getApplicationType();
                String applicationNo = applicationDto.getApplicationNo();
                String loginUrl = "#";
                //new application send email
                if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)){
                    String uenNo = oldLicenseeDto.getUenNo();
                    boolean isNew = false;
                    if(StringUtil.isEmpty(uenNo)){
                        //todo set new uenNo
                        uenNo = "new UEN";
                        isNew = true;
                    }
                    //send email
                    newApplicationApproveSendEmail(licenceDto,applicationNo,licenceNo,loginUrl,isNew,uenNo);
                }else if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)){
                    Map<String ,Object> tempMap = IaisCommonUtils.genNewHashMap();
                    tempMap.put("LICENCE",licenceNo);
                    tempMap.put("APP_NO",applicationNo);
                    String subject = " " + applicationNo + " - Approved ";
                    sendEmailHelper(tempMap,MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_APPROVE,subject,licenceDto.getLicenseeId(),licenceDto.getId());
                }

                //create the lic_app_correlation
                List<LicAppCorrelationDto> licAppCorrelationDtos = IaisCommonUtils.genNewArrayList();
                LicAppCorrelationDto licAppCorrelationDto = new LicAppCorrelationDto();
                licAppCorrelationDto.setApplicationId(applicationDto.getId());
                licAppCorrelationDtos.add(licAppCorrelationDto);
                superLicDto.setLicAppCorrelationDtos(licAppCorrelationDtos);
                //create LicFeeGroupItemDto
                List<LicFeeGroupItemDto> licFeeGroupItemDtos = IaisCommonUtils.genNewArrayList();
                LicFeeGroupItemDto licFeeGroupItemDto = new LicFeeGroupItemDto();
                licFeeGroupItemDtos.add(licFeeGroupItemDto);
                superLicDto.setLicFeeGroupItemDtos(licFeeGroupItemDtos);
                //create the document and lic_document from the primary doc.
                List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = applicationListDto.getAppGrpPrimaryDocDtos();
                List<AppSvcDocDto> appSvcDocDtos = applicationListDto.getAppSvcDocDtos();
                List<LicDocumentRelationDto> licDocumentRelationDtos = getLicDocumentRelationDto(appGrpPrimaryDocDtos,
                        appSvcDocDtos,appPremisesCorrelationDtos,premisesGroupDtos);
                superLicDto.setLicDocumentRelationDto(licDocumentRelationDtos);

                //create key_personnel key_personnel_ext lic_key_personnel
                List<AppGrpPersonnelDto> appGrpPersonnelDtos = applicationListDto.getAppGrpPersonnelDtos();
                List<AppGrpPersonnelExtDto> appGrpPersonnelExtDtos = applicationListDto.getAppGrpPersonnelExtDtos();
                List<AppSvcKeyPersonnelDto> appSvcKeyPersonnelDtos = applicationListDto.getAppSvcKeyPersonnelDtos();
                if(!IaisCommonUtils.isEmpty(appSvcKeyPersonnelDtos)){
                    List<PersonnelsDto> personnelsDtos = getPersonnelsDto(appGrpPersonnelDtos,appGrpPersonnelExtDtos,appSvcKeyPersonnelDtos,organizationId);
                    if(IaisCommonUtils.isEmpty(personnelsDtos)){
                        errorMessage = "There is Error for AppGrpPersonnel -->: "+applicationDto.getApplicationNo();
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
            }
            licenceGroupDto.setSuperLicDtos(superLicDtos);
            if(StringUtil.isEmpty(errorMessage)){
                result.setSuccess(true);
                result.setLicenceGroupDto(licenceGroupDto);
            }else{
                result.setSuccess(false);
                result.setErrorMessage(errorMessage);
            }
        }
        log.debug(StringUtil.changeForLog("The generateLIcence is end ..."));
        return result;
    }


//    private  int getYearLength(AppPremisesRecommendationDto appPremisesRecommendationDto){
//        log.debug(StringUtil.changeForLog("The getYearLength is start ..."));
//        int yearLength = 1;
//        if(appPremisesRecommendationDto!=null){
//            String chrono = appPremisesRecommendationDto.getChronoUnit();
//            if(AppConsts.LICENCE_PERIOD_YEAR.equals(chrono)){
//                yearLength = appPremisesRecommendationDto.getRecomInNumber();
//            }else if(AppConsts.LICENCE_PERIOD_MONTH.equals(chrono)){
//                yearLength = appPremisesRecommendationDto.getRecomInNumber()/12;
//            }else{
//                log.debug(StringUtil.changeForLog("The getYearLength chrono is not Year ..."));
//            }
//        }else{
//            log.error(StringUtil.changeForLog("The getYearLength appPremisesRecommendationDto is null ..."));
//        }
//        log.debug(StringUtil.changeForLog("The getYearLength is end ..."));
//        return yearLength;
//    }
    private boolean isApplicaitonReject(AppPremisesRecommendationDto appPremisesRecommendationDto){
        boolean result = false;
        if(appPremisesRecommendationDto!=null){
            Integer number = appPremisesRecommendationDto.getRecomInNumber();
            if(number != null){
                if(number == 0){
                    result = true;
                }
            }
        }
        return result;
    }

    private List<LicSvcSpecificPersonnelDto> getLicSvcSpecificPersonnelDtos(List<AppSvcPersonnelDto> appSvcPersonnelDtos){
        List<LicSvcSpecificPersonnelDto> result = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(appSvcPersonnelDtos)){
           for (AppSvcPersonnelDto appSvcPersonnelDto : appSvcPersonnelDtos){
               LicSvcSpecificPersonnelDto licSvcSpecificPersonnelDto = MiscUtil.transferEntityDto(appSvcPersonnelDto,LicSvcSpecificPersonnelDto.class);
               result.add(licSvcSpecificPersonnelDto);
           }
        }
        return  result;
    }

    private String getHciCodeFromSameApplicaitonGroup(ApplicationLicenceDto applicationLicenceDto,AppGrpPremisesEntityDto appGrpPremisesEntityDto){
        String hciCode = null;
        if(applicationLicenceDto != null && appGrpPremisesEntityDto != null){
            List<ApplicationListDto> applicationListDtoList = applicationLicenceDto.getApplicationListDtoList();
            if(!IaisCommonUtils.isEmpty(applicationListDtoList)){
              for (ApplicationListDto applicationListDto : applicationListDtoList){
                  List<AppGrpPremisesEntityDto> appGrpPremisesEntityDtos =  applicationListDto.getAppGrpPremisesEntityDtos();
                  if(!IaisCommonUtils.isEmpty(appGrpPremisesEntityDtos)){
                     for (AppGrpPremisesEntityDto appGrpPremisesEntityDto1 : appGrpPremisesEntityDtos){
                         if(appGrpPremisesEntityDto.getPremiseKey().equals(appGrpPremisesEntityDto1.getPremiseKey()) &&
                                 !StringUtil.isEmpty(appGrpPremisesEntityDto1.getHciCode())){
                             hciCode = appGrpPremisesEntityDto1.getHciCode();
                             break;
                         }
                     }
                  }
                  if(!StringUtil.isEmpty(hciCode)){
                      break;
                  }
              }
            }
        }
        return  hciCode;

    }

    private List<PremisesGroupDto> getPremisesGroupDto(ApplicationLicenceDto applicationLicenceDto,
                                                       List<AppGrpPremisesEntityDto> appGrpPremisesEntityDtos,
                                                       List<AppPremisesCorrelationDto> appPremisesCorrelationDtos,
                                                       List<AppSvcPremisesScopeDto> appSvcPremisesScopeDtos,
                                                       List<AppSvcPremisesScopeAllocationDto> appSvcPremisesScopeAllocationDtos,
                                                       HcsaServiceDto hcsaServiceDto,
                                                       String organizationId,
                                                       Integer isPostInspNeeded){
        List<PremisesGroupDto> reuslt = IaisCommonUtils.genNewArrayList();
        if (IaisCommonUtils.isEmpty(appGrpPremisesEntityDtos)){
            return  reuslt;
        }
        for (AppGrpPremisesEntityDto appGrpPremisesEntityDto : appGrpPremisesEntityDtos){
            PremisesGroupDto premisesGroupDto = new PremisesGroupDto();
            premisesGroupDto.setHasError(false);
            //premises
            String hciCode = appGrpPremisesEntityDto.getHciCode();
            if(StringUtil.isEmpty(hciCode)){
                hciCode = getHciCodeFromSameApplicaitonGroup(applicationLicenceDto,appGrpPremisesEntityDto);
                if(StringUtil.isEmpty(hciCode)){
                    hciCode = licenceService.getHciCode(hcsaServiceDto.getSvcCode());
                }
                appGrpPremisesEntityDto.setHciCode(hciCode);
            }
            PremisesDto premisesDto = MiscUtil.transferEntityDto(appGrpPremisesEntityDto,PremisesDto.class);
            premisesDto.setHciCode(hciCode);
            premisesDto.setVersion(getVersionByHciCode(hciCode));
            premisesDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            premisesDto.setOrganizationId(organizationId);
            List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodDtos = appGrpPremisesEntityDto.getAppPremPhOpenPeriodDtoList();
            List<LicPremPhOpenPeriodDto> licPremPhOpenPeriodDtos = IaisCommonUtils.genNewArrayList();
            if(!IaisCommonUtils.isEmpty(appPremPhOpenPeriodDtos)){
                for (AppPremPhOpenPeriodDto appPremPhOpenPeriodDto : appPremPhOpenPeriodDtos){
                    LicPremPhOpenPeriodDto licPremPhOpenPeriodDto = MiscUtil.transferEntityDto(appPremPhOpenPeriodDto,LicPremPhOpenPeriodDto.class);
                    licPremPhOpenPeriodDto.setPremId(null);
                    licPremPhOpenPeriodDtos.add(licPremPhOpenPeriodDto);
                }
            }
            premisesDto.setLicPremPhOpenPeriodDtos(licPremPhOpenPeriodDtos);
            premisesGroupDto.setPremisesDto(premisesDto);
            //create lic_premises
            String premisesId = appGrpPremisesEntityDto.getId();
            String appPremCorrecId = getAppPremCorrecId(appPremisesCorrelationDtos,premisesId);
            if(StringUtil.isEmpty(appPremCorrecId)){
//                premisesGroupDto.setHasError(true);
//                premisesGroupDto.setErrorMessage("This PremisesId can not find out appPremCorrecId -->:"+premisesId);
//                reuslt.clear();
//                reuslt.add(premisesGroupDto);
//                break;
                continue;
            }
            AppPremisesRecommendationDto appPremisesRecommendationDto = licenceService.getTcu(appPremCorrecId);
            LicPremisesDto licPremisesDto = new LicPremisesDto();
            licPremisesDto.setPremisesId(premisesId);
            licPremisesDto.setIsPostInspNeeded(isPostInspNeeded);
            if(appPremisesRecommendationDto == null){
                licPremisesDto.setIsTcuNeeded(Integer.parseInt(AppConsts.NO));
            }else{
                licPremisesDto.setIsTcuNeeded(Integer.parseInt(AppConsts.YES));
                licPremisesDto.setTcuDate(appPremisesRecommendationDto.getRecomInDate());
            }
            premisesGroupDto.setLicPremisesDto(licPremisesDto);
             if(1==isPostInspNeeded){
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
            List<AppSvcPremisesScopeDto> appSvcPremisesScopeDtoList = getAppSvcPremisesScopeDtoByCorrelationId(appSvcPremisesScopeDtos,appPremCorrecId);
            if(!IaisCommonUtils.isEmpty(appSvcPremisesScopeDtoList)){
                List<LicPremisesScopeGroupDto> licPremisesScopeGroupDtoList = IaisCommonUtils.genNewArrayList();
                for(AppSvcPremisesScopeDto appSvcPremisesScopeDto :appSvcPremisesScopeDtoList){
                    LicPremisesScopeGroupDto licPremisesScopeGroupDto = new LicPremisesScopeGroupDto();
                    LicPremisesScopeDto licPremisesScopeDto = new LicPremisesScopeDto();
                    licPremisesScopeDto.setSubsumedType(appSvcPremisesScopeDto.isSubsumedType());
                    licPremisesScopeDto.setScopeName(appSvcPremisesScopeDto.getScopeName());
                    licPremisesScopeGroupDto.setLicPremisesScopeDto(licPremisesScopeDto);
                    //create LicPremisesScopeAllocationDto
                    AppSvcPremisesScopeAllocationDto appSvcPremisesScopeAllocationDto = getAppSvcPremisesScopeAllocationDto(appSvcPremisesScopeAllocationDtos,
                            appSvcPremisesScopeDto.getId());
                    if(appSvcPremisesScopeAllocationDto!= null){
                        LicPremisesScopeAllocationDto licPremisesScopeAllocationDto = new LicPremisesScopeAllocationDto();
                        licPremisesScopeAllocationDto.setLicCgoId(appSvcPremisesScopeAllocationDto.getAppSvcKeyPsnId());
                        licPremisesScopeGroupDto.setLicPremisesScopeAllocationDto(licPremisesScopeAllocationDto);
                    }else{
                        log.info(StringUtil.changeForLog("this appSvcPremisesScopeDto.getId() do not have the AppSvcPremisesScopeAllocationDto -->:"+appSvcPremisesScopeDto.getId()));
                    }
                    licPremisesScopeGroupDtoList.add(licPremisesScopeGroupDto);
                }
                premisesGroupDto.setLicPremisesScopeGroupDtoList(licPremisesScopeGroupDtoList);
            }else{
                log.info(StringUtil.changeForLog("This appPremCorrecId can not get the AppSvcPremisesScopeDto -->:"+appPremCorrecId));
            }
            reuslt.add(premisesGroupDto);
        }

        return  reuslt;
    }


  private List<AppSvcPremisesScopeDto> getAppSvcPremisesScopeDtoByCorrelationId(List<AppSvcPremisesScopeDto> appSvcPremisesScopeDtos,String appPremCorrecId){
      List<AppSvcPremisesScopeDto> result = IaisCommonUtils.genNewArrayList();
        if(IaisCommonUtils.isEmpty(appSvcPremisesScopeDtos)|| StringUtil.isEmpty(appPremCorrecId)){
          return result;
        }

      for(AppSvcPremisesScopeDto appSvcPremisesScopeDto : appSvcPremisesScopeDtos){
         if(appSvcPremisesScopeDto != null && appPremCorrecId.equals(appSvcPremisesScopeDto.getAppPremCorreId())){
             result.add(appSvcPremisesScopeDto);
         }
      }
      return  result;
  }

    private LicFeeGroupDto getLicFeeGroupDto(String amount){
        LicFeeGroupDto licFeeGroupDto = new LicFeeGroupDto();
        licFeeGroupDto.setFeeAmount(amount);
        return licFeeGroupDto;
    }
    private Integer isPostInspNeeded(ApplicationGroupDto applicationGroupDto){
        log.debug(StringUtil.changeForLog("The isPostInspNeeded is start ..."));
        int inspectionNeed = applicationGroupDto.getIsInspectionNeeded();
        log.debug(StringUtil.changeForLog("The inspectionNeed is -->:"+inspectionNeed));
        int isPreInspection = applicationGroupDto.getIsPreInspection();
        log.debug(StringUtil.changeForLog("The isPreInspection is -->:"+isPreInspection));
        Integer isPostInspNeeded = Integer.valueOf(AppConsts.NO);
        if(inspectionNeed == 1 && isPreInspection == 0){
            isPostInspNeeded = Integer.valueOf(AppConsts.YES);
        }
        log.debug(StringUtil.changeForLog("The isPostInspNeeded is end ..."));
        return isPostInspNeeded;
    }
    private List<PersonnelsDto> getPersonnelsDto(List<AppGrpPersonnelDto> appGrpPersonnelDtos,List<AppGrpPersonnelExtDto> appGrpPersonnelExtDtos,
                                                 List<AppSvcKeyPersonnelDto> appSvcKeyPersonnelDtos,
                                                 String organizationId){
        List<PersonnelsDto> result = IaisCommonUtils.genNewArrayList();
        if(IaisCommonUtils.isEmpty(appSvcKeyPersonnelDtos)){
            return result;
        }
        for(AppSvcKeyPersonnelDto appSvcKeyPersonnelDto : appSvcKeyPersonnelDtos){
            PersonnelsDto personnelsDto = new PersonnelsDto();
            //create AppGrpPersonnelDto
            String appGrpPsnId = appSvcKeyPersonnelDto.getAppGrpPsnId();
            AppGrpPersonnelDto appGrpPersonnelDto =  getAppGrpPersonnelDtoById(appGrpPersonnelDtos,appGrpPsnId);
            if(appGrpPersonnelDto == null){
                return  result;
            }
            KeyPersonnelDto keyPersonnelDto = MiscUtil.transferEntityDto(appGrpPersonnelDto,KeyPersonnelDto.class);
            //:controller the psersonnel version
            keyPersonnelDto.setVersion(getKeyPersonnelVersion(keyPersonnelDto.getIdNo(),organizationId));
            //todo: controller status
            keyPersonnelDto.setStatus("active");
            //: controller the Organization
            keyPersonnelDto.setOrganizationId(organizationId);
            personnelsDto.setKeyPersonnelDto(keyPersonnelDto);
            //create AppGrpPersonnelExtDto
            String appGrpPsnExtId = appSvcKeyPersonnelDto.getAppGrpPsnExtId();
            AppGrpPersonnelExtDto appGrpPersonnelExtDto = getAppGrpPersonnelExtDtoById(appGrpPersonnelExtDtos,appGrpPsnExtId);
            KeyPersonnelExtDto keyPersonnelExtDto =  MiscUtil.transferEntityDto(appGrpPersonnelExtDto,KeyPersonnelExtDto.class);
            if(keyPersonnelExtDto != null){
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
        return  result;
    }

    private Integer getKeyPersonnelVersion(String idNo,String orgId){
        Integer result = 1;
        if(StringUtil.isEmpty(idNo)||StringUtil.isEmpty(orgId)){
            return result;
        }
        Integer version = keyPersonnelVersion.get(idNo+orgId);
        if(version==null){
            KeyPersonnelDto keyPersonnelDto = licenceService.getLatestVersionKeyPersonnelByIdNoAndOrgId(idNo,orgId);
            if(keyPersonnelDto!= null){
                result = keyPersonnelDto.getVersion()+1;
            }
        }else{
            result = version+1;
        }
        keyPersonnelVersion.put(idNo+orgId,result);
        return result;
    }
    private AppGrpPersonnelExtDto getAppGrpPersonnelExtDtoById(List<AppGrpPersonnelExtDto> appGrpPersonnelExtDtos ,String appGrpPsnExtId){
        AppGrpPersonnelExtDto result = null;
        if(appGrpPersonnelExtDtos == null || appGrpPersonnelExtDtos.size() == 0 || StringUtil.isEmpty(appGrpPsnExtId)){
            return result;
        }
        for(AppGrpPersonnelExtDto appGrpPersonnelExtDto : appGrpPersonnelExtDtos){
            if(appGrpPsnExtId.equals(appGrpPersonnelExtDto.getId())){
                result = appGrpPersonnelExtDto;
                break;
            }
        }
        return  result;
    }
    private AppGrpPersonnelDto getAppGrpPersonnelDtoById(List<AppGrpPersonnelDto> appGrpPersonnelDtos ,String appGrpPsnId){
        AppGrpPersonnelDto result = null;
        if(appGrpPersonnelDtos == null || appGrpPersonnelDtos.size() == 0 || StringUtil.isEmpty(appGrpPsnId)){
          return result;
        }
        for(AppGrpPersonnelDto appGrpPersonnelDto : appGrpPersonnelDtos){
            if(appGrpPsnId.equals(appGrpPersonnelDto.getId())){
                result = appGrpPersonnelDto;
                break;
            }
        }
        return  result;
    }

    private AppSvcPremisesScopeAllocationDto getAppSvcPremisesScopeAllocationDto(List<AppSvcPremisesScopeAllocationDto> appSvcPremisesScopeAllocationDtos,
                                                                                 String appSvcPremisesScopeId){
        AppSvcPremisesScopeAllocationDto result = null;
        if(StringUtil.isEmpty(appSvcPremisesScopeId)|| appSvcPremisesScopeAllocationDtos == null || appSvcPremisesScopeAllocationDtos.size() == 0){
            return result;
        }
       for (AppSvcPremisesScopeAllocationDto appSvcPremisesScopeAllocationDto : appSvcPremisesScopeAllocationDtos){
           if(appSvcPremisesScopeId.equals(appSvcPremisesScopeAllocationDto.getAppSvcPremScopeId())){
               result =appSvcPremisesScopeAllocationDto;
               break;
           }
       }
       return result;
    }

    private List<LicDocumentRelationDto> getLicDocumentRelationDto(List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos,List<AppSvcDocDto> appSvcDocDtos,
                                                                   List<AppPremisesCorrelationDto> appPremisesCorrelationDtos,List<PremisesGroupDto> premisesGroupDtos){
        List<LicDocumentRelationDto> licDocumentRelationDtos = IaisCommonUtils.genNewArrayList();
        if(appGrpPrimaryDocDtos != null){
            for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : appGrpPrimaryDocDtos){
                if(!IaisCommonUtils.isEmpty(premisesGroupDtos)){
                    for(PremisesGroupDto premisesGroupDto : premisesGroupDtos){
                        PremisesDto premisesDto = premisesGroupDto.getPremisesDto();
                        LicDocumentRelationDto licDocumentRelationDto = new LicDocumentRelationDto();
                        DocumentDto documentDto = new DocumentDto();
                        documentDto.setDocName(appGrpPrimaryDocDto.getDocName());
                        documentDto.setDocSize(appGrpPrimaryDocDto.getDocSize());
                        documentDto.setFileRepoId(appGrpPrimaryDocDto.getFileRepoId());
                        documentDto.setSubmitDt(appGrpPrimaryDocDto.getSubmitDt());
                        documentDto.setSubmitBy(appGrpPrimaryDocDto.getSubmitBy());
                        licDocumentRelationDto.setDocumentDto(documentDto);

                        LicDocumentDto licDocumentDto = new LicDocumentDto();
                        licDocumentDto.setSvcDocId(appGrpPrimaryDocDto.getSvcDocId());
                        licDocumentDto.setDocType(Integer.valueOf(ApplicationConsts.APPLICATION_DOC_TYPE_PARIMARY));
                        //set the old premises Id ,get the releation when the save.
                        if(StringUtil.isEmpty(appGrpPrimaryDocDto.getAppGrpPremId())){
                            licDocumentDto.setLicPremId(premisesDto.getId());
                            licDocumentRelationDto.setLicDocumentDto(licDocumentDto);
                            licDocumentRelationDtos.add(licDocumentRelationDto);
                        }else if(appGrpPrimaryDocDto.getAppGrpPremId().equals(premisesDto.getId())){
                            licDocumentDto.setLicPremId(appGrpPrimaryDocDto.getAppGrpPremId());
                            licDocumentRelationDto.setLicDocumentDto(licDocumentDto);
                            licDocumentRelationDtos.add(licDocumentRelationDto);
                        }
                    }
                }
            }
        }
        if(appSvcDocDtos != null){
           for (AppSvcDocDto appSvcDocDto : appSvcDocDtos){
               LicDocumentRelationDto licDocumentRelationDto = new LicDocumentRelationDto();
               DocumentDto documentDto = new DocumentDto();
               documentDto.setDocName(appSvcDocDto.getDocName());
               documentDto.setDocSize(appSvcDocDto.getDocSize());
               documentDto.setFileRepoId(appSvcDocDto.getFileRepoId());
               documentDto.setSubmitDt(appSvcDocDto.getSubmitDt());
               documentDto.setSubmitBy(appSvcDocDto.getSubmitBy());
               licDocumentRelationDto.setDocumentDto(documentDto);
               LicDocumentDto licDocumentDto = new LicDocumentDto();
               licDocumentDto.setSvcDocId(appSvcDocDto.getSvcDocId());
               licDocumentDto.setDocType(Integer.valueOf(ApplicationConsts.APPLICATION_DOC_TYPE_SERVICE));
               //set the old premises Id ,get the releation when the save.
               String premisesId = getPremisesByAppPremCorreId(appPremisesCorrelationDtos,appSvcDocDto.getAppPremCorreId());
               if(StringUtil.isEmpty(premisesId)){
                   log.info(StringUtil.changeForLog("The premisesId is null"));
                 continue;
               }
               licDocumentDto.setLicPremId(premisesId);
               licDocumentRelationDto.setLicDocumentDto(licDocumentDto);
               licDocumentRelationDtos.add(licDocumentRelationDto);
           }
        }
        return licDocumentRelationDtos;
    }

    private String getPremisesByAppPremCorreId(List<AppPremisesCorrelationDto> appPremisesCorrelationDtos,String appPremCorreId){
        String result = null;
        if(StringUtil.isEmpty(appPremCorreId) || appPremisesCorrelationDtos == null || appPremisesCorrelationDtos.size() == 0){
          return  result;
        }
        for(AppPremisesCorrelationDto appPremisesCorrelationDto : appPremisesCorrelationDtos){
            if(appPremCorreId.equals(appPremisesCorrelationDto.getId())){
                result =  appPremisesCorrelationDto.getAppGrpPremId();
                break;
            }
        }
        return result;

    }

    private String getAppPremCorrecId(List<AppPremisesCorrelationDto> appPremisesCorrelationDtos,String premisesId){
        String result = null;
          if(appPremisesCorrelationDtos == null || appPremisesCorrelationDtos.size() == 0 || StringUtil.isEmpty(premisesId)){
            return  result;
          }
          for(AppPremisesCorrelationDto appPremisesCorrelationDto : appPremisesCorrelationDtos){
              if(premisesId.equals(appPremisesCorrelationDto.getAppGrpPremId())){
                 result = appPremisesCorrelationDto.getId();
              }
          }
          return result;
    }

    private LicenceDto getLicenceDto(String licenceNo,String svcName,String svcType,ApplicationGroupDto applicationGroupDto,
                                     AppPremisesRecommendationDto appPremisesRecommendationDto,
                                     LicenceDto originLicenceDto,
                                     ApplicationDto applicationDto,
                                     List<ApplicationDto> applicationDtos){
        log.info(StringUtil.changeForLog("The  getLicenceDto start ..."));
        LicenceDto licenceDto = new LicenceDto();
        licenceDto.setSvcName(svcName);
        if(!StringUtil.isEmpty(svcType)){
            licenceDto.setSvcType(svcType);
        }
        if(applicationDto!=null && originLicenceDto!=null && ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationDto.getApplicationType())){
            log.info(StringUtil.changeForLog("The  getLicenceDto APPType is RFC ..."));
            licenceDto.setStartDate(originLicenceDto.getStartDate());
            licenceDto.setExpiryDate(originLicenceDto.getExpiryDate());
            //licenceDto.setEndDate(originLicenceDto.getEndDate());
            licenceDto.setGrpLic(originLicenceDto.isGrpLic());
            licenceDto.setOriginLicenceId(originLicenceDto.getId());
            licenceDto.setMigrated(originLicenceDto.isMigrated());
            licenceDto.setLicenceNo(originLicenceDto.getLicenceNo());
            licenceDto.setVersion(originLicenceDto.getVersion()+1);
            licenceDto.setFeeRetroNeeded(originLicenceDto.isFeeRetroNeeded());
            licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_ACTIVE);
            if(applicationGroupDto!=null){
                licenceDto.setLicenseeId(applicationGroupDto.getLicenseeId());
            }
            if(!StringUtil.isEmpty(licenceNo)){
                licenceDto.setLicenceNo(licenceNo);
                licenceDto.setVersion(1);
            }
        }else {
            if(applicationGroupDto!=null){
                Date startDate = applicationGroupDto.getModifiedAt();
                if(applicationDto!=null && originLicenceDto!=null && ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(applicationDto.getApplicationType())){
                    log.info(StringUtil.changeForLog("The  getLicenceDto APPType is Renew ..."));
                    startDate = originLicenceDto.getExpiryDate();
                    log.info(StringUtil.changeForLog("The  getLicenceDto originLicenceDto expiryday is " + startDate));
                    startDate = DateUtils.addDays(startDate,1);
                    log.info(StringUtil.changeForLog("The  getLicenceDto startDate is " + startDate));
                }else if(appPremisesRecommendationDto != null){
                        Date date= appPremisesRecommendationDto.getRecomInDate();
                        if(date !=null){
                            startDate = date;
                        }
                }
                log.debug(StringUtil.changeForLog("The startDate is -->:"+startDate));
                if(startDate == null){
                    startDate = new Date();
                }
                licenceDto.setStartDate(startDate);
                licenceDto.setExpiryDate(LicenceUtil.getExpiryDate(licenceDto.getStartDate(),appPremisesRecommendationDto));
                //licenceDto.setEndDate(licenceDto.getExpiryDate());
                licenceDto.setGrpLic(applicationGroupDto.isGrpLic());
                licenceDto.setLicenseeId(applicationGroupDto.getLicenseeId());
            }
            int version = 1;
            if(originLicenceDto!=null){
                licenceDto.setOriginLicenceId(originLicenceDto.getId());
                licenceDto.setMigrated(originLicenceDto.isMigrated());
            }else{
                licenceDto.setMigrated(false);
            }
            licenceDto.setLicenceNo(licenceNo);
            licenceDto.setVersion(version);
            licenceDto.setFeeRetroNeeded(false);
            //todo:Judge the licence status
            licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_ACTIVE);
        }
        List<ApplicationDto> applicationDtos1 =  IaisCommonUtils.genNewArrayList();
        if(applicationDto!=null){
            applicationDtos1.add(applicationDto);
        }
        if(applicationDtos!=null){
            applicationDtos1.addAll(applicationDtos);
        }
        licenceDto.setApplicationDtos(applicationDtos1);
        log.info(StringUtil.changeForLog("The  getLicenceDto end ..."));
        return licenceDto;
    }

    private LicenseeDto getOrganizationIdBylicenseeId(String licenseeId){
        log.info(StringUtil.changeForLog("The  getOrganizationIdBylicenseeId start ..."));
        //todo:get the organizationid , if do not exist need create the Organizaton.
        String organizationId = "29ABCF6D-770B-EA11-BE7D-000C29F371DC";
        LicenseeDto licenseeDto = null;

        if(!StringUtil.isEmpty(licenseeId)){
            licenseeDto = inspEmailService.getLicenseeDtoById(licenseeId);
            if(licenseeDto != null){
                organizationId = licenseeDto.getOrganizationId();
                if(StringUtil.isEmpty(organizationId)){
                    licenseeDto.setOrganizationId(organizationId);
                }
            }else{
                log.error(StringUtil.changeForLog("This licenseeId can not get he licensee -->:"+licenseeId));
            }
        }else{
            log.error(StringUtil.changeForLog("The  licenseeId is null ..."));
        }
        if(licenseeDto == null){
            licenseeDto = new LicenseeDto();
        }
        log.info(StringUtil.changeForLog("The  getOrganizationIdBylicenseeId end ..."));
       return licenseeDto;
    }


    private Integer getVersionByHciCode(String hciCode){
        Integer result = 1;
        Integer version = hciCodeVersion.get(hciCode);
        if(version==null){
            PremisesDto premisesDto = licenceService.getLatestVersionPremisesByHciCode(hciCode);
            if(premisesDto!= null){
                result = premisesDto.getVersion()+1;
            }
        }else{
            result = version+1;
        }
        hciCodeVersion.put(hciCode,result);
        return result;
    }
    //getAllServiceId
    private List<String> getAllServiceId(List<ApplicationLicenceDto> applicationLicenceDtos){
        log.debug(StringUtil.changeForLog("The getAllServiceId is start ..."));
        List<String> result  = IaisCommonUtils.genNewArrayList();
        Set<String> set = new HashSet();
        if(IaisCommonUtils.isEmpty(applicationLicenceDtos)){
            return  result;
        }
        for (ApplicationLicenceDto applicationLicenceDto : applicationLicenceDtos){
            if(applicationLicenceDto!= null){
                List<ApplicationListDto> applicationListDtoList = applicationLicenceDto.getApplicationListDtoList();
                if(applicationListDtoList != null && applicationListDtoList.size() > 0){
                    for(ApplicationListDto applicationListDto : applicationListDtoList){
                        if(applicationListDto!= null){
                            ApplicationDto applicationDto = applicationListDto.getApplicationDto();
                            set.add(applicationDto.getServiceId());
                        }else{
                            log.warn(StringUtil.changeForLog("There is the null for the ApplicationDto"));
                        }
                    }
                }else{
                    log.warn(StringUtil.changeForLog("There is the null in the List<ApplicationListDto>"));
                }
            }else{
             log.warn(StringUtil.changeForLog("There is the null in the List<ApplicationLicenceDto>"));
            }
        }
        result.addAll(set);
        log.debug(StringUtil.changeForLog("The getAllServiceId is start ..."));
        return  result;
    }

    private HcsaServiceDto getHcsaServiceDtoByServiceId(List<HcsaServiceDto> hcsaServiceDtos,String serviceId){
        HcsaServiceDto result = null;
        if(StringUtil.isEmpty(serviceId)){
           return  result;
        }
        for (HcsaServiceDto hcsaServiceDto : hcsaServiceDtos){
            if(serviceId.equals(hcsaServiceDto.getId())){
                result = hcsaServiceDto;
                break;
            }
        }
        return  result;
    }

    @Setter
    @Getter
    static class GenerateResult{
        private boolean success;
        private String errorMessage;
        private LicenceGroupDto licenceGroupDto;
    }
}
