package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author weilu
 * @date 2020/1/14 17:06
 */
@FeignClient(name = "iais-payment", configuration = FeignConfiguration.class,
        fallback = AppPaymentStatusClientFallback.class)
public interface  AppPaymentStatusClient {

    @PostMapping(value = "/iais-payment/payment-status-appGrpId",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<PaymentDto>> getPaymentDtosByReqRefNos();
}
