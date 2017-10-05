/*
 * Copyright 2000-2013 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.terrific.filter;

import com.namics.oss.spring.support.terrific.service.ContentService;
import com.namics.oss.spring.support.terrific.util.CacheControlUtil;
import com.namics.oss.spring.support.terrific.util.MimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import static org.springframework.util.Assert.notNull;

/**
 * TerrificFilter.
 *
 * @author aschaefer, Namics AG
 * @since 2.0 22.05.2013
 */
public class TerrificFilter extends OncePerRequestFilter {
	private static final Logger LOG = LoggerFactory.getLogger(TerrificFilter.class);
	protected ContentService contentService;
	protected String mapping = "/terrific/";
	protected String debugParamName = "debug";
	protected boolean debug = false;
	protected boolean checkLastModified = true;
	protected long startDate;
	protected CacheControlUtil cacheControlUtil;
	protected String terrificVersion;


	public TerrificFilter(ContentService contentService) {
		notNull(contentService);
		this.contentService = contentService;
		this.startDate = new Date().getTime();
		this.cacheControlUtil = new CacheControlUtil();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request,
	                                HttpServletResponse response,
	                                FilterChain filterChain) throws ServletException, IOException {
		// use getRequestURI to avoid difference between Servlet API 2.5 and 3.0 getServletPath.
		String file = request.getRequestURI().substring(request.getContextPath().length());
		file = file.replace(this.mapping, ""); // remove controller mapping path

		String mimeType = MimeUtils.detectMimeType(file);

		boolean debug = this.debug || ServletRequestUtils.getStringParameter(request, debugParamName) != null;

		LOG.info("Requested path {}, remove mapping {},  resolve file {}, required mime type {}", request.getServletPath(),
		         this.mapping, file, mimeType);
		try {
			if (!debug && checkLastModified(request, response, file)) {
				return;
			}
			byte[] result = this.contentService.getContent(file, debug);
			response.setContentType(mimeType);
			if (terrificVersion != null && debug) {
				response.setHeader("X-Terrific-Version", terrificVersion);
			}
			FileCopyUtils.copy(result, response.getOutputStream());
		} catch (Exception e) {
			final Object[] args = new Object[] { file, e };
			LOG.warn("Exception while resolving file {} : {}", args);
			LOG.debug("Exception while resolving file {} : {}", args, e);
			filterChain.doFilter(request, response);
		}
	}

	protected boolean checkLastModified(HttpServletRequest request, HttpServletResponse response, String file) {
		long modified = startDate;
		if (this.checkLastModified) {
			Long lastModified = this.contentService.lastModified(file);
			modified = startDate > lastModified ? startDate : lastModified;
		}
		ServletWebRequest webRequest = new ServletWebRequest(request, response);
		if (webRequest.checkNotModified(modified)) {
			return true;
		}
		this.cacheControlUtil.cacheControl(response, modified);
		return false;
	}

	/**
	 * mapping, default configured by property ${terrific.mapping:/terrific/}.
	 *
	 * @param mapping the mapping to set
	 */
	public TerrificFilter setMapping(String mapping) {
		this.mapping = mapping;
		return this;
	}

	public TerrificFilter setDebugParamName(String debugParamName) {
		this.debugParamName = debugParamName;
		return this;
	}

	public TerrificFilter setDebug(boolean debug) {
		this.debug = debug;
		return this;
	}

	public TerrificFilter setCheckLastModified(boolean checkLastModified) {
		this.checkLastModified = checkLastModified;
		return this;
	}

	public TerrificFilter setCacheControlAge(int maxAgeDays) {
		this.cacheControlUtil.setMaxAgeDays(maxAgeDays);
		return this;
	}

	public TerrificFilter setTerrificVersion(String terrificVersion) {
		this.terrificVersion = terrificVersion;
		return this;
	}
}
