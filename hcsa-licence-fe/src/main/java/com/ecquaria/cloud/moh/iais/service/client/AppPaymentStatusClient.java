package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.GrioXml.GiroPaymentDto;
import com.ecquaria.cloud.moh.iais.common.dto.GrioXml.GiroPaymentXmlDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author weilu
 * @date 2020/1/14 17:06
 */
@FeignClient(name = "iais-payment", configuration = FeignConfiguration.class,
        fallback = AppPaymentStatusClientFallback.class)
public interface  AppPaymentStatusClient {

    @PostMapping(value = "/iais-payment/payment-status-appGrpId",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<PaymentDto>> getPaymentDtosByReqRefNos();

    @PostMapping(value = "/iais-payment/update-giropaymentxml" ,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<GiroPaymentXmlDto> updateGiroPaymentXmlDto(@RequestBody GiroPaymentXmlDto giroPaymentXmlDto);

    @GetMapping(value = "/iais-payment/get-giropaymentxmls-status-xmltype",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<GiroPaymentXmlDto>> getGiroPaymentDtosByStatusAndXmlType(@RequestParam("status") String status, @RequestParam("xmlType")String xmlType);

    @GetMapping(value = "/iais-payment/get-giropayments-pmtstatus-appgroupno",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<GiroPaymentDto>> getGiroPaymentDtosByPmtStatusAndAppGroupNo(@RequestParam("pmtStatus") String pmtStatus, @RequestParam("appGroupNo")String appGroupNo);

    @PostMapping(value = "/iais-payment/update-giropaymentxmls" ,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<GiroPaymentXmlDto>> updateGiroPaymentXmlDtos(@RequestBody List<GiroPaymentXmlDto>  giroPaymentXmlDtos);

    @PostMapping(value = "/iais-payment/update-giropayment" ,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<GiroPaymentDto> updateGiroPaymentDto(@RequestBody GiroPaymentDto giroPaymentDto);

}
