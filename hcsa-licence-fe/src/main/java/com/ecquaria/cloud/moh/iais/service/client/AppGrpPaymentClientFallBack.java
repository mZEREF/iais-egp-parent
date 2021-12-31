package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

import java.util.List;

/**
 * @author weilu
 * @date 12/10/2019 4:59 PM
 */

public class AppGrpPaymentClientFallBack implements AppGrpPaymentClient {


    @Override
    public FeignResponseEntity<PaymentDto> getPaymentDtoByReqRefNo(String reqRefNo, String sysClientId) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<PaymentRequestDto> getPaymentRequestDtoByReqRefNo(String reqRefNo) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<List<PaymentRequestDto>> getPaymentRequestDtoByReqRefNoLike(String sysClientId, String reqRefNo) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}
