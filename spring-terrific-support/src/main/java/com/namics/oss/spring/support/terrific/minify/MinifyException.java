/*
 * Copyright 2000-2013 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.terrific.minify;

/**
 * MinfyException.
 *
 * @author aschaefer, Namics AG
 * @since 2.0 03.04.2013
 */
public class MinifyException extends RuntimeException {
	// CHECKSTYLE:OFF
	private static final long serialVersionUID = -3322983054994976612L;

	public MinifyException() {
	}

	public MinifyException(String message) {
		super(message);
	}

	public MinifyException(Throwable cause) {
		super(cause);
	}

	public MinifyException(String message, Throwable cause) {
		super(message, cause);
	}
	// CHECKSTYLE:ON
}
