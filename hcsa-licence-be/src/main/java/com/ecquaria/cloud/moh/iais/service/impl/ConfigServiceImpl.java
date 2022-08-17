package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.CategoryDisciplineErrorsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaConfigPageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCategoryDisciplineDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCategoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceSubServiceErrorsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceSubServicePageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceSubTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcCateWrkgrpCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageCompoundDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageCompoundForDbDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpePremisesTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpeRoutingSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpecificStageWorkloadDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpecifiedCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.service.ConfigService;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Wenkang
 * @date 2020/2/12 17:36
 */
@Service
@Slf4j
public class ConfigServiceImpl implements ConfigService {
    
    private static final String NEW_APPLICATION="New Application";
    private static final String  APPEAL="Appeal";
    private static final String REQUEST_FOR_CHANGE="Request For Change";
    private static final String RENEW="Renew";
    private static final String CESSATION ="Cessation";
    private static final String SUSPENSION="Suspension";
    private static final String WITHDRAWAL="Withdrawal";

    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Value("${iais.email.sender}")
    private String mailSender;
    @Autowired
    private BeEicGatewayClient beEicGatewayClient;



    @Override
    public List<HcsaServiceDto> getAllHcsaServices() {
        List<HcsaServiceDto> entity = hcsaConfigClient.allHcsaService().getEntity();
        Collections.sort(entity, (o1, o2) -> {
            if(o1.getSvcName().equals(o2.getSvcName())){
               return (int) (Double.parseDouble(o1.getVersion())-Double.parseDouble(o2.getVersion()));
            }else {
                return o1.getSvcName().compareTo(o2.getSvcName());
            }
        });
        return entity;
    }

    @Override
    public List<HcsaServiceDto> getActiveServicesBySvcType(String svcType) {
        return hcsaConfigClient.getActiveServicesBySvcType(svcType).getEntity();
    }

    @Override
    public List<HcsaServiceDto> getServicesBySvcCode(String svcCode) {
        log.info(StringUtil.changeForLog("The getServicesBySvcCode start ..."));
        log.info(StringUtil.changeForLog("The getServicesBySvcCode svcCode is -->:"+svcCode));
        List<HcsaServiceDto> hcsaServiceDtos = IaisCommonUtils.genNewArrayList();
        if(StringUtil.isNotEmpty(svcCode)){
            hcsaServiceDtos = hcsaConfigClient.getServiceVersions(svcCode).getEntity();
        }
        log.info(StringUtil.changeForLog("The getServicesBySvcCode end ..."));
        return hcsaServiceDtos;
    }

    @Override
    public HcsaServiceConfigDto getHcsaServiceConfigDtoByServiceId(String serviceId) {
        log.info(StringUtil.changeForLog("The getHcsaServiceConfigDtoByServiceId start ..."));
        log.info(StringUtil.changeForLog("The getHcsaServiceConfigDtoByServiceId serviceId is -->:"+serviceId));
        HcsaServiceConfigDto hcsaServiceConfigDto = hcsaConfigClient.getHcsaServiceConfigByServiceId(serviceId).getEntity();
        if(hcsaServiceConfigDto != null){
            //for HcsaSvcSpePremisesTypeDto
            List<HcsaSvcSpePremisesTypeDto> hcsaSvcSpePremisesTypeDtos = hcsaServiceConfigDto.getHcsaSvcSpePremisesTypeDtos();
            if(IaisCommonUtils.isNotEmpty(hcsaSvcSpePremisesTypeDtos)){
                List<String> premisTypes = IaisCommonUtils.genNewArrayList();
                Map<String,HcsaServiceCategoryDisciplineDto> hcsaServiceCategoryDisciplineDtoMap = IaisCommonUtils.genNewHashMap();
                Map<String,HcsaServiceSubServicePageDto> specHcsaServiceSubServicePageDtoMap = IaisCommonUtils.genNewHashMap();
                Map<String,HcsaServiceSubServicePageDto> otherHcsaServiceSubServicePageDtoMap = IaisCommonUtils.genNewHashMap();
                for(HcsaSvcSpePremisesTypeDto hcsaSvcSpePremisesTypeDto : hcsaSvcSpePremisesTypeDtos){
                    String premisType = hcsaSvcSpePremisesTypeDto.getPremisesType();
                    log.info(StringUtil.changeForLog("The getHcsaServiceConfigDtoByServiceId premisType is -->:"+premisType));
                    premisTypes.add(premisType);
                    //HcsaServiceCategoryDisciplineDto
                    addHcsaServiceCategoryDisciplineDtoMap(hcsaServiceCategoryDisciplineDtoMap,hcsaSvcSpePremisesTypeDto.getCategorySectionName(),
                            hcsaSvcSpePremisesTypeDto.getHcsaServiceSubTypeDtos(),premisType);
                    //specHcsaServiceSubServicePageDtoMap
                    addHcsaServiceSubServicePageDtoMap(specHcsaServiceSubServicePageDtoMap,hcsaSvcSpePremisesTypeDto.getSpecialSvcSecName(),
                            hcsaSvcSpePremisesTypeDto.getSpecHcsaSvcSpecifiedCorrelationDtos(),premisType);
                    //otherHcsaServiceSubServicePageDtoMap
                    addHcsaServiceSubServicePageDtoMap(otherHcsaServiceSubServicePageDtoMap,null,
                            hcsaSvcSpePremisesTypeDto.getOtherHcsaSvcSpecifiedCorrelationDtos(),premisType);
                }
                hcsaServiceConfigDto.setOtherHcsaServiceSubServicePageDtoMap(otherHcsaServiceSubServicePageDtoMap);
                hcsaServiceConfigDto.setSpecHcsaServiceSubServicePageDtoMap(specHcsaServiceSubServicePageDtoMap);
                hcsaServiceConfigDto.setHcsaServiceCategoryDisciplineDtoMap(hcsaServiceCategoryDisciplineDtoMap);
                hcsaServiceConfigDto.setPremisesTypes(premisTypes.toArray(new String[premisTypes.size()]));
            }else{
                log.info(StringUtil.changeForLog("The hcsaSvcSpePremisesTypeDtos is null"));
            }
            //for rounting
            HcsaSvcRoutingStageCompoundForDbDto hcsaSvcRoutingStageCompoundForDbDto = hcsaServiceConfigDto.getHcsaSvcRoutingStageCompoundForDbDto();
            if(hcsaSvcRoutingStageCompoundForDbDto != null){
                 if(IaisCommonUtils.isNotEmpty(hcsaSvcRoutingStageCompoundForDbDto.getHcsaSvcRoutingStageDtos())){
                     Map<String, List<HcsaConfigPageDto>> hcsaConfigPageDtoMap =   getHcsaConfigPageDto();
                     for(String appType : hcsaConfigPageDtoMap.keySet()){
                         log.info(StringUtil.changeForLog("The getHcsaServiceConfigDtoByServiceId appType is -->:"+appType));
                         if(!ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(appType)
                                 && !ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION.equals(appType)){
                             List<HcsaConfigPageDto> hcsaConfigPageDtos = hcsaConfigPageDtoMap.get(appType);
                             for(HcsaConfigPageDto hcsaConfigPageDto : hcsaConfigPageDtos){
                                 HcsaSvcRoutingStageCompoundDto hcsaSvcRoutingStageCompoundDto = getHcsaSvcRoutingStageCompoundDto(
                                         hcsaSvcRoutingStageCompoundForDbDto, appType,hcsaConfigPageDto.getStageId(),1);


                                 HcsaSvcSpeRoutingSchemeDto hcsaSvcSpeRoutingSchemeDto = hcsaSvcRoutingStageCompoundDto.getHcsaSvcSpeRoutingSchemeDto();
                                 if(hcsaSvcSpeRoutingSchemeDto == null){
                                     hcsaConfigPageDto.setIsMandatory("optional");
                                 }else {
                                     hcsaConfigPageDto.setIsMandatory("mandatory");
                                     hcsaConfigPageDto.setRoutingSchemeName(hcsaSvcSpeRoutingSchemeDto.getSchemeType());

                                     HcsaSvcRoutingStageDto hcsaSvcRoutingStageDto = hcsaSvcRoutingStageCompoundDto.getHcsaSvcRoutingStageDto();
                                     hcsaConfigPageDto.setCanApprove(hcsaSvcRoutingStageDto.getCanApprove());

                                     HcsaSvcSpecificStageWorkloadDto hcsaSvcSpecificStageWorkloadDto = hcsaSvcRoutingStageCompoundDto.getHcsaSvcSpecificStageWorkloadDto();
                                     hcsaConfigPageDto.setManhours(hcsaSvcSpecificStageWorkloadDto.getManhourCount());

                                     //for ins
                                     List<HcsaSvcSpeRoutingSchemeDto> hcsaSvcSpeRoutingSchemeDtos = hcsaConfigPageDto.getHcsaSvcSpeRoutingSchemeDtos();
                                     if(IaisCommonUtils.isNotEmpty(hcsaSvcSpeRoutingSchemeDtos)){
                                         for(HcsaSvcSpeRoutingSchemeDto hcsaSvcSpeRoutingSchemeDtoIns : hcsaSvcSpeRoutingSchemeDtos){
                                             HcsaSvcRoutingStageCompoundDto hcsaSvcRoutingStageCompoundDtoIns = getHcsaSvcRoutingStageCompoundDto(
                                                     hcsaSvcRoutingStageCompoundForDbDto, appType,hcsaConfigPageDto.getStageId(),Integer.parseInt(hcsaSvcSpeRoutingSchemeDtoIns.getInsOder()));
                                             HcsaSvcSpeRoutingSchemeDto hcsaSvcSpeRoutingSchemeDtoInsDb = hcsaSvcRoutingStageCompoundDtoIns.getHcsaSvcSpeRoutingSchemeDto();
                                             hcsaSvcSpeRoutingSchemeDtoIns.setSchemeType(hcsaSvcSpeRoutingSchemeDtoInsDb.getSchemeType());
                                         }
                                     }
                                 }
                             }
                         }

                     }
                     hcsaServiceConfigDto.setHcsaConfigPageDtoMap(hcsaConfigPageDtoMap);
                 }
            }
        }else{
            log.error(StringUtil.changeForLog("The hcsaServiceConfigDto is null"));
        }
        log.info(StringUtil.changeForLog("The getHcsaServiceConfigDtoByServiceId end ..."));
        return hcsaServiceConfigDto;
    }


    @Override
    public Map<String, List<HcsaConfigPageDto>> getHcsaConfigPageDto() {
        Map<String, List<HcsaConfigPageDto>> result = this.getEmptyHcsaConfigPageDto();
        return result;
    }



    @Override
    public void doDeleteService(String serviceId) {
        log.info(StringUtil.changeForLog("The doDeleteService start ..."));
        hcsaConfigClient.updateService(serviceId);
        HcsaServiceConfigDto hcsaServiceConfigDto=new HcsaServiceConfigDto();
        HcsaServiceDto hcsaServiceDto = new HcsaServiceDto();
        hcsaServiceDto.setId(serviceId);
        hcsaServiceDto.setUseDelete(true);
        hcsaServiceConfigDto.setHcsaServiceDto(hcsaServiceDto);
        eicGateway(hcsaServiceConfigDto);
        log.info(StringUtil.changeForLog("The doDeleteService end ..."));
    }


    private static List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtos;
    @Override
    public synchronized List<HcsaSvcRoutingStageDto> getHcsaSvcRoutingStageDtos() {
        if (hcsaSvcRoutingStageDtos == null) {
            List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDto = hcsaConfigClient.stagelist().getEntity();
            for (int i = 0; i < hcsaSvcRoutingStageDto.size(); i++) {
                String stageOrder = hcsaSvcRoutingStageDto.get(i).getStageOrder();
                try {
                    if (Integer.parseInt(stageOrder) % 100 != 0) {
                        hcsaSvcRoutingStageDto.remove(i);
                        i--;
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
            hcsaSvcRoutingStageDtos = hcsaSvcRoutingStageDto;
        }
        return hcsaSvcRoutingStageDtos;
    }

    private CopyOnWriteArrayList<HcsaServiceCategoryDto> hcsaServiceCatgoryDtos;
    @Override
    public List<HcsaServiceCategoryDto> getHcsaServiceCategoryDto() {
        if(hcsaServiceCatgoryDtos == null){
            hcsaServiceCatgoryDtos = hcsaConfigClient.getHcsaServiceCategorys().getEntity();
        }
        return hcsaServiceCatgoryDtos;
    }



    @Override
    public HcsaSvcPersonnelDto getHcsaSvcPersonnelDto(String man, String mix, String psnType) {
        HcsaSvcPersonnelDto personnelDto=new HcsaSvcPersonnelDto();
        personnelDto.setPageMandatoryCount(man);
        personnelDto.setPageMaximumCount(mix);
        try {
            if (StringUtil.isDigit(man)) {
                personnelDto.setMandatoryCount(Integer.valueOf(man));
            } else {
                personnelDto.setMandatoryCount(null);
            }
        } catch (NumberFormatException e) {
        }
        try {
            if (StringUtil.isDigit(mix)) {
                personnelDto.setMaximumCount(Integer.valueOf(mix));
            } else {
                personnelDto.setMaximumCount(null);
            }
        } catch (NumberFormatException e) {
        }
        personnelDto.setPsnType(psnType);
        personnelDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        return personnelDto;
    }

    @Override
    public Map<String, Boolean> isExistHcsaService(HcsaServiceDto hcsaServiceDto) {
        return hcsaConfigClient.isExistHcsaService(hcsaServiceDto).getEntity();
    }

    @Override
    public void saveHcsaServiceConfigDto(HcsaServiceConfigDto hcsaServiceConfigDto) {
        //transFor(hcsaServiceConfigDto);
        hcsaServiceConfigDto = hcsaConfigClient.saveHcsaServiceConfig(hcsaServiceConfigDto).getEntity();
        eicGateway(hcsaServiceConfigDto);
        HcsaServiceCacheHelper.flushServiceMapping();
    }




    private List<HcsaConfigPageDto> getWorkGrop(String type,String typeName){
        List<HcsaConfigPageDto> hcsaConfigPageDtos = IaisCommonUtils.genNewArrayList();
       // List<WorkingGroupDto> workingGroup = getWorkingGroup();
        List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtos = getHcsaSvcRoutingStageDtos();

        for (HcsaSvcRoutingStageDto hcsaSvcRoutingStageDto : hcsaSvcRoutingStageDtos) {
            HcsaConfigPageDto hcsaConfigPageDto = new HcsaConfigPageDto();
            hcsaConfigPageDto.setStageCode(hcsaSvcRoutingStageDto.getStageCode());
            hcsaConfigPageDto.setStageName(hcsaSvcRoutingStageDto.getStageName());
            hcsaConfigPageDto.setAppTypeName(typeName);
            hcsaConfigPageDto.setAppType(type);
            hcsaConfigPageDto.setStageId(hcsaSvcRoutingStageDto.getId());

            if("INS".equals(hcsaConfigPageDto.getStageCode())){
                List<HcsaSvcSpeRoutingSchemeDto> hcsaSvcSpeRoutingSchemeDtos=new ArrayList<>(2);
                for(int i=0;i<2;i++){
                    HcsaSvcSpeRoutingSchemeDto hcsaSvcSpeRoutingSchemeDto=new HcsaSvcSpeRoutingSchemeDto();
                    hcsaSvcSpeRoutingSchemeDto.setInsOder(String.valueOf(i+2));
                    hcsaSvcSpeRoutingSchemeDtos.add(hcsaSvcSpeRoutingSchemeDto);
                }
                hcsaConfigPageDto.setHcsaSvcSpeRoutingSchemeDtos(hcsaSvcSpeRoutingSchemeDtos);
            }
            hcsaConfigPageDtos.add(hcsaConfigPageDto);
        }
        return  hcsaConfigPageDtos;
    }

    @Override
    public List<String> getType(){
        List<String> list=IaisCommonUtils.genNewArrayList();
        list.add(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
        list.add(ApplicationConsts.APPLICATION_TYPE_RENEWAL);
        list.add(ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL) ;
        list.add(ApplicationConsts.APPLICATION_TYPE_CESSATION) ;
        list.add(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
        list.add(ApplicationConsts.APPLICATION_TYPE_APPEAL);
        //post audit insApplicationConsts
        list.add(ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK);
        list.add(ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION);
        return list;
    }

    @Override
    public List<HcsaSvcCateWrkgrpCorrelationDto> getHcsaSvcCateWrkgrpCorrelationDtoBySvcCateId(String svcCateId) {
        log.info(StringUtil.changeForLog("The svcCateId is -->:"+svcCateId));
        if(StringUtil.isEmpty(svcCateId)){
            return new ArrayList<>();
        }
        List<HcsaSvcCateWrkgrpCorrelationDto> entity =
                hcsaConfigClient.getHcsaSvcCateWrkgrpCorrelationDtoBySvcCateId(svcCateId).getEntity();
        return entity;
    }


    private  Map<String, List<HcsaConfigPageDto>>  getEmptyHcsaConfigPageDto(){
        Map<String, List<HcsaConfigPageDto>> map = IaisCommonUtils.genNewHashMap();
        List<String> types = getType();
        for(String type:types){
            List<HcsaConfigPageDto> hcsaConfigPageDtos = IaisCommonUtils.genNewArrayList();
            if(ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(type)){
                hcsaConfigPageDtos= getWorkGrop(type,APPEAL);
            } else if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(type)){
                hcsaConfigPageDtos=  getWorkGrop(type,NEW_APPLICATION);
            }else if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(type)){
                hcsaConfigPageDtos = getWorkGrop(type, REQUEST_FOR_CHANGE);
            }else if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(type)){
                hcsaConfigPageDtos=  getWorkGrop(type,RENEW);
            }else if(ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(type)){
                hcsaConfigPageDtos= getWorkGrop(type,CESSATION);
            }else if(ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL.equals(type)){
                hcsaConfigPageDtos= getWorkGrop(type,WITHDRAWAL);
            }else  if(ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION.equals(type)){
                hcsaConfigPageDtos= getWorkGrop(type,SUSPENSION);
            }else if(ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(type)){
                hcsaConfigPageDtos= getWorkGrop(type,"Revocation");
            }
            map.put(type,hcsaConfigPageDtos);
        }
        return map;
    }


    public void eic(HcsaServiceConfigDto hcsaServiceConfigDto) {
        beEicGatewayClient.saveFeServiceConfig(hcsaServiceConfigDto);
    }

    private void eicGateway(HcsaServiceConfigDto hcsaServiceConfigDto) {
        beEicGatewayClient.callEicWithTrack(hcsaServiceConfigDto, this::eic, this.getClass(),
                "eic");
    }
    private HcsaSvcRoutingStageCompoundDto getHcsaSvcRoutingStageCompoundDto(HcsaSvcRoutingStageCompoundForDbDto hcsaSvcRoutingStageCompoundForDbDto,
                                                                             String appType,String stageId,int order){
        log.info(StringUtil.changeForLog("The getHcsaSvcRoutingStageCompoundDto start ..."));
        log.info(StringUtil.changeForLog("The getHcsaSvcRoutingStageCompoundDto appType is -->:"+appType));
        log.info(StringUtil.changeForLog("The getHcsaSvcRoutingStageCompoundDto stageId is -->:"+stageId));
        log.info(StringUtil.changeForLog("The getHcsaSvcRoutingStageCompoundDto order is -->:"+order));
        List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtos = hcsaSvcRoutingStageCompoundForDbDto.getHcsaSvcRoutingStageDtos();
        List<HcsaSvcSpecificStageWorkloadDto> hcsaSvcSpecificStageWorkloadDtos = hcsaSvcRoutingStageCompoundForDbDto.getHcsaSvcSpecificStageWorkloadDtos();
        List<HcsaSvcSpeRoutingSchemeDto> hcsaSvcSpeRoutingSchemeDtos = hcsaSvcRoutingStageCompoundForDbDto.getHcsaSvcSpeRoutingSchemeDtos();
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = hcsaSvcRoutingStageCompoundForDbDto.getHcsaSvcStageWorkingGroupDtos();
        HcsaSvcRoutingStageCompoundDto hcsaSvcRoutingStageCompoundDto = new HcsaSvcRoutingStageCompoundDto();
        for(HcsaSvcRoutingStageDto hcsaSvcRoutingStageDto : hcsaSvcRoutingStageDtos){
            if(appType.equals(hcsaSvcRoutingStageDto.getAppType()) && stageId.equals(hcsaSvcRoutingStageDto.getStageId())){
                hcsaSvcRoutingStageCompoundDto.setHcsaSvcRoutingStageDto(hcsaSvcRoutingStageDto);
                break;
            }
        }
        for(HcsaSvcSpecificStageWorkloadDto hcsaSvcSpecificStageWorkloadDto : hcsaSvcSpecificStageWorkloadDtos){
            if(appType.equals(hcsaSvcSpecificStageWorkloadDto.getAppType()) && stageId.equals(hcsaSvcSpecificStageWorkloadDto.getStageId())){
                hcsaSvcRoutingStageCompoundDto.setHcsaSvcSpecificStageWorkloadDto(hcsaSvcSpecificStageWorkloadDto);
                break;
            }
        }

        for(HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto : hcsaSvcStageWorkingGroupDtos){
            if(order==hcsaSvcStageWorkingGroupDto.getOrder() && stageId.equals(hcsaSvcStageWorkingGroupDto.getStageId())){
                for(HcsaSvcSpeRoutingSchemeDto hcsaSvcSpeRoutingSchemeDto : hcsaSvcSpeRoutingSchemeDtos){
                    if(hcsaSvcSpeRoutingSchemeDto.getAppType().equals(appType) && hcsaSvcSpeRoutingSchemeDto.getStageWrkGrpID().equals(hcsaSvcStageWorkingGroupDto.getId())){
                        hcsaSvcRoutingStageCompoundDto.setHcsaSvcStageWorkingGroupDto(hcsaSvcStageWorkingGroupDto);
                        hcsaSvcRoutingStageCompoundDto.setHcsaSvcSpeRoutingSchemeDto(hcsaSvcSpeRoutingSchemeDto);
                        break;
                    }
                }
            }
        }
        log.info(StringUtil.changeForLog("The getHcsaSvcRoutingStageCompoundDto end ..."));
        return  hcsaSvcRoutingStageCompoundDto;
    }

    private void addHcsaServiceCategoryDisciplineDtoMap(Map<String,HcsaServiceCategoryDisciplineDto> hcsaServiceCategoryDisciplineDtoMap,
                                                        String categorySectionName,
                                                        List<HcsaServiceSubTypeDto> hcsaServiceSubTypeDtos,
                                                        String premisType){
        log.info(StringUtil.changeForLog("The addHcsaServiceCategoryDisciplineDtoMap start ..."));
        HcsaServiceCategoryDisciplineDto hcsaServiceCategoryDisciplineDto = new HcsaServiceCategoryDisciplineDto();
        hcsaServiceCategoryDisciplineDto.setSectionHeader(categorySectionName);
        List<CategoryDisciplineErrorsDto> categoryDisciplineDtos = IaisCommonUtils.genNewArrayList();
        if(IaisCommonUtils.isNotEmpty(hcsaServiceSubTypeDtos)){
            for(HcsaServiceSubTypeDto hcsaServiceSubTypeDto : hcsaServiceSubTypeDtos){
                CategoryDisciplineErrorsDto categoryDisciplineErrorsDto = new CategoryDisciplineErrorsDto();
                categoryDisciplineErrorsDto.setCategoryDiscipline(hcsaServiceSubTypeDto.getSubtypeName());
                categoryDisciplineDtos.add(categoryDisciplineErrorsDto);
            }
        }else{
            log.error(StringUtil.changeForLog("the hcsaServiceSubTypeDtos  is null "));
        }
        hcsaServiceCategoryDisciplineDto.setCategoryDisciplineDtos(categoryDisciplineDtos);
        hcsaServiceCategoryDisciplineDtoMap.put(premisType,hcsaServiceCategoryDisciplineDto);
        log.info(StringUtil.changeForLog("The addHcsaServiceCategoryDisciplineDtoMap end ..."));
    }

    private void addHcsaServiceSubServicePageDtoMap(Map<String,HcsaServiceSubServicePageDto> hcsaServiceSubServicePageDtoMap,
                                                    String specialSvcSecName,
                                                    List<HcsaSvcSpecifiedCorrelationDto> hcsaSvcSpecifiedCorrelationDtos,
                                                    String premisType){
        log.info(StringUtil.changeForLog("The addHcsaServiceSubServicePageDtoMap start ..."));
        HcsaServiceSubServicePageDto hcsaServiceSubServicePageDto = new HcsaServiceSubServicePageDto();
        hcsaServiceSubServicePageDto.setSectionHeader(specialSvcSecName);
        List<HcsaServiceSubServiceErrorsDto> hcsaServiceSubServiceErrorsDtos =  IaisCommonUtils.genNewArrayList();

        if(IaisCommonUtils.isNotEmpty(hcsaSvcSpecifiedCorrelationDtos)){
            List<HcsaSvcSpecifiedCorrelationDto> specHcsaSvcSpecifiedCorrelationDtosLevel0 = getLeave0(hcsaSvcSpecifiedCorrelationDtos);
            if(IaisCommonUtils.isNotEmpty(specHcsaSvcSpecifiedCorrelationDtosLevel0)){
                for(HcsaSvcSpecifiedCorrelationDto hcsaSvcSpecifiedCorrelationDto : specHcsaSvcSpecifiedCorrelationDtosLevel0){
                    HcsaServiceSubServiceErrorsDto hcsaServiceSubServiceErrorsDto = new HcsaServiceSubServiceErrorsDto();
                    hcsaServiceSubServiceErrorsDto.setSubServiceCode(hcsaSvcSpecifiedCorrelationDto.getSpecifiedSvcId());
                    hcsaServiceSubServiceErrorsDto.setLevel("0");
                    hcsaServiceSubServiceErrorsDtos.add(hcsaServiceSubServiceErrorsDto);
                    addHcsaServiceSubServiceErrorsDtos(hcsaServiceSubServiceErrorsDtos,hcsaSvcSpecifiedCorrelationDtos,1,hcsaSvcSpecifiedCorrelationDto.getId());
                }
            }
        }
        hcsaServiceSubServicePageDto.setHcsaServiceSubServiceErrorsDtos(hcsaServiceSubServiceErrorsDtos);
        hcsaServiceSubServicePageDtoMap.put(premisType,hcsaServiceSubServicePageDto);
        log.info(StringUtil.changeForLog("The addHcsaServiceSubServicePageDtoMap end ..."));
    }

    private void addHcsaServiceSubServiceErrorsDtos(List<HcsaServiceSubServiceErrorsDto> hcsaServiceSubServiceErrorsDtos,
                                                    List<HcsaSvcSpecifiedCorrelationDto> allHcsaSvcSpecifiedCorrelationDtos,
                                                    int level,
                                                    String parentRelId){
        log.info(StringUtil.changeForLog("The addHcsaServiceSubServiceErrorsDtos start ..."));
        List<HcsaSvcSpecifiedCorrelationDto> hcsaSvcSpecifiedCorrelationDtos =
                getHcsaSvcSpecifiedCorrelationDtoByparentRelId(allHcsaSvcSpecifiedCorrelationDtos,parentRelId);
        if(IaisCommonUtils.isNotEmpty(hcsaSvcSpecifiedCorrelationDtos)){
            for(HcsaSvcSpecifiedCorrelationDto hcsaSvcSpecifiedCorrelationDto : hcsaSvcSpecifiedCorrelationDtos){
                HcsaServiceSubServiceErrorsDto hcsaServiceSubServiceErrorsDto = new HcsaServiceSubServiceErrorsDto();
                hcsaServiceSubServiceErrorsDto.setSubServiceCode(hcsaSvcSpecifiedCorrelationDto.getSpecifiedSvcId());
                hcsaServiceSubServiceErrorsDto.setLevel(String.valueOf(level));
                hcsaServiceSubServiceErrorsDtos.add(hcsaServiceSubServiceErrorsDto);
                addHcsaServiceSubServiceErrorsDtos(hcsaServiceSubServiceErrorsDtos,allHcsaSvcSpecifiedCorrelationDtos,level+1,hcsaSvcSpecifiedCorrelationDto.getId());
            }
        }
        log.info(StringUtil.changeForLog("The addHcsaServiceSubServiceErrorsDtos end ..."));
    }

    private List<HcsaSvcSpecifiedCorrelationDto> getHcsaSvcSpecifiedCorrelationDtoByparentRelId(List<HcsaSvcSpecifiedCorrelationDto> hcsaSvcSpecifiedCorrelationDtos,String parentRelId){
        log.info(StringUtil.changeForLog("The getHcsaSvcSpecifiedCorrelationDtoByparentRelId start ..."));
        List<HcsaSvcSpecifiedCorrelationDto> result = IaisCommonUtils.genNewArrayList();
        for(HcsaSvcSpecifiedCorrelationDto hcsaSvcSpecifiedCorrelationDto : hcsaSvcSpecifiedCorrelationDtos){
            if(parentRelId.equals(hcsaSvcSpecifiedCorrelationDto.getParentRelId())){
                result.add(hcsaSvcSpecifiedCorrelationDto);
            }
        }
        log.info(StringUtil.changeForLog("The getHcsaSvcSpecifiedCorrelationDtoByparentRelId result.size() -->:"+result.size()));
        log.info(StringUtil.changeForLog("The getHcsaSvcSpecifiedCorrelationDtoByparentRelId end ..."));
        return result;
    }

    private List<HcsaSvcSpecifiedCorrelationDto> getLeave0( List<HcsaSvcSpecifiedCorrelationDto> hcsaSvcSpecifiedCorrelationDtos){
        log.info(StringUtil.changeForLog("The getLeave0 start ..."));
        List<HcsaSvcSpecifiedCorrelationDto> result = IaisCommonUtils.genNewArrayList();
        for(HcsaSvcSpecifiedCorrelationDto hcsaSvcSpecifiedCorrelationDto : hcsaSvcSpecifiedCorrelationDtos){
            if(StringUtil.isEmpty(hcsaSvcSpecifiedCorrelationDto.getParentRelId())){
                result.add(hcsaSvcSpecifiedCorrelationDto);
            }
        }
        log.info(StringUtil.changeForLog("The getLeave0 result.size() -->:"+result.size()));
        log.info(StringUtil.changeForLog("The getLeave0 end ..."));
        return result;
    }

}
