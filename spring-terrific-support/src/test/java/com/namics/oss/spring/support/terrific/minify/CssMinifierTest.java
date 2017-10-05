/*
 * Copyright 2000-2015 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.terrific.minify;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * CssMinifierTest.
 *
 * @author aschaefer, Namics AG
 * @since 27.04.15 15:02
 */
public class CssMinifierTest {

	protected CssMinifier minifier = new CssMinifier();

	@Test
	public void testIsApplicable() throws Exception {
		assertTrue(minifier.isApplicable("test.css"));
		assertTrue(minifier.isApplicable("test.less"));

		assertFalse(minifier.isApplicable("test.js"));
		assertFalse(minifier.isApplicable("test.txt"));
		assertFalse(minifier.isApplicable(""));
		assertFalse(minifier.isApplicable(null));
	}
}