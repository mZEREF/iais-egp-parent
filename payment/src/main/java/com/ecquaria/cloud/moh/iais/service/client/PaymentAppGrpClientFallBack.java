package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

/**
 * PaymentAppGrpClientFallBack
 *
 * @author junyu
 * @date 2020/7/22
 */
public class PaymentAppGrpClientFallBack implements PaymentAppGrpClient{
    @Override
    public FeignResponseEntity<ApplicationGroupDto> paymentUpDateByGrpNo(String groupNo) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<String> doUpDate(ApplicationGroupDto applicationGroupDto) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}
