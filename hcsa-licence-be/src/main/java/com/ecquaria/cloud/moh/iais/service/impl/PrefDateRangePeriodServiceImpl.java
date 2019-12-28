package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServicePrefInspPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServicePrefInspPeriodQueryDto;
import com.ecquaria.cloud.moh.iais.service.PrefDateRangePeriodService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: yichen
 * @date time:12/24/2019 9:03 PM
 * @description:
 */

@Service
public class PrefDateRangePeriodServiceImpl implements PrefDateRangePeriodService {
    @Autowired
    private HcsaConfigClient hcsaConfigClient;

    @Override
    public SearchResult<HcsaServicePrefInspPeriodQueryDto> getHcsaServicePrefInspPeriodList(SearchParam searchParam) {
        return hcsaConfigClient.getHcsaServicePrefInspPeriodList(searchParam).getEntity();
    }

    @Override
    public Boolean savePrefInspPeriod(HcsaServicePrefInspPeriodDto period) {
        return hcsaConfigClient.savePrefInspPeriod(period).getEntity();
    }
}
