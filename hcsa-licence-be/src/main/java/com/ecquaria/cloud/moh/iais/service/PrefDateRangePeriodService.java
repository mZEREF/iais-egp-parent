package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServicePrefInspPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServicePrefInspPeriodQueryDto;

/**
 * @author: yichen
 * @date time:12/24/2019 9:03 PM
 * @description:
 */

public interface PrefDateRangePeriodService {

    SearchResult<HcsaServicePrefInspPeriodQueryDto> getHcsaServicePrefInspPeriodList(SearchParam searchParam);
    Boolean savePrefInspPeriod(HcsaServicePrefInspPeriodDto period);
}
