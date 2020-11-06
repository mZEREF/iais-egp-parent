package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
        log.info(StringUtil.changeForLog("==========>getSessionID:"+bpc.getSession().getId()));
        HttpServletRequest request=bpc.request;
        HttpServletResponse response=bpc.response;
        String txnRes=ParamUtil.getRequestString(request,"message");
        String sessionId= (String) ParamUtil.getSessionAttr(bpc.request,"sessionNetsId");
        String url= AppConsts.REQUEST_TYPE_HTTPS + bpc.request.getServerName()+"/payment-web/process/EGPCLOUD/PaymentCallBack";
        Map<String, String> fields = IaisCommonUtils.genNewHashMap();
        fields.put("sessionId",sessionId);
        StringBuilder bud = new StringBuilder();
        bud.append(url).append("?sessionId=").append(sessionId);
        ParamUtil.setSessionAttr(request,"message",txnRes);

        try {
            RedirectUtil.redirect(bud.toString(), bpc.request, bpc.response);
        } catch (IOException e) {
            log.info(e.getMessage(),e);
        }

    }


    private void appendQueryFields(StringBuilder bud, Map<String, String> fields) throws UnsupportedEncodingException {

        // create a list
        List<String> fieldNames = new ArrayList<String>(fields.keySet());
        Collections.sort(fieldNames);

        Iterator<String> itr = fieldNames.iterator();

        // move through the list and create a series of URL key/value pairs
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = fields.get(fieldName);

            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // append the URL parameters
                bud.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8.name()));
                bud.append('=');
                bud.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.name()));
                // add a '&' to the end if we have more fields coming.
                if (itr.hasNext()) {
                    bud.append('&');
                }
            }
        }
        // remove the end char '&'
        int index = bud.length()-1;
        if("&".equals(bud.substring(index))){
            bud.delete(index, index+1);
        }
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
