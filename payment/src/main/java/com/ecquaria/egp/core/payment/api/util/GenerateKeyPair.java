package com.ecquaria.egp.core.payment.api.util;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

public class GenerateKeyPair {

	private String priKey;
	private String pubKey;

	public void run() {
		try {
			java.security.KeyPairGenerator keygen = java.security.KeyPairGenerator
					.getInstance("RSA");
			SecureRandom secrand = new SecureRandom();
			secrand.generateSeed(128);//setSeed("dasdas2dsds".getBytes()); // 初始化随机产生器
			keygen.initialize(1024, secrand);
			KeyPair keys = keygen.genKeyPair();

			PublicKey pubkey = keys.getPublic();
			PrivateKey prikey = keys.getPrivate();

			pubKey = bytesToHexStr(pubkey.getEncoded());
			priKey = bytesToHexStr(prikey.getEncoded());

			//System.out.println("pubKey=" + pubKey);
			//System.out.println("priKey=" + priKey);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("generate fail");
		}
		;

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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GenerateKeyPair n = new GenerateKeyPair();
		n.run();
	}

}