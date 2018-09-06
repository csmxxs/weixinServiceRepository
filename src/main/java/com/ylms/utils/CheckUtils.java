package com.ylms.utils;

import java.security.MessageDigest;
import java.util.Arrays;

/**
 * @ClassName: CheckUtils
 * @Description: TODO
 * @Author: 49524
 * @Date: 2018/8/20 18:24
 * @Version 1.0
 */

public class CheckUtils {

	// 开发者自行定义Tooken
	public static final String token = "21cn_ylms_nb_2018";

	/**
	 * @Author: Xxs
	 * @Description: token校验
	 * @Date: 18:37 2018/8/20
	 * @Param: [signature, timestamp, nonce]
	 * @return: boolean
	 **/
	public static boolean checkSignature(String signature, String timestamp, String nonce) {
		// 1.定义数组存放tooken，timestamp,nonce
		String[] arr = { token, timestamp, nonce };
		// 2.对数组进行排序
		Arrays.sort(arr);
		// 3.生成字符串
		StringBuffer sb = new StringBuffer();
		for (String s : arr) {
			sb.append(s);
		}
		// 4.sha1加密,网上均有现成代码
		String temp = getSha1(sb.toString());
		// 5.将加密后的字符串，与微信传来的加密签名比较，返回结果
		return temp.equals(signature);
	}

	public static String getSha1(String str) {
		if (str == null || str.equals("")) {
			return null;
		}
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
			mdTemp.update(str.getBytes("UTF-8"));
			byte[] md = mdTemp.digest();
			int j = md.length;
			char buf[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
				buf[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(buf);
		} catch (Exception e) {
			return null;
		}
	}
}