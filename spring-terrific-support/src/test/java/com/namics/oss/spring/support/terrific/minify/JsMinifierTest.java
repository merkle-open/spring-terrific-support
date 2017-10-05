/*
 * Copyright 2000-2014 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.terrific.minify;

import junit.framework.TestCase;
import org.springframework.util.FileCopyUtils;

import java.net.URL;

/**
 * JsMinifierTest.
 *
 * @author aschaefer, Namics AG
 * @since 15.04.14 13:28
 */
//@Ignore
public class JsMinifierTest extends TestCase {
	public void testMinify() throws Exception {
		JsMinifier minifier = new JsMinifier().rCrlfFix(true);
		URL resource = JsMinifierTest.class.getResource("jquery-1.9.1.js");
		byte[] bytes = FileCopyUtils.copyToByteArray(resource.openStream());
		String bytesString = new String(bytes, "UTF-8");
		assertTrue("source not contained \"\\\"\\\\r\\\\n\\\")\"", bytesString.contains("\"\\r\\n\""));

		byte[] minified = minifier.minify(bytes);
		String minifiedString = new String(minified, "UTF-8");
		assertTrue("result not contained \"\\\"\\\\r\\\\n\\\")\"", minifiedString.contains("\"\\r\\n\""));
	}

	public void testIsApplicable() throws Exception {
		JsMinifier minifier = new JsMinifier();
		assertEquals(true, minifier.isApplicable("test.js"));
		assertEquals(true, minifier.isApplicable("//some/weird/path/test.js"));
		assertEquals(false, minifier.isApplicable("//some/weird/path/minified/test.min.js"));
		assertEquals(false, minifier.isApplicable("//some/weird/path/minified/test-min.js"));
		assertEquals(false, minifier.isApplicable("//some/weird/path/minified/test_min.js"));
		assertEquals(true, minifier.isApplicable("//some/weird/path/minified/testmin.js"));
		assertEquals(false, minifier.isApplicable("/terrific/t\\artifacts\\dirac\\WEB-INF\\classes\\terrific\\assets\\js\\jquery-1.11.1.min.js"));
	}
}
