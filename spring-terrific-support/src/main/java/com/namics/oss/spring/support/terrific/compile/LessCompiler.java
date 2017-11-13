/*
 * Copyright 2000-2013 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.terrific.compile;

import com.asual.lesscss.LessEngine;
import com.asual.lesscss.LessException;
import com.asual.lesscss.LessOptions;
import com.asual.lesscss.loader.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * LessCompiler.
 *
 * @author aschaefer, Namics AG
 * @since 2.0 03.04.2013
 */
public class LessCompiler implements Compiler {
	private static final Logger LOG = LoggerFactory.getLogger(LessCompiler.class);
	protected static final LessOptions DEFAULT_LESS_OPTIONS = new LessOptions();

	static {
		DEFAULT_LESS_OPTIONS.setCompress(true);
	}

	protected ServletContext servletContext;
	protected LessOptions options;

	protected String encoding = "UTF-8";


	protected LessEngine lessEngine;

	public LessCompiler() {
		this(null, null);
	}

	public LessCompiler(ServletContext servletContext) {
		this(servletContext, null);
	}

	public LessCompiler(ServletContext servletContext, LessOptions options) {
		if (options != null) {
			this.options = options;
		} else {
			this.options = DEFAULT_LESS_OPTIONS;
		}
		this.servletContext = servletContext;

		ResourceLoader resourceLoader = new ChainedResourceLoader(
				new FilesystemResourceLoader(),
				new ClasspathResourceLoader(LessEngine.class.getClassLoader()),
				new LessCompilerServletResourceLoader(this.servletContext),
				new HTTPResourceLoader());
		resourceLoader = new UnixNewlinesResourceLoader(resourceLoader);
		this.lessEngine = new LessEngine(this.options, resourceLoader);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param source
	 */
	@Override
	public byte[] compile(byte[] source) throws CompileException {
		try {
			//FIXME inefficient
			String sourceString = new String(source, this.encoding);
			String normalized = sourceString.replaceAll("\r", "");
			String compiled = this.lessEngine.compile(normalized);
			// fix wrong line break rendering
			compiled = compiled.replaceAll("\\\\n", "\n");

			//fix remove spaces between number and px
			Pattern p = Pattern.compile("(\\d+) (px)");
			Matcher m = p.matcher(compiled);
			if (m.find()) {
				compiled = m.replaceAll("$1$2");
			}

			LOG.debug("Compiled source:\n{}\n--\nresult:\n{}", source, compiled);
			return compiled.getBytes(Charset.forName(this.encoding));
		} catch (LessException e) {
			String msg = "Failed to compile less file " + e.getFilename() + " line " + e.getLine() + " cause: " + e.getMessage();
			LOG.warn(msg);
			throw new CompileException(msg, e);
		} catch (Exception e) {
			String msg = "Failed to compile for unexpected reason " + e.getMessage();
			LOG.warn(msg);
			throw new CompileException(msg, e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isApplicable(String filename) {
		return filename != null && filename.endsWith(".less");
	}

	/**
	 * encoding to use, default by property ${terrific.encoding:UTF-8}.
	 *
	 * @param encoding the encoding to set
	 */
	public LessCompiler setEncoding(String encoding) {
		this.encoding = encoding;
		return this;
	}
}
