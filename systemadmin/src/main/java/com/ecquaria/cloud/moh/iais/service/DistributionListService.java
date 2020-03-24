package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.DistributionListDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.DistributionListWebDto;
import java.util.List;

/**
 * @author guyin
 * @date 2019/12/28 10:40
 */
public interface DistributionListService {
    SearchResult<DistributionListDto> distributionList(SearchParam searchParam);
    List<DistributionListDto> getDistributionList(String mode);
    List<HcsaServiceDto> getServicesInActive();
    DistributionListWebDto saveDistributionList(DistributionListWebDto distributionListDto);
    void deleteDistributionList(List<String> list);
    DistributionListDto getDistributionListById(String id);
}
