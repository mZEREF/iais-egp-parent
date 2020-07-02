package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

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
        Double amount = (Double)ParamUtil.getSessionAttr(bpc.request, "amount");
        ParamUtil.getSessionAttr(bpc.request, "amount");
        String reqRefNo = (String)ParamUtil.getSessionAttr(bpc.request, "reqRefNo");
        String invoiceNo = (String)ParamUtil.getSessionAttr(bpc.request, "invoiceNo");
        String backUrl = (String) ParamUtil.getSessionAttr(bpc.request,"backUrl");
        paymentDto.setAmount(amount);
        paymentDto.setReqRefNo(reqRefNo);
        paymentDto.setInvoiceNo(invoiceNo);
        paymentDto.setPmtStatus(result);
        PaymentDto paymentDtoSave = paymentService.savePayment(paymentDto);
        Date txnDtD = paymentDtoSave.getTxnDt();
        String txnDt = Formatter.formatDateTime(txnDtD, "dd/MM/yyyy HH:mm:ss");
        String txnRefNo = paymentDtoSave.getTxnRefNo();
        String showUrl = "https://" + request.getServerName();
        String results="?result="+MaskUtil.maskValue("result","success")+"&reqRefNo="+MaskUtil.maskValue("reqRefNo",reqRefNo)+"&txnDt="+MaskUtil.maskValue("txnDt",txnDt)+"&txnRefNo="+MaskUtil.maskValue("txnRefNo",txnRefNo);
        String s = showUrl+"/hcsa-licence-web/eservice/INTERNET/MohNewApplication/1/doPayment"+results;
        if(!StringUtil.isEmpty(backUrl)){
             s = showUrl +"/" +backUrl+results;
        }
        String url = RedirectUtil.appendCsrfGuardToken(s, request);
        bpc.response.sendRedirect(url);

    }
}
