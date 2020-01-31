package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author weilu
 * @date 12/10/2019 4:58 PM
 */
@FeignClient(name = "iais-payment", configuration = {FeignConfiguration.class},
        fallback = PaymentClientFallBack.class)
public interface PaymentClient {

    @PostMapping(value = "/iais-payment/tradingReply",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PaymentDto>saveHcsaPayment(@RequestBody PaymentDto paymentDto);

    @PostMapping(value = "/iais-payment/duringThePayment" ,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PaymentRequestDto> saveHcsaPaymentResquset(@RequestBody PaymentRequestDto paymentReqDto);

    @PostMapping(value = "/isTxnRefNo",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> isTxnRef(@RequestBody List<String> txnRefNo);
}
