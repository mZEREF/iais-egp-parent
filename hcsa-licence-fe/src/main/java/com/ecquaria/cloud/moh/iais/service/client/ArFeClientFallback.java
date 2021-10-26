package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

/**
 * @Description ArCommonFeClientFallback
 * @Auther chenlei on 10/26/2021.
 */
public class ArFeClientFallback implements ArFeClient {

    @Override
    public FeignResponseEntity<PatientDto> getPatientDto(String idNumber, String nationality, String orgId) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

}
