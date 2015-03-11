package com.zanelove.ViewHtmlTextDemo.Entity;

import java.security.MessageDigest;
import java.util.ArrayList;

public class SignTool {
	String signkey = "offcn8999|", keycode = "";
	private static char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public String getSign(ArrayList<String> vallist) {

		for (String val : vallist) {
			keycode = keycode + signkey + val;
		}

		if (keycode.equals("")) {
			return null;
		}
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(keycode.getBytes());
			return getFormattedText(messageDigest.digest());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static String getFormattedText(byte[] bytes) {
		int len = bytes.length;
		StringBuilder buf = new StringBuilder(len * 2);
		for (int j = 0; j < len; j++) {
			buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
			buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
		}
		return buf.toString();
	}
}
