package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author weilu
 * @date 12/10/2019 4:58 PM
 */
@FeignClient(name = "iais-payment", configuration = {FeignConfiguration.class},
        fallback = AppGrpPaymentClientFallBack.class)
public interface AppGrpPaymentClient {
    @GetMapping(value = "/iais-payment/payment-reqRefNo/{sysClientId}/{reqRefNo}",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PaymentDto> getPaymentDtoByReqRefNo(@PathVariable("sysClientId") String sysClientId,
                                                            @PathVariable("reqRefNo") String reqRefNo);

    @PostMapping(value = "/iais-payment/payment-request-reqRefNo",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PaymentRequestDto> getPaymentRequestDtoByReqRefNo(@RequestBody String reqRefNo);

    @GetMapping(value = "/iais-payment/payment-request-reqRefNoLike/{sysClientId}/{reqRefNo}",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<PaymentRequestDto>> getPaymentRequestDtoByReqRefNoLike(@PathVariable("sysClientId") String sysClientId,
                                                                                    @PathVariable("reqRefNo") String reqRefNo);

}
