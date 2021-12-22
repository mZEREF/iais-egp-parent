package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.PaymentRedisHelper;
import com.ecquaria.cloud.moh.iais.service.client.PaymentClient;
import com.ecquaria.cloudfeign.FeignException;
import ecq.commons.exception.BaseException;
import ecq.commons.helper.StringHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import sop.config.ConfigUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * @author weilu
 * date 2019/11/9 15:57
 */
@Delegator(value = "payment")
@Slf4j
public class PaymentDelegator {

    @Autowired
    private PaymentClient paymentClient;

    @Autowired
    private PaymentRedisHelper redisCacheHelper;

    public void start(BaseProcessClass bpc) throws UnsupportedEncodingException, FeignException, BaseException {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>payment");
        log.info(StringUtil.changeForLog("==========>getSessionID:"+bpc.getSession().getId()));
        HttpServletRequest request=bpc.request;

        String reqNo= ParamUtil.getRequestString(request,"reqNo");
        PaymentRequestDto paymentRequestDto=paymentClient.getPaymentRequestDtoByReqRefNo(
                AppConsts.MOH_IAIS_SYSTEM_PAYMENT_CLIENT_KEY, reqNo).getEntity();
        String url=  ConfigUtil.getString( "rvl.baiduri.return.url");
        StringBuilder bud = new StringBuilder();
        String header =  ParamUtil.getRequestString(request,"hmac");
        System.out.println("MerchantApp:b2sTxnEndUrl : hmac: " + header);
        String message =  ParamUtil.getRequestString(request,"message");//contains TxnRes message
//        message=message.replace("https://egp.sit.inter.iais.com/payment-web/eservice/INTERNET/Payment","");
        message=message.replace('\n',' ');
        //System.out.println("MerchantApp:b2sTxnEndUrl : data, message: " + message);
        log.info(StringUtil.changeForLog(StringUtil.changeForLog("==========>MerchantApp:b2sTxnEndUrl : data, message:"+message)));
        log.debug(StringUtil.changeForLog(StringUtil.changeForLog("==========>MerchantApp:b2sTxnEndUrl : data, message:"+message)));
        System.out.println("====>  old Session ID : " + paymentRequestDto.getQueryCode());
        System.out.println("====>  new Session ID : " + bpc.request.getSession().getId());
        redisCacheHelper.copySessionAttr(paymentRequestDto.getQueryCode(),bpc.request);
        String sessionIdStr= (String) ParamUtil.getSessionAttr(request,"sessionNetsId");
        sessionIdStr = new String(Base64.decodeBase64(sessionIdStr.getBytes(StandardCharsets.UTF_8)),StandardCharsets.UTF_8);
        String tinyKey = null;
        if (!StringHelper.isEmpty(sessionIdStr)) {
            sessionIdStr = URLDecoder.decode(sessionIdStr, StandardCharsets.UTF_8.name());
            int sepIndex = sessionIdStr.lastIndexOf(95);
            if (0 < sepIndex) {
                tinyKey = sessionIdStr.substring(sepIndex + 1);
            }
        }
        //System.out.println("====>  payment Session ID : " + sessionId);
        String sessionId = new String(Base64.encodeBase64((request.getSession().getId()+"_"+tinyKey).getBytes(StandardCharsets.UTF_8)),StandardCharsets.UTF_8);

        //String sessionId = URLEncoder.encode(request.getSession().getId(), "UTF-8");

        bud.append(url).append("?sessionId=").append(sessionId);
        ParamUtil.setSessionAttr(request,"message",message);
        ParamUtil.setSessionAttr(request,"header",header);

        try {
            RedirectUtil.redirect(bud.toString(), bpc.request, bpc.response);
        } catch (IOException e) {
            log.info(e.getMessage(),e);
        }

    }



    public void savePayment(BaseProcessClass bpc) throws IOException {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>payment");


    }
}
