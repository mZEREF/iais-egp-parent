package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;

import java.util.List;

/**
 * @author weilu
 * @date 12/10/2019 4:59 PM
 */

public class AppGrpPaymentClientFallBack implements AppGrpPaymentClient {


    @Override
    public FeignResponseEntity<PaymentDto> getPaymentDtoByReqRefNo(String reqRefNo, String sysClientId) {
        return IaisEGPHelper.getFeignResponseEntity("getPaymentDtoByReqRefNo",reqRefNo,sysClientId);
    }

    @Override
    public FeignResponseEntity<PaymentRequestDto> getPaymentRequestDtoByReqRefNo(String reqRefNo) {
        return IaisEGPHelper.getFeignResponseEntity("getPaymentRequestDtoByReqRefNo",reqRefNo);
    }

    @Override
    public FeignResponseEntity<List<PaymentRequestDto>> getPaymentRequestDtoByReqRefNoLike(String sysClientId, String reqRefNo) {
        return IaisEGPHelper.getFeignResponseEntity("getPaymentRequestDtoByReqRefNoLike",sysClientId,reqRefNo);
    }
}
