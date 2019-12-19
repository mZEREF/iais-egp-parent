package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author weilu
 * date 2019/11/9 15:57
 */
@Delegator(value = "payment")
@Slf4j
public class PaymentDelegator {

    @Autowired
    private PaymentService paymentService;

    public void start(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>payment");
    }


    public void savePayment(BaseProcessClass bpc) throws IOException {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>payment");
        HttpServletRequest request = bpc.request;
        PaymentDto paymentDto = new PaymentDto();
        String result = (String)ParamUtil.getSessionAttr(bpc.request, "result");
        String am = (String)ParamUtil.getSessionAttr(bpc.request, "amount");
        ParamUtil.getSessionAttr(bpc.request, "amount");
        double amount = Double.parseDouble(am);
        String reqRefNo = (String)ParamUtil.getSessionAttr(bpc.request, "reqRefNo");
        String invoiceNo = (String)ParamUtil.getSessionAttr(bpc.request, "invoiceNo");
        String showUrl = "https://" + request.getServerName();
        String s = showUrl+"/hcsa-licence-web/eservice/INTERNET/MohNewApplication/1/doPayment?result=success&reqRefNo="+reqRefNo;
        String url = RedirectUtil.changeUrlToCsrfGuardUrlUrl(s, request);
        bpc.response.sendRedirect(url);
        paymentDto.setAmount(amount);
        paymentDto.setReqRefNo(reqRefNo);
        paymentDto.setInvoiceNo(invoiceNo);
        paymentDto.setStatus(result);
        paymentService.savePayment(paymentDto);

    }
}
