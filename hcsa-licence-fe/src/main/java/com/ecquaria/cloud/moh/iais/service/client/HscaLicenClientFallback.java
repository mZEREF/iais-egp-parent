package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.HcsaLicenceGroupFeeDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.http.HttpHeaders;

/**
 * @author Wenkang
 * @date 2019/12/26 15:37
 */
public class HscaLicenClientFallback {

    FeignResponseEntity<List<HcsaLicenceGroupFeeDto>> retrieveHcsaLicenceGroupFee(List<String> licenceIds){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

}
