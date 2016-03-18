package com.nju.util;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;




 

@SuppressWarnings("restriction")
public class CryptUtil {
	
	private static final String TAG = CryptUtil.class.getSimpleName();
	private static final String UTF8 = "UTF-8";
	private static final String AES = "AES";
	private static final String AES_ECB_NO_PADDING = "AES/ECB/NoPadding";
	private static String AES_authorization_token_KEY = "lkj5Zlkh54lu08fgslAS4lkGWAF3lkky";
	private static String AES_data_encryption_KEY = "lsahflh3l08x3h3l09clkhl5X23980xy";
	/**
	 * 
	 * @return secret key byte array
	 * @throws Exception
	 */
	private static byte[] getSecretKeyForToken() {
		try {
			String tokenKey =  AES_authorization_token_KEY;
			if(null == tokenKey){
				Log.e(TAG,"_message=\"Unable to find token key\"");
				try {
					throw new Exception("Unable to find token key");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return tokenKey.getBytes(UTF8);
		} catch (UnsupportedEncodingException e) {
			try {
				throw new Exception("Error to get key", e);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * @return secret key byte array
	 * @throws Exception
	 */
	private static byte[] getSecretKeyForData() {
		try{
			String dataKey =  AES_data_encryption_KEY;
			if(null == dataKey){
				Log.e(TAG,"_message=\"Unable to find data key\"");
				try {
					throw new Exception("Unable to find data key");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return dataKey.getBytes(UTF8);
		}catch (UnsupportedEncodingException e) {
			try {
				throw new Exception("Error to get key", e);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * @param data
	 * @return encryptiedData
	 * @throws Exception 
	 */
	public static String getEncryptiedData(String data) {
		return encryption(data, getSecretKeyForData());
	}
	
	/**
	 * Data Field & Full Payload Decryption Using Data Field Secret Key
	 * @param encryptiedData
	 * @return
	 * @throws Exception 
	 */
	public static String dataDecryption(String encryptiedData) {
		return decryption(encryptiedData, getSecretKeyForData());
	}
	
	
	public  static String tokenEncrypt(String token){
		return encryption(token,getSecretKeyForToken());
	}
	
	/**
	 * Token Decryption Using Token Secret Key
	 * @param token
	 * @return data
	 * @throws Exception 
	 */
	public static String tokenDecryption(String token) {
		return decryption(token, getSecretKeyForToken());
	}
	
	
	
	private static String decryption(String encryptedData, byte[] secretKey) {
		String data = null;
		try{
			byte[] encryptedVal = android.util.Base64.decode(encryptedData, android.util.Base64.DEFAULT);
			SecretKeySpec key = new SecretKeySpec(secretKey,AES);
			Cipher cipher = Cipher.getInstance(AES_ECB_NO_PADDING);
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] plainText = new byte[cipher.getOutputSize(encryptedVal.length)];
			int ptLength = cipher.update(encryptedVal, 0, encryptedVal.length, plainText, 0);
			ptLength += cipher.doFinal(plainText, ptLength);
			data = new String(plainText,UTF8);
			data = data.trim();
		}catch(Exception e){
			Log.e(TAG,"_message=\"Error happened when field decryption\n exception={}\"", e);
			try {
				throw new Exception("Decryption Error", e);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return data;
	}
	
	private static String encryption(String data, byte[] secretKey) {
		String encryptedData = null;
		data = paddingWithNull(data);
		try{
			SecretKeySpec key = new SecretKeySpec(secretKey, AES);
			Cipher cipher = Cipher.getInstance(AES_ECB_NO_PADDING);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] cipherText = new byte[cipher.getOutputSize(data.length())];
			int ctLength = cipher.update(data.getBytes(UTF8), 0, data.length(), cipherText, 0);
			ctLength += cipher.doFinal(cipherText, ctLength);
			encryptedData = Base64.encodeToString(cipherText,Base64.DEFAULT);
		}catch(Exception e){
			Log.e(TAG,"_message=\"Error happened when encrypt field\n exception={}\"", e);
			try {
				throw new Exception("Encryption Error", e);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return encryptedData;
	}
	
	private static String paddingWithNull(String data) {
		int modeNum = data.length() % 32;
		if(modeNum != 0){
			int paddingSize = 32 - modeNum;
	        for(int i = 0; i < paddingSize; i++){
	        	data += (char)0;
	        }
	    }
		return data;
	}

	
}
