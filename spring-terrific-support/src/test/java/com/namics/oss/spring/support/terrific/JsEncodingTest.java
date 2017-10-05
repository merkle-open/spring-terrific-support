/*
 * Copyright 2000-2013 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.terrific;

import com.namics.oss.spring.support.terrific.filter.TerrificFilter;
import com.namics.oss.spring.support.terrific.service.ContentService;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
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
@RunWith(SpringJUnit4ClassRunner.class)
//@Ignore
@ContextConfiguration(locations = { "classpath:terrific-test.xml" })
public class JsEncodingTest {

	@Inject
	ContentService contentService;

	@Inject
	TerrificFilter filter;

	CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();

	{
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);
	}

	@Test
	public void testContentServiceEncoding() throws Exception {
		byte[] data = contentService.getContent("main.js");
		assertNotNull(data);
		assertTrue(data.length > 0);

		String verifyString = "Januar_Februar_März_April_Mai_Juni_Juli_August_September_Oktober_November_Dezember";
		String actualData = new String(data, Charset.forName("UTF-8"));
		assertTrue(actualData.contains(verifyString));

		String wrongEncoding = new String(data, Charset.forName("ASCII"));
		assertFalse(wrongEncoding.contains(verifyString));

		wrongEncoding = new String(data, Charset.forName("ISO-8859-15"));
		assertFalse(wrongEncoding.contains(verifyString));
	}

	@Test
	public void testFilterEncodingInMemory() throws Exception {
		String filename = "main.js";

		MockHttpServletResponse response = new MockHttpServletResponse();
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/terrific/main.js");
		MockFilterChain filterChain = new MockFilterChain();

		characterEncodingFilter.doFilter(request, response, filterChain);
		filter.doFilter(request, response, filterChain);

		byte[] data = response.getContentAsByteArray();

		assertNotNull(data);
		assertTrue(data.length > 0);

		String verifyString = "Januar_Februar_März_April_Mai_Juni_Juli_August_September_Oktober_November_Dezember";
		String actualData = new String(data, Charset.forName("UTF-8"));
		assertTrue(actualData.contains(verifyString));

		String wrongEncoding = new String(data, Charset.forName("ASCII"));
		assertFalse(wrongEncoding.contains(verifyString));

		wrongEncoding = new String(data, Charset.forName("ISO-8859-15"));
		assertFalse(wrongEncoding.contains(verifyString));

		String contentUtf8 = response.getContentAsString();

		assertTrue(contentUtf8.contains(verifyString));
	}

	@Test
	public void testFilterEncodingToFile() throws Exception {
		String filename = "main.js";

		File tempFile = File.createTempFile("encodingtest", ".txt");
		FileOutputStream stream = new FileOutputStream(tempFile);
		HttpServletResponse response = new FileOutputResponseWrapper(new MockHttpServletResponse(), stream);
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/terrific/main.js");
		MockFilterChain filterChain = new MockFilterChain();

		characterEncodingFilter.doFilter(request, response, filterChain);
		filter.doFilter(request, response, filterChain);

		IOUtils.closeQuietly(stream);

		byte[] data = new byte[2097152];
		IOUtils.read(new FileInputStream(tempFile), data);

		assertNotNull(data);
		assertTrue(data.length > 0);

		String verifyString = "März";
		String actualData = new String(data, Charset.forName("UTF-8"));
		assertTrue(actualData.contains(verifyString));

		String wrongEncoding = new String(data, Charset.forName("ASCII"));
		assertFalse(wrongEncoding.contains(verifyString));

		wrongEncoding = new String(data, Charset.forName("ISO-8859-15"));
		assertFalse(wrongEncoding.contains(verifyString));
	}
}
