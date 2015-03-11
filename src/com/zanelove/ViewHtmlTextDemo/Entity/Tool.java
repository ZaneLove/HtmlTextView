package com.zanelove.ViewHtmlTextDemo.Entity;

public class Tool {
	
	public static String changeUrl2Path(String url) {
		String path = null;
		if (url.contains(" ")) {
			path = url.replaceAll(" ", "%20");
		}
		if (url.contains(":")) {
			path = url.replaceAll(":", "___");
		}

		return path;
	}

	public static String replaceSpace(String str) {
		if (str.contains(" ")) {
			return str.replaceAll(" ", "%20");
		}
		return str;
	}
}
