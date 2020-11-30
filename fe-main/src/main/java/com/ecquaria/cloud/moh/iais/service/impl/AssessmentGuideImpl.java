package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.AppAlignLicQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.MenuLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesOperationalUnitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.AssessmentGuideService;
import com.ecquaria.cloud.moh.iais.service.client.AppInboxClient;
import com.ecquaria.cloud.moh.iais.service.client.ConfigInboxClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceInboxClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AssessmentGuideImpl implements AssessmentGuideService {

    @Autowired
    private ConfigInboxClient configInboxClient;

    @Autowired
    private LicenceInboxClient licenceClient;

    @Autowired
    private AppInboxClient appInboxClient;

    @Override
    public List<HcsaServiceDto> getServicesInActive() {
        return configInboxClient.getActiveServices().getEntity();
    }

    @Override
    public List<HcsaServiceCorrelationDto> getCorrelation() {
        return configInboxClient.serviceCorrelation().getEntity();
    }

    @Override
    public List<AppAlignLicQueryDto> getAppAlignLicQueryDto(String licenseeId, List<String> svcNameList) {
        List<AppAlignLicQueryDto> appAlignLicQueryDtos = IaisCommonUtils.genNewArrayList();
        if(!StringUtil.isEmpty(licenseeId) && !IaisCommonUtils.isEmpty(svcNameList)) {
            String svcNames = JsonUtil.parseToJson(svcNameList);
            appAlignLicQueryDtos = licenceClient.getAppAlignLicQueryDto(licenseeId,svcNames).getEntity();
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
            List<String> svcIds = IaisCommonUtils.genNewArrayList();
            List<String> svcNames = IaisCommonUtils.genNewArrayList();
            for(HcsaServiceDto hcsaServiceDto:hcsaServiceDtos){
                svcIds.add(hcsaServiceDto.getId());
                svcNames.add(hcsaServiceDto.getSvcName());
            }
            String svcNameStr = JsonUtil.parseToJson(svcNames);
            String svcIdStr = JsonUtil.parseToJson(svcIds);
            List<PremisesDto> premisesDtos = licenceClient.getPremisesByLicseeIdAndSvcName(licenseeId,svcNameStr).getEntity();
            List<AppGrpPremisesEntityDto> appGrpPremisesEntityDtos = appInboxClient.getPendAppPremises(licenseeId,svcIdStr).getEntity();
            if(!IaisCommonUtils.isEmpty(premisesDtos)){
                for(PremisesDto premisesHciDto:premisesDtos){
                    result.addAll(genPremisesHciList(premisesHciDto));
                }
            }
            if(!IaisCommonUtils.isEmpty(appGrpPremisesEntityDtos)){
                for(AppGrpPremisesEntityDto premisesEntityDto:appGrpPremisesEntityDtos){
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
        return configInboxClient.getActiveSvcCorrelation().getEntity();
    }

    private static List<String> setPremiseHciList(AppGrpPremisesEntityDto premisesDto, List<String> premisesHci){
        String premisesKey = IaisCommonUtils.genPremisesKey(premisesDto.getPostalCode(),premisesDto.getBlkNo(),premisesDto.getFloorNo(),premisesDto.getUnitNo());
        if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premisesDto.getPremisesType())){
            premisesHci.add(premisesDto.getHciName()+premisesKey);
        }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesDto.getPremisesType())){
            premisesHci.add(premisesDto.getVehicleNo()+premisesKey);
        }else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(premisesDto.getPremisesType())){
            premisesHci.add(premisesKey);
        }
        return premisesHci;
    }

    private static List<String> genPremisesHciList(PremisesDto premisesDto){
        List<String> premisesHciList = IaisCommonUtils.genNewArrayList();
        if(premisesDto != null){
            String premisesHciPre = "";
            if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premisesDto.getPremisesType())){
                premisesHciPre = premisesDto.getHciName() + premisesDto.getPostalCode() + premisesDto.getBlkNo();
            }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesDto.getPremisesType())){
                premisesHciPre = premisesDto.getVehicleNo() + premisesDto.getPostalCode() + premisesDto.getBlkNo();
            }else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(premisesDto.getPremisesType())){
                premisesHciPre = premisesDto.getPostalCode() + premisesDto.getBlkNo();
            }
            premisesHciList.add(premisesHciPre + premisesDto.getFloorNo() + premisesDto.getUnitNo());
            List<PremisesOperationalUnitDto> operationalUnitDtos = premisesDto.getPremisesOperationalUnitDtos();
            if(!IaisCommonUtils.isEmpty(operationalUnitDtos)){
                for(PremisesOperationalUnitDto operationalUnitDto:operationalUnitDtos){
                    premisesHciList.add(premisesHciPre + operationalUnitDto.getFloorNo() + operationalUnitDto.getUnitNo());
                }
            }
        }
        return premisesHciList;
    }
}
