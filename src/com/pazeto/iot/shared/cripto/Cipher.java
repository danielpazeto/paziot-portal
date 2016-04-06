package com.pazeto.iot.shared.cripto;

import java.io.UnsupportedEncodingException;

/**
 * Cifrador
 * 
 */
public class Cipher {


	public void setPublicKey(int hash) {
		publicKey = new byte[] { (byte) hash };
	}

	private byte[] publicKey;

	public byte[] cripto(String msg) {

		try {
			byte[] chars = msg.getBytes("US-ASCII");
			byte[] criptedChars = new byte[chars.length];
			for (int i = 0; i < chars.length; i++) {
				criptedChars[i] = chars[i];
				for (int j = 0; j < publicKey.length; j++) {
					criptedChars[i] = (byte) (publicKey[j] ^ criptedChars[i]);
				}
			}
			return criptedChars;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;

	}

	public byte[] descripto(byte[] msg) {

		byte[] descriptedChars = new byte[msg.length];

		for (int i = 0; i < msg.length; i++) {
			descriptedChars[i] = msg[i];
			for (int j = publicKey.length - 1; j >= 0; j--) {
				descriptedChars[i] = (byte) (publicKey[j] ^ descriptedChars[i]);
			}

		}

		return descriptedChars;
	}

}
