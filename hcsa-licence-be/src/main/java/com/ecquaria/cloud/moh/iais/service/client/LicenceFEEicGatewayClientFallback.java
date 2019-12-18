package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceGroupDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.http.HttpHeaders;

/**
 * LicenceFEEicGatewayClientFallback
 *
 * @author suocheng
 * @date 12/14/2019
 */

public class LicenceFEEicGatewayClientFallback {
    public FeignResponseEntity<List<LicenceGroupDto>> createLicence(List<LicenceGroupDto> licenceGroupDtoList,
                                                                    String date,
                                                                    String authorization){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}
