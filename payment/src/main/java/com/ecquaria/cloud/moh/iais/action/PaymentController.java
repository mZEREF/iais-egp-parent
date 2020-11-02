package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.SoapiS2S;
import com.ecquaria.egp.core.payment.api.config.GatewayConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

import static com.ecquaria.egp.core.payment.runtime.PaymentNetsProxy.generateSignature;

/**
 * PaymentController
 *
 * @author junyu
 * @date 2020/10/23
 */
@Controller
@Slf4j
public class PaymentController {

    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, value =
            "/s2sTxnEnd", method = RequestMethod.POST)
    public ResponseEntity<Void> receiveS2STxnEnd(@RequestBody String txnRes,
                                                 HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("MERCHANT APP : in receiveS2STxnEnd :" + txnRes));//jsonmessage received as string
        try {
            String generatedHmac = generateSignature(txnRes, GatewayConfig.eNetsSecretKey );//generate mac
            String macFromGW = request.getHeader("hmac");
            log.info (StringUtil.changeForLog("MERCHANT APP : header hmac received :" +
                    macFromGW));//
            log.info(StringUtil.changeForLog("MERCHANT APP : header hmac generated :" +
                    generatedHmac));
            if(generatedHmac.equalsIgnoreCase(macFromGW)){
//parse message
                ObjectMapper mapper = new ObjectMapper();
                SoapiS2S txnResObj = mapper.readValue(txnRes, SoapiS2S.class);
                log.info(StringUtil.changeForLog("MERCHANT APP : in receiveS2STxnEnd :" + txnResObj));
                //Please handle success or failure response code
            }
            else{
                log.error("signature not matched.");
//handle exception flow
            }
        } catch (Exception e) {
// TODO handle exception
            log.error(e.getMessage());}
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
