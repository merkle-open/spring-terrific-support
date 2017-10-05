/*
 * Copyright 2000-2013 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.terrific.minify;

/**
 * Minifier.
 *
 * @author aschaefer, Namics AG
 * @since 2.0 03.04.2013
 */
public interface Minifier {
	/**
	 * Minify provided code.
	 *
	 * @param source the source code to be minified
	 * @return minified content
	 * @throws MinifyException when minify fails for a certain reason
	 */
	byte[] minify(byte[] source) throws MinifyException;

	/**
	 * Detects if this minifier is applicable to a certain file by its file name.
	 *
	 * @param filename file name to check
	 * @return whether it can be applied or not
	 */
	boolean isApplicable(String filename);
}
