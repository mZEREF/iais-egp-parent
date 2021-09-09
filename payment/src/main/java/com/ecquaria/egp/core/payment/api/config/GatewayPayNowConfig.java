package com.ecquaria.egp.core.payment.api.config;

import com.ecquaria.cloud.helper.ConfigHelper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GatewayPayNowConfig {
	

	
	public static final String payment_registry_name = ConfigHelper.getString("payNow.payment.registry.name");
	
	//MD5 key
	public static final String key = ConfigHelper.getString("payNow.payment.md5.key");//"4783D627CE96817C5D5DBA6DADECD2F15ECA5D1F97A0FDD8C07CA43B50C143B6";
	
	//RSA private key:"30820277020100300d06092a864886f70d0101010500048202613082025d02010002818100815bfe7b2d93a625303d912f3d9ce2d4c338e66cdfb6798e876e31b90047e92f7a3f9c6b298e8ab732439010b552eb87ea49714f49ebedde5122966647b400eb47197217ff88a1225c21781a8ef16e57d22ae345fa272816d8638f4bf0036c56ad4015fe44f985021d6a30863c91fa3d427abe1456b615df59ba82638d0b865102030100010281807a8621e195e7b5849fce90c747fa6336d6b9fc36bf265cb84d3a684a2c23f09011a0528d85cd0742418f90d2e28be99ed0a0437cb02d911df3876920f365614017a41b11e40caa64be86ad78c0474494818bd0bb33dc7cecd5e74249f4d42564ac41c92845ddd027fcba69df18e46d4ad4185e193769319586e167648a630861024100e0c3aea34187996162cf86f05f9a7f95bc507be9258adc7e727f6d7b240fa8f2fdf55e92d298064c206cc9a1224fb526668be5e942e164fe16a2015b7ed3958d024100935623d6240c77ff7ddad89446493a93a39d2d139578c01ee5f8178cc0a4ba5674be7dac769a83cae90b43c48d4c7afc79bfdeb81532c63711001ce813fe78d5024100a5c76efdb70d9ed1b7a1bdb379b068be36615ea37e2f47a674290cc530b9e43125a35f1808005d54b4edc861f2febb6c1162ca371c3f46d02a3d5fa6ee8a7cd902403f3cecbabfd43e3901eac9a4b29a41b316b9a29c523e76af8c6334b06c0b08138d9e2950b9c34e27ec68b2588d91330464b94ebdd2136df23ab0c2b775c57655024100c689dfe74c9c3fc97047029a77465022276bc81c636b85aa047b5d75d47251c65e628c867f4b83cd15996a55b38a6540caee852e47abc20d7c4f7fa4758a98fb"
	public static final String rsa_private_key = ConfigHelper.getString("rsa.private.key");
	//RSA public key:"30819f300d06092a864886f70d010101050003818d0030818902818100815bfe7b2d93a625303d912f3d9ce2d4c338e66cdfb6798e876e31b90047e92f7a3f9c6b298e8ab732439010b552eb87ea49714f49ebedde5122966647b400eb47197217ff88a1225c21781a8ef16e57d22ae345fa272816d8638f4bf0036c56ad4015fe44f985021d6a30863c91fa3d427abe1456b615df59ba82638d0b86510203010001"
	public static final String rsa_public_key = ConfigHelper.getString("rsa.public.key");
	//RSA Gateway public key:"30819f300d06092a864886f70d010101050003818d0030818902818100ab3dcb5da02a64a65b25ca8ca2af52bf6e7703dccdadb58f3a64bcb67ba15b6fbecbc38e33c47db1e6a7580fef054c746b689aed0d9c0e6d71c71bcaf945a870ad0cc4737ba64c01ddcb049a16a58afdb77e83d3d7eaa996a0d41948da820c8596f02992e7c96f22aeef30b50f6ef79c89837794b678f9f398b4bc0c8585fa510203010001"
	public static final String rsa_gateway_public_key = ConfigHelper.getString("rsa.gateway.private.key");
	
	public static final String seller_email = ConfigHelper.getString("payNow.payment.seller.email");

	//public static String notify_url = "http://192.168.6.80:8088/egp/commpymt/commpymt_notify.jsp";
	public static final String notify_url = ConfigHelper.getString("payNow.payment.notify.url");
	
	//"http://192.168.6.80:8088/egp/commpymt/commpymt_return.jsp";
	public static final String return_url = ConfigHelper.getString("payNow.payment.return.url");

	//"http://192.168.6.80:8088/egp/process/EGOV/CommPayment?";
	public static final String common_gateway_url = ConfigHelper.getString("payNow.payment.gateway.url");
	//public static final String common_gateway_url = "/payment-web/process/EGPCLOUD/CommPayment?";
	
	//"http://192.168.6.80:8088/egp/process/EGOV/PaymentGatewayService?";
	public static final String common_gateway_service_url = ConfigHelper.getString("payNow.payment.gateway.service.url");

	public static final String merchantCategoryCode = ConfigHelper.getString("payNow.qr.merchant.category.code");

	public static final String txnCurrency = ConfigHelper.getString("payNow.qr.txn.currency");

	public static final String countryCode = ConfigHelper.getString("payNow.qr.country.code");

	public static final String merchantName = ConfigHelper.getString("payNow.qr.merchant.name");

	public static final String merchantCity = ConfigHelper.getString("payNow.qr.merchant.city");
	public static final String globalUniqueID = ConfigHelper.getString("payNow.qr.global.uniqueID");

	public static final String proxyType = ConfigHelper.getString("payNow.qr.proxy.type");
	public static final String proxyValue = ConfigHelper.getString("payNow.qr.proxy.value");
	public static final String editableAmountInd = ConfigHelper.getString("payNow.qr.editable.amountInd");

	public static final String pointOfIntiation = ConfigHelper.getString("payNow.qr.pointOf.intiation");
	public static final String payloadFormatInd = ConfigHelper.getString("payNow.qr.payload.format.ind");


	public static final String timeout = ConfigHelper.getString("payNow.payment.refresh.ms");
	public static final String checkoutTime = ConfigHelper.getString("payNow.payment.poll.time.ms");


	public static final String mockserverSwitch = ConfigHelper.getString("paynow.mockserver.switch");
	public static final String mockserverUrl = ConfigHelper.getString("paynow.mockserver.url");
	public static final String mockserverCallbackUrl = ConfigHelper.getString("paynow.mockserver.callbackUrl");
	//"D:\\alipay_log_" + System.currentTimeMillis() + ".txt";
	public static final String log_path = ConfigHelper.getString("payNow.payment.log.path");

	public static final String input_charset = ConfigHelper.getString("payNow.payment.input.charset");//UTF_8

	public static final String sign_type = ConfigHelper.getString("payNow.payment.sign.type");//RSA
	
	public static final String dtLong                  = "yyyyMMddHHmmss";
    
    public static final String simple                  = "yyyy-MM-dd HH:mm:ss";
    
    public static final String dtShort                 = "yyyyMMdd";
	
}
