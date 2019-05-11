package io.mrth.webutil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;

public class Encryptor {

	private static final Logger LOG = LoggerFactory.getLogger(Encryptor.class);

	public static byte[] encrypt(byte[] key, byte[] initVector, byte[] value) {
		try {
			IvParameterSpec iv = new IvParameterSpec(initVector);
			SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
			byte[] encrypted = cipher.doFinal(value);
			return encrypted;
		} catch (GeneralSecurityException e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	public static byte[] decrypt(byte[] key, byte[] initVector, byte[] encrypted) {
		try {
			IvParameterSpec iv = new IvParameterSpec(initVector);
			SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] original = cipher.doFinal(encrypted);
			return original;
		} catch (GeneralSecurityException e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
}
