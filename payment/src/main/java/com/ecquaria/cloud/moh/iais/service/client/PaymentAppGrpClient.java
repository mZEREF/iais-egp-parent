package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * PaymentAppGrpClient
 *
 * @author junyu
 * @date 2020/7/22
 */
@FeignClient(name = "hcsa-application", configuration = {FeignConfiguration.class},
        fallback = PaymentAppGrpClientFallBack.class)
public interface PaymentAppGrpClient {
    @PutMapping(path="/iais-application/payment-app-grp", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> doPaymentUpDate(@RequestBody ApplicationGroupDto applicationGroupDto);
    @PutMapping(value = "/iais-application/app-grp-by-no",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationGroupDto> paymentUpDateByGrpNo(@RequestBody String groupNo);
    @PutMapping(path="/fe-application-group-status", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationGroupDto>> updateFeApplicationGroupStatus(@RequestBody List<ApplicationGroupDto> applicationGroupDtos);
}
