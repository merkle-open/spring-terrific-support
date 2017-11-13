package com.namics.oss.spring.support.terrific.compile;

import com.asual.lesscss.loader.ResourceLoader;
import com.asual.lesscss.loader.StreamResourceLoader;

import javax.servlet.ServletContext;
import java.io.InputStream;

/**
 * A {@link ResourceLoader} that loads resources from a {@link ServletContext}.
 *
 * @author aschaefer, Namics AG
 * @since 2.0 27.05.2013
 */
public class LessCompilerServletResourceLoader extends StreamResourceLoader {

	private static final String SCHEMA = "servlet";

	private final ServletContext servletContext;

	/**
	 * Creates a new {@link LessCompilerServletResourceLoader}.
	 *
	 * @param servletContext a {@link ServletContext} to load resources from.
	 */
	public LessCompilerServletResourceLoader(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getSchema() {
		return SCHEMA;
	}

	/**
	 * Please note that path should have a leading slash.
	 */
	@Override
	protected InputStream openStream(String path) {
		return this.servletContext.getResourceAsStream(path);
	}
}
