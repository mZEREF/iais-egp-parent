package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.service.AppPremisesInspectionRoutingHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author weilu
 * @date 2019/12/5 18:17
 */
@Slf4j
@Service
public class AppPremisesRoutingInspectionHistoryServiceImpl implements AppPremisesInspectionRoutingHistoryService {


    @Override
    public AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto) {
        return RestApiUtil.postGetObject(RestApiUrlConsts.IAIS_APPLICATION_HISTORY,appPremisesRoutingHistoryDto,AppPremisesRoutingHistoryDto.class);
    }
}
