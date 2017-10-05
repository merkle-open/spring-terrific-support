/*
 * Copyright 2000-2014 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.terrific.util;

import javax.servlet.http.HttpServletResponse;

/**
 * CacheControlUtil.
 *
 * @author aschaefer, Namics AG
 * @since 28.04.14 17:13
 */
public class CacheControlUtil {
	protected int maxAgeDays = 365;

	public void cacheControl(HttpServletResponse response, long modifiedTime) {
		long age = 365 * 24 * 60 * 60 * 1000;
		long expires = modifiedTime + age; // +365 days
		response.addDateHeader("Expires", expires);
		response.addHeader("Cache-Control", "public, max-age=" + Long.toString(age));
	}

	public void setMaxAgeDays(int maxAgeDays) {
		this.maxAgeDays = maxAgeDays;
	}

	public CacheControlUtil maxAgeDays(int maxAgeDays) {
		this.setMaxAgeDays(maxAgeDays);
		return this;
	}
}
