package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesDoQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationSubDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.AppAlignLicQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.MenuLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.AssessmentGuideService;
import com.ecquaria.cloud.moh.iais.service.client.AppInboxClient;
import com.ecquaria.cloud.moh.iais.service.client.ConfigInboxClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceInboxClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class AssessmentGuideImpl implements AssessmentGuideService {

    @Autowired
    private ConfigInboxClient configInboxClient;

    @Autowired
    private LicenceInboxClient licenceClient;

    @Autowired
    private AppInboxClient appInboxClient;

    @Autowired
    private HcsaConfigClient hcsaConfigClient;

    @Override
    public List<HcsaServiceDto> getServicesInActive() {
        return configInboxClient.getActiveServices().getEntity();
    }

    @Override
    public List<HcsaServiceCorrelationDto> getCorrelation() {
        return configInboxClient.serviceCorrelation().getEntity();
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
    public List<HcsaServiceDto> getHcsaServiceDtosById(List<String> ids) {
        return  configInboxClient.getHcsaService(ids).getEntity();
    }

    @Override
    public String selectDarft(Map<String, Object> map) {
        return appInboxClient.selectDarft(map).getEntity();
    }

    @Override
    @SearchTrack(catalog = "interInboxQuery", key = "getLicenceBySerName")
    public SearchResult<MenuLicenceDto> getMenuLicence(SearchParam searchParam) {
        return licenceClient.getMenuLicence(searchParam).getEntity();
    }

    @Override
    public Boolean isNewLicensee(String licenseeId) {
        return licenceClient.checkIsNewLicsee(licenseeId).getEntity();
    }

    @Override
    public List<String> getHciFromPendAppAndLic(String licenseeId, List<HcsaServiceDto> hcsaServiceDtos) {
        List<String> result = IaisCommonUtils.genNewArrayList();
        if(!StringUtil.isEmpty(licenseeId) && !IaisCommonUtils.isEmpty(hcsaServiceDtos)){
            List<String> svcNames = IaisCommonUtils.genNewArrayList();
            for(HcsaServiceDto hcsaServiceDto:hcsaServiceDtos){
                svcNames.add(hcsaServiceDto.getSvcName());
            }
            AppPremisesDoQueryDto appPremisesDoQueryDto = new AppPremisesDoQueryDto();
            List<HcsaServiceDto>  HcsaServiceDtoList= hcsaConfigClient.getHcsaServiceByNames(svcNames).getEntity();
            List<String> svcIds = IaisCommonUtils.genNewArrayList();
            for(HcsaServiceDto hcsaServiceDto:HcsaServiceDtoList){
                svcIds.add(hcsaServiceDto.getId());
            }
            appPremisesDoQueryDto.setLicenseeId(licenseeId);
            appPremisesDoQueryDto.setSvcIdList(svcIds);
            List<PremisesDto> premisesDtos = licenceClient.getPremisesByLicseeIdAndSvcName(licenseeId,svcNames).getEntity();
            List<AppGrpPremisesDto> appGrpPremisesEntityDtos = appInboxClient.getPendAppPremises(appPremisesDoQueryDto).getEntity();
            if(!IaisCommonUtils.isEmpty(premisesDtos)){
                for(PremisesDto premisesHciDto:premisesDtos){
                    result.addAll(genPremisesHciList(premisesHciDto));
                }
            }
            if(!IaisCommonUtils.isEmpty(appGrpPremisesEntityDtos)){
                for(AppGrpPremisesDto premisesEntityDto:appGrpPremisesEntityDtos){
                    PremisesDto premisesDto = MiscUtil.transferEntityDto(premisesEntityDto,PremisesDto.class);
                    result.addAll(genPremisesHciList(premisesDto));
                }
            }
        }
        return result;
    }

    @Override
    public void deleteDraftNUmber(List<String> draftNumbers) {
        appInboxClient.deleteDraftNUmber(draftNumbers);
    }

    @Override
    public HcsaServiceDto getServiceDtoById(String id) {
        return configInboxClient.getHcsaServiceDtoByServiceId(id).getEntity();
    }

    @Override
    public List<HcsaServiceCorrelationDto> getActiveSvcCorrelation() {
        List<HcsaServiceCorrelationDto> hcsaServiceCorrelationDtos = configInboxClient.getActiveSvcCorrelation().getEntity();
        List<HcsaServiceCorrelationDto> newHcsaServiceCorrelationDtos = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(hcsaServiceCorrelationDtos)){
            List<String> baseSpecIdList = IaisCommonUtils.genNewArrayList();
            for(HcsaServiceCorrelationDto hcsaServiceCorrelationDto:hcsaServiceCorrelationDtos){
                String baseSpecId = hcsaServiceCorrelationDto.getBaseSvcId() + hcsaServiceCorrelationDto.getSpecifiedSvcId();
                if(!baseSpecIdList.contains(baseSpecId)){
                    newHcsaServiceCorrelationDtos.add(hcsaServiceCorrelationDto);
                    baseSpecIdList.add(baseSpecId);
                }
            }
        }

        return newHcsaServiceCorrelationDtos;
    }

    @Override
    public List<MenuLicenceDto> setPremAdditionalInfo(List<MenuLicenceDto> menuLicenceDtos) {
        return licenceClient.setPremAdditionalInfo(menuLicenceDtos).getEntity();
    }

    @Override
    public List<ApplicationSubDraftDto> getDraftListBySvcCodeAndStatus(List<String> svcCodeList, String status, String licenseeId, String appType) {
        return appInboxClient.getDraftListBySvcCodeAndStatus(svcCodeList,licenseeId,status,appType).getEntity();
    }

    @Override
    public Set<String> getAppGrpPremisesTypeBySvcId(List<String> svcIds) {
        return configInboxClient.getAppGrpPremisesTypeBySvcId(svcIds).getEntity();
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
            List<HcsaServiceDto>  HcsaServiceDtoList= hcsaConfigClient.getHcsaServiceByNames(svcNames).getEntity();
            List<String> svcIds = IaisCommonUtils.genNewArrayList();
            for(HcsaServiceDto hcsaServiceDto:HcsaServiceDtoList){
                svcIds.add(hcsaServiceDto.getId());
            }
            appPremisesDoQueryDto.setLicenseeId(licenseeId);
            appPremisesDoQueryDto.setSvcIdList(svcIds);
            List<PremisesDto> premisesDtos = licenceClient.getPremisesByLicseeIdAndSvcName(licenseeId,svcNames).getEntity();
            List<AppGrpPremisesDto> appGrpPremisesEntityDtos = appInboxClient.getPendAppPremises(appPremisesDoQueryDto).getEntity();
            log.debug("licence record size {}",premisesDtos.size());
            log.debug("pending application record size {}",appGrpPremisesEntityDtos.size());
            if(IaisCommonUtils.isEmpty(premisesDtos) && IaisCommonUtils.isEmpty(appGrpPremisesEntityDtos)){
                canCreateEasOrMts = true;
            }
        }
        log.debug(StringUtil.changeForLog("check can create eas or mts service end ..."));
        return canCreateEasOrMts;
    }


    private static List<String> genPremisesHciList(PremisesDto premisesDto){
        return IaisCommonUtils.getPremisesHciList(premisesDto);
    }
}
