package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ServiceConfigServiceImpl
 *
 * @author suocheng
 * @date 10/14/2019
 */
@Service
public class ServiceConfigServiceImpl implements ServiceConfigService {
    @Override
    public List<HcsaServiceDto> getHcsaServiceDtosById(List<String> ids) {
        return RestApiUtil.postGetList(RestApiUrlConsts.GET_HCSA_SERVICE_BY_IDS, ids,HcsaServiceDto.class);
    }
}
