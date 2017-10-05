/*
 * Copyright 2000-2013 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.terrific;

import com.namics.oss.spring.support.terrific.service.ContentService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

/**
 * ContentServiceTest.
 *
 * @author aschaefer, Namics AG
 * @since 2.0 08.04.2013
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:terrific-test.xml" })
public class ContentServiceTest {

	@Inject
	ContentService contentService;

	@Test
	public void testContentMainJs() throws Exception {
		byte[] data = this.contentService.getContent("main.js");
		String compare = new String(data, "UTF-8");
		Assert.assertTrue("main.js must contain " + "js_d1.js", compare.contains("js_d1.js"));
		Assert.assertTrue("main.js must contain " + "js_d2.js", compare.contains("js_d2.js"));
		Assert.assertTrue("main.js must contain " + "js_s1.js", compare.contains("js_s1.js"));
		Assert.assertTrue("main.js must contain " + "js_s2.js", compare.contains("js_s2.js"));
		Assert.assertTrue("main.js must contain " + "example.js", compare.contains("example.js"));
		Assert.assertTrue("main.js must contain " + "example-animated.js", compare.contains("example-animated.js"));
	}

	@Test
	public void testContentMainCss() throws Exception {
		byte[] data = this.contentService.getContent("main.css");
		String compare = new String(data, "UTF-8");
		Assert.assertTrue("main.css must contain css_d1.css", compare.contains("css_d1.css"));
		Assert.assertTrue("main.css must contain css_s2.less", compare.contains("css_s2.less"));
		Assert.assertFalse("main.css must not contain css_d1.ie6.css", compare.contains("css_d1.ie6.css"));
	}

	@Test
	public void testAppend() throws Exception {
		byte[] data = this.contentService.getContent("main.css");
		String compare = new String(data, "UTF-8");

		System.err.println(compare);


		Assert.assertTrue("main.css must contain  content:\"test\"", compare.contains("content:\"test\""));
		Assert.assertFalse("main.css must not contain @includedVar", compare.contains("@includedVar"));
	}

	@Test
	public void testContentIE6() throws Exception {
		byte[] data = this.contentService.getContent("main.ie6.css");
		String compare = new String(data, "UTF-8");
		Assert.assertTrue("main.css must contain css_d1.ie6.css", compare.contains("css_d1.ie6.css"));
	}
}
