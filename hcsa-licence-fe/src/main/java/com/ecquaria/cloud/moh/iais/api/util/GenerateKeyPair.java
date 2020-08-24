package com.ecquaria.cloud.moh.iais.api.util;

import java.security.SecureRandom;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GenerateKeyPair {

	public void run() {
		try {
			java.security.KeyPairGenerator keygen = java.security.KeyPairGenerator
					.getInstance("RSA");
			SecureRandom secrand = new SecureRandom();
			secrand.generateSeed(128);//setSeed("dasdas2dsds".getBytes());
			keygen.initialize(1024, secrand);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		;

	}

	/**
	 * Transform the specified byte into a Hex String form.
	 */
	public static final String bytesToHexStr(byte[] bcd) {
		StringBuilder s = new StringBuilder(bcd.length * 2);

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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		com.ecquaria.egp.core.payment.api.util.GenerateKeyPair n = new com.ecquaria.egp.core.payment.api.util.GenerateKeyPair();
		n.run();
	}

}