package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.PaymentRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author weilu
 * date 2019/11/9 13:57
 */

@Delegator(value = "paymentRequest")
@Slf4j
public class PaymentRequestDelegator {

    @Autowired
    private PaymentRequestService paymentRequestService ;


    public void start(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>paymentRequest");
    }


    public void savePaymentRequest(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>saveData");
        HttpServletRequest request = bpc.request;
        PaymentRequestDto paymentRequestDto = new PaymentRequestDto();
        String amo = request.getParameter("amount");
        String payMethod =request.getParameter("payMethod");
        String reqNo = request.getParameter("reqNo");
        if(!StringUtil.isEmpty(amo)&&!StringUtil.isEmpty(payMethod)&&!StringUtil.isEmpty(reqNo)) {
            log.info("------------------------------------->>>save****************");
            double amount = Double.parseDouble(amo);
            paymentRequestDto.setAmount(amount);
            paymentRequestDto.setPayMethod(payMethod);
            paymentRequestDto.setReqDt(new Date());
            paymentRequestDto.setReqRefNo(reqNo);
        }
        paymentRequestService.savePaymentRequestDto(paymentRequestDto);
    }








}
