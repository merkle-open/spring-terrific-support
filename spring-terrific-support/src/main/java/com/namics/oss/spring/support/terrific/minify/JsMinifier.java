/*
 * Copyright 2000-2013 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.terrific.minify;

import com.namics.oss.spring.support.thirdparty.yahoo.yui.compressor.JavaScriptCompressor;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.regex.Pattern;

import static org.springframework.util.Assert.notNull;

/**
 * JsMinifier.
 *
 * @author aschaefer, Namics AG
 * @since 03.04.2013
 */
@Component("terrificJsMinifier")
public class JsMinifier implements Minifier {
	private static final Logger LOG = LoggerFactory.getLogger(JsMinifier.class);

	/**
	 * lineWidth.
	 */
	protected int lineWidth = 8000;

	protected String encoding = "UTF-8";

	protected boolean rCrlfFix = false;
	protected String rCrlf = "\\r\\n";
	protected String rCrlfReplacement = "##r##n";

	protected String exclude = ".*[-\\._]min\\.js$";
	protected Pattern excludePattern = Pattern.compile(exclude, Pattern.CASE_INSENSITIVE);

	/**
	 * {@inheritDoc}
	 *
	 * @param source
	 */
	@Override
	public byte[] minify(byte[] source) throws MinifyException {
		if (source != null && source.length > 0) {
			try {
				if (rCrlfFix) {
					source = replace(source, rCrlf, rCrlfReplacement);
				}
				final InputStreamReader jsReader = new InputStreamReader(new ByteArrayInputStream(source), encoding);
				final JavaScriptCompressor jsCompressor = new JavaScriptCompressor(jsReader, new JavaScriptErrorReporter());
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				final OutputStreamWriter jsWriter = new OutputStreamWriter(byteArrayOutputStream, encoding);
				jsCompressor.compress(jsWriter, this.lineWidth, true, false, true, false);
				jsWriter.flush();
				byte[] result = byteArrayOutputStream.toByteArray();
				if (result != null && result.length > 0) {
					if (rCrlfFix) {
						result = replace(result, rCrlfReplacement, rCrlf);
					}
					return result;
				} else {
					LOG.debug("Minify failed: empty result, return original");
					return source;
				}

			} catch (Exception e) {
				String msg = "Minify failed: " + e.getMessage();
				LOG.warn(msg);
				throw new MinifyException(msg, e);
			}
		} else {
			LOG.debug("Minify not required, source is empty: '{}'", source);
			return source;
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isApplicable(String filename) {
		if (filename != null && filename.endsWith(".js")) {
			return !this.excludePattern.matcher(filename).find();
		}
		return false;
	}

	/**
	 * JavaScriptErrorReporter implementation for specific error handling.
	 *
	 * @author aschaefer, Namics AG
	 * @since 2.0 03.04.2013
	 */
	protected static class JavaScriptErrorReporter implements ErrorReporter {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void warning(final String message,
		                    final String sourceName,
		                    final int line,
		                    final String lineSource,
		                    final int lineOffset) {
			if (line < 0) {
				LOG.debug(message);
			} else {
				LOG.debug("{} +{} : {}", lineOffset, line, message);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void error(final String message,
		                  final String sourceName,
		                  final int line,
		                  final String lineSource,
		                  final int lineOffset) {
			if (line < 0) {
				LOG.debug(message);
			} else {
				LOG.debug("{} +{} : {}", lineOffset, line, message);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public EvaluatorException runtimeError(final String message,
		                                       final String sourceName,
		                                       final int line,
		                                       final String lineSource,
		                                       final int lineOffset) {
			this.error(message, sourceName, line, lineSource, lineOffset);
			return new EvaluatorException(message);
		}
	}

	protected byte[] replace(byte[] source, String target, String replacement) {
		try {
			String heystack = new String(source, encoding);
			String cleaned = heystack.replace(target, replacement);
			return cleaned.getBytes(encoding);
		} catch (UnsupportedEncodingException e) {
			return source;
		}
	}

	/**
	 * Builder for lineWidth. @param lineWidth the lineWidth to set
	 */
	public JsMinifier lineWidth(int lineWidth) {
		this.setLineWidth(lineWidth);
		return this;
	}

	public JsMinifier encoding(String encoding) {
		this.setEncoding(encoding);
		return this;
	}

	/**
	 * Enable fix for problem with rCRLF replacement in jquery: \r\n is interpreted by compressor instead of keep it.
	 * This is an expensive operation, so enable only if you do have the problem, e.g. in unminified jquery.
	 * Enabled by default.
	 *
	 * @param rCrlfFix enable fix.
	 */
	public JsMinifier rCrlfFix(boolean rCrlfFix) {
		this.setrCrlfFix(rCrlfFix);
		return this;
	}

	public JsMinifier exclude(String pattern) {
		this.setExclude(pattern);
		return this;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void setrCrlfFix(boolean rCrlfFix) {
		this.rCrlfFix = rCrlfFix;
	}

	public void setExclude(String exclude) {
		notNull(exclude);
		this.exclude = exclude;
		this.excludePattern = Pattern.compile(Pattern.quote(exclude), Pattern.CASE_INSENSITIVE);
	}

	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
	}
}
