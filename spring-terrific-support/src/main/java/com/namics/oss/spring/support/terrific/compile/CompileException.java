/*
 * Copyright 2000-2013 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.terrific.compile;

/**
 * CompileException.
 *
 * @author aschaefer, Namics AG
 * @since 2.0 03.04.2013
 */
public class CompileException extends RuntimeException {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 803289941948948072L;

	// CHECKSTYLE:OFF
	public CompileException() {
	}

	public CompileException(String message) {
		super(message);
	}

	public CompileException(Throwable throwable) {
		super(throwable);
	}

	public CompileException(String message, Throwable throwable) {
		super(message, throwable);
	}
	// CHECKSTYLE:ON
}
