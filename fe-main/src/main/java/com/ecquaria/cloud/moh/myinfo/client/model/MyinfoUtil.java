/*
 * MyInfo TUO RESTAPI (Staging)
 * ** STAGING ENVIRONMENT ONLY **  REST API for retrieving Person data from MyInfo for Tell-Us-Once.  **Note - this is an initial specification and is subject to changes through the course of the TUO implementation.** 
 *
 * OpenAPI spec version: 1.0
 * Contact: eric_chang@tech.gov.sg
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.ecquaria.cloud.moh.myinfo.client.model;


import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.myinfo.client.auth.MyInfoClient;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Base64;


import ecq.commons.config.Config;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jws.JsonWebSignature;
import org.springframework.context.ApplicationContext;


/**
 * API tests for MyinfoTuoRestfulApi
 */
@Slf4j
public class MyinfoUtil {


    /**
     * Retrieves Person data from MyInfo
     *
     * Retrieves Person data from MyInfo based on UIN/FIN. This API does not require authorisation token, and retrieves only a user&#39;s basic profile (i.e. excluding CPF and IRAS data)  The available returned attributes from this API includes  - name: Name - hanyupinyinname: HanYuPinYin - aliasname: Alias - hanyupinyinaliasname: HanYuPinYinAlias - marriedname: MarriedName - sex: Sex - race: Race - dialect: Dialect - nationality: Nationality - dob: DOB - birthcountry: BirthCountry - vehno: VehNo - regadd: RegAdd - mailadd: MailAdd - billadd: BillAdd - housingtype: HousingType - hdbtype: HDBType - email: Email - homeno: HomeNo - mobileno: MobileNo - marital: Marital - marriagedate: MarriageDate - divorcedate: DivorceDate - householdincome: HouseholdIncome - relationships: Relationships - edulevel: EduLevel - gradyear: GradYear - schoolname: SchoolName - occupation: Occupation - employment: Employment  Note - null values indicate that the field is unavailable
     * @throws Exception 
     */
    public static String getPersonBasic( String authorization,String idNumber,List<String> attributes,String clientId,String singPassEServiceId,String txnNo) throws Exception {
		ApplicationContext context = SpringContextHelper.getContext();
    	if (context == null){
    		return null;
		}

		MyInfoClient myInfoClient = context.getBean(MyInfoClient.class);

    	String  encipheredData =myInfoClient.searchDataByIdNumber(authorization,idNumber,attributes.toArray(new String[attributes.size()]),clientId,singPassEServiceId,txnNo).getBody();
        return decodeEncipheredData(encipheredData);
    }

    private static String decodeEncipheredData(String encipheredData) throws Exception {
    	if(StringUtil.isEmpty(encipheredData)){
    		return encipheredData;
		}
		JsonWebEncryption jwe = new JsonWebEncryption();
		jwe.setCompactSerialization(encipheredData);
		log.info("JWE Algorithm ================> " + jwe.getAlgorithmHeaderValue());
		log.info("JWE Enc ===============> " + jwe.getEncryptionMethodHeaderParameter());
		jwe.setAlgorithmConstraints(
				new AlgorithmConstraints(
						AlgorithmConstraints.ConstraintType.WHITELIST,
						jwe.getAlgorithmHeaderValue())
		);

		jwe.setContentEncryptionAlgorithmConstraints(
				new AlgorithmConstraints(
						AlgorithmConstraints.ConstraintType.WHITELIST,
						jwe.getEncryptionMethodHeaderParameter())
		);
		String  keyStore = Config.get("myinfo.jwe.clientkey");
		jwe.setKey(getPrivateKey(keyStore));

		encipheredData = jwe.getPlaintextString();
		log.info("Get the payload from the JWE =======>" + encipheredData);

		String  jwskeyStore = Config.get("myinfo.jws.pubclientkey");
		PublicKey pubKey = getPublicKey(jwskeyStore);
		// Create a new JsonWebSignature
		JsonWebSignature jws = new JsonWebSignature();
		// Set the compact serialization on the JWS
		jws.setCompactSerialization( encipheredData);
		// Set the verification key
		jws.setKey(pubKey);
		// Check the signature
		boolean signatureVerified = jws.verifySignature();
		//Get the payload, or signed content, from the JWS
		encipheredData = jws.getPayload();
		return  encipheredData;
	}

	public static PrivateKey getPrivateKey(String clientKeystore) {
	    PrivateKey privateKey = null;
		byte[] encodedKey = Base64.decode(clientKeystore);
		try {
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedKey);
			KeyFactory kf = KeyFactory.getInstance("RSA");
	        privateKey = (PrivateKey)kf.generatePrivate(keySpec);
		} catch (Exception e){
			log.info(e.getMessage(),e);
		}
		return privateKey;
	}
	public static PublicKey getPublicKey(String clientKeystore) {
		PublicKey publicKey = null;
		byte[] encodedKey = Base64.decode(clientKeystore);
		try {
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedKey);
			KeyFactory kf = KeyFactory.getInstance("RSA");
			publicKey  = (PublicKey)kf.generatePublic(keySpec);
		} catch (Exception e){
			log.info(e.getMessage(),e);
		}
		return publicKey;
	}

	public static String getBaseString( String idNum, List<String> attrs, String clientId, String singpassEserviceId, String txnNo){
		StringBuilder sb = new StringBuilder();
			String ipAddress = Config.get("myinfo.ip.address.basestring.gateway");
			sb.append("GET&" + ipAddress);
			sb.append("/" + idNum + "/");
		   sb.append("?attributes=");
		   if (attrs.size() > 0) {
			for (int i = 0; i < attrs.size(); i++) {
				if (i == (attrs.size() - 1)) {
					sb.append(attrs.get(i));
				} else {
					sb.append(attrs.get(i) + ",");
				}
			}
		   }
		    sb.append("&client_id=" + clientId);
		   sb.append("&singpassEserviceId=" + singpassEserviceId);
			sb.append("&txnNo=" + txnNo);
		return sb.toString();
	}

	public static String getAuthorization(String realm, String signature, String appId, long nonce, long timestamp) {
		StringBuilder sb = new StringBuilder();
			sb.append("Apex_l2_Eg realm=\"http://" + realm +"\"");
			sb.append(",apex_l2_eg_app_id=\"" + appId +"\"");
			sb.append(",apex_l2_eg_nonce=\"" + nonce + "\"");
			sb.append(",apex_l2_eg_signature_method=\"SHA256withRSA\"");
			sb.append(",apex_l2_eg_signature=\"" + signature + "\"");
			sb.append(",apex_l2_eg_timestamp=\"" + timestamp + "\"");
			sb.append(",apex_l2_eg_version=\"1.0\"");
		return sb.toString();
	}

}
