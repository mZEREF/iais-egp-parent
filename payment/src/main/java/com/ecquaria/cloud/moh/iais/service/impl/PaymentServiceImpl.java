package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloud.moh.iais.service.PaymentService;
import com.ecquaria.cloud.moh.iais.service.client.PaymentAppGrpClient;
import com.ecquaria.cloud.moh.iais.service.client.PaymentClient;
import com.ecquaria.cloud.payment.PaymentTransactionEntity;
import com.ecquaria.egp.core.payment.api.config.GatewayConfig;
import com.ecquaria.egp.core.payment.api.config.GatewayNetsConfig;
import com.ecquaria.egp.core.payment.runtime.PaymentNetsProxy;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author weilu
 * date 2019/11/9 16:01
 */
@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentClient paymentClient;
    @Autowired
    private PaymentAppGrpClient paymentAppGrpClient;

    @Qualifier(value = "iaisRestTemplate")
    private RestTemplate restTemplate=new RestTemplate();

    @Override
    public void retrieveNetsPayment(PaymentRequestDto paymentRequestDto) throws Exception {
        String strGWPostURL= GatewayNetsConfig.nets_gateway_post_url;
        String keyId= GatewayConfig.eNetsKeyId;
        String secretKey=GatewayConfig.eNetsSecretKey ;
        SoapiS2S soapiTxnQueryReq=new SoapiS2S();
        soapiTxnQueryReq.setSs("1");
        SoapiS2S.Msg msg=new SoapiS2S.Msg();
        msg.setNetsMid(GatewayConfig.eNetsUmId);
        msg.setMerchantTxnRef(paymentRequestDto.getMerchantTxnRef());
        msg.setNetsMidIndicator("U");
        soapiTxnQueryReq.setMsg(msg);
        String status=sendTxnQueryReqToGW(strGWPostURL,secretKey,keyId,soapiTxnQueryReq);

        PaymentDto paymentDto=paymentClient.getPaymentDtoByReqRefNo(paymentRequestDto.getReqRefNo()).getEntity();
        String appGrpNo;
        try{
            appGrpNo=paymentRequestDto.getReqRefNo().substring(0,'_');
        }catch (Exception e){
            appGrpNo=paymentRequestDto.getReqRefNo();
        }
        ApplicationGroupDto applicationGroupDto=paymentAppGrpClient.paymentUpDateByGrpNo(appGrpNo).getEntity();
        if(paymentDto!=null){
            if( "0".equals(status)){
                paymentDto.setPmtStatus(PaymentTransactionEntity.TRANS_STATUS_SUCCESS);
                paymentRequestDto.setStatus(PaymentTransactionEntity.TRANS_STATUS_SUCCESS);
                applicationGroupDto.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
            }else if( "1".equals(status)){
                paymentDto.setPmtStatus(PaymentTransactionEntity.TRANS_STATUS_FAILED);
                paymentRequestDto.setStatus(PaymentTransactionEntity.TRANS_STATUS_FAILED);
                applicationGroupDto.setPmtStatus(PaymentTransactionEntity.TRANS_STATUS_FAILED);
            }
            paymentClient.saveHcsaPayment(paymentDto);
        }else{
            if( "0".equals(status)){
                paymentRequestDto.setStatus(PaymentTransactionEntity.TRANS_STATUS_SUCCESS);
                applicationGroupDto.setPmtStatus(PaymentTransactionEntity.TRANS_STATUS_SUCCESS);
            }else {
                paymentRequestDto.setStatus(PaymentTransactionEntity.TRANS_STATUS_FAILED);
                applicationGroupDto.setPmtStatus(PaymentTransactionEntity.TRANS_STATUS_FAILED);
            }
        }
        paymentClient.updatePaymentResquset(paymentRequestDto);
        paymentAppGrpClient.doUpDate(applicationGroupDto);
    }

    /**
     * KEY-ID - provided by eNETS
     * SECRET-KEY - provided by eNETS
     * @param strGWPostURL - https://<domain-name>/GW2/TxnQuery
     * @param soapiTxnQueryReq - pojo contains getter setter based on message
    format
     * @throws Exception
     *
     */
    public String sendTxnQueryReqToGW(String strGWPostURL, String secretKey,
                                    String keyId, SoapiS2S soapiTxnQueryReq) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String soapiToGW = mapper.writeValueAsString(soapiTxnQueryReq);
        String singatureForReq =
                PaymentNetsProxy.generateSignature(soapiToGW,secretKey); // refer to step b
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("keyId",keyId);
        headers.set("hmac", singatureForReq);
        HttpEntity<String> entity = new HttpEntity<String>(soapiToGW,
                headers);
        ResponseEntity<String> response =
                restTemplate.exchange(strGWPostURL, HttpMethod.POST, entity, String.class);
        log.info("S2S response status : " + response.getStatusCodeValue());
        String stringBody = response.getBody();
        String hmacResponseFromGW = response.getHeaders().getFirst("hmac");
        String hmacForResponseGenerated = PaymentNetsProxy.generateSignature(stringBody,
                secretKey);
        if(hmacForResponseGenerated.equalsIgnoreCase(hmacResponseFromGW)){
//parse json response
            SoapiS2SResponse soapi2Res = mapper.readValue(stringBody,
                    SoapiS2SResponse.class);
            return soapi2Res.getMsg().getNetsTxnStatus();
//handle business logic based on response codes
        }
        else{
            log.error("signature not matched.");
            return "1";
//handle exception flow
        }
    }
}
