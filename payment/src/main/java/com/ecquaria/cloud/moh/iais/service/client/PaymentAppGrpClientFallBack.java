package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

import java.util.List;

/**
 * PaymentAppGrpClientFallBack
 *
 * @author junyu
 * @date 2020/7/22
 */
public class PaymentAppGrpClientFallBack implements PaymentAppGrpClient{
    @Override
    public FeignResponseEntity<String> doPaymentUpDate(ApplicationGroupDto applicationGroupDto) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<ApplicationGroupDto> paymentUpDateByGrpNo(String groupNo) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<List<ApplicationGroupDto>> updateFeApplicationGroupStatus(List<ApplicationGroupDto> applicationGroupDtos) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }


}
