package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.DistributionListDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.DistributionListWebDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.service.DistributionListService;
import com.ecquaria.cloud.moh.iais.service.client.DistributionListClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

/**
 * @author guyin
 * @date 2020/2/6 10:41
 */
@Service
@Slf4j
public class DistributionListServiceImpl implements DistributionListService {

    @Autowired
    private DistributionListClient distributionListClient ;

    @Autowired
    private HcsaConfigClient hcsaConfigClient ;

    @Override
    @SearchTrack(catalog = "systemAdmin", key = "queryMassDistributionList")
    public SearchResult<DistributionListDto> distributionList(SearchParam searchParam) {
        SearchResult<DistributionListDto> distributionListDtoSearchResult = distributionListClient.getDistributionList(searchParam).getEntity();
        List<String> userIdList = IaisCommonUtils.genNewArrayList();
        for (DistributionListDto item:distributionListDtoSearchResult.getRows()
             ) {
            userIdList.add(item.getCreateBy());
        }
        for (DistributionListDto item:distributionListDtoSearchResult.getRows()
             ) {
            if (HcsaServiceCacheHelper.getServiceByCode(item.getService()) != null){
                item.setService(HcsaServiceCacheHelper.getServiceByCode(item.getService()).getSvcName());
            }
        }
        return distributionListDtoSearchResult;
    }
    @Override
    public List<DistributionListWebDto> getDistributionList(String mode) {
        return distributionListClient.getDistributionListNoParam(mode).getEntity();
    }

    @Override
    public List<HcsaServiceDto> getServicesInActive(){
        return hcsaConfigClient.getActiveServices().getEntity();
    }

    @Override
    public DistributionListWebDto saveDistributionList(DistributionListWebDto distributionListDto){
        return distributionListClient.saveDistributionList(distributionListDto).getEntity();
    }

    @Override
    public DistributionListWebDto saveDistributionRole(DistributionListWebDto distributionListDto){
        return distributionListClient.saveDistributionRole(distributionListDto).getEntity();
    }


    @Override
    public void deleteDistributionList(List<String> list){
        distributionListClient.deleteDistributionList(list).getEntity();
    }

    @Override
    public DistributionListWebDto getDistributionListById(String id){
        return distributionListClient.getDistributionListById(id).getEntity();
    }

    @Override
    public List<HcsaSvcPersonnelDto> roleByServiceId(String serviceId,String status){
        List<HcsaSvcPersonnelDto> list=hcsaConfigClient.getServiceType(serviceId,status).getEntity();
        list.removeIf(personnelDto -> personnelDto.getPsnType().equals(ApplicationConsts.PERSONNEL_VEHICLES) || personnelDto.getPsnType().equals(ApplicationConsts.PERSONNEL_CHARGES) || personnelDto.getPsnType().equals(ApplicationConsts.PERSONNEL_CHARGES_OTHER));
        return list;
    }

}
