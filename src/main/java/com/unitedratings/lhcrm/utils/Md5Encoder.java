package com.unitedratings.lhcrm.utils;

import org.apache.log4j.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author wangyongxin
 */
public final class Md5Encoder {
	private static final Logger LOG = Logger.getLogger(Md5Encoder.class);
    private Md5Encoder() {
    }

    private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
            'B', 'C', 'D', 'E', 'F' };

    private static char[] encodeHex(byte[] data) {

        int l = data.length;

        char[] out = new char[l << 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS[0x0F & data[i]];
        }

        return out;
    }

    public static String encodePassword(String rawPass) {

        MessageDigest messageDigest = getMessageDigest();

        byte[] digest = messageDigest.digest(rawPass.getBytes());

        return new String(encodeHex(digest));
    }

    protected final static MessageDigest getMessageDigest()  {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
        	LOG.error(e.getMessage(),e);
            throw new IllegalArgumentException("No such algorithm [" + "MD5" + "]");
        }
    }

    public static boolean isPasswordValid(String encPass, String rawPass) {
        String pass1 = "" + encPass;
        String pass2 = encodePassword(rawPass);
        return pass1.equals(pass2);
    }

	public static String getSign(String s) {
	    char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	    try {
	        byte[] buffer = s.getBytes("UTF-8");
	        //获得MD5摘要算法的 MessageDigest 对象
	        MessageDigest mdTemp = MessageDigest.getInstance("MD5");

	        //使用指定的字节更新摘要
	        mdTemp.update(buffer);

	        //获得密文
	        byte[] md = mdTemp.digest();

	        //把密文转换成十六进制的字符串形式
	        int j = md.length;
	        char[] str = new char[j * 2];
	        int k = 0;
	        for (int i = 0; i < j; i++) {
	            byte byte0 = md[i];
	            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
	            str[k++] = hexDigits[byte0 & 0xf];
	        }

	        return new String(str);
	    } catch (Exception e) {
	        return null;
	    }
	}
}
