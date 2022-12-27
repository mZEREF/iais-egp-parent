package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceSubTypeDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import java.util.Set;

public class ConfigInboxFallBack implements ConfigInboxClient{

    @Override
    public FeignResponseEntity<String> getServiceNameById(String serviceId) {
        return IaisEGPHelper.getFeignResponseEntity("getServiceNameById", serviceId);
    }

    @Override
    public FeignResponseEntity<List<HcsaServiceDto>> getActiveServices() {
        return IaisEGPHelper.getFeignResponseEntity("getActiveServices");
    }

    @Override
    public FeignResponseEntity<List<HcsaServiceCorrelationDto>> serviceCorrelation() {
        return IaisEGPHelper.getFeignResponseEntity("serviceCorrelation");
    }

    @Override
    public FeignResponseEntity<List<HcsaServiceDto>> getHcsaService(List<String> serviceId) {
        return IaisEGPHelper.getFeignResponseEntity("getHcsaService", serviceId);
    }

    @Override
    public FeignResponseEntity<List<HcsaServiceCorrelationDto>> getActiveSvcCorrelation() {
        return IaisEGPHelper.getFeignResponseEntity("getActiveSvcCorrelation");
    }

    @Override
    public FeignResponseEntity<Set<String>> getAppGrpPremisesTypeBySvcId(List<String> serviceId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppGrpPremisesTypeBySvcId", serviceId);
    }

    @Override
    public FeignResponseEntity<List<HcsaServiceDto>> getHcsaServiceDtoByCode(List<String> code) {
        return IaisEGPHelper.getFeignResponseEntity("getHcsaServiceDtoByCode", code);
    }

    @Override
    public FeignResponseEntity<List<HcsaServiceSubTypeDto>> getHcsaServiceSubTypeDtosByIds(List<String> ids) {
        return IaisEGPHelper.getFeignResponseEntity("getHcsaServiceSubTypeDtosByIds", ids);
    }
}
