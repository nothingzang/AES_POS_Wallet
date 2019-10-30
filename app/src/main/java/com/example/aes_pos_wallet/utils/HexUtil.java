package com.example.aes_pos_wallet.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * 十六进制转换工具
 * @author PP Lin
 *
 */
public class HexUtil {
	public static String bytesToHex(byte[] data) {
		StringBuilder sb = new StringBuilder();
		for (byte b : data) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}


	public static byte[] hexToBytes(String hex){
		int hexlen = hex.length();
		byte[] result;
		if (hexlen % 2 == 1){
			hexlen++;
			result = new byte[(hexlen/2)];
			hex="0"+hex;
		} else {
			result = new byte[(hexlen/2)];
		}
		int j=0;
		for (int i = 0; i < hexlen; i+=2) {
			result[j]=hexToByte(hex.substring(i,i+2));
			j++;
		}
		return result;
	}

	public static byte[] addBytes(byte[] data1, byte[] data2) {
		byte[] data3 = new byte[data1.length + data2.length];
		System.arraycopy(data1, 0, data3, 0, data1.length);
		System.arraycopy(data2, 0, data3, data1.length, data2.length);
		return data3;
	}

	private static byte hexToByte(String hex){
		return (byte) Integer.parseInt(hex,16);
	}



	/**
	 * 十六进制字符串一字节之间的异或运算
	 *
	 * @param strHex_X
	 * @param strHex_Y
	 * @return
	 */
	private static String xorHex(String strHex_X, String strHex_Y) {
		// 将x、y转成二进制形式
		String anotherBinary = Integer.toBinaryString(Integer.valueOf(strHex_X, 16));
		String thisBinary = Integer.toBinaryString(Integer.valueOf(strHex_Y, 16));
		String result = "";
		// 判断是否为8位二进制，否则左补零
		if (anotherBinary.length() != 8) {
			for (int i = anotherBinary.length(); i < 8; i++) {
				anotherBinary = "0" + anotherBinary;
			}
		}
		if (thisBinary.length() != 8) {
			for (int i = thisBinary.length(); i < 8; i++) {
				thisBinary = "0" + thisBinary;
			}
		}
		// 异或运算
		for (int i = 0; i < anotherBinary.length(); i++) {
			// 如果相同位置数相同，则补0，否则补1
			if (thisBinary.charAt(i) == anotherBinary.charAt(i))
				result += "0";
			else {
				result += "1";
			}
		}
		return Integer.toHexString(Integer.parseInt(result, 2));
	}

	/**
	 * 十六进制字符串按位异或运算
	 *
	 * @param hexString
	 * @return
	 */
	public static String xorHexString(String strHex) {
		int i = 2;
		String result;
		String temp;
		result = strHex.substring(0, 2);
		while (i < strHex.length()) {
			temp = strHex.substring(i, i + 2);
			result = xorHex(result, temp);
			i += 2;
		}
		return result.toUpperCase(); // 小写字母转为大写
	}

	/**
	 * 字符串转十六进制字符串
	 *
	 * @param strHex
	 * @return
	 */
	public static String string2HexString(String str) {
		Charset charset = Charset.forName("GBK");
		byte[] a = str.getBytes(charset);
		return BytesUtils.bcdToString(a);
	}

	/**
	 * 十六进制字符串转字符串
	 *
	 * @param strHex
	 * @return
	 */
	public static String hexString2String(String strHex) {
		String gbkString = null;
		try {
			gbkString = new String(BytesUtils.hexToBytes(strHex), "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return gbkString;
	}

	/**
	 * 位数不足左补0
	 * @param hexStr
	 * @param len
	 * @return
	 */
	public static String hexSupply0(String hexStr, int len) {
		while (hexStr.length() < len) {
			hexStr = "0" + hexStr;
		}
		return hexStr;
	}

	/**
	 * 组建PC端响应报文
	 * @param result
	 * @return
	 */
	public static String buildRspData(String result) {
		String rsp = HexUtil.hexSupply0(String.valueOf(result.length()), 5) + result;
		return rsp;
	}

}
