/*
 * Copyright 2000-2013 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.terrific.minify;

import com.namics.oss.spring.support.thirdparty.yahoo.yui.compressor.CssCompressor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.regex.Pattern;

import static org.springframework.util.Assert.notNull;


/**
 * Minifies css content.
 *
 * @author aschaefer, Namics AG
 * @since 2.0 03.04.2013
 */
public class CssMinifier implements Minifier {

	private static final Logger LOG = LoggerFactory.getLogger(CssMinifier.class);

	protected final String encoding;


	protected int lineWidth = 8000;
	protected String exclude = ".*[-\\._]min\\.css$";
	protected Pattern excludePattern = Pattern.compile(exclude, Pattern.CASE_INSENSITIVE);

	public CssMinifier() {
		encoding = "UTF-8";
	}

	public CssMinifier(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param source
	 */
	@Override
	public byte[] minify(byte[] source) throws MinifyException {
		try {
			final InputStreamReader cssReader = new InputStreamReader(new ByteArrayInputStream(source), encoding);
			final CssCompressor cssCompressor = new CssCompressor(cssReader);

			StringWriter cssWriter = new StringWriter();
			cssCompressor.compress(cssWriter, this.lineWidth);
			return cssWriter.getBuffer().toString().getBytes(encoding);
		} catch (Exception e) {
			String msg = "Exception during css minify: " + e.getMessage();
			LOG.warn(msg);
			throw new MinifyException(msg, e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isApplicable(String filename) {
		if (filename != null && (filename.endsWith(".css") || filename.endsWith(".less"))) {
			return !this.excludePattern.matcher(filename).find();
		}
		return false;
	}

	/**
	 * Setter for lineWidth. @param lineWidth the lineWidth to set
	 */
	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
	}


	public CssMinifier lineWidth(int lineWidth) {
		this.setLineWidth(lineWidth);
		return this;
	}

	public CssMinifier exclude(String pattern) {
		this.setExclude(pattern);
		return this;
	}

	public void setExclude(String exclude) {
		notNull(exclude);
		this.exclude = exclude;
		this.excludePattern = Pattern.compile(Pattern.quote(exclude), Pattern.CASE_INSENSITIVE);
	}
}
