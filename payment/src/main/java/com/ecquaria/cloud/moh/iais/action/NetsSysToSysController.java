package com.ecquaria.cloud.moh.iais.action;

import com.dbs.sgqr.generator.QRGenerator;
import com.dbs.sgqr.generator.QRGeneratorImpl;
import com.dbs.sgqr.generator.io.PayNow;
import com.dbs.sgqr.generator.io.QRDimensions;
import com.dbs.sgqr.generator.io.QRGeneratorResponse;
import com.dbs.sgqr.generator.io.QRType;
import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.entity.sopprojectuserassignment.PaymentBaiduriProxyUtil;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.PaymentRedisHelper;
import com.ecquaria.cloud.moh.iais.service.client.PaymentClient;
import com.ecquaria.cloud.payment.PaymentTransactionEntity;
import com.ecquaria.cloudfeign.FeignException;
import com.ecquaria.egp.core.payment.api.config.GatewayConfig;
import com.ecquaria.egp.core.payment.runtime.Soapi;
import ecq.commons.exception.BaseException;
import ecq.commons.helper.StringHelper;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sop.config.ConfigUtil;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.ecquaria.egp.core.payment.runtime.PaymentNetsProxy.generateSignature;

/**
 * @author junyu
 * @date 2021/03/11
 */
@Controller
@Slf4j
public class NetsSysToSysController {
    @Autowired
    private PaymentClient paymentClient;
    public static final Locale LOCALE = new Locale("en", "SG");
    static BASE64Decoder decoder = new sun.misc.BASE64Decoder();

    @Autowired
    private PaymentRedisHelper redisCacheHelper;
    @RequestMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, value =
            "/s2sTxnEnd", method = RequestMethod.POST)
    public ResponseEntity<Void> receiveS2STxnEnd(@RequestBody String txnRes,
                                                 HttpServletRequest request, HttpServletResponse response) throws IOException, FeignException, BaseException {
        StringBuilder bud = new StringBuilder();
        String header =  request.getParameter("hmac");
        System.out.println("MerchantApp:s2sTxnEndUrl : txnRes: " + txnRes);
        String macFromGW = request.getHeader("hmac");
        System.out.println("MerchantApp:s2sTxnEndUrl :  hmac received: " + macFromGW);

        System.out.println("MerchantApp:b2sTxnEndUrl : hmac: " + header);
        String message =  request.getParameter("message");//contains TxnRes message
        System.out.println("MerchantApp:b2sTxnEndUrl : data, message: " + message);


        String reqNo= ParamUtil.getRequestString(request,"reqNo");
        PaymentRequestDto paymentRequestDto=paymentClient.getPaymentRequestDtoByReqRefNo(reqNo).getEntity();
        String url=  ConfigUtil.getString( "rvl.baiduri.return.url");
        System.out.println("MerchantApp:b2sTxnEndUrl : hmac: " + header);
//        message=message.replace("https://egp.sit.inter.iais.com/payment-web/eservice/INTERNET/Payment","");
        message=message.replace('\n',' ');
        //System.out.println("MerchantApp:b2sTxnEndUrl : data, message: " + message);
        log.info(StringUtil.changeForLog(StringUtil.changeForLog("==========>MerchantApp:b2sTxnEndUrl : data, message:"+message)));
        log.debug(StringUtil.changeForLog(StringUtil.changeForLog("==========>MerchantApp:b2sTxnEndUrl : data, message:"+message)));
        System.out.println("====>  old Session ID : " + paymentRequestDto.getQueryCode());
        System.out.println("====>  new Session ID : " + request.getSession().getId());
        try {
            redisCacheHelper.copySessionAttr(paymentRequestDto.getQueryCode(),request);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
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

        JSONObject jsonObject = JSONObject.fromObject(message);
        Soapi txnResObj = (Soapi) JSONObject.toBean(jsonObject, Soapi.class);
        String generatedHmac= null;
        try {
            generatedHmac = generateSignature(message, GatewayConfig.eNetsSecretKey);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        String eNetsStatus="1";
        if(txnResObj!=null&&txnResObj.getMsg()!=null&&header.equals(generatedHmac)){
            eNetsStatus= txnResObj.getMsg().getNetsTxnStatus();
        }
        String appGrpNo= reqNo;
        try {
            appGrpNo= reqNo.substring(0,'_');
        }catch (Exception e){
            log.error(StringUtil.changeForLog("appGrpNo not found :==== >>>"+ reqNo));
        }
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
        try {
            RedirectUtil.redirect(bud.toString(), request, response);
        } catch (IOException e) {
            log.info(e.getMessage(),e);
        }
        return new ResponseEntity<Void>(HttpStatus.OK);
    }


    @RequestMapping( value = "/payNowRefresh", method = RequestMethod.GET)
    public @ResponseBody
    String payNowImgStringRefresh(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException {
        String amoStr = (String) ParamUtil.getSessionAttr(request,"payNowAmo");
        String reqNo = (String) ParamUtil.getSessionAttr(request,"payNowReqNo");
        PaymentDto paymentDto=paymentClient.getPaymentDtoByReqRefNo(reqNo).getEntity();
        if(paymentDto!=null&&paymentDto.getPmtStatus().equals(PaymentTransactionEntity.TRANS_STATUS_SUCCESS)){
            String url=  (String) ParamUtil.getSessionAttr(request,"vpc_ReturnURL");
            try {
                log.info("payNow SUCCESS");
                RedirectUtil.redirect(url, request, response);
            } catch (IOException e) {
                log.info(e.getMessage(),e);
            }
        }
        String appGrpNo=reqNo;
        try {
            appGrpNo=reqNo.substring(0,'_');
        }catch (Exception e){
            log.error(StringUtil.changeForLog("appGrpNo not found :==== >>>"+reqNo));
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
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, 1);
        String expiryDate = df.format(cal.getTime());


        PayNow payNowObject = qrGenerator.getPayNowObject("0000", "702", "SG", "McDonalds SG", "Singapore", "SG.PAYNOW", "2", "12345678U12A", "1", expiryDate,"12", amoStr,appGrpNo);
        payNowObject.setPayloadFormatInd("01");

        // PayNow
        QRGeneratorResponse qrCodeResponse = qrGenerator.generateSGQR(QRType.PAY_NOW, payNowObject, qrDetails);
        String sgqrTypeFormattedPayLoad = qrCodeResponse.getSgqrPayload();
        System.out.println(sgqrTypeFormattedPayLoad);
        String imageStreamInBase64Format = qrCodeResponse.getImageStream();
        System.out.println(imageStreamInBase64Format);
        ParamUtil.setSessionAttr(request, "imageStreamInBase64Format",imageStreamInBase64Format);
        return imageStreamInBase64Format;
    }
}
