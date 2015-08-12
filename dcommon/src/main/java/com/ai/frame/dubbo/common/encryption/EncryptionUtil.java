package com.ai.frame.dubbo.common.encryption;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.ai.frame.dubbo.common.encryption.base64.BASE64Decoder;
import com.ai.frame.dubbo.common.encryption.base64.BASE64Encoder;
import com.ai.frame.dubbo.common.util.StringUtil;

/**DES-MD5加密工具类***/
public class EncryptionUtil {
	private static final String DESKEY = "DES";
	private static final String AESKEY = "AES";
	private static final String MD5KEY = "MD5";
	public static final int AESNUM_128 = 128;
	public static final int AESNUM_192 = 192;
	public static final int AESNUM_256 = 256;
	private static final String FILLMODE = "AES/CBC/PKCS5Padding";
	private static final String DEFSALT  = "5075428636499153";
	private static final String EMPTYSTR = "";
	private static final String HEXSTR  = "0123456789ABCDEF";
	public static final String DEFAULTKEY = "asiainfo";
	public static final String DEFAULTAESKEY = "www.asiainfo.com";
	public static final String DEFAULTTEAKEY = "www.asiainfo.com";
	
	/**
	 * Aes加密解密
	 * @param input 字节源
	 * @param key   加密KEY
	 * @param encrypt_mode 加密或是解密 ENCRYPT_MODE：加密，DECRYPT_MODE：解密
	 * @param fillMode 填充方式,默认AES/CBC/PKCS5Padding
	 * @param salt 密钥偏移量
	 * @throws Exception 
	 */
	private static byte[] aesEDcode(int aesnum,byte[] input, byte[] key,int encrypt_mode,String fillMode,String salt) throws Exception{
		if(StringUtil.isEmpty(fillMode)){
			fillMode = FILLMODE;
		}
		if(StringUtil.isEmpty(salt)){
			salt = DEFSALT;
		}
		Cipher cipher = Cipher.getInstance(fillMode);    
		int plaintextLength = input.length;
		byte[] plaintext = new byte[plaintextLength];
		System.arraycopy(input, 0, plaintext, 0, input.length);

	    SecretKeySpec keyspec = new SecretKeySpec(key, AESKEY);
		IvParameterSpec ivspec = new IvParameterSpec(salt.getBytes());
		
		cipher.init(encrypt_mode, keyspec, ivspec);
		
		byte[] encodeRs = cipher.doFinal(plaintext);
		return encodeRs;
	}
	
	/**
	 * Aes加密解密
	 * @param input 字节源
	 * @param key   加密KEY
	 * @param encrypt_mode 加密或是解密 ENCRYPT_MODE：加密，DECRYPT_MODE：解密
	 * @throws Exception 
	 */
	private static byte[] aesEDcode(byte[] input, byte[] key,int encrypt_mode) throws Exception{
		SecretKey aeskey = new SecretKeySpec(key,AESKEY);
		Cipher cipher    = Cipher.getInstance(AESKEY);
		cipher.init(encrypt_mode, aeskey);
		byte[] encodeRs = cipher.doFinal(input);
		
		return encodeRs;
	}
	/**
	 * AES加密
	 * @param input 待加密字符源
	 * @param key   加密key
	 * @param salt 密钥偏移量
	 * @return 
	 * @throws Exception
	 */
	public static byte[] aesEncode(byte[] input, byte[] key) throws Exception{
		if(key == null){
			return null;
		}
		if(key.length != 16){
			return null;
		}
		return aesEDcode(input,key,Cipher.ENCRYPT_MODE);
	}
	/**
	 * AES加密
	 * @param input 待加密字符源
	 * @param key   加密key
	 * @return 
	 * @throws Exception
	 */
	public static byte[] aesEncode(byte[] input, byte[] key,String salt) throws Exception{
		if(key == null){
			return null;
		}
		if(key.length != 16){
			return null;
		}
		return aesEDcode(AESNUM_128,input,key,Cipher.ENCRYPT_MODE,null,salt);
	}
	/**
	 * AES加密
	 * @param str 待加密字符源
	 * @param key 加密key
	 * @return
	 */
	public static String aesEncode(String str,String key,String salt){
		if(StringUtil.isEmpty(str))return EMPTYSTR;
		if(StringUtil.isEmpty(key)){
			key = DEFAULTAESKEY;
		}
		if(key.length() != 16){
			return "Key长度不是16位";
		}
		try{
			byte[] aesRtn = aesEncode(str.getBytes(),key.getBytes(),salt);
			
			return encodeStr2Hex(aesRtn);
		}catch(Exception e){
			return EMPTYSTR;
		}
	}
	/**
	 * AES加密
	 * @param str 待加密字符源
	 * @param key 加密key
	 * @return
	 */
	public static String aesEncode(String str,String key){
		if(StringUtil.isEmpty(str))return EMPTYSTR;
		if(StringUtil.isEmpty(key)){
			key = DEFAULTAESKEY;
		}
		if(key.length() != 16){
			return "Key长度不是16位";
		}
		try{
			byte[] aesRtn = aesEncode(str.getBytes(),key.getBytes());
			
			return encodeStr2Hex(aesRtn);
		}catch(Exception e){
			return EMPTYSTR;
		}
	}
	/**
	 * AES解密
	 * @param input 待解密字符源
	 * @param key   加密key
	 * @return
	 * @throws Exception
	 */
	public static byte[] aesDecode(byte[] input, byte[] key,String salt) throws Exception{
		if(key == null || key.length != 16){
			return null;
		}
		return aesEDcode(AESNUM_128,input,key,Cipher.DECRYPT_MODE,null,salt);
	}
	/**
	 * AES解密
	 * @param input 待解密字符源
	 * @param key   加密key
	 * @return
	 * @throws Exception
	 */
	public static byte[] aesDecode(byte[] input, byte[] key) throws Exception{
		if(key == null || key.length != 16){
			return null;
		}
		return aesEDcode(input,key,Cipher.DECRYPT_MODE);
	}
	/**
	 * AES解密
	 * @param hexstr 待解密16进制字符源,
	 * @param key
	 * @return
	 */
	public static String aesDecode(String hexstr,String key,String salt){
		if(StringUtil.isEmpty(hexstr) || StringUtil.isEmpty(key))return EMPTYSTR;
		if(key.length() != 16){
			return "Key长度不是16位";
		}
		
		byte[] str = hexStrDecode2Bytes(hexstr);
		if(str == null){
			return EMPTYSTR;
		}
		try{
			byte[] aesRtn = aesDecode(str,key.getBytes(),salt);
			
			return encodeStr2Hex(aesRtn);
		}catch(Exception e){
			return EMPTYSTR;
		}
	}
	/**
	 * AES解密
	 * @param hexstr 待解密16进制字符源,
	 * @param key
	 * @return
	 */
	public static String aesDecode(String hexstr,String key){
		if(StringUtil.isEmpty(hexstr) || StringUtil.isEmpty(key))return EMPTYSTR;
		if(key.length() != 16){
			return "Key长度不是16位";
		}
		
		byte[] str = hexStrDecode2Bytes(hexstr);
		if(str == null){
			return EMPTYSTR;
		}
		try{
			byte[] aesRtn = aesDecode(str,key.getBytes());
			
			return encodeStr2Hex(aesRtn);
		}catch(Exception e){
			return EMPTYSTR;
		}
	}
	/**
	 * Des加密解密
	 * @param input 字节源
	 * @param key   加密KEY
	 * @param encrypt_mode 加密或是解密 ENCRYPT_MODE：加密，DECRYPT_MODE：解密
	 * @throws Exception 
	 */
	private static byte[] desEDcode(byte[] input, byte[] key,int encrypt_mode) throws Exception{
		//SecretKey deskey = new SecretKeySpec(key,DESKEY);
		KeySpec ks = new DESKeySpec(key);
		SecretKeyFactory kf = SecretKeyFactory.getInstance(DESKEY);
		SecretKey deskey = kf.generateSecret(ks);
		
		Cipher cipher    = Cipher.getInstance(DESKEY);
		cipher.init(encrypt_mode, deskey);
		byte[] encodeRs = cipher.doFinal(input);
		
		return encodeRs;
	}
	/**
	 * DES加密
	 * @param input 待加密字符源
	 * @param key   加密key
	 * @return 
	 * @throws Exception
	 */
	public static byte[] desEncode(byte[] input, byte[] key) throws Exception{
		return desEDcode(input,key,Cipher.ENCRYPT_MODE);
	}
	/**
	 * DES加密
	 * @param str 待加密字符源
	 * @param key 加密key
	 * @return
	 */
	public static String desEncode(String str,String key){
		if(StringUtil.isEmpty(str))return EMPTYSTR;
		if(StringUtil.isEmpty(key)){
			key = DEFAULTKEY;
		}
		try{
			byte[] desRtn = desEncode(str.getBytes(),key.getBytes());
			
			return encodeStr2Hex(desRtn);
		}catch(Exception e){
			return EMPTYSTR;
		}
	}
	
	/**
	 * DES解密
	 * @param input 待解密字符源
	 * @param key   加密key
	 * @return
	 * @throws Exception
	 */
	public static byte[] desDecode(byte[] input, byte[] key) throws Exception{
		return desEDcode(input,key,Cipher.DECRYPT_MODE);
	}
	/**
	 * DES解密
	 * @param hexstr 待解密16进制字符源,
	 * @param key
	 * @return
	 */
	public static String desDecode(String hexstr,String key){
		if(StringUtil.isEmpty(hexstr) || StringUtil.isEmpty(key))return EMPTYSTR;
		
		byte[] str = hexStrDecode2Bytes(hexstr);
		if(str == null){
			return EMPTYSTR;
		}
		try{
			byte[] desRtn = desDecode(str,key.getBytes());
			
			return encodeStr2Hex(desRtn);
		}catch(Exception e){
			return EMPTYSTR;
		}
	}
	/**
	 * MD5加密
	 * @param input 待解密字符源
	 * @return
	 * @throws Exception
	 */
	public static byte[] md5(byte[] input) throws Exception{
		MessageDigest messageDigest = MessageDigest.getInstance(MD5KEY);
		messageDigest.update(input);
		byte[] updateBytes = messageDigest.digest();
		
		return updateBytes;
	}
	/**
	 * MD5加密
	 * @param str 待加密字符串源
	 * @return
	 */
	public static String md5Str(String str){
		if(StringUtil.isEmpty(str))return EMPTYSTR;
		try{
			byte[] bytes = str.getBytes();
			byte[] updateBytes = md5(bytes);
			
			return encodeStr2Hex(updateBytes);
		}catch(Exception e){return EMPTYSTR;}
	}
	
	/**
	 * 字节数组转成16进制字符串
	 * @param bytes
	 * @return
	 */
	public static String encodeStr2Hex(byte[] bytes){
		if(bytes == null || bytes.length < 1){
            return EMPTYSTR;
        }
		StringBuffer hexstr = new StringBuffer();

		for(int i=0;i<bytes.length;i++){
            hexstr.append(HEXSTR.charAt((bytes[i] & 0xf0) >> 4));
            hexstr.append(HEXSTR.charAt((bytes[i] & 0x0f) >> 0));
        }
		
		String hexStr = hexstr.toString();
        return hexStr;
	}
	/**
	 * 16进制字符串转字节数组
	 * @param hexstr
	 * @return
	 */
	public static byte[] hexStrDecode2Bytes(String hexstr){
		if(StringUtil.isEmpty(hexstr))return null;
		if(hexstr.length() % 2 != 0){
			return null;
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream(hexstr.length()/2);
		// 将每2位16进制整数组装成一个字节
		for(int i=0;i<hexstr.length();i+=2){
            baos.write(HEXSTR.indexOf(hexstr.charAt(i))<<4 |HEXSTR.indexOf(hexstr.charAt(i+1)));
        }
		return baos.toByteArray();
	}
	
	/**
	 * string 编码成base64
	 * @param str
	 * 
	 * */
    public static String encodeStrBase64(String str){
        String base64 = str;
        try {
            byte[] inputBytes     = str.getBytes("UTF-8");
            base64 = encodeStrBase64(inputBytes);
        } catch (Exception e) {
            base64 = str;
        }
        return base64;
    }
    public static String encodeStrBase64(byte[] inputBytes){
        String base64 = null;
        try {
            BASE64Encoder encoder = new BASE64Encoder();
            base64 = encoder.encode(inputBytes);
        } catch (Exception e) {
            base64 = null;
        }
        return base64;
    }
    /**
     * base64 string 反编码
     * @param base64str
     * */
    public static String decryptBase64(String base64str){
        String result = base64str;
        try {
            byte[] raw = decryptBase64Bytes(base64str);
            result = new String(raw, "UTF-8");
        } catch (Exception e) {
            result  = base64str;
        }
        return result;
    }
    public static byte[] decryptBase64Bytes(String base64str){
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] raw = decoder.decodeBuffer(base64str);
            return raw;
        } catch (Exception e) {
        }
        return null;
    }
}
