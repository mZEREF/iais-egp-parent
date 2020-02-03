package com.ecquaria.egp.core.payment.api.util;

import com.ecquaria.egp.core.payment.api.config.GatewayConfig;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class GatewayRSAEncrypt {

	public static String sign(String myinfo) {
		try {
			String prikeyvalue = GatewayConfig.rsa_private_key;
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
					hexStrToBytes(prikeyvalue));
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			PrivateKey myprikey = keyf.generatePrivate(priPKCS8);

			java.security.Signature signet = java.security.Signature
					.getInstance("SHA256withRSA");
			signet.initSign(myprikey);
			signet.update(myinfo.getBytes("UTF-8"));
			byte[] signed = signet.sign();

			return bytesToHexStr(signed);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * Transform the specified byte into a Hex String form.
	 */
	public static final String bytesToHexStr(byte[] bcd) {
		StringBuffer s = new StringBuffer(bcd.length * 2);

		for (int i = 0; i < bcd.length; i++) {
			s.append(bcdLookup[(bcd[i] >>> 4) & 0x0f]);
			s.append(bcdLookup[bcd[i] & 0x0f]);
		}

		return s.toString();
	}

	/**
	 * Transform the specified Hex String into a byte array.
	 */
	public static final byte[] hexStrToBytes(String s) {
		byte[] bytes;

		bytes = new byte[s.length() / 2];

		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2),
					16);
		}

		return bytes;
	}

	private static final char[] bcdLookup = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	
	public static boolean verifySign(String info, String sign_value) {
		try {
			String pubkeyvalue = GatewayConfig.rsa_gateway_public_key;
			X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(
					hexStrToBytes(pubkeyvalue));
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey pubKey = keyFactory.generatePublic(bobPubKeySpec);

			byte[] signed = hexStrToBytes(sign_value);
			java.security.Signature signetcheck = java.security.Signature
					.getInstance("SHA256withRSA");
			signetcheck.initVerify(pubKey);
			signetcheck.update(info.getBytes());
			if (signetcheck.verify(signed)) {
				return true;
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
