package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPersonnelExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesScopeAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesScopeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicSvcSpecificPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.SuperLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationGroupService;
import com.ecquaria.cloud.moh.iais.service.LicenceService;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
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

    private Map<String,Integer> hciCodeVersion = new HashMap();
    private Map<String,Integer> keyPersonnelVersion = new HashMap<>();

    public void doBatchJob(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("The LicenceApproveBatchjob is start ..."));
        int day = 0;
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
        List<LicenceGroupDto> licenceGroupDtos = new ArrayList<>();
        List<ApplicationGroupDto> success = new ArrayList<>();
        List<Map<String,String>> fail = new ArrayList<>();
        for(ApplicationLicenceDto applicationLicenceDto : applicationLicenceDtos ){
            if(applicationLicenceDto != null){
                ApplicationGroupDto applicationGroupDto = applicationLicenceDto.getApplicationGroupDto();
                if(applicationGroupDto != null){
                    int isGrpLic = applicationGroupDto.getIsGrpLic();
                    log.debug(StringUtil.changeForLog("The application group no is -->;"+applicationGroupDto.getGroupNo()) );
                    log.debug(StringUtil.changeForLog("The isGrpLic is -->;"+isGrpLic));
                    GenerateResult generateResult;
                    if(AppConsts.YES.equals(String.valueOf(isGrpLic))){
                        //generate the Group licence
                        generateResult = generateGroupLicence(applicationLicenceDto,hcsaServiceDtos);
                    }else{
                        //generate licence
                        generateResult = generateLIcence(applicationLicenceDto,hcsaServiceDtos);
                    }
                    toDoResult(licenceGroupDtos,generateResult,success,fail,applicationGroupDto);
                }
            }
        }
        //
        AuditTrailDto auditTrailDto = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTRANET);
        String eventRefNo = EventBusHelper.getEventRefNo();
        EventBusLicenceGroupDtos eventBusLicenceGroupDtos = new EventBusLicenceGroupDtos();
        eventBusLicenceGroupDtos.setEventRefNo(eventRefNo);
        eventBusLicenceGroupDtos.setLicenceGroupDtos(licenceGroupDtos);
        eventBusLicenceGroupDtos.setAuditTrailDto(auditTrailDto);
        //step1 create Licence to BE DB
         licenceService.createSuperLicDto(eventBusLicenceGroupDtos);

        //if create licence success
        //todo:update the success application group.
           if(success.size() > 0){
               //get the application
               List<ApplicationDto> applicationDtos =getApplications(licenceGroupDtos);
               //
               EventApplicationGroupDto eventApplicationGroupDto = new EventApplicationGroupDto();
               eventApplicationGroupDto.setEventRefNo(eventRefNo);
               eventApplicationGroupDto.setRollBackApplicationGroupDtos(success);
               success = updateStatusToGenerated(success);
               eventApplicationGroupDto.setApplicationGroupDtos(success);
               eventApplicationGroupDto.setAuditTrailDto(auditTrailDto);
               applicationGroupService.updateEventApplicationGroupDto(eventApplicationGroupDto);
               //step2 save licence to Fe DB
              // licenceGroupDtos =licenceService.createFESuperLicDto(licenceGroupDtos);
           }
        //todo:send the email to admin for fail Data.
        //else{ rollback step1}
        //step2 save licence to Fe DB
         //if(step2) success  completed
        //else{roll back step1 and step 2}

        log.debug(StringUtil.changeForLog("The LicenceApproveBatchjob is end ..."));
    }
   private List<ApplicationDto> getApplications(List<LicenceGroupDto> licenceGroupDtos){
       List<ApplicationDto> result = new ArrayList<>();
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
        List<ApplicationGroupDto> result = new ArrayList<>();
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
            }
        }

    }

    private Map<String,List<ApplicationListDto>> tidyAppForGroupLicence(List<ApplicationListDto> applicationListDtoList){
        Map<String,List<ApplicationListDto>> result = new HashMap<>();
        if(IaisCommonUtils.isEmpty(applicationListDtoList)){
            return  result;
        }
        for(ApplicationListDto applicationListDto : applicationListDtoList){
            ApplicationDto applicationDto = applicationListDto.getApplicationDto();
            String serviceId = applicationDto.getServiceId();
            log.debug(StringUtil.changeForLog("The serviceId is -->:" + serviceId));
            List<ApplicationListDto> applicationListDtos = result.get(serviceId);
            if(applicationListDtos != null){
                applicationListDtos.add(applicationListDto);
            }else{
                applicationListDtos = new ArrayList<>();
                applicationListDtos.add(applicationListDto);
            }
            result.put(serviceId,applicationListDtos);
        }
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

        //get organizationId
        String organizationId = getOrganizationIdBylicenseeId(applicationGroupDto.getLicenseeId());
        log.debug(StringUtil.changeForLog("The organizationId is -->:"+organizationId));
        if(applicationListDtoList == null || applicationListDtoList.size() == 0){
            result.setSuccess(false);
            result.setErrorMessage("The applicationListDtoList is null ...");
        }else{
            log.debug(StringUtil.changeForLog("The applicationListDtoList size is -->:"+applicationListDtoList.size()));
            //tidy up Application for Group Licence use
            Map<String,List<ApplicationListDto>> applications = tidyAppForGroupLicence(applicationListDtoList);
            List<SuperLicDto> superLicDtos = new ArrayList<>();
            String errorMessage = null;
            if(applications.size()<=0){
                return result;
            }
            for (String key : applications.keySet()){
                SuperLicDto superLicDto = new SuperLicDto();
                List<ApplicationListDto> applicationListDtos = applications.get(key);
                if(IaisCommonUtils.isEmpty(applicationListDtos)){
                    continue;
                }
                //get service code
                log.debug(StringUtil.changeForLog("The serviceId is -->:" + key));
                HcsaServiceDto hcsaServiceDto = getHcsaServiceDtoByServiceId(hcsaServiceDtos,key);
                if(hcsaServiceDto ==  null){
                    errorMessage = "This ServiceId can not get the HcsaServiceDto -->:"+key;
                    break;
                }
                //create licence
                //todo:get the yearLenth.
                int yearLength = 1;
                String licenceNo = licenceService.getGroupLicenceNo(hcsaServiceDto.getSvcCode(),yearLength);
                log.debug(StringUtil.changeForLog("The licenceNo is -->;"+licenceNo));
                if(StringUtil.isEmpty(licenceNo)){
                    errorMessage = "The licenceNo is null .-->:" + hcsaServiceDto.getSvcCode() + ":" + applicationListDtos.size() + ":" + yearLength;
                    break;
                }
                List<ApplicationDto> applicationDtos =  getApplicationDtos(applicationListDtos);
                LicenceDto licenceDto = getLicenceDto(licenceNo,hcsaServiceDto.getSvcName(),applicationGroupDto,yearLength,
                        applicationDtos.get(0).getOriginLicenceId(),organizationId,null,applicationDtos);
                superLicDto.setLicenceDto(licenceDto);
                //
                List<PremisesGroupDto> premisesGroupDtos = new ArrayList<>();
                List<LicAppCorrelationDto> licAppCorrelationDtos = new ArrayList<>();
                List<LicDocumentRelationDto> licDocumentRelationDtos = new ArrayList<>();
                List<PersonnelsDto> personnelsDtos = new ArrayList<>();
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

                    List<PremisesGroupDto> premisesGroupDtos1 = getPremisesGroupDto(appGrpPremisesEntityDtos,appPremisesCorrelationDtos,appSvcPremisesScopeDtos,
                            appSvcPremisesScopeAllocationDtos, hcsaServiceDto,organizationId,isPostInspNeeded);
                    PremisesGroupDto premisesGroupDto =premisesGroupDtos1.get(0);
                    if(premisesGroupDto == null || premisesGroupDto.isHasError()){
                        errorMessage = premisesGroupDto.getErrorMessage();
                        break;
                    }
                    premisesGroupDtos.addAll(premisesGroupDtos1);

                    //create the lic_app_correlation
                    LicAppCorrelationDto licAppCorrelationDto = new LicAppCorrelationDto();
                    licAppCorrelationDto.setApplicationId(applicationListDto.getApplicationDto().getId());
                    licAppCorrelationDtos.add(licAppCorrelationDto);
                    superLicDto.setLicAppCorrelationDtos(licAppCorrelationDtos);
                    //create LicFeeGroupItemDto
                    List<LicFeeGroupItemDto> licFeeGroupItemDtos = new ArrayList<>();
                    LicFeeGroupItemDto licFeeGroupItemDto = new LicFeeGroupItemDto();
                    licFeeGroupItemDtos.add(licFeeGroupItemDto);
                    superLicDto.setLicFeeGroupItemDtos(licFeeGroupItemDtos);
                    //create the document and lic_document from the primary doc.
                    List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = applicationListDto.getAppGrpPrimaryDocDtos();
                    List<AppSvcDocDto> appSvcDocDtos = applicationListDto.getAppSvcDocDtos();
                    List<LicDocumentRelationDto> licDocumentRelationDto1s = getLicDocumentRelationDto(appGrpPrimaryDocDtos,
                            appSvcDocDtos,appPremisesCorrelationDtos,premisesGroupDtos);
                    licDocumentRelationDtos.addAll(licDocumentRelationDto1s);

                    //create key_personnel key_personnel_ext lic_key_personnel
                    List<AppGrpPersonnelDto> appGrpPersonnelDtos = applicationListDto.getAppGrpPersonnelDtos();
                    List<AppGrpPersonnelExtDto> appGrpPersonnelExtDtos = applicationListDto.getAppGrpPersonnelExtDtos();
                    List<AppSvcKeyPersonnelDto> appSvcKeyPersonnelDtos = applicationListDto.getAppSvcKeyPersonnelDtos();
                    if(appSvcKeyPersonnelDtos == null || appSvcKeyPersonnelDtos.size() == 0){
                        errorMessage = "There is not the AppSvcPersonnel for this applicaiton -->: "+applicationListDto.getApplicationDto().getApplicationNo();
                        break;
                    }
                    List<PersonnelsDto> personnelsDto1s = getPersonnelsDto(appGrpPersonnelDtos,appGrpPersonnelExtDtos,appSvcKeyPersonnelDtos,organizationId);
                    if(personnelsDtos == null){
                        errorMessage = "There is Error for AppGrpPersonnel -->: "+applicationListDto.getApplicationDto().getApplicationNo();
                        break;
                    }
                    personnelsDtos.addAll(personnelsDto1s);

                    //create the lic_fee_group_item
                    //do not need create in the Dto
                    //todo:lic_base_specified_correlation
                    //
                }
                superLicDto.setPremisesGroupDtos(premisesGroupDtos);
                superLicDto.setLicAppCorrelationDtos(licAppCorrelationDtos);
                superLicDto.setLicDocumentRelationDto(licDocumentRelationDtos);
                superLicDto.setPersonnelsDtos(personnelsDtos);
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

    private List<ApplicationDto> getApplicationDtos(List<ApplicationListDto> applicationListDtos){
        List<ApplicationDto> result = new ArrayList<>();
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

        //get organizationId
        String organizationId = getOrganizationIdBylicenseeId(applicationGroupDto.getLicenseeId());
        log.debug(StringUtil.changeForLog("The organizationId is -->:"+organizationId));

        if(applicationListDtoList == null || applicationListDtoList.size() == 0){
            result.setSuccess(false);
            result.setErrorMessage("The applicationListDtoList is null ...");
        }else{
            log.debug(StringUtil.changeForLog("The applicationListDtoList size is -->:"+applicationListDtoList.size()));

            List<SuperLicDto> superLicDtos = new ArrayList<>();
            String errorMessage = null;
            for(ApplicationListDto applicationListDto : applicationListDtoList){
                SuperLicDto superLicDto = new SuperLicDto();
                //get service code
                ApplicationDto applicationDto = applicationListDto.getApplicationDto();
                if(applicationDto == null){
                    errorMessage = "There is a ApplicationDto is null";
                    break;
                }
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

                List<PremisesGroupDto> premisesGroupDtos = getPremisesGroupDto(appGrpPremisesEntityDtos,appPremisesCorrelationDtos,appSvcPremisesScopeDtos,
                        appSvcPremisesScopeAllocationDtos, hcsaServiceDto,organizationId,isPostInspNeeded);
                PremisesGroupDto premisesGroupDto =premisesGroupDtos.get(0);
                if(premisesGroupDto == null || premisesGroupDto.isHasError()){
                    errorMessage = premisesGroupDto.getErrorMessage();
                    break;
                }
                superLicDto.setPremisesGroupDtos(premisesGroupDtos);
                //create licence
                 //todo:get the yearLenth.
                int yearLength = 1;
                String licenceNo = licenceService.getLicenceNo(premisesGroupDto.getPremisesDto().getHciCode(),hcsaServiceDto.getSvcCode(),yearLength);
                log.debug(StringUtil.changeForLog("The licenceNo is -->;"+licenceNo));
                if(StringUtil.isEmpty(licenceNo)){
                    errorMessage = "The licenceNo is null .-->:" + premisesGroupDto.getPremisesDto().getHciCode() + ":" + hcsaServiceDto.getSvcCode() + ":" + yearLength;
                    break;
                }
                LicenceDto licenceDto = getLicenceDto(licenceNo,hcsaServiceDto.getSvcName(),applicationGroupDto,yearLength,
                        applicationDto.getOriginLicenceId(),organizationId,applicationDto,null);
                superLicDto.setLicenceDto(licenceDto);
                //create the lic_app_correlation
                List<LicAppCorrelationDto> licAppCorrelationDtos = new ArrayList<>();
                LicAppCorrelationDto licAppCorrelationDto = new LicAppCorrelationDto();
                licAppCorrelationDto.setApplicationId(applicationDto.getId());
                licAppCorrelationDtos.add(licAppCorrelationDto);
                superLicDto.setLicAppCorrelationDtos(licAppCorrelationDtos);
                //create LicFeeGroupItemDto
                List<LicFeeGroupItemDto> licFeeGroupItemDtos = new ArrayList<>();
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
                if(appSvcKeyPersonnelDtos == null || appSvcKeyPersonnelDtos.size() == 0){
                    errorMessage = "There is not the AppSvcPersonnel for this applicaiton -->: "+applicationDto.getApplicationNo();
                    break;
                }
                List<PersonnelsDto> personnelsDtos = getPersonnelsDto(appGrpPersonnelDtos,appGrpPersonnelExtDtos,appSvcKeyPersonnelDtos,organizationId);
                if(personnelsDtos == null){
                    errorMessage = "There is Error for AppGrpPersonnel -->: "+applicationDto.getApplicationNo();
                    break;
                }
                superLicDto.setPersonnelsDtos(personnelsDtos);
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

    private List<LicSvcSpecificPersonnelDto> getLicSvcSpecificPersonnelDtos(List<AppSvcPersonnelDto> appSvcPersonnelDtos){
        List<LicSvcSpecificPersonnelDto> result = new ArrayList<>();
        if(!IaisCommonUtils.isEmpty(appSvcPersonnelDtos)){
           for (AppSvcPersonnelDto appSvcPersonnelDto : appSvcPersonnelDtos){
               LicSvcSpecificPersonnelDto licSvcSpecificPersonnelDto = MiscUtil.transferEntityDto(appSvcPersonnelDto,LicSvcSpecificPersonnelDto.class);
               result.add(licSvcSpecificPersonnelDto);
           }
        }
        return  result;
    }


    private List<PremisesGroupDto> getPremisesGroupDto(List<AppGrpPremisesEntityDto> appGrpPremisesEntityDtos,
                                                       List<AppPremisesCorrelationDto> appPremisesCorrelationDtos,
                                                       List<AppSvcPremisesScopeDto> appSvcPremisesScopeDtos,
                                                       List<AppSvcPremisesScopeAllocationDto> appSvcPremisesScopeAllocationDtos,
                                                       HcsaServiceDto hcsaServiceDto,
                                                       String organizationId,
                                                       Integer isPostInspNeeded){
        List<PremisesGroupDto> reuslt = new ArrayList<>();
        if (IaisCommonUtils.isEmpty(appGrpPremisesEntityDtos)){
            return  reuslt;
        }
        for (AppGrpPremisesEntityDto appGrpPremisesEntityDto : appGrpPremisesEntityDtos){
            PremisesGroupDto premisesGroupDto = new PremisesGroupDto();
            premisesGroupDto.setHasError(false);
            //premises
            PremisesDto premisesDto = new PremisesDto();
            String hciCode = appGrpPremisesEntityDto.getHciCode();
            if(StringUtil.isEmpty(hciCode)){
                hciCode = licenceService.getHciCode(hcsaServiceDto.getSvcCode());
            }
            premisesDto = MiscUtil.transferEntityDto(appGrpPremisesEntityDto,PremisesDto.class);
            premisesDto.setHciCode(hciCode);
            premisesDto.setVersion(getVersionByHciCode(hciCode));
            premisesDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            premisesDto.setOrganizationId(organizationId);
            premisesGroupDto.setPremisesDto(premisesDto);
            //create lic_premises
            String premisesId = appGrpPremisesEntityDto.getId();
            String appPremCorrecId = getAppPremCorrecId(appPremisesCorrelationDtos,premisesId);
            if(StringUtil.isEmpty(appPremCorrecId)){
                premisesGroupDto.setHasError(true);
                premisesGroupDto.setErrorMessage("This PremisesId can not find out appPremCorrecId -->:"+premisesId);
                reuslt.clear();
                reuslt.add(premisesGroupDto);
                break;
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
            //create LicPremisesScopeDto
            AppSvcPremisesScopeDto appSvcPremisesScopeDto = getAppSvcPremisesScopeDtoByCorrelationId(appSvcPremisesScopeDtos,appPremCorrecId);
            if(appSvcPremisesScopeDto == null){
                premisesGroupDto.setHasError(true);
                premisesGroupDto.setErrorMessage("This appPremCorrecId can not find out AppSvcPremisesScopeDto -->:"+appPremCorrecId);
                reuslt.clear();
                reuslt.add(premisesGroupDto);
                break;
            }
            LicPremisesScopeDto licPremisesScopeDto = new LicPremisesScopeDto();
            licPremisesScopeDto.setSubsumedType(appSvcPremisesScopeDto.isSubsumedType());
            licPremisesScopeDto.setScopeName(appSvcPremisesScopeDto.getScopeName());
            premisesGroupDto.setLicPremisesScopeDto(licPremisesScopeDto);
            //create LicPremisesScopeAllocationDto
            AppSvcPremisesScopeAllocationDto appSvcPremisesScopeAllocationDto = getAppSvcPremisesScopeAllocationDto(appSvcPremisesScopeAllocationDtos,
                    appSvcPremisesScopeDto.getId());
            if(appSvcPremisesScopeAllocationDto!= null){
                LicPremisesScopeAllocationDto licPremisesScopeAllocationDto = new LicPremisesScopeAllocationDto();
                licPremisesScopeAllocationDto.setLicCgoId(appSvcPremisesScopeAllocationDto.getAppSvcKeyPsnId());
                premisesGroupDto.setLicPremisesScopeAllocationDto(licPremisesScopeAllocationDto);
            }
            reuslt.add(premisesGroupDto);
        }

        return  reuslt;
    }


  private AppSvcPremisesScopeDto getAppSvcPremisesScopeDtoByCorrelationId(List<AppSvcPremisesScopeDto> appSvcPremisesScopeDtos,String appPremCorrecId){
      AppSvcPremisesScopeDto result = null;
        if(appSvcPremisesScopeDtos == null || appSvcPremisesScopeDtos.size() == 0 || StringUtil.isEmpty(appPremCorrecId)){
          return result;
        }

      for(AppSvcPremisesScopeDto appSvcPremisesScopeDto : appSvcPremisesScopeDtos){
         if(appPremCorrecId.equals(appSvcPremisesScopeDto.getAppPremCorreId())){
             result = appSvcPremisesScopeDto;
             break;
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
        Integer isPostInspNeeded = Integer.parseInt(AppConsts.NO);
        if(inspectionNeed == 1 && isPreInspection == 0){
            isPostInspNeeded = Integer.parseInt(AppConsts.YES);
        }
        log.debug(StringUtil.changeForLog("The isPostInspNeeded is end ..."));
        return isPostInspNeeded;
    }
    private List<PersonnelsDto> getPersonnelsDto(List<AppGrpPersonnelDto> appGrpPersonnelDtos,List<AppGrpPersonnelExtDto> appGrpPersonnelExtDtos,
                                                 List<AppSvcKeyPersonnelDto> appSvcKeyPersonnelDtos,
                                                 String organizationId){
        List<PersonnelsDto> result = new ArrayList<>();
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
        List<LicDocumentRelationDto> licDocumentRelationDtos = new ArrayList<>();
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
                        licDocumentDto.setDocType(Integer.parseInt(ApplicationConsts.APPLICATION_DOC_TYPE_PARIMARY));
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
               licDocumentDto.setDocType(Integer.parseInt(ApplicationConsts.APPLICATION_DOC_TYPE_SERVICE));
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

    private LicenceDto getLicenceDto(String licenceNo,String svcName,ApplicationGroupDto applicationGroupDto,
                                     int yearLength,String originLicenceId,String organizationId,
                                     ApplicationDto applicationDto,
                                     List<ApplicationDto> applicationDtos){
        LicenceDto licenceDto = new LicenceDto();
        licenceDto.setLicenceNo(licenceNo);
        licenceDto.setSvcName(svcName);
        //todo:The latest choose from Giro pay Date, Approved Date,Aso set Date,
        Date startDate = applicationGroupDto.getModifiedAt();
        log.debug(StringUtil.changeForLog("The startDate is -->:"+startDate));
        if(startDate == null){
            startDate = new Date();
        }
        licenceDto.setStartDate(startDate);
        licenceDto.setExpiryDate(getExpiryDate(licenceDto.getStartDate(),yearLength));
        licenceDto.setIsGrpLic(applicationGroupDto.getIsGrpLic());
        licenceDto.setOrganizationId(organizationId);
        licenceDto.setOriginLicenceId(originLicenceId);
        licenceDto.setIsMigrated(Integer.parseInt(AppConsts.NO));
        licenceDto.setIsFeeRetroNeeded(Integer.parseInt(AppConsts.NO));
        licenceDto.setVersion(1);
        //todo:Judge the licence status
        licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_ACTIVE);
        licenceDto.setLicenseeId(applicationGroupDto.getLicenseeId());
        List<ApplicationDto> applicationDtos1 =  new ArrayList<>();
        if(applicationDto!=null){
            applicationDtos1.add(applicationDto);
        }
        if(applicationDtos!=null){
            applicationDtos1.addAll(applicationDtos);
        }
        licenceDto.setApplicationDtos(applicationDtos1);
        return licenceDto;
    }
    private String getOrganizationIdBylicenseeId(String licenseeId){
        //todo:get the organizationid , if do not exist need create the Organizaton.
       return "29ABCF6D-770B-EA11-BE7D-000C29F371DC";
    }

    private Date getExpiryDate(Date startDate, int yearLength){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.YEAR,yearLength);
        return  calendar.getTime();
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
        List<String> result  = new ArrayList<>();
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
    class GenerateResult{
        private boolean success;
        private String errorMessage;
        private LicenceGroupDto licenceGroupDto;
    }
}
