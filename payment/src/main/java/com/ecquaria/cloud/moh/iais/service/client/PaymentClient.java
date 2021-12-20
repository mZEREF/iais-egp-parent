package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.SrcSystemConfDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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

    @PostMapping(value = "/iais-payment/duringTheSrcSystemConfDto" ,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SrcSystemConfDto> accessApplicationSrcSystemConfDto(@RequestBody SrcSystemConfDto srcSystemConfDto);

    @PostMapping(value = "/iais-payment/update-payment-resquset" ,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PaymentRequestDto> updatePaymentResquset(@RequestBody PaymentRequestDto paymentReqDto);
    @PostMapping(value = "/iais-payment/isTxnRefNo",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> isTxnRef(@RequestBody List<String> txnRefNo, @RequestParam(name = "systemClientId") String systemClientId);

    @GetMapping(value = "/iais-payment/payment-reqRefNo/{sysClientId}/{reqRefNo}",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PaymentDto> getPaymentDtoByReqRefNo(@PathVariable("sysClientId") String sysClientId,
                                                            @PathVariable("reqRefNo") String reqRefNo);

    @GetMapping(value = "/iais-payment/payment-request-reqRefNo/{sysClientId}/{reqRefNo}",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PaymentRequestDto> getPaymentRequestDtoByReqRefNo(@PathVariable("sysClientId") String sysClientId,
                                                                          @PathVariable("reqRefNo") String reqRefNo);

    @GetMapping(value = "/iais-payment/payment-request-reqRefNoLike/{sysClientId}/{reqRefNo}",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<PaymentRequestDto>> getPaymentRequestDtoByReqRefNoLike(@PathVariable("sysClientId") String sysClientId,
                                                                                    @PathVariable("reqRefNo") String reqRefNo);

    @GetMapping(value = "/iais-payment/paying-payment-requests/{sysClientId}")
    FeignResponseEntity<List<PaymentRequestDto>> getAllPayingPaymentRequestDto(@PathVariable("sysClientId") String sysClientId);
}
