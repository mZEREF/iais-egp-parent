package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.HcsaLicenceGroupFeeDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;

import java.util.List;

/**
 * @author Wenkang
 * @date 2019/12/26 15:37
 */
public class HscaLicenClientFallback implements HcsaLicenClient{
    @Override
    public FeignResponseEntity<List<HcsaLicenceGroupFeeDto>> retrieveHcsaLicenceGroupFee(List<String> licenceIds){
        return IaisEGPHelper.getFeignResponseEntity("retrieveHcsaLicenceGroupFee",licenceIds);
    }

    @Override
    public FeignResponseEntity<List<Boolean>> vehicleIsUsed(List<String> vehicle) {
        return IaisEGPHelper.getFeignResponseEntity("vehicleIsUsed",vehicle);
    }

}
