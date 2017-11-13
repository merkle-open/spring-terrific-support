/*
 * Copyright 2000-2017 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.thirdparty.yahoo.yui.compressor;

import org.junit.Test;

import java.io.StringReader;
import java.io.StringWriter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * CssCompressorTest.
 *
 * @author aschaefer, Namics AG
 * @since 02.02.17 16:12
 */
public class CssCompressorTest {

	@Test
	public void compress() throws Exception {

		String input = ".b {\n\t\t color:red; \n\t}";
		String expected = ".b{color:red}";

		StringReader reader = new StringReader(input);
		StringWriter result = new StringWriter();
		new CssCompressor(reader).compress(result, 8000);
		assertThat(result.toString(), equalTo(expected));

	}

}