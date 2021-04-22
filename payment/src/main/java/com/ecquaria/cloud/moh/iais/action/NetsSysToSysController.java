package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author junyu
 * @date 2021/03/11
 */
@Controller
@Slf4j
public class NetsSysToSysController {

    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, value =
            "/s2sTxnEnd", method = RequestMethod.POST)
    public ResponseEntity<Void> receiveS2STxnEnd(@RequestBody String txnRes,
                                                 HttpServletRequest request, HttpServletResponse response) throws IOException {
        String sessionId= request.getParameter("reqNo");
        String url= AppConsts.REQUEST_TYPE_HTTPS + request.getServerName()+"/payment-web/eservice/INTERNET/Payment";
        StringBuilder bud = new StringBuilder();
        bud.append(url).append("?reqNo=").append(sessionId);
        String header =  request.getParameter("hmac");
        System.out.println("MerchantApp:s2sTxnEndUrl : txnRes: " + txnRes);
        String macFromGW = request.getHeader("hmac");
        System.out.println("MerchantApp:s2sTxnEndUrl :  hmac received: " + macFromGW);

        System.out.println("MerchantApp:b2sTxnEndUrl : hmac: " + header);
        String message =  request.getParameter("message");//contains TxnRes message
        System.out.println("MerchantApp:b2sTxnEndUrl : data, message: " + message);
        bud.append("&message=").append(message);
        bud.append("&hmac=").append(header);
        RedirectUtil.redirect(bud.toString(), request, response);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }


}
