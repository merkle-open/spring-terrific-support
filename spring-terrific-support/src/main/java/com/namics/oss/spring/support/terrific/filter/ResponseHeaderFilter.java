/*
 * Copyright 2013-2013 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.terrific.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * ResponseHeaderFilter.
 *
 * @author aschaefer, Namics AG
 * @since 2.1.1 08.08.2013
 */
public class ResponseHeaderFilter implements Filter {
	private static final Logger LOG = LoggerFactory.getLogger(ResponseHeaderFilter.class);

	protected Map<String, String> headers;

	protected List<String> applyFilterPatterns;

	protected PathMatcher pathMatcher = new AntPathMatcher();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		LOG.info("Setup {} directly as ServletFilter", this.getClass());
		if (this.headers == null) {
			this.headers = new HashMap<>();
		}
		Enumeration<?> params = filterConfig.getInitParameterNames();
		while (params.hasMoreElements()) {
			String name = (String) params.nextElement();
			this.headers.put(name, filterConfig.getInitParameter(name));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doFilter(ServletRequest req,
	                     ServletResponse res,
	                     FilterChain chain) throws IOException, ServletException {

		HttpServletResponse response = (HttpServletResponse) res;
		HttpServletRequest request = (HttpServletRequest) req;

		if (this.isApplyFilter(request)) {
			if (this.headers != null && this.headers.size() > 0) {
				for (Entry<String, String> header : this.headers.entrySet()) {
					LOG.info("{} set header  {}={}", request.getRequestURI(), header.getKey(), header.getValue());
					response.setHeader(header.getKey(), header.getValue());
				}
			}
		}
		chain.doFilter(request, response);
	}

	protected boolean isApplyFilter(HttpServletRequest request) {
		String uri = request.getServletPath();
		if (!CollectionUtils.isEmpty(this.applyFilterPatterns)) {
			for (String pattern : this.applyFilterPatterns) {
				if (this.pathMatcher.match(pattern, uri)) {
					LOG.info("Apply filter {}", uri);
					return true;
				}
			}
			LOG.info("Ignore filter {}", uri);
			return false; // there are configured patterns but none that matches
		}
		LOG.info("Always filter {}", uri);
		return true;// no configuration means apply to all
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void destroy() {
	}

	// CHECKSTYLE:OFF

	public Map<String, String> getHeaders() {
		return this.headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public List<String> getApplyFilterPatterns() {
		return this.applyFilterPatterns;
	}

	public void setApplyFilterPatterns(List<String> applyFilterPatterns) {
		this.applyFilterPatterns = applyFilterPatterns;
	}

	public PathMatcher getPathMatcher() {
		return this.pathMatcher;
	}

	public void setPathMatcher(PathMatcher pathMatcher) {
		this.pathMatcher = pathMatcher;
	}

	// CHECKSTYLE:ON
}
