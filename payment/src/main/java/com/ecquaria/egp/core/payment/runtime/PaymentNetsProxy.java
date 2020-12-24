package com.ecquaria.egp.core.payment.runtime;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.entity.sopprojectuserassignment.PaymentBaiduriProxyUtil;
import com.ecquaria.cloud.entity.sopprojectuserassignment.SMCStringHelperUtil;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.payment.PaymentTransactionEntity;
import com.ecquaria.egp.api.EGPCaseHelper;
import com.ecquaria.egp.core.payment.PaymentData;
import com.ecquaria.egp.core.payment.api.config.GatewayConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ecq.commons.helper.StringHelper;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import sop.config.ConfigUtil;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
public class PaymentNetsProxy extends PaymentProxy {


	public static final String DEFAULT_ENCODING = "UTF-8";
	public static final String IMPL_CONTINUE_TOKEN_PREFIX = "IMPL_CONTINUE_TOKEN_";

	public static final String DEFAULT_SECURE_HASH_TYPE = "SHA256";

	static final String SECURE_SECRET = "6A92740F77EFF1C21DFF9281EC53C519";

	private static final char[] HEX_TABLE = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	@Override
	public void pay(BaseProcessClass bpc) throws PaymentException {
		AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_ONLINE_PAYMENT, AuditTrailConsts.FUNCTION_ONLINE_PAYMENT);

		String continueToken = getContinueToken();
		bpc.request.getSession().setAttribute(IMPL_CONTINUE_TOKEN_PREFIX + getTinyKey(), continueToken);

		String bigsURL = AppConsts.REQUEST_TYPE_HTTPS + bpc.request.getServerName()+ConfigUtil.getString("eNets.url");
		if (StringHelper.isEmpty(bigsURL)) {
			throw new PaymentException("Nets.url is not set.");
		}
		Map<String, String> fields = null;
		try {
			fields = getFieldsMap(bpc);
			fields.put("vpc_ReturnURL",AppConsts.REQUEST_TYPE_HTTPS + bpc.request.getServerName()+fields.get("vpc_ReturnURL"));
			String secureHash = hashAllFields(fields, DEFAULT_SECURE_HASH_TYPE);
			fields.put("vpc_SecureHash", secureHash);
			fields.put("vpc_SecureHashType", DEFAULT_SECURE_HASH_TYPE);
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException e1) {
			log.info(e1.getMessage(),e1);
			log.debug(e1.getMessage());
			throw new PaymentException(e1);
		}
		PaymentRequestDto paymentRequestDto = new PaymentRequestDto();
		String merchantTxnRef = Formatter.formatDateTime(new Date(), "yyyyMMdd HH:mm:ss.SSS");
		merchantTxnRef=merchantTxnRef.substring(0,merchantTxnRef.length()-1);
		String merchantTxnDtm = Formatter.formatDateTime(new Date(), "yyyyMMdd HH:mm:ss.SSS");
		String amo = fields.get("vpc_Amount");
		String amoOo= String.valueOf(Integer.parseInt(amo));
		String payMethod = fields.get("vpc_OrderInfo");
		String reqNo = fields.get("vpc_MerchTxnRef");
		if(reqNo.length()>=19){
			reqNo=reqNo.substring(0,18)+reqNo.substring(reqNo.length()-3);
		}
		String returnUrl=this.getPaymentData().getContinueUrl();
		String umId= GatewayConfig.eNetsUmId;
		String keyId=GatewayConfig.eNetsKeyId;
		String secretKey=GatewayConfig.eNetsSecretKey ;
		String b2sUrl=AppConsts.REQUEST_TYPE_HTTPS + bpc.request.getServerName()+"/payment-web/back.jsp?reqNo="+reqNo;
		String sessionId=bpc.getSession().getId();
		ObjectMapper mapper = new ObjectMapper();
		SoapiB2S soapiTxnQueryReq=new SoapiB2S();
		soapiTxnQueryReq.setSs("1");
		SoapiB2S.Msg msg=new SoapiB2S.Msg();
		msg.setNetsMid(umId);
		msg.setTid("");
		msg.setSubmissionMode("B");
		msg.setTxnAmount(amoOo);
		msg.setMerchantTxnRef(merchantTxnRef);
		msg.setMerchantTxnDtm(merchantTxnDtm);
		msg.setPaymentType("SALE");
		msg.setCurrencyCode("SGD");
		msg.setPaymentMode("");
		msg.setMerchantTimeZone("+8:00");
		msg.setNetsMidIndicator("U");
		msg.setB2sTxnEndURL(b2sUrl);
		msg.setB2sTxnEndURLParam("");
		msg.setS2sTxnEndURL("");
		msg.setS2sTxnEndURLParam("");
		msg.setIpAddress("127.0.0.1");
		msg.setLanguage("en");
		msg.setClientType("W");
		msg.setSupMsg("");
		soapiTxnQueryReq.setMsg(msg);
		String txnRep = null;
		try {
			txnRep = mapper.writeValueAsString(soapiTxnQueryReq);
		} catch (JsonProcessingException e) {
			log.debug(e.getMessage(),e);
		}

		String hmac= null;
		try {
			hmac = generateSignature(txnRep,secretKey);
		} catch (Exception e) {
			log.info(e.getMessage(),e);
		}
		ParamUtil.setSessionAttr(bpc.request,"txnReq",txnRep);
		ParamUtil.setSessionAttr(bpc.request,"API_KEY",keyId);
		ParamUtil.setSessionAttr(bpc.request,"newHMAC",hmac);
		ParamUtil.setSessionAttr(bpc.request,"sessionNetsId",fields.get("vpc_ReturnURL").substring(fields.get("vpc_ReturnURL").indexOf('=')+1));
		log.info(StringUtil.changeForLog("==========>sessionNetsId:"+fields.get("vpc_ReturnURL").substring(fields.get("vpc_ReturnURL").indexOf('=')+1)));

		log.info(StringUtil.changeForLog("==========>getSessionID:"+bpc.getSession().getId()));
		paymentRequestDto.setQueryCode(sessionId);
		if(!StringUtil.isEmpty(amo)&&!StringUtil.isEmpty(reqNo)) {
			paymentRequestDto.setReturnUrl(returnUrl);
			double amount = Double.parseDouble(amo)/100;
			paymentRequestDto.setAmount(amount);
			paymentRequestDto.setPayMethod("eNets");
			paymentRequestDto.setReqDt(new Date());
			paymentRequestDto.setReqRefNo(reqNo);
			paymentRequestDto.setMerchantTxnRef(merchantTxnRef);
			paymentRequestDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
			PaymentBaiduriProxyUtil.getPaymentClient().saveHcsaPaymentResquset(paymentRequestDto);

		}
//		try {
//			String backUrl=AppConsts.REQUEST_TYPE_HTTPS + bpc.request.getServerName()+"/payment-web/eservice/INTERNET/Payment";
//			RedirectUtil.redirect("http://192.168.6.195:8090/eNets?txnReq="+ URLEncoder.encode(txnRep + "", StandardCharsets.UTF_8.name())+"&API_KEY="+keyId+"&hmac="+URLEncoder.encode(hmac + "", StandardCharsets.UTF_8.name())+"&backUrl="+URLEncoder.encode(backUrl + "", StandardCharsets.UTF_8.name()), bpc.request, bpc.response);
//		} catch (IOException e) {
//			log.debug(e.getMessage());
//		}
		try {
			StringBuilder bud = new StringBuilder();
			bud.append(bigsURL).append('?');
			appendQueryFields(bud, fields);


			RedirectUtil.redirect(bud.toString(), bpc.request, bpc.response);
		} catch (IOException e) {
			log.info(e.getMessage(),e);
			log.debug(e.getMessage());

			throw new PaymentException(e);
		}

	}

	@Override
	public void callBack(BaseProcessClass bpc) throws PaymentException {
		AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_ONLINE_PAYMENT, AuditTrailConsts.FUNCTION_ONLINE_PAYMENT);

		String continueToken = (String)bpc.request.getSession().getAttribute(IMPL_CONTINUE_TOKEN_PREFIX + getTinyKey());
		if(StringHelper.isEmpty(continueToken)){
			throw new PaymentException("Continue token is null.");
		}
		setContinueToken(continueToken);

		String transNo = this.getPaymentData().getPaymentTrans().getTransNo();
		String refNo = this.getPaymentData().getSvcRefNo();
		if(refNo.length()>=19){
			refNo=refNo.substring(0,18)+refNo.substring(refNo.length()-3);
		}
		double amount = this.getPaymentData().getAmount();
		PaymentRequestDto paymentRequestDto=PaymentBaiduriProxyUtil.getPaymentClient().getPaymentRequestDtoByReqRefNo(refNo).getEntity();

		Map<String, String> fields = getResponseFieldsMap(bpc);

		String gwNo = fields.get("vpc_TransactionNo");
		setGatewayRefNo(gwNo);
		HttpServletRequest request = bpc.request;
		String status = PaymentTransactionEntity.TRANS_STATUS_FAILED;//"Send";
		String txnRes= (String) ParamUtil.getSessionAttr(request,"message");
		String header= (String) ParamUtil.getSessionAttr(request,"header");
		String generatedHmac= null;
		try {
			generatedHmac = generateSignature(txnRes, GatewayConfig.eNetsSecretKey);
		} catch (Exception e) {
			log.debug(e.getMessage(),e);
		}
		log.info(StringUtil.changeForLog("MERCHANT APP : in hmac received :" + header));
		log.info(StringUtil.changeForLog("MERCHANT APP : in hmac generated :" + generatedHmac));

		try {
			JSONObject jsonObject = JSONObject.fromObject(txnRes);
			Soapi txnResObj = (Soapi) JSONObject.toBean(jsonObject, Soapi.class);
			log.info(StringUtil.changeForLog("MERCHANT APP : in receiveb2sTxnEnd :" + txnResObj));
			if ("0".equals(txnResObj.getMsg().getNetsTxnStatus())) {
				status = PaymentTransactionEntity.TRANS_STATUS_SUCCESS;
			}
		} catch (Exception ex) {
			log.info(ex.getMessage(),ex);
		}

		String response = "payment success";
		setPaymentResponse(response);

		//if(txnResObj.getMsg().get)
		String invoiceNo = "1234567";//"Send";

//		if(receiveS2STxnEnd(txnReq,request).getStatusCodeValue()==200){
//			status =PaymentTransactionEntity.TRANS_STATUS_SUCCESS;
//		}else {
//			status = PaymentTransactionEntity.TRANS_STATUS_FAILED;
//		}
		setPaymentTransStatus(status);


		PaymentDto paymentDto = new PaymentDto();
		paymentDto.setAmount(amount);
		paymentDto.setReqRefNo(refNo);
		paymentDto.setTxnRefNo(transNo);
		paymentDto.setInvoiceNo(invoiceNo);

		paymentDto.setPmtStatus(status);
		paymentDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
		PaymentBaiduriProxyUtil.getPaymentClient().saveHcsaPayment(paymentDto);

		try {

			String results="?result="+ MaskUtil.maskValue("result",status)+"&reqRefNo="+MaskUtil.maskValue("reqRefNo",refNo)+"&txnDt="+MaskUtil.maskValue("txnDt", DateUtil.formatDate(new Date(), "dd/MM/yyyy"))+"&txnRefNo="+MaskUtil.maskValue("txnRefNo",transNo);
			String bigsUrl =AppConsts.REQUEST_TYPE_HTTPS + request.getServerName()+paymentRequestDto.getReturnUrl()+results;


			RedirectUtil.redirect(bigsUrl, bpc.request, bpc.response);
		} catch (UnsupportedEncodingException e) {
			log.info(e.getMessage(),e);
			log.debug(e.getMessage());

			throw new PaymentException(e);
		} catch (IOException e) {
			log.info(e.getMessage(),e);
			log.debug(e.getMessage());

			throw new PaymentException(e);
		}
	}


	@Override
	public void cancel(BaseProcessClass bpc) throws PaymentException {
		Map<String, String> fields = null;
		try {
			fields = getFieldsMap(bpc);
			String secureHash = hashAllFields(fields, DEFAULT_SECURE_HASH_TYPE);
			fields.put("vpc_SecureHash", secureHash);
			fields.put("vpc_SecureHashType", DEFAULT_SECURE_HASH_TYPE);
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException e1) {
			log.info(e1.getMessage(),e1);
			log.debug(e1.getMessage());
			throw new PaymentException(e1);
		}
		String reqNo = fields.get("vpc_MerchTxnRef");
		PaymentRequestDto paymentRequestDto=PaymentBaiduriProxyUtil.getPaymentClient().getPaymentRequestDtoByReqRefNo(reqNo).getEntity();
		String results="?result="+MaskUtil.maskValue("result","cancelled")+"&reqRefNo="+MaskUtil.maskValue("reqRefNo",reqNo)+"&txnDt="+MaskUtil.maskValue("txnDt",DateUtil.formatDate(new Date(), "dd/MM/yyyy"))+"&txnRefNo="+MaskUtil.maskValue("txnRefNo","");
		String bigsUrl =AppConsts.REQUEST_TYPE_HTTPS + bpc.request.getServerName()+paymentRequestDto.getSrcSystemConfDto().getReturnUrl()+results;

		try {
			RedirectUtil.redirect(bigsUrl, bpc.request, bpc.response);
		} catch (IOException e) {
			log.info(e.getMessage(),e);
			log.info(e.getMessage(),e);
		}
	}

	@SuppressWarnings("rawtypes")
	private Map<String, String> getResponseFieldsMap(BaseProcessClass bpc) {
		Map<String, String> fields = new HashMap<String, String>();
		for (Enumeration e = bpc.request.getParameterNames(); e.hasMoreElements();) {
			String fieldName = (String) e.nextElement();
			String fieldValue = bpc.request.getParameter(fieldName);
			if ((fieldValue != null) && (fieldValue.length() > 0)) {
				fields.put(fieldName, fieldValue);
			}
		}

		Iterator<String> iterator = fields.keySet().iterator();
		while(iterator.hasNext()){
			String key = iterator.next();
			if(!key.startsWith("vpc_") && !key.startsWith("user_")){
				iterator.remove();
				fields.remove(key);
			}
		}
		return fields;
	}

	private Map<String, String> getFieldsMap(BaseProcessClass bpc) throws UnsupportedEncodingException {
		PaymentData pd = EGPCaseHelper.getPaymentCaseData(bpc.currentCase);

		String prefix = "rvl";

		String version = ConfigUtil.getString(prefix + ".baiduri.version");
		String command = ConfigUtil.getString(prefix + ".baiduri.command");
		String accessCode = ConfigUtil.getString(prefix + ".baiduri.accessCode");
		String merchTxnRef = pd.getSvcRefNo();
		String merchantId = ConfigUtil.getString(prefix + ".baiduri.merchant");
		String orderInfo = pd.getPaymentDescription();
		int amount = mul(pd.getAmount(), 100);
		String currency = ConfigUtil.getString(prefix + ".baiduri.currency");//
		String locale = ConfigUtil.getString(prefix + ".baiduri.local");
		String returnURL = ConfigUtil.getString(prefix + ".baiduri.return.url");
		String returnAuthResponseData = ConfigUtil.getString(prefix + ".baiduri.return.authResponseData");
//		String merchTxnRef = "123456";
//		String orderInfo = "";
//		int amount = mul(2.01, 100);

		String sessionId = bpc.request.getSession().getId();

		returnURL = returnURL+"?sessionId="+ SMCStringHelperUtil.newString(Base64.encodeBase64(SMCStringHelperUtil.getStringBytes(URLEncoder.encode(bpc.request.getSession().getId()+"_"+getTinyKey(), StandardCharsets.UTF_8.name()))));

		Map<String, String> fields = new HashMap<String, String>();
		fields.put("user_SessionId", sessionId);
		fields.put("vpc_Version", version);
		fields.put("vpc_Command", command);
		fields.put("vpc_AeccessCode", accessCode);
		fields.put("vpc_MerchTxnRef", merchTxnRef);
		fields.put("vpc_Merchant", merchantId);
		fields.put("vpc_OrderInfo", orderInfo);
		fields.put("vpc_Amount", String.valueOf(amount));
		fields.put("vpc_Currency", currency);
		fields.put("vpc_Locale", locale);
		fields.put("vpc_ReturnURL", returnURL);
		fields.put("vpc_ReturnAuthResponseData", returnAuthResponseData);

		return fields;
	}

	private String hashAllFields(Map<String, String> fields, String secureHashType) throws NoSuchAlgorithmException {
		// create a list and sort it
		List<String> fieldNames = new ArrayList<String>(fields.keySet());
		Collections.sort(fieldNames);

		// create a buffer for the md5 input and add the secure secret first
		StringBuilder buf = new StringBuilder();
		buf.append(SECURE_SECRET);

		// iterate through the list and add the remaining field values
		Iterator<String> itr = fieldNames.iterator();

		while (itr.hasNext()) {
			String fieldName = itr.next();
			String fieldValue = fields.get(fieldName);
			if ((fieldValue != null) && (fieldValue.length() > 0)) {
				buf.append(fieldValue);
			}
		}

		if(StringHelper.isEmpty(secureHashType)){
			secureHashType = DEFAULT_SECURE_HASH_TYPE;
		}

		// create the SHA-256(default) hash and UTF-8 encode it
		MessageDigest md = MessageDigest.getInstance(DEFAULT_SECURE_HASH_TYPE.equals(secureHashType) ? "SHA-256" :  secureHashType);
		byte[] ba = md.digest(SMCStringHelperUtil.getStringBytes(buf.toString()));


		return hex(ba);
	}

	private static String hex(byte[] input) {
		// create a StringBuffer 2x the size of the hash array
		StringBuilder sb = new StringBuilder(input.length * 2);

		// retrieve the byte array data, convert it to hex
		// and add it to the StringBuilder
		for (int i = 0; i < input.length; i++) {
			sb.append(HEX_TABLE[(input[i] >> 4) & 0xf]);
			sb.append(HEX_TABLE[input[i] & 0xf]);
		}
		return sb.toString();
	}

	/**
	 * This method is for creating a URL query string.
	 *
	 * @param bud
	 *            is the inital URL for appending the encoded fields to
	 * @param fields
	 *            is the input parameters from the order page
	 * @throws UnsupportedEncodingException
	 */
	// Method for creating a URL query string
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

	public static int mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).setScale(2, BigDecimal.ROUND_HALF_UP).intValueExact();
	}

	public static void main(String[] args) throws Exception {
		String logInf = String.valueOf(mul(4.10, 100));
		log.info(logInf);
		log.debug(logInf);
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] ba = md.digest(SMCStringHelperUtil.getStringBytes("123"));
		log.info(hex(ba));
		log.debug(hex(ba));
	}

	@Override
	public String oob(BaseProcessClass bpc) throws PaymentException {
		setPaymentTransStatus(PaymentTransactionEntity.TRANS_STATUS_SUCCESS);
		return "";
	}


	public static String generateSignature(String txnReq,String secretKey) throws Exception{
		String concatPayloadAndSecretKey = txnReq + secretKey;
		String concatPayloadAndSecretKey1 = new String(concatPayloadAndSecretKey.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
		String hmac = encodeBase64(hashSHA256ToBytes(concatPayloadAndSecretKey1.getBytes(StandardCharsets.UTF_8)));
		System.out.println("hmac" + hmac);
		return hmac;

	}


	public static byte[] hashSHA256ToBytes(byte[] input) throws Exception
	{
		byte[] byteData = null;

		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(input);

		byteData = md.digest();

		return byteData;
	}

	public static String encodeBase64(byte[] data)
	{
		return DatatypeConverter.printBase64Binary(data);
	}
}
