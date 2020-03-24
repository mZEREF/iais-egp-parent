package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.DistributionListDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.DistributionListWebDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.service.DistributionListService;
import com.ecquaria.cloud.moh.iais.service.client.DistributionListClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

    @Autowired
    private OrganizationClient organizationClient;
    @Override
    public SearchResult<DistributionListDto> distributionList(SearchParam searchParam) {
        SearchResult<DistributionListDto> distributionListDtoSearchResult = distributionListClient.getDistributionList(searchParam).getEntity();
        List<String> userIdList = IaisCommonUtils.genNewArrayList();
        for (DistributionListDto item:distributionListDtoSearchResult.getRows()
             ) {
            userIdList.add(item.getCreateBy());
        }
        List<OrgUserDto> userList = organizationClient.retrieveOrgUserAccount(userIdList).getEntity();
        Map<String , String > userNameList = IaisCommonUtils.genNewHashMap();
        for (OrgUserDto item :userList
             ) {
            userNameList.put(item.getId(),item.getDisplayName());
        }
        for (DistributionListDto item:distributionListDtoSearchResult.getRows()
             ) {
            String name = userNameList.get(item.getCreateBy());
            item.setCreateBy(name);

            item.setService(HcsaServiceCacheHelper.getServiceByCode(item.getService()).getSvcName());
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
    public void deleteDistributionList(List<String> list){
        distributionListClient.deleteDistributionList(list).getEntity();
    }

    @Override
    public DistributionListWebDto getDistributionListById(String id){
        return distributionListClient.getDistributionListById(id).getEntity();
    }
}
