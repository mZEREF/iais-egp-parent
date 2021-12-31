package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.PaymentService;
import com.ecquaria.cloud.moh.iais.service.client.PaymentAppGrpClient;
import com.ecquaria.cloud.moh.iais.service.client.PaymentClient;
import com.ecquaria.cloud.payment.PaymentTransactionEntity;
import com.ecquaria.egp.core.payment.api.config.GatewayConfig;
import com.ecquaria.egp.core.payment.runtime.PaymentNetsProxy;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

/**
 * @author weilu
 * date 2019/11/9 16:01
 */
@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    @Value("${iais.hmac.keyId}")
    private String keyId1;
    @Value("${iais.inter.gateway.url}")
    private String gateWayUrl;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey1;

    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;
    @Autowired
    private PaymentClient paymentClient;
    @Autowired
    private PaymentAppGrpClient paymentAppGrpClient;

    @Override
    public void retrieveNetsPayment(PaymentRequestDto paymentRequestDto) throws Exception {
        String keyId= GatewayConfig.eNetsKeyId;
        String secretKey=GatewayConfig.eNetsSecretKey ;
        SoapiS2S soapiTxnQueryReq=new SoapiS2S();
        soapiTxnQueryReq.setSs("1");
        SoapiS2S.Msg msg=new SoapiS2S.Msg();
        msg.setNetsMid(GatewayConfig.eNetsUmId);
        msg.setMerchantTxnRef(paymentRequestDto.getMerchantTxnRef());
        msg.setNetsMidIndicator("U");
        soapiTxnQueryReq.setMsg(msg);
        SoapiS2SResponse soapiS2SResponse =sendTxnQueryReqToGW(secretKey,keyId,soapiTxnQueryReq);

        PaymentDto paymentDto=paymentClient.getPaymentDtoByReqRefNo(AppConsts.MOH_IAIS_SYSTEM_PAYMENT_CLIENT_KEY,
                paymentRequestDto.getReqRefNo()).getEntity();
        String appGrpNo;
        try{
            appGrpNo=paymentRequestDto.getReqRefNo().substring(0,paymentRequestDto.getReqRefNo().indexOf('_'));
        }catch (Exception e){
            appGrpNo=paymentRequestDto.getReqRefNo();
        }
        ApplicationGroupDto applicationGroupDto=paymentAppGrpClient.paymentUpDateByGrpNo(appGrpNo).getEntity();
        if(paymentDto!=null){
            paymentDto.setResponseMsg(JsonUtil.parseToJson(soapiS2SResponse));
            if( "0".equals(soapiS2SResponse.getMsg().getNetsTxnStatus())){
                paymentDto.setPmtStatus(PaymentTransactionEntity.TRANS_STATUS_SUCCESS);
                paymentRequestDto.setStatus(PaymentTransactionEntity.TRANS_STATUS_SUCCESS);
                applicationGroupDto.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
            }else {
                paymentDto.setPmtStatus(PaymentTransactionEntity.TRANS_STATUS_FAILED);
                paymentRequestDto.setStatus(PaymentTransactionEntity.TRANS_STATUS_FAILED);
                //applicationGroupDto.setPmtStatus(PaymentTransactionEntity.TRANS_STATUS_FAILED);
            }
        }else{
            paymentDto = new PaymentDto();
            paymentDto.setResponseMsg(JsonUtil.parseToJson(soapiS2SResponse));
            paymentDto.setAmount(paymentRequestDto.getAmount());
            paymentDto.setReqRefNo(paymentRequestDto.getReqRefNo());
            paymentDto.setTxnRefNo("TRANS");
            paymentDto.setInvoiceNo("1234567");

            if("0".equals(soapiS2SResponse.getMsg().getNetsTxnStatus())){
                paymentRequestDto.setStatus(PaymentTransactionEntity.TRANS_STATUS_SUCCESS);
                paymentDto.setPmtStatus(PaymentTransactionEntity.TRANS_STATUS_SUCCESS);
                applicationGroupDto.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
            }else {
                paymentRequestDto.setStatus(PaymentTransactionEntity.TRANS_STATUS_FAILED);
                paymentDto.setPmtStatus(PaymentTransactionEntity.TRANS_STATUS_FAILED);
                //applicationGroupDto.setPmtStatus(PaymentTransactionEntity.TRANS_STATUS_FAILED);
            }
            paymentDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        }
        applicationGroupDto.setPaymentDt(new Date());
        applicationGroupDto.setPmtRefNo(paymentDto.getReqRefNo());
        applicationGroupDto.setPayMethod(ApplicationConsts.PAYMENT_METHOD_NAME_CREDIT);
        applicationGroupDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        if(applicationGroupDto.getPmtStatus().equals(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS)){
            paymentAppGrpClient.doPaymentUpDate(applicationGroupDto);
        }
        paymentDto.setSystemClientId(AppConsts.MOH_IAIS_SYSTEM_PAYMENT_CLIENT_KEY);
        paymentClient.saveHcsaPayment(paymentDto);
        paymentRequestDto.setSystemClientId(AppConsts.MOH_IAIS_SYSTEM_PAYMENT_CLIENT_KEY);
        paymentClient.updatePaymentResquset(paymentRequestDto);
    }

    /**
     * KEY-ID - provided by eNETS
     * SECRET-KEY - provided by eNETS
     * @param soapiTxnQueryReq - pojo contains getter setter based on message
    format
     * @throws Exception
     *
     */
    @Override
    public SoapiS2SResponse sendTxnQueryReqToGW( String secretKey,
                                    String keyId, SoapiS2S soapiTxnQueryReq) throws Exception {
        String strGWPostURL= gateWayUrl+"/v1/enets/GW2/TxnQuery";
        ObjectMapper mapper = new ObjectMapper();
        String soapiToGW = mapper.writeValueAsString(soapiTxnQueryReq);
        String singatureForReq =
                PaymentNetsProxy.generateSignature(soapiToGW,secretKey); // refer to step b
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId1, secretKey1);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("keyId",keyId);
        headers.set("hmac", singatureForReq);
        headers.set("date", signature.date());
        headers.set("authorization", signature.authorization());
        headers.set("date-Secondary", signature2.date());
        headers.set("authorization-Secondary", signature2.authorization());
        HttpEntity<String> entity = new HttpEntity<String>(soapiToGW,
                headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response =
                restTemplate.exchange(strGWPostURL, HttpMethod.POST, entity, String.class);
        log.info(StringUtil.changeForLog("S2S response status : " + response.getStatusCodeValue()));
        String stringBody = response.getBody();
        AuditTrailDto auditTrailDto = new AuditTrailDto();
        auditTrailDto.setOperation(AuditTrailConsts.OPERATION_FOREIGN_INTERFACE);
        auditTrailDto.setOperationType(AuditTrailConsts.OPERATION_TYPE_INTERNET);
        auditTrailDto.setModule("Payment");
        auditTrailDto.setFunctionName("enets Retrieve Payment");
        auditTrailDto.setBeforeAction(soapiToGW);
        auditTrailDto.setAfterAction(response.getBody());
        AuditTrailHelper.callSaveAuditTrail(auditTrailDto);
        String hmacResponseFromGW = response.getHeaders().getFirst("hmac");
        String hmacForResponseGenerated = PaymentNetsProxy.generateSignature(stringBody,
                secretKey);
        if(hmacForResponseGenerated.equalsIgnoreCase(hmacResponseFromGW)){
//parse json response
            SoapiS2SResponse soapi2Res = mapper.readValue(stringBody,
                    SoapiS2SResponse.class);
            return soapi2Res;
//handle business logic based on response codes
        }
        else{
            log.error("signature not matched.");
            return null;
//handle exception flow
        }
    }

    @Override
    public void retrievePayNowPayment(PaymentRequestDto paymentRequestDto)  {
        String appGrpNo;
        try{
            appGrpNo=paymentRequestDto.getReqRefNo().substring(0,paymentRequestDto.getReqRefNo().indexOf('_'));
        }catch (Exception e){
            appGrpNo=paymentRequestDto.getReqRefNo();
        }
        PaymentDto paymentDto=paymentClient.getPaymentDtoByReqRefNo(AppConsts.MOH_IAIS_SYSTEM_PAYMENT_CLIENT_KEY,
                appGrpNo).getEntity();
        ApplicationGroupDto applicationGroupDto=paymentAppGrpClient.paymentUpDateByGrpNo(appGrpNo).getEntity();
        if(paymentDto!=null){
            paymentDto.setPmtStatus(PaymentTransactionEntity.TRANS_STATUS_SUCCESS);
            paymentRequestDto.setStatus(PaymentTransactionEntity.TRANS_STATUS_SUCCESS);
            applicationGroupDto.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
            paymentDto.setSystemClientId(AppConsts.MOH_IAIS_SYSTEM_PAYMENT_CLIENT_KEY);
            paymentClient.saveHcsaPayment(paymentDto);
        }else{
            paymentRequestDto.setStatus(PaymentTransactionEntity.TRANS_STATUS_FAILED);
        }
        applicationGroupDto.setPaymentDt(new Date());
        applicationGroupDto.setPmtRefNo(appGrpNo);
        applicationGroupDto.setPayMethod(ApplicationConsts.PAYMENT_METHOD_NAME_PAYNOW);
        applicationGroupDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        if(applicationGroupDto.getPmtStatus().equals(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS)){
            paymentAppGrpClient.doPaymentUpDate(applicationGroupDto);
        }
        paymentRequestDto.setSystemClientId(AppConsts.MOH_IAIS_SYSTEM_PAYMENT_CLIENT_KEY);
        paymentClient.updatePaymentResquset(paymentRequestDto);
    }
}
