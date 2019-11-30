package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPremisesScopeAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPremisesScopeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.DocumentDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicDocumentDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicDocumentRelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesScopeAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesScopeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesScopeRelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.SuperLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
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

    public void doBatchJob(){
        log.debug(StringUtil.changeForLog("The LicenceApproveBatchjob is start ..."));
        int day = 0;
       //get can Generate Licence
        List<ApplicationLicenceDto> applicationLicenceDtos =licenceService.getCanGenerateApplications(day);
        if(applicationLicenceDtos == null || applicationLicenceDtos.size() == 0 ){
            log.debug(StringUtil.changeForLog("This time do not have need Generate Licence"));
           return;
        }
        //get the all use serviceCode.
        List<String> serviceIds = getAllServiceId(applicationLicenceDtos);
        List<HcsaServiceDto> hcsaServiceDtos = licenceService.getHcsaServiceById(serviceIds);
        if(hcsaServiceDtos == null || hcsaServiceDtos.size() == 0){
            log.error(StringUtil.changeForLog("This serviceIds can not get the HcsaServiceDto -->:"+serviceIds));
            return;
        }
        List<SuperLicDto> superLicDtos = new ArrayList<>();
        List<String> success = new ArrayList<>();
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
                    toDoResult(superLicDtos,generateResult,success,fail,applicationGroupDto.getGroupNo());
                }
            }
        }
        //create Licence
        superLicDtos = licenceService.createSuperLicDto(superLicDtos);
        //todo:save licence to Fe DB

        log.debug(StringUtil.changeForLog("The LicenceApproveBatchjob is end ..."));
    }

    private void toDoResult(List<SuperLicDto> superLicDtos,GenerateResult generateResult,List<String> success,
                            List<Map<String,String>> fail, String groupNo){
        boolean isSuccess = generateResult.isSuccess();
        if(isSuccess){
            success.add(groupNo);
            superLicDtos.addAll(generateResult.getSuperLicDtos());
        }else{
            Map<String,String> error = new HashMap();
            error.put(groupNo,generateResult.getErrorMessage());
            fail.add(error);
        }
    }

    private GenerateResult generateGroupLicence(ApplicationLicenceDto applicationLicenceDto,List<HcsaServiceDto> hcsaServiceDtos){
        log.debug(StringUtil.changeForLog("The generateGroupLicence is start ..."));
        log.debug(StringUtil.changeForLog("The generateGroupLicence is end ..."));
       return null;
    }
    private GenerateResult generateLIcence(ApplicationLicenceDto applicationLicenceDto,List<HcsaServiceDto> hcsaServiceDtos){
        log.debug(StringUtil.changeForLog("The generateLIcence is start ..."));
        GenerateResult result = new GenerateResult();
        List<ApplicationListDto> applicationListDtoList = applicationLicenceDto.getApplicationListDtoList();
        ApplicationGroupDto applicationGroupDto = applicationLicenceDto.getApplicationGroupDto();
        int inspectionNeed = applicationGroupDto.getIsInspectionNeeded();
        log.debug(StringUtil.changeForLog("The inspectionNeed is -->:"+inspectionNeed));
        int isPreInspection = applicationGroupDto.getIsPreInspection();
        log.debug(StringUtil.changeForLog("The isPreInspection is -->:"+isPreInspection));
        String isPostInspNeeded = AppConsts.NO;
        if(inspectionNeed == 1 && isPreInspection == 0){
            isPostInspNeeded = AppConsts.YES;
        }
        log.debug(StringUtil.changeForLog("The isPostInspNeeded is -->:"+isPostInspNeeded));
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
                List<PremisesDto> premisesDtos  = getPremises(appGrpPremisesEntityDtos,hcsaServiceDto,organizationId);
                superLicDto.setPremisesDtos(premisesDtos);
                //create licence
                 //todo:get the yearLenth.
                int yearLength = 1;
                String licenceNo = licenceService.getLicenceNo(premisesDtos.get(0).getHciCode(),hcsaServiceDto.getSvcCode(),yearLength);
                log.debug(StringUtil.changeForLog("The licenceNo is -->;"+licenceNo));
                if(StringUtil.isEmpty(licenceNo)){
                    errorMessage = "The licenceNo is null .-->:" + premisesDtos.get(0).getHciCode() + ":" + hcsaServiceDto.getSvcCode() + ":" + yearLength;
                    break;
                }
                LicenceDto licenceDto = getLicenceDto(licenceNo,hcsaServiceDto.getSvcName(),applicationGroupDto,yearLength,applicationDto.getOriginLicenceId(),organizationId);
                superLicDto.setLicenceDto(licenceDto);
                 //create lic_premises
                List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = applicationListDto.getAppPremisesCorrelationDtos();
                List<LicPremisesDto> licPremisesDtos = getLicPremisesDto(premisesDtos,appPremisesCorrelationDtos,isPostInspNeeded);
                if(licPremisesDtos == null){
                    errorMessage = "There is  Premises can not get the appPremCorrecId.";
                    break;
                }
                superLicDto.setLicPremisesDtos(licPremisesDtos);
                //create the document and lic_document from the primary doc.
                List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = applicationListDto.getAppGrpPrimaryDocDtos();
                List<AppSvcDocDto> appSvcDocDtos = applicationListDto.getAppSvcDocDtos();
                List<LicDocumentRelationDto> licDocumentRelationDtos = getLicDocumentRelationDto(appGrpPrimaryDocDtos,
                        appSvcDocDtos,appPremisesCorrelationDtos);
                superLicDto.setLicDocumentRelationDto(licDocumentRelationDtos);
                //create LicPremisesScopeDto
                List<AppSvcPremisesScopeDto> appSvcPremisesScopeDtos = applicationListDto.getAppSvcPremisesScopeDtos();
                List<AppSvcPremisesScopeAllocationDto> appSvcPremisesScopeAllocationDtos = applicationListDto.getAppSvcPremisesScopeAllocationDtos();
                List<LicPremisesScopeRelationDto> licPremisesScopeRelationDtos = getLicPremisesScopeRelationDto(appSvcPremisesScopeDtos,
                        appSvcPremisesScopeAllocationDtos,appPremisesCorrelationDtos);
                superLicDto.setLicDocumentRelationDto(licDocumentRelationDtos);

                //
                superLicDtos.add(superLicDto);
            }
            if(StringUtil.isEmpty(errorMessage)){
                result.setSuccess(true);
                result.setSuperLicDtos(superLicDtos);
            }else{
                result.setSuccess(false);
                result.setErrorMessage(errorMessage);
            }
        }
        log.debug(StringUtil.changeForLog("The generateLIcence is end ..."));
        return result;
    }

    private List<LicPremisesScopeRelationDto> getLicPremisesScopeRelationDto(List<AppSvcPremisesScopeDto> appSvcPremisesScopeDtos,
                                                                             List<AppSvcPremisesScopeAllocationDto> appSvcPremisesScopeAllocationDtos,
                                                                             List<AppPremisesCorrelationDto> appPremisesCorrelationDtos){
        List<LicPremisesScopeRelationDto> licPremisesScopeRelationDtos = new ArrayList();
        for(AppSvcPremisesScopeDto appSvcPremisesScopeDto : appSvcPremisesScopeDtos){
            LicPremisesScopeRelationDto licPremisesScopeRelationDto  = new LicPremisesScopeRelationDto();
            LicPremisesScopeDto licPremisesScopeDto = new LicPremisesScopeDto();
            licPremisesScopeDto.setIsSubsumedType(appSvcPremisesScopeDto.getIsSubsumedType());
            licPremisesScopeDto.setScopeName(appSvcPremisesScopeDto.getScopeName());
            String premisesId = getPremisesByAppPremCorreId(appPremisesCorrelationDtos,appSvcPremisesScopeDto.getAppPremCorreId());
            if(StringUtil.isEmpty(premisesId)){
                return  null;
            }
            AppSvcPremisesScopeAllocationDto appSvcPremisesScopeAllocationDto = getAppSvcPremisesScopeAllocationDto(appSvcPremisesScopeAllocationDtos,
                    appSvcPremisesScopeDto.getId());
            if(appSvcPremisesScopeAllocationDto!= null){
                licPremisesScopeRelationDto.setLicPremisesScopeDto(licPremisesScopeDto);
                LicPremisesScopeAllocationDto licPremisesScopeAllocationDto = new LicPremisesScopeAllocationDto();
                licPremisesScopeAllocationDto.setLicCgoId(appSvcPremisesScopeAllocationDto.getAppSvcKeyPsnId());
                licPremisesScopeRelationDto.setLicPremisesScopeAllocationDto(licPremisesScopeAllocationDto);
            }
            licPremisesScopeRelationDtos.add(licPremisesScopeRelationDto);
        }

        return licPremisesScopeRelationDtos;

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
                                                                   List<AppPremisesCorrelationDto> appPremisesCorrelationDtos){
        List<LicDocumentRelationDto> licDocumentRelationDtos = new ArrayList<>();
        if(appGrpPrimaryDocDtos != null){
            for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : appGrpPrimaryDocDtos){
                LicDocumentRelationDto licDocumentRelationDto = new LicDocumentRelationDto();
                DocumentDto documentDto = new DocumentDto();
                documentDto.setDocName(appGrpPrimaryDocDto.getDocName());
                documentDto.setDocSize(appGrpPrimaryDocDto.getDocSize());
                documentDto.setFileRepoId(appGrpPrimaryDocDto.getFileRepoId());
                documentDto.setSubmitDt(appGrpPrimaryDocDto.getSubmitDt());
                documentDto.setSubmitBy(appGrpPrimaryDocDto.getSubmitBy());
                licDocumentRelationDto.setDocumentDto(documentDto);
                LicDocumentDto licDocumentDto = new LicDocumentDto();
                licDocumentDto.setSvcDocId(appGrpPrimaryDocDto.getSvcComDocId());
                //set the old premises Id ,get the releation when the save.
                licDocumentDto.setLicPremCorreId(appGrpPrimaryDocDto.getAppGrpPremId());
                licDocumentRelationDto.setLicDocumentDto(licDocumentDto);
                licDocumentRelationDtos.add(licDocumentRelationDto);
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
               //set the old premises Id ,get the releation when the save.
               String premisesId = getPremisesByAppPremCorreId(appPremisesCorrelationDtos,appSvcDocDto.getAppPremCorreId());
               if(StringUtil.isEmpty(premisesId)){
                 return  null;
               }
               licDocumentDto.setLicPremCorreId(premisesId);
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
    private List<LicPremisesDto> getLicPremisesDto(List<PremisesDto> premisesDtos,List<AppPremisesCorrelationDto> appPremisesCorrelationDtos,
                                                   String isPostInspNeeded){
        List<LicPremisesDto> licPremisesDtos = new ArrayList<>();
        for(PremisesDto premisesDto :premisesDtos ){
            String premisesId = premisesDto.getId();
            String appPremCorrecId = getAppPremCorrecId(appPremisesCorrelationDtos,premisesId);
            if(StringUtil.isEmpty(appPremCorrecId)){
                return null;
            }
            AppPremisesRecommendationDto appPremisesRecommendationDto = licenceService.getTcu(appPremCorrecId);
            LicPremisesDto licPremisesDto = new LicPremisesDto();
            licPremisesDto.setPremisesId(premisesId);
            licPremisesDto.setIsPostInspNeeded(isPostInspNeeded);
            if(appPremisesRecommendationDto == null){
                licPremisesDto.setIsTcuNeeded(AppConsts.NO);
            }else{
                licPremisesDto.setIsTcuNeeded(AppConsts.YES);
                licPremisesDto.setTcuDate(appPremisesRecommendationDto.getRecomInDate());
            }
            licPremisesDtos.add(licPremisesDto);
        }
        return licPremisesDtos;
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
                                     int yearLength,String originLicenceId,String organizationId){
        LicenceDto licenceDto = new LicenceDto();
        licenceDto.setLicenceNo(licenceNo);
        licenceDto.setSvcName(svcName);
        //todo:The latest choose from Giro pay Date, Approved Date,Aso set Date,
        licenceDto.setStartDate(applicationGroupDto.getModifiedAt());
        licenceDto.setExpiryDate(getExpiryDate(licenceDto.getStartDate(),yearLength));
        licenceDto.setIsGrpLic(applicationGroupDto.getIsGrpLic());
        licenceDto.setOrganizationId(organizationId);
        licenceDto.setOriginLicenceId(originLicenceId);
        licenceDto.setIsMigrated(AppConsts.NO);
        licenceDto.setIsFeeRetroNeeded(AppConsts.NO);
        licenceDto.setVersion("1");
        //todo:Judge the licence status
        licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_ACTIVE);
        licenceDto.setLicenseeId(applicationGroupDto.getLicenseeId());
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

    private List<PremisesDto> getPremises(List<AppGrpPremisesEntityDto> appGrpPremisesEntityDtos,HcsaServiceDto hcsaServiceDto,String organizationId){
        List<PremisesDto> premisesDtos = new ArrayList<>();
        for (AppGrpPremisesEntityDto appGrpPremisesEntityDto : appGrpPremisesEntityDtos){
            PremisesDto premisesDto = new PremisesDto();
            //set the old Id when the create use find out the AppPremisesCorrelationDto
            premisesDto.setId(appGrpPremisesEntityDto.getId());
            String hciCode = appGrpPremisesEntityDto.getHciCode();
            if(StringUtil.isEmpty(hciCode)){
                hciCode = licenceService.getHciCode(hcsaServiceDto.getSvcCode());
            }
            premisesDto.setHciName(appGrpPremisesEntityDto.getHciName());
            premisesDto.setHciCode(hciCode);
            premisesDto.setHciContactNo(appGrpPremisesEntityDto.getHciContactNo());
            premisesDto.setScdfRefNo(appGrpPremisesEntityDto.getScdfRefNo());
            premisesDto.setCertIssuedDt(appGrpPremisesEntityDto.getCertIssuedDt());
            premisesDto.setVehicleNo(appGrpPremisesEntityDto.getVehicleNo());
            premisesDto.setPremisesType(appGrpPremisesEntityDto.getPremisesType());
            premisesDto.setPostalCode(appGrpPremisesEntityDto.getPostalCode());
            premisesDto.setAddrType(appGrpPremisesEntityDto.getAddrType());
            premisesDto.setBlkNo(appGrpPremisesEntityDto.getBlkNo());
            premisesDto.setFloorNo(appGrpPremisesEntityDto.getFloorNo());
            premisesDto.setUnitNo(appGrpPremisesEntityDto.getUnitNo());
            premisesDto.setStreetName(appGrpPremisesEntityDto.getStreetName());
            premisesDto.setBuildingName(appGrpPremisesEntityDto.getBuildingName());
            premisesDto.setVersion(getVersionByHciCode(hciCode));
            premisesDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            premisesDto.setOrganizationId(organizationId);

            premisesDtos.add(premisesDto);
        }
        return premisesDtos;
    }
    private String getVersionByHciCode(String hciCode){
        //todo:controller the version
        return "1";
    }
    //getAllServiceId
    private List<String> getAllServiceId(List<ApplicationLicenceDto> applicationLicenceDtos){
        log.debug(StringUtil.changeForLog("The getAllServiceId is start ..."));
        List<String> result  = new ArrayList<>();
        Set<String> set = new HashSet();
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
        private List<SuperLicDto> superLicDtos;
    }
}
