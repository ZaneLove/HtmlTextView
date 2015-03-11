package com.zanelove.ViewHtmlTextDemo.Entity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.InputStream;

public class HttpTool {
	
	public static String getData(String url) {
		String result = null;
		try {
			
			HttpGet httpget = new HttpGet(url);
			
			HttpResponse response = new DefaultHttpClient().execute(httpget);
			
			result = EntityUtils.toString(response.getEntity());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (result == null) {
			return "";
		}
		return result.trim();
	}
	
	public InputStream readInputStreamFromInternet(String url, String filename) {
		InputStream inputStream = null;
		HttpGet httpget = new HttpGet(url);
		try {
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
			HttpConnectionParams.setSoTimeout(httpParams, 15000);
			HttpClient httpClient = new DefaultHttpClient(httpParams);
			HttpResponse httpResponse = httpClient.execute(httpget);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				FileTool file = new FileTool();
				inputStream = httpResponse.getEntity().getContent();
				file.savaInputStreamImage(inputStream, filename);
				return inputStream;
			}
			httpResponse = null;
			httpParams = null;
			httpClient = null;
		} catch (Exception e) {
			httpget.abort();
			e.printStackTrace();
		}
		if (inputStream != null) {
			// netState = LOADOK;
		}
		return null;
	}
}
