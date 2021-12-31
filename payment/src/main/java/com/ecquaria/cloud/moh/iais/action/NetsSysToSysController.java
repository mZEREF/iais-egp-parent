package com.ecquaria.cloud.moh.iais.action;

import com.dbs.sgqr.generator.QRGenerator;
import com.dbs.sgqr.generator.QRGeneratorImpl;
import com.dbs.sgqr.generator.io.PayNow;
import com.dbs.sgqr.generator.io.QRDimensions;
import com.dbs.sgqr.generator.io.QRGeneratorResponse;
import com.dbs.sgqr.generator.io.QRType;
import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.entity.sopprojectuserassignment.PaymentBaiduriProxyUtil;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.PaymentRedisHelper;
import com.ecquaria.cloud.moh.iais.service.client.PaymentClient;
import com.ecquaria.cloud.payment.PaymentTransactionEntity;
import com.ecquaria.egp.core.payment.api.config.GatewayConfig;
import com.ecquaria.egp.core.payment.api.config.GatewayPayNowConfig;
import com.ecquaria.egp.core.payment.runtime.Soapi;
import ecq.commons.helper.StringHelper;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import sop.config.ConfigUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import static com.ecquaria.egp.core.payment.runtime.PaymentNetsProxy.generateSignature;

/**
 * @author junyu
 * @date 2021/03/11
 */
@Slf4j
@Controller
public class NetsSysToSysController {
    @Autowired
    private PaymentClient paymentClient;
    public static final Locale LOCALE = new Locale("en", "SG");
    @Value("${paynow.qr.expiry.minutes}")
    private int expiryMinutes;
    @Autowired
    private PaymentRedisHelper redisCacheHelper;
    @RequestMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, value =
            "/s2sTxnEnd", method = RequestMethod.POST)
    public ResponseEntity<Void> receiveS2STxnEnd(@RequestBody String txnRes,
                                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        StringBuilder bud = new StringBuilder();
        String header =  request.getParameter("hmac");

        String message =  request.getParameter("message");//contains TxnRes message


        String reqNo= ParamUtil.getRequestString(request,"reqNo");
        PaymentRequestDto paymentRequestDto=paymentClient.getPaymentRequestDtoByReqRefNo(
                AppConsts.MOH_IAIS_SYSTEM_PAYMENT_CLIENT_KEY, reqNo).getEntity();
        String url=  ConfigUtil.getString( "rvl.baiduri.return.url");
//        message=message.replace("https://egp.sit.inter.iais.com/payment-web/eservice/INTERNET/Payment","");
        message=message.replace('\n',' ');


        redisCacheHelper.copySessionAttr(paymentRequestDto.getQueryCode(),request);

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
        String sessionId = new String(Base64.encodeBase64((request.getSession().getId()+"_"+tinyKey).getBytes(StandardCharsets.UTF_8)),StandardCharsets.UTF_8);

        //String sessionId = URLEncoder.encode(request.getSession().getId(), "UTF-8");

        bud.append(url).append("?sessionId=").append(sessionId);
        ParamUtil.setSessionAttr(request,"message",message);
        ParamUtil.setSessionAttr(request,"header",header);

        JSONObject jsonObject = JSONObject.fromObject(message);
        Soapi txnResObj = (Soapi) JSONObject.toBean(jsonObject, Soapi.class);
        String generatedHmac= null;
        generatedHmac = generateSignature(message, GatewayConfig.eNetsSecretKey);

        String eNetsStatus="1";
        if(txnResObj!=null&&txnResObj.getMsg()!=null&&header.equals(generatedHmac)){
            eNetsStatus= txnResObj.getMsg().getNetsTxnStatus();
        }
        String appGrpNo= reqNo;
        appGrpNo= reqNo.substring(0,reqNo.indexOf('_'));

        ApplicationGroupDto applicationGroupDto= PaymentBaiduriProxyUtil.getPaymentAppGrpClient().paymentUpDateByGrpNo(appGrpNo).getEntity();
        if(applicationGroupDto!=null){
            if ("0".equals(eNetsStatus)){
                applicationGroupDto.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
                applicationGroupDto.setPmtRefNo(reqNo);
                applicationGroupDto.setPaymentDt(new Date());
                applicationGroupDto.setPayMethod(ApplicationConsts.PAYMENT_METHOD_NAME_NETS);
                PaymentBaiduriProxyUtil.getPaymentAppGrpClient().doPaymentUpDate(applicationGroupDto);
            }
        }
        RedirectUtil.redirect(bud.toString(), request, response);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }


    @RequestMapping( value = "/payNowRefresh", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> payNowImgStringRefresh(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        String amoStr = (String) ParamUtil.getSessionAttr(request,"payNowAmo");
        String reqNo = (String) ParamUtil.getSessionAttr(request,"payNowReqNo");
        String appGrpNo=reqNo.substring(0,reqNo.indexOf('_'));
        PaymentDto paymentDto=paymentClient.getPaymentDtoByReqRefNo(AppConsts.MOH_IAIS_SYSTEM_PAYMENT_CLIENT_KEY, appGrpNo).getEntity();
        if(paymentDto!=null&&paymentDto.getPmtStatus().equals(PaymentTransactionEntity.TRANS_STATUS_SUCCESS)){
            AuditTrailDto auditTrailDto = new AuditTrailDto();
            auditTrailDto.setOperation(AuditTrailConsts.OPERATION_FOREIGN_INTERFACE);
            auditTrailDto.setOperationType(AuditTrailConsts.OPERATION_TYPE_INTERNET);
            auditTrailDto.setModule("Payment");
            auditTrailDto.setFunctionName("payNow Call Back");
            auditTrailDto.setAfterAction(paymentDto.getResponseMsg());
            AuditTrailHelper.callSaveAuditTrail(auditTrailDto);
            map.put("result", "Success");
            return map;
        }


        QRGenerator qrGenerator = new QRGeneratorImpl();

        File inputFile = ResourceUtils.getFile("classpath:image/paymentPayNow.png");
        //sample
        QRDimensions qrDetails = qrGenerator.getQRDimensions(200, 200, Color.decode("#7C1A78"), inputFile.getPath());

        // sample Static QR
        //PayNow payNowObject = qrGenerator.getPayNowObject("0000", "702", "SG", "McDonalds SG", "Singapore", "SG.PAYNOW", "2", "12345678U12A", "1", "20181225");

        //sample Dynamic QR
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss", LOCALE);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, expiryMinutes);
        String expiryDate = df.format(cal.getTime());
        log.info("merchantCategoryCode {}",GatewayPayNowConfig.merchantCategoryCode);
        log.info("txnCurrency {}",GatewayPayNowConfig.txnCurrency);
        log.info("countryCode {}",GatewayPayNowConfig.countryCode);
        log.info("merchantName {}",GatewayPayNowConfig.merchantName);
        log.info("merchantCity {}",GatewayPayNowConfig.merchantCity);
        log.info("globalUniqueID {}",GatewayPayNowConfig.globalUniqueID);
        log.info("proxyType {}",GatewayPayNowConfig.proxyType);
        log.info("proxyValue {}",GatewayPayNowConfig.proxyValue);
        log.info("editableAmountInd {}",GatewayPayNowConfig.editableAmountInd);
        log.info("expiryDate {}",expiryDate);
        log.info("pointOfIntiation {}",GatewayPayNowConfig.pointOfIntiation);
        log.info("amount {}",amoStr);
        log.info("billReferenceNumber {}",appGrpNo);
        log.info("payloadFormatInd {}",GatewayPayNowConfig.payloadFormatInd);

        PayNow payNowObject = qrGenerator.getPayNowObject(GatewayPayNowConfig.merchantCategoryCode,
                GatewayPayNowConfig.txnCurrency, GatewayPayNowConfig.countryCode,
                GatewayPayNowConfig.merchantName, GatewayPayNowConfig.merchantCity,
                GatewayPayNowConfig.globalUniqueID, GatewayPayNowConfig.proxyType,
                GatewayPayNowConfig.proxyValue, GatewayPayNowConfig.editableAmountInd,
                expiryDate,GatewayPayNowConfig.pointOfIntiation, amoStr,appGrpNo);
        payNowObject.setPayloadFormatInd(GatewayPayNowConfig.payloadFormatInd);

        // PayNow
        QRGeneratorResponse qrCodeResponse = qrGenerator.generateSGQR(QRType.PAY_NOW, payNowObject, qrDetails);
        String sgqrTypeFormattedPayLoad = qrCodeResponse.getSgqrPayload();
        String imageStreamInBase64Format = qrCodeResponse.getImageStream();
        ParamUtil.setSessionAttr(request, "imageStreamInBase64Format",imageStreamInBase64Format);
        map.put("result", "Fail");
        map.put("QrString", imageStreamInBase64Format);
        return map;
    }


    @RequestMapping( value = "/payNowPoll", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> payNowPoll(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        String amoStr = (String) ParamUtil.getSessionAttr(request,"payNowAmo");
        String reqNo = (String) ParamUtil.getSessionAttr(request,"payNowReqNo");
        String appGrpNo=reqNo.substring(0,reqNo.indexOf('_'));
        PaymentDto paymentDto=paymentClient.getPaymentDtoByReqRefNo(AppConsts.MOH_IAIS_SYSTEM_PAYMENT_CLIENT_KEY, appGrpNo).getEntity();
        if(paymentDto!=null&&paymentDto.getPmtStatus().equals(PaymentTransactionEntity.TRANS_STATUS_SUCCESS)){
            AuditTrailDto auditTrailDto = new AuditTrailDto();
            auditTrailDto.setOperation(AuditTrailConsts.OPERATION_FOREIGN_INTERFACE);
            auditTrailDto.setOperationType(AuditTrailConsts.OPERATION_TYPE_INTERNET);
            auditTrailDto.setModule("Payment");
            auditTrailDto.setFunctionName("payNow Call Back");
            auditTrailDto.setAfterAction(paymentDto.getResponseMsg());
            AuditTrailHelper.callSaveAuditTrail(auditTrailDto);
            map.put("result", "Success");
            return map;
        }
        map.put("result", "Fail");
        return map;

    }

    @RequestMapping( value = "/payNowMockServer", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> payNowMockServer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        String reqNo = (String) ParamUtil.getSessionAttr(request,"payNowReqNo");
        String appGrpNo=reqNo.substring(0,reqNo.indexOf('_'));
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> params=IaisCommonUtils.genNewHashMap();
        params.put("responseUrl",GatewayPayNowConfig.mockserverCallbackUrl);
        params.put("appGrpNum", appGrpNo);

        StringBuilder sb = new StringBuilder(GatewayPayNowConfig.mockserverUrl);
        sb.append('?');
        for (String key : params.keySet()) {
            sb.append(key).append("={").append(key).append("}&");
        }
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<String> s =restTemplate.exchange(sb.substring(0, sb.length() - 1),  HttpMethod.GET, entity, String.class, params);
        map.put("result", "Success");
        return map;

    }
}
