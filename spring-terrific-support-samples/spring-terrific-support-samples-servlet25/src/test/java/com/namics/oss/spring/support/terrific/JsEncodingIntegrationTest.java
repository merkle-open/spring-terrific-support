/*
 * Copyright 2000-2013 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.terrific;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;

import static org.junit.Assert.*;

/**
 * JsEncodingTest.
 *
 * @author aschaefer
 * @since 12.12.13 10:27
 */
@Ignore
public class JsEncodingIntegrationTest {

	String port = "8080";

	{
		port = System.getProperty("integration.test.application.port", port);
	}

	@Test
	public void testJavascript() throws Exception {
		testContentServiceEncoding("http://localhost:" + port + "/terrific/main.js",
		                           "encöding pröbläm ä ö ü é è ê ?!", "text/javascript");
	}


	@Test
	public void testCss() throws Exception {
		testContentServiceEncoding("http://localhost:" + port + "/terrific/main.css", "Hällö", "text/css");
	}


	public void testContentServiceEncoding(String uri, String verifyString, String contentType) throws Exception {
		File tempFile = File.createTempFile("encodingtest", ".txt");
		FileOutputStream stream = new FileOutputStream(tempFile);

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
		schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));

		PoolingClientConnectionManager connectionManager = new PoolingClientConnectionManager(schemeRegistry);
		connectionManager.setMaxTotal(1);
		connectionManager.setDefaultMaxPerRoute(1);

		HttpClient httpClient = new DefaultHttpClient(connectionManager);

		HttpGet request = new HttpGet(uri);
		request.setHeader("Accept", contentType + ";charset=UTF-8");

		HttpResponse response = httpClient.execute(request);
		response.getEntity().writeTo(stream);

		IOUtils.closeQuietly(stream);

		byte[] data = new byte[2097152];
		IOUtils.read(new FileInputStream(tempFile), data);
		assertNotNull(data);
		assertTrue(data.length > 0);

		String actualData = new String(data, Charset.forName("UTF-8"));
		assertTrue(actualData.contains(verifyString));

		String wrongEncoding = new String(data, Charset.forName("ASCII"));
		assertFalse(wrongEncoding.contains(verifyString));

		wrongEncoding = new String(data, Charset.forName("ISO-8859-15"));
		assertFalse(wrongEncoding.contains(verifyString));
	}

}
