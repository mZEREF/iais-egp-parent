package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.http.HttpHeaders;

/**
 * FEEicGatewayClientFallback
 *
 * @author suocheng
 * @date 12/19/2019
 */

public class FEEicGatewayClientFallback {
    public FeignResponseEntity<List<LicenceGroupDto>> createLicence(List<LicenceGroupDto> licenceGroupDtoList,
                                                                    String date,
                                                                    String authorization){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
    public FeignResponseEntity<ApplicationDto> updateApplication(ApplicationDto applicationDto,
                                                                 String date,
                                                                 String authorization){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
    public FeignResponseEntity<InterMessageDto> saveInboxMessage(InterMessageDto interInboxDto,
                                                                 String date,
                                                                 String authorization){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}
