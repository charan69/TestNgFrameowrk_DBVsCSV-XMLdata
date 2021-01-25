package com.mdsol.ctms.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Base64;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;

import com.mdsol.ctms.processortests.BaseTestProcessor;
import com.mdsol.mauth.SignerConfiguration;
import com.mdsol.mauth.apache.HttpClientRequestSigner;
import com.mdsol.mauth.apache.SignerHttpRequestInterceptor;

public class APIutils extends BaseTestProcessor {

	static SignerConfiguration configuration;
	static HttpClientRequestSigner httpClientRequestSigner;
	static SignerHttpRequestInterceptor signerHttpRequestInterceptor;
	static CloseableHttpClient httpClient;

	static String responseString = null;
	static String[] responseStringarray = null;
	static int exppectedSuccessStatusCode = 200;
	static int postSuccessStatusCode = 200;
	static int DeleteSuccessStatusCode = 204;
	static int patchSuccessStatusCode = 201;
	static int actualStatusCode ;

	private static String getPrivateKey() throws FileNotFoundException {
		try (BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "//src//config//"+ token));) {
			String key = br.readLine();
			byte[] decoded = Base64.getDecoder().decode(key);
			return new String(decoded);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	public static void configureMauthAuthentication() throws IOException {
		String privateKey = getPrivateKey();
		configuration = new SignerConfiguration(UUID.fromString(appUUID), privateKey);
		httpClientRequestSigner = new HttpClientRequestSigner(configuration);
		signerHttpRequestInterceptor = new SignerHttpRequestInterceptor(httpClientRequestSigner);
		httpClient = HttpClients.custom().addInterceptorFirst(signerHttpRequestInterceptor).build();
	}

	public static String responseofApiGETcall(String geturl) throws IOException {
		configureMauthAuthentication();
		HttpGet request = new HttpGet(geturl);
		request.setHeader(mccheader, mccheadervalue);

		try (CloseableHttpResponse response = httpClient.execute(request)) {
			StatusLine status = response.getStatusLine();
			actualStatusCode = status.getStatusCode();
			HttpEntity entity = response.getEntity();
			responseString = EntityUtils.toString(entity, "UTF-8");
			Assert.assertEquals(actualStatusCode, exppectedSuccessStatusCode);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseString;

	}
	
	public static String responseofApiPostcall(String geturl) throws IOException {
		configureMauthAuthentication();
		HttpPost request = new HttpPost(geturl);
		request.setHeader(mccheader, mccheadervalue);
		StringEntity params = new StringEntity("{}");
		request.addHeader("content-type", "application/json");
		request.setEntity(params);

		try (CloseableHttpResponse response = httpClient.execute(request)) {
			StatusLine status = response.getStatusLine();
			actualStatusCode = status.getStatusCode();

			HttpEntity entity = response.getEntity();
			responseString = EntityUtils.toString(entity, "UTF-8");
			Assert.assertEquals(actualStatusCode, postSuccessStatusCode);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseString;

	}
	
	public static String[] arrayResponseofApiGETcall(String geturl) throws IOException {
		configureMauthAuthentication();
		HttpPost request = new HttpPost(geturl);
		request.setHeader(mccheader, mccheadervalue);

		try (CloseableHttpResponse response = httpClient.execute(request)) {
			StatusLine status = response.getStatusLine();
			actualStatusCode = status.getStatusCode();

			HttpEntity entity = response.getEntity();
			responseString = EntityUtils.toString(entity, "UTF-8");
			Assert.assertEquals(actualStatusCode, exppectedSuccessStatusCode);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String[] {responseString};

	}
	
	public static String[] arrayResponseofApiPostcall(String geturl) throws IOException {
		configureMauthAuthentication();
		HttpGet request = new HttpGet(geturl);
		request.setHeader(mccheader, mccheadervalue);

		try (CloseableHttpResponse response = httpClient.execute(request)) {
			StatusLine status = response.getStatusLine();
			actualStatusCode = status.getStatusCode();

			HttpEntity entity = response.getEntity();
			responseString = EntityUtils.toString(entity, "UTF-8");
			Assert.assertEquals(actualStatusCode, postSuccessStatusCode);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String[] {responseString};

	}
	
	public static int responseofApiDELETEcall(String geturl) throws IOException {
		configureMauthAuthentication();
		HttpDelete request = new HttpDelete(geturl);
		request.setHeader(mccheader, mccheadervalue);

		try (CloseableHttpResponse response = httpClient.execute(request)) {
			StatusLine status = response.getStatusLine();
			actualStatusCode = status.getStatusCode();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return actualStatusCode;

	}
	
	public static int responseofApiPatchcall(String geturl) throws IOException {
		configureMauthAuthentication();
		HttpPatch request = new HttpPatch(geturl);
		request.setHeader(mccheader, mccheadervalue);

		try (CloseableHttpResponse response = httpClient.execute(request)) {
			StatusLine status = response.getStatusLine();
			actualStatusCode = status.getStatusCode();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return actualStatusCode;

	}
	
	
	public static String responseofApiDeleteCall(String geturl) throws IOException {
		configureMauthAuthentication();
		HttpDelete request = new HttpDelete(geturl);
		request.setHeader(mccheader, mccheadervalue);

		try (CloseableHttpResponse response = httpClient.execute(request)) {
			StatusLine status = response.getStatusLine();
			actualStatusCode = status.getStatusCode();

			HttpEntity entity = response.getEntity();
			responseString = EntityUtils.toString(entity, "UTF-8");
			Assert.assertEquals(actualStatusCode, DeleteSuccessStatusCode);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseString;

	}
	
	public String apiGETcallwithoutMauth(String geturl,String mccheader,String mccheadervalue) {
		HttpGet request = new HttpGet(geturl);
		request.setHeader(mccheader, mccheadervalue);

		try (CloseableHttpResponse response = httpClient.execute(request)) {
			StatusLine status = response.getStatusLine();
			actualStatusCode = status.getStatusCode();

			HttpEntity entity = response.getEntity();
			responseString = EntityUtils.toString(entity, "UTF-8");
			System.out.println(responseString);
			Assert.assertEquals(actualStatusCode, exppectedSuccessStatusCode);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseString;

}
	
}
