package com.ecquaria.egp.core.payment.runtime;

import com.dbs.sgqr.generator.QRGenerator;
import com.dbs.sgqr.generator.QRGeneratorImpl;
import com.dbs.sgqr.generator.io.PayNow;
import com.dbs.sgqr.generator.io.QRDimensions;
import com.dbs.sgqr.generator.io.QRGeneratorResponse;
import com.dbs.sgqr.generator.io.QRType;
import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.entity.sopprojectuserassignment.PaymentBaiduriProxyUtil;
import com.ecquaria.cloud.entity.sopprojectuserassignment.SMCStringHelperUtil;
import com.ecquaria.cloud.helper.ConfigHelper;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.payment.PaymentTransactionEntity;
import com.ecquaria.egp.api.EGPCaseHelper;
import com.ecquaria.egp.core.payment.PaymentData;
import com.ecquaria.egp.core.payment.PaymentTransaction;
import com.ecquaria.egp.core.payment.api.config.GatewayPayNowConfig;
import ecq.commons.helper.StringHelper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.util.ResourceUtils;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Slf4j
public class PaymentPayNowProxy extends PaymentProxy {

	public static final String DEFAULT_ENCODING = "UTF-8";
	public static final String IMPL_CONTINUE_TOKEN_PREFIX = "IMPL_CONTINUE_TOKEN_";

	public static final String DEFAULT_SECURE_HASH_TYPE = "SHA256";

	static final String SECURE_SECRET = "6A92740F77EFF1C21DFF9281EC53C519";

	private static final char[] HEX_TABLE = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	public static final Locale LOCALE = new Locale("en", "SG");

	@SneakyThrows
	@Override
	public void pay(BaseProcessClass bpc) throws PaymentException {
		//AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_PAYMENT, "");
		AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_ONLINE_PAYMENT, AuditTrailConsts.FUNCTION_ONLINE_PAYMENT);
		String continueToken = getContinueToken();
		bpc.request.getSession().setAttribute(IMPL_CONTINUE_TOKEN_PREFIX + getTinyKey(), continueToken);

		String bigsURL = AppConsts.REQUEST_TYPE_HTTPS + bpc.request.getServerName()+ ConfigHelper.getString("payNow.url");
		if (StringHelper.isEmpty(bigsURL)) {
			throw new PaymentException("payNow.url is not set.");
		}
		log.info(StringUtil.changeForLog("==========>getSessionID:"+bpc.getSession().getId()));

		Map<String, String> fields = null;
		try {
			fields = getFieldsMap(bpc);
			fields.put("vpc_ReturnURL",fields.get("vpc_ReturnURL"));
			String secureHash = hashAllFields(fields, DEFAULT_SECURE_HASH_TYPE);
			fields.put("vpc_SecureHash", secureHash);
			fields.put("vpc_SecureHashType", DEFAULT_SECURE_HASH_TYPE);
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException e1) {
			log.info(e1.getMessage(),e1);
			log.debug(e1.getMessage());
			throw new PaymentException(e1);
		}

		PaymentRequestDto paymentRequestDto = new PaymentRequestDto();

		String amo = fields.get("vpc_Amount");
		String payMethod = fields.get("vpc_OrderInfo");
		String reqNo = fields.get("vpc_MerchTxnRef");
		String returnUrl=this.getPaymentData().getContinueUrl();
		String results="?result="+ MaskUtil.maskValue("result",PaymentTransactionEntity.TRANS_STATUS_FAILED)+"&reqRefNo="+MaskUtil.maskValue("reqRefNo",reqNo)+"&txnDt="+MaskUtil.maskValue("txnDt", DateUtil.formatDate(new Date(), "dd/MM/yyyy"))+"&txnRefNo="+MaskUtil.maskValue("txnRefNo","TRN-000");
		String failUrl =AppConsts.REQUEST_TYPE_HTTPS + bpc.request.getServerName()+returnUrl+results;
		ParamUtil.setSessionAttr(bpc.request,"failUrl",failUrl);
		ParamUtil.setSessionAttr(bpc.request,"payNowCallBackUrl",fields.get("vpc_ReturnURL"));
		String appGrpNo=reqNo;
		try {
			appGrpNo=reqNo.substring(0,reqNo.indexOf('_'));
		}catch (Exception e){
			log.error(StringUtil.changeForLog("appGrpNo not found :==== >>>"+reqNo));
		}

		QRGenerator qrGenerator = new QRGeneratorImpl();

		File inputFile = ResourceUtils.getFile("classpath:image/paymentPayNow.png");
		//sample
		QRDimensions qrDetails = qrGenerator.getQRDimensions(200, 200, java.awt.Color.decode("#7C1A78"), inputFile.getPath());
		// sample Static QR
		//PayNow payNowObject = qrGenerator.getPayNowObject("0000", "702", "SG", "McDonalds SG", "Singapore", "SG.PAYNOW", "2", "12345678U12A", "1", "20181225");

		//sample Dynamic QR
		int expiryMinutes = ConfigHelper.getInt("paynow.qr.expiry.minutes");
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss", LOCALE);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, expiryMinutes);
		String expiryDate = df.format(cal.getTime());

		double amount = Double.parseDouble(amo)/100;
		String amoStr=Double.toString(amount);
		//PayNow payNowObject = qrGenerator.getPayNowObject("0000", "702", "SG", "McDonalds SG", "Singapore", "SG.PAYNOW", "2", "12345678U12A", "1", expiryDate,"12", amoStr,"1234567890123456789012345");

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
		System.out.println(sgqrTypeFormattedPayLoad);
		String imageStreamInBase64Format = qrCodeResponse.getImageStream();
		System.out.println(imageStreamInBase64Format);
		ParamUtil.setSessionAttr(bpc.request, "imageStreamInBase64Format",imageStreamInBase64Format);

		ParamUtil.setSessionAttr(bpc.request, "payNowReqNo",reqNo);
		ParamUtil.setSessionAttr(bpc.request, "payNowAmo",amoStr);


		if(!StringUtil.isEmpty(amo)&&!StringUtil.isEmpty(reqNo)) {
			paymentRequestDto.setReturnUrl(returnUrl);
			paymentRequestDto.setAmount(amount);
			paymentRequestDto.setPayMethod(ApplicationConsts.PAYMENT_METHOD_NAME_PAYNOW);
			paymentRequestDto.setReqDt(new Date());
			paymentRequestDto.setReqRefNo(reqNo);
			paymentRequestDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
			paymentRequestDto.setSystemClientId(AppConsts.MOH_IAIS_SYSTEM_PAYMENT_CLIENT_KEY);
			PaymentBaiduriProxyUtil.getPaymentClient().saveHcsaPaymentResquset(paymentRequestDto);
		}
		PaymentDto paymentDto=PaymentBaiduriProxyUtil.getPaymentClient().getPaymentDtoByReqRefNo(
				AppConsts.MOH_IAIS_SYSTEM_PAYMENT_CLIENT_KEY, appGrpNo).getEntity();
		if(paymentDto!=null&&paymentDto.getPmtStatus().equals(PaymentTransactionEntity.TRANS_STATUS_SUCCESS)){
			RedirectUtil.redirect(fields.get("vpc_ReturnURL"), bpc.request, bpc.response);
		}
		try {
			StringBuilder bud = new StringBuilder();
			bud.append(bigsURL).append('?');
			appendQueryFields(bud, fields);


			RedirectUtil.redirect(bud.toString(), bpc.request, bpc.response);
			setPaymentTransStatus(PaymentTransaction.TRANS_STATUS_SEND);
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
	public void callBack(BaseProcessClass bpc) throws PaymentException {
		AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_ONLINE_PAYMENT, AuditTrailConsts.FUNCTION_ONLINE_PAYMENT);
		String continueToken = (String)bpc.request.getSession().getAttribute(IMPL_CONTINUE_TOKEN_PREFIX + getTinyKey());
		if(StringHelper.isEmpty(continueToken)){
			throw new PaymentException("Continue token is null.");
		}
		setContinueToken(continueToken);

		String transNo = this.getPaymentData().getPaymentTrans().getTransNo();
		String refNo = this.getPaymentData().getSvcRefNo();
		double amount = this.getPaymentData().getAmount();
		PaymentRequestDto paymentRequestDto=PaymentBaiduriProxyUtil.getPaymentClient()
				.getPaymentRequestDtoByReqRefNo(AppConsts.MOH_IAIS_SYSTEM_PAYMENT_CLIENT_KEY, refNo).getEntity();

		Map<String, String> fields = getResponseFieldsMap(bpc);
		log.info(StringUtil.changeForLog("==========>getSessionID:"+bpc.getSession().getId()));

		String gwNo = fields.get("vpc_TransactionNo");
		setGatewayRefNo(gwNo);
		HttpServletRequest request = bpc.request;

		String appGrpNo=refNo;
		try {
			appGrpNo=refNo.substring(0,refNo.indexOf('_'));
		}catch (Exception e){
			log.error(StringUtil.changeForLog("appGrpNo not found :==== >>>"+refNo));
		}

		String status = PaymentTransactionEntity.TRANS_STATUS_FAILED;//"Send";
		PaymentDto paymentDto=PaymentBaiduriProxyUtil.getPaymentClient().getPaymentDtoByReqRefNo(
				AppConsts.MOH_IAIS_SYSTEM_PAYMENT_CLIENT_KEY, appGrpNo).getEntity();
		if(paymentDto!=null&&paymentDto.getPmtStatus().equals(PaymentTransactionEntity.TRANS_STATUS_SUCCESS)){
			status=PaymentTransactionEntity.TRANS_STATUS_SUCCESS;
			paymentRequestDto.setStatus(status);
		}else {
			paymentRequestDto.setStatus(status);
		}
		String invoiceNo = "1234567";
		String response = "payment "+status;
		setPaymentResponse(response);
		setPaymentTransStatus(status);
		paymentRequestDto.setSystemClientId(AppConsts.MOH_IAIS_SYSTEM_PAYMENT_CLIENT_KEY);
		PaymentBaiduriProxyUtil.getPaymentClient().updatePaymentResquset(paymentRequestDto);


		ApplicationGroupDto applicationGroupDto=PaymentBaiduriProxyUtil.getPaymentAppGrpClient().paymentUpDateByGrpNo(appGrpNo).getEntity();
		if(applicationGroupDto!=null){
			if(status.equals(PaymentTransactionEntity.TRANS_STATUS_SUCCESS)){
				applicationGroupDto.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
				applicationGroupDto.setPmtRefNo(refNo);
				applicationGroupDto.setPaymentDt(new Date());
				applicationGroupDto.setPayMethod(ApplicationConsts.PAYMENT_METHOD_NAME_PAYNOW);
				PaymentBaiduriProxyUtil.getPaymentAppGrpClient().doPaymentUpDate(applicationGroupDto);
			}
			else {
				List<ApplicationGroupDto> groupDtoList= IaisCommonUtils.genNewArrayList();
				applicationGroupDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_SUBMITED);
				groupDtoList.add(applicationGroupDto);
				PaymentBaiduriProxyUtil.getPaymentAppGrpClient().updateFeApplicationGroupStatus(groupDtoList);
			}
		}

		try {
			//setPaymentTransStatus(PaymentTransaction.TRANS_STATUS_SEND);
			log.debug(StringUtil.changeForLog("result="+status+"&reqRefNo="+refNo+"&txnRefNo="+transNo));

			String results="?result="+ MaskUtil.maskValue("result",status)+"&reqRefNo="+MaskUtil.maskValue("reqRefNo",refNo)+"&txnDt="+MaskUtil.maskValue("txnDt", DateUtil.formatDate(new Date(), "dd/MM/yyyy"))+"&txnRefNo="+MaskUtil.maskValue("txnRefNo",transNo);
			String bigsUrl =AppConsts.REQUEST_TYPE_HTTPS + request.getServerName()+paymentRequestDto.getReturnUrl()+results;
			Boolean payNowSucc= (Boolean) ParamUtil.getSessionAttr(request,appGrpNo+"payNowResult");
			if(status.equals(PaymentTransactionEntity.TRANS_STATUS_SUCCESS)){
				ParamUtil.setSessionAttr(request,appGrpNo+"payNowResult",Boolean.TRUE);
			}
			if(payNowSucc==null|| !payNowSucc.equals(Boolean.TRUE)){
				RedirectUtil.redirect(bigsUrl, bpc.request, bpc.response);
			}
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
		PaymentBaiduriProxyUtil.getPaymentClient()
				.getPaymentRequestDtoByReqRefNo(AppConsts.MOH_IAIS_SYSTEM_PAYMENT_CLIENT_KEY, reqNo).getEntity();
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

		String version = ConfigHelper.getString(prefix + ".baiduri.version");
		String command = ConfigHelper.getString(prefix + ".baiduri.command");
		String accessCode = ConfigHelper.getString(prefix + ".baiduri.accessCode");
		String merchTxnRef = pd.getSvcRefNo();
		String merchantId = ConfigHelper.getString(prefix + ".baiduri.merchant");
		String orderInfo = pd.getPaymentDescription();
		int amount = mul(pd.getAmount(), 100);
		String currency = ConfigHelper.getString(prefix + ".baiduri.currency");//
		String locale = ConfigHelper.getString(prefix + ".baiduri.local");
		String returnURL = ConfigHelper.getString(prefix + ".baiduri.return.url");
		String returnAuthResponseData = ConfigHelper.getString(prefix + ".baiduri.return.authResponseData");
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
		return b1.multiply(b2).setScale(2, RoundingMode.HALF_UP).intValueExact();
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



}
