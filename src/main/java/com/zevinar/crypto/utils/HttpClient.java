package com.zevinar.crypto.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public enum HttpClient {
	CLIENT;
	private CloseableHttpClient httpclient = HttpClients.createDefault();

	public HttpResponse doGet(String url) {
		HttpResponse response;
		try (CloseableHttpResponse closeableResponse = httpclient.execute( new HttpGet(url))) {
			int statusCode = closeableResponse.getStatusLine().getStatusCode();
			HttpEntity entity1 = closeableResponse.getEntity();
			String body = EntityUtils.toString(entity1, "UTF-8");
			// do something useful with the response body
			// and ensure it is fully consumed
			EntityUtils.consume(entity1);
			response = new HttpResponse(statusCode, body);
		} catch (Exception e) {
			response = new HttpResponse(HttpStatus.SC_INTERNAL_SERVER_ERROR, StringUtils.EMPTY);
		}
		
		return response;
	}

	public static class HttpResponse {
		private int statusCode;
		private String body;

		private HttpResponse(int statusCode, String body) {
			super();
			this.statusCode = statusCode;
			this.body = body;
		}

		public int getStatusCode() {
			return statusCode;
		}

		public String getBody() {
			return body;
		}
	}
}
