package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.DistributionListDto;
import com.ecquaria.cloud.moh.iais.service.DistributionListService;
import com.ecquaria.cloud.moh.iais.service.client.DistributionListClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public SearchResult<DistributionListDto> distributionList(SearchParam searchParam) {
        return distributionListClient.getDistributionList(searchParam).getEntity();
    }
    @Override
    public List<DistributionListDto> getDistributionList() {
        return distributionListClient.getDistributionListNoParam().getEntity();
    }

    @Override
    public List<HcsaServiceDto> getServicesInActive(){
        return hcsaConfigClient.getActiveServices().getEntity();
    }

    @Override
    public DistributionListDto saveDistributionList(DistributionListDto distributionListDto){
        return distributionListClient.saveDistributionList(distributionListDto).getEntity();
    }

    @Override
    public void deleteDistributionList(List<String> list){
        distributionListClient.deleteDistributionList(list).getEntity();
    }

    @Override
    public DistributionListDto getDistributionListById(String id){
        return distributionListClient.getDistributionListById(id).getEntity();
    }
}
