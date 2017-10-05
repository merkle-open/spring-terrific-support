/*
 * Copyright 2013-2013 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.terrific;


import com.namics.oss.spring.support.terrific.compile.LessCompiler;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.core.io.ClassRelativeResourceLoader;
import org.springframework.mock.web.MockServletContext;

import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * LessCompilerTest.
 *
 * @author aschaefer, Namics AG
 * @since TODO 07.06.2013
 */
public class LessCompilerTest {
	LessCompiler compiler = new LessCompiler(new MockServletContext("/", new ClassRelativeResourceLoader(LessCompilerTest.class)));

	@Test
	public void testCompile() throws Exception {
		final InputStream stream = this.getClass().getResourceAsStream("/terrific_less/elements.less");
		final InputStreamReader reader = new InputStreamReader(stream);
		byte[] content = IOUtils.toByteArray(reader);
		this.compiler.compile(content);
	}

	@Test
	public void testSpace() throws Exception {
		final InputStream stream = this.getClass().getResourceAsStream("/terrific_less/newsgallery.less");
		final InputStreamReader reader = new InputStreamReader(stream);
		byte[] content = IOUtils.toByteArray(reader);

		String lessString = new String(content);
		byte[] cssByte = this.compiler.compile(content);

		String cssString = new String(cssByte);

		assertTrue("Space between number and px", cssString.contains("4711px"));
		assertFalse("Space between number and px", cssString.contains("4711 px"));


	}
}
