package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.HcsaFeeBundleItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpecifiedCorrelationDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;

import java.util.List;

/**
 * @author Wenkang
 * @date 2019/12/4 15:28
 */
public class HcsaConfigClientFallback implements HcsaConfigClient{

    @Override
    public FeignResponseEntity<List<HcsaServiceDto>> getActiveServices() {
        return IaisEGPHelper.getFeignResponseEntity("getActiveServices");
    }

    @Override
    public FeignResponseEntity<List<HcsaServiceDto>> allHcsaService() {
        return IaisEGPHelper.getFeignResponseEntity("allHcsaService");
    }

    @Override
    public FeignResponseEntity<List<HcsaServiceDto>> getHcsaServiceByNames(List<String> svcNames) {
        return IaisEGPHelper.getFeignResponseEntity("allHcsaService",svcNames);
    }

    @Override
    public FeignResponseEntity<List<HcsaFeeBundleItemDto>> getActiveBundleDtoList() {
        return IaisEGPHelper.getFeignResponseEntity("allHcsaService");
    }

    @Override
    public FeignResponseEntity<HcsaServiceDto> getServiceDtoByName(String svcName) {
        return IaisEGPHelper.getFeignResponseEntity("allHcsaService",svcName);
    }

    @Override
    public FeignResponseEntity<List<HcsaSvcSpecifiedCorrelationDto>> getHcsaSvcSpecifiedCorrelationDtos(String svcCode,String serviceId, String premisesType){
        return IaisEGPHelper.getFeignResponseEntity("allHcsaService",svcCode);
    }

    @Override
    public FeignResponseEntity<HcsaServiceDto> getActiveHcsaServiceDtoByName(String svcName) {
        return IaisEGPHelper.getFeignResponseEntity("getActiveHcsaServiceDtoByName",svcName);
    }
}
