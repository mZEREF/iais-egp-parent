package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremSubSvcRelDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;


import java.util.List;


public class AppPremSubSvcBeClientFallBack implements AppPremSubSvcBeClient {

    @Override
    public FeignResponseEntity<List<AppPremSubSvcRelDto>> getAppPremSubSvcRelDtoListByCorrId(String appPremCorrId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppPremSubSvcRelDtoListByCorrId",appPremCorrId);
    }

    @Override
    public FeignResponseEntity<List<AppPremSubSvcRelDto>> getAppPremSubSvcRelDtoListByCorrIdAndType(String appPremCorrId, String type) {
        return IaisEGPHelper.getFeignResponseEntity("getAppPremSubSvcRelDtoListByCorrIdAndType",appPremCorrId,type);
    }
}
