package com.ecquaria.egp.core.payment.api.config;

public class GatewayConstants {

	public static final String SIGN_KEY = "sign";
	public static final String SIGN_TYPE_KEY = "sign_type";
	public static final String SIGN_TYPE_MD5 = "MD5";
	public static final String SIGN_TYPE_RSA = "RSA";
	public static final String RETURN_URL_KEY = "return_url";
	public static final String REGISTRY_NAME_KEY = "payment_registry_name";
	public static final String NOTIFY_URL_KEY = "notify_url";
	public static final String INPUT_CHARSET = "input_charset";
	
	public static final String AMOUNT_KEY = "amount";
	public static final String PYMT_DESCRIPTION_KEY = "description";
	public static final String SVCREF_NO = "svc_refno";
	public static final String PYMT_STATUS = "pymt_status";
	public static final String NOTIFY_STATUS = "notify_status";
	public static final String GATEWAY_REFNO = "gateway_ref_no";
	public static final String CPS_REFNO = "cps_ref_no";
	
	public static final String ACTION_KEY = "action";
	public static final String ACTION_CHECK = "check_payment";
	public static final String ACTION_UPDATE = "update_payment";
}
